package com.tr.ui.user.modified;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.baidu.navisdk.util.common.StringUtils;
import com.tr.App;
import com.tr.R;
import com.tr.api.UserReqUtil;
import com.tr.model.api.DataBox;
import com.tr.model.home.MListCountry;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.widgets.EmailAutoCompleteTextView;
import com.utils.common.EConsts;
import com.utils.common.EUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;

public class RegisterEmailFragment extends JBaseFragment {
	private JBaseFragmentActivity mParentActivity; // 父Activity
	private App mMainApp; // App全局对象

	private boolean hasEmail = false;
	private boolean hasPassword = false;

	private LinearLayout register_by_phone_ll;
	private com.tr.ui.widgets.AutoCompeleteEmailSuffixEditText accountEt;

	private ImageView delelte_email_content;
	private boolean flag = true;

	private EditText passwordEt;
	private ImageView view_password;
	private ImageView deletePassword;
	private Button register_gintong_account;
	private TextView gintong_aggreement;
	// 文字提示
	private final String TIP_REGET_VERIFY_CODE = "重获验证码";
	private final String TIP_GET_VERIFY_CODE = "获取验证码";
	private final String TIP_WRONG_VERIFY_CODE = "验证码错误";
	// private final String TIP_LOADING_SEND_VERIFY_EMAIL = "正在请求发送验证邮件，请稍等...";
	// private final String TIP_LOADING_GET_VERIFY_CODE = "正在获取验证码，请稍等...";
	private final String TIP_EMPTY_VCODE = "请输入验证码";
	private final String TIP_ILLEGAL_ACCOUNT = "请输入正确的手机号码或邮箱";
	private final String TIP_GET_VERIFY_CODE_SUCCESS = "验证码已发送到您的手机，请注意查收";

	private MListCountry mListCountry;

