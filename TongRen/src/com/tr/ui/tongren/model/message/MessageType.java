/*
 * 文件名： MessageType.java
 * 创建日期： 2015年11月10日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.tr.ui.tongren.model.message;


 /**
 * 消息类型
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年11月10日
 */
public enum MessageType {
	/*普通消息*/
	GENERAL_MESSAGE(0,"普通消息"),
	/*组织同意、拒绝操作  begin*/
	INVITATION(1, "邀请组织消息"),
	APPLICATION(2,"申请组织消息"),
	SIGNOUT(3,"申请退出组织消息"),
	/*组织同意、拒绝操作  end*/
	/*组织删除操作  begin*/
	AGREEJIONIN(4,"加入组织消息"),
	REFUSEAGREEJOININ(5,"拒绝加入组织消息"),
	AGREESIGNOUT(6,"退出组织消息"),
	REFUSEAGREESIGNOUT(7,"拒绝退出组织消息"),
	DISSOLUTION(8,"解散组织"),
	KICKED(9,"被组织踢"),
	/*组织删除操作  end*/
	/*组织查看操作begin*/
	ASSIGNMENT_TASK(10,"分配任务"),
	RETURN_TASK(11,"退回任务"),
	REPEAT_TASK(12,"重发任务"),
	/*组织查看操作end*/
	/*项目删除操作begin*/
	GIVE_UP_PROJECT(13,"放弃项目"),
	END_PROJECT(14,"结束项目"),
	/*项目删除操作end*/
	/*项目查看操作begin*/
	ORGANIZATION_TO_UNDERTAKE_PROJECTS(15,"某组织想承接项目"),
	PROJECT_DOCUMENT(16,"提项目文档"),
	ASSIGNMENT_SUB_TASK(17,"分配子任务"),
	/*项目查看操作end*/
	/*项目同意、拒绝操作begin*/
	INVITATION_PROJECT(18,"项目邀请组织承接"),//主要是项目可以发起申请承接人承接项目
	/*项目同意、拒绝操作end*/
	/*项目删除操作begin*/
	AGREE_TO_UNDERTAKE_PROJECTS(19,"同意承接项目"),
	REFUSEAGREE_TO_UNDERTAKE_PROJECTS(20,"拒绝承接项目"),
	/*项目删除操作end*/
	/*项目同意、拒绝操作begin*/
	APPLICATION_EXTENSION_PROJECT(21,"延期项目申请"),
	/*项目同意、拒绝操作end*/
	/*项目删除操作begin*/
	AGREE_APPLICATION_EXTENSION_PROJECT(22,"同意项目延期申请"),
	REFUSEAGREE_APPLICATION_EXTENSION_PROJECT(23,"拒绝延期项目申请");
	/*项目删除操作end*/
    private MessageType(int type, String message) {
	   this.type = type;
	   this.message = message;
	}
	private int type;
	private String message;
	/**
	 * @return 返回 type。
	 */
	public int getType() {
		return type;
	}
	/**
	 * ---@param type 要设置的 type。
	 */
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * @return 返回 message。
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * ---@param message 要设置的 message。
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
}
