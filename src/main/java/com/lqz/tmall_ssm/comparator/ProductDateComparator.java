package com.lqz.tmall_ssm.comparator;

import com.lqz.tmall_ssm.pojo.Product;

import java.util.Comparator;

/**
 * 上架日期比较器
 */
public class ProductDateComparator implements Comparator<Product> {
    @Override
    public int compare(Product o1, Product o2) {
        return o2.getCreateDate().compareTo(o1.getCreateDate());
    }
}
