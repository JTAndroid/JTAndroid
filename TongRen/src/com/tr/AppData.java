package com.tr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;

import com.tr.db.AppDataDBManager;
import com.tr.model.home.MIndustrys;
import com.tr.model.obj.Area;
import com.tr.model.obj.InvestType;
import com.tr.model.obj.JTFile;
import com.tr.model.obj.MoneyType;
import com.tr.model.obj.Trade;
import com.tr.model.user.JTMember;
import com.utils.common.EConsts;
import com.utils.common.EUtil;

/**
 * @ClassName: AppData.java
 * @Description: App对象中的数据（需要在App发生资源回收时进行数据恢复）
 * @Author leon
 * @Version v 1.0
 * @Date 2014-04-10
 * @LastEdit 2014-04-10
 */
public class AppData implements Serializable {

	/*
	 * public int mUpdateMode =0; // 更新模式 public String mUpdateInfo = ""; //
	 * 更新状态提示信息 public String mAppUrl = ""; // 更新下载地址,当updateMode为1,2时有效 public
	 * String mInviteJoinGinTongInfo = "";
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 用户ID，数据库键值
	private String mUserID = "";
	private String mNickName = "";

	// 用户名和密码（自动登录时使用）
	private String mUserName = "";
	private String mPassword = "";
	private boolean login_timeout = false;

	// private Context mContext;

	// 需要存数据库的对象
	private List<Trade> mListTrade = new ArrayList<Trade>(); // 行业类型
	private List<InvestType> mListInInvestType = new ArrayList<InvestType>(); // 融资类型
	private List<InvestType> mListOutInvestType = new ArrayList<InvestType>(); // 投资类型
	private List<Area> mListArea = new ArrayList<Area>(); // 区域
	private List<MoneyType> mListMoneyType = new ArrayList<MoneyType>(); // 货币类型
	private List<String> mListMoneyRange = new ArrayList<String>(); // 金额范围
	// 会话ID，用来和服务器的会话
	private String mSessionID = "";
	// 金桐网会员对象
	private JTMember mUser = new JTMember();
	// 已下载的文件列表
	private List<JTFile> mListJTFile = new ArrayList<JTFile>();
	// 已上传的文件列表
	// private List<JTFile> listUploadedJTFile = new ArrayList<JTFile>();
	// 新提醒通知
	private boolean mNewMessageAlert = true;
	// 新提醒提示音
	private boolean mNewMessageAlertVoice = true;
	// 通讯录同步
	private boolean mContactSync = false;
	// 免打扰模式
	private boolean mDisturbable = false;
	//动态推送 默认开启
	private boolean mDynamicPush = true;
	/*金桐脑动态推送 默认开启*/
	private boolean mGinTongDynamicush = true;
	// 程序缓存
	private long appCache = 0;
	// 邀请语
	public String mInviteJoinGinTongInfo = "";
	// 允许非好友浏览我的主页 1不容许(false) 2允许 (true)
	private boolean mBrowseHomepageType;
	// 允许非好友对我评价 1不容许(false) 2允许(true)
	private boolean mFriendsAppraiseType;
	/* 免打扰开始时间 */
	private long mNotDisturbStartTimes;
	/* 免打扰结束时间 */
	private long mNotDisturbEndTimes;
	/* 定制设置 */
	private MIndustrys mIndustrys;
	/* 记录动态数目 */
	private int mFrgFlowCount;
	/* 是否开启自动更新功能 true 开启，false 不开启 */
	public boolean misAutomatiUpdates = false;// 是否自动更新app
	/* 是否允许高速录音 true 允许，false 不允许 */
	public boolean mRecord = false;
	/* 是否允许预览视频 0关闭，1仅wifi，2，3G和wifi */
	public int mPreview = 0;

	private boolean isAutosave  = true;;

	public AppData() {
		// mContext = context;
		doInit();
	}

