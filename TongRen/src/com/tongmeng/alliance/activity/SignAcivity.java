package com.tongmeng.alliance.activity;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tongmeng.alliance.dao.ServerResultDao;
import com.tongmeng.alliance.util.Constant;
import com.tongmeng.alliance.util.HttpRequestUtil;
import com.tongmeng.alliance.util.SoftKeyboardUtil;
import com.tongmeng.alliance.util.SoftKeyboardUtil.OnSoftKeyboardChangeListener;
import com.tongmeng.alliance.util.Utils;
import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.utils.log.KeelLog;

public class SignAcivity extends JBaseActivity implements OnClickListener,OnSoftKeyboardChangeListener {

	private ImageView search;
	private TextView myTitle;
	private TextView create_Tv;
	// 界面控件
	RelativeLayout relativeLayout;
	EditText edittext;
	TextView makesureText, signNoText, allNOText, notSignText;
	int heightDifference;
	boolean isShow = false;
	Button signBtn;
	ImageView scanImg;
	LinearLayout failLayout, sucLayout;

	String activityId, role, index;
	int unsignedCount, signInCount, totalCount;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign);
		initView();

		index = getIntent().getStringExtra("index");
		role = getIntent().getStringExtra("role");
		activityId = getIntent().getStringExtra("activityId");
		KeelLog.e(TAG, "activityId::" + activityId);
		KeelLog.e(TAG, "role::" + role);
		KeelLog.e(TAG, "index::" + index);

		if (index.equals("group")) {//从群工具跳转过来，获取数据
			
		} else {// 直接设置成功

			if (sucLayout.getVisibility() == View.GONE) {
				sucLayout.setVisibility(View.VISIBLE);
			}

			if (role.equals("2")) {// 是群主,提示成功

			} else {// 是群成员（扫一扫默认是群成员），提示成功，输入框隐藏
				if (relativeLayout.getVisibility() == View.VISIBLE) {
					relativeLayout.setVisibility(View.GONE);
				}
			}
		}
	}
	
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:// 获取签到信息成功
				signNoText.setText(signInCount + "");
				allNOText.setText(totalCount + "");
				notSignText.setText(unsignedCount + "");
				break;
			case 1:// 获取签到信息失败
				Toast.makeText(SignAcivity.this, msg.obj + "", 0).show();
				break;
			case 2:
				if (failLayout.getVisibility() == View.VISIBLE) {
					failLayout.setVisibility(View.GONE);
				}
				if (sucLayout.getVisibility() == View.GONE) {
					sucLayout.setVisibility(View.VISIBLE);
				}

				signNoText.setText((signInCount + 1) + "");
				notSignText.setText((unsignedCount - 1) + "");

				if (role.equals("2")) {// 是群主，还可继续帮别人签到

				} else {// 是成员，提示签到成功，输入框隐藏
					if (relativeLayout.getVisibility() == View.VISIBLE) {
						relativeLayout.setVisibility(View.GONE);
					}
					signBtn.setText("已签到");
				}

				break;
			case 3:
				if (failLayout.getVisibility() == View.GONE) {
					failLayout.setVisibility(View.VISIBLE);
				}
				if (sucLayout.getVisibility() == View.VISIBLE) {
					sucLayout.setVisibility(View.GONE);
				}
				edittext.clearFocus();
				edittext.setFocusable(false);
				InputMethodManager imm = (InputMethodManager) getSystemService(SignAcivity.this.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(edittext.getWindowToken(), 0);
				// allNOText.setFocusable(true);
				break;

			default:
				break;
			}
		};
	};
	
	public void initValue() {
		new Thread() {
			public void run() {
				String param = "{\"activityId\":\"" + activityId + "\"}";
				KeelLog.e(TAG, "getSignInCount param ::" + param);
				String result = HttpRequestUtil.sendPost(Constant.signedNopath,
						param, SignAcivity.this);
				KeelLog.e(TAG, "grouptoolsActivity   result:::" + result);
				ServerResultDao dao = Utils.getServerResult(result);
				if (dao == null) {
					Message message = new Message();
					message.what = 1;
					message.obj = "获取签到信息失败";
					handler.sendMessage(message);
				} else {
					if (dao.getNotifyCode().equals("0001")) {
						if (dao.getResponseData() != null
								&& !"".equals(dao.getResponseData())
								|| !"null".equals(dao.getResponseData())) {
							boolean isgetSignNo = getSignCount(dao
									.getResponseData());
							if (isgetSignNo) {
								handler.sendEmptyMessage(0);
							} else {
								Message message = new Message();
								message.what = 1;
								message.obj = "获取签到信息失败";
								handler.sendMessage(message);
							}
						} else {
							Message message = new Message();
							message.what = 1;
							message.obj = "获取签到信息失败";
							handler.sendMessage(message);
						}
					} else {
						Message message = new Message();
						message.what = 1;
						message.obj = "获取签到信息失败，失败原因：" + dao.getNotifyInfo();
						handler.sendMessage(message);
					}
				}
			};
		}.start();
	}

	public void initView() {

		signBtn = (Button) findViewById(R.id.sign_signBtn);
		signNoText = (TextView) findViewById(R.id.sign_signNo);
		allNOText = (TextView) findViewById(R.id.sign_allNo);
		notSignText = (TextView) findViewById(R.id.sign_noSignNo);
		scanImg = (ImageView) findViewById(R.id.sign_scanImg);
		scanImg.setOnClickListener(this);
		relativeLayout = (RelativeLayout) findViewById(R.id.sign_signLayout);
		edittext = (EditText) findViewById(R.id.sign_signEditText);
		failLayout = (LinearLayout) findViewById(R.id.sign_signfailLayout);
		sucLayout = (LinearLayout) findViewById(R.id.sign_signsuccessLayout);
		makesureText = (TextView) findViewById(R.id.sign_makeSureSign);
		makesureText.setOnClickListener(this);
		SoftKeyboardUtil
				.observeSoftKeyboard(SignAcivity.this, SignAcivity.this);
	}

	@SuppressLint("NewApi")
	@Override
	public void onSoftKeyBoardChange(int softKeybardHeight, boolean visible) {
		// TODO Auto-generated method stub
		if (!isShow && visible) {
			makesureText.setVisibility(View.VISIBLE);
			if (failLayout.getVisibility() == View.VISIBLE) {
				failLayout.setVisibility(View.GONE);
			}
			if (sucLayout.getVisibility() == View.VISIBLE) {
				sucLayout.setVisibility(View.GONE);
			}
			Log.e("", "加载完成");
			isShow = true;
		} else if (!visible) {
			makesureText.setVisibility(View.GONE);
			isShow = false;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sign_makeSureSign:
			if (edittext.getText().toString() == null
					|| "".equals(edittext.getText().toString())) {
				Toast.makeText(SignAcivity.this, "请输入您的签到码", 0).show();
			} else {
				new Thread() {
					public void run() {
						String param = "{\"activityId\":\"" + activityId
								+ "\",\"code\":\""
								+ edittext.getText().toString() + "\"}";
						KeelLog.e(TAG, "onClick  param::" + param);
						String result = HttpRequestUtil.sendPost(
								Constant.signPath, param, SignAcivity.this);
						KeelLog.e(TAG, "onClick  result::" + result);
						ServerResultDao dao = Utils.getServerResult(result);
						if (dao == null) {
							handler.sendEmptyMessage(3);
						} else {
							if (dao.getNotifyCode().equals("0001")) {
								if (dao.getResponseData() != null
										&& !"".equals(dao.getResponseData())
										&& !"null"
												.equals(dao.getResponseData())) {
									boolean isSigned = signResult(dao
											.getResponseData());
									if (isSigned) {
										handler.sendEmptyMessage(2);
									} else {
										handler.sendEmptyMessage(3);
									}
								} else {
									handler.sendEmptyMessage(3);
								}
							} else {
								handler.sendEmptyMessage(3);
							}
						}
					};
				}.start();
			}
			break;

		case R.id.sign_scanImg:
			Intent intent = new Intent(SignAcivity.this, ActionScanActivity.class);
			intent.putExtra("activityId", activityId);
			intent.putExtra("index", "sign");
			intent.putExtra("role", role);
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}
	}
	
	public boolean getSignCount(String result) {
		KeelLog.e(TAG, "getSignCount  result::" + result);
		try {
			JSONObject job = new JSONObject(result);
			unsignedCount = job.getInt("unsignedCount");
			signInCount = job.getInt("signInCount");
			totalCount = job.getInt("totalCount");
			KeelLog.e(TAG, "getSignCount  unsignedCount::" + unsignedCount);
			KeelLog.e(TAG, "getSignCount  signInCount::" + signInCount);
			KeelLog.e(TAG, "getSignCount  totalCount::" + totalCount);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// 签到
	public boolean signResult(String result) {
		KeelLog.e(TAG, "signResult result::" + result);
		try {
			JSONObject job = new JSONObject(result);
			return job.getBoolean("succeed");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void initJabActionBar() {
		// TODO Auto-generated method stub
		KeelLog.e("ContactsMainPageActivity", "initJabActionBar");
		ActionBar mActionBar = jabGetActionBar();
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(true);
		View mCustomView = getLayoutInflater().inflate(
				R.layout.org_firstpage_actionbar_title, null);
		mActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView
				.getLayoutParams();
		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK
				| Gravity.CENTER_HORIZONTAL;
		mActionBar.setCustomView(mCustomView, mP);
		mActionBar.setTitle(" ");
		myTitle = (TextView) mCustomView.findViewById(R.id.titleTv);
		myTitle.setText("活动签到");
		create_Tv = (TextView) mCustomView.findViewById(R.id.create_Tv);
		create_Tv.setVisibility(View.GONE);
		search = (ImageView) mCustomView.findViewById(R.id.titleIv);
		search.setVisibility(View.GONE);
	}

}
