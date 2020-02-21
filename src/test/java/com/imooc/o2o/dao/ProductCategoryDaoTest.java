package com.imooc.o2o.dao;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class ProductCategoryDaoTest extends BaseTest {

    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Test
    public void queryProductCategoryList() {
        List<ProductCategory> productCategories = productCategoryDao.queryProductCategoryList(1L);
        assertEquals(2,productCategories.size());
        System.out.println("--->"+productCategories.toString());
    }
}