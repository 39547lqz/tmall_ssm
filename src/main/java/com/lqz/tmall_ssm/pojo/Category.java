package com.lqz.tmall_ssm.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * products比较好理解，代表一个分类下有多个产品。
 * productsByRow这个属性的类型是List<List<Product>> productsByRow。
 * 即一个分类又对应多个 List<Product>，提供这个属性，是为了在首页竖状导航的分类名称右边显示推荐产品列表。
 * 一个分类会对应多行产品，而一行产品里又有多个产品记录。
 * 为了实现界面上的这个功能，为Category类设计了
 * List<List<Product>> productsByRow
 * 这样一个集合属性
 */
@Setter
@Getter
public class Category {
    private Integer id;

    private String name;

    /*如下是非数据库字段*/
    private List<Product> products;

    private List<List<Product>> productsByRow;

}