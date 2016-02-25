package com.tr.model.obj;

import java.net.MalformedURLException;
import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @ClassName:     Notification.java
 * @Description:   通知对象
 * @Author         leon
 * @Version        v 1.0  
 * @Date           2014-04-14
 * @LastEdit       2014-04-14
 */
public class Notification {

	// "notification": {
	// "notifCode": "通知信息代号",
	// "notifInfo": "通知信息描述"
	// }
	
	public int mNotifCode;
	public String mNotifInfo;
	
	public Notification(){
		
	}
	
	public void initWithJson(JSONObject jsonObject) throws JSONException,
			MalformedURLException, ParseException {

		String str_key = null;

		// 通知信息代号
		str_key = "notifCode";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mNotifCode = jsonObject.getInt(str_key);
		}

		// 通知信息描述
		str_key = "notifInfo";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mNotifInfo = jsonObject.getString(str_key);
		}
	}
}
