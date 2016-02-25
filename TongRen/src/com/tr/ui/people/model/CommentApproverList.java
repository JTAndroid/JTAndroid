package com.tr.ui.people.model;

import java.util.List;

/**
* 更多评价 数据解析
*/
public class CommentApproverList {
	public boolean success;
	public List<CommentApprover> listCommentApprover;

	public class CommentApprover {
		/** 用户评价id */
		public Long ueid;
		/** 评价内容 */
		public String comment;
		/** 主页人是否赞同了该评价 */
		public boolean feedback;
		/** 评价人mini对象 */
		public List<ApproverMiNi> listApproverMiNi;
	}
	
	public class ApproverMiNi {
		/** 评价人id */
		public long userId;
		/** 头像地址 */
		public String imageUrl;
		/**用户/人脉*/
		public boolean isOnline;
	}
}

