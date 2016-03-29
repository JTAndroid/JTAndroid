package com.tongmeng.alliance.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tongmeng.alliance.dao.MyNoteList;
import com.tongmeng.alliance.util.Constant;
import com.tongmeng.alliance.util.HttpRequestUtil;
import com.tongmeng.alliance.util.SharedPreferencesUtils;
import com.tongmeng.alliance.util.Utils;
import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.utils.http.EAPIConsts.handler;
import com.utils.log.KeelLog;

/**
 * 我的笔记
 * 
 * @author Administrator
 * 
 */
public class MyNotesActivity extends JBaseActivity implements OnClickListener ,IXListViewListener{

	private ImageView search;
	private TextView myTitle;
	private TextView create_Tv;

	private XListView listview;
	private ImageView search_delete;
	private TextView seach_summint;
	private EditText search_note;

	int pag = 0;
	int pagsearch = 0;
	List<MyNoteList> list_note = new ArrayList<MyNoteList>();
	List<MyNoteList> adapterList_note = new ArrayList<MyNoteList>();
	Map<String, String> map = new HashMap<String, String>();
	boolean isChanged = true;
	MyNoteList myNoteList;
	MyNoteListAdapter mynote_adapter;
	int mposition;
	Map<String, String> mapsearch = new HashMap<String, String>();
	List<MyNoteList> list_searchnote = new ArrayList<MyNoteList>();
	List<MyNoteList> adapterList_searchnote = new ArrayList<MyNoteList>();

