package com.imooc.o2o.web.shopadmin;

import com.imooc.o2o.dto.Result;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ProductCategoryStateEnum;
import com.imooc.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/shopAdmin")
public class ProductCategoryManagementController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping("/getProductCategoryList")
    @ResponseBody
    private Result<List< ProductCategory>> getProductCategoryList(HttpServletRequest request){
//        // To be removed
//        Shop shop = new Shop();
//        shop.setShopId(1L);
//        request.getSession().setAttribute("currentShop",shop);

        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        List<ProductCategory> list = null;
        if (currentShop!=null && currentShop.getShopId()>0){
            list = productCategoryService.getProductCategoryList(currentShop.getShopId());
            return new Result<List<ProductCategory>>(true,list);
        }else {
            ProductCategoryStateEnum ps = ProductCategoryStateEnum.INNER_ERROR;
            return new Result<List<ProductCategory>>(false, ps.getState(),ps.getStateInfo());
        }
    }
}
