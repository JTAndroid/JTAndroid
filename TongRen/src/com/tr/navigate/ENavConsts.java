package com.tr.navigate;


/**
 * @author xuxinjian
 * @E-mail: xuxinjian@gintong.com
 * @version 1.0
 * @创建时间：2014-4-23 上午11:10:29
 * @类说明
 */
public class ENavConsts {
	public final static String EMucID = "MUCID";
	public final static String TopicID = "TopicID";
	public final static String EMucDetail = "EMucDetail";
	public final static String EMeetingDetail = "EMeetingDetail"; // 主会场详情
	public final static String EMeetingTopicDetail = "EMeetingTopicDetail"; // 分会场详情
	public final static String EMeetingId = "EMeetingId"; // 会议id
	public final static String EHasTopic = "EHasTopic"; // 是否有议题
	public final static String EFromPush = "EFromPush"; // 来自于推送
	public final static String ETopicId = "ETopicId"; // 议题id
	public final static String EJTFile = "EJTFile"; // 文件对象
	public final static String EListJTFile = "EListJTFile"; //
	public final static String EChatDetail = "EChatDetail";
	public final static String EConnectionsMini = "EConnectionsMini";
	public final static String EMessageID = "EMessageID";
	public final static String EPushMessageDetail = "EPushMessageDetail"; // 推送过来的消息详情
	public final static String EIMNotifyCleanHistory = "EIMNotifyCleanHistory";// 通知清空了聊天记录
	public final static String ENotifyParam = "ENotifyParam";
	public final static String EFriendId = "EFriendId"; // 二维码扫描后的对方的id
	public final static String IsFaile = "isFaile"; // 登录环信服务器是否失败
	public final static String EPushMessageType = "EPushMessageType"; // 推送消息类型
	public final static String EShareParam = "EShareParam";
	public final static String EProfessionAndCustomzation = "EProfessionAndCustomzation"; // 跳转类型
	public final static String EShareParamList = "EShareParamList"; // 分享一组数据
	public final static String SendMessagesForwardingList = "SendMessagesForwardingList";//分享单聊和群聊
	// "type":"0-视频(video),1-音频(audio),2-文件(file),3-图片(image),4-其它(other),5-网页链接,6-人脉(JTContact),7-knowledge知识,8-机构客户,9-机构用户(线上金桐网用户),10-个人用户(线上个人用户),11-需求,12-普通文本,13-knowledge2新知识"
	public final static String EShareType = "EShareType"; // 分享的资源类型
	public final static String EIMBaseMessage = "EIMBaseMessage"; // 消息对象
	public final static String EIMBaseMessageList = "EIMBaseMessageList"; // 消息列表对象

	public final static String EEnableSearchChatRecord = "EEnableSearchChatRecord"; // 是否支持清空聊天记录
	public final static String EStartIMGroupChatType = "EStartIMGroupChatType"; // 跳转到群聊的类型  0默认 1.类似从事物中跳转
	public final static String EClient_Data = "EClient_Data"; // 客户数据
	public final static String EClient_Id = "EClient_Id"; // 客户数据
	public final static String EFromActivityName = "fromActivity";// activity的来源
	public final static String IsSingleSelection = "isSingleSelection";// 单选
	/**	是否初使化删除传进来的数据	*/
	public final static String IsInitInDataDel = "isInitInDataDel";
	/**	是否初使化自己	*/
	public final static String IsInitMyself = "isInitMyself";
	/** 是否必需选择	*/
	public final static String isIndispensableSelect = "isIndispensableSelect";
	
	public final static String EListCountry = "mListCountry";// activity的来源
	public final static String ECountry = "mCountry";// activity的来源
	public final static String EFromActivityType = "fromActivitytype";// activity的来源
	
	public final static String Report = "Report";//举报
	public final static String Org_Report = "Org_Report";//举报
	
	/** 类型		*/
	public final static String TYPE = "type";
	public final static String EFromActivityResult = "EFromActivityResult";// activity的来源
	public final static String EStartActivityForResult = "EStartActivityForResult"; // 是否返回数据

	public final static String EMeetingNotifications = "EMeetingNofifications"; // 会议通知
	public final static String EMeetingMessageMap = "EMeetingMessageMap"; // 会议新消息

