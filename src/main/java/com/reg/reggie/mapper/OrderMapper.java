package com.reg.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reg.reggie.entity.OrderDetail;
import com.reg.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by e1hax on 2022-09-11.
 */
@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
