package com.tongmeng.alliance.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tongmeng.alliance.dao.ConferenceClass;
import com.tongmeng.alliance.dao.Label;
import com.tongmeng.alliance.dao.ServerResultDao;
import com.tongmeng.alliance.util.Constant;
import com.tongmeng.alliance.util.HttpRequestUtil;
import com.tongmeng.alliance.util.Utils;
import com.tongmeng.alliance.view.FindActionPopupWindow;
import com.tr.R;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.communities.home.CommumitiesNotificationActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.people.homepage.ContactsMainPageActivity;
import com.tr.ui.relationship.NewConnectionActivity;
import com.tr.ui.widgets.title.menu.popupwindow.ActionItem;
import com.tr.ui.widgets.title.menu.popupwindow.TitlePopup;
import com.tr.ui.widgets.title.menu.popupwindow.TitlePopup.OnPopuItemOnClickListener;
import com.utils.log.KeelLog;

public class ActionMainActivity extends JBaseActivity implements
		OnClickListener {

	public String TAG = "ActionMainActivity";

	private TitlePopup titlePopup;
	RelativeLayout placeLayout;
	RelativeLayout timeLayout;
	RelativeLayout typeLayout;
	RelativeLayout priceLayout;
	TextView placeTv;
	TextView timeTv, typeTv, priceTv;
	View placeView;
	View timeView, typeView, priceView;
	private TextView view_line;
	private XListView listView;
	private MyAdapter adapter;

	Map<String, String> map = new HashMap<String, String>();// 发送数据时使用的map
	List<Label> labelList = new ArrayList<Label>();
	List<ConferenceClass> confList = new ArrayList<ConferenceClass>(),
			adapterList = new ArrayList<ConferenceClass>();
	int page = 0;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int position = msg.getData().getInt("position");
			switch (msg.what) {
			case 0:
				listView.stopLoadMore();
				listView.stopRefresh();
				listView.showFooterView(false);
				if (confList == null || confList.size() == 0) {
					return;
				} else {
					adapterList.addAll(confList);
				}
				if (adapter == null) {
					adapter = new MyAdapter(ActionMainActivity.this);
					listView.setAdapter(adapter);
				} else {
					adapter.notifyDataSetChanged();
				}
				break;
			case 1:
				placeTv.setText(locationStrList.get(position));
				page = 0;
				map.put("page", page + "");
				adapterList.clear();
				getListViewData();
				break;
			case 2:
				timeTv.setText(timeStrList.get(position));
				page = 0;
				map.put("page", page + "");
				adapterList.clear();
				getListViewData();
				break;
			case 3:
				typeTv.setText(typeStrList.get(position));
				page = 0;
				map.put("page", page + "");
				adapterList.clear();
				getListViewData();
				break;
			case 4:
				priceTv.setText(priceStrList.get(position));
				page = 0;
				map.put("page", page + "");
				adapterList.clear();
				getListViewData();
				break;

			default:
				break;
			}
		};
	};

	List<String> locationStrList, timeStrList, typeStrList, priceStrList;// 地点、时间、类型、费用的具体

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.main_findaction_locationLayout:
			placeTv.setTextColor(Color.parseColor("#63a8eb"));
			placeView.setBackgroundColor(Color.parseColor("#63a8eb"));
			timeTv.setTextColor(Color.parseColor("#999999"));
			timeView.setBackgroundColor(Color.parseColor("#ffffff"));
			typeTv.setTextColor(Color.parseColor("#999999"));
			typeView.setBackgroundColor(Color.parseColor("#ffffff"));
			priceTv.setTextColor(Color.parseColor("#999999"));
			priceView.setBackgroundColor(Color.parseColor("#ffffff"));
			FindActionPopupWindow popupWindow = new FindActionPopupWindow(this,
					locationStrList, 1, handler);
			popupWindow.showPopupWindow(placeLayout);
			break;
		case R.id.main_findaction_timeLayout:
			timeTv.setTextColor(Color.parseColor("#ffbe6c"));
			timeView.setBackgroundColor(Color.parseColor("#ffbe6c"));
			placeTv.setTextColor(Color.parseColor("#999999"));
			placeView.setBackgroundColor(Color.parseColor("#ffffff"));
			typeTv.setTextColor(Color.parseColor("#999999"));
			typeView.setBackgroundColor(Color.parseColor("#ffffff"));
			priceTv.setTextColor(Color.parseColor("#999999"));
			priceView.setBackgroundColor(Color.parseColor("#ffffff"));
			FindActionPopupWindow popupWindow1 = new FindActionPopupWindow(
					this, timeStrList, 2, handler);
			popupWindow1.showPopupWindow(timeLayout);
			break;
		case R.id.main_findaction_typeLayout:
			typeTv.setTextColor(Color.parseColor("#89d6aa"));
			typeView.setBackgroundColor(Color.parseColor("#89d6aa"));
			timeTv.setTextColor(Color.parseColor("#999999"));
			timeView.setBackgroundColor(Color.parseColor("#ffffff"));
			placeTv.setTextColor(Color.parseColor("#999999"));
			placeView.setBackgroundColor(Color.parseColor("#ffffff"));
			priceTv.setTextColor(Color.parseColor("#999999"));
			priceView.setBackgroundColor(Color.parseColor("#ffffff"));
			FindActionPopupWindow popupWindow2 = new FindActionPopupWindow(
					this, typeStrList, 3, handler);
			popupWindow2.showPopupWindow(typeLayout);
			break;
		case R.id.main_findaction_priceLayout:
			priceTv.setTextColor(Color.parseColor("#fd8680"));
			priceView.setBackgroundColor(Color.parseColor("#fd8680"));
			placeTv.setTextColor(Color.parseColor("#999999"));
			placeView.setBackgroundColor(Color.parseColor("#ffffff"));
			timeTv.setTextColor(Color.parseColor("#999999"));
			timeView.setBackgroundColor(Color.parseColor("#ffffff"));
			typeTv.setTextColor(Color.parseColor("#999999"));
			typeView.setBackgroundColor(Color.parseColor("#ffffff"));
			FindActionPopupWindow popupWindow3 = new FindActionPopupWindow(
					this, priceStrList, 4, handler);
			popupWindow3.showPopupWindow(priceLayout);
			break;

		default:
			break;
		}
	}

	@Override
	public void initJabActionBar() {
		// TODO Auto-generated method stub
		setContentView(R.layout.action_actionmainpage);
		initView();
		initTitle();
		initPopWindow();
		initData();
	}

	private void initData() {
		// 获取类型中的数据
		new Thread() {
			public void run() {
				String result = HttpRequestUtil.sendPost(Constant.typePath,
						null, ActionMainActivity.this);
				KeelLog.e(TAG, "type result::" + result);
				ServerResultDao typeDao = Utils.getServerResult(result);
				if (typeDao.getNotifyCode().endsWith("0001")) {
					typeStrList = Utils.getStringList(
							typeDao.getResponseData(), "labelList", "name");
					typeStrList.add("全部");
					KeelLog.e(TAG, "typeStrList.size::" + typeStrList.size());
				} else {
					Toast.makeText(ActionMainActivity.this, "活动类型查询失败，失败原因："
							+ typeDao.getNotifyInfo(), 0);
				}
			};
		}.start();

		map = getMap(page);
		// 获取活动数据
		getListViewData();
	}

	private void getListViewData() {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				String param = Utils.simpleMapToJsonStr(map);
				KeelLog.e(TAG, "param::" + param);
				String result = HttpRequestUtil.sendPost(Constant.discoverPath,
						param, ActionMainActivity.this);
				KeelLog.e(TAG, "result::" + result);
				ServerResultDao dao = Utils.getServerResult(result);
				if (dao.getNotifyCode().equals("0001")) {
					if (dao.getResponseData() != null
							&& !"".equals(dao.getResponseData())) {
						confList = getConferenceList(dao.getResponseData());
						KeelLog.e(TAG, "confList::" + confList.size());
						if (confList == null) {
							Message msg = new Message();
							msg.what = 7;
							msg.obj = "对不起，已经没有活动数据了";
							handler.sendMessage(msg);
						} else {
							handler.sendEmptyMessage(0);
						}
					} else {
						Message msg = new Message();
						msg.what = 7;
						msg.obj = "对不起，没有查询到当前活动数据";
						handler.sendMessage(msg);
					}
				} else {
					Toast.makeText(ActionMainActivity.this,
							"获取活动列表失败，失败原因：" + dao.getNotifyInfo(), 0).show();
				}
			}
		}.start();
	}

	/**
	 * 访问服务器数据
	 * 
	 * @param page
	 *            默认是0
	 * @return
	 */
	public HashMap<String, String> getMap(int page) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("keyword", "");
		map.put("time", "0");
		map.put("day", "");
		map.put("place", "");
		map.put("label", "");
		map.put("payType", "0");
		map.put("page", page + "");
		map.put("size", "10");
		return map;
	}

	/**
	 * 解析类型数据
	 * 
	 * @param result
	 * @return
	 */
	public List<String> getTypeList(String result) {
		List<String> list = new ArrayList<String>();
		try {
			JSONObject responseObj = new JSONObject(result);
			String s = responseObj.getString("labelList");
			JSONArray array = new JSONArray(s);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = (JSONObject) array.opt(i);
				list.add(obj.getString("name"));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 解析活动数据
	 * 
	 * @param result
	 * @return
	 */
	private List<ConferenceClass> getConferenceList(String result) {
		// TODO Auto-generated method stub
		List<ConferenceClass> list = new ArrayList<ConferenceClass>();
		try {
			JSONObject responseObj = new JSONObject(result);
			int count = responseObj.getInt("count");
			String activityStr = responseObj.getString("activityList");
			if (count == 0) {
				Message msg = new Message();
				msg.what = 7;
				msg.obj = "当前活动列表数据为空";
				handler.sendMessage(msg);
			} else {
				if (activityStr == null || "".equals(activityStr)
						|| "null".equals(activityStr)) {
					Message msg = new Message();
					msg.what = 7;
					msg.obj = "当前活动列表数据为空";
					handler.sendMessage(msg);
				} else {
					JSONArray array = new JSONArray(activityStr);
					for (int i = 0; i < array.length(); i++) {
						String activity = array.optJSONObject(i).toString();
						Gson gson = new Gson();
						ConferenceClass conference = gson.fromJson(activity,
								ConferenceClass.class);
						list.add(conference);
					}
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	};

	private void initView() {
		// TODO Auto-generated method stub
		placeLayout = (RelativeLayout) findViewById(R.id.main_findaction_locationLayout);
		placeTv = (TextView) findViewById(R.id.main_findaction_locationText);
		placeView = findViewById(R.id.main_findaction_locationImg);

		timeLayout = (RelativeLayout) findViewById(R.id.main_findaction_timeLayout);
		timeTv = (TextView) findViewById(R.id.main_findaction_timeText);
		timeView = findViewById(R.id.main_findaction_timeImg);

		typeLayout = (RelativeLayout) findViewById(R.id.main_findaction_typeLayout);
		typeTv = (TextView) findViewById(R.id.main_findaction_typeText);
		typeView = findViewById(R.id.main_findaction_typeImg);

		priceLayout = (RelativeLayout) findViewById(R.id.main_findaction_priceLayout);
		priceTv = (TextView) findViewById(R.id.main_findaction_priceText);
		priceView = findViewById(R.id.main_findaction_priceImg);

		placeLayout.setOnClickListener(this);
		timeLayout.setOnClickListener(this);
		typeLayout.setOnClickListener(this);
		priceLayout.setOnClickListener(this);

		view_line = (TextView) findViewById(R.id.text_transparent_line);

		locationStrList = new ArrayList<String>();
		locationStrList.add("北京");
		locationStrList.add("上海");
		locationStrList.add("广州");
		locationStrList.add("深圳");
		locationStrList.add("全国");

		timeStrList = new ArrayList<String>();
		timeStrList.add("全部");
		timeStrList.add("今天");
		timeStrList.add("明天");
		timeStrList.add("周末");
		timeStrList.add("近一周");
		timeStrList.add("自定义");

		priceStrList = new ArrayList<String>();
		priceStrList.add("全部");
		priceStrList.add("免费");
		priceStrList.add("收费");

		listView = (XListView) findViewById(R.id.action_mainlistview);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				position = position - 1;
				Log.e("XListView", "XListView item::" + position);
				ConferenceClass cof = confList.get(position);
				Log.e("", "会议id：" + cof.getId() + ",会议名称：" + cof.getTitle()
						+ ",会议来源:" + cof.getSource());
				Intent intent = new Intent(ActionMainActivity.this,
						ActionDetailActivity.class);
				intent.putExtra("activityId", cof.getId());
				intent.putExtra("source", cof.getSource());
				startActivity(intent);
			}
		});
		listView.showFooterView(false);
		// 设置xlistview可以加载、刷新
		listView.setPullLoadEnable(true);
		listView.setPullRefreshEnable(true);
		listView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				page = 0;
				map.put("page", page + "");
				adapterList.clear();
				getListViewData();
			}

			@Override
			public void onLoadMore() {
				if ((page + 1) > adapterList.size() / 10) {
					Toast.makeText(ActionMainActivity.this, "没有更多数据显示",
							Toast.LENGTH_SHORT).show();
				}
				page = page + 1;
				map.put("page", page + "");
				getListViewData();
			}
		});
		listView.setAdapter(adapter);
	}

	private void initTitle() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "发现活动",
				false, null, false, true);
	}

	private void initPopWindow() {
		// 实例化标题栏弹窗
		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		titlePopup.setItemOnClickListener(onitemClick);
		// 给标题栏弹窗添加子类
		// titlePopup.addAction(new ActionItem(this,
		// "发布活动",R.drawable.icon_post_activity));
		// titlePopup.addAction(new ActionItem(this,
		// "添加联系人",R.drawable.icon_add_contact));
		// titlePopup.addAction(new ActionItem(this,
		// "扫一扫",R.drawable.icon_scan));
		// titlePopup.addAction(new ActionItem(this,
		// "意见反馈",R.drawable.icon_feedback));
		titlePopup.addAction(new ActionItem(this, "发布活动"));
		titlePopup.addAction(new ActionItem(this, "添加联系人"));
		titlePopup.addAction(new ActionItem(this, "扫一扫"));
		titlePopup.addAction(new ActionItem(this, "意见反馈"));
	}

	private OnPopuItemOnClickListener onitemClick = new OnPopuItemOnClickListener() {

		@Override
		public void onItemClick(ActionItem item, int position) {
			switch (position) {
			case 0:// 发布活动
				Intent intent1 = new Intent(ActionMainActivity.this,
						ReleaseActivity.class);
				intent1.putExtra("activityId", "0");
				startActivity(intent1);
				break;
			case 1:// 添加联系人
				ENavigate.startMeetingInviteFriendsActivity(ActionMainActivity.this);
				break;
			case 2:// 扫一扫
				Intent intent3 = new Intent(ActionMainActivity.this,
						ActionScanActivity.class);
				startActivity(intent3);
				break;
			case 3:// 意见反馈
				ENavigate.startOnekeyBackActivity(ActionMainActivity.this);
			default:
				break;
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_new_main, menu);
		menu.findItem(R.id.home_new_menu_more).setIcon(
				R.drawable.ic_action_overflow);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.home_new_menu_more:// 更多
			titlePopup.show(view_line);
			break;
		case R.id.home_new_menu_search:// 活动搜索
			Intent intent = new Intent(ActionMainActivity.this,
					ActionSearchActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	class MyAdapter extends BaseAdapter {

		private Context context;
		private DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.default_picture_liebiao) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.default_picture_liebiao) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.default_picture_liebiao) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.build();

		public MyAdapter(Context context) {
			this.context = context;
			KeelLog.e("MyAdapter");
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return adapterList.size();
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
			KeelLog.e("getView", "getView");
			ViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.main_findaction_listitem, null);
				viewHolder.listImg = (ImageView) convertView
						.findViewById(R.id.main_findaction_listitem_Img);
				viewHolder.titleText = (TextView) convertView
						.findViewById(R.id.main_findaction_listitem_titleText);
				viewHolder.timeText = (TextView) convertView
						.findViewById(R.id.main_findaction_listitem_timeText);
				viewHolder.priceText = (TextView) convertView
						.findViewById(R.id.main_findaction_listitem_priceText);
				viewHolder.locationText = (TextView) convertView
						.findViewById(R.id.main_findaction_listitem_locationText);
				viewHolder.peopleNuText = (TextView) convertView
						.findViewById(R.id.main_findaction_listitem_personNoText);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			// float weight = context.getResources().getDimension(R.dimen.x190);
			// MyLog.e("", "weight::"+weight);

			KeelLog.e("getView", "adapterList.size::" + adapterList.size());
			ConferenceClass conf = adapterList.get(position);
			KeelLog.e("getView", "conf.picPath::" + conf.picPath);

			if (!conf.picPath.contains("http")) {
				ImageLoader.getInstance().displayImage(
						"http://wimg.huodongxing.com" + conf.picPath,
						viewHolder.listImg, options);
			} else {
				ImageLoader.getInstance().displayImage(conf.picPath,
						viewHolder.listImg, options);
			}

			viewHolder.titleText.setText(conf.getTitle());
			viewHolder.timeText.setText(conf.startTime);
			if (TextUtils.equals(conf.getFee(), "0")) {
				viewHolder.priceText.setText("免费");
			} else {
				viewHolder.priceText.setText(conf.getFee() + "元");
			}

			String province = conf.getProvince();
			String city = conf.getCity();
			String tempAddress = conf.getAddress();
			KeelLog.e("", "tempAddress::" + tempAddress);
			if ((province == null || "".equals(province))
					&& (city == null || "".equals(city))) {
				if (tempAddress != null || !"".equals(tempAddress)) {
					String address = tempAddress.substring(1,
							tempAddress.indexOf("）"));
					viewHolder.locationText.setText(address);
				}
			} else {
				viewHolder.locationText.setText(province + city);
			}

			viewHolder.peopleNuText.setText(conf.getPeopleNumber() + "人报名");

			KeelLog.e("", "position:" + position + "会议id:" + conf.getId()
					+ "会议名称：" + conf.getTitle() + "来源:" + conf.getSource());
			return convertView;
		}

		class ViewHolder {
			public ImageView listImg;
			public TextView titleText, timeText, priceText, locationText,
					peopleNuText;
		}

	}
}
