package com.tr.ui.user.modified;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.R;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.common.EUtil;
import com.utils.string.StringUtils;

public class RegisterActivity extends JBaseActivity implements OnClickListener{

	
	private EditText phone_call;
	private TextView vcodeTv, region_number, gintong_aggreement;
	private ImageView delete_phone_call;
	
	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar(), "注册", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_act_register_new);
		phone_call = (EditText) findViewById(R.id.phone_call);
		delete_phone_call = (ImageView) findViewById(R.id.delete_phone_call);
		region_number = (TextView) findViewById(R.id.region_number);
		gintong_aggreement = (TextView) findViewById(R.id.gintong_aggreement);
		vcodeTv = (TextView) findViewById(R.id.vcodeTv);
		phone_call.addTextChangedListener(phoneCallTextWatcher);
		vcodeTv.setClickable(false);
		
		vcodeTv.setOnClickListener(this);
		delete_phone_call.setOnClickListener(this);
		gintong_aggreement.setOnClickListener(this);
	}
	
	private TextWatcher phoneCallTextWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			if (!StringUtils.isEmpty(s.toString())) {
				delete_phone_call.setVisibility(View.VISIBLE);
				vcodeTv.setClickable(true);
				vcodeTv.setBackgroundResource(R.drawable.button_circle_click);
			}
			else {
				delete_phone_call.setVisibility(View.GONE);
				vcodeTv.setClickable(false);
				vcodeTv.setBackgroundResource(R.drawable.button_circle_noclick);
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.delete_phone_call:
			phone_call.setText("");
			break;
		case R.id.gintong_aggreement:
			ENavigate.startAgreementActivity(this);
			break;
		case R.id.vcodeTv:
			// 验证手机号或邮箱是否正确
			if (EUtil.isMobileNO(region_number.getText().toString().trim(), phone_call.getText().toString())) { // 手机号和验证码----//后端决定此处由后台验证
				Intent intent = new Intent(this, RegisterSecondActivity.class);
				intent.putExtra("mobileAreaCode", region_number.getText().toString());
				intent.putExtra("mobile", phone_call.getText().toString());
				startActivity(intent);
			}else{
				Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}
	
}
