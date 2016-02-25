package com.tr.ui.user.modified;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.LocalActivityManager;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.LayoutParams;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.App;
import com.tr.R;
import com.tr.api.UserReqUtil;
import com.tr.model.api.DataBox;
import com.tr.model.home.MListCountry;
import com.tr.navigate.ENavigate;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.organization.create_clientele.CreateOrganizationActivity;
import com.tr.ui.organization.widgets.OrganizationInfoAlertDialog;
import com.tr.ui.organization.widgets.OrganizationInfoAlertDialog.OnDialogClickListener;
import com.tr.ui.user.utils.CommonUtils;
import com.tr.ui.widgets.AutoCompeleteEmailSuffixEditText;
import com.tr.ui.widgets.EProgressDialog;
import com.utils.common.EConsts;
import com.utils.common.EUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.log.KeelLog;
import com.utils.string.StringUtils;

public class RegisterGintongAllPathActivity extends Activity implements OnTabChangeListener, OnFocusChangeListener {
	
	private boolean hasEmail = false;

	private AutoCompeleteEmailSuffixEditText accountEt_person;//个人邮箱注册
	private AutoCompeleteEmailSuffixEditText accountEt_orangization;

	private ImageView delelte_email_content_person;
	private ImageView delelte_email_content_organization;
	private ImageView delelte_organization_name;

	private boolean hasPhoneCall = false;
	private boolean hasVerifyCode = false;
	private boolean hasPassword = false;
	private boolean hasOrgName = false;
	private boolean hasOrgMail = false;
	private boolean hasOrgPassword = false;
	
	private boolean is_mail_register = false;
	private boolean is_phone_register = false;

	private LinearLayout register_by_email_ll;
	private LinearLayout register_gintong_by_phone;
  
	private final String TAG = getClass().getSimpleName();
	private App mMainApp; // App全局对象
	// 常量
	private final int EXPIRED_TIME = 60; // 验证码超时时间,60s
	private final int COUNTDOWN_INTERVAL = 1000; // 倒计时时间间隔

	// 消息类型
	private final int MSG_BASE = 100;
	private final int MSG_COUNT_DOWN = MSG_BASE + 1; // 倒计时的消息标识

	// 文字提示
	private final String TIP_REGET_VERIFY_CODE = "重获验证码";
	private final String TIP_GET_VERIFY_CODE = "获取验证码";
	private final String TIP_WRONG_VERIFY_CODE = "验证码错误";
	// private final String TIP_LOADING_SEND_VERIFY_EMAIL = "正在请求发送验证邮件，请稍等...";
	// private final String TIP_LOADING_GET_VERIFY_CODE = "正在获取验证码，请稍等...";
	private final String TIP_EMPTY_VCODE = "请输入验证码";
	private final String TIP_ILLEGAL_ACCOUNT = "请输入正确的手机号码或邮箱";
	private final String TIP_GET_VERIFY_CODE_SUCCESS = "验证码已发送到您的手机，请注意查收";

	// 控件相关
	private EditText vcodeEt; // 输入验证码
	private TextView countSeconds; // 获取验证码
	private TextView vcodeTv; // 获取验证码

	// 变脸相关
	private int mCountdownLeft; // 倒计时剩余时间
	private Timer mTimer; // 计时任务
	private String mVerifyCode; // 验证码

	// 控件
	private TextView region_number;
	private EditText phone_call;
	private TextView gintong_aggreement_person;
	private TextView gintong_aggreement_person_mail;
	private TextView gintong_aggreement_orangization;
	private ImageView delete_phone_call;
	// 变量
	private String mMobile; // 用户输入的手机号
	private static boolean flag = true;

	MListCountry mListCountry;
	protected EProgressDialog mProgressDialog;
	
//	private TabHost orgAndPersonTabhost;
	protected int menuSettingTag=0;
	private View containerView;

	private EditText organization_name;//组织全称

	private EditText passwordEt_person;//个人注册密码框
	private EditText passwordEt_organization;//组织注册密码框

	private ImageView delete_password_person;//个人注册删除密码图标
	private ImageView delete_password_organization;//组织注册删除密码图标
	private ImageView view_password_person;//个人注册查看密码图标
	private ImageView view_password_organization;//组织注册查看密码图标

	private Button register_gintong_account_person;//个人注册按钮
	private Button register_gintong_account_organization;//组织注册按钮
	private Button register_gintong_account_person_mail;//个人邮箱注册按钮
	private RelativeLayout person;
	private RelativeLayout organizationall;
	private RelativeLayout personmail;

	private EditText passwordEt_person_mail;

	private ImageView delete_password_person_mail;

	private ImageView view_password_person_mail;
	private String HINT_MESG="组织全称一旦注册不可随意修改，请与您要提交的审核资料（例如：营业执照）上的全称保持一致，您确定要提交注册吗？";


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		//去除标题栏
//		requestWindowFeature(Window.FEATURE_NO_TITLE);   
//		orgAndPersonTabhost=this.getTabHost();
		containerView = LayoutInflater.from(this).inflate(R.layout.user_act_register_organdpersonal, null);
		
		setContentView(containerView);
		
