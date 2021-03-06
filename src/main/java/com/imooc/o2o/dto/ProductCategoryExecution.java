package com.imooc.o2o.dto;

import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.enums.ProductCategoryStateEnum;
import lombok.Data;

import java.util.List;

@Data
public class ProductCategoryExecution {
	// 结果状态
	private int state;

	// 状态标识
	private String stateInfo;

	// 操作的商铺类别
	private List<ProductCategory> productCategoryList;

	public ProductCategoryExecution() {}

	// 预约失败的构造器
	public ProductCategoryExecution(ProductCategoryStateEnum stateEnum) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
	}

	// 预约成功的构造器
	public ProductCategoryExecution(ProductCategoryStateEnum stateEnum,
			List<ProductCategory> productCategoryList) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.productCategoryList = productCategoryList;
	}

}
