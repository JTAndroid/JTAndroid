package com.tr.model.im; 

import java.io.Serializable;

import org.json.JSONObject;

import android.text.TextUtils;

/** 
 * @author xuxinjian
 * @E-mail: xuxinjian@gintong.com
 * @version 1.0
 * @创建时间：2014-4-11 上午10:59:28 
 * @类说明 
 */
public class MSetChannelIDResp  implements Serializable{

    private static final long serialVersionUID = 198009944230064422L;
    private boolean succeed;
    private String tag;
	

	public static MSetChannelIDResp createFactory(JSONObject jsonObject)
	{
		MSetChannelIDResp self = null;
		
            try {
            	if (jsonObject != null) {
            		 self  = new MSetChannelIDResp();
            		 self.succeed = jsonObject.optBoolean("succeed");
            		 self.tag = jsonObject.optString("tag");
            	}
        						
                return self;
                
            } catch (Exception e) {
            }
        return null;
    }


	public boolean isSucceed() {
		return succeed;
	}


	public void setSucceed(boolean succeed) {
		this.succeed = succeed;
	}


	public String getTag() {
		return tag;
	}


	public void setTag(String tag) {
		this.tag = tag;
	}

}
 