package com.imooc.o2o.dto;

import lombok.Data;

/**
 * 封装json对象
 * @param <T>
 */
@Data
public class Result<T> {
    private boolean success;
    private T data;
    private String errMsg;
    private int errorCode;

    public Result(){}
    // 成功时的构造器
    public Result(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    // 错误时的构造器
    public Result(boolean success, int errorCode, String errMsg) {
        this.success = success;
        this.errMsg = errMsg;
        this.errorCode = errorCode;
    }
}
