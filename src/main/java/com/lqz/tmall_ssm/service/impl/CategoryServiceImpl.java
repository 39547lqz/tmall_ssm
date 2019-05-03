package com.lqz.tmall_ssm.service.impl;

import com.lqz.tmall_ssm.mapper.CategoryMapper;
import com.lqz.tmall_ssm.pojo.Category;
import com.lqz.tmall_ssm.pojo.CategoryExample;
import com.lqz.tmall_ssm.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;


    @Override
    public List<Category> list() {
        CategoryExample example = new CategoryExample();
        example.setOrderByClause("id desc");
        return categoryMapper.selectByExample(example);
    }

    @Override
    public int add(Category category) {
        return categoryMapper.insert(category);
    }

    @Override
    public int delete(int id) {
        return categoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Category get(int id) {
        return categoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(Category category) {
        return categoryMapper.updateByPrimaryKeySelective(category);
    }
}
