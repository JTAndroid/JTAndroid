package com.tr.ui.conference.common;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;

public class Recorder {
	private MediaRecorder mRecorder = null;

	private final static String TAG = Recorder.class.getSimpleName();

	public void startRecord(String audioFilePath) {

		try {
			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
			mRecorder.setOutputFile(audioFilePath);
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

			mRecorder.prepare();
			mRecorder.start();
			//开始监听音量变化
			updateMicStatus();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void stopRecord() {
		try {
			if (null != mRecorder) {

				mRecorder.stop();
				mRecorder.release();
				mRecorder = null;
				mHandler.removeCallbacksAndMessages(TAG);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getVoiceFilePath() {
		String strVoicePath = FileUtils.VOICE;
		File out = new File(strVoicePath);
		if (!out.exists()) {
			out.mkdirs();
		}
		if (!out.exists()) {
			return "";
		}
		String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + FileUtils.AMR;
		return strVoicePath + fileName;
	}
	
	//一定时间范围内循环获取音量的分贝值
	private final Handler mHandler = new Handler();
	private Runnable mUpdateMicStatusTimer = new Runnable() {
		public void run() {
			updateMicStatus();
		}
	};

	/**
	 * 更新话筒状态
	 * 
	 */
	private int BASE = 1;
	private int SPACE = 100;// 间隔取样时间

	private void updateMicStatus() {
		if (mRecorder != null) {
			double ratio = (double) mRecorder.getMaxAmplitude() / BASE;
			double db = 0;// 分贝
			if (ratio > 1) {
				db = 20 * Math.log10(ratio);
			}
			onVolumeChangedListener.onVolumeChanged(db);
			Log.d(TAG, "分贝值：" + db);
			mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
		}
	}

	// 监听音量变化
	public interface VolumeChangedListener {
		public void onVolumeChanged(double volume);
	}

	private VolumeChangedListener onVolumeChangedListener;

	public void setOnVolumeChangedListener(VolumeChangedListener onVolumeChangedListener) {
		this.onVolumeChangedListener = onVolumeChangedListener;
	}
}