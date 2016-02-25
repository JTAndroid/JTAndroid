package com.utils.common;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.tr.model.obj.JTFile;
import com.utils.http.EAPIConsts;

/**
 * 组织图片上传类
 * 
 * @author leon
 */
public class OrganizationPictureUploader {

	// TAG
	private final static String TAG = OrganizationPictureUploader.class
			.getSimpleName();
	// 后台上传地址
	private final String WEB_URL = EAPIConsts.PIC_ORG_URL;
	// 线程池对象
	private Executor mExecutor;
	// 回调监听函数
	private OnOrganizationPictureUploadListener mUploadListener;
	// 消息处理器
	private Handler mHandler;

	// 上传状态
	public static class Status {
		private static final int StatusBase = 0;
		public static final int Prepared = StatusBase + 1; // 准备就绪
		public static final int Started = StatusBase + 2; // 上传中
		public static final int Success = StatusBase + 3; // 上传成功
		public static final int Failed = StatusBase + 4; // 上传失败
		public static final int Canceled = StatusBase + 5; // 取消上传
	}

	// 错误码状态
	public static class ErrorCode {
		public static final int ErrorCodeBase = 0;
		public static final int NetworkError = ErrorCodeBase + 1;
		public static final int FileNotExistError = ErrorCodeBase + 2;
		public static final int UploadParamsError = ErrorCodeBase + 3;
		public static final int FileExceedLimit = ErrorCodeBase + 4;
		public static final int ParamsError = ErrorCodeBase + 5;
	}

	// 错误消息
	public static class ErrorMessage {
		public static final String NETWORK_FAILED = "网络错误，上传失败，请重试";
		public static final String FILE_NOT_EXIST = "文件不存在";
		public static final String PARAMS_ERROR = "参数错误";
		public static final String FILE_EXCEED_LIMIT = "文件大小超出限制";
		public static final String UPLOAD_PARAMS_ERROR = "上传参数错误";
	}

	/**
	 * 构造函数
	 */
	public OrganizationPictureUploader() {
		doInit();
	}

	/**
	 * 构造函数
	 * 
	 * @param listener
	 */
	public OrganizationPictureUploader(
			OnOrganizationPictureUploadListener listener) {
		doInit();
		mUploadListener = listener;
	}

	/**
	 * 进行一些初始化的工作
	 */
	private void doInit() {
		mExecutor = Executors.newCachedThreadPool();
		mHandler = new Handler(Looper.getMainLooper());
	}

	/**
	 * 开始上传任务
	 * 
	 * @param jtFile
	 *            ，有三个参数是必须的：id(保证唯一且不为空)、mLocalPath（本地路径，不为空）、type：1营业执照图片、2身份证
	 *            、3名片图片 、4头像
	 * @descirption 必须保证参数齐全否则会报错
	 */
	public void startNewUploadTask(JTFile jtFile) {
		mExecutor.execute(new UploadTask(jtFile, mUploadListener));
	}

	/**
	 * 设置上传回调函数
	 * 
	 * @param listener
	 */
	public void setOnOrganizationPictureUploadListener(
			OnOrganizationPictureUploadListener listener) {
		mUploadListener = listener;
	}

	/**
	 * 上传回调
	 * 
	 * @param args
	 */
	/*
	 * private void UploadCallBack(final Object...args){ mHandler.post(new
	 * Runnable() {
	 * 
	 * @Override public void run() { if(args.length > 1){ int } } }); }
	 */

	/**
	 * 文件上传任务
	 * 
	 * @author leon
	 */
	private class UploadTask implements Runnable {

		private JTFile mJtFile;
		private OnOrganizationPictureUploadListener mListener;
		private int mStatus;
		private boolean mCanceled;

		/**
		 * 构造函数
		 * 
		 * @param jtFile
		 */
		@SuppressWarnings("unused")
		public UploadTask(JTFile jtFile) {
			doInit(jtFile);
		}

		/**
		 * 构造函数
		 * 
		 * @param jtFile
		 * @param listener
		 */
		public UploadTask(JTFile jtFile,
				OnOrganizationPictureUploadListener listener) {
			doInit(jtFile);
			mListener = listener;
		}

		/**
		 * 做一些初始化工作
		 */
		private void doInit(JTFile jtFile) {

			// 对JTFile对象的必填参数做一些判断
			Message msg = new Message();
			if (jtFile == null || TextUtils.isEmpty(jtFile.mLocalFilePath)
					|| TextUtils.isEmpty(jtFile.getId()) || jtFile.mType < 1
					|| jtFile.mType > 4) {
				mStatus = Status.Failed;
				msg.arg1 = ErrorCode.ParamsError;
				msg.obj = ErrorMessage.PARAMS_ERROR;
				// mHandler.sendMessage(msg);
				if (mListener != null) {
					mListener.onError(jtFile == null ? "" : jtFile.getId(),
							ErrorCode.ParamsError, ErrorMessage.PARAMS_ERROR);
				}
				return;
			}
			mJtFile = jtFile;
		}

		/**
		 * 设置上传监听器
		 * 
		 * @param listener
		 */
		@SuppressWarnings("unused")
		public void setOnOrganizationPictureUploadListener(
				OnOrganizationPictureUploadListener listener) {
			mListener = listener;
		}

