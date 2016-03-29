package com.tongmeng.alliance.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tongmeng.alliance.dao.AccountUser;
import com.tongmeng.alliance.util.Constant;
import com.tongmeng.alliance.util.HttpRequestUtil;
import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.utils.log.KeelLog;

public class AccountUserDetailActivity extends JBaseActivity implements OnClickListener{

	private ImageView search;
	private TextView myTitle;
	private TextView create_Tv;
	
	TextView add_account_type;
	EditText add_account_name, add_account_num;
	AccountUser accountUser;
	LinearLayout account_type_linearlayout;
	private String FILE = "saveSetting";
	
	String notifyInfo;
	Handler msgHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.arg1) {
			case 1:
				Intent intent = new Intent(AccountUserDetailActivity.this,
						AccountUserSetActivity.class);
				startActivity(intent);
				finish();
				break;
			case 2:
				Toast.makeText(getApplicationContext(), notifyInfo, 1).show();
				break;
			default:
				break;
			}

		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_user_detail);
		
		account_type_linearlayout = (LinearLayout) findViewById(R.id.account_type_linearlayout);
		add_account_name = (EditText) findViewById(R.id.add_account_name);
		add_account_num = (EditText) findViewById(R.id.add_account_num);
		add_account_type = (TextView) findViewById(R.id.add_account_type);
		accountUser = new AccountUser();
		account_type_linearlayout.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.account_type_linearlayout:
			Intent intent = new Intent(AccountUserDetailActivity.this,
					AccountUserTypeActivity.class);
			startActivityForResult(intent, 1);
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == RESULT_OK) {
			add_account_type.setText(data.getStringExtra("accountType"));
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
		View mCustomView = getLayoutInflater().inflate(R.layout.org_firstpage_actionbar_title, null);
		mActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView.getLayoutParams();
		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK | Gravity.CENTER_HORIZONTAL;
		mActionBar.setCustomView(mCustomView, mP);
		mActionBar.setTitle(" ");
		myTitle = (TextView) mCustomView.findViewById(R.id.titleTv);
		myTitle.setText("收款账户设置");
		create_Tv = (TextView) mCustomView.findViewById(R.id.create_Tv);
		create_Tv.setText("保存");
		create_Tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String userAccount = add_account_name.getText().toString();
				String numAccount = add_account_num.getText().toString();
				String accountType = add_account_type.getText().toString();
				
				if(userAccount== null || "".equals(userAccount) ){
					Toast.makeText(AccountUserDetailActivity.this, "请输入您的帐户名", 0).show();
					return;
				}
				if(numAccount== null || "".equals(numAccount) ){
					Toast.makeText(AccountUserDetailActivity.this, "请输入您的帐号", 0).show();
					return;
				}
				if(accountType== null || "".equals(accountType) ){
					Toast.makeText(AccountUserDetailActivity.this, "请选择您的账户类型", 0).show();
					return;
				}
				String url = null;
				if(accountType.equals("支付宝")){
					url = Constant.applyPath;
				}else if(accountType.equals("微信")){
					url = Constant.weChatpath;
				}
				addAccountUser(userAccount, numAccount, url);
			}
		});
		search = (ImageView) mCustomView.findViewById(R.id.titleIv);
		search.setVisibility(View.GONE);
	}
	
	private void addAccountUser(final String accountName,
			final String accountNum, final String path) {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				System.out.println("执行的代码");
				String param = "{\"account\":\""+accountNum+"\",\"name\":\""+accountName+"\"}";
				KeelLog.e("AccountUserSetActivity", "param::"+param);
				String result = HttpRequestUtil.sendPost(path, param,
						AccountUserDetailActivity.this);
				KeelLog.e("AccountUserSetActivity", "result::"+result);
				try {
					JSONObject rev = new JSONObject(result);
					JSONObject responseData = rev.getJSONObject("responseData");
					JSONObject account = responseData.getJSONObject("account");
					int id = account.getInt("id");
					int type = account.getInt("type");
					String name = account.getString("name");
					String act = account.getString("account");
					int isDefault = account.getInt("isDefault");
					accountUser.setId(id);
					accountUser.setType(type);
					accountUser.setName(name);
					accountUser.setAccount(act);
					accountUser.setIsDefault(String.valueOf(isDefault));
					JSONObject notification = rev.getJSONObject("notification");
					String notifyCode = notification.getString("notifyCode");
					notifyInfo = notification.getString("notifyInfo");
					if (notifyCode.equals("0001")) {
						if (msgHandler != null) {
							Message msg = msgHandler.obtainMessage();
							msg.arg1 = 1;
							// msg.obj = message;
							msgHandler.sendMessage(msg);
						}
					} else {
						if (msgHandler != null) {
							Message msg = msgHandler.obtainMessage();
							msg.arg1 = 2;
							// msg.obj = message;
							msgHandler.sendMessage(msg);
						}

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			};
		}.start();

	}
}
