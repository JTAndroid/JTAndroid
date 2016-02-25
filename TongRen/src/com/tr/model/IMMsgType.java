
package com.tr.model;



import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 消息类型，消息类别、状态等
 * 
 * @author darisk
 */
public class IMMsgType extends IMMsgTypeBase implements Serializable,Parcelable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -408308930915457793L;
	
	
	private int    mType;
	private String mBody;
	private String mSubject;
	private String mTo;
	private String mThread;
	
	/**
	 * 0:接受 1：发送 2： 系统
	 */
	public int msgType = 0;//表示消息是自己发送的， 还是接受的（别人发送的）， 还是系统发送的通知消息
	public int msgContentType=0;
	private int readType = 0;
	public  int mSendType = 1;
	private String mPacketID;//包id
	private static int msgIndex = 0;
	private int mode = 0;

	public static final int MSG_TYPE_NORMAL = 100;
	public static final int MSG_TYPE_CHAT = 200;
	public static final int MSG_TYPE_GROUP_CHAT = 300;
	public static final int MSG_TYPE_ERROR = 400;
	public static final int MSG_TYPE_INFO = 500;
	
	/*接收*/
	public static final int MSG_TYPE_RECEIVE =0;//接收的消息， 别人发送的消息
	/*发送*/
	public static final int MSG_TYPE_SEND =1;//“我”发送的消息
	public static final int MSG_TYPE_SYSTEM =2;//系统发送的通知消息
	

	public static final int  MSG_CONTENT_AUDIO=1;
	public static final int  MSG_CONTENT_TEXT=2;
	
	
	
	/*已读*/
	public static final int MSG_TYPE_READ =1;
	/*未读*/
	public static final int MSG_TYPE_UNREAD =0;
	
	//消息发送状态
	public static final int MSG_SEND_TYPE_SENT 		= 0;//发送成功，或者接受成功，代表一条正常消息
	public static final int MSG_SEND_TYPE_SENDING 	= 1;//发送中
	public static final int MSG_SEND_TYPE_FAIL		= 2;//发送失败
	public static final int MSG_SEND_TYPE_WAITING		= 3;//等待发送，当聊天室未创建成功时， 用户点发送消息按钮， 消息为该状态， 当聊天室创建成功后，再发送
	
	/*单人模式*/
	public static final int MSG_MODE_CHAT=0	;
	/*多人模式*/
	public static final int MSG_MODE_MUC=1;

//	private static final long serialVersionUID =1L;
	

	public static final Parcelable.Creator<IMMsgType> CREATOR = new Parcelable.Creator<IMMsgType>() {

		@Override
		public IMMsgType createFromParcel(Parcel source) {
			return new IMMsgType(source);
		}

		@Override
		public IMMsgType[] newArray(int size) {
			return new IMMsgType[size];
		}
	};
	
	

	
	

	public IMMsgType(final String to, final int type) {
		mTo = to;
		mType = type;
		mBody = "";
		mSubject = "";
		mThread = "";

		mSendType = MSG_SEND_TYPE_SENDING;
		mPacketID = "";
	}

	public IMMsgType(final String to) {
		this(to, MSG_TYPE_CHAT);
	}




	private IMMsgType(final Parcel in) {

		msgType = in.readInt();
		mType = in.readInt();
		mTo = in.readString();
		mBody = in.readString();
		mSubject = in.readString();
		mThread = in.readString();
		readType = in.readInt();
		mPacketID = in.readString();
		mSendType = in.readInt();
		mode = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(msgType);
		dest.writeInt(mType);
		dest.writeString(mTo);
		dest.writeString(mBody);
		dest.writeString(mSubject);
		dest.writeString(mThread);
		dest.writeInt(readType);
		dest.writeString(mPacketID);
		dest.writeInt(mSendType);
		dest.writeInt(mode);
	}
	
	
	
	public int getMode() {
		return mode;
	}




	public void setMode(int mode) {
		this.mode = mode;
	}

	public int getReadType() {
		return readType;
	}

	public void setReadType(int readType) {
		this.readType = readType;
	}



	public int getmSendType() {
		return mSendType;
	}




	public void setmSendType(int mSendType) {
		this.mSendType = mSendType;
	}




	public int getMsgType() {
		/*
		if(mFrom==null||IMUtils.getKey(mFrom).equals(IMService.username)){
			
			return MSG_TYPE_SEND;	
		}else{
			return MSG_TYPE_RECEIVE;	
		}
		*/
		return this.msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}



	public int getType() {
		return mType;
	}

	public void setType(int type) {
		mType = type;
	}

	public String getBody() {
		return mBody;
	}

	public void setBody(String body) {
		mBody = body;
	}

	public String getSubject() {
		return mSubject;
	}

	public void setSubject(String subject) {
		mSubject = subject;
	}

	public String getTo() {
		return mTo;
	}

	public void setTo(String to) {
		mTo = to;
	}



	public String getThread() {
		return mThread;
	}

	public void setThread(String thread) {
		mThread = thread;
	}
	
	public void setmPacketID(String packetID) {
		this.mPacketID = packetID;
	}
	
	public String getmPacketID()
	{
		return mPacketID;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getMsgContentType(){
		return msgContentType;
	}
	public void setMsgContentType(int msgContentType){
		this.msgContentType=msgContentType;
	}
	
}
