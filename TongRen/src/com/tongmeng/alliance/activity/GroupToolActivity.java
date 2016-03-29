package com.tongmeng.alliance.activity;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tongmeng.alliance.dao.ServerResultDao;
import com.tongmeng.alliance.util.Constant;
import com.tongmeng.alliance.util.HttpRequestUtil;
import com.tongmeng.alliance.util.Utils;
import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.log.KeelLog;

public class GroupToolActivity extends JBaseActivity implements OnClickListener {

	// 界面控件
	TextView signNoText, allNoText, notsignNoText;
	ImageView actiondetailImg, meetingnoteImg, memberManagerImg, sendInformImg;
	Button signBtn;
	String activityID, role;
	int unsignedCount, signInCount, totalCount;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:// 获取签到信息成功
				signNoText.setText(signInCount + "");
				allNoText.setText(totalCount + "");
				notsignNoText.setText(unsignedCount + "");
				break;
			case 1:// 获取签到信息失败
				Toast.makeText(GroupToolActivity.this, msg.obj + "", 0).show();
				break;
			}
		};
	};

	public void initView() {

		signNoText = (TextView) findViewById(R.id.grouptool_signNo);
		allNoText = (TextView) findViewById(R.id.grouptool_allNo);
		notsignNoText = (TextView) findViewById(R.id.grouptool_noSignNo);
		actiondetailImg = (ImageView) findViewById(R.id.grouptool_actiondetailImg);
		meetingnoteImg = (ImageView) findViewById(R.id.grouptool_meetingnotImg);
		memberManagerImg = (ImageView) findViewById(R.id.grouptool_peopleManagerImg);
		sendInformImg = (ImageView) findViewById(R.id.grouptool_sendInformImg);
		signBtn = (Button) findViewById(R.id.grouptool_signBtn);

		actiondetailImg.setOnClickListener(this);
		meetingnoteImg.setOnClickListener(this);
		memberManagerImg.setOnClickListener(this);
		sendInformImg.setOnClickListener(this);
		signBtn.setOnClickListener(this);
	}

	public void initValue() {

		new Thread() {
			public void run() {
				String param = "{\"activityId\":\"" + activityID + "\"}";
				KeelLog.e(TAG, "getSignInCount param ::" + param);
				String result = HttpRequestUtil.sendPost(Constant.signedNopath,
						param, GroupToolActivity.this);
				KeelLog.e(TAG, "grouptoolsActivity   result:::" + result);
				ServerResultDao dao = Utils.getServerResult(result);
				if (dao.getNotifyCode().equals("0001")) {
					if (dao.getResponseData() != null
							&& !"".equals(dao.getResponseData())
							&& !"null".equals(dao.getResponseData())) {
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
					message.obj = "获取签到信息失败" + dao.getNotifyInfo();
					handler.sendMessage(message);
				}
			};
		}.start();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.grouptool_actiondetailImg:// 活动详情
			Intent actionIntent = new Intent(GroupToolActivity.this,
					ActionDetailActivity.class);
			actionIntent.putExtra("activityId", activityID);
			actionIntent.putExtra("source", "");
			startActivity(actionIntent);
			break;
		case R.id.grouptool_meetingnotImg:// 会议笔记
			Intent noteIntent = new Intent(GroupToolActivity.this,
					ActionNoteActivity.class);
			noteIntent.putExtra("activityId", activityID);
			noteIntent.putExtra("role", role);
			startActivity(noteIntent);
			break;
		case R.id.grouptool_peopleManagerImg:// 成员管理
			Intent peopleIntent = new Intent(GroupToolActivity.this,
					PeopleManagerActivity.class);
			peopleIntent.putExtra("activityId", activityID);
			peopleIntent.putExtra("role", role);
			startActivity(peopleIntent);
			break;
		case R.id.grouptool_sendInformImg:// 发通知
			Intent sendIntent = new Intent(GroupToolActivity.this,
					SendMessageActivity.class);
			sendIntent.putExtra("activityId", activityID);
			sendIntent.putExtra("role", role);
			startActivity(sendIntent);
			break;
		case R.id.grouptool_signBtn:// 签到
			Intent signIntent = new Intent(GroupToolActivity.this,
					SignAcivity.class);
			signIntent.putExtra("activityId", activityID);
			signIntent.putExtra("role", role);
			signIntent.putExtra("index", "group");
			startActivity(signIntent);
			break;

		default:
			break;
		}
	}

	@Override
	public void initJabActionBar() {
		// TODO Auto-generated method stub
		setContentView(R.layout.grouptool);
		initTitle();
		activityID = getIntent().getStringExtra("activityID");
		role = getIntent().getStringExtra("role");
		KeelLog.e("", "activityID::" + activityID);
		KeelLog.e("", "role::" + role);

		initView();
		initValue();
	}

	private void initTitle() {
		// TODO Auto-generated method stub
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "群工具",
				false, null, false, true);
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
}
