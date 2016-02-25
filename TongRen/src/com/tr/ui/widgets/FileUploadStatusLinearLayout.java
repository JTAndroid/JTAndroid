package com.tr.ui.widgets;

import java.io.File;

import com.tr.R;
import com.tr.api.UserReqUtil;
import com.tr.model.obj.JTFile;
import com.utils.common.EUtil;
import com.utils.common.FileUploader;
import com.utils.common.FileUploader.OnFileUploadListener;
import com.utils.common.OpenFiles;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @ClassName:     FileUploadStatusLinearLayout.java
 * @Description:   文件上传状态条（包括文件名，文件大小和取消按钮）
 * @Author         leon
 * @Version        v 1.0  
 * @Date           2014-04-04 
 */
public class FileUploadStatusLinearLayout extends LinearLayout {

	private String TAG = getClass().getSimpleName();

	// 常量
	private final int MSG_BASE = 100;
	private final int MSG_PREPARED = MSG_BASE + 1;
	private final int MSG_STARTED = MSG_BASE + 2;
	private final int MSG_UPDATE = MSG_BASE + 3;
	private final int MSG_SUCCESS = MSG_BASE + 4;
	private final int MSG_ERROR = MSG_BASE + 5;
	private final int MSG_CANCELED = MSG_BASE + 6;
	
	// 控件
	private ProgressBar progressPb; // 文件上传进度
	private TextView fileNameTv; // 文件名
	private TextView fileSizeTv; // 文件大小
	private ImageView cancelIv; // 取消上传
	private TextView retryTv; // 重新上传
	
	// 变量
	private Context mContext; // 上下文对象
	public FileUploader mFileUploader; // 文件上传类
	
	public FileUploadStatusLinearLayout(Context context) {
		super(context);
		initVars(context);
		initControls();
	}

	public FileUploadStatusLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initVars(context);
		initControls();
	}
		
	private void initVars(Context context){
		mContext = context;
		mFileUploader = new FileUploader(mFileUploadListener);
	}
	
	private void initControls() {
		// 初始化根面板
		LayoutInflater.from(mContext).inflate(R.layout.widget_file_upload_status,
				this);
		// 文件名
		fileNameTv = (TextView) findViewById(R.id.fileNameTv);
		// 文件大小
		fileSizeTv = (TextView) findViewById(R.id.fileSizeTv);
		// 取消上传
		cancelIv = (ImageView) findViewById(R.id.cancelIv);
		cancelIv.setOnClickListener(mClickListener);
		// 进度条
		progressPb = (ProgressBar) findViewById(R.id.progressPb);
		// 重试
		retryTv = (TextView) findViewById(R.id.retryTv);
		retryTv.setOnClickListener(mClickListener);
		// 设置点击事件
		setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				OpenFiles.open(mContext, mFileUploader.getJTFile().mLocalFilePath);
			}
		});
	}
	
	// 取消上传
	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(v == cancelIv){ // 取消上传
				if(StringUtils.isEmpty(mFileUploader.getJTFile().getId())){ 
					setVisibility(View.GONE);
					mFileUploader.cancel();
				}
				else{
					setVisibility(View.GONE);
					UserReqUtil.doDeleteFile(mContext, bindData, 
							UserReqUtil.getDoDeleteFileParams(mFileUploader.getJTFile().getId()), null);
				}
			}
			else if(v == retryTv){ // 重试
				mFileUploader.start();
			}
		}
	};
	
	
	// 文件上传监听器
	private OnFileUploadListener mFileUploadListener = new OnFileUploadListener(){

		@Override
		public void onStarted() { // 开始上传
			// TODO Auto-generated method stub
			mHandler.obtainMessage(MSG_STARTED).sendToTarget();
		}

		@Override
		public void onUpdate(int value) { // 更新进度
			// TODO Auto-generated method stub
			mHandler.obtainMessage(MSG_UPDATE, value).sendToTarget();
		}

		@Override
		public void onCanceled() {
			// TODO Auto-generated method stub
			mHandler.obtainMessage(MSG_CANCELED).sendToTarget();
		}

		@Override
		public void onSuccess(String fileUrl) {
			// TODO Auto-generated method stub
			mHandler.obtainMessage(MSG_SUCCESS, fileUrl).sendToTarget();
		}

		@Override
		public void onError(int code,String message) {
			// TODO Auto-generated method stub
			Message msg = new Message();
			msg.what = MSG_ERROR;
			msg.arg1 = code;
			msg.obj = message;
			mHandler.sendMessage(msg);
		}

		@Override
		public void onPrepared() {
			// TODO Auto-generated method stub
			mHandler.obtainMessage(MSG_PREPARED).sendToTarget();
		}
	};
	
	// 消息处理器
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){
		
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case MSG_PREPARED: // 准备就绪
				fileNameTv.setText(mFileUploader.getJTFile().mFileName);
				fileSizeTv.setText(EUtil.formatFileSize(mFileUploader.getJTFile().mFileSize));
				retryTv.setText("准备上传");
				retryTv.setOnClickListener(null);
				break;
			case MSG_STARTED: // 开始上传
				// fileNameTv.setText("上传中......");
				retryTv.setText("0%");
				retryTv.setTextColor(getResources().getColor(R.color.commen_text_color_1));
				retryTv.setOnClickListener(null);
				// fileSizeTv.setVisibility(View.GONE);
				break;
			case MSG_UPDATE: // 上传更新
				progressPb.setProgress((Integer) msg.obj);
				retryTv.setText( msg.obj + "%");
				break;
			case MSG_ERROR: // 上传失败（根据原因判断）
				if(msg.arg1 == FileUploader.ErrorCode.NetworkError){
					progressPb.setProgress(0);
					// fileNameTv.setText(mFileUploader.getJTFile().mFileName);
					retryTv.setText("重试");
					retryTv.setTextColor(mContext.getResources().getColor(
							R.color.commen_text_color_3));
					// retryTv.setVisibility(View.VISIBLE);
					retryTv.setOnClickListener(mClickListener);
					// fileSizeTv.setVisibility(View.VISIBLE);
				}
				else{
					setVisibility(View.GONE); // 隐藏面板
				}
				Toast.makeText(mContext, msg.obj + "", Toast.LENGTH_LONG).show();
				break;
			case MSG_SUCCESS: // 上传成功
				// fileNameTv.setText(mFileUploader.getJTFile().mFileName);
				progressPb.setProgress(0);
				// retryTv.setVisibility(View.INVISIBLE);
				retryTv.setText("已上传");
				retryTv.setTextColor(getResources().getColor(R.color.commen_text_color_1));
				retryTv.setOnClickListener(null);
			    // fileSizeTv.setVisibility(View.VISIBLE);
				// cancelIv.setVisibility(View.GONE);
				break;
			case MSG_CANCELED: // 上传取消
				setVisibility(View.GONE);
				break;
			}
		}
	};
	
	private IBindData bindData = new IBindData(){

		@Override
		public void bindData(int tag, Object object) {
			
		}		
	};
}
