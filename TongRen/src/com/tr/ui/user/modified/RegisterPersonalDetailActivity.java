package com.tr.ui.user.modified;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
//import com.baidu.navisdk.util.common.StringUtils;
import com.tr.App;
import com.tr.R;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.Data;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.tr.api.CommonReqUtil;
import com.tr.api.UserReqUtil;
import com.tr.model.SimpleResult;
import com.tr.model.api.DataBox;
import com.tr.model.home.MIndustry;
import com.tr.model.home.MIndustrys;
import com.tr.model.home.MUserProfile;
import com.tr.model.user.JTMember;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.MainActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.organization.create_clientele.CreateOrganizationActivity;
import com.utils.common.AvatarUploader;
import com.utils.common.EConsts;
import com.utils.common.EUtil;
import com.utils.common.FileUploader;
import com.utils.common.FileUploader.OnFileUploadListener;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;

/**
 * @ClassName: RegisterPersonalDetailActivity.java
 * @Description: 个人用户信息完善页面
 * @Author leon
 * @Version v 1.0
 * @Created 2014-04-01
 * @Updated 2014-04-14
 */
public class RegisterPersonalDetailActivity extends JBaseFragmentActivity implements OnFileUploadListener {

	private final String TAG = getClass().getSimpleName();
	private LinearLayout the_entrance_of_orgnization;
	private EditText professionTv;

	private boolean hasAcount = false;
	private boolean hasProfession = true;
	// 图片文件统一放在/Android/data/com.tr/pictures/cache 文件夹下

	// 常量
	/*
	 * private final int MSG_BASE = 100; private final int MSG_UPLOAD_PREPARED =
	 * MSG_BASE + 1; private final int MSG_UPLOAD_STARTED = MSG_BASE + 2;
	 * private final int MSG_UPLOAD_CANCELED = MSG_BASE + 3; private final int
	 * MSG_UPLOAD_ERROR = MSG_BASE + 4; private final int MSG_UPLOAD_SUCCESS =
	 * MSG_BASE + 5;
	 */
	// private final int MENU_ITEM_ID_BASE = 200;
	// private final int MENU_ITEM_ID_JUMP = MENU_ITEM_ID_BASE + 1;
	private final String TIP_AVATAR_MISSING = "请选择一张图片作为头像";
	private final String TIP_FILE_UPLOADING = "头像上传中，请稍等";
	private final String TIP_NAME_MISSING = "请填写您的姓名信息";
	private final String TIP_PROFESSION_MISSING = "请填写您感兴趣的行业";

	private final String TIP_POST_MISSING = "请填写您的职位信息";
	private final String TIP_COMPANY_MISSING = "请填写您的公司名称";
	private final String TIP_EMAIL_MISSING = "请填写您的邮箱信息";
	private final String TIP_MOBILE_MISSING = "请填写您的手机号";
	private final String TIP_ILLADGE_MOBILE = "请输入正确的手机号";
	private final String TIP_ILLADGE_EMAIL = "请输入正确的邮箱";

	// 控件
	private RelativeLayout avatarParentRl;
	private ImageView avatarIv; // 头像
	private EditText nameEt; // 姓名
	private EditText postEt; // 职位
	private EditText companyEt; // 公司名称
	private EditText extraEt; // 手机/邮箱
	private TextView nextTv; // 下一步
	private ProgressBar loadingPb; // 加载控件

