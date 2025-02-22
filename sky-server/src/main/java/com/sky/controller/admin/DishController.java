package com.sky.controller.admin;

//菜品管理

import com.sky.constant.MessageConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {
//    新增菜品
    @Autowired
    private DishService dishService;
    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("新增菜品:{}",dishDTO);
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

//    菜品分页查询
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询:{}",dishPageQueryDTO);
        PageResult pageResult=dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

//    批量删除菜品
    @DeleteMapping
    @ApiOperation("删除菜品")
    public Result delete(@RequestParam List<Long> ids){
        log.info("删除菜品:{}",ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }

//    根据id查询对应的菜品和口味数据
    @GetMapping("/{id}")
    @ApiOperation("根据id查询对应的菜品和口味数据")
    public Result<DishVO> getByIdWithFlavor(@PathVariable Long id){
        log.info("根据id查询对应菜品和口味数据",id);
        DishVO dishVO=dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

//    修改菜品
    @PutMapping
    @ApiOperation("修改菜品信息")
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品",dishDTO);
        dishService.updateWithFlavor(dishDTO);
        return Result.success(dishDTO);
    }

//    菜品起售和停售
    @PostMapping("/status/{status}")
    @ApiOperation("菜品起售或停售")
    public Result startOrStop(@PathVariable Integer status,Long id){
        log.info("菜品停售和起售",status,id);
        dishService.startOrStop(status,id);
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
//    根据分类id查询菜品
    public Result<List<Dish>> getDishByCategoryId(Long categoryId){
        log.info("根据分类id查询菜品",categoryId);
        List<Dish> dish=dishService.getDishByCategoryId(categoryId);
        return Result.success(dish);
    }
}
