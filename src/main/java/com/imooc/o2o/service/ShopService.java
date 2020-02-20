package com.imooc.o2o.service;

import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.exceptions.ShopOperationException;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.InputStream;

public interface ShopService {
    /**
     * 通过店铺ID获取店铺信息
     * @param shopId
     * @return
     */
    Shop getByShopId(Long shopId);

    /**
     *
     */
    ShopExecution modifyShop(Shop shop, InputStream shopImgInputStream, String fileName) throws ShopOperationException;

    /**
     * 添加店铺
     * @param shop
     * @param
     * @return
     */
//    ShopExecution addShop(Shop shop, CommonsMultipartFile shopImg);
//    ShopExecution addShop(Shop shop, File shopImg);
    ShopExecution addShop(Shop shop, InputStream shopImgInputStream, String fileName) throws ShopOperationException;


}
