package com.lqz.tmall_ssm.mapper;

import com.lqz.tmall_ssm.pojo.PropertyValue;
import com.lqz.tmall_ssm.pojo.PropertyValueExample;

import java.util.List;

public interface PropertyValueMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PropertyValue record);

    int insertSelective(PropertyValue record);

    List<PropertyValue> selectByExample(PropertyValueExample example);

    PropertyValue selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PropertyValue record);

    int updateByPrimaryKey(PropertyValue record);
}