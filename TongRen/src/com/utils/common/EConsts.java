package com.utils.common;

import com.tr.model.joint.ConnectionNode;

public class EConsts {
	
	public static final String share_firstLoginGetConnections = "firstLoginGetConnections";
	public static final String share_itemUserTableName = "share_itemUserTableName";
	public static final String share_itemFirstLogin = "share_itemFirstLogin";
	public static final String share_firstSplash = "share_itemFirstLogin";
	public static final String share_invisible_create_button = "share_invisible_create_button";
	public static final String share_firstGetloginConfiguration = "firstGetloginConfiguration";
	/*************** 知识广场 SharedPreferences start *************/
	/** 知识广场已读过的知识 */
	public static final String share_knowledgeSquare_readed_knowledge = "knowledgeSquare_readed_knowledge";
	/*************** 知识广场 SharedPreferences end *************/


	// 拍照请求码
	public static final int REQ_CODE_TAKE_PICTURE = 1001;
	// 选照请求码
	public static final int REQ_CODE_PICK_PICTURE = 1002;
	// 裁切图片请求码
	public static final int REQ_CODE_CROP_PICTURE = 1003;
	// 选视频请求码
	public static final int REQ_CODE_PICK_VIDEO = 1004;
	// 拍视频请求码
	public static final int REQ_CODE_TAKE_VIDEO = 1005;
	// 选择文件请求码
	public static final int REQ_CODE_PICK_FILE = 1006;
	// 获取录音文件请求码
	public static final int REQ_CODE_GET_RECORD = 1007;
	// 选人请求码
	public static final int REQ_CODE_CONNECTIONS = 1008;
	// 选关键字请求码
	public static final int REQ_CODE_KEYWORD = 1009;
	// 选择负责人
	public static final int REQ_CODE_PRINCIPAL = 1010;
	// 参与者
	public static final int REQ_CODE_PARTICIPANT = 1011;
	// 项目成员
	public static final int REQ_CODE_MEMBER = 1012;
	// 维护人
	public static final int REQ_CODE_MAINTAINER = 1013;
	// 修改需求
	public static final int REQ_CODE_EDIT_REQUIREMENT = 1014;
	// 修改事务
	public static final int REQ_CODE_EDIT_AFFAIR = 1015;
	// 选择需求
	public static final int REQ_CODE_SEARCH_REQUIREMENT = 1016;
	// 选择事务
	public static final int REQ_CODE_SEARCH_AFFAIR = 1017;
	// 选择知识
	public static final int REQ_CODE_KNOWLEDGE = 1018;
	// 选择行业
	public static final int REQ_CHOOSE_INDUSTRY = 1019;

	// 编辑议题
	public static final int CALL_MEETING_TOPIC_EDIT = 119;

	//与会信息
	public static final int CALL_MEETING_ATTEND_INFOMATION = 120;
	
	// 用户头像尺寸
	public static final int AVATAR_PIC_SIZE = 200;
	// 机构联系人身份证图片名
	public static final String ORG_CONTACT_PIC_FILE_NAME = "contact_pic.jpg";
	// 用户头像（企业或个人）
	public static final String USER_AVATAR_FILE_NAME = "user_avatar.jpg";
	// 组织机构代码图片名
	public static final String ORG_CODE_PIC_FILE_NAME = "org_code_pic.jpg";
	// 营业执照图片名
	public static final String ORG_LICENSE_PIC_FILE_NAME = "org_license_pic.jpg";
	// 企业法人身份证图片名
	public static final String LEGAL_PERSON_ID_FILE_NAME = "legal_person_id_pic.jpg";
	// 临时文件名
	public static final String TEMP_FILE_NAME = "temp.jpg";
	// jpg临时文件名
	public static final String TEMP_JPG_FILE_NAME = "temp.jpg";
	// 透镜系统App Key:
	public static final String BENCH_APP_KEY = "223b074422824e58b6ad3d4a3426d85d";

