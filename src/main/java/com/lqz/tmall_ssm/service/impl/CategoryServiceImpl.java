package com.lqz.tmall_ssm.service.impl;

import com.lqz.tmall_ssm.mapper.CategoryMapper;
import com.lqz.tmall_ssm.pojo.Category;
import com.lqz.tmall_ssm.service.CategoryService;
import com.lqz.tmall_ssm.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;


    @Override
    public List<Category> list(Page page) {
        return categoryMapper.list(page);
    }

    @Override
    public int total() {
        return categoryMapper.total();
    }

    @Override
    public int add(Category category) {
        return categoryMapper.add(category);
    }

    @Override
    public int delete(int id) {
        return categoryMapper.delete(id);
    }

    @Override
    public Category get(int id) {
        return categoryMapper.get(id);
    }

    @Override
    public int update(Category category) {
        return categoryMapper.update(category);
    }
}
