package com.utils.http;

public class EAPIConsts {

	// QA(QA)-0，仿真(SIMULATER)-1，正式(生产ONLINE)-2，开发(DEV)-3，
	// 个人(PERSON)-4,5-DEVPUBLIC开发外网地址（端口映射，方便协作方测试）
	private final static int QA = 0;
	public final static int SIMULATER = 1;
	private final static int ONLINE = 2;
	private final static int DEV = 3;
	private final static int PERSON = 4;

	private final static int DEVPUBLIC = 5;
	/** 是否把log写到sdcard 上 log文件位置在sd根目录 文件名GushiLog */
	public static boolean isLog2Sdcard = true;

	public static int ENVIRONMENT = SIMULATER; // api接口地址
	public static int IM_ENVIRONMENT = SIMULATER; // im部分api接口地址
	public static int CONFERENCE_ENVIRONMENT = SIMULATER; // 会议模式api接口地址

	// 0-QA；1-仿真；2-生产；3-开发；4-个人；5-vpns
	public enum Environment {
		QA, DEV, SIMULATER, ONLINE, PERSON, DEVPUBLIC
	}

	// 自定义head
	public static class Header {
		public static final String ERRORCODE = "errorCode"; // 生产环境
		public static final String ERRORMESSAGE = "errorMessage"; // 生产环境
	}

	// 事务开始
	public static String WORK_URL_DEV = "http://192.168.101.131:3333";
	public static String FLOW_URL_DEV = "http://192.168.120.155:3356";
	// public static String WORK_URL_DEV = "http://103.10.87.204:8080/ssm";

	public static String AFFAIR_LIST_GET = "/affair/affairList.json"; // 事务列表
	public static String AFFAIR_LOG_LIST_GET = "/affair/affairLogList.json"; // 事务日志
	public static String AFFAIR_LIST_MONTH_DATE_GET = "/affair/monthAffair.json"; // 每月的事务
	public static String AFFAIR_DETAIL_GET = "/affair/affairDetail.json"; // 事务详情
	public static String AFFAIR_CREATE = "/affair/createAffair.json"; // 创建事务
	public static String AFFAIR_EDIT = "/affair/updateAffair.json"; // 编辑事务

	public static String AFFAIR_RELATION_GET = "/affair/affairRelationList.json"; // 事务关联
	public static String AFFAIR_MODIFY_STATUS = "/affair/actionAffair.json"; // 事务状态修改

	public static String AFFAIR_CHART = "/mobile/im/fetchMucIdForAffair"; // 事务聊天

	public static String AFFAIR_CHAR_URL = "http://192.168.101.131:2222/ImServer";
	

	public static String getWorkUrl() {
		/*
		 * switch (ENVIRONMENT) { case QA: return WORK_URL_DEV; case SIMULATER:
		 * return WORK_URL_DEV; case ONLINE: return WORK_URL_DEV; case DEV:
		 * return WORK_URL_DEV; case PERSON: return WORK_URL_DEV; case
		 * DEVPUBLIC: return WORK_URL_DEV; default: // 默认线上 return WORK_URL_DEV;
		 * }
		 */
		return EAPIConsts.TMS_URL;
	}

	// 通用接口请求

	public static class WorkReqType {
		public static final int ReqBase = 5800;
		public static final int ReqEnd = 5999;
		public static final int AFFAIR_LIST_GET = ReqBase + 1; // 获取事务列表
		public static final int AFFAIR_LOG_LIST_GET = ReqBase + 2; // 获取事务日志列表
		public static final int AFFAIR_LIST_MONTH_DATE_GET = ReqBase + 3; // 每月的事务
		public static final int AFFAIR_DETAIL_GET = ReqBase + 4; // 事务详情
		public static final int AFFAIR_CREATE = ReqBase + 5; // 创建事务
		public static final int AFFAIR_RELATION_GET = ReqBase + 6; // 事务关联
		public static final int AFFAIR_EDIT = ReqBase + 7; // 编辑事务
		public static final int AFFAIR_MODIFY_STATUS = ReqBase + 8; // 事务状态

		public static final int AFFAIR_CHART = ReqBase + 9; // 事务畅聊
		public static final int AFFAIR_LIST_GET_ALL = ReqBase + 10; // 获所有取事务列表

	}

	// 事务结束
	// http://192.168.120.78:3333/mobileApp/checkFriend.json

	// 测试环境,主api入口
	public static final String TMS_URL_QGC = "http://192.168.170.115:9999/"; // qgc
	public static final String TMS_URL_JC = "http://192.168.170.178:8080/"; // jc
	public static final String TMS_URL_ZW = "http://192.168.120.243:8080/"; // zw
	public static final String TMS_URL_PERSON_ZZ = "http://192.168.120.99:81/"; // 个人联调
	public static final String TMS_URL_PERSON_LRY = "http://192.168.170.49:81/"; // 个人联调

	public static final String TMS_URL_PERSON_WMZ = "http://192.168.120.93:81/";// 个人联调王美洲

	// 王世春
	public static final String TMS_URL_YWQ = "http://192.168.171.66";// 个人联调闫伟旗
	public static final String TMS_URL__PERSON_FDZ = "http://192.168.120.162:8080/";// 个人联调
																					// 冯德贞
	public static final String TMS_URL_PERSON_TANGHUIHUANG = "http://192.168.150.103:81/";// 个人联调唐辉煌

	public static final String TMS_URL_ZZ = "http://192.168.170.60:8080/jtmobileserver"; // zz张震
	/** 个人联调 张震 */
	public static final String TMS_URL_ZHANG_ZHEN = "http://192.168.170.60:8080/jtmobileserver"; // zz张震

	public static final String TMS_URL_CH = "http://192.168.120.216:8070/";

	public static final String TMS_URL_FLW = "http://192.168.120.221:3333/"; // FLW付利文
	public static final String TMS_URL_PERSON_LIU_BANG = "http://192.168.120.206:3333/"; // 个人联调
																							// 刘邦

	public static final String TMS_URL_XXJ = "http://192.168.120.50:8080/jtmobileserver";
	public static final String TMS_URL_BZW = "http://192.168.120.179:8080/";// 边志伟

	public static final String TMS_URL_LHY = "http://192.168.120.181:8080/";// 李海岩
	public static final String TMS_URL_PERSON_THH = "http://192.168.150.103:81/";// 唐辉煌

	public static final String TMS_URL_XXJ1 = "http://192.168.120.179:8080/";

	public static final String TMS_URL_HT = "http://192.168.120.181:8080/";

	public static final String TMS_URL_PERSON_WFL = "http://192.168.120.184:5555/"; // 个人联调
																					// 王飞亮
	public static final String TMS_URL_PERSON_XKH = "http://192.168.120.155:3333/"; // 个人联调
																					// 邢开虎
																					// IM、
	// 普通接口环境 jtmobileserver项目
//<<<<<<< HEAD
//	public static final String TMS_URL_DEV = "http://192.168.101.131:3333/"; // 组织开发环境地址（）
//	// public static final String TMS_URL_DEV = "http://192.168.120.134:3344/";
//=======
//	public static final String TMS_URL_DEV = "http://192.168.120.234:3322/"; //
	public static final String TMS_URL_DEV = "http://dev.gintong.com/cross/"; //http://192.168.120.234:3322/advertise/getStaticAdvertiseList.json
//	public static final String TMS_URL_DEV = "http://192.168.120.134:3344/";
	public static final String TMS_URL_QA = "http://192.168.101.90:5555/"; // QA环境地址
	public static final String TMS_URL_SIMULATER = "http://test.online.gintong.com/cross/"; // 仿真环境地址
	public static final String TMS_URL_COMMUNITY_SIMULATER = "http://test.online.gintong.com/"; // 仿真社群二维码环境地址
//	public static final String TMS_URL_SIMULATER = "http://192.168.170.60:8080/jtmobileserver/"; // 仿真环境地址 张震
	public static final String TMS_URL_ONLINE = "http://jtmobile.gintong.com:4445/"; // 域名线上地址
	// public static final String TMS_URL_ONLINE = "http://123.59.74.95:4445/";
	// // 云主机线上地址
	// public static final String TMS_URL_ONLINE =
	// "http://211.103.198.41:4445/"; // 线上地址
	// public static final String TMS_URL_ONLINE = "http://123.59.74.95:4445/";
	// // 线上地址(云主机)
	public static final String TMS_URL_DEVPUBLIC = "http://124.65.120.198:10002/"; // 开发环境地址(外网映射）

	// 会议环境
	public static final String CONFERENCE_URL_PERSON_XKH = "http://192.168.120.155:4447/"; // 邢开虎
	public static final String CONFERENCE_URL_DEV = "http://192.168.101.22:9999/"; // 开发环境地址

	public static final String CONFERENCE_URL_QA = "http://192.168.101.90:4447/"; // 测试环境地址
	public static final String CONFERENCE_URL_SIMULATER = "http://192.168.101.42:4447/"; // 仿真环境地址
	public static final String CONFERENCE_URL_ONLINE = "http://meeting.gintong.com:4447/"; // 域名线上地址
	// public static final String CONFERENCE_URL_ONLINE =
	// "http://123.59.74.95:4447/"; // 云主机线上地址
	// public static final String CONFERENCE_URL_ONLINE =
	// "http://211.103.198.42:4447/"; // 线上地址
	// public static final String CONFERENCE_URL_ONLINE =
	// "http://123.59.74.95:4447/"; // 线上地址(云主机)
	public static final String CONFERENCE_URL_DEVPUBLIC = "http://124.65.120.198:10006/"; // 开发环境地址
	public static final String CONFERENCE_URL_PERSON_QGC = "http://192.168.170.115:9999/";

	// IM api入口地址
	public static final String IM_URL_PERSON_ZZ = "http://192.168.170.60:3333/ImServer/"; // 个人联调ZZ张震
	public static final String IM_URL_PERSON_KH = "http://192.168.120.144:8080/ImServer/"; // 个人联调邢开虎
	public static final String IM_URL_PERSON_LMY = "http://192.168.170.250:3333/ImServer/"; // 个人联调LMY李梦阳
	public static final String IM_URL_PERSON_QGC = "http://192.168.170.115:3333/";

	public static final String IM_URL_PERSON_XKH = "http://192.168.120.144:8080/ImServer/"; // 邢开虎
																							// IM
	public static final String IM_URL_PERSON_FLW = "http://192.168.120.221:3333/"; // 付利文
	public static final String IM_URL_PERSON_QMJ = "http://192.168.170.144:80/"; // 钱明金

	public static final String IM_URL_DEV = "http://192.168.101.131:2222/ImServer/"; // 开发环境地址
	public static final String IM_URL_QA = "http://192.168.101.90:4444/ImServer/";// 测试环境地址

	public static final String IM_URL_SIMULATER = "http://192.168.101.41:4446/";// 仿真环境地址
	public static final String IM_URL_ONLINE = "http://jtim.gintong.com:4446/"; // 域名映射线上地址
	// public static final String IM_URL_ONLINE = "http://123.59.74.95:4446/";
	// // 云主机线上地址

	// public static final String IM_URL_ONLINE = "http://211.103.198.41:4446/";
	// // 线上地址
	// public static final String IM_URL_ONLINE = "http://123.59.74.95:4446/";
	// // 线上地址(云主机)
	public static final String IM_URL_DEVPUBLIC = "http://124.65.120.198:10001/ImServer/"; // 开发环境地址

	// ---------------------------------------------个人联调-----------------------------------------
	public static final String TMS_URL_PERSON = TMS_URL_ZZ; // 个人联调主脉
	// 个人联调主脉
//<<<<<<< HEAD
////	public static final String IM_URL_PERSON = IM_URL_PERSON_XKH; // 个人联调
//	public static final String CONFERENCE_URL_PERSON = TMS_URL_ZZ; // 个人联调
//	public static final String IM_URL_PERSON = TMS_URL_ZZ;// 个人联调
//	
//=======
	// public static final String IM_URL_PERSON = IM_URL_PERSON_XKH; // 个人联调
	public static final String CONFERENCE_URL_PERSON = CONFERENCE_URL_DEV; // 个人联调
	public static final String IM_URL_PERSON = IM_URL_PERSON_QGC;// 个人联调

	// 文件上传地址

	// 附件：
	public static final String FILE_URL_SIMULATER = "http://file.online.gintong.com/mobile/upload";
	public static final String FILE_URL_QA = "http://file.qatest.gintong.com/mobile/upload";
	public static final String FILE_URL_DEV = "http://192.168.101.22:81/mobile/upload";
	public static final String FILE_URL_ONLINE = "http://file.gintong.com/mobile/upload";

	// 需求附件上次
	public static final String FILE_DEMAND_URL_SIMULATER = "http://file.online.gintong.com/mobile/demand/upload";
	public static final String FILE_DEMAND_URL_QA = "http://file.qatest.gintong.com/mobile/demand/upload";
	public static final String FILE_DEMAND_URL_DEV = "http://192.168.101.22:81/mobile/demand/upload";
	public static final String FILE_DEMAND_URL_ONLINE = "http://file.gintong.com/mobile/demand/upload";

	// 会议图片上传地址
	public static final String FILE_MEETING_PICTURE_URL_SIMULATER = "http://file.online.gintong.com/meeting/user/avatar";
	public static final String FILE_MEETING_PICTURE_URL_QA = "http://file.qatest.gintong.com/meeting/user/avatar";
	public static final String FILE_MEETING_PICTURE_URL_DEV = "http://file.dev.gintong.com/meeting/user/avatar";
	public static final String FILE_MEETING_PICTURE_URL_ONLINE = "http://file.gintong.com/meeting/user/avatar";

	// 会议音视频附件上传地址
	public static final String FILE_MEETING_FILE_URL_SIMULATER = "http://file.online.gintong.com/meeting/upload";
	public static final String FILE_MEETING_FILE_URL_QA = "http://file.qatest.gintong.com/meeting/upload";
	public static final String FILE_MEETING_FILE_URL_DEV = "http://file.dev.gintong.com/meeting/upload";
	public static final String FILE_MEETING_FILE_URL_ONLINE = "http://file.gintong.com/meeting/upload";

	// 用户头像：
	public static final String AVATAR_USER_URL_SIMULATER = "http://file.online.gintong.com/mobile/user/avatar";
	public static final String AVATAR_USER_URL_QA = "http://file.qatest.gintong.com/mobile/user/avatar";
	public static final String AVATAR_USER_URL_DEV = "http://192.168.101.22:81/mobile/user/avatar";
	public static final String AVATAR_USER_URL_ONLINE = "http://file.gintong.com/mobile/user/avatar";
	/** 用户头像前缀 */
	public static final String AVATAR_USER_URL_HEADER_SIMULATER = "http://192.168.101.22";
	public static final String AVATAR_USER_URL_HEADER_ONLINE = "http://file.gintong.com";
	/** 金桐脑头像地址 */
	public static final String AVATAR_GIN_TONG_NAO_URL_QA = "http://file.qatest.gintong.com/web/pic/user/gintong.png";
	public static final String AVATAR_GIN_TONG_NAO_URL_ONLINE = "http://file.gintong.com/web/pic/user/gintong.png";

	// 组织用户图片上传地址
	public static final String PIC_ORG_USER_URL_DEV = "http://file.dev.gintong.com/mobile/idcard/avatar";
	public static final String PIC_ORG_USER_URL_QA = "http://file.qa.gintong.com/mobile/idcard/avatar";
	public static final String PIC_ORG_USER_URL_SIMULATER = "http://file.online.gintong.com/mobile/idcard/avatar";
	public static final String PIC_ORG_USER_URL_ONLINE = "http://file.gintong.com/mobile/idcard/avatar";

	// 人脉头像：
	public static final String AVATAR_CONNS_URL_SIMULATER = "http://file.online.gintong.com/mobile/people/avatar";
	public static final String AVATAR_CONNS_URL_QA = "http://file.qatest.gintong.com/mobile/people/avatar";
	public static final String AVATAR_CONNS_URL_DEV = "http://192.168.101.22:81/mobile/people/avatar";
	public static final String AVATAR_CONNS_URL_ONLINE = "http://file.gintong.com/mobile/people/avatar";
	// 各种身份证、logo等图片
	public static final String CARD_URL_SIMULATER = "http://file.online.gintong.com/mobile/idcard/avatar";
	public static final String CARD_URL_QA = "http://file.qatest.gintong.com/mobile/idcard/avatar";
	public static final String CARD_URL_DEV = "http://192.168.101.131:880/mobile/idcard/avatar";
	public static final String CARD_URL_ONLINE = "http://file.gintong.com/mobile/idcard/avatar";

	/* 上传图片 */
	public final static String URL_HY_QA_INTRODUCE_PHOTO = "http://file.qatest.gintong.com/meeting/user/avatar";
	public final static String URL_HY_QA_OTHER = "http://file.qatest.gintong.com/meeting/upload";
	// http://192.168.101.22/meeting/upload
	public final static String URL_HY_DEV_INTRODUCE_PHOTO = "http://192.168.101.22/meeting/user/avatar";
	public final static String URL_HY_DEV_OTHER = "http://192.168.101.22/meeting/upload";

	public final static String URL_HY_SIMULATER_INTRODUCE_PHOTO = "http://file.online.gintong.com/meeting/user/avatar";
	public final static String URL_HY_SIMULATER_OTHER = "http://file.online.gintong.com/meeting/upload";

	public final static String URL_HY_ONLINE_INTRODUCE_PHOTO = "http://file.gintong.com/meeting/user/avatar";
	public final static String URL_HY_ONLINE_OTHER = "http://file.gintong.com/meeting/upload";

	public static final String DynamicNews_URL_DEV = "http://192.168.120.155:3356/"; // DEV环境地址
	public static final String DynamicNews_URL_SIMULATER = "http://192.168.101.15:3330/"; // 仿真环境地址
	public static final String DynamicNews_URL_ONLINE = "http://gintong.com/"; // 域名线上地址
	public static final String DynamicNews_URL_PERSON = ""; // 个人联调主脉
	public static final String MAIN_PAGE_URL = ""; // 首页链接
	
	public static final String COMMUNITY_URL_SIMULATER = "http://test.online.gintong.com/community/"; // 社群仿真环境地址
	public static final String COMMUNITY_URL_ONLINE = "http://gintong.com/community/"; //  社群
	public static final String COMMUNITY_URL_PERSON = ""; //  社群
	public static final String COMMUNITY_URL_QA = ""; //  社群
	public static final String COMMUNITY_URL_DEV = "http://192.168.120.234:9999/"; //  社群
	
	public static final String TONGREN_SIMULATER = "http://192.168.101.53:4448/"; // 桐人仿真环境地址
//	public static final String TONGREN_SIMULATER = "http://192.168.101.15:4448/"; // 桐人仿真环境地址
	public static final String TONGREN_ONLINE = "http://www.gintong.com/crossgtadmin/"; // 桐人线上环境
	public static final String TONGREN_PERSON = ""; // 桐人个人联调
	public static final String TONGREN_QA = ""; // 桐人Qa环境
	public static final String TONGREN_DEV = "http://192.168.101.131:6789"; //  桐人DEV环境
	
	public static final String COMMUNITY_IM_URL_SIMULATER = "http://192.168.101.41:4446/"; // 社群聊天有关的仿真环境地址
	public static final String COMMUNITY_IM_URL_ONLINE = "http://jtim.gintong.com:4446/"; //  社群聊天有关的
	public static final String COMMUNITY_IM_URL_PERSON = ""; //  社群聊天有关的
	public static final String COMMUNITY_IM_URL_QA = ""; //  社群聊天有关的
	public static final String COMMUNITY_IM_URL_DEV = "http://192.168.101.131:2244/"; //  社群聊天有关的
	// 普通接口地址
	public static final String TMS_URL = getTMSUrl();
	// 动态地址
	public static final String DynamicNews_URL = getDynamicNewsUrl();

