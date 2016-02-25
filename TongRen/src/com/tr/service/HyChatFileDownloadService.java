package com.tr.service;

import java.util.ArrayList;
import java.util.List;
import com.tr.model.obj.JTFile;
import com.utils.common.EConsts;
import com.utils.common.HyChatFileDownloader;
import com.utils.common.HyChatFileDownloader.OnFileDownloadListener;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;

/**
 * 会议畅聊文件下载管理服务
 * @author leon
 */
public class HyChatFileDownloadService extends Service implements OnFileDownloadListener{

	private final String TAG = getClass().getSimpleName();
	
	// 下载管理器
	private List<HyChatFileDownloader> listDownloader = new ArrayList<HyChatFileDownloader>();
	// Binder，用来获取服务对象
	private ServiceBinder binder;
	// 会议id
	// private long meetingId = 0;
	// 议题id
	// private long topicId = 0;
	
	public ServiceBinder getBinder() {
		if(binder == null){
			binder = new ServiceBinder();
		}
		return binder;
	}

	public List<HyChatFileDownloader> getListDownloader() {
		if(listDownloader == null){
			listDownloader = new ArrayList<HyChatFileDownloader>();
		}
		return listDownloader;
	}


	@Override
	public IBinder onBind(Intent intent) {
		return getBinder();
	}

	@Override
	public void onCreate(){
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
	
	/**
	 * 设置绑定的会议议题信息
	 * @param meetingId
	 * @param topicId
	 */
	/*
	public void setBindedMeetingTopicInfo(long meetingId, long topicId){
		this.meetingId = meetingId;
		this.topicId = topicId;
	}
	*/
	
	/**
	 * 查看下载任务是否存在
	 * @param url
	 * @return
	 */
	public boolean isTaskExist(String messageId){
		if(TextUtils.isEmpty(messageId)){
			return false;
		}
		for (HyChatFileDownloader downloader : listDownloader) {
			if (downloader.getMessageId().equals(messageId)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 启动新的下载任务
	 * @param jtFile
	 */
	public void startNewTask(String messageId, JTFile jtFile, long meetingId, long topicId){
		if(jtFile == null 
				|| TextUtils.isEmpty(jtFile.mUrl)
				|| TextUtils.isEmpty(messageId)){
			return;
		}
		if(!isTaskExist(messageId)){
			HyChatFileDownloader downloader = new HyChatFileDownloader(this, jtFile, messageId, meetingId, topicId);
			downloader.setOnFileDownloadListener(this);
			listDownloader.add(downloader);
			downloader.start();
		}
		else{
			for (HyChatFileDownloader downloader : listDownloader) {
				if (downloader.getMessageId().equals(messageId)) {
					if(downloader.getStatus() != HyChatFileDownloader.Status.Started
							&& downloader.getStatus() != HyChatFileDownloader.Status.Prepared){
						downloader.start();
					}
					break;
				}
			}
		}
	}
	
	/**
	 * 查看下载任务状态
	 * @param url
	 * @return
	 */
	public int getTaskStatus(String messageId){
		if(!isTaskExist(messageId)){
			return 0;
		}
		for (HyChatFileDownloader downloader : listDownloader) {
			if (downloader.getMessageId().equals(messageId)) {
				return downloader.getStatus();
			}
		}
		return 0;
	}
	
	/**
	 * 获取下载任务进度
	 * @param url
	 * @return
	 */
	public int getTaskProgress(String messageId){
		if(!isTaskExist(messageId)){
			return 0;
		}
		for (HyChatFileDownloader downloader : listDownloader) {
			if (downloader.getMessageId().equals(messageId)) {
				return downloader.getProgress();
			}
		}
		return 0;
	}
	
	/**
	 * 取消下载任务
	 * @param url
	 */
	public void cancelTask(String messageId){
		if(isTaskExist(messageId)){
			for (HyChatFileDownloader downloader : listDownloader) {
				if (downloader.getMessageId().equals(messageId)) {
					downloader.cancel();
					break;
				}
			}
		}
	}
	
	/**
	 * 移除下载任务
	 * @param url
	 */
	public void removeTask(String messageId){
		if(isTaskExist(messageId)){
			for (int i = 0; i < listDownloader.size(); i++) {
				if (listDownloader.get(i).getMessageId().equals(messageId)) {
					listDownloader.remove(i);
					break;
				}
			}
		}
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
	
	public class ServiceBinder extends Binder {
		/**
		 * 获取服务对象
		 * @return
		 */
		public HyChatFileDownloadService getService() {
			return HyChatFileDownloadService.this;
		}
	}

	@Override
	public void onPrepared(String messageId) {
		
	}


	@Override
	public void onStarted(String messageId) {
		Intent intent = new Intent(EConsts.Action.DOWNLOAD_START);
		intent.putExtra(EConsts.Key.MESSAGE_ID, messageId);
		sendBroadcast(intent);
	}


	@Override
	public void onSuccess(String messageId, JTFile jtFile) {
		// 从下载队列中删除任务
		removeTask(messageId);
		// 发送广播
		Intent intent = new Intent(EConsts.Action.DOWNLOAD_SUCCESS);
		intent.putExtra(EConsts.Key.MESSAGE_ID, messageId);
		intent.putExtra(EConsts.Key.JT_FILE, jtFile);
		sendBroadcast(intent);
	}


	@Override
	public void onError(String messageId, int errCode, String errMsg) {
		Intent intent = new Intent(EConsts.Action.DOWNLOAD_FAILED);
		intent.putExtra(EConsts.Key.MESSAGE_ID, messageId);
		intent.putExtra(EConsts.Key.ERROR_CODE, errCode);
		intent.putExtra(EConsts.Key.ERROR_MESSAGE, errMsg);
		sendBroadcast(intent);	
	}


	@Override
	public void onUpdate(String messageId, int progress) {
		Intent intent = new Intent(EConsts.Action.DOWNLOAD_UPDATE);
		intent.putExtra(EConsts.Key.MESSAGE_ID, messageId);
		intent.putExtra(EConsts.Key.PROGRESS_UPDATE, progress);
		sendBroadcast(intent);
	}


	@Override
	public void onCanceled(String messageId) {
		// 从下载队列中删除任务
		removeTask(messageId);
		// 发送广播
		Intent intent = new Intent(EConsts.Action.DOWNLOAD_CANCELED);
		intent.putExtra(EConsts.Key.MESSAGE_ID, messageId);
		sendBroadcast(intent);
	}
}
