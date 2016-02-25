package com.tr.model.conference;

import java.io.Serializable;
import java.util.List;

import org.json.JSONObject;

import com.google.gson.Gson;

public class MListFileIndex implements Serializable{

	/**
	 * sunjianan 20141120
	 */
	private static final long serialVersionUID = -6773843422902498871L;
	
	private List<MFileIndex> listFileIndex;
	
	/**
	 * @param jsonObject
	 * @return Meeting
	 */
	public static MListFileIndex createFactory(JSONObject jsonObject) {
		MListFileIndex file = null;
		try {
			if (jsonObject != null) {
//				file = JSON.parseObject(jsonObject.toString(), MListFileIndex.class);
				
				Gson gson = new Gson();
				//这里将javabean转化成json字符串
				String jsonString = gson.toJson(jsonObject);
				System.out.println(jsonString);
				//这里将json字符串转化成javabean对象,
				file = gson.fromJson(jsonString,MListFileIndex.class);
			}
			return file;

		} catch (Exception e) {
		}
		return null;
	}
	
	public List<MFileIndex> getListFileIndex() {
		return listFileIndex;
	}

	public void setListFileIndex(List<MFileIndex> listFileIndex) {
		this.listFileIndex = listFileIndex;
	} 
}
