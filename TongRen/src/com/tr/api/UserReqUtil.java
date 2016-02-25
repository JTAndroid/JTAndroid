package com.tr.api;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.os.Handler;
import com.tr.model.obj.Requirement;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.string.Base64;

/**
 * @ClassName:     UserReqUtil.java
 * @Description:   用户相关的接口
 * @Author         leon
 * @Version        v 1.0  
 * @Date           2014-04-11
 * @LastEdit       2014-04-11
 */
public class UserReqUtil extends ReqBase {
	
	/**
	 * 登录配置
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doLoginConfiguration(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler) {

		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.LOGIN_CONFIGURATION;
		doExecute(context, bind, EAPIConsts.ReqType.LOGIN_CONFIGURATION, url,
				requestStr, handler);
	}
	
	/**
	 * 用户登录
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doLogin(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler) {

		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.LOGIN;
		doExecute(context, bind, EAPIConsts.ReqType.LOGIN, url, requestStr,
				handler);
	}
	
	/**
	 * 用户登出
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doLoginOut(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler) {

		String requestStr = jsonObject.toString();
		String url =  EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.LOGIN_OUT;
		doExecute(context, bind, EAPIConsts.ReqType.LOGIN_OUT, url, requestStr,
				handler);
	}
	
	/**
	 * 用户注册
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doRegister(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler) {

		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.REGISTER;
		doExecute(context, bind, EAPIConsts.ReqType.REGISTER, url, requestStr,
				handler);
	}
	
	
	/**
	 * 查询组织信息
	 * @param context
	 * @param bind
	 * @param orgId
	 * @param handler
	 */
	public static void doFindOrg(Context context, IBindData bind,String orgId, Handler handler) {
		
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("orgId", orgId);
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.FINDORG;
		doExecute(context, bind, EAPIConsts.ReqType.FINDORG, url, jObject.toString(),handler);
	}
	
	
	
	
	
