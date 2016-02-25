package com.utils.common;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.baidu.frontia.FrontiaApplication;

/**
 * 应用超类，子类App为应用配置的类，
 *
 * @author archko
 */
public class KeelApplication extends FrontiaApplication {

//	private static final String TAG = "KeelApplication";
    /**
     * 图片存储目录，隐藏的目录，不会被系统图库引进。
     */
    //public static String KEEL_IMG_PATH;
    /**
     * 图片存储目录，告警的
     */
    //public static String KEEL_ALERT_IMG_PATH;
    //自定义的变量  
    public int version=1;
    
//    public int mGridView2Position;
//    public int mGridViewPosition;

    /**
     * 大图片存储路径
     */
    public static final String LOG_DIR="/crashlog/";
    
    @Override
    protected void attachBaseContext(Context base) {
    	super.attachBaseContext(base);
    	MultiDex.install(base);
    }

    /**
     * 图片缓存，替换原来的bmpcache
     */
    @Override
    public void onCreate() {
        super.onCreate();
        /*try {
            ApplicationInfo apInfo=getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            Bundle metaData=apInfo.metaData;
            if (metaData!=null) {
                String cacheDir=metaData.getString("cache_dir");
                if (!TextUtils.isEmpty(cacheDir)) {
                    KEEL_PATH=Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+cacheDir+"/";
                    KEEL_IMG_PATH=KEEL_PATH+".img/";
                    KEEL_ALERT_IMG_PATH=KEEL_PATH+".img/alarm/";
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            KEEL_IMG_PATH=KEEL_PATH+".img/";
            KEEL_ALERT_IMG_PATH=KEEL_PATH+".img/alarm/";
        }
        KeelLog.v(TAG, "onCreate:KeelPath=="+KeelApplication.KEEL_PATH+" KEEL_IMG_PATH:"+KEEL_IMG_PATH);

        // 注册KeelExceptionHandler
        //KeelExceptionHandler.getInstance().init(getApplicationContext());

        File logDir=new File(KEEL_PATH);
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
        logDir=new File(KEEL_ALERT_IMG_PATH);
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
        logDir=new File(KEEL_ALERT_IMG_PATH+PICTURE_DIR);
        if (!logDir.exists()) {
            logDir.mkdirs();
        }*/

        /*JPushInterface.setDebugMode(true);
        JPushInterface.init(getApplicationContext());*/
        
        //KeelExceptionHandler.getInstance().init(getApplicationContext());      
    }

    /*public static KeelApplication getApp() {
        return mDemoApp;
    }*/

    /**
     * 判断网络是否连接 （未使用，注掉）
     *
     * @param activity
     * @return
     */
/*    public static boolean hasInternetConnection(Activity activity) {
        try {
            ConnectivityManager connectivity=(ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity==null) {
                return false;
            } else {
                NetworkInfo[] info=connectivity.getAllNetworkInfo();
                if (info!=null) {
                    for (int i=0; i<info.length; i++) {
                        if (info[i].getState()==NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }*/

   /* public static boolean hasInternetConnection(Context context) {
        ConnectivityManager connectivity=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity==null) {
            return false;
        } else {
            NetworkInfo[] info=connectivity.getAllNetworkInfo();
            if (info!=null) {
                for (int i=0; i<info.length; i++) {
                    if (info[i].getState()==NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }*/

    //--------------------- 缓存 ---------------------
    /*private static void initCache() {
        mLruCache=new LruCache<String, Bitmap>(CACHE_CAPACITY);
    }

    public static LruCache<String, Bitmap> getLruCache() {
        if (null==mLruCache) {
            initCache();
        }
        return mLruCache;
    }

    public ImageManager getImageManager() {
        if (null==mImageManager) {
            mImageManager=new ImageManager();
        }
        return mImageManager;
    }*/
   
}
