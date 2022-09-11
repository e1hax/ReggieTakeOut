package com.reg.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reg.reggie.entity.OrderDetail;
import com.reg.reggie.mapper.OrderDetailMapper;
import com.reg.reggie.mapper.OrderMapper;
import com.reg.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * Created by e1hax on 2022-09-11.
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
