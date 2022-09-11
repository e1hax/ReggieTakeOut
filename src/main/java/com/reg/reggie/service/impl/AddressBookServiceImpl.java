package com.reg.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reg.reggie.entity.AddressBook;
import com.reg.reggie.mapper.AddressBookMapper;
import com.reg.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * Created by e1hax on 2022-09-11.
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
