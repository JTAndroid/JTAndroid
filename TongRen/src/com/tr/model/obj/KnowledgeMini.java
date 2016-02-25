package com.tr.model.obj;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;

import com.tr.model.page.IPageBaseItem;
import com.utils.common.JTDateUtils;

/**
 * @ClassName: KnowledgeMini.java
 * @Description: 知识缩略对象
 * @Author leon
 * @Version v 1.0
 * @Date 2014-04-12
 * @Update 2014-04-14
 */
public class KnowledgeMini implements IPageBaseItem {
	private static final long serialVersionUID = 1982123441110064422L;

	// "WorkExperience":{
	// "id":"相关知识列表，知识id",
	// "url":"知识对应url",
	// "title":"知识标题"
	// }
	
    //"id": "知识id",
//"type": "类型：1-资讯，2-投融工具，3-行业，4-经典案例，5-图书报告，6-资产管理，7-宏观，8-观点，9-判例，10-法律法规，11-文章",
//"title": "标题",
//"desc": "描述",
	
	public int id;
	public int type;
	public String title;
	public String desc;

	public String mTitle = "";
	public String time;
	public int mID = 0;
	public String mUrl = "";
//	public boolean isSelected;
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

	public KnowledgeMini() {

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
			id = jsonObject.getInt(str_key);
		}

		// 知识url
		str_key = "type";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			type = jsonObject.getInt(str_key);
		}

		// 知识标题
		str_key = "title";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			title = jsonObject.getString(str_key);
		}

		// 发布时间
		str_key = "desc";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			desc = jsonObject.getString(str_key);
		}
	}

	public JSONObject toJSONObject() throws JSONException {

		JSONObject jObject = new JSONObject();
		jObject.put("id", id);
		jObject.put("type", type);
		jObject.put("title", title);
		jObject.put("desc", desc);
		return jObject;
	}
	@SuppressLint("SimpleDateFormat")
	public JTFile toJTFile(){
		JTFile jtFile = new JTFile();
		jtFile.mSuffixName = mTitle;
		jtFile.mUrl = mUrl;
		jtFile.mTaskId =  mID + "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(JTDateUtils.DATE_FORMAT_2);
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(time));
			jtFile.mCreateTime = cal.getTimeInMillis();
		} 
		catch (ParseException e) {
			e.printStackTrace();
		}
		return jtFile;
	}
}
