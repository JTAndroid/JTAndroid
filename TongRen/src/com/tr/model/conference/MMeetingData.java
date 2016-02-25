package com.tr.model.conference;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class MMeetingData implements Serializable {
	private static final long serialVersionUID = 5110276335640635858L;

	/**
	 * "createTime":"资料创建时间", "dataId":"资料ID", "dataName":"资料名称",
	 * "dataReqType": "1-资讯，2-投融工具，3-行业，4-经典案例，5-图书报告，6-资产管理，7-宏观，8-观点，9-判例，10-法律法规，11-文章,12-投资，13-融资"", "dataType":"资料类型 0需求，1知识",
	 * "dataUrl":"资料对应的url", "id":"资料序号", "meetingId":"会议ID",
	 * "meetingName":"会议名称"
	 * 
	 */
	private String createTime;
	private long dataId;

	private String dataName;
	private int dataReqType;

	private int dataType;
	private String dataUrl;

	private long id;
	private long meetingId;
	private String meetingName;

	public JSONObject toJSONObject() throws JSONException {
		JSONObject jObject = new JSONObject();
		jObject.put("dataName", dataName);
		jObject.put("dataId", dataId);
		jObject.put("dataType", dataType);
		jObject.put("dataReqType", dataReqType);
		jObject.put("dataUrl", dataUrl);
		
		jObject.put("createTime", createTime);
		jObject.put("id", id);
		jObject.put("meetingId", meetingId);
		jObject.put("meetingName", meetingName);
		return jObject;
	}

	public String getCreateTime() {
		if(createTime == null) 
			return "";
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public long getDataId() {
		return dataId;
	}

	public void setDataId(long dataId) {
		this.dataId = dataId;
	}

	public String getDataName() {
		if (dataName == null) {
			return "";
		}
		return dataName;
	}

	public void setDataName(String dataName) {
		this.dataName = dataName;
	}

	public int getDataReqType() {
		return dataReqType;
	}

	public void setDataReqType(int dataReqType) {
		this.dataReqType = dataReqType;
	}

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public String getDataUrl() {
		if (dataUrl == null) {
			return "";
		}
		return dataUrl;
	}

	public void setDataUrl(String dataUrl) {
		this.dataUrl = dataUrl;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getMeetingId() {
		return meetingId;
	}

	public void setMeetingId(long meetingId) {
		this.meetingId = meetingId;
	}

	public String getMeetingName() {
		if (meetingName == null) {
			return "";
		}
		return meetingName;
	}

	public void setMeetingName(String meetingName) {
		this.meetingName = meetingName;
	}

}