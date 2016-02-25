package com.utils.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;

import com.tr.App;
import com.tr.db.ChatFileDownloadInfoDBManager;
import com.tr.model.obj.JTFile;


/**
 * 会议模式畅聊下载
 * @author leon
 * 只针对视频和文件
 */
public class HyChatFileDownloader {

	private final String TAG = getClass().getName();
	
	private Context context;
	private OnFileDownloadListener listener;
	private boolean canceled;
	private JTFile jtFile;
	private int status;
	private int progress; // 下载进度
	private boolean toastMessage; // 是否弹出消息
	private ChatFileDownloadInfoDBManager dbManager; // 数据库访问对象
	private long meetingId; // 会议id
	private long topicId; // 议题id
	private String messageId; // 消息id
	private File downloadPath; // 下载路径
	
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
		public static final int NetworkError = ErrorCodeBase + 1; // 网络错误
		public static final int InvalidURL = ErrorCodeBase + 2; // 无效的下载地址
		public static final int NoSDCard = ErrorCodeBase + 3; // 没有存储卡
		public static final int OOM = ErrorCodeBase + 4; // 内存溢出
		public static final int Unknown = ErrorCodeBase + 5; // 未知错误
		public static final int InvalidFileSize = ErrorCodeBase + 6; // 文件大小错误
		public static final int InvalidFileInfo = ErrorCodeBase + 7; // 无效的文件信息
		public static final int InvalidDownloadPath = ErrorCodeBase + 8; // 无效下载路径
	}
		
	// 错误消息提示
	public static class ErrorMessage {
		public static final String NetwordError = "下载失败，请重试";
		public static final String InvalidURL = "无效的下载地址";
		public static final String NoSDCard = "没有存储卡";
		public static final String OOM = "内存溢出";
		public static final String Unknown = "未知错误";
		public static final String InvalidFileSize = "未知文件大小";
		public static final String InvalidFileInfo = "无效的文件信息";
		public static final String InvalidDownloadPath = "无效的下载路径";
	}
	
	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	
	/**
	 * 构造函数
	 * @param url
	 */
	public HyChatFileDownloader(Context context, JTFile jtFile, String messageId, long meetingId, long topicId) {
		
		this.context = context;
		this.jtFile = jtFile;
		this.canceled = false;
		progress = 0;
		this.meetingId = 0;
		this.topicId = 0;
		// 文件信息是否正确
		if(jtFile == null){
			changeStatus(Status.Error, ErrorCode.InvalidFileInfo, ErrorMessage.InvalidFileInfo);
			return;
		}
		else{
			changeStatus(Status.Prepared);
		}
		// 下载路径
		this.meetingId = meetingId;
		this.topicId = topicId;
		this.messageId = messageId;
		downloadPath = EUtil.getMeetingChatFileDir(context, this.jtFile.mType, this.meetingId, this.topicId);
		// 校验数据
		dbManager = new ChatFileDownloadInfoDBManager(context);
		if(downloadPath != null){
			long downloadedSize = dbManager.query(App.getUserID(), this.jtFile.mUrl);
			File file = new File(downloadPath, jtFile.mFileName);
			long fileSize = file.length();
			if(downloadedSize != fileSize){
				dbManager.synchronous(App.getUserID(), jtFile.mUrl, 0, jtFile.mFileSize);
				file.delete();
			}
		}
	}

	/**
	 * 开始下载
	 */
	public void start() {
		
		// 文件信息是否正确
		if(jtFile == null){
			changeStatus(Status.Error, ErrorCode.InvalidFileInfo, ErrorMessage.InvalidFileInfo);
			return;
		}
		// 开始下载
		progress = 0;
		canceled = false;
		changeStatus(Status.Started);
		// 检查下载路径
		if(downloadPath == null){
			downloadPath = EUtil.getMeetingChatFileDir(context, jtFile.mType, meetingId, topicId);
			// 再次校验数据
			if (downloadPath != null) {
				long downloadedSize = dbManager.query(App.getUserID(), jtFile.mUrl);
				File file = new File(downloadPath, jtFile.mFileName);
				long fileSize = file.length();
				if(downloadedSize != fileSize){
					dbManager.synchronous(App.getUserID(), jtFile.mUrl, 0, jtFile.mFileSize);
					file.delete();
				}
				else if(downloadedSize == jtFile.mFileSize){ // 下载成功
					changeStatus(Status.Success);
					return;
				}
			}
			else{
				changeStatus(Status.Error, ErrorCode.InvalidDownloadPath, ErrorMessage.InvalidDownloadPath);
				return;
			}
		}
		// 开始下载
		doInBackground();
	}	
	
	/**
	 * 设置下载监听器
	 * @param listener
	 */
	public void setOnFileDownloadListener(OnFileDownloadListener listener){
		this.listener = listener;
	}
	
	/**
	 * 取消下载
	 */
	public void cancel() {
		canceled = true;
		changeStatus(Status.Canceled);
	}
	
	/**
	 * 获取当前下载状态
	 */
	public int getStatus(){
		return status;
	}
	
	/**
	 * 获取当前下载进度
	 * @return
	 */
	public int getProgress(){
		return progress;
	}
	
	/**
	 * 获取关联的文件对象
	 * @return
	 */
	public JTFile getJTFile(){
		return jtFile;
	}
	
	// 更改下载状态
	private void changeStatus(int status, Object...args){
		this.status = status;
		switch(status){
		case Status.Prepared:
			if(listener != null){
				listener.onPrepared(messageId);
			}
			break;
		case Status.Started:
			if(listener != null){
				if(args.length < 1){
					listener.onStarted(messageId);
				}
				else{
					listener.onUpdate(messageId, (Integer) args[0]);
				}
			}
			break;
		case Status.Success:
			if(listener != null){
				listener.onSuccess(messageId, jtFile);
			}
			break;
		case Status.Error:
			if(listener != null){
				if(args.length > 1){
					listener.onError(messageId, (Integer) args[0], (String) args[1]);
				}
			}
			break;
		case Status.Canceled:
			if(listener != null){
				listener.onCanceled(messageId);
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
					URL url = new URL(jtFile.mUrl);
					// 打开到URL的连接
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					// 设置续传起点
					long downloadedSize = dbManager.query(App.getUserID(), jtFile.mUrl);
					conn.setRequestProperty("Range", "bytes=" + downloadedSize + "-");
					File file = new File(downloadPath, jtFile.mFileName);
					InputStream is = conn.getInputStream();
					OutputStream os = new FileOutputStream(file, true);
					byte[] buffer = new byte[1024];
					int count = 0;
					long length = downloadedSize;
					long total = jtFile.mFileSize;
					while ((count = is.read(buffer)) != -1) {
						os.write(buffer, 0, count);
						length += count;
						// 更新下载进度
						progress = (int) ((length * 1.0 / total) * 100);
						changeStatus(Status.Started, progress);
						// 取消下载
						if (canceled) {
							dbManager.update(App.getUserID(), jtFile.mUrl, file.length(), jtFile.mFileSize);
							os.flush();
							os.close();
							is.close();
							break;
						} 
					}
					if(canceled){
						dbManager.update(App.getUserID(), jtFile.mUrl, file.length(), jtFile.mFileSize);
						os.flush();
						os.close();
						return;
					}
					os.flush();
					os.close();
					// 下载成功，更新信息
					dbManager.update(App.getUserID(), jtFile.mUrl, jtFile.mFileSize, jtFile.mFileSize);
					// 修改状态
					changeStatus(Status.Success);
				} 
				catch (Exception e) { 
					// 更新数据库中的下载进度
					File file = new File(downloadPath, jtFile.mFileName);
					dbManager.synchronous(App.getUserID(), jtFile.mUrl, file.length(), jtFile.mFileSize);
					// 显示失败原因
					changeStatus(Status.Error, ErrorCode.Unknown, e.getMessage());
				}
			}
		}).start();
	}
	
	// 下载监听器
	public interface OnFileDownloadListener {
		public void onPrepared(String messageId);
		public void onStarted(String messageId);
		public void onSuccess(String messageId, JTFile jtFile);
		public void onError(String messageId, int errCode, String errMsg);
		public void onUpdate(String messageId, int progress);
		public void onCanceled(String messageId);
	}
}
