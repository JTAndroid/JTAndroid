package com.tr.ui.demand.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.tr.App;
import com.tr.model.obj.JTFile;
import com.utils.common.EUtil;
import com.utils.common.TaskIDMaker;
import com.utils.http.EAPIConsts;

/**
 * @ClassName:     FileUploader.java
 * @Description:   文件上传类
 * @Author         leon
 * @Version        v 1.0  
 * @Createed       2014-04-11 
 * @Updated        2014-04-11
 */
public class FileUploader{

	private final String TAG = getClass().getSimpleName();
	
	// 常量
	public  String WEB_URL = EAPIConsts.FILE_URL; // 服务器地址

	// 上传状态
	public static class Status {
		private static final int StatusBase = 0;
		public static final int Prepared = StatusBase + 1;
		public static final int Started = StatusBase + 2;
		public static final int Success = StatusBase + 3;
		public static final int Error = StatusBase + 4;
		public static final int Canceled = StatusBase + 5;
	}
	
	// 错误码状态
	public static class ErrorCode{
		private static final int ErrorCodeBase = 10;
		public static final int NetworkError = ErrorCodeBase + 1;
		public static final int FileNotExist = ErrorCodeBase + 2;
		public static final int OOMError = ErrorCodeBase + 3;
		public static final int FileExceedLimit = ErrorCodeBase + 4;
		public static final int UnknownError = ErrorCodeBase + 5;
	}
	
	// 错误消息提示
	public static class ErrorMessage{
		public static final String NETWORK_FAILED = "上传失败，请重试";
		public static final String FILE_NOT_EXIST = "本地文件不存在";
		public static final String FILE_SIZE_EXCEED_LIMIT = "文件大小不能超过10M";
		public static final String OOM = "内存溢出";
		public static final String UNKNOWN_ERROR = "未知错误";
	}
	