	// 变量
	private File mAvatarFile; // 头像文件
	private File mAvatarTempFile; // 头像临时文件信息（用户多次拍照或选图的情况）
	// private String mSuffixName; // 文件后缀名
	private String mFileUrl; // 网络文件地址
	private String mLocalFilePath; // 本地文件地址
	private App mMainApp;
	private AvatarUploader mUploader; // 文件上传对象

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 上传图片成功
			case 1:
				Toast.makeText(RegisterPersonalDetailActivity.this, "上传照片成功", 0).show();
				loadingPb.setVisibility(View.GONE);
				break;
			// 上传失败
			case 2:
				Toast.makeText(RegisterPersonalDetailActivity.this, "上传失败", 0).show();
				loadingPb.setVisibility(View.GONE);
				break;
			// 上传取消
			case 3:
				Toast.makeText(RegisterPersonalDetailActivity.this, "上传已取消", 0).show();
				loadingPb.setVisibility(View.GONE);
				break;
			// 行业选择
			case 4:
				StringBuilder builder = new StringBuilder();
				if (mIndustrys != null && mIndustrys.getListIndustry().size() > 0) {
					for (MIndustry data : mIndustrys.getListIndustry()) {
						ids.add(data.getId());
						industrys.add(data.getName());
						builder.append(data.getName() + ",");
					}
					mUserProfile.setListCareIndustryIds(ids);
					mUserProfile.setListCareIndustryNames(industrys);
					builder.deleteCharAt((builder.length() - 1));
					if (!StringUtils.isEmpty(builder.toString())) {
						professionTv.setText(builder.toString());
					}
				} else {
					professionTv.setText("");
				}
				break;
			}
		};
	};

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar(), "完善信息", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_act_register_personal_detail);
		mUserProfile = new MUserProfile();

		ids = new ArrayList<String>();
		industrys = new ArrayList<String>();
		mUserProfile.setListCareIndustryIds(ids);
		mUserProfile.setListCareIndustryNames(industrys);
		initVars();
		initControls();
	}

	// 初始化变量
	private void initVars() {

		// 本地文件地址
		mLocalFilePath = "";
		// 网络文件地址
		mFileUrl = "";
		// 头像临时文件
		mAvatarTempFile = new File(EUtil.getAppCacheFileDir(RegisterPersonalDetailActivity.this),
				System.currentTimeMillis() + EConsts.DEFAULT_PIC_SUFFIX);
		if (mAvatarTempFile.exists()) {
			mAvatarTempFile.delete();
		}
		// 头像文件
		mAvatarFile = new File(EUtil.getAppCacheFileDir(this), EConsts.USER_AVATAR_FILE_NAME);
		if (mAvatarFile.exists()) {
			mAvatarFile.delete();
		}
		mLocalFilePath = mAvatarFile.getAbsolutePath();
		// 全局对象
		mMainApp = App.getApp();
		// 文件上传对象
		mUploader = new AvatarUploader(this);
	}

	// 初始化控件
	private void initControls() {

		// 头像
		avatarParentRl = (RelativeLayout) findViewById(R.id.avatarParentRl);
		the_entrance_of_orgnization = (LinearLayout) findViewById(R.id.the_entrance_of_orgnization);
		avatarParentRl.setOnClickListener(mClickListener);
		the_entrance_of_orgnization.setOnClickListener(mClickListener);
		professionTv = (EditText) findViewById(R.id.professionTv);
		professionTv.setOnClickListener(mClickListener);
		// 提示文字
		// 头像
		avatarIv = (ImageView) findViewById(R.id.avatarIv);
		// 姓名
		nameEt = (EditText) findViewById(R.id.nameEt);

		// 职位
		postEt = (EditText) findViewById(R.id.postEt);
		// 公司名称
		companyEt = (EditText) findViewById(R.id.companyEt);
		// 手机或邮箱信息
		extraEt = (EditText) findViewById(R.id.extraEt);
		if (mMainApp.getAppData().getUser().mEmail.length() <= 0) {
			extraEt.setHint("邮箱");
		}
		if (mMainApp.getAppData().getUser().mMobile.length() <= 0) {
			extraEt.setHint("手机");
			extraEt.setInputType(InputType.TYPE_CLASS_PHONE);
		}
		// 下一步
		nextTv = (TextView) findViewById(R.id.nextTv);
		nextTv.setOnClickListener(mClickListener);
		// 加载控件
		loadingPb = (ProgressBar) findViewById(R.id.loadingPb);
		nameEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!StringUtils.isEmpty(s.toString())) {
					hasAcount = true;
				} else {
					hasAcount = false;
				}
				if (hasAcount == true && hasProfession == true) {
					nextTv.setClickable(true);
					nextTv.setBackgroundResource(R.drawable.sign_in);
				} else {
					nextTv.setClickable(false);
					nextTv.setBackgroundResource(R.drawable.sign_in_normal);
				}
			}
		});
		// professionTv.addTextChangedListener(new TextWatcher() {
		// @Override
		// public void onTextChanged(CharSequence s, int start, int before, int
		// count) {
		// }
		//
		// @Override
		// public void beforeTextChanged(CharSequence s, int start, int count,
		// int after) {
		// }
		//
		// @Override
		// public void afterTextChanged(Editable s) {
		// if (StringUtils.isNotEmpty(s.toString())) {
		// hasProfession = true;
		// }
		// else {
		// hasProfession = false;
		// }
		// if (hasAcount == true && hasProfession == true) {
		// nextTv.setClickable(true);
		// nextTv.setBackgroundResource(R.drawable.sign_in);
		// }
		// else {
		// nextTv.setClickable(false);
		// nextTv.setBackgroundResource(R.drawable.sign_in_normal);
		// }
		// }
		// });
	}

	/** 点击头像后，弹出popupwindow */
	private void showAvatarPopWindow() {

		View decoreView = getWindow().peekDecorView();
		if (decoreView != null) {
			InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(decoreView.getWindowToken(), 0);
		}

		View view = View.inflate(this, R.layout.activity_consummate_personal_info, null);
		TextView tvCapture = (TextView) view.findViewById(R.id.take_a_photo);
		TextView tvFromAlbum = (TextView) view.findViewById(R.id.get_from_album);
		TextView tvCancel = (TextView) view.findViewById(R.id.cancel);
		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		popupWindow.setAnimationStyle(R.style.ConnsDialogAnim);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		tvCapture.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mAvatarTempFile = new File(EUtil.getAppCacheFileDir(RegisterPersonalDetailActivity.this), System
						.currentTimeMillis() + ".jpg");
				EUtil.dispatchTakePictureIntent(RegisterPersonalDetailActivity.this, Uri.fromFile(mAvatarTempFile),
						EConsts.REQ_CODE_TAKE_PICTURE);
				popupWindow.dismiss();
			}
		});
		tvFromAlbum.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EUtil.dispatchPickPictureIntent(RegisterPersonalDetailActivity.this, EConsts.REQ_CODE_PICK_PICTURE);
				popupWindow.dismiss();
			}
		});
		tvCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
			}
		});
		popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
	}

	// 图片处理结果
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {

		if (requestCode == EConsts.REQ_CHOOSE_INDUSTRY) {
			if (resultCode == RESULT_OK) {
				// TODO
				if (intent.getExtras() != null) {
					mIndustrys = (MIndustrys) intent.getExtras().get(EConsts.Key.INDUSTRYS);
					handler.sendEmptyMessageDelayed(4, 100);
				}

			}
		}

		if (requestCode == EConsts.REQ_CODE_TAKE_PICTURE) { // 拍照结果
			if (resultCode == RESULT_OK) {
				EUtil.dispatchCropPictureIntent(RegisterPersonalDetailActivity.this, Uri.fromFile(mAvatarTempFile),
						EConsts.REQ_CODE_CROP_PICTURE, EConsts.AVATAR_PIC_SIZE);
			}
		} else if (requestCode == EConsts.REQ_CODE_PICK_PICTURE) { // 选照结果
			if (resultCode == RESULT_OK) {

				// 获取图片的Uri
				Uri originalUri = intent.getData();
				// 发送裁切图片请求
				EUtil.dispatchCropPictureIntent(RegisterPersonalDetailActivity.this, originalUri,
						Uri.fromFile(mAvatarTempFile), EConsts.REQ_CODE_CROP_PICTURE);
			}
		} else if (requestCode == EConsts.REQ_CODE_CROP_PICTURE) { // 裁切结果

			if (resultCode == RESULT_OK) { // 裁切成功

				// 隐藏提示文字
				// 显示图片（压缩到100*100）
				avatarIv.setImageBitmap(EUtil.getImageThumbnail(mAvatarTempFile.getAbsolutePath(), 100, 100));
				// 复制文件
				EUtil.copyFile(mAvatarTempFile.getAbsolutePath(), mAvatarFile.getAbsolutePath());
				loadingPb.setVisibility(View.VISIBLE);

				mUploader.setmListener(new OnFileUploadListener() {
					@Override
					public void onUpdate(int value) {
					}

					@Override
					public void onSuccess(String fileUrl) {
						App.getUser().mImage = fileUrl;
						handler.sendEmptyMessage(1);
					}

					@Override
					public void onStarted() {
						loadingPb.setVisibility(View.VISIBLE);
					}

					@Override
					public void onPrepared() {
						loadingPb.setVisibility(View.VISIBLE);
					}

					@Override
					public void onError(int code, String message) {
						handler.sendEmptyMessage(2);
					}

					@Override
					public void onCanceled() {
						handler.sendEmptyMessage(3);
					}
				});

				// 删除临时文件
				if (mAvatarTempFile.exists()) {
					mAvatarTempFile.delete();
				}
				// 开始上传
				if (mUploader.getStatus() == FileUploader.Status.Started) {
					mUploader.cancel();
				}
				// 开始上传
				mUploader.start(mLocalFilePath);

			} else if (resultCode == RESULT_CANCELED) { // 取消裁切
				// 删除临时文件
				if (mAvatarTempFile.exists()) {
					mAvatarTempFile.delete();
				}
			} else { // 裁切失败

				// 删除头像文件
				if (mAvatarFile.exists()) {
					mAvatarFile.delete();
				}
				// 删除临时头像文件
				if (mAvatarTempFile.exists()) {
					mAvatarTempFile.delete();
				}
			}
		}
	}

	// 用户信息完整性检查
	private boolean infoIntegrityCheck() {

		// 头像
		if (!mAvatarFile.exists() && mLocalFilePath.length() <= 0) {
			showToast(TIP_AVATAR_MISSING);
			return false;
		}
		// 正在上传
		if (mUploader.getStatus() == FileUploader.Status.Started) {
			showToast(TIP_FILE_UPLOADING);
			return false;
		}
		// 姓名
		if (nameEt.getText().length() <= 0 || nameEt.getText().toString().trim().length() <= 0) {
			showToast(TIP_NAME_MISSING);
			return false;
		}
		// // 职位
		// if (postEt.getText().length() <= 0 ||
		// postEt.getText().toString().trim().length() <= 0) {
		// showToast(TIP_POST_MISSING);
		// return false;
		// }
		// // 公司名称
		// if (companyEt.getText().length() <= 0 ||
		// companyEt.getText().toString().trim().length() <= 0) {
		// showToast(TIP_COMPANY_MISSING);
		// return false;
		// }
		// // 手机号是否为空
		// if (extraEt.getText().length() <= 0 &&
		// mMainApp.mAppData.getUser().mMobile.equals("")) {
		// showToast(TIP_MOBILE_MISSING);
		// return false;
		// }
		// // 手机格式是否正确
		// if (mMainApp.mAppData.getUser().mMobile.equals("") &&
		// !EUtil.isMobileNO(extraEt.getText().toString())) {
		// showToast(TIP_ILLADGE_MOBILE);
		// return false;
		// }
		// // 邮箱是否为空
		// if (extraEt.getText().length() <= 0 &&
		// mMainApp.mAppData.getUser().mEmail.equals("")) {
		// showToast(TIP_EMAIL_MISSING);
		// return false;
		// }
		// // 邮箱格式是否正确
		// if (mMainApp.mAppData.getUser().mEmail.equals("") &&
		// !EUtil.isEmail(extraEt.getText().toString())) {
		// showToast(TIP_ILLADGE_EMAIL);
		// return false;
		// }
		return true;
	}

	// 点击事件监听器
	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == avatarParentRl) { // 选择头像
				showAvatarPopWindow();
			} else if (v == nextTv) { // 下一步
				if (!infoIntegrityCheck()) {
					return;
				}
				// 显示加载框
				showLoadingDialog();
				// 调用接口
				// TODO
				// UserReqUtil.doFullPersonMemberInfo(RegisterPersonalDetailActivity.this,
				// mBindData,
				// UserReqUtil.getDoFullPersonMemberInfoParams(nameEt.getText().toString().trim(),
				// postEt.getText()
				// .toString().trim(), companyEt.getText().toString().trim(),
				// mMainApp.mAppData.getUser().mMobile.equals("") ?
				// extraEt.getText().toString() : "",
				// mMainApp.mAppData.getUser().mEmail.equals("") ?
				// extraEt.getText().toString() : "", mFileUrl), null);
				if (!StringUtils.isEmpty(App.getUser().mImage)) {
					mUserProfile.setImage(App.getUser().mImage);
				} else {
					mUserProfile.setImage("");
				}
				mUserProfile.setShortName(nameEt.getText().toString().trim());

				CommonReqUtil.doUploadUserProfile(RegisterPersonalDetailActivity.this, mBindData, mUserProfile, null);

			} else if (v == the_entrance_of_orgnization) {
				Intent intent = new Intent(RegisterPersonalDetailActivity.this, CreateOrganizationActivity.class);// 组织用户入口，跳转到创建组织页面
				startActivity(intent);
			} else if (v == professionTv) {
				// TODO
				// Toast.makeText(RegisterPersonalDetailActivity.this,
				// "选择感兴趣的行业", 0).show();

				ENavigate.startChooseProfessionActivityForResult(RegisterPersonalDetailActivity.this,
						EConsts.REQ_CHOOSE_INDUSTRY, 0, mIndustrys);
			}
		}
	};

	// 接口回调对象
	private IBindData mBindData = new IBindData() {

		@Override
		public void bindData(int type, Object object) {
			// TODO Auto-generated method stub
			if (!isLoadingDialogShowing()) {
				return;
			} else {
				dismissLoadingDialog();
			}
			if (type == EAPIConsts.ReqType.FULL_PERSON_MEMBER_INFO) {
				// 初始化数据
				if (object != null) {
					// 初始化全局变量
					DataBox dataBox = (DataBox) object;
					if (dataBox.mJTMember == null) {
						return;
					}
					App.getApp().getAppData().setUser(dataBox.mJTMember);
					JTMember member = App.getApp().getAppData().getUser();
					Log.d("", member + "");
					// 跳转到完善个人信息页面
					// startActivity(new
					// Intent(RegisterPersonalDetailActivity.this,
					// MainActivity.class));

					// 销毁本页面
				}
			} else if (type == EAPIConsts.CommonReqType.UploadUserProfile) {

				if (object != null) {
					DataBox dataBox = (DataBox) object;
					if (dataBox.mJTMember == null) {
						showToast("注册失败，请重试");
						return;
					}
					mMainApp.getAppData().setUser(dataBox.mJTMember);
					String nick = App.getNick();
					String image = App.getUser().getImage();
					JTMember member = mMainApp.getAppData().getUser();
					Log.d("", nick + image + member);
					showToast("注册成功");
					// ENavigate.startMainActivity(RegisterPersonalDetailActivity.this);
					EMChatManager.getInstance().login(mMainApp.getAppData().getUserID(),
							mMainApp.getAppData().getUserID(), new EMCallBack() {// 回调
								@Override
								public void onSuccess() {
									runOnUiThread(new Runnable() {
										public void run() {
											EMGroupManager.getInstance().loadAllGroups();
											EMChatManager.getInstance().loadAllConversations();
										}
									});
									
								}

								@Override
								public void onProgress(int progress, String status) {

								}

								@Override
								public void onError(int code, String message) {
									Log.d("main", "登陆聊天服务器失败！");
									handler.sendEmptyMessage(6);
								}
							});
					ENavigate.startWantPeopleActivity(RegisterPersonalDetailActivity.this);
				} else {
					showToast("注册失败，请重试");
				}
			}
		}
	};

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FileUploader.Status.Prepared:
				loadingPb.setVisibility(View.VISIBLE);
				break;
			case FileUploader.Status.Started:
				loadingPb.setVisibility(View.VISIBLE);
				break;
			case FileUploader.Status.Canceled:
				loadingPb.setVisibility(View.GONE);
				break;
			case FileUploader.Status.Error:
				loadingPb.setVisibility(View.GONE);
				showToast(msg.obj + "");
				break;
			case FileUploader.Status.Success:
				loadingPb.setVisibility(View.GONE);
			case 6:
				Toast.makeText(RegisterPersonalDetailActivity.this, "连接聊天服务器失败，请重新登陆", 0).show();
				break;
			}
		}
	};
	private PopupWindow popupWindow;
	private MUserProfile mUserProfile;
	private MIndustrys mIndustrys = null;
	private List<String> ids;
	private List<String> industrys;

	@Override
	public void onPrepared() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStarted() {
		// TODO Auto-generated method stub
		mHandler.sendEmptyMessage(FileUploader.Status.Prepared);
	}

	@Override
	public void onUpdate(int value) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onCanceled() {
		// TODO Auto-generated method stub
		mHandler.sendEmptyMessage(FileUploader.Status.Canceled);
	}

	@Override
	public void onSuccess(String fileUrl) {
		// TODO Auto-generated method stub
		mFileUrl = fileUrl;
		mHandler.sendEmptyMessage(FileUploader.Status.Success);
	}

	@Override
	public void onError(int code, String message) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.what = FileUploader.Status.Error;
		msg.obj = message;
		mHandler.sendEmptyMessage(FileUploader.Status.Error);
	}
}
