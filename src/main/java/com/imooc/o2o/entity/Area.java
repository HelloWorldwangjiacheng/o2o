package com.imooc.o2o.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Area {
    //区域id
    private Integer areaId;
    //区域名称
    private String areaName;
    //权重
    private Integer priority;
    //创建时间
    private Date createTime;
    //更新时间
    private Date lastEditTime;
}
