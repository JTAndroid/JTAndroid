package com.tr.model.demand;

import java.io.Serializable;

import android.text.TextUtils;

import com.tr.ui.organization.model.profile.CustomerPersonalLine;

/**
 * @ClassName: CustomData.java
 * @author fxtx
 * @Date 2015年3月18日 下午4:20:22
 * @Description: 自定义字段数据适配器
 */
public class CustomData implements Serializable {
	/**
	 * 
	 */
	public static final long serialVersionUID = -3580271351693787146L;
	/**
	 * 
	 */
	public String key;
	public String value = "";
	public boolean isVisable;

	public CustomData() {
	}

	/**
	 */
	public CustomData(String kay, boolean isVisable) {
		this.key = kay;
		this.isVisable = isVisable;
	}

	/**
	 * 数据转换
	 * 
	 * @param customerPersonalLine
	 * @return
	 */
	public static CustomerPersonalLine toCustomerPersonalLine(CustomData custom) {
		CustomerPersonalLine customer = new CustomerPersonalLine();
		customer.content = custom.value;
		customer.name = custom.key;
		customer.type = "1";// 需求模块 只会是1
		return customer;
	}

	/**
	 * 数据转换
	 * 
	 * @param customerPersonalLine
	 * @return
	 */
	public static CustomData toCustomData(
			CustomerPersonalLine customerPersonalLine) {
		CustomData customerData = new CustomData();
		if (!TextUtils.isEmpty(customerPersonalLine.content)) {
			customerData.value = customerPersonalLine.content;
		}
		customerData.key = customerPersonalLine.name;
		return customerData;
	}

}
