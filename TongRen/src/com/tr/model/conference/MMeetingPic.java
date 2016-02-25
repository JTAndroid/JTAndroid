package com.tr.model.conference;

import java.io.Serializable;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.tr.model.obj.JTFile;

public class MMeetingPic implements Serializable {

	private static final long serialVersionUID = -4661256518581639150L;
	/**图片ID*/
	private Long id;
	/**会议ID*/
	private Long meetingId;
	/**图片路径*/
	private String picPath;
	/**图片名字*/
	private String picName;
	/**图片的真实名字*/
	private String picRealName;
	/**图片描述*/
	private String picDesc;
	/**是否把此图片作为封面*/
	private Integer ishomePage;
	/**文件关联ID*/
	private Long fileIndexId;
	/**任务ID*/
	private String taskId;
	/**上传图片的用户ID*/
	private Long createUserId;
	/**上传图片的用户登录名*/
	private String createUserName;
	/**图片创建时间*/
	private String createDate;
	/**图片状态 0：待生效，1：已生效，2：屏蔽*/
	private String picStatus;
	/**图片待删除标记 1：待删除*/
	private Integer picDel;
	/**图片更新时间*/
	private String updateDate;
	/**会议/议题/笔记ID*/
	private String moduleId;
	/**模块类型 1：会议 2：议题 3：笔记*/
	private String moduleType;
	/**图片宽度*/
	private String width;
	/**图片高度*/
	private String height;
	
	
	private String createDateString;
	private String updateDateString;
	
	/**
	 * 转换为fileImage
	 * @return
	 * @throws JSONException
	 */
	public JSONObject toJSONObject() throws JSONException {
		JSONObject jObject = new JSONObject();
		jObject.put("id", id);
		jObject.put("meetingId", meetingId);
		jObject.put("picPath", picPath);
		jObject.put("picName", picName);
		jObject.put("picRealName", picRealName);
		jObject.put("picDesc", picDesc);
		jObject.put("ishomePage", ishomePage);
		jObject.put("fileIndexId", fileIndexId);
		jObject.put("createUserId", createUserId);
		jObject.put("createUserName", createUserName);
		jObject.put("createDate", createDate);
		jObject.put("picStatus", picStatus);
		jObject.put("picDel", picDel);
		jObject.put("updateDate", updateDate);
		jObject.put("createDateString", createDateString);
		jObject.put("updateDateString", updateDateString);
		jObject.put("taskId", taskId);
		jObject.put("moduleId", moduleId);
		jObject.put("moduleType", moduleType);
		jObject.put("width", width);
		jObject.put("height", height);
		return jObject;
	}

	
	public String getCreateDate() {
		if(createDate == null) 
			return "";
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public Long getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}
	public String getCreateUserName() {
		if(createUserName == null) 
			return "";
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	public long getFileIndexId() {
		if(fileIndexId == null) 
			return 0;
		return fileIndexId;
	}
	public void setFileIndexId(Long fileIndexId) {
		this.fileIndexId = fileIndexId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getIshomePage() {
		return ishomePage;
	}
	public void setIshomePage(Integer ishomePage) {
		this.ishomePage = ishomePage;
	}
	public Long getMeetingId() {
		return meetingId;
	}
	public void setMeetingId(Long meetingId) {
		this.meetingId = meetingId;
	}
	public Integer getPicDel() {
		return picDel;
	}
	public void setPicDel(Integer picDel) {
		this.picDel = picDel;
	}
	public String getPicDesc() {
		if(picDesc == null) 
			return "";
		return picDesc;
	}
	public void setPicDesc(String picDesc) {
		this.picDesc = picDesc;
	}
	public String getPicName() {
		if(picName == null) 
			return "";
		return picName;
	}
	public void setPicName(String picName) {
		this.picName = picName;
	}
	public String getPicPath() {
		if(picPath == null) 
			return "";
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	public String getPicRealName() {
		if(picRealName == null) 
			return "";
		return picRealName;
	}
	public void setPicRealName(String picRealName) {
		this.picRealName = picRealName;
	}
	public String getPicStatus() {
		if(picStatus == null) 
			return "";
		return picStatus;
	}
	public void setPicStatus(String picStatus) {
		this.picStatus = picStatus;
	}
	public String getUpdateDate() {
		if(updateDate == null) 
			return "";
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}


	public String getTaskId() {
		taskId = taskId==null?"":taskId;
		return taskId;
	}


	public void setTaskId(String taskId) {
		taskId = taskId==null?"":taskId;
		this.taskId = taskId;
	}


	public String getModuleId() {
		moduleId = moduleId==null?"":moduleId;
		return moduleId;
	}


	public void setModuleId(String moduleId) {
		moduleId = moduleId==null?"":moduleId;
		this.moduleId = moduleId;
	}


	public String getModuleType() {
		moduleType = moduleType==null?"":moduleType;
		return moduleType;
	}


	public void setModuleType(String moduleType) {
		moduleType = moduleType==null?"":moduleType;
		this.moduleType = moduleType;
	}


	public String getWidth() {
		width = width==null?"":width;
		return width;
	}


	public void setWidth(String width) {
		width = width==null?"":width;
		this.width = width;
	}


	public String getHeight() {
		height = height==null?"":height;
		return height;
	}


	public void setHeight(String height) {
		height = height==null?"":height;
		this.height = height;
	}


	public String getUpdateDateString() {
		updateDateString = updateDateString==null?"":updateDateString;
		return updateDateString;
	}


	public void setUpdateDateString(String updateDateString) {
		updateDateString = updateDateString==null?"":updateDateString;
		this.updateDateString = updateDateString;
	}


	public String getCreateDateString() {
		createDateString = createDateString==null?"":createDateString;
		return createDateString;
	}


	public void setCreateDateString(String createDateString) {
		createDateString = createDateString==null?"":createDateString;
		this.createDateString = createDateString;
	}
	
	public JTFile toJTFile(){
		JTFile jtFile= new JTFile();
		jtFile.mTaskId = taskId;
		jtFile.reserved2 = fileIndexId+"";//存入文件索引
		jtFile.reserved3 = meetingId+"";
		jtFile.mLocalFilePath = picPath;
		jtFile.mModuleType = -1;//会议
		jtFile.mUrl = picPath;
		jtFile.mType = 1;//会议中jtfile  type为2 是视频，非2为图片
		return jtFile;
	}
	
}
