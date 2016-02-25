package com.tr.model.obj;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * 转发动态对象
 * @author zhongshan
 * @since 2015-01-26
 *
 */
public class ForwardDynamicNews implements Serializable {

	/**动态标题*/
	public String title;
	/**类型(10：创建知识；11：转发知识20：创建需求；21：转发需求30：新建人脉；31：转发人脉;40：新建会议；41：转发会议;50：用户名片;51：组织名片；52：人脉名片；53:客户名片)*/
	public String type; 
	/**动态内容*/
	public String content;
	/**转发内容*/
	public String forwardingContent; 
	/**创建者ID*/
	public String createrId; 
	/**源图片地址*/
	public String imgPath;
	/**源id，如知识id*/
	public String targetId;
	/**子类型，如：不同知识类型，转发会议0表示邀请函，1表示会议*/
	public String lowType;
	/**接受者id列表*/
	public ArrayList<String> listReceiverId;
	
	
	public JSONObject toJSONObject() throws JSONException{
		JSONObject jObject = new JSONObject();
		jObject.put("title", title);
		jObject.put("type", type);
		jObject.put("content", content);
		jObject.put("forwardingContent", forwardingContent);
		jObject.put("createrId", createrId);
		jObject.put("imgPath", imgPath);
		jObject.put("targetId", targetId);
		jObject.put("lowType", lowType);
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < listReceiverId.size(); i++) {
			jsonArray.put(i,listReceiverId.get(i));
		}
		jObject.put("listReceiverId", jsonArray);
		return jObject;
	}

}
