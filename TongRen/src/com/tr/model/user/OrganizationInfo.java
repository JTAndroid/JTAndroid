package com.tr.model.user;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @ClassName:     OrganizationInfo.java
 * @Description:   机构信息对象
 * @Author         leon
 * @Version        v 1.0  
 * @Date           2014-04-09
 * @LastEdit       2014-04-09
 */
public class OrganizationInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * "OrganizationInfo":{
                    "state":"-1 未进行第一次认证 0第一次认证进行中 1第一次认证失败 2第一次认证成功",
                    "failInfo":"审核失败原因，审核失败时有效",
                    "fullname":"机构全称",
                    "shortName": "机构简称",
                    "contactCardImage":"机构联系人身份证照片",
                    "logo":"机构logo图片链接地址，jpg格式",
                    "OCCImage":"组织机构代码证图片地址",
                    "TCImage":"营业执照图片地址",
                    "legalPersonIDCardImage":"法人身份证图片"
                    
                    "occCode":"组织机构代码",
                    "address":"机构地址",
                    "legalName":"机构法人代表名称",
                    "docomentID":"纸文档编号",
                    "areaName":"所属地区名称",
                    "tradeName":"所属行业"
    	}
	 * 
	 */
	
	public int mState = 0;
	public String mFailInfo = "";
	public String mFullName = "";
	public String mShortName = "";
	public String mContactCardImage = "";
	public String mLogo = "";
	public String mOCCImage = "";
	public String mTCImage = "";
	//暂且当作customerId使用
	public String mLegalPersonIDCardImage = "";
	
	public String mOccCode = "";
	public String mAddress = "";
	public String mLegalName = "";
	public String mDocomentID = "";
	public String mAreaName = "";
	public String mTradeName = "";
	
	public OrganizationInfo(){
		
	}
	
	public void initWithJson(JSONObject jsonObject) throws JSONException,
			MalformedURLException, ParseException {

		String str_key;
		
		// 状态
		str_key = "state";
		if(jsonObject.has(str_key)){
			mState = jsonObject.optInt(str_key);
		}
		
		// 审核失败原因
		str_key = "failInfo";
		if (jsonObject.has(str_key)) {
			mFailInfo = jsonObject.optString(str_key);
		}
		
		// 机构全称
		str_key = "fullname";
		if (jsonObject.has(str_key)) {
			mFullName = jsonObject.optString(str_key);
		}
		
		// 机构简称
		str_key = "shortname";
		if (jsonObject.has(str_key)) {
			mShortName = jsonObject.optString(str_key);
		}
		
		// 机构联系人身份证照片
		str_key = "contactCardImage";
		if (jsonObject.has(str_key)) {
			mContactCardImage = jsonObject.optString(str_key);
		}
		
		// 机构logo图片链接地址
		str_key = "logo";
		if (jsonObject.has(str_key)) {
			mLogo = jsonObject.optString(str_key);
		}
		
		// 组织机构代码证图片
		str_key = "occImage";
		if (jsonObject.has(str_key)) {
			mOCCImage = jsonObject.optString(str_key);
		}
		
		// 营业执照图片
		str_key = "tcImage";
		if (jsonObject.has(str_key)) {
			mTCImage = jsonObject.optString(str_key);
		};

		// 法人身份证图片
		str_key = "legalPersonIDCardImage";
		if (jsonObject.has(str_key)) {
			mLegalPersonIDCardImage = jsonObject.optString(str_key);
		};
	}
}
