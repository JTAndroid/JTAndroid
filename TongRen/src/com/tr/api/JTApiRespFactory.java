package com.tr.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tr.model.home.MainPageList;
import com.tr.ui.communities.model.Notification;
import com.utils.http.EAPIConsts;

/**
 * @author 作者姓名 E-mail: email地址
 * @version 创建时间：2014-4-11 上午9:54:49 类说明
 */
public class JTApiRespFactory {

	public static final String TAG = JTApiRespFactory.class.getSimpleName();

	public static Object createMsgObject(int msgId, String response) {

		// Log.d("xmx","respone:"+msgId+" str:"+response); 这里不要 打注释 原来工程里有 自己加
		// logfilters "keel"
		Log.v("TAG", "" + msgId);
		// 不同模块的api在这里分发
		try {
			JSONObject obj = new JSONObject(response);
			if ((msgId >= EAPIConsts.HomeReqType.HOME_REQ_DEL_APPORVE)
					&& (msgId <= EAPIConsts.HomeReqType.HOME_REQ_DEL_FLOW)) { // 首页相关
				return HomeRespFactory.createMsgObject(msgId, obj);
			}
			if (msgId == EAPIConsts.IMReqType.IM_REQ_FETCHFIRENDS) {
				return IMRespFactory.createMsgObject(msgId, obj);
			}
			if (msgId == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_COMMUNITY_STATE) {
				return ConferenceRespFactory.createMsgObject(msgId, obj);
			}
			if (msgId > EAPIConsts.CommunityReqType.Community_REQ_BASE
					&& msgId < EAPIConsts.CommunityReqType.Community_REQ_END) {
				// 社群请求处理
				return CommunityRespFactory.createMsgObject(msgId, response);
			}
			JSONObject respObj = obj.optJSONObject("responseData");
			if (respObj != null) {
				if ((msgId > EAPIConsts.IMReqType.IM_REQ_BASE)
						&& (msgId < EAPIConsts.IMReqType.IM_REQ_END)) { // IM部分api处理
					return IMRespFactory.createMsgObject(msgId, respObj);
				} else if ((msgId > EAPIConsts.ReqType.REQ_TYPE_BASE)
						&& (msgId < EAPIConsts.ReqType.REQ_TYPE_END)) { // 用户相关
					return UserRespFactory.createMsgObject(msgId, respObj);
				} else if ((msgId > EAPIConsts.HomeReqType.HOME_REQ_BASE)
						&& (msgId < EAPIConsts.HomeReqType.HOME_REQ_END)) { // 首页相关
					return HomeRespFactory.createMsgObject(msgId, respObj);
				} else if ((msgId > EAPIConsts.concReqType.conc_REQ_BASE)
						&& (msgId < EAPIConsts.concReqType.conc_REQ_END)) { // 人脉相关
					return ConnectionsRespFactory.createMsgObject(msgId,
							respObj);
				} else if ((msgId > EAPIConsts.ConferenceReqType.CONFERENCE_REQ_BASE)
						&& (msgId < EAPIConsts.ConferenceReqType.CONFERENCE_REQ_END)) { // 会议相关
					return ConferenceRespFactory
							.createMsgObject(msgId, respObj);
				} else if ((msgId > EAPIConsts.KnoReqType.ReqBase)
						&& (msgId < EAPIConsts.KnoReqType.ReqEnd)) { // 知识相关
					return KnowledgeRespFactory.createMsgObject(msgId, respObj);
				} else if ((msgId > EAPIConsts.CommonReqType.ReqBase)
						&& (msgId < EAPIConsts.CommonReqType.ReqEnd)) { // 通用
					return CommonRespFactory.createMsgObject(msgId, respObj);
				} else if ((msgId > EAPIConsts.demandReqType.demand_REQ_BASE)
						&& (msgId < EAPIConsts.demandReqType.demand_REQ_END)) { // 需求
					Log.e("Response", obj.toString());
					JSONObject errorJson = null;
					if (!obj.isNull("notification")) {
						errorJson = obj.optJSONObject("notification");
					}
					return DemandRespFactory.createMsgObject(msgId, respObj,
							errorJson);
				} else if ((msgId > EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_BASE)
						&& (msgId < EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_END)) {// 组织相关
					return OrganizationRespFactory.createMsgObject(msgId,
							respObj);
				} else if (msgId > EAPIConsts.PeopleRequestType.PEOPLE_REQ_BASE
						&& msgId < EAPIConsts.PeopleRequestType.PEOPLE_REQ_END) {// 人脉相关
					return PeopleRespFactory.doPeopleFromAPI(msgId, respObj);
				} else if (msgId > EAPIConsts.WorkReqType.ReqBase
						&& msgId < EAPIConsts.WorkReqType.ReqEnd) {
					return WorkRespFactory.doResponseFromAPI(msgId, respObj);
				} else if (msgId > EAPIConsts.CloudDiskType.CLOUD_DISK_REQ_BASE
						&& msgId < EAPIConsts.CloudDiskType.CLOUD_DISK_REQ_END) {// 文件管理
					return CloudDiskRespFactory.createMsgObject(msgId, respObj);
				} else if (msgId > EAPIConsts.TongRenRequestType.TONGREN_REQ_BASE
						&& msgId < EAPIConsts.TongRenRequestType.TONGREN_REQ_END) {// 桐人
					return TongRenRespFactory.doTongRenFromAPI(msgId, respObj);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
