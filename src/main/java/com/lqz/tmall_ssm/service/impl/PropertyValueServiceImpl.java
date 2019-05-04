package com.lqz.tmall_ssm.service.impl;

import com.lqz.tmall_ssm.mapper.PropertyValueMapper;
import com.lqz.tmall_ssm.pojo.Product;
import com.lqz.tmall_ssm.pojo.Property;
import com.lqz.tmall_ssm.pojo.PropertyValue;
import com.lqz.tmall_ssm.pojo.PropertyValueExample;
import com.lqz.tmall_ssm.service.PropertyService;
import com.lqz.tmall_ssm.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PropertyValueServiceImpl implements PropertyValueService {

    @Autowired
    private PropertyValueMapper propertyValueMapper;
    @Autowired
    private PropertyService propertyService;

    @Override
    public int init(Product product) {
        List<Property> propertyList = propertyService.list(product.getCid());
        for (Property property : propertyList) {
            PropertyValue propertyValue = get(property.getId(), product.getId());
            if (propertyValue == null) {
                propertyValue = new PropertyValue();
                propertyValue.setPid(product.getId());
                propertyValue.setPtid(propertyValue.getId());
                return propertyValueMapper.insert(propertyValue);
            }
        }
        return 0;
    }

    @Override
    public int update(PropertyValue propertyValue) {
        return propertyValueMapper.updateByPrimaryKeySelective(propertyValue);
    }

    @Override
    public PropertyValue get(int ptid, int pid) {
        PropertyValueExample example = new PropertyValueExample();
        example.createCriteria().andPtidEqualTo(ptid).andPidEqualTo(pid);
        List<PropertyValue> propertyValueList = propertyValueMapper.selectByExample(example);
        if (propertyValueList == null) {
            return null;
        }
        return propertyValueList.get(0);
    }

    @Override
    public List<PropertyValue> list(int pid) {
        PropertyValueExample example = new PropertyValueExample();
        example.createCriteria().andPidEqualTo(pid);
        List<PropertyValue> propertyValues = propertyValueMapper.selectByExample(example);
        for (PropertyValue propertyValue : propertyValues) {
            Property property = propertyService.get(propertyValue.getPtid());
            propertyValue.setProperty(property);
        }
        return propertyValues;
    }
}
