package com.imooc.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.exceptions.ProductOperationException;
import com.imooc.o2o.service.ProductCategoryService;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;
import com.imooc.o2o.util.ImageUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shopAdmin")
public class ProductManagementController {
    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ProductService productService;


    /**
     * 支持上传商品详情图的最大数量 IMAGE_MAX_COUNT 为 6
     */
    private static final int IMAGE_MAX_COUNT = 6;


    /**
     * 通过店铺Id获取该店铺下的商品列表
     *
     * @param request
     * @return
     */
    @GetMapping("/getProductListByShop")
    @ResponseBody
    public Map<String, Object> getProductListByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //获取前台传过来的页码
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        //获取前台传过来的每页要求返回的商品数上限
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        //从当前的session中获取店铺信息，主要是获取shopId
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //空值判断
        if ((pageIndex >= 0) && (pageSize >= 0) && (currentShop != null) && (currentShop.getShopId() != null)) {
            //获取传入的需要检索的条件，包括是否需要从某个商品类别以及模糊查找商品名去筛选某个店铺下的商品列表
            //筛选条件可以排列组合
            Long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
            String productName = HttpServletRequestUtil.getString(request, "productName");
            Product productCondition =
                    compactProductCondition(currentShop.getShopId(), productCategoryId, productName);
            //传入查询条件以及分页信息进行查询，返回相应的商品列表以及总数
            ProductExecution productExecution =
                    productService.getProductList(productCondition, pageIndex, pageSize);
            modelMap.put("success", true);
            modelMap.put("productList", productExecution.getProductList());
            modelMap.put("count", productExecution.getCount());
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "pageSize或者pageIndex或者shopId为空值");
        }
        return modelMap;
    }

    /**
     * @param shopId
     * @param productCategoryId
     * @param productName
     * @return
     */
    private Product compactProductCondition(Long shopId, Long productCategoryId, String productName) {
        Product productCondition = new Product();
        Shop shop = new Shop();
        shop.setShopId(shopId);
        productCondition.setShop(shop);
        // 若有指定类别的要求则添加进去
        if (productCategoryId != -1L) {
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            productCondition.setProductCategory(productCategory);
        }
        // 若有商铺名模糊查询的要求则添加进去
        if (productName != null) {
            productCondition.setProductName(productName);
        }
        return productCondition;
    }


    /**
     * @param request
     * @return
     */
    @PostMapping("/modifyProduct")
    @ResponseBody
    public Map<String, Object> modifyProduct(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        // 是商品编辑的时候调用还是上下架操作的时候调用
        // 若为前者则进行验证码判断，后者则跳过验证码判断
        boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");
        // 验证码判断
        if (!statusChange && !CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }

        //接收前端参数的变量的初始化，包括商品，缩略图，详情图列表实体类
        ObjectMapper mapper = new ObjectMapper();
        Product product = null;
        ImageHolder thumbnail = null;
        List<ImageHolder> productImgList = new ArrayList<>();
        CommonsMultipartResolver multipartResolver =
                new CommonsMultipartResolver(request.getSession().getServletContext());

        //若请求中存在文件流，则取出相关的文件（包括缩略图和详情图）
        try {
            if (multipartResolver.isMultipart(request)) {
                thumbnail = handleImage(request, thumbnail, productImgList);
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }

        try {
            String productStr = HttpServletRequestUtil.getString(request, "productStr");
            //尝试获取前端传过来的表单string流并将其转换成Product实体类
            product = mapper.readValue(productStr, Product.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }

        //非空判断
        if (product != null) {
            try {
                //从session中获取当前店铺的Id并赋值给product，减少对前端数据的依赖
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                Shop shop = new Shop();
                shop.setShopId(currentShop.getShopId());
                product.setShop(shop);
                // 开始进行商品信息变更操作
                ProductExecution productExecution =
                        productService.modifyProduct(product, thumbnail, productImgList);
                if (productExecution.getState() == ProductStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", productExecution.getStateInfo());
                }
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "product为空值");
            return modelMap;
        }

        return modelMap;
    }


    /**
     * 根据productId得到商品列表
     *
     * @param productId
     * @return
     */
    @GetMapping("/getProductById")
    @ResponseBody
    public Map<String, Object> getProductById(@RequestParam Long productId) {
        Map<String, Object> modelMap = new HashMap<>();
        //判断空值
        if (productId > -1) {
            //获取商品信息
            Product product = productService.getProductById(productId);
            //获取该店铺下的商品类别列表
            List<ProductCategory> productCategoryList =
                    productCategoryService.getProductCategoryList(product.getShop().getShopId());
            modelMap.put("product", product);
            modelMap.put("productCategoryList", productCategoryList);
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "productId为空");
        }
        return modelMap;
    }

    /**
     * 增加商品
     *
     * @param request
     * @return
     */
    @PostMapping("/addProduct")
    @ResponseBody
    public Map<String, Object> addProduct(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //验证码校验
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }

        //接受前端参数的变量的初始化，包括商品、缩略图、详情图列表实体类
        ObjectMapper mapper = new ObjectMapper();
        Product product = null;
        String productStr = HttpServletRequestUtil.getString(request, "productStr");
        //处理文件流的
        MultipartHttpServletRequest multipartHttpServletRequest = null;
        ImageHolder thumbnail = null;
        List<ImageHolder> productImgList = new ArrayList<>();
        CommonsMultipartResolver multipartResolver =
                new CommonsMultipartResolver(request.getSession().getServletContext());

        try {
            //若请求中存在文件流，则取出相关文件（包括缩略图和详情图）
            if (multipartResolver.isMultipart(request)) {
                thumbnail = handleImage(request, thumbnail, productImgList);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "上传图片不能为空");
                return modelMap;
            }

        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
        }

        try {
            //尝试获取前端传过来的表单string流并将其转换成Product实体类
            product = mapper.readValue(productStr, Product.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }

        //若Product信息，缩略图以及详情图列表为空，则开始进行商品添加操作
        if (product != null && thumbnail != null && productImgList.size() > 0) {
            try {
                //从session中获取当前店铺的Id并赋值给product，减少对前端数据的依赖
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                product.setShop(currentShop);
                //执行添加操作
                ProductExecution productExecution =
                        productService.addProduct(product, thumbnail, productImgList);
                if (productExecution.getState() == ProductStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", productExecution.getStateInfo());
                }
            } catch (ProductOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入商品信息");
        }
        return modelMap;
    }


    /**
     * 处理图片最终返回一个ImageHolder类型的对象
     *
     * @param request
     * @param thumbnail
     * @param productImgList
     * @return
     * @throws IOException
     */
    private static ImageHolder handleImage(HttpServletRequest request,
                                           ImageHolder thumbnail,
                                           List<ImageHolder> productImgList) throws IOException {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        //取出缩略图并构建ImageHolder对象
        CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("thumbnail");
        thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
        //取出详情图列表并构建List<ImageHolder>列表对象，最多支持六张图
        for (int i = 0; i < IMAGE_MAX_COUNT; i++) {
            CommonsMultipartFile productImgFile =
                    (CommonsMultipartFile) multipartHttpServletRequest.getFile("productImg" + i);
            if (productImgFile != null) {
                //若取出的第i个详情图片文件流不为空，则将其加入详情图列表
                ImageHolder productImg =
                        new ImageHolder(productImgFile.getOriginalFilename(), productImgFile.getInputStream());
                productImgList.add(productImg);
            } else {
                //如果取出的第i个详情图片文件流为空，则终止循环
                break;
            }
        }
        return thumbnail;
    }
}
