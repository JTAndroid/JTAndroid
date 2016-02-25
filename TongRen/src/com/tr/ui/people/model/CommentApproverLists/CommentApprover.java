package com.tr.ui.people.model.CommentApproverLists;

import java.io.Serializable;
import java.util.ArrayList;

public class CommentApprover implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 用户评价id */
	public Long ueid;
	/** 评价内容 */
	public String comment;
	/** 主页人是否赞同了该评价 */
	public boolean feedback;
	/** 评价人mini对象 */
	public ArrayList<ApproverMiNi> listApproverMiNi;
	@Override
	public String toString() {
		return "CommentApprover [ueid=" + ueid + ", comment=" + comment
				+ ", feedback=" + feedback + ", listApproverMiNi="
				+ listApproverMiNi + "]";
	}
	
	
	

}
