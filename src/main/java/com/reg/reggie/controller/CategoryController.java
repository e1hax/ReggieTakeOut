package com.reg.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reg.reggie.common.R;
import com.reg.reggie.entity.Category;
import com.reg.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理
 * @author e1hax
 * @date 2022-09-09
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info(category.toString());
        categoryService.save(category);
        return R.success("新增分类成功");
    }


    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        //构造分页构造器
        Page<Category> pageInfo = new Page<>(page, pageSize);

        //添加条件构造器
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        //添加排序条件
        lqw.orderByAsc(Category::getSort);

        categoryService.page(pageInfo,lqw);

        return R.success(pageInfo);
    }

    /**
     * 根据id删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long id){
        log.info(id.toString());
        //categoryService.removeById(id);
        categoryService.remove(id);
        return R.success("删除成功！");
    }

    /**
     * 根据id修改分类信息
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info(category.toString());
        categoryService.updateById(category);
        return R.success("修改分类信息成功");
    }


    /**
     * 菜品分类列表
     * @param category
     * @return
     */
    @GetMapping("/list")
     public R<List<Category>> list(Category category){
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        //添加查询条件
        lqw.eq(category.getType()!=null,Category::getType,category.getType());
        //添加排序条件
        lqw.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(lqw);
        return R.success(list);
      }
}
