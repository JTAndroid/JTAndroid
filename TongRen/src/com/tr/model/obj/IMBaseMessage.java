package com.tr.model.obj;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.tr.model.page.IPageBaseItem;

/**
 * @ClassName: IMBaseMessage.java 消息基类
 * @author xuxinjian
 * @version V1.0
 * @Date 2014-4-14 上午7:54:17
 */
public class IMBaseMessage implements IPageBaseItem {
	private static final long serialVersionUID = 19812312133322L;

	/**
	 * IOS
	 * //好友客户 17
	 *  //人脉5
	 */
	// 消息类型
	public final static int TYPE_TEXT = 0;
	public final static int TYPE_AUDIO = 1;
	public final static int TYPE_IMAGE = 2;
	public final static int TYPE_VIDEO = 3;
	public final static int TYPE_FILE = 4;
	public final static int TYPE_JTCONTACT_OFFLINE = 5; //人脉
	public final static int TYPE_KNOWLEDGE = 6;
	public final static int TYPE_REQUIREMENT = 7;
	public final static int TYPE_ORG_OFFLINE = 8;
	public final static int TYPE_JTCONTACT_ONLINE = 9;
	public final static int TYPE_ORG_ONLINE = 10;
	public final static int TYPE_KNOWLEDGE2 = 11; // 新知识
	public final static int TYPE_CONFERENCE = 12; // 会议
	public final static int TYPE_ORGANIZATION = 16; // 组织
	public final static int TYPE_CUSTOMER = 17; // 客户
	public final static int TYPE_COMMUNITY = 18; // 社群

	// 消息发送状态，谁发送的
	public static final int MSG_MY_SEND = 1;// “我”发送的消息
	public static final int MSG_SYS_SEND = 2;// 系统发送的通知消息
	public static final int MSG_OTHER_SEND = 3;// 其他人发送的通知消息

	// 群聊和私聊
	public final static int IM_TYPE_CHAT = 1; // 私聊
	public final static int IM_TYPE_MUC = 2; // 群聊
	public final static int IM_TYPE_CONF = 3; // 会议

	// 消息发送状态
	public static final int SEND_TYPE_SENT = 0; // 发送成功，或者接受成功，代表一条正常消息
	public static final int SEND_TYPE_SENDING = 1; // 发送中
	public static final int SEND_TYPE_FAIL = 2; // 发送失败
	public static final int SEND_TYPE_WAITING = 3; // 等待发送，当聊天室未创建成功时，
													// 用户点发送消息按钮，消息为该状态，
													// 当聊天室创建成功后，再发送
	public static final int SEND_TYPE_PUSH = 4; // 推送的消息

	protected int index; // 消息记录的序号， 以数据表中的id递增主键为值即可
	protected int sequence = -1; // 某个群聊内部消息记录的序号，同一畅聊中相邻的两条消息的sequence值是连续的
	protected String recvID; // 接受方id，如果是群聊，为会议id
	protected String senderID;
	protected String senderName; // 发送方名称
	protected int type; // 参考内容type，0-text；1-audio；2-image；3-video；4-file；5-JTContact(人脉）;6-knowledge(知识）;7-requirement",8-机构客户,9-机构用户(线上金桐网用户),10-个人用户(线上个人用户),11-knowledge2新知识,12-会议(conference),
	protected String content;
	protected String time;
	protected JTFile jtFile;
	protected String messageID;// 信息id，客户端生成的随机数

	protected int sendType; // 发送状态，从服务器获取的聊天记录的状态，都为 SEND_TYPE_SENT
	protected int senderType; // 发送方种类，分 我，其他人，系统发送的三种，参见 MSG_MY_SEND
	protected int imtype; // IM_TYPE_CHAT, IM_TYPE_MUC
	protected boolean hide; // 是否显示在界面,如果为false,表示显示,默认显示

	protected Date dateTime = null; // 消息发送时间

	private List<Integer> atIds; // @好友的Id

	public void setWithNewItem(IMBaseMessage msg) {
		this.index = msg.index;
		this.recvID = msg.recvID;
		this.senderID = msg.senderID;
		this.senderName = msg.senderName;
		this.type = msg.type;
		this.content = msg.content;
		this.jtFile = msg.jtFile;
		this.messageID = msg.messageID;
		this.sendType = msg.sendType;
		this.senderType = msg.senderType;
		this.imtype = msg.imtype;
		this.hide = msg.hide;
	}

