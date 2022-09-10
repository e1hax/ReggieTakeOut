package com.reg.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reg.reggie.entity.User;
import com.reg.reggie.mapper.UserMapper;
import com.reg.reggie.service.UserService;
import org.springframework.stereotype.Service;

/**
 * Created by e1hax on 2022-09-10.
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