		LocalActivityManager mLocalActivityManager =new LocalActivityManager(this, false);
		mLocalActivityManager.dispatchCreate(savedInstanceState);
//		orgAndPersonTabhost.setup(mLocalActivityManager);
		
//		orgAndPersonTabhost.addTab(orgAndPersonTabhost.newTabSpec("person").setIndicator("个人",null).setContent(R.id.person));
//		orgAndPersonTabhost.addTab(orgAndPersonTabhost.newTabSpec("organization").setIndicator("组织",null).setContent(R.id.organizationall));
		
		person = (RelativeLayout) findViewById(R.id.person);
		organizationall = (RelativeLayout) findViewById(R.id.organizationall);
		personmail = (RelativeLayout) findViewById(R.id.personmail);
		
		initControls(containerView);
		initJActionBarImpl();
		initVars();
//		updateTab(orgAndPersonTabhost);
		mMainApp = App.getApp();
//		orgAndPersonTabhost.setOnTabChangedListener(this);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onTabChanged(String tagString) {
//		updateTab(orgAndPersonTabhost);
		
	}
	
	private void initJActionBarImpl(){
				// 设置ActionBar样式
				ActionBar actionbar = this.getActionBar();
				actionbar.setIcon(R.color.none);
				// 设置actionbar的背景图
				Drawable myDrawable = getResources().getDrawable(R.drawable.auth_title_back);
				actionbar.setBackgroundDrawable(myDrawable); // 设置背景图片
				actionbar.setSplitBackgroundDrawable(getResources().getDrawable(R.drawable.auth_title_back));
				actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
				actionbar.setDisplayShowTitleEnabled(true);
				actionbar.setDisplayShowCustomEnabled(true);
				actionbar.setDisplayShowHomeEnabled(false);// 不显示应用图标
				actionbar.setHomeButtonEnabled(true);
				actionbar.setDisplayHomeAsUpEnabled(true);
				actionbar.setDisplayShowTitleEnabled(true);
				View mCustomView = getLayoutInflater().inflate(
						R.layout.demand_actionbar, null);
				actionbar.setCustomView(mCustomView, new ActionBar.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView
						.getLayoutParams();
				mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK
						| Gravity.CENTER_HORIZONTAL;
				actionbar.setCustomView(mCustomView, mP);
//				actionbar.setTitle(" 加入金桐");
				HomeCommonUtils.initLeftCustomActionBar(this, actionbar, " 加入金桐", false, null, false, true);
				actionbar.setDisplayShowHomeEnabled(true);
	}
	
	/**
     * 更新Tab标签的颜色，和字体的颜色
     * @param tabHost
     */
    private void updateTab(final TabHost tabHost) {
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            View view = tabHost.getTabWidget().getChildAt(i);
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextSize(16);
            tv.setTypeface(Typeface.DEFAULT, Typeface.NORMAL); // 设置字体和风格
            if (tabHost.getCurrentTab() == i) {//选中
                view.setBackground(getResources().getDrawable(R.drawable.organization_person_login_tab));//选中后的背景
                tv.setTextColor(this.getResources().getColor(R.color.gintongyellow));
                if ("个人".equals(tv.getText())) {
                	if (is_mail_register) {
                    	person.setVisibility(View.GONE);
    					personmail.setVisibility(View.VISIBLE);
    					organizationall.setVisibility(View.GONE);
    				}
                	if (is_phone_register)  {
    					person.setVisibility(View.VISIBLE);
    					personmail.setVisibility(View.GONE);
    					organizationall.setVisibility(View.GONE);
					}
                }
                if ("组织".equals(tv.getText())) {
                	person.setVisibility(View.GONE);
					personmail.setVisibility(View.GONE);
					organizationall.setVisibility(View.VISIBLE);
				}
            } else {//不选中
                tv.setTextColor(this.getResources().getColorStateList(R.color.black));
                if ("个人".equals(tv.getText())) {
                	view.setBackground(getResources().getDrawable(R.drawable.organization_person_login_tab_left));//非选择的背景
				} else {
					view.setBackground(getResources().getDrawable(R.drawable.organization_person_login_tab_right));//非选择的背景
				}
            }
        }
        initControls(containerView);
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
				finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
    
    
 // 初始化变量
 	private void initVars() {
 		mTimer = new Timer();
 		mCountdownLeft = EXPIRED_TIME; // 60s倒计时时间
 		mVerifyCode = ""; // 验证码
 	}
 	
 // 初始化控件
 	private void initControls(View container) {
 		region_number = (TextView) container.findViewById(R.id.region_number);
 		region_number.setOnClickListener(mClickListener);
 		phone_call = (EditText) container.findViewById(R.id.phone_call);
 		phone_call.addTextChangedListener(phoneCallTextWatcher);
 		
 		
 		View person_login = container.findViewById(R.id.person_login);
 		View organzation_login = container.findViewById(R.id.organzation_login);
 		View person_login_mail = container.findViewById(R.id.person_login_mail);
 		gintong_aggreement_person = (TextView) person_login.findViewById(R.id.gintong_aggreement);
 		gintong_aggreement_person.setOnClickListener(mClickListener);
 		gintong_aggreement_person_mail = (TextView) person_login_mail.findViewById(R.id.gintong_aggreement);
 		gintong_aggreement_person_mail.setOnClickListener(mClickListener);
 		gintong_aggreement_orangization = (TextView) organzation_login.findViewById(R.id.gintong_aggreement);
 		gintong_aggreement_orangization.setOnClickListener(mClickListener);
 		register_gintong_account_person = (Button) person_login.findViewById(R.id.register_gintong_account);
 		register_gintong_account_person.setOnClickListener(mClickListener);
 		register_gintong_account_person_mail = (Button) person_login_mail.findViewById(R.id.register_gintong_account);
 		register_gintong_account_person_mail.setOnClickListener(mClickListener);
 		register_gintong_account_organization = (Button) organzation_login.findViewById(R.id.register_gintong_account);
 		register_gintong_account_organization.setOnClickListener(mClickListener);
 		
 		delete_phone_call = (ImageView) container.findViewById(R.id.delete_phone_call);
 		delete_phone_call.setOnClickListener(mClickListener);
 		countSeconds = (TextView) container.findViewById(R.id.count_backwards);
 		register_by_email_ll = (LinearLayout) container.findViewById(R.id.register_by_email_ll);
 		register_gintong_by_phone = (LinearLayout) container.findViewById(R.id.register_gintong_by_phone);
 		register_by_email_ll.setOnClickListener(mClickListener);
		register_gintong_by_phone.setOnClickListener(mClickListener);
 		// 输入验证码
 		vcodeEt = (EditText) container.findViewById(R.id.vcodeEt);
 		vcodeEt.addTextChangedListener(new TextWatcher() {
 			@Override
 			public void onTextChanged(CharSequence s, int start, int before, int count) {}
 			@Override
 			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
 			@Override
 			public void afterTextChanged(Editable s) {
 				if (!StringUtils.isEmpty(s.toString())) {
 					hasVerifyCode = true;
 				}
 				else {
 					hasVerifyCode = false;
 				}
 				if (hasPhoneCall == true && hasVerifyCode == true && hasPassword == true) {
 					register_gintong_account_person.setClickable(true);
 					register_gintong_account_person.setBackgroundResource(R.drawable.sign_in);
 				}
 				else {
 					register_gintong_account_person.setClickable(false);
 					register_gintong_account_person.setBackgroundResource(R.drawable.sign_in_normal);
 				}
 			}
 		});
 		// 获取验证码
 		vcodeTv = (TextView) container.findViewById(R.id.vcodeTv);
 		vcodeTv.setText(TIP_GET_VERIFY_CODE);
 		vcodeTv.setOnClickListener(mClickListener);
 		
 		//include对象初始化
 		View person_password = container.findViewById(R.id.person_password);
 		View person_password_mail = container.findViewById(R.id.person_password_mail);
 		View organization_password = container.findViewById(R.id.organization_password);
 		//组织和个人密码框对象初始化
 		passwordEt_person = (EditText) person_password.findViewById(R.id.passwordEt);
 		passwordEt_person_mail = (EditText) person_password_mail.findViewById(R.id.passwordEt);
 		passwordEt_organization = (EditText) organization_password.findViewById(R.id.passwordEt);
 		//组织和个人删除密码图标对象初始化
 		delete_password_person = (ImageView) person_password.findViewById(R.id.delete_password);
 		delete_password_person_mail = (ImageView) person_password_mail.findViewById(R.id.delete_password);
 		delete_password_organization = (ImageView) organization_password.findViewById(R.id.delete_password);
 		//组织和个人查看密码对象初始化
 		view_password_person = (ImageView) person_password.findViewById(R.id.view_password);
 		view_password_person_mail = (ImageView) person_password_mail.findViewById(R.id.view_password);
 		view_password_organization = (ImageView) organization_password.findViewById(R.id.view_password);
 		
 		
 		
 		// 对图标进行初始化操作
 		passwordEt_person.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
 		passwordEt_person_mail.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
 		passwordEt_organization.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
 		view_password_person.setBackgroundResource(R.drawable.show_password_bg_normal);
 		view_password_person_mail.setBackgroundResource(R.drawable.show_password_bg_normal);
 		view_password_organization.setBackgroundResource(R.drawable.show_password_bg_normal);
 		// 个人注册根据点击改变图标状态
 		view_password_person.setOnClickListener(new OnClickListener() {

 			@Override
 			public void onClick(View v) {
 				if (flag) {
 					// 以点点的形式显示密码
 					passwordEt_person.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
 					passwordEt_person.setSelection(passwordEt_person.getText().toString().length());
 					view_password_person.setBackgroundResource(R.drawable.show_password_bg_pressed);
 					flag = false;

 				}
 				else {
 					// 不以以点点的形式显示密码
 					passwordEt_person.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
 					passwordEt_person.setSelection(passwordEt_person.getText().toString().length());
 					view_password_person.setBackgroundResource(R.drawable.show_password_bg_normal);
 					flag = true;
 				}
 			}
 		});
 		// 个人邮箱注册根据点击改变图标状态
 		view_password_person_mail.setOnClickListener(new OnClickListener() {
 			
 			@Override
 			public void onClick(View v) {
 				if (flag) {
 					// 以点点的形式显示密码
 					passwordEt_person_mail.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
 					passwordEt_person_mail.setSelection(passwordEt_person_mail.getText().toString().length());
 					view_password_person_mail.setBackgroundResource(R.drawable.show_password_bg_pressed);
 					flag = false;
 					
 				}
 				else {
 					// 不以以点点的形式显示密码
 					passwordEt_person_mail.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
 					passwordEt_person_mail.setSelection(passwordEt_person_mail.getText().toString().length());
 					view_password_person_mail.setBackgroundResource(R.drawable.show_password_bg_normal);
 					flag = true;
 				}
 			}
 		});
 		// 组织注册根据点击改变图标状态
 		view_password_organization.setOnClickListener(new OnClickListener() {
 			
 			@Override
 			public void onClick(View v) {
 				if (flag) {
 					// 以点点的形式显示密码
 					passwordEt_organization.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
 					passwordEt_organization.setSelection(passwordEt_organization.getText().toString().length());
 					view_password_organization.setBackgroundResource(R.drawable.show_password_bg_pressed);
 					flag = false;
 					
 				}
 				else {
 					// 不以以点点的形式显示密码
 					passwordEt_organization.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
 					passwordEt_organization.setSelection(passwordEt_organization.getText().toString().length());
 					view_password_organization.setBackgroundResource(R.drawable.show_password_bg_normal);
 					flag = true;
 				}
 			}
 		});

 		//个人电话注册删除密码
 		delete_password_person.setOnClickListener(new OnClickListener() {
 			@Override
 			public void onClick(View v) {
 				passwordEt_person.setText("");
 			}
 		});
 		//个人邮箱注册删除密码
 		delete_password_person_mail.setOnClickListener(new OnClickListener() {
 			@Override
 			public void onClick(View v) {
 				passwordEt_person_mail.setText("");
 			}
 		});
 		//组织邮箱注册删除密码
 		delete_password_organization.setOnClickListener(new OnClickListener() {
 			@Override
 			public void onClick(View v) {
 				passwordEt_organization.setText("");
 			}
 		});

 		//个人电话用户密码框
 		passwordEt_person.addTextChangedListener(new TextWatcher() {
 			@Override
 			public void onTextChanged(CharSequence s, int start, int before, int count) {
 			}

 			@Override
 			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
 			}

 			@Override
 			public void afterTextChanged(Editable s) {
 				if (!StringUtils.isEmpty(s.toString()) && s.length() >= 6 && s.length() <= 19) {
 					delete_password_person.setVisibility(View.VISIBLE);
 					hasPassword = true;
 				}
 				else {
 					delete_password_person.setVisibility(View.INVISIBLE);
 					hasPassword = false;
 					if (s.length() > 19) {
 						Toast.makeText(RegisterGintongAllPathActivity.this, "请输入6-19位密码", 0).show();
 						delete_password_person.setVisibility(View.VISIBLE);
 					}
 				}
 				if (hasPhoneCall == true && hasVerifyCode == true && hasPassword == true) {
 					register_gintong_account_person.setClickable(true);
 					register_gintong_account_person.setBackgroundResource(R.drawable.sign_in);
 				}
 				else {
 					register_gintong_account_person.setClickable(false);
 					register_gintong_account_person.setBackgroundResource(R.drawable.sign_in_normal);
 				}
 			}
 		});
 		//个人邮箱用户
 		passwordEt_person_mail.addTextChangedListener(new TextWatcher() {
 			@Override
 			public void onTextChanged(CharSequence s, int start, int before, int count) {
 			}
 			
 			@Override
 			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
 			}
 			
 			@Override
 			public void afterTextChanged(Editable s) {
 				if (!StringUtils.isEmpty(s.toString()) && s.length() >= 6 && s.length() <= 19) {
 					delete_password_person_mail.setVisibility(View.VISIBLE);
 					hasOrgPassword = true;
 				}
 				else {
 					delete_password_person_mail.setVisibility(View.INVISIBLE);
 					hasOrgPassword = false;
 					if (s.length() > 19) {
 						Toast.makeText(RegisterGintongAllPathActivity.this, "请输入6-19位密码", 0).show();
 						delete_password_person_mail.setVisibility(View.VISIBLE);
 					}
 				}
 				if (hasOrgMail == true && hasOrgPassword == true) {
 					register_gintong_account_person_mail.setClickable(true);
 					register_gintong_account_person_mail.setBackgroundResource(R.drawable.sign_in);
 				}
 				else {
 					register_gintong_account_person_mail.setClickable(false);
 					register_gintong_account_person_mail.setBackgroundResource(R.drawable.sign_in_normal);
 				}
 			}
 		});
 		//组织用户
 		passwordEt_organization.addTextChangedListener(new TextWatcher() {
 			@Override
 			public void onTextChanged(CharSequence s, int start, int before, int count) {
 			}
 			
 			@Override
 			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
 			}
 			
 			@Override
 			public void afterTextChanged(Editable s) {
 				if (!StringUtils.isEmpty(s.toString()) && s.length() >= 6 && s.length() <= 19) {
 					delete_password_organization.setVisibility(View.VISIBLE);
 					hasOrgPassword = true;
 				}
 				else {
 					delete_password_organization.setVisibility(View.INVISIBLE);
 					hasOrgPassword = false;
 					if (s.length() > 19) {
 						Toast.makeText(RegisterGintongAllPathActivity.this, "请输入6-19位密码", 0).show();
 						delete_password_organization.setVisibility(View.VISIBLE);
 					}
 				}
 				if (hasOrgMail == true && hasOrgName == true && hasOrgPassword == true) {
 					register_gintong_account_organization.setClickable(true);
 					register_gintong_account_organization.setBackgroundResource(R.drawable.sign_in);
 				}
 				else {
 					register_gintong_account_organization.setClickable(false);
 					register_gintong_account_organization.setBackgroundResource(R.drawable.sign_in_normal);
 				}
 			}
 		});
 		
 		register_gintong_account_person.setClickable(false);
 		register_gintong_account_organization.setClickable(false);
 		
 		organization_name = (EditText) container.findViewById(R.id.organization_name);
 		delelte_organization_name = (ImageView) container.findViewById(R.id.delelte_organization_name);
 		delelte_organization_name.setOnClickListener(mClickListener);
 		organization_name.setOnFocusChangeListener(this);
		
		
		View person_accountEt = container.findViewById(R.id.person_accountEt);
 		View organzation_accountEt = container.findViewById(R.id.organzation_accountEt);
 		delelte_email_content_person = (ImageView) person_accountEt.findViewById(R.id.delelte_email_content);
 		delelte_email_content_person.setOnClickListener(mClickListener);
 		delelte_email_content_organization = (ImageView) organzation_accountEt.findViewById(R.id.delelte_email_content);
 		delelte_email_content_organization.setOnClickListener(mClickListener);
 		accountEt_person = (AutoCompeleteEmailSuffixEditText) person_accountEt.findViewById(R.id.accountEt);
 		accountEt_orangization = (AutoCompeleteEmailSuffixEditText) organzation_accountEt.findViewById(R.id.accountEt);
 		accountEt_orangization.setOnFocusChangeListener(this);
 		accountEt_person.setOnFocusChangeListener(this);
		
 		//个人邮箱注册框文字变化监听
 		accountEt_person.addTextChangedListener(new TextWatcher() {
 			@Override
 			public void onTextChanged(CharSequence s, int start, int before, int count) {
 			}
 			
 			@Override
 			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
 			}
 			
 			@Override
 			public void afterTextChanged(Editable s) {
 				if (!StringUtils.isEmpty(s.toString())) {
 					delelte_email_content_person.setVisibility(View.VISIBLE);
 					hasOrgMail = true;
 				}
 				else {
 					delelte_email_content_person.setVisibility(View.INVISIBLE);
 					hasOrgMail = false;
 				}
 				if (hasOrgMail == true && hasOrgPassword == true) {
 					register_gintong_account_organization.setClickable(true);
 					register_gintong_account_organization.setBackgroundResource(R.drawable.sign_in);
 				}
 				else {
 					register_gintong_account_organization.setClickable(false);
 					register_gintong_account_organization.setBackgroundResource(R.drawable.sign_in_normal);
 				}
 			}
 		});
 		
 		//组织邮箱注册监听
 		accountEt_orangization.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!StringUtils.isEmpty(s.toString())) {
					delelte_email_content_organization.setVisibility(View.VISIBLE);
					hasOrgMail = true;
				}
				else {
					delelte_email_content_organization.setVisibility(View.INVISIBLE);
					hasOrgMail = false;
				}
				if (hasOrgMail == true && hasOrgPassword == true && hasOrgName == true) {
					register_gintong_account_organization.setClickable(true);
					register_gintong_account_organization.setBackgroundResource(R.drawable.sign_in);
				}
				else {
					register_gintong_account_organization.setClickable(false);
					register_gintong_account_organization.setBackgroundResource(R.drawable.sign_in_normal);
				}
			}
		});
 		
 		organization_name.addTextChangedListener(new TextWatcher() {
 			@Override
 			public void onTextChanged(CharSequence s, int start, int before, int count) {
 			}
 			
 			@Override
 			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
 			}
 			
 			@Override
 			public void afterTextChanged(Editable s) {
 				if (!StringUtils.isEmpty(s.toString())) {
 					delelte_organization_name.setVisibility(View.VISIBLE);
 					hasOrgName = true;
 				}
 				else {
 					delelte_organization_name.setVisibility(View.INVISIBLE);
 					hasOrgName = false;
 				}
 				if (hasOrgMail == true && hasOrgName == true && hasOrgPassword == true) {
 					register_gintong_account_organization.setClickable(true);
 					register_gintong_account_organization.setBackgroundResource(R.drawable.sign_in);
 				}
 				else {
 					register_gintong_account_organization.setClickable(false);
 					register_gintong_account_organization.setBackgroundResource(R.drawable.sign_in_normal);
 				}
 			}
 		});
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
				hasPhoneCall = true;
			}
			else {
				delete_phone_call.setVisibility(View.GONE);
				vcodeTv.setClickable(false);
				hasPhoneCall = false;
			}
			if (hasPhoneCall == true && hasVerifyCode == true && hasPassword == true) {
				register_gintong_account_person.setClickable(true);
				register_gintong_account_person.setBackgroundResource(R.drawable.sign_in);
			}
			else {
				register_gintong_account_person.setClickable(false);
				register_gintong_account_person.setBackgroundResource(R.drawable.sign_in_normal);
			}
		}
	};

	// 检查信息的完整性（是否显示下一步按钮）
		private boolean infoIntegrityCheck() {

			if (EUtil.isMobileNO(region_number.getText().toString().trim(), 
					phone_call.getText().toString())) { // 手机号和验证码----//后端决定此处由后台验证
				// 验证码的正确性
				if (vcodeEt.getText().toString().length() <= 0) {
					Toast.makeText(RegisterGintongAllPathActivity.this, TIP_EMPTY_VCODE, 0).show();
					return false;
				}
				// if (!mVerifyCode.equals(vcodeEt.getText().toString())) {
				// showToast(TIP_WRONG_VERIFY_CODE);
				// return false;
				// }
			}
			else if (EUtil.isEmail(accountEt_person.getText().toString())) {
				return true;
			}
			else {
				Toast.makeText(RegisterGintongAllPathActivity.this, TIP_ILLEGAL_ACCOUNT, 0).show();
				return false;
			}
			return true;
		}
		/*
		 * // 检查信息的合法性（只检查邮箱格式，手机号错误不会显示下一步按钮） private boolean infoLegalityCheck(){
		 * if(EUtil.isEmail(phone_call.getText().toString())){ return true; } return
		 * false; }
		 */

		// 点击事件监听器
		private OnClickListener mClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v == vcodeTv) { // 开始倒计时
					// 获取验证码
					if (vcodeTv.getText().equals(TIP_GET_VERIFY_CODE)) {
						// 验证手机号或邮箱是否正确
						if (EUtil.isMobileNO(region_number.getText().toString().trim(), phone_call.getText().toString())) { // 手机号和验证码----//后端决定此处由后台验证
							showLoadingDialog("");
							UserReqUtil.doGetVerifyCode(RegisterGintongAllPathActivity.this, mBindData, UserReqUtil.getDoGetVerifyCodeParams(0, region_number.getText().toString().trim(), phone_call.getText().toString()), null);
						}else {
							if (StringUtils.isEmpty(phone_call.getText().toString())) {
								//什么也不做
							} else {
								Toast.makeText(RegisterGintongAllPathActivity.this, "您填写的号码格式不正确", 0).show();
							}
						}
					}
					else if (vcodeTv.getText().equals(TIP_REGET_VERIFY_CODE)) { // 重获验证码
	 
						// 不再需要提示，直接显示加载框
						showLoadingDialog("");
						// 发送获取验证码请求
						UserReqUtil.doGetVerifyCode(RegisterGintongAllPathActivity.this, mBindData, UserReqUtil.getDoGetVerifyCodeParams(0, region_number.getText().toString().trim(), phone_call.getText().toString()), null);
					}
				}
				if (v == region_number) {
					ENavigate.startCountryCodeActivity(RegisterGintongAllPathActivity.this, mListCountry, 2056);
				}
				if (v == delete_phone_call) {
					phone_call.setText("");
				}
				if (v == register_gintong_account_person) {
					nextStep("personphone");
				}
				if (v == register_gintong_account_organization) {
					nextStep("organization");
				}
				if (v == register_gintong_account_person_mail) {
					nextStep("personmail");
				}
				if (v == gintong_aggreement_person) {
					ENavigate.startAgreementActivity(RegisterGintongAllPathActivity.this);
				}
				if (v == gintong_aggreement_person_mail) {
					ENavigate.startAgreementActivity(RegisterGintongAllPathActivity.this);
				}
				if (v == gintong_aggreement_orangization) {
					ENavigate.startAgreementActivity(RegisterGintongAllPathActivity.this);
				}
				if (v == register_by_email_ll) {
					person.setVisibility(View.GONE);
					personmail.setVisibility(View.VISIBLE);
					is_mail_register = true;
					is_phone_register = false;
				}
				if (v == register_gintong_by_phone) {
					person.setVisibility(View.VISIBLE);
					personmail.setVisibility(View.GONE);
					is_phone_register = true;
					is_mail_register = false;
				}
				if (v == delelte_email_content_organization) {
					accountEt_orangization.setText("");
				}
				if (v == delelte_email_content_person) {
					accountEt_person.setText("");
				}
				if (v == delelte_organization_name) {
					organization_name.setText("");
				}
			
				
			}
		};
		
		/** 下一步 */
	private void nextStep(String type) {
		if (type.equals("personphone")) {
			if (!infoIntegrityCheck()) {
				return;
			}
			if (EUtil.isMobileNO(region_number.getText().toString().trim(),phone_call.getText().toString())) { // 手机注册

				// 显示加载框
				showLoadingDialog("");
				// 是否有SessionID
				if (mMainApp.getAppData().getSessionID().length() <= 0) {
					UserReqUtil.doLoginConfiguration(RegisterGintongAllPathActivity.this,mBindData,UserReqUtil.getDoLoginConfigurationParams("","",EUtil.getDeviceID(RegisterGintongAllPathActivity.this),EUtil.getAppVersionName(RegisterGintongAllPathActivity.this),"", "", "", "android", "", "", "",""), null);
				} else {
					UserReqUtil.doRegister(RegisterGintongAllPathActivity.this, mBindData,UserReqUtil.getDoRegisterParams(phone_call.getText().toString(), "",passwordEt_person.getText().toString(),vcodeEt.getText().toString(), 1,region_number.getText().toString().substring(1)), null);
				}
			} else {
				Toast.makeText(RegisterGintongAllPathActivity.this,"请输入正确的手机号", 0).show();
			}
		}else if (type.equals("personmail")) {

			if (!infoIntegrityCheck()) {
				return;
			}

			if (EUtil.isEmail(accountEt_person.getText().toString())) { // 邮箱注册

				// 显示加载框
				showLoadingDialog("");
				// 是否有SessionID
				if (mMainApp.getAppData().getSessionID().length() <= 0) {
					UserReqUtil.doLoginConfiguration(RegisterGintongAllPathActivity.this, mBindData,UserReqUtil.getDoLoginConfigurationParams("", "", EUtil.getDeviceID(RegisterGintongAllPathActivity.this), EUtil.getAppVersionName(RegisterGintongAllPathActivity.this), "", "", "", "android", "", "", "", ""), null);
				}
				else {
					UserReqUtil.doRegister(RegisterGintongAllPathActivity.this, mBindData, UserReqUtil.getDoRegisterParams("", "" ,accountEt_person.getText().toString(), passwordEt_person_mail.getText().toString(), "", 1, ""), null);
				}
			}
			else {
				Toast.makeText(RegisterGintongAllPathActivity.this, "请输入正确的手机号或邮箱", 0).show();
			}
		
		}
		else {
			if (EUtil.isEmail(accountEt_orangization.getText().toString())) { // 组织注册
				if (CommonUtils.isContainChinese(passwordEt_organization.getText().toString())) {
 					Toast.makeText(RegisterGintongAllPathActivity.this, "密码中不能使用中文字符", 0).show();
 					return;
 				}
				UserReqUtil.doJudgeUserAndMail(RegisterGintongAllPathActivity.this, mBindData, UserReqUtil.getDoJudgeUserandMail(accountEt_orangization.getText().toString(), organization_name.getText().toString()), null);
//				showDialog();
				
			}else {
					Toast.makeText(RegisterGintongAllPathActivity.this, "请输入正确的邮箱", 0).show();
				  }
		}
	}
		
	// 接口回调函数
	private IBindData mBindData = new IBindData() {

		@Override
		public void bindData(int type, Object object) {
			dismissLoadingDialog();
			if (type == EAPIConsts.ReqType.SEND_VALIDATE_EMAIL) { // 发送验证邮件
				// 跳转邮件验证页面
				if (object != null) {
					// 初始化全局变量
					DataBox dataBox = (DataBox) object;
					if (dataBox.mIsSuccess) {
						Intent intent = new Intent(RegisterGintongAllPathActivity.this, SendVerifyEmailActivity.class);
						intent.putExtra(EConsts.Key.EMAIL, accountEt_person.getText().toString());
						startActivity(intent);
						finish();
					}
				}
			}
			else if (type == EAPIConsts.ReqType.GET_VERIFY_CODE) { // 获取验证码
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
						// 显示消息
						Toast.makeText(RegisterGintongAllPathActivity.this, TIP_GET_VERIFY_CODE_SUCCESS, 0).show();
					}
				}
			}
			if (type == EAPIConsts.ReqType.FINDORG) { // 请求组织信息
				// 是否进行下一步操作
				dismissLoadingDialog();
				// 跳转到个人信息完善界面
				if (object != null) {
					// 初始化全局变量
					DataBox dataBox = (DataBox) object;
					Intent intent = new Intent(RegisterGintongAllPathActivity.this, CreateOrganizationActivity.class);
					intent.putExtra(EConsts.Key.ORGINFOVO, dataBox.orgInfoVo);
					// 跳转到组织完善信息页面
					startActivity(intent);
				}
			}
			if (type == EAPIConsts.ReqType.JUDGE_USERANDMAIL) { // 判断组织全称和组织邮箱是否被注册
				// 是否进行下一步操作
				dismissLoadingDialog();
				// 跳转到个人信息完善界面
				if (object != null) {
					// 初始化全局变量
					DataBox dataBox = (DataBox) object;
					if (dataBox.mIsSuccess) {
						showDialog();
					} else {
						Toast.makeText(RegisterGintongAllPathActivity.this, dataBox.mMessage, 1).show();
						return;
					}
				}
			}
			if (type == EAPIConsts.ReqType.REGISTER) { // 注册成功
				// 是否进行下一步操作
				dismissLoadingDialog();
				// 跳转到信息完善界面
				if (object != null) {
					// 初始化全局变量
					DataBox dataBox = (DataBox) object;
					mMainApp.getAppData().setUser(dataBox.mJTMember);
					if (dataBox.mJTMember.mRole == 0) {
						// 发送验证邮件
						if(hasOrgMail&& hasOrgPassword){//个人邮箱注册
							if (dataBox.mJTMember.userStatus == 0) {
								showLoadingDialog("");
								UserReqUtil.doSendValidateEmail(RegisterGintongAllPathActivity.this, mBindData, UserReqUtil.getDoSendValidateEmailParams(accountEt_person.getText().toString()), null);
							}
						}else{
							// 跳转到完善个人信息页面
							startActivity(new Intent(RegisterGintongAllPathActivity.this, RegisterPersonalDetailActivity.class));
							finish();
						}
					} else {
						// 跳转到组织完善信息页面
						UserReqUtil.doFindOrg(RegisterGintongAllPathActivity.this, mBindData, dataBox.mJTMember.mOrganizationInfo.mLegalPersonIDCardImage, null);
					}
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
					if (EUtil.isMobileNO(region_number.getText().toString().trim(), phone_call.getText().toString())) { // 手机注册
						UserReqUtil.doRegister(
								RegisterGintongAllPathActivity.this,
								mBindData,
								UserReqUtil.getDoRegisterParams(phone_call.getText().toString(), "", passwordEt_person.getText().toString(), vcodeEt.getText().toString(), 1, region_number.getText()
										.toString().substring(1)), null);
					}
					if(hasOrgMail&& hasOrgPassword){//个人邮箱注册
						showLoadingDialog("");
						UserReqUtil.doRegister(RegisterGintongAllPathActivity.this, mBindData, UserReqUtil.getDoRegisterParams("", "" ,accountEt_person.getText().toString(), passwordEt_person_mail.getText().toString(), "", 1, ""), null);
					}
					if (hasOrgMail&&hasOrgPassword&&hasOrgName) {//hasOrgMail == true && hasOrgName == true && hasOrgPassword == true
						showLoadingDialog("");
						UserReqUtil.doRegister(RegisterGintongAllPathActivity.this, mBindData, UserReqUtil.getDoRegisterParams("","", accountEt_orangization.getText().toString(), passwordEt_organization.getText().toString(), "", 2,organization_name.getText().toString()), null);
					}
				}
			}
		}
	};	
		// 消息处理器
		@SuppressLint("HandlerLeak")
		private Handler mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case MSG_COUNT_DOWN: // 倒计时消息
					if (mCountdownLeft <= 0) { // 倒计时结束
						// 取消倒计时任务
						if (mTimer != null) {
							mTimer.cancel();
							mTimer = null;
						}
						// 更改态验证码按钮状态和文字
						vcodeTv.setText(TIP_REGET_VERIFY_CODE);
						countSeconds.setVisibility(View.GONE);
						vcodeTv.setEnabled(true);
					}
					else { // 倒计时仍在进行
						countSeconds.setVisibility(View.VISIBLE);
						vcodeTv.setVisibility(View.VISIBLE);
						countSeconds.setText("" + mCountdownLeft);
						vcodeTv.setText("秒后可重发");
					}
					break;
				}
			}
		};
		
		
		public void showLoadingDialog(String message) {
			if (null == mProgressDialog) {
				mProgressDialog = new EProgressDialog(RegisterGintongAllPathActivity.this);
				mProgressDialog
						.setOnCancelListener(new DialogInterface.OnCancelListener() {
							@Override
							public void onCancel(DialogInterface dialogInterface) {
								KeelLog.d(TAG, "mProgressDialog.onCancel");
								// onLoadingDialogCancel();//如果取消对话框， 结束当前activity
							}
						});
			}
			mProgressDialog.setMessage(message);
			//加载进度条show判断 如果已经是show状态 就不再调show方法
//			if (!mProgressDialog.isShowing()) {
				mProgressDialog.show();
//			}
		}
		

		public void dismissLoadingDialog() {
			try{
				if (mProgressDialog != null && mProgressDialog.isShowing()) {
					mProgressDialog.dismiss();
				}
			}catch(Exception e){
				
			}
		}

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (v == organization_name) {
				if (TextUtils.isEmpty(organization_name.getText())) {
				}else {
					if (!hasFocus) {
						hasOrgName = true;
//						Toast.makeText(RegisterGintongAllPathActivity.this, "检测组织名称是否已经注册", 0).show();
					}
				}
			}
			if (v == accountEt_orangization) {
				if (TextUtils.isEmpty(accountEt_orangization.getText())) {
				}else {
					if (!hasFocus) {
						hasOrgMail = true;
//						Toast.makeText(RegisterGintongAllPathActivity.this, "检测组织邮箱是否已经注册", 0).show();
					}
				}
			}
			if (v == accountEt_person) {
				if (TextUtils.isEmpty(accountEt_person.getText())) {
				}else {
					if (!hasFocus) {
						hasOrgMail = true;
//						Toast.makeText(RegisterGintongAllPathActivity.this, "检测个人邮箱是否已经注册", 0).show();
					}
				}
			}
			
		}

	/**
	 * 
	 * 信息确定对话框
	 * @param dataBox
	 */
	private void showDialog() {
		final OrganizationInfoAlertDialog infoAlertDialog = new OrganizationInfoAlertDialog(RegisterGintongAllPathActivity.this);
		infoAlertDialog.setTipTv(HINT_MESG);
		infoAlertDialog.setOnDialogClickListener(new OnDialogClickListener() {

			@Override
			public void okTv() {
				// 显示加载框
				showLoadingDialog("");
				// 是否有SessionID
				if (mMainApp.getAppData().getSessionID().length() <= 0) {
					UserReqUtil.doLoginConfiguration(RegisterGintongAllPathActivity.this, mBindData,UserReqUtil.getDoLoginConfigurationParams("", "", EUtil.getDeviceID(RegisterGintongAllPathActivity.this), EUtil.getAppVersionName(RegisterGintongAllPathActivity.this), "", "", "", "android", "", "", "", ""), null);
				}
				else {
					UserReqUtil.doRegister(RegisterGintongAllPathActivity.this, mBindData, UserReqUtil.getDoRegisterParams("","", accountEt_orangization.getText().toString(), passwordEt_organization.getText().toString(), "", 2,organization_name.getText().toString()), null);
				}
				infoAlertDialog.dismiss();
			}

			@Override
			public void cancelTv() {
				infoAlertDialog.dismiss();
			}
		});
		infoAlertDialog.show();
	}
}
