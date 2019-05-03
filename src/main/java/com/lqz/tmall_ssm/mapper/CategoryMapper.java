package com.lqz.tmall_ssm.mapper;

import com.lqz.tmall_ssm.pojo.Category;
import com.lqz.tmall_ssm.pojo.CategoryExample;

import java.util.List;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    List<Category> selectByExample(CategoryExample example);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);
}