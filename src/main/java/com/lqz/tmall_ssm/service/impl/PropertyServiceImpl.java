package com.lqz.tmall_ssm.service.impl;

import com.lqz.tmall_ssm.mapper.PropertyMapper;
import com.lqz.tmall_ssm.pojo.Property;
import com.lqz.tmall_ssm.pojo.PropertyExample;
import com.lqz.tmall_ssm.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyServiceImpl implements PropertyService {

    @Autowired
    private PropertyMapper propertyMapper;

    @Override
    public int add(Property property) {
        return propertyMapper.insert(property);
    }

    @Override
    public int delete(int id) {
        return propertyMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(Property property) {
        return propertyMapper.updateByPrimaryKeySelective(property);
    }

    @Override
    public Property get(int id) {
        return propertyMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Property> list(int cid) {
        PropertyExample propertyExample = new PropertyExample();
        propertyExample.createCriteria().andCidEqualTo(cid);
        propertyExample.setOrderByClause("id desc");
        return propertyMapper.selectByExample(propertyExample);
    }
}
