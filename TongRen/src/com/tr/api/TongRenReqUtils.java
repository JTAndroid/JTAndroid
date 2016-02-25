package com.tr.api;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.http.EAPIConsts.TongRenRequestUrl;

public class TongRenReqUtils extends ReqBase {
	/**
	 * 桐人请求接口
	 * 
	 * @param context
	 * @param bind
	 * @param obj
	 *            请求参数
	 * @param handler
	 * @param TongRenReqType
	 *            桐人请求类型
	 */
	public static void doRequestWebAPI(Context context, IBindData bind,
			Object obj, Handler handler, int TongRenReqType) {

		String requestParam = "";
		Gson gson = new Gson();
		requestParam = gson.toJson(obj);
		String url = EAPIConsts.TONGREN_URL;
		switch (TongRenReqType) {
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_CREATE_PROJECT: // 创建项目
			url += TongRenRequestUrl.CREATE_PROJECT;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMYCREATEALL_PROJECTS: // 获取我创建的项目列表
			url += TongRenRequestUrl.GETMYCREATEALL_PROJECTS;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_USERMESSAGE: // 首页消息列表
			url += TongRenRequestUrl.GET_USERMESSAGE;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMYCREATEALL_UNDERTAKEN: // 我的承接项目列表
			url += TongRenRequestUrl.GETMYCREATEALL_UNDERTAKEN;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETALLPUBLISHVALIDITY: // 我发布的项目、已承接、未承接接口
			url += TongRenRequestUrl.GETALLPUBLISHVALIDITY;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETPAGEPUBLISHVALIDITY: // 未过期的项目列表接口
			url += TongRenRequestUrl.GETPAGEPUBLISHVALIDITY;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETPROJECTDETAIL: // 未过期的项目列表接口
			url += TongRenRequestUrl.GETPROJECTDETAIL;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETRESOURCEPROJECT:// 获得项目中的文档资源
			url += TongRenRequestUrl.GETRESOURCEPROJECT;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMYCREATEORGANIZATIONS:// 获取我创建的组织
			url += TongRenRequestUrl.GETMYCREATEORGANIZATIONS;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMYJOINORGANIZATION:// 获取我参与的组织
			url += TongRenRequestUrl.GETMYJOINORGANIZATION;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETORGANIZATIONBYID:// 获取组织详情
			url += TongRenRequestUrl.GETORGANIZATIONBYID;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETUNDERTAKENPROJECTINFO:// 查看承接的项目详情接口
			url+=TongRenRequestUrl.GETUNDERTAKENPROJECTINFO;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_APPLY:// 申请项目接口
			url+=TongRenRequestUrl.PROJECT_APPLY;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETPROJECTOPERATION:// 查看项目任务操作记录 
			url+=TongRenRequestUrl.GETPROJECTOPERATION;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETPRIMARYTASK:// 获取项目主任务
			url+=TongRenRequestUrl.GETPRIMARYTASK;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_ADDSUBTASK:// 增加子任务
			url+=TongRenRequestUrl.ADDSUBTASK;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_UPDATESUBTASK:// 修改子任务
			url+=TongRenRequestUrl.UPDATESUBTASK;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_ASSIGN:// 分配子任务
			url+=TongRenRequestUrl.ASSIGN;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETORGANIZATIONMEMBERINFO:// 查看正式组织成员列表
			url+=TongRenRequestUrl.GETORGANIZATIONMEMBERINFO;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMYPROJECTAPPLY:// 根据项目Id查询项目申请列表
			url+=TongRenRequestUrl.GETMYPROJECTAPPLY;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_SELECTASSIGNTASKBYTASKID:// 查看任务分配人 /assignTask/selectAssignTaskByTaskId.json¶
			url+=TongRenRequestUrl.SELECTASSIGNTASKBYTASKID;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_COMPLETETASK:// 完成任务
			url+=TongRenRequestUrl.COMPLETETASK;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_DELPROJECT:// 删除项目发布
			url+=TongRenRequestUrl.DELPROJECT;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_PROJECTOPERATION:// 放弃项目和结束项目
			url+=TongRenRequestUrl.PROJECTOPERATION;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_REMOVETASK:// 删除任务
			url+=TongRenRequestUrl.REMOVETASK;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_UNDERTAKEPROJECT:// 确认合作承接项目
			url+=TongRenRequestUrl.UNDERTAKEPROJECT;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMYORGRESOURCE:// 获得我的资源列表
			url+=TongRenRequestUrl.GETMYORGRESOURCE;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETORGRESOURCE:// 组织管理下的组织资源列表
			url+=TongRenRequestUrl.GETORGRESOURCE;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMYTASKLIST:// 我的任务列表
			url+=TongRenRequestUrl.GETMYTASKLIST;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETORGTASKLIST:// 组织任务列表
			url+=TongRenRequestUrl.GETORGTASKLIST;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_ORGANIZATIONTASKCREATE:// 创建组织任务接口
			url+=TongRenRequestUrl.ORGANIZATIONTASKCREATE;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_ORGANIZATIONTASKDELETE:// 删除组织任务接口
			url+=TongRenRequestUrl.ORGANIZATIONTASKDELETE;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMYROLE:// 获取我在组织中的角色
			url+=TongRenRequestUrl.GETMYROLE;
			break;
		case  EAPIConsts.TongRenRequestType.TONGREN_REQ_ASSIGNORGANIZATIONTASK://重新分配组织任务接口 
			url+=TongRenRequestUrl.ASSIGNORGANIZATIONTASK;
			break;
		case  EAPIConsts.TongRenRequestType.TONGREN_REQ_PROCESSING://(首页消息操作同意、忽略)申请类，退出申请，邀请类 
			url+=TongRenRequestUrl.PROCESSING;
			break;
		case  EAPIConsts.TongRenRequestType.TONGREN_REQ_DELMESSAGE://删除消息操作 
			url+=TongRenRequestUrl.DELMESSAGE;
			break;
		default:
			break;
		}
		doExecute(context, bind, TongRenReqType, url, requestParam, handler);
	}

