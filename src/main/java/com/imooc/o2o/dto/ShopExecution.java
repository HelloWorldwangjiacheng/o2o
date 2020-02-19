package com.imooc.o2o.dto;

import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ShopStateEnum;
import lombok.Data;

import java.util.List;

@Data
public class ShopExecution {
    //结果标识
    private int state;

    //状态标识
    private String stateInfo;

    //店铺数量
    private int count;

    //操作的shop（增删改查店铺的时候需要用到）
    private Shop shop;

    //shop列表（查询店铺列表的时候使用）
    private List<Shop> shopList;

    public ShopExecution(){}
    //失败情况下的构造器,就只会展示状态码和错误信息
    public ShopExecution(ShopStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }
    //成功情况下的构造器，返回一个shop对象
    public ShopExecution(ShopStateEnum stateEnum, Shop shop) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.shop = shop;
    }
    //成功情况下的构造器，返回一个shop对象列表
    public ShopExecution(ShopStateEnum stateEnum, List<Shop> shopList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.shopList = shopList;
    }

}
