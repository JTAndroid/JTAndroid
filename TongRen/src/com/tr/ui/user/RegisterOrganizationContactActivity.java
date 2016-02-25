package com.tr.ui.user;

import java.io.File;
import com.tr.App;
import com.tr.R;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.tr.api.UserReqUtil;
import com.tr.model.api.DataBox;
import com.tr.ui.base.JBaseFragmentActivity;
import com.utils.common.CardUploader;
import com.utils.common.EConsts;
import com.utils.common.EUtil;
import com.utils.common.FileUploader;
import com.utils.common.FileUploader.OnFileUploadListener;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/**
 * @ClassName:     RegisterOrganizationContactActivity.java
 * @Description:   机构联系人信息
 * @Author         leon
 * @Version        v 1.0  
 * @Created        2014-04-02
 * @Updated	   	   2014-04-14
 */
public class RegisterOrganizationContactActivity extends JBaseFragmentActivity {

	private final String TAG = getClass().getSimpleName();
	
	// 常量
	private final int MSG_BASE = 100;
	private final int MSG_UPLOAD_PREPARED = MSG_BASE + 1;
	private final int MSG_UPLOAD_STARTED = MSG_BASE + 2;
	private final int MSG_UPLOAD_CANCELED = MSG_BASE + 3;
	private final int MSG_UPLOAD_ERROR = MSG_BASE + 4;
	private final int MSG_UPLOAD_SUCCESS = MSG_BASE + 5;
	private final int THUMBNAIL_MAX_WIDTH = 80; // 缩略图最大宽度
	private final int THUMBNAIL_MAX_HEIGHT = 40; // 缩略图最大高度
	private final String TIP_EMPTY_PICTURE = "请上传联系人身份证图片";
	private final String TIP_FILE_UPLOADING = "图片上传中，请稍等...";
	private final String TIP_EMPTY_EMAIL = "请输入邮箱";
	private final String TIP_ERROR_EMAIL = "请输入正确的邮箱";
	private final String TIP_EMPTY_MOBILE = "请输入手机号码";
	private final String TIP_ERROR_MOBILE = "请输入正确的手机号码";
	
	// 控件
	private ImageView contactPicIv; // 联系人缩略图
	private TextView cameraTipTv; 
	private ImageView cameraIv; // 拍照
	private EditText extraEt; // 额外的信息，邮箱/手机号
	private TextView nextTv; // 下一步
	private ProgressBar loadingPb; // 加载框
	