	// 请求码
	public static class ReqCode {
		private static final int Base = 2000;
		public static final int SelectFromMyRequirement = Base + 1; // 选择我的需求
		public static final int SelectFromMyKnowledge = Base + 2; // 选择我的知识
		public static final int SelectFromMyConnection = Base + 3; // 选择我的人脉
		public static final int PickPhoto = Base + 4;
		public static final int SelectFromMyConnection_share = Base + 5; // 在内部分享
		// add by leon
		public static final int CreateConnections = Base + 6; // 创建人脉
		public static final int CreateConnectionsForResult = Base + 7; // 创建人脉并返回结果
		public static final int CreateKnowledge = Base + 8; // 创建知识
		public static final int CreateKnowledgeForResult = Base + 9; // 创建知识并返回结果
		public static final int CreateAffair = Base + 10; // 创建事件
		public static final int CreateAffairForResult = Base + 11; // 创建事件并返回结果
		public static final int CreateRequirement = Base + 12;
		public static final int CreateRequirementForResult = Base + 13;
		public static final int UpdateKnowledgeForResult = Base + 14; // 编辑知识并返回结果
		public static final int SelectFromMyConnectionAndOrg = Base + 14; // 编辑知识并返回结果
	}

	// 键值
	public static class Key {

		// 跳转来源
		public static final String FROM_ACTIVITY = "from_activity";
		// 来源名称
		public static final String ACTIVITY_NAME = "activity_name";
		public static final String CUSTOMERID = "customerId";
		public static final String APP_SETTING = "app_setting";
		public static final String SESSION_ID = "session_id"; // 会话id
		public static final String USER_ID = "user_id";
		public static final String USER_AVATAR = "user_avatar";
		// 跳转请求码
		public static final String REQUEST_CODE = "request_code";
		// 跳转请求类型
		public static final String REQUEST_TYPE = "request_type";
		// 操作类型
		public static final String OPERATE_TYPE = "operate_type";
		//标签页面跳转：模块类型
		public static final String MODULES_TYPE = "modules_type";
		// 用户昵称
		public static final String NICK_NAME = "nick_name";
		// 用户名
		public static final String USER_NAME = "user_name";
		// 密码
		public static final String PASSWORD = "password";
		public static final String USER_MOBILE = "user_mobile";
		public static final String USER_EMAIL = "user_email";
		public static final String USER_ROLE = "user_role";
		public static final String USER_TYPE = "user_type";
		public static final String FILE_URL = "file_url";
		public static final String WEB_FILE_URL = "web_file_url";
		public static final String LOCAL_FILE_PATH = "local_file_path";
		public static final String ERROR_MESSAGE = "error_message";
		public static final String ERROR_CODE = "error_code";
		public static final String PROGRESS_UPDATE = "progress_update";
		// 事务类型
		public static final String AFFAIR_TYPE = "affair_type";
		public static final String AFFAIR_OBJECT = "affair_object";
		// 对象关键字
		public static final String REQUIREMENT = "requirement";
		public static final String REQUIREMENT_MINI = "requirement_mini";
		public static final String REQUIREMENT_ID = "requirement_id";
		public static final String BUSINESS_REQUIREMENT = "business_requirement";
		public static final String BUSINESS_REQUIREMENT_ID = "business_requirement_id";
		public static final String PROJECT = "project";
		public static final String PROJECT_ID = "project_id";
		public static final String TASK = "task";
		public static final String TASK_ID = "task_id";
		public static final String AFFAIR_MINI = "affair_mini";
		public static final String REQUIREMENT_TO_BUSINESS_REQUIREMENT = "requirement_to_business_requirement"; //
		public static final String REQUIREMENT_TO_PROJECT = "requirement_to_project";
		public static final String BUSINESS_REQUIREMENT_TO_PROJECT = "business_requirement_to_project";
		public static final String KNOWLEDGE = "knowledge";
		public static final String KNOWLEDGE_ID = "knowledge_id";
		public static final String TYPE = "type";
		public static final String ID = "id";
		public static final String IS_GOOD_FRIEND = "is_good_friend";
		public static final String PERSONTYPE = "personType";
		public static final String HOME_PAGE_ROLE = "home_page_role";
//		public static final String USEID = "useid";
		public static final String CreateOrg= "CreateOrg";
		public static final String NAME = "name";
		public static final String isOnline = "online";
		public static final String RETURN_DATA = "return_data";
		// 录音文件
		public static final String RECORDING = "recording";
		// 邮件
		public static final String EMAIL = "email";
		// 组织信息
		public static final String ORGINFOVO = "orginfovo";
		// 组织完善信息是否显示对话框
		public static final String ISSHOWDIALOG = "isshowdialog";
		// 组织审核失败信息
		public static final String FAILETEXT = "failetext";
		// 提交组织信息时内容
		public static final String ISCONTANINMAIL = "iscontaninmail";
		// 手机号
		public static final String MOBILE = "mobile";
		// 审核结果
		public static final String AUDIT_STATE = "audit_state";
		// 新消息提醒
		public static final String NEW_MESSAGE_ALERT = "new_message_alert";
		// 通讯录同步
		public static final String CONTACT_SYNC = "contact_sync";
		// 免打扰模式
		public static final String DISTURBABLE = "disturbable";
		// 文件对象
		public static final String JT_FILE = "jt_file";
		// 图片地址
		public static final String IMAGE_URL = "image_url";
		// 图片本地地址
		public static final String IMAGE_PATH = "image_path";
		// 人脉还是用户
		public static final String IS_ONLINE = "is_online";
		// 下载路径
		public static final String DOWNLOAD_PATH = "download_path";
		// 消息列表
		public static final String LIST_MESSAGE = "list_message";
		// 消息ID
		public static final String MESSAGE_ID = "message_id";
		// 会议id
		public static final String MEETING_ID = "meeting_id";
		// 议题id
		public static final String TOPIC_ID = "topic_id";
		
