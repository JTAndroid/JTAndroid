package com.tr.api;

import java.net.MalformedURLException;
import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.tr.model.work.BUAffar;
import com.tr.model.work.BUAffarList;
import com.tr.model.work.BUAffarLog;
import com.tr.model.work.BUAffarRelation;
import com.tr.model.work.BUResponseData;
import com.utils.http.EAPIConsts;

public class WorkRespFactory {
	public static Object doResponseFromAPI(int msgId, JSONObject jsonObject) throws MalformedURLException, JSONException, ParseException {

		Object vObject=null;
		if (jsonObject == null) {
			return null;
		}
		switch (msgId) {
			case EAPIConsts.WorkReqType.AFFAIR_LIST_GET:
			case EAPIConsts.WorkReqType.AFFAIR_LIST_GET_ALL:
				vObject=BUAffarList.genAfferByJson(jsonObject);
				break;
			case EAPIConsts.WorkReqType.AFFAIR_LOG_LIST_GET:
				vObject=BUAffarLog.genAffarListByJson(jsonObject);
				break;
				
			case EAPIConsts.WorkReqType.AFFAIR_LIST_MONTH_DATE_GET:
				vObject=BUAffarList.genAfferMonthDataByJson(jsonObject);
				break;
			case EAPIConsts.WorkReqType.AFFAIR_DETAIL_GET:
				vObject=BUAffar.genAffarDetailByJson(jsonObject);
				break;
			case EAPIConsts.WorkReqType.AFFAIR_CREATE:
				BUResponseData vResponseData=new BUResponseData();
				vResponseData.id=jsonObject.getLong("affairId");
				vResponseData.succeed=jsonObject.getBoolean("succeed");
				vObject=vResponseData;
				break;	
			case EAPIConsts.WorkReqType.AFFAIR_RELATION_GET:
				vObject=BUAffarRelation.genAffarRelationListByJson(jsonObject);
				break;
			case EAPIConsts.WorkReqType.AFFAIR_EDIT:
				BUResponseData vResponseDataEdit=new BUResponseData();
				vResponseDataEdit.id=jsonObject.getLong("affairId");
				vResponseDataEdit.succeed=jsonObject.getBoolean("succeed");
				vObject=vResponseDataEdit;
				break;
			case EAPIConsts.WorkReqType.AFFAIR_MODIFY_STATUS:
				BUResponseData vResponseDataModify=new BUResponseData();
				vResponseDataModify.id=jsonObject.getLong("affairId");
				vResponseDataModify.succeed=jsonObject.getBoolean("succeed");
				vObject=vResponseDataModify;
				break;
			case EAPIConsts.WorkReqType.AFFAIR_CHART:
				BUResponseData vResponseDataChar=new BUResponseData();
				vResponseDataChar.id=jsonObject.getLong("mucId");
				vResponseDataChar.succeed=jsonObject.getBoolean("succed");
				Log.d("xmx","respon:"+jsonObject.toString());
				vObject=vResponseDataChar;
				break;
			case EAPIConsts.WorkReqType.AFFAIR_ALL_MES_READED://事务消息全部已读
				//TODO 事务消息全部已读action 结果
			    String key="result";
			    Boolean bkey=jsonObject.getBoolean(key);
			    vObject=bkey;
				break;
		}
		return vObject;
	}
}
