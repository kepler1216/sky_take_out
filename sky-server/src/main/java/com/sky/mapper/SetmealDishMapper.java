package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
//    根据菜品id查询套餐id
    List<Long> getSetmealIdsByDishId(List<Long> dishIds);

//    插入菜品数据
    void insertBatch(List<SetmealDish> setmealDishList);

//    删除套餐包含的菜品
    void delete(List<Long> ids);

//    根据套餐id删除菜品
    @Delete("delete from setmeal_dish where setmeal_id=#{id}")
    void deleteBySetmealId(Long id);

//    根据套餐id查询菜品
    @Select("select * from setmeal_dish where setmeal_id=#{id}")
    List<SetmealDish> getDishBySetmealId(Long id);
}