	// IM接口地址
	public static final String IM_URL = getIMUrl();

	// 会议接口地址
	public static final String CONFERENCE_URL = getConferenceUrl(); // 测试地址
	// 社群接口地址
	public static final String COMMUNITY_URL = getCommunityUrl(); 
	// 社群聊天有关的接口地址
	public static final String COMMUNITY_IM_URL = getCommunityImUrl(); 
	// 桐人有关的接口地址
		public static final String TONGREN_URL = getTongrenUrl(); 
	// 文件上传地址
	/** 会议图片上传地址 */
	public static final String MEETING_PICTURE_UPLOADER_URL = getMeetingPicUrl();
	/** 会议音视频附件上传地址 */
	public static final String MEETING_FILE_UPLOADER_URL = getMeetingFileUrl();

	public static final String FILE_URL = getFileUrl();
	public static final String FILE_DEMAND_URL = getDemandFileUrl(); // 需求附件上传
	public static final String AVATAR_USER_URL = getAvatarUserUrl();
	public static final String AVATAR_CONNS_URL = getAvatarConnsUrl();
	public static final String CARD_URL = getCardUrl();

	public static final String PIC_ORG_URL = getOrganizationUploadUrl(); // 组织上传地址
	/** 头像前缀 */
	public static final String AVATAR_USER_URL_HEADER = getAvatarUserUrlHeader();
	/** 金桐脑头像 */
	public static final String AVATAR_GinTongNao_URL = getAvatarGinTongNaoUrl();

	public static String getUrlHyOther() {
		switch (ENVIRONMENT) {
		case QA:
			return URL_HY_QA_OTHER;
		case SIMULATER:
			return URL_HY_SIMULATER_OTHER;
		case ONLINE:
			return URL_HY_ONLINE_OTHER;
		case DEV:
			return URL_HY_DEV_OTHER;

		default:
			return URL_HY_QA_OTHER;
		}
	}

	public static String getUrlHyIntroducePhoto() {
		switch (ENVIRONMENT) {
		case QA:
			return URL_HY_QA_INTRODUCE_PHOTO;
		case DEV:
			return URL_HY_DEV_INTRODUCE_PHOTO;
		case SIMULATER:
			return URL_HY_SIMULATER_INTRODUCE_PHOTO;
		case ONLINE:
			return URL_HY_ONLINE_INTRODUCE_PHOTO;
		default:
			return URL_HY_QA_INTRODUCE_PHOTO;
		}
	}

	public static String getTMSUrl() {
		switch (ENVIRONMENT) {
		case QA:
			return TMS_URL_QA;
		case SIMULATER:
			return TMS_URL_SIMULATER;
		case ONLINE:
			return TMS_URL_ONLINE;
		case DEV:
			return TMS_URL_DEV;
		case PERSON:
			return TMS_URL_PERSON;
		case DEVPUBLIC:
			return TMS_URL_DEVPUBLIC;
		default: // 默认线上
			return TMS_URL_ONLINE;
		}
	}
	public static String getCommunityTMSUrl() {
		switch (ENVIRONMENT) {
		case QA:
			return TMS_URL_QA;
		case SIMULATER:
			return TMS_URL_COMMUNITY_SIMULATER;
		case ONLINE:
			return TMS_URL_ONLINE;
		case DEV:
			return TMS_URL_DEV;
		case PERSON:
			return TMS_URL_PERSON;
		case DEVPUBLIC:
			return TMS_URL_DEVPUBLIC;
		default: // 默认线上
			return TMS_URL_ONLINE;
		}
	}
	/**
	 * 新动态的环境配置
	 * 
	 * @return
	 */
	public static String getDynamicNewsUrl() {
		switch (ENVIRONMENT) {

		case SIMULATER:
			return DynamicNews_URL_SIMULATER;
		case ONLINE:
			return DynamicNews_URL_ONLINE;
		case DEV:
			return DynamicNews_URL_DEV;
		case PERSON:
			return DynamicNews_URL_PERSON;
		default: // 默认线上
			return DynamicNews_URL_ONLINE;
		}
	}

	public static String getIMUrl() {
		switch (IM_ENVIRONMENT) {
		case QA:
			return IM_URL_QA;
		case SIMULATER:
			return IM_URL_SIMULATER;
		case ONLINE:
			return IM_URL_ONLINE;
		case DEV:
			return IM_URL_DEV;
		case PERSON:
			return IM_URL_PERSON;
		case DEVPUBLIC:
			return IM_URL_DEVPUBLIC;
		default: // 默认线上
			return IM_URL_ONLINE;
		}
	}

	public static String getConferenceUrl() {
		switch (CONFERENCE_ENVIRONMENT) {
		case QA:
			return CONFERENCE_URL_QA;
		case SIMULATER:
			return CONFERENCE_URL_SIMULATER;
		case ONLINE:
			return CONFERENCE_URL_ONLINE;
		case DEV:
			return CONFERENCE_URL_DEV;
		case PERSON:
			return CONFERENCE_URL_PERSON;
		case DEVPUBLIC:
			return CONFERENCE_URL_DEVPUBLIC;
		default: // 默认线上
			return CONFERENCE_URL_DEVPUBLIC;
		}
	}
	public static String getCommunityUrl() {
		switch (ENVIRONMENT) {
		case QA:
			return COMMUNITY_URL_QA;
		case SIMULATER:
			return COMMUNITY_URL_SIMULATER;
		case ONLINE:
			return COMMUNITY_URL_ONLINE;
		case DEV:
			return COMMUNITY_URL_DEV;
		case PERSON:
			return COMMUNITY_URL_PERSON;
		default: // 默认线上
			return COMMUNITY_URL_ONLINE;
		}
	}
	public static String getCommunityImUrl() {
		switch (ENVIRONMENT) {
		case QA:
			return COMMUNITY_IM_URL_DEV;
		case SIMULATER:
			return COMMUNITY_IM_URL_SIMULATER;
		case ONLINE:
			return COMMUNITY_IM_URL_ONLINE;
		case DEV:
			return COMMUNITY_IM_URL_DEV;
		case PERSON:
			return COMMUNITY_URL_PERSON;
		default: // 默认线上
			return COMMUNITY_IM_URL_ONLINE;
		}
	}
	public static String getTongrenUrl() {
		switch (ENVIRONMENT) {
		case QA:
			return TONGREN_QA;
		case SIMULATER:
			return TONGREN_SIMULATER;
		case ONLINE:
			return TONGREN_ONLINE;
		case DEV:
			return TONGREN_DEV;
		case PERSON:
			return TONGREN_PERSON;
		default: // 默认线上
			return TONGREN_ONLINE;
		}
	}
	public static String getFileUrl() {
		switch (ENVIRONMENT) {
		case QA:
			return FILE_URL_QA;
		case SIMULATER:
			return FILE_URL_SIMULATER;
		case ONLINE:
			return FILE_URL_ONLINE;
		case DEV:
			return FILE_URL_DEV;
		default:
			return FILE_URL_ONLINE;
		}
	}

	/**
	 * 需求文件上传类
	 * 
	 * @return
	 */
	public static String getDemandFileUrl() {
		switch (ENVIRONMENT) {
		case QA:
			return FILE_DEMAND_URL_QA;
		case SIMULATER:
			return FILE_DEMAND_URL_SIMULATER;
		case ONLINE:
			return FILE_DEMAND_URL_ONLINE;
		case DEV:
			return FILE_DEMAND_URL_DEV;
		default:
			return FILE_DEMAND_URL_ONLINE;
		}
	}

	public static String getMeetingPicUrl() {
		switch (ENVIRONMENT) {
		case QA:
			return FILE_MEETING_PICTURE_URL_QA;
		case SIMULATER:
			return FILE_MEETING_PICTURE_URL_SIMULATER;
		case ONLINE:
			return FILE_MEETING_PICTURE_URL_ONLINE;
		case DEV:
			return FILE_MEETING_PICTURE_URL_DEV;
		default:
			return FILE_MEETING_PICTURE_URL_QA;
		}
	}

	public static String getMeetingFileUrl() {
		switch (ENVIRONMENT) {
		case QA:
			return FILE_MEETING_FILE_URL_QA;
		case SIMULATER:
			return FILE_MEETING_FILE_URL_SIMULATER;
		case ONLINE:
			return FILE_MEETING_FILE_URL_ONLINE;
		case DEV:
			return FILE_MEETING_FILE_URL_DEV;
		default:
			return FILE_MEETING_FILE_URL_QA;
		}
	}

	/**
	 * 组织图片上传地址
	 * 
	 * @return
	 */
	public static String getOrganizationUploadUrl() {
		switch (ENVIRONMENT) {
		case QA:
			return PIC_ORG_USER_URL_QA;
		case SIMULATER:
			return PIC_ORG_USER_URL_SIMULATER;
		case ONLINE:
			return PIC_ORG_USER_URL_ONLINE;
		case DEV:
			return PIC_ORG_USER_URL_DEV;
		default:
			return PIC_ORG_USER_URL_DEV;
		}
	}

	/**
	 * 用户图片上传地址
	 * 
	 * @return
	 */
	public static String getAvatarUserUrl() {
		switch (ENVIRONMENT) {
		case QA:
			return AVATAR_USER_URL_QA;
		case SIMULATER:
			return AVATAR_USER_URL_SIMULATER;
		case ONLINE:
			return AVATAR_USER_URL_ONLINE;
		case DEV:
			return AVATAR_USER_URL_DEV;
		default:
			return AVATAR_USER_URL_DEV;
		}
	}

	/** 获取不同环境用户头像的前缀 */
	public static String getAvatarUserUrlHeader() {
		switch (ENVIRONMENT) {
		case SIMULATER:
			return AVATAR_USER_URL_HEADER_SIMULATER;
		case ONLINE:
			return AVATAR_USER_URL_HEADER_ONLINE;
		default:
			return AVATAR_USER_URL_HEADER_SIMULATER;
		}
	}

	/** 获取不同环境金桐脑头像 */
	public static String getAvatarGinTongNaoUrl() {
		switch (ENVIRONMENT) {
		case QA:
			return AVATAR_GIN_TONG_NAO_URL_QA;
		case ONLINE:
			return AVATAR_GIN_TONG_NAO_URL_ONLINE;
		default:
			return AVATAR_GIN_TONG_NAO_URL_ONLINE;
		}
	}

	public static String getAvatarConnsUrl() {
		switch (ENVIRONMENT) {
		case QA:
			return AVATAR_CONNS_URL_QA;
		case SIMULATER:
			return AVATAR_CONNS_URL_SIMULATER;
		case ONLINE:
			return AVATAR_CONNS_URL_ONLINE;
		case DEV:
			return AVATAR_CONNS_URL_DEV;
		default:
			return AVATAR_CONNS_URL_ONLINE;
		}
	}

	public static String getCardUrl() {
		switch (ENVIRONMENT) {
		case QA:
			return CARD_URL_QA;
		case SIMULATER:
			return CARD_URL_SIMULATER;
		case ONLINE:
			return CARD_URL_ONLINE;
		case DEV:
			return CARD_URL_DEV;
		default:
			return CARD_URL_ONLINE;
		}
	}

	// 演示环境
	public static final String CODE_ERROR = "1000";
	public static final String CODE_SUCCESS = "S000";
	public static final String PLATFORM_ID = "111111";

	public static class ReqType {
		public static final int MSG_LOGGER = 19000;// 日志，返回值丢弃
		public static final int REQ_TYPE_BASE = 1000;
		public static final int LOGIN = REQ_TYPE_BASE + 1; // 登录
		public static final int REGISTER = REQ_TYPE_BASE + 2; // 注册
		public static final int GET_VERIFY_CODE = REQ_TYPE_BASE + 3;// 获取验证码
		public static final int FULL_PERSON_MEMBER_INFO = REQ_TYPE_BASE + 4; // 完善个人会员信息
		public static final int FULL_CONTACT_INFO = REQ_TYPE_BASE + 5; // 完善机构联系人信息
		public static final int FULL_ORGANIZATION_AUTH = REQ_TYPE_BASE + 6; // 上传机构验证信息
		public static final int SET_NEW_PASSWORD = REQ_TYPE_BASE + 7; // 设置新密码
		public static final int SEND_VALIDATE_EMAIL = REQ_TYPE_BASE + 8;// 发送验证邮箱
		public static final int ADD_REQUIREMENT = REQ_TYPE_BASE + 9; // 创建新需求
		public static final int GET_REQUIREMENT_BY_ID = REQ_TYPE_BASE + 10; // 需求详情
		public static final int EDIT_REQUIREMENT = REQ_TYPE_BASE + 11; // 编辑需求
		public static final int CLOSE_REQUIREMENT = REQ_TYPE_BASE + 12; // 关闭需求
		public static final int ADD_FRIEND = REQ_TYPE_BASE + 13; // 添加好友
		public static final int ADD_BUSINESS_REQUIREMENT = REQ_TYPE_BASE + 14; // 创建业务需求
		public static final int EDIT_BUSINESS_REQUIREMENT = REQ_TYPE_BASE + 15;// 编辑业务需求
		public static final int CLOSE_BUSINESS_REQUIREMENT = REQ_TYPE_BASE + 16; // 关闭业务需求
		public static final int ADD_TASK = REQ_TYPE_BASE + 17; // 创建任务
		public static final int EDIT_TASK = REQ_TYPE_BASE + 18; // 编辑任务
		public static final int CLOSE_TASK = REQ_TYPE_BASE + 19; // 关闭任务
		public static final int ADD_PROJECT = REQ_TYPE_BASE + 20; // 创建项目
		public static final int EDIT_PROJECT = REQ_TYPE_BASE + 21; // 编辑项目
		public static final int CLOSE_PROJECT = REQ_TYPE_BASE + 22; // 关闭项目
		public static final int UPLOAD_FILE = REQ_TYPE_BASE + 23; // 上传文件
		public static final int FOCUS_REQUIREMENT = REQ_TYPE_BASE + 24; // 关注或取消关注需求
		public static final int LOGIN_OUT = REQ_TYPE_BASE + 25; // 登出
		public static final int LOGIN_CONFIGURATION = REQ_TYPE_BASE + 26; // 登录配置
		public static final int REQUIREMENT_TO_BUSINESS_REQUIREMENT = REQ_TYPE_BASE + 27; // 需求转为业务需求
		public static final int REQUIREMENT_TO_PROJECT = REQ_TYPE_BASE + 28; // 需求转为项目
		public static final int ADD_COMMENT = REQ_TYPE_BASE + 29; // 发表评论
		public static final int GET_LIST_COMMENT = REQ_TYPE_BASE + 30; // 获取评论列表
		public static final int GET_BUSINESS_REQUIREMENT_DETAIL_BY_ID = REQ_TYPE_BASE + 31; // 获取业务需求详情
		public static final int GET_TASK_DETAIL_BY_ID = REQ_TYPE_BASE + 32; // 获取任务详情
		public static final int GET_PROJECT_DETAIL_BY_ID = REQ_TYPE_BASE + 33; // 获取项目详情
		public static final int GET_KNOWLEDGE_DETAIL_BY_ID = REQ_TYPE_BASE + 34; // 获取知识详情
		public static final int GET_MATCH_KNOWLEDGE_MINI = REQ_TYPE_BASE + 35; // 获取匹配知识
		public static final int GET_MATCH_REQUIREMENT_MINI = REQ_TYPE_BASE + 36; // 获取匹配需求
		public static final int GET_KNOWLEDGE_DETAIL = REQ_TYPE_BASE + 37;
		public static final int GET_LIST_REQUIREMENT = REQ_TYPE_BASE + 38; // 需求列表
		public static final int GET_LIST_AFFAIR = REQ_TYPE_BASE + 39; // 事务列表
		public static final int OPERATE_PROJECT = REQ_TYPE_BASE + 40; // 操作项目
		public static final int FINDORG = REQ_TYPE_BASE + 41; // 查询组织信息
		public static final int GET_TREATED_HTML = REQ_TYPE_BASE + 42; // 获取处理后的网页和标题
		public static final int DELETE_FILE = REQ_TYPE_BASE + 43; // 删除附件

		/** 允许非好友浏览我的主页 1 不允许 2 允许 允许非好友对我评价 1 不允许 2 允许 */
		public static final int UPDATE_USER_CONFIG = REQ_TYPE_BASE + 45;
		public static final int UpdateUserConfig = REQ_TYPE_BASE + 46;// 获取用户的二维码内容
		public static final int GET_DYNAMIC_LIST_COMMENT = REQ_TYPE_BASE + 47; // 获取动态评论列表
		public static final int CHANGE_USER_PWD = REQ_TYPE_BASE + 48; // 修改密碼
		public static final int SET_UNBINDING_QQ_WB = REQ_TYPE_BASE + 49; // 解绑QQ/Sina微博
		public static final int SET_BINDING_QQ_WB = REQ_TYPE_BASE + 50; // 绑定QQ/Sina微博
		public static final int SET_MOBILE_EMAIL_WHETHER_CAN_BINDING = REQ_TYPE_BASE + 51; // 判断手机或邮箱状态
																							// 可绑
																							// 验证成功
		public static final int SET_CHECK_EMAIL_STATUS = REQ_TYPE_BASE + 52; // 验证新/老邮箱
																				// 是否验证成功
		public static final int SET_CHECK_MOBILE_STATUS = REQ_TYPE_BASE + 53; // 验证新/老手机
																				// 验证码是否正确
		public static final int THIRD_LOGIN = REQ_TYPE_BASE + 54; // 验证新/老手机
																	// 验证码是否正确
		public static final int SET_REFRESH_ACCOUNT_INFO = REQ_TYPE_BASE + 55; // 刷新账号信息
		public static final int JUDGE_USERANDMAIL = REQ_TYPE_BASE + 56; // 刷新账号信息

		public static final int REQ_TYPE_END = REQ_TYPE_BASE + 60; // //// 终止区域
	}

