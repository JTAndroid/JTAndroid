package com.tr.model.demand;

import java.util.ArrayList;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.tr.model.page.IPageBaseItem;

/**
 * 模版信息
 * 
 * @author Administrator
 *
 */
public class TemplateData implements IPageBaseItem {
	/**
	 * 
	 */
	public static final long serialVersionUID = 3457525949719017300L;
	public String id; // 信息的id
	public String title;// 模版标题
	public String name;// 模版名称
	public String demandIntro;// 模版介绍
	public int type;// 模版类型
	public String createrTime;// 创建时间
	public String createrName;// 创建人
	public String contacts;// 联系人手机号
	public String amount;// 模版金额
	public String phoneNum;// 联系人手机号
	public String area;// 模版地区
	public String createrId;// 创建人id
	public String industry; // 行业
	public int demandType;// 模版类型
	public ArrayList<String> listProperty = new ArrayList<String>();//自定义属性
	
	/**
	 * 解析json
	 * 
	 * @param jsonObject
	 * @return
	 */
	public static TemplateData createFactory(JSONObject jsonObject) {
		try {
			
			TemplateData bean = new TemplateData();
			Gson gson = new Gson();
			bean = gson.fromJson(jsonObject.toString(), TemplateData.class); //将数据以Gson的形式进行处理
			return bean;
		} catch (Exception e) {
			return null;
		}
	}
}
