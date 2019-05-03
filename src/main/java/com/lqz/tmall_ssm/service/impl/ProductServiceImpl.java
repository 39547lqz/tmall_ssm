package com.lqz.tmall_ssm.service.impl;

import com.lqz.tmall_ssm.mapper.ProductMapper;
import com.lqz.tmall_ssm.pojo.Category;
import com.lqz.tmall_ssm.pojo.Product;
import com.lqz.tmall_ssm.pojo.ProductExample;
import com.lqz.tmall_ssm.service.CategoryService;
import com.lqz.tmall_ssm.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl  implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryService categoryService;

    @Override
    public int add(Product product) {
        return productMapper.insert(product);
    }

    @Override
    public int delete(int id) {
        return productMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(Product product) {
        return productMapper.updateByPrimaryKeySelective(product);
    }

    @Override
    public Product get(int id) {
        Product product = productMapper.selectByPrimaryKey(id);
        setCategory(product);
        return product;
    }

    public void setCategory(Product product) {
        Integer cid = product.getCid();
        Category category = categoryService.get(cid);
        product.setCategory(category);
    }

    public void setCategory(List<Product> ps){
        for (Product product :ps
             ) {
            setCategory(product);
        }
    }

    @Override
    public List<Product> list(int cid) {
        ProductExample productExample = new ProductExample();
        productExample.createCriteria().andCidEqualTo(cid);
        productExample.setOrderByClause("id desc");
        List<Product> products = productMapper.selectByExample(productExample);
        setCategory(products);
        return products;
    }
}