	public static class ReqUrl {
		public static final String LOGIN_CONFIGURATION = "/login/loginConfiguration.json"; // 登录配置
		public static final String LOGIN = "/login/userLogin.json"; // 登录
		public static final String LOGIN_OUT = "/login/userLoginOut.json"; // 登出
		public static final String REGISTER = "/register/register.json"; // 注册
		public static final String FINDORG = "/org/save/find.json"; // 查询组织
		public static final String GET_VERIFY_CODE = "/register/getVCodeForPassword.json"; // 获取验证码
		public static final String FULL_PERSON_MEMBER_INFO = "/register/fullPersonMemberInfo.json"; // 完善个人会员信息
		public static final String FULL_CONTACT_INFO = "/orginazition/fullContactInfo.json"; // 完善机构会员联系人信息
		public static final String FULL_ORGANIZATION_AUTH = "/orginazition/fullOrganizationAuth.json"; // 上传机构验证信息
		public static final String SEND_VALIDATE_EMAIL = "/register/sendValidateEmail.json"; // 发送验证邮件
		public static final String SET_NEW_PASSWORD = "/register/setNewPassword.json"; // 设置新密码
		public static final String ADD_REQUIREMENT = "/requirement/addRequirement.json"; // 创建新需求
		public static final String GET_REQUIREMENT_BY_ID = "/requirement/getRequirementByID.json"; // 获取需求详情
		public static final String EDIT_REQUIREMENT = "/requirement/editRequirement.json"; // 编辑需求
		public static final String CLOSE_REQUIREMENT = "/requirement/closeRequirement.json"; // 关闭需求
		public static final String FOCUS_REQUIREMENT = "/requirement/focusRequirement.json"; // 关注或取消关注需求
		public static final String REQUIREMENT_TO_BUSINESS_REQUIREMENT = "/businessRequirement/requirementToBusinessRequirement.json"; // 转为业务需求
		public static final String REQUIREMENT_TO_PROJECT = "/project/requirementToProject.json";// 转为项目
		public static final String ADD_FRIEND = "/friend/addFriend.json"; // 添加好友
		public static final String ADD_BUSINESS_REQUIREMENT = "/businessRequirement/addBusinessRequirement.json"; // 创建业务需求
		public static final String EDIT_BUSINESS_REQUIREMENT = "/businessRequirement/editBusinessRequirement.json";// 编辑业务需求
		public static final String CLOSE_BUSINESS_REQUIREMENT = "/businessRequirement/closeBusinessRequirement.json"; // 关闭业务需求
		public static final String ADD_TASK = "/task/addTask.json"; // 创建任务
		public static final String EDIT_TASK = "/task/editTask.json"; // 编辑任务
		public static final String CLOSE_TASK = "/task/closeTask.json"; // 关闭任务
		public static final String ADD_PROJECT = "/project/addProject.json"; // 创建项目
		public static final String EDIT_PROJECT = "/project/editProject.json"; // 编辑项目
		public static final String CLOSE_PROJECT = "/project/closeProject.json"; // 关闭项目
		public static final String UPLOAD_FILE = "goUpload.json"; // 上传文件
		public static final String ADD_COMMENT = "/reply/addComment.json"; // 发表评论
		public static final String JUDGE_USERANDMAIL = "/register/checkOrganRegister.json"; // 判断邮箱或组织全称是否注册
		public static final String GET_LIST_COMMENT = "/reply/getListComment.json"; // 获取评论列表
		public static final String GET_DYNAMIC_LIST_COMMENT = "/dynamicNews/getDynamicComment.json"; // 获取动态评论列表
		public static final String GET_BUSINESS_REQUIREMENT_DETAIL_BY_ID = "/businessRequirement/getBusinessRequirementDetailByID.json"; // 获取业务需求详情
		public static final String GET_TASK_DETAIL_BY_ID = "/task/getTaskDetailByID.json"; // 获取任务详情
		public static final String GET_PROJECT_DETAIL_BY_ID = "/project/getProjectDetailByID.json";// 获取项目详情
		public static final String GET_KNOWLEDGE_DETAIL_BY_ID = "/knowledge/getKnowledgeDetailByID.json"; // 获取知识详情
		public static final String GET_KNOWLEDGE_DETAIL = "/knowledge/getKnowledgeDetail.json"; // 获取知识详情
		public static final String GET_MATCH_KNOWLEDGE_MINI = "/match/getMatchKnowledgeMini.json"; // 获取匹配知识
		public static final String GET_MATCH_REQUIREMENT_MINI = "/match/getMatchRequirementMini.json"; // 获取匹配需求
		public static final String GET_LIST_REQUIREMENT = "/requirement/getListRequirement.json"; // 返回需求列表
		public static final String GET_LIST_AFFAIR = "/affair/getListAffair.json"; // 返回事务列表
		public static final String OPERATE_PROJECT = "/project/operateProject.json"; // 操作项目
		public static final String ADD_MY_KNOWLEDGE = "/knowledge/addMyKnowledge.json"; // 添加我的知识
		// public static final String GET_TREATED_HTML =
		// "http://192.168.101.17:8001/depoapp/"; // 获取处理后的网页(原内网地址)
		public static final String GET_TREATED_HTML = "http://211.103.198.53:2001/depoapp/"; // 获取处理后的网页(外网地址)
		public static final String DELETE_FILE = "/file/delete.json"; // 删除附件
		public static final String GET_PROFESSION_LIST = "/register/getInterestIndustry.json"; // 获取行业列表

		// 关系
		/** 获得 我的好友和人脉 列表 */
		public static final String im_CONNECTIONSLIST = "/connections/getConnections.json";
		/** 获得 通讯录( 人好友 和 组织好友 ) 列表 */
		public static final String getFriends = "/connections/getFriends.json";
		/**
		 * 根据类型获得指定类型关系及 全部类型关系 列表 {'organizationFriend':true,
		 * 'personFriend':true, 'customer':false, 'person':false}
		 */
		public static final String getAllRelations = "/connections/getAllRelations.json";
		public static final String im_ContactDetail = "/connections/getJTContactDetail.json"; // 用户详情
		public static final String im_getOrganizationDetail = "/orginazition/getOrganizationDetail.json"; // 用户详情
		public static final String im_upphonebook = "/connections/upPhoneBook.json"; // 上传电话本
		/** 新上传电话本获得推荐人脉列表 */
		public static final String CheckMobiles = "/mobileApp/checkMobiles.json";
		public static final String im_addJTContact = "/connections/addJTContact.json"; // 添加新人脉
		public static final String im_getnewConnections = "/connections/getNewConnections.json"; // 获得请求新关系
		/** 添加好友 */
		public static final String im_addFriend = "/friend/addFriend.json";
		public static final String im_deleteFriend = "/friend/deleteFriend.json";// 删除好友

		/** 同意添加好友 */
		public static final String im_allowConnectionsRequest = "/friend/allowConnectionsRequest.json";
		public static final String im_getWorkmate = "/friend/getWorkmate.json";// 获取机构成员
		public static final String im_getMatchConnectionsMini = "/match/getMatchConnectionsMini.json";// 获取机构成员

		public static final String im_delJtContact = "/person/removePeople.json";// 新删除人脉地址
		public static final String im_inviteJoinGinTong = "/register/inviteJoinGinTong.json";// 邀请加入金桐
		public static final String im_recommend2Friend = "/friend/recommend2Friend.json";// 推荐
		public static final String im_relevantPeopleAndCustomer = "/friend/relatedPeopleAndCustomer.json";// 推荐
		public static final String im_getNewConnectionsCount = "/connections/getNewConnectionsCount.json";// 新关系规个数
		public static final String im_getNewDynamicCount = "/feed/getNewDynamicCount.json";// 新关系规个数
		public static final String im_addOrginazitionGuest = "/orginazition/addOrginazitionGuest.json";
		public static final String im_holdJTContact = "/connections/holdJTContact.json";
		public static final String im_holdOrginazitionGuest = "/orginazition/holdOrginazitionGuest.json";
		public static final String im_delOrganization = "/orginazition/delOrganization.json";
		public static final String im_getJTContactTemplet = "/connections/getJTContactTemplet.json";
		public static final String getPeopleRelatedResources = "/connections/getPeopleRelatedResources.json";
		public static final String getActionList = "/connections/getActionList.json"; // 获取用户动态
		public static final String getSharePart = "/connections/getSharePart.json"; // 分享人脉的权限
		public static final String getVisible = "/connections/getVisible.json"; // 获取用户
																				// 查看权限
		public static final String setVisible = "/connections/setVisible.json"; // 设置用户查看权限
		public static final String getShareDetail = "/connections/getShareDetail.json"; // 分享后人脉详情
		public static final String findEvaluate = "/mydata/findEvaluate.json";// 获取评价
		public static final String feedbackEvaluate = "/mydata/feedbackEvaluate.json";// 赞同/不赞同评价
		public static final String checkFriend = "/mobileApp/checkFriend.json";// 检查是否为好友
		public static final String moreEvaluate = "/mydata/moreEvaluate.json";// 获取更多评价
		public static final String addEvaluate = "/mydata/addEvaluate.json";// 添加评价
		public static final String deleteEvaluate = "/mydata/deleteEvaluate.json";// 删除评价
		public static final String editBlack = "/user/set/editBlack.json";// 编辑黑名单:加入/移除
		public static final String blacklist = "/user/set/blackList.json";// 获取黑名单列表
		public static final String addDynamic = "/dynamicNews/addDynamic.json";// 转发生产动态
		/** 根据感兴趣的行业推送用户列表 */
		public static final String pushPeopleList = "/register/pushPeopleList.json";
		/** 发送邀请加入金桐短信 */
		public static final String sendSMS = "/mobileApp/sendSMS.json";
		/** 批量添加好友 */
		public static final String addFriends = "/friend/addFriends.json";

		// 会议
		public static final String hy_getrequirement = "/requirement/getListRequirementByUserID.json"; // 根据用户id获取需求列表
		public static final String hy_getknowleadge = "/knowledge/getKnowledgeByTypeAndKeyword.json"; // 根据用户id获取知识列表
		public static final String hy_meetingList = "/my/meetingList.json"; // 获取会议首页详情
																			// 6.1
		public static final String hy_getMyInvitation = "/my/getMyInvitation.json"; // 获取我的邀请列表
																					// 6.2
		public static final String hy_getNoticeList = "/notice/getNoticeList.json"; // 我的通知列表
																					// 6.27
		public static final String deleteNotice = "/notice/deleteNotice.json"; // 6.49
																				// 删除通知
		public static final String deleteMyInvitation = "/my/deleteMyInvitation.json"; // 6.50删除邀请函
		public static final String deleteMeetingMember = "/member/deleteMeetingMember.json"; // 6.53
																								// 删除会议成员
		public static final String finishMeetingTopic = "/topic/finishTopic.json"; // 6.62
																					// 结束议题

		public static final String hy_updateNoticeReadState = "/notice/updateNoticeReadState.json"; // 6.51
																									// 更新通知为已读状态
		// 6.27
		// public static final String hy_getmeetingMemberList =
		// "/member/getmeetingMemberList.json"; //获取参会人 6.10
		// public static final String hy_getmeetingVisitantList =
		// "/member/getmeetingVisitantList.json"; //获取嘉宾 6.12
		public static final String hy_getMeetingSquare = "/my/getMeetingSquare.json"; // 获取广场列表6.23

		public static final String hy_setMeetingMemberReport = "/member/changeAttendMeetStatus.json"; // 接受邀请、拒绝邀请、取消报名
																										// 6.6

		/** 6.5 根据用户id和会议id获取会议详情 */
		public static final String hy_GET_MEETING_DETAIL_BY_ID = "/meeting/getByIdAndMemberId.json";
		public static final String hy_SING_UP_MEETING = "/member/signUpMeeting.json"; // 获取用户的登录状态
		public static final String hy_MY_MEETING = "/my/MyMeeting.json"; // 获取我的会议
																			// 6.17
		public static final String hy_RequiredSignupInfo = "/member/getRequiredSignupInfo.json"; // 获取会议必填信息
																									// 6.31
		public static final String hy_improveSignInformation = "/member/improveSignInformation.json"; // 完善报名信息
		public static final String hy_invitationByFaceToFace = "/member/add.json"; // 当面邀请6.4
		// public static final String hy_getMeetingTopicListById =
		// "/topic/getMeetingTopicList.json"; //当面邀请6.11
		public static final String hy_updateTopic = "/topic/updateTopic.json"; // 当面邀请6.9
		public static final String hy_signInMeeting = "/member/signInMeeting.json"; // 会议签到
																					// 6.16
		public static final String hy_signUpReview = "/member/signUpReview.json"; // 报名审核
																					// 6.17
																					// public
																					// static
																					// final
																					// String
																					// hy_deleteNotBeginMeeting
																					// =
																					// "/meeting/deleteNotBeginMeeting.json";
																					// //
																					// 删除我创建的会议
		public static final String hy_deleteNotBeginMeeting = "/meeting/delete.json"; // 删除我创建的会议（及我保存的）
																						// 6.13
		public static final String hy_changeMyMemberMeetStatus = "/my/changeMyMemberMeetStatus.json"; // 归档,删除我的会议
																										// 6.14
		public static final String hy_deleteNote = "/note/deleteNote.json"; // 删除会议笔记
																			// 6.18
		public static final String hy_getNoteByMeetingId = "/note/getNoteByMeetingId.json"; // 查询会议笔记
																							// 6.32
		public static final String hy_changeMeetingStatus = "/meeting/changeMeetingStatus.json"; // 修改会议状态
																									// 6.20
		// public static final String hy_getFileListByTaskId =
		// "/meeting/getFileListByTaskId.json"; //获取上传文件接口 6.21
		public static final String hy_addMeetingLabel = "/label/addMeetingLabel.json"; // 新建自定义标签
																						// 6.24
		public static final String hy_getMeetingLabelByCreateId = "/label/getMeetingLabelByCreateId.json"; // 获取用户自定义标签列表
																											// 6.25
		public static final String hy_update = "/meeting/upate.json"; // 修改会议
																		// 6.22
		public static final String hy_saveMeetingNote = "/note/addNote.json"; // 保存会议笔记
																				// 6.22
		public static final String hy_getMyForwardingMeeting = "/my/getMyForwardingMeeting.json"; // 获取用户可转发的会议列表

		/** 6.3 创建会议 */
		public static final String hy_create_meeting = "/meeting/add.json";
		public static final String hy_SendMeetingChat = "/mobile/im/sendMeetingMessage.action"; // 发送消息
		public static final String hy_GetMeetingMessage = "/mobile/im/getMeetingChat.action"; // 获取会议聊天记录
		public static final String hy_collectKnowledge = "/knowledge/collectKnowledge.json"; // 收藏知识
																								// 3.10
		public static final String hy_focusRequirement = "/requirement/focusRequirement.json"; // 关注需求
																								// 2.56
		public static final String hy_addNoteDetailByChat = "/note/addNoteDetailByChat.json"; // 会议畅聊添加会议笔记明细
		public static final String hy_globalSearchByMeeting = "/meeting/seach.json"; // 6.26会议全局搜索
		public static final String hy_getMeetingAndChat = "/meeting/index.json"; // 6.31
		public static final String hy_doEntryMeetingNotice = "/topic/enteredMeetingNotice.json";
		// public static final String hy_doGetSocialList =
		// "/meeting/socialList.json"; // 获取社交列表
		public static final String hy_doGetSocialList2 = "/meeting/socialList2.json"; // 获取社交列表
		public static final String hy_doGetSocialList3 = "/meeting/socialList3.json"; // 获取社交列表
		public static final String hy_doGetMeetList = "/meeting/meetingList.json"; // 获取会议列表
		public static final String hy_removeSocial = "/meeting/removeSocial.json"; // 删除社交列表

		public static final String hy_deletePhoto = "/meeting/delete.json";

		// 知识接口
		public static final String CreateKnowledge = "/knowledge/createKnowledge.json"; // 创建知识
		public static final String GetKnowledgeByTypeAndKeyword = "/knowledge/getKnowledgeByTypeAndKeyword.json"; // 根据类型（全部、我收藏的、分享给我的、我创建的）和关键字分页获取知识列表
		public static final String GetKnowledgeByTagAndKeyword = "/knowledge/getKnowledgeByTagAndKeyword.json";// 根据标签名和关键字分页获取知识列表
		public static final String GetKnowledgeByUserCategoryAndKeyword = "/knowledge/getKnowledgeByUserCategoryAndKeyword.json";// 根据目录id和关键字分页获取知识列表
		public static final String DeleteKnowledgeById = "/knowledge/deleteKnowledgeById.json"; // 删除指定的知识
		/** 更新知识url */
		public static final String updateKnowledge = "/knowledge/updateKnowledge.json";
		public static final String GetKnowledgeByColumnAndSource = "/knowledge/getKnowledgeByColumnAndSource.json"; // 根据栏目和来源获取知识列表
		public static final String GetKnoDetails = "/knowledge/getKnowledgeDetails.json";// 知识详情
		public static final String AddKnowledgeComment = "/knowledge/addKnowledgeComment.json";// 对知识或评论发表评论
		public static final String GetKnowledgeComment = "/knowledge/getKnowledgeComment.json";// 获取知识或评论的评论

		public static final String GetUserCategory = "/knowledge/getUserCategory.json"; // 获取知识目录
		public static final String EditUserCategory = "/knowledge/editUserCategory.json"; // 编辑知识目录
		public static final String AddUserCategory = "/knowledge/addUserCategory.json"; // 添加知识目录
		public static final String DelUserCategory = "/knowledge/deleteUserCategory.json"; // 删除知识目录

		public static final String GetKnowledgeTagList = "/knowledge/getKnowledgeTagList.json"; // 获取知识标签
		public static final String EditUserKnowledgeTag = "/knowledge/editUserKnowledgeTag.json"; // 编辑知识标签
		public static final String EditKnoTagByKnoId = "/knowledge/editKnowledgeTagById.json";// 编辑指定知识的标签
		public static final String GetKnoCommentsByType = "/reply/getListComment.json";// 获取各种详情页的评论列表，
																						// 主要包括需求、业务需求、任务、项目
		public static final String UpdateCollectKnowledge = "/knowledge/collectKnowledge.json";// 收藏知识
		public static final String GetCollectKnowledgeState = "/jonit/knowledge/collectKnowledge.json";// 得到收藏知识的状态

		public static final String GetColumnByUserId = "/knowledge/columnManager/getColumnByUserId.json"; // 获取用户的栏目
		public static final String GetSubscribedColumnByUserId = "/knowledge/columnManager/getSubscribedColumnByUserId.json"; // 获取用户订阅的栏目
		public static final String EditSubscribedColumn = "/knowledge/columnManager/editSubscribedColumn.json"; // 订阅/取消订阅栏目
		public static final String UpdateSubscribedColumn = "/knowledge/columnManager/updateSubscribedColumn.json"; // 更新/排序
																													// 订阅的栏目

		// 通用接口
		public static final String GetRelatedResource = "/knowledge/getKnowledgeRelatedResources.json"; // 获取目标资源的关联资源
		public static final String GetJointResources = "/resource/getRelatedResources.json"; // 获取生态对接的资源
		public static final String CorrectJointResult = "/knowledge/correctJointResult.json"; // 生态资源纠错
		public static final String FetchExternalKnowledgeUrl = "/knowledge/fetchExternalKnowledgeUrl.json"; // 解析Url类型的知识
		public static final String GetInterestIndustry = "/register/getInterestIndustry.json"; // 获取感兴趣的行业列表
		public static final String GetMyCountList = "/friend/myCountList.json"; // 获取我的页面的书；数量信息
		public static final String GetCheckFriend = "/mobileApp/fullPersonMemberInfo.json"; // 获取我的页面的书；数量信息
		public static final String fileDocumentDelete = "/fileManager/deleteBoth.json"; // 删除目录文件
		public static final String searchFileDocument = "/fileManager/searchRCategory.json"; // 搜索目录文件
		public static final String queryAllRCategory = "/fileManager/queryAllRCategory.json"; // 查询文件目录关系
																								// fileManager/queryAllRCategory.json¶

		// 设置访问权限

		/** 允许非好友浏览我的主页 1 不允许 2 允许 允许非好友对我评价 1 不允许 2 允许 */
		public static final String UpdateUserConfig = "/user/set/updateUserConfig.json";
		public static final String getUserQRUrl = "/mobileApp/getUserQRUrl.json";// 获取用户的二维码内容
		public static final String uploadUserProfile = "/register/perfectPersonMemberInfo.json";// 上传用户基本资料
		public static final String getCountryCode = "/mobileApp/getCountryCode.json";// 获取国家列表
		public static final String setCustomMade = "/user/set/customMade.json";// 获取国家列表

		public static final String GetMyForwardingSocial = "/meeting/getMyForwardingSocial.json"; // 获取我的社交转发列表
		public static final String GetForwardMeetingData = "/meeting/getForwardingMeetingData.json"; // 获取会议详情

		/** 需求 模块接口 */
		public static final String Demand_GetTemplatelist = "demandtemplate/demandtemplateListAll.json";// 获取需求模板
		public static final String Demand_SaveTemplate = "demandtemplate/createdemandtemplate.json";// 保存模版对象
		public static final String Demand_deletedemandtemplate = "demandtemplate/deletedemandtemplate.json";// 删除模版
		public static final String Demand_GetMyNeedList = "demand/mydemandQuery.json";// 我的需求
		public static final String Demand_mydemandDelete = "demand/mydemandDelete.json";// 删除我的需求接口
		public static final String Demand_GetProjectList = "demand/getDemandList.json";// 找项目/资金getDemandList

