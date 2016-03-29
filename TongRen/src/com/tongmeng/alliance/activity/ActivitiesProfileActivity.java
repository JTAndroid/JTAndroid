package com.tongmeng.alliance.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tongmeng.alliance.activity.NoteActivity.JavaScriptinterface;
import com.tongmeng.alliance.util.LoadUtil;
import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.utils.log.KeelLog;

public class ActivitiesProfileActivity extends JBaseActivity implements OnClickListener{

	private ImageView search;
	private TextView myTitle;
	private TextView create_Tv;
	
	private static WebView myWebView;
	static String url;
	String filename = null, activityDes;
	String htmlText;
	ImageView iv_back;

	ProgressBar progressBar;
	public Dialog dialog;
	Button phonegraph_complete_info, image_complete_info, cancle_complete_info,
			comeinto_main;
	String imagePath = null;
	Uri uri, mOutPutFileUri;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activities_profile);
		
		activityDes = getIntent().getStringExtra("activitydes");
		progressBar = (ProgressBar) findViewById(R.id.activity_profile_progressBar);
		myWebView = (WebView) findViewById(R.id.myWebView);
		myWebView.getSettings().setJavaScriptEnabled(true);
		myWebView.getSettings().setBuiltInZoomControls(true);
		myWebView.getSettings().setSavePassword(false);
		// 支持多种分辨率，需要js网页支持
		myWebView.getSettings().setUserAgentString("mac os");
		myWebView.getSettings().setDefaultTextEncodingName("utf-8");
		iv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		Button btnimage = (Button) findViewById(R.id.btnimage);
		htmlText = "http://test.etongmeng.com/html5/activity/android_editor.html";
		myWebView.addJavascriptInterface(new JavaScriptinterface(this),
				"android");

		btnimage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// Intent intent = new Intent();// ACTION_OPEN_DOCUMENT
				// // intent.addCategory(Intent.CATEGORY_OPENABLE);
				// intent.setType("image/*");
				// if (android.os.Build.VERSION.SDK_INT >=
				// android.os.Build.VERSION_CODES.KITKAT) {
				// intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
				// intent.addCategory(Intent.CATEGORY_OPENABLE);
				// } else {
				// intent.setAction(Intent.ACTION_GET_CONTENT);
				// }
				// startActivityForResult(intent, 2);
				createDialog();
			}
		});
		String htmlText = "http://test.etongmeng.com/html5/activity/android_editor.html";
		myWebView.loadUrl(htmlText);

		new Thread() {

			public void run() {

				// Toast.makeText(getApplicationContext(),
				// "发起活动内容"+activity_profile, 1).show();
				if (activityDes != null && !activityDes.equals("")) {
					if (progressBar.getVisibility() == View.GONE) {
						progressBar.setVisibility(View.VISIBLE);
					}
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (msgHandler != null) {
						Message msg = msgHandler.obtainMessage();
						msg.what = 2;
						msg.obj = activityDes;
						msgHandler.sendMessage(msg);
					}
				}
			};
		}.start();
		myWebView.loadUrl("javascript:setHtml('" + activityDes + "')");
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		myWebView.resumeTimers();
	}

	Handler msgHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {

			case 1:
				// Toast.makeText(ActivitiesProfileActivity.this, "上传返回" + url,
				// 1).show();
				myWebView.loadUrl("javascript:addImage('" + url + "')");
				if (progressBar.getVisibility() == View.VISIBLE) {
					progressBar.setVisibility(View.GONE);
				}
				break;
			case 2:
				myWebView.loadUrl("javascript:setHtml('" + msg.obj + "')");
				if (progressBar.getVisibility() == View.VISIBLE) {
					progressBar.setVisibility(View.GONE);
				}
				break;

			default:
				break;
			}
		}
	};


	
	@Override
	public void initJabActionBar() {
		// TODO Auto-generated method stub
		KeelLog.e("ContactsMainPageActivity", "initJabActionBar");
		ActionBar mActionBar = jabGetActionBar();
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(true);
		View mCustomView = getLayoutInflater().inflate(R.layout.org_firstpage_actionbar_title, null);
		mActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView.getLayoutParams();
		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK | Gravity.CENTER_HORIZONTAL;
		mActionBar.setCustomView(mCustomView, mP);
		mActionBar.setTitle(" ");
		myTitle = (TextView) mCustomView.findViewById(R.id.titleTv);
		myTitle.setText("活动简介");
		create_Tv = (TextView) mCustomView.findViewById(R.id.create_Tv);
		create_Tv.setText("保存");
		create_Tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				myWebView.loadUrl("javascript:getHtml()");
			}
		});
		search = (ImageView) mCustomView.findViewById(R.id.titleIv);
		search.setVisibility(View.GONE);
		
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (data != null) {
//			Uri uri = data.getData();
//			filename = LoadUtil.getImageAbsolutePath(this, uri);
//			if (progressBar.getVisibility() == View.GONE) {
//				progressBar.setVisibility(View.VISIBLE);
//			}
//			getImage();
//		} else {
//
//		}
		KeelLog.e("", "requestCode::"+requestCode);
		KeelLog.e("", "resultCode::"+resultCode);
		KeelLog.e("", "data::"+data);
		if (requestCode == 1 && resultCode == RESULT_OK) {

			String sdStatus = Environment.getExternalStorageState();
			// ���sd�Ƿ����
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
				Log.v("TestFile",
						"SD card is not avaiable/writeable right now.");
				return;
			}

			Bundle bundle = data.getExtras();
			String str;
			Date date;
			try {
				Bitmap bitmap = (Bitmap) bundle.get("data");
				FileOutputStream b = null;
				File file = new File("/sdcard/myImage/");
				file.mkdirs();
				str = null;
				date = null;

				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy-MM-dd-HH-mm-ss");
				date = new Date();
				str = format.format(date);
				// private static final String IMAGE_PATH = "/sdcard/myImage/";
				filename = "/sdcard/myImage/" + str + ".jpg";

				try {
					b = new FileOutputStream(filename);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} finally {
					try {
						b.flush();
						b.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				imagePath = filename;
				Log.e("", "相机照片地址imagePath：：：" + imagePath);
				
				getImage();
//				myWebView.loadUrl("javascript:addImage('" + imagePath + "')");
//				if (progressBar.getVisibility() == View.VISIBLE) {
//					progressBar.setVisibility(View.GONE);
//				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if (requestCode == 2 && resultCode == RESULT_OK) {
			if (data == null) {
				Log.e("", "相册返回数据为空");
			} else {
				Log.e("", " 相册data::" + data);

				uri = data.getData();
				Log.e("", "uri::" + uri);

//				Bitmap bitmap = null, bitmap1 = null;
				filename = LoadUtil.getImageAbsolutePath(
						ActivitiesProfileActivity.this, uri);
				Log.e("", "相册图片地址：：" + filename);
				
				imagePath = filename;
				Log.e("", "相机照片地址imagePath：：：" + imagePath);
 
				getImage();
//				String result = LoadUtil.multiPart(imagePath,this);
//				KeelLog.e("requestCode == 2", "result::"+result);
//				String serverUrl = LoadUtil.getFileServerInfo(result, "url");
//				KeelLog.e("requestCode == 2", "serverUrl::"+serverUrl);
//				myWebView.loadUrl("javascript:addImage('" + serverUrl + "')");
			}
		}
	}
	
	protected void getImage() {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				String result = LoadUtil.multiPart(filename,
						ActivitiesProfileActivity.this);

				Log.e("getImage ", "result::" + result);
				url = LoadUtil.getFileServerInfo(result, "url");
				Log.e("", "url::" + url);
				if (!"".equals(uri)&& null!=url) {
					if (msgHandler != null) {
						Message msg = msgHandler.obtainMessage();
						msg.what = 1;
						msgHandler.sendMessage(msg);
					}
				}
			};
		}.start();
	}

	class JavaScriptinterface {

		private Context mContext;

		/** Instantiate the interface and set the context */
		public JavaScriptinterface(Context c) {
			mContext = c;
		}

		@JavascriptInterface
		public void jsMethod(String result) {
			KeelLog.e("----jsMethod----", "result::" + result);
			Intent intent = new Intent();
			intent.putExtra("activitydes", result);
			setResult(RESULT_OK, intent);
			if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
				getWindow().setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
			}
			finish();

		}

	}
	
	public void createDialog() {
		dialog = new Dialog(ActivitiesProfileActivity.this,
				R.style.MyDialogStyle);
		View layout = LayoutInflater.from(this).inflate(
				R.layout.dialog_complete_info, null);
		dialog.setContentView(layout);
		phonegraph_complete_info = (Button) layout
				.findViewById(R.id.phonegraph_complete_info);
		image_complete_info = (Button) layout
				.findViewById(R.id.image_complete_info);
		cancle_complete_info = (Button) layout
				.findViewById(R.id.cancle_complete_info);
		phonegraph_complete_info.setOnClickListener(this);
		image_complete_info.setOnClickListener(this);
		cancle_complete_info.setOnClickListener(this);
		WindowManager m = getWindowManager();
		Display d = m.getDefaultDisplay();

		Window dialogWindow = dialog.getWindow();
		android.view.WindowManager.LayoutParams p = dialogWindow
				.getAttributes(); // ��ȡ�Ի���ǰ�Ĳ���ֵ
		p.width = (int) (d.getWidth());
		dialogWindow.setGravity(Gravity.BOTTOM);
		dialog.getWindow().setAttributes(p);
		dialog.show();
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnimage:
			createDialog();
			break;
		case R.id.phonegraph_complete_info:// 拍照
			dialog.dismiss();
			Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent1, 1);
			break;
		case R.id.image_complete_info:
			dialog.dismiss();
			Intent intent = new Intent();// ACTION_OPEN_DOCUMENT
			// intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setType("image/*");
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
				intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
			} else {
				intent.setAction(Intent.ACTION_GET_CONTENT);
			}
			startActivityForResult(intent, 2);
			break;
		case R.id.cancle_complete_info:
			dialog.dismiss();
		default:
			break;
		}
	}
}
