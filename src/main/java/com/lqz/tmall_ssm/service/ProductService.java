package com.lqz.tmall_ssm.service;

import com.lqz.tmall_ssm.pojo.Product;

import java.util.List;

public interface ProductService {

    int add(Product product);

    int delete(int id);

    int update(Product product);

    Product get(int id);

    List<Product> list(int cid);
}
