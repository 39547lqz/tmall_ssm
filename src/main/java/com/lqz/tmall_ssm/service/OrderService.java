package com.lqz.tmall_ssm.service;

import com.lqz.tmall_ssm.pojo.Order;
import com.lqz.tmall_ssm.pojo.OrderItem;

import java.util.List;

public interface OrderService {

    String waitPay = "waitPay";
    String waitDelivery = "waitDelivery";
    String waitConfirm = "waitConfirm";
    String waitReview = "waitReview";
    String finish = "finish";
    String delete = "delete";

    int add(Order order);

    int delete(int id);

    int update(Order order);

    Order get(int id);

    List<Order> list();

    float add(Order order, List<OrderItem> orderItems);

    List<Order> list(int uid, String excludedStatus);
}
