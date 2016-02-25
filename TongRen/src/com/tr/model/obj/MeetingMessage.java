package com.tr.model.obj;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.json.JSONObject;

import android.util.Log;

import com.tr.App;

/**
 * 会议消息对象
 * @author leon
 */
public class MeetingMessage extends MUCMessage {

	private static final String TAG = MeetingMessage.class.getSimpleName();
	private static final long serialVersionUID = 1L;

	private String topicID = "";
	
	public MeetingMessage(){
		imtype = IM_TYPE_CONF;
	}
	
	public MeetingMessage(String message){
		super(message);
		imtype = IM_TYPE_CONF;
	}

	public String getTopicID() {
		return topicID;
	}

	public void setTopicID(String topicID) {
		this.topicID = topicID;
	}
	
	public static MeetingMessage createFactory(JSONObject jsonObject) {
		try {
			MeetingMessage self = new MeetingMessage();
			self.index = jsonObject.optInt("index");
			self.recvID = jsonObject.optString("id");
			self.senderID = jsonObject.optString("senderID");
			self.senderName = jsonObject.optString("senderName");
			self.type = jsonObject.optInt("type");
			self.content = jsonObject.optString("content");
			self.time = jsonObject.optString("time");
			self.messageID = jsonObject.optString("messageID");
			self.hide = false;
			JSONObject fileObj = jsonObject.optJSONObject("jtFile");
			if(fileObj != null){
				self.jtFile = new JTFile();
				self.jtFile.initWithJson(fileObj);
			}
			self.sendType = IMBaseMessage.SEND_TYPE_SENT;
			self.imtype = IMBaseMessage.IM_TYPE_MUC;
			if(App.getUserID().equalsIgnoreCase(self.senderID)){
				self.senderType = IMBaseMessage.MSG_MY_SEND;
			}
			else if(self.senderID.equalsIgnoreCase("0")){
				self.senderType = IMBaseMessage.MSG_SYS_SEND;
			}
			else{
				self.senderType = IMBaseMessage.MSG_OTHER_SEND;
			}
			self.topicID = jsonObject.optString("topicId");
			return self;
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public static MeetingMessage createFactory(MUCMessage mucMsg) {
		MeetingMessage metMsg = null;
		try {
			metMsg = new MeetingMessage();
			Class fatherClass = mucMsg.getClass();
			Field fields[] = fatherClass.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i]; // 取出每一个属性，如deleteDate
				Class type = field.getType();
				Method method = fatherClass.getMethod("get"
						+ upperHeadChar(field.getName()));// 方法getDeleteDate
				Object obj = method.invoke(mucMsg);// 取出属性值
				field.set(metMsg, obj);
			}
		} 
		catch (Exception e) {
			Log.d(TAG, e.getMessage());
		}
		return metMsg;
	}
	
	private static String upperHeadChar(String in) {
		String head = in.substring(0, 1);
		String out = head.toUpperCase() + in.substring(1, in.length());
		return out;
	}
	
	/**
	 * 根据dialog的位置获取到会议消息对应的操作类型
	 * @param which
	 * @return 0-复制文字；1-转发；2-转发/分享；3-收藏知识；4-关注需求；5-保存知识；6-删除；7-对图片、视频、音频、文件操作
	 */
	public int getMeetingMessageOperationTypeByDialogPosition (int which){
		
		int operationType = -1;
		if (this.getType() == IMBaseMessage.TYPE_TEXT) { // 对文本操作
			if (which == 0) {
				operationType = 0;
			} else if (which == 1) {
				operationType = 1;
			} else if (which == 2) {
				operationType = 6;
			}
		} else if (this.getType() == IMBaseMessage.TYPE_IMAGE || this.getType() == IMBaseMessage.TYPE_VIDEO
				|| this.getType() == IMBaseMessage.TYPE_FILE || this.getType() == IMBaseMessage.TYPE_AUDIO) { // 对图片、视频、音频、文件操作
			if (which == 0) {
				operationType = 1;
			} else if (which == 1) {
				operationType = 6;
			}else if(which == 2){
				operationType = 7;
			}
		} else if (this.getType() == IMBaseMessage.TYPE_KNOWLEDGE
				|| this.getType() == IMBaseMessage.TYPE_KNOWLEDGE2) { // 对知识操作
			if (which == 0) {
				operationType = 3;
			} else if (which == 1) {
				operationType = 2;
			} else if (which == 2) {
				operationType = 5;
			} else if (which == 3) {
				operationType = 6;
			}
		} else if (this.getType() == IMBaseMessage.TYPE_REQUIREMENT) { // 对需求操作
			if (which == 0) {
				operationType = 5;
			} else if (which == 1) {
				operationType = 2;
			} else if (which == 2) {
				operationType = 6;
			}
		} else if (this.getType() == IMBaseMessage.TYPE_JTCONTACT_ONLINE
				|| this.getType() == IMBaseMessage.TYPE_JTCONTACT_OFFLINE
				|| this.getType() == IMBaseMessage.TYPE_ORG_ONLINE
				|| this.getType() == IMBaseMessage.TYPE_ORG_OFFLINE) { // 对关系操作
			if (which == 0) {
				operationType = 2;
			} else if (which == 1) {
				operationType = 6;
			}
		} else if (this.getType() == IMBaseMessage.TYPE_CONFERENCE) { // 对会议操作
			if (which == 0) {
				operationType = 1;
			} else if (which == 1) {
				operationType = 6;
			}
		}else if (this.getType() == IMBaseMessage.TYPE_COMMUNITY) { // 对社群操作
			if (which == 0) {
				operationType = 1;
			} else if (which == 1) {
				operationType = 6;
			}
		}
		return operationType;
	}
}
