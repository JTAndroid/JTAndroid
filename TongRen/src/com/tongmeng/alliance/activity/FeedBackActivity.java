package com.tongmeng.alliance.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import com.tongmeng.alliance.R;
//import com.tongmeng.alliance.util.Constant;
//import com.tongmeng.alliance.util.HttpRequestUtil;
//import com.tongmeng.alliance.util.JSONUtil;
//import com.tongmeng.alliance.util.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FeedBackActivity extends Activity implements OnClickListener {
	// 意见反馈

	// 头部
	ImageView backImg, moreImg;
	TextView titleText;

	// 界面
	// ImageView functionImg;
	RelativeLayout functionchooseLayout;
	TextView noText, typeText;
	EditText contentText, phoneText;
	Button submitBtn;
	String key, value;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(FeedBackActivity.this, "提交成功！！", 0).show();
				finish();
				break;
			case 1:
				Toast.makeText(FeedBackActivity.this, "提交失败，请重试", 0).show();
				break;
			default:
				break;
			}
		};
	};

//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_feedback);
//
//		backImg = (ImageView) findViewById(R.id.index_top_seartchImg);
//		moreImg = (ImageView) findViewById(R.id.index_top_moreImg);
//		titleText = (TextView) findViewById(R.id.index_top_nameText);
//		backImg.setBackgroundResource(R.drawable.back);
//		moreImg.setVisibility(View.GONE);
//		titleText.setText("意见反馈");
//		backImg.setOnClickListener(this);
//
//		functionchooseLayout = (RelativeLayout) findViewById(R.id.activity_feeabadk_enterLayout);
//		typeText = (TextView) findViewById(R.id.activity_feeabadk_typeText);
//
//		noText = (TextView) findViewById(R.id.activity_feedback_noText);
//		contentText = (EditText) findViewById(R.id.activity_feedback_contentEditText);
//		phoneText = (EditText) findViewById(R.id.activity_feedback_phoneEditText);
//		submitBtn = (Button) findViewById(R.id.activity_feedback_submitBtn);
//		submitBtn.setOnClickListener(this);
//		functionchooseLayout.setOnClickListener(this);
//
//		contentText.addTextChangedListener(new TextWatcher() {
//
//			private CharSequence temp;
//			private int selectionStart;
//			private int selectionEnd;
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before,
//					int count) {
//				// TODO Auto-generated method stub
//				temp = s;
//				System.out.println("s=" + s);
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//				// TODO Auto-generated method stub
//				int number = 100 - s.length();
//				noText.setText(number + "/100");
//				selectionStart = contentText.getSelectionStart();
//				selectionEnd = contentText.getSelectionEnd();
//				// System.out.println("start="+selectionStart+",end="+selectionEnd);
//				if (temp.length() > 100) {
//					s.delete(selectionStart - 1, selectionEnd);
//					int tempSelection = selectionStart;
//					contentText.setText(s);
//					contentText.setSelection(tempSelection);// 设置光标在最后
//				}
//			}
//		});
//	}
//
	@Override
	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		switch (v.getId()) {
//		case R.id.index_top_seartchImg:
//			finish();
//			break;
//		case R.id.activity_feedback_submitBtn:
//			String s = contentText.getText().toString();
//			String phoneNo = phoneText.getText().toString();
//
//			if ((!isMobileNO(phoneNo)) && (!isEmail(phoneNo))) {
//				Toast.makeText(this, "您输入的联系方式不正确", 0).show();
//				break;
//			}
//
//			if (s == null || "".equals(s)) {
//				Toast.makeText(FeedBackActivity.this, "请输入您的意见", 0).show();
//			} else if (typeText.getText().toString() == null
//					|| "".equals(typeText.getText().toString())) {
//				Toast.makeText(this, "请选择反馈意见的类型", 0).show();
//			} else {
//				final Map<String, String> map = new HashMap<String, String>();
//				map.put("type", key);
//				map.put("content", s);
//				map.put("origin", "2");
//				map.put("contact", phoneNo);
//				new Thread() {
//					public void run() {
//
//						String param = JSONUtil.simpleMapToJsonStr(map);
//						Log.e("", "param::" + param);
//						String result = HttpRequestUtil.sendPost(
//								Constant.feedbackPath, param,
//								FeedBackActivity.this);
//						Log.e("", "result::" + result);
//						boolean isSubmit = Utils.getApplyResult(result);
//						Log.e("", "反馈意见提交结果：：" + isSubmit);
//						if (isSubmit) {
//							handler.sendEmptyMessage(0);
//						} else {
//							handler.sendEmptyMessage(1);
//						}
//					};
//				}.start();
//			}
//			break;
//		case R.id.activity_feeabadk_enterLayout:
//			Intent intent = new Intent(FeedBackActivity.this,
//					FunctionOptionActivity.class);
//			startActivityForResult(intent, 0);
//			break;
//
//		default:
//			break;
//		}
	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);
//		if (requestCode == 0 && resultCode == RESULT_OK) {
//			key = data.getStringExtra("key");
//			value = data.getStringExtra("value");
//			typeText.setText(value);
////			Toast.makeText(this, "返回的数据是：" + value, 0).show();
//			Log.e("", "option::" + value);
//		}
//	}
//
//	// 判断是否是手机号
//	public static boolean isMobileNO(String mobiles) {
//		Pattern p = Pattern
//				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
//		Matcher m = p.matcher(mobiles);
//		return m.matches();
//	}
//
//	/**
//	 * 判断邮箱是否合法
//	 * 
//	 * @param email
//	 * @return
//	 */
//	public static boolean isEmail(String email) {
//		if (null == email || "".equals(email))
//			return false;
//		// Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
//		Pattern p = Pattern
//				.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");// 复杂匹配
//		Matcher m = p.matcher(email);
//		return m.matches();
//	}
}
