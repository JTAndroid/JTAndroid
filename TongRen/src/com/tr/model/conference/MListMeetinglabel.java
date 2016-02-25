package com.tr.model.conference;

import java.io.Serializable;
import java.util.List;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.utils.time.Util;

public class MListMeetinglabel implements Serializable{

	/**
	 * sunjianan 20141120
	 */
	private static final long serialVersionUID = -8554572693510393894L;
	
	private List<MMeetingLabel> listMeetingLabel;
	
	/**
	 * @param jsonObject
	 * @return Meeting
	 */
	public static MListMeetinglabel createFactory(JSONObject jsonObject) {
		MListMeetinglabel label = null;
		try {
			if (jsonObject != null) {
//				label = JSON.parseObject(jsonObject.toString(), MListMeetinglabel.class);
				label = (MListMeetinglabel) Util.getParseJsonObject(jsonObject,MListMeetinglabel.class);
			}
			return label;
		} catch (Exception e) {
		}
		return null;
	}

	public List<MMeetingLabel> getListMeetingLabel() {
		return listMeetingLabel;
	}

	public void setListMeetingLabel(List<MMeetingLabel> listMeetingLabel) {
		this.listMeetingLabel = listMeetingLabel;
	}
	
	

}
