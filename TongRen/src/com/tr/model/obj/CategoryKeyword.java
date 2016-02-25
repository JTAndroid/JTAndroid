package com.tr.model.obj;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.MonthDisplayHelper;


public class CategoryKeyword implements Serializable{

	// {"investKeyword":{
	// "moneyType":"CNY,USD等，配置登录里返回的数组里选择",
	// "moneyRange":"资金范围，配置登录里返回的数组里选择",
	// "listInvestType":[],
	// "listTrade":[],
	// "area":{}
	// }
	// }

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public MoneyType mMoneyType = new MoneyType();
	public String mMoneyRange = "";
	public List<Trade> mListTrade = new ArrayList<Trade>(); // 行业
	public List<InvestType> mListintInvestType = new ArrayList<InvestType>(); // 投资类型
	public List<InvestType> mListInvestType = new ArrayList<InvestType>(); // 投资类型
	public List<Area> mListArea = new ArrayList<Area>(); // 区域
	
	public CategoryKeyword(){
		
	}
	
	public void initWithJson(JSONObject jsonObject) throws JSONException,
			MalformedURLException, ParseException {

		String str_key = null;

		// 货币类型
		str_key = "moneyType";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mMoneyType.initWithJson(jsonObject.getJSONObject(str_key));
		}

		// 金额
		str_key = "moneyRange";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mMoneyRange = jsonObject.getString(str_key);
		}

		// 投资类型
		str_key = "listInvestType";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			JSONArray jArray = jsonObject.getJSONArray(str_key);
			if (jArray != null && jArray.length() > 0) {
				for (int i = 0; i < jArray.length(); i++) {
					InvestType obj = new InvestType();
					obj.initWithJson(jArray.getJSONObject(i));
					mListInvestType.add(obj);
				}
			}
		}

		// 投资行业
		str_key = "listTrade";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			JSONArray jArray = jsonObject.getJSONArray(str_key);
			if (jArray != null && jArray.length() > 0) {
				for (int i = 0; i < jArray.length(); i++) {
					Trade obj = new Trade();
					obj.initWithJson(jArray.getJSONObject(i));
					mListTrade.add(obj);
				}
			}
		}
	}

	// 获取相关的JSON对象
	public JSONObject toJSONObject() throws JSONException {
		JSONObject jObject = new JSONObject();
		jObject.put("moneyType", mMoneyType);
		jObject.put("moneyRange", mMoneyRange);
		List<JSONObject> jsonArr = new ArrayList<JSONObject>();
		// 投资类型
		jsonArr.clear();
		for (int i = 0; i < mListInvestType.size(); i++) {
			jsonArr.add(mListInvestType.get(i).toJSONObject());
		}
		jObject.put("listInvestType", jsonArr);
		// 行业
		jsonArr.clear();
		for (int i = 0; i < mListTrade.size(); i++) {
			jsonArr.add(mListTrade.get(i).toJSONObject());
		}
		jObject.put("listTrade", jsonArr);
		return jObject;
	}
}