	// 变量
	private OnFileUploadListener mListener;
	private OnFileUploadListenerEx mListenerEx;
	private int mStatus; // 当前状态
	private boolean mIsCancel; // 是否取消上传
	private JTFile mJTFile; // 下载的文件对象
	private int mProgress = 0; // 下载进度
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			EUtil.showToast(msg.obj + "");
		}
	};
	
	/**
	 * 构造函数
	 * @param listener
	 */
	public FileUploader(String localFilePath, OnFileUploadListener listener){
		
		// 准备就绪
		mStatus = Status.Prepared;
		if (mListener != null) {
			mListener.onPrepared();
		}
		// 判断文件大小和文件是否存在
		File file = new File(localFilePath);
		if (!file.exists() || !file.isFile()) {
			mStatus = Status.Error;
			if (mListener != null) {
				mListener.onError(ErrorCode.FileNotExist,
						ErrorMessage.FILE_NOT_EXIST);
			}
			return;
		}
		mStatus = Status.Prepared;
		mIsCancel = false;
		mListener = listener;
		mJTFile = new JTFile();
		// 初始化本地文件地址
		mJTFile.mLocalFilePath = localFilePath;
		// 文件名
		mJTFile.mFileName = localFilePath.substring(
				localFilePath.lastIndexOf(File.separator) + 1);
		// 文件大小
		File tempFile = new File(localFilePath);
		mJTFile.mFileSize = tempFile.length();
		// taskId
		mJTFile.mTaskId = TaskIDMaker.getTaskId(App.getUser().mUserName);
	}
	
	/**
	 * 构造函数
	 * @param listener
	 */
	public FileUploader(JTFile jtfile, OnFileUploadListener listener){
		
		// 准备就绪
		mStatus = Status.Prepared;
		if (mListener != null) {
			mListener.onPrepared();
		}
		// 初始化变量
		mIsCancel = false;
		mListener = listener;
		mJTFile = jtfile;
	}
	
	/**
	 * 构造函数
	 * @param listener
	 */
	public FileUploader(JTFile jtfile, OnFileUploadListenerEx listener){
		
		// 准备就绪
		mJTFile = jtfile;
		mStatus = Status.Prepared;
		if (mListenerEx != null) {
			mListenerEx.onPrepared(mJTFile.mCreateTime);
		}
		// 初始化变量
		mIsCancel = false;
		mListenerEx = listener;
	}
	
	/**
	 * 构造函数
	 * @param listener
	 */
	public FileUploader(OnFileUploadListener listener){
		
		mIsCancel = false;
		mListener = listener;
		mJTFile = new JTFile();
		mJTFile.mTaskId = TaskIDMaker.getTaskId(App.getUser().mUserName);
	}
	
	/**
	 * 开始下载
	 */
	public void start(){
		// 判断文件大小和文件是否存在
		File file = new File(mJTFile.mLocalFilePath);
		if (!file.exists()) {
			mStatus = Status.Error;
			if (mListener != null) {
				mListener.onError(ErrorCode.FileNotExist, ErrorMessage.FILE_NOT_EXIST);
			}
			if(mListenerEx != null){
				mListenerEx.onError(mJTFile.mCreateTime, ErrorCode.FileNotExist, ErrorMessage.FILE_NOT_EXIST,mJTFile.mLocalFilePath);
			}
			Message msg = new Message();
			msg.obj =  ErrorMessage.FILE_NOT_EXIST;
			mHandler.sendMessage(msg);
			return;
		}
		// 开始后台任务
		doInBackgroundEx();
		mStatus = Status.Started;
		if (mListener != null) {
			mListener.onStarted();
		}
		if(mListenerEx != null){
			mListenerEx.onStarted(mJTFile.mCreateTime);
		}
	}
	
	/**
	 * 开始下载
	 * @param localFilePath
	 */
	public void start(String localFilePath){
		WEB_URL = EAPIConsts.FILE_URL;
		// 准备就绪
		mStatus = Status.Prepared;
		if (mListener != null) {
			mListener.onPrepared();
		}
		// 判断文件是否存在
		File file = new File(localFilePath);
		if (!file.exists() || !file.isFile()) {
			mStatus = Status.Error;
			if (mListener != null) {
				mListener.onError(ErrorCode.FileNotExist,
						ErrorMessage.FILE_NOT_EXIST);
			}
			return;
		}
		// 文件是否超出大小
		/*
		if (file.length() > FILE_SIZE_LIMIT) {
			mStatus = Status.Error;
			if (mListener != null) {
				mListener.onError(ErrorCode.FileExceedLimit,
						ErrorMessage.FILE_SIZE_EXCEED_LIMIT);
				return;
			}
			return;
		}
		*/
		// 初始化本地文件地址
		mJTFile.mLocalFilePath = localFilePath;
		// 文件名
		mJTFile.mFileName = localFilePath.substring(localFilePath.lastIndexOf(File.separator)+1);
		// 文件大小
		File tempFile = new File(localFilePath);
		mJTFile.mFileSize = tempFile.length();
		// 开始后台任务
		doInBackgroundEx();
		if (mListener != null) {
			mListener.onStarted();
		}
		mStatus = Status.Started;
	}
	
	public void cancel(){
		mIsCancel = true;
		mStatus = Status.Canceled;
		if(mListener!=null){
			mListener.onCanceled();
		}
	}

	/**
	 * 设置上传参数
	 * @param taskId
	 * @param moduleType
	 * @param userId
	 */
	public void setParams(String taskId,int moduleType,String userId){
		mJTFile.mTaskId = taskId;
		mJTFile.mModuleType = moduleType;
		// mUserId = userId;
	}
	
	
	
	public void setmStatus(int mStatus) {
		this.mStatus = mStatus;
	}

	/**
	 * 获取上传状态
	 * @return
	 */
	public int getStatus(){
		return mStatus;
	}
	
	/**
	 * 获取上传进度
	 * @return
	 */
	public int getProgress(){
		return mProgress;
	}
	
	/**
	 * 获取下载到的文件信息
	 * @return
	 */
	public JTFile getJTFile(){
		return mJTFile;
	}
	
	/**
	 * 开始上传（后台进行）
	 */
	protected void doInBackground() {
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// 上传文件的代码
				String end = "\r\n"; 
				String twoHyphens = "--";
				String boundary ="******"; //  UUID.randomUUID().toString()，边界标识
				try {
					URL url = new URL(WEB_URL);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setDoInput(true); // 允许输入流
					conn.setDoOutput(true); // 允许输出流
					conn.setUseCaches(false); // 不使用缓存
					// 防止上传大文件时全部读入内存造成崩溃
					conn.setChunkedStreamingMode(1024);
					conn.setRequestMethod("POST"); // 请求方式 
					conn.setConnectTimeout(60 * 1000); // 60s超时
					conn.setRequestProperty("Connection", "Keep-Alive");
					conn.setRequestProperty("Charset", HTTP.UTF_8);
					conn.setRequestProperty("Content-Type",
							"multipart/form-data;boundary=" + boundary);
					
					DataOutputStream dos = new DataOutputStream(
							conn.getOutputStream());
					
					// 上传参数
					HashMap<String,String> params = new HashMap<String,String>();
					params.put("taskId", mJTFile.mTaskId);
					params.put("moduleType", mJTFile.mModuleType + "");
					if(mJTFile.mModuleType==JTFile.TYPE_DEMAND) //如果是上传的需求附件  就添加
						params.put("fileType", mJTFile.getmType()+"");
					params.put("uid", App.getUserID());
					
					for (Map.Entry<String, String> entry : params.entrySet()) {
						StringBuilder sb = new StringBuilder();
						sb.append(twoHyphens);   
			            sb.append(boundary); 
			            sb.append(end);
			            sb.append("Content-Disposition: form-data; name=\""+entry.getKey()+"\""+end);
			            sb.append(end);
			            sb.append(entry.getValue());
			            sb.append(end);
			            dos.write(sb.toString().getBytes());
					}					
					
					// 上传文件
					StringBuffer sb = new StringBuffer();   
		            sb.append(twoHyphens);   
		            sb.append(boundary); 
		            sb.append(end);

					// 这里重点注意：
					// name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
					// filename是文件的名字，包含后缀名的 比如:abc.png
					sb.append("Content-Disposition: form-data; name=\"file\"; filename=\""
							+ mJTFile.mLocalFilePath.substring(mJTFile.mLocalFilePath.lastIndexOf("/") + 1)
							+ "\""
							+ end);
					sb.append(end);
					dos.write(sb.toString().getBytes());
					
					// 开始写文件
					FileInputStream fis = new FileInputStream(mJTFile.mLocalFilePath);
					long total = fis.available();
					byte[] buffer = new byte[1024]; 
					int count = 0;
					int length = 0;
					while ((count = fis.read(buffer)) != -1) {
						// 写入数据
						dos.write(buffer, 0, count);
						// 获取进度，调用publishProgress()
						length += count;
						// 更新进度
						mProgress = (int) ((length * 1.0 / total) * 100);
						if(mListener!=null){
							mListener.onUpdate((int) ((length / (float) total) * 100));
						}
						// Task被取消了，马上退出循环
						if (mIsCancel) {
							fis.close();
							dos.close();
							if (mListener != null) {
								mListener.onCanceled();
							}
						}
					}
					fis.close();
					dos.writeBytes(end);
					dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
					dos.flush();
					dos.close();
					
		            // 获取响应码  200=成功  
		            // 当响应成功，获取响应的流  
					int res = conn.getResponseCode();
					String msg = conn.getResponseMessage();
					Log.d(TAG, msg);
					if(res == 200){
						InputStream is = conn.getInputStream();
						InputStreamReader isr = new InputStreamReader(is, HTTP.UTF_8); // 不行换小写
						BufferedReader br = new BufferedReader(isr);
						String responseStr = br.readLine();
						JSONObject jObject= new JSONObject(responseStr);
						String str_key = "responseData";
						if(jObject.has(str_key) && !jObject.isNull(str_key)){
							JSONObject subJObject = jObject.getJSONObject(str_key);
							str_key = "succeed";
							boolean result = subJObject.optBoolean(str_key);
							if (result) {
								str_key = "jtFile";
								if(subJObject.has(str_key) && !subJObject.isNull(str_key)){
									mJTFile.initWithJson(subJObject.getJSONObject(str_key));
									// 保存本地文件路径
									mJTFile.reserved1 = mJTFile.mLocalFilePath; 
									mStatus = Status.Success;
									if (mListener != null) {
										mListener.onSuccess(mJTFile.mUrl);
									}
									is.close();
									// Log.d(TAG,mJTFile.toString());
									// 添加到已上传文件列表
									// App.getApp().mAppData.getListUploadedJTFile().add(mJTFile);
									// App.getApp().mAppData.setListUploadedJTFile(App.getApp().mAppData.getListUploadedJTFile());
									return;
								}
							}
						}
						// 失败
						mStatus = Status.Error;
						if (mListener != null) {
							mListener.onError(ErrorCode.NetworkError,
									ErrorMessage.NETWORK_FAILED);
						}
						is.close();
					}
					else{
						mStatus = Status.Error;
						if (mListener != null) {
							mListener.onError(ErrorCode.NetworkError,
									ErrorMessage.NETWORK_FAILED);
						}
					}
				} 
				catch (Exception e) {
					mStatus = Status.Error;
					if (mListener != null) {
						mListener.onError(ErrorCode.NetworkError,
								ErrorMessage.NETWORK_FAILED);
					}
				}
			}
		}).start();
	}
	

	private void changeStatus(int status,int extraValue,String extraMsg){
		mStatus = status;
		if(mListener == null){
			return;
		}
		switch(mStatus){
		case Status.Prepared:
			break;
		case Status.Started:
			break;
		case Status.Success:
			break;
		case Status.Canceled:
			break;
		case Status.Error:
			break;
		}
	}
	
	protected void doInBackgroundEx() {
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String end = "\r\n"; 
				String twoHyphens = "--";
				String boundary ="******"; //  UUID.randomUUID().toString()，边界标识
								
				// 计算参数长度
				int paramsLen = 0;
				
				// 上传参数
				HashMap<String,String> params = new HashMap<String,String>();
				params.put("taskId", mJTFile.mTaskId);
				params.put("moduleType", mJTFile.mModuleType + "");
				if(mJTFile.mModuleType==JTFile.TYPE_DEMAND) //如果是上传的需求附件  就添加
				{
					params.put("fileType", mJTFile.getmType()+"");
				}
				if (mJTFile.mModuleType==-1) //如果上传会议附件就添加 会议默认-1
				{
					params.put("meetingId", mJTFile.reserved3);
				}
				params.put("uid", App.getUserID());
				
				for (Map.Entry<String, String> entry : params.entrySet()) {
					StringBuilder sb = new StringBuilder();
					sb.append(twoHyphens);   
		            sb.append(boundary); 
		            sb.append(end);
		            sb.append("Content-Disposition: form-data; name=\""+entry.getKey()+"\""+end);
		            sb.append(end);
		            sb.append(entry.getValue());
		            sb.append(end);
		            // 添加到总长度
					paramsLen += sb.toString().getBytes().length;
				}					
				
				// 上传文件
				StringBuffer sb = new StringBuffer();   
	            sb.append(twoHyphens);   
	            sb.append(boundary); 
	            sb.append(end);
				sb.append("Content-Disposition: form-data; name=\"file\"; filename=\""
						+ mJTFile.mLocalFilePath.substring(mJTFile.mLocalFilePath.lastIndexOf("/") + 1)
						+ "\""
						+ end);
				sb.append(end);
				// 添加到总长度
				paramsLen += sb.toString().getBytes().length;
				// 添加文件长度
				paramsLen += new File(mJTFile.mLocalFilePath).length();
				// 添加后缀长度
				paramsLen += (end + twoHyphens + boundary + twoHyphens + end).getBytes().length;
				
				try {
					
					URL url = new URL(WEB_URL);
					//URL url = new URL("http://192.168.120.181:8080/mobile/demand/upload");
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setDoInput(true); // 允许输入流
					conn.setDoOutput(true); // 允许输出流
					conn.setUseCaches(false); // 不使用缓存
					// 防止上传大文件时全部读入内存（获取Content-Length）造成崩溃
					conn.setFixedLengthStreamingMode(paramsLen);
					conn.setRequestMethod("POST"); // 请求方式 
					conn.setConnectTimeout(60 * 1000); // 60s超时
					conn.setRequestProperty("Connection", "Keep-Alive");
					conn.setRequestProperty("Charset", HTTP.UTF_8);
					conn.setRequestProperty("Content-Type","multipart/form-data;boundary=" + boundary);
					
					DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
					
					// 上传参数
					HashMap<String,String> paramsEx = new HashMap<String,String>();
					paramsEx.put("taskId", mJTFile.mTaskId);
					paramsEx.put("moduleType", mJTFile.mModuleType + "");
					paramsEx.put("uid", App.getUserID());
					
					for (Map.Entry<String, String> entry : params.entrySet()) {
						StringBuilder sbEx = new StringBuilder();
						sbEx.append(twoHyphens);   
						sbEx.append(boundary); 
						sbEx.append(end);
						sbEx.append("Content-Disposition: form-data; name=\""+entry.getKey()+"\""+end);
						sbEx.append(end);
						sbEx.append(entry.getValue());
						sbEx.append(end);
			            dos.write(sbEx.toString().getBytes());
					}					
					
					// 上传文件
					StringBuffer sbEx = new StringBuffer();   
					sbEx.append(twoHyphens);   
					sbEx.append(boundary); 
					sbEx.append(end);
					sbEx.append("Content-Disposition: form-data; name=\"file\"; filename=\""
							+ mJTFile.mLocalFilePath.substring(mJTFile.mLocalFilePath.lastIndexOf("/") + 1)
							+ "\""
							+ end);
					sbEx.append(end);
					dos.write(sbEx.toString().getBytes());
					// 开始写入文件
					FileInputStream fis = new FileInputStream(mJTFile.mLocalFilePath);			        
					long total = fis.available();
					byte[] buffer = new byte[1024]; 
					int count = 0;
					int length = 0;
					while ((count = fis.read(buffer)) != -1) {
						
						// 写入数据
						dos.write(buffer, 0, count);
						// 获取进度
						length += count;
						// 更新进度
						mProgress = (int) ((length * 1.0 / total) * 100);
						if (mListener != null) {
							mListener.onUpdate(mProgress);
						}
						// Task被取消了，退出循环
						if (mIsCancel) {
							fis.close();
							dos.close();
							if (mListener != null) {
								mListener.onCanceled();
							}
							if(mListenerEx != null){
								mListenerEx.onCanceled(mJTFile.mCreateTime);
							}
							break;
						}
					}
					if(mIsCancel){
						fis.close();
						dos.close();
						return;
					}
					fis.close();
					dos.writeBytes(end);
					dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
					dos.flush();
					dos.close();
					
		            // 获取响应码  200=成功  
					int res = conn.getResponseCode();
					if(res == 200){
						InputStream is = conn.getInputStream();
						InputStreamReader isr = new InputStreamReader(is, HTTP.UTF_8); // 不行换小写
						BufferedReader br = new BufferedReader(isr);
						String responseStr = br.readLine();
						JSONObject jObject= new JSONObject(responseStr);
						String str_key = "responseData";
						if(jObject.has(str_key) && !jObject.isNull(str_key)){
							JSONObject subJObject = jObject.getJSONObject(str_key);
							str_key = "succeed";
							boolean result = subJObject.optBoolean(str_key);
							if (result) {
								str_key = "jtFile";
								if(subJObject.has(str_key) && !subJObject.isNull(str_key)){
									mJTFile.initWithJson(subJObject.getJSONObject(str_key));
									// 用 reserved1 字段来保存本地路径
									mJTFile.reserved1 = mJTFile.mLocalFilePath; 
									// 通知发送成功
									mStatus = Status.Success;
									if (mListener != null) {
										mListener.onSuccess(mJTFile.mUrl);
									}
									if(mListenerEx != null){
										mListenerEx.onSuccessForJTFile(mJTFile);
										mListenerEx.onSuccess(mJTFile.mCreateTime, mJTFile.mUrl);
									}
									is.close();;
									return;
								}
							}
							else{ // 上传失败
								mStatus = Status.Error;
								if (mListener != null) {
									mListener.onError(ErrorCode.UnknownError, ErrorMessage.UNKNOWN_ERROR);
								}
								if(mListenerEx != null){
									mListenerEx.onError(mJTFile.mCreateTime, ErrorCode.UnknownError, ErrorMessage.UNKNOWN_ERROR,mJTFile.mLocalFilePath);
								}
							}
						}
					}
					// 上传失败
					mStatus = Status.Error;
					if (mListener != null) {
						mListener.onError(ErrorCode.NetworkError,ErrorMessage.NETWORK_FAILED);
					}
					if(mListenerEx != null){
						mListenerEx.onError(mJTFile.mCreateTime, ErrorCode.NetworkError,ErrorMessage.NETWORK_FAILED,mJTFile.mLocalFilePath);
					}
				} 
				catch (Exception e) {
					mStatus = Status.Error;
					if (mListener != null) {
						mListener.onError(ErrorCode.NetworkError, ErrorMessage.NETWORK_FAILED);
					}
					if(mListenerEx != null){
						mListenerEx.onError(mJTFile.mCreateTime, ErrorCode.NetworkError,ErrorMessage.NETWORK_FAILED,mJTFile.mLocalFilePath);
					}
				}
			}
		}).start();
	}
	
	/**
	 * 文件上传监听器
	 * @author leon
	 */
	public interface OnFileUploadListener {
		public void onPrepared(); // 准备就绪
		public void onStarted(); // 开始任务
		public void onUpdate(int value); // 进度更新
		public void onCanceled(); // 取消上传
		public void onSuccess(String fileUrl); // 上传成功
		public void onError(int code, String message); // 上传失败
	}
	
	/**
	 * 文件上传监听器
	 * @author leon
	 */
	public interface OnFileUploadListenerEx {
		public void onPrepared(long uid); // 准备就绪
		public void onStarted(long uid); // 开始任务
		public void onUpdate(long uid, int progress); // 进度更新
		public void onCanceled(long uid); // 取消上传
		public void onSuccess(long uid, String fileUrl); // 上传成功
		public void onSuccessForJTFile(JTFile jtFile); // 上传成功后获取JTFile对象
		public void onError(long uid, int code, String message,String namePath); // 上传失败
	}
}
