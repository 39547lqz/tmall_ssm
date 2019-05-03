package com.lqz.tmall_ssm.service.impl;

import com.lqz.tmall_ssm.mapper.ProductImageMapper;
import com.lqz.tmall_ssm.pojo.ProductImage;
import com.lqz.tmall_ssm.pojo.ProductImageExample;
import com.lqz.tmall_ssm.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductImageServiceImpl implements ProductImageService {

    @Autowired
    private ProductImageMapper productImageMapper;

    @Override
    public int add(ProductImage productImage) {
        return productImageMapper.insert(productImage);
    }

    @Override
    public int delete(int id) {
        return productImageMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(ProductImage productImage) {
        return productImageMapper.updateByPrimaryKeySelective(productImage);
    }

    @Override
    public ProductImage get(int id) {
        return productImageMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<ProductImage> list(int pid, String type) {
        ProductImageExample productImageExample = new ProductImageExample();
        productImageExample.createCriteria().andPidEqualTo(pid).andTypeEqualTo(type);
        productImageExample.setOrderByClause("id desc");
        return productImageMapper.selectByExample(productImageExample);
    }
}
