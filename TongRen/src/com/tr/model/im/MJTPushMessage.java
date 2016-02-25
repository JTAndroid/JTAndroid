package com.tr.model.im;

import java.io.Serializable;
import java.sql.Connection;

import org.json.JSONObject;

import com.tr.model.conference.MSociality;
import com.tr.model.conference.MSocialityDetail;
import com.tr.model.obj.ChatMessage;
import com.tr.model.obj.Connections;
import com.tr.model.obj.MUCMessage;
import com.tr.model.obj.MeetingMessage;
import com.utils.string.StringUtils;

import android.text.TextUtils;

/** @author xuxinjian
 * @E-mail: xuxinjian@gintong.com
 * @version 1.0
 * @创建时间：2014-4-11 上午10:59:28
 * @类说明 */
public class MJTPushMessage implements Serializable {

	private static final long serialVersionUID = 198009944230064422L;
	public static final int TYPE_CHAT = 1; // 私聊消息
	public static final int TYPE_MUC = 2; // 群聊消息
	public static final int TYPE_CONF = 3; // 会议消息
	public static final int TYPE_CONF_NOTI = 4; // 会议通知
	public static final int TYPE_APPLY_FRIEND = 5; // 申请加好友
	public static final int TYPE_AGREE_FRIEND = 6; // 同意加好友
	public static final int TYPE_AFFAIR = 7; // 推送事物
	public static final int TYPE_COMMUNITY = 40; // 社群聊天

	private int type;
	private String title;
	private String content;
	private String sendUserID;
	private String sendName;
	private String mucID;
	

	private String topicID;
	private String affairId;//事务id,用于区分是普通畅聊还是事务畅聊
	private boolean hasTopic; // 是否有议题
	private String orderTime; // 排序时间
	private int atFlag; // 是否被@了,0-无，1-被@

	// add by leon
	private ChatMessage chatMessage; // 私聊
	private MUCMessage mucMessage; // 群聊
	private MeetingMessage meetingMessage; // 会议
	private Connections connections; // 申请者/好友信息

