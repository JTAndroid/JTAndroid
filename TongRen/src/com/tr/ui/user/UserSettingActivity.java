package com.tr.ui.user;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.App;
import com.tr.R;
import com.tr.api.UserReqUtil;
import com.tr.image.FileUtils;
import com.tr.model.api.DataBox;
import com.tr.model.home.MIndustry;
import com.tr.model.home.MIndustrys;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.conference.initiatorhy.SharePeopleHubFragment;
import com.tr.ui.home.PrivacyActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.widgets.selectTime.TosAdapterView;
import com.tr.ui.widgets.selectTime.TosGallery;
import com.tr.ui.widgets.selectTime.Utils;
import com.tr.ui.widgets.selectTime.WheelTextView;
import com.tr.ui.widgets.selectTime.WheelView;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;
import com.utils.common.EConsts;
import com.utils.common.EUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.tr.ui.home.FrameWorkUtils;
/**
 * @ClassName: UserSettingActivity.java
 * @Description: Frame设置
 * @Author CJJ
 * @Created 2015-01-19
 */

public class UserSettingActivity extends JBaseFragmentActivity implements View.OnClickListener {

	private final String TAG = getClass().getSimpleName();
	private String qrUrl;
	private App mMainApp;
	/* 账号管理 */
	private RelativeLayout mSettingAccountManagement;
	/* 隐私策略 */
	private RelativeLayout user_setting_privacy;
	/* 时间段选择 */
	private LinearLayout mUserSettingSelectTimesLl;
	/* 行业偏好 */
	private RelativeLayout mUserSettingCustomized;
	/* 黑名单 */
	private RelativeLayout mUserSettingRemoveName;
	/* 检查更新 */
	private RelativeLayout mUserSettingCheckUpdatesRl;
	/* 清除缓存 */
	private RelativeLayout mUserSettingClearCacheRl;
	/* 分享金桐 */
	private RelativeLayout share_gintong;
	/* 新消息提醒 */
	private CheckBox mUserSettingRemindCb;
	/* 新消息提示音 */
	private CheckBox mUserSettingRemindVoiceCb;
	/* 免打扰模式 */
	private CheckBox mUserSettingNotDisturbCb;
	/* 允许非好友浏览我的主页 */
	private CheckBox mUserSettingBrowsingHomepageCb;
	/* 允许非好友对我评价 */
	private CheckBox mUserSettingEvaluationCb;
	/* 版本号 */
	private TextView mUserSettingCheckUpdatesTv;
	/* 定制行业 */
	private TextView mSettingSelectedIndustryTv;
	/* 清除缓存 */
	private TextView mUserSettingClearCacheTv;
	/* 开始时间 */
	private TextView mSettingSelectStartTimesTv;
	/* 结束时间 */
	private TextView mSettingSelectEndTimesTv;
	/* TimePicker */
	private MyTimePickerDialog timePickDlg;
	private View headerVi;

	/* 高速录音模式 */
	private CheckBox mSettingRecordCb;
	/*金桐脑推送*/
	private CheckBox mGintongPushInfoCb;
	/* 上传预览视频 */
	private TextView mSettingPreviewTv;
	/* 自动更新APP */
	private TextView mSettingAutomaticUpdatesTv;
	/**隐藏万能创建键按钮*/
	private CheckBox mGoneCreateButton;

	/* 记录时间 */
	private String stratTimes;
	private String endTimes;
	private String[] splitArr;
	int startH, startM, endH, endM;
	/* 行业对象集合 */
	private List<MIndustry> mIndustryList;
	private MIndustrys mIndustrys;
	private StringBuilder strBuilder;
	/* 1 允许非好友浏览我的主页 2允许非好友对我评价" */
	private String reqTypeStr;

