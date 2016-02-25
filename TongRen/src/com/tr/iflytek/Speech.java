package com.tr.iflytek;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

@SuppressLint("ShowToast")
public class Speech {
	private Context context;
	private SpeechRecognizer recognizer;
	private RecognizerDialog recognizerDialog;
//	private SpeechRecognizerResultListener recognizerResultListener;
	private Toast toast;
	private boolean isShowDialog;
	
	public Speech(Context context){
		this.context = context;
	}
	
	public void init(){
		isShowDialog = true;
		
		SpeechUtility.createUtility(context, "appid=5459b822");//54649614 5459b822
		// 初始化识别对象
		recognizer = SpeechRecognizer.createRecognizer(context, initListener);
		// 初始化听写Dialog,如果只使用有UI听写功能,无需创建SpeechRecognizer
		recognizerDialog = new RecognizerDialog(context, initListener);
		toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
	}
	public void setShowDialog(boolean isShowDialog){
		this.isShowDialog = isShowDialog;
	}
//	public void setRecognizerResultListener(SpeechRecognizerResultListener recognizerResultListener){
//		this.recognizerResultListener = recognizerResultListener;
//	}
	public void startSpeech(){
		if(isShowDialog){
			// 显示听写对话框
			recognizerDialog.setListener(recognizerDialogListener);
			recognizerDialog.show();
			showTip("请开始讲话");
		}else{
			// 不显示听写对话框
			int ret = recognizer.startListening(recognizerListener);
			if(ret != ErrorCode.SUCCESS){
				showTip("听写失败, 错误码：" + ret);
			}else {
				showTip("请开始讲话");
			}
		}
	}
	public void stopSpeech(){
		recognizer.stopListening();
		showTip("停止听写");
	}
	public void cancelSpeech(){
		recognizer.cancel();
		showTip("取消听写");
	}
	private void showTip(final String str){
		Activity activity = (Activity)context;
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				toast.setText(str);
				toast.show();
			}
		});
	}
	/**
	 * 初始化监听器。
	 */
	private InitListener initListener = new InitListener() {
		@Override
		public void onInit(int code) {
			if (code != ErrorCode.SUCCESS) {
        		showTip("初始化失败, 错误码："+code);
        	}
		}
	};
	/**
	 * 听写监听器。
	 */
	private RecognizerListener recognizerListener = new RecognizerListener(){

		@Override
		public void onBeginOfSpeech() {	
			showTip("开始说话");
		}

		@Override
		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));
		}

		@Override
		public void onEndOfSpeech() {
			showTip("结束说话");
		}

		@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			if(recognizerListener == null){
				showTip("识别监听未设置");
				return;
			}
			String text = JsonParser.parseIatResult(results.getResultString());
//			recognizerResultListener.onRecognizer(text);
			((SpeechOnRecognizerResultListener)context).onRecognizerResultListener(text);
//			if(isLast) {
//				//TODO 最后的结果
//			}
		}

		@Override
		public void onVolumeChanged(int volume) {
			showTip("当前正在说话，音量大小：" + volume);
		}


		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
		}
	};
	/**
	 * 听写UI监听器
	 */
	private RecognizerDialogListener recognizerDialogListener = new RecognizerDialogListener(){
		public void onResult(RecognizerResult results, boolean isLast) {
			if(recognizerListener == null){
				showTip("识别监听未设置");
				return;
			}
			String text = JsonParser.parseIatResult(results.getResultString());
			((SpeechOnRecognizerResultListener)context).onRecognizerResultListener(text);
		}

		/**
		 * 识别回调错误.
		 */
		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));
		}

	};
	
	public interface SpeechOnRecognizerResultListener {
		public abstract void onRecognizerResultListener(String result);
	}
}
