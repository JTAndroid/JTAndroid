package com.tongmeng.alliance.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tongmeng.alliance.dao.PeopleNumberDao;
import com.tongmeng.alliance.dao.ServerResultDao;
import com.tongmeng.alliance.util.Constant;
import com.tongmeng.alliance.util.HttpRequestUtil;
import com.tongmeng.alliance.util.Utils;
import com.tongmeng.alliance.view.PeopleManagerPopupWindow;
import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.utils.log.KeelLog;

public class PeopleManagerActivity extends JBaseActivity {
	
	private ImageView search;
	private TextView myTitle;
	private TextView create_Tv;

	public String TAG = "---PeopleManagerActivity---";
	String activityId;
	// 头部控件
	ImageView backImg, moreImg;
	TextView titleText, moreText1;

	XListView listview;
	GroupAdapter adapter;
	List<PeopleNumberDao> list = new ArrayList<PeopleNumberDao>(),
			tempList = new ArrayList<PeopleNumberDao>();
	Map<String, String> map = new HashMap<String, String>();
	int page = 0;
	boolean isLoad = false;
	private DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.default_picture_liebiao) // 设置图片下载期间显示的图片
			.showImageForEmptyUri(R.drawable.default_picture_liebiao) // 设置图片Uri为空或是错误的时候显示的图片
			.showImageOnFail(R.drawable.default_picture_liebiao) // 设置图片加载或解码过程中发生错误显示的图片
			.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
			.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
			.build();

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:// 已签到
				page = 0;
				isLoad = false;
				map = getMap();
				map.put("isSignIn", "1");
				map.put("page", page + "");
				initValue();
				break;
			case 1:// 未签到
				page = 0;
				isLoad = false;
				map = getMap();
				map.put("isSignIn", "0");
				map.put("page", page + "");
				initValue();
				break;
			case 2:// 全部
				page = 0;
				isLoad = false;
				map = getMap();
				map.put("isSignIn", "-1");
				map.put("page", page + "");
				initValue();
				break;
			case 3:
				KeelLog.e(TAG, "handler case 3 adapter::" + adapter);
				if (adapter != null) {
					listview.stopRefresh();
					listview.stopLoadMore();
					adapter.notifyDataSetChanged();
				} else {
					initListViewData();// 获取成员信息成功，设置数据
				}
				break;
			case 4:
				listview.stopRefresh();
				listview.stopLoadMore();
				Toast.makeText(PeopleManagerActivity.this, msg.obj + "", 0)
						.show();
				if (isLoad) {
					Toast.makeText(PeopleManagerActivity.this, "当前已无更多数据", 0)
							.show();
				}

				break;
			case 5:

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
		setContentView(R.layout.peoplemanager);
		activityId = getIntent().getStringExtra("activityId");
		initView();
		map = getMap();
		initValue();
	}

	class GroupAdapter extends BaseAdapter {
		private Context context;
		private List<PeopleNumberDao> list;

		public GroupAdapter(Context context, List<PeopleNumberDao> list) {
			// TODO Auto-generated constructor stub
			this.context = context;
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(PeopleManagerActivity.this)
						.inflate(R.layout.peoplemanager_item, null);
				viewHolder.picImg = (ImageView) convertView
						.findViewById(R.id.peoplemanager_item_picImg);
				viewHolder.qunzhuImg = (ImageView) convertView
						.findViewById(R.id.peoplemanager_item_qunzhuImg);
				viewHolder.nameText = (TextView) convertView
						.findViewById(R.id.peoplemanager_item_nameText);
				viewHolder.qiandaoImg = (ImageView) convertView
						.findViewById(R.id.peoplemanager_item_qiandaoImg);
				viewHolder.positionText = (TextView) convertView
						.findViewById(R.id.peoplemanager_item_positionText);
				viewHolder.companyText = (TextView) convertView
						.findViewById(R.id.peoplemanager_item_companyText);
				viewHolder.messageImg = (ImageView) convertView
						.findViewById(R.id.peoplemanager_item_messageImg);
				viewHolder.callImg = (ImageView) convertView
						.findViewById(R.id.peoplemanager_item_callImg);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			PeopleNumberDao dao = list.get(position);
			if (dao.getPicPath() != null && !"".equals(dao.getPicPath())) {
				ImageLoader.getInstance().displayImage(dao.getPicPath(),
						viewHolder.picImg, options);
			}
			viewHolder.nameText.setText(dao.getNickname());
			viewHolder.positionText.setText(dao.getPosition());
			viewHolder.companyText.setText(dao.getCompany());
			return convertView;
		}

		class ViewHolder {
			ImageView picImg, qunzhuImg, qiandaoImg, messageImg, callImg;
			TextView nameText, positionText, companyText;
		}

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
		myTitle.setText("上传名单");
		create_Tv = (TextView) mCustomView.findViewById(R.id.create_Tv);
		create_Tv.setText("群成员");
		create_Tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PeopleManagerActivity.this,ActionScanActivity.class);
				intent.putExtra("activityId", activityId);
				intent.putExtra("role", "-1");
				intent.putExtra("index", "load");
				startActivity(intent);
			}
		});
		search = (ImageView) mCustomView.findViewById(R.id.titleIv);
		search.setBackgroundResource(R.drawable.ic_action_overflow);
		search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PeopleManagerPopupWindow window = new PeopleManagerPopupWindow(
						PeopleManagerActivity.this, handler);
				window.showPopupWindow(moreImg);
			}
		});
	}

	public Map<String, String> getMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("keyword", "");
		map.put("isSignIn", "-1");
		map.put("activityId", activityId);
		map.put("isAll", 1 + "");
		map.put("page", page + "");
		map.put("size", 10 + "");
		return map;
	}

	public void initView() {
		listview = (XListView) findViewById(R.id.peoplenamager_listview);
		listview.setPullLoadEnable(true);// 设置让它上拉，FALSE为不让上拉，便不加载更多数据
		listview.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub

			}
		});
	}

	// 获取成员信息
	public void initValue() {
		new Thread() {
			public void run() {
				String param = Utils.simpleMapToJsonStr(map);
				KeelLog.e(TAG, "param::" + param);
				String result = HttpRequestUtil.sendPost(
						Constant.searchUserPath, param,
						PeopleManagerActivity.this);
				KeelLog.e(TAG, "result::" + result);
				ServerResultDao dao = Utils.getServerResult(result);
				if (dao != null) {
					if (dao.getNotifyCode().equals("0001")) {
						if (dao.getResponseData() != null
								&& !"".equals(dao.getResponseData())
								&& !"null".equals(dao.getResponseData())) {
							List<PeopleNumberDao> tempLsit = new ArrayList<PeopleNumberDao>();
							tempLsit = getPeopleNumber(dao.getResponseData());
							if (tempLsit == null || tempLsit.size() == 0) {
								Message msg = new Message();
								msg.what = 4;
								msg.obj = "查询成员列表失败";
								handler.sendMessage(msg);
							} else {
								list.addAll(tempLsit);
								handler.sendEmptyMessage(3);
							}
						} else {
							Message msg = new Message();
							msg.what = 4;
							msg.obj = "查询成员列表失败";
							handler.sendMessage(msg);
						}
					} else {
						Message msg = new Message();
						msg.what = 4;
						msg.obj = "查询成员列表失败，失败原因：" + dao.getNotifyInfo();
						handler.sendMessage(msg);
					}
				} else {
					Message msg = new Message();
					msg.what = 4;
					msg.obj = "查询成员列表失败，请重试！";
					handler.sendMessage(msg);
				}
			};
		}.start();
	}

	public List<PeopleNumberDao> getPeopleNumber(String responseData) {
		List<PeopleNumberDao> list = new ArrayList<PeopleNumberDao>();
		try {
			JSONObject job = new JSONObject(responseData);
			if (!job.getString("count").equals("0")) {
				JSONArray arr = job.getJSONArray("userList");
				Gson gson = new Gson();
				for (int i = 0; i < arr.length(); i++) {
					PeopleNumberDao dao = gson.fromJson(arr.optString(i),
							PeopleNumberDao.class);
					list.add(dao);
				}
				return list;
			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	public void initListViewData() {
		adapter = new GroupAdapter(this, list);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(PeopleManagerActivity.this,
						PeopleMemberActivity.class);
				intent.putExtra("activityId", activityId);
				intent.putExtra("userId", list.get(position).getId() + "");
				intent.putExtra("type", list.get(position).getType());
				startActivity(intent);
			}
		});
	}
}