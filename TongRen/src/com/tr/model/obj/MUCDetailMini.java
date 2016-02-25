package com.tr.model.obj;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * @ClassName:     MUCDetailMini.java 文档1.42
 * @author         xuxinjian
 * @version        V1.0  
 * @Date           2014-4-14 上午7:54:17
 * @description    会议详情信息精简
 */
public class MUCDetailMini implements Serializable{
	private static final long serialVersionUID = 198155222211124422L;
	
	private List<String> 		listImage = new ArrayList<String>();
	private int 		id;//会议id
	private String 		title;
	private String  	subject;
	private boolean		isConference;

	public static MUCDetailMini createFactory(JSONObject jsonObject) {
		try {
			MUCDetailMini self = new MUCDetailMini();
			self.id = jsonObject.optInt("id");
			self.title = jsonObject.optString("title");
			self.subject = jsonObject.optString("subject");
			self.isConference = jsonObject.optBoolean("isConference");
			
			if(jsonObject.has("listImage")){
			JSONArray array = jsonObject.getJSONArray("listImage");
			if(array != null ){
				for(int i = 0; i < array.length(); i++){
					String image	 = array.getString(i);
					self.listImage.add(image);
				}
			}
			}
			
			return self;
		}catch (Exception e) {
			return null;
		}
	}

	public List<String> getListImage() {
		return listImage;
	}

	public void setListImage(List<String> listImage) {
		this.listImage = listImage;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public boolean isConference() {
		return isConference;
	}

	public void setConference(boolean isConference) {
		this.isConference = isConference;
	}

}
