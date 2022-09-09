package com.reg.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reg.reggie.common.CustomException;
import com.reg.reggie.entity.Category;
import com.reg.reggie.entity.Dish;
import com.reg.reggie.entity.Setmeal;
import com.reg.reggie.mapper.CategoryMapper;
import com.reg.reggie.service.CategoryService;
import com.reg.reggie.service.DishService;
import com.reg.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by e1hax on 2022-09-09.
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper,Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类，删除前进行判断
     * @param id
     */
    @Override
    public void remove(Long id) {
        //构造条件构造器
        LambdaQueryWrapper<Dish> disLqw = new LambdaQueryWrapper<>();
        //添加查询条件，根据id分类查询
        disLqw.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(disLqw);
        //查询当前菜品分类是否关联了菜品，如果关联了，则抛出异常
        if (count1 >0) {
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }

        //构造条件构造器
        LambdaQueryWrapper<Setmeal> setMealLqw = new LambdaQueryWrapper<>();
        setMealLqw.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setMealLqw);
        //查询当前菜品分类是否关联了套餐，如果关联了，则抛出异常
        if (count2 >0) {
            //抛出异常
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }

        //正常删除分类
        super.removeById(id);
    }
}
