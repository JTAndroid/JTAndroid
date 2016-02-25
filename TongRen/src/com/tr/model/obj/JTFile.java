package com.tr.model.obj;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.text.ParseException;
import org.json.JSONException;
import org.json.JSONObject;

import com.hp.hpl.sparta.xpath.TrueExpr;
import com.tr.model.knowledge.Knowledge2;
import com.tr.model.knowledge.KnowledgeMini2;

import android.content.Intent;

/**
 * @ClassName:     JTFile.java
 * @Description:   附件对象
 * @Author         leon
 * @Version        v 1.0  
 * @Date           2014-04-10
 * @LastEdit       2014-04-10
 */
public class JTFile implements Serializable{

	private static final long serialVersionUID = 19834212142324422L;

	public final static int TYPE_VIDEO = 0;
	public final static int TYPE_AUDIO = 1;
	public final static int TYPE_FILE  = 2;
	public final static int TYPE_IMAGE = 3;
	public final static int TYPE_OTHER = 4;
//	public final static int TYPE_WEBURL = 5;
	public final static int TYPE_JTCONTACT_OFFLINE = 5;
	public final static int TYPE_ORG_OFFLINE = 8;
	public final static int TYPE_ORG_ONLINE = 9;
	public final static int TYPE_JTCONTACT_ONLINE = 10;
	public final static int TYPE_KNOWLEDGE = 7;
	public final static int TYPE_REQUIREMENT = 11;
	public final static int TYPE_TEXT = 12; // 文本
	public final static int TYPE_KNOWLEDGE2 = 13; // 新知识
	public final static int TYPE_CONFERENCE = 14; // 会议
	public final static int TYPE_DEMAND = 15; // 需求
	public final static int TYPE_ORGANIZATION = 16; // 组织：按照用户的格式封装type16
	public final static int TYPE_CLIENT = 17; // 客户：按照用户的格式封装type17
	public final static int TYPE_COMMUNITY = 18; // 社群
	private String id = "";  // 文件id（本地使用）
	public String mUrl = ""; // 图片url
	public String mFileName = ""; // 人脉和机构title
	public String mSuffixName = ""; // 人脉公司名称 人脉和机构 第一字段
	public int mType = 0;
	/**
	 * 是否是组织 0 客户 1 用户注册组织 2 未注册的组织(或者是大数据推送过来的组织（主页底部不显示任何操作）) 
	 * [1(用户) or 2(人脉)]
	 */
	public String virtual ;
	public String mLocalFilePath = ""; // 本地存储路径（本地使用）  
	public String mScreenshotFilePath = ""; // 视频缩略图地址（本地使用） 
	public String mTaskId = ""; // id
	public long mFileSize = 0;
	public long mDownloadSize = 0; // 已下载的文件大小（本地使用）
	public long mCreateTime = 0; // 文件创建时间（本地使用）
	public String messageID = ""; //  "messageID":"消息id串，客户端随机生成，每条记录唯一",多条知识转发时传入
	public String reserved1 = ""; // 备用字段 人脉和机构第二字段
	public String reserved2 = ""; // 备用字段，视频缩略图地址，语音文件时长,社群名称
	public String reserved3 = ""; // 备用字段...在会议上传时，传入meetingId
	
	// 0:需求、1：业务需求、2：公司客户、3：公司项目、4：会员、5：名片 、6 公司名片 、7资讯、8客户、9人脉分享 、10机构
	public int mModuleType = 0; // 类型    会议默认-1
	/*true : 第三方知识转发*/
	public boolean isOnlineUrl = false;
	
	//知识附件
	public String fileName;//文件名
	public long fileSize;//文件大小
	public String url;//文件地址
	public String suffixName;//jpg,png,amr,pdf等
	public String type;//0-video,1-audio,2-file,3-image,4-other
	public String moduleType;//0:需求、1：业务需求、2：公司客户、3：公司项目、4：会员、5：名片 、6 公司名片 、7资讯、8客户、9人脉分享 、10机构
	public String taskId;//附件索引
	public boolean isUpLoad = false;// 是否上传成功（本地使用）
	public JTFile(){

	}
	
