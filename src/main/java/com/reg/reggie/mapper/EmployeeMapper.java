package com.reg.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reg.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 *  Employee映射器类
 * @author e1hax
 * @date 2022-09-07
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
