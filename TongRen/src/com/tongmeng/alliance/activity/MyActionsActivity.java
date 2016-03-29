package com.tongmeng.alliance.activity;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tongmeng.alliance.dao.MyAttendActivityDao1;
import com.tongmeng.alliance.dao.MyCreateActivityDao1;
import com.tongmeng.alliance.dao.ServerResultDao;
import com.tongmeng.alliance.util.Constant;
import com.tongmeng.alliance.util.HttpRequestUtil;
import com.tongmeng.alliance.util.TimeUtil;
import com.tongmeng.alliance.util.Utils;
import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.common.view.XListView;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.log.KeelLog;

/**
 * 我的活动
 * 
 * @author Administrator
 * 
 */
public class MyActionsActivity extends JBaseActivity implements OnClickListener {

	// 界面
	RelativeLayout createLayout, attendLayout, cancleLayout;
	LinearLayout noresultLayout;
	TextView createText, attendText, cancleText, noresultItemText;
	View createView, attendView, cancleView;

	XListView listview;
	Map<String, String> map = new HashMap<String, String>();
	List<MyCreateActivityDao1> createList = new ArrayList<MyCreateActivityDao1>();// 我发布的
																					// list
	List<MyAttendActivityDao1> attendList = new ArrayList<MyAttendActivityDao1>(),
			cancleList = new ArrayList<MyAttendActivityDao1>();
	CreateAdapter createAdapter;// 我发布的 adapter
	AttendAdapter attendAdapter, cancleAdapter;

	int page = 0;
	String index = "create";// 标识状态，分为“create”、“attend”和“cancle”,分别对应界面中“我发布的”、“我参加的”和“已取消的”