	private PopupWindow popupWindow;
	private WheelView mStartHours;
	private WheelView mEndHours;
	private WheelView mStartMins;
	private WheelView mEndMins;
	private NumberAdapter hourAdapter;
	private NumberAdapter minAdapter;
	private static final String[] hoursArray = { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" };
	private static final String[] minsArray = { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24",
			"25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55",
			"56", "57", "58", "59" };
	private String[] previewVideo;
	private Calendar calendar;

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "设置", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// TODO
			showSettingIndustry();
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_setting);
		previewVideo = getResources().getStringArray(R.array.demand_preview_video);
		initVars();
		initControls();

	};

	private void initVars() {
		mMainApp = App.getApp();
	}

	private OnCheckedChangeListener mCheckChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			switch (buttonView.getId()) {
			case R.id.user_setting_remind_cb:
				mMainApp.getAppData().setNewMessageAlert(isChecked);// 提醒
				if (!mMainApp.getAppData().isNewMessageAlert()) {
					mUserSettingRemindVoiceCb.setChecked(false);
					mMainApp.getAppData().setNewMessageAlertVoice(false);// 提示音
				}
				break;
			case R.id.user_setting_remindvoice_cb:
				if (mMainApp.getAppData().isNewMessageAlert()) {// 只有当允许发送提醒时才活动
					mUserSettingRemindVoiceCb.setChecked(isChecked);
					mMainApp.getAppData().setNewMessageAlertVoice(isChecked);
				} else {
					mUserSettingRemindVoiceCb.setChecked(false);
				}
				break;
			case R.id.user_setting_dynamicpush_cb://动态推送开关
				mMainApp.getAppData().setmDynamicPush(isChecked);
				if (!mMainApp.getAppData().ismDynamicPush()) {//不接受
					
				}
				break;
			case R.id.gintong_push_info_cb://金桐脑推送
				mMainApp.getAppData().setmGinTongDynamicush(isChecked);
				break;
			case R.id.user_setting_not_disturb_cb:
				mMainApp.getAppData().setDisturbable(isChecked);
				showSelectTimesRLUI(isChecked);
				break;
			case R.id.user_setting_browsing_homepage_cb:
				if (mUserSettingBrowsingHomepageCb.isChecked() ^ mMainApp.getAppData().ismBrowseHomepageType()) {
					UserReqUtil.doUpdateUserConfig(UserSettingActivity.this, mBindData, null, 1, mUserSettingBrowsingHomepageCb.isChecked() ? 2 : 1);
					reqTypeStr = "1";
					showLoadingDialog();
				}
				break;
			case R.id.user_setting_evaluation_cb:
				if (mUserSettingEvaluationCb.isChecked() ^ mMainApp.getAppData().ismFriendsAppraiseType()) {
					UserReqUtil.doUpdateUserConfig(UserSettingActivity.this, mBindData, null, 2, mUserSettingEvaluationCb.isChecked() ? 2 : 1);
					reqTypeStr = "2";
					showLoadingDialog();
				}
				break;
			case R.id.setting_demand_recordCB:// 使用高速录音模式
				if (!isChecked) {
					View view = View.inflate(UserSettingActivity.this, R.layout.demand_user_setting_dialog1, null);
					showDialog(view);
					// 确定
					view.findViewById(R.id.confirmTv).setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// 确定修改
							mMainApp.getAppData().setHighSeppdRecord(false);
							dialog.dismiss();
						}
					});
					view.findViewById(R.id.containerLl).setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();
							mSettingRecordCb.setChecked(true);
							mMainApp.getAppData().setHighSeppdRecord(true);
						}
					});
				} else {
					mMainApp.getAppData().setHighSeppdRecord(true);
				}
				break;
			case R.id.user_setting_autosave_cb:  //自动保存
				mMainApp.getAppData().setAutosave(isChecked);
				UserReqUtil.doUpdateUserConfig(UserSettingActivity.this, mBindData, null, 5, mGintongAutosaveCb.isChecked() ? 2 : 1);
				break;
			case R.id.user_setting_gone_button_cb:  //隐藏万能创建键
				isShowCreateButton = !isChecked?true:false;
				Editor editor = sp.edit();
				editor.putBoolean(EConsts.share_invisible_create_button,isShowCreateButton);
				editor.commit();
				break;
			}
		}
	};

	private CheckBox mGintongAutosaveCb;

	/**
	 * 弹出dialog
	 * 
	 * @param view
	 */
	private void showDialog(View view) {
		dialog = new Dialog(UserSettingActivity.this, R.style.MyDialog);

		// dialog.setCancelable(false);//是否允许返回
		dialog.addContentView(view, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		dialog.show();

	}

	/**
	 * 选择免打扰UI
	 * 
	 * @param isChecked
	 */
	private void showSelectTimesRLUI(boolean isChecked) {
		if (isChecked) {
			mUserSettingSelectTimesLl.setVisibility(View.VISIBLE);
		} else {
			mUserSettingSelectTimesLl.setVisibility(View.GONE);
		}
	}

	private void initControls() {
		mSettingAccountManagement = (RelativeLayout) findViewById(R.id.user_setting_account_management);
		user_setting_privacy = (RelativeLayout) findViewById(R.id.user_setting_privacy);
		mUserSettingSelectTimesLl = (LinearLayout) findViewById(R.id.user_setting_select_times_lL);
		mUserSettingCustomized = (RelativeLayout) findViewById(R.id.user_setting_customized);
		mUserSettingRemoveName = (RelativeLayout) findViewById(R.id.user_setting_remove_name);
		mUserSettingCheckUpdatesRl = (RelativeLayout) findViewById(R.id.user_setting_check_updates_rl);
		mUserSettingClearCacheRl = (RelativeLayout) findViewById(R.id.user_setting_clear_cache_rl);
		share_gintong = (RelativeLayout) findViewById(R.id.share_gintong);
		
		mUserSettingRemindCb = (CheckBox) findViewById(R.id.user_setting_remind_cb);
		mUserSettingRemindVoiceCb = (CheckBox) findViewById(R.id.user_setting_remindvoice_cb);
		mUserSettingDynamicPush = (CheckBox) findViewById(R.id.user_setting_dynamicpush_cb);
		mUserSettingNotDisturbCb = (CheckBox) findViewById(R.id.user_setting_not_disturb_cb);
		mUserSettingCheckUpdatesTv = (TextView) findViewById(R.id.user_setting_check_updates_tv);
		mSettingSelectedIndustryTv = (TextView) findViewById(R.id.setting_selected_industry_tv);
		mUserSettingClearCacheTv = (TextView) findViewById(R.id.user_setting_clear_cache_tv);
		mSettingSelectStartTimesTv = (TextView) findViewById(R.id.setting_select_start_times_tv);
		mSettingSelectEndTimesTv = (TextView) findViewById(R.id.setting_select_end_times_tv);
		mUserSettingBrowsingHomepageCb = (CheckBox) findViewById(R.id.user_setting_browsing_homepage_cb);
		mUserSettingEvaluationCb = (CheckBox) findViewById(R.id.user_setting_evaluation_cb);
		mSettingAutomaticUpdatesTv = (TextView) findViewById(R.id.setting_demand_upload_apkTv);
		mSettingPreviewTv = (TextView) findViewById(R.id.setting_demand_previewTv);
		mSettingRecordCb = (CheckBox) findViewById(R.id.setting_demand_recordCB);
		mGintongPushInfoCb = (CheckBox) findViewById(R.id.gintong_push_info_cb);//金桐脑推送
		mGintongAutosaveCb = (CheckBox) findViewById(R.id.user_setting_autosave_cb);//自动保存
		mGoneCreateButton = (CheckBox) findViewById(R.id.user_setting_gone_button_cb);
		findViewById(R.id.feedbackTv).setOnClickListener(this);
		findViewById(R.id.setting_demand_previewRl).setOnClickListener(this);
		findViewById(R.id.setting_demand_upload_apkRl).setOnClickListener(this);
		headerVi = findViewById(R.id.headerVi);

		mSettingAccountManagement.setOnClickListener(mClickListener);
		user_setting_privacy.setOnClickListener(mClickListener);
		/* mUserSettingSelectTimesRl.setOnClickListener(mClickListener); */
		mUserSettingCustomized.setOnClickListener(mClickListener);
		mUserSettingRemoveName.setOnClickListener(mClickListener);
		mUserSettingCheckUpdatesRl.setOnClickListener(mClickListener);
		mUserSettingClearCacheRl.setOnClickListener(mClickListener);
		mSettingSelectStartTimesTv.setOnClickListener(mClickListener);
		mSettingSelectEndTimesTv.setOnClickListener(mClickListener);
		share_gintong.setOnClickListener(mClickListener);
		// 使用高速录音模式
		mSettingRecordCb.setOnCheckedChangeListener(mCheckChangeListener);
		mGintongPushInfoCb.setOnCheckedChangeListener(mCheckChangeListener);
		// 新消息提醒
		mUserSettingRemindCb.setOnCheckedChangeListener(mCheckChangeListener);
		mUserSettingRemindVoiceCb.setOnCheckedChangeListener(mCheckChangeListener);
		mUserSettingDynamicPush.setOnCheckedChangeListener(mCheckChangeListener);
		mGintongAutosaveCb.setOnCheckedChangeListener(mCheckChangeListener);
		mUserSettingRemindCb.setChecked(mMainApp.getAppData().isNewMessageAlert());
		mUserSettingRemindVoiceCb.setChecked(mMainApp.getAppData().isNewMessageAlertVoice());
		mUserSettingDynamicPush.setChecked(mMainApp.getAppData().ismDynamicPush());
		mGintongPushInfoCb.setChecked(mMainApp.getAppData().ismGinTongDynamicush());
		// 免打扰模式
		mUserSettingNotDisturbCb.setOnCheckedChangeListener(mCheckChangeListener);
		mUserSettingNotDisturbCb.setChecked(mMainApp.getAppData().isDisturbable());
		mGintongAutosaveCb.setChecked(mMainApp.getAppData().isAutosave());
		showSelectTimesRLUI(mMainApp.getAppData().isDisturbable());
		/* 允许非好友浏览我的主页 1 不允许 2 允许 */
		mUserSettingBrowsingHomepageCb.setOnCheckedChangeListener(mCheckChangeListener);
		mUserSettingBrowsingHomepageCb.setChecked(mMainApp.getAppData().ismBrowseHomepageType());
		/* 允许非好友对我评价 1 不允许 2 允许 */
		mUserSettingEvaluationCb.setOnCheckedChangeListener(mCheckChangeListener);
		mUserSettingEvaluationCb.setChecked(mMainApp.getAppData().ismFriendsAppraiseType());
		
		getApplicationContext();
		sp = getApplication().getSharedPreferences(EConsts.share_invisible_create_button, Context.MODE_PRIVATE);
		isShowCreateButton = sp.getBoolean(EConsts.share_invisible_create_button, true);
		mGoneCreateButton.setChecked(!isShowCreateButton);
		
		mGoneCreateButton.setOnCheckedChangeListener(mCheckChangeListener);
		/* 初始化日期和日历控件 */
		calendar = Calendar.getInstance();

		/* 允许 高速录音模式 预览视频设置，自动更新app设置 */
		mSettingRecordCb.setChecked(mMainApp.getAppData().mRecord);

		if (previewVideo != null) {
			mSettingPreviewTv.setText(previewVideo[mMainApp.getAppData().mPreview]);
		}
		if (mMainApp.getAppData().misAutomatiUpdates) {
			mSettingAutomaticUpdatesTv.setText(R.string.automati_updates2);// 进行下载
		} else {
			mSettingAutomaticUpdatesTv.setText(R.string.automati_updates1);// 从不进行
		}

		if (mMainApp.getAppData().getmNotDisturbStartTimes() == 0) {
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 0);
			String mStartTimeStr = getTimeFormat(
					calendar.get(Calendar.HOUR_OF_DAY),
					calendar.get(Calendar.MINUTE));
			mSettingSelectStartTimesTv.setText(mStartTimeStr);
		} else {
			calendar.setTimeInMillis(mMainApp.getAppData()
					.getmNotDisturbStartTimes());
			String mStartTimeStr = getTimeFormat(
					calendar.get(Calendar.HOUR_OF_DAY),
					calendar.get(Calendar.MINUTE));
			mSettingSelectStartTimesTv.setText(mStartTimeStr);
		}

		if (mMainApp.getAppData().getmNotDisturbEndTimes() == 0) {
			calendar.set(Calendar.HOUR_OF_DAY, 8);
			calendar.set(Calendar.MINUTE, 0);
			String mEndTimeStr = getTimeFormat(
					calendar.get(Calendar.HOUR_OF_DAY),
					calendar.get(Calendar.MINUTE));
			mSettingSelectEndTimesTv.setText(mEndTimeStr);
		} else {
			calendar.setTimeInMillis(mMainApp.getAppData()
					.getmNotDisturbEndTimes());
			String mEndTimeStr = getTimeFormat(
					calendar.get(Calendar.HOUR_OF_DAY),
					calendar.get(Calendar.MINUTE));
			mSettingSelectEndTimesTv.setText(mEndTimeStr);
		}
		// 检查更新
		mUserSettingCheckUpdatesRl.setOnClickListener(mClickListener);
		mUserSettingCheckUpdatesTv.setText("v" + EUtil.getVersionName(this));

		long fileSize = EUtil.getDirSize(getCacheDir());
		if (fileSize < mMainApp.getAppData().getAppCache()) {
			mMainApp.getAppData().setAppCache(fileSize);
			mUserSettingClearCacheTv.setText(EUtil.formatFileSize(0));
		} else {
			mUserSettingClearCacheTv.setText(EUtil.formatFileSize(fileSize - mMainApp.getAppData().getAppCache()));
		}
		mIndustrys = new MIndustrys();
		mIndustrys.setListIndustry(mMainApp.getAppData().getUser().getListIndustry());
		showSettingIndustry();
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			dismissLoadingDialog();
			mMainApp.getAppData().setAppCache(EUtil.getDirSize(getCacheDir()));
			mUserSettingClearCacheTv.setText(EUtil.formatFileSize(0));
		}
	};

	protected void onActivityResult(int requestCode, int resultCode, android.content.Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		// 各种activity界面回调方法
		if (ENavConsts.ActivityReqCode.REQUEST_CODE_SETTING_INDUSTRY_ACTIVITY == requestCode && null != intent) { // 知识内容
			mIndustrys = (MIndustrys) intent.getExtras().getSerializable(EConsts.Key.INDUSTRYS);
			handler.sendEmptyMessageDelayed(0, 100);
		}
	}

	private void showSettingIndustry() {
		if (null != mIndustrys && null != mIndustrys.getListIndustry() && mIndustrys.getListIndustry().size() > 0) {
			mIndustryList = mIndustrys.getListIndustry();
			strBuilder = new StringBuilder("");
			for (int i = 0; i < mIndustryList.size(); i++) {
				if (mIndustryList.size() - 1 == i) {
					strBuilder.append(mIndustryList.get(i).getName());
				} else {
					strBuilder.append(mIndustryList.get(i).getName() + "、");
				}
			}
			mMainApp.getAppData().setmIndustrys(mIndustrys);
			mMainApp.getAppData().getUser().setListIndustry(mIndustryList);
			mSettingSelectedIndustryTv.setText(strBuilder.toString().trim());
		} else {
			mSettingSelectedIndustryTv.setText("还未设置您感兴趣的行业");
		}
	};

	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 帐号管理
			if (v == mSettingAccountManagement) {
				ENavigate.startAccountManagerActivity(UserSettingActivity.this);
			}else if (v==user_setting_privacy) {
				startActivity(new Intent(UserSettingActivity.this, PrivacyActivity.class));
			}
			// 定制设置
			else if (v == mUserSettingCustomized) {
				ENavigate.startChooseProfessionActivityForResult(UserSettingActivity.this, ENavConsts.ActivityReqCode.REQUEST_CODE_SETTING_INDUSTRY_ACTIVITY, 1, mIndustrys);
			}
			// 黑名单
			else if (v == mUserSettingRemoveName) {
				ENavigate.startBlackListActivity(UserSettingActivity.this);
			} else if (v == mUserSettingClearCacheRl) { // 清空缓存
				showLoadingDialog();
				new Thread(new Runnable() {
					@Override
					public void run() {
						EUtil.deleteFilesByDirectory(getCacheDir());
						//删除本地TongRen下的文件
						File icondir = new File(FileUtils.getIconDir());
						EUtil.deleteDirectory(icondir);
						EUtil.deleteDirectory(new File(FileUtils.getDir("image")));
						mHandler.sendEmptyMessage(0);
					}
				}).start();
			} else if (v == mUserSettingCheckUpdatesRl) { // 检查更新
				showLoadingDialog();
				// 检查更新
				UmengUpdateAgent.setUpdateOnlyWifi(false);
				UmengUpdateAgent.setUpdateAutoPopup(false);
				UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
					@Override
					public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
						dismissLoadingDialog();
						switch (updateStatus) {
						case UpdateStatus.Yes: // has update
							UmengUpdateAgent.showUpdateDialog(UserSettingActivity.this, updateInfo);
							break;
						case UpdateStatus.No: // has no update
							showToast("您当前使用的已经是最新版本");
							break;
						case UpdateStatus.Timeout: // time out
							showToast("请求超时，请重试");
							break;
						}
					}
				});
				UmengUpdateAgent.forceUpdate(UserSettingActivity.this);

			} else if (v == mSettingSelectStartTimesTv) {
				showSelectTimePopupWindow(UserSettingActivity.this);
				// setDateTime(v, 1);
			} else if (v == mSettingSelectEndTimesTv) {
				// setDateTime(v, 2);
				showSelectTimePopupWindow(UserSettingActivity.this);
			}else if(v==share_gintong){
				FrameWorkUtils.showSharePopupWindow(UserSettingActivity.this, qrUrl);
			}
		}
	};

	class MyTimePickerDialog extends TimePickerDialog {

		public MyTimePickerDialog(Context context, int theme, OnTimeSetListener callBack, int hourOfDay, int minute, boolean is24HourView) {
			super(context, theme, callBack, hourOfDay, minute, is24HourView);
		}

		public MyTimePickerDialog(Context context, OnTimeSetListener callBack, int hourOfDay, int minute, boolean is24HourView) {
			super(context, callBack, hourOfDay, minute, is24HourView);
		}

		@Override
		protected void onStop() {
		}
	}