	public final static String EMUCDetailActivity = "MUCActivity";// 群聊详情设置页
	/** 首页直接创建群聊	*/
	public final static String EMainActivity = "MainActivity";
	public final static String ESelectConnection = "ESelectConnection";// 选择一般用户
	public final static String ESelectOrgman = "ESelectOrgman";// 选择负责人
	public final static String EIMCreateGroupActivity = "IMCreateGroupActivity";
	public final static String EForwardToFriendActivity = "ForwardToFriendActivity";
	public final static String EIMEditMemberActivity = "IMEditMemberActivity";
	public final static String EShareActivity = "ShareActivity"; // 分享页面
	public final static String EShareCnsSelectActivity = "EShareCnsSelectActivity"; // 选择一个要分享的人脉
	public final static String ESquareActivity = "SquareActivity"; // 会议详情页面
	public final static String ECommunityChatSettingActivity = "CommunityChatSettingActivity"; // 社群设置页面

	public final static String ESearchKeyword = "ESearchKeyword"; // 搜索关键字
	public final static String ESearchFromIndex = "ESearchFromIndex"; // 要搜索的消息index
	public final static String ESearchMessageID = "ESearchMessageID"; // 要搜索的消息ID
	public final static String ELocateMessageID = "ELocateMessageID"; // 需要定位的消息的ID


	public final static String datas = "datas";
	public final static String IS_SELF_BOOL  = "is_self_bool";
	public final static String redatas = "redatas";
	public final static String main_create = "main_create";
	public final static String detail_edit = "detail_edit";
	
	public final static int PEOPLE = 1;
	public final static int CLIENT = 2;
	public final static int type_details_other = 1;//好友 用户
	public final static int TYPE_CONNECTIONS_HOME_PAGE = 2;//人脉
	public final static int type_details_org = 2; // 机构信息
	public final static int type_details_member = 4; // 用户自己
	// public final static int type_details_onlineuser=5;//在线用户
	public final static int type_details_share = 6; // 在线用户
	public final static int type_details_recommend = 7; // 推荐的用户或人脉
	public final static int type_details_guest = 8; // 游客身份
	public static final String QRCodeStr = "QRCodeStr";
	public static final String phoneCall = "phoneCall";
	public static final String QRName = "QRName";
	public static final String userAvatar = "userAvatar";
	public static final String KEY_FRG_FLOW_COMMENT = "key_frg_flow_comment";
	public static final String KEY_FRG_SETTING_MINDUSTRYS = "key_frg_setting_mindustrys";
	public static final String KEY_FRG_CHANGE_COMMENTS = "key_frg_change_comments";
	public static final String KEY_FRG_FLOW_DYNAMIC_PRAISES = "key_frg_flow_dynamic_praises";
	public static final String KEY_FRG_FLOW_INDEX = "key_frg_flow_index";
	public static final String KEY_FRG_FLOW_TYPE = "key_frg_flow_type";
	public static final String KEY_FRG_FLOW_FORWARDING_KNOWLEDGE_TYPE = "key_frg_flow_forwarding_knowledge_type";
	public static final String KEY_COLLECTION_KNOWLEDGE_TYPE = "key_collection_knowledge_type";

	/** Demand 需求模块 */
	public static final String DEMAND_TYPE = "type"; // 类型，告诉模版界面 当前是投资还是融资
	public static final String DEMAND_NEW = "newDemand";// 开始创建新的需求
	public static final String DEMAND_EDIT="editDemand";//修改编辑需求

	/** 多选效果 */
	public static final String DEMAND_CHOOSE_DATA = "data";// 三级选择时数据
	public static final String DEMAND_CHOOSE_TITLE = "choosetitle";// 选择的标题
	public static final String DEMAND_CHOOSE_TYEP = "ChooseType";// 类型
																	// ，行业，地区（区域）
	public static final String PEOPLE_ID = "People_Id";// 类型
	public static final String DEMAND_CHOOSE_ID = "chooseid";// 父类信息id
	public static final String DEMAND_CHOOSE_MULTI = "MultiSelect";// 三级多选效果
	/** 货币的回调 */
	public static final String DEMAND_MONEY_TAG = "money_tag";// 货币类型信息
	public static final String DEMAND_MONEY_DATA = "money_string";// 货币范围
	/** 权限的回调 */
	public static final String DEMAND_PERMISSION_TEXT = "permissionText";// 权限控制最下面显示的数据
	public static final String DEMAND_PERMISSION_SAVE = "permissionSave";// 保存

