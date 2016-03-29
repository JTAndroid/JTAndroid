package com.tongmeng.alliance.dao;

public class NoteDao {
	public int id;
	public String title;
	public String content;
	public String isTop;
	public String createTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getIsTop() {
		return isTop;
	}

	public void setIsTop(String isTop) {
		this.isTop = isTop;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "id:" + id + ",title:" + title + ",content:" + content
				+ ",isTop:" + isTop + ",createTime:" + createTime;
	}
}
