package com.imooc.o2o.dao;

import com.imooc.o2o.entity.Shop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopDao {
    /**
     * 新增店铺
     * @param shop
     * @return
     */
    int insertShop(Shop shop);

    /**
     * 更新店铺
     * @param shop
     * @return
     */
    int updateShop(Shop shop);

    /**
     *  通过shop id 查询店铺
     * @param shopId
     * @return Shop
     */
    Shop queryByShopId(Long shopId);

    /**
     * 分页查询店铺列表,可输入的条件有：店铺名（模糊查询），店铺状态，店铺类别，区域ID，owner
     * rowIndex 是在数据库的第几行记录开始查数据，
     * pageSize 是指一次查几行记录，而这几行记录会在同一页展示出来，也就是返回的条数
     */
    List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition,
                             @Param("rowIndex") int rowIndex,
                             @Param("pageSize") int pageSize);

    /**
     * shop表的总记录的数量
     * @param shopCondition
     * @return
     */
    int queryShopCount(@Param("shopCondition") Shop shopCondition);

}