	/**
	 * 将JTFile的type转为IMBaseMessage的type
	 * 
	 * @param type
	 * @return
	 */
	public static int convertJTFileType2IMBaseMessageType(int type) {
		switch (type) {
		case JTFile.TYPE_JTCONTACT_OFFLINE: // 人脉
			return IMBaseMessage.TYPE_JTCONTACT_OFFLINE;
		case JTFile.TYPE_JTCONTACT_ONLINE: // 用户
			return IMBaseMessage.TYPE_JTCONTACT_ONLINE;
		case JTFile.TYPE_ORG_OFFLINE: // 线下机构
			return IMBaseMessage.TYPE_ORG_OFFLINE;
		case JTFile.TYPE_ORG_ONLINE: // 线上机构
			return IMBaseMessage.TYPE_ORG_ONLINE;
		case JTFile.TYPE_AUDIO: // 语音
			return IMBaseMessage.TYPE_AUDIO;
		case JTFile.TYPE_IMAGE: // 图片
			return IMBaseMessage.TYPE_IMAGE;
		case JTFile.TYPE_VIDEO: // 视频
			return IMBaseMessage.TYPE_VIDEO;
		case JTFile.TYPE_FILE: // 文件
			return IMBaseMessage.TYPE_FILE;
		case JTFile.TYPE_KNOWLEDGE: // 知识
			return IMBaseMessage.TYPE_KNOWLEDGE;
		case JTFile.TYPE_KNOWLEDGE2: // 新知识
			return IMBaseMessage.TYPE_KNOWLEDGE2;
		case JTFile.TYPE_REQUIREMENT: // 需求
			return IMBaseMessage.TYPE_REQUIREMENT;
		case JTFile.TYPE_TEXT: // 文本
			return IMBaseMessage.TYPE_TEXT;
		case JTFile.TYPE_CONFERENCE: // 会议
			return IMBaseMessage.TYPE_CONFERENCE;
		case JTFile.TYPE_ORGANIZATION:
			return IMBaseMessage.TYPE_ORGANIZATION;
		case JTFile.TYPE_CLIENT:
			return IMBaseMessage.TYPE_CUSTOMER;
		case JTFile.TYPE_COMMUNITY://社群
			return IMBaseMessage.TYPE_COMMUNITY;
		default:
			return IMBaseMessage.TYPE_TEXT;
		}
	}

	public void setType(JTFile jtFile) {
		if (jtFile == null) {
			return;
		}
		switch (jtFile.mType) {
		case JTFile.TYPE_JTCONTACT_OFFLINE: // 人脉
			this.setType(IMBaseMessage.TYPE_JTCONTACT_OFFLINE);
			break;
		case JTFile.TYPE_JTCONTACT_ONLINE: // 用户
			this.setType(IMBaseMessage.TYPE_JTCONTACT_ONLINE);
			break;
		case JTFile.TYPE_ORG_OFFLINE: // 线下机构
			this.setType(IMBaseMessage.TYPE_ORG_OFFLINE);
			break;
		case JTFile.TYPE_ORG_ONLINE: // 线上机构
			this.setType(IMBaseMessage.TYPE_ORG_ONLINE);
			break;
		case JTFile.TYPE_AUDIO: // 语音
			this.setType(IMBaseMessage.TYPE_AUDIO);
			break;
		case JTFile.TYPE_IMAGE: // 图片
			this.setType(IMBaseMessage.TYPE_IMAGE);
			break;
		case JTFile.TYPE_VIDEO: // 视频
			this.setType(IMBaseMessage.TYPE_VIDEO);
			break;
		case JTFile.TYPE_FILE: // 文件
			this.setType(IMBaseMessage.TYPE_FILE);
			break;
		case JTFile.TYPE_KNOWLEDGE: // 知识
			this.setType(IMBaseMessage.TYPE_KNOWLEDGE);
			break;
		case JTFile.TYPE_KNOWLEDGE2: // 新知识
			this.setType(IMBaseMessage.TYPE_KNOWLEDGE2);
			break;
		case JTFile.TYPE_REQUIREMENT: // 需求
			this.setType(IMBaseMessage.TYPE_REQUIREMENT);
			break;
		case JTFile.TYPE_TEXT: // 文本
			this.setType(IMBaseMessage.TYPE_TEXT);
			break;
		case JTFile.TYPE_CONFERENCE: // 会议
			this.setType(IMBaseMessage.TYPE_CONFERENCE);
			break;
		case JTFile.TYPE_CLIENT:
			this.setType(IMBaseMessage.TYPE_CUSTOMER);
			break;
		default:
			break;
		}
	}

	public List<Integer> getAtIds() {
		return atIds;
	}

	public void setAtIds(List<Integer> atIds) {
		this.atIds = atIds;
	}

	public String getMessageID() {
		return messageID;
	}

	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getRecvID() {
		return recvID;
	}

	public void setRecvID(String id) {
		this.recvID = id;
	}

	public String getSenderID() {
		return senderID;
	}

	public void setSenderID(String senderID) {
		this.senderID = senderID;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public JTFile getJtFile() {
		return jtFile;
	}

	public void setJtFile(JTFile jtFile) {
		this.jtFile = jtFile;
	}

	public int getSendType() {
		return sendType;
	}

	public void setSendType(int sendType) {
		this.sendType = sendType;
	}

	public int getSenderType() {
		return senderType;
	}

	public void setSenderType(int senderType) {
		this.senderType = senderType;
	}

	public int getImtype() {
		return imtype;
	}

	public void setImtype(int imtype) {
		this.imtype = imtype;
	}

	public String getSenderName() {
		if (TextUtils.isEmpty(senderName)) {
			return "";
		}
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	@SuppressLint("SimpleDateFormat")
	public Date getDateTime() {
		if (dateTime != null)
			return dateTime;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date1 = sdf.parse(getTime());
			return date1;
		} catch (Exception e) {

		}
		return new Date();
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public boolean isHide() {
		return hide;
	}

	public void setHide(boolean hide) {
		this.hide = hide;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
}
