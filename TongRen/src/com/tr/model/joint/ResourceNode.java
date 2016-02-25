package com.tr.model.joint;

import java.io.Serializable;


// 对接资源组
public abstract class ResourceNode implements Serializable{

	private static final long serialVersionUID = 1L;
	
	// 栏目名称
	protected String memo;
	protected String type;
	
	

	//	protected int type; // 1-事件;2-人脉;3-组织;4-知识
//	
//	public int getType() {
//		return type;
//	}
//	public void setType(int type) {
//		this.type = type;
//	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	// 转换为ResourceNodeMini对象
	public abstract ResourceNodeMini toResourceNodeMini();
}
