package com.tr.ui.user.frg;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tr.App;
import com.tr.R;
import com.tr.model.home.MIndustry;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.user.modified.RegisterPersonalDetailActivity;
import com.utils.common.AvatarUploader;
import com.utils.common.EConsts;
import com.utils.common.EUtil;
import com.utils.common.FileUploader;
import com.utils.common.FileUploader.OnFileUploadListener;
import com.utils.string.StringUtils;

public class FrgRegisterPersonalOne extends JBaseFragment implements
		OnClickListener, OnFileUploadListener {

	private RelativeLayout avatarParentRl;
	private ImageView avatarIv;
	private ProgressBar loadingPb;
	private EditText nameEt;
	private RadioGroup sexRg;
	private RadioButton secretRb;
	private TextView nextTv;

	private int sex = 0;

	private PopupWindow popupWindow;
	private File mAvatarTempFile; // 头像临时文件信息（用户多次拍照或选图的情况）
	private File mAvatarFile; // 头像文件
	private AvatarUploader mUploader; // 文件上传对象
	private String mLocalFilePath; // 本地文件地址

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 上传图片成功
			case 1:
				Toast.makeText(getActivity(), "上传照片成功", 0).show();
				loadingPb.setVisibility(View.GONE);
				break;
			// 上传失败
			case 2:
				Toast.makeText(getActivity(), "上传失败", 0).show();
				loadingPb.setVisibility(View.GONE);
				break;
			// 上传取消
			case 3:
				Toast.makeText(getActivity(), "上传已取消", 0).show();
				loadingPb.setVisibility(View.GONE);
				break;
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.user_frg_register_personal_one,
				container, false);
		avatarParentRl = (RelativeLayout) view
				.findViewById(R.id.avatarParentRl);
		avatarIv = (ImageView) view.findViewById(R.id.avatarIv);
		loadingPb = (ProgressBar) view.findViewById(R.id.loadingPb);
		nameEt = (EditText) view.findViewById(R.id.nameEt);
		sexRg = (RadioGroup) view.findViewById(R.id.sexRg);
		secretRb = (RadioButton) view.findViewById(R.id.secretRb);
		nextTv = (TextView) view.findViewById(R.id.nextTv);
		nameEt.addTextChangedListener(myTextWatcher);
		secretRb.setChecked(true);

		initListener();
		initVars();
		nextTv.setClickable(false);
		return view;
	}

	private void initListener() {
		avatarParentRl.setOnClickListener(this);
		nextTv.setOnClickListener(this);
		sexRg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch(checkedId){
				case R.id.boyRb:
					sex = 1;
					break;
				case R.id.girlRb:
					sex = 2;
					break;
				case R.id.secretRb:
					sex = 0;
					break;
				}
			}
		});
	}

	// 初始化变量
	private void initVars() {

		// 本地文件地址
		mLocalFilePath = "";
		// 头像临时文件
		mAvatarTempFile = new File(EUtil.getAppCacheFileDir(getActivity()),
				System.currentTimeMillis() + EConsts.DEFAULT_PIC_SUFFIX);
		if (mAvatarTempFile.exists()) {
			mAvatarTempFile.delete();
		}
		// 头像文件
		mAvatarFile = new File(EUtil.getAppCacheFileDir(getActivity()),
				EConsts.USER_AVATAR_FILE_NAME);
		if (mAvatarFile.exists()) {
			mAvatarFile.delete();
		}
		mLocalFilePath = mAvatarFile.getAbsolutePath();
		// 文件上传对象
		mUploader = new AvatarUploader(this);
	}

	private final String TIP_AVATAR_MISSING = "请选择一张图片作为头像";
	private final String TIP_FILE_UPLOADING = "头像上传中，请稍等";
	private final String TIP_NAME_MISSING = "请填写用户名";

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
		if (nameEt.getText().length() <= 0
				|| nameEt.getText().toString().trim().length() <= 0) {
			showToast(TIP_NAME_MISSING);
			return false;
		}
		return true;
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
		switch (v.getId()) {
		case R.id.avatarParentRl:
			showAvatarPopWindow();
			break;
		case R.id.nextTv:
			if (!infoIntegrityCheck()) {
				return;
			}
			Bundle bundle = new Bundle();
			bundle.putString("nick", nameEt.getText().toString());
			bundle.putString("image", App.getUser().mImage);
			bundle.putInt("sex", sex);
			FrgRegisterPersonalTwo frgTwo = new FrgRegisterPersonalTwo();
			frgTwo.setArguments(bundle);
			FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.fragment_conainer, frgTwo);
			transaction.commit();
			break;
		}

	}

	/** 点击头像后，弹出popupwindow */
	private void showAvatarPopWindow() {

		View decoreView = getActivity().getWindow().peekDecorView();
		if (decoreView != null) {
			InputMethodManager inputmanger = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(decoreView.getWindowToken(), 0);
		}

		View view = View.inflate(getActivity(),
				R.layout.activity_consummate_personal_info, null);
		TextView tvCapture = (TextView) view.findViewById(R.id.take_a_photo);
		TextView tvFromAlbum = (TextView) view
				.findViewById(R.id.get_from_album);
		TextView tvCancel = (TextView) view.findViewById(R.id.cancel);
		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);
		popupWindow.setAnimationStyle(R.style.ConnsDialogAnim);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		tvCapture.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mAvatarTempFile = new File(EUtil
						.getAppCacheFileDir(getActivity()), System
						.currentTimeMillis() + ".jpg");
				EUtil.dispatchTakePictureIntent(getActivity(),
						Uri.fromFile(mAvatarTempFile),
						EConsts.REQ_CODE_TAKE_PICTURE);
				popupWindow.dismiss();
			}
		});
		tvFromAlbum.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EUtil.dispatchPickPictureIntent(getActivity(),
						EConsts.REQ_CODE_PICK_PICTURE);
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == EConsts.REQ_CODE_TAKE_PICTURE) { // 拍照结果
			if (resultCode == getActivity().RESULT_OK) {
				EUtil.dispatchCropPictureIntent(getActivity(),
						Uri.fromFile(mAvatarTempFile),
						EConsts.REQ_CODE_CROP_PICTURE, EConsts.AVATAR_PIC_SIZE);
			}
		} else if (requestCode == EConsts.REQ_CODE_PICK_PICTURE) { // 选照结果
			if (resultCode == getActivity().RESULT_OK) {

				// 获取图片的Uri
				Uri originalUri = intent.getData();
				// 发送裁切图片请求
				EUtil.dispatchCropPictureIntent(getActivity(), originalUri,
						Uri.fromFile(mAvatarTempFile),
						EConsts.REQ_CODE_CROP_PICTURE);
			}
		} else if (requestCode == EConsts.REQ_CODE_CROP_PICTURE) { // 裁切结果

			if (resultCode == getActivity().RESULT_OK) { // 裁切成功

				// 隐藏提示文字
				// 显示图片（压缩到100*100）
				avatarIv.setImageBitmap(EUtil.getImageThumbnail(
						mAvatarTempFile.getAbsolutePath(), 100, 100));
				// 复制文件
				EUtil.copyFile(mAvatarTempFile.getAbsolutePath(),
						mAvatarFile.getAbsolutePath());
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

			} else if (resultCode == getActivity().RESULT_CANCELED) { // 取消裁切
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
