package com.reg.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reg.reggie.dto.SetmealDto;
import com.reg.reggie.entity.Setmeal;
import com.reg.reggie.entity.SetmealDish;
import com.reg.reggie.mapper.SetmealMapper;
import com.reg.reggie.service.SetmealDishService;
import com.reg.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by e1hax on 2022-09-09.
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 保存套餐，并保存套餐菜品关联关系
     *
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //更新setmeal表
        this.save(setmealDto);

        //获取套餐的菜品
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        //对套餐菜品关联关系做处理
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);

    }


    /**
     * 更具id删除套餐，套餐关联的菜品信息
     *
     * @param ids
     */
    @Override
    @Transactional
    public void deleteWithDish(List<Long> ids) {

        //查询售卖状态
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        //添加条件
        lqw.in(Setmeal::getId, ids);
        lqw.eq(Setmeal::getStatus, 1);
        int count = this.count(lqw);
        if (count > 0) {
            //正在售卖中不能删除
            throw new RuntimeException("正在售卖，不能删除");
        }
        //删除 setmeal表中的数据
        this.removeByIds(ids);

        // delete from setmeal_dish where setmealId in (ids);
        LambdaQueryWrapper<SetmealDish> dishLqw = new LambdaQueryWrapper<>();
        dishLqw.in(SetmealDish::getSetmealId, ids);
        //删除 setmeal_dish表中的数据
        setmealDishService.remove(dishLqw);

    }
}