	public JSONObject toJson(){
		JSONObject obj = null;
		try {
			obj = new JSONObject();
			obj.put("id", this.id);
			obj.put("url", this.mUrl);
			obj.put("suffixName", this.mSuffixName);
			obj.put("type", this.mType);
			obj.put("fileName", this.fileName);
			obj.put("fileSize", this.mFileSize);
			obj.put("taskId", this.mTaskId);
			obj.put("moduleType", this.mModuleType);
			obj.put("messageID", this.messageID);
			obj.put("reserved1", this.reserved1);
			obj.put("reserved2", this.reserved2);
			obj.put("reserved3", this.reserved3);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return obj;
	}
	
	public static JTFile createFactory(JSONObject obj){
		JTFile self;
		try{
			self = new JTFile();
			self.initWithJson(obj);
			return self;
		}
		catch(Exception e){
			return null;
		}
	}
	
	public void initWithJson(JSONObject jsonObject) throws JSONException,
			MalformedURLException, ParseException {

		String strKey = null;
		
		// 文件id
		strKey = "id";
		if (!jsonObject.isNull(strKey)) {
			id = jsonObject.getString(strKey);
		}

		// 文件地址
		strKey = "url";
		if (!jsonObject.isNull(strKey)) {
			mUrl = jsonObject.getString(strKey);
		}
		
		// 文件名
		strKey = "fileName";
		if(!jsonObject.isNull(strKey)){
			mFileName = jsonObject.optString(strKey);
		}
		
		// 文件大小
		strKey = "fileSize";
		if(!jsonObject.isNull(strKey)){
			mFileSize = jsonObject.optLong(strKey);
		}

		// 文件后缀 
		strKey = "suffixName";
		if (!jsonObject.isNull(strKey)) {
			mSuffixName = jsonObject.getString(strKey);
		}

		// 文件类型
		strKey = "type";
		if (!jsonObject.isNull(strKey)) {
			mType = jsonObject.getInt(strKey);
		}
		
		// taskId
		strKey = "taskId";
		if(!jsonObject.isNull(strKey)){
			mTaskId = jsonObject.optString(strKey);
		}
		
		// 消息id串
		strKey = "messageID";
		if(!jsonObject.isNull(strKey)){
			messageID = jsonObject.optString(strKey);
		}
		// 保留字段1
		strKey = "reserved1";
		if(!jsonObject.isNull(strKey)){
			reserved1 = jsonObject.optString(strKey);
		}
		
		// 保留字段2
		strKey = "reserved2";
		if (!jsonObject.isNull(strKey)) {
			reserved2 = jsonObject.optString(strKey);
		}
		
		// 保留字段3
		strKey = "reserved3";
		if (!jsonObject.isNull(strKey)) {
			reserved3 = jsonObject.optString(strKey);
		}
		
		// 附件所属类型
		strKey = "moduleType";
		if(!jsonObject.isNull(strKey)){
			mModuleType = jsonObject.optInt(strKey);
		}
	}
	
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getmUrl() {
		return mUrl;
	}

	public void setmUrl(String mUrl) {
		this.mUrl = mUrl;
	}

	public String getmSuffixName() {
		return mSuffixName;
	}

	public void setmSuffixName(String mSuffixName) {
		this.mSuffixName = mSuffixName;
	}

	public int getmType() {
		return mType;
	}

	public void setmType(int mType) {
		this.mType = mType;
	}

	public String getmLocalFilePath() {
		return mLocalFilePath;
	}

	public void setmLocalFilePath(String mLocalFilePath) {
		this.mLocalFilePath = mLocalFilePath;
	}
	
	public String getMessageID() {
		return messageID;
	}

	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}
	
	public String getReserved1() {
		return reserved1;
	}
	
	public void setReserved1(String reserved1) {
		this.reserved1 = reserved1;
	}

	public String getReserved2() {
		return reserved2;
	}

	public void setReserved2(String reserved2) {
		this.reserved2 = reserved2;
	}

	public String getReserved3() {
		return reserved3;
	}

	public void setReserved3(String reserved3) {
		this.reserved3 = reserved3;
	}

