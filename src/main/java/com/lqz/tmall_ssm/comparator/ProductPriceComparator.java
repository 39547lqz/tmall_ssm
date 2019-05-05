package com.lqz.tmall_ssm.comparator;

import com.lqz.tmall_ssm.pojo.Product;

import java.util.Comparator;

/**
 * 价格比较器
 */
public class ProductPriceComparator implements Comparator<Product> {
    @Override
    public int compare(Product o1, Product o2) {
        return (int) (o2.getPromotePrice() - o1.getPromotePrice());
    }
}
