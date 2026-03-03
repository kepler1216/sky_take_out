package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
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

        Setmeal setMeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setMeal);
        setmealMapper.insert(setMeal);
//        插入菜品数据
        Long setMealId = setMeal.getId();
//        向套餐表插入多个菜品
        List<SetmealDish> setmealDishList = setmealDTO.getSetmealDishes();
        log.info("获取套餐菜品",setmealDishList);
        if (setmealDishList != null && setmealDishList.size() > 0) {
            for (SetmealDish setmealDish : setmealDishList) {
                setmealDish.setSetmealId(setMealId);
            }
            setmealDishMapper.insertBatch(setmealDishList);
        }
    }


    //       根据id查询套餐
    @Override
    public SetmealVO getByIdWithDish(Long id) {
        Setmeal setmeal = setmealMapper.getById(id);
        List<SetmealDish> dishes = setmealDishMapper.getDishBySetmealId(id);
        SetmealVO setmealVO=new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(dishes);
        return setmealVO;
    }

    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    //    批量删除套餐
    @Override
    public void delete(List<Long> ids) {
        log.info("批量删除套餐", ids);
        for (Long id : ids) {
            Setmeal setmeal = setmealMapper.getById(id);
            if (setmeal.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        setmealDishMapper.delete(ids);
        setmealMapper.delete(ids);
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        Setmeal setmeal=Setmeal.builder().status(status).id(id).build();
        setmealMapper.update(setmeal);
    }

    //    修改套餐
    @Override
    public void update(SetmealDTO setmealDTO) {
        //        根据套餐id查询菜品
        dishMapper.getDishByCategoryId(setmealDTO.getCategoryId());

        Setmeal setMeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setMeal);
        setmealMapper.update(setMeal);
//        插入菜品数据
        Long setMealId = setMeal.getId();
//        删除套餐的菜品数据
        setmealDishMapper.deleteBySetmealId(setmealDTO.getId());
//        向套餐表插入多个菜品
        List<SetmealDish> setmealDishList = setmealDTO.getSetmealDishes();
        if (setmealDishList != null && setmealDishList.size() > 0) {
            for (SetmealDish setmealDish : setmealDishList) {
                setmealDish.setDishId(setMealId);
            }
            setmealDishMapper.insertBatch(setmealDishList);
        }
    }
}
