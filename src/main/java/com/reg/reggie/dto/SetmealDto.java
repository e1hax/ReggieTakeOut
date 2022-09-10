package com.reg.reggie.dto;


import com.reg.reggie.entity.Setmeal;
import com.reg.reggie.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
