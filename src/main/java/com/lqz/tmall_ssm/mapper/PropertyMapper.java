package com.lqz.tmall_ssm.mapper;

import com.lqz.tmall_ssm.pojo.Property;
import com.lqz.tmall_ssm.pojo.PropertyExample;

import java.util.List;

public interface PropertyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Property record);

    int insertSelective(Property record);

    List<Property> selectByExample(PropertyExample example);

    Property selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Property record);

    int updateByPrimaryKey(Property record);
}