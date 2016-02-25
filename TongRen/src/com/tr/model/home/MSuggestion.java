package com.tr.model.home;

import java.io.Serializable;

public class MSuggestion implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4303438023158690748L;
	
	/*"dicId":"类型id",
    "dicName":"类型名称",
    "problemContent":"内容",
    "source":"1:为app 2:为web",
    "contact":"联系方式"*/
	
	private String dicId;
	private String dicName;
	private String problemContent;
	private String source;
	private String contact;
	
	public String getDicId() {
		return dicId;
	}
	public void setDicId(String dicId) {
		this.dicId = dicId;
	}
	public String getDicName() {
		return dicName;
	}
	public void setDicName(String dicName) {
		this.dicName = dicName;
	}
	public String getProblemContent() {
		return problemContent;
	}
	public void setProblemContent(String problemContent) {
		this.problemContent = problemContent;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	
	
}