		public static final String Demand_saveTag = "demand/tagSaveOrUpdate.json";// 修改或删除标签接口
		public static final String Demand_getDemandDetail = "demand/getDemandDetail.json";// 获取需求详情接口
		public static final String Demand_getTagQuery = "demand/rTagQuery.json";// 查询标签和需求数量
		public static final String Demand_mydemandSearch = "demand/mydemandSearch.json"; // 搜索我的需求界面
																							// 调
																							// ----------------
		public static final String Demand_tagList = "tag/list.json";// 获取推荐标签
		public static final String Demand_categoryQueryTree = "demand/categoryQueryTree.json";// 获取目录信息接口
		public static final String Demand_deleteTag = "demand/tagDelete.json";// 删除标签接口
		public static final String Demand_QueryTag = "demand/tagQuery.json";// 查询我的标签信息
		public static final String Demand_createCategory = "demand/categorySaveOrUpdate.json";// 新增或者修改用户目录
		public static final String Demand_deleteCategory = "demand/categoryDelete.json";// 删除用户目录
		public static final String Demand_QueryCategory = "demand/categoryQuery.json";// 查询用户目录信息
		public static final String Demand_rTagSave = "demand/rTagSave.json";// 保存需求标签关系(批量打标签)
		public static final String Demand_rTagDelete = "demand/rTagDelete.json";// 删除需求标签关系。
		public static final String Demand_rTagQueryByTagId = "demand/rTagQueryByTagId.json";// 查询需求标签关系
		public static final String Demand_rCategorySave = "demand/rCategorySave.json";// 保存需求目录关系（批量设置目录）
		public static final String Demand_rCategoryDelete = "demand/rCategoryDelete.json";// 删除需求目录关系
		public static final String Demand_rCategoryQuery = "demand/rCategoryQuery.json";// 查询需求目录关系
		public static final String Demand_createDemand = "demand/createDemand.json";// 创建需求
		public static final String Demand_updateDemand = "demand/updateDemand.json";// 修改需求
		public static final String Demand_findDemandFile = "demand/findDemandFile.json";// 查看需求介绍
		public static final String Demand_DemandDetail = "demand/getDemandDetail.json";// 需求详情
		public static final String Demand_saveOthersDemand = "demandOpt/saveOthersDemand.json";// 保存需求
		public static final String Demand_collectOthersDemand = "demandOpt/collectOthersDemand.json";// 收藏需求详情
		public static final String Demand_deleteMyDemand = "demand/deleteMyDemand.json";// 删除需求
		public static final String Demand_addDemandComment = "demandComment/addDemandComment.json";// 需求评论
		public static final String Demand_deleteDemandComment = "demandComment/deleteDemandComment.json";// 删除需求评论
		public static final String Demand_DemandCommentList = "demandComment/getDemandCommentList.json";// 获取需求评论列表
		public static final String Demand_reportDemand = "demandReport/reportDemand.json";// 需求举报
		public static final String Demand_getDemandASSO = "demand/getDemandASSO.json";// 需求详情页获取关联信息
		public static final String Demand_findDemand = "demand/findDemand.json";// 查看需求
		public static final String Demand_deleteFile = "demand/deleteFile.json";// 删除附件接口
		/** 获取标签的内容数量 */
		public static final String KNOWLEDGE_REQ_TAG_QUERY = "/customer/tag/group.json"; // 查询标签和数量
		/* 查询用户目录（返回整棵树） */
		public static final String CATEGORY_REQ_TAG_QUERY = "demand/categoryQueryTree.json";
		public static final String CATEGORY_SAVEORUPDATE = "/fileManager/saveOrUpdateCategory.json";
		public static final String CATEGORY_DOCUMENT_SUM = "/fileManager/sum.json";
		public static final String RENAME_FILE = "/fileManager/renameFile.json";

		public static final String CHAT_SAVE_CATEGORY = "/fileManager/saveOrUpdateRCategory.json"; // 保存文件目录关系
		// 第三方登录
		public static final String THIRD_LOGIN = "/userthirdlogin/isbind.json"; // 第三方登录
		/* 设置--账户--账户信息 */
		public static final String SET_REFRESH_ACCOUNT_INFO = "/login/refreshJtmember.json"; // 刷新账号信息
		public static final String SET_UnBinding_QQ_WB = "/set/release.json"; // 解绑QQ/Sina微博
		public static final String SET_Binding_QQ_WB = "/userthirdlogin/bind.json"; // 绑定QQ/Sina微博
		public static final String SET_MOBILE_EMAIL_WHETHER_CAN_BINDING = "/set/existNew.json"; // 判断手机或邮箱是否可绑
		public static final String SET_CHECK_EMAIL_STATUS = "/set/checkMailByStatus.json"; // 验证新/老邮箱
																							// 是否验证成功
		public static final String SET_CHECK_MOBILE_STATUS = "/set/checkMobileByStatus.json"; // 验证新/老手机
																								// 是否验证成功
		public static final String SET_SEND_VALIDATE_EMAIL = "/set/sendMailByStatus.json"; // 发邮件到新/老邮箱
	}

	/**
	 * 需求信息
	 * 
	 * @author Administrator
	 * 
	 */
	public static class demandReqType {
		public static final int demand_REQ_BASE = 5000;
		public static final int demand_REQ_END = 5300;
		public static final int demand_gettemplatelist = demand_REQ_BASE + 1;// 获取模版列表
		public static final int demand_saveTemplate = demand_REQ_BASE + 2;// 保存模版接口
		public static final int demand_deletedtemplate = demand_REQ_BASE + 3;// 删除模版接口；
		public static final int demand_getProjectList = demand_REQ_BASE + 4;// 获取找项目或找资金
		public static final int demand_getMyNeedList = demand_REQ_BASE + 5;// 获取我的需求列表
		public static final int demand_mydemandDelete = demand_REQ_BASE + 6;// 删除我的需求接口
		public static final int demand_getDemandDetail = demand_REQ_BASE + 7;// 获取需求详情界面
		public static final int demand_saveTag = demand_REQ_BASE + 8;// 修改和保存标签接口
		public static final int demand_getTagQuery = demand_REQ_BASE + 9;// 查询标签和需求数量
		public static final int demand_tag_list = demand_REQ_BASE + 10;// 获取金桐推荐标签信息
		public static final int demand_categoryQueryTree = demand_REQ_BASE + 11;// 获取目录信息接口
		public static final int demand_deleteTag = demand_REQ_BASE + 12;// 删除标签接口
		public static final int demand_QueryTag = demand_REQ_BASE + 13;// 查询我的标签
		public static final int demand_createCategory = demand_REQ_BASE + 14;// 新增或修改用户目录
		public static final int demand_deleteCategory = demand_REQ_BASE + 15;// 删除目录
		public static final int demand_rTagSave = demand_REQ_BASE + 16;// 保存需求标签关系(批量打标签)
		public static final int demand_rTagDelete = demand_REQ_BASE + 17;// 删除标签关系
		public static final int demand_rTagQueryByTagId = demand_REQ_BASE + 18;// 查询需求标签关系
		public static final int demand_rCategorySave = demand_REQ_BASE + 19;// 保存需求目录关系（批量设置目录）
		public static final int demand_rCategoryDelete = demand_REQ_BASE + 20;// 删除需求目录关系
		public static final int demand_rCategoryQuery = demand_REQ_BASE + 21;// 查询需求目录关系
		public static final int demand_createDemand = demand_REQ_BASE + 22;// 创建需求
		public static final int demand_updateDemand = demand_REQ_BASE + 23;// 编辑需求详情
		public static final int demand_findDemandFile = demand_REQ_BASE + 24;// 查询需求介绍内容
		public static final int demand_DemandDetail = demand_REQ_BASE + 25;// 查询需求详情
		public static final int demand_saveOthersDemand = demand_REQ_BASE + 26;// 保存需求接口
		public static final int demand_collectOthersDemand = demand_REQ_BASE + 27;// 收藏需求详情
		public static final int demand_deleteMyDemand = demand_REQ_BASE + 28;// 删除我的需求
		public static final int demand_DemandCommentList = demand_REQ_BASE + 29;// 需求评论列表
		public static final int demand_reportDemand = demand_REQ_BASE + 30;// 需求举报接口
		public static final int demand_getDemandASSO = demand_REQ_BASE + 31;// 需求详情页获取关联信息
		public static final int demand_findDemand = demand_REQ_BASE + 32;// 查看需求
		public static final int demand_deleteFile = demand_REQ_BASE + 33;// 删除附件
		public static final int demand_addDemandComment = demand_REQ_BASE + 34;// 发布评论
	}

	public static class concReqType {
		public static final int conc_REQ_BASE = 3199;//
		public static final int conc_REQ_END = 3300;//
		/** 获得 我的好友和人脉 列表 */
		public static final int CONNECTIONSLIST = conc_REQ_BASE + 1;
		public static final int ContactDetail = conc_REQ_BASE + 2; // 好友详情
		public static final int im_upphonebook = conc_REQ_BASE + 3; // 上传电话本
		public static final int im_addJTContact = conc_REQ_BASE + 4;// 添加人脉
		public static final int im_getnewConnections = conc_REQ_BASE + 5;// 添加人脉
		public static final int im_addFriend = conc_REQ_BASE + 6;// 添加好友
		public static final int im_deleteFriend = conc_REQ_BASE + 7;// 删除好友
		public static final int im_allowConnectionsRequest = conc_REQ_BASE + 8;// 删除好友
		public static final int im_getWorkmate = conc_REQ_BASE + 9;// 获取机构成员
		public static final int im_getOrganizationDetail = conc_REQ_BASE + 10;// 获取机构详情
		public static final int im_getMatchConnectionsMini = conc_REQ_BASE + 11;// 获取匹配关系列表
		public static final int im_delJtContact = conc_REQ_BASE + 12; // 删除人脉
		public static final int im_inviteJoinGinTong = conc_REQ_BASE + 13;// 邀请
		public static final int im_recommend2Friend = conc_REQ_BASE + 14;// 推荐
		public static final int im_relevantPeopleAndCustomer = conc_REQ_BASE + 15;// 推荐
		public static final int im_getNewConnectionsCount = conc_REQ_BASE + 17;// 新关系个数提示
		public static final int im_getNewDynamicCount = conc_REQ_BASE + 18;// 新动态个数提示
		public static final int im_addOrginazitionGuest = conc_REQ_BASE + 19;// 新动态个数提示
		public static final int im_holdJTContact = conc_REQ_BASE + 20;// 分享的人脉保存在本地
		public static final int im_holdOrginazitionGuest = conc_REQ_BASE + 21;// 分享的人脉保存在本地
		public static final int im_delOrganization = conc_REQ_BASE + 22;// 分享的人脉保存在本地
		public static final int im_getJTContactTemplet = conc_REQ_BASE + 23;// 分享的人脉保存在本地
		public static final int getPeopleRelatedResources = conc_REQ_BASE + 24; // 获取人脉相关资源
		public static final int getActionList = conc_REQ_BASE + 25; // 获取用户动态
		public static final int getSharePart = conc_REQ_BASE + 26; // 分享人脉的权限
		public static final int getVisible = conc_REQ_BASE + 27; // 获取用户 查看权限
		public static final int setVisible = conc_REQ_BASE + 28; // 设置 用户 查看权限
		public static final int getShareDetail = conc_REQ_BASE + 29; // 分享后人脉详情
		public static final int findEvaluate = conc_REQ_BASE + 30; // 获取评价
		public static final int feedbackEvaluate = conc_REQ_BASE + 31; // 获取评价
		public static final int checkFriend = conc_REQ_BASE + 32; //
		public static final int moreEvaluate = conc_REQ_BASE + 33; // 获取更多评价\
		/** 新上传电话本获得推荐人脉列表 */
		public static final int CheckMobiles = conc_REQ_BASE + 34;
		public static final int addEvaluate = conc_REQ_BASE + 35; // 添加评价
		public static final int deleteEvaluate = conc_REQ_BASE + 36; // 删除评价
		public static final int editBlack = conc_REQ_BASE + 37; // 编辑黑名单
		public static final int addDynamic = conc_REQ_BASE + 38; // 转发生成动态
		/** 根据感兴趣的行业推送用户列表 */
		public static final int pushPeopleList = conc_REQ_BASE + 39;
		/** 发送邀请加入金桐短信 */
		public static final int sendSMS = conc_REQ_BASE + 40;
		public static final int blacklist = conc_REQ_BASE + 41;
		/** 批量添加好友 */
		public static final int addFriends = conc_REQ_BASE + 42;
		/** 获得 通讯录( 人好友 和 组织好友 ) 列表 */
		public static final int getFriends = conc_REQ_BASE + 43;
		/** 根据类型获得指定类型关系及 全部类型关系 列表 */
		public static final int getAllRelations = conc_REQ_BASE + 44;
	}

	// IM相关
	public static class IMReqType {
		public static final int IM_REQ_BASE = 3000; //
		public static final int IM_REQ_END = 3099; //
		public static final int IM_REQ_SEND_MESSAGE = IM_REQ_BASE + 1;
		public static final int IM_REQ_CREATE_MUC = IM_REQ_BASE + 2;
		public static final int IM_REQ_EXIT_MUC = IM_REQ_BASE + 3;
		public static final int IM_REQ_INVITE2MUC = IM_REQ_BASE + 4;
		public static final int IM_REQ_GET_LIST_MUC = IM_REQ_BASE + 5;
		public static final int IM_REQ_KICKFROMMUC = IM_REQ_BASE + 6;
		public static final int IM_REQ_GET_MUC_DETAIL = IM_REQ_BASE + 7;
		public static final int IM_REQ_GET_MUC_MESSAGE = IM_REQ_BASE + 8;
		public static final int IM_REQ_GET_CHAT_MESSAGE = IM_REQ_BASE + 9;
		public static final int IM_REQ_SET_CHANNELID = IM_REQ_BASE + 10;
		public static final int IM_REQ_GET_LISTIM = IM_REQ_BASE + 11;
		public static final int IM_REQ_MODIFY_MUC = IM_REQ_BASE + 12;
		public static final int IM_REQ_MODIFY_CONFERENCE = IM_REQ_BASE + 13;
		public static final int IM_DELETE_IM_FROM_LIST = IM_REQ_BASE + 14;
		public static final int IM_REQ_CLEAN_MESSAGE = IM_REQ_BASE + 15;
		public static final int IM_GET_NEW_GROUP_COUNT = IM_REQ_BASE + 16;
		public static final int IM_REQ_GET_MEETING_DETAIL = IM_REQ_BASE + 17;
		/* (多选)转发到社交 */
		public static final int IM_REQ_SEND_MESSAGE_FOR_FORWARD_SOCIAL = IM_REQ_BASE + 18;
		public static final int IM_REQ_CLEAR_UNREAD_MESSAGENUMBER = IM_REQ_BASE + 19;
		public static final int IM_REQ_CLIENTDELETEMESSAGE = IM_REQ_BASE + 20;
		public static final int IM_REQ_FETCHHISTORYMESSAGE_CHAT = IM_REQ_BASE + 21;
		public static final int IM_REQ_FETCHHISTORYMESSAGE_MUC = IM_REQ_BASE + 22;
		public static final int IM_REQ_FETCHFIRENDS = IM_REQ_BASE + 23;
	}

	// 知识请求相关
	public static class KnoReqType {
		public static final int ReqBase = 3300;
		public static final int ReqEnd = 3399;
		public static final int CreateKnowledge = ReqBase + 1; // 发布知识
		public static final int GetUserCategory = ReqBase + 2; // 获取知识目录
		public static final int AddUserCategory = ReqBase + 3; // 添加知识目录
		public static final int DelUserCategory = ReqBase + 4; // 删除知识目录
		public static final int GetKnowledgeTagList = ReqBase + 5; // 获取知识标签列表
		public static final int EditUserKnowledgeTag = ReqBase + 6; // 编辑知识标签
		public static final int GetColumnByUserId = ReqBase + 7; // 获取用户栏目
		public static final int GetSubscribedColumnByUserId = ReqBase + 8; // 获取用户订阅的栏目
		public static final int EditSubscribedColumn = ReqBase + 9; // 订阅/取消订阅
		public static final int UpdateSubscribedColumn = ReqBase + 10; // 更新用户订阅的栏目
		public static final int GetKnoDetails = ReqBase + 11;// 知识详情
		public static final int EditKnoTagByKnoId = ReqBase + 12;// 编辑指定知识的标签
		public static final int GetKnowledgeByTypeAndKeyword = ReqBase + 13; // 根据类型（全部、我收藏的、分享给我的、我创建的）和关键字分页获取知识列表
		public static final int DeleteKnowledgeById = ReqBase + 14; // 删除指定的知识

		public static final int GetKnoCommentsByType = ReqBase + 15;// 获取各种详情页的评论列表，
																	// 主要包括需求、业务需求、任务、项目
		public static final int UpdateCollectKnowledge = ReqBase + 16;// 收藏知识
		public static final int GetCollectKnowledgeState = ReqBase + 17;// 收藏知识

		public static final int EditUserCategory = ReqBase + 18; // 编辑知识目录
		public static final int GetKnowledgeByColumnAndSource = ReqBase + 19; // 根据栏目和来源获取知识列表

		public static final int AddKnowledgeComment = ReqBase + 20;// 对知识或评论发表评论
		public static final int GetKnowledgeComment = ReqBase + 21;// 获取知识或评论的评论

		public static final int GetKnowledgeByTagAndKeyword = ReqBase + 22;// 根据标签名和关键字分页获取知识列表
		public static final int GetKnowledgeByUserCategoryAndKeyword = ReqBase + 23;// 根据目录id和关键字分页获取知识列表
		public static final int FetchExternalKnowledgeUrl = ReqBase + 24;// 解析Url类型的知识
		public static final int doGetKnoDetailsBySaveKno = ReqBase + 25;// 解析Url类型的知识
		/** 更新知识 */
		public static final int updateKnowledge = ReqBase + 25;
		/** 获取标签内容数量 */
		public static final int KNOWLEDGE_REQ_TAG_QUERY = ReqBase + 26;
	}

	// 通用接口请求
	public static class CommonReqType {
		public static final int ReqBase = 3400;
		public static final int ReqEnd = 3599;
		public static final int GetRelatedResource = ReqBase + 1; // 获取目标资源的关联资源
		public static final int GetJointResource = ReqBase + 2; // 获取对接的资源
		public static final int CorrectJointResult = ReqBase + 3;// 对接纠错
		public static final int FetchExternalKnowledgeUrl = ReqBase + 4; // 解析url
		public static final int GetProfessionList = ReqBase + 5; // 获取职业列表
		public static final int GetInterestIndustry = ReqBase + 6; // 获取感兴趣的行业列表
		public static final int GetMyCountList = ReqBase + 7; // 获取感兴趣的行业列表
		public static final int UploadUserProfile = ReqBase + 8; // 上传用户基本资料
		public static final int CHECK_FRIEND = ReqBase + 9; // 上传用户基本资料
		public static final int getUserQRUrl = ReqBase + 10; // 上传用户基本资料
		public static final int getCountryCode = ReqBase + 11; // 获取国家资料
		public static final int setCustomMade = ReqBase + 12; // 定制设置
		public static final int getCategoryQueryTree = ReqBase + 13; // 查询用户目录
		public static final int categorySaveOrUpdate = ReqBase + 14; // 新增或者修改用户目录
		public static final int GetJointResource_New = ReqBase + 15; // 获取对接的资源
		public static final int GetJointResource_MY = ReqBase + 16; // 获取对接的资源
		public static final int GetJointResource_FRIEND = ReqBase + 17; // 获取对接的资源
		public static final int GetJointResource_GT = ReqBase + 18; // 获取对接的资源
	}

