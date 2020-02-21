package com.imooc.o2o.service;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exceptions.ShopOperationException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class ShopServiceTest extends BaseTest {

    @Autowired
    private ShopService shopService;

    @Test
    public void addShopTest() throws FileNotFoundException {
        Shop shop = new Shop();
        PersonInfo owner = new PersonInfo();
        Area area = new Area();
        ShopCategory shopCategory = new ShopCategory();
        owner.setUserId(1L);
        area.setAreaId(2);
        shopCategory.setShopCategoryId(1L);
        shop.setOwner(owner);
        shop.setArea(area);
        shop.setShopCategory(shopCategory);
        shop.setShopName("测试的店铺4");
        shop.setShopDesc("test");
        shop.setShopAddress("test");
        shop.setPhone("test");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(ShopStateEnum.CHECK.getState());
        shop.setAdvice("审核中");

        File shopImg = new File("C:\\Users\\w1586\\Desktop\\images\\heihei.jpg");
//        FileInputStream fileInputStream = new FileInputStream(shopImg);
//        ShopExecution shopExecution = shopService.addShop(shop, fileInputStream, shopImg.getName());
//        ShopExecution shopExecution = shopService.addShop(shop, shopImg);
//        assertEquals(ShopStateEnum.CHECK.getState(),shopExecution.getState());
    }

    @Test
    public void modifyShopTest() throws ShopOperationException, FileNotFoundException {
        Shop shop = new Shop();
        shop.setShopId(1L);
        shop.setShopName("修改之后的店铺的名称");
        File shopImg = new File("C:\\Users\\w1586\\Desktop\\images\\upload\\item\\shop\\9\\2020022003482718679.jpg");
        InputStream is = new FileInputStream(shopImg);
        ShopExecution shopExecution = shopService.modifyShop(shop, is, "2020022003482718679.jpg");
        System.out.println(shopExecution.getShop().getShopImg());
    }

    @Test
    public void getShopListTest(){
        Shop shopCondition = new Shop();
        ShopCategory shopCategory = new ShopCategory();
        shopCategory.setShopCategoryId(1L);
        shopCondition.setShopCategory(shopCategory);
        ShopExecution shopList = shopService.getShopList(shopCondition, 2, 4);
        System.out.println("店铺列表数（一页有几个店铺）：" + shopList.getShopList().size());
        System.out.println("店铺总数为："+shopList.getCount());
    }
}