package com.tr.ui.organization.model;

import java.io.Serializable;

public class NewsBean implements Serializable {

	/**
	 * 最新资讯 
	 */
	private static final long serialVersionUID = -3644825039207945992L;
    
	public long id;		//知识id
	public String title;	//知识标题
	public String pic="";		//知识标题
	public String author;	//作者名称
	public String authorId;// 作者id
	public String cpathid;	//栏目路径
	public String ctime;	//创建时间
	public String tags;	//标签
	public String desc;	//描述
	public String type;	//类型（0-全部,1-资讯，2-投融工具，3-行业，4-经典案例，5-图书报告，6-资产管理，7-宏观，8-观点，9-判例，10-法律法规，11-文章）
	
	@Override
	public String toString() {
		return "NoticeBean [id=" + id + ", title=" + title + ", pic=" + pic
				+ ", author=" + author + ", authorId=" + authorId
				+ ", cpathid=" + cpathid + ", ctime=" + ctime + ", tags="
				+ tags + ", desc=" + desc + ", type=" + type + "]";
	}
	
	
}