	/**
	 * 获取验证码
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doGetVerifyCode(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler) {

		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.GET_VERIFY_CODE;
		doExecute(context, bind, EAPIConsts.ReqType.GET_VERIFY_CODE, url,
				requestStr, handler);
	}
	
	/**
	 * 完善个人会员信息
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doFullPersonMemberInfo(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler) {

		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.FULL_PERSON_MEMBER_INFO;
		doExecute(context, bind, EAPIConsts.ReqType.FULL_PERSON_MEMBER_INFO, url,
				requestStr, handler);
	}
	
	/**
	 * 完善机构会员联系人信息
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doFullContactInfo(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler) {

		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.FULL_CONTACT_INFO;
		doExecute(context, bind, EAPIConsts.ReqType.FULL_CONTACT_INFO, url,
				requestStr, handler);
	}
	
	/**
	 * 上传机构验证信息
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doFullOrganizationAuth(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler) {

		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.FULL_ORGANIZATION_AUTH;
		doExecute(context, bind, EAPIConsts.ReqType.FULL_ORGANIZATION_AUTH, url,
				requestStr, handler);
	}
	
	/**
	 * 发送验证邮箱
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doSendValidateEmail(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler){
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.SEND_VALIDATE_EMAIL;
		doExecute(context, bind, EAPIConsts.ReqType.SEND_VALIDATE_EMAIL, url,
				requestStr, handler);
	}

	/**
	 * 设置新密码
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doSetNewPassword(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler) {

		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.SET_NEW_PASSWORD;
		doExecute(context, bind, EAPIConsts.ReqType.SET_NEW_PASSWORD, url,
				requestStr, handler);
	}
	
	/**
	 * 发布需求
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doAddRequirement(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler) {

		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.ADD_REQUIREMENT;
		doExecute(context, bind, EAPIConsts.ReqType.ADD_REQUIREMENT, url,
				requestStr, handler);
	}
	
	/**
	 * 需求详情
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doGetRequirementById(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler){
		
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.GET_REQUIREMENT_BY_ID;
		doExecute(context, bind, EAPIConsts.ReqType.GET_REQUIREMENT_BY_ID, url,
				requestStr, handler);
	}
	
	/**
	 * 编辑需求
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doEditRequirement(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler){
		
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.EDIT_REQUIREMENT;
		doExecute(context, bind, EAPIConsts.ReqType.EDIT_REQUIREMENT, url,
				requestStr, handler);
	}
	/**
	 * 判断组织全称或组织邮箱是否被注册
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doJudgeUserAndMail(Context context, IBindData bind, JSONObject jsonObject, Handler handler){
		
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.JUDGE_USERANDMAIL;
		doExecute(context, bind, EAPIConsts.ReqType.JUDGE_USERANDMAIL, url,requestStr, handler);
	}
	
	/**
	 * 关闭需求
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doCloseRequirement(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler){
		
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.CLOSE_REQUIREMENT;
		doExecute(context, bind, EAPIConsts.ReqType.CLOSE_REQUIREMENT, url,
				requestStr, handler);
	}
	
	/**
	 * 关注或取消关注需求
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doFocusRequirement(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler){
		
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.FOCUS_REQUIREMENT;
		doExecute(context, bind, EAPIConsts.ReqType.FOCUS_REQUIREMENT, url,
				requestStr, handler);
	}
	
	/**
	 * 需求转为业务需求
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doRequirementToBusinessRequirement(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler){
		
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.REQUIREMENT_TO_BUSINESS_REQUIREMENT;
		doExecute(context, bind, EAPIConsts.ReqType.REQUIREMENT_TO_BUSINESS_REQUIREMENT, url,
				requestStr, handler);
	}
	
	/**
	 * 需求转为项目
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doRequirementToProject(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler){
		
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.REQUIREMENT_TO_PROJECT;
		doExecute(context, bind, EAPIConsts.ReqType.REQUIREMENT_TO_PROJECT, url,
				requestStr, handler);
	}
	
	/**
	 * 添加好友
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doAddFriend(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler){
		
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.ADD_FRIEND;
		doExecute(context, bind, EAPIConsts.ReqType.ADD_FRIEND, url,
				requestStr, handler);
	}
	
	/**
	 * 发表评论
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doAddComment(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler){
		
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.ADD_COMMENT;
		doExecute(context, bind, EAPIConsts.ReqType.ADD_COMMENT, url,
				requestStr, handler);
	}
	
	/**
	 * 获取评论列表
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doGetListComment(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler){
		
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.GET_LIST_COMMENT;
		doExecute(context, bind, EAPIConsts.ReqType.GET_LIST_COMMENT, url,
				requestStr, handler);
	}
	
	/**
	 * 获取评论列表
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doGetDynamicListComment(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler){
		
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.GET_DYNAMIC_LIST_COMMENT;
		doExecute(context, bind, EAPIConsts.ReqType.GET_DYNAMIC_LIST_COMMENT, url,
				requestStr, handler);
	}
	
	/**
	 * 创建业务需求
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doAddBusinessRequirement(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler){
		
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.ADD_BUSINESS_REQUIREMENT;
		doExecute(context, bind, EAPIConsts.ReqType.ADD_BUSINESS_REQUIREMENT, url,
				requestStr, handler);
	}
	
	/**
	 * 编辑业务需求
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doEditBusinessRequirement(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler){
		
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.EDIT_BUSINESS_REQUIREMENT;
		doExecute(context, bind, EAPIConsts.ReqType.EDIT_BUSINESS_REQUIREMENT, url,
				requestStr, handler);
	}
	
	/**
	 * 关闭业务需求
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doCloseBusinessRequirement(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler){
		
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.CLOSE_BUSINESS_REQUIREMENT;
		doExecute(context, bind, EAPIConsts.ReqType.CLOSE_BUSINESS_REQUIREMENT, url,
				requestStr, handler);
	}
	
	/**
	 * 获取业务需求详情
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doGetBusinessRequirementDetailById(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler){
		
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.GET_BUSINESS_REQUIREMENT_DETAIL_BY_ID;
		doExecute(context, bind, EAPIConsts.ReqType.GET_BUSINESS_REQUIREMENT_DETAIL_BY_ID, url,
				requestStr, handler);
	}
	
	/**
	 * 创建任务
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doAddTask(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler){
		
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.ADD_TASK;
		doExecute(context, bind, EAPIConsts.ReqType.ADD_TASK, url,
				requestStr, handler);
	}
	
	/**
	 * 编辑任务
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doEditTask(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler){
		
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.EDIT_TASK;
		doExecute(context, bind, EAPIConsts.ReqType.EDIT_TASK, url,
				requestStr, handler);
	}
	
	/**
	 * 关闭任务
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doCloseTask(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler){
		
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.CLOSE_TASK;
		doExecute(context, bind, EAPIConsts.ReqType.CLOSE_TASK, url,
				requestStr, handler);
	}
	
	/**
	 * 获取任务详情
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doGetTaskDetailByID(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler){
		
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.GET_TASK_DETAIL_BY_ID;
		doExecute(context, bind, EAPIConsts.ReqType.GET_TASK_DETAIL_BY_ID, url,
				requestStr, handler);
	}
	
	/**
	 * 创建项目
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doAddProject(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler){
		
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.ADD_PROJECT;
		doExecute(context, bind, EAPIConsts.ReqType.ADD_PROJECT, url,
				requestStr, handler);
	}
	
	/**
	 * 编辑项目
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doEditProject(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler){
		
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.EDIT_PROJECT;
		doExecute(context, bind, EAPIConsts.ReqType.EDIT_PROJECT, url,
				requestStr, handler);
	}
	
	/**
	 * 关闭项目
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doCloseProject(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler){
		
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.CLOSE_PROJECT;
		doExecute(context, bind, EAPIConsts.ReqType.CLOSE_PROJECT, url,
				requestStr, handler);
	}
	
	/**
	 * 获取项目详情
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doGetProjectDetailByID(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler){
		
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.GET_PROJECT_DETAIL_BY_ID;
		doExecute(context, bind, EAPIConsts.ReqType.GET_PROJECT_DETAIL_BY_ID, url,
				requestStr, handler);
	}
	
	/**
	 * 获取知识详情
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doGetKnowledgeDetailByID(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler){
		
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.GET_KNOWLEDGE_DETAIL_BY_ID;
		doExecute(context, bind, EAPIConsts.ReqType.GET_KNOWLEDGE_DETAIL_BY_ID, url,
				requestStr, handler);
	}
	
	/**
	 * 获取知识详情
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 * @deprecated
	 */
	public static void doGetKnowledgeDetail(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler){
		
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.GET_KNOWLEDGE_DETAIL;
		doExecute(context, bind, EAPIConsts.ReqType.GET_KNOWLEDGE_DETAIL, url,
				requestStr, handler);
	}
	
