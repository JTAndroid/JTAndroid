package com.tongmeng.alliance.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tongmeng.alliance.dao.ApplyForm;
import com.tongmeng.alliance.util.Constant;
import com.tongmeng.alliance.util.HttpRequestUtil;
import com.tongmeng.alliance.util.Utils;
import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.log.KeelLog;

public class ApplyVerifyActivity extends JBaseActivity implements OnClickListener{

	TextView themeText;
	String themeTitle, location, starttime, endtime, price, activityId;
	TextView titletext, starttimeText, endtimeText, locationText;
	EditText numText, verrfyText, nameText;
	Button payButton, authButton;
	private TimeCount timeNo;
	ApplyForm applyForm = new ApplyForm();// 报名表单

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(ApplyVerifyActivity.this, "验证码获取成功，请注意查看", 0)
						.show();
				break;
			case 1:
				Toast.makeText(ApplyVerifyActivity.this, "验证码获取失败，请稍后重试", 0)
						.show();
				break;
			case 2:
				Intent intent = new Intent(ApplyVerifyActivity.this,
						PayActivity.class);
				intent.putExtra("activityId", activityId);
				intent.putExtra("title", themeTitle);
				intent.putExtra("price", price);
				intent.putExtra("starttime", starttime);
				intent.putExtra("endtime", endtime);
				intent.putExtra("name", nameText.getText().toString());
				intent.putExtra("phoneNo", numText.getText().toString());
				startActivity(intent);
				finish();
				break;
			case 3:
				Toast.makeText(ApplyVerifyActivity.this, "验证码验证失败，请稍后重试", 0)
						.show();
				break;
			default:
				break;
			}
		}

	};
	
	@Override
	public void initJabActionBar() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_apply_verify);
		initTitle();
		activityId = getIntent().getStringExtra("activityId");
		themeTitle = getIntent().getStringExtra("title");
		location = getIntent().getStringExtra("location");
		starttime = getIntent().getStringExtra("starttime");
		endtime = getIntent().getStringExtra("endtime");
		price = getIntent().getStringExtra("price");
		initView();
		initValue();
	}
	
	public void initView() {

		themeText = (TextView) findViewById(R.id.activity_apply_verify_titleText);
		starttimeText = (TextView) findViewById(R.id.activity_apply_verify_action_startTimeText);
		endtimeText = (TextView) findViewById(R.id.activity_apply_verify_action_endTimeText);
		locationText = (TextView) findViewById(R.id.activity_apply_verify_actionLocationText);

		themeText.setText(themeTitle);
		starttimeText.setText(starttime);
		endtimeText.setText(endtime);
		locationText.setText(location);

		numText = (EditText) findViewById(R.id.activity_apply_verify_phoneEdittext);
		verrfyText = (EditText) findViewById(R.id.activity_apply_verify_authCodeEdit);
		nameText = (EditText) findViewById(R.id.activity_apply_verify_nameEdit);

		authButton = (Button) findViewById(R.id.activity_apply_verify_authCodeBtn);
		payButton = (Button) findViewById(R.id.activity_apply_verify_payBtn);

		authButton.setOnClickListener(this);
		payButton.setOnClickListener(this);

		timeNo = new TimeCount(30000, 1000, authButton, "发送验证码");
	}

	public void initValue(){
		titletext.setText(themeTitle);
		starttimeText.setText(starttime);
		endtimeText.setText(endtime);
		locationText.setText(location);
	}
	

	private void initTitle() {
		// TODO Auto-generated method stub
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "验证手机号",
				false, null, false, true);
	}
	
	class TimeCount extends CountDownTimer {
		Button btn_reget_captcha;
		String str;
	    public TimeCount(long millisInFuture, long countDownInterval,Button button,String str) {
	        super(millisInFuture, countDownInterval);
	        this.btn_reget_captcha = button;
	        this.str = str;
	    }

	    public void onFinish() {
	        btn_reget_captcha.setText(str);
	        btn_reget_captcha.setClickable(true);
	    }

	    public void onTick(long millisUntilFinished) {
	        btn_reget_captcha.setClickable(false);
	        btn_reget_captcha.setText("在" + millisUntilFinished / 1000 + "秒后可重新发送");
	    }
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.activity_apply_verify_authCodeBtn:
			if (TextUtils.equals(numText.getText().toString(), "")
					|| TextUtils.equals(numText.getText().toString(), null)) {
				Toast.makeText(this, "请输入您的手机号或邮箱", 0).show();
			} else {
				String phoneNo = numText.getText().toString();
				if (authButton.isClickable()) {
					timeNo.start();
				}
				new Thread() {
					public void run() {
						Map<String, String> map = new HashMap<String, String>();
						map.put("mobile", numText.getText().toString());
						map.put("activityId", activityId);
						String param = Utils.simpleMapToJsonStr(map);
						Log.e("", "param:" + param);
						String result = HttpRequestUtil.sendPost(Constant.applySendCodePath, param,
								ApplyVerifyActivity.this);
						boolean result1 = getApplyResult(result);
						if (result1) {
							handler.sendEmptyMessage(0);
						} else {
							handler.sendEmptyMessage(1);
						}
						Log.e("", "result1:" + result1);
						Log.e("", "发送验证码结果：" + (result1 ? "成功" : "失败"));
					};
				}.start();
			}

			break;
		case R.id.activity_apply_verify_payBtn:
			// 验证码成功

			if (numText.getText().toString() == null
					|| "".equals(numText.getText().toString())) {
				Toast.makeText(this, "请输入您的手机号", 0).show();
			} else if (verrfyText.getText().toString() == null
					|| "".equals(verrfyText.getText().toString())) {
				Toast.makeText(this, "请输入您的验证码", 0).show();
			} else if (nameText.getText().toString() == null
					|| "".equals(nameText.getText().toString())) {
				Toast.makeText(this, "请输入您的姓名", 0).show();
			} else {
				new Thread() {
					public void run() {
						Map<String, String> map = new HashMap<String, String>();
						map.put("mobile", numText.getText().toString());
						map.put("activityId", activityId);
						map.put("code", verrfyText.getText().toString());
						String param = Utils.simpleMapToJsonStr(map);
						KeelLog.e("", "param:" + param);
						String result = HttpRequestUtil.sendPost(Constant.applyVerifyPath, param,
								ApplyVerifyActivity.this);
						KeelLog.e("", "result::" + result);
						boolean result1 = getApplyResult(result);
						KeelLog.e("", "result1:" + result1);
						KeelLog.e("", "手机验证的结果是：" + (result1 ? "成功" : "失败"));
						if (result1) {
							handler.sendEmptyMessage(2);
						} else {
							handler.sendEmptyMessage(3);
						}
					};
				}.start();
			}

		default:
			break;
		}
	}
	
	/**
	 * 获取报名结果
	 * 
	 * @param result
	 * @return
	 */
	public  boolean getApplyResult(String result) {
		Log.e("", "报名结果返回：" + getContent(result));
		try {
			JSONObject obj = new JSONObject(getContent(result));
			Log.e("", "报名结果：" + obj.getBoolean("succeed"));
			return obj.getBoolean("succeed");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 调用结果成功则获取返回数据中 responseData的数据，失败返回提示信息
	 * 
	 * @param result
	 * @return
	 */
	public  String getContent(String result) {
		try {
			JSONObject obj = new JSONObject(result);
			String notification = obj.getString("notification");
			if (TextUtils.equals("0001",
					new JSONObject(notification).getString("notifyCode"))) {
				return obj.getString("responseData");
			} else {
				return new JSONObject(notification).getString("notifyInfo");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
