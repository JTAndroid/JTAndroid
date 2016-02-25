package com.tr.model.universal;

import java.net.MalformedURLException;
import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * @ClassName:     Comment.java
 * @Description:   评论对象
 * @Author         leon
 * @Version        v 1.0  
 * @Date           2014-04-10
 * @LastEdit       2014-04-10
 */
public class Comment {

	// "Comment":{
	// "userID":"发布评论用户的id",
	// "name":"发布用户名称",
	// "content":"评论内容",
	// "time":"发布时间"
	// "image":"个人头像url"
	// }

	public int mUserID = 0;
	public String mName = "";
	public String mContent = "";
	public String mPublishTime = "";
	public String mImage = "";

	public Comment() {

	}

	public void initWithJson(JSONObject jsonObject) throws JSONException,
			MalformedURLException, ParseException {

		String str_key = null;

		// 发布评论用户的id
		str_key = "userID";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mUserID = jsonObject.getInt(str_key);
		}

		// 发布用户名称
		str_key = "name";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mName = jsonObject.getString(str_key);
		}

		// 评论内容
		str_key = "content";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mContent = jsonObject.getString(str_key);
		}

		// 发布时间
		str_key = "time";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mPublishTime = jsonObject.getString(str_key);
		}
		
		// 个人头像
		str_key = "image";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mImage = jsonObject.getString(str_key);
		}
	}
	
	public JSONObject toJSONObject() throws JSONException{
		
		JSONObject jObject =  new JSONObject();
		jObject.put("userID", mUserID);
		jObject.put("name", mName);
		jObject.put("content", mContent);
		jObject.put("image", mImage);
		jObject.put("time", mPublishTime);
		return jObject;
	}
	
	public static Comment createFactory(JSONObject jsonObject) {
		try {
			Comment self = new Comment();
			self.initWithJson(jsonObject);
			return self;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
