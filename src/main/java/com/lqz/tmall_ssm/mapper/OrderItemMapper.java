package com.lqz.tmall_ssm.mapper;

import com.lqz.tmall_ssm.pojo.OrderItem;
import com.lqz.tmall_ssm.pojo.OrderItemExample;

import java.util.List;

public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    List<OrderItem> selectByExample(OrderItemExample example);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);
}