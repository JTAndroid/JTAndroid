package com.tongmeng.alliance.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.google.gson.Gson;
import com.tongmeng.alliance.dao.NoteDao;
import com.tongmeng.alliance.dao.ServerResultDao;
import com.tongmeng.alliance.util.Constant;
import com.tongmeng.alliance.util.HttpRequestUtil;
import com.tongmeng.alliance.util.Utils;
import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.common.view.XListView;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.log.KeelLog;

public class ActionNoteActivity extends JBaseActivity implements
		OnClickListener {

	private ImageView search;
	private TextView myTitle;
	private TextView create_Tv;
	
	String activityId;
	TextView writenoteText;
	XListView listview;
	LinearLayout noresultLayout, listLayout;
	NoteAdapter adapter;
	ProgressBar progressBar;
	List<NoteDao> notesList = new ArrayList<NoteDao>(),
			adapterList = new ArrayList<NoteDao>();
	Map<String, String> map = new HashMap<String, String>();

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if (progressBar.getVisibility() == View.VISIBLE) {
					progressBar.setVisibility(View.GONE);
				}
				if (noresultLayout.getVisibility() == View.VISIBLE) {
					noresultLayout.setVisibility(View.GONE);
				}
				if (listLayout.getVisibility() == View.GONE) {
					listLayout.setVisibility(View.VISIBLE);
				}
				if (adapter != null) {
					listview.stopRefresh();
					listview.stopLoadMore();
					adapter.notifyDataSetChanged();
				} else {
					initListView();
				}
				break;
			case 1:// 失败处理
				Toast.makeText(ActionNoteActivity.this, msg.obj + "", 0).show();
				break;
			case 2:// 删除笔记成功
				if (adapterList.size() != 0) {
					adapterList.clear();
				}
				adapterList.addAll(notesList);
				adapter.notifyDataSetChanged();
				break;
			case 3:
				if (progressBar.getVisibility() == View.VISIBLE) {
					progressBar.setVisibility(View.GONE);
				}
				if (listLayout.getVisibility() == View.VISIBLE) {
					listLayout.setVisibility(View.GONE);
				}
				if (noresultLayout.getVisibility() == View.GONE) {
					noresultLayout.setVisibility(View.VISIBLE);
				}
				break;
			case 6:

				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actionnote);
		activityId = getIntent().getStringExtra("activityId");
		initView();
		initValue();
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
		myTitle.setText("我的笔记");
		create_Tv = (TextView) mCustomView.findViewById(R.id.create_Tv);
		create_Tv.setText("");
		create_Tv.setBackgroundResource(R.drawable.ic_actionbar_more);
		create_Tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ActionNoteActivity.this, NoteActivity.class);
				intent.putExtra("activityId", activityId);
				startActivity(intent);
			}
		});
		search = (ImageView) mCustomView.findViewById(R.id.titleIv);
		search.setVisibility(View.GONE);
		
	}

	private void initTitle() {
		// TODO Auto-generated method stub
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "笔记",
				false, null, false, true);
	}

	public void initView() {
		// 界面控件
		noresultLayout = (LinearLayout) findViewById(R.id.actionnote_nonotelayout);
		listLayout = (LinearLayout) findViewById(R.id.actionnote_listlayout);
		writenoteText = (TextView) findViewById(R.id.actionnote_writenoteText);
		writenoteText.setOnClickListener(this);
		progressBar = (ProgressBar) findViewById(R.id.actionnote_progressBar);

		listview = (XListView) findViewById(R.id.actionnote_listview);

		map = getMap();
	}

	public void initValue() {
		new Thread() {
			public void run() {
				String param = simpleMapToJsonStr(map);
				KeelLog.e(TAG, "initValue  param::" + param);
				String result = HttpRequestUtil.sendPost(Constant.mynotePath,
						param, ActionNoteActivity.this);
				KeelLog.e(TAG, "initValue  result::" + result);
				ServerResultDao dao = Utils.getServerResult(result);

				if (dao.getNotifyCode().equals("0001")) {
					if (dao.getResponseData() != null
							|| !"".equals(dao.getResponseData())
							|| !"null".equals(dao.getResponseData())) {
						List<NoteDao> tempList = getNotesList(dao
								.getResponseData());
						KeelLog.e(TAG, "notesList笔记：：" + notesList);
						if (tempList != null && tempList.size() > 0) {

							notesList.addAll(tempList);
							handler.sendEmptyMessage(0);
						} else {
							Message message = new Message();
							message.what = 3;
							message.obj = "获取笔记列表失败";
							handler.sendMessage(message);
						}
					} else {
						Message message = new Message();
						message.what = 1;
						message.obj = "获取笔记列表失败";
						handler.sendMessage(message);
					}
				} else {
					Message message = new Message();
					message.what = 1;
					message.obj = "获取笔记列表失败，失败原因：" + dao.getNotifyInfo()
							+ ",请重试";
					handler.sendMessage(message);
				}
			};
		}.start();
	}

	public void initListView() {
		if (listLayout.getVisibility() == View.GONE) {
			listLayout.setVisibility(View.VISIBLE);
		}

		if (adapterList != null && adapterList.size() > 0) {
			adapterList.clear();
		}
		adapterList.addAll(notesList);

		adapter = new NoteAdapter(adapterList);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent noteDetailIntent = new Intent(ActionNoteActivity.this,
						MyNotedetailActivity.class);
				noteDetailIntent.putExtra("id", notesList.get(position - 1)
						.getId() + "");
				noteDetailIntent.putExtra("title", notesList.get(position - 1)
						.getTitle());
				noteDetailIntent.putExtra("content", notesList
						.get(position - 1).getContent().replace("\'", "\""));
				noteDetailIntent.putExtra("isTop", notesList.get(position - 1)
						.getIsTop());
				File file = new File("/sdcard/myhtml/note.html");

				try {
					// 创建文件夹及文件
					boolean created = Utils.createFile(file);
					String content = notesList.get(position - 1).getContent()
							.replace("\'", "\"");
					String html = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' /></head><body>"
							+ content + "</body></html>";

					OutputStreamWriter out = new OutputStreamWriter(
							new FileOutputStream(file), "UTF-8");
					out.write(html.toCharArray());
					out.flush();
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				startActivity(noteDetailIntent);
			}
		});
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				KeelLog.e(TAG, "第" + position + "条笔记要被删除了！！");
				new Thread() {
					public void run() {
						String param = "{\"id\":\""
								+ notesList.get(position).getId() + "\"}";
						String result = HttpRequestUtil.sendPost(
								Constant.deleteNotePath, param,
								ActionNoteActivity.this);
						ServerResultDao dao = Utils.getServerResult(result);
						if (dao.getNotifyCode().equals("0001")) {

							if (dao.getResponseData() != null
									&& !"".equals(dao.getResponseData())
									&& !"null".equals(dao.getResponseData())) {
								boolean isDelete = deleteNote(dao
										.getResponseData());
								if (isDelete) {
									notesList.remove(position);
									handler.sendEmptyMessage(2);
								} else {

									Message message = new Message();
									message.what = 1;
									message.obj = "删除笔记失败,请重试!";
									handler.sendMessage(message);
								}
							} else {
								Message message = new Message();
								message = new Message();
								message.what = 1;
								message.obj = "删除笔记失败,请重试!";
								handler.sendMessage(message);
							}
						} else {
							Message message = new Message();
							message = new Message();
							message.what = 1;
							message.obj = "删除笔记失败，失败原因：" + dao.getNotifyInfo()
									+ ",请重试!";
							handler.sendMessage(message);
						}
					};
				}.start();

				return true;
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.actionnote_writenoteText:
			Intent writeIntent = new Intent(this, NoteActivity.class);
			writeIntent.putExtra("activityId", activityId);
			startActivity(writeIntent);
			break;

		default:
			break;
		}
	}

	class NoteAdapter extends BaseAdapter {
		List<NoteDao> list;

		public NoteAdapter(List<NoteDao> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return position - 1;
		}

		@Override
		public long getItemId(int position) {
			return position - 1;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(ActionNoteActivity.this)
						.inflate(R.layout.meetingnote_item, null);

				viewHolder.title = (TextView) convertView
						.findViewById(R.id.meetingnote_item_titleText);
				viewHolder.time = (TextView) convertView
						.findViewById(R.id.meetingnote_item_timeText);
				viewHolder.content = (TextView) convertView
						.findViewById(R.id.meetingnote_item_contentText);
				viewHolder.img = (ImageView) convertView
						.findViewById(R.id.meetingnote_item_img);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			NoteDao dao = list.get(position);

			// 获取第一张图片的地址，下载并显示
			String content = dao.getContent().replace("\'", "\"");
			/**
			 * <img id="divImage0" src=\
			 * "http://test.etongmeng.com/file//alliance/download?id=560"
			 * style="max-width: 100%; max-height: 200px; ">gal;gnkal <img
			 * id="divImage1" src=\
			 * "http://test.etongmeng.com/file//alliance/download?id=558"
			 * style="max-width: 100%; max-height: 200px; ">
			 */
			String imagePath = null;
			if (content.contains("src=")) {
				String strs = content.substring(content.indexOf("src=\"") + 5,
						content.length());
				imagePath = strs.substring(0, strs.indexOf("\""));
				// MyLog.e(TAG, "getView()  imagePath::"+imagePath);
				// ImageListener listener = ImageLoader.getImageListener(
				// viewHolder.img, R.drawable.default_picture_liebiao,
				// R.drawable.default_picture_liebiao, (int) weight, (int)
				// weight);
				// mImageLoader.get(imagePath, listener);
			} else {
				viewHolder.img.setVisibility(View.GONE);
			}

			// if (content != null && !"".equals(dao.getContent())) {
			// if (content.contains("img id")) {
			// String strs = content.substring(content.indexOf("src=\""));
			// MyLog.e(TAG, "adapter  strs::" + strs);
			// imagePath = strs.substring(0, strs.indexOf("\""));
			// MyLog.e(TAG, "adapter  imagePath::" + imagePath);
			// }
			// }
			// Log.e("", "adapter  dao ::" + dao.toString());
			// ImageListener listener = ImageLoader.getImageListener(
			// viewHolder.img, R.drawable.default_picture_liebiao,
			// R.drawable.default_picture_liebiao, (int) weight, (int) weight);
			// mImageLoader.get(imagePath, listener);

			viewHolder.title.setText(dao.getTitle());
			viewHolder.time.setText(dao.getCreateTime());
			viewHolder.content.setText(getNoteContent(content));
			return convertView;
		}

		class ViewHolder {
			TextView title, time;
			TextView content;
			ImageView img;

		}
	}

	public String getNoteContent(String content) {
		StringBuffer sb = new StringBuffer();
		if (!content.contains("<")) {
			sb.append(content);
		} else {
			if (content.startsWith("<")) {
				content = content.substring(content.indexOf(">") + 1,
						content.length());
			}
			if (content.endsWith(">")) {
				content = content.substring(0, content.lastIndexOf("<"));
			}
			int count = 0;
			if (content.contains("<")) {
				count = content.split("<").length - 1;
			}
			if (count > 0) {
				for (int i = 0; i <= count; i++) {
					if (content.contains("<")) {
						int index1 = content.indexOf("<");
						int index2 = content.indexOf(">");
						String temp1 = content.substring(0, index1);
						sb.append(temp1);
						content = content.substring(index2 + 1,
								content.length());
					} else {
						sb.append(content);
					}
				}
			} else {
				sb.append(content);
			}
		}
		return sb.toString();
	}

	public HashMap<String, String> getMap() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("activityId", activityId);
		map.put("keyword", "");
		map.put("page", "0");
		map.put("size", "10");
		return map;
	}

	/**
	 * 多条数据转换成json
	 * 
	 * @param map
	 * @return
	 */
	public static String simpleMapToJsonStr(Map<String, String> map) {
		if (map == null || map.isEmpty()) {
			return "null";
		}
		String jsonStr = "{";
		Set<?> keySet = map.keySet();
		for (Object key : keySet) {
			jsonStr += "\"" + key + "\":\"" + map.get(key) + "\",";
		}
		jsonStr = jsonStr.substring(0, jsonStr.length() - 1);
		jsonStr += "}";
		return jsonStr;
	}

	// 获取笔记列表
	public List<NoteDao> getNotesList(String responseData) {
		List<NoteDao> list = new ArrayList<NoteDao>();
		try {
			JSONObject job = new JSONObject(responseData);
			String noteListStr = job.getString("noteList");
			if (noteListStr == null || "".equals(noteListStr)
					|| "null".equals(noteListStr)) {
				return null;
			} else {
				JSONArray jarr = job.getJSONArray("noteList");
				for (int i = 0; i < jarr.length(); i++) {
					JSONObject noteObj = (JSONObject) jarr.opt(i);
					KeelLog.e(TAG, "noteObj:::" + noteObj.toString());
					Gson gson = new Gson();
					NoteDao dao = gson.fromJson(noteObj.toString(),
							NoteDao.class);
					KeelLog.e(TAG, "NoteDao::" + dao.toString());
					list.add(dao);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

	// 删除笔记
	public boolean deleteNote(String responseData) {
		try {
			JSONObject job = new JSONObject(responseData);
			return job.getBoolean("succeed");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}
}
