package com.tongmeng.alliance.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.tongmeng.alliance.util.HttpRequestUtil;
import com.tongmeng.alliance.util.Utils;
import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.utils.log.KeelLog;

public class MyNotedetailActivity extends JBaseActivity {

	private ImageView search;
	private TextView myTitle;
	private TextView create_Tv;

	TextView notedetail_tv;
	ImageView note_menu, note_iv_back;
	Dialog dialog;
	// View layout;
	String id;
	String notedetail;
	String notifyInfo;
	private PopupWindow popupWindow;
	private LinearLayout layout;
	private ListView listView;
	PopuWindowAdapter popuAdapter;
	List<String> list;
	String isTop;
	String content;
	// RelativeLayout note_header;
	String content_update;
	private static WebView myWebView;
	private static final String IMAGE_PATH = "/sdcard/myhtml/";
	TextView update_note;
	String title;

	Handler msgHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.arg1) {
			case 1:
				Toast.makeText(getApplicationContext(), "成功", 1).show();
				dialog.dismiss();
				finish();
				break;
			case 2:
				Toast.makeText(getApplicationContext(), notifyInfo, 1).show();
				break;
			case 3:
				Toast.makeText(getApplicationContext(), "删除成功", 1).show();
				finish();
				break;
			case 4:
				Toast.makeText(getApplicationContext(), "刷新成功", 1).show();
				// notedetail_tv.setText(content_update);
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
		setContentView(R.layout.note_detail);
		dialog = new Dialog(this, R.style.MyDialogStyle);
		dialog = new Dialog(this, R.style.MyDialogStyle);
		notedetail_tv = (TextView) findViewById(R.id.notedetail_tv);
		// Toast.makeText(getApplicationContext(), id, 1).show();
		// notedetail_tv.setText(notedetail);
		notedetail = getIntent().getExtras().getString("content");
		id = getIntent().getExtras().getString("id");
		isTop = getIntent().getExtras().getString("isTop");
		title = getIntent().getExtras().getString("title");
		myWebView = (WebView) findViewById(R.id.note_webview);
		myWebView.getSettings().setJavaScriptEnabled(true);
		myWebView.getSettings().setBuiltInZoomControls(true);
		myWebView.getSettings().setSavePassword(false);
		// 支持多种分辨率，需要js网页支持
		myWebView.getSettings().setUserAgentString("mac os");
		myWebView.getSettings().setDefaultTextEncodingName("utf-8");
		myWebView.loadUrl("file:///mnt/sdcard/myhtml/note.html");
		// myWebView.loadUrl("file:///android_asset/note.html");
		// myWebView.loadUrl("/sdcard/myhtml/note.html");
	}

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
		myTitle.setText("查看笔记");
		create_Tv = (TextView) mCustomView.findViewById(R.id.create_Tv);
		create_Tv.setText("修改");
		create_Tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyNotedetailActivity.this,
						UpdateNoteActivity.class);
				intent.putExtra("id", id);
				intent.putExtra("content", notedetail);
				intent.putExtra("title", title);
				startActivity(intent);
				finish();
			}
		});
		search = (ImageView) mCustomView.findViewById(R.id.titleIv);
		search.setBackgroundResource(R.drawable.ic_action_overflow);
		search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int x = getWindowManager().getDefaultDisplay().getWidth() * 4 / 5;
				showPopupWindow(x, 98);
			}
		});
	}
	
	public void showPopupWindow(int x, int y) {
		layout = (LinearLayout) LayoutInflater.from(MyNotedetailActivity.this)
				.inflate(R.layout.dialog, null);
		listView = (ListView) layout.findViewById(R.id.lv_dialog);
		list = new ArrayList<String>();
		list.add("刷新");
		if (isTop.equals("1")) {
			list.add("取消置顶");
		} else {
			list.add("置顶");
		}

		list.add("删除");
		popuAdapter = new PopuWindowAdapter(this, list);
		listView.setAdapter(popuAdapter);

		popupWindow = new PopupWindow(MyNotedetailActivity.this);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow
				.setWidth(getWindowManager().getDefaultDisplay().getWidth() / 3);
		popupWindow.setHeight(200);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setContentView(layout);
		// showAsDropDown会把里面的view作为参照物，所以要那满屏幕parent
		// popupWindow.showAsDropDown(findViewById(R.id.tv_title), x, 10);
		popupWindow.showAtLocation(findViewById(R.id.main), Gravity.LEFT
				| Gravity.TOP, x, y);// 需要指定Gravity，默认情况是center.

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (list.get(arg2).equals("刷新")) {
					// Toast.makeText(getApplicationContext(), "点击了刷新",
					// 1).show();
					String pathupdate = "http://www.etongmeng.com/server/note/getNote.json";
					SetupdateNote(id, pathupdate);

				} else if (list.get(arg2).equals("删除")) {
					// Toast.makeText(getApplicationContext(), "点击了删除",
					// 1).show();
					String pathdelete = "http://www.etongmeng.com/server/note/delete.json";
					SetDeleteNote(id, pathdelete);
				} else if (list.get(arg2).equals("置顶")) {
					// Toast.makeText(getApplicationContext(), "点击了置顶",
					// 1).show();
					String pathTop = "http://www.etongmeng.com/server/note/top.json";

					SetNote(id, pathTop);

				} else if (list.get(arg2).equals("取消置顶")) {
					setNotTop(id);
				}
				popupWindow.dismiss();
				popupWindow = null;
			}
		});
	}
	
	protected void setNotTop(String id2) {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				System.out.print("AccountUserAdapter设置默认账户状态id" + id);
				String path = "http://www.etongmeng.com/server/note/cancelTop.json";

				String params = "{\"id\":\""+id+"\"}";
				String result = HttpRequestUtil.sendPost(path, params,
						MyNotedetailActivity.this);
				System.out.print("删除笔记输出" + result);
				try {
					JSONObject rev = new JSONObject(result);
					System.out.print("删除笔记输出rev" + rev);
					JSONObject notification = rev.getJSONObject("notification");
					String notifyCode = notification.getString("notifyCode");
					notifyInfo = notification.getString("notifyInfo");
					if (notifyCode.equals("0001")) {
						if (msgHandler != null) {
							Message msg = msgHandler.obtainMessage();
							msg.arg1 = 1;

							msgHandler.sendMessage(msg);
						}

					} else {
						if (msgHandler != null) {
							Message msg = msgHandler.obtainMessage();
							msg.arg1 = 2;

							msgHandler.sendMessage(msg);
						}
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			};
		}.start();
	}
	
	protected void SetNote(final String id, final String path) {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				System.out.print("AccountUserAdapter设置默认账户状态id" + id);
				// String path =
				// "http://www.etongmeng.com/server/note/delete.json";

				String params = "{\"id\":\""+id+"\"}";
				String result = HttpRequestUtil.sendPost(path, params,
						MyNotedetailActivity.this);
				System.out.print("删除笔记输出" + result);
				try {
					JSONObject rev = new JSONObject(result);
					System.out.print("删除笔记输出rev" + rev);
					JSONObject notification = rev.getJSONObject("notification");
					String notifyCode = notification.getString("notifyCode");
					notifyInfo = notification.getString("notifyInfo");
					if (notifyCode.equals("0001")) {
						if (msgHandler != null) {
							Message msg = msgHandler.obtainMessage();
							msg.arg1 = 1;

							msgHandler.sendMessage(msg);
						}

					} else {
						if (msgHandler != null) {
							Message msg = msgHandler.obtainMessage();
							msg.arg1 = 2;

							msgHandler.sendMessage(msg);
						}
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			};
		}.start();
	}
	
	protected void SetDeleteNote(final String id, final String path) {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				System.out.print("AccountUserAdapter设置默认账户状态id" + id);
				// String path =
				// "http://www.etongmeng.com/server/note/delete.json";

				String params = "{\"id\":\""+id+"\"}";
				String result = HttpRequestUtil.sendPost(path, params,
						MyNotedetailActivity.this);
				System.out.print("删除笔记输出" + result);
				try {
					JSONObject rev = new JSONObject(result);
					System.out.print("删除笔记输出rev" + rev);

					JSONObject responseData = rev.getJSONObject("responseData");
					String succeed = responseData.getString("succeed");
					System.out.print("删除笔记输出succeed" + succeed);

					JSONObject notification = rev.getJSONObject("notification");
					String notifyCode = notification.getString("notifyCode");
					notifyInfo = notification.getString("notifyInfo");
					if (notifyCode.equals("0001")) {
						if (msgHandler != null) {
							Message msg = msgHandler.obtainMessage();
							msg.arg1 = 3;

							msgHandler.sendMessage(msg);
						}

					} else {
						if (msgHandler != null) {
							Message msg = msgHandler.obtainMessage();
							msg.arg1 = 2;

							msgHandler.sendMessage(msg);
						}
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			};
		}.start();
	}
	
	protected void SetupdateNote(final String id, final String path) {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				System.out.print("AccountUserAdapter设置默认账户状态id" + id);
				// String path =
				// "http://www.etongmeng.com/server/note/delete.json";

				String params = "{\"id\":\""+id+"\"}";
				String result = HttpRequestUtil.sendPost(path, params,
						MyNotedetailActivity.this);
				System.out.print("删除笔记输出" + result);
				try {
					JSONObject rev = new JSONObject(result);
					System.out.print("删除笔记输出rev" + rev);

					JSONObject responseData = rev.getJSONObject("responseData");
					JSONObject note = responseData.getJSONObject("note");
					String contentnew = note.getString("content");
					content_update = contentnew.replace("<p>", "")
							.replace("</p>", "").replace("<br/>", "");

					JSONObject notification = rev.getJSONObject("notification");
					String notifyCode = notification.getString("notifyCode");
					notifyInfo = notification.getString("notifyInfo");
					if (notifyCode.equals("0001")) {
						if (msgHandler != null) {
							Message msg = msgHandler.obtainMessage();
							msg.arg1 = 4;

							msgHandler.sendMessage(msg);
						}

					} else {
						if (msgHandler != null) {
							Message msg = msgHandler.obtainMessage();
							msg.arg1 = 2;

							msgHandler.sendMessage(msg);
						}
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			};
		}.start();
	}

	class PopuWindowAdapter extends BaseAdapter {
		List<String> list;
		Context context;
		ViewHolder holder;
		private LayoutInflater mInflater;

		public PopuWindowAdapter(Context context, List<String> list) {
			this.context = context;
			this.list = list;
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			// TODO Auto-generated method stub
			holder = new ViewHolder();
			if (convertView == null) {

				convertView = mInflater.inflate(R.layout.tm_pop, null);
				holder.tv = (TextView) convertView.findViewById(R.id.popshow);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv.setText(list.get(position));

			return convertView;
		}

		class ViewHolder {
			TextView tv;
		}
	}
}
