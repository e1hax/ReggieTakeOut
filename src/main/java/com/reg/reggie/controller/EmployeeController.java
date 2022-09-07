package com.reg.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.reg.reggie.common.R;
import com.reg.reggie.entity.Employee;
import com.reg.reggie.entity.Login;
import com.reg.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 *  Employee控制器类
 * @author e1hax
 * @date 2022-09-07
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录方法
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //1.获取密码，对密码进行MD5加密
        String passWord = employee.getPassword();
        passWord= DigestUtils.md5DigestAsHex(passWord.getBytes());

        //2. 获取用户名，从数据库中查询  mp方法
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(lqw);

        //3. 判断此用户名是否查询成功  不是等于null才失败吗
        if (emp==null) {
            return R.error("登录失败");
        }

        //4. 对密码进行比
        if (!(emp.getPassword().equals(passWord))) {
            return R.error("登录失败");
        }

        //5.对用户的状态进行判断
        if (emp.getStatus() == 0) {
            return R.error("账号已被禁用");
        }

        //6. 将id封装到session
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清理Session中的当前登录的员工id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }
}
