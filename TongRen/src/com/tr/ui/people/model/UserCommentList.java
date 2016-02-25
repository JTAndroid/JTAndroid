package com.tr.ui.people.model;

import java.util.List;
/**
 * 获取该用户的评价 数据解析
 * @author user
 *
 */
public class UserCommentList {
	public boolean isEvaluated;
	public boolean success;
	public List<UserComment> listUserComment;
	
	public class UserComment{
		public Long id;// 主键
		public Long count; // 赞同个数
		public Long userId; // 主页人id
		public Integer userType; // 主页人类型：1-线上用户；2-人脉
		public Long createId; // 创建人id
		public String userComment;// 评论内容
		public long commentTime;// 评论时间
		
		public boolean evaluateStatus; // 赞同状态

		@Override
		public String toString() {
			return "UserComment [id=" + id + ", count=" + count + ", userId="
					+ userId + ", userType=" + userType + ", createId="
					+ createId + ", userComment=" + userComment
					+ ", commentTime=" + commentTime + ", evaluateStatus="
					+ evaluateStatus + "]";
		}

		
	}

	@Override
	public String toString() {
		return "UserCommentList [isEvaluated=" + isEvaluated + ", success="
				+ success + ", listUserComment=" + listUserComment + "]";
	}
	
	
	
	
}