	Handler msgHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.arg1) {
			case 0:
				setAdapter();
				listview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						mposition = position;
						mynote_adapter.setChoic_position(position);
						mynote_adapter.notifyDataSetChanged();
					}
				});
				break;
			case 1:
				// Toast.makeText(getApplicationContext(),
				// "请求页码"+map.get("page"), 1).show();
				// Toast.makeText(getApplicationContext(),
				// "请求条数"+map.get("size"), 1).show();
				adapterList_note.clear();
				adapterList_note.addAll(list_note);
				mynote_adapter.notifyDataSetChanged();
				listview.stopRefresh();
				listview.stopLoadMore();
				break;
			case 2:
				setSearchAdapter();
				break;
			case 3:
				adapterList_searchnote.clear();
				adapterList_searchnote.addAll(list_searchnote);
				mynote_adapter.notifyDataSetChanged();
				listview.stopRefresh();
				listview.stopLoadMore();
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
		myTitle.setText("我的笔记");
		create_Tv = (TextView) mCustomView.findViewById(R.id.create_Tv);
		create_Tv.setText("");
		create_Tv.setBackgroundResource(R.drawable.ic_actionbar_more);
		create_Tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyNotesActivity.this,
						CreateNoteActivity.class);
				startActivity(intent);
			}
		});
		search = (ImageView) mCustomView.findViewById(R.id.titleIv);
		search.setVisibility(View.GONE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mynote_list);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		listview = (XListView) findViewById(R.id.listnote);
		search_delete = (ImageView) findViewById(R.id.search_note_delete);
		seach_summint = (TextView) findViewById(R.id.serach_note_summint);
		search_delete.setOnClickListener(this);
		seach_summint.setOnClickListener(this);
	}

	// 设置listview的adapter数据
	private void setAdapter() {
		adapterList_note.clear();
		adapterList_note.addAll(list_note);
		mynote_adapter = new MyNoteListAdapter(MyNotesActivity.this,
				adapterList_note);
		listview.setAdapter(mynote_adapter);
		mynote_adapter.notifyDataSetChanged();
		isChanged = false;
		listview.setXListViewListener(this);
	}

	private void setSearchAdapter() {
		adapterList_searchnote.clear();
		adapterList_searchnote.addAll(list_searchnote);
		mynote_adapter = new MyNoteListAdapter(MyNotesActivity.this,
				adapterList_searchnote);
		listview.setAdapter(mynote_adapter);
		mynote_adapter.notifyDataSetChanged();
		isChanged = false;
		listview.setXListViewListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		SharedPreferencesUtils.setParam(getApplicationContext(), "noteList",
				"0");

		search_note.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				search_delete.setVisibility(View.VISIBLE);
				seach_summint.setVisibility(View.VISIBLE);
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				search_delete.setVisibility(View.VISIBLE);
				seach_summint.setVisibility(View.VISIBLE);
			}
		});
		search_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				search_note.setText("");
				search_delete.setVisibility(View.GONE);
				seach_summint.setVisibility(View.GONE);
				SharedPreferencesUtils.setParam(getApplicationContext(),
						"noteList", "0");
				map.put("page", "0");
				map.put("size", "10");

				getMyNote();
			}
		});
		seach_summint.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * "keyword":"关键字", "page":"页数 从0开始", "size":"每页显示条数"
				 */
				SharedPreferencesUtils.setParam(getApplicationContext(),
						"noteList", "1");
				mapsearch.put("activityId", "");
				mapsearch.put("keyword", search_note.getText().toString());

				mapsearch.put("page", "0");

				mapsearch.put("size", "10");
				getSearchMyNote();

			}
		});
	}
	
	private void getSearchMyNote() {
		new Thread() {
			public void run() {
//				System.out.println("执行的代码");
//				String path = "http://www.etongmeng.com/server/note/getMyNotes.json";
				// String params = JSONUtil.setMyCreateActivitis("0", "10");
				String params = Utils.simpleMapToJsonStr(mapsearch);
				KeelLog.e(TAG, "params::"+params);
				String result = HttpRequestUtil.sendPost(Constant.mynotePath, params,
						MyNotesActivity.this);
				KeelLog.e(TAG, "result::"+result);

				try {

					JSONObject rev = new JSONObject(result);
					JSONObject responseData = rev.getJSONObject("responseData");
					// System.out.print("获取我的笔记" + responseData);
					int count = responseData.getInt("count");

					JSONArray array = responseData.getJSONArray("noteList");
					list_searchnote.clear();
					adapterList_searchnote.clear();
					for (int i = 0, n = array.length(); i < n; i++) {
						/*
						 * /* "id":笔记ID, "content":"笔记内容", "isTop":是否置顶 0:默认状态
						 * 1:置顶, "createTime":"创建时间 格式如2016-01-05 11:28:48"
						 */
						
						myNoteList = new MyNoteList();
						JSONObject obj = array.getJSONObject(i);
						int id = obj.getInt("id");
						String content = obj.getString("content");
						int isTop = obj.getInt("isTop");
						String title = obj.getString("title");
						String createTime = obj.getString("createTime");
						String contentnew = content.replace("<p>", "")
								.replace("</p>", "").replace("<br/>", "");

						myNoteList.setId(id);
						myNoteList.setContent(contentnew);
						myNoteList.setIsTop(isTop);
						myNoteList.setCreateTime(createTime);
						myNoteList.setTitle(title);
						list_searchnote.add(myNoteList);

					}

					JSONObject notification = rev.getJSONObject("notification");
					String notifyCode = notification.getString("notifyCode");
					String notifyInfo = notification.getString("notifyInfo");

					if (msgHandler != null) {
						Message msg = msgHandler.obtainMessage();
						msg.arg1 = 2;
						// msg.obj = message;
						msgHandler.sendMessage(msg);

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			};
		}.start();

	}

	private void getMyNote() {
		new Thread() {
			public void run() {
				// String path =
				// "http://www.etongmeng.com/server/note/getMyNotes.json";
				// String params = JSONUtil.setMyCreateActivitis("0", "10");
				String params = Utils.simpleMapToJsonStr(map);
				KeelLog.e(TAG, "params::" + params);
				String result = HttpRequestUtil.sendPost(Constant.mynotePath,
						params, MyNotesActivity.this);
				KeelLog.e(TAG, "result::" + result);

				try {
					JSONObject rev = new JSONObject(result);
					JSONObject responseData = rev.getJSONObject("responseData");
					System.out.print("获取我的笔记" + responseData);
					int count = responseData.getInt("count");

					JSONArray array = responseData.getJSONArray("noteList");
					for (int i = 0, n = array.length(); i < n; i++) {
						/*
						 * /* "id":笔记ID, "content":"笔记内容", "isTop":是否置顶 0:默认状态
						 * 1:置顶, "createTime":"创建时间 格式如2016-01-05 11:28:48"
						 */

						myNoteList = new MyNoteList();
						JSONObject obj = array.getJSONObject(i);
						int id = obj.getInt("id");
						String content = obj.getString("content");
						int isTop = obj.getInt("isTop");
						String title = obj.getString("title");
						String createTime = obj.getString("createTime");
						String contentnew = content.replace("<p>", "")
								.replace("</p>", "").replace("<br/>", "");

						myNoteList.setId(id);
						myNoteList.setContent(content);
						myNoteList.setIsTop(isTop);
						myNoteList.setCreateTime(createTime);
						myNoteList.setTitle(title);
						list_note.add(myNoteList);

					}

					JSONObject notification = rev.getJSONObject("notification");
					String notifyCode = notification.getString("notifyCode");
					String notifyInfo = notification.getString("notifyInfo");

					if (msgHandler != null) {
						Message msg = msgHandler.obtainMessage();
						msg.arg1 = 0;
						// msg.obj = message;
						msgHandler.sendMessage(msg);

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			};
		}.start();

	}

	class MyNoteListAdapter extends BaseAdapter {

		Context context;
		List<MyNoteList> list;
		Dialog dialog;
		View layout;
		private LayoutInflater mInflater;
		ViewHolder holder;
		String notifyInfo;
		public int choic_position;
		private DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.default_picture_liebiao) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.default_picture_liebiao) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.default_picture_liebiao) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.build();

		public MyNoteListAdapter(Context context, List<MyNoteList> list) {
			this.context = context;
			this.list = list;
			this.mInflater = LayoutInflater.from(context);
			dialog = new Dialog(context, R.style.MyDialogStyle);
		}

		public int getChoic_position() {
			return choic_position;
		}

		public void setChoic_position(int choic_position) {
			this.choic_position = choic_position;
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

				convertView = mInflater
						.inflate(R.layout.mynote_list_item, null);
				holder.title = (TextView) convertView
						.findViewById(R.id.mynote_title);
				holder.content = (TextView) convertView
						.findViewById(R.id.mynote_content);
				holder.time = (TextView) convertView
						.findViewById(R.id.mynote_time);
				holder.note_item = (RelativeLayout) convertView
						.findViewById(R.id.note_item);
				holder.img_note = (ImageView) convertView
						.findViewById(R.id.mynote_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.note_item.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					LayoutInflater inflater = ((Activity) context)
							.getLayoutInflater();
					View layout = inflater.inflate(R.layout.dialog_judgment,
							(ViewGroup) ((Activity) context)
									.findViewById(R.id.set_dialog_judgment));
					dialog.setContentView(layout);

					WindowManager m = ((Activity) context).getWindowManager();
					Display d = m.getDefaultDisplay(); // Ϊ��ȡ��Ļ�?��
					Window dialogWindow = dialog.getWindow();
					android.view.WindowManager.LayoutParams p = dialogWindow
							.getAttributes(); // ��ȡ�Ի���ǰ�Ĳ���ֵ
					p.width = (int) (d.getWidth()); // �������Ϊ��Ļ�Ŀ�
					p.height = (int) (1 * d.getHeight() / 4);
					dialogWindow.setGravity(Gravity.CENTER);
					dialog.getWindow().setAttributes(p); // ������Ч
					dialog.show();
					Button cancle = (Button) layout.findViewById(R.id.cancle);
					Button summint = (Button) layout.findViewById(R.id.summint);
					TextView title_set = (TextView) layout
							.findViewById(R.id.title_set);
					title_set.setText("删除笔记");
					cancle.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
					summint.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							String idDelete = String.valueOf(list.get(position)
									.getId());
							deleteNote(idDelete);

						}
					});
					return false;
				}
			});
			holder.note_item.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent notedetail = new Intent(context,
							MyNotedetailActivity.class);
					notedetail.putExtra("title", list.get(position).getTitle());
					notedetail.putExtra("content", list.get(position)
							.getContent());
					notedetail.putExtra("id",
							String.valueOf(list.get(position).getId()));
					notedetail.putExtra("isTop",
							String.valueOf(list.get(position).getIsTop()));
					File file = new File("/sdcard/myhtml/note.html");

					try {
						// 创建文件夹及文件
						boolean created = createFile(file);
						System.out.println(created ? "File created"
								: "File exists, not created.");

						String content = list.get(position).getContent();
						String html = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' /></head><body>"
								+ content + "</body></html>";

						OutputStreamWriter out = new OutputStreamWriter(
								new FileOutputStream(file), "UTF-8");
						out.write(html.toCharArray());
						out.flush();
						out.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					context.startActivity(notedetail);
					((Activity) context).finish();

				}
			});
			holder.title.setText(list.get(position).getTitle());
			String contentsub = list.get(position).getContent();
			if (contentsub.contains("<")) {
				String contentTitle = contentsub.substring(0,
						contentsub.indexOf("<"));
				String contentimg = contentsub.substring(
						contentsub.indexOf("src") + 5,
						contentsub.indexOf("style") - 2);

				ImageLoader.getInstance().displayImage(contentimg,
						holder.img_note, options);
				holder.content.setText(contentTitle);
			} else {
				holder.content.setText(contentsub);
			}

			// holder.content.setText(content);

			holder.time.setText(list.get(position).getCreateTime());
			return convertView;
		}

		public boolean createFile(File file) throws IOException {
			if (!file.exists()) {
				makeDir(file.getParentFile());
			}
			return file.createNewFile();
		}

		public void makeDir(File dir) {
			if (!dir.getParentFile().exists()) {
				makeDir(dir.getParentFile());
			}
			dir.mkdirs();
		}

		class ViewHolder {
			TextView title, content, time;
			RelativeLayout note_item;
			ImageView img_note;
		}

		protected void deleteNote(final String id) {
			// TODO Auto-generated method stub
			new Thread() {
				public void run() {
					System.out.print("AccountUserAdapter设置默认账户状态id" + id);
					String path = "http://www.etongmeng.com/server/note/delete.json";

					String params = "{\"id\":\"" + id + "\"}";
					String result = HttpRequestUtil.sendPost(path, params,
							context);
					System.out.print("删除笔记输出" + result);
					try {
						JSONObject rev = new JSONObject(result);
						System.out.print("删除笔记输出rev" + rev);

						JSONObject responseData = rev
								.getJSONObject("responseData");
						String succeed = responseData.getString("succeed");
						System.out.print("删除笔记输出succeed" + succeed);

						JSONObject notification = rev
								.getJSONObject("notification");
						String notifyCode = notification
								.getString("notifyCode");
						String notifyInfo = notification
								.getString("notifyInfo");
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

		Handler msgHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.arg1) {
				case 1:
					Toast.makeText(context, "删除成功", 1).show();
					dialog.dismiss();
					list.remove(choic_position);
					notifyDataSetChanged();
					break;
				case 2:

					break;
				default:
					break;
				}

			}
		};
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if (SharedPreferencesUtils.getParam(getApplicationContext(),
				"noteList", "").equals("0")) {
			map.put("page", "0");
			map.put("size", 10 + pag * 10 + "");
			list_note.clear();
			getLoadMyNote();
		} else if (SharedPreferencesUtils.getParam(getApplicationContext(),
				"noteList", "").equals("1")) {
			// Toast.makeText(getApplicationContext(), "沒有更多了", 1).show();
			mapsearch.put("page", "0");
			mapsearch.put("size", 10 + pag * 10 + "");
			list_searchnote.clear();
			getLoadSearchMyNote();
		}
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		if (SharedPreferencesUtils.getParam(getApplicationContext(),
				"noteList", "").equals("0")) {
			pag = pag + 1;
			map.put("page", pag + "");
			// map.put("size", 10 + pag * 10 + "");
			Log.e("", "当前页数::" + pag);
			getLoadMyNote();
		}

		else if (SharedPreferencesUtils.getParam(getApplicationContext(),
				"noteList", "").equals("1")) {
			// Toast.makeText(getApplicationContext(), "沒有更多了", 1).show();
			pagsearch = pagsearch + 1;
			mapsearch.put("page", pagsearch + "");
//			list_searchnote.clear();
			getLoadSearchMyNote();
		}
	}
	
	private void getLoadMyNote() {
		new Thread() {
			public void run() {
				System.out.println("执行的代码");
//				String path = "http://www.etongmeng.com/server/note/getMyNotes.json";
				String params = Utils.simpleMapToJsonStr(map);
				KeelLog.e(TAG, "params::"+params);
				String result = HttpRequestUtil.sendPost(Constant.mynotePath, params,
						MyNotesActivity.this);
				KeelLog.e(TAG, "result::"+result);
				try {
					JSONObject rev = new JSONObject(result);
					JSONObject responseData = rev.getJSONObject("responseData");
					System.out.print("获取我的笔记" + responseData);

					if (responseData.isNull("noteList")) {
						list_note = list_note;
						// 这就是null的情况
					} else {
						// 不为null，正常解析
						list_note = getmyNote(result, list_note);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// list_note = Utils.getmyNote(result, list_note);

				if (msgHandler != null) {
					Message msg = msgHandler.obtainMessage();
					msg.arg1 = 1;
					// msg.obj = message;
					msgHandler.sendMessage(msg);

				}

			};
		}.start();

	}
	
	private void getLoadSearchMyNote() {
		new Thread() {
			public void run() {
				System.out.println("执行的代码");
//				http://www.etongmeng.com/server/note/getMyNotes.json
//				String path = "http://www.etongmeng.com/server/note/getMyNotes.json";
				String params = Utils.simpleMapToJsonStr(mapsearch);
				KeelLog.e(TAG, "params::"+params);
				String result = HttpRequestUtil.sendPost(Constant.mynotePath, params,
						MyNotesActivity.this);
				KeelLog.e(TAG, "result::"+result);
				try {
					JSONObject rev = new JSONObject(result);
					JSONObject responseData = rev.getJSONObject("responseData");

					if (responseData.isNull("noteList")) {
						list_searchnote = list_searchnote;
						// 这就是null的情况
					} else {
						// 不为null，正常解析
						list_searchnote = getmyNote(result,
								list_searchnote);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (msgHandler != null) {
					Message msg = msgHandler.obtainMessage();
					msg.arg1 = 3;
					msgHandler.sendMessage(msg);

				}

			};
		}.start();

	}
	
	
	public  List<MyNoteList> getmyNote(String result,
			List<MyNoteList> list) {
		if (list.size() == 0) {
			list = new ArrayList<MyNoteList>();
		}
		try {
			Log.e("", "获取信息：：" + getContent(result));
			JSONArray jsonArray = new JSONObject(getContent(result))
					.getJSONArray("noteList");
			for (int i = 0; i < jsonArray.length(); i++) {
				Gson gson = new Gson();
				MyNoteList myNoteList = gson.fromJson(jsonArray.opt(i)
						.toString(), MyNoteList.class);
				list.add(myNoteList);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}
	
	public  String getContent(String result) {
		try {
			JSONObject obj = new JSONObject(result);
			String notification = obj.getString("notification");
			if (TextUtils.equals("0001",
					new JSONObject(notification).getString("notifyCode"))) {
				return obj.getString("responseData");
			} else {
				return new JSONObject(notification).getString("notifyInfo");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
