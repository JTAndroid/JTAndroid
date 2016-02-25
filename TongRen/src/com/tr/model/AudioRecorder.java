package com.tr.model;

import java.io.File;
import java.io.IOException;
import android.media.MediaRecorder;
import android.util.Log;

public class AudioRecorder {
	
	private final String TAG = getClass().getSimpleName();
	private MediaRecorder mRecorder;

	// 开始录音
	public void start(String ouputFilePath,String ouputFileName) {
		
		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		// mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		// 设置最大时长（暂不设限制）
		// mRecorder.setMaxDuration(60000);
		// 设置文件输出位置
		mRecorder.setOutputFile(ouputFilePath + File.separator + ouputFileName);
		try {
			mRecorder.prepare();
		} 
		catch (IllegalStateException e) {
			Log.d(TAG,e.getMessage());
		} 
		catch (IOException e) {
			Log.d(TAG,e.getMessage());
		}
		mRecorder.start();
	}

	// 停止录音
	public void stop() {
		if (mRecorder != null) {
			mRecorder.stop();
			mRecorder.release();
			mRecorder = null;
		}
	}
}
