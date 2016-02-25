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
import android.text.TextUtils;
import android.util.Log;

import com.tr.App;
import com.tr.model.obj.JTFile;
import com.utils.http.EAPIConsts;

/**
 * 会议畅聊文件上传
 * @author leon
 */
public class HyChatFileUploader {

	// 常量
	private final String TAG = getClass().getSimpleName();
	private final String WebUrl = EAPIConsts.FILE_URL; // 文件上传地址

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
	public static class ErrorCode {
		public static final int ErrorCodeBase = 10;
		public static final int NetworkError = ErrorCodeBase + 1;
		public static final int FileNotExist = ErrorCodeBase + 2;
		public static final int FileExceedLimit = ErrorCodeBase + 3;
	}

	// 错误消息提示
	public static class ErrorMessage {
		public static final String NetworkError = "上传失败，请重试";
		public static final String FileNotExist = "文件不存在";
	}

	// 变量
	private OnFileUploadListener listener; // 上传监听器
	private int status; // 当前状态
	private boolean canceled; // 是否取消上传
	private JTFile jtFile; // 上传的文件对象
	private Context context; // 上下文
	private boolean toastMsg; // 是否弹出消息
	private int progress; // 上传进度
	private String messageId; // 绑定的消息id
	
	public String getMessageId() {
		return messageId;
	}

	/**
	 * 
	 * @param context
	 * @param jtfile
	 * @param listener
	 */
	public HyChatFileUploader(Context context, JTFile jtFile, String messageId) {

		// 初始化变量
		canceled = false;
		progress = 0;
		this.toastMsg = false;
		this.jtFile = jtFile;
		this.messageId = messageId;
		this.context = context;
		// 文件是否存在
		if (TextUtils.isEmpty(jtFile.mLocalFilePath)
				|| ! new File(jtFile.mLocalFilePath).exists()) {
			status = Status.Error;
			if(toastMsg){
				EUtil.showToast(this.context, ErrorMessage.FileNotExist);
			}
			return;
		}
		else{
			status = Status.Prepared; // 设置上传状态
		}
	}
	
	/**
	 * 设置监听器
	 * @param listener
	 */
	public void setOnFileUploadListener(OnFileUploadListener listener){
		this.listener = listener;
	}
	
	/**
	 * 是否显示消息
	 * @param enable
	 */
	public void enableToastMessage(boolean toastMsg){
		this.toastMsg = toastMsg;
	}

	/**
	 * 开始上传
	 */
	public void start() {

		if (jtFile == null) { // 文件不存在
			changeStatus(Status.Error, ErrorCode.FileNotExist, ErrorMessage.FileNotExist);
		}
		else{
			doInBackground(); // 开始后台任务
			changeStatus(Status.Started);
		}
	}

	/**
	 * 取消上传
	 */
	public void cancel() {
		canceled = true;
		status = Status.Canceled;
		if (listener != null) {
			listener.onCanceled(jtFile.getId());
		}
	}

	/**
	 * 获取当前状态
	 * @return
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * 获取文件信息
	 * @return
	 */
	public JTFile getJTFile() {
		return jtFile;
	}
	
	/**
	 * 获取下载进度
	 * @return
	 */
	public int getProgress(){
		return progress;
	}
	
	// 更改上传状态
	private void changeStatus(int status, Object... args) {
		this.status = status;
		switch (status) {
		case Status.Prepared:
			if (listener != null) {
				listener.onPrepared(messageId);
			}
			break;
		case Status.Started:
			if (listener != null) {
				if (args.length < 1) {
					listener.onStarted(messageId);
				} 
				else {
					listener.onUpdate(messageId, (Integer) args[0]);
				}
			}
			break;
		case Status.Success:
			if (listener != null) {
				listener.onSuccess(messageId, jtFile);
			}
			break;
		case Status.Error:
			if (listener != null) {
				listener.onError(messageId, (Integer) args[0], (String) args[1]);
			}
			break;
		case Status.Canceled:
			if (listener != null) {
				listener.onCanceled(messageId);
			}
			break;
		}
	}

	/**
	 * 开始上传任务
	 */
	protected void doInBackground() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				String end = "\r\n";
				String twoHyphens = "--";
				String boundary = "******"; // UUID.randomUUID().toString()，边界标识
				try {
					URL url = new URL(WebUrl);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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
					StringBuilder strBuilder = new StringBuilder(); // 使用StringBuilder来提高性能
					DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
					// 上传参数
					HashMap<String, String> params = new HashMap<String, String>();
					params.put("taskId", jtFile.mTaskId);
					params.put("moduleType", "1");
					params.put("uid", App.getUserID());
					for (Map.Entry<String, String> entry : params.entrySet()) {
						StringBuilder builder = new StringBuilder();
						builder.append(twoHyphens);
						builder.append(boundary);
						builder.append(end);
						builder.append("Content-Disposition: form-data; name=\""
								+ entry.getKey() + "\"" + end);
						builder.append(end);
						builder.append(entry.getValue());
						builder.append(end);
						dos.write(builder.toString().getBytes());
					}

					// 文件流
					strBuilder.append(twoHyphens);
					strBuilder.append(boundary);
					strBuilder.append(end);

					// 这里重点注意：
					// name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
					// filename是文件的名字，包含后缀名的 比如:abc.png
					strBuilder.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + jtFile.mFileName
							+ "\"" + end);
					strBuilder.append(end);
					dos.write(strBuilder.toString().getBytes());
					// 获取文件大小
					FileInputStream fis = new FileInputStream(jtFile.mLocalFilePath);
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
						progress = (int) ((length * 1.0 / total) * 100);
						changeStatus(Status.Started, progress);
						// 上传任务被取消了，马上退出循环
						if (canceled) {
							fis.close();
							dos.close();
							// 更新状态
							changeStatus(Status.Canceled);
						}
					}
					fis.close();
					dos.writeBytes(end);
					dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
					dos.flush();
					dos.close();

					// 获取响应码 200=成功
					// 当响应成功，获取响应的流
					int res = conn.getResponseCode();
					if (res == 200) {
						InputStream is = conn.getInputStream();
						InputStreamReader isr = new InputStreamReader(is, HTTP.UTF_8);
						BufferedReader br = new BufferedReader(isr);
						String responseStr = br.readLine();
						is.close();
						JSONObject jObject = new JSONObject(responseStr);
						String strKey = "responseData";
						if (jObject.has(strKey) && !jObject.isNull(strKey)) {
							JSONObject subJObject = jObject.getJSONObject(strKey);
							strKey = "succeed";
							boolean result = subJObject.optBoolean(strKey);
							if (result) {
								strKey = "jtFile";
								if (subJObject.has(strKey) && !subJObject.isNull(strKey)) {
									jtFile.initWithJson(subJObject.getJSONObject(strKey));
									changeStatus(Status.Success);
									return;
								}
							}
						}
					} 
					changeStatus(Status.Error, ErrorCode.NetworkError, ErrorMessage.NetworkError);
				} 
				catch (Exception e) {
					Log.d(TAG, e.getMessage());
					changeStatus(Status.Error, ErrorCode.NetworkError, e.getMessage());
				}
			}
		}).start();
	}

	/**
	 * 文件上传监听器
	 * @author leon
	 */
	public interface OnFileUploadListener {
		public void onPrepared(String messageId); // 准备就绪
		public void onStarted(String messageId); // 开始任务
		public void onUpdate(String messageId, int value); // 进度更新
		public void onCanceled(String messageId); // 取消上传
		public void onSuccess(String messageId, JTFile jtFile); // 上传成功
		public void onError(String messageId, int errCode, String errMsg); // 上传失败
	}
}
