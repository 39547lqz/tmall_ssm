package com.lqz.tmall_ssm.comparator;

import com.lqz.tmall_ssm.pojo.Product;

import java.util.Comparator;

/**
 * 评价数比较器
 */
public class ProductReviewComparator implements Comparator<Product> {
    @Override
    public int compare(Product o1, Product o2) {
        return o2.getReviewCount() - o1.getReviewCount();
    }
}
