package com.tr;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.R.string;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap.Config;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;
import cn.sharesdk.framework.ShareSDK;

import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tr.baidumapsdk.BaiduLoc;
import com.tr.imservice.HuanXinPushMessageReceiver;
import com.tr.model.user.JTMember;
import com.utils.common.EConsts;
import com.utils.common.EUtil;
import com.utils.common.KeelApplication;
import com.utils.exception.CrashHandler;
import com.utils.http.DownloadPool;
import com.utils.log.KeelLog;

/**
 * @author Michael Date: 2013-06-11
 */
public class App extends KeelApplication {
	
	public static final String TAG = "App";
	
	public DownloadPool mDownloadPool = null;
	public static App mDemoApp;

	public boolean refresh_accountInfo = false;//刷新账号信息页面的标志
	public boolean isLogined = false;
	public String myQrString = "";
//	final Handler eHandler = new Handler();
    private Map<Object, Object> innerMap =new HashMap<Object, Object>();
	private AppData mAppData; // 应用程序级数据对象
	
	public static boolean connectionDataBaseWriteOver = false;

	// 退出时用来销毁Activity
	public static List<Activity> activityList = new LinkedList<Activity>();
	private HuanXinPushMessageReceiver callReceiver;

	// 定位
	private BaiduLoc mBaiduLoc = null;
	private static ImageLoader imageLoader;
	
	/** 全局Context，原理是因为Application类是应用最先运行的，所以在我们的代码调用时，该值已经被赋值过了 */
	private static App mInstance;
	/** 主线程ID */
	private static int mMainThreadId = -1;
	/** 主线程ID */
	private static Thread mMainThread;
	/** 主线程Handler */
	private static Handler mMainThreadHandler;
	/** 主线程Looper */
	private static Looper mMainLooper;	
	
	/**
	 * 获取全局用户对象
	 * 
	 * @return
	 */
	public AppData getAppData() {
		if (mAppData == null) {
			mAppData = new AppData();
		}
		return mAppData;
	}

	public static Context getApplicationConxt() {
		return getApp().getApplicationContext();
	}

