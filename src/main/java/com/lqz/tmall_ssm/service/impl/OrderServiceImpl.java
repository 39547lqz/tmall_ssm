package com.lqz.tmall_ssm.service.impl;

import com.lqz.tmall_ssm.mapper.OrderMapper;
import com.lqz.tmall_ssm.pojo.Order;
import com.lqz.tmall_ssm.pojo.OrderExample;
import com.lqz.tmall_ssm.pojo.OrderItem;
import com.lqz.tmall_ssm.pojo.User;
import com.lqz.tmall_ssm.service.OrderItemService;
import com.lqz.tmall_ssm.service.OrderService;
import com.lqz.tmall_ssm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderItemService orderItemService;

    @Override
    public int add(Order order) {
        return orderMapper.insert(order);
    }

    @Override
    public int delete(int id) {
        return orderMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(Order order) {
        return orderMapper.updateByPrimaryKeySelective(order);
    }

    @Override
    public Order get(int id) {
        return orderMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Order> list() {
        OrderExample example = new OrderExample();
        example.setOrderByClause("id desc");
        List<Order> orders = orderMapper.selectByExample(example);
        setUser(orders);
        return orders;
    }

    private void setUser(List<Order> orders) {
        for (Order order : orders) {
            setUser(order);
        }
    }

    private void setUser(Order order) {
        Integer uid = order.getUid();
        User user = userService.get(uid);
        order.setUser(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackForClassName = "Exception")
    public float add(Order order, List<OrderItem> orderItems) {
        float total = 0;
        add(order);
        if (false){
            throw new RuntimeException();
        }
        for (OrderItem orderItem : orderItems) {
            orderItem.setOid(order.getId());
            orderItemService.update(orderItem);
            total += orderItem.getProduct().getPromotePrice() * orderItem.getNumber();
        }
        return total;
    }

    @Override
    public List<Order> list(int uid, String excludedStatus) {
        OrderExample example = new OrderExample();
        example.createCriteria().andUidEqualTo(uid).andStatusNotEqualTo(excludedStatus);
        example.setOrderByClause("id desc");
        return orderMapper.selectByExample(example);
    }
}
