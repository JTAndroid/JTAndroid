package com.tr.model.conference;

import java.io.Serializable;
import java.util.List;

import org.json.JSONObject;

import com.google.gson.Gson;

public class ConferenceAndChat implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -839933877470231429L;

	private List<MeetingVo> meetingVos;

	public List<MeetingVo> getMeetingVos() {
		return meetingVos;
	}

	public void setMeetingVos(List<MeetingVo> meetingVos) {
		this.meetingVos = meetingVos;
	}

	public static Object createFactory(JSONObject jsonObject) {
		ConferenceAndChat query = null;
		try {
			if (jsonObject != null) {
//				query = JSON.parseObject(jsonObject.toString(), ConferenceAndChat.class);
				Gson gson = new Gson();
				//这里将javabean转化成json字符串
				String jsonString = gson.toJson(jsonObject);
				System.out.println(jsonString);
				//这里将json字符串转化成javabean对象,
				query = gson.fromJson(jsonString,ConferenceAndChat.class);
			}
			return query;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
