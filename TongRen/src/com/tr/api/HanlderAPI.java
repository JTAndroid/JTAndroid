package com.tr.api; 

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.utils.common.EUtil;
import com.utils.http.EAPIConsts;

/** 
 * @author xuxinjian
 * @E-mail: xuxinjian@gintong.com
 * @version 1.0
 * @创建时间：2014-4-15 下午12:55:58 
 * @类说明 
 */
class HandlerAPI extends Handler {  
    /* 在UI线程中创建handler时，可以直接调用这个构造函数 */ 
    public HandlerAPI() {  
        super();  
        // TODO Auto-generated constructor stub  
    }  

    /* 在子线程中创建一个Handler需要用到这个构造函数，否则报错 */ 
    public HandlerAPI(Looper looper) {  
        super(looper);  
        // TODO Auto-generated constructor stub  
    }  

    @Override 
    public void handleMessage(Message msg) {  
        // TODO Auto-generated method stub  
        super.handleMessage(msg);  

        switch (msg.what) {  
        case EAPIConsts.handler.show_err: {  
            Bundle bundle = (Bundle)msg.getData();
            String errCode = (String)bundle.getString(EAPIConsts.Header.ERRORCODE);
            String errMessage = (String)bundle.getString(EAPIConsts.Header.ERRORMESSAGE);
            //EUtil.showToast(errMessage + " errCode:" + errCode);
            	 EUtil.showToast(errMessage);
           
        }  
            break;  
        
        default:  
            break;  

        }  

    }  
}  