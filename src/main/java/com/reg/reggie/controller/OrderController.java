package com.reg.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reg.reggie.common.BaseContext;
import com.reg.reggie.common.R;
import com.reg.reggie.entity.Orders;
import com.reg.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * @author e1hax
 * @date 2022-09-11
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {

        orderService.submit(orders);

        return  R.success("成功支付！");
    }


    /**
     * 用户订单查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public R<Page<Orders>> page(int page,int pageSize){
        log.info("page,pageSize:{},{}",page,pageSize);
        //用户id
        Long userId = BaseContext.getCurrentId();
        //分页构造器
        Page<Orders> pageInfo = new Page<>(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getUserId,userId);
        wrapper.orderByDesc(Orders::getOrderTime);

        Page<Orders> ordersPage = orderService.page(pageInfo, wrapper);

        return R.success(ordersPage);
    }
}
