package com.tr.model.obj;

import java.util.Date;
import org.json.JSONObject;
import com.tr.App;
import com.utils.common.EUtil;

/**
 * @ClassName:     ChatMessage.java 文档1.29
 * @author         xuxinjian
 * @version        V1.0  
 * @Date           2014-4-14 上午7:54:17
 */
public class ChatMessage extends IMBaseMessage{
	private static final long serialVersionUID = 19812312142324422L;
	
	public ChatMessage(String text) {
		sendType = SEND_TYPE_SENT;
		senderType = MSG_MY_SEND;
		imtype = IM_TYPE_CHAT;
		content = text;
		senderID = App.getUserID();
		type = TYPE_TEXT;
		messageID = EUtil.genMessageID();
		time = EUtil.getFormatFromDate(new Date());
		jtFile = null;
		hide = false;
	}
	
	public ChatMessage(){
		
	}
	
	public static ChatMessage createFactory(JSONObject jsonObject) {
		try {
			ChatMessage self = new ChatMessage();
			self.index = jsonObject.optInt("index");
			self.sequence = jsonObject.optInt("sequence");
			self.recvID = jsonObject.optString("recvID");
			self.senderID = jsonObject.optString("senderID");
			self.senderName = jsonObject.optString("senderName");
			self.type = jsonObject.optInt("type");
			self.content = jsonObject.optString("content");
			self.time = jsonObject.optString("time");
			self.messageID = jsonObject.optString("messageID");
			self.hide = false;
			
			self.sendType = IMBaseMessage.SEND_TYPE_SENT;
			self.imtype = IMBaseMessage.IM_TYPE_CHAT;
			if(0 == App.getUserID().compareToIgnoreCase(self.senderID)){
				self.senderType = IMBaseMessage.MSG_MY_SEND;
			}else if(self.senderID.equalsIgnoreCase("0")){
				self.senderType = IMBaseMessage.MSG_SYS_SEND;
			}else{
				self.senderType = IMBaseMessage.MSG_OTHER_SEND;
			}
			
			JSONObject fileObj = jsonObject.optJSONObject("jtFile");
			if(fileObj != null){
				self.jtFile = new JTFile();
				self.jtFile.initWithJson(fileObj);
			}
			return self;
		}catch (Exception e) {
			return null;
		}
	}
	

}
