package com.reg.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reg.reggie.dto.DishDto;
import com.reg.reggie.entity.Dish;
import com.reg.reggie.entity.DishFlavor;
import com.reg.reggie.mapper.DishMapper;
import com.reg.reggie.service.DishFlavorSerivce;
import com.reg.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by e1hax on 2022-09-09.
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorSerivce dishFlavorSerivce;

    @Override
    /**
     * 新增菜品同时，将口味保存到dish_flavor表
     */
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品到dish
        this.save(dishDto);

        //获取菜品id
        Long disId = dishDto.getId();

        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map((item) ->{
            item.setDishId(disId);
            return item;
        }).collect(Collectors.toList());

        //保存菜品口味到dish_flavor表
        dishFlavorSerivce.saveBatch(flavors);
    }


    /**
     * 根据菜品id查询口味
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id){
        //根据id从 dish表中查询信息
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        //查询当前菜品对应的口味信息，从dish_flavor表查询
        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
        lqw.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavors = dishFlavorSerivce.list(lqw);
        dishDto.setFlavors(flavors);

        return dishDto;
    }


    /**
     * 更新dish表，并更新关联的dishflavor表
     * @param dishDto
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto){
        //更新dish表
        this.updateById(dishDto);

        //清理当前菜品对应口味数据---dish_flavor表的delete操作
        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
        lqw.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorSerivce.remove(lqw);

        //给当前菜品添加提交过来的口味数据----dish_flavor表的insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorSerivce.saveBatch(flavors);
    }


    /**
     * 根据id删除菜品，及关联口味
     * @param id
     */
    @Override
    @Transactional
    public void deleteWithFlavor(List<Long> id){
        //检查售卖状态
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.in(Dish::getId,id);
        lqw.eq(Dish::getStatus,1);
        int count = this.count(lqw);
        if (count>0) {
            //抛出异常，在售菜品不能删除
            throw  new RuntimeException("在售菜品，不能删除");
        }
        //删除dish表中相关菜品数据
        this.removeByIds(id);

        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(DishFlavor::getDishId,id);

        //删除dish_flavor表中相关口味数据
        dishFlavorSerivce.remove(lambdaQueryWrapper);
    }

}
