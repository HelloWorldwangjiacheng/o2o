package com.imooc.o2o.dao;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class ShopDaoTest extends BaseTest {

    @Autowired
    private ShopDao shopDao;

    @Test
    public void insertShopTest() {
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
        shop.setShopName("测试的店铺1");
        shop.setShopDesc("test");
        shop.setShopAddress("test");
        shop.setPhone("test");
        shop.setShopImg("test");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(1);
        shop.setAdvice("审核中");
        int effectedNum = shopDao.insertShop(shop);
//        Assert.assertEquals(1,effectedNum);
        assertEquals(1,effectedNum);
    }

    @Test
    public void updateShopTest(){
        Shop shop = new Shop();
        shop.setShopId(2L);
        shop.setShopDesc("描述");
        shop.setShopAddress("地址");
        shop.setLastEditTime(new Date());
        int effectedNum = shopDao.updateShop(shop);
        Assert.assertEquals(1,effectedNum);
//        assertEquals(1,effectedNum);
    }

    @Test
    public void queryByShopIdTest(){
        Long shopId = 1L;
        Shop shop = shopDao.queryByShopId(shopId);
        System.out.println(shop.getArea().getAreaId());
        System.out.println(shop.getArea().getAreaName());
//        return shop;
    }

    @Test
    public void queryShopListTest(){
        Shop shopCondition = new Shop();
        PersonInfo owner = new PersonInfo();
        owner.setUserId(1L);
        shopCondition.setOwner(owner);
        List<Shop> shops = shopDao.queryShopList(shopCondition, 0, 5);
        System.out.println(shops.size());

        ShopCategory shopCategory = new ShopCategory();
        shopCategory.setShopCategoryId(1L);
        shopCondition.setShopCategory(shopCategory);
        List<Shop> shopList = shopDao.queryShopList(shopCondition, 0, 2);
        System.out.println("---->"+shopList.size());
        int count = shopDao.queryShopCount(shopCondition);
        System.out.println("---->"+count);
    }

    @Test
    public void queryShopListCountTest(){
        Shop shopCondition = new Shop();
        PersonInfo owner = new PersonInfo();
        owner.setUserId(1L);
        shopCondition.setOwner(owner);
//        List<Shop> shops = shopDao.queryShopList(shopCondition, 0, 5);
        int i = shopDao.queryShopCount(shopCondition);
        System.out.println(i);
    }
}