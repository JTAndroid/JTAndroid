package com.tr.ui.user.frg;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.App;
import com.tr.R;
import com.tr.api.HomeReqUtil;
import com.tr.api.UserReqUtil;
import com.tr.model.api.DataBox;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.flow.frg.FrgFlow;
import com.tr.ui.user.SplashActivity;
import com.tr.ui.user.modified.LoginActivity;
import com.tr.ui.widgets.MessageDialog;
import com.tr.ui.widgets.MessageDialog.OnDialogFinishListener;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;

public class FrgRegisterSecondOne extends JBaseFragment implements OnClickListener{

	private EditText phone_call,vcodeET;
	private TextView vcodeTv, nextTv, gintong_aggreement;
	private String mobile, mobileAreaCode;
	// 变脸相关
	private int mCountdownLeft; // 倒计时剩余时间
	private Timer mTimer; // 计时任务
	private String mVerifyCode; // 验证码// 消息类型
	private final int MSG_BASE = 100;
	private final int MSG_COUNT_DOWN = MSG_BASE + 1; // 倒计时的消息标识
	// 常量
	private final int EXPIRED_TIME = 60; // 验证码超时时间,60s
	private final int COUNTDOWN_INTERVAL = 1000; // 倒计时时间间隔
	private final String TIP_GET_VERIFY_CODE_SUCCESS = "验证码已发送到您的手机，请注意查收";
	private final String TIP_REGET_VERIFY_CODE = "重获验证码";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		mobile = bundle.getString("mobile");
		mobileAreaCode = bundle.getString("mobileAreaCode");
		