	// 页面变量
	private File mPicFile; // 要上传的文件
	// private String mSuffixName; // 文件扩展名
	private String mLocalFilePath; // 本地文件地址
	private String mFileUrl; // 网络文件地址（上传数据时用到）
	private App mMainApp; // App全局对象
	private CardUploader mUploader; // 文件上传对象
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_act_register_organization_contact);
		initVars();
		initControls();
	}
	
	private void initVars(){
		
		// 本地文件地址
		mLocalFilePath = "";
		// 网络文件地址
		mFileUrl = "";
		// 创建路径
		/*
		File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		if(!storageDir.exists()){
			storageDir.mkdirs();
		}
		*/
		// 本地图片文件
		mPicFile = new File(EUtil.getAppCacheFileDir(this), 
				System.currentTimeMillis() +".jpg");
		if(mPicFile.exists()){
			mPicFile.delete();
		}
		// mLocalFilePath = mPicFile.getAbsolutePath(); 
		// App全局对象
		mMainApp = App.getApp();
		// 文件上传对象
		mUploader = new CardUploader(4, mFileUploadListener);
	}
	
	private void initControls(){
		// 联系人头像
		contactPicIv = (ImageView) findViewById(R.id.contactPicIv);
		// 拍照提示文字
		cameraTipTv = (TextView) findViewById(R.id.cameraTipTv);
		// 拍照
		cameraIv = (ImageView) findViewById(R.id.cameraIv);
		cameraIv.setOnClickListener(mClickListener);
		// 附加信息
		extraEt = (EditText) findViewById(R.id.extraEt);
		if(mMainApp.getAppData().getUser().mMobile.equals("")){
			extraEt.setHint("手机号");
			extraEt.setInputType(InputType.TYPE_CLASS_PHONE);
		}
		else if(mMainApp.getAppData().getUser().mEmail.equals("")){
			extraEt.setHint("邮箱");
		}
		// 下一步
		nextTv = (TextView) findViewById(R.id.nextTv);
		nextTv.setOnClickListener(mClickListener);
		// 加载控件
		loadingPb = (ProgressBar) findViewById(R.id.loadingPb);
	}
	
	@Override
	public void initJabActionBar() {
		jabGetActionBar().setTitle("联系人信息");
		jabGetActionBar().setDisplayShowTitleEnabled(true);
	}
	
	// 检查联系人信息的完整性
	private boolean infoLegalityCheck(){
		
		if (mLocalFilePath.length() <= 0) { // 是否选择头像
			showToast(TIP_EMPTY_PICTURE);
			return false;
		}
		if (mUploader.getStatus() == FileUploader.Status.Started) { // 正在上传文件
			showToast(TIP_FILE_UPLOADING);
			return false;
		}
		if (mMainApp.getAppData().getUser().mMobile.equals("")) { // 手机号非空且格式正确
			if (extraEt.getText().length() <= 0) {
				showToast(TIP_EMPTY_MOBILE);
				return false;
			} 
			else if (!EUtil.isMobileNO(extraEt.getText().toString())) {
				showToast(TIP_ERROR_MOBILE);
				return false;
			}
		} 
		else if (mMainApp.getAppData().getUser().mEmail.equals("")) { // 邮箱非空且格式正确
			if (extraEt.getText().length() <= 0) {
				showToast(TIP_EMPTY_EMAIL);
				return false;
			} 
			else if (!EUtil.isEmail(extraEt.getText().toString())) {
				showToast(TIP_ERROR_EMAIL);
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		
		if(requestCode == EConsts.REQ_CODE_TAKE_PICTURE){ // 拍照
			if (resultCode == RESULT_OK) { 
					
				// 本地图片地址
				mLocalFilePath = mPicFile.getAbsolutePath();
				// 隐藏提示文字
				cameraTipTv.setVisibility(View.GONE);
				// 显示缩略图
				contactPicIv.setImageBitmap(EUtil.getImageThumbnail(
						mLocalFilePath,
						EUtil.convertDpToPx(THUMBNAIL_MAX_WIDTH),
						EUtil.convertDpToPx(THUMBNAIL_MAX_HEIGHT)));
				// 上传文件
				if(mUploader.getStatus() == FileUploader.Status.Started){
					mUploader.cancel();
				}
				mUploader.start(mLocalFilePath);
			}
		}
		else if(requestCode == EConsts.REQ_CODE_PICK_PICTURE){ // 选照
			if (resultCode == RESULT_OK) { // 选照成功 （不裁切图片的情况下不用拷贝到本地）
				
				// 本地图片地址
				mLocalFilePath = EUtil.uri2Path(getContentResolver(),
						intent.getData());
				// 隐藏提示文字
				cameraTipTv.setVisibility(View.GONE);
				// 显示缩略图
				contactPicIv.setImageBitmap(EUtil.getImageThumbnail(
						mLocalFilePath,
						EUtil.convertDpToPx(THUMBNAIL_MAX_WIDTH),
						EUtil.convertDpToPx(THUMBNAIL_MAX_HEIGHT)));
				// 上传文件
				if(mUploader.getStatus() == FileUploader.Status.Started){
					mUploader.cancel();
				}
				mUploader.start(mLocalFilePath);
			}
		}
	}
	
	private OnClickListener mClickListener=new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v == cameraIv){ // 拍照/选照
				EUtil.showPhotoSelectDialog(
						RegisterOrganizationContactActivity.this,
						mDlgClickListener);
			}
			else if(v == nextTv){ // 下一步
				if(!infoLegalityCheck()){
					return;
				}
					// 显示加载框
					showLoadingDialog();
					// 发送请求
					UserReqUtil.doFullContactInfo(RegisterOrganizationContactActivity.this, mBindData, 
							UserReqUtil.getDoFullContactInfoParams(mMainApp.getAppData().getUser().mID, mFileUrl, 
									mMainApp.getAppData().getUser().mMobile.equals("")?extraEt.getText().toString():"", 
									mMainApp.getAppData().getUser().mEmail.equals("")?extraEt.getText().toString():""), null);
				
			}
		}
	};
	
	private DialogInterface.OnClickListener mDlgClickListener=new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			if (which == 0) { // 拍照
				EUtil.dispatchTakePictureIntent(
						RegisterOrganizationContactActivity.this,
						Uri.fromFile(mPicFile),
						EConsts.REQ_CODE_TAKE_PICTURE);
			} 
			else { // 选照
				EUtil.dispatchPickPictureIntent(
						RegisterOrganizationContactActivity.this,
						EConsts.REQ_CODE_PICK_PICTURE);
			}
		}
	};
	
	// 接口回调
	private IBindData mBindData = new IBindData(){

		@Override
		public void bindData(int type, Object object) {
			// TODO Auto-generated method stub
			if(!isLoadingDialogShowing()){
				return;
			}
			else{
				dismissLoadingDialog();
			}
			if(type == EAPIConsts.ReqType.FULL_CONTACT_INFO){
				// 初始化数据
				if(object != null){
					DataBox dataBox = (DataBox) object;
					if (dataBox.mIsSuccess) {
						startActivity(new Intent(
								RegisterOrganizationContactActivity.this,
								RegisterOrganizationDetailActivity.class));
						finish();
					}
				}
			}
		}
	};
	
	// 消息处理器
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){
		
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case MSG_UPLOAD_PREPARED:
				loadingPb.setVisibility(View.GONE);
				break;
			case MSG_UPLOAD_STARTED:
				loadingPb.setVisibility(View.VISIBLE);
				break;
			case MSG_UPLOAD_CANCELED:
				loadingPb.setVisibility(View.GONE);
				break;
			case MSG_UPLOAD_SUCCESS:
				loadingPb.setVisibility(View.GONE);
				break;
			case MSG_UPLOAD_ERROR:
				loadingPb.setVisibility(View.GONE);
				break;
			}
		}
	};
	
	private OnFileUploadListener mFileUploadListener = new OnFileUploadListener(){

		@Override
		public void onPrepared() {
			// TODO Auto-generated method stub
			mHandler.sendEmptyMessage(MSG_UPLOAD_PREPARED);
		}

		@Override
		public void onStarted() {
			// TODO Auto-generated method stub
			mHandler.sendEmptyMessage(MSG_UPLOAD_STARTED);
		}

		@Override
		public void onUpdate(int value) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onCanceled() {
			// TODO Auto-generated method stub
			mHandler.sendEmptyMessage(MSG_UPLOAD_CANCELED);
		}

		@Override
		public void onSuccess(String fileUrl) {
			// TODO Auto-generated method stub
			mFileUrl = fileUrl;
			mHandler.sendEmptyMessage(MSG_UPLOAD_SUCCESS);
		}

		@Override
		public void onError(int code,String message) {
			// TODO Auto-generated method stub
			mHandler.sendEmptyMessage(MSG_UPLOAD_ERROR);
		}
	};
}
