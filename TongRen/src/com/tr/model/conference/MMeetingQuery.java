package com.tr.model.conference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * 会议详情
 */
public class MMeetingQuery implements Serializable {
	private static final long serialVersionUID = -471651205286217365L;

	/** 会议ID */
	private long id;
	/** 会议名称 */
	private String meetingName;
	/** 会议地点 */
	private String meetingAddress;
	/** 会议地点横坐标 */
	private String meetingAddressPosX;
	/** 会议地点纵坐标 */
	private String meetingAddressPosY;
	/** 国家 0：国内，1：国外 */
	private long country;
	/** 省份 */
	private String province;
	/** 城市 */
	private String city;
	/** 城镇 */
	private String town;
	/** 会议开始时间 */
	private String startTime;
	/** 会议结束时间 */
	private String endTime;
	/** 会议类型 0：无主讲，1：有主讲 */
	private int meetingType;
	/** 会议状态 0：草稿，1：发起，2：会议进行中，3：会议结束 */
	private int meetingStatus;
	/** 是否保密 true：是，false：否 */
	private boolean isSecrecy;
	/** 会议人数 */
	private int memberCount;
	/** 会议介绍 */
	private String meetingDesc;
	/** 创建人ID */
	private long createId;
	/** 创建人名称 */
	private String createName;
	/** 附件ID */
	private String taskId;
	/** 创建时间 */
	private String createTime;
	/** 行业列表 */
	private List<String> listIndustry;
	/** 会议介绍音频视频+附件集合 */
	private List<JTFile2ForHY> listMeetingFile;
	/** 会议成员列表 */
	private List<MMeetingMember> listMeetingMember;
	/** 主讲人列表 */
	private List<MMeetingMember> listSpeaker;
	/** 会议介绍图片集合 */
	private List<MMeetingPic> listMeetingPic;
	/** 会议时间列表 */
	private List<MMeetingTime> listMeetingTime;
	/** 会议议题列表 */
	private List<MMeetingTopic> listMeetingTopic;
	/** 会议资料列表 */
	private List<MMeetingData> listMeetingData;
	/** 会议需求列表 */
	private List<MMeetingData> listMeetingRequirement;
	/** 会议知识列表 */
	private List<MMeetingData> listMeetingKnowledge;
	/** 会议人脉列表 */
	private List<MMeetingPeople> listMeetingPeople;
	/** 会议议题列表（详细） */
	private List<MMeetingTopicQuery> listMeetingTopicQuery;
	/** 会议报名必填信息列表 */
	private List<MMeetingSignLabel> listMeetingSignLabel;
	/** 会议笔记列表 */
	private List<MMeetingNoteQuery> listMeetingNoteQuery;
	/** 参会人数 */
	private int attendMeetingCount;
	/** 签订人数 */
	private int signInCount;
	/** 0：会议，1：邀请函，2：通知 */
	private int type;
	/** 是否签到 0：未签，1：已签 */
	private int isSign;
	/** 类型 0：会议，1：邀请函 */
	private int isSignUp;
	/** 会议封面图 */
	private String path;
	/** 处理会议的状态 0：默认，1：归档，2：删除 */
	private int memberMeetStatus;

	public List<MMeetingNoteQuery> getListMeetingNoteQuery() {
		return listMeetingNoteQuery;
	}

	public void setListMeetingNoteQuery(List<MMeetingNoteQuery> listMeetingNoteQuery) {
		this.listMeetingNoteQuery = listMeetingNoteQuery;
	}

	public int getIsSign() {
		return isSign;
	}

	public void setIsSign(int isSign) {
		this.isSign = isSign;
	}

	public int getIsSignUp() {
		return isSignUp;
	}

	public void setIsSignUp(int isSignUp) {
		this.isSignUp = isSignUp;
	}

	public List<MMeetingTopicQuery> getListMeetingTopicQuery() {
		return listMeetingTopicQuery;
	}

	public void setListMeetingTopicQuery(List<MMeetingTopicQuery> listMeetingTopicQuery) {
		this.listMeetingTopicQuery = listMeetingTopicQuery;
	}

	private List<MMeetingOrgan> listMeetingOrgan;

