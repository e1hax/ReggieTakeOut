package com.reg.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reg.reggie.common.R;
import com.reg.reggie.dto.DishDto;
import com.reg.reggie.entity.Category;
import com.reg.reggie.entity.Dish;
import com.reg.reggie.entity.DishFlavor;
import com.reg.reggie.service.CategoryService;
import com.reg.reggie.service.DishFlavorSerivce;
import com.reg.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by e1hax on 2022-09-09.
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorSerivce dishFlavorSerivce;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 新增菜品
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);

        //新增产品后，清理缓存信息
        //全部清除
        //Set key = redisTemplate.keys("dish_*");
        //redisTemplate.delete(key);

        //更新部分清除
        String key = "redis_"+dishDto.getCategoryId()+"_1";
        redisTemplate.delete(key);

        return R.success("添加菜品成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {

        //构造分页构造器
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Dish> dishLqw = new LambdaQueryWrapper<>();
        //添加查询条件
        dishLqw.like(name != null, Dish::getName, name);
        //添加排序条件
        dishLqw.orderByDesc(Dish::getUpdateTime);
        //执行分页查询
        dishService.page(pageInfo, dishLqw);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();//分类id

            Category category = categoryService.getById(categoryId);

            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }


    /**
     * 根据id查询菜品信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {
        log.info(id.toString());
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * 更新菜品信息
     *
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);
        //更新产品后，清理缓存信息
        //全部清除
        //Set key = redisTemplate.keys("dish_*");
        //redisTemplate.delete(key);

        //更新部分清除
        String key = "redis_"+dishDto.getCategoryId()+"_1";
        redisTemplate.delete(key);

        return R.success("更新菜品成功");
    }

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping
    public R<String> deleteById(@RequestParam List<Long> id) {
        log.info(id.toString());
        dishService.deleteWithFlavor(id);
        return R.success("删除菜品成功");
    }

    /**
     * 修改售卖状态
     *
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable("status") Integer status, @RequestParam List<Long> ids) {
        //update dish set status=0 where id in ids
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(ids != null, Dish::getId, ids);
        List<Dish> list = dishService.list(lambdaQueryWrapper);
        list.stream().map((item) -> {
            item.setStatus(status);
            return item;
        }).collect(Collectors.toList());
        dishService.updateBatchById(list);
        return R.success("修改状态成功");
    }


    /**
     * 根据菜品类别查询
     *
     * @param dish
     * @return
     */
    //@GetMapping("/list")
    //public R<List<Dish>> list(Dish dish) {
    //    //构建条件构造器
    //    LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
    //    //添加条件
    //    lqw.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
    //    //菜品为起售状态
    //    lqw.eq(Dish::getStatus, 1);
    //
    //    List<Dish> list = dishService.list(lqw);
    //
    //    return R.success(list);
    //}

    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        List<DishDto> dishDtoList = null;
        String key = "dish_"+dish.getCategoryId()+"_"+dish.getStatus();
        //从redis中查询数据
        dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);

        //如果redis中有数据，则直接返回
        if (dishDtoList != null) {
            return R.success(dishDtoList);
        }
        //redis中没有，则执行查询
        //构建条件构造器
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        //添加条件
        lqw.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        //菜品为起售状态
        lqw.eq(Dish::getStatus, 1);

        List<Dish> list = dishService.list(lqw);

         dishDtoList = list.stream().map((item)->{
            DishDto dishDto = new DishDto();

            //对象拷贝
            BeanUtils.copyProperties(item,dishDto);

            //根据id查询分类对象
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);

            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            //当前菜品id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            List<DishFlavor> dishFlavorList = dishFlavorSerivce.list(lambdaQueryWrapper);

            dishDto.setFlavors(dishFlavorList);

            return dishDto;
        }).collect(Collectors.toList());

         //并将查询出的数据缓存到redis中,有效期为60分钟
        redisTemplate.opsForValue().set(key,dishDtoList,60, TimeUnit.MINUTES);

        return R.success(dishDtoList);
    }

}
