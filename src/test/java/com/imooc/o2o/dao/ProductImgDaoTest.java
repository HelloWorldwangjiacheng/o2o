package com.imooc.o2o.dao;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.ProductImg;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductImgDaoTest extends BaseTest {

    @Autowired
    private ProductImgDao productImgDao;

    @Test
    public void queryProductImgListTest() {
        List<ProductImg> productImgList = productImgDao.queryProductImgList(1L);
        System.out.println(productImgList);
    }

    @Test
    public void batchInsertProductImgTest() {
        //productId为1的商品里添加两个详情图片记录
        ProductImg productImg1 = new ProductImg();
        productImg1.setImgAddress("图片1");
        productImg1.setImgDesc("测试图片1");
        productImg1.setPriority(1);
        productImg1.setCreateTime(new Date());
        productImg1.setProductId(1L);

        ProductImg productImg2 = new ProductImg();
        productImg2.setImgAddress("图片2");
        productImg2.setImgDesc("测试图片2");
        productImg2.setPriority(2);
        productImg2.setCreateTime(new Date());
        productImg2.setProductId(1L);

        List<ProductImg> productImgList = new ArrayList<>();
        productImgList.add(productImg1);
        productImgList.add(productImg2);

        int i = productImgDao.batchInsertProductImg(productImgList);
        assertEquals(2,i);
    }

    @Test
    public void deleteProductImgByProductId() {
        int i = productImgDao.deleteProductImgByProductId(1L);
        assertEquals(2,i);
    }
}