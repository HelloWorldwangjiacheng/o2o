package com.imooc.o2o.service;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.imooc.o2o.BaseTest;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ProductStateEnum;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductServiceTest extends BaseTest {

    @Autowired
    private ProductService productService;


    @Test
    public void addProductTest() throws FileNotFoundException {
        Product product = new Product();
        Shop shop = new Shop();
        shop.setShopId(1L);
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(1L);
        product.setShop(shop);
        product.setProductCategory(productCategory);
        product.setProductName("测试商品11");
        product.setProductDesc("测试商品11");
        product.setPriority(20);
        product.setCreateTime(new Date());
        product.setEnableStatus(ProductStateEnum.SUCCESS.getState());
        //创建缩略图文件流
        File file = new File("C:\\Users\\w1586\\Desktop\\images\\heihei.jpg");
        FileInputStream fileInputStream = new FileInputStream(file);
        ImageHolder imageHolder = new ImageHolder(file.getName(), fileInputStream);
        //创建两个商品详情图文件流并将他们添加到详情图列表中
        File file1 = new File("C:\\Users\\w1586\\Desktop\\images\\heihei.jpg");
        FileInputStream fileInputStream1 = new FileInputStream(file);
        File file2 = new File("C:\\Users\\w1586\\Desktop\\images\\hua.jpg");
        FileInputStream fileInputStream2 = new FileInputStream(file);
        List<ImageHolder> productImgList = new ArrayList<>();
        productImgList.add(new ImageHolder(file1.getName(),fileInputStream1));
        productImgList.add(new ImageHolder(file2.getName(),fileInputStream2));
        //添加商品认证
        ProductExecution productExecution = productService.addProduct(product, imageHolder, productImgList);
        assertEquals(ProductStateEnum.SUCCESS.getState(),productExecution.getState());
    }

    @Test
    public void modifyProductTest() throws FileNotFoundException {
        Product product = new Product();
        Shop shop = new Shop();
        shop.setShopId(1L);
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(1L);
        product.setProductId(4L);
        product.setShop(shop);
        product.setProductCategory(productCategory);
        product.setProductName("红烧肉");
        product.setProductDesc("红烧肉");
        //创建缩略图文件流
        File file = new File("C:\\Users\\w1586\\Desktop\\images\\hua.jpg");
        FileInputStream fileInputStream = new FileInputStream(file);
        ImageHolder imageHolder = new ImageHolder(file.getName(), fileInputStream);
        //创建两个商品详情图文件流并将他们添加到详情图列表中
        File file1 = new File("C:\\Users\\w1586\\Desktop\\images\\heihei.jpg");
        FileInputStream fileInputStream1 = new FileInputStream(file);
        File file2 = new File("C:\\Users\\w1586\\Desktop\\images\\hua.jpg");
        FileInputStream fileInputStream2 = new FileInputStream(file);
        List<ImageHolder> productImgList = new ArrayList<>();
        productImgList.add(new ImageHolder(file1.getName(),fileInputStream1));
        productImgList.add(new ImageHolder(file2.getName(),fileInputStream2));
        //添加商品认证
        ProductExecution productExecution = productService.modifyProduct(product, imageHolder, productImgList);
        assertEquals("红烧肉",productExecution.getProduct().getProductName());
    }
}