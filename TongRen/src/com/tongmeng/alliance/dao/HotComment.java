package com.tongmeng.alliance.dao;

/**
 * 热门评论
 * 
 * @author Administrator
 * 
 */
public class HotComment {

	int id;
	int userId;
	String userName;
	String userPic;
	String comment;
	String createTime;
	int approvalCount;
	String isApproval;

	public String getIsApproval() {
		return isApproval;
	}

	public void setIsApproval(String isApproval) {
		this.isApproval = isApproval;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPic() {
		return userPic;
	}

	public void setUserPic(String userPic) {
		this.userPic = userPic;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getApprovalCount() {
		return approvalCount;
	}

	public void setApprovalCount(int approvalCount) {
		this.approvalCount = approvalCount;
	}

	@Override
	public String toString() {
		return "id:" + id + ",userId:" + userId + ",userName:" + userName
				+ ",userPic:" + userPic + ",comment:" + comment
				+ ",createTime:" + createTime + ",approvalCount:"
				+ approvalCount+",isApproval:"+isApproval;
	}
}
