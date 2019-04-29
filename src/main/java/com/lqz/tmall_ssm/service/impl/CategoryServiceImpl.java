package com.lqz.tmall_ssm.service.impl;

import com.lqz.tmall_ssm.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl {

    @Autowired
    private CategoryMapper categoryMapper;
}
