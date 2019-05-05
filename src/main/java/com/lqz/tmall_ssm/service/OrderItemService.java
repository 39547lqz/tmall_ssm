package com.lqz.tmall_ssm.service;

import com.lqz.tmall_ssm.pojo.Order;
import com.lqz.tmall_ssm.pojo.OrderItem;

import java.util.List;

public interface OrderItemService {

    int add(OrderItem orderItem);

    int delete(int id);

    int update(OrderItem orderItem);

    OrderItem get(int id);

    List<OrderItem> list();

    void fill(List<Order> orders);

    void fill(Order order);

    int getSaleCount(int pid);

    List<OrderItem> listByUser(int uid);
}
