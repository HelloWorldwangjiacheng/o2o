package com.imooc.o2o.dao;

import com.imooc.o2o.entity.ProductCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductCategoryDao {
    /**
     * 通过shopId查找该店铺类别
     *
     * @param shopId
     * @return
     */
    List<ProductCategory> queryProductCategoryList(Long shopId);

    /**
     * 批量新增商品类别
     *
     * @param productCategoryList
     * @return
     */
    int batchInsertProductCategory(List<ProductCategory> productCategoryList);

    /**
     * 根据商品类别编号来进行批量的删除
     * @param productCategoryId, shopId
     * @return effectedNum
     */
    int deleteProductCategory(@Param("productCategoryId") Long productCategoryId, @Param("shopId") Long shopId);
}
