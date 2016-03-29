package com.tongmeng.alliance.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tongmeng.alliance.dao.ServerResultDao;
import com.tongmeng.alliance.util.Constant;
import com.tongmeng.alliance.util.HttpRequestUtil;
import com.tongmeng.alliance.util.Utils;
import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.utils.log.KeelLog;

public class CancleApplyActivity extends JBaseActivity {

	private ImageView search;
	private TextView myTitle;
	private TextView create_Tv;

	private TextView numTv;
	private EditText editEt;
	private String activityId;
	private Map<String, String> map = new HashMap<String, String>();

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(CancleApplyActivity.this, msg.obj+"", 0).show();
				break;
			case 1:
				Toast.makeText(CancleApplyActivity.this, "数据发送成功！", 0).show();
				finish();
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
		setContentView(R.layout.activity_action_cancleapply);
		activityId = getIntent().getStringExtra("activityId");
		KeelLog.e(TAG, "activityId::" + activityId);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		editEt = (EditText) findViewById(R.id.acivity_action_cancleapply_edit);
		numTv = (TextView) findViewById(R.id.acivity_action_cancleapply_numText);
		editEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				String str = editEt.getText().toString();
				if (str.length() <= 200) {
					numTv.setText(str.length());
				} else {
					Toast.makeText(CancleApplyActivity.this, "您只能输入200个汉字", 0)
							.show();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
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
		myTitle.setText("取消报名");
		create_Tv = (TextView) mCustomView.findViewById(R.id.create_Tv);
		create_Tv.setText("确定");
		create_Tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String textStr = editEt.getText().toString();
				if (textStr == null || "".equals(textStr)) {
					Toast.makeText(CancleApplyActivity.this, "请输入您取消报名的原因", 0)
							.show();
				} else {
					sendToServer(textStr);
				}
			}
		});
		search = (ImageView) mCustomView.findViewById(R.id.titleIv);
		search.setVisibility(View.GONE);
	}

	private void sendToServer(final String str) {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				String param = "{\"activityId\":\"" + activityId
						+ "\",\"reason\":\"" + str + "\"}";
				KeelLog.e(TAG, "param::" + param);
				String result = HttpRequestUtil.sendPost(
						Constant.cancleApplyPath, param,
						CancleApplyActivity.this);
				ServerResultDao dao = Utils.getServerResult(result);
				if (dao == null) {
					sendMessge(0, "数据发送失败");
				}else{
					if(dao.getNotifyCode().equals("0001")){
						if(dao.getResponseData() == null || "".equals(dao.getResponseData()) || "null".equals(dao.getResponseData())){
							sendMessge(0, "数据发送失败");
						}else{
							try {
								JSONObject job = new JSONObject(dao.getResponseData());
								boolean b = job.getBoolean("succeed");
								if(b){
									handler.sendEmptyMessage(1);
								}else{
									sendMessge(0, "数据发送失败");
								}
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
						}
					}else{
						sendMessge(0, "数据发送失败,失败原因:"+dao.getNotifyInfo());
					}
				}
			};
		}.start();
	}
	
	private void sendMessge(int what,String str){
		Message msg = new Message();
		msg.what = what;
		msg.obj = str;
		handler.sendMessage(msg);
	}
}
