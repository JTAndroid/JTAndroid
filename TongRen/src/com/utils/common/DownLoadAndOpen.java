package com.utils.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

public class DownLoadAndOpen extends AsyncTask<String, Void, File> {
	private String fileUrl;
	private String fileType;
	private Context context;

	public DownLoadAndOpen(Context context,String fileUrl, String fileType) {
		this.fileUrl = fileUrl;
		this.fileType = fileType;
		this.context = context;
	}

	@Override
	protected File doInBackground(String... paramsArr) {
//		if (null != downLoadAndOpen) {
//			downLoadAndOpen.cancel(false);
//		}
//		downLoadAndOpen = this;
		if (this!=null) {
			this.cancel(false);
		}

		File fileCache = null;
		File uploadFileDir = new File(
				Environment.getExternalStorageDirectory(), "/jt/fileCache");
		if (!uploadFileDir.exists()) {
			uploadFileDir.mkdirs();
		}
		String fileName = UUID.randomUUID().toString();
		try {
			fileCache = new File(uploadFileDir, fileName);
			if (!fileCache.exists()) {
				fileCache.createNewFile();
			}

			URL url = new URL(fileUrl);
			HttpURLConnection conn = (HttpURLConnection) url
					.openConnection();
			conn.setRequestProperty("Accept-Encoding", "identity");
			conn.connect();

			if (conn.getResponseCode() == 200) {
				// 创建输入流
				InputStream inputStream = conn.getInputStream();

				OutputStream outputStream = new FileOutputStream(fileCache);
				byte[] data = new byte[2048];
				int length = 0;
				while ((length = inputStream.read(data)) != -1) {
					outputStream.write(data, 0, length);
				}
				inputStream.close();
				outputStream.close();
			} else {
				fileCache = null;
			}

		} catch (Exception e) {
			fileCache = null;
		}
		return fileCache;
	}

	@Override
	protected void onPostExecute(File openFile) {
		if (!isCancelled()) {
			if (null == openFile) {
				Toast.makeText(context, "附件下载失败", Toast.LENGTH_SHORT).show();
				return;
			}
			Intent intent = null;
			String temp = fileType.replaceAll(
					"^([\\s\\S]*)([tT][xX][tT])$", "$2");
			if (fileType.replaceAll("^([\\s\\S]*)([tT][xX][tT]) *$", "$2")
					.length() == 3) {
				intent = new Intent("android.intent.action.VIEW");
				intent.addCategory("android.intent.category.DEFAULT");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Uri uri2 = Uri.fromFile(openFile);
				intent.setDataAndType(uri2, "text/plain");
			} else if (fileType.replaceAll("^([\\s\\S]*)([pP][dD][fF]) *$",
					"$2").length() == 3) {
				intent = new Intent("android.intent.action.VIEW");
				intent.addCategory("android.intent.category.DEFAULT");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Uri uri = Uri.fromFile(openFile);
				intent.setDataAndType(uri, "application/vnd.ms-powerpoint");

			} else if (fileType.replaceAll("^([\\s\\S]*)([dD][oO][cC]) *$",
					"$2").length() == 3) {
				intent = new Intent("android.intent.action.VIEW");
				intent.addCategory("android.intent.category.DEFAULT");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Uri uri = Uri.fromFile(openFile);
				intent.setDataAndType(uri, "application/msword");

			} else if (fileType.replaceAll(
					"^([\\s\\S]*)([dD][oO][cD][xX]) *$", "$2").length() == 4) {
				intent = new Intent("android.intent.action.VIEW");
				intent.addCategory("android.intent.category.DEFAULT");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Uri uri = Uri.fromFile(openFile);
				intent.setDataAndType(uri, "application/msword");

			} else if (fileType.replaceAll("^([\\s\\S]*)([xX][lL][sS]) *$",
					"$2").length() == 3) {
				intent = new Intent("android.intent.action.VIEW");
				intent.addCategory("android.intent.category.DEFAULT");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Uri uri = Uri.fromFile(openFile);
				intent.setDataAndType(uri, "application/vnd.ms-excel");

			} else if (fileType.replaceAll(
					"^([\\s\\S]*)([xX][lL][sS][xX]) *$", "$2").length() == 4) {
				intent = new Intent("android.intent.action.VIEW");
				intent.addCategory("android.intent.category.DEFAULT");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Uri uri = Uri.fromFile(openFile);
				intent.setDataAndType(uri, "application/vnd.ms-excel");

			} else if (fileType.replaceAll("^([\\s\\S]*)([pP][pP][tT]) *$",
					"$2").length() == 3) {
				intent = new Intent("android.intent.action.VIEW");
				intent.addCategory("android.intent.category.DEFAULT");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Uri uri = Uri.fromFile(openFile);
				intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
			} else if (fileType.replaceAll("^([\\s\\S]*)([pP][nN][gG]) *$",
					"$2").length() == 3
					|| fileType.replaceAll("^([\\s\\S]*)([jJ][pP][gG]) *$",
							"$2").length() == 3
					|| fileType.replaceAll(
							"^([\\s\\S]*)([jJ][pP][eE][gG]) *$", "$2")
							.length() == 4) {

				intent = new Intent("android.intent.action.VIEW");
				intent.addCategory("android.intent.category.DEFAULT");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Uri uri = Uri.fromFile(openFile);
				intent.setDataAndType(uri, "image/*");
			} else {
				intent = new Intent("android.intent.action.VIEW");
				intent.addCategory("android.intent.category.DEFAULT");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Uri uri = Uri.fromFile(openFile);
				intent.setDataAndType(uri, "*/*");
			}
			context.startActivity(intent);
		}
	}

}
