package com.utils.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * jsonObject对象  快速解析数据, 致力于解决解析json代码冗余,及工厂类过大问题.
 * @author gushi
 *
 */
public class QJsonParser {
	
	/**
	 * 解析单个类型
	 * @param strKey
	 * @param dataType
	 * @param dataMap
	 * @param jsonObject
	 * @throws JSONException
	 */
	public static Map<String, Object> parse(String strKey, DataType dataType, Map<String, Object> dataMap, JSONObject jsonObject) throws JSONException {

		if(dataMap == null ){
			dataMap = new HashMap<String, Object>();
		}
		
		if (!jsonObject.has(strKey)) {
			return dataMap;
		}
		
		if (jsonObject.has(strKey)) {
			switch (dataType) {

			case INTEGER:
					int intObj = jsonObject.getInt(strKey);
					dataMap.put(strKey, intObj);
				break;
				
			case STRING:
					String stringObj = jsonObject.getString(strKey);
					dataMap.put(strKey, stringObj);
				break;

			case BOOLEAN:
				boolean booleanObj = jsonObject.getBoolean(strKey);
				dataMap.put(strKey, booleanObj);
				break;

			default:
				break;
			}

		}
		
		return dataMap;

	}
	
	
	/**
	 * 解析array类型
	 * @param strKey
	 * @param dataType
	 * @param dataMap
	 * @param jsonObject
	 * @throws JSONException
	 */
//	public static Map<String, Object> parseArray(String strKey, DataType dataType, Map<String, Object> dataMap, JSONObject jsonObject) throws JSONException {
//
//		if(dataMap == null ){
//			dataMap = new HashMap<String, Object>();
//		}
//		
//		switch (dataType) {
//		
//		case INTEGER:
//			if (jsonObject.has(strKey)) {
//				String listCountStr = jsonObject.getJSONArray(strKey).toString();
//				List<Integer> arrayInteger = JSON.parseArray(listCountStr, Integer.class);
//				dataMap.put(strKey, arrayInteger);
//			}
//			break;
//			
//		case STRING:
//			
//			if (jsonObject.has(strKey)) {
//				String listTagStr = jsonObject.getJSONArray(strKey).toString();
//				List<String> listTag = JSON.parseArray(listTagStr, String.class);
//				dataMap.put(strKey, listTag);
//			}
//			break;
//
//		default:
//			break;
//		}
//		
//		return dataMap;
//	}
	
	/**
	 * 新array解析，
	 * @param strKey
	 * @param clazz  
	 * @param dataMap
	 * @param jsonObject
	 * @return
	 * @throws JSONException
	 */
	public static Map<String, Object> parseArray(String strKey, Class clazz, Map<String, Object> dataMap, JSONObject jsonObject) throws JSONException {

		if (dataMap == null) {
			dataMap = new HashMap<String, Object>();
		}

		if (jsonObject.has(strKey)) {
//			String listCountStr = jsonObject.getJSONArray(strKey).toString();
//			List<Object> arrayObj = JSON.parseArray(listCountStr, clazz);
			
			Gson gson = new Gson();
			//这里将lsit<javabean>转化成json字符串
			String jsonString = gson.toJson(strKey);
			//解析json字符串
			List<Object> arrayObj = gson.fromJson(jsonString, new TypeToken<List<Object>>(){}.getType());
			dataMap.put(strKey, arrayObj);
		}

		return dataMap;
	}
	
	public enum DataType {  
		INTEGER, 
		BOOLEAN, 
		STRING, 
	} 

}
