package com.lqz.tmall_ssm.controller;

import com.lqz.tmall_ssm.pojo.Product;
import com.lqz.tmall_ssm.pojo.ProductImage;
import com.lqz.tmall_ssm.service.ProductImageService;
import com.lqz.tmall_ssm.service.ProductService;
import com.lqz.tmall_ssm.util.ImageUtil;
import com.lqz.tmall_ssm.util.UploadedImageFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

@Controller
public class ProductImageController {

    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private ProductService productService;

    /**
     * 1. 通过pi对象接受type和pid的注入
     * 2. 借助productImageService，向数据库中插入数据。
     * 3. 根据session().getServletContext().getRealPath( "img/productSingle")，定位到存放单个产品图片的目录
     * 除了productSingle，还有productSingle_middle和productSingle_small。 因为每上传一张图片，都会有对应的正常，
     * 中等和小的三种大小图片，并且放在3个不同的目录下
     * 4. 文件命名以保存到数据库的产品图片对象的id+".jpg"的格式命名
     * 5. 通过uploadedImageFile保存文件
     * 6. 借助ImageUtil.change2jpg()方法把格式真正转化为jpg，而不仅仅是后缀名为.jpg
     * 7. 再借助ImageUtil.resizeImage把正常大小的图片，改变大小之后，分别复制到productSingle_middle和productSingle_small目录下。
     * 8. 处理完毕之后，客户端条跳转到admin_productImage_list?pid=，并带上pid。
     * 详情图片做的是一样的事情，区别在于复制到目录productDetail下，并且不需要改变大小
     * 注：ImageUtil.resizeImage 使用了swing自带的修改图片大小的API，
     * @param productImage
     * @param session
     * @param uploadedImageFile
     * @return
     */
    @RequestMapping("admin_productImage_add")
    public String add(ProductImage productImage, HttpSession session, UploadedImageFile uploadedImageFile){
        productImageService.add(productImage);
        String fileName = productImage.getId() + ".jpg";
        String imageFolder;
        String imageFolder_small = null;
        String imageFolder_middle = null;
        if (ProductImageService.type_single.equals(productImage.getType())){
            imageFolder = session.getServletContext().getRealPath("img/productSingle");
            imageFolder_small = session.getServletContext().getRealPath("img/productSingle_small");
            imageFolder_middle = session.getServletContext().getRealPath("img/productSingle_middle");
        }else {
            imageFolder = session.getServletContext().getRealPath("img/productDetail");
        }

        File file = new File(imageFolder, fileName);
        file.getParentFile().mkdirs();
        try {
            uploadedImageFile.getImage().transferTo(file);
            BufferedImage bufferedImage = ImageUtil.change2jpg(file);
            ImageIO.write(bufferedImage, "jpg", file);

            if (ProductImageService.type_single.equals(productImage.getType())){
                File file_small = new File(imageFolder_small, fileName);
                File file_middle = new File(imageFolder_middle, fileName);

                ImageUtil.resizeImage(file, 56, 56, file_small);
                ImageUtil.resizeImage(file, 217, 190, file_middle);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/admin_productImage_list?pid=" + productImage.getPid();
    }

    @RequestMapping("admin_productImage_delete")
    public String delete(int id, HttpSession session){
        ProductImage productImage = productImageService.get(id);
        String fileName = productImage.getId() + ".jpg";
        String imageFolder;
        String imageFolder_small = null;
        String imageFolder_middle = null;
        if (ProductImageService.type_single.equals(productImage.getType())){
            imageFolder = session.getServletContext().getRealPath("img/productSingle");
            imageFolder_small = session.getServletContext().getRealPath("img/productSingle_small");
            imageFolder_middle = session.getServletContext().getRealPath("img/productSingle_middle");
            File imageFile = new File(imageFolder, fileName);
            File file_small = new File(imageFolder_small, fileName);
            File file_middle = new File(imageFolder_middle, fileName);
            imageFile.delete();
            file_middle.delete();
            file_small.delete();
        }else {
            imageFolder = session.getServletContext().getRealPath("img/productDetail");
            File imageFile = new File(imageFolder, fileName);
            imageFile.delete();
        }
        productImageService.delete(id);

        return "redirect:/admin_productImage_list?pid=" + productImage.getPid();
    }

    @RequestMapping("admin_productImage_list")
    public String list(int pid, Model model){
        Product product = productService.get(pid);
        List<ProductImage> listSingle = productImageService.list(pid, ProductImageService.type_single);
        List<ProductImage> listDetail = productImageService.list(pid, ProductImageService.type_detail);

        model.addAttribute("p", product);
        model.addAttribute("pisSingle", listSingle);
        model.addAttribute("pisDetail", listDetail);

        return "admin/listProductImage";
    }
}
