package com.lqz.tmall_ssm.service;

import com.lqz.tmall_ssm.pojo.Category;

import java.util.List;

public interface CategoryService {

    List<Category> list();

    int add(Category category);

    int delete(int id);

    Category get(int id);

    int update(Category category);
}
