package com.tr.api;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.tr.model.work.BUAffar;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

public class WorkReqUtil extends ReqBase {

	public static int WORK_PAGE_SIZE = 1000;

	// 获取事务列表
	public static void getAffarListByDate(Context context, IBindData bind,
			String inUserId, String inDate, String inType, Handler handler) {

		String requestStr = "";
		try {
			JSONObject jObject = new JSONObject();
			jObject.put("optType", inType);
			jObject.put("userId", inUserId);
			jObject.put("qdate", inDate);
			jObject.put("size", WORK_PAGE_SIZE);
			jObject.put("keyword", "");
			jObject.put("page", 1);
			requestStr = jObject.toString();
		} catch (JSONException e) {
			Log.d("xmx", e.getMessage());
		}
		String url = EAPIConsts.getWorkUrl() + EAPIConsts.AFFAIR_LIST_GET;
		Log.d("xmx", "send url:" + url);
		Log.d("xmx", "send body:" + requestStr);
		doExecute(context, bind, EAPIConsts.WorkReqType.AFFAIR_LIST_GET, url,
				requestStr, handler);
	}

	/**获取全部事务列表*/ 

	public static void getAffarListByAll(Context context, IBindData bind,
			String inUserId, String inDate, String inType, String inKey,
			Handler handler) {

		String requestStr = "";
		try {
			JSONObject jObject = new JSONObject();
			jObject.put("optType", inType);
			jObject.put("userId", inUserId);
			jObject.put("qdate", "");
			jObject.put("size", WORK_PAGE_SIZE);
			jObject.put("keyword", inKey);
			jObject.put("page", 1);
			requestStr = jObject.toString();
		} catch (JSONException e) {
			Log.d("xmx", e.getMessage());
		}
		String url = EAPIConsts.getWorkUrl() + EAPIConsts.AFFAIR_LIST_GET;
		Log.d("xmx", "send url:" + url);
		Log.d("xmx", "send body:" + requestStr);
		doExecute(context, bind, EAPIConsts.WorkReqType.AFFAIR_LIST_GET_ALL, url,
				requestStr, handler);
	}

	// 获取事务日志
	public static void getAffarLogById(Context context, IBindData bind,
			long inAffairId, Handler handler) {

		String requestStr = "";
		try {
			JSONObject jObject = new JSONObject();
			jObject.put("affairId", inAffairId);
			jObject.put("size", WORK_PAGE_SIZE);
			jObject.put("page", 1);
			requestStr = jObject.toString();
		} catch (JSONException e) {
			Log.d("xmx", e.getMessage());
		}
		String url = EAPIConsts.getWorkUrl() + EAPIConsts.AFFAIR_LOG_LIST_GET;
		Log.d("xmx", "send url:" + url);
		Log.d("xmx", "send body:" + requestStr);
		doExecute(context, bind, EAPIConsts.WorkReqType.AFFAIR_LOG_LIST_GET,
				url, requestStr, handler);
	}

	/**获取每月事务数量*/ 
	public static void getAffarMonthDateByDate(Context context, IBindData bind,
			String inUserId, String inDate, Handler handler) {

		String requestStr = "";
		try {
			JSONObject jObject = new JSONObject();
			jObject.put("month", inDate.substring(0, 6));
			jObject.put("userId", inUserId);
			requestStr = jObject.toString();
		} catch (JSONException e) {
			Log.d("xmx", e.getMessage());
		}
		String url = EAPIConsts.getWorkUrl()
				+ EAPIConsts.AFFAIR_LIST_MONTH_DATE_GET;
		Log.d("xmx", "send url:" + url);
		Log.d("xmx", "send body:" + requestStr);
		doExecute(context, bind,
				EAPIConsts.WorkReqType.AFFAIR_LIST_MONTH_DATE_GET, url,
				requestStr, handler);
	}

