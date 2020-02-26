package com.imooc.o2o.dao;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductDaoTest extends BaseTest {

    @Autowired
    private ProductDao productDao;

    @Test
    public void queryProductCount() {
    }

    @Test
    public void insertProduct() {
        Shop shop1 = new Shop();
        shop1.setShopId(1L);
        ProductCategory productCategory1 = new ProductCategory();
        productCategory1.setProductCategoryId(1L);

        Product product1 = new Product();
        product1.setProductName("测试1");
        product1.setProductDesc("测试Desc1");
        product1.setImgAddress("test1");
        product1.setPriority(1);
        product1.setEnableStatus(1);
        product1.setCreateTime(new Date());
        product1.setLastEditTime(new Date());
        product1.setShop(shop1);
        product1.setProductCategory(productCategory1);

        Product product2 = new Product();
        product2.setProductName("测试2");
        product2.setProductDesc("测试Desc2");
        product2.setImgAddress("test2");
        product2.setPriority(2);
        product2.setEnableStatus(2);
        product2.setCreateTime(new Date());
        product2.setLastEditTime(new Date());
        product2.setShop(shop1);
        product2.setProductCategory(productCategory1);

        int i = productDao.insertProduct(product1);
        assertEquals(1,i);
        i = productDao.insertProduct(product2);
        assertEquals(1,i);
    }

    @Test
    public void queryProductByProductIdTest(){
        Product product = productDao.queryProductByProductId(5);
        System.out.println(product.toString());
    }

    @Test
    public void deleteProductTest(){
        int effectNum = productDao.deleteProduct(5L,1L);
        assertEquals(1,effectNum);
    }

    @Test
    public void updateProduct() {

        Shop shop1 = new Shop();
        shop1.setShopId(1L);

        Product product1 = new Product();
        product1.setProductName("测试111111111111111111");
        product1.setProductDesc("测试Desc11111111111111111");
        product1.setImgAddress("test1");
        product1.setPriority(1);
        product1.setEnableStatus(1);
        product1.setCreateTime(new Date());
        product1.setLastEditTime(new Date());
        product1.setShop(shop1);
        product1.setProductId(3L);

        int effectNum = productDao.updateProduct(product1);
        assertEquals(1,effectNum);
    }
}