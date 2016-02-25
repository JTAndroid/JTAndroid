package com.tr.ui.widgets;

import java.io.File;

import com.tr.App;
import com.tr.R;
import com.tr.model.obj.JTFile;
import com.tr.service.FileDownloadService;
import com.utils.common.EConsts;
import com.utils.common.EUtil;
import com.utils.common.OpenFiles;
import com.utils.common.FileDownloader.OnFileDownloadListener;
import com.utils.common.FileUploader;
import com.utils.common.FileUploader.OnFileUploadListener;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @ClassName:     FileDownloadStatusLinearLayout.java
 * @Description:   文件下载状态条（包括文件名，文件大小和取消按钮）
 * @Author         leon
 * @Version        v 1.0  
 * @Date           2014-04-04 
 */
public class FileDownloadStatusLinearLayout extends LinearLayout {

	private String TAG = getClass().getSimpleName();
	
	// 常量
	// private final int FILE_NAME_LIMIT = 10; // 文件名长度（最大8个字符）
	
	// 控件
	private ProgressBar progressPb; // 文件下载进度
	private TextView fileNameTv; // 文件名
	private TextView fileSizeTv; // 文件大小
	// private ImageView cancelIv; // 取消上传
	private TextView retryTv; // 重新下载（开始下载）
	
	// 变量
	private Context mContext; // 上下文对象
	private JTFile mJTFile; // 文件对象
	
	public FileDownloadStatusLinearLayout(Context context) {
		super(context);
		initVars(context);
		initControls();
	}

	public FileDownloadStatusLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initVars(context);
		initControls();
	}
	
	private void initVars(Context context){
		mContext = context;
		mJTFile = new JTFile();
	}
	
	private void initControls() {
		// 初始化根面板
		LayoutInflater.from(mContext).inflate(R.layout.widget_file_download_status,
				this);
		// 文件名
		fileNameTv = (TextView) findViewById(R.id.fileNameTv);
		// 文件大小
		fileSizeTv = (TextView) findViewById(R.id.fileSizeTv);
		// fileSizeTv.setVisibility(View.GONE);
		// 取消上传
		// cancelIv = (ImageView) findViewById(R.id.cancelIv);
		// cancelIv.setOnClickListener(mClickListener);
		// 进度条
		progressPb = (ProgressBar) findViewById(R.id.progressPb);
		// 重试
		retryTv = (TextView) findViewById(R.id.retryTv);
		retryTv.setOnClickListener(mClickListener);
		// 设置面板的点击事件
		setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (mJTFile.mLocalFilePath.length() > 0) { // 是否下载成功
					OpenFiles.open(mContext, mJTFile.mLocalFilePath);
				}
			}
		});
	}
	
	// 点击事件监听器
	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) { 
			// TODO Auto-generated method stub
			/*
			if(v == cancelIv){  // 取消下载
				// 隐藏
				onCancel();
				// 通知下载服务取消下载
				Intent intent = new Intent(EConsts.Action.DOWNLOAD_CANCELED);
				intent.setClass(mContext, FileDownloadService.class);
				intent.putExtra(EConsts.Key.WEB_FILE_URL, mJTFile.mUrl);
				mContext.startService(intent); 
			}
			*/
			if(v == retryTv){ // 开始下载/重新下载
				
				if(retryTv.getText().equals("开始下载")
						|| retryTv.getText().equals("重试")){
					
					// 设置界面状态
					onStart();
					 // 通知下载服务开始下载/重新下载
					Intent intent = new Intent(EConsts.Action.DOWNLOAD_START);
					intent.setClass(mContext, FileDownloadService.class);
					intent.putExtra(EConsts.Key.JT_FILE, mJTFile);
					mContext.startService(intent);
				}
				else if(retryTv.getText().equals("打开")){
					OpenFiles.open(mContext, mJTFile.mLocalFilePath);
				}
			}
		}
	};
	
	/**
	 * 设置关联的文件
	 * @param file
	 */
	public void setJTFile(JTFile jtFile) {
		mJTFile = jtFile;
		boolean isExist = false;
		for (JTFile file : App.getApp().getAppData().getListJTFile()) {
			if (file.mUrl.equals(mJTFile.mUrl)) {
				isExist = true;
				mJTFile = file;
				break;
			}
		}
		if(!isExist){ // 未下载
			onPrepare();
		}
		else{ // 已下载
			onSuccess(mJTFile.mLocalFilePath);
		}
	}
	
	/**
	 * 获取关联的文件
	 * @return
	 */
	public JTFile getJTFile(){
		return mJTFile;
	}
	
	/**
	 * 准备下载
	 */
	public void onPrepare(){
		// 文件名
		fileNameTv.setText(mJTFile.mFileName);
		// 下载进度
		progressPb.setProgress(0);
		// 下载按钮
		// retryTv.setOnClickListener(mClickListener);
		retryTv.setText("开始下载");
		retryTv.setTextColor(mContext.getResources().getColor(R.color.commen_text_color_3));
		// 文件大小	
		fileSizeTv.setText(EUtil.formatFileSize(mJTFile.mFileSize));
	}
	
	/**
	 * 开始下载
	 */
	public void onStart(){
		progressPb.setProgress(0);
		retryTv.setText("0%");
		retryTv.setTextColor(mContext.getResources().getColor(R.color.commen_text_color_2));
		// retryTv.setOnClickListener(null);
	}
	
	/**
	 * 下载进度更新
	 * @param progress
	 */
	public void OnUpdate(int progress){
		progressPb.setProgress(progress);
		retryTv.setText(progress + "%");
	}
	
	/**
	 * 下载失败
	 * @param errMessage
	 */
	public void onError(String errMessage){
		// fileNameTv.setText(mJTFile.mFileName);
		progressPb.setProgress(0);
		retryTv.setText("重试");
		retryTv.setTextColor(mContext.getResources().getColor(R.color.commen_text_color_3));
		// retryTv.setOnClickListener(mClickListener);
	}
	
	/**
	 * 下载成功
	 * @param url
	 */
	public void onSuccess(String localFilePath){
		mJTFile.mLocalFilePath = localFilePath;
		fileNameTv.setText(mJTFile.mFileName);
		progressPb.setProgress(0);
		retryTv.setText("打开");
		retryTv.setTextColor(mContext.getResources().getColor(R.color.commen_text_color_2));
		// retryTv.setOnClickListener(null);
		fileSizeTv.setText(EUtil.formatFileSize(mJTFile.mFileSize));
	}
	
	
	
	/**
	 * 取消下载
	 */
	public void onCancel(){
		onPrepare();
	}
}