		//高速录音
		public static final String HIGH_SEPPD_RECORD = "high_speed_record";
		//预览视频
		public static final String PREVIEW_VIDEO="preview_video";
		//自动安装更新包
		public static final String AUTOMATIC_UPDATES="Automati_updates";
		//自动保存
				public static final String AUTOSAVE="Autosave";
		// 知识相关
		public static final String KNOWLEDGE_CONTENT = "knowledge_content";
		public static final String KNOWLEDGE_LIST_JTFILE = "knowledge_list_jtfile";

		// 知识关键字
		public static final String KNOWLEDGE_KEYWORD = "knowledge_keyword";
		public static final String RELATE_REMOVE_ID = "relate_remove_id";

		// 人脉对象
		public static final String CONNECTIONS = "connections";
		
		//客户详情评价id
		public static final String CLIENTID = "clientId";
		
		// 关联资源类型
		public static final String RELATED_RESOURCE_TYPE = "related_resource_type";
		public static final String RELATED_RESOURCE_SOURCE = "related_resource_source";
		// 关联资源的所有数据
		public static final String RELATED_RESOURCE_ALL_NODE = "related_resource_all_node";
		// 关联资源元数据
		public static final String RELATED_RESOURCE_NODE = "related_resource_node";
		// 关联人脉元数据
		public static final String RELATED_PEOPLE_NODE = "related_people_node";
		// 关联组织元数据
		public static final String RELATED_ORGANIZATION_NODE = "related_organization_node";
		// 关联知识元数据
		public static final String RELATED_KNOWLEDGE_NODE = "related_knowledge_node";
		// 关联事件元数据
		public static final String RELATED_AFFAIR_NODE = "related_affair_node";
		// 知识目录
		public static final String KNOWLEDGE_CATEGORY = "knowledge_category";
		// 知识目录列表
		public static final String KNOWLEDGE_CATEGORY_LIST = "knowledge_category_list";
		// 知识目录是否选择“未分组”
		public static final String KNOWLEDGE_CATEGORY_GROUP = "knowledge_category_group";
		// 对接资源栏目
		public static final String JOINT_RESOURCE_NODE = "joint_resource_node";
		// 对接资源类型
		public static final String JOINT_RESOURCE_TYPE = "joint_resource_type";
		// 对接资源来源
		public static final String JOINT_RESOURCE_SOURCE = "joint_resource_source";
		// 被对接的资源id
		public static final String TARGET_RESOURCE_ID = "targt_resource_id";
		// 被对接的资源类型
		public static final String TARGET_RESOURCE = "target_resource";
		// 被对接的资源类型
		public static final String TARGET_RESOURCE_TYPE = "target_resource_type";
		// 被对接的资源细分类型
		public static final String TARGET_RESOURCE_SUB_TYPE = "target_resource_sub_type";
		// 被选中的知识id列表，跳转到编辑标签页面
		public static final String KNOWLEDGE_ID_LIST = "knowledge_id_list";
		// 被选中的知识的类型列表，跳转到编辑标签页面
		public static final String KNOWLEDGE_TYPE_LIST = "knowledge_type_list";
		// 知识详情ID
		public static final String KNOWLEDGE_DETAIL_ID = "knowledge_detail_id";
		// 知识类型
		public static final String KNOWLEDGE_DETAIL_TYPE = "knowledge_detail_type";
		// 知识
		public static final String KNOWLEDGE2 = "knowledge2";
		public static final String KNOWLEDGE_MINI2 = "knowledge_mini2";
		public static final String KNOWLEDGE_IS_CREATE = "knowledge_iscreate";
		// 外部知识链接
		public static final String EXTERNAL_URL = "external_url";
		public static final String INDUSTRYS = "industrys";	
		
		
		//权限控制
		public static final String CONTROL = "control";
		//  *********** 关系联系人列表 start   *************/  
		/** 表名 */
		public static final String TABLE_NAME = "table_name";
		/** 请求是否为空 */
		public static final String SUCCESS = "success";
		