	// 用户、im模块之外的其他api
	public static class HomeReqType {
		public static final int HOME_REQ_BASE = 3100;//
		public static final int HOME_REQ_END = 3199;//

		public static final int HOME_REQ_GET_FLOW = HOME_REQ_BASE + 1;
		public static final int HOME_REQ_ADD_COMMENT = HOME_REQ_BASE + 2;
		public static final int HOME_REQ_GET_MY_REQUIREMENT = HOME_REQ_BASE + 3;
		public static final int HOME_REQ_GET_MY_AFFAIR = HOME_REQ_BASE + 4;
		public static final int HOME_REQ_GET_MY_KNOWLEDGE = HOME_REQ_BASE + 5;
		public static final int HOME_REQ_GET_SEARCH_LIST = HOME_REQ_BASE + 6;
		public static final int HOME_REQ_GET_SEARCH_LIST_MEETING = HOME_REQ_BASE + 7;
		public static final int HOME_REQ_GET_DYNAMIC = HOME_REQ_BASE + 8;
		public static final int HOME_REQ_ADD_DYNAMIC_COMMENT = HOME_REQ_BASE + 9;
		public static final int HOME_REQ_DELETE_DYNAMIC_COMMENT = HOME_REQ_BASE + 10;
		public static final int HOME_REQ_ADD_DYNAMIC_PRAISE = HOME_REQ_BASE + 11;// 赞同动态
		public static final int HOME_REQ_CANCEL_DYNAMIC_PRAISE = HOME_REQ_BASE + 12;// 取消赞同动态
		public static final int HOME_REQ_GETCONNECTORORG = HOME_REQ_BASE + 13;// 获取是否是人脉还是组织
		public static final int HOME_REQ_GET_SUGGESTION_TYPE = HOME_REQ_BASE + 14;// 获取建议类型
		public static final int HOME_REQ_ADD_SUGGESTION = HOME_REQ_BASE + 15;// 增加建议
//<<<<<<< HEAD
//		
//		public static final int HOME_REQ_GET_SEARCH_INDEX_LIST = HOME_REQ_BASE + 16;
//
//=======
		public static final int HOME_REQ_ADD_FLOW = HOME_REQ_BASE + 16;
		public static final int HOME_REQ_GET_MY_DYNAMIC = HOME_REQ_BASE + 17;
		public static final int HOME_REQ_ADD_APPORVE = HOME_REQ_BASE + 18;//点赞
		public static final int HOME_REQ_ADD_GT_DYNAMIC_COMMENT = HOME_REQ_BASE + 19;
		public static final int HOME_REQ_ADD_GT_APPORVE = HOME_REQ_BASE + 20;//金桐脑点赞
		public static final int HOME_REQ_ADD_DYNAMIC_COMMENT_NEW = HOME_REQ_BASE + 21;//添加评论
		public static final int HOME_REQ_DEL_APPORVE = HOME_REQ_BASE + 22;//取消点赞
		public static final int HOME_REQ_DELETE_GT_DYNAMIC_COMMENT = HOME_REQ_BASE + 23;
		public static final int HOME_REQ_DEL_GT_APPORVE = HOME_REQ_BASE + 24;//金桐脑取消点赞
		public static final int HOME_REQ_DELETE_DYNAMIC_COMMENT_NEW = HOME_REQ_BASE + 25;//删除评论
		public static final int HOME_REQ_GET_DYNAMIC_ASSO = HOME_REQ_BASE + 26;//删除评论
		public static final int HOME_REQ_DEL_FLOW = HOME_REQ_BASE + 27;
		public static final int HOME_REQ_GET_GT_DYNAMIC = HOME_REQ_BASE + 28;
		public static final int HOME_REQ_GET_SEARCH_INDEX_LIST = HOME_REQ_BASE + 29;
		public static final int HOME_PAGE_LIST = HOME_REQ_BASE + 30;//获取首页数据类型
		public static final int HOME_REQ_GET_SEARCH_LIST_PERSON = HOME_REQ_BASE + 31;//人脉
		public static final int HOME_REQ_GET_SEARCH_LIST_ORG = HOME_REQ_BASE + 32;//组织
		public static final int HOME_REQ_GET_SEARCH_LIST_KNOWLEDGE = HOME_REQ_BASE + 33;//知识
		public static final int HOME_REQ_GET_SEARCH_LIST_MEET = HOME_REQ_BASE + 34;//会议
		public static final int HOME_REQ_GET_SEARCH_LIST_DEMEND = HOME_REQ_BASE + 35;//需求
	}

	// 文件目录管理
	public static class CloudDiskType {
		public static final int CLOUD_DISK_REQ_BASE = 8000;//
		public static final int CLOUD_DISK_REQ_END = 8100;//
		public static final int fileDocumentDelete = CLOUD_DISK_REQ_BASE + 1; // 删除用户文件或目录
		public static final int queryAllRCategory = CLOUD_DISK_REQ_BASE + 2; // 查询文件目录关系
		public static final int CHAT_SAVE_CATEGORY = CLOUD_DISK_REQ_BASE + 3; // 保存文件目录关系
		public static final int searchFileDocument = CLOUD_DISK_REQ_BASE + 4; // 搜索文件目录
		public static final int categorySaveOrUpdate = CLOUD_DISK_REQ_BASE + 5; // 新增或修改文件目录
		public static final int categoryDocumentSum = CLOUD_DISK_REQ_BASE + 6; // 获取所有文件的大小
		public static final int renamefile = CLOUD_DISK_REQ_BASE + 7; // 重命名文件
	}

	public static class ConferenceReqType {
		public static final int CONFERENCE_REQ_BASE = 4000;//
		public static final int CONFERENCE_REQ_END = 4500;//
		public static final int CONFERENCE_REQ_MEETING_HOME = CONFERENCE_REQ_BASE + 1; // 获取会议首页详情
																						// 6.1
		public static final int CONFERENCE_REQ_MY_INVITION = CONFERENCE_REQ_BASE + 2; // 获取我的邀请列表
																						// 6.2
		public static final int CONFERENCE_REQ_MY_NOYICE = CONFERENCE_REQ_BASE + 3; // 我的通知列表
																					// 6.27
		public static final int CONFERENCE_REQ_MEETINT_MENBER = CONFERENCE_REQ_BASE + 4; // 获取参会人
																							// 6.10
		public static final int CONFERENCE_REQ_MEETING_GUEST = CONFERENCE_REQ_BASE + 5; // 获取嘉宾
																						// 6.12
		public static final int CONFERENCE_REQ_MEETING_PLAZA = CONFERENCE_REQ_BASE + 6; // //获取广场列表6.23

		public static final int CONFERENCE_REQ_MEETING_MEMBER_REPORT = CONFERENCE_REQ_BASE + 7; // //获取广场列表6.23

		/** 6.5 根据用户id和会议id获取会议详情 */
		public static final int CONFERENCE_REQ_MEETING_DETAIL = CONFERENCE_REQ_BASE + 8;
		public static final int CONFERENCE_REQ_SIGN_UP_MEETING = CONFERENCE_REQ_BASE + 9; // 会议报名
		public static final int CONFERENCE_REQ_CREATE_MEETING = CONFERENCE_REQ_BASE + 10; // 创建会议
		public static final int CONFERENCE_REQ_MY_MEETING = CONFERENCE_REQ_BASE + 11; // 我的会议
		public static final int CONFERENCE_REQ_REQUIREDSIGNUPINFO = CONFERENCE_REQ_BASE + 12; // 获取会议必填项
		public static final int CONFERENCE_REQ_IMPROVESIGNINFORMATION = CONFERENCE_REQ_BASE + 13; // 获取会议必填项
		public static final int CONFERENCE_REQ_GETREQUIREMENTLIST = CONFERENCE_REQ_BASE + 14; // 获取会议必填项
		public static final int CONFERENCE_REQ_INVITATIONBYFACETOFACE = CONFERENCE_REQ_BASE + 15; // 当面邀请
		public static final int CONFERENCE_REQ_DOGETMEETINGTOPICLIST = CONFERENCE_REQ_BASE + 16; // 根据会议ID获取会议议题聊天数据
		public static final int CONFERENCE_REQ_UPDATETOPIC = CONFERENCE_REQ_BASE + 17; // 修改议题
		public static final int CONFERENCE_REQ_SIGNINMEETING = CONFERENCE_REQ_BASE + 18; // 会议签到
		public static final int CONFERENCE_REQ_SIGNUPREVIEW = CONFERENCE_REQ_BASE + 19; // 报名审核
		public static final int CONFERENCE_REQ_DELETENOTBEGINMEETING = CONFERENCE_REQ_BASE + 20; // 删除我创建的会议
		public static final int CONFERENCE_REQ_CHANGEMYMEMBERMEETSTATUS = CONFERENCE_REQ_BASE + 21; // 归档删除我的会议
		public static final int CONFERENCE_REQ_DELETENOTE = CONFERENCE_REQ_BASE + 22; // 删除会议笔记
		public static final int CONFERENCE_REQ_GETNOTEBYMEETINGID = CONFERENCE_REQ_BASE + 23; // 查询会议笔记
		public static final int CONFERENCE_REQ_CHANGEMEETINGSTATUS = CONFERENCE_REQ_BASE + 24; // 修改会议状态
		public static final int CONFERENCE_REQ_GETFILELISTBYTASKID = CONFERENCE_REQ_BASE + 25; // 获取上传文件接口
		public static final int CONFERENCE_REQ_ADDMEETINGLABEL = CONFERENCE_REQ_BASE + 26; // 获取上传文件接口
		public static final int CONFERENCE_REQ_GETMEETINGLABELBYCREATEID = CONFERENCE_REQ_BASE + 27; // 获取用户自定义标签列表
		public static final int CONFERENCE_REQ_UPDATE = CONFERENCE_REQ_BASE + 28; // 修改会议
		public static final int CONFERENCE_REQ_SAVEMEETINGNOTE = CONFERENCE_REQ_BASE + 29; // 保存会议笔记
		public static final int CONFERENCE_REQ_SEND_MEETING_CHAT = CONFERENCE_REQ_BASE + 30;// 发送会议消息
		public static final int CONFERENCE_REQ_GET_MEETING_MESSAGE = CONFERENCE_REQ_BASE + 31; // 获取会议聊天记录
		public static final int CONFERENCE_REQ_GETMYFORWARDINGMEETING = CONFERENCE_REQ_BASE + 32; // 获取用户可转发的会议列表
		public static final int CONFERENCE_REQ_GETKNOWLEADGELIST = CONFERENCE_REQ_BASE + 33; // 获取会议必填项
		public static final int CONFERENCE_REQ_COLLECTKNOWLEDGE = CONFERENCE_REQ_BASE + 34; // 收藏知识
		public static final int CONFERENCE_REQ_FOCUSREQUIREMENT = CONFERENCE_REQ_BASE + 35; // 关注需求
		public static final int CONFERENCE_REQ_ADD_NOTE_DETAIL_BY_CHAT = CONFERENCE_REQ_BASE + 36; // 添加会议笔记
		public static final int CONFERENCE_REQ_GLOBALSEARCHBYMEETING = CONFERENCE_REQ_BASE + 37; // 搜索
		public static final int CONFERENCE_REQ_doEntryMeetingNotice = CONFERENCE_REQ_BASE + 38;
		public static final int CONFERENCE_REQ_GETMEETINGANDCHAT = CONFERENCE_REQ_BASE + 39;
		public static final int CONFERENCE_REQ_GETSOCIALLIST = CONFERENCE_REQ_BASE + 40;
		public static final int CONFERENCE_REQ_GET_MY_FORWARDING_SOCIAL = CONFERENCE_REQ_BASE + 41; // 获取转发列表
		public static final int CONFERENCE_REQ_GET_FORWARD_MEETING_DATA = CONFERENCE_REQ_BASE + 42; // 获取转发会议前获取转发会议的详细信息接口
		public static final int CONFERENCE_REQ_DELETE_PHOTO = CONFERENCE_REQ_BASE + 43; // 删除图片
		public static final int CONFERENCE_REQ_UPDATE_NOTICE_READSTATE = CONFERENCE_REQ_BASE + 44; // 更新通知为已读状态
		public static final int CONFERENCE_REQ_DELETE_NOTICE = CONFERENCE_REQ_BASE + 45; // 6.49
																							// 删除通知
		public static final int CONFERENCE_REQ_DELETE_INVITION_CARD = CONFERENCE_REQ_BASE + 47; // 6.49
																								// 删除邀请函
		public static final int CONFERENCE_REQ_DELETE_MEETING_MEMBER = CONFERENCE_REQ_BASE + 46; // 6.53
																									// 删除会议成员
		public static final int CONFERENCE_REQ_DELETE_SOCIAL = CONFERENCE_REQ_BASE + 48; // 6.58在社交列表中移除单聊、群聊、会议
		public static final int CONFERENCE_REQ_FINISH_TOPIC = CONFERENCE_REQ_BASE + 49; // 6.62
																						// 结束议题
		public static final int CONFERENCE_REQ_GETMEETLIST = CONFERENCE_REQ_BASE + 50; // 获取会议列表
		public static final int CONFERENCE_REQ_COMMUNITY_STATE = CONFERENCE_REQ_BASE + 51; // 获取会议列表
	}

	/**
	 * 组织请求相关的API OrganizationReqType 的id
	 */
	public static class OrganizationReqType {

		public static final int ORGANIZATION_REQ_BASE = 6000;

		public static final int ORGANIZATION_REQ_END = 6200;

		// 创建组织:1.1 上传图片接口 uploadIdCardImg
		public static final int ORGANIZATION_REQ_CREATE_ORG = ORGANIZATION_REQ_BASE + 1;

		// 创建组织:1.3 组织注册 /org/save.json
		public static final int ORGANIZATION_REQ_REGISTRATION = ORGANIZATION_REQ_BASE + 2;

		// 创建组织:1.4 获取验证码 getVCodeForPassword
		public static final int ORGANIZATION_REQ_GETVCODEFORPASSWORD = ORGANIZATION_REQ_BASE + 3;

		// 动态首页:2.1 动态列表
		public static final int ORGANIZATION_REQ_DYNAMICNEWSLIST = ORGANIZATION_REQ_BASE + 4;

		// 动态首页:2.2 转发组织动态 /org/forwardOrg.json
		public static final int ORGANIZATION_REQ_FORWARDORG = ORGANIZATION_REQ_BASE + 5;

		// 动态首页:2.3 转发知识动态 /org/forwardKnowledge.json
		public static final int ORGANIZATION_REQ_FORWARDKONWLEDGE = ORGANIZATION_REQ_BASE + 6;

		// 组织首页3.1 获得组织列表数据 /org/getDiscoverList.json
		public static final int ORGANIZATION_REQ_GETDISCOVERLIST = ORGANIZATION_REQ_BASE + 7;

		// 组织首页3.2 地区查询接口 getAreaList
		public static final int ORGANIZATION_REQ_GETAREALIST = ORGANIZATION_REQ_BASE + 8;

		// 组织首页3.3 行业查询接口 getTradeList
		public static final int ORGANIZATION_REQ_GETTRADELIST = ORGANIZATION_REQ_BASE + 9;

		// 组织详情页面4.1 组织动态列表 /org/orgDynamicList.json
		public static final int ORGANIZATION_REQ_ORGDYNAMICLIST = ORGANIZATION_REQ_BASE + 10;

		// 组织详情页面4.2 评论列表 customer/comment/findCommentList.json
		public static final int ORGANIZATION_REQ_FINDCOMMENTLIST = ORGANIZATION_REQ_BASE + 11;

		// 组织详情页面4.3 组织转客户 org/changeCustomer.json
		public static final int ORGANIZATION_REQ_CHANGECUSTOMER = ORGANIZATION_REQ_BASE + 12;

		// 组织详情页面4.4 组织详情 /org/orgAndProInfo
		public static final int ORGANIZATION_REQ_ORGANDPROINFO = ORGANIZATION_REQ_BASE + 13;

		// 组织详情页面4.5 添加客户详情 customer/saveCusProfile.json
		public static final int ORGANIZATION_REQ_CUSTOMER_SAVECUSPROFILE = ORGANIZATION_REQ_BASE + 14;

		// 组织详情页面4.6 添加组织详情 org/saveCusProfile.json
		public static final int ORGANIZATION_REQ_ORG_SAVECUSPROFILE = ORGANIZATION_REQ_BASE + 15;

		// 组织详情页面4.6 获得最新公告列表接口 getOrgNoticesList
		public static final int ORGANIZATION_REQ_GETORGNOTICESLIST = ORGANIZATION_REQ_BASE + 16;

		// 组织详情页面4.7 获得最新资讯接口 /org/getOrgNewsList.json
		public static final int ORGANIZATION_REQ_GETNEWSLIST = ORGANIZATION_REQ_BASE + 17;

		// 组织详情页面4.8 查询资源需求 /customer/resource/findResource.json
		public static final int ORGANIZATION_REQ_FINDRESOUTCE = ORGANIZATION_REQ_BASE + 18;

		// 组织详情页面4.9 添加客户资源需求 /customer/resource/save.json
		public static final int ORGANIZATION_REQ_SAVE = ORGANIZATION_REQ_BASE + 19;

		// 组织详情页面4.10.1 获得权限模块查询 /customer/getModleList.json
		public static final int ORGANIZATION_REQ_GETMODLELIST = ORGANIZATION_REQ_BASE + 20;

		// 组织详情页面4.10.2 获得权限查列表用户查询 /customer/getPermissonUser.json
		public static final int ORGANIZATION_REQ_GETPERMISSONUSER = ORGANIZATION_REQ_BASE + 21;

		// 组织详情页面4.10.3 获得客户标签分组列表 /customer/tag/group.json
		public static final int ORGANIZATION_REQ_GROUP = ORGANIZATION_REQ_BASE + 22;

		// 组织详情页面4.11 按年份，报表类型,季度，获取财务分析详情 /customer/finance/details.json
		public static final int ORGANIZATION_REQ_DETAILS = ORGANIZATION_REQ_BASE + 23;

		// 组织详情页面4.12 查询高层治理 /customer/hight/findHightOne
		public static final int ORGANIZATION_REQ_FINDHEGHTONE = ORGANIZATION_REQ_BASE + 24;

		// 组织详情页面4.13 添加高层治理 /customer/hight/save.json
		public static final int ORGANIZATION_REQ_ADD_HIGHLEVEL = ORGANIZATION_REQ_BASE + 25;

		// 组织详情页面4.14 查询股东研究 /customer/stock/findStockOne.json
		public static final int ORGANIZATION_REQ_FINDSTOCKONE = ORGANIZATION_REQ_BASE + 26;

		// 组织详情页面4.15 添加股东研究 /customer/stock/saveOrUpdate.json
		public static final int ORGANIZATION_REQ_SAVEORUPDATE = ORGANIZATION_REQ_BASE + 27;

		// 组织详情页面4.16 查询行业动态 customer/industry/findIndustry.json
		public static final int ORGANIZATION_REQ_FINDINDUSTRY = ORGANIZATION_REQ_BASE + 28;

		// 组织详情页面4.17 行业动态根据行业查询接口 /customer/industry/searchPeer.json
		public static final int ORGANIZATION_REQ_SEARCHPEER = ORGANIZATION_REQ_BASE + 29;

