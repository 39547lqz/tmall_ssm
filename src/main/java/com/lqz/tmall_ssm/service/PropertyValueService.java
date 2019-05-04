package com.lqz.tmall_ssm.service;

import com.lqz.tmall_ssm.pojo.Product;
import com.lqz.tmall_ssm.pojo.PropertyValue;

import java.util.List;

public interface PropertyValueService {

    int init(Product product);

    int update(PropertyValue propertyValue);

    PropertyValue get(int ptid, int pid);

    List<PropertyValue> list(int pid);
}