	public static final String DEMAND_PERMISSION_DATA = "permissionData";// 选中的字段
	public static final String DEMAND_PERMISSION_CUSTOM = "permissioncustom";// 权限控制中的自定义字段
	/** 标签的回调 */
	public static final String DEMAND_LABEL_DATA = "demand_lable_data";// 标签请求和返回时
																		// data
																		// 的键
	public static final String PEOPLE_LABEL_DATA = "people_lable_data";// 标签请求和返回时
																		// data
																		// 的键
	public static final String DEMAND_LABEL_TYPE="demand_lable_type"; //单选 标签还是多选标签
	public static final String DEMAND_INTENT_SELECTED_PICTURE = "intent_selected_picture";//选择照片
	public static final String DEMAND_INTENT_SELECTED_ACCESSORY = "intent_selected_accessory";//选择附件
	public static final String ISFROMUPLOAD = "isFromUpload";//上传文件
	public static final String DEMAND_BROWER_DELETE="browerDelete";//删除
	public static final String DEMAND_NOTE_DATA="noteData";//需求介绍
	public static final String DEMAND_DETAILS_ID="demand_details_id";//需求详情的id
	public static final String DEMAND_DETAILS_CREATE="demand_details_createid";//创建者id
	public static final String DEMAND_DETAILS_FROM="demand_details_from";//需求来源
	public static final String DEMAND_DETAILS_TYPE="demand_details_type";//需求类型  主要是为了区分动态信息 是转发还是创建
	public static final String DEMAND_CATEGORY_DATA="demand_category_data";//目录信息对象
	public static final String Category_ENUM_TYPE="category_enum_type";//目录接口的枚举对象
	public static final String Category_SELECT_ACTION="category_select_action";//目录的多选操作
	public static final String DEMAND_FOR_RESULT = "demand_for_result";//回调

	public static final String FILEMANAGEMENT_ID = "FileManagement_id"; //文件管理中目录id	
	public static final String FILEMANAGEMENT_PID = "FileManagement_Pid"; //文件管理中父目录id	
	public static final String INTRODUCE_MODEL_DATA = "introduce_model_data";//介绍数据源
	
	public static final String WORK_EXPERIENCE = "work_experience";
	
	/**
	 * onActivityResult请求码
	 */

	public static class ActivityReqCode {
		public static final int ACTIVITY_REQ_CODE = 4000;//
		/* 设置行业 */
		public static final int REQUEST_CODE_SETTING_INDUSTRY_ACTIVITY = 4002;
		/* 动态查看更多评论 */
		public static final int REQUEST_CODE_LOOK_MORE_COMMENT = 4003;
		/** 评价查看更多评价 */
		public static final int REQUEST_CODE_FOR_MORE_EVALUATION = 4004;
		/** 评价编辑职业标签 */
		public static final int REQUEST_CODE_FOR_EDIT_EVALUATION = 4005;
		/**需求创建时的回调  主要是四大组建中的回调 */
		public static final int REQUEST_DEMAND_ACTIVITY = 1001;// 信息回调
		/** 三级多选 */
		public static final int REQUEST_CHOOSE_SELECT = 5000;
		/** 选择金额信息的回调 */
		public static final int REQUEST_MOENY_SELECT = 50001;

		public static final int REQUEST = 1;
		/** @ 好友 */
		public static final int REQUEST_CODE_FOR_AT_FRIENDS = 4006;
		public static final int REQUEST_CODE_GET_GENDER = 4100;//获取性别
		public static final int REQUEST_CODE_GET_AREA = 4101;//获取地区
		
		public static final int REQUEST_CODE_UPDATE_FRIDENDS = 4102;//添加好友返回刷新界面
	}

	public static class PeopleModuleReqCode{
		public static final int  WORK_EXPERIENCE_REQ_CODE = 1000;
	}
}
