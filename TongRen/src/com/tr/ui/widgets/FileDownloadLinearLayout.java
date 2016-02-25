package com.tr.ui.widgets;

import java.util.ArrayList;
import java.util.List;

import com.tr.model.obj.JTFile;
import com.utils.common.FileDownloader.OnFileDownloadListener;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class FileDownloadLinearLayout extends LinearLayout implements OnFileDownloadListener{

	private String TAG = getClass().getSimpleName();
	
	private Context mContext; // 上下文对象
	
	public FileDownloadLinearLayout(Context context) {
		super(context);
		initVars(context);
	}

	public FileDownloadLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initVars(context);
	}
	
	private void initVars(Context context){
		mContext = context;
	}
	
	/**
	 * 设置待下载的文件集合
	 * @param files
	 */
	public void setJTFiles(List<JTFile> files){
		// 移除所有子项
		removeAllViews();
		// 添加新子项
		for (int i = 0; i < files.size(); i++) {
			FileDownloadStatusLinearLayout statusLl = new FileDownloadStatusLinearLayout(
					mContext);
			// 设置要下载的文件对象
			statusLl.setJTFile(files.get(i));
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			params.setMargins(0, 15, 0, 0);
			statusLl.setLayoutParams(params);
			// 添加到待上传文件容器
			addView(statusLl);
			// 刷新界面
			invalidate();
		}
	}

	@Override
	public void onPrepared(String url) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStarted(String url) {
		// TODO Auto-generated method stub
		for (int i = 0; i < getChildCount(); i++) {
			FileDownloadStatusLinearLayout statusLl = (FileDownloadStatusLinearLayout) getChildAt(i);
			if (statusLl.getVisibility() == View.VISIBLE
					&& statusLl.getJTFile().mUrl.equals(url)) {
				statusLl.onStart(); // 开始下载
			}
		}
	}

	@Override
	public void onSuccess(String url, JTFile jtFile) {
		// TODO Auto-generated method stub
		for (int i = 0; i < getChildCount(); i++) {
			FileDownloadStatusLinearLayout statusLl = (FileDownloadStatusLinearLayout) getChildAt(i);
			if (statusLl.getVisibility() == View.VISIBLE
					&& statusLl.getJTFile().mUrl.equals(url)) {
				statusLl.onSuccess(jtFile.mLocalFilePath); // 下载成功
			}
		}
	}

	@Override
	public void onError(String url,int extraCode, String errMessage) {
		// TODO Auto-generated method stub
		for (int i = 0; i < getChildCount(); i++) {
			FileDownloadStatusLinearLayout statusLl = (FileDownloadStatusLinearLayout) getChildAt(i);
			if (statusLl.getVisibility() == View.VISIBLE
					&& statusLl.getJTFile().mUrl.equals(url)) {
				statusLl.onError(errMessage); // 下载失败
			}
		}
	}

	@Override
	public void onUpdate(String url, int progress) {
		// TODO Auto-generated method stub
		for (int i = 0; i < getChildCount(); i++) {
			FileDownloadStatusLinearLayout statusLl = (FileDownloadStatusLinearLayout) getChildAt(i);
			if (statusLl.getVisibility() == View.VISIBLE
					&& statusLl.getJTFile().mUrl.equals(url)) {
				statusLl.OnUpdate(progress); // 下载进度更新
			}
		}
	}

	@Override
	public void onCanceled(String url) {
		// TODO Auto-generated method stub
		for (int i = 0; i < getChildCount(); i++) {
			FileDownloadStatusLinearLayout statusLl = (FileDownloadStatusLinearLayout) getChildAt(i);
			if (statusLl.getVisibility() == View.VISIBLE
					&& statusLl.getJTFile().mUrl.equals(url)) {
				statusLl.onCancel(); // 取消下载
			}
		}
	}
}