	/**
	 * 获取匹配知识
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doGetMatchKnowledgeMini(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler){
		
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.GET_MATCH_KNOWLEDGE_MINI;
		doExecute(context, bind, EAPIConsts.ReqType.GET_MATCH_KNOWLEDGE_MINI, url,
				requestStr, handler);
	}
	
	/**
	 * 获取匹配需求
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doGetMatchRequirementMini(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler){
		
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.GET_MATCH_REQUIREMENT_MINI;
		doExecute(context, bind, EAPIConsts.ReqType.GET_MATCH_REQUIREMENT_MINI, url,
				requestStr, handler);
	}
	
	
	/**
	 * 按关键字搜索需求列表
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doGetListRequirement(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler){
		
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.GET_LIST_REQUIREMENT;
		doExecute(context, bind, EAPIConsts.ReqType.GET_LIST_REQUIREMENT, url,
				requestStr, handler);
	}
	
	/**
	 * 按关键字搜索事务列表
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doGetListAffair(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler){
		
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.GET_LIST_AFFAIR;
		doExecute(context, bind, EAPIConsts.ReqType.GET_LIST_AFFAIR, url,
				requestStr, handler);
	}
	
	/**
	 * 操作项目
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doOperateProject(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler){
		
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.OPERATE_PROJECT;
		doExecute(context, bind, EAPIConsts.ReqType.OPERATE_PROJECT, url,
				requestStr, handler);
	}
	
	/**
	 * 上传文件
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doUploadFile(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler){
		
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.UPLOAD_FILE;
		doExecute(context, bind, EAPIConsts.ReqType.UPLOAD_FILE, url,
				requestStr, handler);
	}
	
	/**
	 * 添加我的知识
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	/*
	public static void doAddMyKnowledge(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler){
		
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.ADD_MY_KNOWLEDGE;
		doExecute(context, bind, EAPIConsts.ReqType.ADD_MY_KNOWLEDGE, url,
				requestStr, handler);
	}
	*/
	
	/**
	 * 获取处理后的网页及其标题
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doGetTreatedHtml(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler){
		
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.ReqUrl.GET_TREATED_HTML;
		doExecute(context, bind, EAPIConsts.ReqType.GET_TREATED_HTML, url,
				requestStr, handler);
	}
	
	/**
	 * 删除附件
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doDeleteFile(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler) {

		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.DELETE_FILE;
		doExecute(context, bind, EAPIConsts.ReqType.DELETE_FILE, url, requestStr, handler);
	}
	
	/**
	 * 第三方登陆
	 * 
	 * @param context
	 * @param bind
	 * @param clientID
	 *            客户端串号
	 * @param clientPassword
	 *            客户端配置登录密码
	 * @param imei
	 *            手机串号
	 * @param version
	 *            客户端版本号,四段数字,如1.6.0.0609
	 * @param platform
	 *            平台，如：iPhone
	 * @param model
	 *            型号,如:iPhone 3G
	 * @param resolution
	 *            分辨率,如:480x320
	 * @param systemName
	 *            系统名称,如:iOS
	 * @param systemVersion
	 *            系统版本 (版本号用点号分开)
	 * @param channelID
	 *            渠道id
	 * @param access_token
	 *            第三方访问令牌(QQ,weibo)
	 * @param openid
	 *            QQ或weibo的userid
	 * @param login_type
	 *            登陆类型 QQ=100;weibo=200
	 * @param handler
	 */
	public static void doThird_Login(Context context, IBindData bind,
			String clientID, String clientPassword, String imei,
			String version, String platform, String model, String resolution,
			String systemName, String systemVersion, String channelID,
			String access_token, String openid, String login_type,
			Handler handler) {
		String requestStr = "";
		JSONObject jObj = getThird_Login_Params(clientID, clientPassword, imei,
				version, platform, model, resolution, systemName,
				systemVersion, channelID, access_token, openid, login_type);
		requestStr = jObj.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.THIRD_LOGIN;
		doExecute(context, bind, EAPIConsts.ReqType.THIRD_LOGIN, url,
				requestStr, handler);

	}
	
	/**
	 * 绑定QQ /Sina微博
	 * @param context
	 * @param bind
	 * @param access_token QQ/Sina微博token
	 * @param openid QQ/Sina微博userid
	 * @param login_type 100:qq 200：微博
	 * @param sessionID 会话ID
	 * @param handler
	 */
	public static void doBindingQQ_WB(Context context, IBindData bind,String access_token,String openid,int login_type,String sessionID,Handler handler){
		String requestStr = "";
		JSONObject jObj = getBindingQQ_WB_Params(access_token, openid, login_type, sessionID);
		requestStr = jObj.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.SET_Binding_QQ_WB;
		doExecute(context, bind, EAPIConsts.ReqType.SET_BINDING_QQ_WB, url,
				requestStr, handler);
	}
	/**
	 * 解绑QQ /Sina微博
	 * @param context
	 * @param bind
	 * @param status 1:qq解绑 2：微博解绑"
	 * @param handler
	 */
	public static void doUnBindingQQ_WB(Context context, IBindData bind,String status,Handler handler){
		String requestStr = "";
			JSONObject jObj = getUnBindingQQ_WB_Params(status);
			requestStr = jObj.toString();
			String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.SET_UnBinding_QQ_WB;
			doExecute(context, bind, EAPIConsts.ReqType.SET_UNBINDING_QQ_WB, url,
					requestStr, handler);
	}
	
