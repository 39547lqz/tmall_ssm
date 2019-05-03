package com.lqz.tmall_ssm.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lqz.tmall_ssm.pojo.Category;
import com.lqz.tmall_ssm.pojo.Product;
import com.lqz.tmall_ssm.service.CategoryService;
import com.lqz.tmall_ssm.service.ProductService;
import com.lqz.tmall_ssm.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("admin_product_add")
    public String add(Model model, Product product){
        product.setCreateDate(new Date());
        productService.add(product);
        return "redirect:/admin_product_list?cid=" + product.getCid();
    }

    @RequestMapping("admin_product_delete")
    public String delete(int id){
        Product product = productService.get(id);
        productService.delete(id);
        return "redirect:/admin_product_list?cid=" + product.getCid();
    }

    @RequestMapping("admin_product_edit")
    public String edit(Model model, int id){
        Product product = productService.get(id);
        Category category = categoryService.get(product.getCid());
        product.setCategory(category);
        model.addAttribute("p", product);
        return "admin/editProduct";
    }

    @RequestMapping("admin_product_update")
    public String update(Product product){
        productService.update(product);
        return "redirect:/admin_product_list?cid=" + product.getCid();
    }

    /**
     * 1. 获取分类 cid,和分页对象page
     * 2. 通过PageHelper设置分页参数
     * 3. 基于cid，获取当前分类下的产品集合
     * 4. 通过PageInfo获取产品总数
     * 5. 把总数设置给分页page对象
     * 6. 拼接字符串"&cid="+c.getId()，设置给page对象的Param值。 因为产品分页都是基于当前分类下的分页，所以分页的时候需要传递这个cid
     * 7. 把产品集合设置到 request的 "ps" 产品上
     * 8. 把分类对象设置到 request的 "c" 产品上。 ( 这个c有什么用呢？ 在 其他-面包屑导航 中会用于显示分类名称)
     * 9. 把分页对象设置到 request的 "page" 对象上
     * 10. 服务端跳转到admin/listProduct.jsp页面
     * 11. 在listProduct.jsp页面上使用c:forEach 遍历ps集合，并显示
     * @param model
     * @param page
     * @param cid
     * @return
     */
    @RequestMapping("admin_product_list")
    public String list(Model model, Page page, int cid){
        Category category = categoryService.get(cid);

        PageHelper.offsetPage(page.getStart(), page.getCount());
        List<Product> productList = productService.list(cid);

        int total = (int) new PageInfo<>(productList).getTotal();
        page.setTotal(total);
        page.setParam("&cid=" + category.getId());

        model.addAttribute("ps", productList);
        model.addAttribute("c", category);
        model.addAttribute("page", page);

        return "admin/listProduct";
    }
}
