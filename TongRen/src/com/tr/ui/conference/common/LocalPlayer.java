package com.tr.ui.conference.common;

import java.io.FileDescriptor;
import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;

public class LocalPlayer {
	private MediaPlayer mPlayer = null;
	private boolean isPlay = false;

	public void startPlay(String playFilePath, OnCompletionListener listener) {
		mPlayer = new MediaPlayer();
		try {
			mPlayer.setOnCompletionListener(listener);
			mPlayer.setDataSource(playFilePath);
			mPlayer.prepare();
			mPlayer.start();
			isPlay = true;
		} catch (IOException e) {
			isPlay = false;
		}
	}
	public void startPlay(AssetFileDescriptor openFd, OnCompletionListener listener) {
		mPlayer = new MediaPlayer();
		try {
			mPlayer.setOnCompletionListener(listener);
			mPlayer.setDataSource(openFd.getFileDescriptor(),openFd.getStartOffset(),openFd.getLength());
			mPlayer.prepare();
			mPlayer.start();
			isPlay = true;
		} catch (IOException e) {
			isPlay = false;
		}
	}

	public void stopPlay() {
		isPlay = false;
		if (null != mPlayer) {
			mPlayer.stop();
			mPlayer.release();
			mPlayer = null;
		}
	}

	public int getDuration() {
		if (null != mPlayer && isPlay) {
			return mPlayer.getDuration();
		}
		return 0;
	}

	public int getCurrentPosition() {
		if (null != mPlayer && isPlay) {
			return mPlayer.getCurrentPosition();
		}
		return 0;
	}

	public int getProgress() {
		try {
			if (null != mPlayer && isPlay) {
				int a = mPlayer.getDuration();
				int b = mPlayer.getCurrentPosition();
				if (0 != a) {
					return b * 100 / a;
				} else {
					return 0;
				}
			}
		} catch (Exception e) {

		}
		return 0;
	}

	public void pause() {
		try {
			if (mPlayer != null) {
				mPlayer.pause();
				isPlay = false;
			}
		} catch (Exception e) {
			isPlay = false;
		}
	}

	public void start() {
		try {
			if (mPlayer != null) {
				mPlayer.start();
				isPlay = true;
			}
		} catch (Exception e) {
			isPlay = false;
		}
	}

	public boolean isPlay() {
		return isPlay;
	}

}