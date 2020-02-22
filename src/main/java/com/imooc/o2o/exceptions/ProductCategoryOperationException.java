package com.imooc.o2o.exceptions;

public class ProductCategoryOperationException extends RuntimeException{


    private static final long serialVersionUID = -7758956879714860892L;

    public ProductCategoryOperationException(String message) {
        super(message);
    }
}
