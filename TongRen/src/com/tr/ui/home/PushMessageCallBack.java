package com.tr.ui.home;

import com.tr.model.conference.MSociality;

public interface PushMessageCallBack {

	// 推送消息回调
	public void onPushMessage(String UserId, MSociality mSociality);

}
