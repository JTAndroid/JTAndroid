package com.tr.model.conference;

import java.io.Serializable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
/**
 * 议题对象列表（详细信息）
 */
public class MMeetingTopicQuery implements Serializable{

	private static final long serialVersionUID = 9222553783827639367L;
	/**议题ID*/
	private long id;
	/**会议ID*/
	private long meetingId;
	/**议题名称*/
	private String topicContent;
	/**议题开始时间*/
	private String topicStartTime;
	/**议题结束时间*/
	private String topicEndTime;
	/**议题介绍*/
	private String topicDesc;
	/**附件ID*/
	private String taskId;
	/**主讲人ID*/
	private long memberId;
	/**主讲人姓名*/
	private String memberName;
	/**主讲人头像*/
	private String memberPic;
	/**主讲人描述*/
	private String memberDesc;
	/**创建人ID*/
	private long createId;
	/**创建人名字*/
	private String createName;
	/**创建时间*/
	private String createTime;
	/**更新时间*/
	private String updateTime;
	/**人员头像*/
	private String memberImage;
	/**议题封面图片*/
	private String path;
	/**议题图片集合*/
	private List<MMeetingPic> listMeetingPic;
	/**议题音频视频附件集合*/
	private List<JTFile2ForHY> listMeetingFile;
	/**议题聊天内容*/
	private List<MTopicChat> listTopicChat;
	
	//增加字段 "isFinished":"是否已结束 0：未结束 1：已结束",
	
	private int isFinished;
	
	/**旧版会议中字段，保留*/
	private List<JTFile2ForHY> listJTFile;
	
	//TODO

	public JSONObject toJSONObject() throws JSONException {
		JSONObject jObject = new JSONObject();
		jObject.put("id", id);
		jObject.put("meetingId", meetingId);
		jObject.put("topicContent", topicContent);
		jObject.put("topicStartTime", topicStartTime);
		jObject.put("topicEndTime", topicEndTime);
		jObject.put("topicDesc", topicDesc);
		jObject.put("taskId", taskId);
		jObject.put("memberId", memberId);
		jObject.put("memberName", memberName);
		jObject.put("memberPic", memberPic);
		jObject.put("memberDesc", memberDesc);
		jObject.put("createId", createId);
		jObject.put("createName", createName);
		jObject.put("createTime", createTime);
		jObject.put("updateTime", updateTime);
		jObject.put("memberImage", memberImage);
		jObject.put("path", path);
		jObject.put("isFinished", isFinished);
		
		if (null != listMeetingPic) {
			if (listMeetingPic.size() > 0) {
				int cnt = listMeetingPic.size();
				JSONArray ary = new JSONArray();
				for (int i = 0; i < cnt; ++i) {
					MMeetingPic mMeetingPic = listMeetingPic.get(i);
					ary.put(mMeetingPic.toJSONObject());
				}
				jObject.put("listMeetingPic", ary);
			}
		}
		
		if (null != listMeetingFile) {
			if (listMeetingFile.size() > 0) {
				int cnt = listMeetingFile.size();
				JSONArray ary = new JSONArray();
				for (int i = 0; i < cnt; ++i) {
					JTFile2ForHY jtFile2ForHY = listMeetingFile.get(i);
					ary.put(jtFile2ForHY.toJson());
				}
				jObject.put("listMeetingFile", ary);
			}
		}
		
		if (null != listTopicChat) {
			if (listTopicChat.size() > 0) {
				int cnt = listTopicChat.size();
				JSONArray ary = new JSONArray();
				for (int i = 0; i < cnt; ++i) {
					MTopicChat aData = listTopicChat.get(i);
					ary.put(aData.toJSONObject());
				}
				jObject.put("listTopicChat", ary);
			}
		}
		return jObject;
	}
	
	
	
	public int getIsFinished() {
		return isFinished;
	}



	public void setIsFinished(int isFinished) {
		this.isFinished = isFinished;
	}



	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<MMeetingPic> getListMeetingPic() {
		return listMeetingPic;
	}

	public void setListMeetingPic(List<MMeetingPic> listMeetingPic) {
		this.listMeetingPic = listMeetingPic;
	}

	public List<JTFile2ForHY> getListMeetingFile() {
		return listMeetingFile;
	}

	public void setListMeetingFile(List<JTFile2ForHY> listMeetingFile) {
		this.listMeetingFile = listMeetingFile;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public List<MTopicChat> getListTopicChat() {
		return listTopicChat;
	}

	public void setListTopicChat(List<MTopicChat> listTopicChat) {
		this.listTopicChat = listTopicChat;
	}

	public String getMemberPic() {
		return memberPic;
	}

	public void setMemberPic(String memberPic) {
		this.memberPic = memberPic;
	}

	public long getMeetingId() {
		return meetingId;
	}

	public void setMeetingId(long meetingId) {
		this.meetingId = meetingId;
	}

	public long getMemberId() {
		return memberId;
	}

	public void setMemberId(long memberId) {
		this.memberId = memberId;
	}

	public String getTaskId() {
		if(taskId == null) 
			return "";
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTopicContent() {
		if(topicContent == null) 
			return "";
		return topicContent;
	}

	public void setTopicContent(String topicContent) {
		this.topicContent = topicContent;
	}

	public String getTopicDesc() {
		if(topicDesc == null) 
			return "";
		return topicDesc;
	}

	public void setTopicDesc(String topicDesc) {
		this.topicDesc = topicDesc;
	}

	public String getTopicEndTime() {
		if(topicEndTime == null) {
			return "";
		}	
		return topicEndTime;
	}

	public void setTopicEndTime(String topicEndTime) {
		this.topicEndTime = topicEndTime;
	}

	public String getTopicStartTime() {
		if(topicStartTime == null) {
			return "";
		}	
		return topicStartTime;
	}

	public void setTopicStartTime(String topicStartTime) {
		this.topicStartTime = topicStartTime;
	}

	public String getMemberImage() {
		if(memberImage == null) 
			return "";
		return memberImage;
	}

	public void setMemberImage(String memberImage) {
		this.memberImage = memberImage;
	}

	public String getMemberName() {
		if(memberName == null) 
			return "";
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberDesc() {
		if(memberDesc == null) 
			return "";
		return memberDesc;
	}

	public void setMemberDesc(String memberDesc) {
		this.memberDesc = memberDesc;
	}

	public String getMeberPic() {
		if(memberPic == null) 
			return "";
		return memberPic;
	}

	public void setMeberPic(String meberPic) {
		this.memberPic = meberPic;
	}

	public long getCreateId() {
		return createId;
	}

	public void setCreateId(long createId) {
		this.createId = createId;
	}

	public String getCreateName() {
		if(createName == null) 
			return "";
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getCreateTime() {
		if(createTime == null) 
			return "";
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		if(updateTime == null) 
			return "";
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public List<JTFile2ForHY> getListJTFile() {
		return listJTFile;
	}

	public void setListJTFile(List<JTFile2ForHY> listJTFile) {
		this.listJTFile = listJTFile;
	}
	
}
