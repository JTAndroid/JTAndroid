package com.tr.model.conference;

import java.io.Serializable;
import java.util.List;
/**
 * 我的会议——返回的数据结构格式
 */
public class MeetingOfMineListQuery implements Serializable{
	
	private static final long serialVersionUID = 6108389212716093777L;
	/**会议序号*/
	private long id;
	/**会议名称*/
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
	private String createImage;
	/** 附件ID */
	private String taskId;
	/** 创建时间 */
	private String createTime;
	/** 0：会议，1：邀请函，2：通知 */
	private int type;
	/** 类型 0：会议，1：邀请函 */
	private int isSignUp;
	/** 会议封面图 */
	private String path;
	/** 处理会议的状态 0：默认，1：归档，2：删除 */
	private int memberMeetStatus;
	/**签到距离*/
	private String distance;
	/**参会时间*/
	private String attendMeetTime;
	/** 会议人脉列表 */
	private List<MMeetingPeople> listMeetingPeople;
	/**参会组织列表*/
	private List<MMeetingOrgan> listMeetingOrgan;
	/** 会议知识列表 */
	private List<MMeetingData> listMeetingKnowledge;
	/** 会议需求列表 */
	private List<MMeetingData> listMeetingRequirement;
	/** 会议笔记列表 */
	private List<MMeetingNoteQuery> listMeetingNoteQuery;
	/** 会议议题列表 */
	private List<MMeetingTopic> listMeetingTopic;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getMeetingName() {
		return nullToString(meetingName);
	}
	public void setMeetingName(String meetingName) {
		this.meetingName = nullToString(meetingName);
	}
	public String getMeetingAddress() {
		return nullToString(meetingAddress);
	}
	public void setMeetingAddress(String meetingAddress) {
		this.meetingAddress = nullToString(meetingAddress);
	}
	public String getMeetingAddressPosX() {
		return nullToString(meetingAddressPosX);
	}
	public void setMeetingAddressPosX(String meetingAddressPosX) {
		this.meetingAddressPosX = nullToString(meetingAddressPosX);
	}
	public String getMeetingAddressPosY() {
		return nullToString(meetingAddressPosY);
	}
	public void setMeetingAddressPosY(String meetingAddressPosY) {
		this.meetingAddressPosY = nullToString(meetingAddressPosY);
	}
	public long getCountry() {
		return country;
	}
	public void setCountry(long country) {
		this.country = country;
	}
	public String getProvince() {
		return nullToString(province);
	}
	public void setProvince(String province) {
		this.province = nullToString(province);
	}
	public String getCity() {
		return nullToString(city);
	}
	public void setCity(String city) {
		this.city = nullToString(city);
	}
	public String getTown() {
		return nullToString(town);
	}
	public void setTown(String town) {
		this.town = nullToString(town);
	}
	public String getStartTime() {
		return nullToString(startTime);
	}
	public void setStartTime(String startTime) {
		this.startTime = nullToString(startTime);
	}
	public String getEndTime() {
		return  nullToString(endTime);
	}
	public void setEndTime(String endTime) {
		this.endTime = nullToString(endTime);
	}
	public int getMeetingType() {
		return meetingType;
	}
	public void setMeetingType(int meetingType) {
		this.meetingType = meetingType;
	}
	public int getMeetingStatus() {
		return meetingStatus;
	}
	public void setMeetingStatus(int meetingStatus) {
		this.meetingStatus = meetingStatus;
	}
	public boolean isSecrecy() {
		return isSecrecy;
	}
	public void setSecrecy(boolean isSecrecy) {
		this.isSecrecy = isSecrecy;
	}
	public int getMemberCount() {
		return memberCount;
	}
	public void setMemberCount(int memberCount) {
		this.memberCount = memberCount;
	}
	public String getMeetingDesc() {
		return nullToString(meetingDesc);
	}
	public void setMeetingDesc(String meetingDesc) {
		this.meetingDesc = nullToString(meetingDesc);
	}
	public long getCreateId() {
		return createId;
	}
	public void setCreateId(long createId) {
		this.createId = createId;
	}
	public String getCreateName() {
		return nullToString(createName);
	}
	public void setCreateName(String createName) {
		this.createName = nullToString(createName);
	}
	public String getCreateImage() {
		return nullToString(createImage);
	}
	public void setCreateImage(String createImage) {
		this.createImage = nullToString(createImage);
	}
	public String getTaskId() {
		return nullToString(taskId);
	}
	public void setTaskId(String taskId) {
		this.taskId = nullToString(taskId);
	}
	public String getCreateTime() {
		return nullToString(createTime);
	}
	public void setCreateTime(String createTime) {
		this.createTime = nullToString(createTime);
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getIsSignUp() {
		return isSignUp;
	}
	public void setIsSignUp(int isSignUp) {
		this.isSignUp = isSignUp;
	}
	public String getPath() {
		return nullToString(path);
	}
	public void setPath(String path) {
		this.path = nullToString(path);
	}
	public int getMemberMeetStatus() {
		return memberMeetStatus;
	}
	public void setMemberMeetStatus(int memberMeetStatus) {
		this.memberMeetStatus = memberMeetStatus;
	}
	public String getDistance() {
		return nullToString(distance);
	}
	public void setDistance(String distance) {
		this.distance = nullToString(distance);
	}
	public String getAttendMeetTime() {
		return nullToString(attendMeetTime);
	}
	public void setAttendMeetTime(String attendMeetTime) {
		this.attendMeetTime = nullToString(attendMeetTime);
	}
	public List<MMeetingPeople> getListMeetingPeople() {
		return listMeetingPeople;
	}
	public void setListMeetingPeople(List<MMeetingPeople> listMeetingPeople) {
		this.listMeetingPeople = listMeetingPeople;
	}
	public List<MMeetingOrgan> getListMeetingOrgan() {
		return listMeetingOrgan;
	}
	public void setListMeetingOrgan(List<MMeetingOrgan> listMeetingOrgan) {
		this.listMeetingOrgan = listMeetingOrgan;
	}
	public List<MMeetingData> getListMeetingKnowledge() {
		return listMeetingKnowledge;
	}
	public void setListMeetingKnowledge(List<MMeetingData> listMeetingKnowledge) {
		this.listMeetingKnowledge = listMeetingKnowledge;
	}
	public List<MMeetingData> getListMeetingRequirement() {
		return listMeetingRequirement;
	}
	public void setListMeetingRequirement(List<MMeetingData> listMeetingRequirement) {
		this.listMeetingRequirement = listMeetingRequirement;
	}
	public List<MMeetingNoteQuery> getListMeetingNoteQuery() {
		return listMeetingNoteQuery;
	}
	public void setListMeetingNoteQuery(List<MMeetingNoteQuery> listMeetingNoteQuery) {
		this.listMeetingNoteQuery = listMeetingNoteQuery;
	}
	public List<MMeetingTopic> getListMeetingTopic() {
		return listMeetingTopic;
	}
	public void setListMeetingTopic(List<MMeetingTopic> listMeetingTopic) {
		this.listMeetingTopic = listMeetingTopic;
	}
	
	private String nullToString(String str){
		return str==null?"":str;
	}
	
}
