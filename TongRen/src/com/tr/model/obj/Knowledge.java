package com.tr.model.obj;

import java.net.MalformedURLException;
import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import com.tr.model.page.IPageBaseItem;

/**
 * @ClassName:     Knowledge.java
 * @Description:   知识对象
 * @Author         leon
 * @Version        v 1.0  
 * @Date           2014-04-11
 * @LastEdit       2014-04-11
 */
public class Knowledge implements IPageBaseItem{

	private static final long serialVersionUID = 1982123441110064422L;

	// "WorkExperience":{
	// "id":"相关知识列表，知识id",
	// "url":"知识对应url",
	// "title":"知识标题"
	// }
	public String mTitle = "";
	private String time;
	public int mID = 0;
	public String mUrl = "";
	public String mTime = "";

	public int getmID() {
		return mID;
	}

	public void setmID(int mID) {
		this.mID = mID;
	}

	public String getmUrl() {
		return mUrl;
	}

	public void setmUrl(String mUrl) {
		this.mUrl = mUrl;
	}

	public String getmTitle() {
		return mTitle;
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Knowledge() {

	}

	public static KnowledgeMini createFactory(JSONObject jsonObject) {
		KnowledgeMini self = null;
		try {
			self = new KnowledgeMini();
			self.initWithJson(jsonObject);
		} catch (Exception e) {

		}
		return self;
	}

	public void initWithJson(JSONObject jsonObject) throws JSONException,
			MalformedURLException, ParseException {

		String str_key = null;

		// 知识id
		str_key = "id";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mID = jsonObject.getInt(str_key);
		}

		// 知识url
		str_key = "url";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mUrl = jsonObject.getString(str_key);
		}

		// 知识标题
		str_key = "title";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mTitle = jsonObject.getString(str_key);
		}

		// 发布时间
		str_key = "time";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			time = jsonObject.getString(str_key);
		}
	}

	public JSONObject toJSONObject() throws JSONException {

		JSONObject jObject = new JSONObject();
		jObject.put("id", mID);
		jObject.put("url", mUrl);
		jObject.put("title", mTitle);
		jObject.put("time", mTime);
		return jObject;
	}

}
