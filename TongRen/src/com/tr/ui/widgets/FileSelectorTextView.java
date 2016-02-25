package com.tr.ui.widgets;

import java.io.File;
import java.util.List;

import com.tr.R;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.base.JBaseFragmentActivity.OnFileSelectedListener;
import com.tr.ui.common.RecordingActivity;
import com.utils.common.EConsts;
import com.utils.common.EUtil;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;


/**
 * @ClassName:     FileSelectorTextView.java
 * @Description:   上传文件
 * @Author         leon
 * @Version        v 1.0  
 * @Create         2014-04-04
 * @Update         2014-04-15
 */
public class FileSelectorTextView extends TextView{

	private final String TAG = getClass().getSimpleName();
	private final String CUSTOM_DATE_FORMAT ="yyyyMMdd_kkmmss";
	private Context mContext; 
	private OnFileSelectedListener mFileSelectedListener;
	private String mFileType;  // 附件类型(图片:pic,视频:video,文档:doc,录音:rec)
		
	public FileSelectorTextView(Context context){
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	
	public FileSelectorTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		// 根据不同属性显示文字和图片
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.com_tr_ui_widgets_FileSelectorTextView);
		mFileType = typedArray
				.getString(R.styleable.com_tr_ui_widgets_FileSelectorTextView_type);
		if (mFileType.equals("pic")) {
			setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_pic,0, 0, 0);
			setText("图片");
		} 
		else if (mFileType.equals("video")) {
			setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_video,0, 0,
					0);
			setText("视频");
		} 
		else if (mFileType.equals("doc")) {
			setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_file,0, 0, 0);
			setText("文件");
		} 
		else if (mFileType.equals("rec")) {
			setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_rec,0, 0, 0);
			setText("录音");
		}
		// 设置文字和图片的位置
		setGravity(Gravity.CENTER_VERTICAL);
		// 设置默认点击事件
		setOnClickListener(mClickListener);
		// 回收资源
		typedArray.recycle();
	}
	
	// 设置文件选择监听器
	public void setOnFileSelectedListener(OnFileSelectedListener listener) {
		mFileSelectedListener = listener;
	}
	
	// 按钮点击事件
	private OnClickListener mClickListener=new OnClickListener(){

		@Override
		public void onClick(View v){
			if(mFileType.equals("pic")){ // 选择图片
				// 显示图片选择对话框
				EUtil.showPhotoSelectDialog(mContext, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (which == 0) { // 请求所属Activity发送拍照请求
							dispatchIntent(EConsts.REQ_CODE_TAKE_PICTURE);
						} 
						else {  // 请求所属Activity发送选照请求
							dispatchIntent(EConsts.REQ_CODE_PICK_PICTURE);
						}
					}
				});
				// 设置文件选择监听器
				if (mContext instanceof JBaseFragmentActivity) {
					((JBaseFragmentActivity) mContext)
							.setOnFileSelctedListener(mFileSelectedListener);
				}
			}
			else if(mFileType.equals("video")){ // 选择视频
				// 显示视频选择对话框
				EUtil.showVideoSelectDialog(mContext, new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if(which == 0){ // 请求所属Activity发送拍视频请求
							dispatchIntent(EConsts.REQ_CODE_TAKE_VIDEO);
						}
						else{ // 请求所属Activity发送选视频请求
							dispatchIntent(EConsts.REQ_CODE_PICK_VIDEO);
						}
					}
				});
				// 设置文件选择监听器
				if (mContext instanceof JBaseFragmentActivity) {
					((JBaseFragmentActivity) mContext)
							.setOnFileSelctedListener(mFileSelectedListener);
				}
			}
			else if(mFileType.equals("doc")){ // 选择文档
				// 请求所属Activity发送文档选择请求
				dispatchIntent(EConsts.REQ_CODE_PICK_FILE);
				// 设置文件选择监听器
				if (mContext instanceof JBaseFragmentActivity) {
					((JBaseFragmentActivity) mContext)
							.setOnFileSelctedListener(mFileSelectedListener);
				}
			}
			else if(mFileType.equals("rec")){ // 选择录音
				// 请求所属Activity发送录音选择请求
				dispatchIntent(EConsts.REQ_CODE_GET_RECORD);
				// 设置文件选择监听器
				if (mContext instanceof JBaseFragmentActivity) {
					((JBaseFragmentActivity) mContext)
							.setOnFileSelctedListener(mFileSelectedListener);
				}
			}
		}
	};
	
	// 发送Intent
	private void dispatchIntent(int requestCode) {
		
		if(!(mContext instanceof JBaseFragmentActivity)){
			Log.d(TAG ,"Context not instanceof JBaseFragmentActivity");
			return;
		}
		JBaseFragmentActivity activity=(JBaseFragmentActivity) mContext;
		switch (requestCode) {
		case EConsts.REQ_CODE_TAKE_PICTURE: // 拍照
			// 默认文件名为 ：时间戳.jpg
			activity.mPicFile = new File(
					mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
					DateFormat.format(CUSTOM_DATE_FORMAT,
							System.currentTimeMillis())
							+ ".jpg");
			// 发送拍照请求
			EUtil.dispatchTakePictureIntent(activity,
					Uri.fromFile(activity.mPicFile), requestCode);
			break;
		case EConsts.REQ_CODE_PICK_PICTURE: // 选图
			EUtil.dispatchPickPictureIntent(activity, requestCode);
			break;
		case EConsts.REQ_CODE_TAKE_VIDEO: // 拍视频
			// 默认文件名为： 时间戳.mp4
			activity.mVideoFile = new File(
					mContext.getExternalFilesDir(Environment.DIRECTORY_MOVIES),
					DateFormat.format(CUSTOM_DATE_FORMAT,
							System.currentTimeMillis())
							+ ".mp4");
			// 发送拍视频请求
			EUtil.dispatchTakeVideoIntent(activity,
					Uri.fromFile(activity.mVideoFile), requestCode);
			break;
		case EConsts.REQ_CODE_PICK_VIDEO: // 选视频
			EUtil.dispatchPickVideoIntent(activity, requestCode);
			break;
		case EConsts.REQ_CODE_PICK_FILE: // 选文件
			EUtil.dispatchPickFileIntent(activity, requestCode);
			break;
		case EConsts.REQ_CODE_GET_RECORD: // 选音频
			EUtil.dispatchGetRecordIntent(activity, requestCode);
			break;
		}
	}
}