	public RegisterEmailFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_register_gintong_acount_by_email, null);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initVars();
		mMainApp = App.getApp();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mParentActivity = (JBaseFragmentActivity) activity;
	};

	@Override
	public void onViewCreated(View container, Bundle savedInstanceState) {
		initControls(container);
	}

	private void initVars() {

	}

	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}

	}

	private void initControls(View container) {
		register_by_phone_ll = (LinearLayout) container.findViewById(R.id.register_by_phone_ll);
		register_by_phone_ll.setOnClickListener(mClickListener);
		passwordEt = (EditText) container.findViewById(R.id.passwordEt);
		delelte_email_content = (ImageView) container.findViewById(R.id.delelte_email_content);
		delelte_email_content.setOnClickListener(mClickListener);
		accountEt = (com.tr.ui.widgets.AutoCompeleteEmailSuffixEditText) container.findViewById(R.id.accountEt);
		accountEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!StringUtils.isEmpty(s.toString())) {
					delelte_email_content.setVisibility(View.VISIBLE);
					hasEmail = true;
				}
				else {
					delelte_email_content.setVisibility(View.INVISIBLE);
					hasEmail = false;
				}
				if (hasEmail == true && hasPassword == true) {
					register_gintong_account.setClickable(true);
					register_gintong_account.setBackgroundResource(R.drawable.sign_in);
				}
				else {
					register_gintong_account.setClickable(false);
					register_gintong_account.setBackgroundResource(R.drawable.sign_in_normal);
				}
			}
		});
		// 删除密码
		deletePassword = (ImageView) container.findViewById(R.id.delete_password);
		gintong_aggreement = (TextView) container.findViewById(R.id.gintong_aggreement);
		gintong_aggreement.setOnClickListener(mClickListener);
		// 确认密码
		view_password = (ImageView) container.findViewById(R.id.view_password);
		// 对图标进行初始化操作
		passwordEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		view_password.setBackgroundResource(R.drawable.show_password_bg_normal);
		// 根据点击改变图标状态
		view_password.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (flag) {
					// 以点点的形式显示密码
					passwordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
					view_password.setBackgroundResource(R.drawable.show_password_bg_pressed);
					flag = false;

				}
				else {
					// 不以以点点的形式显示密码
					passwordEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
					view_password.setBackgroundResource(R.drawable.show_password_bg_normal);
					flag = true;
				}
			}
		});

		deletePassword.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				passwordEt.setText("");
			}
		});

		passwordEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!StringUtils.isEmpty(s.toString()) && s.length() >= 6 && s.length() <= 19) {
					deletePassword.setVisibility(View.VISIBLE);
					hasPassword = true;
				}
				else {
					deletePassword.setVisibility(View.INVISIBLE);
					hasPassword = false;
					if (s.length() > 19) {
						Toast.makeText(getActivity(), "请输入6-19位密码", 0).show();
						deletePassword.setVisibility(View.VISIBLE);
					}
				}
				if (hasEmail == true && hasPassword == true) {
					// TODO
					register_gintong_account.setClickable(true);
					register_gintong_account.setBackgroundResource(R.drawable.sign_in);
				}
				else {
					register_gintong_account.setClickable(false);
					register_gintong_account.setBackgroundResource(R.drawable.sign_in_normal);
				}
			}
		});
		register_gintong_account = (Button) container.findViewById(R.id.register_gintong_account);
		register_gintong_account.setOnClickListener(mClickListener);
		register_gintong_account.setClickable(false);
	}

	// 点击事件监听器
	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == register_gintong_account) {
				nextStep();
			}
			if (v == gintong_aggreement) {
				// TODO
				// Toast.makeText(getActivity(), "金桐用户协议", 0).show();
				ENavigate.startAgreementActivity(getActivity());
			}
			if (v == register_by_phone_ll) {
				// Toast.makeText(getActivity(), "手机注册", 0).show();
				RegisterPhoneFragment fragment = new RegisterPhoneFragment();
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
			}
			if (v == delelte_email_content) {
				accountEt.setText("");
			}
		}
	};

	// 检查信息的完整性（是否显示下一步按钮）
	private boolean infoIntegrityCheck() {
		System.out.println("邮箱判断结果=" + EUtil.isEmail(accountEt.getText().toString()));
		if (EUtil.isEmail(accountEt.getText().toString())) {
			return true;
		}
		else {
			showToast(TIP_ILLEGAL_ACCOUNT);
			return false;
		}
	}

	/** 下一步 */
	private void nextStep() {
		if (!infoIntegrityCheck()) {
			return;
		}

		if (EUtil.isEmail(accountEt.getText().toString())) { // 邮箱注册

			// 显示加载框
			mParentActivity.showLoadingDialog();
			// 是否有SessionID
			if (mMainApp.getAppData().getSessionID().length() <= 0) {
				UserReqUtil.doLoginConfiguration(mParentActivity, mBindData,
						UserReqUtil.getDoLoginConfigurationParams("", "", EUtil.getDeviceID(mParentActivity), EUtil.getAppVersionName(mParentActivity), "", "", "", "android", "", "", "", ""), null);
			}
			else {
				UserReqUtil.doRegister(mParentActivity, mBindData, UserReqUtil.getDoRegisterParams("", "",accountEt.getText().toString(), passwordEt.getText().toString(), "", 1,""), null);
			}
		}
		else {
			Toast.makeText(getActivity(), "请输入正确的手机号或邮箱", 0).show();
		}
	}

	// 接口回调函数
	private IBindData mBindData = new IBindData() {

		@Override
		public void bindData(int type, Object object) {
			if (mParentActivity.isLoadingDialogShowing()) {
				mParentActivity.dismissLoadingDialog();
			}
			if (type == EAPIConsts.ReqType.SEND_VALIDATE_EMAIL) { // 发送验证邮件
				// 跳转邮件验证页面
				if (object != null) {
					// 初始化全局变量
					DataBox dataBox = (DataBox) object;
					if (dataBox.mIsSuccess) {
						Intent intent = new Intent(getActivity(), SendVerifyEmailActivity.class);
						intent.putExtra(EConsts.Key.EMAIL, accountEt.getText().toString());
						startActivity(intent);
					}
				}
			}
			if (type == EAPIConsts.ReqType.REGISTER) { // 注册成功
				// 是否进行下一步操作
				if (mParentActivity.isLoadingDialogShowing()) {
					mParentActivity.dismissLoadingDialog();
				}
				// 跳转到个人信息完善界面
				if (object != null) {
					// 初始化全局变量
					DataBox dataBox = (DataBox) object;
					mMainApp.getAppData().setUser(dataBox.mJTMember);
					// 跳转到完善个人信息页面
					// mParentActivity.startActivity(new Intent(getActivity(),
					// RegisterPersonalDetailActivity.class));
					Intent intent = new Intent(getActivity(), SendVerifyEmailActivity.class);
					intent.putExtra(EConsts.Key.EMAIL, accountEt.getText().toString());
					startActivity(intent);
					// ENavigate.startSendVerifyEmailActivity(getActivity(),intent);
					mParentActivity.finish();
				}
			}
			else if (type == EAPIConsts.ReqType.LOGIN_CONFIGURATION) {
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

					// 邮箱注册
					UserReqUtil.doRegister(mParentActivity, mBindData, UserReqUtil.getDoRegisterParams("","", accountEt.getText().toString(), passwordEt.getText().toString(), "", 1,""), null);
				}
			}
		}
	};

}
