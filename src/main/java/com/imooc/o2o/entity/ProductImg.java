package com.imooc.o2o.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ProductImg {
    private Long productImgId;
    private String imgAddress;
    private String imgDesc;
    private Integer priority;
    private Date createTime;
    private Long productId;
}
