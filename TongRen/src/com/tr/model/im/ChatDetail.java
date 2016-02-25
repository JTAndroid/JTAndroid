package com.tr.model.im; 

import java.io.Serializable;

import com.tr.model.obj.ConnectionsMini;

/** 
 * @author xuxinjian
 * @E-mail: xuxinjian@gintong.com
 * @version 1.0
 * @创建时间：2014-4-24 上午10:41:40 
 * @类说明 聊天对象， 目前就存放聊天对方的名称、头像和用户id
 */
public class ChatDetail implements Serializable{
	
	private static final long serialVersionUID = 198299121111064422L;
	
	private String thatID;//对方id
	private String thatName;//对方名称
	private String thatImage;//对方头像
	private int type; // 对方类型：0-个人，1-组织
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getThatID() {
		return thatID;
	}
	public void setThatID(String thatID) {
		this.thatID = thatID;
	}
	public String getThatName() {
		return thatName;
	}
	public void setThatName(String thatName) {
		this.thatName = thatName;
	}
	public String getThatImage() {
		if(thatImage == null){
			return "";
		}
		else{
			return thatImage;
		}
	}
	public void setThatImage(String thatImage) {
		this.thatImage = thatImage;
	}
	
	/**
	 * 转换成ConnectionsMini
	 * @return
	 */
	public ConnectionsMini toConnectionsMini(){
		ConnectionsMini connsMini = new ConnectionsMini();
		connsMini.setName(thatName);
		connsMini.setId(thatID);
		connsMini.setType(type);
		connsMini.setImage(thatImage);
		return connsMini;
	}
}
 