package com.lqz.tmall_ssm.mapper;


import com.lqz.tmall_ssm.pojo.Category;
import com.lqz.tmall_ssm.util.Page;

import java.util.List;


public interface CategoryMapper {

    List<Category> list(Page page);

    int total();

    int add(Category category);

    int delete(int id);

    Category get(int id);

    int update(Category category);
}
