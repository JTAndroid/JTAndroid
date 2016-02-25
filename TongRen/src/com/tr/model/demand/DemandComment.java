package com.tr.model.demand;

import java.io.Serializable;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.tr.model.page.IPageBaseItem;

/**
 * @ClassName: DemandComment.java
 * @author fxtx
 * @Date 2015年3月27日 下午4:54:20
 * @Description: TODO(用一句话描述该文件做什么)
 */
public class DemandComment implements IPageBaseItem {
	/**
	 * 
	 */
	public static final long serialVersionUID = -2334032406046172835L;
	
	public String id;
	public String demandId;
	public String createrName;
	public String createrId;
	public String picPath;
	public String createTime;
	public int visable;
	public String content;


	/**
	 * 解析json
	 * 
	 * @param jsonObject
	 * @return
	 */
	public static DemandComment createFactory(JSONObject jsonObject) {
		try {
			DemandComment bean = new DemandComment();
			Gson gson = new Gson();
			bean = gson.fromJson(jsonObject.toString(), DemandComment.class); // 将数据以Gson的形式进行处理
			return bean;
		} catch (Exception e) {
			return null;
		}
	}
}