		// 组织详情页面4.18 添加行业动态 /customer/industry/save.json
		public static final int ORGANIZATION_REQ_ORGANIZATIONDETAILS = ORGANIZATION_REQ_BASE + 30;

		// 组织详情页面4.19 查询同业竞争列表 customer/peer/findPeer.json
		public static final int ORGANIZATION_REQ_FINDPER = ORGANIZATION_REQ_BASE + 31;

		// 组织详情页面4.20 同业竞争根据公司名称查询接口 /customer/peer/searchPeerByName.json
		public static final int ORGANIZATION_REQ_SEARCHPEERBYNAME = ORGANIZATION_REQ_BASE + 32;

		// 组织详情页面4.21 添加同业竞争 /customer/peer/save.json
		public static final int ORGANIZATION_REQ_ADD_COMPETITION = ORGANIZATION_REQ_BASE + 33;

		// 组织详情页面4.22 查询研究报告 /customer/findReport.json
		public static final int ORGANIZATION_REQ_FINDREPORT = ORGANIZATION_REQ_BASE + 34;

		// 组织详情页面4.23 添加研究报告 /customer/report/save.json
		public static final int ORGANIZATION_REQ_ADD_RESEARCH_REPORT = ORGANIZATION_REQ_BASE + 35;

		// 组织详情页面4.24 添加财务分析相关描述 /customer/finance/save.json
		public static final int ORGANIZATION_REQ_ADD_FINANCIAL_ANALYSIS = ORGANIZATION_REQ_BASE + 36;

		// 组织详情页面4.25 添加政府地区概况 /customer/areaInfo/save.json
		public static final int ORGANIZATION_REQ_ADD_GOVERNMENTAREASURVEY = ORGANIZATION_REQ_BASE + 37;

		// 组织详情页面4.26 添加主要职能部门 /customer/departMents/save.json
		public static final int ORGANIZATION_REQ_ADD_MAIN_DEPARTMENT = ORGANIZATION_REQ_BASE + 38;

		// 组织详情页面4.27 删除客户信息 /customer/allPart/delete.json
		public static final int ORGANIZATION_REQ_DELETE_CUSTOMER_INFO = ORGANIZATION_REQ_BASE + 39;

		// 组织详情页面4.28 标签公共组件查询 /tag/list.json
		public static final int ORGANIZATION_REQ_LABE_PUBLIC_ASSEMBLY_FIND = ORGANIZATION_REQ_BASE + 40;

		/** 组织详情页面4.29 客户分组列表查询 /customer/group/list.json */
		public static final int ORGANIZATION_REQ_CUSTOMER_LIST_QUERY = ORGANIZATION_REQ_BASE + 41;

		/** 组织详情页面4.29.1 添加分组 /customer/group/add.json */
		public static final int ORGANIZATION_REQ_ADD_GROUPING = ORGANIZATION_REQ_BASE + 42;

		/** 组织详情页面4.29.2 删除客户分组 /customer/group/delete.json */
		public static final int ORGANIZATION_REQ_DELETE_GROUPING = ORGANIZATION_REQ_BASE + 43;

		// 组织详情页面4.29.3 更新客户分组 /customer/group/update.json
		public static final int ORGANIZATION_REQ_UPDAET_GROUPING = ORGANIZATION_REQ_BASE + 44;

		// 组织详情页面4.30 得到客户主键Id /customer/getId.json
		public static final int ORGANIZATION_REQ_GETMAINID = ORGANIZATION_REQ_BASE + 45;

		// 4.30 得到客户主键Id /customer/getId.json
		public static final int ACCESS_TO_THE_PRIMARY_KEY = ORGANIZATION_REQ_BASE + 46;

		// 组织详情页面4.31 获得十大股东和流通股东 /customer/findTenStockDetail.json
		public static final int ORGANIZATION_REQ_GETSHAREHOLDER = ORGANIZATION_REQ_BASE + 47;

		// 组织/客户列表5.2 获得组织/客户列表 /org/getOrgAndCustomer.json
		public static final int ORAGANIZATION_REQ_GETCUSTOMANDORG = ORGANIZATION_REQ_BASE + 48;

		// 6.1 查询客户详情 customer/findCusProfile.json
		public static final int ORGANIZATION_CUSTOMER_DETILS = ORGANIZATION_REQ_BASE + 49;

		// 6.3 查询模板下的栏目 /customer/column/list.json
		public static final int ORGANIZATION_CUSTOMER_COLUMNLIST = ORGANIZATION_REQ_BASE + 50;
		// 6.4 保存所选模块和栏目 /customer/column/saveRelation.json
		public static final int ORGANIZATION_CUSTOMER_SAVERELATION = ORGANIZATION_REQ_BASE + 51;

		// 6.5 新增和修改自定义栏目 /customer/column/save.json
		public static final int COLUMN_SAVE = ORGANIZATION_REQ_BASE + 52;
		// 6.6 删除自定义栏目 /customer/column/delete.json¶
		public static final int COLUMN_DELETE = ORGANIZATION_REQ_BASE + 53;

		// 6.7 获取对该用户的评价 /customer/findEvaluate.json
		public static final int GET_ORG_HOME_Evaluate = ORGANIZATION_REQ_BASE + 54;

		// 6.8 添加评价 /customer/addEvaluate.json
		public static final int ADD_ORG_HOME_Evaluate = ORGANIZATION_REQ_BASE + 55;

		// 6.9 赞同与取消赞同 /customer/feedbackEvaluate.json
		public static final int FEEDBACK_EVALUATE = ORGANIZATION_REQ_BASE + 56;

		// 6.10 删除评价 /customer/deleteEvaluate.json
		public static final int DELETE_EVALUATE = ORGANIZATION_REQ_BASE + 57;

		// 6.11 查看更多评价 /customer/moreOrganEvaluate.json
		public static final int FIND_MORE_EVALUATE = ORGANIZATION_REQ_BASE + 58;

		// 发表评论
		public static final int ORG_COMMENT = ORGANIZATION_REQ_BASE + 59;
		// 7.1 增加会面情况 /customer/meet/save.json
		public static final int MEET_SAVE = ORGANIZATION_REQ_BASE + 60;
		// 7.7加好友
		public static final int ORAGANIZATION_REQ_ADD_FRIENDS = ORGANIZATION_REQ_BASE + 61;
		// 7.8解除好友
		public static final int ORAGANIZATION_REQ_RELIEVE_FRIENDS = ORGANIZATION_REQ_BASE + 62;

		// 7.3 查询会面情况列表 /customer/meet/findList.json
		public static final int MEET_FINDLIST = ORGANIZATION_REQ_BASE + 64;

		// 7.4 查询单个会面情况对象 /customer/meet/findOne.json
		public static final int MEET_FINDONE = ORGANIZATION_REQ_BASE + 65;

		// 7.4 查询单个会面情况对象 /customer/meet/findOne.json
		public static final int MEET_DELETEBYID = ORGANIZATION_REQ_BASE + 66;

		public static final int EDITBLACKLIST = ORGANIZATION_REQ_BASE + 67; // 编辑黑名单

		public static final int BLACKLIST = ORGANIZATION_REQ_BASE + 68; // 黑名单列表

		public static final int ORG_DELETEFRIED = ORGANIZATION_REQ_BASE + 69;// 解除好友关系

		// 组织/客户列表5.3 删除组织/客户 /org/deleteOrgAndCustomer.json
		public static final int ORAGANIZATION_REQ_DELCUSANDORG = ORGANIZATION_REQ_BASE + 70;

		/** 获取标签内容数量 */
		public static final int ORGANIZATION_REQ_TAG_QUERY = ORGANIZATION_REQ_BASE + 71;

		/** 删除标签 */
		public static final int ORGANIZATION_REQ_DELETE_TAG = ORGANIZATION_REQ_BASE + 72;

		/** 保存标签 */
		public static final int ORGANIZATION_REQ_SAVE_TAG = ORGANIZATION_REQ_BASE + 73;

		/** 获取注册组织的详情 */
		public static final int ORGANIZATION_REQ_REGIST_ORG_DETAIL = ORGANIZATION_REQ_BASE + 74;

		/** 获取金桐脑推荐的标签 */
		public static final int ORGANIZATION_REQ_TAG_LIST_GINTONG = ORGANIZATION_REQ_BASE + 75;

		// 4.35 举报组织客户 /customer/inform/save.json
		public static final int CUSTOMER_INFORM_SAVE = ORGANIZATION_REQ_BASE + 76;
		// 4.34 收藏或取消收藏组织客户 /customer/collect/operate.json
		public static final int CUSTOMER_COLLECT_PERATE = ORGANIZATION_REQ_BASE + 77;

		// 完善组织信息
		public static final int SAVE_ORGANIZATION = ORGANIZATION_REQ_BASE + 78;
	}

	public static class OrganizationUrl {

		// 1.1上传图片 http://file.dev.gintong.com/mobile/idcard/avatar
		public static final String UPLOADIDCARDIMG = "http://file.dev.gintong.com/mobile/idcard/avatar";
		// 2.1 动态列表 /org/dynamicNews/dynamicNewsList.json
		public static final String DYNAMICNEWSLIST_STR = "dynamicNews/getListDynamicNews.json";
		// 3.1 获得组织列表数据 /org/getDiscoverList.json
		public static final String DISCOVERLIST_STR = "org/getDiscoverList.json";
		// 1.4 获取验证码 /register/getVCodeForPassword.json
		public static final String GETVCODEFORPASSWORD_STR = "register/getVCodeForPassword.json";
		// 1.3 组织注册 /org/save.json
		public static final String ORGSAVE_STR = "org/save.json";
		// 2.2 转发组织动态 /org/forwardOrg.json
		public static final String FORWARDORG_STR = "org/forwardOrg.json";
		// 4.5 添加客户详情 customer/saveCusProfile.json
		public static final String SAVECUSPROFILE_STR = "customer/saveCusProfile.json";
		// 组织详情页面4.4 组织详情 /org/orgAndProInfo
		public static final String ORGANIZATION_DETAILS_STR = "org/orgAndProInfo.json";
		// 4.6 获得最新公告列表接口 org/getOrgNoticesList.json
		public static final String ORGNOTICESLIST_STR = "org/getOrgNoticesList.json";
		// 4.7 获得最新资讯接口 /org/getOrgNewsList.json
		public static final String ORGNEWSLIST_STR = "org/getOrgNewsList.json";
		// 4.10.1 获得权限模块查询 /customer/getModleList.json
		public static final String GETMODELLIST_STR = "/customer/getModleList.json";
		// 4.10.3 获得客户标签分组列表 /customer/tag/group.json
		public static final String GROUP_STR = "/customer/tag/group.json";
		// 4.13 添加高层治理 /customer/hight/save.json
		public static final String HIGHTSAVE_STR = "/customer/hight/save.json";
		// 4.14 查询股东研究 /customer/stock/findStockOne.json
		public static final String FINDSTOCKONE_STR = "/customer/stock/findStockOne.json";
		// 查询资源需求 /customer/resource/findResource.json
		public static final String FINDRESOURCE_STR = "/customer/resource/findResource.json";
		// 组织详情页面4.11 按年份，报表类型,季度，获取财务分析详情 /customer/finance/details.json
		public static final String FINANCE_DETAILS_STR = "/customer/finance/details.json";
		// 组织详情页面4.12 查询高层治理 /customer/hight/findHightOne.json
		public static final String HEGHTONE_STR = "/customer/hight/findHightOne.json";
		// 4.16 查询行业动态 customer/industry/findIndustry.json
		public static final String FINDINDUSTRY_DYN_STR = "/customer/industry/findIndustry.json";
		// 组织客户列表5.2 获取组织客户列表/org/getOrgAndCustomer.json
		public static final String CUSANDORG_STR = "/org/getOrgAndCustomer.json";
		// 7.7添加好友
		public static final String ADD_FRIENDS_STR = "/org/addFriend.json";
		// 6.3 查询模板下的栏目 /customer/column/list.json
		public static final String COLUMNLIST_STR = "/customer/column/list.json";
		// 6.4 保存所选模块和栏目 /customer/column/saveRelation.json
		public static final String SAVERELATION_STR = "/customer/column/saveRelation.json";
		// 4.30 得到客户主键Id /customer/getId.json
		public static final String ACCESS_STR = "/customer/getId.json";
		// 4.19查询同业竞争列表
		public static final String CUSTOMER_STR = "/customer/peer/findPeer.json";
		// 6.1 查询客户详情 customer/findCusProfile.json
		public static final String CUSTOMER_DETILS = "/customer/findCusProfile.json";
		// 4.6 添加组织详情
		public static final String ORGSAVEDETAIL_STR = "org/saveCusProfile.json";
		// 6.7 获取对该用户的评价 /customer/findEvaluate.json
		public static final String FIND_Evaluate_STR = "/customer/findEvaluate.json";
		// 6.8 添加评价 /customer/addEvaluate.json
		public static final String ADD_Evaluate_STR = "/customer/addEvaluate.json";
		// 6.9 赞同与取消赞同 /customer/feedbackEvaluate.json
		public static final String FEEDBACK_Evaluate_STR = "/customer/feedbackEvaluate.json";
		// 6.10 删除评价 /customer/deleteEvaluate.json
		public static final String DELETE_Evaluate_STR = "/customer/deleteEvaluate.json";
		// 6.11 查看更多评价 /customer/moreOrganEvaluate.json
		public static final String FIND_MORE_Evaluate_STR = "/customer/moreOrganEvaluate.json";
		// 6.5 新增和修改自定义栏目 /customer/column/save.json
		public static final String COLUMN_SAVE_STR = "/customer/column/save.json";
		// 6.6 删除自定义栏目 /customer/column/delete.json
		public static final String COLUMN_DELETE_STR = "/customer/column/delete.json";
		// 4.26 添加主要职能部门 /customer/departMents/save.json
		public static final String DEPARTMENTS_SAVE_STR = "/customer/departMents/save.json";
		// 4.25 添加政府地区概况 /customer/areaInfo/save.json
		public static final String AREAINFO_SAVE_STR = "/customer/areaInfo/save.json";
		// 7.1 增加会面情况 /customer/meet/save.json
		public static final String MEET_SAVE_STR = "/customer/meet/save.json";
		// 7.8解除好友 /org/deleteFriend.json
		public static final String RELIEVE_FRIENDS_ = "/org/deleteFriend.json";
		// 7.3 查询会面情况列表 /customer/meet/findList.json
		public static final String MEET_FINDLIST_STR = "/customer/meet/findList.json";
		// 7.4 查询单个会面情况对象 /customer/meet/findOne.json
		public static final String MEET_FINDONE_STR = "/customer/meet/findOne.json";
		// 7.5 删除会面情况对象 /customer/meet/deleteById.json
		public static final String MEET_DELETEBYID_STR = "/customer/meet/deleteById.json";
		// 4.22 查询研究报告/customer/findReport.json
		public static final String FINDREPORT_STR = "/customer/findReport.json";

		public static final String EDITBLACKLIST_STR = "/user/set/editBlack.json";// 编辑黑名单:加入/移除
		public static final String BLACKLIST_STR = "/user/set/blackList.json";// 获取黑名单列表

		public static final String DELETEFRIEND_STR = "/friend/deleteFriend.json";// 解除好友关系
		// 5.3删除组织客户
		public static final String DELETEORGANDCUSTOMER = "/org/deleteOrgAndCustomer.json";
		/** 客户分组列表查询 */
//		public static final String ORGANIZATION_REQ_CUSTOMER_LIST_QUERY = "/customer/group/list.json"; 
		/** 客户WEB分组列表查询 */
		public static final String ORGANIZATION_REQ_CUSTOMER_LIST_QUERY = "/customer/group/list.json"; 
		/** 组织目录添加分组 */
		public static final String ORGANIZATION_REQ_ADD_GROUPING = "/customer/group/add.json"; // 添加分组
		/** 组织目录 更新客户分组 */
		public static final String ORGANIZATION_REQ_UPDAET_GROUPING = "/customer/group/update.json"; // 更新客户分组
		/** 组织目录 删除客户分组 */
		public static final String ORGANIZATION_REQ_DELETE_GROUPING = "/customer/group/delete.json"; // 删除客户分组

		/** 获取标签的内容数量 */
		public static final String ORGANIZATION_REQ_TAG_QUERY = "/customer/tag/group.json"; // 查询标签和数量
		/** 删除标签 */
		public static final String ORGANIZATION_REQ_DELETE_TAG = "/customer/tag/delete.json";

		/** 添加标签 */
		public static final String ORGANIZATION_REQ_SAVE_TAG = "/customer/tag/add.json";

		/** 获取金桐脑推荐的标签 */
		public static final String ORGANIZATION_REQ_TAG_LIST = "/tag/list.json";

