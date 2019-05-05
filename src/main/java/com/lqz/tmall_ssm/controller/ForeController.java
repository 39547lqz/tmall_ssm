package com.lqz.tmall_ssm.controller;

import com.github.pagehelper.PageHelper;
import com.lqz.tmall_ssm.comparator.*;
import com.lqz.tmall_ssm.pojo.*;
import com.lqz.tmall_ssm.service.*;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller
public class ForeController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private PropertyValueService propertyValueService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private ReviewService reviewService;

    /**
     * 主页
     * @param model
     * @return
     */
    @RequestMapping("forehome")
    public String home(Model model){
        List<Category> list = categoryService.list();
        productService.fill(list);
        productService.fillByRow(list);
        model.addAttribute("cs", list);
        return "fore/home";
    }

    /**
     * 注册
     * 1. 通过参数User获取浏览器提交的账号密码
     * 2. 通过HtmlUtils.htmlEscape(name);把账号里的特殊符号进行转义
     * 3. 判断用户名是否存在
     * 3.1 如果已经存在，就服务端跳转到reigster.jsp，并且带上错误提示信息
     * 3.2 如果不存在，则加入到数据库中，并服务端跳转到registerSuccess.jsp页面
     * @param model
     * @param user
     * @return
     */
    @RequestMapping("foreregister")
    public String register(Model model, User user){
        String name = user.getName();
        name = HtmlUtils.htmlEscape(name);
        user.setName(name);
        boolean exist = userService.isExist(name);

        if (exist){
            String msg = "用户名已经存在";
            model.addAttribute("msg", msg);
            model.addAttribute("user", null);
            return "fore/register";
        }
        userService.add(user);

        return "redirect:/registerSuccessPage";
    }

    /**
     * 登录
     * @param name
     * @param password
     * @param model
     * @param session
     * @return
     */
    @RequestMapping("forelogin")
    public String login(@RequestParam("name")String name, @RequestParam("password")String password, Model model, HttpSession session){
        name = HtmlUtils.htmlEscape(name);
        User user = userService.get(name, password);

        if (null == user){
            model.addAttribute("msg", "账号或密码错误");
            return "fore/login";
        }
        session.setAttribute("user", user);
        return "redirect:/forehome";
    }

    /**
     * 登出
     * @param session
     * @return
     */
    @RequestMapping("forelogout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/forehome";
    }

    /**
     * 跳转到商品详情页
     * @param model
     * @param pid
     * @return
     */
    @RequestMapping("foreproduct")
    public String product(Model model, int pid){
        Product product = productService.get(pid);

        List<ProductImage> productImageSingle = productImageService.list(pid, ProductImageService.type_single);
        List<ProductImage> productImageDetail = productImageService.list(pid, ProductImageService.type_detail);
        product.setProductSingleImages(productImageSingle);
        product.setProductDetailImages(productImageDetail);

        List<PropertyValue> propertyValues = propertyValueService.list(pid);
        List<Review> reviews = reviewService.list(pid);
        productService.setSaleAndReviewNumber(product);

        model.addAttribute("p", product);
        model.addAttribute("pvs", propertyValues);
        model.addAttribute("reviews", reviews);

        return "fore/product";
    }

    /**
     * 查询是否登录
     * @param session
     * @return
     */
    @RequestMapping("forecheckLogin")
    @ResponseBody
    public String checkLogin(HttpSession session){
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return "success";
        }
        return "fail";
    }

    /**
     * 使用ajax异步查询登录账号密码是否正确
     * @param name
     * @param password
     * @param session
     * @return
     */
    @RequestMapping("foreloginAjax")
    @ResponseBody
    public String loginAjax(@RequestParam("name")String name, @RequestParam("password")String password, HttpSession session){
        name = HtmlUtils.htmlEscape(name);
        User user = userService.get(name, password);
        if (user == null) {
            return "fail";
        }
        session.setAttribute("user", user);
        return "success";
    }

    /**
     * 跳转到分类页
     * 1. 获取参数cid
     * 2. 根据cid获取分类Category对象 c
     * 3. 为c填充产品
     * 4. 为产品填充销量和评价数据
     * 5. 获取参数sort
     * 5.1 如果sort==null，即不排序
     * 5.2 如果sort!=null，则根据sort的值，从5个Comparator比较器中选择一个对应的排序器进行排序
     * 6. 把c放在model中
     * 7. 服务端跳转到 category.jsp
     * @return
     */
    @RequestMapping("forecategory")
    public String category(int cid, String sort, Model model){
        Category category = categoryService.get(cid);
        productService.fill(category);
        productService.setSaleAndReviewNumber(category.getProducts());

        if (sort != null) {
            switch (sort){
                case "review":
                    Collections.sort(category.getProducts(), new ProductReviewComparator());
                    break;
                case "date":
                    Collections.sort(category.getProducts(), new ProductDateComparator());
                    break;
                case "saleCount":
                    Collections.sort(category.getProducts(), new ProductSaleCountComparator());
                    break;
                case "price":
                    Collections.sort(category.getProducts(), new ProductPriceComparator());
                    break;
                case "all":
                    Collections.sort(category.getProducts(), new ProductAllComparator());
                    break;
            }
        }
        model.addAttribute("c", category);
        return "fore/category";
    }

    /**
     * 搜索
     * 1. 获取参数keyword
     * 2. 根据keyword进行模糊查询，获取满足条件的前20个产品
     * 3. 为这些产品设置销量和评价数量
     * 4. 把产品结合设置在model的"ps"属性上
     * 5. 服务端跳转到 searchResult.jsp 页面
     * @param keyword
     * @param model
     * @return
     */
    @RequestMapping("foresearch")
    public String search(String keyword, Model model){
        PageHelper.offsetPage(0, 20);
        List<Product> products = productService.search(keyword);
        productService.setSaleAndReviewNumber(products);
        model.addAttribute("ps", products);
        return "fore/searchResult";
    }

    /**
     *  点击立即购买
     * 1. 获取参数pid
     * 2. 获取参数num
     * 3. 根据pid获取产品对象p
     * 4. 从session中获取用户对象user
     *
     * 接下来就是新增订单项OrderItem， 新增订单项要考虑两个情况
     * a. 如果已经存在这个产品对应的OrderItem，并且还没有生成订单，即还在购物车中。 那么就应该在对应的OrderItem基础上，调整数量
     * a.1 基于用户对象user，查询没有生成订单的订单项集合
     * a.2 遍历这个集合
     * a.3 如果产品是一样的话，就进行数量追加
     * a.4 获取这个订单项的 id
     *
     * b. 如果不存在对应的OrderItem,那么就新增一个订单项OrderItem
     * b.1 生成新的订单项
     * b.2 设置数量，用户和产品
     * b.3 插入到数据库
     * b.4 获取这个订单项的 id
     *
     * 最后， 基于这个订单项id客户端跳转到结算页面/forebuy
     * @param pid
     * @param num
     * @param session
     * @return
     */
    @RequestMapping("forebuyone")
    public String buyone(int pid, int num, HttpSession session){
        Product product = productService.get(pid);
        int oiid = 0;

        User user = (User) session.getAttribute("user");
        boolean found = false;
        List<OrderItem> orderItems = orderItemService.listByUser(user.getId());
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getProduct().getId().intValue() == product.getId().intValue()){
                orderItem.setNumber(orderItem.getNumber() + num);
                orderItemService.update(orderItem);
                found = true;
                oiid = orderItem.getId();
                break;
            }
        }
        if (!found){
            OrderItem orderItem = new OrderItem();
            orderItem.setUid(user.getId());
            orderItem.setNumber(num);
            orderItem.setPid(pid);
            orderItemService.add(orderItem);
            oiid = orderItem.getId();
        }
        return "redirect:forebuy?oiid=" + oiid;
    }

    /**
     * 进入结算页
     * 1. 通过字符串数组获取参数oiid
     * 为什么这里要用字符串数组试图获取多个oiid，而不是int类型仅仅获取一个oiid?
     * 因为根据购物流程环节与表关系，结算页面还需要显示在购物车中选中的多条OrderItem数据，
     * 所以为了兼容从购物车页面跳转过来的需求，要用字符串数组获取多个oiid
     * 2. 准备一个泛型是OrderItem的集合ois
     * 3. 根据前面步骤获取的oiids，从数据库中取出OrderItem对象，并放入ois集合中
     * 4. 累计这些ois的价格总数，赋值在total上
     * 5. 把订单项集合放在session的属性 "ois" 上
     * 6. 把总价格放在 model的属性 "total" 上
     * 7. 服务端跳转到buy.jsp
     * @param model
     * @param oiid
     * @param session
     * @return
     */
    @RequestMapping("forebuy")
    public String buy(Model model, String[] oiid, HttpSession session){
        List<OrderItem> orderItems = new ArrayList<>();
        float total = 0;

        for (String strid : oiid) {
            int id = Integer.parseInt(strid);
            OrderItem orderItem = orderItemService.get(id);
            total += orderItem.getProduct().getPromotePrice() * orderItem.getNumber();
            orderItems.add(orderItem);
        }
        session.setAttribute("ois", orderItems);
        model.addAttribute("total", total);

        return "fore/buy";
    }

    /**
     * 1. 获取参数pid
     * 2. 获取参数num
     * 3. 根据pid获取产品对象p
     * 4. 从session中获取用户对象user
     * 接下来就是新增订单项OrderItem， 新增订单项要考虑两个情况
     * a. 如果已经存在这个产品对应的OrderItem，并且还没有生成订单，即还在购物车中。 那么就应该在对应的OrderItem基础上，调整数量
     * a.1 基于用户对象user，查询没有生成订单的订单项集合
     * a.2 遍历这个集合
     * a.3 如果产品是一样的话，就进行数量追加
     * a.4 获取这个订单项的 id
     * b. 如果不存在对应的OrderItem,那么就新增一个订单项OrderItem
     * b.1 生成新的订单项
     * b.2 设置数量，用户和产品
     * b.3 插入到数据库
     * b.4 获取这个订单项的 id
     * @param pid
     * @param num
     * @param model
     * @param session
     * @return
     */
    @RequestMapping("foreaddCart")
    @ResponseBody
    public String addCart(int pid, int num, Model model, HttpSession session){
        Product product = productService.get(pid);
        User user = (User) session.getAttribute("user");
        boolean found = false;

        List<OrderItem> orderItems = orderItemService.listByUser(user.getId());
        for (OrderItem orderItem : orderItems) {
            orderItem.setNumber(orderItem.getNumber() + num);
            orderItemService.update(orderItem);
            found = true;
            break;
        }

        if (!found){
            OrderItem orderItem = new OrderItem();
            orderItem.setUid(user.getId());
            orderItem.setNumber(num);
            orderItem.setPid(pid);
            orderItemService.add(orderItem);
        }
        return "success";
    }

    /**
     * 1. 通过session获取当前用户
     * 所以一定要登录才访问，否则拿不到用户对象,会报错
     * 2. 获取为这个用户关联的订单项集合 ois
     * 3. 把ois放在model中
     * 4. 服务端跳转到cart.jsp
     * @param model
     * @param session
     * @return
     */
    @RequestMapping("forecart")
    public String cart(Model model, HttpSession session){
        User user = (User) session.getAttribute("user");
        List<OrderItem> orderItems = orderItemService.listByUser(user.getId());
        model.addAttribute("ois", orderItems);
        return "fore/cart";
    }

    /**
     *  修改购物车
     * 1. 判断用户是否登录
     * 2. 获取pid和number
     * 3. 遍历出用户当前所有的未生成订单的OrderItem
     * 4. 根据pid找到匹配的OrderItem，并修改数量后更新到数据库
     * 5. 返回字符串"success"
     * @param model
     * @param session
     * @param pid
     * @param number
     * @return
     */
    @RequestMapping("forechangeOrderItem")
    @ResponseBody
    public String changeOrderItem(Model model, HttpSession session, int pid, int number){
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "fail";
        }

        List<OrderItem> orderItems = orderItemService.listByUser(user.getId());
        for (OrderItem orderItem : orderItems) {
            orderItem.setNumber(number);
            orderItemService.update(orderItem);
            break;
        }
        return "success";
    }

    /**
     * 删除订单项
     * 1. 判断用户是否登录
     * 2. 获取oiid
     * 3. 删除oiid对应的OrderItem数据
     * 4. 返回字符串"success"
     * @param model
     * @param session
     * @param oiid
     * @return
     */
    @RequestMapping("foredeleteOrderItem")
    @ResponseBody
    public String deleteOrderItem(Model model, HttpSession session, int oiid){
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "fail";
        }
        orderItemService.delete(oiid);
        return "success";
    }

    /**
     *  跳转到支付页
     * 1. 从session中获取user对象
     * 2. 通过参数Order接受地址，邮编，收货人，用户留言等信息
     * 3. 根据当前时间加上一个4位随机数生成订单号
     * 4. 根据上述参数，创建订单对象
     * 5. 把订单状态设置为等待支付
     * 6. 从session中获取订单项集合 ( 在结算功能的ForeController.buy() 13行，订单项集合被放到了session中 )
     * 7. 把订单加入到数据库，并且遍历订单项集合，设置每个订单项的order，更新到数据库
     * 8. 统计本次订单的总金额
     * 9. 客户端跳转到确认支付页forealipay，并带上订单id和总金额
     * @param model
     * @param order
     * @param session
     * @return
     */
    @RequestMapping("forecreateOrder")
    public String createOrder(Model model, Order order, HttpSession session){
        User user = (User) session.getAttribute("user");
        String orderCode = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + RandomUtils.nextInt(10000);
        order.setOrderCode(orderCode);
        order.setCreateDate(new Date());
        order.setUid(user.getId());
        order.setStatus(OrderService.waitPay);
        List<OrderItem> orderItemList = (List<OrderItem>) session.getAttribute("ois");

        float total = orderService.add(order, orderItemList);
        return "redirect:/forealipay?oid=" + order.getId() + "&total=" + total;
    }

    /**
     * 1.1 获取参数oid
     * 1.2 根据oid获取到订单对象order
     * 1.3 修改订单对象的状态和支付时间
     * 1.4 更新这个订单对象到数据库
     * 1.5 把这个订单对象放在model的属性"o"上
     * 1.6 服务端跳转到payed.jsp
     * @param oid
     * @param total
     * @param model
     * @return
     */
    @RequestMapping("forepayed")
    public String payed(int oid, float total, Model model){
        Order order = orderService.get(oid);
        order.setStatus(OrderService.waitDelivery);
        order.setPayDate(new Date());
        orderService.update(order);
        model.addAttribute("o", order);
        return "fore/payed";
    }

    /**
     * 查看已买到的宝贝
     * 1. 通过session获取用户user
     * 2. 查询user所有的状态不是"delete" 的订单集合os
     * 3. 为这些订单填充订单项
     * 4. 把os放在model的属性"os"上
     * 5. 服务端跳转到bought.jsp
     * @param model
     * @param session
     * @return
     */
    @RequestMapping("forebought")
    public String bought(Model model, HttpSession session){
        User user =(User)  session.getAttribute("user");
        List<Order> os= orderService.list(user.getId(),OrderService.delete);

        orderItemService.fill(os);

        model.addAttribute("os", os);
        return "fore/bought";
    }

    /**
     * 确认收货
     * 2.1 获取参数oid
     * 2.2 通过oid获取订单对象o
     * 2.3 为订单对象填充订单项
     * 2.4 把订单对象放在request的属性"o"上
     * 2.5 服务端跳转到 confirmPay.jsp
     * @param model
     * @param oid
     * @return
     */
    @RequestMapping("foreconfirmPay")
    public String confirmPay(Model model, int oid){
        Order order = orderService.get(oid);
        orderItemService.fill(order);
        model.addAttribute("o", order);
        return "fore/confirmPay";
    }

    /**
     * 确认收货成功
     * 1.1 获取参数oid
     * 1.2 根据参数oid获取Order对象o
     * 1.3 修改对象o的状态为等待评价，修改其确认支付时间
     * 1.4 更新到数据库
     * 1.5 服务端跳转到orderConfirmed.jsp页面
     * @param model
     * @param oid
     * @return
     */
    @RequestMapping("foreorderConfirmed")
    public String orderConfirmed(Model model, int oid){
        Order order = orderService.get(oid);
        order.setStatus(OrderService.waitReview);
        order.setConfirmDate(new Date());
        orderService.update(order);
        return "fore/orderConfirmed";
    }

    /**
     * 删除订单
     * 1.1 获取参数oid
     * 1.2 根据oid获取订单对象o
     * 1.3 修改状态
     * 1.4 更新到数据库
     * 1.5 返回字符串"success"
     * @param model
     * @param oid
     * @return
     */
    @RequestMapping("foredeleteOrder")
    @ResponseBody
    public String deleteOrder(Model model, int oid){
        Order order = orderService.get(oid);
        order.setStatus(OrderService.delete);
        orderService.update(order);
        return "success";
    }

    /**
     * 评价页面
     * 1.1 获取参数oid
     * 1.2 根据oid获取订单对象o
     * 1.3 为订单对象填充订单项
     * 1.4 获取第一个订单项对应的产品,因为在评价页面需要显示一个产品图片，那么就使用这第一个产品的图片了
     * 1.5 获取这个产品的评价集合
     * 1.6 为产品设置评价数量和销量
     * 1.7 把产品，订单和评价集合放在request上
     * 1.8 服务端跳转到 review.jsp
     * @param model
     * @param oid
     * @return
     */
    @RequestMapping("forereview")
    public String review(Model model, int oid){
        Order order = orderService.get(oid);
        orderItemService.fill(order);
        Product product = order.getOrderItems().get(0).getProduct();
        List<Review> reviews = reviewService.list(product.getId());
        productService.setSaleAndReviewNumber(product);
        model.addAttribute("p", product);
        model.addAttribute("o", order);
        model.addAttribute("reviews", reviews);
        return "fore/review";
    }

    /**
     * 1. ForeController.doreview()
     * 1.1 获取参数oid
     * 1.2 根据oid获取订单对象o
     * 1.3 修改订单对象状态
     * 1.4 更新订单对象到数据库
     * 1.5 获取参数pid
     * 1.6 根据pid获取产品对象
     * 1.7 获取参数content (评价信息)
     * 1.8 对评价信息进行转义，道理同注册ForeController.register()
     * 1.9 从session中获取当前用户
     * 1.10 创建评价对象review
     * 1.11 为评价对象review设置 评价信息，产品，时间，用户
     * 1.12 增加到数据库
     * 1.13.客户端跳转到/forereview： 评价产品页面，并带上参数showonly=true
     * 2. reviewPage.jsp
     * @param model
     * @param session
     * @param oid
     * @param pid
     * @param content
     * @return
     */
    @RequestMapping("foredoreview")
    public String doReview(Model model, HttpSession session, @RequestParam("oid")int oid, @RequestParam("pid")int pid, String content){
        Order order = orderService.get(oid);
        order.setStatus(OrderService.finish);
        orderService.update(order);

        Product product = productService.get(pid);
        content = HtmlUtils.htmlEscape(content);

        User user = (User) session.getAttribute("user");
        Review review = new Review();
        review.setContent(content);
        review.setPid(pid);
        review.setCreateDate(new Date());
        review.setUid(user.getId());
        reviewService.add(review);

        return "redirect:/forereview?oid=" + oid + "&showonly=true";
    }
}
