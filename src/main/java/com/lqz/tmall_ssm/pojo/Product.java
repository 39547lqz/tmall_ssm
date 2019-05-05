package com.lqz.tmall_ssm.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class Product {
    private Integer id;

    private String name;

    private String subTitle;

    private Float originalPrice;

    private Float promotePrice;

    private Integer stock;

    private Integer cid;

    private Date createDate;

    /*非数据库字段*/
    private Category  category;

    private ProductImage firstProductImage;

    /*单个产品图片集合*/
    private List<ProductImage> productSingleImages;

    /*** 详情产品图片集合*/
    private List<ProductImage> productDetailImages;

    /*销量*/
    private int saleCount;

    /*累计评价*/
    private int reviewCount;


}