package com.imooc.o2o.entity;

import lombok.Data;

import java.util.Date;

@Data
public class PersonInfo {
    private Long userId;
    private String name;
    private String profileImg;
    private String email;
    private String gender;
    private Integer enableStatus;
    // 1是顾客 2是店家 3是超级管理员
    private Integer userType;
    private Date createTime;
    private Date lastEditTime;
}
