package com.reg.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reg.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by e1hax on 2022-09-10.
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
