package com.tr.model.user;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.text.ParseException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @ClassName: Person.java
 * @Description: 个人对象
 * @Author leon
 * @Version v 1.0
 * @Created 2014-04-10
 * @Updated 2014-04-10
 */
public class Person implements Serializable{

	// "Person":{
	// "name":"用户名称,如果用户信息没完善,该 person对象整体为null",
	// "jobTitle":"个人用户存放person对象，公司职位",
	// "company":"所在公司",
	// "image":"用户头像"
	// }

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String mName = "";
	public String mJobTitle = "";
	public String mCompany = "";
	public String mImage = "";

	
	@Override
	public String toString() {
		return "Person [mName=" + mName + ", mJobTitle=" + mJobTitle
				+ ", mCompany=" + mCompany + ", mImage=" + mImage + "]";
	}

	public Person() {

	}

	public void initWithJson(JSONObject jsonObject) throws JSONException,
			MalformedURLException, ParseException {

		String str_key;

		// 用户名称
		str_key = "name";
		if (jsonObject.has(str_key)
				&& !jsonObject.isNull(str_key)) {
			mName = jsonObject.getString(str_key);
		}

		// 公司职位
		str_key = "jobTitle";
		if (jsonObject.has(str_key)
				&& !jsonObject.isNull(str_key)) {
			mJobTitle = jsonObject.getString(str_key);
		}

		// 所在公司
		str_key = "company";
		if (jsonObject.has(str_key)
				&& !jsonObject.isNull(str_key)) {
			mCompany = jsonObject.getString(str_key);
		}

		// 头像
		str_key = "image";
		if (jsonObject.has(str_key)
				&& !jsonObject.isNull(str_key)) {
			mImage = jsonObject.getString(str_key);
		}
	}
}