	// 获取事务明晰
	public static void getAffarDetail(Context context, IBindData bind,
			String inUserId, String inAffarID, Handler handler) {

		String requestStr = "";
		try {
			JSONObject jObject = new JSONObject();
			jObject.put("affairId", inAffarID);
			jObject.put("userId", inUserId);
			requestStr = jObject.toString();
		} catch (JSONException e) {
			Log.d("xmx", e.getMessage());
		}
		String url = EAPIConsts.getWorkUrl() + EAPIConsts.AFFAIR_DETAIL_GET;
		Log.d("xmx", "send url:" + url);
		Log.d("xmx", "send body:" + requestStr);
		doExecute(context, bind, EAPIConsts.WorkReqType.AFFAIR_DETAIL_GET, url,
				requestStr, handler);
	}

	// 创建事务
	public static void createAffar(Context context, IBindData bind,
			BUAffar inAffar, Handler handler) {

		String requestStr = "";
		JSONObject jObjectAffar = inAffar.getAffarJson();
		requestStr = jObjectAffar.toString();

		String url = EAPIConsts.getWorkUrl() + EAPIConsts.AFFAIR_CREATE;
		Log.d("xmx", "send url:" + url);
		Log.d("xmx", "send body:" + requestStr);
		doExecute(context, bind, EAPIConsts.WorkReqType.AFFAIR_CREATE, url,
				requestStr, handler);
	}

	// 获取事务关联
	public static void getAffarRelation(Context context, IBindData bind,
			long inAffarID, Handler handler) {

		String requestStr = "";
		try {
			JSONObject jObject = new JSONObject();
			jObject.put("affairId", inAffarID);
			requestStr = jObject.toString();
		} catch (JSONException e) {
			Log.d("xmx", e.getMessage());
		}
		String url = EAPIConsts.getWorkUrl() + EAPIConsts.AFFAIR_RELATION_GET;
		Log.d("xmx", "send url:" + url);
		Log.d("xmx", "send body:" + requestStr);
		doExecute(context, bind, EAPIConsts.WorkReqType.AFFAIR_RELATION_GET,
				url, requestStr, handler);
	}

	// 编辑事务
	public static void editAffar(Context context, IBindData bind,
			BUAffar inAffar,String inUserId, Handler handler) {

		String requestStr = "";
		
		
		
		try {
			JSONObject jObject = new JSONObject();
			JSONObject jObjectAffar = inAffar.getAffarJson();
			jObject.put("affairDetail", jObjectAffar);
			jObject.put("userId", inUserId);
			requestStr = jObject.toString();
		} catch (JSONException e) {
			Log.d("xmx", e.getMessage());
		}

		String url = EAPIConsts.getWorkUrl() + EAPIConsts.AFFAIR_EDIT;
		Log.d("xmx", "send url:" + url);
		Log.d("xmx", "send body:" + requestStr);
		doExecute(context, bind, EAPIConsts.WorkReqType.AFFAIR_EDIT, url,
				requestStr, handler);
	}
	
		/**修改状态*/ 
		public static void modifyAffarStatus(Context context, IBindData bind,
				String inAffarId,String inUserId,String inType, Handler handler) {

			String requestStr = "";
			
			try {
				JSONObject jObject = new JSONObject();
				jObject.put("affairId", inAffarId);
				jObject.put("userId", inUserId);
				jObject.put("type", inType);
				requestStr = jObject.toString();
			} catch (JSONException e) {
				Log.d("xmx", e.getMessage());
			}
			
			String url = EAPIConsts.getWorkUrl() + EAPIConsts.AFFAIR_MODIFY_STATUS;
			Log.d("xmx", "send url:" + url);
			Log.d("xmx", "send body:" + requestStr);
			doExecute(context, bind, EAPIConsts.WorkReqType.AFFAIR_MODIFY_STATUS, url,
					requestStr, handler);
		}	
	
		
		/**获取聊天id*/ 
		public static void getCharId(Context context, IBindData bind,
						BUAffar inAffar, long inUserId,Handler handler) {

					String requestStr = "";
					JSONObject jObject=inAffar.getCharJson(inUserId);
					requestStr = jObject.toString();
					String url = EAPIConsts.getIMUrl() + EAPIConsts.AFFAIR_CHART;
					Log.d("xmx", "send url:" + url);
					Log.d("xmx", "send body:" + requestStr);
					doExecute(context, bind, EAPIConsts.WorkReqType.AFFAIR_CHART, url,
							requestStr, handler);
				}	
			
		
}
