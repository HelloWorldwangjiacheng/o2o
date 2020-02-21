package com.imooc.o2o.dao;

import com.imooc.o2o.entity.ProductCategory;

import java.util.List;

public interface ProductCategoryDao {
    /**
     * 通过shopId查找该店铺类别
     * @param shopId
     * @return
     */
    List<ProductCategory> queryProductCategoryList(Long shopId);
}