	/**
	 * 判断手机或邮箱是否可绑
	 * 
	 * @param context
	 * @param bind
	 * @param status 1:mobile 2：Email"
	 * @param name 手机或邮箱号
	 * @param handler
	 */
	public static void doSet_MobileOrEmailwhetherCanBinding(Context context,
			IBindData bind, String status, String name, Handler handler) {
		String requestStr = "";
		JSONObject jObj = getSet_MobileOrEmailwhetherCanBindingParams(status,
				name);
		requestStr = jObj.toString();
		String url = EAPIConsts.TMS_URL
				+ EAPIConsts.ReqUrl.SET_MOBILE_EMAIL_WHETHER_CAN_BINDING;
		doExecute(context, bind,
				EAPIConsts.ReqType.SET_MOBILE_EMAIL_WHETHER_CAN_BINDING, url,
				requestStr, handler);
	}
	
	/**
	 * 验证新/老邮箱 是否验证成功
	 * 
	 * @param context
	 * @param bind
	 * @param status 1:old Email 2：New Email"
	 * @param email 邮箱号
	 * @param handler
	 */
	public static void doSet_CheckEmailStatus(Context context, IBindData bind,
			String status, String email, Handler handler) {
		String requestStr = "";
		JSONObject jObj = getSet_CheckEmailStatusParams(status, email);
		requestStr = jObj.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.SET_CHECK_EMAIL_STATUS;
		doExecute(context, bind, EAPIConsts.ReqType.SET_CHECK_EMAIL_STATUS,
				url, requestStr, handler);
	}
	
	/**
	 * 验证新/老手机 是否验证成功
	 * 
	 * @param context
	 * @param bind
	 * @param status 1:old mobile 2：New mobile"
	 * @param code 验证码
	 * @param mobile  mobile号
	 * @param handler
	 */
	public static void doSet_CheckMobileStatus(Context context, IBindData bind,
			String status, String code, String mobile, Handler handler) {
		String requestStr = "";
		JSONObject jObj = getSet_CheckMobileStatusParams(status, code, mobile);
		requestStr = jObj.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.SET_CHECK_MOBILE_STATUS;
		doExecute(context, bind, EAPIConsts.ReqType.SET_CHECK_MOBILE_STATUS,
				url, requestStr, handler);
	}
	
	/**
	 * 发送验证邮件到新/老邮箱
	 * 
	 * @param context
	 * @param bind
	 * @param status 1:老邮箱 2：新邮箱 3：直接绑定(原来就没有邮箱)
	 * @param email 邮箱号
	 * @param from 1： 移动端
	 * @param handler
	 */
	public static void doSet_SendValidateEmail(Context context, IBindData bind,
			String status, String email, String from,Handler handler) {
		String requestStr = "";
		JSONObject jObj = getSet_SendValidateEmailParams(status, email,from);
		requestStr = jObj.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.SET_SEND_VALIDATE_EMAIL;
		doExecute(context, bind, EAPIConsts.ReqType.SEND_VALIDATE_EMAIL, url,
				requestStr, handler);
	}
	