	// 读取保存的用户数据信息用来初始化AppData
	// 发生资源回收时起作用
	public void doInit() {
		// mUser = new JTMember(); // 这里只做简单的初始化
		// 初始化
		// mSessionID = ;
		/*
		 * mUser.mID = EUtil.getIntFromAppSetting(mContext,
		 * EConsts.Key.USER_ID); mUser.mMobile =
		 * EUtil.getStringFromAppSetting(mContext, EConsts.Key.USER_MOBILE);
		 * mUser.mEmail = EUtil.getStringFromAppSetting(mContext,
		 * EConsts.Key.USER_EMAIL); mUser.mUserName =
		 * EUtil.getStringFromAppSetting(mContext, EConsts.Key.USER_NAME);
		 * mUser.mRole = EUtil.getIntFromAppSetting(mContext,
		 * EConsts.Key.USER_ROLE); mUser.mUserType =
		 * EUtil.getIntFromAppSetting(mContext, EConsts.Key.USER_TYPE);
		 */

		// 恢复UserID（键值）
		mUserID = EUtil.getStringFromAppSetting(App.getApplicationConxt(),
				EConsts.Key.USER_ID);
		mNickName = EUtil.getStringFromAppSetting(App.getApplicationConxt(),
				EConsts.Key.NICK_NAME);
		// 用户名和密码
		mUserName = EUtil.getStringFromAppSetting(App.getApplicationConxt(),
				EConsts.Key.USER_NAME);
		mPassword = EUtil.getStringFromAppSetting(App.getApplicationConxt(),
				EConsts.Key.PASSWORD);
		// 恢复用户数据
		AppDataDBManager dbManager = new AppDataDBManager(
				App.getApplicationConxt());
		AppData app_data = dbManager.query(mUserID);
		if (app_data != null) {
			mUser = app_data.mUser;
			mListJTFile = app_data.mListJTFile;
			// listUploadedJTFile = app_data.listUploadedJTFile;
			mListTrade = app_data.mListTrade;
			mListInInvestType = app_data.mListInInvestType;
			mListOutInvestType = app_data.mListOutInvestType;
			mListArea = app_data.mListArea;
			mListMoneyType = app_data.mListMoneyType;
			mListMoneyRange = app_data.mListMoneyRange;
			mNewMessageAlert = app_data.isNewMessageAlert(); 
			mNewMessageAlertVoice = app_data.isNewMessageAlertVoice(); 
			mDynamicPush = app_data.ismDynamicPush(); 
			mContactSync = app_data.isContactSync();
			mDisturbable = app_data.isDisturbable();
			mGinTongDynamicush = app_data.ismGinTongDynamicush();
			mBrowseHomepageType = app_data.ismBrowseHomepageType();
			mFriendsAppraiseType = app_data.ismFriendsAppraiseType();
			mNotDisturbStartTimes = app_data.getmNotDisturbStartTimes();
			mNotDisturbEndTimes = app_data.getmNotDisturbEndTimes();
			mIndustrys = app_data.getmIndustrys();
			mFrgFlowCount = app_data.getmFrgFlowCount();
		}
		// 恢复SessionID
		mSessionID = EUtil.getStringFromAppSetting(App.getApplicationConxt(),
				EConsts.Key.SESSION_ID);
		// 用户设置（新消息提醒、通讯录同步、免打扰模式）
		// mNewMessageAlert =
		// EUtil.getBooleanFromAppSetting(App.getApplicationConxt(),EConsts.Key.NEW_MESSAGE_ALERT);
		// mContactSync =
		// EUtil.getBooleanFromAppSetting(App.getApplicationConxt(),
		// EConsts.Key.CONTACT_SYNC);
		// mDisturbable =
		// EUtil.getBooleanFromAppSetting(App.getApplicationConxt(),
		// EConsts.Key.DISTURBABLE);
		mPreview = EUtil.getIntFromAppSetting(App.getApplicationConxt(), EConsts.Key.PREVIEW_VIDEO,2);// 视频
		misAutomatiUpdates = EUtil.getBooleanFromAppSetting(App.getApplicationConxt(), EConsts.Key.AUTOMATIC_UPDATES);//是否自动更新
		mRecord = EUtil.getBooleanFromAppSetting(App.getApplicationConxt(), EConsts.Key.HIGH_SEPPD_RECORD);//是否开启高速录音模式
	}

	private void saveAppData() {

		AppDataDBManager dbManager = new AppDataDBManager(
				App.getApplicationConxt());
		dbManager.synchronous(mUser.mID + "", AppData.this);
	}

	public String getSessionID() {
		return mSessionID;
	}

	public void setSessionID(String sessionID) {
		EUtil.setStringToAppSetting(App.getApplicationConxt(),
				EConsts.Key.SESSION_ID, sessionID);
		mSessionID = sessionID;
	}

	public JTMember getUser() {
		return mUser;
	}

	public void setUser(JTMember user) {
		if (user != null) {
			mUser = user;
			setUserID(mUser.mID + "");
			if (!TextUtils.isEmpty(mUser.mNick)) {
				setNickName(mUser.mNick);
			} else if (!TextUtils.isEmpty(mUser.getmNick())) {
				setNickName(mUser.getmNick());
			}
			saveAppData();
		}
	}

	public boolean isNewMessageAlert() {
		return mNewMessageAlert;
	}
	public boolean isNewMessageAlertVoice() {
		return mNewMessageAlertVoice;
	}

	public boolean ismDynamicPush() {
		return mDynamicPush;
	}

	public void setmDynamicPush(boolean mDynamicPush) {
		this.mDynamicPush = mDynamicPush;
		saveAppData();
	}

