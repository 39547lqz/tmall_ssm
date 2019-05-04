package com.lqz.tmall_ssm.controller;

import com.lqz.tmall_ssm.pojo.Product;
import com.lqz.tmall_ssm.pojo.PropertyValue;
import com.lqz.tmall_ssm.service.ProductService;
import com.lqz.tmall_ssm.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class PropertyValueController {

    @Autowired
    private PropertyValueService propertyValueService;
    @Autowired
    private ProductService productService;

    @RequestMapping("admin_propertyValue_edit")
    public String edit(Model model, int pid){
        Product product = productService.get(pid);
        propertyValueService.init(product);
        List<PropertyValue> propertyValueList = propertyValueService.list(product.getId());

        model.addAttribute("p", product);
        model.addAttribute("pvs", propertyValueList);
        return "admin/editPropertyValue";
    }

    @RequestMapping("admin_propertyValue_update")
    @ResponseBody
    public String update(PropertyValue propertyValue){
        propertyValueService.update(propertyValue);
        return "success";
    }


}