		showLoadingDialog("");
		UserReqUtil.doGetVerifyCode(getActivity(), mBindData, UserReqUtil.getDoGetVerifyCodeParams(0, mobileAreaCode, mobile), mHandler);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.user_frg_register_second_one,
				container, false);
		phone_call = (EditText) view.findViewById(R.id.phone_call);
		vcodeET = (EditText) view.findViewById(R.id.vcodeET);
		vcodeTv = (TextView) view.findViewById(R.id.vcodeTv);
		nextTv = (TextView) view.findViewById(R.id.nextTv);
		gintong_aggreement = (TextView) view.findViewById(R.id.gintong_aggreement);
		
		phone_call.setText(mobile);
		
		vcodeTv.setOnClickListener(this);
		nextTv.setOnClickListener(this);
		gintong_aggreement.setOnClickListener(this);
		
		vcodeTv.addTextChangedListener(myTextWatcher);
		nextTv.setClickable(false);
		
		return view;
	}
	
	private TextWatcher myTextWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			if (!StringUtils.isEmpty(s.toString())) {
				nextTv.setClickable(true);
				nextTv.setBackgroundResource(R.drawable.button_circle_click);
			}
			else {
				nextTv.setClickable(false);
				nextTv.setBackgroundResource(R.drawable.button_circle_noclick);
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.vcodeTv:
			showLoadingDialog("");
			UserReqUtil.doGetVerifyCode(getActivity(), mBindData, UserReqUtil.getDoGetVerifyCodeParams(0, mobileAreaCode, mobile), mHandler);
			break;
		case R.id.nextTv:
			if(TextUtils.isEmpty(vcodeET.getText().toString())){
				Toast.makeText(getActivity(), "请输入验证码", Toast.LENGTH_SHORT).show();
				return;
			}else if(!vcodeET.getText().toString().equals(mVerifyCode)){
				Toast.makeText(getActivity(), "验证码输入错误", Toast.LENGTH_SHORT).show();
				return;
			}
			Bundle bundle = new Bundle();
			bundle.putString("mobile", mobile);
			bundle.putString("mobileAreaCode", mobileAreaCode);
			bundle.putString("vcode", vcodeET.getText().toString());
			FrgRegisterSecondTwo frgTwo = new FrgRegisterSecondTwo();
			frgTwo.setArguments(bundle);
			FragmentTransaction transaction =getFragmentManager().beginTransaction();
			transaction.replace(R.id.fragment_conainer,frgTwo);
			transaction.commit();
			break;
		case R.id.gintong_aggreement:
			ENavigate.startAgreementActivity(getActivity());
			break;
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			MessageDialog messageDialog = new MessageDialog(getActivity());
			messageDialog.setTitle("");
			messageDialog.setContent("验证码短信可能略有延迟\n\n确定返回并重新开始?");
			messageDialog.setOkTv("返回");
			messageDialog.setCancelTv("等待");
			messageDialog.show();
			messageDialog.setOnDialogFinishListener(new OnDialogFinishListener(){
				@Override
				public void onFinish(String content) {
					getActivity().finish();
				}

				@Override
				public void onCancel(String content) {
					
				}
			});
			break;
		}
		return true;
	}
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case EAPIConsts.handler.show_err:
				MessageDialog messageDialog = new MessageDialog(getActivity());
				messageDialog.setTitle("");
				messageDialog.setContent("该手机号已经被注册\n\n你可以更换手机号注册,或直接登录");
				messageDialog.setOkTv("直接登录");
				messageDialog.setCancelTv("更换手机号");
				messageDialog.setCanceledOnTouchOutside(false);
				messageDialog.setCancelable(false);
				messageDialog.setOnDialogFinishListener(new OnDialogFinishListener(){
					@Override
					public void onFinish(String content) {
						for (Activity activity : App.activityList) {
							activity.finish();
						}
						// 跳转到登录界面
						Intent intent = new Intent(App.getApp(),
								LoginActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}

					@Override
					public void onCancel(String content) {
						getActivity().finish();
						
					}
				});
				Bundle bundle = msg.getData();
				String errCode = bundle.getString(EAPIConsts.Header.ERRORCODE);
				String errMessage = bundle.getString(EAPIConsts.Header.ERRORMESSAGE);
				if(errCode.equals("-1") && errMessage.equals("该手机号已被占用")){
					messageDialog.show();
				}else{
					Toast.makeText(getActivity(), errMessage, Toast.LENGTH_SHORT).show();
				}
				break;
			case MSG_COUNT_DOWN: // 倒计时消息
				if (mCountdownLeft <= 0) { // 倒计时结束
					// 取消倒计时任务
					if (mTimer != null) {
						mTimer.cancel();
						mTimer = null;
					}
					// 更改态验证码按钮状态和文字
					vcodeTv.setText(TIP_REGET_VERIFY_CODE);
					vcodeTv.setEnabled(true);
					vcodeTv.setTextColor(0xfff39b75);
				}
				else { // 倒计时仍在进行
					vcodeTv.setVisibility(View.VISIBLE);
					vcodeTv.setText("接收短信大概需要"+mCountdownLeft+"秒");
				}
				break;
			}
		}
	};
	
	// 接口回调函数
	private IBindData mBindData = new IBindData() {

		@Override
		public void bindData(int tag, Object object) {
			dismissLoadingDialog();
			if (tag == EAPIConsts.ReqType.GET_VERIFY_CODE) { // 获取验证码
				// 是否获取成功
				if (object != null) {
					DataBox dataBox = (DataBox) object;
					mVerifyCode = dataBox.mVerifyCode;
					if (dataBox.mIsSuccess) { // 获取验证码成功，开始倒计时
						// 重置倒计时Timer
						if (mTimer != null) {
							mTimer.cancel();
							mTimer = null;
						}
						mTimer = new Timer();
						mTimer.schedule(new TimerTask() {

							@Override
							public void run() {
								mCountdownLeft--;
								mHandler.sendEmptyMessage(MSG_COUNT_DOWN);
							}
						}, 0, COUNTDOWN_INTERVAL);
						// 设置倒计时时间
						mCountdownLeft = EXPIRED_TIME;
						// 设置验证按钮状态
						vcodeTv.setEnabled(false);
						vcodeTv.setTextColor(0xff4b5e63);
						// 显示消息
						Toast.makeText(getActivity(), TIP_GET_VERIFY_CODE_SUCCESS, 0).show();
					}
				}
			}
		}
		
	};
	
}
