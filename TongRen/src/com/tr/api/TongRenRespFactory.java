package com.tr.api;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tr.model.knowledge.UserCategory;
import com.tr.ui.people.model.PeopleDetails;
import com.tr.ui.tongren.model.message.TongRenMessage;
import com.tr.ui.tongren.model.project.Apply;
import com.tr.ui.tongren.model.project.Operation;
import com.tr.ui.tongren.model.project.OrganizationDetail;
import com.tr.ui.tongren.model.project.OrganizationRole;
import com.tr.ui.tongren.model.project.OrganizationRoleInfo;
import com.tr.ui.tongren.model.project.Organization;
import com.tr.ui.tongren.model.project.OrganizationMember;
import com.tr.ui.tongren.model.project.OrganizationSumup;
import com.tr.ui.tongren.model.project.Project;
import com.tr.ui.tongren.model.project.ProjectApply;
import com.tr.ui.tongren.model.project.Publish;
import com.tr.ui.tongren.model.project.RecommendPagePublish;
import com.tr.ui.tongren.model.project.Resource;
import com.tr.ui.tongren.model.project.Undertaken;
import com.tr.ui.tongren.model.task.AssignUserInfo;
import com.tr.ui.tongren.model.task.TaskVO;
import com.tr.ui.tongren.model.record.RecordDetail;
import com.tr.ui.tongren.model.record.RecordRule;
import com.tr.ui.tongren.model.record.Records;
import com.tr.ui.tongren.model.review.ApplyList;
import com.tr.ui.tongren.model.review.Process;
import com.tr.ui.tongren.model.task.Task;
import com.utils.http.EAPIConsts;
import com.utils.http.EAPIConsts.PeopleRequestType;
import com.utils.http.EAPIConsts.TongRenRequestType;
import com.utils.http.EAPIConsts.TongRenRequestUrl;

public class TongRenRespFactory {
	private static Gson gson;
	private static String strKey;
	private static String jsonStr;

