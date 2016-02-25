package com.tr.ui.demand.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * @ClassName: NetUtil.java
 * @author fxtx
 * @Date 2015年4月3日 上午11:37:54
 * @Description: 网络检查工具类
 */
public class NetUtil {

	private static final int NETWORK_TYPE_UNAVAILABLE = -1;
	private static final int NETWORK_TYPE_WIFI = -101;

	/**
	 * wifi
	 */
	public static final int NETWORK_CLASS_WIFI = -101;
	/** 没有网络*/
	public static final int NETWORK_CLASS_UNAVAILABLE = -1;
	/** Unknown network class. 未知网络 */
	public static final int NETWORK_CLASS_UNKNOWN = 0;
	/** Class of broadly defined "2G" networks. */
	public static final int NETWORK_CLASS_2_G = 1;
	/** Class of broadly defined "3G" networks. */
	public static final int NETWORK_CLASS_3_G = 2;
	/** Class of broadly defined "4G" networks. */
	public static final int NETWORK_CLASS_4_G = 3;

	// 适配低版本手机
	/** Network type is unknown */
	public static final int NETWORK_TYPE_UNKNOWN = 0;
	/** Current network is GPRS */
	public static final int NETWORK_TYPE_GPRS = 1;
	/** Current network is EDGE */
	public static final int NETWORK_TYPE_EDGE = 2;
	/** Current network is UMTS */
	public static final int NETWORK_TYPE_UMTS = 3;
	/** Current network is CDMA: Either IS95A or IS95B */
	public static final int NETWORK_TYPE_CDMA = 4;
	/** Current network is EVDO revision 0 */
	public static final int NETWORK_TYPE_EVDO_0 = 5;
	/** Current network is EVDO revision A */
	public static final int NETWORK_TYPE_EVDO_A = 6;
	/** Current network is 1xRTT */
	public static final int NETWORK_TYPE_1xRTT = 7;
	/** Current network is HSDPA */
	public static final int NETWORK_TYPE_HSDPA = 8;
	/** Current network is HSUPA */
	public static final int NETWORK_TYPE_HSUPA = 9;
	/** Current network is HSPA */
	public static final int NETWORK_TYPE_HSPA = 10;
	/** Current network is iDen */
	public static final int NETWORK_TYPE_IDEN = 11;
	/** Current network is EVDO revision B */
	public static final int NETWORK_TYPE_EVDO_B = 12;
	/** Current network is LTE */
	public static final int NETWORK_TYPE_LTE = 13;
	/** Current network is eHRPD */
	public static final int NETWORK_TYPE_EHRPD = 14;
	/** Current network is HSPA+ */
	public static final int NETWORK_TYPE_HSPAP = 15;

	/**
	 * 获取网络格式
	 * @param context
	 * @return
	 */
	public  static int getNetworkClass(Context context) {
		int networkType = NETWORK_TYPE_UNKNOWN;
		try {
			final NetworkInfo network = ((ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE))
					.getActiveNetworkInfo();
			if (network != null && network.isAvailable()
					&& network.isConnected()) {
				int type = network.getType();
				if (type == ConnectivityManager.TYPE_WIFI) {
					networkType = NETWORK_TYPE_WIFI;
				} else if (type == ConnectivityManager.TYPE_MOBILE) {
					TelephonyManager telephonyManager = (TelephonyManager) context
							.getSystemService(Context.TELEPHONY_SERVICE);
					networkType = telephonyManager.getNetworkType();
				}
			} else {
				networkType = NETWORK_TYPE_UNAVAILABLE;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return getNetworkClassByType(networkType);

	}

	private static int getNetworkClassByType(int networkType) {
		switch (networkType) {
		case NETWORK_TYPE_UNAVAILABLE:
			return NETWORK_CLASS_UNAVAILABLE;
		case NETWORK_TYPE_WIFI:
			return NETWORK_CLASS_WIFI;
		case NETWORK_TYPE_GPRS:
		case NETWORK_TYPE_EDGE:
		case NETWORK_TYPE_CDMA:
		case NETWORK_TYPE_1xRTT:
		case NETWORK_TYPE_IDEN:
			return NETWORK_CLASS_2_G;
		case NETWORK_TYPE_UMTS:
		case NETWORK_TYPE_EVDO_0:
		case NETWORK_TYPE_EVDO_A:
		case NETWORK_TYPE_HSDPA:
		case NETWORK_TYPE_HSUPA:
		case NETWORK_TYPE_HSPA:
		case NETWORK_TYPE_EVDO_B:
		case NETWORK_TYPE_EHRPD:
		case NETWORK_TYPE_HSPAP:
			return NETWORK_CLASS_3_G;
		case NETWORK_TYPE_LTE:
			return NETWORK_CLASS_4_G;
		default:
			return NETWORK_CLASS_UNKNOWN;
		}
	}
	/**
	 * 通过类型获取当前网络格式
	 * @param TYPE
	 * @return
	 */
	public static  String getNetType(int TYPE){
		String s="";
		switch(TYPE){
		case NetUtil.NETWORK_CLASS_2_G:
			s = "2G";
			break;
		case NetUtil.NETWORK_CLASS_3_G:
			s = "3G";
			break;
		case NetUtil.NETWORK_CLASS_4_G:
			s = "4G";
			break;
		case NetUtil.NETWORK_CLASS_WIFI:
			s = "wifi";
			break;
		case  NetUtil.NETWORK_CLASS_UNAVAILABLE:
			s = "无网络";
			break;
		case  NetUtil.NETWORK_CLASS_UNKNOWN:
			s = "未知";
			break;
		}
		return s;
	}
}
