package com.lqz.tmall_ssm.mapper;

import com.lqz.tmall_ssm.pojo.Order;
import com.lqz.tmall_ssm.pojo.OrderExample;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    List<Order> selectByExample(OrderExample example);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);
}