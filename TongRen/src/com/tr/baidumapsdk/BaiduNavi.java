package com.tr.baidumapsdk;

import com.baidu.lbsapi.auth.LBSAuthManagerListener;
//import com.baidu.navisdk.BaiduNaviManager;
//import com.baidu.navisdk.BNaviEngineManager.NaviEngineInitListener;
//import com.baidu.navisdk.BaiduNaviManager.OnStartNavigationListener;
//import com.baidu.navisdk.comapi.routeplan.RoutePlanParams.NE_RoutePlan_Mode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

public class BaiduNavi {
	private boolean mIsEngineInitSuccess = true;
	private Activity mAct = null;

	public BaiduNavi(Activity act) {
		mAct = act;
//		BaiduNaviManager.getInstance().initEngine(act, getSdcardDir(), mNaviEngineInitListener, new LBSAuthManagerListener() {
//			@Override
//			public void onAuthResult(int status, String msg) {
//				if (0 != status) {
//					mIsEngineInitSuccess = false;
//				}
//			}
//		});
	}

	private String getSdcardDir() {
		if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().toString();
		}
		return null;
	}

	
//	private NaviEngineInitListener mNaviEngineInitListener = new NaviEngineInitListener() {
//		public void engineInitSuccess() {
//
//		}
//
//		public void engineInitStart() {
//		}
//
//		public void engineInitFail() {
//			mIsEngineInitSuccess = false;
//		}
//	};

	public boolean naviGuide(double startLatitude, double startLongitude, String startName, double endLatitude, double endLongitude, String endName) {
//		if (null != mAct) {
//			boolean isInitSuccess = BaiduNaviManager.getInstance().checkEngineStatus(mAct.getApplicationContext());
//			if (!isInitSuccess || !mIsEngineInitSuccess) {
//				return isInitSuccess;
//			}
//		} else {
//			return false;
//		}
//		BaiduNaviManager.getInstance().launchNavigator(mAct, startLatitude, startLongitude, startName, endLatitude, endLongitude, endName,
//				NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_TIME, true, BaiduNaviManager.STRATEGY_FORCE_ONLINE_PRIORITY, new OnStartNavigationListener() {
//
//					@Override
//					public void onJumpToNavigator(Bundle configParams) {
//						Intent intent = new Intent(mAct, BNavigatorActivity.class);
//						intent.putExtras(configParams);
//						mAct.startActivity(intent);
//					}
//
//					@Override
//					public void onJumpToDownloader() {
//					}
//				});
		return true;
	}
}