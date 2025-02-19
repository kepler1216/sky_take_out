package com.sky.service.impl;

import com.github.pagehelper.Constant;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {
    //    新增菜品和口味数据
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
//        向菜品表插入一条数据
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.insert(dish);
//        获取insert语句生成的主键值
        Long dishId = dish.getId();
//        向口味表插入多条数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dishId);
            }
            dishFlavorMapper.insertBatch(flavors);
        }
    }


    //    菜品分页查询
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    //    批量删除菜品
    @Transactional
    public void deleteBatch(List<Long> ids) {
        //判断当前菜品能否被删除（是否起售）
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if (dish.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        //判断当前菜品能否被删除（是否关联套餐）
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishId(ids);
        if (setmealIds != null && setmealIds.size() > 0) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

//    //删除菜品数据
//        for (Long id : ids) {
//            dishMapper.deleteById(id);
//            //删除口味数据
//            dishFlavorMapper.deleteByDishId(id);
//        }
//        批量删除
        dishMapper.deleteByIds(ids);
        dishFlavorMapper.deleteByDishIds(ids);

    }

    //    根据id查询对应的菜品和口味数据
    @Override
    public DishVO getByIdWithFlavor(Long id) {
//        根据id查询菜品数据
        Dish dish = dishMapper.getById(id);
//        根据id查询口味数据
        List<DishFlavor> dishFlavors = dishFlavorMapper.getByDishId(id);
//        将查询到的数据封装到VO
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }

    //    修改菜品信息
    public void updateWithFlavor(DishDTO dishDTO) {
//        修改菜品表基本信息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);
//        删除口味数据
        dishFlavorMapper.deleteByDishId(dishDTO.getId());
//        重新插入口味数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dishDTO.getId());
            }
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        Dish dish=Dish.builder()
                .status(status)
                .id(id)
                .build();
    }

}
