package com.utils.common;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import com.utils.file.FileConstants;
import com.utils.string.StringUtils;



import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;


public class Constants {
	// 图片最大小界限
    public final static int IMAGE_SIZE_COMPRESS = 200 * 1024;
    public static int IMAGE_COMPRESS_WIDTH = 800;
    public static int IMAGE_COMPRESS_HEIGHT = 600;

	
	// 设置图片超过100K就进行压缩
    public final static int POST_PICTURE_MAX_COMPRESS_SIZE = 1024 * 200;
	
	public static final String NULL = "null";
	public static final int TIMEOUT = 45;
	public static boolean IS_TRAFFIC_STATS = false;//流量统计
	
	public static final int TYPE_NET_WORK_DISABLED = 0;// 网络不可用
    public static final int TYPE_MNO_CM = 1;// 移动
    public static final int TYPE_MNO_CU = 2;// 联通
    public static final int TYPE_MNO_CT = 3;// 电信
    public static final int TYPE_CM_CU_WAP = 4;// 移动联通wap10.0.0.172
    public static final int TYPE_CT_WAP = 5;// 电信wap 10.0.0.200
    public static final int TYPE_OTHER_NET = 6;// 电信,移动,联通,wifi 等net网络
	
 // 设置当前联网类型
    public static int CURRECT_NET_WORK_TYPE = Constants.TYPE_OTHER_NET;
	/**
     * 单位换算
     * @param count
     * @return
     */
    public static String unitExchange(long count) {
        final double tem = count / (1024 * 1024.0);
        final DecimalFormat df = new DecimalFormat("0");
        final DecimalFormat dfs = new DecimalFormat("0.00");
        if (Integer.parseInt(df.format(tem)) > 0)
        {
            return dfs.format(tem) + " M";
        }
        else
        {
            return dfs.format(count / 1024.0) + " KB";
        }
    }
	public static String getStringTime() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-"
				+ c.get(Calendar.DAY_OF_MONTH) + "-" + c.get(Calendar.HOUR_OF_DAY) + ":"
				+ c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
	}
	
	public static void showToast(Context context, String msg) {
        showToast(context, msg, Toast.LENGTH_SHORT);
    }
	
	public static void showToast(Context context, int resId) {
		showToast(context, resId, Toast.LENGTH_SHORT);
	}

	public static void showToast(Context context, int resId, int length) {
		if (context != null) {
			Toast.makeText(context, resId, length).show();
		}
	}
	
	public static void showToast(Context context, String content, int length) {
		if (context != null) {
			Toast.makeText(context, content, length).show();
		}
    }
	
	/**
	 * wifi是否用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifiAvailable(Context context) {
		final ConnectivityManager mConnectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo info = mConnectivity.getActiveNetworkInfo();
		if (info != null && info.getType() == ConnectivityManager.TYPE_WIFI) {
			return info.isConnected();
		} else {
			return false;
		}
	}
	
	static String  cacheFileDirPath=null;
	public static String getCacheFileDirPath(){
        if (StringUtils.isEmpty(cacheFileDirPath)) {
            cacheFileDirPath = FileConstants.SAVE_FILE_PATH +"/chujianLife/";
        }
            return cacheFileDirPath;
    }
	
}
