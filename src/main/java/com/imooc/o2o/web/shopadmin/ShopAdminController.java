package com.imooc.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "shopAdmin",method = {RequestMethod.GET})
public class ShopAdminController {

    @GetMapping("/shopOperation")
    public String shopOperation(){
        //已经在spring-web.xml里面定义了转发地址prefix以及加了后缀
        return "shop/shopOperation";
    }

    @GetMapping("/shopList")
    public String shopList(){
        return "shop/shopList";
    }

    @GetMapping("/shopManagement")
    public String shopManagement(){
        return "shop/shopManagement";
    }

    @GetMapping("/productCategoryManagement")
    public String productCategoryManagement(){
        return "shop/productCategoryManagement";
    }
}
