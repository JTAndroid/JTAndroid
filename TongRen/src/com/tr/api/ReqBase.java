package com.tr.api; 

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.utils.http.EAPITask;
import com.utils.http.IBindData;

/**
 * 网络请求入口类
 * @author xuxinjian/leon
 */
public class ReqBase {
	
	/**
	 * 网络请求通用接口
	 * @param context
	 * @param bind
	 * @param type
	 * @param url
	 * @param request
	 */
	public static void doExecute(Context context, IBindData bind, int type,
			String url, String request) {
		EAPITask.doExecute(context, bind, type, url, request, null);
	}
	
	/**
	 * 
	 * 网络请求通用接口
	 * 
	 * @param context  数据上下文
	 * @param bind	返回接口
	 * @param type	联网类型
	 * @param url	请求地址
	 * @param request	请求JSON串
	 * @param handler	错误处理hander
	 */
	public static void doExecute(Context context, IBindData bind, int type,
			String url, String request, Handler handler) {
		if(handler == null){
			handler =  new HandlerAPI();
		}
		EAPITask.doExecute(context, bind, type, url, request, handler);
	}
}
