package com.reg.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reg.reggie.dto.SetmealDto;
import com.reg.reggie.entity.Setmeal;

import java.util.List;

/**
 * Created by e1hax on 2022-09-09.
 */
public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐，并关联菜品
     *
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 根据id删除套餐，并删除关联的菜品
     *
     * @param ids
     */
    public void deleteWithDish(List<Long> ids);
}
