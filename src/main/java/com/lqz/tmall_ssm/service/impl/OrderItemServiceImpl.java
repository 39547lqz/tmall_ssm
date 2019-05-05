package com.lqz.tmall_ssm.service.impl;

import com.lqz.tmall_ssm.mapper.OrderItemMapper;
import com.lqz.tmall_ssm.pojo.Order;
import com.lqz.tmall_ssm.pojo.OrderItem;
import com.lqz.tmall_ssm.pojo.OrderItemExample;
import com.lqz.tmall_ssm.pojo.Product;
import com.lqz.tmall_ssm.service.OrderItemService;
import com.lqz.tmall_ssm.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private ProductService productService;
    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    public int add(OrderItem orderItem) {
        return orderItemMapper.insert(orderItem);
    }

    @Override
    public int delete(int id) {
        return orderItemMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(OrderItem orderItem) {
        return orderItemMapper.updateByPrimaryKeySelective(orderItem);
    }

    @Override
    public OrderItem get(int id) {
        OrderItem orderItem = orderItemMapper.selectByPrimaryKey(id);
        setProduct(orderItem);
        return orderItem;
    }

    private void setProduct(OrderItem orderItem) {
        Product product = productService.get(orderItem.getPid());
        orderItem.setProduct(product);
    }

    @Override
    public List<OrderItem> list() {
        OrderItemExample orderItemExample = new OrderItemExample();
        orderItemExample.setOrderByClause("id desc");
        return orderItemMapper.selectByExample(orderItemExample);
    }

    /**
     * 在fill(List<Order> orders) 中，就是遍历每个订单，然后挨个调用fill(Order order)。
     * @param orders
     */
    @Override
    public void fill(List<Order> orders) {
        for (Order order : orders) {
            fill(order);
        }
    }

    /**
     * 1. 根据订单id查询出其对应的所有订单项
     * 2. 通过setProduct为所有的订单项设置Product属性
     * 3. 遍历所有的订单项，然后计算出该订单的总金额和总数量
     * 4. 最后再把订单项设置在订单的orderItems属性上。
     * @param order
     */
    @Override
    public void fill(Order order) {
        OrderItemExample orderItemExample = new OrderItemExample();
        orderItemExample.createCriteria().andOidEqualTo(order.getId());
        orderItemExample.setOrderByClause("id desc");
        List<OrderItem> orderItems = orderItemMapper.selectByExample(orderItemExample);
        setProduct(orderItems);

        float total = 0;
        int totalNumber = 0;
        for (OrderItem orderItem : orderItems) {
            total += orderItem.getNumber() * orderItem.getProduct().getPromotePrice();
            totalNumber += orderItem.getNumber();
        }
        order.setTotal(total);
        order.setTotalNumber(totalNumber);
        order.setOrderItems(orderItems);
    }

    public void setProduct(List<OrderItem> orderItems){
        for (OrderItem orderItem : orderItems) {
            setProduct(orderItem);
        }
    }

    @Override
    public int getSaleCount(int pid) {
        OrderItemExample example = new OrderItemExample();
        example.createCriteria().andPidEqualTo(pid).andOidIsNotNull();
        List<OrderItem> orderItems = orderItemMapper.selectByExample(example);
        int result = 0;
        for (OrderItem orderItem : orderItems) {
            result += orderItem.getNumber();
        }
        return result;
    }

    @Override
    public List<OrderItem> listByUser(int uid) {
        OrderItemExample example = new OrderItemExample();
        example.createCriteria().andUidEqualTo(uid).andOidIsNull();
        List<OrderItem> orderItems = orderItemMapper.selectByExample(example);
        setProduct(orderItems);
        return orderItems;
    }
}
