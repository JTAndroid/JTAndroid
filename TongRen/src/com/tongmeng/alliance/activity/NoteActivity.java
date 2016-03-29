package com.tongmeng.alliance.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tongmeng.alliance.dao.NoteDao;
import com.tongmeng.alliance.dao.ServerResultDao;
import com.tongmeng.alliance.util.Constant;
import com.tongmeng.alliance.util.HttpRequestUtil;
import com.tongmeng.alliance.util.LoadUtil;
import com.tongmeng.alliance.util.Utils;
import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.utils.log.KeelLog;

public class NoteActivity extends JBaseActivity{

	private ImageView search;
	private TextView myTitle;
	private TextView create_Tv;
	
	private WebView myWebView;
	static String url;
	RelativeLayout layout;
	EditText edittext;
	String activityId;
	String htmlText;
	String filename = null;
	
	Handler msgHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {

			case 1:
				myWebView.loadUrl("javascript:addImage('" + url + "')");
				break;
			case 2:
				myWebView.loadUrl("javascript:setHtml('" + msg.obj + "')");
				break;
			case 3:// 报存笔记成功
				Toast.makeText(NoteActivity.this, "保存笔记成功！", 0).show();
				Intent intent = new Intent(NoteActivity.this,ActionNoteActivity.class);
				intent.putExtra("activityId", activityId);
				startActivity(intent);
				finish();
				break;
			case 4:// 报存笔记失败
				Toast.makeText(NoteActivity.this, msg.obj+"", 0).show();
				break;
			default:
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activities_profile);
		layout = (RelativeLayout) findViewById(R.id.activity_profile_layou);
		layout.setVisibility(View.VISIBLE);
		edittext = (EditText) findViewById(R.id.activity_profile_edittext);
		myWebView = (WebView) findViewById(R.id.myWebView);
		myWebView.getSettings().setJavaScriptEnabled(true);
		myWebView.getSettings().setBuiltInZoomControls(true);
		myWebView.getSettings().setSavePassword(false);
		// 支持多种分辨率，需要js网页支持
		myWebView.getSettings().setUserAgentString("mac os");
		myWebView.getSettings().setDefaultTextEncodingName("utf-8");
		Button btnimage = (Button) findViewById(R.id.btnimage);
		htmlText = "http://test.etongmeng.com/html5/activity/android_editor.html";
		myWebView.addJavascriptInterface(new JavaScriptinterface(this),
				"android");
		btnimage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intentimage = new Intent();
				intentimage.setType("image/*");
				intentimage.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intentimage, 2);
			}
		});
		String htmlText = "http://test.etongmeng.com/html5/activity/android_editor.html";
		myWebView.loadUrl(htmlText);
	}
	

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
		myTitle.setText("新建笔记");
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
	protected void onResume() {
		super.onResume();
		myWebView.resumeTimers();
	}
	
	protected void getImage() {
		new Thread() {

			public void run() {
				String result = LoadUtil.multiPart(filename, NoteActivity.this);

				KeelLog.e("", "getImage result::" + result);
				url = LoadUtil.getFileServerInfo(result, "url");
				KeelLog.e("", "getImage url::" + url);
				if (!url.equals(" ")) {
					if (msgHandler != null) {
						Message msg = msgHandler.obtainMessage();
						msg.what = 1;
						msgHandler.sendMessage(msg);
					}
				}
			};
		}.start();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			Uri uri = data.getData();
			filename = LoadUtil.getImageAbsolutePath(this, uri);
			getImage();
		} else {

		}
	}


	class JavaScriptinterface {

		private Context mContext;

		/** Instantiate the interface and set the context */
		public JavaScriptinterface(Context c) {
			mContext = c;
		}

		@JavascriptInterface
		public void jsMethod(final String result) {
			KeelLog.e("----jsMethod----", "result::" + result);
			if ("".equals(edittext.getText().toString())
					|| edittext.getText().toString() == null) {
				Toast.makeText(NoteActivity.this, "请输入您的笔记标题", 0).show();
			} else if (result == null && "".equals(result)) {
				Toast.makeText(NoteActivity.this, "请输入您的笔记内容", 0).show();
			} else {
				new Thread() {
					public void run() {
						
						Map<String,String> paramMap = new HashMap<String, String>();
						paramMap.put("activityId", activityId);
						paramMap.put("title", edittext.getText().toString());
						paramMap.put("content",result.replace("\"", "\'"));
						String param = Utils.simpleMapToJsonStr(paramMap);
						KeelLog.e(TAG, "jsMethod param::" + param);
						String result = HttpRequestUtil.sendPost(
								Constant.addNotePath, param, NoteActivity.this);
						KeelLog.e(TAG, "jsMethod result::" + result);
						ServerResultDao dao = Utils.getServerResult(result);
						if (dao.getResponseData() != null
								&& !"".equals(dao.getResponseData())
								&& !"null".equals(dao.getResponseData())) {
							NoteDao noteDao = new NoteDao();
							noteDao = addNote(dao.getResponseData());
							if (noteDao != null) {
								msgHandler.sendEmptyMessage(3);
							} else {
								Message message = new Message();
								message.what = 4;
								message.obj = "保存笔记失败,请重试！";
								msgHandler.sendMessage(message);
							}
						} else {
							Message message = new Message();
							message = new Message();
							message.what = 4;
							message.obj = "保存笔记失败，失败原因：" + dao.getNotifyInfo()
									+ ",请重试！";
							msgHandler.sendMessage(message);
						}
					};
				}.start();
			}
		}

	}

	public NoteDao addNote(String responseData) {
		try {
			JSONObject job = new JSONObject(responseData);
			String note = job.getString("note");
			Gson gson = new Gson();
			NoteDao dao = gson.fromJson(note, NoteDao.class);
			return dao;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return null;
	}

	
}
