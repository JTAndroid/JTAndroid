package com.tr.ui.user.frg;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.App;
import com.tr.R;
import com.tr.api.UserReqUtil;
import com.tr.model.api.DataBox;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.user.modified.LoginActivity;
import com.tr.ui.user.modified.RegisterGintongAllPathActivity;
import com.tr.ui.user.modified.RegisterPersonalDetailActivity;
import com.utils.common.EUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;

public class FrgRegisterSecondTwo extends JBaseFragment implements OnClickListener{

	private EditText pwdEt, confirmPwdEt;
	private TextView errorPwdTv, errorComfirmPwdTv, confirmTv;
	private String mobile,mobileAreaCode,vcode;
	private App mMainApp;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		mobile = bundle.getString("mobile");
		mobileAreaCode = bundle.getString("mobileAreaCode");
		vcode = bundle.getString("vcode");
		
		mMainApp = App.getApp();
		
		HomeCommonUtils.initLeftCustomActionBar(getActivity(), getActivity().getActionBar(), "设置密码", false, null, false, true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.user_frg_register_second_two,
				container, false);
		pwdEt = (EditText) view.findViewById(R.id.pwdEt);
		confirmPwdEt = (EditText) view.findViewById(R.id.confirmPwdEt);
		errorPwdTv = (TextView) view.findViewById(R.id.errorPwdTv);
		errorComfirmPwdTv = (TextView) view.findViewById(R.id.errorComfirmPwdTv);
		confirmTv = (TextView) view.findViewById(R.id.confirmTv);
		
		initControls();
		return view;
	}
	
	private void initControls(){
		confirmTv.setOnClickListener(this);
		//个人电话用户密码框
		pwdEt.addTextChangedListener(new TextWatcher() {
 			@Override
 			public void onTextChanged(CharSequence s, int start, int before, int count) {}

 			@Override
 			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

 			@Override
 			public void afterTextChanged(Editable s) {
 				if (!StringUtils.isEmpty(s.toString()) && s.length() >= 6 && s.length() <= 20) {
 					errorPwdTv.setVisibility(View.GONE);
 				} else {
 					errorPwdTv.setVisibility(View.VISIBLE);
 				}
 			}
 		});
		
		confirmPwdEt.addTextChangedListener(new TextWatcher() {
 			@Override
 			public void onTextChanged(CharSequence s, int start, int before, int count) {}

 			@Override
 			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

 			@Override
 			public void afterTextChanged(Editable s) {
 				if (!StringUtils.isEmpty(s.toString()) && s.length()==pwdEt.getText().toString().length()) {
 					confirmTv.setBackgroundResource(R.drawable.button_circle_click);
 				}else{
 					confirmTv.setBackgroundResource(R.drawable.button_circle_noclick);
 				}
 			}
 		});
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.confirmTv:
			String pwd = pwdEt.getText().toString();
			String confirmPwd = confirmPwdEt.getText().toString();
			if (StringUtils.isEmpty(pwd) || pwd.length() < 6 || pwd.length() > 20) {
				errorPwdTv.setVisibility(View.VISIBLE);
			}else if(!EUtil.isPwd(pwd)){
				showToast("密码仅支持数字、下划线、字母");
			}else if(!pwd.equals(confirmPwd)){
				errorComfirmPwdTv.setVisibility(View.VISIBLE);
			}else{
				// 显示加载框
				showLoadingDialog("");
				// 是否有SessionID
				if (App.getApp().getAppData().getSessionID().length() <= 0) {
					UserReqUtil.doLoginConfiguration(getActivity(),mBindData,UserReqUtil.getDoLoginConfigurationParams("","",EUtil.getDeviceID(getActivity()),EUtil.getAppVersionName(getActivity()),"", "", "", "android", "", "", "",""), null);
				} else {
					UserReqUtil.doRegister(getActivity(), mBindData,UserReqUtil.getDoRegisterParams(mobile, "",pwdEt.getText().toString(),vcode, 1,mobileAreaCode.substring(1)), null);
				}
			}
			break;
		}
	}
	// 接口回调函数
	private IBindData mBindData = new IBindData() {

		@Override
		public void bindData(int tag, Object object) {
			if (tag == EAPIConsts.ReqType.REGISTER) { // 注册成功
				// 是否进行下一步操作
				dismissLoadingDialog();
				// 跳转到信息完善界面
				if (object != null) {
					// 初始化全局变量
					DataBox dataBox = (DataBox) object;
					App.getApp().getAppData().setUser(dataBox.mJTMember);
					if (dataBox.mJTMember.mRole == 0) {
						// 跳转到完善个人信息页面
						ENavigate.startRegisterPersonalDetailActivity(getActivity());
						getActivity().finish();
					}
				}
			}
			else if (tag == EAPIConsts.ReqType.LOGIN_CONFIGURATION) {
				if (object != null) {
					DataBox dataBox = (DataBox) object;
					// 以下字段均保存到数据库
					// SessionID
					mMainApp.getAppData().setSessionID(dataBox.mSessionID);
					// 货币范围
					if (dataBox.mListMoneyRange != null) {
						mMainApp.getAppData().setListMoneyRange(dataBox.mListMoneyRange);
					}
					// 货币类型
					if (dataBox.mListMoneyType != null) {
						mMainApp.getAppData().setListMoneyType(dataBox.mListMoneyType);
					}
					// 融资类型
					if (dataBox.mListInInvestType != null) {
						mMainApp.getAppData().setListInInvestType(dataBox.mListInInvestType);
					}
					// 保存投资类型
					if (dataBox.mListOutInvestType != null) {
						mMainApp.getAppData().setListOutInvestType(dataBox.mListOutInvestType);
					}
					// 投资区域
					if (dataBox.mListTrade != null) {
						mMainApp.getAppData().setListTrade(dataBox.mListTrade);
					}
					// 投资区域
					if (dataBox.mListArea != null) {
						mMainApp.getAppData().setListArea(dataBox.mListArea);
					}
					// 邀请语
					mMainApp.getAppData().mInviteJoinGinTongInfo = dataBox.mInviteJoinGinTongInfo;
					// 用户注册
					UserReqUtil.doRegister(
							getActivity(),
							mBindData,
							UserReqUtil.getDoRegisterParams(mobile, "", pwdEt.getText().toString(), vcode, 1, mobileAreaCode.substring(1)), null);
				}
			}
		}
		
	};
}
