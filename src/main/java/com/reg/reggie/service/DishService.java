package com.reg.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reg.reggie.dto.DishDto;
import com.reg.reggie.entity.Dish;

/**
 * Created by e1hax on 2022-09-09.
 */
public interface DishService extends IService<Dish> {
      public void saveWithFlavor(DishDto dishDto);

      public DishDto getByIdWithFlavor(Long id);

      public void updateWithFlavor(DishDto dishDto);
}
