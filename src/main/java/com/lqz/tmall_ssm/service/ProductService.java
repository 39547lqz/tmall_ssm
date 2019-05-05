package com.lqz.tmall_ssm.service;

import com.lqz.tmall_ssm.pojo.Category;
import com.lqz.tmall_ssm.pojo.Product;

import java.util.List;

public interface ProductService {

    int add(Product product);

    int delete(int id);

    int update(Product product);

    Product get(int id);

    List<Product> list(int cid);

    void setFirstProductImage(Product p);

    /**
     * 为多个分类填充产品集合
     * @param categories
     */
    void fill(List<Category> categories);

    /**
     * 为分类填充产品集合
     * @param category
     */
    void fill(Category category);

    /**
     * 为多个分类填充推荐产品集合，即把分类下的产品集合，按照8个为一行，拆成多行，以利于后续页面上进行显示
     * @param categories
     */
    void fillByRow(List<Category> categories);

    void setSaleAndReviewNumber(Product p);

    void setSaleAndReviewNumber(List<Product> ps);

    List<Product> search(String keyword);
}
