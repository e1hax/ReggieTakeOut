package com.reg.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reg.reggie.entity.ShoppingCart;
import com.reg.reggie.mapper.ShoppingCartMapper;
import com.reg.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * Created by e1hax on 2022-09-11.
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