//	Calendar calendar = Calendar.getInstance();
//
//	private void setDateTime(final View v, final int flag) {
//		if (flag == 1) {// 开始
//			calendar.setTimeInMillis(mMainApp.getAppData().getmNotDisturbStartTimes());
//		} else if (flag == 2) {// 结束
//			calendar.setTimeInMillis(mMainApp.getAppData().getmNotDisturbEndTimes());
//		}
//
//		timePickDlg = new MyTimePickerDialog(UserSettingActivity.this, new TimePickerDialog.OnTimeSetListener() {
//			@Override
//			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//				String time = getTimeFormat(hourOfDay, minute);
//				calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
//				calendar.set(Calendar.MINUTE, minute);
//				if (flag == 1) {
//					mSettingSelectStartTimesTv.setText(time);
//					mMainApp.getAppData().setmNotDisturbStartTimes(calendar.getTimeInMillis());
//				} else {
//					mSettingSelectEndTimesTv.setText(time);
//					mMainApp.getAppData().setmNotDisturbEndTimes(calendar.getTimeInMillis());
//				}
//				timePickDlg.dismiss();
//			}
//		}, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
//		timePickDlg.show();
//	}

	/**
	 * 格式化输出时间
	 * 
	 * @param hourOfDay
	 * @param minute
	 * @return
	 */
	private String getTimeFormat(int hourOfDay, int minute) {
		String time = hourOfDay + ":" + minute;
		if ((hourOfDay + "").length() == 1) {
			time = "0" + hourOfDay + ":" + minute;
		}
		if ((minute + "").length() == 1) {
			time = hourOfDay + ":" + "0" + minute;
		}
		if ((hourOfDay + "").length() == 1 && (minute + "").length() == 1) {
			time = "0" + hourOfDay + ":" + "0" + minute;
		}
		return time;
	}

	/* 1 允许非好友浏览我的主页 2允许非好友对我评价" */
	private IBindData mBindData = new IBindData() {
		@Override
		public void bindData(int tag, Object object) {
			UserSettingActivity.this.dismissLoadingDialog();
			if (tag == EAPIConsts.ReqType.UPDATE_USER_CONFIG && null != object) {
				DataBox dataBox = (DataBox) object;
				if (null != dataBox && dataBox.mUpdateSuccess) {
					if ("1".equals(reqTypeStr)) {
						mMainApp.getAppData().setmBrowseHomepageType(mUserSettingBrowsingHomepageCb.isChecked() ? 2 : 1);
					} else if ("2".equals(reqTypeStr)) {
						mMainApp.getAppData().setmFriendsAppraiseType(mUserSettingEvaluationCb.isChecked() ? 2 : 1);
					}
				} else {
					Toast.makeText(UserSettingActivity.this, "修改失败", 1).show();
				}
			}
		}
	};

	private Dialog dialog;

	/**
	 * @deprecated 状态change访问服务
	 * @param //type1-允许非好友浏览我的主页 2-允许非好友对我评价 sign 1-不允许 2-允许
	 */
	@Override
	public void onPause() {
		super.onPause();
	}

	private class NumberAdapter extends BaseAdapter {
		int mHeight = 50;
		String[] mData = null;

		public NumberAdapter(String[] data) {
			mHeight = (int) Utils.dipToPx(UserSettingActivity.this, mHeight);
			this.mData = data;
		}

		@Override
		public int getCount() {
			return (null != mData) ? mData.length : 0;
		}

		@Override
		public View getItem(int arg0) {
			return getView(arg0, null, null);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			WheelTextView textView = null;

			if (null == convertView) {
				convertView = new WheelTextView(UserSettingActivity.this);
				convertView.setLayoutParams(new TosGallery.LayoutParams(-1, mHeight));
				textView = (WheelTextView) convertView;
				if (mData.length > 24 && position == calendar.get(Calendar.MINUTE)) {
					textView.setTextSize(28);
					textView.setTextColor(Color.BLACK);
				} else if (mData.length <= 24 && position == calendar.get(Calendar.HOUR_OF_DAY)) {
					textView.setTextSize(28);
					textView.setTextColor(Color.BLACK);
				} else {
					textView.setTextSize(25);
				}
				textView.setGravity(Gravity.CENTER);
			}

			String text = mData[position];
			if (null == textView) {
				textView = (WheelTextView) convertView;
			} else {
				textView.setText(text);
			}
			return convertView;
		}
	}

	/**
	 * 设置 开始时间 -- 结束时间
	 * 
	 * @param context
	 */
	public void showSelectTimePopupWindow(final Context context) {

		View view = View.inflate(context, R.layout.setting_select_time, null);
		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);
		popupWindow.setAnimationStyle(R.style.PupwindowAnimation);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);

		mStartHours = (WheelView) view.findViewById(R.id.startSelectHourTime);
		mStartMins = (WheelView) view.findViewById(R.id.startSelectMinsTime);
		mEndHours = (WheelView) view.findViewById(R.id.endSelectHourTime);
		mEndMins = (WheelView) view.findViewById(R.id.endSelectMinsTime);

		mStartHours.setScrollCycle(true);
		mStartMins.setScrollCycle(true);
		mEndHours.setScrollCycle(true);
		mEndMins.setScrollCycle(true);

		hourAdapter = new NumberAdapter(hoursArray);
		minAdapter = new NumberAdapter(minsArray);

		if (mMainApp.getAppData().getmNotDisturbStartTimes() == 0) {
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 0);
		} else {
			calendar.setTimeInMillis(mMainApp.getAppData()
					.getmNotDisturbStartTimes());
		}
		mStartHours.setAdapter(hourAdapter);
		mStartMins.setAdapter(minAdapter);
		mStartHours.setSelection(calendar.get(Calendar.HOUR_OF_DAY), true);
		mStartMins.setSelection(calendar.get(Calendar.MINUTE),true);

		if (mMainApp.getAppData().getmNotDisturbEndTimes() == 0) {
			calendar.set(Calendar.HOUR_OF_DAY, 8);
			calendar.set(Calendar.MINUTE, 0);
		} else {
			calendar.setTimeInMillis(mMainApp.getAppData()
					.getmNotDisturbEndTimes());
		}
		mEndHours.setAdapter(hourAdapter);
		mEndMins.setAdapter(minAdapter);
		mEndHours.setSelection(calendar.get(Calendar.HOUR_OF_DAY), true);
		mEndMins.setSelection(calendar.get(Calendar.MINUTE), true);

		mStartHours.setOnItemSelectedListener(mListener);
		mStartMins.setOnItemSelectedListener(mListener);
		mEndHours.setOnItemSelectedListener(mListener);
		mEndMins.setOnItemSelectedListener(mListener);

		mStartHours.setUnselectedAlpha(0.5f);
		mStartMins.setUnselectedAlpha(0.5f);
		mEndHours.setUnselectedAlpha(0.5f);
		mEndMins.setUnselectedAlpha(0.5f);
		
		((WheelTextView) mStartHours.getSelectedView()).setTextSize(28);
		((WheelTextView) mStartMins.getSelectedView()).setTextSize(28);
		((WheelTextView) mEndHours.getSelectedView()).setTextSize(28);
		((WheelTextView) mEndMins.getSelectedView()).setTextSize(28);

		TextView cancel = (TextView) view.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
			}
		});

		TextView finish = (TextView) view.findViewById(R.id.finish);

		finish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 开始时间
				Calendar calendar = Calendar.getInstance();
				int startHourOfDay = mStartHours.getSelectedItemPosition();
				int startMinuteMinute = mStartMins.getSelectedItemPosition();

				String startTime = getTimeFormat(startHourOfDay,
						startMinuteMinute);
				calendar.set(Calendar.HOUR_OF_DAY, startHourOfDay);
				calendar.set(Calendar.MINUTE, startMinuteMinute);
				mSettingSelectStartTimesTv.setText(startTime);
				mMainApp.getAppData().setmNotDisturbStartTimes(
						calendar.getTimeInMillis());

				// 结束时间
				int endHourOfDay = mEndHours.getSelectedItemPosition();
				int endMinuteMinute = mEndMins.getSelectedItemPosition();
				String endTime = getTimeFormat(endHourOfDay, endMinuteMinute);
				calendar.set(Calendar.HOUR_OF_DAY, endHourOfDay);
				calendar.set(Calendar.MINUTE, endMinuteMinute);
				mSettingSelectEndTimesTv.setText(endTime);
				mMainApp.getAppData().setmNotDisturbEndTimes(
						calendar.getTimeInMillis());

				if (popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
			}
		});
		popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
	}

	private TosAdapterView.OnItemSelectedListener mListener = new TosAdapterView.OnItemSelectedListener() {
		@Override
		public void onItemSelected(TosAdapterView<?> parent, View view, int position, long id) {
			((WheelTextView) view).setTextSize(28);
			((WheelTextView) view).setTextColor(Color.BLACK);
			int index = Integer.parseInt(view.getTag().toString());
			int count = parent.getChildCount();
			if (index < count - 1) {
				((WheelTextView) parent.getChildAt(index + 1)).setTextSize(25);
			}
			if (index > 0) {
				((WheelTextView) parent.getChildAt(index - 1)).setTextSize(25);
			}
		}

		@Override
		public void onNothingSelected(TosAdapterView<?> parent) {
		}
	};

	private CheckBox mUserSettingDynamicPush;

	private boolean isShowCreateButton;

	private SharedPreferences sp;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting_demand_previewRl:// 上传/浏览视频
			View view = View.inflate(UserSettingActivity.this, R.layout.demand_network_state_dialog, null);
			view.findViewById(R.id.allTv).setOnClickListener(this);
			view.findViewById(R.id.OnlyWifiTv).setOnClickListener(this);
			view.findViewById(R.id.closeTv).setOnClickListener(this);
			showDialog(view);
			break;
		case R.id.setting_demand_upload_apkRl:// 自动下载安装包
			View upView = View.inflate(UserSettingActivity.this, R.layout.demand_network_update_dialog, null);
			upView.findViewById(R.id.OnlyWifiUpdateTv).setOnClickListener(this);
			upView.findViewById(R.id.neverTv).setOnClickListener(this);
			showDialog(upView);
			break;
		case R.id.allTv:// android:text="3G/4G和WI-FI" 上传浏览视频
			mSettingPreviewTv.setText(previewVideo[2]);
			mMainApp.getAppData().setPreviewVideo(2);
			dialog.dismiss();
			break;
		case R.id.OnlyWifiTv:// 仅WI-FI
			mSettingPreviewTv.setText(previewVideo[1]);
			mMainApp.getAppData().setPreviewVideo(1);
			dialog.dismiss();
			break;
		case R.id.closeTv:// 关闭
			mSettingPreviewTv.setText(previewVideo[0]);
			mMainApp.getAppData().setPreviewVideo(0);
			dialog.dismiss();
			break;
		case R.id.OnlyWifiUpdateTv:
			mSettingAutomaticUpdatesTv.setText(R.string.automati_updates2);
			mMainApp.getAppData().setAutomatiUpdates(true);
			dialog.dismiss();
			break;
		case R.id.neverTv:
			mSettingAutomaticUpdatesTv.setText(R.string.automati_updates1);
			mMainApp.getAppData().setAutomatiUpdates(false);
			dialog.dismiss();
			break;
		case R.id.feedbackTv:
			ENavigate.startOnekeyBackActivity(this);
			break;
		}
	}
}
