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

import android.util.Log;

import com.tr.App;
import com.utils.common.FileUploader.OnFileUploadListener;
import com.utils.common.FileUploader.Status;
import com.utils.http.EAPIConsts;

public class CardUploader {

	private final String TAG = getClass().getSimpleName();
	
	// 常量
	private final String WEB_URL = EAPIConsts.CARD_URL; // 服务器地址
	private final int FILE_SIZE_LIMIT = 10 * 1024 * 1024; // 最大10M
	
	// 变量
	private OnFileUploadListener mListener;
	private int mStatus; // 当前状态
	private boolean mIsCancel; // 是否取消上传
	private String mLocalFilePath;
	private int mType; // 文件标识
	
	
	/**
	 * 构造函数（1机构组织代码图片、2营业执照图片、3企业法人身份证、4 机构联系人身份证）
	 * @param listener
	 */
	public CardUploader(int type, OnFileUploadListener listener) {

		mIsCancel = false;
		mListener = listener;
		mLocalFilePath = "";
		mType = type;
	}
	
	/**
	 * 开始下载
	 * @param localFilePath
	 */
	public void start(String localFilePath){
		
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
				mListener.onError(FileUploader.ErrorCode.FileNotExist,
						FileUploader.ErrorMessage.FILE_NOT_EXIST);
			}
			return;
		}
		// 文件是否超出大小
		/*
		if (file.length() > FILE_SIZE_LIMIT) {
			mStatus = Status.Error;
			if (mListener != null) {
				mListener.onError(FileUploader.ErrorCode.FileExceedLimit,
						FileUploader.ErrorMessage.FILE_SIZE_EXCEED_LIMIT);
				return;
			}
			return;
		}
		*/
		// 初始化本地文件地址
		mLocalFilePath = localFilePath;
		// 开始后台任务
		doInBackground();
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
	 * 获取当前下载状态
	 * @return
	 */
	public int getStatus(){
		return mStatus;
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
					conn.setRequestMethod("POST"); // 请求方式 
					conn.setConnectTimeout(10 * 1000); // 10s超时
					conn.setRequestProperty("Connection", "Keep-Alive");
					conn.setRequestProperty("Charset", HTTP.UTF_8);
					conn.setRequestProperty("Content-Type",
							"multipart/form-data;boundary=" + boundary);
					
					DataOutputStream dos = new DataOutputStream(
							conn.getOutputStream());
					
					// 上传参数
					HashMap<String, String> params = new HashMap<String, String>();
					params.put("uid", App.getUserID()); // 用户id
					params.put("type ", mType + ""); // 图片类型
					
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
					
					Log.d(TAG, "request:" + new JSONObject(params).toString() +"\n");
					
					// 上传文件
					StringBuffer sb = new StringBuffer();   
		            sb.append(twoHyphens);   
		            sb.append(boundary); 
		            sb.append(end);

					// name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
		            sb.append("Content-Disposition: form-data; name=\"file\"; filename=\""
							+ mLocalFilePath.substring(mLocalFilePath.lastIndexOf("/") + 1)
							+ "\""
							+ end);
					sb.append(end);
					dos.write(sb.toString().getBytes());
					// 获取文件总大小
					FileInputStream fis = new FileInputStream(mLocalFilePath);
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
						Log.d(TAG, "response:" + responseStr + "\n");
						JSONObject jObject= new JSONObject(responseStr);
						String str_key = "responseData";
						if(jObject.has(str_key) && !jObject.isNull(str_key)){
							JSONObject subJObject = jObject.getJSONObject(str_key);
							str_key = "succeed";
							boolean result = subJObject.optBoolean(str_key);
							String relativelyUrl = subJObject.optString("relativelyUrl");
							if(result){
								mStatus = Status.Success;
								if (mListener != null) {
									mListener.onSuccess(relativelyUrl);
								}
								is.close();
								return;
							}
						}
						mStatus = Status.Error;
						if (mListener != null) {
							mListener.onError(FileUploader.ErrorCode.NetworkError,
									FileUploader.ErrorMessage.NETWORK_FAILED);
						}
						is.close();
					}
					else{
						mStatus = Status.Error;
						if (mListener != null) {
							mListener.onError(FileUploader.ErrorCode.NetworkError,
									FileUploader.ErrorMessage.NETWORK_FAILED);
						}
						Log.d(TAG, "response:errcode " + res + "\n");
					}
				} 
				catch (Exception e) {
					mStatus = Status.Error;
					if (mListener != null) {
						mListener.onError(FileUploader.ErrorCode.NetworkError,
								FileUploader.ErrorMessage.NETWORK_FAILED);
					}
				}
			}
		}).start();
	}
}
