package com.utils.common;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.text.format.DateFormat;

import com.tr.App;
import com.tr.db.DownloadFileDBManager;
import com.tr.model.obj.JTFile;

/**
 * @ClassName:     FileDownloader.java
 * @Description:   文件下载类（支持断点续传功能）
 * @Author         leon
 * @Version        v 2.0  
 * @Createed       2014-04-11 
 * @Updated        2014-06-09
 */

public class FileDownloader{

	private final String TAG = getClass().getName();
	
	private Context mContext;
	private OnFileDownloadListener mListener;
	private boolean mCanceled;
	private JTFile mJTFile;
	private int mStatus;
	private DownloadFileDBManager mFileManager; // 本地文件管理
	private int mProgress; // 下载进度
	// private File downloadPath; // 下载路径
	
	// 下载状态
	public static class Status {
		private static final int StatusBase = 0;
		public static final int Prepared = StatusBase + 1;
		public static final int Started = StatusBase + 2;
		public static final int Success = StatusBase + 3;
		public static final int Error = StatusBase + 4;
		public static final int Canceled = StatusBase + 5;
	}
	
	// 错误码状态
	public static class ErrorCode {
		public static final int ErrorCodeBase = 10;
		public static final int NetworkFailed = ErrorCodeBase + 1; // 网络错误
		public static final int InvalidURL = ErrorCodeBase + 2; // 无效的下载地址
		public static final int NoSDCard = ErrorCodeBase + 3; // 没有存储卡
		public static final int OOM = ErrorCodeBase + 4; // 内存溢出
		public static final int Unknown = ErrorCodeBase + 5; // 未知错误
		public static final int InvalidFileSize = ErrorCodeBase + 6; // 文件大小错误
		public static final int InvalidFileInfo = ErrorCodeBase + 7; // 无效的文件信息
	}
		
	// 错误消息提示
	public static class ErrorMessage {
		public static final String NETWORK_FAILED = "下载失败，请重试";
		public static final String InvalidURL = "无效的下载地址";
		public static final String NoSDCard = "没有存储卡";
		public static final String OOM = "内存溢出";
		public static final String Unknown = "未知错误";
		public static final String InvalidFileSize = "未知文件大小";
		public static final String InvalidFileInfo = "无效的文件信息";
	}
	
	/**
	 * 构造函数
	 * @param url
	 */
	public FileDownloader(Context context, JTFile jtFile, OnFileDownloadListener listener) {
		JTFile jtFile2 = new JTFile();
		jtFile2.mFileName = jtFile.mFileName;
		jtFile2.mSuffixName = jtFile.mSuffixName;
		jtFile2.mType = jtFile.mType;
		jtFile2.mUrl = jtFile.mUrl;
		
		mContext = context;
		mJTFile = jtFile2;
		mCanceled = false;
		mListener = listener;
		mProgress = 0;
		// 初始化数据库管理对象
		mFileManager = new DownloadFileDBManager(mContext);
		// 文件信息是否正确
		if(mJTFile == null){
			changeStatus(Status.Error, ErrorCode.InvalidFileInfo, ErrorMessage.InvalidFileInfo);
			return;
		}
		else{
			changeStatus(Status.Prepared, 0, null);
		}
		// 本地文件是否存在
		if(EUtil.isFileExist(mContext,mJTFile)){
			//by d.c changeStatus(Status.Success, 0, null);
			File file = null;
			if(jtFile.mType == JTFile.TYPE_FILE){		
				file = new File(EUtil.getAppCacheFileDir(context), jtFile.mFileName);
			}
			else{
				file = new File(EUtil.getAppFileDir(context), jtFile.mFileName);
			}
			if(file.exists() /*by d.c && file.length() == jtFile.mFileSize*/){
				file.delete();
			}
			//by d.c return;
		}
		// 文件尚未下载完成（获取下载信息）
		JTFile jtfile = EUtil.getFileDownloadInfo(mContext, mJTFile.mUrl);
		if(jtfile != null){
			mJTFile = jtfile;
		}
		// 默认下载路径
		// downloadPath = EUtil.getAppFileDir(context);
	}
	
	/**
	 * 设置下载路径
	 * @param path
	 */
	/*
	public void setDownloadPath(File downloadPath){
		if(downloadPath != null){
			this.downloadPath = downloadPath;
		}
	}
	*/

	/**
	 * 开始下载
	 */
	public void start() {
		
		// 文件信息是否正确
		if(mJTFile == null || mJTFile.mUrl == null || mJTFile.mUrl.length() <= 0){
			changeStatus(Status.Error, ErrorCode.InvalidFileInfo, ErrorMessage.InvalidFileInfo);
			return;
		}
		// 文件尚未下载完成（获取下载信息）
		JTFile jtfile = EUtil.getFileDownloadInfo(mContext, mJTFile.mUrl);
		if(jtfile != null){ // 获取下载信息
			mJTFile = jtfile;
		}
		// 开始下载
		mProgress = 0;
		mCanceled = false;
		changeStatus(Status.Started, 0, null);
		doInBackground();
	}	
	
	/**
	 * 取消下载
	 */
	public void cancel() {
		mCanceled = true;
		changeStatus(Status.Canceled, 0, null);
	}
	
