package com.tr.model;

import java.io.Serializable;
import java.util.List;

import javax.crypto.SecretKey;

import org.json.JSONObject;

import com.tr.model.obj.JTFile;
/**
 * 发送消息
 */
public class SendMessages implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8287202482834430740L;
	/*畅聊/单聊/会议名称*/
	public String chatName;
	/*好友id(说明：此字段只在单聊的时候存储对方的用户id)*/
	public long jtContactID;
	/*会议id*/
	public long mucID;
	/*议题id*/
	public long topicId;
	/*"消息类型，参见 MUCMessage里的type定义*/
	public int type;
	/*发送的文本内容*/
	public String text;
	/*"senderName":"发送方昵称",*/
	public String senderName;
	/*消息id串，客户端随机生成，每条记录唯一*/
	public String messageID;
	/*当前聊天记录中最新的聊天记录发送时间，如果时间为null，返回最新的十条*/
	public String fromTime;
	/*本地记录索引，fromTime为空且fromIndex大于0时有效*/
//	public int fromIndex;//目前可以不用传
	/*请求记录条数（为0或为空表示查询全部）*/
	public int size;//可以不传，但后台需要数据，默认传1
	/*[@的用户id的数组]*/
//	public List<Integer> ats;//目前可以不用传
	public JTFile jtFile;//已抛弃
	
	
	public JSONObject toJson(){
		JSONObject obj = null;
		try {
			obj = new JSONObject();
			obj.put("chatName", this.chatName);
			obj.put("jtContactID", this.jtContactID);
			obj.put("mucID", this.mucID);
			obj.put("topicId", this.topicId);
			obj.put("type", this.type);
			obj.put("text", this.text);
			obj.put("senderName", this.senderName);
			obj.put("messageID", this.messageID);
			obj.put("fromTime", this.fromTime);
			obj.put("size", this.size);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return obj;
	}
}
