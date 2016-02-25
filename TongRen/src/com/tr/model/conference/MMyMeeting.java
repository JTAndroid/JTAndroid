package com.tr.model.conference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
/**
 * 我的会议 数据解析
 */
public class MMyMeeting implements Serializable {
	
	private static final long serialVersionUID = -7946951259242886551L;
	private Map<String, Object> dataMap = new HashMap<String, Object>(); // 返回数据
	private String str_key = "";
	private Gson gson;
	private ArrayList<MeetingOfMineListQuery> meetinglist;

	/**
	 * @param jsonObject
	 * @return MMyMeeting我的会议
	 */
	public Map<String, Object> createFactory(JSONObject jsonObject) {
		try {
			if (jsonObject != null) {
				str_key = "total";
				if (!jsonObject.isNull(str_key)) {
					dataMap.put("total", jsonObject.getInt(str_key));
				}
				str_key = "index";
				if (!jsonObject.isNull(str_key)) {
					dataMap.put("index", jsonObject.getInt(str_key));
				}
				str_key = "listMeetingMemberListQuery";
				if (!jsonObject.isNull(str_key)) {
					JSONArray jsonArray = jsonObject.getJSONArray("listMeetingMemberListQuery");
					if (jsonArray != null && jsonArray.length() > 0) {
						meetinglist = new ArrayList<MeetingOfMineListQuery>();
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jObject = (JSONObject) jsonArray.get(i);
							if (jObject != null) {
								if (gson == null) {
									gson = new Gson();
								}
								MeetingOfMineListQuery fromJson = gson.fromJson(jObject.toString(), MeetingOfMineListQuery.class);
								meetinglist.add(fromJson);
							}
						}
					}
					dataMap.put("listMeetingMemberListQuery", meetinglist);
				}
			}
			return dataMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
