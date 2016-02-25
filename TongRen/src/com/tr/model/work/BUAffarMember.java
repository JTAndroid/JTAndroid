package com.tr.model.work;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BUAffarMember implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1154905699680989875L;
	public String affairId;
	public long memeberId=0;
	public String channelId="";     //手机号码
	public String type ;			//"成员类型，c:创建者，o：负责人，m：成员"
	
	public String name;
	public String picUrl="";
	public List<BUPhone> phones;
	public String deviceType;   //负责人类型，1是好友人脉，2是组织好友
	
	public BUAffarMember()
	{
		phones = new ArrayList<BUPhone>();
		
	}
	
	public String getAffairId() {
		return affairId;
	}
	public void setAffairId(String affairId) {
		this.affairId = affairId;
	}
	public long getMemeberId() {
		return memeberId;
	}
	public void setMemeberId(long memeberId) {
		this.memeberId = memeberId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHeadPic() {
		return picUrl;
	}
	public void setHeadPic(String headPic) {
		this.picUrl = headPic;
	}
	
}
