package com.tongmeng.alliance.util;

public class Constant {

	// 测试环境host
	public static String host = "http://test.etongmeng.com/phoenix";
	// 测试环境上传文件
	public static String uploadPath = "http://test.etongmeng.com/file/alliance/upload";
	// html地址
	public static String htmlPath = "http://test.etongmeng.com/html5/activity/android_editor.html";

	// // 正式环境host
	// public static String host = "http://www.etongmeng.com/phoenix";
	// // 正式环境上传文件
	// public static String uploadPath =
	// "http://www.etongmeng.com/file/alliance/upload";

	// 查询标签
	public static String typePath = host + "/label/search.json";
	// 发现活动
	public static String discoverPath = host + "/activity/discover.json";
	// 查询活动详情
	public static String actionDetailPath = host + "/activity/getDetail.json";
	// 感兴趣的
	public static String interestPath = host + "/activity/getInterested.json";
	// 评论
	public static String commentPath = host + "/comment/getList.json";
	// 点赞
	public static String approvalPath = host + "/comment/approval.json";
	// 报名
	public static String applyPath = host + "/member/apply.json";
	//取消报名
	public static String cancleApplyPath  = host+"/member/cancleapply.json";
	// 活动编辑
	public static String editPath = host + "/activity/edit.json";
	// 创建活动
	public static String createPath = host + "/activity/create.json";
	// 删除活动
	public static String deletePath = host + "/activity/delete.json";
	// 查询报名表单
	public static String searchApplyFormPath = host
			+ "/label/searchApplyForm.json";
	// 发布活动查询标签
	public static String tagPath = host + "/label/searchApply.json";
	// 查询帐号
	public static String getAccounPath = host + "/user/getAccounts.json";
	// 删除帐号
	public static String deleteAccountPath = host + "/user/deleteAccount.json";
	// 添加微信帐号
	public static String weChatpath = host + "/user/addWechatAccount.json";
	// 设置默认帐号
	public static String setDefaltAccountPath = host
			+ "/user/setDefaultAccount.json";
	// 我发布的
	public static String myGreatePath = host + "/activity/getMyCreated.json";
	// 已参加的
	public static String myAttendPath = host + "/activity/getMyAttended.json";
	// 已取消的
	public static String myCanceledPath = host + "/activity/getMyCanceled.json";
	// 查询参会人列表
	public static String attenderListpath = host + "/member/attenderList.json";
	// 获取签到人数
	public static String signedNopath = host + "/member/getSignInCount.json";
	// 我的笔记
	public static String mynotePath = host + "/note/getMyNotes.json";
	// 删除笔记
	public static String deleteNotePath = host + "/note/delete.json";
	// 增加笔记
	public static String addNotePath = host + "/note/add.json";
	// 刷新笔记
	public static String refreshNotePath = host + "/note/getNote.json";
	// 置顶笔记
	public static String topNotePath = host + "/note/top.json";
	// 搜索用户
	public static String searchUserPath = host + "/user/search.json";
	// 成员报名信息
	public static String peopleMumberPath = host + "/member/getApplyInfo.json";
	// 成员删除标签
	public static String peopledeleteTagPath = host + "/tag/delete.json";
	// 成员添加标签
	public static String peopleAddTagPath = host + "/tag/add.json";
	// 发送通知
	public static String sendMessagePath = host + "/member/sendNotice.json";
	// 签到
	public static String signPath = host + "/member/signIn.json";
	// 获取签到码
	public static String getSignInCodePath = host
			+ "/member/getSignInCode.json";
	// APP扫描上传名单二维码
	public static String appLoadPath = host + "/member/scanCode.json";
	// 报名信息验证
	public static String applyVerifyPath = host + "/deal/validatePayCode.json";
	// 报名发送验证码
	public static String applySendCodePath = host + "/deal/sendPayCode.json";
	// 从服务器获取配置信息（包括微信、qq等的key、secret等）
	public static String getCinfigPath = host
			+ "/config/getSystemConfig.json";
	//分页查询目录
	public static String queryCatalogPath = host+"/catalog/getByPage.json";
	//增加目录
	public static String addCatalogPath = host+"/catalog/add.json";
	//修改目录
	public static String modifyCatalogPath = host+"/catalog/edit.json";
	//删除目录
	public static String deleteCatalogPath = host+"/catalog/delete.json";
	//增加标签
	public static String addTagPath = host+"/tag/add.json";
	//查询标签
	public static String queryTagPath = host+"/tag/getList.json";
	//删除标签
	public static String deleteTagPath = host+"/tag/delete.json";
}
