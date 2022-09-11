package com.reg.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reg.reggie.entity.OrderDetail;
import com.reg.reggie.entity.Orders;

/**
 * Created by e1hax on 2022-09-11.
 */
public interface OrderService extends IService<Orders> {
    /**
     * 提交订单
     * @param orders
     */
   public void submit(Orders orders);
}