	public static void doRequestOrg(Context context, IBindData bind,
			JSONObject requestParam, Handler handler, int TongRenReqType) {
		String url = EAPIConsts.TONGREN_URL;
		switch (TongRenReqType) {
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMYCREATEORGANIZATIONS:// 获取我创建的组织
			url += TongRenRequestUrl.GETMYCREATEORGANIZATIONS;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMYJOINORGANIZATION:// 获取我参与的组织
			url += TongRenRequestUrl.GETMYJOINORGANIZATION;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETORGANIZATIONBYID:// 获取组织详情
			url += TongRenRequestUrl.GETORGANIZATIONBYID;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_EXIT:// 退出组织
			url += TongRenRequestUrl.EXIT;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_DISBAND:// 解散组织
			url += TongRenRequestUrl.DISBAND;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETORGANIZATIONSUMUP:// 组织概况
			url += TongRenRequestUrl.GETORGANIZATIONSUMUP;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETORGANIZATIONMEMBERINFO:// 组织成员
			url += TongRenRequestUrl.GETORGANIZATIONMEMBERINFO;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_ADDBEGIN:// 上班打卡
			url += TongRenRequestUrl.ADDBEGIN;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_ADDEND://下班打卡
			url += TongRenRequestUrl.ADDEND;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMONTHINFO:// 月度考核信息
			url += TongRenRequestUrl.GETMONTHINFO;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETATTENDANCERECORDSOFDATE:// 查看用户某天打卡信息
			url += TongRenRequestUrl.GETATTENDANCERECORDSOFDATE;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETRULE:// 获取考勤规则
			url += TongRenRequestUrl.GETRULE;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETREVIEWAPPLICATIONLIST:// 获取审批列表
			url += TongRenRequestUrl.GETREVIEWAPPLICATIONLIST;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMYAPPLYFOR:// 获取审批列表
			url += TongRenRequestUrl.GETMYAPPLYFOR;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_CREATEAPPLICATION:// 获取审批列表
			url += TongRenRequestUrl.CREATEAPPLICATION;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETPROCESSBYORGID:// 获取审批流程列表
			url += TongRenRequestUrl.GETPROCESSBYORGID;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_RECALLRECORDS://撤销申请
			url += TongRenRequestUrl.RECALLRECORDS;
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_SIGNOFF://我的审批（同意、驳回）
			url += TongRenRequestUrl.SIGNOFF;
			break;
		default:
			break;
		}
		doExecute(context, bind, TongRenReqType, url, requestParam.toString(),
				handler);
	}
}
