package com.tr.ui.widgets;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.tr.App;
import com.tr.R;
import com.tr.model.obj.JTFile;
import com.tr.ui.base.JBaseFragmentActivity.OnFileSelectedListener;
import com.utils.common.FileUploader;
import com.utils.common.TaskIDMaker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @ClassName:     FileUploaderLinearLayout.java
 * @Description:   文件状态（包括文件名，文件大小和取消按钮）
 * @Author         leon
 * @Version        v 1.0  
 * @Create         2014-04-04 
 * @Update         2014-04-15
 */
public class FileUploaderLinearLayout extends LinearLayout {

	private String TAG = getClass().getSimpleName();
	
	// 控件
	private FileSelectorTextView addPicTv; // 添加图片
	private FileSelectorTextView addVideoTv; // 添加视频
	private FileSelectorTextView addDocTv; // 添加文档
	private FileSelectorTextView addRecTv; // 添加录音（amr格式）文件
	private LinearLayout uploadQueueParentLl; // 待上传文件队列
	
	// 变量
	private Context mContext; // 上下文对象
	private String mTaskId; // 附件索引
	private String mUserId; // 用户Id
	private int mModuleType; // 所属类型
	
	public FileUploaderLinearLayout(Context context) {
		super(context);
		initVars(context);
		initControls();
	}

	public FileUploaderLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initVars(context);
		initControls();
	}
	
	/**
	 * 设置上传参数
	 * @param taskId
	 * @param moduleType
	 * @param userId
	 */
	public void setParams(String taskId,int moduleType,String userId){
		mTaskId = taskId;
		mModuleType = moduleType;
		mUserId = userId;
	}
	
	private void initVars(Context context){
		mContext = context;
	}
	
	private void initControls() {
		// 根面板
		LayoutInflater.from(mContext).inflate(R.layout.widget_file_uploader,
				this);
		// 待上传文件队列
		uploadQueueParentLl = (LinearLayout) findViewById(R.id.uploadQueueParentLl);
		// 添加图片按钮
		addPicTv = (FileSelectorTextView) findViewById(R.id.addPicTv);
		addPicTv.setOnFileSelectedListener(mFileSelectedListener);
		// 添加视频按钮
		addVideoTv = (FileSelectorTextView) findViewById(R.id.addVideoTv);
		addVideoTv.setOnFileSelectedListener(mFileSelectedListener);
		// 添加文档按钮
		addDocTv = (FileSelectorTextView) findViewById(R.id.addDocTv);
		addDocTv.setOnFileSelectedListener(mFileSelectedListener);
		// 添加录音按钮
		addRecTv = (FileSelectorTextView) findViewById(R.id.addRecTv);
		addRecTv.setOnFileSelectedListener(mFileSelectedListener);
	}
	
	// 文件选择监听器
	private OnFileSelectedListener mFileSelectedListener=new OnFileSelectedListener(){

		@Override
		public void onFileSelected(String filePath) {
			// TODO Auto-generated method stub
			FileUploadStatusLinearLayout statusLl = new FileUploadStatusLinearLayout(
					mContext);
			// 设置下载参数
			statusLl.mFileUploader.setParams(mTaskId, mModuleType, mUserId);
			// 开始下载
			statusLl.mFileUploader.start(filePath);
			// 设置控件间的间隔
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			params.setMargins(0, 15, 0, 0);
			statusLl.setLayoutParams(params);
			// 添加到待上传文件容器
			uploadQueueParentLl.addView(statusLl);
			// 刷新界面
			invalidate();
		}

		@Override
		public void onFilesSelected(List<String> filePaths) {
			// TODO Auto-generated method stub
			for (int i = 0; i < filePaths.size(); i++) {
				// 文件状态面板
				FileUploadStatusLinearLayout statusLl = new FileUploadStatusLinearLayout(
						mContext);
				// 设置下载参数
				statusLl.mFileUploader.setParams(mTaskId, mModuleType, mUserId);
				// 开始下载
				statusLl.mFileUploader.start(filePaths.get(i));
				// 设置控件间的间隔
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				params.setMargins(0, 15, 0, 0);
				statusLl.setLayoutParams(params);
				// 添加到待上传文件容器
				uploadQueueParentLl.addView(statusLl);
				// 刷新界面
				invalidate();
			}
		}
	};
	
	/**
	 * 是否有文件正在上传
	 * @return
	 */
	public boolean isFileUploading() {

		for (int i = 0; i < uploadQueueParentLl.getChildCount(); i++) {
			FileUploadStatusLinearLayout statusLl = (FileUploadStatusLinearLayout) uploadQueueParentLl
					.getChildAt(i);
			if (statusLl.getVisibility() == View.VISIBLE
					&& statusLl.mFileUploader.getStatus() == FileUploader.Status.Started) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获取所有已上传文件对象
	 * @return
	 */
	public List<JTFile> getListJTFile(){
		
		List<JTFile> listJTFile = new ArrayList<JTFile>();
		for (int i = 0; i < uploadQueueParentLl.getChildCount(); i++) {
			FileUploadStatusLinearLayout statusLl = (FileUploadStatusLinearLayout) uploadQueueParentLl
					.getChildAt(i);
			// 是否下载完成
			if (statusLl.getVisibility() == View.VISIBLE
					&& !statusLl.mFileUploader.getJTFile().mUrl.equals("")) {
				listJTFile.add(statusLl.mFileUploader.getJTFile());
			}
		}
		return listJTFile;
	}
	
	/**
	 * 取消上传
	 */
	public void cancelUploading(){
		
		for (int i = 0; i < uploadQueueParentLl.getChildCount(); i++) {
			FileUploadStatusLinearLayout statusLl = (FileUploadStatusLinearLayout) uploadQueueParentLl
					.getChildAt(i);
			statusLl.mFileUploader.cancel();
		}
	}
}
