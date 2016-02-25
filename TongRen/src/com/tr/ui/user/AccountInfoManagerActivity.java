package com.tr.ui.user;

import java.util.HashMap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;

import com.tr.App;
import com.tr.R;
import com.tr.api.UserReqUtil;
import com.tr.model.api.DataBox;
import com.tr.model.user.JTMember;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;
import com.utils.time.Util;

/**
 * 账号信息页面
 * 
 * @author cui
 * 
 */
public class AccountInfoManagerActivity extends JBaseActivity {
	// 控件
	private TextView tvPhoneNum;
	private TextView tvEmailNum;
	private TextView qqName;
	private TextView wbName;
	private Button btPhone;
	private Button btEmail;
	private Button btQQ;
	private Button btWb;

	private Platform sinaWb;
	private Platform qq;
	// 变量
	private App mMainApp;
	private boolean hasPhoneNum = false;
	private boolean hasEmailNum = false;
	private boolean hasQq = false;
	private boolean hasSinaWb = false;

	private int platFormType; // 第三方平台 QQ 100/新浪微博 200
	private String userId, token;// QQ/sina微博的userid和Token
	private static final int MSG_AUTH_COMPLETE = 11;
	private static final int MSG_AUTH_ERROR = 12;
	private static final int MSG_AUTH_CANCEL = 13;

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "帐号信息", false, null, false, true);
//		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_info_manager);
		initViews();
		// 全局对象
		mMainApp = App.getApp();
