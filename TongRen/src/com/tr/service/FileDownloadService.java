package com.tr.service;

import java.util.ArrayList;
import java.util.List;
import com.tr.model.obj.JTFile;
import com.utils.common.EConsts;
import com.utils.common.FileDownloader;
import com.utils.common.FileDownloader.OnFileDownloadListener;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;

public class FileDownloadService extends Service {

	private final String TAG = getClass().getName();
	
	// 下载管理器
	private List<FileDownloader> mDownloaders;
	
	public List<FileDownloader> getListDownloader() {
		return mDownloaders;
	}

	private MyBinder mBinder;
	
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate(){
		super.onCreate();
		initVars();
		registerReceiver(mReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
	}
	
	private void initVars(){
		mDownloaders = new ArrayList<FileDownloader>();
		mBinder = new MyBinder();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		if (intent == null || intent.getAction() == null) {
			return;
		}
		if(intent.getAction().equals(EConsts.Action.DOWNLOAD_START)){ // 开始下载
			JTFile jtFile = (JTFile) intent.getSerializableExtra(EConsts.Key.JT_FILE);
			boolean taskExist = false;
			for (FileDownloader downloader : mDownloaders) {
				if (downloader.getJTFile().mUrl.equals(jtFile.mUrl)) {
					taskExist = true;
					downloader.start();
					break;
				}
			}
			if(!taskExist){
				FileDownloader downloader = new FileDownloader(this, jtFile, mFileDownloadListener);
				mDownloaders.add(downloader);
				downloader.start();
			}
		}
		else if(intent.getAction().equals(EConsts.Action.DOWNLOAD_CANCELED)){ // 取消下载
			
			String url = intent.getStringExtra(EConsts.Key.WEB_FILE_URL);
			// 取消下载进程
			for (FileDownloader downloader : mDownloaders) {
				if (downloader.getJTFile().mUrl.equals(url)) {
					downloader.cancel();
					mDownloaders.remove(downloader);
					break;
				}
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}
	
	// 发送广播通知界面更改
	private OnFileDownloadListener mFileDownloadListener = new OnFileDownloadListener(){

		
		@Override
		public void onPrepared(String url){
			
		}
		
		@Override
		public void onStarted(String url) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(EConsts.Action.DOWNLOAD_START);
			intent.putExtra(EConsts.Key.WEB_FILE_URL, url);
			sendBroadcast(intent);
		}

		@Override
		public void onSuccess(String url, JTFile jtFile) {
			// TODO Auto-generated method stub
			// 从下载队列中删除
			for (FileDownloader downloader : mDownloaders) {
				if(downloader.getJTFile().mUrl.equals(url)){
					mDownloaders.remove(downloader);
				}
			}
			// 发送广播
			Intent intent = new Intent(EConsts.Action.DOWNLOAD_SUCCESS);
			intent.putExtra(EConsts.Key.WEB_FILE_URL, url);
			intent.putExtra(EConsts.Key.JT_FILE, jtFile);
			sendBroadcast(intent);
		}

		@Override
		public void onError(String url,int errCode, String errMessage) {
			// 从下载队列中删除
			for (FileDownloader downloader : mDownloaders) {
				if(downloader.getJTFile().mUrl.equals(url)){
					mDownloaders.remove(downloader);
				}
			}
			// 发送广播
			Intent intent = new Intent(EConsts.Action.DOWNLOAD_FAILED);
			intent.putExtra(EConsts.Key.WEB_FILE_URL, url);
			intent.putExtra(EConsts.Key.ERROR_CODE, errCode);
			intent.putExtra(EConsts.Key.ERROR_MESSAGE, errMessage);
			sendBroadcast(intent);	
		}

		@Override
		public void onUpdate(String url, int progress) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(EConsts.Action.DOWNLOAD_UPDATE);
			intent.putExtra(EConsts.Key.WEB_FILE_URL, url);
			intent.putExtra(EConsts.Key.PROGRESS_UPDATE, progress);
			sendBroadcast(intent);
		}

		@Override
		public void onCanceled(String url) {
			// TODO Auto-generated method stub
			// 发送广播
			Intent intent = new Intent(EConsts.Action.DOWNLOAD_CANCELED);
			intent.putExtra(EConsts.Key.WEB_FILE_URL, url);
			sendBroadcast(intent);
		}	
	};

	// 为获取服务对象
	public class MyBinder extends Binder {

		public FileDownloadService getService() {
			return FileDownloadService.this;
		}
	}
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            	ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connManager.getActiveNetworkInfo();  
                if(networkInfo == null || !networkInfo.isAvailable()) {
                	// 网络不可用
                	for (FileDownloader downloader : mDownloaders) {
        				downloader.cancel();
        				mDownloaders.remove(downloader);
        			}
                } 
            }
        }
    };
	
}