	public JSONObject toJSONObject() throws JSONException {
		JSONObject jObject = new JSONObject();
		jObject.put("id", id);
		jObject.put("meetingName", meetingName);
		jObject.put("meetingAddress", meetingAddress);
		jObject.put("meetingAddressPosX", meetingAddressPosX);
		jObject.put("meetingAddressPosY", meetingAddressPosY);
		jObject.put("country", country);
		jObject.put("province", province);
		jObject.put("city", city);
		jObject.put("town", town);
		jObject.put("startTime", startTime);
		jObject.put("endTime", endTime);
		jObject.put("meetingType", meetingType);
		jObject.put("meetingStatus", meetingStatus);
		jObject.put("isSecrecy", isSecrecy);
		jObject.put("memberCount", memberCount);
		jObject.put("meetingDesc", meetingDesc);
		jObject.put("createId", createId);
		jObject.put("createName", createName);
		jObject.put("taskId", taskId);
		jObject.put("createTime", createTime);
		jObject.put("attendMeetingCount", attendMeetingCount);
		jObject.put("signInCount", signInCount);
		jObject.put("type", type);

		jObject.put("isSignUp", isSignUp);
		jObject.put("memberMeetStatus", memberMeetStatus);
		jObject.put("path", path);

		if (listIndustry != null && listIndustry.size() > 0) {
			int cnt = listIndustry.size();
			JSONArray ary = new JSONArray();
			for (int i = 0; i < cnt; ++i) {
				ary.put(i, listIndustry.get(i));
			}
			jObject.put("listIndustry", ary);
		}

		if (listMeetingFile != null && listMeetingFile.size() > 0) {
			int cnt = listMeetingFile.size();
			JSONArray ary = new JSONArray();
			for (int i = 0; i < cnt; ++i) {
				JTFile2ForHY jtFile2ForHY = listMeetingFile.get(i);
				ary.put(jtFile2ForHY.toJson());
			}
			jObject.put("listMeetingFile", ary);
		}

		if (listMeetingOrgan != null && listMeetingOrgan.size() > 0) {
			int cnt = listMeetingOrgan.size();
			JSONArray ary = new JSONArray();
			for (int i = 0; i < cnt; ++i) {
				MMeetingOrgan mMeetingOrgan = listMeetingOrgan.get(i);
				ary.put(mMeetingOrgan.toJsonObj());
			}
			jObject.put("listMeetingOrgan", ary);
		}

		if (null != listMeetingData) {
			if (listMeetingData.size() > 0) {
				int cnt = listMeetingData.size();
				JSONArray ary = new JSONArray();
				for (int i = 0; i < cnt; ++i) {
					MMeetingData aData = listMeetingData.get(i);
					ary.put(aData.toJSONObject());
				}
				jObject.put("listMeetingData", ary);
			}
		}
		if (null != listMeetingMember) {
			if (listMeetingMember.size() > 0) {
				int cnt = listMeetingMember.size();
				JSONArray ary = new JSONArray();
				for (int i = 0; i < cnt; ++i) {
					MMeetingMember aData = listMeetingMember.get(i);
					ary.put(aData.toJSONObject());
				}
				jObject.put("listMeetingMember", ary);
			}
		}
		if (null != listMeetingPeople) {
			if (listMeetingPeople.size() > 0) {
				int cnt = listMeetingPeople.size();
				JSONArray ary = new JSONArray();
				for (int i = 0; i < cnt; ++i) {
					MMeetingPeople aData = listMeetingPeople.get(i);
					ary.put(aData.toJSONObject());
				}
				jObject.put("listMeetingPeople", ary);
			}
		}
		if (null != listMeetingPic) {
			if (listMeetingPic.size() > 0) {
				int cnt = listMeetingPic.size();
				JSONArray ary = new JSONArray();
				for (int i = 0; i < cnt; ++i) {
					MMeetingPic aData = listMeetingPic.get(i);
					ary.put(aData.toJSONObject());
				}
				jObject.put("listMeetingPic", ary);
			}
		}
		if (null != listMeetingTime) {
			if (listMeetingTime.size() > 0) {
				int cnt = listMeetingTime.size();
				JSONArray ary = new JSONArray();
				for (int i = 0; i < cnt; ++i) {
					MMeetingTime aData = listMeetingTime.get(i);
					ary.put(aData.toJSONObject());
				}
				jObject.put("listMeetingTime", ary);
			}
		}
		if (null != listMeetingTopic) {
			if (listMeetingTopic.size() > 0) {
				int cnt = listMeetingTopic.size();
				JSONArray ary = new JSONArray();
				for (int i = 0; i < cnt; ++i) {
					MMeetingTopic aData = listMeetingTopic.get(i);
					ary.put(aData.toJSONObject());
				}
				jObject.put("listMeetingTopic", ary);
			}
		}
		if (null != listMeetingTopicQuery) {
			if (listMeetingTopicQuery.size() > 0) {
				int cnt = listMeetingTopicQuery.size();
				JSONArray ary = new JSONArray();
				for (int i = 0; i < cnt; ++i) {
					MMeetingTopicQuery aData = listMeetingTopicQuery.get(i);
					ary.put(aData.toJSONObject());
				}
				jObject.put("listMeetingTopicQuery", ary);
			}
		}

		if (null != listMeetingRequirement) {
			if (listMeetingRequirement.size() > 0) {
				int cnt = listMeetingRequirement.size();
				JSONArray ary = new JSONArray();
				for (int i = 0; i < cnt; ++i) {
					MMeetingData aData = listMeetingRequirement.get(i);
					ary.put(aData.toJSONObject());
				}
				jObject.put("listMeetingRequirement", ary);
			}
		}
		if (null != listMeetingKnowledge) {
			if (listMeetingKnowledge.size() > 0) {
				int cnt = listMeetingKnowledge.size();
				JSONArray ary = new JSONArray();
				for (int i = 0; i < cnt; ++i) {
					MMeetingData aData = listMeetingKnowledge.get(i);
					ary.put(aData.toJSONObject());
				}
				jObject.put("listMeetingKnowledge", ary);
			}
		}
		if (null != listMeetingSignLabel) {
			if (listMeetingSignLabel.size() > 0) {
				int cnt = listMeetingSignLabel.size();
				JSONArray ary = new JSONArray();
				for (int i = 0; i < cnt; ++i) {
					MMeetingSignLabel aData = listMeetingSignLabel.get(i);
					ary.put(aData.toJSONObject());
				}
				jObject.put("listMeetingSignLabel", ary);
			}
		}
		if (null != listSpeaker) {
			if (listSpeaker.size() > 0) {
				int cnt = listSpeaker.size();
				JSONArray ary = new JSONArray();
				for (int i = 0; i < cnt; ++i) {
					MMeetingMember aData = listSpeaker.get(i);
					ary.put(aData.toJSONObject());
				}
				jObject.put("listSpeaker", ary);
			}
		}
		return jObject;
	}

