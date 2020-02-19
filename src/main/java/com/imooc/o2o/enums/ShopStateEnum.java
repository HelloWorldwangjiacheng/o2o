package com.imooc.o2o.enums;

import lombok.Getter;

@Getter
public enum ShopStateEnum {
    OFFLINE(-1,"非法店铺"),
    CHECK(0,"审核中"),
    SUCCESS(1,"操作成功"),
    PASS(2,"通过认证"),
    INNER_ERROR(-1001,"内部系统错误"),
    NULL_SHOPID(-1002,"shopId为空"),
    NULL_SHOP(-1003,"shop信息为空")
    ;

    private int state;
    private String stateInfo;

    //这里将其私有化是希望程序外的不能修改它,也就是将其变成常量
    private ShopStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    /**
     *  依据传入的state返回相应的enum值
     */
    public static ShopStateEnum stateOf(int state){
        for (ShopStateEnum stateEnum : values()){
            if (stateEnum.getState() == state){
                return stateEnum;
            }
        }

        return null;
    }
}
