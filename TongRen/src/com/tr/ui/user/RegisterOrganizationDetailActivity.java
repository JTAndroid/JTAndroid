package com.tr.ui.user;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import com.tr.App;
import com.tr.R;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.tr.api.UserReqUtil;
import com.tr.model.api.DataBox;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.MainActivity;
import com.utils.common.AvatarUploader;
import com.utils.common.CardUploader;
import com.utils.common.EConsts;
import com.utils.common.EUtil;
import com.utils.common.FileUploader;
import com.utils.common.FileUploader.OnFileUploadListener;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/**
 * @ClassName:     RegisterOrganizationDetailActivity.java
 * @Description:   机构用户信息完善页面
 * @Author         leon
 * @Version        v 1.0  
 * @Created        2014-04-01
 * @LastEdit       2014-04-14
 */
public class RegisterOrganizationDetailActivity extends JBaseFragmentActivity{

	private final String TAG = getClass().getSimpleName();
	
	// 常量
	// private final String CUSTOM_DATE_FORMAT = "yyyyMMdd_kkmmss";
	private final int MSG_BASE = 100;
	private final int MSG_UPLOAD_AVATAR_PREPARED = MSG_BASE + 1;
	private final int MSG_UPLOAD_AVATAR_STARTED = MSG_BASE + 2;
	private final int MSG_UPLOAD_AVATAR_CANCELED = MSG_BASE + 3;
	private final int MSG_UPLOAD_AVATAR_ERROR = MSG_BASE + 4;
	private final int MSG_UPLOAD_AVATAR_SUCCESS = MSG_BASE + 5;
	private final int MSG_UPLOAD_CODE_PIC_PREPARED = MSG_BASE + 6;
	private final int MSG_UPLOAD_CODE_PIC_STARTED = MSG_BASE + 7;
	private final int MSG_UPLOAD_CODE_PIC_CANCELED = MSG_BASE + 8;
	private final int MSG_UPLOAD_CODE_PIC_ERROR = MSG_BASE + 9;
	private final int MSG_UPLOAD_CODE_PIC_SUCCESS = MSG_BASE + 10;
	private final int MSG_UPLOAD_LIC_PIC_PREPARED = MSG_BASE + 11;
	private final int MSG_UPLOAD_LIC_PIC_STARTED = MSG_BASE + 12;
	private final int MSG_UPLOAD_LIC_PIC_CANCELED = MSG_BASE + 13;
	private final int MSG_UPLOAD_LIC_PIC_ERROR = MSG_BASE + 14;
	private final int MSG_UPLOAD_LIC_PIC_SUCCESS = MSG_BASE + 15;
	private final int MSG_UPLOAD_LPID_PIC_PREPARED = MSG_BASE + 16;
	private final int MSG_UPLOAD_LPID_PIC_STARTED = MSG_BASE + 17;
	private final int MSG_UPLOAD_LPID_PIC_CANCELED = MSG_BASE + 18;
	private final int MSG_UPLOAD_LPID_PIC_ERROR = MSG_BASE + 19;
	private final int MSG_UPLOAD_LPID_PIC_SUCCESS = MSG_BASE + 20;
	private final int THUMBNAIL_MAX_WIDTH = 80; // 缩略图最大宽度
	private final int THUMBNAIL_MAX_HEIGHT = 40; // 缩略图最大高度
	private final String TIP_AVATAR_MISSING = "请选择一张图片作为头像";
	private final String TIP_FULLNAME_MISSING = "请填写机构全称";
	private final String TIP_FULLNAME_ERROR = "25个中文汉字或50个英文字母以内";
	private final String TIP_ABBREVIATION_MISSING = "请填写机构简称";
	private final String TIP_AVATAR_UPLOADING = "头像上传中，请稍等";
	private final String TIP_ORG_CODE_PIC_UPLOADING = "机构代码图片上传中，请稍等";
	private final String TIP_ORG_LIC_PIC_UPLOADING = "营业执照图片上传中，请稍等";
	private final String TIP_LP_ID_PIC_UPLOADING = "法人身份证图片上传中，请稍等";
	private final String TIP_ABBREVIATION_ERROR = "10个中文汉字或20个英文字母以内";
	private final String TIP_ORG_CODE_PIC_MISSING = "请上传组织机构代码证图片";
	private final String TIP_ORG_LICENSE_PIC_MISSING = "请上传营业执照证图片";
	private final String TIP_LEGAL_PERSON_ID_PIC_MISSING = "请上传企业法人身份证图片";
	