		//  *********** 关系联系人列表 end   *************/  
		
		/**人脉详情入口标记*/
		public static final String ENTER_PEOPLE_TAG= "enter_people_tag";//由发现入口
		public static final String FROM_FOUND  = "from_found";//由发现入口
		public static final String FROM_ME = "from_me";//由我入口,创建人脉入口
		public static final String FROM_CREARTE = "from_create";//由我入口,创建人脉入口
		/**用户or人脉标记*/
		public static final String PERSON_TYPE= "person_type";
		/**对象id*/
		public static final String PERSON_ID= "person_id";
		/*人脉权限*/
		public static final String VIEW= "view";			
		/**编辑传递对象*/
		public static final String PERSON_OBJECT = "peron_object";
	
		/**目录*/
		public static final String CATEGORY_KEY = "category_key";
		/**目录名*/
		public static final String CATEGORY_NAME = "category_name";
		/**标签*/
		public static final String LABEL_KEY = "label_key";
		/**标签名*/
		public static final String LABEL_NAME = "label_name";
		public static final String SUM = "sum" ;
		
		public static final String IS_LOGIN_TIMEOUT = "is_login_timeout";
		
		public static final String PROFESSION = "profession";
		
	}

	public static class Action {
		public static final String DOWNLOAD_SUCCESS = "download_success";
		public static final String DOWNLOAD_START = "download_start";
		public static final String DOWNLOAD_UPDATE = "download_update";
		public static final String DOWNLOAD_FAILED = "download_failed";
		public static final String DOWNLOAD_CANCELED = "download_canceled";
		// 选择需求
		public static final String SEARCH_REQUIREMENT = "search_requirement";
		// 选择事务
		public static final String SEARCH_AFFAIR = "search_affair";
		// 用户登出
		public static final String LOGIN_OUT = "login_out";
		/** 获得联系人列表后 写入数据完成后 action */
		public static final String  ACTION_GET_CONNECTIONS_LIST_FINISH = "com.tr.action.GET_CONNECTIONS_LIST_FINISH";	
	}

	// 密码加密秘钥
	public static final String PASSWORD_KEY = "Gintong2014Beta1.0";

	// 默认图片文件格式
	public static final String DEFAULT_PIC_SUFFIX = ".jpg";
	// 默认视频文件格式
	public static final String DEFAULT_VIDEO_SUFFIX = ".mp4";
	// 默认语音文件格式
	public static final String DEFAULT_AUDIO_SUFFIX = ".amr";
}
