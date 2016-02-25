package com.tr.model.demand;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * @ClassName: LableData.java
 * @author fxtx
 * @Date 2015年3月19日 下午1:32:45
 * @Description: 标签对象
 */
public class LableData extends DemandSection {
	/**
	 * 
	 */
	public static final long serialVersionUID = 8145318279132507318L;
	/**
	 * 
	 */
	public int id;
	public int num;
	public String tag;



	public LableData(int id, int num, String tag) {
		super();
		this.id = id;
		this.num = num;
		this.tag = tag;
	}

	public LableData() {
	}

	/**
	 * 解析并获取用户标签
	 * 
	 * @param jsonObject
	 * @return
	 */
	public static List<LableData> createFactory(JSONObject jsonObject) {
		try {
			JSONObject json = jsonObject.optJSONObject("page");
			Gson gson = new Gson();
			ArrayList<LableData> arr = new ArrayList<LableData>();
			Type type = new TypeToken<ArrayList<LableData>>() {
			}.getType();
			arr = gson.fromJson(json.optJSONArray("list").toString(), type);
			return arr;
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}
}