	// 控件部分
	private RelativeLayout avatarParentRl;
	private TextView avatarCameraTipTv; // 上传头像提示文字
	private ImageView avatarIv; // 头像
	private ProgressBar avatarLoadingPb; // 加载控件
	private EditText fullNameEt; // 机构全称
	private EditText shortNameEt; // 机构简称
	// 机构代码
	private TextView orgCodeCameraTipTv;
	private ImageView orgCodeCameraIv;
	private ImageView orgCodePicIv;
	private ProgressBar orgCodeLoadingPb;
	// 营业执照
	private TextView orgLicCameraTipTv;
	private ImageView orgLicCameraIv;
	private ImageView orgLicPicIv;
	private ProgressBar orgLicLoadingPb;
	// 企业法人
	private TextView lpIDCameraTipTv;
	private ImageView lpIDCameraIv;
	private ImageView lpIDPicIv;
	private ProgressBar lpIDLoadingPb;
	// 登陆
	private TextView submitLettersTv;
	private TextView submitTv;
	
	// 变量
	private File mAvatarFile;  // 头像文件
	private File mOrgCodePicFile; // 机构代码图片文件
	private File mLicensePicFile; // 营业执照图片文件
	private File mLegalPersonIDPicFile; // 法人身份证文件
	private File mTempFile; // 临时文件（有裁切图像的地方是需要的）
	private int mPicType; // 头像：0，机构代码：1，营业执照：2，法人身份证：3
	private String mAvatarLocalFilePath = ""; // 头像本地地址
	private String mAvatarFileUrl = ""; //  头像网络地址
	private String mOrgCodePicLocalFilePath = ""; // 机构代码图片本地地址
	private String mOrgCodePicFileUrl = ""; // 机构代码图片网络地址
	private String mLicensePicLocalFilePath = ""; // 营业执照图片本地地址
	private String mLicensePicFileUrl = ""; // 营业执照图片网络地址
	private String mLegalPersonIDPicLocalFilePath = ""; // 法人身份证图片本地地址
	private String mLegalPersonIDPicFileUrl = "";  // 法人身份证网络图片地址
	private String mSuffixName = ".jpg"; // 文件后缀（默认为.jpg）
	private App mMainApp; // App 全局对象
	private AvatarUploader mAvatarUploader;
	private CardUploader mOrgCodePicUploader;
	private CardUploader mOrgLicPicUploader;
	private CardUploader mLPIDPicUploader;
	
