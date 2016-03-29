package com.tongmeng.alliance.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

import com.tr.App;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

public class LoadUtil {
	public static String TAG = "LoadUtil";

	/**
	 * 上传文件，获取返回结果
	 * 
	 * @param fileName
	 * @return
	 */
	public static String multiPart(String fileName,Context context) {
		try {
			String line, result = null;
			int paramsLen = 0;
			String end = "\r\n";
			String twoHyphens = "--";
			String boundary = "******"; // UUID.randomUUID().toString()，边界标识

			URL url = new URL(Constant.uploadPath);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true); // 允许输入流
			conn.setDoOutput(true); // 允许输出流
			conn.setUseCaches(false); // 不使用缓存
			// 防止上传大文件时全部读入内存（获取Content-Length）造成崩溃
			conn.setRequestMethod("POST"); // 请求方式
			conn.setConnectTimeout(60 * 1000); // 60s超时
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

			StringBuilder sbEx1 = new StringBuilder();
			sbEx1.append(twoHyphens);
			sbEx1.append(boundary);
			sbEx1.append(end);
			sbEx1.append("Content-Disposition: form-data; name=\"" + "userId"
					+ "\"" + end);
			sbEx1.append(end);
			sbEx1.append(App.getApp().getAppData().getUserID());
			sbEx1.append(end);
			dos.write(sbEx1.toString().getBytes());

			// 上传文件
			StringBuffer sbEx = new StringBuffer();
			sbEx.append(twoHyphens);
			sbEx.append(boundary);
			sbEx.append(end);
			sbEx.append("Content-Disposition: form-data; name=\"file\"; filename=\""
					+ new File(fileName).getName() + "\"" + end);
			sbEx.append(end);
			dos.write(sbEx.toString().getBytes());
			// 开始写入文件
			FileInputStream fis = new FileInputStream(fileName);
			byte[] buffer = new byte[1024];
			int count = 0;
			int length = 0;
			while ((count = fis.read(buffer)) != -1) {

				// 写入数据
				dos.write(buffer, 0, count);
				// 获取进度
				length += count;
				// 更新进度

			}
			fis.close();
			dos.writeBytes(end);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
			dos.flush();
			dos.close();

			// 获取响应码 200=成功
			int res = conn.getResponseCode();
			System.out.println(res);

			BufferedReader br = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			System.out.println("InputStream::" + conn.getInputStream());

			while ((line = br.readLine()) != null) {
				result += line;
			}

			System.out.println("result::"
					+ new String(result.getBytes("gbk"), "utf-8"));

			System.out.println("multipart 返回码为: " + conn.getResponseCode());
			System.out.println("multipart 返回信息为: " + conn.getResponseMessage());

			return result;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// /**
	// * 获取当前文件的绝对路径，主要用于从相册等通过activityresult方法返回的文件
	// * @param activity
	// * @param uri
	// * @return
	// */
	// public static String getFilePath(Activity activity, Uri uri) {
	//
	// ContentResolver resolver = activity.getContentResolver();
	// String[] proj = { MediaStore.Images.Media.DATA };
	// Cursor cursor = activity.managedQuery(uri, proj, null, null, null);
	// int column_index = cursor
	// .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	// cursor.moveToFirst();
	// String path = cursor.getString(column_index);
	// return path;
	// }

	/**
	 * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
	 * 
	 * @param activity
	 * @param imageUri
	 * @author yaoxing
	 * @date 2014-10-12
	 */
	@SuppressLint("NewApi") public static String getImageAbsolutePath(Context context, Uri imageUri) {
		if (context == null || imageUri == null)
			return null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT
				&& DocumentsContract.isDocumentUri(context, imageUri)) {
			if (isExternalStorageDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}
			} else if (isDownloadsDocument(imageUri)) {
				String id = DocumentsContract.getDocumentId(imageUri);
				Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));
				return getDataColumn(context, contentUri, null, null);
			} else if (isMediaDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}
				String selection = MediaStore.Images.Media._ID + "=?";
				String[] selectionArgs = new String[] { split[1] };
				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		} // MediaStore (and general)
		else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
			// Return the remote address
			if (isGooglePhotosUri(imageUri))
				return imageUri.getLastPathSegment();
			return getDataColumn(context, imageUri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
			return imageUri.getPath();
		}
		return null;
	}

	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {
		Cursor cursor = null;
		String column = MediaStore.Images.Media.DATA;
		String[] projection = { column };
		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}

	/**
	 * 上传文件成功后，解析服务器返回数据
	 * 
	 * @param requestString
	 *            服务器返回的数据
	 * @param param
	 *            想要获取的字段：“id”或者“url”
	 * @return
	 */
	public static String getFileServerInfo(String requestString, String param) {
		String str = null;
		try {
			String result = requestString.substring(requestString.indexOf("{"),
					requestString.lastIndexOf("}") + 1);
			Log.e("", "result::" + result);
			JSONObject job = new JSONObject(result);
			String responseData = job.getString("responseData");
			String notification = job.getString("notification");
			Log.e("", "responseData::" + responseData);
			Log.e("", "notification::" + notification);
			JSONObject noJob = new JSONObject(notification);
			String notifyCode = noJob.getString("notifyCode");
			Log.e("", "notifyCode::" + notifyCode);
			if (notifyCode.equals("0001")) {
				JSONObject fileJon = new JSONObject(
						new JSONObject(responseData).getString("file"));
				String id = fileJon.getString("id");
				String url = fileJon.getString("url");
				Log.e("", "id::" + id);
				Log.e("", "url::" + url);
				str = fileJon.getString(param);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
}
