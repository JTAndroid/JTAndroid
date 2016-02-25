package com.utils.http;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.tr.App;
import com.tr.db.ChatRecordDBManager;
import com.tr.model.im.MGetChatMessage;
import com.tr.model.im.MGetMUCMessage;
import com.tr.model.im.MGetMessage;
import com.tr.model.im.MSendMessage;
import com.tr.model.obj.IMBaseMessage;
import com.utils.common.ApolloUtils;
import com.utils.log.KeelLog;

/**
 * @Filename EAPITask.java
 * @Author xuxinjian/leon
 * @Date 2014-3-13
 * @description 后台API的访问接口方法，所有获取后台api数据都从该类访问
 */ 

public class EAPITask extends AsyncTask<Object, Integer, Object> {

	private final static String TAG = EAPITask.class.getSimpleName();
	
	private Context context;
	private int tag = -1;
	private IBindData bindData = null;
	
	public EAPITask(IBindData bindData) {
		this.bindData = bindData;
	}
	
	@Override
	protected Object doInBackground(Object... params) {
		
		// 上下文环境变量
		if (params[0] instanceof Context) {
			context = (Context) params[0];
		}
		else{
			return null;
		}
		// 回调接口对象
		if (params[1] instanceof IBindData) {
			bindData = (IBindData) params[1];
		}
		else{
			return null;
		}
		// 消息唯一标识
		if(params[2] instanceof Integer){
			tag = (Integer) params[2];
		}
		// 接口完整地址
		String url = null;
		if (params[3] instanceof String) {
			url = (String) params[3];
		}
		// 请求参数
		String request = null;
		if (params[4] instanceof String) {
			request = (String) params[4];
		}
		// 消息处理器
		Handler handler = null;
		if(params[5] instanceof Handler){
			handler = (Handler) params[5];
		}
		KeelLog.d(TAG, "task:" + tag);
		// 其它常规命令
		EAPIDataMode mode = new EAPIDataMode(context);
		Object resultObj = mode.getCommonObject(tag, url, request, handler);
		
		if(tag == EAPIConsts.IMReqType.IM_REQ_SEND_MESSAGE
				|| tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_SEND_MEETING_CHAT){  // 发送消息接口的特殊处理（私聊、群聊和会议）
			MSendMessage sm = (MSendMessage) resultObj;
			if(sm == null){
				sm = new MSendMessage();
				sm.setSucceed(false);
				resultObj = sm;
			}
			if(!sm.isSucceed()){ // 消息如果失败，需要将messageId存入对象中，供上层处理
				try{
					JSONObject jsonObj = new JSONObject(request);
					String messageID = jsonObj.getString("messageID");
					sm.setMessageID(messageID);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		else if(tag == EAPIConsts.IMReqType.IM_REQ_GET_CHAT_MESSAGE
				|| tag == EAPIConsts.IMReqType.IM_REQ_GET_MUC_MESSAGE){ // 获取聊天记录接口返回
			MGetMessage gm = (MGetMessage) resultObj;
			if(gm == null){
				if(tag == EAPIConsts.IMReqType.IM_REQ_GET_CHAT_MESSAGE){
					gm = new MGetChatMessage();
				}
				else if(tag == EAPIConsts.IMReqType.IM_REQ_GET_MUC_MESSAGE){
					gm = new MGetMUCMessage();
				}
				resultObj = gm;
			}
			try {
				gm.setBackward(new JSONObject(request).optBoolean("isBackward"));
			} 
			catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return resultObj;
	}

	@Override
	protected void onPostExecute(Object result) {
		KeelLog.d(TAG, "post:" + bindData + " tag:" + tag);
		if (bindData != null) {
			bindData.bindData(tag, result); // 将数据发送给注册的回调函数
		}
	}
	
	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}

	public static void doExecute(Context context, IBindData bind, int type,
			String url, String request) {
		doExecute(context, bind, type, url, request, null);
	}

	public static void doExecute(Context context, IBindData bind, int type,
			String url, String request, Handler handler) {
		ApolloUtils.execute(false, new EAPITask(bind), context, bind, type,
				url, request, handler);
	}
}
