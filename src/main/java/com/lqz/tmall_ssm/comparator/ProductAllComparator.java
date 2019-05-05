package com.lqz.tmall_ssm.comparator;

import com.lqz.tmall_ssm.pojo.Product;

import java.util.Comparator;

/**
 * 销量评价比较器
 */
public class ProductAllComparator implements Comparator<Product> {
    @Override
    public int compare(Product o1, Product o2) {
        return o2.getReviewCount()*o2.getSaleCount() - o1.getReviewCount()*o1.getSaleCount();
    }
}
