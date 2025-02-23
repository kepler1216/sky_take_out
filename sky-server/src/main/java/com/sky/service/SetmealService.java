package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;

import java.util.List;

public interface SetmealService {

//    新增菜品
    void saveWithDish(SetmealDTO setmealDTO);

//    根据id查询套餐
    Setmeal getById(Long id);

//    套餐分页查询
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

//    修改套餐
    void update(SetmealDTO setmealDTO);

//    批量删除套餐
    void delete(List<Long> ids);
}
