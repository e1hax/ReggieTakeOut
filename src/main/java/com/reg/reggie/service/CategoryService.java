package com.reg.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reg.reggie.entity.Category;

/**
 * Created by e1hax on 2022-09-09.
 */
public interface CategoryService extends IService<Category> {

     void remove(Long id);
}