		/** 获取推荐的标签 */
		public static final String ORGANIZATION_REQ_REGIST_ORG_DETAIL = "/org/save/find.json";
		// 4.35 举报组织客户 /customer/inform/save.json
		public static final String CUSTOMER_INFORM_SAVE_STR = "/customer/inform/save.json";
		// 4.34 收藏或取消收藏组织客户 /customer/collect/operate.json
		public static final String CUSTOME_COLLECT_OPERATE_STR = "/customer/collect/operate.json";
		/** 完善组织信息 */
		public static final String SAVE_ORGANIZATION = "/org/save.json";
	}
	/*
	 * 桐人请求相关API 对应的请求类型
	 */
	public static class TongRenRequestType {
		public static final int TONGREN_REQ_BASE = 9000;
		public static final int TONGREN_REQ_END = 9200;
		public enum TongRenInfoType{
			PROJECT,
			MY,
			ORGANIZATION;
		}
		public static final String TongRenInfoType  = "tongRenInfoType";
		// 1.1 /project/manage/create.json创建项目
		public static final int TONGREN_REQ_CREATE_PROJECT = TONGREN_REQ_BASE + 1;
		// 1.2 我创建的项目列表接口 /project/manage/getMyAllProjects.json¶
		public static final int TONGREN_REQ_GETMYCREATEALL_PROJECTS = TONGREN_REQ_BASE + 2;
//		1.3 我的承接项目列表 /project/undertaken/undertakenList.json
		public static final int TONGREN_REQ_GETMYCREATEALL_UNDERTAKEN = TONGREN_REQ_BASE + 4;
		// 3.5.1 首页消息列表 /message/userMessage.json
		public static final int TONGREN_REQ_USERMESSAGE = TONGREN_REQ_BASE + 3;
		//	我发布的项目、已承接、未承接接口 	/project/manage/getAllPublishValidity.json¶
		public static final int TONGREN_REQ_GETALLPUBLISHVALIDITY = TONGREN_REQ_BASE + 5;
		//未过期的项目列表接口 /project/manage/getPagePublishValidity.json
		public static final int TONGREN_REQ_GETPAGEPUBLISHVALIDITY = TONGREN_REQ_BASE + 6;
		// 查看项目详情接口 /project/manage/getProjectDetail.json
		public static final int TONGREN_REQ_GETPROJECTDETAIL = TONGREN_REQ_BASE + 7;
		// 查看承接的项目详情接口 /project/undertaken/getUndertakenProjectInfo.json
		public static final int TONGREN_REQ_GETUNDERTAKENPROJECTINFO = TONGREN_REQ_BASE + 9;
		//获得项目中的文档资源 /resource/getResourceProject.json
		public static final int TONGREN_REQ_GETRESOURCEPROJECT = TONGREN_REQ_BASE + 8;
		//申请项目接口 /project/manage/apply.json
		public static final int TONGREN_REQ_APPLY = TONGREN_REQ_BASE + 10;
		//查看项目任务操作记录 /operation/getProjectOperation.json
		public static final int TONGREN_REQ_GETPROJECTOPERATION = TONGREN_REQ_BASE + 11;
		//获取项目主任务 /projectTask/getPrimaryTask.json
		public static final int TONGREN_REQ_GETPRIMARYTASK = TONGREN_REQ_BASE + 12;
		//增加子任务 /projectTask/addSubTaskWithMobile.json
		public static final int TONGREN_REQ_ADDSUBTASK = TONGREN_REQ_BASE + 13;
		//修改子任务(修改子任务名称，设置开始时间和结束时间) /projectTask/updateSubTask.json
		public static final int TONGREN_REQ_UPDATESUBTASK = TONGREN_REQ_BASE + 14;
		//分配子任务 /assignTask/assign.json
		public static final int TONGREN_REQ_ASSIGN = TONGREN_REQ_BASE + 15;
		//查看正式组织成员列表 getOrganizationMemberInfo.json
		public static final int TONGREN_REQ_GETORGANIZATIONMEMBERINFO = TONGREN_REQ_BASE + 16;
		//查询我创建的组织 /resource/getMyCreateOrganizations.json
		public static final int TONGREN_REQ_GETMYCREATEORGANIZATIONS = TONGREN_REQ_BASE + 17;
		//查询我参与的组织 /organization/getMyJoinOrganization.json
		public static final int TONGREN_REQ_GETMYJOINORGANIZATION = TONGREN_REQ_BASE + 18;
		//查询组织详情 /organization/getOrganizationById.json
		public static final int TONGREN_REQ_GETORGANIZATIONBYID = TONGREN_REQ_BASE + 19;
		//退出组织 /organizationMember/exit.json
		public static final int TONGREN_REQ_EXIT = TONGREN_REQ_BASE + 20;
		//解散组织 /organization/disband.json
		public static final int TONGREN_REQ_DISBAND = TONGREN_REQ_BASE + 21;
		//组织概况 /organization/getOrganizationSumup.json
		public static final int TONGREN_REQ_GETORGANIZATIONSUMUP = TONGREN_REQ_BASE + 22;
//		根据项目Id查询项目申请列表 /project/manage/getMyProjectApply.json
		public static final int TONGREN_REQ_GETMYPROJECTAPPLY = TONGREN_REQ_BASE + 23;
		//查看任务分配人 /assignTask/selectAssignTaskByTaskId.json¶ 
		public static final int TONGREN_REQ_SELECTASSIGNTASKBYTASKID = TONGREN_REQ_BASE + 24;
		//完成任务 /projectTask/completeTask.json
		public static final int TONGREN_REQ_COMPLETETASK = TONGREN_REQ_BASE + 25;
		//上班打卡 /attendanceRecords/addBegin.json
		public static final int TONGREN_REQ_ADDBEGIN = TONGREN_REQ_BASE + 123;
		//下班打卡 /attendanceRecords/addEnd.json
		public static final int TONGREN_REQ_ADDEND = TONGREN_REQ_BASE + 124;
		//月度考勤信息 /attendanceRecords/getMonthInfo.json
		public static final int TONGREN_REQ_GETMONTHINFO = TONGREN_REQ_BASE + 125;
		//删除项目发布 /project/manage/del.json
		public static final int TONGREN_REQ_DELPROJECT = TONGREN_REQ_BASE + 26;
		//放弃项目和结束项目 /project/undertaken/projectOperation.json
		public static final int TONGREN_REQ_PROJECTOPERATION = TONGREN_REQ_BASE + 27;
		//删除任务 /projectTask/removeTask.json
		public static final int TONGREN_REQ_REMOVETASK = TONGREN_REQ_BASE + 28;
		//确认合作承接项目 /project/undertaken/undertakeProject.json
		public static final int TONGREN_REQ_UNDERTAKEPROJECT = TONGREN_REQ_BASE + 29;
		//获得我的资源列表 /resource/getMyOrgResource.json
		public static final int TONGREN_REQ_GETMYORGRESOURCE = TONGREN_REQ_BASE + 30;
		//组织管理下的组织资源列表 /resource/getOrgResource.json
		public static final int TONGREN_REQ_GETORGRESOURCE = TONGREN_REQ_BASE + 31;
		//我的任务列表 /projectTask/getMyTaskList.json 
		public static final int TONGREN_REQ_GETMYTASKLIST = TONGREN_REQ_BASE + 32;
		//组织任务列表 organization/getOrgTaskList.json
		public static final int TONGREN_REQ_GETORGTASKLIST = TONGREN_REQ_BASE + 33;
		
		//创建组织任务接口organizationTask/create.json
		public static final int TONGREN_REQ_ORGANIZATIONTASKCREATE = TONGREN_REQ_BASE + 34;
		//重新分配组织任务接口 assignOrganizationTask.json
				public static final int TONGREN_REQ_ASSIGNORGANIZATIONTASK = TONGREN_REQ_BASE + 38;
		///删除组织任务接口 organizationTask/delete.json
		public static final int TONGREN_REQ_ORGANIZATIONTASKDELETE = TONGREN_REQ_BASE + 35;
		//获取我在组织中的角色 /manage/role/getMyRole.json
		public static final int TONGREN_REQ_GETMYROLE = TONGREN_REQ_BASE + 36;
		//查询用户某天的打卡信息 /attendanceRecords/getAttendanceRecordsOfDate.json
		public static final int TONGREN_REQ_GETATTENDANCERECORDSOFDATE = TONGREN_REQ_BASE + 126;
		//获取考勤时间 /attendanceSystem/get.json
		public static final int TONGREN_REQ_GETRULE = TONGREN_REQ_BASE + 127;
		//我的申批列表 /reviewRecords/getReviewApplicationList.json
		public static final int TONGREN_REQ_GETREVIEWAPPLICATIONLIST = TONGREN_REQ_BASE + 128;
		//我的申请列表 /reviewApplication/getMyApplyFor.json
		public static final int TONGREN_REQ_GETMYAPPLYFOR = TONGREN_REQ_BASE + 129;
		//新建审请createApplication.json
		public static final int TONGREN_REQ_CREATEAPPLICATION = TONGREN_REQ_BASE + 130;
		//查询组织下的审批流程集合 /reviewProcess/getProcessByOrgId.json
		public static final int TONGREN_REQ_GETPROCESSBYORGID = TONGREN_REQ_BASE + 131;
		//撤销我的申请/reviewApplication/recallRecords.json
		public static final int TONGREN_REQ_RECALLRECORDS = TONGREN_REQ_BASE + 132;
		//我的审批（同意、驳回）/reviewRecords/signOff.json
		public static final int TONGREN_REQ_SIGNOFF = TONGREN_REQ_BASE + 133;
		//删除消息操作 /message/delMessage.json 
		public static final int TONGREN_REQ_DELMESSAGE = TONGREN_REQ_BASE + 37;
		//(首页消息操作同意、忽略)申请类，退出申请，邀请类 /message/processing/handle.json
		public static final int TONGREN_REQ_PROCESSING = TONGREN_REQ_BASE + 40;
	}
	/*
	 * 桐人请求相关url 对应的请求类型
	 */
	public static class TongRenRequestUrl {
		
		// 1.1 /project/manage/create.json创建项目
		public static final String CREATE_PROJECT = "/project/manage/create.json";
		
		// 1.2 我创建的项目列表接口 /project/manage/getMyAllProjects.json¶
		public static final String GETMYCREATEALL_PROJECTS = "/project/manage/getMyAllProjects.json";
//		1.3 我的承接项目列表 /project/undertaken/undertakenList.json
		public static final String GETMYCREATEALL_UNDERTAKEN = "/project/undertaken/undertakenList.json";
		// 3.5.1 首页消息列表 /message/userMessage.json
		public static final String GET_USERMESSAGE = "/message/userMessage.json";
		//		/project/manage/getAllPublishValidity.json¶
		public static final String GETALLPUBLISHVALIDITY = "/project/manage/getAllPublishValidity.json";
		//未过期的项目列表接口 /project/manage/getPagePublishValidity.json
		public static final String GETPAGEPUBLISHVALIDITY = "/project/manage/getPagePublishValidity.json";
		//// 查看项目详情接口 /project/manage/getProjectDetail.json
		public static final String GETPROJECTDETAIL = "/project/manage/getProjectDetail.json";
		//获得项目中的文档资源 /resource/getResourceProject.json
		public static final String GETRESOURCEPROJECT = "/resource/getResourceProject.json";
		//// 查看承接的项目详情接口 /project/undertaken/getUndertakenProjectInfo.json
		public static final String GETUNDERTAKENPROJECTINFO = "/project/undertaken/getUndertakenProjectInfo.json";
		//申请项目接口 /project/manage/apply.json
		public static final String PROJECT_APPLY = "/project/manage/apply.json";
		//查看项目任务操作记录 /operation/getProjectOperation.json
		public static final String GETPROJECTOPERATION = "/operation/getProjectOperation.json";
		//获取项目主任务 /projectTask/getPrimaryTask.json
		public static final String GETPRIMARYTASK = "/projectTask/getPrimaryTask.json";
		//增加子任务/projectTask/addSubTaskWithMobile.json
		public static final String ADDSUBTASK = "/projectTask/addSubTaskWithMobile.json";
		//修改子任务(修改子任务名称，设置开始时间和结束时间) /projectTask/updateSubTask.json
		public static final String UPDATESUBTASK = "/projectTask/updateSubTask.json";
		//分配子任务 /assignTask/assign.json
		public static final String ASSIGN = "/assignTask/assign.json";
		////查看正式组织成员列表 getOrganizationMemberInfo.json
		public static final String GETMYCREATEORGANIZATIONS = "/organization/getMyCreateOrganizations.json";
		//查询我参与的组织 /organization/getMyJoinOrganization.json
		public static final String GETMYJOINORGANIZATION = "/organization/getMyJoinOrganization.json";
		//查询组织详情 /organization/getOrganizationById.json
		public static final String GETORGANIZATIONBYID = "/organization/getOrganizationById.json";
		//解散组织 /organization/disband.json
		public static final String DISBAND = "/organization/disband.json";
		//退出组织 /organizationMember/exit.json
		public static final String EXIT = "/organizationMember/exit.json";
		//组织概况 /organization/getOrganizationSumup.json
		public static final String GETORGANIZATIONSUMUP = "/organization/getOrganizationSumup.json";
		//组织成员 /organizationMember/getOrganizationMemberInfo.json
		public static final String GETORGANIZATIONMEMBERINFO = "/organizationMember/getOrganizationMemberInfo.json";
		//根据项目Id查询项目申请列表 /project/manage/getMyProjectApply.json
		public static final String GETMYPROJECTAPPLY = "/project/manage/getMyProjectApply.json";
		////查看任务分配人 /assignTask/selectAssignTaskByTaskId.json¶ 
		public static final String SELECTASSIGNTASKBYTASKID = "/assignTask/selectAssignTaskByTaskId.json";
		//完成任务 /projectTask/completeTask.json
		public static final String COMPLETETASK = "/projectTask/completeTask.json";
		//上班打卡 /attendanceRecords/addBegin.json
		public static final String ADDBEGIN = "/attendanceRecords/addBegin.json";
		//下班打卡 /attendanceRecords/addEnd.json
		public static final String ADDEND = "/attendanceRecords/addEnd.json";
		//月度考勤信息 /attendanceRecords/getMonthInfo.json
		public static final String GETMONTHINFO = "/attendanceRecords/getMonthInfo.json";
		//删除项目发布 /project/manage/del.json
		public static final String DELPROJECT = "/project/manage/del.json";
		//放弃项目和结束项目 /project/undertaken/projectOperation.json
		public static final String PROJECTOPERATION = "/project/undertaken/projectOperation.json";
		//删除任务 /projectTask/removeTask.json
		public static final String REMOVETASK = "/projectTask/removeTask.json";
		//确认合作承接项目 /project/undertaken/undertakeProject.json
		public static final String UNDERTAKEPROJECT = "/project/undertaken/undertakeProject.json";
		//获得我的资源列表 /resource/getMyOrgResource.json
		public static final String GETMYORGRESOURCE = "/resource/getMyOrgResource.json";
		////组织管理下的组织资源列表 /resource/getOrgResource.json
		public static final String GETORGRESOURCE = "/resource/getOrgResource.json";
		//我的任务列表 /projectTask/getMyTaskList.json 
		public static final String GETMYTASKLIST = "/projectTask/getMyTaskList.json";
		//组织任务列表 organization/getOrgTaskList.json
		public static final String GETORGTASKLIST = "/organization/getOrgTaskList.json";
		//创建组织任务接口organizationTask/create.json
		public static final String ORGANIZATIONTASKCREATE = "/organizationTask/create.json";
		///删除组织任务接口 organizationTask/delete.json
		public static final String ORGANIZATIONTASKDELETE = "/organizationTask/delete.json";
		//查询用户某天的打卡信息 /attendanceRecords/getAttendanceRecordsOfDate.json
		public static final String GETATTENDANCERECORDSOFDATE = "/attendanceRecords/getAttendanceRecordsOfDate.json";
		//获取考勤时间 /attendanceSystem/get.json
		public static final String GETRULE = "/attendanceSystem/get.json";
		//我的申批列表 /reviewRecords/getReviewApplicationList.json
		public static final String GETREVIEWAPPLICATIONLIST = "/reviewRecords/getReviewApplicationList.json";
		//我的申请列表 /reviewApplication/getMyApplyFor.json
		public static final String GETMYAPPLYFOR = "/reviewApplication/getMyApplyFor.json";
		//新建审请 /reviewApplication/createApplication.json
		public static final String CREATEAPPLICATION = "/reviewApplication/createApplication.json";
		//获取我在组织中的角色 /manage/role/getMyRole.json
		public static final String GETMYROLE = "/manage/role/getMyRole.json";
		//查询组织下的审批流程集合 /reviewProcess/getProcessByOrgId.json
		public static final String GETPROCESSBYORGID = "/reviewProcess/getProcessByOrgId.json";
		//重新分配组织任务接口 assignOrganizationTask.json
		public static final String ASSIGNORGANIZATIONTASK = "/organizationTask/assignOrganizationTask.json";
		//撤销我的申请/reviewApplication/recallRecords.json
		public static final String RECALLRECORDS = "/reviewApplication/recallRecords.json";
		//我的审批（同意、驳回）/reviewRecords/signOff.json
		public static final String SIGNOFF = "/reviewRecords/signOff.json";
		//删除消息操作 /message/delMessage.json 
		public static final String DELMESSAGE = "/message/delMessage.json";
		//(首页消息操作同意、忽略)申请类，退出申请，邀请类 /message/processing/handle.json
		public static final String PROCESSING = "/message/processing/handle.json";
	}
	/*
	 * 人脉请求相关API 对应的请求类型
	 */
	public static class PeopleRequestType {
  
		public static final int PEOPLE_REQ_BASE = 7000;
		public static final int PEOPLE_REQ_END = 7200;

		// 1.0 /person/createPeopleDetail.json 新增/修改人脉
		public static final int PEOPLE_REQ_CREATE = PEOPLE_REQ_BASE + 1;
		// 1.1 /person/removePeople.json 删除人脉
		public static final int PEOPLE_REQ_REMOVE = PEOPLE_REQ_BASE + 2;
		// 1.2 /person/getPeopleDetail.json 获取人脉详情
		public static final int PEOPLE_REQ_GETPEOPLE = PEOPLE_REQ_BASE + 3;
		/** 1.3 /person/personList.json 根据目录标签查人脉列表 */
		public static final int PEOPLE_REQ_PEOPLELIST = PEOPLE_REQ_BASE + 4;
		// 1.4 /person/convertToPeople.json 转为人脉
		public static final int PEOPLE_REQ_CONVERTPEOPLE = PEOPLE_REQ_BASE + 5;
		// 1.5 /person/peopleHomeList.json 人脉首页列表
		public static final int PEOPLE_REQ_HOME = PEOPLE_REQ_BASE + 6;
		// 1.6 /person/peopleMergeList.json 可合并资料的人脉列表
		public static final int PEOPLE_REQ_MERGELIST = PEOPLE_REQ_BASE + 7;
		// 1.7 /person/peopleMerge.json 合并人脉资料
		public static final int PEOPLE_REQ_MERGE = PEOPLE_REQ_BASE + 8;

		// 1.8 /person/meet/save.json 保存会面记录
		public static final int PEOPLE_REQ_MEET_SAVE = PEOPLE_REQ_BASE + 9;
		// 1.9 /person/meet/update.json 修改会面记录
		public static final int PEOPLE_REQ_MEET_UPDATE = PEOPLE_REQ_BASE + 10;
		// 2.0 /person/meet/findList.json 查询会面情况列表
		public static final int PEOPLE_REQ_MEET_FINDLIST = PEOPLE_REQ_BASE + 11;
		// 2.1 /person/meet/findOne.json 查询会面情况
		public static final int PEOPLE_REQ_MEET_FINDONE = PEOPLE_REQ_BASE + 12;
		// 2.2 /person/meet/deleteById.json 删除会面情况
		public static final int PEOPLE_REQ_MEET_DELETE = PEOPLE_REQ_BASE + 13;
		/** 2.3 /person/saveOrUpdateCategory.json 新增、修改目录 */
		public static final int PEOPLE_REQ_SAVEORUPDATECATEGORY = PEOPLE_REQ_BASE + 14;
		// 2.4 /person/findCategory.json 查询目录
		public static final int PEOPLE_REQ_FINDCATEGORY = PEOPLE_REQ_BASE + 15;
		/** 2.5 /person/removeCategory.json 删除目录 */
		public static final int PEOPLE_REQ_REMOVECATEGORY = PEOPLE_REQ_BASE + 16;
		// 2.6 /categoryRelation/collectPeople.json 收藏人脉
		public static final int PEOPLE_REQ_COLLECTPEOPLE = PEOPLE_REQ_BASE + 17;
		// 2.7 /categoryRelation/cancelCollect.json 取消收藏
		public static final int PEOPLE_REQ_CANCELCOLLECT = PEOPLE_REQ_BASE + 18;
		// 2.8 /code/peopleCodeList.json 职业列表查询、分类列表查询
		public static final int PEOPLE_REQ_PEOPLECODELIST = PEOPLE_REQ_BASE + 19;
		// 2.9 /person/findEvaluate.json 获取该用户的评价
		public static final int PEOPLE_REQ_FINDEVALUTE = PEOPLE_REQ_BASE + 20;
		// 2.10 /person/addEvaluate.json 添加评价
		public static final int PEOPLE_REQ_ADDEVALUATE = PEOPLE_REQ_BASE + 21;
		// 2.11 /person/feedbackEvaluate.json 赞同与取消赞同
		public static final int PEOPLE_REQ_FEEDBACKEVALUATE = PEOPLE_REQ_BASE + 22;
		// 2.12 /person/deleteEvaluate.json 删除评价
		public static final int PEOPLE_REQ_DELETEEVALUATE = PEOPLE_REQ_BASE + 23;
		// 2.13 /person/moreEvaluate.json 更多评价
		public static final int PEOPLE_REQ_MOREEVALUATE = PEOPLE_REQ_BASE + 24;
		// 2.14 /person/querytag.json 查询用户的标签使用个数
		public static final int PEOPLE_REQ_TAG_QUERY = PEOPLE_REQ_BASE + 25;
		// 2.15 /person/deletetag.json 删除标签信息接口
		public static final int PEOPLE_REQ_TAG_DELETE = PEOPLE_REQ_BASE + 26;
		// 2.15 /person/deletetag.json 修改和保存标签
		public static final int PEOPLE_REQ_TAG_SAVE = PEOPLE_REQ_BASE + 27;
		// 2.15 /person/deletetag.json 获得金桐网推荐标签
		public static final int PEOPLE_REQ_TAG_LIST = PEOPLE_REQ_BASE + 28;
		// 2.15 /person/mytag.json 查询我的标签信息
		public static final int PEOPLE_REQ_TAG_MY = PEOPLE_REQ_BASE + 29;
		// 2.15 /person/listtag.json 通过标签获取需求信息列表数据
		public static final int PEOPLE_REQ_LIST_TAG = PEOPLE_REQ_BASE + 30;
		// 2.15 /person/listtag.json 删除标签关系
		public static final int PEOPLE_DELETE_TAG = PEOPLE_REQ_BASE + 31;
		// 41 人脉举报person/report/save.json
		public static final int REPORT_SAVE = PEOPLE_REQ_BASE + 32;
		// 13 收藏人脉 /categoryRelation/collectPeople.json
		public static final int COLLECT_PEOPLE = PEOPLE_REQ_BASE + 33;
		// 14 取消收藏人脉 /categoryRelation/cancelCollect.json
		public static final int CANCEL_COLLECT = PEOPLE_REQ_BASE + 34;
	}

