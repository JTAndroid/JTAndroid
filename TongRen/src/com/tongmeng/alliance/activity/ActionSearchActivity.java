package com.tongmeng.alliance.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;

import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tongmeng.alliance.activity.ActionMainActivity.MyAdapter.ViewHolder;
import com.tongmeng.alliance.dao.ConferenceClass;
import com.tongmeng.alliance.dao.ServerResultDao;
import com.tongmeng.alliance.util.Constant;
import com.tongmeng.alliance.util.HttpRequestUtil;
import com.tongmeng.alliance.util.Utils;
import com.tr.R;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.communities.home.CommunitiesActivity;
import com.tr.ui.communities.model.ImMucinfo;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.http.EAPIConsts.handler;
import com.utils.log.KeelLog;

public class ActionSearchActivity extends JBaseActivity {

	public String TAG = "ActionSearchActivity";

	@ViewInject(R.id.common_text_empty)
	private TextView common_text_empty;
	@ViewInject(R.id.empty)
	private View empty;
	@ViewInject(R.id.EditTextSearch)
	private EditText editTextSearch;
	@ViewInject(R.id.new_xlistview)
	private XListView xListView;
	private MyAdapter adapter;

	private String key;
	int page = 0;
	Map<String, String> map = new HashMap<String, String>();
	List<ConferenceClass> confList = new ArrayList<ConferenceClass>(),
			adapterList = new ArrayList<ConferenceClass>();
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				xListView.stopLoadMore();
				xListView.stopRefresh();
				xListView.showFooterView(false);
				if (confList == null || confList.size() == 0) {
					return;
				} else {
					adapterList.addAll(confList);
				}
				
				if (adapter == null) {
					adapter = new MyAdapter(ActionSearchActivity.this);
					xListView.setAdapter(adapter);
				} else {
					adapter.notifyDataSetChanged();
				}
				break;
			case 1:
				Toast.makeText(ActionSearchActivity.this, msg.obj + "", 0)
						.show();
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void initJabActionBar() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_communities);
		initTitle();
		ViewUtils.inject(this); // 注入view和事件..
		initControl();
		initXlistview();
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub

	}

	private void initXlistview() {
		// TODO Auto-generated method stub
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
		xListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				page = 0;
				adapterList.clear();
				setKeyWord(key);
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				page = page + 1;
				setKeyWord(key);
			}
		});
		xListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				position = position - 1;
				KeelLog.e("XListView", "XListView item::" + position);
				ConferenceClass cof = confList.get(position);
				KeelLog.e("", "会议id：" + cof.getId() + ",会议名称：" + cof.getTitle()
						+ ",会议来源:" + cof.getSource());
				Intent intent = new Intent(ActionSearchActivity.this,
						ActionDetailActivity.class);
				intent.putExtra("activityId", cof.getId());
				intent.putExtra("source", cof.getSource());
				startActivity(intent);
			}
		});
	}

	public HashMap<String, String> getMap(int page, String key) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("keyword", key);
		map.put("time", "0");
		map.put("day", "");
		map.put("place", "");
		map.put("label", "");
		map.put("payType", "0");
		map.put("page", page + "");
		map.put("size", "10");
		return map;
	}

	private void initControl() {
		// TODO Auto-generated method stub
		common_text_empty.setText("暂无活动");
		editTextSearch.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				Log.e("", "actionId::" + actionId + ";event:" + event);
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					key = editTextSearch.getText().toString();
					KeelLog.e("SearchActivity", "SearchActivity::"
							+ editTextSearch.getText().toString());
					if ("".equals(key) || key == null) {
						Toast.makeText(ActionSearchActivity.this, "请输入搜索关键字", 0)
								.show();
					} else {
						page = 0;
						setKeyWord(key);
					}
				}
				return false;
			}
		});
	}

	private void setKeyWord(String key) {
		// TODO Auto-generated method stub
		map = getMap(page, key);
		new Thread() {
			public void run() {
				String param = Utils.simpleMapToJsonStr(map);
				KeelLog.e(TAG, "param::" + param);
				String result = HttpRequestUtil.sendPost(Constant.discoverPath,
						param, ActionSearchActivity.this);
				KeelLog.e(TAG, "result::" + result);
				ServerResultDao dao = Utils.getServerResult(result);
				if (dao.getNotifyCode().equals("0001")) {
					if (dao.getResponseData() != null
							&& !"".equals(dao.getResponseData())) {
						confList = getConferenceList(dao.getResponseData());
						if (confList != null && confList.size() > 0) {
							handler.sendEmptyMessage(0);
						} else {
							Message msg = new Message();
							msg.what = 1;
							msg.obj = "获取活动数据失败";
							handler.sendMessage(msg);
						}
					} else {
						Message msg = new Message();
						msg.what = 1;
						msg.obj = "获取活动数据失败";
						handler.sendMessage(msg);
					}
				} else {
					Message msg = new Message();
					msg.what = 1;
					msg.obj = "获取活动数据失败，失败原因：" + dao.getNotifyInfo();
					handler.sendMessage(msg);
				}
			};
		}.start();
	}

	private void initTitle() {
		// TODO Auto-generated method stub
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "发现活动",
				false, null, false, true);
	}

	// 获取活动列表
	public List<ConferenceClass> getConferenceList(String result) {
		List<ConferenceClass> list = new ArrayList<ConferenceClass>();
		try {
			JSONObject responseObj = new JSONObject(result);
			int count = responseObj.getInt("count");
			String activityStr = responseObj.getString("activityList");
			KeelLog.e(TAG, "count::" + count);
			if (count == 0) {
				return null;
			} else {
				if (activityStr == null || "".equals(activityStr)
						|| "null".equals(activityStr)) {
					Message msg = new Message();
					msg.what = 1;
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
