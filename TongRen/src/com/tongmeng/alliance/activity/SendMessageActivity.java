package com.tongmeng.alliance.activity;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tongmeng.alliance.dao.ServerResultDao;
import com.tongmeng.alliance.util.Constant;
import com.tongmeng.alliance.util.HttpRequestUtil;
import com.tongmeng.alliance.util.Utils;
import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.utils.log.KeelLog;

public class SendMessageActivity extends JBaseActivity implements
		OnClickListener {

	private ImageView search;
	private TextView myTitle;
	private TextView create_Tv;

	// 界面
	TextView contentText, modeText, objText;
	RelativeLayout modeLayout, objLayout;

	String objCode, modeCode, activityId;
	Message message;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(SendMessageActivity.this, "消息发送成功！", 0).show();
				finish();
				break;
			case 1:
				Toast.makeText(SendMessageActivity.this, msg.obj + "", 0)
						.show();
				break;
			case 2:

				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sendmessage);
		activityId = getIntent().getStringExtra("activityId");
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		// 界面
		modeLayout = (RelativeLayout) findViewById(R.id.sendmessage_chooseModeLayout);
		modeText = (TextView) findViewById(R.id.sendmessage_chooseModeText);
		objLayout = (RelativeLayout) findViewById(R.id.sendmessage_chooseObjLayout);
		objText = (TextView) findViewById(R.id.sendmessage_chooseObjText);

		modeLayout.setOnClickListener(this);
		objLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sendmessage_chooseModeLayout:
			Intent modeIntent = new Intent(this, ChooseModeActivity.class);
			startActivityForResult(modeIntent, 0);
			break;
		case R.id.sendmessage_chooseObjLayout:
			Intent objIntent = new Intent(this, ChooseObjActivity.class);
			startActivityForResult(objIntent, 1);
			break;
		default:
			break;
		}
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
		myTitle.setText("通知");
		create_Tv = (TextView) mCustomView.findViewById(R.id.create_Tv);
		create_Tv.setText("发送");
		create_Tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (modeText.getText().toString() == null
						|| "".equals(modeText.getText().toString())) {
					Toast.makeText(SendMessageActivity.this, "请选择模版", 0).show();
					return;
				} else if (objText.getText().toString() == null
						|| "".equals(objText.getText().toString())) {
					Toast.makeText(SendMessageActivity.this, "请选择发送对象", 0).show();
					return;
				} else {
					new Thread() {
						public void run() {
							String param = "{\"activityId\":\"" + activityId
									+ "\",\"type\":\"" + modeCode
									+ "\",\"target\":\"" + objCode + "\"}";
							KeelLog.e(TAG, "send message param :::" + param);

							String result = HttpRequestUtil.sendPost(
									Constant.sendMessagePath, param,
									SendMessageActivity.this);
							KeelLog.e(TAG, "send message result :::" + result);
							message = new Message();
							ServerResultDao dao = Utils.getServerResult(result);
							if (dao.getNotifyCode().equals("0001")) {
								boolean a = (dao.getResponseData() != null);
								boolean b = (!"".equals(dao.getResponseData()));
								boolean c = ("null".equals(dao.getResponseData()));
								
								if (dao.getResponseData() != null
										&& !"".equals(dao.getResponseData())
										&& !"null".equals(dao.getResponseData())) {
									String isSuccess = getResult(dao
											.getResponseData());
									if (isSuccess.equals("true")) {
										handler.sendEmptyMessage(0);
									} else {
										message.what = 1;
										message.obj = "发送通知消息失败，请重新发送";
										handler.sendMessage(message);
									}
								} else {
									message.what = 1;
									message.obj = "发送通知消息失败，请重新发送";
									handler.sendMessage(message);
								}
							} else {
								message.what = 1;
								message.obj = "发送通知消息失败，失败原因："
										+ dao.getNotifyInfo() + ",请重新发送";
								handler.sendMessage(message);
							}
						};
					}.start();
				}
			}
		});
		search = (ImageView) mCustomView.findViewById(R.id.titleIv);
		search.setVisibility(View.GONE);
	}

	public String getResult(String result) {
		try {
			JSONObject job = new JSONObject(result);
			KeelLog.e(TAG, "getResult   succeed "+job.getString("succeed"));
			return job.getString("succeed");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == 0) {// 返回模版信息
			String modeStr = data.getStringExtra("mode");
			modeCode = data.getStringExtra("code");
			KeelLog.e("", "模版界面返回数据:::" + modeStr);
			if (modeStr != null && !"".equals(modeStr)) {
				modeText.setText(modeStr);
			}
		} else if (resultCode == RESULT_OK && requestCode == 1) {// 返回对象信息
			String objStr = data.getStringExtra("obj");
			KeelLog.e("", "模版界面返回数据:::" + objStr);
			objCode = data.getStringExtra("code");
			if (objStr != null && !"".equals(objStr)) {
				objText.setText(objStr);
			}
		}
	}
}
