package com.reg.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.reg.reggie.common.R;
import com.reg.reggie.entity.User;
import com.reg.reggie.service.UserService;
import com.reg.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by e1hax on 2022-09-10.
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 移动端发送验证码
     *
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        //1.获取输入的手机号
        String phone = user.getPhone();
        if (StringUtils.isNotBlank(phone)) {
            //2.生成随机验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("验证码：{}", code);
            //3.发送验证码
            //SendMessage.sendMessage("瑞吉外卖","",code,phone);

            //4.将验证码保存到session，用于登陆校验
            //session.setAttribute("phone", code);
            //4.将验证码缓存到redis中，有效期为5分钟
            redisTemplate.opsForValue().set(phone,code,5L, TimeUnit.MINUTES);


            return R.success("验证码发送成功");
        }
        return R.success("验证码发送失败");
    }


    /**
     * 移动端用户登录
     *
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> sendMsg(@RequestBody Map map, HttpSession session) {
        //1.获取手机号
        String phone = map.get("phone").toString();
        //2.获取验证码
        String code = map.get("code").toString();
        //3.获取session中的验证码
        //Object codeInSession = session.getAttribute("phone");
        //3. 从redis中获取验证码
        Object codeInRedis = redisTemplate.opsForValue().get(phone);
        //4.进行比对
        if (codeInRedis != null && codeInRedis.equals(code)) {
            //5.查询数据库中有无此用户
            LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
            lqw.eq(User::getPhone, phone);
            User user = userService.getOne(lqw);
            //6.没有则注册
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());
            //5.用户登录成功，从redis中删除验证码
            redisTemplate.delete(phone);
            return R.success(user);
        }
        return R.error("登录失败");
    }

}
