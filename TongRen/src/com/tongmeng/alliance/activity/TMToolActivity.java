package com.tongmeng.alliance.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tongmeng.alliance.activity.MyActionsActivity.AttendAdapter;
import com.tongmeng.alliance.activity.MyActionsActivity.CreateAdapter;
import com.tongmeng.alliance.dao.MyAttendActivityDao1;
import com.tongmeng.alliance.dao.MyCreateActivityDao1;
import com.tongmeng.alliance.dao.ServerResultDao;
import com.tongmeng.alliance.util.Constant;
import com.tongmeng.alliance.util.HttpRequestUtil;
import com.tongmeng.alliance.util.Utils;
import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.log.KeelLog;

public class TMToolActivity extends JBaseActivity {
	public String TAG = "TMToolActivity";
	ListView listview1,listview2;
	CreateAdapter createAdapter;// 我发布的 adapter
	AttendAdapter attendAdapter;
	int index = 0, createNum;
	List<MyCreateActivityDao1> list = new ArrayList<MyCreateActivityDao1>();
	List<MyAttendActivityDao1> attendList = new ArrayList<MyAttendActivityDao1>();
	private DisplayImageOptions options = new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.default_picture_liebiao) // 设置图片下载期间显示的图片
	.showImageForEmptyUri(R.drawable.default_picture_liebiao) // 设置图片Uri为空或是错误的时候显示的图片
	.showImageOnFail(R.drawable.default_picture_liebiao) // 设置图片加载或解码过程中发生错误显示的图片
	.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
	.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
	.build();

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(TMToolActivity.this, msg.obj + "", 0).show();
				break;
			case 1:
				setCreatAdapter();
				break;
			case 2:
				setAttendAdapter();
				break;
			default:
				break;
			}
		}
	};

	private void setAttendAdapter() {
		// TODO Auto-generated method stub
		attendAdapter = new AttendAdapter();
		listview2.setAdapter(attendAdapter);
		listview2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				position = position - 1;
				Intent intent = new Intent(TMToolActivity.this,
						GroupToolActivity.class);
				intent.putExtra("activityId", list.get(position).getId());
				intent.putExtra("isCeater", false);
				startActivity(intent);
			}
		});
	}
	
	private void setCreatAdapter() {
		// TODO Auto-generated method stub
		createAdapter = new CreateAdapter();
		listview1.setAdapter(createAdapter);
		listview1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				position = position - 1;
				Intent intent = new Intent(TMToolActivity.this,
						GroupToolActivity.class);
				intent.putExtra("activityId", list.get(position).getId());
				intent.putExtra("isCeater", true);
				startActivity(intent);
			}
		});
	};

	@Override
	public void initJabActionBar() {
		// TODO Auto-generated method stub
		setContentView(R.layout.tm_tool);
		initTitle();
		listview1 = (ListView) findViewById(R.id.tm_tool_lv1);
		listview2 = (ListView) findViewById(R.id.tm_tool_lv2);
		
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				String param = "{\"tagId\":\"0\",\"catalogId\":\"0\",\"status\":\"0\",\"page\":\"0\",\"size\":\"10\"}";// 我发布的
				String result = HttpRequestUtil.sendPost(Constant.myGreatePath,
						param, TMToolActivity.this);
				KeelLog.e(TAG, "result::" + result);
				ServerResultDao dao = Utils.getServerResult(result);
				if (dao == null) {
					Message msg = new Message();
					msg.what = 0;
					msg.obj = "查询数据失败";
					handler.sendMessage(msg);
				} else {
					if (dao.getNotifyCode().equals("0001")) {
						if (dao.getResponseData() != null
								&& !"".equals(dao.getResponseData())
								&& !"null".equals(dao.getResponseData())) {
							List<MyCreateActivityDao1> tempList = new ArrayList<MyCreateActivityDao1>();
							if (tempList != null && tempList.size() != 0) {
								list.addAll(tempList);
								handler.sendEmptyMessage(1);
							}
						} else {
							Message msg = new Message();
							msg.what = 0;
							msg.obj = "查询数据失败";
							handler.sendMessage(msg);
						}
					} else {
						Message msg = new Message();
						msg.what = 0;
						msg.obj = "查询数据失败，失败原因：" + dao.getNotifyInfo();
						handler.sendMessage(msg);
					}
				}
			};
		}.start();

		new Thread() {
			public void run() {
				String param = "";
				String result = HttpRequestUtil.sendPost(Constant.myAttendPath,
						param, TMToolActivity.this);
				KeelLog.e(TAG, "result::" + result);
				ServerResultDao dao = Utils.getServerResult(result);
				if (dao == null) {
					Message msg = new Message();
					msg.what = 0;
					msg.obj = "获取数据失败";
					handler.sendMessage(msg);
				} else {
					if (dao.getNotifyCode().equals("0001")) {
						if (dao.getNotifyCode() != null
								&& !"null".equals(dao.getResponseData())
								&& !"".equals(dao.getResponseData())) {
							List<MyAttendActivityDao1> tempList = new ArrayList<MyAttendActivityDao1>();
							tempList = getAttendActionList(dao
									.getResponseData());
							if (tempList != null && tempList.size() != 0) {
								attendList.addAll(tempList);
								handler.sendEmptyMessage(2);
							} else {
								Message msg = new Message();
								msg.what = 0;
								msg.obj = "获取活动数据失败";
								handler.sendMessage(msg);
							}
						}
					} else {
						Message msg = new Message();
						msg.what = 0;
						msg.obj = "获取数据失败,失败原因：" + dao.getNotifyInfo();
						handler.sendMessage(msg);
					}
				}
			};
		}.start();
	}

	private void initTitle() {
		// TODO Auto-generated method stub
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "桐盟工具",
				false, null, false, true);
	}

	class CreateAdapter extends BaseAdapter {

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
				convertView = LayoutInflater.from(TMToolActivity.this).inflate(
						R.layout.tm_tool_item, null);
				viewHolder.img = (ImageView) convertView
						.findViewById(R.id.tmtool_item_iv);
				viewHolder.titleTv = (TextView) convertView
						.findViewById(R.id.tmtool_item_nameTv);
				viewHolder.timeTv = (TextView) convertView
						.findViewById(R.id.tmtool_item_timeTv);
				viewHolder.contentTv = (TextView) convertView
						.findViewById(R.id.tmtool_item_contentTv);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			MyCreateActivityDao1 dao = list.get(position);
			ImageLoader.getInstance().displayImage(dao.getPicPath(),
					viewHolder.img, options);
			viewHolder.titleTv.setText(dao.getTitle());
			viewHolder.timeTv.setText(dao.getStartTime());
			return convertView;
		}
	}

	class ViewHolder {
		ImageView img;
		TextView titleTv, timeTv, contentTv;
	}
	
	class AttendAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return attendList.size();
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
				convertView = LayoutInflater.from(TMToolActivity.this).inflate(
						R.layout.tm_tool_item, null);
				viewHolder.img = (ImageView) convertView
						.findViewById(R.id.tmtool_item_iv);
				viewHolder.titleTv = (TextView) convertView
						.findViewById(R.id.tmtool_item_nameTv);
				viewHolder.timeTv = (TextView) convertView
						.findViewById(R.id.tmtool_item_timeTv);
				viewHolder.contentTv = (TextView) convertView
						.findViewById(R.id.tmtool_item_contentTv);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			MyAttendActivityDao1 dao = attendList.get(position);
			ImageLoader.getInstance().displayImage(dao.getPicPath(),
					viewHolder.img, options);
			viewHolder.titleTv.setText(dao.getTitle());
			viewHolder.timeTv.setText(dao.getStartTime());
			viewHolder.contentTv.setText(dao.getAddress());
			return convertView;
		}
		
	}
	public List<MyCreateActivityDao1> getCreateActionList(String responseData) {
		List<MyCreateActivityDao1> list = new ArrayList<MyCreateActivityDao1>();
		try {
			JSONObject job = new JSONObject(responseData);
			String activityList = job.getString("activityList");
			if (activityList == null || "".equals(activityList)
					|| "null".equals(activityList)) {
				return null;
			} else {
				JSONArray arr = job.getJSONArray("activityList");
				Gson gson = new Gson();
				for (int i = 0; i < arr.length(); i++) {
					MyCreateActivityDao1 dao = gson.fromJson(arr.optString(i),
							MyCreateActivityDao1.class);
					list.add(dao);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

	public List<MyAttendActivityDao1> getAttendActionList(String responseData) {
		List<MyAttendActivityDao1> list = new ArrayList<MyAttendActivityDao1>();
		try {
			JSONObject job = new JSONObject(responseData);
			String activityList = job.getString("activityList");
			if (activityList == null || "".equals("activityList")
					|| "null".equals("activityList")) {
				return null;
			} else {
				JSONArray arr = job.getJSONArray("activityList");
				Gson gson = new Gson();
				for (int i = 0; i < arr.length(); i++) {
					MyAttendActivityDao1 dao = gson.fromJson(arr.optString(i),
							MyAttendActivityDao1.class);
					list.add(dao);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}
}
