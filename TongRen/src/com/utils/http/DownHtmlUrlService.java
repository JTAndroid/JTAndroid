package com.utils.http;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class DownHtmlUrlService extends Service {
	private String LOCAL_PATH;
	private ArrayList<String> filepathes = new ArrayList<String>();
	private ArrayList<String> allLocalUrlHtmlpathes = new ArrayList<String>();

	@Override
	public IBinder onBind(Intent intent) {
		return null;
		
	}
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		LOCAL_PATH = intent.getStringExtra("LOCAL_PATH");
		filepathes = intent.getStringArrayListExtra("filepathes");
		allLocalUrlHtmlpathes = intent.getStringArrayListExtra("allLocalUrlHtmlpathes");
		downLoad(filepathes);
	}

	/**
	 * 利用XUtils下载图片
	 * @param view
	 * @param content
	 * @param imgsrcLists
	 */
	private void downLoad(List<String> filepathes) {
		HttpUtils http = new HttpUtils();
		
		for (String path : filepathes) {
			File filetmp = new File(LOCAL_PATH + getSuffix(path));
			if (filetmp.exists()) {
			}else {
				http.download(path, LOCAL_PATH + allLocalUrlHtmlpathes.get(filepathes.indexOf(path)), false, new RequestCallBack<File>() {
					@Override
					public void onSuccess(ResponseInfo<File> responseInfo) {
//						Toast.makeText(getApplicationContext(),"下载完成，文件保存在"+ responseInfo.result.getAbsolutePath(), 0).show();
					}
					@Override
					public void onFailure(HttpException error, String msg) {
//						Toast.makeText(getApplicationContext(), "下载失败," + msg, 0).show();
					}
				});
			}
		}
	}
	
	/**
	 * 获得URL中的文件名
	 * @param localpath
	 * @return
	 */
	public String getSuffix(String UrlPath) {
		UUID uuid = UUID.nameUUIDFromBytes(UrlPath.getBytes());
		String uuidString =uuid.toString().replaceAll("-", "");
		return uuidString+".png";
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