	@Override
	public void initJabActionBar() {
		jabGetActionBar().setTitle("完善机构资料");
		jabGetActionBar().setDisplayShowTitleEnabled(true);
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_act_register_organization_detail);
		initVars();
		initControls();
	}
	
	// 初始化变量
	private void initVars(){
		
		// 创建路径 
		/*
		File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		if (!storageDir.exists()) {
			storageDir.mkdirs();
		}
		*/
		// 头像文件（输出一定是.jpg格式，经过了裁切过程）
		Calendar calendar = Calendar.getInstance();
		/*
		mAvatarFile = new File(storageDir, calendar.getTimeInMillis() + mSuffixName);
		if (mAvatarFile.exists()) {
			mAvatarFile.delete();
		}
		*/
		mAvatarFile = new File(EUtil.getAppCacheFileDir(this), "avatar.jpg");
		if (mAvatarFile.exists()) {
			mAvatarFile.delete();
		}
		// mAvatarLocalFilePath = mAvatarFile.getAbsolutePath();
		// 头像临时文件（格式不确定，默认.jpg）
		// calendar.add(Calendar.SECOND, 1);
		mTempFile = new File(EUtil.getAppCacheFileDir(this), calendar.getTimeInMillis() + mSuffixName);
		if (mTempFile.exists()) {
			mTempFile.delete();
		}
		// 组织机构代码图片文件（文件格式不确定，默认.jpg）
		calendar.add(Calendar.SECOND, 1);
		mOrgCodePicFile = new File(EUtil.getAppCacheFileDir(this), calendar.getTimeInMillis() + mSuffixName);
		if (mOrgCodePicFile.exists()) {
			mOrgCodePicFile.delete();
		}
		// mOrgCodePicLocalFilePath = mOrgCodePicFile.getAbsolutePath();
		// 营业执照图片文件（文件格式不确定，默认.jpg）
		calendar.add(Calendar.SECOND, 1);
		mLicensePicFile = new File(EUtil.getAppCacheFileDir(this), calendar.getTimeInMillis() + mSuffixName);
		if (mLicensePicFile.exists()) {
			mLicensePicFile.delete();
		}
		// mLicensePicLocalFilePath = mLicensePicFile.getAbsolutePath();
		// 企业法人图片文件（文件格式不确定，默认.jpg）
		calendar.add(Calendar.SECOND, 1);
		mLegalPersonIDPicFile = new File(EUtil.getAppCacheFileDir(this), calendar.getTimeInMillis() + mSuffixName);
		if (mLegalPersonIDPicFile.exists()) {
			mLegalPersonIDPicFile.delete();
		}
		// mLegalPersonIDPicLocalFilePath = mLegalPersonIDPicFile.getAbsolutePath();
		// 全局对象
		mMainApp = App.getApp();
		// 文件上传对象
		mAvatarUploader = new AvatarUploader(mAvatarUploadListener);
		mOrgCodePicUploader = new CardUploader(1, mOrgCodePicUploadListener);
		mOrgLicPicUploader = new CardUploader(2, mOrgLicPicUploadListener);
		mLPIDPicUploader = new CardUploader(3, mLPIDPicUploadListener);
	}
	
	// 初始化控件
	private void initControls(){
		// 头像
		avatarParentRl = (RelativeLayout) findViewById(R.id.avatarParentRl);
		avatarParentRl.setOnClickListener(mClickListener);
		avatarCameraTipTv = (TextView) findViewById(R.id.avatarCameraTipTv);
		avatarLoadingPb = (ProgressBar) findViewById(R.id.avatarLoadingPb);
		avatarIv= (ImageView) findViewById(R.id.avatarIv);
		// avatarIv.setOnClickListener(mClickListener);
		// 机构全称
		fullNameEt = (EditText) findViewById(R.id.fullNameEt);
		// 机构简称
		shortNameEt = (EditText) findViewById(R.id.shortNameEt);
		//机构代码
		orgCodeCameraTipTv = (TextView) findViewById(R.id.orgCodeCameraTipTv);
		orgCodeCameraIv= (ImageView) findViewById(R.id.orgCodeCameraIv);
		orgCodeCameraIv.setOnClickListener(mClickListener);
		orgCodePicIv = (ImageView) findViewById(R.id.orgCodePicIv);
		orgCodeLoadingPb = (ProgressBar) findViewById(R.id.orgCodeLoadingPb);
		// 营业执照
		orgLicCameraTipTv = (TextView) findViewById(R.id.orgLicCameraTipTv);
		orgLicPicIv = (ImageView) findViewById(R.id.orgLicPicIv);
		orgLicLoadingPb = (ProgressBar) findViewById(R.id.orgLicLoadingPb);
		orgLicCameraIv = (ImageView) findViewById(R.id.orgLicCameraIv);
		orgLicCameraIv.setOnClickListener(mClickListener);
		// 企业法人
		lpIDCameraTipTv= (TextView) findViewById(R.id.lpIDCameraTipTv);
		lpIDCameraIv = (ImageView) findViewById(R.id.lpIDCameraIv);
		lpIDCameraIv.setOnClickListener(mClickListener);
		lpIDPicIv= (ImageView) findViewById(R.id.lpIDPicIv);
		lpIDLoadingPb = (ProgressBar) findViewById(R.id.lpIDLoadingPb);
		// 上传公函
		submitLettersTv = (TextView) findViewById(R.id.submitLettersTv);
		submitLettersTv.setOnClickListener(mClickListener);
		// 提交
		submitTv = (TextView) findViewById(R.id.submitTv);
		submitTv.setOnClickListener(mClickListener);
	}
	
	// 图片处理结果
	@Override
	public void onActivityResult(int requestCode,int resultCode,Intent intent){
		
		if(requestCode == EConsts.REQ_CODE_TAKE_PICTURE){ // 拍照
			if(resultCode==RESULT_OK){				
				if (mPicType == 0) { // 裁切头像
					EUtil.dispatchCropPictureIntent(RegisterOrganizationDetailActivity.this,
							Uri.fromFile(mTempFile),
							EConsts.REQ_CODE_CROP_PICTURE,
							EConsts.AVATAR_PIC_SIZE);
				} 
				else if(mPicType == 1){
					// 获取本地文件名
					mOrgCodePicLocalFilePath = mOrgCodePicFile.getAbsolutePath();
					// 隐藏提示文字
					orgCodeCameraTipTv.setVisibility(View.GONE);
					// 显示图像缩略图
					orgCodePicIv.setImageBitmap(EUtil.getImageThumbnail(
							mOrgCodePicLocalFilePath,
							EUtil.convertDpToPx(THUMBNAIL_MAX_WIDTH),
							EUtil.convertDpToPx(THUMBNAIL_MAX_HEIGHT)));
					// 开始上传
					if(mOrgCodePicUploader.getStatus() == FileUploader.Status.Started){
						mOrgCodePicUploader.cancel();
					}
					mOrgCodePicUploader.start(mOrgCodePicLocalFilePath);
				}
				else if(mPicType == 2){
					// 获取本地文件名
					mLicensePicLocalFilePath = mLicensePicFile.getAbsolutePath();
					// 隐藏提示文字
					orgLicCameraTipTv.setVisibility(View.GONE);
					// 显示图像缩略图
					orgLicPicIv.setImageBitmap(EUtil.getImageThumbnail(
							mLicensePicLocalFilePath,
							EUtil.convertDpToPx(THUMBNAIL_MAX_WIDTH),
							EUtil.convertDpToPx(THUMBNAIL_MAX_HEIGHT)));
					// 开始上传
					if(mOrgLicPicUploader.getStatus() == FileUploader.Status.Started){
						mOrgLicPicUploader.cancel();
					}
					mOrgLicPicUploader.start(mLicensePicLocalFilePath);
				}
				else if(mPicType == 3){
					// 获取本地文件名
					mLegalPersonIDPicLocalFilePath = mLegalPersonIDPicFile.getAbsolutePath();
					// 隐藏提示文字
					lpIDCameraTipTv.setVisibility(View.GONE);
					// 显示图像缩略图
					lpIDPicIv.setImageBitmap(EUtil.getImageThumbnail(
							mLegalPersonIDPicLocalFilePath,
							EUtil.convertDpToPx(THUMBNAIL_MAX_WIDTH),
							EUtil.convertDpToPx(THUMBNAIL_MAX_HEIGHT)));
					// 开始上传
					if(mLPIDPicUploader.getStatus() == FileUploader.Status.Started){
						mLPIDPicUploader.cancel();
					}
					mLPIDPicUploader.start(mLegalPersonIDPicLocalFilePath);
				}
			}
		}
		else if (requestCode == EConsts.REQ_CODE_PICK_PICTURE) { // 选照
			
			if (resultCode == RESULT_OK) {
				if (mPicType == 0) {
	                Uri originalUri = intent.getData();
	                // String fileName = EUtil.uri2Path(getContentResolver(),originalUri);
	                // 获取文件扩展名
					// mSuffixName = EUtil.getExtensionName(fileName);
	                // 重命名文件
	                /*
					mTempFile.renameTo(new File(mTempFile.getPath(), DateFormat
							.format(CUSTOM_DATE_FORMAT, System.currentTimeMillis())
							+ mSuffixName));
					*/
					// 拷贝文件
					//EUtil.copyFile(fileName, mTempFile.getAbsolutePath());
					// 发送裁切图片请求
					EUtil.dispatchCropPictureIntent(
							RegisterOrganizationDetailActivity.this,
							originalUri, Uri.fromFile(mTempFile),
							EConsts.REQ_CODE_CROP_PICTURE);
				} 
				else if (mPicType == 1) {
					// 获取本地文件名
					mOrgCodePicLocalFilePath = EUtil.uri2Path(
							getContentResolver(), intent.getData());
					// 隐藏提示文字
					orgCodeCameraTipTv.setVisibility(View.GONE);
					// 显示图像缩略图
					orgCodePicIv.setImageBitmap(EUtil.getImageThumbnail(
							mOrgCodePicLocalFilePath,
							EUtil.convertDpToPx(THUMBNAIL_MAX_WIDTH),
							EUtil.convertDpToPx(THUMBNAIL_MAX_HEIGHT)));
					// 上传文件
					if(mOrgCodePicUploader.getStatus() == FileUploader.Status.Started){
						mOrgCodePicUploader.cancel();
					}
					mOrgCodePicUploader.start(mOrgCodePicLocalFilePath);
				} 
				else if (mPicType == 2) {
					// 获取本地文件名
					mLicensePicLocalFilePath = EUtil.uri2Path(
							getContentResolver(), intent.getData());
					// 隐藏文字
					orgLicCameraTipTv.setVisibility(View.GONE);
					// 显示图像缩略图
					orgLicPicIv.setImageBitmap(EUtil.getImageThumbnail(
							mLicensePicLocalFilePath,
							EUtil.convertDpToPx(THUMBNAIL_MAX_WIDTH),
							EUtil.convertDpToPx(THUMBNAIL_MAX_HEIGHT)));
					// 开始上传
					if(mOrgLicPicUploader.getStatus() == FileUploader.Status.Started){
						mOrgLicPicUploader.cancel();
					}
					mOrgLicPicUploader.start(mLicensePicLocalFilePath);
				} 
				else if (mPicType == 3) {
					// 获取本地文件名
					mLegalPersonIDPicLocalFilePath = EUtil.uri2Path(
							getContentResolver(), intent.getData());
					// 隐藏文字
					lpIDCameraTipTv.setVisibility(View.GONE);
					// 显示图像缩略图
					lpIDPicIv.setImageBitmap(EUtil.getImageThumbnail(
							mLegalPersonIDPicLocalFilePath,
							EUtil.convertDpToPx(THUMBNAIL_MAX_WIDTH),
							EUtil.convertDpToPx(THUMBNAIL_MAX_HEIGHT)));
					// 上传文件
					if(mLPIDPicUploader.getStatus() == FileUploader.Status.Started){
						mLPIDPicUploader.cancel();
					}
					mLPIDPicUploader.start(mLegalPersonIDPicLocalFilePath);
				}
			}
		}
		else if(requestCode == EConsts.REQ_CODE_CROP_PICTURE){ // 裁切照片
			if(resultCode == RESULT_OK){ // 裁切成功
				
				// 显示图片
				avatarIv.setImageBitmap(EUtil.getImageThumbnail(mTempFile.getAbsolutePath(), 100, 100));
				// 复制文件
				EUtil.copyFile(mTempFile.getAbsolutePath(),
						mAvatarFile.getAbsolutePath());
				// 删除临时文件
				if (mTempFile.exists()) {
					mTempFile.delete();
				}
				// 文件路径
				mAvatarLocalFilePath =  mAvatarFile.getAbsolutePath();
				// 隐藏提示文字
				avatarCameraTipTv.setVisibility(View.GONE);
				// 开始上传
				if(mAvatarUploader.getStatus() ==FileUploader.Status.Started){
					mAvatarUploader.cancel();
				}
				mAvatarUploader.start(mAvatarLocalFilePath);
			}
			else if(resultCode == RESULT_CANCELED){ // 取消裁切
				
				// 删除临时文件
				if(mTempFile.exists()){
					mTempFile.delete();
				}
			}
			else{ // 裁切失败
			
				// 显示默认图片
				// avatarIv.setImageResource(R.drawable.ic_avatar);
				// 删除头像文件
				if(mAvatarFile.exists()){
					mAvatarFile.delete();
				}
				// 删除临时头像文件
				if(mTempFile.exists()){
					mTempFile.delete();
				}
			}
		}
	}
	
	// 信息完整性检查
	private boolean infoIntegrityCheck(){
		
		// 头像
		if (!mAvatarFile.exists()
				&& mAvatarLocalFilePath.length() <=0) {
			showToast(TIP_AVATAR_MISSING);
			return false;
		}
		// 机构全称
		if (fullNameEt.getText().toString().trim().length() <= 0 ) {
			showToast(TIP_FULLNAME_MISSING);
			return false;
		}
		// 机构全称字数限制
		if (fullNameEt.getText().toString().trim().length() > 25) {
			showToast(TIP_FULLNAME_ERROR);
			return false;
		}
		// 机构简称
		if (shortNameEt.getText().toString().trim().length() <= 0) {
			showToast(TIP_ABBREVIATION_MISSING);
			return false;
		}
		// 机构简称字数限制
		if (shortNameEt.getText().toString().trim().length() > 10) {
			showToast(TIP_ABBREVIATION_ERROR);
			return false;
		}
		// 机构代码图片
		if (!this.mOrgCodePicFile.exists()
				&& this.mOrgCodePicLocalFilePath.length() <=0) {
			showToast(TIP_ORG_CODE_PIC_MISSING);
			return false;
		}
		// 机构营业执照
		if (!this.mLicensePicFile.exists()
				&& this.mLicensePicLocalFilePath.length() <=0) {
			showToast(TIP_ORG_LICENSE_PIC_MISSING);
			return false;
		}
		// 企业法人
		if (!this.mLegalPersonIDPicFile.exists()
				&& this.mLegalPersonIDPicLocalFilePath.length() <=0) {
			showToast(TIP_LEGAL_PERSON_ID_PIC_MISSING);
			return false;
		}
		// 是否有文件在上传
		if(mAvatarUploader.getStatus()== FileUploader.Status.Started){
			showToast(TIP_AVATAR_UPLOADING);
			return false;
		}
		if(mOrgCodePicUploader.getStatus()== FileUploader.Status.Started){
			showToast(TIP_ORG_CODE_PIC_UPLOADING);
			return false;
		}
		if(mOrgLicPicUploader.getStatus()== FileUploader.Status.Started){
			showToast(TIP_ORG_LIC_PIC_UPLOADING);
			return false;
		}
		if(mLPIDPicUploader.getStatus()== FileUploader.Status.Started){
			showToast(TIP_LP_ID_PIC_UPLOADING);
			return false;
		}
		return true;
	}
	
	private OnClickListener mClickListener =new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v == avatarParentRl){ // 头像
				mPicType = 0;
				EUtil.showPhotoSelectDialog(
						RegisterOrganizationDetailActivity.this,
						mDlgClickListener);
			}
			else if(v == orgCodeCameraIv){ // 机构组织代码图
				mPicType = 1;
				EUtil.showPhotoSelectDialog(
						RegisterOrganizationDetailActivity.this,
						mDlgClickListener);
			}
			else if(v == orgLicCameraIv){ // 营业执照图
				mPicType = 2;
				EUtil.showPhotoSelectDialog(
						RegisterOrganizationDetailActivity.this,
						mDlgClickListener);
			}
			else if(v == lpIDCameraIv){ // 法人身份证图
				mPicType = 3;
				EUtil.showPhotoSelectDialog(
						RegisterOrganizationDetailActivity.this,
						mDlgClickListener);
			}
			else if(v == submitLettersTv){ // 提交公函
				
			}
			else if(v == submitTv){ // 进入主界面
				if(!infoIntegrityCheck()){
					return;
				}
					// 显示加载框
					showLoadingDialog();
					// 发送请求
					UserReqUtil.doFullOrganizationAuth(
							RegisterOrganizationDetailActivity.this, mBindData,
							UserReqUtil.getDoFullOrganizationAuthParams(mMainApp.getAppData().getUser().mID,
									mAvatarFileUrl, fullNameEt.getText().toString().trim(), 
									shortNameEt.getText().toString().trim(), 
									mOrgCodePicFileUrl, mLicensePicFileUrl, 
									mLegalPersonIDPicFileUrl), null);
			}
		}
	};
	
	private DialogInterface.OnClickListener mDlgClickListener=new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			if(which==0){ // 拍照
				if(mPicType == 0){ // 头像
					mTempFile = new File(
							EUtil.getAppCacheFileDir(RegisterOrganizationDetailActivity.this),
							System.currentTimeMillis() + ".jpg");
					EUtil.dispatchTakePictureIntent(
							RegisterOrganizationDetailActivity.this,
							Uri.fromFile(mTempFile), EConsts.REQ_CODE_TAKE_PICTURE);
				}
				else if(mPicType == 1){ // 机构组织代码图片
					EUtil.dispatchTakePictureIntent(
							RegisterOrganizationDetailActivity.this,
							Uri.fromFile(mOrgCodePicFile), EConsts.REQ_CODE_TAKE_PICTURE);
				}
				else if(mPicType == 2){ // 营业执照图片
					EUtil.dispatchTakePictureIntent(
							RegisterOrganizationDetailActivity.this,
							Uri.fromFile(mLicensePicFile), EConsts.REQ_CODE_TAKE_PICTURE);
				}
				else if(mPicType == 3){ // 法人图片
					EUtil.dispatchTakePictureIntent(
							RegisterOrganizationDetailActivity.this,
							Uri.fromFile(mLegalPersonIDPicFile), EConsts.REQ_CODE_TAKE_PICTURE);
				}
			}
			else{ // 选照
				EUtil.dispatchPickPictureIntent(
						RegisterOrganizationDetailActivity.this,
						EConsts.REQ_CODE_PICK_PICTURE);
			}
		}
	};
	
	private IBindData mBindData = new IBindData(){

		@Override
		public void bindData(int type, Object object) {
			if(!isLoadingDialogShowing()){
				return;
			}
			else{
				dismissLoadingDialog();
			}
			// TODO Auto-generated method stub
			if(type == EAPIConsts.ReqType.FULL_ORGANIZATION_AUTH){
				// 处理数据
				if (object != null) {
					DataBox dataBox = (DataBox) object;
					if (dataBox.mIsSuccess) { // 进入审核界面
						
						// 更新用户状态
						mMainApp.getAppData().getUser().mOrganizationInfo.mState = 0;
						mMainApp.getAppData().setUser(mMainApp.getAppData().getUser());
						// 跳转到审核页面
						Intent intent = new Intent(
								RegisterOrganizationDetailActivity.this,
								AuditResultActivity.class);
						startActivity(intent);
						finish();
					}
				}
			}
		}
	};
	
	@SuppressLint("HandlerLeak") 
	private Handler mHandler=new Handler(){
		
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			// 头像
			case MSG_UPLOAD_AVATAR_PREPARED:
			case MSG_UPLOAD_AVATAR_STARTED:
				avatarLoadingPb.setVisibility(View.VISIBLE);
				break;
			case MSG_UPLOAD_AVATAR_CANCELED:
			case MSG_UPLOAD_AVATAR_ERROR:
			case MSG_UPLOAD_AVATAR_SUCCESS:
				avatarLoadingPb.setVisibility(View.GONE);
				break;
			// 机构代码
			case MSG_UPLOAD_CODE_PIC_PREPARED:
			case MSG_UPLOAD_CODE_PIC_STARTED:
				orgCodeLoadingPb.setVisibility(View.VISIBLE);
				break;
			case MSG_UPLOAD_CODE_PIC_CANCELED:
			case MSG_UPLOAD_CODE_PIC_ERROR:
			case MSG_UPLOAD_CODE_PIC_SUCCESS:
				orgCodeLoadingPb.setVisibility(View.GONE);
				break;
			// 营业执照
			case MSG_UPLOAD_LIC_PIC_PREPARED:
			case MSG_UPLOAD_LIC_PIC_STARTED:
				orgLicLoadingPb.setVisibility(View.VISIBLE);
				break;
			case MSG_UPLOAD_LIC_PIC_CANCELED:
			case MSG_UPLOAD_LIC_PIC_ERROR:
			case MSG_UPLOAD_LIC_PIC_SUCCESS:
				orgLicLoadingPb.setVisibility(View.GONE);
				break;
			// 企业法人
			case MSG_UPLOAD_LPID_PIC_PREPARED:
			case MSG_UPLOAD_LPID_PIC_STARTED:
				lpIDLoadingPb.setVisibility(View.VISIBLE);
				break;
			case MSG_UPLOAD_LPID_PIC_CANCELED:
			case MSG_UPLOAD_LPID_PIC_ERROR:
			case MSG_UPLOAD_LPID_PIC_SUCCESS:
				lpIDLoadingPb.setVisibility(View.GONE);
				break;
			}
		}
	};
	
	private OnFileUploadListener mAvatarUploadListener= new OnFileUploadListener(){

		@Override
		public void onPrepared() {
			// TODO Auto-generated method stub
			mHandler.sendEmptyMessage(MSG_UPLOAD_AVATAR_PREPARED);
		}

		@Override
		public void onStarted() {
			// TODO Auto-generated method stub
			mHandler.sendEmptyMessage(MSG_UPLOAD_AVATAR_STARTED);
		}

		@Override
		public void onUpdate(int value) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onCanceled() {
			// TODO Auto-generated method stub
			mHandler.sendEmptyMessage(MSG_UPLOAD_AVATAR_CANCELED);
		}

		@Override
		public void onSuccess(String fileUrl) {
			// TODO Auto-generated method stub
			mAvatarFileUrl = fileUrl;
			mHandler.sendEmptyMessage(MSG_UPLOAD_AVATAR_SUCCESS);
		}

		@Override
		public void onError(int code,String message) {
			// TODO Auto-generated method stub
			mHandler.sendEmptyMessage(MSG_UPLOAD_AVATAR_ERROR);
		}
		
	};
	
	private OnFileUploadListener mOrgCodePicUploadListener= new OnFileUploadListener(){

		@Override
		public void onPrepared() {
			// TODO Auto-generated method stub
			mHandler.sendEmptyMessage(MSG_UPLOAD_CODE_PIC_PREPARED);
		}

		@Override
		public void onStarted() {
			// TODO Auto-generated method stub
			mHandler.sendEmptyMessage(MSG_UPLOAD_CODE_PIC_STARTED);
		}

		@Override
		public void onUpdate(int value) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onCanceled() {
			// TODO Auto-generated method stub
			mHandler.sendEmptyMessage(MSG_UPLOAD_CODE_PIC_CANCELED);
		}

		@Override
		public void onSuccess(String fileUrl) {
			// TODO Auto-generated method stub
			mOrgCodePicFileUrl = fileUrl;
			mHandler.sendEmptyMessage(MSG_UPLOAD_CODE_PIC_SUCCESS);
		}

		@Override
		public void onError(int code,String message) {
			// TODO Auto-generated method stub
			mHandler.sendEmptyMessage(MSG_UPLOAD_CODE_PIC_ERROR);
		}
		
	};
	
	private OnFileUploadListener mOrgLicPicUploadListener= new OnFileUploadListener(){

		@Override
		public void onPrepared() {
			// TODO Auto-generated method stub
			mHandler.sendEmptyMessage(MSG_UPLOAD_LIC_PIC_PREPARED);
		}

		@Override
		public void onStarted() {
			// TODO Auto-generated method stub
			mHandler.sendEmptyMessage(MSG_UPLOAD_LIC_PIC_STARTED);
		}

		@Override
		public void onUpdate(int value) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onCanceled() {
			// TODO Auto-generated method stub
			mHandler.sendEmptyMessage(MSG_UPLOAD_LIC_PIC_CANCELED);
		}

		@Override
		public void onSuccess(String fileUrl) {
			// TODO Auto-generated method stub
			mLicensePicFileUrl = fileUrl;
			mHandler.sendEmptyMessage(MSG_UPLOAD_LIC_PIC_SUCCESS);
		}

		@Override
		public void onError(int code,String message) {
			// TODO Auto-generated method stub
			mHandler.sendEmptyMessage(MSG_UPLOAD_LIC_PIC_ERROR);
		}
		
	};
	
	
	private OnFileUploadListener mLPIDPicUploadListener= new OnFileUploadListener(){

		@Override
		public void onPrepared() {
			// TODO Auto-generated method stub
			mHandler.sendEmptyMessage(MSG_UPLOAD_LPID_PIC_PREPARED);
		}

		@Override
		public void onStarted() {
			// TODO Auto-generated method stub
			mHandler.sendEmptyMessage(MSG_UPLOAD_LPID_PIC_STARTED);
		}

		@Override
		public void onUpdate(int value) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onCanceled() {
			// TODO Auto-generated method stub
			mHandler.sendEmptyMessage(MSG_UPLOAD_LPID_PIC_CANCELED);
		}

		@Override
		public void onSuccess(String fileUrl) {
			// TODO Auto-generated method stub
			mLegalPersonIDPicFileUrl = fileUrl;
			mHandler.sendEmptyMessage(MSG_UPLOAD_LPID_PIC_SUCCESS);
		}

		@Override
		public void onError(int code,String message) {
			// TODO Auto-generated method stub
			mHandler.sendEmptyMessage(MSG_UPLOAD_LPID_PIC_ERROR);
		}
		
	};
}
