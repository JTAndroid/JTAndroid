package com.utils.common;

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

import android.content.Context;

import com.tr.App;
import com.tr.model.obj.JTFile;
import com.utils.http.EAPIConsts;

public class FileUploaderEx {

private final String TAG = getClass().getSimpleName();
	
	// 常量
	private final String WEB_URL = EAPIConsts.FILE_URL; // 服务器地址
	// private final int FILE_SIZE_LIMIT = 35 * 1024 * 1024; // 最大35M
	
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
		public static final int ErrorCodeBase = 10;
		public static final int NetworkError = ErrorCodeBase + 1;
		public static final int FileNotExist = ErrorCodeBase + 2;
		public static final int FileExceedLimit = ErrorCodeBase + 3;
	}
	
	// 错误消息提示
	public static class ErrorMessage{
		public static final String NETWORK_FAILED = "网络错误，上传失败，请重试";
		public static final String FILE_NOT_EXIST = "文件不存在";
	}
	
	// 变量
	private OnFileUploadListener mListener; // 上传监听器
	private int mStatus; // 当前状态
	private boolean mIsCancel; // 是否取消上传
	private JTFile mJTFile; // 上传的文件对象
	private Context mContext; // 上下文
	
	/**
	 * 
	 * @param context
	 * @param jtfile
	 * @param listener
	 */
	public FileUploaderEx(Context context,JTFile jtfile, OnFileUploadListener listener){

		// 初始化变量
		mIsCancel = false;
		mListener = listener;
		mJTFile = jtfile;
		mContext = context;
		// 文件是否存在
		if(jtfile == null || jtfile.mLocalFilePath == null){
			mStatus = Status.Error;
			if (mListener != null) {
				mListener.onError(ErrorCode.FileNotExist,ErrorMessage.FILE_NOT_EXIST);				
				EUtil.showToast(ErrorMessage.FILE_NOT_EXIST);
			}
			return;
		}
		File file = new File(jtfile.mLocalFilePath);
		if (!file.exists() || !file.isFile()) {
			mStatus = Status.Error;
			if (mListener != null) {
				mListener.onError(ErrorCode.FileNotExist,ErrorMessage.FILE_NOT_EXIST);
				EUtil.showToast(ErrorMessage.FILE_NOT_EXIST);
			}
			return;
		}
		// 设置上传状态
		mStatus = Status.Prepared;
		if(mListener != null){
			mListener.onPrepared();
		}
	}
	
	/**
	 * 开始上传
	 */
	public void start(){
		
		// 文件是否存在
		if(mJTFile == null){
			mStatus = Status.Error;
			if (mListener != null) {
				mListener.onError(ErrorCode.FileNotExist,ErrorMessage.FILE_NOT_EXIST);
				EUtil.showToast(ErrorMessage.FILE_NOT_EXIST);
			}
			return;
		}
		// 开始后台任务
		doInBackground();
		mStatus = Status.Started;
		if(mListener!=null){
			mListener.onStarted();
		}
	}
	
	/**
	 * 取消上传
	 */
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
	public void setParams(String taskId, int moduleType) {
		mJTFile.mTaskId = taskId;
		mJTFile.mModuleType = moduleType;
	}
	
	/**
	 * 获取当前下载状态
	 * @return
	 */
	public int getStatus(){
		return mStatus;
	}
	
	/**
	 * 获取上传的文件信息
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
					// conn.setChunkedStreamingMode(1024);
					conn.setRequestMethod("POST"); // 请求方式 
					conn.setConnectTimeout(10 * 1000); // 10s超时
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
					// 获取文件大小
					FileInputStream fis = new FileInputStream(mJTFile.mLocalFilePath);
					/*
					bytesAvailable = fileInputStream.available();
			        bufferSize = Math.min(bytesAvailable, maxBufferSize);
			        buffer = new byte[bufferSize];
			        */
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
									mStatus = Status.Success;
									if (mListener != null) {
										mListener.onSuccess(mJTFile);
									}
									is.close();
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
	
	/**
	 * 文件上传监听器
	 * @author leon
	 */
	public interface OnFileUploadListener {
		public void onPrepared(); // 准备就绪
		public void onStarted(); // 开始任务
		public void onUpdate(int value); // 进度更新
		public void onCanceled(); // 取消上传
		public void onSuccess(JTFile jtfile); // 上传成功
		public void onError(int code, String message); // 上传失败
	}
}