	public static MJTPushMessage createFactory(String response) {

		MJTPushMessage self = null;

		if (!TextUtils.isEmpty(response) && !"null".equals(response)) {
			try {
				final JSONObject jsonObject = new JSONObject(response);
				if (jsonObject != null) {
					self = new MJTPushMessage();
					self.type = jsonObject.optInt("type");
					self.title = jsonObject.optString("title");
					self.content = jsonObject.optString("content");
					self.sendUserID = jsonObject.optString("sendUserID");
					self.sendName = jsonObject.optString("sendName");
					self.mucID = jsonObject.optString("mucID");
					self.topicID = jsonObject.optString("topicId");
					self.affairId= jsonObject.optString("affairId");
					self.hasTopic = jsonObject.optBoolean("hasTopic");
					self.orderTime = jsonObject.optString("orderTime");
					String atFlag = jsonObject.optString("atFlag");
					if (StringUtils.isEmpty(atFlag)) {
						self.atFlag = 0;
					} else {
						try {
							self.atFlag = Integer.parseInt(atFlag);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					// 新增
					if (self.type == MJTPushMessage.TYPE_CHAT) { // 私聊
						self.chatMessage = ChatMessage.createFactory(jsonObject.optJSONObject("chatMessage"));
					}
					else if (self.type == MJTPushMessage.TYPE_MUC || self.type == MJTPushMessage.TYPE_AFFAIR) { // 群聊&事务，服务端返回的数据类型一致
						self.mucMessage = MUCMessage.createFactory(jsonObject.optJSONObject("mucMessage"));
					}
					else if (self.type == MJTPushMessage.TYPE_CONF) { // 会议
						self.meetingMessage = MeetingMessage.createFactory(jsonObject.optJSONObject("mucMessage"));
					}
					else if (self.type == MJTPushMessage.TYPE_APPLY_FRIEND || self.type == MJTPushMessage.TYPE_AGREE_FRIEND) { // 申请加好友、同意加好友
						self.connections = Connections.createFactory(jsonObject.optJSONObject("connections"));
					}
				}
				return self;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSendUserID() {
		return sendUserID;
	}

	public void setSendUserID(String sendUserID) {
		this.sendUserID = sendUserID;
	}

	public String getSendName() {
		return sendName;
	}

	public void setSendName(String sendName) {
		this.sendName = sendName;
	}

	public String getMucID() {
		return mucID;
	}

	public void setMucID(String mucID) {
		this.mucID = mucID;
	}

	public ChatMessage getChatMessage() {
		return chatMessage;
	}

	public void setChatMessage(ChatMessage chatMessage) {
		this.chatMessage = chatMessage;
	}

	public MUCMessage getMucMessage() {
		return mucMessage;
	}

	public void setMucMessage(MUCMessage mucMessage) {
		this.mucMessage = mucMessage;
	}

	public MeetingMessage getMeetingMessage() {
		return meetingMessage;
	}

	public void setMeetingMessage(MeetingMessage meetingMessage) {
		this.meetingMessage = meetingMessage;
	}

	public String getTopicID() {
		return topicID;
	}

	public void setTopicID(String topicID) {
		this.topicID = topicID;
	}

	public boolean isHasTopic() {
		return hasTopic;
	}

	public void setHasTopic(boolean hasTopic) {
		this.hasTopic = hasTopic;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public Connections getConnections() {
		return connections;
	}

	public void setConnections(Connections connections) {
		this.connections = connections;
	}
	
	public String getAffairId() {
		return affairId;
	}

	public void setAffairId(String affairId) {
		this.affairId = affairId;
	}

	/** 转换为MSociality对象
	 * 
	 * @return */
	public MSociality toMSociality() {
		MSociality sociality = new MSociality();
		sociality.setSocialDetail(new MSocialityDetail());
		if (getType() == MJTPushMessage.TYPE_CHAT) { // 私聊
			sociality.setType(1); // 类型
			try {
				sociality.setId(Long.parseLong(getChatMessage().getSenderID())); // 发送者id
				sociality.getSocialDetail().setSenderID(Long.parseLong(getChatMessage().getSenderID())); // 发送者id
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			sociality.setOrderTime(getChatMessage().getTime()); // 排序时间
			sociality.setTime(getChatMessage().getTime()); // 最后更新时间
			sociality.getSocialDetail().setContent(getChatMessage().getSenderName()+":"+getChatMessage().getContent()); // 消息内容
			sociality.getSocialDetail().setSenderName(getChatMessage().getSenderName()); // 消息发送人
		}
		else if (getType() == MJTPushMessage.TYPE_MUC) { // 群聊
			sociality.setType(2); // 类型
			
			try {
				sociality.setId(Long.parseLong(getMucID())); // 群聊id
				
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			if (atFlag == 1) { // 被@的消息
				sociality.setAtMsgId(getMucMessage().getMessageID());
				sociality.setAtName(getMucMessage().getSenderName());
			}
			sociality.setOrderTime(getMucMessage().getTime()); // 排序时间
			sociality.setTime(getMucMessage().getTime()); // 最后更新时间
			sociality.getSocialDetail().setContent(getMucMessage().getSenderName()+":"+getMucMessage().getContent()); // 消息内容
			sociality.getSocialDetail().setSenderName(getMucMessage().getSenderName()); // 消息发送人
		}
		else if (getType() == MJTPushMessage.TYPE_CONF) { // 会议
			sociality.setType(3); // 类型
			try {
				sociality.setId(Long.parseLong(getMucID())); // 会议id
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			sociality.setTime(getMeetingMessage().getTime()); // 最后更新时间
		}
		else if (getType() == MJTPushMessage.TYPE_CONF_NOTI) { // 通知
			sociality.setType(6); // 类型
			sociality.setOrderTime(getOrderTime()); // 排序时间
			sociality.setTime(getOrderTime()); // 最后更新时间
			sociality.getSocialDetail().setContent(getContent()); // 消息内容
		}
		else if(getType() == MJTPushMessage.TYPE_COMMUNITY){//社群
			sociality.setType(MSociality.COMMUNITY);
		}
		return sociality;
	}
}
