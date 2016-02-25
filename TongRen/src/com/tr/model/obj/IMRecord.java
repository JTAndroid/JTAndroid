package com.tr.model.obj;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tr.App;
import com.tr.model.im.IMConsts;
import com.tr.model.page.IPageBaseItem;
import com.utils.common.JTDateUtils;

import android.text.TextUtils;
import android.text.format.DateFormat;


/**
 * @ClassName:     MIMRecord.java
 * @Description:   首页-聊天列表页一个对应的项
 * @author         xuxinjian
 * @version        V1.0  
 * @Date           2014-3-28 上午7:54:17
 */
public class IMRecord implements IPageBaseItem{
	private static final long serialVersionUID = 19800444110064422L;
	public final static int TYPE_CHAT = 1;
	public final static int TYPE_MUC  = 2;
	
	private String id;
	private String title;
	private String content;
	private String startTime;//预约会议开始时间
	private List<String> listImageUrl;
	private int type;
	private int newCount;
	private String compereName;
	private MUCMessage mucMessage;//图片链接地址
	private ChatMessage chatMessage;//图片链接地址
	
	public static IMRecord createFactory(JSONObject jsonObject) {
		try {
			IMRecord self = new IMRecord();
			self.id = jsonObject.optString("id");
			self.title = jsonObject.optString("title");
			self.content = jsonObject.optString("content");
			JSONArray arrImage = jsonObject.optJSONArray("listImageUrl");
			if (arrImage != null) {
				self.listImageUrl = new ArrayList<String>();
				for (int j = 0; j < arrImage.length(); j++) {
					String temp = arrImage.getString(j);
					self.listImageUrl.add(temp);
				}
			}
			self.type = jsonObject.optInt("type");
			self.newCount = jsonObject.optInt("newCount");
			self.compereName = jsonObject.optString("compereName");
			self.startTime = jsonObject.optString("startTime");
			if (self.type == IMConsts.MUC_TYPE)
				self.mucMessage = MUCMessage
						.createFactory((JSONObject) jsonObject
								.opt("mucMessage"));
			else
				self.chatMessage = ChatMessage
						.createFactory((JSONObject) jsonObject
								.opt("chatMessage"));

			return self;
		} catch (Exception e) {
			return null;
		}
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<String> getListImageUrl() {
		return listImageUrl;
	}
	public void setListImageUrl(List<String> listImageUrl) {
		this.listImageUrl = listImageUrl;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getNewCount() {
		return newCount;
	}
	public void setNewCount(int newCount) {
		this.newCount = newCount;
	}
	public String getCompereName() {
		return compereName;
	}
	public void setCompereName(String compereName) {
		this.compereName = compereName;
	}
	public MUCMessage getMucMessage() {
		return mucMessage;
	}
	public void setMucMessage(MUCMessage mucMessage) {
		this.mucMessage = mucMessage;
	}
	public ChatMessage getChatMessage() {
		return chatMessage;
	}
	public void setChatMessage(ChatMessage chatMessage) {
		this.chatMessage = chatMessage;
	}

	public String getContent() {
		if(TextUtils.isEmpty(getSenderName()))
			return content;
		else{
			String senderName = "";
			if(getSenderID().equalsIgnoreCase(App.getUserID())){
				senderName = "我";
			}else{
				senderName = getSenderName();
			}
			return senderName + ":" + content;
		}
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	//获取发送者用户id
	public String getSenderID() {
		if(chatMessage != null){
			return chatMessage.getSenderID();
		}else if(mucMessage != null){
			return mucMessage.getSenderID();
		}else
			return "";
	}
	
	//获取发送者昵称
	public String getSenderName() {
		if(chatMessage != null){
			return chatMessage.getSenderName();
		}else if(mucMessage != null){
			return mucMessage.getSenderName();
		}else
			return "";
	}
	
	public String getTime() {
		if(chatMessage != null){
			return chatMessage.getTime();
		}else if(mucMessage != null){
			return mucMessage.getTime();
		}else
			return "";
	}
	
	//如果会议尚未开始， 返回会议开始时间， 如果已开始或者非会议，返回null
	public String getConferenceStartTime(){
		if(!TextUtils.isEmpty(startTime)
				&& JTDateUtils.getIntervalMoreTime(startTime,""+DateFormat.format("yyyy-MM-dd kk:mm:ss", System.currentTimeMillis()))<0){
			// 开始时间非空并且会以尚未开始
			return startTime;
		}else
			return null;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
