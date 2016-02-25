package com.tr.ui.people.model.CommentApproverLists;

import java.io.Serializable;
import java.util.ArrayList;


public class CommentApproverLists implements Serializable{

	/**
	 * 获取更多评价的数据类
	 */
	private static final long serialVersionUID = 1L;
	
	public boolean success;
	public ArrayList<CommentApprover> listCommentApprover;
	@Override
	public String toString() {
		return "CommentApproverLists [success=" + success
				+ ", listCommentApprover=" + listCommentApprover + "]";
	}
	
	

}
