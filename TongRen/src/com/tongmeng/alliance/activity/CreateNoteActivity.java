package com.tongmeng.alliance.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tongmeng.alliance.util.Constant;
import com.tongmeng.alliance.util.HttpRequestUtil;
import com.tongmeng.alliance.util.LoadUtil;
import com.tongmeng.alliance.util.Utils;
import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.utils.log.KeelLog;

public class CreateNoteActivity extends JBaseActivity {

	private ImageView search;
	private TextView myTitle;
	private TextView create_Tv;

	String htmlText, activityDes;
	private static WebView myWebView;
	String filename = null;
	static String url;
	EditText add_note_title;
	Map<String, String> map = new HashMap<String, String>();
	String notifyInfo;
	private String FILE = "saveSetting";
	SharedPreferences sp;
	Editor editor;
	ProgressBar progressBar;

	Handler msgHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				Toast.makeText(getApplicationContext(), "添加笔记成功", 1).show();
				Intent intent = new Intent(CreateNoteActivity.this,
						MyNotesActivity.class);
				startActivity(intent);
				finish();
				break;
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
			case 3:
				Toast.makeText(getApplicationContext(), notifyInfo, 1).show();
				break;
			case 4:
				// myWebView.loadUrl("javascript:getHtml()");
				map.put("activityId", "");
				map.put("title", add_note_title.getText().toString());
				String result_note = sp.getString("result", "").replace("\"",
						"\'");
				// result_note.replace("+" +", "/");
				System.out.print("result_note" + result_note);
				map.put("content", result_note);
				AddNote();
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
		View mCustomView = getLayoutInflater().inflate(
				R.layout.org_firstpage_actionbar_title, null);
		mActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView
				.getLayoutParams();
		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK
				| Gravity.CENTER_HORIZONTAL;
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
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_note);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		sp = this.getSharedPreferences(FILE, this.MODE_PRIVATE);
		editor = sp.edit();
		add_note_title = (EditText) findViewById(R.id.add_note_title);

		progressBar = (ProgressBar) findViewById(R.id.activity_profile_progressBar_add);
		myWebView = (WebView) findViewById(R.id.myWebView_note);
		myWebView.getSettings().setJavaScriptEnabled(true);
		myWebView.getSettings().setBuiltInZoomControls(true);
		myWebView.getSettings().setSavePassword(false);
		// 支持多种分辨率，需要js网页支持
		myWebView.getSettings().setUserAgentString("mac os");
		myWebView.getSettings().setDefaultTextEncodingName("utf-8");
		htmlText = "http://test.etongmeng.com/html5/activity/android_editor.html";
		myWebView.addJavascriptInterface(new JavaScriptinterface(this),
				"android");

		Button btnimage = (Button) findViewById(R.id.add_note_image);
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

	protected void AddNote() {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				System.out.println("执行的代码");
				// String addnotepath =
				// "http://192.168.101.140:8801/note/add.json";
				// String params = JSONUtil.setMyCreateActivitis("0", "10");
				String params = Utils.simpleMapToJsonStr(map);

				String result = HttpRequestUtil.sendPost(Constant.addNotePath,
						params, CreateNoteActivity.this);
				System.out.print("添加笔记输出值" + result);

				try {

					JSONObject rev = new JSONObject(result);
					JSONObject responseData = rev.getJSONObject("responseData");
					System.out.print("获取添加我的笔记" + responseData);
					// int count = responseData.getInt("count");

					// JSONArray array = responseData.getJSONArray("noteList");

					JSONObject notification = rev.getJSONObject("notification");
					String notifyCode = notification.getString("notifyCode");
					notifyInfo = notification.getString("notifyInfo");

					if (notifyCode.equals("0001")) {

						if (msgHandler != null) {
							Message msg = msgHandler.obtainMessage();
							msg.arg1 = 0;
							// msg.obj = message;
							msgHandler.sendMessage(msg);

						}
					} else {
						if (msgHandler != null) {
							Message msg = msgHandler.obtainMessage();
							msg.arg1 = 3;
							// msg.obj = message;
							msgHandler.sendMessage(msg);

						}
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 2:
			try {
				Uri uri = data.getData();
				filename = LoadUtil.getImageAbsolutePath(this, uri);
				if (progressBar.getVisibility() == View.GONE) {
					progressBar.setVisibility(View.VISIBLE);
				}
				getImage();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}

	protected void getImage() {
		// TODO Auto-generated method stub
		new Thread() {

			public void run() {
				String result = LoadUtil.multiPart(filename,
						CreateNoteActivity.this);

				Log.e("", "result::" + result);
				url = LoadUtil.getFileServerInfo(result, "url");
				Log.e("", "url::" + url);
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

	class JavaScriptinterface {

		private Context mContext;

		/** Instantiate the interface and set the context */
		public JavaScriptinterface(Context c) {
			mContext = c;
		}

		@JavascriptInterface
		public void jsMethod(String result) {
			if (!TextUtils.isEmpty(result)) {
				editor.putString("result", result);
				editor.commit();
				if (msgHandler != null) {
					Message msg = msgHandler.obtainMessage();
					msg.what = 4;

					msgHandler.sendMessage(msg);
				}

			}

		}

	}
}
