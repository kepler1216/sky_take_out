package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
//    新增分类
    @Override
    public void save(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
//        分类状态 0标识禁用 1表示启用
        category.setStatus(StatusConstant.ENABLE);
//        创建时间
        category.setCreateTime(LocalDateTime.now());
//        更新时间
        category.setUpdateTime(LocalDateTime.now());
//        创建人
        category.setCreateUser(BaseContext.getCurrentId());
//        修改人
        category.setUpdateUser(BaseContext.getCurrentId());

        categoryMapper.insert(category);
    }
}
