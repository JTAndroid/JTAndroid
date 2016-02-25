package com.tr.model.im; 

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tr.model.SendMessageForwardSocial;
import com.tr.model.knowledge.UserCategory;
import com.tr.model.obj.ChatMessage;
import com.tr.model.obj.IMBaseMessage;
import com.tr.model.obj.MUCMessage;
import com.tr.model.obj.MeetingMessage;

/** 
 * 发送消息返回结果解析类
 * @author xuxinjian/leon
 * @version 1.0
 * @创建时间：2014-04-11 10:59 
 */
public class MSendMessage implements Serializable{

	private static final String TAG = MSendMessage.class.getSimpleName();
	
    private static final long serialVersionUID = 198239941110064422L; // 序列表时的识别码
    private List<IMBaseMessage> listMessage; // 聊天记录列表，群聊、单聊、会议混合在一起
    private boolean succeed; // 是否发送成功
    private String messageID; // (没用，iOS用，咨询的刑开虎)消息id，用来识别消息发送失败时处理

	/**
	 * 解析函数
	 * @param jsonObject
	 * @return
	 */
	public static MSendMessage createFactory(JSONObject jsonObject){
		MSendMessage self = null;
		try{
			if (jsonObject != null) {
				self = new MSendMessage();
				self.succeed = true;
				// 私聊聊天记录
				JSONArray arr = jsonObject.optJSONArray("listChatMessage");
				if(arr != null){
					self.listMessage = new ArrayList<IMBaseMessage>();
					for(int i = 0; i < arr.length(); i++){
						JSONObject chatObj = arr.getJSONObject(i);
						ChatMessage cm = ChatMessage.createFactory(chatObj);
						if(cm != null){
							self.listMessage.add(cm);
						}
					}
				}
				// 群聊聊天记录
				arr = jsonObject.optJSONArray("listMUCMessage");
				if(arr != null){
					self.listMessage = new ArrayList<IMBaseMessage>();
					for(int i = 0; i < arr.length(); i++){
						JSONObject mucObj = arr.getJSONObject(i);
						MUCMessage cm = MUCMessage.createFactory(mucObj);
						if(cm != null){
							self.listMessage.add(cm);
						}
					}
				}
				// 会议聊天记录
				arr = jsonObject.optJSONArray("listMeetingMessage");
				if(arr != null){
					self.listMessage = new ArrayList<IMBaseMessage>();
					for(int i = 0; i < arr.length(); i++){
						JSONObject mucObj = arr.getJSONObject(i);
						MeetingMessage cm = MeetingMessage.createFactory(mucObj);
						if(cm != null){
							self.listMessage.add(cm);
						}
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		// 如果消息发送失败，需要将发送的消息的messageID返回，以让客户端确认是哪条消息失败
		if(self == null){
			self = new MSendMessage();
			self.succeed = false;
		}
		return self;
	}
	
	
	public List<SendMessageForwardSocial> forwardSocialsResponseDataList; // 聊天记录列表，群聊、单聊、会议混合在一起

	/**
	 * 解析函数 (多选)转发到社交
	 * @param jsonObject
	 * @return
	 */
	public static MSendMessage createFactoryForwardSocial(JSONObject jsonObject){
		Gson gson = new Gson();
		MSendMessage self = null;
		try{
			if (jsonObject != null) {
				self = new MSendMessage();
				self.succeed = true;
				// 私聊聊天记录
				self.forwardSocialsResponseDataList = new ArrayList<SendMessageForwardSocial>();
				String jsonString = jsonObject.getJSONArray("resultInfo").toString();
				self.forwardSocialsResponseDataList = gson.fromJson(jsonString, 
						new TypeToken<List<SendMessageForwardSocial>>(){}.getType());
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		// 如果消息发送失败，需要将发送的消息的messageID返回，以让客户端确认是哪条消息失败
		if(self == null){
			self = new MSendMessage();
			self.succeed = false;
		}
		return self;
	}
	
	public boolean isSucceed() {
		return succeed;
	}

	public void setSucceed(boolean succeed) {
		this.succeed = succeed;
	}

	public String getMessageID() {
		return messageID;
	}

	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}

	public List<IMBaseMessage> getListMessage() {
		return listMessage;
	}

	public void setListMessage(List<IMBaseMessage> listMessage) {
		this.listMessage = listMessage;
	}

}
 