	/*
	public int getmProgress() {
		return mProgress;
	}

	public void setmProgress(int mProgress) {
		this.mProgress = mProgress;
	}

	public int getmDownloadStatus() {
		return mDownloadStatus;
	}

	public void setmDownloadStatus(int mDownloadStatus) {
		this.mDownloadStatus = mDownloadStatus;
	}
	*/
	
	
	/**
	 * 创建分享对象
	 * @param intent
	 * @return
	 */
	public static JTFile createFromIntent(Intent intent) {

		JTFile jtFile = new JTFile();
		jtFile.mFileName = intent.getExtras().getString(Intent.EXTRA_TITLE);
		jtFile.mSuffixName = intent.getExtras().getString(Intent.EXTRA_TEXT);
		if (jtFile.mSuffixName != null) {
			
			int urlIndex = jtFile.mSuffixName.indexOf("http");
			
			if(urlIndex >= 0){
				String subStr = jtFile.mSuffixName.substring(urlIndex);
				jtFile.mUrl = subStr;
				// 网址结束标记
				String[] suffixSet = new String[] { " ", "\n" };
				int spaceIndex = subStr.indexOf(suffixSet[0]);
				int returnIndex = subStr.indexOf(suffixSet[1]);
				if((returnIndex >=0 && spaceIndex < returnIndex && spaceIndex >= 0) || (returnIndex < 0 && spaceIndex >= 0)){
					jtFile.mUrl = subStr.substring(0, spaceIndex);
				}
				else if((spaceIndex >= 0 && returnIndex < spaceIndex && returnIndex >= 0) || (spaceIndex < 0 && returnIndex >= 0)){
					jtFile.mUrl = subStr.substring(0, returnIndex);
				}
				jtFile.mSuffixName = jtFile.mSuffixName.substring(0, urlIndex);
				jtFile.mType = JTFile.TYPE_KNOWLEDGE;
			}
			else{
				jtFile.mUrl = ""; // 不包含链接
				jtFile.mType = JTFile.TYPE_TEXT;
			}
		}
		else{
			jtFile.mSuffixName = "";
			jtFile.mType = JTFile.TYPE_TEXT;
		}
		// jtFile.mType = JTFile.TYPE_KNOWLEDGE;
		return jtFile;
	}
	
	/**
	 * 从需求创建分享对象
	 * @param intent
	 * @return
	 */
	public static JTFile createFromRequirementMini(RequirementMini reqMini){
		if(reqMini == null){
			return null;
		}
		JTFile jtFile = new JTFile();
		jtFile.mFileName = reqMini.getmTitle();
		jtFile.mType = JTFile.TYPE_REQUIREMENT;
		jtFile.mTaskId = reqMini.getmID() + "";
		jtFile.reserved1 = reqMini.getTime();
		return jtFile;
	}
	
	/**
	 * 从关系创建分享对象
	 * @param contactMini
	 * @return
	 */
	public static JTFile createFromJTContactMini(JTContactMini contactMini){
		if(contactMini == null){
			return null;
		}
		JTFile jtFile = new JTFile();
		jtFile.mType = contactMini.isOnline ? JTFile.TYPE_JTCONTACT_ONLINE:JTFile.TYPE_JTCONTACT_OFFLINE;
		return jtFile;
	}
	
	/**
	 * 从知识创建分享对象
	 * @param knowledgeMini2
	 * @return
	 */
	public static JTFile createFromKnowledgeMini2(KnowledgeMini2 knowledgeMini2){
		if(knowledgeMini2 == null){
			return null;
		}
		JTFile jtFile = new JTFile();
		jtFile.mType = JTFile.TYPE_KNOWLEDGE2;
		jtFile.mUrl = knowledgeMini2.pic ;
		jtFile.mTaskId = knowledgeMini2.id + "";
		jtFile.mSuffixName = knowledgeMini2.desc;
		jtFile.reserved1 = knowledgeMini2.type + "";
		jtFile.reserved2 = knowledgeMini2.title;
		return jtFile;
	}
	
	
	public KnowledgeMini toKnowledgeMini(){
		KnowledgeMini knoMini = new KnowledgeMini();
		try{
			knoMini.mID = Integer.parseInt(mTaskId); // id
		}
		catch(NumberFormatException e){
			e.printStackTrace();
		}
		knoMini.mUrl = mUrl; // url
		knoMini.mTitle = mSuffixName; // title
		return knoMini;
	}
	
	public KnowledgeMini2 toKnowledgeMini2(){
		KnowledgeMini2 knoMini2 = new KnowledgeMini2();
		knoMini2.title = reserved2; // 标题
		knoMini2.desc = mSuffixName; // 内容
		return knoMini2;
	}
	
	public Knowledge2 toKnowledge2(){
		Knowledge2 kno2 = new Knowledge2();
		kno2.setTitle(reserved2); // 标题
		kno2.setContent(mSuffixName); // 内容
		return kno2;
	}
}
