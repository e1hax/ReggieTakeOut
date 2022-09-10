package com.reg.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reg.reggie.entity.DishFlavor;
import com.reg.reggie.mapper.DishFlavorMapper;
import com.reg.reggie.service.DishFlavorSerivce;
import org.springframework.stereotype.Service;

/**
 * Created by e1hax on 2022-09-09.
 */
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorSerivce {
}