	public void setNewMessageAlert(boolean newMessageAlert) {
		// EUtil.setBooleanToAppSetting(App.getApplicationConxt(),
		// EConsts.Key.NEW_MESSAGE_ALERT, newMessageAlert);
		this.mNewMessageAlert = newMessageAlert;
		saveAppData();
	}
	
	public void setNewMessageAlertVoice(boolean newMessageAlertVoice) {
		// EUtil.setBooleanToAppSetting(App.getApplicationConxt(), EConsts.Key.NEW_MESSAGE_ALERT, newMessageAlert);
		this.mNewMessageAlertVoice = newMessageAlertVoice;
		saveAppData();
	}

	public boolean isContactSync() {
		return mContactSync;
	}

	public void setContactSync(boolean contactSync) {
		// EUtil.setBooleanToAppSetting(App.getApplicationConxt(),
		// EConsts.Key.CONTACT_SYNC, contactSync);
		this.mContactSync = contactSync;
		saveAppData();
	}

	public boolean isDisturbable() {
		return mDisturbable;
	}

	public void setDisturbable(boolean disturbable) {
		// EUtil.setBooleanToAppSetting(App.getApplicationConxt(),
		// EConsts.Key.CONTACT_SYNC, disturbable);
		this.mDisturbable = disturbable;
		saveAppData();
	}

	public List<Trade> getListTrade() {
		return mListTrade;
	}

	public void setListTrade(List<Trade> listTrade) {
		if (listTrade != null) {
			mListTrade = listTrade;
			// saveAppData();
		}
	}

	public List<InvestType> getListInInvestType() {
		return mListInInvestType;
	}

	public void setListInInvestType(List<InvestType> listInInvestType) {
		if (listInInvestType != null) {
			mListInInvestType = listInInvestType;
			// saveAppData();
		}
	}

	public List<InvestType> getListOutInvestType() {
		return mListOutInvestType;
	}

	public void setListOutInvestType(List<InvestType> listOutInvestType) {
		if (listOutInvestType != null) {
			mListOutInvestType = listOutInvestType;
			// saveAppData();
		}
	}

	public List<Area> getListArea() {
		return mListArea;
	}

	public void setListArea(List<Area> listArea) {
		if (listArea != null) {
			mListArea = listArea;
			// saveAppData();
		}
	}

	public List<MoneyType> getListMoneyType() {
		return mListMoneyType;
	}

	public void setListMoneyType(List<MoneyType> listMoneyType) {
		if (listMoneyType != null) {
			this.mListMoneyType = listMoneyType;
			// saveAppData();
		}
	}

	public List<String> getListMoneyRange() {
		return mListMoneyRange;
	}

	public void setListMoneyRange(List<String> listMoneyRange) {
		if (listMoneyRange != null) {
			mListMoneyRange = listMoneyRange;
			// saveAppData();
		}
	}

	public List<JTFile> getListJTFile() {
		return mListJTFile;
	}

	public void setListJTFile(List<JTFile> listJTFile) {
		if (listJTFile != null) {
			mListJTFile = listJTFile;
			saveAppData();
		}
	}

	public String getUserID() {
		return mUserID;
	}

	public void setUserID(String userID) {
		EUtil.setStringToAppSetting(App.getApplicationConxt(),
				EConsts.Key.USER_ID, userID);
		this.mUserID = userID;
	}

	public String getNickName() {
		return mNickName;
	}

	public void setNickName(String nickName) {
		EUtil.setStringToAppSetting(App.getApplicationConxt(),
				EConsts.Key.NICK_NAME, nickName);
		mNickName = nickName;
	}

	public String getUserName() {
		return mUserName;
	}

	public void setUserName(String userName) {
		EUtil.setStringToAppSetting(App.getApplicationConxt(),
				EConsts.Key.USER_NAME, userName);
		this.mUserName = userName;
	}

	public String getPassword() {
		return mPassword;
	}

	public void setPassword(String password) {
		EUtil.setStringToAppSetting(App.getApplicationConxt(),
				EConsts.Key.PASSWORD, password);
		this.mPassword = password;
	}

	public long getAppCache() {
		return appCache;
	}

	public void setAppCache(long appCache) {
		this.appCache = appCache;
	}

	public long getmNotDisturbStartTimes() {
		return mNotDisturbStartTimes;
	}

	public boolean ismBrowseHomepageType() {
		return mBrowseHomepageType;
	}

	public void setmBrowseHomepageType(int mBrowseHomepageType) {
		if (mBrowseHomepageType == 1) {
			this.mBrowseHomepageType = false;
		} else {
			this.mBrowseHomepageType = true;
		}
		saveAppData();
	}

	public boolean ismFriendsAppraiseType() {
		return mFriendsAppraiseType;
	}

