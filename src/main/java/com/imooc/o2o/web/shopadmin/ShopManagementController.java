package com.imooc.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.service.AreaService;
import com.imooc.o2o.service.ShopCategoryService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shopAdmin")
public class ShopManagementController {

    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private AreaService areaService;

    @GetMapping("/getShopManagementInfo")
    @ResponseBody
    public Map<String,Object> getShopManagementInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        Long shopId = HttpServletRequestUtil.getLong(request,"shopId");
        if (shopId<=0){
            Object currentShopObj = request.getSession().getAttribute("currentShop");
            if (currentShopObj == null){
                modelMap.put("redirect",true);
                modelMap.put("url","/o2o_war/shopAdmin/shopList");
            }else{
                Shop currentShop = (Shop) currentShopObj;
                modelMap.put("redirect",false);
                modelMap.put("shopId",currentShop.getShopId());
            }
        }else {
            Shop currentShop = new Shop();
            currentShop.setShopId(shopId);
            request.getSession().setAttribute("currentShop",currentShop);
            modelMap.put("redirect",false);
        }

        return modelMap;
    }

    @GetMapping("/getShopList")
    @ResponseBody
    public Map<String,Object> getShopList(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();

        PersonInfo user = new PersonInfo();
        user.setUserId(1L);
        request.getSession().setAttribute("user",user);
        user = (PersonInfo) request.getSession().getAttribute("user");
        user.setUserId(1L);
        user.setName("wjc");
        try {
            Shop shopCondition = new Shop();
            shopCondition.setOwner(user);
            ShopExecution shopExecution = shopService.getShopList(shopCondition, 0, 100);
            modelMap.put("shopList",shopExecution.getShopList());
            modelMap.put("user",user);
            modelMap.put("success",true);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
        }
        return modelMap;
    }

    @GetMapping("/getShopById")
    @ResponseBody
    public Map<String,Object> getShopById(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();
        Long shopId = HttpServletRequestUtil.getLong(request,"shopId");
        if (shopId > -1){
            try{
                Shop shop = shopService.getByShopId(shopId);
                List<Area> areaList = areaService.getAreaList();
                modelMap.put("shop",shop);
                modelMap.put("areaList",areaList);
                modelMap.put("success",true);
            }catch (Exception e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
            }

        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","shopId 为空");
        }
        return modelMap;
    }

    @ResponseBody
    @GetMapping("/getShopInitInfo")
    public Map<String, Object> getShopInitInfo(){
        Map<String,Object> modelMap = new HashMap<>();
        List<ShopCategory> shopCategoryList = new ArrayList<>();
        List<Area> areaList = new ArrayList<>();
        try {
            shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
            areaList = areaService.getAreaList();
            modelMap.put("shopCategoryList",shopCategoryList);
            modelMap.put("areaList",areaList);
            modelMap.put("success",true);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
        }

        return modelMap;
    }

    /**
     * 注册店铺
     * @param request
     * @return
     */
    @PostMapping("/registerShop")
    @ResponseBody
    public Map<String, Object> registerShop(HttpServletRequest request)  {
        //1.接受并转化相应的参数，包括店铺信息和图片信息
        Map<String,Object> modelMap = new HashMap<>();

        if (!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","输入了错误的验证码");
            return modelMap;
        }


        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper objectMapper = new ObjectMapper();
        Shop shop = null;

        try {
            shop = objectMapper.readValue(shopStr,Shop.class);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        CommonsMultipartFile shopImg = null;
        //上传文件流
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());

        if (commonsMultipartResolver.isMultipart(request)){
            //获取前端传递的文件流
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","上传图片不能为空");
            return modelMap;
        }

        //2.注册店铺
        //判断shop和shopImg是否为空
        if (shop!=null && shopImg!=null){
            PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
            //TODO   session
//            owner.setUserId(1L);
            shop.setOwner(owner);
            //这样就是一个随机的新的文件
//            File shopImgFile = new File(PathUtil.getImgBasePath()+ ImageUtil.getRandomFileName());
//            try{
//                shopImgFile.createNewFile();
//            }catch (IOException io){
//                modelMap.put("success",false);
//                modelMap.put("errMsg",io.getMessage());
//                return modelMap;
//            }

//            try {
//                inputStreamToFile(shopImg.getInputStream(),shopImgFile);
//            }catch (IOException e){
//                modelMap.put("success",false);
//                modelMap.put("errMsg",e.getMessage());
//                return modelMap;
//            }

//            ShopExecution shopExecution = shopService.addShop(shop, shopImgFile);
            ShopExecution shopExecution = null;
            try {
                shopExecution  = shopService.addShop(shop, shopImg.getInputStream(), shopImg.getOriginalFilename());
//                shopExecution  = shopService.addShop(shop, shopImgFile);
//                shopExecution  = shopService.addShop(shop, shopImg);
            }catch (Exception e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
            }

            if (shopExecution.getState() == ShopStateEnum.CHECK.getState()){
                modelMap.put("success",true);
                //该用户可以操作的店铺列表
                @SuppressWarnings("unchecked")
                List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
                if (shopList == null || shopList.size() == 0){
                    shopList = new ArrayList<Shop>();
                }
                shopList.add(shopExecution.getShop());
                request.getSession().setAttribute("shopList",shopList);
            }else {
                modelMap.put("success",false);
                modelMap.put("errMsg",shopExecution.getStateInfo());
            }
            return modelMap;
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入店铺信息");
            return modelMap;
        }

        //3.返回结果,这个已经穿插在上面的各个异常中了，也就是返回的modelMap
    }

    /**
     * 注册店铺
     * @param request
     * @return
     */
    @PostMapping("/modifyShop")
    @ResponseBody
    public Map<String, Object> modifyShop(HttpServletRequest request)  {
        //1.接受并转化相应的参数，包括店铺信息和图片信息
        Map<String,Object> modelMap = new HashMap<>();
        if (!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","输入了错误的验证码");
            return modelMap;
        }

        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper objectMapper = new ObjectMapper();
        Shop shop = null;
        try {
            shop = objectMapper.readValue(shopStr,Shop.class);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        CommonsMultipartFile shopImg = null;
        //上传文件流
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());

        if (commonsMultipartResolver.isMultipart(request)){
            //获取前端传递的文件流
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }
        //2.修改店铺信息
        //判断shop和shopImg是否为空
        if (shop!=null && shop.getShopId() != null){
            ShopExecution shopExecution = null;
            try {
                if (shopImg == null){
                    shopExecution = shopService.modifyShop(shop,null,null);
                }else {
                    shopExecution  = shopService.modifyShop(shop, shopImg.getInputStream(), shopImg.getOriginalFilename());
                }
            }catch (Exception e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
            }

            if (shopExecution.getState() == ShopStateEnum.SUCCESS.getState()){
                modelMap.put("success",true);
            }else {
                modelMap.put("success",false);
                modelMap.put("errMsg",shopExecution.getStateInfo());
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入店铺Id");
        }
        return modelMap;
    }

//    private static void inputStreamToFile(InputStream ins, File file){
//        FileOutputStream outputStream = null;
//        try {
//            outputStream = new FileOutputStream(file);
//            int bytesRead = 0;
//            byte[] buffer = new byte[1024];
//            while((bytesRead = ins.read(buffer)) != -1){
//                outputStream.write(buffer,0,bytesRead);
//            }
//        }catch (Exception e){
//            throw new RuntimeException("调用inputStreamToFile产生异常:" + e.getMessage());
//        }finally {
//            try {
//                if (outputStream != null){
//                    outputStream.close();
//                }
//                if (ins != null){
//                    ins.close();
//                }
//            }catch (IOException ioe){
//                throw new RuntimeException("inputStreamToFile关闭io产生异常：" + ioe.getMessage());
//            }
//        }
//    }
}
