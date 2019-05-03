package com.lqz.tmall_ssm.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lqz.tmall_ssm.pojo.Category;
import com.lqz.tmall_ssm.pojo.Property;
import com.lqz.tmall_ssm.service.CategoryService;
import com.lqz.tmall_ssm.service.PropertyService;
import com.lqz.tmall_ssm.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class PropertyController {

    @Autowired
    private PropertyService propertyService;
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("admin_property_add")
    public String add(Model model, Property property){
        propertyService.add(property);
        return "redirect:/admin_property_list?cid=" + property.getCid();
    }

    @RequestMapping("admin_property_delete")
    public String delete(int id){
        Property property = propertyService.get(id);
        propertyService.delete(id);
        return "redirect:/admin_property_list?cid=" + property.getCid();
    }

    @RequestMapping("admin_property_edit")
    public String edit(Model model, int id){
        Property property = propertyService.get(id);
        Category category = categoryService.get(property.getCid());
        property.setCategory(category);
        model.addAttribute("p", property);
        return "admin/editProperty";
    }

    @RequestMapping("admin_property_update")
    public String update(Property property){
        propertyService.update(property);
        return "redirect:/admin_property_list?cid=" + property.getCid();
    }

    @RequestMapping("admin_property_list")
    public String list(int cid, Model model, Page page){
        Category category = categoryService.get(cid);

        PageHelper.offsetPage(page.getStart(), page.getCount());
        List<Property> propertyList = propertyService.list(cid);

        int total = (int) new PageInfo<>(propertyList).getTotal();
        page.setTotal(total);
        page.setParam("&cid=" + category.getId());

        model.addAttribute("ps", propertyList);
        model.addAttribute("page", page);
        model.addAttribute("c", category);

        return "admin/listProperty";
    }

}
