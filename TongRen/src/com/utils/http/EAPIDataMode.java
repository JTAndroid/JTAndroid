package com.utils.http;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.baidu.android.pushservice.PushManager;
import com.tr.App;
import com.tr.api.IMRespFactory;
import com.tr.api.JTApiRespFactory;
import com.tr.db.AppDataDBManager;
import com.tr.ui.user.modified.LoginActivity;
import com.utils.common.EConsts;
import com.utils.common.GlobalVariable;
import com.utils.file.FileHelper;
import com.utils.log.KeelLog;
import com.utils.log.LogWriter;
import com.utils.log.ToastUtil;

/**
 * @Filename 联网数据结果处理
 * @Author xuxinjian
 * @Date 2014-3-13
 * @description 后台接口访问数据类
 */

public class EAPIDataMode {
	private EHttpAgent httpAgent;
	private Context mContext;
	private String  mRequest;
    
	public EAPIDataMode(Context context) {
		this.httpAgent = new EHttpAgent(context);
		mContext = context;
	}

	// 获取数据类
	public Object getCommonObject(int msgID, String url, String request, Handler handler){
		KeelLog.v("msgID:" + msgID + "==url==" + url);
		String[] responses = httpAgent.getPostNetMessage(url, request, msgID);
		
		mRequest = request;
    	KeelLog.v("request:" + request + "  respose:"+responses[1]);
    	
    	if(EAPIConsts.isLog2Sdcard){
    		log2SDcardByGushi(url, request, responses);
    	}
    	
    	if (comparisonNetworkStatus(responses, handler)){ //数据是否有错
    		return createMsgObject(msgID, responses[1]);
        }
        KeelLog.d(" error .");
        showErrorMessage(handler, responses);
        KeelLog.e(GlobalVariable.PRINT_NETWORK_STATUS, responses.toString());
        return null;
	}

	/**
	 * 把log写到sdcard 上     只写入最后一次写入的数据
	 * @param url	访问地址
	 * @param request	请求String
	 * @param responses	响应数据
	 * @author gushi
	 */
	private void log2SDcardByGushi(String url, String request, String[] responses) {
		// log2sdcard by gushi 
		FileHelper.WriteStringToFile("==url==" + url + " request: " + request + "  respose:"+responses[1] + "\r\n", Environment.getExternalStorageDirectory().getAbsolutePath()+"/Keel/keel_log.txt", true);
		File logf = new File(Environment.getExternalStorageDirectory() + File.separator + "GushiLog.txt");
		try {
			LogWriter mLogWriter = LogWriter.open(logf.getAbsolutePath());
			mLogWriter.print(EAPIDataMode.class, "==url==" + url + " request: " + request + "  respose:"+responses[1] + "\r\n");
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// 返回json数据解析统一到JTApiFactory里面处理
	private Object createMsgObject(int msgId, String response){
		return JTApiRespFactory.createMsgObject(msgId, response);
	}
      
    /**
     * 显示errMessage
     * @param errMessage
     */
    public void showErrorMessage(Handler handler, String[]  err) {
        showErrorMessage(handler, err, "");
    }
    public void showErrorMessage(Handler handler, String[]  err, String loginString) {
        String errCode = err[0];
        String errMessage = err[1];
        
        
        if (KeelLog.DEBUG) {
            KeelLog.e("errCode," + errCode + ",errMessage :" + errMessage);
        }

        if((errCode.equals(EAPIConsts.CODE_ERROR) || errMessage.equals("用户长时间未操作或已过期,请重新登录")) && !App.getApp().getAppData().isLogin_timeout()){
        	App.getApp().getAppData().setLogin_timeout(true);//解决登录过期，连续多次跳转到登录页面
        	exit();
        }
        
        if(TextUtils.isEmpty(errMessage)){
//        	errMessage = "网络连接错误";
//        	errMessage = "网络不给力...";
        	KeelLog.e("errCode," + errCode + ",errMessage : 网络不给力...");
        }
        
        if (handler == null 
                || TextUtils.isEmpty(errMessage) 
                || "null".equals(errMessage)) {
            return;
        }
        
        if (errMessage.indexOf("<html>") > -1 
                || errMessage.indexOf("<head>") > -1 
                || errMessage.indexOf("<body>") > -1
                || errMessage.indexOf("</") > -1) {
        }
        int httpConnectCode = 200;
        if(!TextUtils.isEmpty(errCode)){
        	httpConnectCode = Integer.valueOf(errCode);
        }
        if(handler!=null && httpConnectCode < 400){//404错误单独做提示
        	final Bundle bundle = new Bundle();
            bundle.putString(EAPIConsts.Header.ERRORCODE, errCode);
            bundle.putString(EAPIConsts.Header.ERRORMESSAGE, errMessage);
            final Message msg = new Message();
            msg.what = EAPIConsts.handler.show_err;
            msg.setData(bundle);
            handler.sendMessage(msg);
        }else  if(handler!=null && httpConnectCode > 500){
			ToastUtil.showToast(App.getApplicationConxt(), "服务器繁忙，请稍后访问!");
		}/*else  if(handler!=null &&( httpConnectCode > 400 && httpConnectCode < 500)){
			ToastUtil.showToast(mContext, "服务器繁忙，请稍后访问!");
		}*/
    }
    
    public void exit() {

		// 清除用户名密码信息
		App.getApp().getAppData().setUserName("");
		App.getApp().getAppData().setPassword("");
		// 停止百度推送
		PushManager.stopWork(App.getApp());
		// 清除用户对象信息
		AppDataDBManager dbManager = new AppDataDBManager(App.getApp());
		dbManager.delete(App.getApp().getAppData().getUser().mID + "");
		// 清除UserID
		App.getApp().getAppData().setUserID("");
		// 发送广播销毁所有页面
		// sendBroadcast(new Intent(EConsts.Action.LOGIN_OUT));
		// 销毁所有Activity

		/*
		 * 这里是原来的代码 if (FrgConnections.connectionsDBManager != null) {
		 * FrgConnections.connectionsDBManager.clearTable(); }
		 */
		// 这里是新改的代码 20150119 by hanqi
		/*
		 * if (connectionsDBManager != null) {
		 * connectionsDBManager.clearTable(); }
		 */

		SharedPreferences sp = App.getApp().getSharedPreferences(EConsts.share_firstLoginGetConnections,
				App.getApp().MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putInt(EConsts.share_itemFirstLogin, 0);
		editor.putString(EConsts.share_itemUserTableName, "");
		editor.commit();
		for (Activity activity : App.activityList) {
			activity.finish();
		}

		// System.exit(0);
		// 跳转到登录界面
		Intent intent = new Intent(App.getApp(), LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		App.getApp().startActivity(intent);
	}
    
    
    
    /**
     * 
     * @des: 检查返回的http码是否正确，正确为200
     * @author Michael
     * @param response
     * @return
     */
    private boolean comparisonNetworkStatus(String[] responses, Handler handler) {
        if (responses != null && responses.length == 2) {
            final String code = responses[0];
            final String message = responses[1];
            if (KeelLog.DEBUG) {
                KeelLog.v("CODE_HTTP " + code);
            }
            if (EHttpAgent.CODE_HTTP_SUCCEED.equals(code) && (message != null)) {
                KeelLog.d("sucuessfull."+message);
                return true;
            } 
        }
        return false;
    }
}