	public static Object doTongRenFromAPI(int msgId, JSONObject jsonObject)
			throws JSONException {
		gson = new Gson();
		strKey = "";
		if (jsonObject == null) {
			return null;
		}
		Object obj = null;
		switch (msgId) {
		case TongRenRequestType.TONGREN_REQ_CREATE_PROJECT: // 创建项目
			obj = gson.fromJson(jsonObject.toString(), Project.class);
			break;
		case TongRenRequestType.TONGREN_REQ_GETMYCREATEALL_PROJECTS: //获取我创建的项目列表
			
			strKey = "projects";
			if (!jsonObject.isNull(strKey)) {
				String jsonString = jsonObject.getJSONArray(strKey).toString();
				List<Project> listProject = gson.fromJson(jsonString,
						new TypeToken<List<Project>>() {
						}.getType());
				obj = listProject;
			}
			break;
		case TongRenRequestType.TONGREN_REQ_GETMYCREATEALL_UNDERTAKEN://获取我承接的项目列表
			strKey = "list";
			if (!jsonObject.isNull(strKey)) {
				String jsonString = jsonObject.getJSONArray(strKey).toString();
				List<Undertaken> listProject = gson.fromJson(jsonString,
						new TypeToken<List<Undertaken>>() {
						}.getType());
				obj = listProject;
			}
			break;
		case TongRenRequestType.TONGREN_REQ_GETALLPUBLISHVALIDITY: //我发布的项目、已承接、未承接接口 
			strKey = "publishs";
			if (!jsonObject.isNull(strKey)) {
				String jsonString = jsonObject.getJSONArray(strKey).toString();
				List<Publish> listPublish = gson.fromJson(jsonString,
						new TypeToken<List<Publish>>() {
						}.getType());
				obj = listPublish;
			}
			break;
		case TongRenRequestType.TONGREN_REQ_GETPAGEPUBLISHVALIDITY: // 未过期的项目列表接口 
			strKey = "pagePublish";
			if (!jsonObject.isNull(strKey)) {
				String jsonString = jsonObject.getJSONObject(strKey).toString();
				obj = gson.fromJson(jsonString, RecommendPagePublish.class);
			}
			
			break;
		case TongRenRequestType.TONGREN_REQ_GETPROJECTDETAIL: //获取我创建的项目详情
			
			strKey = "projectVO";
			if (!jsonObject.isNull(strKey)) {
				String jsonString = jsonObject.getJSONObject(strKey).toString();
				obj = gson.fromJson(jsonString, Project.class);
			}
			
			break;
		case TongRenRequestType.TONGREN_REQ_GETUNDERTAKENPROJECTINFO: //获取我承接的项目详情
			
			strKey = "project";
			if (!jsonObject.isNull(strKey)) {
				String jsonString = jsonObject.getJSONObject(strKey).toString();
				obj = gson.fromJson(jsonString, Project.class);
			}
			
			break;
		case TongRenRequestType.TONGREN_REQ_GETRESOURCEPROJECT: //获得项目中的文档资源 
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETORGRESOURCE: //组织管理下的组织资源列表
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMYORGRESOURCE:// 获得我的资源列表
			strKey = "resourceList";
			if (!jsonObject.isNull(strKey)) {
				String jsonString = jsonObject.getJSONArray(strKey).toString();
				List<Resource> listResource = gson.fromJson(jsonString,
						new TypeToken<List<Resource>>() {
						}.getType());
				obj = listResource;
			}
			break;
		case TongRenRequestType.TONGREN_REQ_APPLY://申请项目接口
			strKey = "apply";
			if (!jsonObject.isNull(strKey)) {
				String jsonString = jsonObject.getJSONObject(strKey).toString();
				obj = gson.fromJson(jsonString, Apply.class);
			}
			break;
			
		case TongRenRequestType.TONGREN_REQ_GETPROJECTOPERATION://查看项目任务操作记录
			strKey = "result";
			if (!jsonObject.isNull(strKey)) {
				String jsonString = jsonObject.getJSONArray(strKey).toString();
				List<Operation> listOperation = gson.fromJson(jsonString,
						new TypeToken<List<Operation>>() {
						}.getType());
				obj = listOperation;
			}
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETPRIMARYTASK: //获取项目主任务
			strKey = "task";
			if (!jsonObject.isNull(strKey)) {
				String jsonString = jsonObject.getJSONObject(strKey).toString();
				obj = gson.fromJson(jsonString, Task.class);
			}
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMYTASKLIST:// 我的任务列表
			strKey = "tasklist";
			if (!jsonObject.isNull(strKey)) {
				String jsonString = jsonObject.getJSONArray(strKey).toString();
				List<TaskVO> listTaskVO = gson.fromJson(jsonString,
						new TypeToken<List<TaskVO>>() {
						}.getType());
				obj = listTaskVO;
			}
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_ORGANIZATIONTASKCREATE://创建组织任务接口
			strKey = "id";
			if (!jsonObject.isNull(strKey)) {
				String jsonString = jsonObject.optString(strKey).toString();
				obj = jsonString;
			}
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETORGTASKLIST:// 组织任务列表
			strKey = "tasks";
			if (!jsonObject.isNull(strKey)) {
				String jsonString = jsonObject.getJSONArray(strKey).toString();
				List<Task> listTask = gson.fromJson(jsonString,
						new TypeToken<List<Task>>() {
						}.getType());
				obj = listTask;
			}
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_ADDSUBTASK: //增加子任务
			strKey = "code";
			if (!jsonObject.isNull(strKey)) {
				String jsonString = jsonObject.optString(strKey).toString();
				obj = jsonString;
			}
			break;
		case  EAPIConsts.TongRenRequestType.TONGREN_REQ_UPDATESUBTASK://修改子任务
			strKey = "code";
			if (!jsonObject.isNull(strKey)) {
				String jsonString = jsonObject.optString(strKey).toString();
				obj = jsonString;
			}
			break;
		case  EAPIConsts.TongRenRequestType.TONGREN_REQ_ASSIGNORGANIZATIONTASK://重新分配组织任务接口 
			strKey = "id";
			if (!jsonObject.isNull(strKey)) {
				String jsonString = jsonObject.optString(strKey).toString();
				obj = jsonString;
			}
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_ASSIGN:// 分配子任务
			strKey = "code";
			if (!jsonObject.isNull(strKey)) {
				String jsonString = jsonObject.optString(strKey).toString();
				obj = jsonString;
			}
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETORGANIZATIONMEMBERINFO:// 查看正式组织成员列表
			strKey = "result";
			if (!jsonObject.isNull(strKey)) {
				String jsonString = jsonObject.getJSONArray(strKey).toString();
				List<OrganizationRoleInfo> listOrganizationRoleInfo = gson.fromJson(jsonString,
						new TypeToken<List<OrganizationRoleInfo>>() {
						}.getType());
				obj = listOrganizationRoleInfo;
			}
				break;
		case TongRenRequestType.TONGREN_REQ_GETMYCREATEORGANIZATIONS://创建的组织
		case TongRenRequestType.TONGREN_REQ_GETMYJOINORGANIZATION://参与的组织
			strKey = "success";
			if (!jsonObject.isNull(strKey)) {
				String jsonString = jsonObject.getJSONArray(strKey).toString();
				List<Organization> listOrganization = gson.fromJson(jsonString,
						new TypeToken<List<Organization>>() {
						}.getType());
				obj = listOrganization;
			}
			break;
		case TongRenRequestType.TONGREN_REQ_GETORGANIZATIONBYID://组织详情
			if(jsonObject!=null){
				obj = gson.fromJson(jsonObject.toString(), OrganizationDetail.class);
			}
			break;
		case TongRenRequestType.TONGREN_REQ_EXIT://退出组织
		case TongRenRequestType.TONGREN_REQ_DISBAND://解散组织
			obj = false;
			if(jsonObject.length()==0){
				obj = true;
			}
			break;
		case TongRenRequestType.TONGREN_REQ_GETORGANIZATIONSUMUP:
			if(jsonObject!=null){
				obj = gson.fromJson(jsonObject.toString(), OrganizationSumup.class);
			}
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMYPROJECTAPPLY:// 根据项目Id查询项目申请列表
			strKey = "applies";
			if (!jsonObject.isNull(strKey)) {
				String jsonString = jsonObject.getJSONArray(strKey).toString();
				List<ProjectApply> listProjectApply = gson.fromJson(jsonString,
						new TypeToken<List<ProjectApply>>() {
						}.getType());
				obj = listProjectApply;
 			}
 			break;
		case TongRenRequestType.TONGREN_REQ_ADDBEGIN:
		case TongRenRequestType.TONGREN_REQ_ADDEND:
			strKey = "record";
			if (!jsonObject.isNull(strKey)) {
				String jsonString = jsonObject.getJSONObject(strKey).toString();
				obj = gson.fromJson(jsonString, RecordDetail.class);
			}
			break;
		case TongRenRequestType.TONGREN_REQ_GETMONTHINFO:
			strKey = "records";
			if (!jsonObject.isNull(strKey)) {
				String jsonString = jsonObject.getJSONArray(strKey).getJSONObject(0).toString();
				obj = gson.fromJson(jsonString, Records.class);
			}
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_SELECTASSIGNTASKBYTASKID://查看任务分配人
			strKey = "userList";
			if (!jsonObject.isNull(strKey)) {
				String jsonString = jsonObject.getJSONArray(strKey).toString();
				List<AssignUserInfo> listAssignUserInfo = gson.fromJson(jsonString,
						new TypeToken<List<AssignUserInfo>>() {
						}.getType());
				obj = listAssignUserInfo;
			}
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_COMPLETETASK://完成任务
			strKey = "code";
			if (!jsonObject.isNull(strKey)) {
				String jsonString = jsonObject.optString(strKey).toString();
				obj = jsonString;
			}
			break;
		case 	EAPIConsts.TongRenRequestType.TONGREN_REQ_DELPROJECT://删除项目发布
			strKey = "status";
			if (!jsonObject.isNull(strKey)) {
				boolean jsonString = jsonObject.optBoolean(strKey);
				obj = jsonString;
			}
		break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_UNDERTAKEPROJECT:// 确认合作承接项目
			strKey = "undertaken";
			if (!jsonObject.isNull(strKey)) {
				String jsonString = jsonObject.getJSONObject(strKey).toString();
				obj = gson.fromJson(jsonString, Undertaken.class);
			}
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETATTENDANCERECORDSOFDATE:// 查看用户某天的打卡信息
			strKey = "records";
			if (!jsonObject.isNull(strKey)) {
				String jsonString = jsonObject.getJSONObject(strKey).toString();
				obj = gson.fromJson(jsonString, RecordDetail.class);
			}
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETRULE://获取打卡规则
			strKey = "success";
			if (!jsonObject.isNull(strKey)) {
				String jsonString = jsonObject.getJSONObject(strKey).toString();
				obj = gson.fromJson(jsonString, RecordRule.class);
			}
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETREVIEWAPPLICATIONLIST://我的申批列表
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMYAPPLYFOR://我的申请列表
			strKey = "success";
			if (!jsonObject.isNull(strKey)) {
				String jsonString = jsonObject.getJSONObject(strKey).toString();
				obj = gson.fromJson(jsonString, ApplyList.class);
			}
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_CREATEAPPLICATION://新建审批 	
			strKey = "result";
			if (!jsonObject.isNull(strKey)) {
				String jsonString = jsonObject.getJSONObject(strKey).toString();
				obj = gson.fromJson(jsonString, com.tr.ui.tongren.model.review.Apply.class);
			}
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETPROCESSBYORGID://查询组织下的审批流程集合 
			if(jsonObject!=null){
				String jsonString = jsonObject.toString().substring(jsonObject.toString().indexOf(":")+1,jsonObject.toString().length()-1);
				List<Process> listProcess = gson.fromJson(jsonString,
						new TypeToken<List<Process>>() {
						}.getType());
				obj = listProcess;
			}
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMYROLE://获取我在组织中的角色 
			strKey = "organizationRole";
			if (!jsonObject.isNull(strKey)) {
				String jsonString = jsonObject.getJSONArray(strKey).toString();
				List<OrganizationRole> listOrganizationRole = gson.fromJson(jsonString,
						new TypeToken<List<OrganizationRole>>() {
						}.getType());
				obj = listOrganizationRole;
			}
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_SIGNOFF://我的审批（同意、驳回）
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_RECALLRECORDS://撤销申请
			strKey = "result";
			if (!jsonObject.isNull(strKey)) {
				obj = jsonObject.opt(strKey);
			}
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_USERMESSAGE://首页消息列表 
			strKey = "messageList";
			if (!jsonObject.isNull(strKey)) {
				String jsonString = jsonObject.getJSONArray(strKey).toString();
				List<TongRenMessage> listTongRenMessage = gson.fromJson(jsonString,
						new TypeToken<List<TongRenMessage>>() {
						}.getType());
				obj = listTongRenMessage;
			}
			break;
		}
		return obj;
		}
}
