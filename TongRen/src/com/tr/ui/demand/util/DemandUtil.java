package com.tr.ui.demand.util;

import android.content.Context;

import com.tr.App;

/**
 * @ClassName: DemandUtil.java
 * @author fxtx
 * @Date 2015年4月9日 下午5:49:30
 * @Description: 需求模块工具类
 */
public class DemandUtil {
	/**
	 * 判断视频当前是否能播放和上传
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isVideo(Context context) {
		int netType = NetUtil.getNetworkClass(context);
		boolean isVideo = false;
		switch (App.mDemoApp.getAppData().mPreview) {
		case 1:
			if (netType == NetUtil.NETWORK_CLASS_WIFI)
				isVideo = true;
			break;
		case 2:
			if (netType == NetUtil.NETWORK_CLASS_3_G
					|| netType == NetUtil.NETWORK_CLASS_4_G
					|| netType == NetUtil.NETWORK_CLASS_WIFI) {
				isVideo = true;
			}
			break;
		default:
			isVideo = false;
			break;
		}
		return isVideo;
	}
}
