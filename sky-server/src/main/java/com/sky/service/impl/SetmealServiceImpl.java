package com.sky.service.impl;
import com.sky.dto.SetmealDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;
//    新增套餐
    @Override
    public void saveWithDish(SetmealDTO setmealDTO) {
//        根据套餐id查询菜品
        dishMapper.getDishByCategoryId(setmealDTO.getCategoryId());

        Setmeal setMeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setMeal);
        setmealMapper.insert(setMeal);
//        插入菜品数据
        Long setMealId=setMeal.getId();
//        向套餐表插入多个菜品
        List<SetmealDish> setmealDishList =setmealDTO.getSetmealDishes();
        if(setmealDishList!=null && setmealDishList.size()>0){
            for (SetmealDish setmealDish : setmealDishList) {
                setmealDish.setDishId(setMealId);
            }
            setmealDishMapper.insertBatch(setmealDishList);
        }
    }
}
