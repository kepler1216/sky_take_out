package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
//    根据菜品id查询套餐id
    List<Long> getSetmealIdsByDishId(List<Long> dishIds);

//    插入菜品数据
    void insertBatch(List<SetmealDish> setmealDishList);

//    删除套餐包含的菜品
    void delete(List<Long> ids);
}
