package com.tr.model.work;

import java.io.Serializable;

public class BUAffarInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4545445433898927917L;
	public long affairId; 		//"事务id",
	public String info;				//"事务备注，如果类型为文本，则直接显示，如果为语音，则此字段为语音文件id",
	
	
	public long getAffairId() {
		return affairId;
	}
	public void setAffairId(long affairId) {
		this.affairId = affairId;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
}
