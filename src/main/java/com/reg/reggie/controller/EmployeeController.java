package com.reg.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reg.reggie.common.R;
import com.reg.reggie.entity.Employee;
import com.reg.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * Employee控制器类
 *
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
     *
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        //1.获取密码，对密码进行MD5加密
        String passWord = employee.getPassword();
        passWord = DigestUtils.md5DigestAsHex(passWord.getBytes());

        //2. 获取用户名，从数据库中查询  mp方法
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(lqw);

        //3. 判断此用户名是否查询成功  不是等于null才失败吗
        if (emp == null) {
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
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /**
     * 员工退出
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        //清理Session中的当前登录的员工id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }


    /**
     * 添加员工
     * @param request
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info(String.valueOf(employee));
        //设置初始密码
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        //设置createTime,updateTime,createUser,updateUser
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        //获取操作用户的id
        Long empId = (Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);

        employeeService.save(employee);

        return R.success("添加员工成功");
    }

    /**
     * 员工管理分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page={},pageSize={},name={}", page, pageSize, name);

        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper();
        //添加查询条件
        lqw.like(StringUtils.isNotBlank(name), Employee::getName, name);
        //添加排序条件
        lqw.orderByDesc(Employee::getUpdateTime);

        //执行查询
        employeeService.page(pageInfo, lqw);

        return R.success(pageInfo);
    }

    /**
     * 修改员工账号状态
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request ,@RequestBody Employee employee){
        log.info(employee.toString());

        Long empId =(Long) request.getSession().getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(empId);

        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }


    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable String id){
        Employee emp = employeeService.getById(id);
        if (emp != null) {
            return R.success(emp);

        }
        return R.error("员工信息修改失败");
    }
}