	/**
	 * 获取当前下载状态
	 */
	public int getStatus(){
		return mStatus;
	}
	
	/**
	 * 获取当前下载进度
	 * @return
	 */
	public int getProgress(){
		return mProgress;
	}
	
	/**
	 * 获取关联的文件对象
	 * @return
	 */
	public JTFile getJTFile(){
		return mJTFile;
	}
	
	// 下载状态改变
	private void changeStatus(int status, int extraCode, String extraMsg){
		mStatus = status;
		switch(status){
		case Status.Prepared:
			if(mListener != null){
				mListener.onPrepared(mJTFile.mUrl);
			}
			break;
		case Status.Started:
			if(mListener != null){
				mListener.onStarted(mJTFile.mUrl);
			}
			break;
		case Status.Success:
			// 更新全局变量
			App.getApp().getAppData().getListJTFile().add(0, mJTFile);
			
			App.getApp().getAppData().setListJTFile(App.getApp().getAppData().getListJTFile());
			// 从待下载数据库中删除
			mFileManager.delete(App.getUserID(), mJTFile.mUrl);
			if(mListener != null){
				mListener.onSuccess(mJTFile.mUrl, mJTFile);
			}
			break;
		case Status.Error:
			if(mListener != null){
				mListener.onError(mJTFile.mUrl, extraCode, extraMsg);
			}
			// EUtil.showToast(mContext, extraMsg);
			break;
		case Status.Canceled:
			// 从待下载数据库中删除
			// mFileManager.delete(App.getUserID(), mJTFile.mUrl);
			if(mListener != null){
				mListener.onCanceled(mJTFile.mUrl);
			}
			break;
		}
	}
	
	// 开始后台下载
	protected void doInBackground(){
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					
					// 获取存放路径
					/*
					if (downloadPath == null) {
						changeStatus(Status.Error, ErrorCode.NoSDCard, ErrorMessage.NoSDCard);
						return;
					}
					*/
					URL url = new URL(mJTFile.mUrl);
					// 打开到URL的连接
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setDoOutput(true);
					// 设置续传起点
					conn.setRequestProperty("Range", "bytes=" + mJTFile.mDownloadSize + "-");
					if(mJTFile.mDownloadSize == 0){
						mJTFile.mFileSize = conn.getContentLength();
					}
					else{
						if(mJTFile.mFileSize <= 0){
							changeStatus(Status.Error, ErrorCode.InvalidFileSize, ErrorMessage.InvalidFileSize);
							return;
						}
					}
					if(mJTFile.mFileName == null || mJTFile.mFileName.length() <= 0){
						mJTFile.mFileName =  DateFormat.format("temp_yyyyMMddkkmmss", System.currentTimeMillis()) + "";
					}
					File file = null;
					if(mJTFile.mType == JTFile.TYPE_FILE){ // 2 是文件（需要缓存）
						file = new File(EUtil.getAppCacheFileDir(mContext), mJTFile.mFileName);
					}
					else{ // （不需要缓存）
						file = new File(EUtil.getAppFileDir(mContext), mJTFile.mFileName);
					}
					// 重命名文件
					// file = new File(downloadPath, mJTFile.mFileName);
					
					
					InputStream is = conn.getInputStream();
					OutputStream os = new FileOutputStream(file, true);
					byte[] buffer = new byte[1024];
					int count = 0;
					long length = mJTFile.mDownloadSize;
					long total = mJTFile.mFileSize;
					while ((count = is.read(buffer)) != -1) {
						os.write(buffer, 0, count);
						length += count;
						mJTFile.mDownloadSize += count;
						// 取消下载
						if (mCanceled) {
							mFileManager.synchronous(App.getUserID(), mJTFile);
							os.flush();
							os.close();
							is.close();
							conn.disconnect();
							break;
						} 
						else { 
							// 更新下载进度
							mProgress = (int) ((length * 1.0 / total) * 100);
							if (mListener != null) {
								mListener.onUpdate(mJTFile.mUrl, mProgress);
							}
						}
					}
					if(mCanceled){
						os.flush();
						os.close();
						conn.disconnect();
						return;
					}
					os.flush();
					os.close();
					conn.disconnect();
					// 下载成功，更新信息
					mJTFile.mCreateTime = System.currentTimeMillis();
					mJTFile.mLocalFilePath = file.getAbsolutePath();
					// 修改状态
					changeStatus(Status.Success, 0, mJTFile.mUrl);
				} 
				catch (Exception e) { // 下载失败
					// 更新数据库中的下载进度
					mFileManager.synchronous(App.getUserID(), mJTFile);
					// 显示失败原因
					changeStatus(Status.Error, ErrorCode.Unknown, e.getMessage());
				}
			}
		}).start();
	}
	
	// 下载监听器
	public interface OnFileDownloadListener {
		
		public void onPrepared(String url); //暂停下载
		public void onStarted(String url); //开始下载
		public void onSuccess(String url, JTFile jtFile); //下载成功
		public void onError(String url,int code, String errMsg); //下载错误
		public void onUpdate(String url, int progress); //更新下载进度 
		public void onCanceled(String url);//取消下载
	}
}
