package com.tr.model; 

import java.io.Serializable;

import org.json.JSONObject;

import android.text.TextUtils;

/** 
 * @author xuxinjian
 * @E-mail: xuxinjian@gintong.com
 * @version 1.0
 * @创建时间：2014-4-11 上午10:59:28 
 * @类说明 简单返回对象说明， 如 "succeed"这种
 */
public class SimpleResult  implements Serializable{

    private static final long serialVersionUID = 198009944223064422L;
    private boolean succeed;
    private long meetingid;
	
	public boolean isSucceed() {
		return succeed;
	}
	
	public void setSucceed(boolean succeed) {
		this.succeed = succeed;
	}
	
	public long getMeetingid() {
		return meetingid;
	}

	public void setMeetingid(long meetingid) {
		this.meetingid = meetingid;
	}

	public static SimpleResult createFactory(JSONObject jsonObject) {
		SimpleResult self = null;

		try {
			if (jsonObject != null) {
				self = new SimpleResult();
				self.succeed = jsonObject.optBoolean("succeed");
				self.meetingid = jsonObject.optLong("meetingid");
			}
			return self;
		} 
		catch (Exception e) {
		}
		return null;
	}

}
 