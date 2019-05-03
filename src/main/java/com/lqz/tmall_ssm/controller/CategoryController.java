package com.lqz.tmall_ssm.controller;

import com.lqz.tmall_ssm.pojo.Category;
import com.lqz.tmall_ssm.service.CategoryService;
import com.lqz.tmall_ssm.util.ImageUtil;
import com.lqz.tmall_ssm.util.Page;
import com.lqz.tmall_ssm.util.UploadedImageFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("admin_category_list")
    public String list(Model model, Page page){
        List<Category> cs= categoryService.list(page);
        int total = categoryService.total();
        page.setTotal(total);
        model.addAttribute("cs", cs);
        model.addAttribute("page", page);
        return "admin/listCategory";
    }

    @RequestMapping("admin_category_add")
    public String add(Category category, HttpSession session, UploadedImageFile imageFile) throws IOException {
        categoryService.add(category);
        File imageFolder = new File(session.getServletContext().getRealPath("img/category"));
        File file = new File(imageFolder, category.getId() + ".jpg");
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        imageFile.getImage().transferTo(file);
        BufferedImage image = ImageUtil.change2jpg(file);
        ImageIO.write(image, "jpg", file);
        return "redirect:/admin_category_list";
    }

    @RequestMapping("admin_category_delete")
    public String delete(int id, HttpSession session){
        categoryService.delete(id);
        File imageFolder = new File(session.getServletContext().getRealPath("img/category"));
        File file = new File(imageFolder, id + ".jpg");
        file.delete();
        return "redirect:/admin_category_list";
    }

    @RequestMapping("admin_category_edit")
    public String edit(int id, Model model){
        Category category = categoryService.get(id);
        model.addAttribute("c", category);
        return "admin/editCategory";
    }

    /**
     * 新增update方法
     * 1. update 方法映射路径admin_category_update的访问
     * 1.1 参数 Category c接受页面提交的分类名称
     * 1.2 参数 session 用于在后续获取当前应用的路径
     * 1.3 UploadedImageFile 用于接受上传的图片
     * 2. 通过categoryService更新c对象
     * 3. 首先判断是否有上传图片，如果有上传，那么通过session获取ControllerContext,再通过getRealPath定位存放分类图片的路径。
     * 4. 根据分类id创建文件名
     * 5. 通过UploadedImageFile 把浏览器传递过来的图片保存在上述指定的位置
     * 6. 通过ImageUtil.change2jpg(file); 确保图片格式一定是jpg，而不仅仅是后缀名是jpg.
     * 7. 客户端跳转到admin_category_list
     * @param category
     * @param session
     * @param uploadedImageFile
     * @return
     * @throws IOException
     */
    @RequestMapping("admin_category_update")
    public String update(Category category, HttpSession session, UploadedImageFile uploadedImageFile) throws IOException {
        categoryService.update(category);
        MultipartFile image = uploadedImageFile.getImage();
        if (null != image && !image.isEmpty()) {
            File imageFolder = new File(session.getServletContext().getRealPath("img/category"));
            File file = new File(imageFolder, category.getId() + ".jpg");
            image.transferTo(file);
            BufferedImage bufferedImage = ImageUtil.change2jpg(file);
            ImageIO.write(bufferedImage, "jpg", file);
        }
        return "redirect:/admin_category_list";
    }
}
