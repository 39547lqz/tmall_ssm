package com.lqz.tmall_ssm.service;

import com.lqz.tmall_ssm.pojo.ProductImage;

import java.util.List;

public interface ProductImageService {

    String type_single = "type_single";
    String type_detail = "type_detail";

    int add(ProductImage productImage);

    int delete(int id);

    int update(ProductImage productImage);

    ProductImage get(int id);

    List<ProductImage> list(int pid, String type);
}