	public void setmFriendsAppraiseType(int mFriendsAppraiseType) {
		if (mFriendsAppraiseType == 1) {
			this.mFriendsAppraiseType = false;
		} else {
			this.mFriendsAppraiseType = true;
		}
		saveAppData();
	}

	public void setmNotDisturbStartTimes(long mNotDisturbStartTimes) {
		this.mNotDisturbStartTimes = mNotDisturbStartTimes;
		saveAppData();
	}

	public long getmNotDisturbEndTimes() {
		return mNotDisturbEndTimes;
	}

	public void setmNotDisturbEndTimes(long mNotDisturbEndTimes) {
		this.mNotDisturbEndTimes = mNotDisturbEndTimes;
		saveAppData();
	}

	public MIndustrys getmIndustrys() {
		if (null == mIndustrys) {
			mIndustrys = new MIndustrys();
		}
		return mIndustrys;
	}

	public void setmIndustrys(MIndustrys mIndustrys) {
		this.mIndustrys = mIndustrys;
		saveAppData();
	}

	public int getmFrgFlowCount() {
		return mFrgFlowCount;
	}

	public void setmFrgFlowCount(int mFrgFlowCount) {
		this.mFrgFlowCount = mFrgFlowCount;
		saveAppData();
	}

	/*
	 * public String getInviteJoinGinTongInfo() { return inviteJoinGinTongInfo;
	 * }
	 * 
	 * public void setInviteJoinGinTongInfo(String inviteJoinGinTongInfo) {
	 * this.inviteJoinGinTongInfo = inviteJoinGinTongInfo; }
	 */

	/*
	 * public List<JTFile> getListUploadedJTFile() { return listUploadedJTFile;
	 * }
	 * 
	 * public void setListUploadedJTFile(List<JTFile> listUploadedJTFile) {
	 * if(listUploadedJTFile != null){ this.listUploadedJTFile =
	 * listUploadedJTFile; saveAppData(); } }
	 */
	// conference
	// private List<MPhotoItem> photoList = new ArrayList<MPhotoItem>();
	// public List<MPhotoItem> getPhotoList(){
	// return photoList;
	// }
	//
	// /* 是否开启自动更新功能 true 开启，false 不开启*/
	// private boolean misAppDown= false;//是否自动更新app
	// /*是否允许高速录音 true 允许，false 不允许*/
	// private boolean mRecord = true;
	// /*是否允许预览视频 0从不，1仅wifi，2，3G和wifi */
	// private int mPreview= 0 ;HIGH_SEPPD_RECORD
	/**
	 * 设置高速录音模式
	 * 
	 * @param isRecord
	 *            :true 开启录制，false 不开启
	 */
	public void setHighSeppdRecord(boolean isRecord) {
		EUtil.setBooleanToAppSetting(App.getApplicationConxt(),
				EConsts.Key.HIGH_SEPPD_RECORD, isRecord);
		this.mRecord = isRecord;
	}

	/**
	 * 设置 自动更新 true 自动更新，false 不自动更新
	 */
	public void setAutomatiUpdates(boolean isAutomatiUpdates) {
		EUtil.setBooleanToAppSetting(App.getApplicationConxt(),
				EConsts.Key.AUTOMATIC_UPDATES, isAutomatiUpdates);
		this.misAutomatiUpdates = isAutomatiUpdates;
	}
	/**
	 * 设置 自动保存true 自动保存，false 不自动保存
	 */
	public void setAutosave(boolean isAutosave) {
		EUtil.setBooleanToAppSetting(App.getApplicationConxt(),
				EConsts.Key.AUTOSAVE, isAutosave);
		this.isAutosave = isAutosave;
	}
	/**
	 * 设置视频浏览
	 * 
	 * @param previewView
	 *            0关闭，1仅wifi，2，3G/4G和wifi
	 */
	public void setPreviewVideo(int previewView) {
		EUtil.setIntToAppSetting(App.getApplicationConxt(),
				EConsts.Key.PREVIEW_VIDEO, previewView);
		this.mPreview = previewView;
	}

	public boolean ismGinTongDynamicush() {
		return mGinTongDynamicush;
	}

	public void setmGinTongDynamicush(boolean mGinTongDynamicush) {
		this.mGinTongDynamicush = mGinTongDynamicush;
		saveAppData();
	}

	public boolean isAutosave() {
		return isAutosave;
	}

	public boolean isLogin_timeout() {
		return login_timeout;
	}

	public void setLogin_timeout(boolean login_timeout) {
		EUtil.setBooleanToAppSetting(App.getApplicationConxt(),
				EConsts.Key.IS_LOGIN_TIMEOUT, login_timeout);
		this.login_timeout = login_timeout;
	}

}