	private DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.default_picture_liebiao) // 设置图片下载期间显示的图片
			.showImageForEmptyUri(R.drawable.default_picture_liebiao) // 设置图片Uri为空或是错误的时候显示的图片
			.showImageOnFail(R.drawable.default_picture_liebiao) // 设置图片加载或解码过程中发生错误显示的图片
			.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
			.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
			.build();

	Handler hander = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:// 获取我发布的活动数据成功
				if (listview.getVisibility() == View.GONE) {
					listview.setVisibility(View.VISIBLE);
				}
				if (createAdapter != null) {
					createAdapter.notifyDataSetChanged();
				} else if (attendAdapter != null) {
					attendAdapter.notifyDataSetChanged();
				} else {
					setListData(index);
				}
				break;
			case 1:
				Toast.makeText(MyActionsActivity.this, msg.obj + "", 0).show();
				if (listview.getVisibility() == View.VISIBLE) {
					listview.setVisibility(View.GONE);
				}
				if (noresultLayout.getVisibility() == View.GONE) {
					noresultLayout.setVisibility(View.VISIBLE);
				}
				if (index.equals("create")) {
					noresultItemText.setText("发布一个活动试试");
				} else if (index.equals("attend")) {
					noresultItemText.setText("参加一个活动试试");
				} else if (index.equals("cancle")) {
					noresultItemText.setText("取消一个活动试试");
				}

				break;
			case 2:// 获取我参加的活动数据成功

				break;
			case 3:// 获取已取消的活动数据成功

				break;
			case 4:// 加载或刷新
				listview.stopRefresh();
				listview.stopLoadMore();
				Calendar calendar = Calendar.getInstance();
				String time = calendar.get(Calendar.YEAR) + "年"
						+ (calendar.get(Calendar.MONTH) + 1) + "月"
						+ calendar.get(Calendar.DAY_OF_MONTH) + "日 "
						+ calendar.get(Calendar.HOUR_OF_DAY) + ":"
						+ calendar.get(Calendar.MINUTE);
				listview.stopRefresh();
				listview.stopLoadMore();
				if (index.equals("create")) {
					createAdapter.notifyDataSetChanged();
				} else if (index.equals("attend")) {
					attendAdapter.notifyDataSetChanged();
				}
				break;
			default:
				break;
			}
		};
	};
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.my_event1_createLayout:
			createText.setTextColor(Color.parseColor("#5cafdb"));
			createView.setBackgroundColor(Color.parseColor("#5cafdb"));

			attendText.setTextColor(Color.parseColor("#999999"));
			attendView.setBackgroundColor(Color.parseColor("#ffffff"));
			cancleText.setTextColor(Color.parseColor("#999999"));
			cancleView.setBackgroundColor(Color.parseColor("#ffffff"));

			if (attendAdapter != null) {
				attendAdapter = null;
			}

			if (cancleAdapter != null) {
				cancleAdapter = null;
			}
			index = "create";
			page = 0;
			map = getMap();
			initValue(map, Constant.myGreatePath, index);
			break;
		case R.id.my_event1_attendLayout:
			attendText.setTextColor(Color.parseColor("#5cafdb"));
			attendView.setBackgroundColor(Color.parseColor("#5cafdb"));

			createText.setTextColor(Color.parseColor("#999999"));
			createView.setBackgroundColor(Color.parseColor("#ffffff"));
			cancleText.setTextColor(Color.parseColor("#999999"));
			cancleView.setBackgroundColor(Color.parseColor("#ffffff"));

			if (createAdapter != null) {
				createAdapter = null;
			}

			if (cancleAdapter != null) {
				cancleAdapter = null;
			}

			index = "attend";
			page = 0;
			map = getMap();
			initValue(map, Constant.myAttendPath, index);
			break;
		case R.id.my_event1_cancleLayout:
			cancleText.setTextColor(Color.parseColor("#5cafdb"));
			cancleView.setBackgroundColor(Color.parseColor("#5cafdb"));

			createText.setTextColor(Color.parseColor("#999999"));
			createView.setBackgroundColor(Color.parseColor("#ffffff"));
			attendText.setTextColor(Color.parseColor("#999999"));
			attendView.setBackgroundColor(Color.parseColor("#ffffff"));

			if (createAdapter != null) {
				createAdapter = null;
			}

			if (attendAdapter != null) {
				attendAdapter = null;
			}

			index = "cancle";
			page = 0;
			map = getMap();
			initValue(map, Constant.myCanceledPath, index);
			break;

		default:
			break;
		}
	}

	@Override
	public void initJabActionBar() {
		// TODO Auto-generated method stub
		setContentView(R.layout.my_activitys);
		initTitle();
		
		initView();
		map = getMap();
		initValue(map, Constant.myGreatePath, index);
	}
	
	public void setListData(final String index) {
		if (index.equals("create")) {
			createAdapter = new CreateAdapter(MyActionsActivity.this, createList);
			listview.setAdapter(createAdapter);
		} else if (index.equals("attend")) {
			attendAdapter = new AttendAdapter(MyActionsActivity.this, attendList);
			listview.setAdapter(attendAdapter);
		} else if (index.equals("cancle")) {
			cancleAdapter = new AttendAdapter(MyActionsActivity.this, cancleList);
			listview.setAdapter(cancleAdapter);
		}

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				KeelLog.e(TAG, "position::" + position);
				if (index.equals("create")) {
					KeelLog.e(TAG, "create id::"
							+ createList.get(position - 1).getId());
					MyCreateActivityDao1 dao = createList.get(position - 1);
					Intent intentdetail = new Intent(MyActionsActivity.this,
							MySendDetailActivity.class);
					intentdetail.putExtra("id", dao.getId());
					intentdetail.putExtra("imagepic", dao.getPicPath());
					intentdetail.putExtra("theme", dao.getTitle());
					intentdetail.putExtra("time", dao.getStartTime());
					intentdetail.putExtra("place", "海淀区");
					startActivity(intentdetail);

				} else if (index.equals("attend")) {
					KeelLog.e(TAG, "create id::"
							+ attendList.get(position - 1).getId());
				} else if (index.equals("cancle")) {
					KeelLog.e(TAG, "create id::"
							+ cancleList.get(position - 1).getId());
				}
			}
		});
	}
	
	public void initView() {
		// 界面
		createLayout = (RelativeLayout) findViewById(R.id.my_event1_createLayout);
		createText = (TextView) findViewById(R.id.my_event1_createText);
		createView = findViewById(R.id.my_event1_createView);
		createLayout.setOnClickListener(this);

		attendLayout = (RelativeLayout) findViewById(R.id.my_event1_attendLayout);
		attendText = (TextView) findViewById(R.id.my_event1_attendText);
		attendView = findViewById(R.id.my_event1_attendView);
		attendLayout.setOnClickListener(this);

		cancleLayout = (RelativeLayout) findViewById(R.id.my_event1_cancleLayout);
		cancleText = (TextView) findViewById(R.id.my_event1_cancleText);
		cancleView = findViewById(R.id.my_event1_cancleView);
		cancleLayout.setOnClickListener(this);

		listview = (XListView) findViewById(R.id.my_event1_listview);
		listview.setPullLoadEnable(true);// 设置让它上拉，FALSE为不让上拉，便不加载更多数据
		listview.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// 当此选中的item的子控件需要获得焦点时
				parent.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				parent.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
			}
		});

		noresultLayout = (LinearLayout) findViewById(R.id.my_event1_noresuleLayout);
		noresultItemText = (TextView) findViewById(R.id.my_event1_noresultItemText);
	}

	public Map<String, String> getMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("status", "0");
		map.put("page", page + "");
		map.put("size", "10");
		return map;
	}
	
	public void initValue(final Map<String, String> map, final String url,
			final String index) {
		new Thread() {
			public void run() {
				String param = simpleMapToJsonStr(map);
				KeelLog.e(TAG, "param::" + param);
				String result = HttpRequestUtil.sendPost(url, param,
						MyActionsActivity.this);
				KeelLog.e(TAG, "result::" + result);
				ServerResultDao dao = Utils.getServerResult(result);
				if (dao == null) {
					Message msg = new Message();
					msg.what = 1;
					msg.obj = "获取活动数据失败";
					hander.sendMessage(msg);
				} else {
					if (dao.getNotifyCode().equals("0001")) {
						if (dao.getResponseData() == null
								|| "".equals(dao.getResponseData())
								|| "null".equals(dao.getResponseData())) {
							Message msg = new Message();
							msg.what = 1;
							msg.obj = "获取活动数据失败";
							hander.sendMessage(msg);
						} else {
							if (index.equals("create")) {
								List<MyCreateActivityDao1> tempList = new ArrayList<MyCreateActivityDao1>();
								tempList = getCreateActionList(dao
										.getResponseData());
								if (tempList != null && tempList.size() != 0) {
									createList.addAll(tempList);
									hander.sendEmptyMessage(0);
								} else {
									Message msg = new Message();
									msg.what = 1;
									msg.obj = "获取活动数据失败";
									hander.sendMessage(msg);
								}
							} else if (index.equals("attend")) {
								List<MyAttendActivityDao1> tempList = new ArrayList<MyAttendActivityDao1>();
								tempList = getAttendActionList(dao
										.getResponseData());
								if (tempList != null && tempList.size() != 0) {
									attendList.addAll(tempList);
									hander.sendEmptyMessage(0);
								} else {
									Message msg = new Message();
									msg.what = 1;
									msg.obj = "获取活动数据失败";
									hander.sendMessage(msg);
								}
							} else if (index.equals("cancle")) {
								List<MyAttendActivityDao1> tempList = new ArrayList<MyAttendActivityDao1>();
								tempList = getAttendActionList(dao
										.getResponseData());
								if (tempList != null && tempList.size() != 0) {
									cancleList.addAll(tempList);
									hander.sendEmptyMessage(0);
								} else {
									Message msg = new Message();
									msg.what = 1;
									msg.obj = "获取活动数据失败";
									hander.sendMessage(msg);
								}
							}

						}
					} else {
						Message msg = new Message();
						msg.what = 1;
						msg.obj = "获取活动数据失败,失败原因：" + dao.getNotifyInfo();
						hander.sendMessage(msg);
					}
				}
			};
		}.start();
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
	
	private void initTitle() {
		// TODO Auto-generated method stub
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "我的活动",
				false, null, false, true);
	}

	class AttendAdapter extends BaseAdapter {

		List<MyAttendActivityDao1> list;
		private Context context;
		public int choic_position;

		public AttendAdapter(Context context, List<MyAttendActivityDao1> list) {
			this.context = context;
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final AttendViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new AttendViewHolder();
				convertView = LayoutInflater.from(MyActionsActivity.this)
						.inflate(R.layout.my_activitys_attenditem, null);
				// 当前状态
				viewHolder.status = (TextView) convertView
						.findViewById(R.id.my_event1_attenditem_statusText);
				// 地点
				viewHolder.addrss = (TextView) convertView
						.findViewById(R.id.my_event1_attenditem_placeText);
				// 开始时间
				viewHolder.startTime = (TextView) convertView
						.findViewById(R.id.my_event1_attenditem_timeText);
				// 主题
				viewHolder.theme = (TextView) convertView
						.findViewById(R.id.my_event1_attenditem_titleText);
				// 支付状态
				viewHolder.payStatus = (TextView) convertView
						.findViewById(R.id.my_event1_attenditem_paystatusText);
				// 活动图片
				viewHolder.img = (ImageView) convertView
						.findViewById(R.id.my_event1_attenditem_img);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (AttendViewHolder) convertView.getTag();
			}

			final MyAttendActivityDao1 dao = list.get(position);
			KeelLog.e(TAG, "MyAttendActivityDao1  dao::" + dao.toString());

			viewHolder.theme.setText(dao.getTitle());
			if (dao.getStatus().equals("1")) {
				viewHolder.status.setText("即将开始");
				if (index.equals("attend")) {
					viewHolder.status.setTextColor(Color.parseColor("#eb6100"));
				} else if (index.equals("cancle")) {
					viewHolder.status.setTextColor(Color.parseColor("#999999"));
				}
			} else if (dao.getStatus().equals("2")) {
				viewHolder.status.setText("已经开始");
				if (index.equals("attend")) {
					viewHolder.status.setTextColor(Color.parseColor("#8fc31f"));
				} else if (index.equals("cancle")) {
					viewHolder.status.setTextColor(Color.parseColor("#999999"));
				}
			} else if (dao.getStatus().equals("3")) {
				viewHolder.status.setText("已经结束");
				viewHolder.status.setTextColor(Color.parseColor("#999999"));
			}
			if (index.equals("attend")) {
				if (dao.getPayStatus().equals("0")) {
					viewHolder.payStatus.setVisibility(View.GONE);
				} else if (dao.getPayStatus().equals("1")) {
					if (viewHolder.payStatus.getVisibility() == View.GONE) {
						viewHolder.payStatus.setVisibility(View.VISIBLE);
					}
					viewHolder.payStatus.setText("付款");
					viewHolder.payStatus
							.setBackgroundResource(R.drawable.btn_orange);
				} else if (dao.getPayStatus().equals("2")) {
					if (viewHolder.payStatus.getVisibility() == View.GONE) {
						viewHolder.payStatus.setVisibility(View.VISIBLE);
					}
					viewHolder.payStatus.setText("申请退款");
					viewHolder.payStatus
							.setBackgroundResource(R.drawable.btn_blue);
				} else if (dao.getPayStatus().equals("3")) {
					viewHolder.payStatus.setVisibility(View.GONE);
				} else if (dao.getPayStatus().equals("4")) {
					viewHolder.payStatus.setVisibility(View.GONE);
				}
			} else if (index.equals("cancle")) {
				if (dao.getPayStatus().equals("0")) {
					if (viewHolder.payStatus.getVisibility() == View.GONE) {
						viewHolder.payStatus.setVisibility(View.VISIBLE);
					}
					viewHolder.payStatus.setText("重新报名");
				} else if (dao.getPayStatus().equals("1")) {
					viewHolder.payStatus.setVisibility(View.GONE);
				} else if (dao.getPayStatus().equals("2")) {
					viewHolder.payStatus.setVisibility(View.GONE);
				} else if (dao.getPayStatus().equals("3")) {
					if (viewHolder.payStatus.getVisibility() == View.GONE) {
						viewHolder.payStatus.setVisibility(View.VISIBLE);
					}
					viewHolder.payStatus.setText("退款中");
				} else if (dao.getPayStatus().equals("4")) {
					if (viewHolder.payStatus.getVisibility() == View.GONE) {
						viewHolder.payStatus.setVisibility(View.VISIBLE);
					}
					viewHolder.payStatus.setText("重新报名");
				}
			}
			// viewHolder.payStatus.setText(payStatusString);
			viewHolder.payStatus.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (viewHolder.payStatus.getText().toString().equals("付款")) {
						 Intent intent = new Intent(context,
						 PayActivity.class);
						 intent.putExtra("activityId", dao.getId());
						 context.startActivity(intent);
					} else if (viewHolder.payStatus.getText().toString()
							.equals("申请退款")) {
						 Intent intent = new Intent(context,
						 CancleApplyActivity.class);
						 intent.putExtra("activityId", dao.getId());
						 context.startActivity(intent);
					} else if (viewHolder.payStatus.getText().toString()
							.equals("重新报名")) {
						Intent intent = new Intent(context, ApplyActivity.class);
						intent.putExtra("activityId", dao.getId());
						context.startActivity(intent);
					}
				}
			});
			viewHolder.startTime.setText(dao.getStartTime());
			viewHolder.addrss.setText(dao.getProvince() + dao.getCity());

			// float weight = viewHolder.img.getWidth();

			ImageLoader.getInstance().displayImage(dao.picPath, viewHolder.img,
					options);
			return convertView;
		}

		class AttendViewHolder {
			TextView payStatus, status, addrss, startTime, theme;
			ImageView img;
		}
	}

	class CreateAdapter extends BaseAdapter {
		List<MyCreateActivityDao1> list;
		private Context context;
		public int choic_position;

		public CreateAdapter(Context context, List<MyCreateActivityDao1> list) {
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final CreateViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new CreateViewHolder();
				convertView = LayoutInflater.from(MyActionsActivity.this)
						.inflate(R.layout.my_activitys_createitem, null);
				KeelLog.e(TAG, "CreateAdapter convertView ::" + convertView);
				viewHolder.statusText = (TextView) convertView
						.findViewById(R.id.my_event1_createitem_statusText);
				viewHolder.titleText = (TextView) convertView
						.findViewById(R.id.my_event1_createitem_titleText);
				viewHolder.timeText = (TextView) convertView
						.findViewById(R.id.my_event1_createitem_timeText);
				viewHolder.dayText = (TextView) convertView
						.findViewById(R.id.my_event1_createitem_dayText);
				viewHolder.noText = (TextView) convertView
						.findViewById(R.id.my_event1_createitem_NoText);
				viewHolder.setText = (TextView) convertView
						.findViewById(R.id.my_event1_createitem_setText);
				viewHolder.namelistText = (TextView) convertView
						.findViewById(R.id.my_event1_createitem_namelistText);
				viewHolder.actioneditText = (TextView) convertView
						.findViewById(R.id.my_event1_createitem_actioneditText);
				viewHolder.actiondeleteText = (TextView) convertView
						.findViewById(R.id.my_event1_createitem_actiondeleteText);
				viewHolder.setLayout = (LinearLayout) convertView
						.findViewById(R.id.my_event1_createitem_setLayout);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (CreateViewHolder) convertView.getTag();
			}
			final MyCreateActivityDao1 dao = list.get(position);
			KeelLog.e(TAG, "CreateAdapter dao ::" + dao.toString());
			String status = dao.getStatus();
			KeelLog.e(TAG, "CreateAdapter status ::" + status);

			if (status.equals("1")) {// 即将开始
				viewHolder.statusText.setText("即将开始");
				viewHolder.statusText.setTextColor(Color.parseColor("#eb6100"));
			} else if (status.equals("2")) {// 已经开始
				viewHolder.statusText.setText("已经开始");
				viewHolder.statusText.setTextColor(Color.parseColor("#8fc31f"));
			} else if (status.equals("3")) {// 已经结束
				viewHolder.statusText.setText("已经结束");
				viewHolder.statusText.setTextColor(Color.parseColor("#999999"));
			}

			String startTime = dao.getStartTime();
			TimeUtil t = new TimeUtil();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date currTime = new Date(System.currentTimeMillis());
			String beginDateStr = sdf.format(currTime);

			int time = (int) (t.getDaySub(beginDateStr, startTime));
			if (time == 0) {
				viewHolder.dayText.setText("已经开始");
			} else if (time < 0) {
				viewHolder.dayText.setText("已经结束");
			} else {
				viewHolder.dayText.setText("还有" + String.valueOf(time) + "天开始");
			}

			viewHolder.titleText.setText(dao.getTitle());
			viewHolder.timeText.setText(dao.getStartTime());
			viewHolder.noText.setText(dao.getPeopleNumber());

			viewHolder.setText.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (viewHolder.setLayout.getVisibility() == View.GONE) {
						viewHolder.setLayout.setVisibility(View.VISIBLE);
					} else {
						viewHolder.setLayout.setVisibility(View.GONE);
					}
				}
			});
			// 名单管理
			viewHolder.namelistText.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 Intent intent = new Intent(MyActionsActivity.this,
					 AttendPeopleInfoActivity.class);
					 intent.putExtra("id", dao.getId());
					 context.startActivity(intent);
				}
			});
			// 活动编辑
			viewHolder.actioneditText.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(context, ReleaseActivity.class);

					intent.putExtra("activityId", Integer.parseInt(dao.getId()));
					context.startActivity(intent);
				}
			});

			viewHolder.actiondeleteText
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							DeleteMyCreateActivity(dao.getId());
						}
					});
			return convertView;
		}

		protected void DeleteMyCreateActivity(final String id) {
			// TODO Auto-generated method stub
			new Thread() {
				public void run() {
					String params = "{\"id\":\"" + id + "\"}";
					KeelLog.e(TAG, "删除活动 params ::" + params);
					String result = HttpRequestUtil.sendPost(
							Constant.deletePath, params, context);
					KeelLog.e(TAG, "删除活动 result ::" + result);
					try {
						JSONObject rev = new JSONObject(result);

						JSONObject responseData = rev
								.getJSONObject("responseData");
						String succeed = responseData.getString("succeed");
						JSONObject notification = rev
								.getJSONObject("notification");
						String notifyCode = notification
								.getString("notifyCode");
						String notifyInfo = notification
								.getString("notifyInfo");
						if (succeed.equals("true")) {
							if (msgHandler != null) {
								Message msg = msgHandler.obtainMessage();
								msg.arg1 = 1;
								msgHandler.sendMessage(msg);
							}
						} else if (succeed.equals("false")) {
							if (msgHandler != null) {
								Message msg = msgHandler.obtainMessage();
								msg.arg1 = 2;
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

		Handler msgHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.arg1) {

				case 1:
					Toast.makeText(context, "删除成功", 1).show();
					// dialog.dismiss();
					list.remove(choic_position);
					notifyDataSetChanged();
					break;
				case 2:
					Toast.makeText(context, "删除失败", 1).show();

					break;
				default:
					break;
				}

			}
		};

		class CreateViewHolder {
			TextView statusText, titleText, timeText, dayText, noText, setText,
					namelistText, actioneditText, actiondeleteText;
			LinearLayout setLayout;
		}
	}

	/**
	 * 多条数据转换成json
	 * @param map
	 * @return
	 */
	public String simpleMapToJsonStr(Map<String ,String > map){  
        if(map==null||map.isEmpty()){  
            return "null";  
        }  
        String jsonStr = "{";  
        Set<?> keySet = map.keySet();  
        for (Object key : keySet) {  
            jsonStr += "\""+key+"\":\""+map.get(key)+"\",";       
        }  
        jsonStr = jsonStr.substring(0,jsonStr.length()-1);  
        jsonStr += "}";  
        return jsonStr;  
    }  
}
