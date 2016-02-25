package com.tr.ui.organization.model;

import java.io.Serializable;

import android.text.TextUtils;
/**
 * 地区类
 * @author liubang
 *
 */
public class Area implements Serializable{
	/**
	 * 
	 */
	public String id;//主键
	public static final long serialVersionUID = 7526691684164520101L;
	public String country; //国家 0 国内 1国外
    public String province;//省份
    public String city;//城市
    public String county;//县城
    public String address;//具体地址
	@Override
	public String toString() {
		return (TextUtils.isEmpty(country)?"":country) +(TextUtils.isEmpty(province)?"":province) + (TextUtils.isEmpty(city)?"":city)+ (TextUtils.isEmpty(county)?"":county) 
				+  (TextUtils.isEmpty(address)?"":address );
	}
    
}
