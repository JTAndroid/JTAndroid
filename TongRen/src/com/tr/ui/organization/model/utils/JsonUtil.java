package com.tr.ui.organization.model.utils;



import org.json.JSONException;
import org.json.JSONObject;

import com.utils.string.StringUtils;

/**
 * json 工具类
 * <p>  </p>
 * @date 2015-3-26
 */
public class JsonUtil {
	public static String getNodeToString(JSONObject jsonObj,String key){
		return getNodeToString(jsonObj, key, "");
	}
	public static String getNodeToString(JSONObject jsonObj,String key,String defaultvalue){
		try {
			if(StringUtils.isEmpty(String.valueOf(jsonObj.get(key)))){
				return defaultvalue;
			}else{
				return StringUtils.trim(String.valueOf(jsonObj.get(key)));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return defaultvalue;
	}
	public static int getNodeToInt(JSONObject jsonObj,String key ){
		return getNodeToInt(jsonObj, key,0);
	}
	public static int getNodeToInt(JSONObject jsonObj,String key ,int defaultvalue){
		if(StringUtils.isEmpty(getNodeToString(jsonObj, key))){
			return defaultvalue;
		}else{
			return Integer.valueOf(getNodeToString(jsonObj, key));
		}
	}
	public static Long getNodeToLong(JSONObject jsonObj,String key){
		return getNodeToLong(jsonObj, key,0L);
	}
	public static Long getNodeToLong(JSONObject jsonObj,String key,long defaultvalue){
		if(StringUtils.isEmpty(getNodeToString(jsonObj, key))){
			return defaultvalue;
		}else{
			return Long.valueOf(getNodeToString(jsonObj, key));
		}
	}
}