	@Override
	public void onCreate() {
		super.onCreate();

		mDemoApp = this;
		// loadLocalWeatherConfig();
		initAccount();
		initDownloadPool();
		mAppData = new AppData();
		// // 定位
		 mBaiduLoc = new BaiduLoc(getApplicationContext());
		 mBaiduLoc.startLocation();
		
		// 初始化环信聊天SDK
		int pid = android.os.Process.myPid();
		String processAppName = getAppName(pid);

		Log.d(TAG, "process app name : " + processAppName);

		EMChat.getInstance().setDebugMode(true);
		EMChat.getInstance().init(this);

		IntentFilter callFilter = new IntentFilter(EMChatManager.getInstance().getIncomingCallBroadcastAction());
		if (callReceiver == null) {
			callReceiver = new HuanXinPushMessageReceiver();
		}

		// 注册通话广播接收者
		this.registerReceiver(callReceiver, callFilter);
		// 注册消息事件监听
		callReceiver.initEventListener();
		
		//默认不能保存缓存，必须通过下面的方式指定
		DisplayImageOptions options = new DisplayImageOptions.Builder()  
		.bitmapConfig(Config.RGB_565)
		.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//		.displayer(new FadeInBitmapDisplayer(100))// 淡入 
        .cacheInMemory(true)  
        .cacheOnDisc(true)  
        .build(); 

		// 缓存文件的目录
		File cacheDir = StorageUtils.getOwnCacheDirectory(this, "/TongRen/image/ClientDetails");
   
		ImageLoaderConfiguration config = new ImageLoaderConfiguration  
                .Builder(getApplicationContext())  
                .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽  
                .threadPoolSize(3)//线程池内加载的数量  
                .threadPriority(Thread.NORM_PRIORITY - 2)  
//                .denyCacheImageMultipleSizesInMemory()  
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现  
                .memoryCacheSize(2 * 1024 * 1024)
                .discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
                .discCacheSize(50 * 1024 * 1024)    
                .discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密  
                .tasksProcessingOrder(QueueProcessingType.LIFO)  
                .discCacheFileCount(100) //缓存的文件数量  
                .defaultDisplayImageOptions(options)  
                .imageDownloader(new BaseImageDownloader(getApplicationContext(), 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间  
                .writeDebugLogs() // Remove for release app  
                .build();//开始构建 
        
        ImageLoader.getInstance().init(config);
        /*初始化线程*/
		mMainThreadId = android.os.Process.myTid();
		mMainThread = Thread.currentThread();
		mMainThreadHandler = new Handler();
		mMainLooper = getMainLooper();
		mInstance = this;
		
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
		
		if (processAppName == null ||!processAppName.equalsIgnoreCase("com.tr")) {
		    Log.e(TAG, "enter the service process!");
		    // 则此application::onCreate 是被service 调用的，直接返回
		    return;
		}else {
			// 初始化分享
			ShareSDK.initSDK(this);
		}
	}
	
	public static App getApplication() {
		return mInstance;
	}

	/** 获取主线程ID */
	public static int getMainThreadId() {
		return mMainThreadId;
	}

	/** 获取主线程 */
	public static Thread getMainThread() {
		return mMainThread;
	}

	/** 获取主线程的handler */
	public static Handler getMainThreadHandler() {
		return mMainThreadHandler;
	}

	/** 获取主线程的looper */
	public static Looper getMainThreadLooper() {
		return mMainLooper;
	}
	
	/**
	 * 加载图片使用
	 * @return
	 */
	public static ImageLoader getImageLoader() {
		return imageLoader;
	}

	

	public BaiduLoc getBaiduLoc() {
		return mBaiduLoc;
	}

	/**
	 * 获取App全局对象
	 * 
	 * @return
	 */
	public static App getApp() {
		return (App) mDemoApp;
	}

	/**
	 * 获取全局用户对象
	 * 
	 * @return
	 */
	public static JTMember getUser() {
		if (TextUtils.isEmpty(App.getApp().mAppData.getSessionID())) {
			App.getApp().mAppData.doInit();
		}
		return App.getApp().mAppData.getUser();
	}

	/**
	 * 获取用户id
	 * 
	 * @return
	 */
	public static String getUserID() {
		String userID = "";
		try {
			if (TextUtils.isEmpty(App.getApp().getAppData().getUserID())) {
				App.getApp().getAppData().doInit();
				if (App.getUser().mUserType == JTMember.UT_PERSON) {
					userID = App.getApp().getAppData().getUserID();
				} else {
					userID = App.getApp().getAppData().getUser().getmOrganizationInfo().mLegalPersonIDCardImage;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (TextUtils.isEmpty(userID)) { // 如果仍为空则直接读数据库
			userID = EUtil.getStringFromAppSetting(getApplicationConxt(), EConsts.Key.USER_ID);
			App.getApp().getAppData().setUserID(userID);
		}
		return userID;
	}

	/**
	 * 获取用户昵称
	 * 
	 * @return
	 */
	public static String getNick() {
		String nickName = "";
		try {
			if (TextUtils.isEmpty(App.getUser().getmNick())) {
				App.getApp().getAppData().doInit();
				nickName = App.getUser().getmNick();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (TextUtils.isEmpty(nickName)) {
			nickName = EUtil.getStringFromAppSetting(getApplicationConxt(), EConsts.Key.NICK_NAME);
			App.getApp().getAppData().setNickName(nickName);
		}
		return nickName;
	}

	/**
	 * 获取SessionID
	 * 
	 * @return
	 */
	public static String getSessionID() {
		String sessionID = "";
		try {
			if (TextUtils.isEmpty(App.getApp().getAppData().getSessionID())) {
				App.getApp().getAppData().doInit();
				sessionID = App.getApp().getAppData().getSessionID();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (TextUtils.isEmpty(sessionID)) {
			sessionID = EUtil.getStringFromAppSetting(getApplicationConxt(), EConsts.Key.SESSION_ID);
			App.getApp().getAppData().setSessionID(sessionID);
		}
		return sessionID;
	}

	/**
	 * 获取用户名
	 * 
	 * @return
	 */
	public static String getUserName() {
		String userName = "";
		try {
			if (TextUtils.isEmpty(App.getApp().mAppData.getUserName())) {
				App.getApp().mAppData.doInit();
			}
			userName = App.getApp().mAppData.getUserName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userName;
	}

	// 获取用户头像
	public static String getUserAvatar() {
		try {
			return App.getApp().mAppData.getUser().mImage;
		} catch (Exception e) {
			return "";
		}
	}

	private void initDownloadPool() {
		if (this.mDownloadPool != null) {
			return;
		}

		KeelLog.d(TAG, "initDownloadPool.");
		DownloadPool downloadPool = new DownloadPool(this);
		this.mDownloadPool = downloadPool;
		this.mDownloadPool.setPriority(Thread.MIN_PRIORITY);
		this.mDownloadPool.setName("DownloadPool");
		this.mDownloadPool.start();
	}

	/**
	 * 初始化用户帐户
	 */
	private void initAccount() {
	}

	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	public void finishAllActivity() {
		for (Activity activity : activityList) {
			activity.finish();
		}
	}

	/**
	 * 支持多Dex打包
	 */
	@Override
	public void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(base);
	}

	/**
	 * @param pID
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = this.getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
			try {
				if (info.pid == pID) {
					CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName,
							PackageManager.GET_META_DATA));
					processName = info.processName;
//					LogUtils.d(c.toString());
					return processName;
				}
			} catch (Exception ex) {
//				LogUtils.d(ex.toString());
			}
		}
		return processName;
	}

	public void addParam(Object key, Object value) {
		innerMap.put(key, value);
	}

	public Object getParam(Object key) {
		return innerMap.get(key);
	}
	public void reMoveParam(Object key) {
		innerMap.remove(key);
	}
	
}