	public List<String> getListIndustry() {
		return listIndustry;
	}

	public void setListIndustry(List<String> listIndustry) {
		this.listIndustry = listIndustry;
	}

	public List<JTFile2ForHY> getListMeetingFile() {
		if (listMeetingFile==null) {
			listMeetingFile = new ArrayList<JTFile2ForHY>();
		}
		return listMeetingFile;
	}

	public void setListMeetingFile(List<JTFile2ForHY> listMeetingFile) {
		this.listMeetingFile = listMeetingFile;
	}

	public List<MMeetingOrgan> getListMeetingOrgan() {
		return listMeetingOrgan;
	}

	public void setListMeetingOrgan(List<MMeetingOrgan> listMeetingOrgan) {
		this.listMeetingOrgan = listMeetingOrgan;
	}

	public void setSecrecy(boolean isSecrecy) {
		this.isSecrecy = isSecrecy;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCreateId() {
		return createId;
	}

	public void setCreateId(long createId) {
		this.createId = createId;
	}

	public String getCreateName() {
		if (createName == null)
			return "";
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getCreateTime() {
		if (createTime == null)
			return "";
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getEndTime() {
		if (endTime == null)
			return "";
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public boolean getIsSecrecy() {
		return isSecrecy;
	}

	public void setIsSecrecy(boolean isSecrecy) {
		this.isSecrecy = isSecrecy;
	}

	public String getMeetingAddress() {
		if (meetingAddress == null)
			return "";
		return meetingAddress;
	}

	public void setMeetingAddress(String meetingAddress) {
		this.meetingAddress = meetingAddress;
	}

	public String getMeetingAddressPosX() {
		if (meetingAddressPosX == null) {
			return "0";
		}
		return meetingAddressPosX;
	}

	public void setMeetingAddressPosX(String meetingAddressPosX) {
		this.meetingAddressPosX = meetingAddressPosX;
	}

	public String getMeetingAddressPosY() {
		if (meetingAddressPosY == null) {
			return "0";
		}
		return meetingAddressPosY;
	}

	public void setMeetingAddressPosY(String meetingAddressPosY) {
		this.meetingAddressPosY = meetingAddressPosY;
	}

	public long getCountry() {
		return country;
	}

	public void setCountry(long country) {
		this.country = country;
	}

	public String getProvince() {
		if (province == null)
			return "";
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		if (city == null)
			return "";
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTown() {
		if (town == null)
			return "";
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getMeetingDesc() {
		if (meetingDesc == null)
			return "";
		return meetingDesc;
	}

	public void setMeetingDesc(String meetingDesc) {
		this.meetingDesc = meetingDesc;
	}

	public String getMeetingName() {
		if (meetingName == null)
			return "";
		return meetingName;
	}

	public void setMeetingName(String meetingName) {
		this.meetingName = meetingName;
	}
	/** 会议状态 0：草稿，1：发起，2：会议进行中，3：会议结束 */
	public int getMeetingStatus() {
		return meetingStatus;
	}
	/** 会议状态 0：草稿，1：发起，2：会议进行中，3：会议结束 */
	public void setMeetingStatus(int meetingStatus) {
		this.meetingStatus = meetingStatus;
	}

	public int getMeetingType() {
		return meetingType;
	}

	public void setMeetingType(int meetingType) {
		this.meetingType = meetingType;
	}

	public int getMemberCount() {
		return memberCount;
	}

	public void setMemberCount(int memberCount) {
		this.memberCount = memberCount;
	}

	public int getMemberMeetStatus() {
		return memberMeetStatus;
	}

	public void setMemberMeetStatus(int memberMeetStatus) {
		this.memberMeetStatus = memberMeetStatus;
	}

	public String getPath() {
		if (path == null)
			return "";
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getStartTime() {
		if (startTime == null)
			return "";
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getTaskId() {
		if (taskId == null)
			return "";
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getAttendMeetingCount() {
		return attendMeetingCount;
	}

	public void setAttendMeetingCount(int attendMeetingCount) {
		this.attendMeetingCount = attendMeetingCount;
	}

	public int getSignInCount() {
		return signInCount;
	}

	public void setSignInCount(int signInCount) {
		this.signInCount = signInCount;
	}

	public List<MMeetingData> getListMeetingData() {
		return listMeetingData;
	}

	public void setListMeetingData(List<MMeetingData> listMeetingData) {
		this.listMeetingData = listMeetingData;
	}

	public List<MMeetingMember> getListMeetingMember() {
		return listMeetingMember;
	}

	public void setListMeetingMember(List<MMeetingMember> listMeetingMember) {
		this.listMeetingMember = listMeetingMember;
	}

	public List<MMeetingPeople> getListMeetingPeople() {
		return listMeetingPeople;
	}

	public void setListMeetingPeople(List<MMeetingPeople> listMeetingPeople) {
		this.listMeetingPeople = listMeetingPeople;
	}

	public List<MMeetingPic> getListMeetingPic() {
		if (listMeetingPic==null) {
			listMeetingPic = new ArrayList<MMeetingPic>();
		}
		return listMeetingPic;
	}

	public void setListMeetingPic(List<MMeetingPic> listMeetingPic) {
		this.listMeetingPic = listMeetingPic;
	}

	public List<MMeetingTime> getListMeetingTime() {
		return listMeetingTime;
	}

	public void setListMeetingTime(List<MMeetingTime> listMeetingTime) {
		this.listMeetingTime = listMeetingTime;
	}

	public List<MMeetingTopic> getListMeetingTopic() {
		return listMeetingTopic;
	}

	public void setListMeetingTopic(List<MMeetingTopic> listMeetingTopic) {
		this.listMeetingTopic = listMeetingTopic;
	}

	public List<MMeetingData> getListMeetingRequirement() {
		return listMeetingRequirement;
	}

	public void setListMeetingRequirement(List<MMeetingData> listMeetingRequirement) {
		this.listMeetingRequirement = listMeetingRequirement;
	}

	public List<MMeetingData> getListMeetingKnowledge() {
		return listMeetingKnowledge;
	}

	public void setListMeetingKnowledge(List<MMeetingData> listMeetingKnowledge) {
		this.listMeetingKnowledge = listMeetingKnowledge;
	}

	public List<MMeetingSignLabel> getListMeetingSignLabel() {
		return listMeetingSignLabel;
	}

	public void setListMeetingSignLabel(List<MMeetingSignLabel> listMeetingSignLabel) {
		this.listMeetingSignLabel = listMeetingSignLabel;
	}

	public List<MMeetingMember> getListSpeaker() {
		return listSpeaker;
	}

	public void setListSpeaker(List<MMeetingMember> listSpeaker) {
		this.listSpeaker = listSpeaker;
	}

	// 根据与会者id返回与会者对象
	public MMeetingMember getMeetingMemberByUserId(String userID) {
		for (MMeetingMember member : listMeetingMember) {
			if ((member.getMemberId() + "").equalsIgnoreCase(userID)) {
				return member;
			}
		}
		return null;
	}

}