	/**
	 * 刷新账号信息
	 * 
	 * @param context
	 * @param bind
	 * @param status 1:old email 2：New email"
	 * @param email 邮箱号
	 * @param handler
	 */
	public static void doRefresh_AccountInfo(Context context, IBindData bind,
			Handler handler) {// /jtmember login/refreshJtmember.json
		String requestStr = "";
		try {
			JSONObject jObj = new JSONObject();
			requestStr = jObj.toString();
			String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.SET_REFRESH_ACCOUNT_INFO;
			doExecute(context, bind,
					EAPIConsts.ReqType.SET_REFRESH_ACCOUNT_INFO, url,
					requestStr, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//下面是拼json参数的方法
	
	/**
	 * 发送验证邮件到新/老邮箱参数
	 * @param status 1:老邮箱 2：新邮箱 3：直接绑定(原来就没有邮箱)
	 * @param email 邮箱号
	 * @param from  1:移动端 
	 * @return
	 */
	public static JSONObject getSet_SendValidateEmailParams(String status,
			String email, String from) {
		JSONObject jObj = new JSONObject();
		try {
			jObj.put("status", status);
			jObj.put("email", email);
			jObj.put("from", from);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jObj;
	}
	/**
	 * 验证新/老手机 是否验证成功参数
	 * @param status
	 * @param code
	 * @param mobile
	 * @return
	 */
	public static JSONObject getSet_CheckMobileStatusParams(String status,
			String code, String mobile) {
		JSONObject jObj = new JSONObject();
		try {
			jObj.put("status", status);
			jObj.put("code", code);
			jObj.put("mobile", mobile);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jObj;
	}
	/**
	 * 验证新/老邮箱 是否验证成功参数
	 * @param status
	 * @param email
	 * @return
	 */
	public static JSONObject getSet_CheckEmailStatusParams(String status,
			String email) {
		JSONObject jObj = new JSONObject();
		try {
			jObj.put("status", status);
			jObj.put("email", email);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jObj;
	}
	/**
	 * 判断手机或邮箱是否可绑参数
	 * @param status  1:mobile 2：Email"
	 * @param name 手机或邮箱号
	 * @return
	 */
	public static JSONObject getSet_MobileOrEmailwhetherCanBindingParams(
			String status, String name) {
		JSONObject jObj = new JSONObject();
		try {
			jObj.put("status", status);
			jObj.put("name", name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jObj;
	}

	/**
	 * 绑定QQ/微博参数
	 * 
	 * @param access_token
	 * @param openid
	 * @param login_type
	 * @param sessionID
	 * @return
	 */
	public static JSONObject getBindingQQ_WB_Params(String access_token,
			String openid, int login_type, String sessionID) {
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("access_token", access_token);
			jObject.put("openid", openid);
			jObject.put("login_type", login_type);
			jObject.put("sessionID", sessionID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}
	/**
	 * 解绑QQ/微博参数
	 * @param status 1:qq解绑 2：微博解绑"
	 * @return
	 */
	public static JSONObject getUnBindingQQ_WB_Params(String status) {
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("status", status);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}
	/**
	 * 第三方登陆参数
	 * @param clientID
	 * @param clientPassword
	 * @param imei
	 * @param version
	 * @param platform
	 * @param model
	 * @param resolution
	 * @param systemName
	 * @param systemVersion
	 * @param channelID
	 * @param access_token QQ/微博的Token
	 * @param openid 第三方QQ/微博的userId
	 * @param login_type  100 ：QQ， 200：微博
	 * @return
	 */
	public static JSONObject getThird_Login_Params(
			String clientID, String clientPassword, String imei,
			String version, String platform, String model, String resolution,
			String systemName, String systemVersion, String channelID,
			String access_token, String openid, String login_type
			) {
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("clientID", clientID);
			jObject.put("clientPassword", clientPassword);
			jObject.put("imei", imei);
			jObject.put("version", version);
			jObject.put("platform", platform);
			jObject.put("model", model);
			jObject.put("resolution", resolution);
			jObject.put("systemName", systemName);
			jObject.put("systemVersion", systemVersion);
			jObject.put("channelID", channelID);
			jObject.put("access_token", access_token);
			jObject.put("openid", openid);
			jObject.put("login_type", login_type);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}
	
	/**
	 * 获取登录配置参数对象
	 * @param clientID	客户端串号
	 * @param clientPassword	
	 * @param imei	手机串号
	 * @param version	客户端版本号,四段数字,如1.6.0.0609
	 * @param platform 平台,如:iPhone
	 * @param model	型号,如:iPhone 3G
	 * @param resolution 分辨率,如:480x320
	 * @param systemName 系统名称,如:iOS
	 * @param systemVersion 系统版本 (版本号用点号分开)
	 * @param channelID 渠道id
	 * @param loginString 登录字符串，对于之前登陆过的用户自动登录时使用
	 * @param password 密码
	 * @return
	 */
	public static JSONObject getDoLoginConfigurationParams(String clientID,
			String clientPassword, String imei, String version,
			String platform, String model, String resolution,
			String systemName, String systemVersion, String channelID,
			String loginString, String password) {
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("clientID", clientID);
			jObject.put("clientPassword", clientPassword);
			jObject.put("imei", imei);
			jObject.put("version", version);
			jObject.put("platform", platform);
			jObject.put("model", model);
			jObject.put("resolution", resolution);
			jObject.put("systemName", systemName);
			jObject.put("systemVersion", android.os.Build.VERSION.SDK_INT + "");
			jObject.put("channelID", channelID);
			jObject.put("loginString", loginString);
			String encPwd = new String(Base64.encode(password.getBytes()));
			jObject.put("password", encPwd);
			// jObject.put("password", EUtil.encrypt(password));
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}
	/**
	 * 第三方登陆后获取登录配置参数对象
	 * @param clientID	客户端串号
	 * @param clientPassword	
	 * @param imei	手机串号
	 * @param version	客户端版本号,四段数字,如1.6.0.0609
	 * @param platform 平台,如:iPhone
	 * @param model	型号,如:iPhone 3G
	 * @param resolution 分辨率,如:480x320
	 * @param systemName 系统名称,如:iOS
	 * @param systemVersion 系统版本 (版本号用点号分开)
	 * @param channelID 渠道id
	 * @param loginString 登录字符串，对于之前登陆过的用户自动登录时使用
	 * @param password 密码
	 * @param mToken 第三方QQ、微博的Token
	 * @param mNickName 昵称
	 * @param mLoginType QQ/微博 100qq  200weibo
	 * @return
	 */
	public static JSONObject getDoLoginConfigurationParams(String clientID,
			String clientPassword, String imei, String version,
			String platform, String model, String resolution,
			String systemName, String systemVersion, String channelID,
			String loginString, String password, String mToken,
			String mNickName, int mLoginType) {
		JSONObject jObject = new JSONObject();
		try {
			jObject = getDoLoginConfigurationParams(clientID,
					clientPassword, imei, version, platform, model, resolution,
					systemName, systemVersion, channelID, loginString, password);
			jObject.put("access_token", mToken);
			jObject.put("mNickName", mNickName);
			jObject.put("login_type", mLoginType);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}
	
	/**
	 * 获取用户登录参数对象
	 * @param account
	 * @param password
	 * @return
	 */
	public static JSONObject getDoLoginParams(String account, String password) {
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("loginString", account);
			String encPwd = new String(Base64.encode(password.getBytes()));
			// jObject.put("password", EUtil.encrypt(password));
			jObject.put("password", encPwd);
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}
	/**
	 * 第三方绑定已有账号获取用户登录参数对象
	 * @param account
	 * @param password
	 * @param mToken 第三方QQ、微博的Token
	 *  @param mLoginType QQ/微博 100qq  200weibo
	 * @return
	 */
	public static JSONObject getDoLoginParams(String account, String password,String mToken,int mLoginType) {
		JSONObject jObject = new JSONObject();
		try {
			jObject=getDoLoginParams(account, password);
			jObject.put("access_token", mToken);
			jObject.put("login_type", mLoginType);
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}
	
	/**
	 * 获取用户注册参数对象
	 * @param mobile 手机号码
	 * @param mobileAreaCode 手机号码前缀(国家区号)
	 * @param email 邮箱，手机号码邮箱二选一，没选的为空 
	 * @param password 密码
	 * @param code 手机验证码，手机注册时有效 
	 * @param userType 1-个人用户，2-机构用户 
	 * @param orgName 组织全称
	 * @return
	 */
	public static JSONObject getDoRegisterParams(String mobile, String mobileAreaCode,String email,String password, String code, int userType,String orgName) {
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("mobile", mobile);
			jObject.put("mobileAreaCode", mobileAreaCode);
			jObject.put("email", email);
			
			String encPwd = new String(Base64.encode(password.getBytes()));
			jObject.put("password", encPwd);
			jObject.put("code", code);
			jObject.put("userType", userType);
			jObject.put("orgName", orgName);
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}
	/**
	 * 获取用户注册参数对象
	 * @param mobile 手机号码
	 * @param email 邮箱，手机号码邮箱二选一，没选的为空
	 * @param password 密码
	 * @param code 手机验证码，手机注册时有效
	 * @param userType 1-个人用户，2-机构用户
	 * @return
	 */
	public static JSONObject getDoRegisterParams(String mobile, String email,
			String password, String code, int userType,String mobileAreaCode) {
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("mobile", mobile);
			jObject.put("email", email);
			
			String encPwd = new String(Base64.encode(password.getBytes()));
			jObject.put("password", encPwd);
			jObject.put("code", code);
			jObject.put("userType", userType);
			jObject.put("mobileAreaCode", mobileAreaCode);
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}
	/**
	 * 第三方登录完善资料--获取用户注册参数对象
	 * @param mobile 手机号码
	 * @param email 邮箱，手机号码邮箱二选一，没选的为空
	 * @param password 密码
	 * @param code 手机验证码，手机注册时有效
	 * @param userType 1-个人用户，2-机构用户
	 * @param mToken 第三方QQ或微博的Token
	 * @param mNickName 昵称
	 * @param mLoginType QQ/微博
	 * @return
	 */
	public static JSONObject getDoRegisterParams(String mobile, String email,
			String password, String code, int userType,String mobileAreaCode,String mToken,String mNickName,int mLoginType) {
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("mobile", mobile);
			jObject.put("email", email);
			String encPwd = new String(Base64.encode(password.getBytes()));
			jObject.put("password", encPwd);
			jObject.put("code", code);
			jObject.put("userType", userType);
			jObject.put("mobileAreaCode", mobileAreaCode);
			jObject.put("access_token", mToken);
			jObject.put("mNickName", mNickName);
			jObject.put("login_type", mLoginType);
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}

	/**
	 * 获取发送验证码参数对象
	 * @param type 0-注册时获取验证码， 1-找回密码时获取验证码，3创建和编辑需求时获取验证码
	 * @param mobile 手机号码或者邮箱，如果是邮箱，后台直接发验证链接到邮箱，找回密码流程在手机端结束；如果是手机号码，发送验证码到手机端
	 * @return
	 */
	public static JSONObject getDoGetVerifyCodeParams(int type,String mobileAreaCode, String mobile) {
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("type", type);
			jObject.put("mobileAreaCode", mobileAreaCode);
			jObject.put("mobile", mobile);
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}

	/**
	 * 获取完善个人会员信息参数对象
	 * @param name 用户姓名
	 * @param jobTitle 职位
	 * @param company 公司
	 * @param mobile 手机号码
	 * @param email 邮箱，手机号码和邮箱，两个只有一个，之前注册的时候，没填写的， 这次填写完善
	 * @param image 用户头像，先将用户头像上传到服务器，得到一个图片地址，然后将地址放这里传给服务器端，可能为空
	 * @return
	 */
	public static JSONObject getDoFullPersonMemberInfoParams(String name,
			String jobTitle, String company, String mobile, String email,
			String image) {
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("name", name);
			jObject.put("jobTitle", jobTitle);
			jObject.put("company", company);
			jObject.put("mobile", mobile);
			jObject.put("email", email);
			jObject.put("image", image);
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}

	/**
	 * 获取完善机构会员联系人信息参数对象
	 * @param contactCardImage 机构联系人身份证图片链接
	 * @param mobile 手机号码，两个只有一个有内容，注册时没填那个有
	 * @param email 邮件地址
	 * @return
	 */
	public static JSONObject getDoFullContactInfoParams(String jtContactID,
			String contactCardImage, String mobile, String email) {
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("jtContactID", jtContactID);
			jObject.put("contactCardImage", contactCardImage);
			jObject.put("mobile", mobile);
			jObject.put("email", email);
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}

	/**
	 * 获取上传机构验证信息参数对象
	 * @param logoImage 机构logo图片地址，先上传图片， 拿到地址再提交
	 * @param fullName 机构全称
	 * @param shortName 机构简称
	 * @param OCCImage 组织机构代码证图片地址
	 * @param TCImage 营业执照图片地址
	 * @param legalPersonIDCardImage 法人身份证图片
	 * @return
	 */
	public static JSONObject getDoFullOrganizationAuthParams(String jtContactID,String logoImage,
			String fullName, String shortName, String occImage, String tcImage,
			String legalPersonIDCardImage) {
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("jtContactID", jtContactID);
			jObject.put("logoImage", logoImage);
			jObject.put("fullName", fullName);
			jObject.put("shortName", shortName);
			jObject.put("occImage", occImage);
			jObject.put("tcImage", tcImage);
			jObject.put("legalPersonIDCardImage", legalPersonIDCardImage);
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}
	
	/**
	 * 获取发送验证邮件的参数对象
	 * @param email
	 * @return
	 */
	public static JSONObject getDoSendValidateEmailParams(String email){
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("email", email);
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}
	/**
	 * 获取判断组织全称和组织邮箱是否被注册
	 * @param email
	 * @return
	 */
	public static JSONObject getDoJudgeUserandMail(String email,String orgName){
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("orgName", orgName);
			jObject.put("email", email);
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}

	/**
	 * 获取设置新密码参数对象
	 * @param mobile 	手机号
	 * @param vcode 	验证码，通过短信验证码找回时填写该值
	 * @param oldPassword	老密码，修改密码时填写该值
	 * @param newPassword	新密码
	 * @return
	 */
	public static JSONObject getDoSetNewPasswordParams(String mobile,String vcode,
			String oldPassword, String newPassword) {
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("mobile", mobile);
			jObject.put("vcode", vcode);
			
			String encPwd = new String(Base64.encode(oldPassword.getBytes()));
			jObject.put("oldPassword", encPwd);
			
			encPwd = new String(Base64.encode(newPassword.getBytes()));
			jObject.put("newPassword", encPwd);
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}
	
	/**
	 * 获取发布需求参数对象
	 * @param requirement 需求对象
	 * @return 
	 */
	public static JSONObject getDoAddRequirementParams(Requirement requirement) {

		JSONObject jObject = new JSONObject();
		try {
			jObject.put("requirement", requirement.toJSONObject());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}
	
	/**
	 * 获取需求详情参数对象
	 * @param id
	 * @return
	 */
	public static JSONObject getDoGetRequirentByIdParams(String id) {
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("id", id);
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}
	
	/**
	 * 获取编辑需求参数对象
	 * @param requirement 需求对象
	 * @return
	 */
	public static JSONObject getDoEditRequirementParams(Requirement requirement) {
		
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("requirement", requirement.toJSONObject());
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}
	
	/**
	 * 获取关闭需求参数对象
	 * @param id 需求id
	 * @return
	 */
	public static JSONObject getDoCloseRequirementParams(String id) {
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("requirementID", id);
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}
	
	/**
	 * 关注或取消关注需求
	 * @param requirementID
	 * @param isFocus
	 * @return
	 */
	public static JSONObject getDoFocusRequirementParams(String requirementID,boolean isFocus) {
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("requirementID", requirementID);
			jObject.put("isFocus", isFocus);
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}
	
	
	
	/**
	 * 获取添加好友参数对象
	 * @param userType 1-个人用户，2-机构用户
	 * @param id 对方的会员id，JTContactId或者OrganizationID
	 * @return
	 */
	public static JSONObject getDoAddFriendParams(int userType,String id) {
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("userType", userType);
			jObject.put("id", id);
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}
	
	/**
	 * 获取发表评论参数对象
	 * @param type
	 * @param id
	 * @param content
	 * @return
	 */
	public static JSONObject getDoAddCommentParams(int type,String id,String content) {
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("type", type);
			jObject.put("id", id);
			jObject.put("content", content);
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}
	
	/**
	 * 获取参数列表参数对象
	 * @param type
	 * @param id
	 * @param index
	 * @param size
	 * @return
	 */
	public static JSONObject getDoGetListCommentParams(int type,String id,int index,int size) {
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("type", type);
			jObject.put("id", id);
			jObject.put("index", index);
			jObject.put("size", size);
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}
	
	/**
	 * 获取参数列表参数对象
	 * @param projectType
	 * @param id
	 * @param index
	 * @param size
	 * @return
	 */
	public static JSONObject getDoGetDynamicListCommentParams(long id,int index,int size) {
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("newsId", id);
			jObject.put("size", 200);
			jObject.put("index", index);
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}
	
	
	/**
	 * 获取关闭业务需求参数对象
	 * @param businessRequirementID 业务需求id
	 * @return
	 */
	public static JSONObject getDoCloseBusinessRequirementParams(
			String id){
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("businessRequirementID", id);
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}
	
	/**
	 * 获取业务需求详情
	 * @param id
	 * @return
	 */
	public static JSONObject getDoGetBusinessRequirementDetailByIDParams(String id){
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("id", id);
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}
	
	
	/**
	 * 获取关闭任务参数对象
	 * @param taskID 任务id
	 * @return
	 */
	public static JSONObject getDoCloseTaskParams(int id){
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("taskID", id);
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}
	
	/**
	 * 获取任务详情
	 * @param id
	 * @return
	 */
	public static JSONObject getDoGetTaskDetailByIDParams(String id){
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("id", id);
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}
	
	
	/**
	 * 获取关闭项目参数对象
	 * @param projectID 项目id
	 * @return
	 */
	public static JSONObject getDoCloseProjectParams(int id){
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("projectID", id);
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}	
	
	/**
	 * 获取项目详情
	 * @param id
	 * @return
	 */
	public static JSONObject getDoGetProjectDetailByIDParams(String id){
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("id", id);
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}
	
	/**
	 * 获取知识详情参数对象
	 * @param id
	 * @return
	 */
	public static JSONObject getDoGetKnowledgeDetailByIDParams(String id){
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("id", id);
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}
	
	/**
	 * 获取知识详情参数对象
	 * @param id
	 * @return
	 * @deprecated
	 */
	public static JSONObject getDoGetKnowledgeDetailParams(String id){
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("id", id);
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}
	
	/**
	 * 获取匹配知识参数对象
	 * @param type
	 * @param id
	 * @return
	 */
	// "type":"1-需求；2-业务需求；3-任务；4-项目；5-用户",
    // "id":"需求id"
	public static JSONObject getDoGetMatchKnowledgeMiniParams(int type,int id){
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("type", type);
			jObject.put("id", id);
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}
	
	/**
	 * 获取匹配需求参数对象
	 * @param type
	 * @param id
	 */
	 // "type":"1-需求；2-业务需求；3-任务；4-项目；5-用户",
     // "id":"需求id"
	public static JSONObject getDoGetMatchRequirementMiniParams(int type,int id){
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("type", type);
			jObject.put("id", id);
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}
	
	/**
	 *  获取按关键字搜索需求列表参数对象
	 * @param keyword
	 * @param index
	 * @param size
	 */
	public static JSONObject getDoGetListRequirementParams(String keyword,int index,int size){
		
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("keyword", keyword);
			jObject.put("index", index);
			jObject.put("size", size);
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}
	
	/**
	 *  获取按关键字搜索事务列表参数对象
	 * @param keyword
	 * @param index
	 * @param size
	 */
	public static JSONObject getDoGetListAffairParams(int type,String keyword,int index,int size){
		
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("type", type);
			jObject.put("keyword", keyword);
			jObject.put("index", index);
			jObject.put("size", size);
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}
	
	/**
	 * 获取操作项目参数
	 * @param operateType
	 * @param id
	 * @return
	 */
	public static JSONObject getDoOperateProjectParams(int operateType, String id) {

		JSONObject jObject = new JSONObject();
		try {
			jObject.put("operateType", operateType);
			jObject.put("id", id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}
	
	/**
	 * 获取添加我的知识参数对象
	 * @param title
	 * @param url
	 * @return
	 */
	/*
	public static JSONObject getDoAddMyKnowledgeParams(String title, String url) {

		JSONObject jObject = new JSONObject();
		try {
			jObject.put("title", title);
			jObject.put("url", url);
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}
	*/
	
	/**
	 * 获取处理后的网页及其标题参数对象
	 * @param url
	 * @return
	 */
	public static JSONObject getTreatedHtmlParams(String url) {
		
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("url", url);
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}
	
	/**
	 * 删除附件
	 * @param id
	 * @return
	 */
	public static JSONObject getDoDeleteFileParams(String id) {
		
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("id", id);
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}
	/**
	 * 更新访问主页和评价的权限
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doUpdateUserConfig(Context context, IBindData bind,Handler handler,int type,int sign){
		String requestStr = "";
		try {
			JSONObject jObj = new JSONObject();
			jObj.put("type", type+"");
			jObj.put("sign", sign+"");
			requestStr = jObj.toString();
			String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.UpdateUserConfig;
			doExecute(context, bind, EAPIConsts.ReqType.UPDATE_USER_CONFIG, url,
					requestStr, handler);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取用户的二维码内容
	 * @param context
	 * @param bind
	 * @param userId 用户id
	 * @param handler
	 */
	public static void doGetUserQRUrl(Context context, IBindData bind,long userId,Handler handler){
		String requestStr = "";
		try {
			JSONObject jObj = new JSONObject();
			jObj.put("userId", userId);
			requestStr = jObj.toString();
			String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.UpdateUserConfig;
			doExecute(context, bind, EAPIConsts.ReqType.UpdateUserConfig, url,
					requestStr, handler);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
}