		/**
		 * 获取上传状态
		 * 
		 * @return
		 */
		@SuppressWarnings("unused")
		public int getStatus() {
			return mStatus;
		}

		/**
		 * 取消上传
		 */
		@SuppressWarnings("unused")
		public void cancel() {
			mCanceled = true;
			mStatus = Status.Canceled;
			if (mListener != null) {
				mListener.onCanceled(mJtFile.getId());
			}
		}

		@Override
		public void run() {
			String MSG = "run()";

			String end = "\r\n";
			String twoHyphens = "--";
			String boundary = "******"; // UUID.randomUUID().toString()，边界标识
			try {
				URL url = new URL(WEB_URL);
//				Log.i(TAG, MSG + " url = " + url );
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
				Map<String, String> params = new HashMap<String, String>();
				// params.put("taskId", "");
				params.put("type", mJtFile.mType + "");
				// params.put("uid", "");
				StringBuilder paramsStringBuilder = new StringBuilder();
				for (Map.Entry<String, String> entry : params.entrySet()) {
					paramsStringBuilder.append(twoHyphens);
					paramsStringBuilder.append(boundary);
					paramsStringBuilder.append(end);
					paramsStringBuilder
							.append("Content-Disposition: form-data; name=\""
									+ entry.getKey() + "\"" + end);
					paramsStringBuilder.append(end);
					paramsStringBuilder.append(entry.getValue());
					paramsStringBuilder.append(end);
				}
				dos.write(paramsStringBuilder.toString().getBytes());

				// 上传文件数据
				StringBuffer pictureStringBuilder = new StringBuffer();
				pictureStringBuilder.append(twoHyphens);
				pictureStringBuilder.append(boundary);
				pictureStringBuilder.append(end);

				// 这里重点注意：
				// name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
				// filename是文件的名字，包含后缀名的 比如:abc.png
				pictureStringBuilder.append("Content-Disposition: form-data; name=\"file\"; filename=\""+ mJtFile.mLocalFilePath.substring(mJtFile.mLocalFilePath.lastIndexOf("/") + 1)+ "\""+ end);
				pictureStringBuilder.append(end);
				dos.write(pictureStringBuilder.toString().getBytes());

				// 获取文件大小
				FileInputStream fis = new FileInputStream(mJtFile.mLocalFilePath);
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
					if (mUploadListener != null) {
						mUploadListener.onUpdate(mJtFile.getId(),
								(int) ((length / (float) total) * 100));
					}
					// Task被取消了，马上退出循环
					if (mCanceled) {
						if (mUploadListener != null) {
							mUploadListener.onCanceled(mJtFile.getId());
						}
						fis.close();
						dos.close();
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
					InputStreamReader isr = new InputStreamReader(is,HTTP.UTF_8); // 不行换小写
					BufferedReader br = new BufferedReader(isr);
					String responseStr = br.readLine();
					JSONObject jObject = new JSONObject(responseStr);
					String strKey = "responseData";
					if (jObject.has(strKey) && !jObject.isNull(strKey)) {
						JSONObject subJObject = jObject.getJSONObject(strKey);
						boolean successed = subJObject.optBoolean("success");
						if (successed) {
							String url1 = subJObject.optString("url");
							String urlToSql = subJObject.optString("urlToSql");
							Map<String, String> result = new HashMap<String, String>();
							result.put("url", url1);
							result.put("urlToSql", urlToSql);
							mStatus = Status.Success;
							if (mListener != null) {
								mListener.onSuccess(mJtFile.getId(), result);
							}
							is.close();
							return;
						}
					}
					// 上传失败
					mStatus = Status.Failed;
					if (mListener != null) {
						mListener.onError(mJtFile.getId(),
								ErrorCode.NetworkError,
								ErrorMessage.NETWORK_FAILED);
					}
					is.close();
				} else {
					mStatus = Status.Failed;
					if (mListener != null) {
						mListener.onError(mJtFile.getId(),
								ErrorCode.NetworkError,
								ErrorMessage.NETWORK_FAILED);
					}
				}
			} catch (Exception e) {
				mStatus = Status.Failed;
				if (mListener != null) {
					mListener.onError(mJtFile == null ? "" : mJtFile.getId(), ErrorCode.NetworkError,
							ErrorMessage.NETWORK_FAILED);
				}
			}
		}
	}

	/**
	 * 文件上传监听器
	 * 
	 * @author leon
	 */
	public static interface OnOrganizationPictureUploadListener {

		/**
		 * 准备就绪
		 * 
		 * @param id
		 */
		public void onPrepared(String id);

		/**
		 * 开始任务
		 * 
		 * @param id
		 */
		public void onStarted(String id);

		/**
		 * 更新上传进度
		 * 
		 * @param value
		 */
		public void onUpdate(String id, int progress);

		/**
		 * 取消上传
		 */
		public void onCanceled(String id);

		/**
		 * 上传成功
		 * 
		 * @param id
		 * @param result
		 */
		public void onSuccess(String id, Map<String, String> result);

		/**
		 * 上传错误
		 * 
		 * @param code
		 * @param message
		 */
		public void onError(String id, int code, String message);
	}
}