	public static class PeopleRequestUrl {
		// 1.0 /person/createPeopleDetail.json 新增/修改人脉
		public static final String CREATE_PEOPLE_STR = "person/createPeopleDetail.json";
		// 1.1 /person/removePeople.json 删除人脉
		public static final String REMOVE_PEOPLE_STR = "person/removePeople.json";
		// 1.2 /person/getPeopleDetail.json 获取人脉详情
		public static final String PEOPLE_DETAIL_STR = "person/getPeopleDetail.json";
		// 1.3 /person/personList.json 人脉列表
		public static final String PEOPLE_LIST_STR = "person/personList.json";
		// 1.4 /person/convertToPeople.json 转为人脉
		public static final String PEOPLE_CONVERT_STR = "person/convertToPeople.json";
		// 1.5 /person/peopleHomeList.json 人脉首页列表
		public static final String PEOPLE_HOMELIST_STR = "person/peopleHomeList.json";
		// 1.6 /person/peopleMergeList.json 可合并资料的人脉列表
		public static final String PEOPLE_MERGELIST_STR = "person/peopleMergeList.json";
		// 1.7 /person/peopleMerge.json 合并人脉资料
		public static final String PEOPLE_MERGE_STR = "person/peopleMerge.json";

		// 1.8 /person/meet/save.json 保存会面记录
		public static final String PEOPLE_MEET_SAVE_STR = "/person/meet/saveOrUpdate.json";
		// 1.9 /person/meet/update.json 修改会面记录
		public static final String PEOPLE_MEET_UPDATE_STR = "person/meet/update.json";
		// 2.0 /person/meet/findList.json 查询会面情况列表
		public static final String PEOPLE_MEET_FINDLIST_STR = "person/meet/findList.json";
		// 2.1 /person/meet/findOne.json 查询会面情况
		public static final String PEOPLE_MEET_FINDONE_STR = "person/meet/findMeet.json";
		// 2.2 /person/meet/deleteById.json 删除会面情况
		public static final String PEOPLE_MEET_DELETE_STR = "person/meet/deleteById.json";
		// 2.3 /person/saveOrUpdateCategory.json 新增、修改目录
		public static final String PEOPLE_SAVEORUPDATECATEGORY_STR = "person/saveOrUpdateCategory.json";
		// 2.4 /person/findCategory.json 查询目录
		public static final String PEOPLE_FINDCATEGORY_STR = "person/findCategory.json";
		// 2.5 /person/removeCategory.json 删除目录
		public static final String PEOPLE_REMOVECATEGORY_STR = "person/removeCategory.json";
		// 2.6 /categoryRelation/collectPeople.json 收藏人脉
		public static final String PEOPLE_COLLECTPEOPLE_STR = "categoryRelation/collectPeople.json";
		// 2.7 /categoryRelation/cancelCollect.json 取消收藏
		public static final String PEOPLE_CANCELCOLLECT_STR = "categoryRelation/cancelCollect.json";
		// 2.8 /code/peopleCodeList.json 职业列表查询、分类列表查询
		public static final String PEOPLE_PEOPLECODELIST_STR = "code/peopleCodeList.json";
		// 2.9 /person/findEvaluate.json 获取该用户的评价
		public static final String PEOPLE_FINDEVALUATE_STR = "person/findEvaluate.json";
		// 2.10 /person/addEvaluate.json 添加评价
		public static final String PEOPLE_ADDEVALUATE_STR = "person/addEvaluate.json";
		// 2.11 /person/feedbackEvaluate.json 赞同与取消赞同
		public static final String PEOPLE_FEEDBACKEVALUATE_STR = "person/feedbackEvaluate.json";
		// 2.12 /person/deleteEvaluate.json 删除评价
		public static final String PEOPLE_DELETEEVALUATE_STR = "person/deleteEvaluate.json";
		// 2.13 /person/moreEvaluate.json 更多评价
		public static final String PEOPLE_MOREEVALUATE_STR = "person/moreEvaluate.json";
		// 2.14 /person/querytag.json 查询用户的标签使用个数
		public static final String PEOPLE_QUERY_TAG = "personTag/group.json";
		// 2.15 /person/deletetag.json 删除标签信息接口
		public static final String PEOPLE_DELETE_TAG = "personTag/deleteById.json";
		// 2.16 /person/savetag.json 修改和保存标签
		public static final String PEOPLE_SAVE_TAG = "personTag/addTag.json";
		// 2.17 /person/listtag.json 获取金桐网推荐标签信息
		public static final String PEOPLE_LIST_TAG = "tag/list.json";
		// 2.18 /person/mytag.json 查询我的标签信息
		public static final String PEOPLE_MY_TAG = "personTag/list.json";
		// 2.19 /person/peoplelisttag.json 通过标签获取需求信息列表数据
		public static final String PEOPLE_REQ_LIST_TAG = "personTag/selectPersonByTag.json";
		// 2.20 /person/peoplelisttag.json 删除标签关系
		public static final String PEOPLE_DELETE_MY_TAG = "person/deletelisttag.json";
		// 41 人脉举报person/report/save.json
		public static final String REPORT_SAVE_STR = "person/report/save.json";

		// 13 收藏人脉 /categoryRelation/collectPeople.json
		public static final String COLLECT_PEOPLE_STR = "categoryRelation/collectPeople.json";
		// 14 取消收藏人脉 /categoryRelation/cancelCollect.json
		public static final String CANCEL_COLLECT_STR = "categoryRelation/cancelCollect.json";
	}
	/**
	 *社群请求api类型
	 */
	public static class CommunityReqUrl{
		// 1.获取社群所有成员详情
		public static final String GET_COMMUNITY_MEMBER_LIST="mobile/im/getCommunityMemberList.action";
		//2.获取畅聊的好友列表+畅聊创建者
		public static final String FETCH_FRIENDS="mobile/im/fetchFirends.action";
		//3.获取单聊、群聊聊天历史记录
		public static final String FETCH_HISTORY_MESSAGES="mobile/im/fetchHistoryMessages.action";
		//4.退出社群群聊 与现有畅聊逻辑相同
		public static final String EXIT_FROM_MUC="mobile/im/exitFromMUC.action";
		//5.社群群聊加人 与现有畅聊逻辑相同
		public static final String INVITE2MUC="mobile/im/invite2MUC.action";
		//6.社群群聊踢人
		public static final String KICK_FROM_MUC="mobile/im/kickFromMUC.action";
		//社群群聊踢人(批量)
		public static final String KICK_FROM_MUC_FOR_BATCH="/mobile/im/kickMutipleMembersForCommunity.action";
		
		//7.社群群聊修改名称
		public static final String MODIFY_MUC_NAME="mobile/im/modifyMuc.action";
		//8.社群中的解散该群
		public static final String EXIT_COMMUNITY="mobile/im/exitFromMUC.action";
		//9.社群属性设置
		public static final String SET_COMMUNITY="communitycon/setcommunity";
		//10.按照发言时间排序获取用户id集合, 时间倒序：最后发言的放在最前面
		public static final String ORDER_USER_BY_TIME="message/service/orderUsersByLastestMessageTime/";
//		//11.按照发言时间排序获取用户id集合, 时间倒序：最后发言的放在最前面
//		public static final String ORDER_USER_BY_TIME="message/service/orderUsersByLastestMessageTime/";
		//12.管理群成员-禁言（单个）
		public static final String MANAGE_COMMUNITY="managecommunity/notalk";
		//13.管理群成员-禁言（批量）
		public static final String MANAGE_COMMUNITY_BATCH="managecommunity/batchnotalk";
		//14.获取社群属性信息
		public static final String COMMUNITY_SET_DETAIL="communitycon/communitySettingDetail.json";
		//15.创建社群
		public static final String CREATE_COMMUNITY="community/createCommunity.json";
		//16.获取社群详情
		public static final String GET_COMMUNITY_DETAIL="community/getCommunityDetail.json";
		//17.获取社群的设置（权限）信息
		public static final String GET_COMMUNITY_SETPERSSION="community/getCommunitySetPerssion.json";
		//18.获取社群标签
		public static final String GET_COMMUNITY_LABELS="community/getCommunityLabels.json";
		//19.创建标签
		public static final String CREATE_LABEL="community/createLabel.json";
		//20.获取先决条件信息
		public static final String GET_PRECONDITION="community/getPrecondition.json";
		//21.添加社群标签
		public static final String ADD_COMMUNITY_LABEL="community/addCommunityLabel.json";
		//22.修改关联关系
		public static final String MODIFY_ASSO="community/modifyAsso.json";
		//23.修改社群权限设置
		public static final String MODIFY_COMMUNITY_PERMISSION="community/modifyCommunityPermission.json";
		//24.管理群成员-转让群组
		public static final String ASSIGNMENT_COMUNITY="managecommunity/assignmentCommunity.json";
		//25.删除社群标签
		public static final String DELETE_COMMUNITY_LABEL="community/deleteCommunityLabel.json";
		//26.删除标签
		public static final String DELETE_LABEL="comminity/deleteLabel.json";
		//27.管理群成员-举报(单)
		public static final String RESPORT_ONE="report/reportone";
		//28.管理群成员-举报(批)
		public static final String RESPORT_BATCH="report/reportbach";
		//29.获取社群通知列表根据社群id
		public static final String GET_NOTICE_LIST_BY_COMMUNITYID="community/notice/communityListByCommunityId.json";
		//30.获取社群通知列表根据用户id
		public static final String GET_NOTICE_LIST_BY_USERID="community/notice/communityListByUserId.json";
		//31.创建社群通知(申请加入社群，转让社群)
		public static final String CREATE_COMMUNITY_NOTICE="community/notice/createCommunityNotice.json";
		//32.处理社群的申请(同意/拒绝)
		public static final String HANDLE_APPLY="community/notice/handleApply.json";
		//33.修改社群基本信息
		public static final String MODIFY_COMMUNITYINFO="community/modifyCommunityInfo.json";
		//34.获取社群详详情中的群成员(包含群人员的数量，只获取10个成员，第一位是社群的群主)，接口参考【序号1】
		public static final String GET_COMMUNITY_MEMBER_DETAILS="mobile/im/getCommunityMemberDetails.action";
		//35.主页-社群列表
		public static final String MAIN_COMMUNITY_LIST="community/communityhomepage.json";
		//36.我的社群列表
		public static final String MY_COMMUNITY_LIST="managecommunity/communitylistuid.json";
		//37.获取公告
		public static final String GET_NOTICE="community/getNotice.json";
		//39通过社群号加社群
		public static final String GET_COMMUNITY_BY_COMMUNITYNO="community/getCommunityByCommunityNo.json";
		//40 修改社群标签（先删后加）
		public static final String MODIFY_COMMUNITYLABELS="community/modifyCommunityLabels.json";
		//41 判断社群号是否存在
		public static final String EXIST_COMMUNITYNO="community/existCommunityNo.json";
		//42 批量创建社群通知
		public static final String CREATE_BATCH_COMMUNITY_NOTICES="community/notice/createBatchCommunityNotices.json";
		//43 社群首页全局搜索
		public static final String SEARCH_HOMEPAGE="community/searchhomepage.json";
		//44 社群批量删除成员
		public static final String KICK_MUTIPLEMEMBERS_FOR_COMMUNITY="mobile/im/kickMutipleMembersForCommunity.action";
		//45获取社群的未读消息
		public static final String GET_COMMUNITY_NEWCOUNT_BY_USERID="meeting/getCommunityNewCountByUserId/";
		//46获取登录用户的具有未读消息的社群列表
		public static final String GET_COMMUNITY_LIST="meeting/getCommunityList.json";
	}
	/**
	 *社群请求api接口
	 */
	public static class CommunityReqType{
		public static final int  Community_REQ_BASE = 6300;
		public static final int  Community_REQ_END = 6500;

		// 1.获取社群所有成员详情
		public static final int  TYPE_GET_COMMUNITY_MEMBER_LIST = Community_REQ_BASE + 1;
		//2.获取畅聊的好友列表+畅聊创建者
		public static final int  TYPE_FETCH_FRIENDS = Community_REQ_BASE + 2;
		//3.获取单聊、群聊聊天历史记录
		public static final int  TYPE_FETCH_HISTORY_MESSAGES = Community_REQ_BASE + 3;
		//4.退出社群群聊
		public static final int  TYPE_EXIT_FROM_MUC = Community_REQ_BASE + 4;
		//5.社群群聊加人
		public static final int  TYPE_INVITE2MUC = Community_REQ_BASE + 5;
		//6.社群群聊踢人
		public static final int  TYPE_KICK_FROM_MUC = Community_REQ_BASE + 6;
		
		//7.社群群聊修改名称
		public static final int  TYPE_MODIFY_MUC_NAME = Community_REQ_BASE + 7;
		//8.社群中的解散该群
		public static final int  TYPE_EXIT_COMMUNITY = Community_REQ_BASE + 8;
		//9.社群属性设置
		public static final int  TYPE_SET_COMMUNITY = Community_REQ_BASE + 9;
		//10.按照发言时间排序获取用户id集合
		public static final int  TYPE_ORDER_USER_BY_TIME = Community_REQ_BASE + 10;
//		//11.
//		public static final int  TYPE_ORDER_USER_BY_TIME = Community_REQ_BASE + 11;
		//12..管理群成员-禁言（单个）
		public static final int  TYPE_MANAGE_COMMUNITY = Community_REQ_BASE + 12;
		//13.管理群成员-禁言（批量
		public static final int  TYPE_MANAGE_COMMUNITY_BATCH = Community_REQ_BASE + 13;
		//14.获取社群属性信息
		public static final int  TYPE_COMMUNITY_SET_DETAIL = Community_REQ_BASE + 14;
		//15.创建社群
		public static final int  TYPE_CREATE_COMMUNITY = Community_REQ_BASE + 15;
		//16.获取社群详情
		public static final int  TYPE_GET_COMMUNITY_DETAIL = Community_REQ_BASE + 16;
		//17.获取社群的设置（权限）信息
		public static final int  TYPE_GET_COMMUNITY_SETPERSSION = Community_REQ_BASE + 17;
		//18.获取社群标签
		public static final int  TYPE_GET_COMMUNITY_LABELS = Community_REQ_BASE + 18;
		//19.创建标签
		public static final int  TYPE_CREATE_LABEL = Community_REQ_BASE + 19;
		//20.获取先决条件信息
		public static final int  TYPE_GET_PRECONDITION = Community_REQ_BASE + 20;
		//21.添加社群标签
		public static final int  TYPE_ADD_COMMUNITY_LABEL = Community_REQ_BASE + 21;
		//22.修改关联关系
		public static final int  TYPE_MODIFY_ASSO = Community_REQ_BASE + 22;
		//23.修改社群权限设置
		public static final int  TYPE_MODIFY_COMMUNITY_PERMISSION = Community_REQ_BASE + 23;
		//24.管理群成员-转让群组
		public static final int  TYPE_ASSIGNMENT_COMUNITY = Community_REQ_BASE + 24;
		//25.删除社群标签
		public static final int  TYPE_DELETE_COMMUNITY_LABEL = Community_REQ_BASE + 25;
		//26.删除标签
		public static final int  TYPE_DELETE_LABEL = Community_REQ_BASE + 26;
		//27.管理群成员-举报(单)
		public static final int  TYPE_RESPORT_ONE = Community_REQ_BASE + 27;
		//28.管理群成员-举报(批)
		public static final int  TYPE_RESPORT_BATCH = Community_REQ_BASE + 28;
		//29.获取社群通知列表根据社群id
		public static final int  TYPE_GET_NOTICE_LIST_BY_COMMUNITYID = Community_REQ_BASE + 29;
		//30.获取社群通知列表根据用户id
		public static final int  TYPE_GET_NOTICE_LIST_BY_USERID = Community_REQ_BASE + 30;
		//31.创建社群通知(申请加入社群，转让社群)
		public static final int  TYPE_CREATE_COMMUNITY_NOTICE = Community_REQ_BASE + 31;
		//32.处理社群的申请(同意/拒绝)
		public static final int  TYPE_HANDLE_APPLY = Community_REQ_BASE + 32;
		//33.修改社群基本信息
		public static final int  TYPE_MODIFY_COMMUNITYINFO = Community_REQ_BASE + 33;
		//34.获取社群详详情中的群成员(包含群人员的数量，只获取10个成员，第一位是社群的群主)，接口参考【序号1】
		public static final int  TYPE_GET_COMMUNITY_MEMBER_DETAILS = Community_REQ_BASE + 34;
		//35.主页-社群列表
		public static final int  TYPE_MAIN_COMMUNITY_LIST = Community_REQ_BASE + 35;
		//36.我的社群列表
		public static final int  TYPE_MY_COMMUNITY_LIST = Community_REQ_BASE + 36;
		//37.获取公告
		public static final int  TYPE_GET_NOTICE = Community_REQ_BASE + 37;
		//39.通过社群号加社群
		public static final int TYPE_GET_COMMUNITY_BY_COMMUNITYNO = Community_REQ_BASE + 39;
		//40.修改社群标签
		public static final int TYPE_MODIFY_COMMUNITYLABELS = Community_REQ_BASE + 40;
		//41.判断社群号是否存在
		public static final int TYPE_EXIST_COMMUNITYNO = Community_REQ_BASE + 41;
		//42.批量创建社群通知
		public static final int TYPE_CREATE_BATCH_COMMUNITY_NOTICES = Community_REQ_BASE + 42;
		//社群群聊踢人(批量)
		public static final int  TYPE_KICK_FROM_MUC_FOR_BATCH = Community_REQ_BASE + 43;
		//45获取社群的未读消息
		public static final int TYPE_GET_COMMUNITY_NEWCOUNT_BY_USERID = Community_REQ_BASE + 45;
		//46获取登录用户的具有未读消息的社群列表
		public static final int TYPE_GET_COMMUNITY_LIST = Community_REQ_BASE + 46;
	}
	
	public static class handler {
		public static final byte show_err = 1;
	}

	public static final int MSG_INIT_DEVICE = 2111;// 集成播控平台接入协议初始化设备，api2.1.1
	public static final int MSG_LOGIN_DEVICE = 2122;// 集成播控平台接入协议登录，api2.1.1
	public static final int MSG_CHECK_VERSION = 2123;// 检查版本号
	public static final int IM_REQ_SET_CHANNELID = 0;

	public static class JsonKey {

		public static final String CONTACT_listPhoneBookItem = "listPhoneBookItem";
		public static final String CONTACT_familyName = "lastName";
		public static final String CONTACT_givenName = "firstName";
		public static final String CONTACT_listMobilePhone = "listMobilePhone";
		public static final String CONTACT_listEmail = "listEmail";

		public static final String CONTACT_comment = "comment";
		public static final String CONTACT_listPersonInfo = "listPersonInfo";
	}
}