//		showLoadingDialog();
		initData();
		doGetAccountInfo();// 请求数据
	}

	@Override
	public void onResume() {
		super.onResume();
		if(mMainApp.refresh_accountInfo)
		doGetAccountInfo();// 请求数据
	}

	/**
	 * 请求账号信息数据
	 */
	private void doGetAccountInfo() {
		showLoadingDialog();
		UserReqUtil.doRefresh_AccountInfo(AccountInfoManagerActivity.this,
				mBindData, null);
	}

	private void initViews() {
		////150723新添组织能进账号信息页面,但只显示更改密码项
		LinearLayout layoutOther = (LinearLayout) findViewById(R.id.accountInfo_layout_other);// 除密码之外的账号信息布局
		if (App.getUser().getmUserType() == JTMember.UT_PERSON) {
			layoutOther.setVisibility(View.VISIBLE);
		} else {
			layoutOther.setVisibility(View.GONE);

		}
		// 密码
		RelativeLayout rlPassword = (RelativeLayout) findViewById(R.id.rl_password_info);
		rlPassword.setOnClickListener(mClickListener);
		btPhone = (Button) findViewById(R.id.bt_phone_bing);
		btPhone.setOnClickListener(mClickListener);
		tvPhoneNum = (TextView) findViewById(R.id.tv_phone_num);

		btEmail = (Button) findViewById(R.id.bt_email_bing);
		btEmail.setOnClickListener(mClickListener);
		tvEmailNum = (TextView) findViewById(R.id.tv_email_num);

		btQQ = (Button) findViewById(R.id.bt_qq_bing);
		btQQ.setOnClickListener(mClickListener);
		qqName = (TextView) findViewById(R.id.tv_qq_name);
		btWb = (Button) findViewById(R.id.bt_wb_bign);
		btWb.setOnClickListener(mClickListener);
		wbName = (TextView) findViewById(R.id.tv_wb_name);

	}

	private void updateView(Button v, int resid) {
		if (resid == R.drawable.bing_bg) {
			v.setText("更改");

		} else
			v.setText("绑定");
		v.setBackgroundResource(resid);
	}

	private void updateView(TextView v, int visibility, Button bt, int resid) {
		if (visibility == View.VISIBLE && bt == btEmail) {
			tvEmailNum.setText(App.getUser().getmEmail());
		}
		if (visibility == View.VISIBLE && bt == btPhone) {
			tvPhoneNum.setText(App.getUser().getmMobile());
		}

		v.setVisibility(visibility);
		updateView(bt, resid);
	}

	private void updateView(Button bt,TextView view,String name, boolean is) {
		// 判断是否授权成功
		if (is) {
			updateView(bt, R.drawable.bing_bg);
//			view.setText(name);
//			view.setVisibility(View.VISIBLE);
			bt.setText("解绑");
		} else {
			updateView(bt, R.drawable.unbing_bg);
			view.setText("");
			view.setVisibility(View.GONE);
			bt.setText("绑定");
		}
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		if (!StringUtils.isEmpty(App.getUser().getmEmail())) {
			hasEmailNum = true;
			updateView(tvEmailNum, View.VISIBLE, btEmail, R.drawable.bing_bg);
		} else {
			hasEmailNum = false;
			updateView(tvEmailNum, View.GONE, btEmail, R.drawable.unbing_bg);
		}

		if (!StringUtils.isEmpty(App.getUser().getmMobile())) {
			hasPhoneNum = true;
			updateView(tvPhoneNum, View.VISIBLE, btPhone, R.drawable.bing_bg);
		} else {
			hasPhoneNum = false;
			updateView(tvPhoneNum, View.GONE, btPhone, R.drawable.unbing_bg);
		}
		if (!StringUtils.isEmpty(App.getUser().getmQqlogin())) {
			hasQq = true;
		} else {
			hasQq = false;
		}
		if (!StringUtils.isEmpty(App.getUser().getmSinalogin())) {
			hasSinaWb = true;
		} else {
			hasSinaWb = false;
		}
		sinaWb = ShareSDK.getPlatform(SinaWeibo.NAME);
		if(!hasSinaWb){
			sinaWb.removeAccount(true);
		}
		updateView(btWb,wbName,sinaWb.getDb().getUserName(), hasSinaWb);

		qq = ShareSDK.getPlatform(QQ.NAME);
		if(!hasQq){
			qq.removeAccount(true);
		}
		updateView(btQQ, qqName,qq.getDb().getUserName(),hasQq);

	}

	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			// 修改密码
			case R.id.rl_password_info:
				ENavigate
						.startChangePasswordActivity(AccountInfoManagerActivity.this);
				break;
			// 绑定/更改手机号
			case R.id.bt_phone_bing:
				if (hasPhoneNum) {
					// 跳转到更改原手机号
					ENavigate
							.startUnBindingMobileActivity(AccountInfoManagerActivity.this,App.getUser().getmMobile());

				} else {
					// 跳转到绑定手机号页面
					ENavigate.startBindingMobileActivity(
							AccountInfoManagerActivity.this, 1);
				}
				break;
			// 绑定/更改邮箱
			case R.id.bt_email_bing:
				if (hasEmailNum) {
					// 已绑定邮箱 进行解绑, 请求数据,进行发送验证邮件
					showLoadingDialog();
					UserReqUtil.doSet_SendValidateEmail(
							AccountInfoManagerActivity.this, mBindData, "1",
							App.getUser().getmEmail(),"1", null);
				} else {
					// 未绑定邮箱 ，跳转到绑定邮箱页面
					ENavigate.startBindingEmailActivity(
							AccountInfoManagerActivity.this, 1);
				}
				break;
			// 绑定/解绑QQ
			case R.id.bt_qq_bing:
				platFormType = 100;
				authorize(qq);
				break;
			// 绑定/解绑新浪微博
			case R.id.bt_wb_bign:
				platFormType = 200;
				authorize(sinaWb);
				break;

			default:
				break;
			}
		}

	};

	/**
	 * 执行授权,获取用户信息
	 * 
	 * @param plat
	 */
	private void authorize(Platform plat) {
		// 判断指定平台是否已经完成授权
		if (hasQq&&platFormType==100) {
			showMyDialog("关闭后，将解除QQ账号绑定", plat, btQQ, "1");
			return;
		}
		if (hasSinaWb&&platFormType==200) {
			showMyDialog("关闭后，将解除微博绑定", plat, btWb, "2");
			return;
		}
		MyPlatformActionListener listener = new MyPlatformActionListener();
		plat.setPlatformActionListener(listener);
		String name = plat.getName();
		if ("QQ".equals(name))
			// true不使用SSO授权，false使用SSO授权
			plat.SSOSetting(false);
		else
			// 微博不使用客户端登录（登录得升级SDK）
			plat.SSOSetting(true);
		// 执行登录，登录后在回调里面获取用户资料
		plat.showUser(null);
		// plat.showUser(“3189087725”);//获取账号为“3189087725”的资料
	}

	/**
	 * QQ/Sina微博是否解绑对话框
	 * 
	 * @param msg
	 *            内容
	 * @param plat
	 *            平台
	 * @param bt
	 *            按钮
	 * @param status
	 *            1:qq解绑 2：微博解绑
	 */
	private void showMyDialog(String msg, final Platform plat, final Button bt,
			final String status) {
		LayoutInflater inflater = LayoutInflater
				.from(AccountInfoManagerActivity.this);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.dialog_binding_qq_weibo, null);
		TextView content = (TextView) layout.findViewById(R.id.content_tv);
		content.setText(msg);
		final Dialog dialog = new AlertDialog.Builder(
				AccountInfoManagerActivity.this).create();//
		dialog.show();
		dialog.getWindow().setContentView(layout);
		layout.findViewById(R.id.negative).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 取消
						dialog.dismiss();
					}
				});
		layout.findViewById(R.id.positive).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 确定
						// 已授权，请求解绑
						showLoadingDialog();
						UserReqUtil.doUnBindingQQ_WB(
								AccountInfoManagerActivity.this, mBindData,
								status, null);
						dialog.dismiss();

					}
				});
	}

	/**
	 * 自定义带图标、显示在中间的toast
	 */
	private void showCompleteToast(String msg) {
		Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);// 设置位置为中间
		LinearLayout layout = (LinearLayout) toast.getView();
		ImageView imageView = new ImageView(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(10, 10, 10, 10);
		params.gravity=Gravity.CENTER;
		imageView.setImageResource(R.drawable.pass);
		layout.addView(imageView, 0, params);
		toast.show();
	}

	/**
	 * 
	 * 授权登陆监听
	 */
	private class MyPlatformActionListener implements PlatformActionListener {

		@Override
		public void onCancel(Platform platform, int action) {
			// TODO 取消授权
			Message msg = new Message();
			msg.what = MSG_AUTH_CANCEL;
			handler.sendMessage(msg);

		}

		@Override
		public void onComplete(Platform platform, int action,
				HashMap<String, Object> res) {
			showLoadingDialog();
			// 解析部分用户资料字段
			if (action == Platform.ACTION_USER_INFOR) {
				userId = platform.getDb().getUserId();// ID
				token = platform.getDb().getToken();
				Message msg = new Message();
				msg.what = MSG_AUTH_COMPLETE;
				handler.sendMessage(msg);
			}

		}

		@Override
		public void onError(Platform platform, int action, Throwable t) {
			// TODO 授权失败
			platform.removeAccount();
			Message msg = new Message();
			msg.what = MSG_AUTH_ERROR;
			handler.sendMessage(msg);
		}

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_AUTH_COMPLETE:
//				showLoadingDialog();
				// TODO 本地绑定成功，应告诉后台绑定XX
				UserReqUtil.doBindingQQ_WB(AccountInfoManagerActivity.this,
						mBindData, token, userId, platFormType, mMainApp
								.getAppData().getSessionID(), null);
				break;
			case MSG_AUTH_CANCEL:
				hasQq = false;
				hasSinaWb = false;
//				Util.toast(AccountInfoManagerActivity.this, "取消绑定", false);
				break;
			case MSG_AUTH_ERROR:
				hasQq = false;
				hasSinaWb = false;
				Util.toast(AccountInfoManagerActivity.this, "绑定失败", false);
				break;
			}
		};
	};

	// 接口回调函数
	private IBindData mBindData = new IBindData() {

		@Override
		public void bindData(int type, Object object) {
			if (isLoadingDialogShowing()) {
				dismissLoadingDialog();
			}
			// 刷新，获取账号信息
			if (type == EAPIConsts.ReqType.SET_REFRESH_ACCOUNT_INFO) {
				mMainApp.refresh_accountInfo=false;
				// 处理数据
				if (object != null) {
					DataBox dataBox = (DataBox) object;
					if (dataBox.mJTMember != null) {
						mMainApp.getAppData().setUser(dataBox.mJTMember);
						initData();
					}
				}
			}
			// 解绑邮箱，发送验证邮箱
			if (type == EAPIConsts.ReqType.SEND_VALIDATE_EMAIL) {
				// 处理数据
				if (object != null) {
					DataBox dataBox = (DataBox) object;

					if (dataBox.mIsSuccess) {
						// TODO 跳转到更改绑定邮箱页面
						ENavigate.startUnBindingEmailActivity(
								AccountInfoManagerActivity.this, App.getUser()
										.getmEmail());
					} else {
						showToast("发送失败");

					}
				}
			}
			// 解除绑定QQ/sina
			if (type == EAPIConsts.ReqType.SET_UNBINDING_QQ_WB) {
				// 处理数据
				if (object != null) {
					DataBox dataBox = (DataBox) object;
					if (platFormType == 100) {
						if (dataBox.mIsSuccess) {
							qq.removeAccount(true);
							hasQq = false;
							updateView(btQQ,qqName,qq.getDb().getUserName(), hasQq);
							showCompleteToast("操作成功");
						} else {
							showToast("解除绑定QQ失败");

						}
					}
					if (platFormType == 200) {
						if (dataBox.mIsSuccess) {
							sinaWb.removeAccount(true);
							hasSinaWb = false;
							updateView(btWb,wbName,sinaWb.getDb().getUserName(), hasSinaWb);
							showCompleteToast("操作成功");
						} else {
							showToast("解除绑定微博失败");

						}
					}

				}
			}
			// 绑定QQ/Sina微博
			if (type == EAPIConsts.ReqType.SET_BINDING_QQ_WB) {
				// 处理数据
				if (object != null) {
					DataBox dataBox = (DataBox) object;
					if (platFormType == 100) {
						if (dataBox.mBindingStatus) {
							hasQq = true;
							updateView(btQQ,qqName,qq.getDb().getUserName(), hasQq);
							showCompleteToast("绑定成功");
						} else {
							showToast("QQ绑定失败");
							qq.removeAccount(true);

						}
					}
					if (platFormType == 200) {
						if (dataBox.mBindingStatus) {
							hasSinaWb = true;
							updateView(btWb,wbName,sinaWb.getDb().getUserName(), hasSinaWb);
							showCompleteToast("绑定成功");
						} else {
							showToast("微博绑定失败");
							sinaWb.removeAccount(true);

						}
					}

				}
			}
		}
	};

}
