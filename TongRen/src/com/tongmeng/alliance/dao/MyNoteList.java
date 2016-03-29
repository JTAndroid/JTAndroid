package com.tongmeng.alliance.dao;

public class MyNoteList {
/*
 * "id":笔记ID,
"content":"笔记内容",
"isTop":是否置顶 0:默认状态 1:置顶,
"createTime":"创建时间 格式如2016-01-05 11:28:48"
 * 
 * 
 * */
	String title;
	int id;
 
	String content;
	int isTop;
	String createTime;
	public MyNoteList() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MyNoteList(String title, int id, String content, int isTop,
			String createTime) {
		super();
		this.title = title;
		this.id = id;
		this.content = content;
		this.isTop = isTop;
		this.createTime = createTime;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getIsTop() {
		return isTop;
	}
	public void setIsTop(int isTop) {
		this.isTop = isTop;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
}