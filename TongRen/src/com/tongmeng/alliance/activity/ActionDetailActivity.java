package com.tongmeng.alliance.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tongmeng.alliance.dao.HotComment;
import com.tongmeng.alliance.dao.InteresteDao;
import com.tongmeng.alliance.dao.Meeting;
import com.tongmeng.alliance.dao.ServerResultDao;
import com.tongmeng.alliance.util.Constant;
import com.tongmeng.alliance.util.HttpRequestUtil;
import com.tongmeng.alliance.util.Utils;
import com.tongmeng.alliance.view.MyGallery;
import com.tr.R;
import com.tr.model.obj.JTFile;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.FrameWorkUtils;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.log.KeelLog;

public class ActionDetailActivity extends JBaseActivity implements
		OnClickListener {

	public String TAG = "ActionDetailActivity";

	TextView themeTv, personNameTv, timeTv, placeTv, priceTv, applyNumTv,
			limitNumTv, moreCommentTv, applyTv;
	LinearLayout tagLayout, reviewlayout, applylayout;
	MyGallery gallery;
	ListView listview;
	WebView webview;
	ImageView personImg;

	String activityId, source;
	Meeting meeting;
	List<String> activityLabelList = new ArrayList<String>();
	List<String> applyLabelList = new ArrayList<String>();
	List<HotComment> commentsList = new ArrayList<HotComment>();
	List<InteresteDao> interesteDaoList = new ArrayList<InteresteDao>();
	List<ImageView> lickImgList = new ArrayList<ImageView>();
	String applyLabelListStr;
	Ladapter ladapter;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:// 获取数据失败
				Toast.makeText(ActionDetailActivity.this, msg.obj + "", 0)
						.show();
				break;
			case 1:// 获取活动详情成功
				initMeetingData();
				break;
			case 2:// 获取感兴趣成功
				initGalleryData();
				break;
			case 3:// 获取评论成功
				initListViewData();
				break;
			case 4:// 加载html
				webview.loadUrl("javascript:setHtml('"
						+ meeting.getActivityDesc() + "')");
				break;
			case 5:// 点赞成功
				int position = Integer.parseInt(msg.obj + "");
				int count = commentsList.get(position).getApprovalCount();
				commentsList.get(position).setApprovalCount(count + 1);
				commentsList.get(position).setIsApproval(1 + "");
				ladapter.notifyDataSetChanged();
				break;

			case 6:// 取消点赞成功
				int position1 = Integer.parseInt(msg.obj + "");
				int count1 = commentsList.get(position1).getApprovalCount();
				if (count1 - 1 >= 0) {
					commentsList.get(position1).setApprovalCount(count1 - 1);
				} else {
					commentsList.get(position1).setApprovalCount(0);
				}
				commentsList.get(position1).setIsApproval(0 + "");
				ladapter.notifyDataSetChanged();
				break;
			case 7://报名成功
				break;
			default:
				break;
			}
		}
	};

	private void initMeetingData() {
		themeTv.setText(meeting.getTitle());
		// 默认标题TextView获取焦点
		themeTv.setFocusable(true);
		themeTv.setFocusableInTouchMode(true);
		themeTv.requestFocus();
		themeTv.requestFocusFromTouch();
		if (themeTv.hasFocus()) {
			themeTv.clearFocus();
		}

		initImage();
		personNameTv.setText(meeting.getCreaterName());
		if (meeting.getFee() == null || "".equals(meeting.getFee())) {
			priceTv.setText("免费");
		} else {
			priceTv.setText("￥" + meeting.getFee());
		}
		timeTv.setText(meeting.getStartTime() + "——" + meeting.getEndTime());
		placeTv.setText(meeting.getProvince() + meeting.getCity()
				+ meeting.getAddress());
		applyNumTv.setText("已有" + meeting.getApplyNumber() + "人报名");
		if (meeting.getPeopleNumber() == 0) {
			limitNumTv.setText("不限");
		} else {
			limitNumTv.setText("限" + meeting.getPeopleNumber() + "人报名");
		}

		new Thread() {
			public void run() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				handler.sendEmptyMessage(4);
			};
		}.start();

		activityLabelList = meeting.getActivityLabelList();
		applyLabelList = meeting.getApplyLabelList();

		// 添加标签
		for (int i = 0; i < activityLabelList.size(); i++) {
			LinearLayout layout = (LinearLayout) LayoutInflater.from(
					ActionDetailActivity.this).inflate(
					R.layout.actiondetail_label_textview, null);
			TextView textview = (TextView) layout
					.findViewById(R.id.actiondetail_label_textview_textview);
			textview.setText(activityLabelList.get(i));
			tagLayout.addView(layout);
		}

		if (meeting.getIsAttender().equals("1")) {
			applyTv.setText("已报名");
			applylayout.setBackgroundResource(R.drawable.btn_gray);
			applylayout.setClickable(false);
		}
	};

	private void initImage() {
		// TODO Auto-generated method stub
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.default_people_avatar) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.default_people_avatar) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.default_people_avatar) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.build();

		ImageLoader.getInstance().displayImage(meeting.getCreaterPic(),
				personImg, options);
	}

	private void initGalleryData() {
		// TODO Auto-generated method stub
		Gadapter gadapter = new Gadapter(ActionDetailActivity.this,
				interesteDaoList);

		gallery.setAdapter(gadapter);
		// if (InteresteDaoList.size() >= 2) {
		// gallery.setSelection(2);
		// }
		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				InteresteDao dao = interesteDaoList.get(position);
				Intent interestIntent = new Intent(ActionDetailActivity.this,
						ActionDetailActivity.class);
				interestIntent.putExtra("activityId", dao.getId() + "");
				interestIntent.putExtra("source", "");
				startActivity(interestIntent);
				finish();
			}
		});
	};

	private void initListViewData() {
		// TODO Auto-generated method stub
		ladapter = new Ladapter(ActionDetailActivity.this, commentsList);
		listview.setAdapter(ladapter);
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.activity_actiondetal_IwantReviewLayout:
			if (commentsList != null && commentsList.size() > 0) {
				listview.setFocusable(true);
				listview.setFocusableInTouchMode(true);
				listview.requestFocus();
				listview.requestFocusFromTouch();
				listview.scrollTo(0, 0);
				if (listview.hasFocus()) {
					listview.clearFocus();
				}
			} else {
				Toast.makeText(this, "当前活动没有评论", 0).show();
			}
			break;
		case R.id.activity_actiondetal_IwantSignLayout:
			/**
			 * 1.判断是否是第三方发布的活动：是则跳转网页；不是，则继续 2.判断报名信息是否为空：是则跳转验证界面；不是，则跳转支付界面
			 */
			if (meeting.getSource() != null && meeting.getRedirectUrl() != null) {
				Intent uriIntent = new Intent();
				uriIntent.setAction("android.intent.action.VIEW");
				Uri contentUri = Uri.parse(meeting.getRedirectUrl());
				uriIntent.setData(contentUri);
				startActivity(uriIntent);
				finish();
			} else if (meeting.getActivityLabelList() == null
					|| meeting.getActivityLabelList().size() == 0) {

				new Thread() {
					public void run() {
						String param = "{\"activityId\":\"" + activityId
								+ "\",\"applyList\":[]}";
						KeelLog.e(TAG, "报名信息为空时，用户报名param::" + param);
						String result = HttpRequestUtil.sendPost(
								Constant.applyPath, param,
								ActionDetailActivity.this);
						KeelLog.e(TAG, "报名result::" + result);

						ServerResultDao dao = Utils.getServerResult(result);
						if (dao.getNotifyCode().equals("0001")) {
							if (dao.getResponseData() == null
									|| "".equals(dao.getResponseData())) {
								boolean isSuccess = getBooleanResult(dao
										.getResponseData());
								if (isSuccess) {
									handler.sendEmptyMessage(7);
								} else {
									Message message = new Message();
									message.what = 0;
									message.obj = "报名失败";
									handler.sendMessage(message);
								}
							} else {
								Message message = new Message();
								message.what = 0;
								message.obj = "报名失败";
								handler.sendMessage(message);
							}
						} else {
							Message message = new Message();
							message.what = 0;
							message.obj = "报名失败，失败原因" + dao.getNotifyInfo();
							handler.sendMessage(message);
						}
					};
				}.start();
			} else {
				Intent applyIntent = new Intent(ActionDetailActivity.this,
						ApplyActivity.class);
				applyIntent.putExtra("activityId", activityId);
				startActivityForResult(applyIntent, 0);
			}
			break;
		case R.id.activity_actiondetal_morecommentText:
			Intent intent = new Intent(ActionDetailActivity.this,
					ActionCommentActivity.class);
			intent.putExtra("activityId", activityId);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 0 && resultCode ==RESULT_OK){
			applyTv.setText("已报名");
			applylayout.setBackgroundResource(R.drawable.btn_gray);
			applylayout.setClickable(false);
		}
	}

	@Override
	public void initJabActionBar() {
		// TODO Auto-generated method stub
		setContentView(R.layout.action_detail1);
		initTitle();
		getBundle();
		initView();
		initData();
	}

	private void initTitle() {
		// TODO Auto-generated method stub
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "活动详情",
				false, null, false, true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_new_main, menu);
		menu.findItem(R.id.home_new_menu_search).setVisible(false);
		menu.findItem(R.id.home_new_menu_more)
				.setIcon(R.drawable.forward_share);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.home_new_menu_more:// 分享
			// titlePopup.show(view_line);
			JTFile jtFile = new JTFile();
			// jtFile.mFileName = App.getNick()+"分享了[会议]";
			jtFile.fileName = meeting.getTitle();
			jtFile.mUrl = meeting.getPicPath();
			if ((meeting.getTitle()).length() > 50) {
				jtFile.mSuffixName = (meeting.getTitle()).substring(0, 50);
			} else {
				jtFile.mSuffixName = meeting.getTitle();
			}
			jtFile.mType = JTFile.TYPE_CONFERENCE;
			jtFile.mModuleType = 1;// 会议类型
			jtFile.mTaskId = meeting.getId() + "";
			if ((meeting.getActivityDesc()).length() > 50) {
				jtFile.reserved1 = (meeting.getActivityDesc()).substring(0, 50);
			} else {
				jtFile.reserved1 = meeting.getActivityDesc();
			}
			// 弹出分享对话框
			FrameWorkUtils.showSharePopupWindow2(ActionDetailActivity.this,
					jtFile);
			break;
		case R.id.home_new_menu_search:
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initData() {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				String idParam = "{\"id\":\"" + activityId + "\",\"source\":\""
						+ source + "\"}";
				KeelLog.e(TAG, "action Param::" + idParam);
				String result = HttpRequestUtil.sendPost(
						Constant.actionDetailPath, idParam,
						ActionDetailActivity.this);
				KeelLog.e(TAG, "action result：：" + result);
				ServerResultDao dao = Utils.getServerResult(result);
				if (dao.getNotifyCode().equals("0001")) {
					if (dao.getResponseData() == null
							|| "".equals(dao.getResponseData())) {
						Message msg = new Message();
						msg.what = 0;
						msg.obj = "获取活动详情失败";
						handler.sendMessage(msg);
					} else {
						meeting = getMeetingDetail(dao.getResponseData());
						KeelLog.e(TAG, meeting.toString());
						if (meeting == null) {
							Message msg = new Message();
							msg.what = 0;
							msg.obj = "获取活动详情失败";
							handler.sendMessage(msg);
						} else {
							handler.sendEmptyMessage(1);
						}
					}
				} else {
					Message msg = new Message();
					msg.what = 0;
					msg.obj = "获取活动详情失败,失败原因：" + dao.getNotifyInfo();
					handler.sendMessage(msg);
				}
			};
		}.start();

		// 获取热门评论
		new Thread() {
			@Override
			public void run() {
				String idParam = "{\"activityId\":\"" + activityId
						+ "\",\"page\":\"0\",\"size\":\"3\"}";
				KeelLog.e(TAG, "idParam::" + idParam);
				String result = HttpRequestUtil.sendPost(Constant.commentPath,
						idParam, ActionDetailActivity.this);
				KeelLog.e(TAG, "评论信息  result：" + result);
				ServerResultDao dao = Utils.getServerResult(result);
				KeelLog.e(TAG, "评论信息 dao:" + dao.toString());

				if (!dao.getNotifyCode().equals("0001")) {
					Message message = new Message();
					message.what = 0;
					message.obj = "获取热门评论信息失败，失败原因：" + dao.getNotifyInfo();
					handler.sendMessage(message);
				} else {
					if (dao.getResponseData() == null
							|| "".equals(dao.getResponseData())
							|| "null".equals(dao.getResponseData())) {
						Message msg = new Message();
						msg.what = 0;
						msg.obj = "获取热门评论信息失败！";
						handler.sendMessage(msg);
					} else {
						commentsList = getCommentList(dao.getResponseData());
						if (commentsList == null || commentsList.size() == 0) {
							Message msg = new Message();
							msg.what = 0;
							msg.obj = "获取热门评论信息失败";
							handler.sendMessage(msg);
						} else {
							handler.sendEmptyMessage(3);
						}
					}
				}
			}
		}.start();

		// 获取感兴趣列表
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String idParam = "{\"activityId\":\"" + activityId + "\"}";
				KeelLog.e(TAG, "idParam::" + idParam);
				String result = HttpRequestUtil.sendPost(Constant.interestPath,
						idParam, ActionDetailActivity.this);
				KeelLog.e(TAG, "感兴趣列表信息 result ：：" + result);
				ServerResultDao dao = Utils.getServerResult(result);

				if (dao.getNotifyCode().equals("0001")) {
					if (dao.getResponseData() == null
							|| "".equals(dao.getResponseData())
							|| "null".equals(dao.getResponseData())) {
						Message msg = new Message();
						msg.what = 0;
						msg.obj = "获取可能感兴趣信息失败";
						handler.sendMessage(msg);
					} else {
						interesteDaoList = getInterstList(dao.getResponseData());
						if (interesteDaoList == null
								|| interesteDaoList.size() == 0) {
							Message msg = new Message();
							msg.what = 0;
							msg.obj = "获取可能感兴趣信息失败";
							handler.sendMessage(msg);
						} else {
							handler.sendEmptyMessage(2);
						}
					}
				} else {
					Message msg = new Message();
					msg.what = 0;
					msg.obj = "获取可能感兴趣信息失败，失败原因：" + dao.getNotifyInfo();
					handler.sendMessage(msg);
				}
				// InteresteDaoList = Utils.getInteresteDaoList(result);
				// message = new Message();
				// if (InteresteDaoList != null) {
				// handler.sendEmptyMessage(6);
				// } else {
				// message.what = 1;
				// message.obj = "获取可能感兴趣信息失败";
				// handler.sendMessage(message);
				// }

			}
		}.start();
	}

	private void initView() {
		// TODO Auto-generated method stub
		// 界面
		themeTv = (TextView) findViewById(R.id.activity_actiondetal_titleText);
		personImg = (ImageView) findViewById(R.id.activity_actiondetal_personIcon);
		personNameTv = (TextView) findViewById(R.id.activity_actiondetal_personNameText);
		priceTv = (TextView) findViewById(R.id.activity_actiondetal_priceText);
		timeTv = (TextView) findViewById(R.id.activity_actiondetal_timeText);
		placeTv = (TextView) findViewById(R.id.activity_actiondetal_locationText);
		applyNumTv = (TextView) findViewById(R.id.activity_actiondetal_applyPersonNoText);
		limitNumTv = (TextView) findViewById(R.id.activity_actiondetal_limitPersonNoText);

		moreCommentTv = (TextView) findViewById(R.id.activity_actiondetal_morecommentText);
		tagLayout = (LinearLayout) findViewById(R.id.activity_actiondetal_tagLayout);
		listview = (ListView) findViewById(R.id.activity_actiondetal_lsitview);
		gallery = (MyGallery) findViewById(R.id.activity_actiondetal_gallery);

		reviewlayout = (LinearLayout) findViewById(R.id.activity_actiondetal_IwantReviewLayout);
		applylayout = (LinearLayout) findViewById(R.id.activity_actiondetal_IwantSignLayout);
		applyTv = (TextView) findViewById(R.id.activity_actiondetal_applyText);
		moreCommentTv.setOnClickListener(this);

		webview = (WebView) findViewById(R.id.action_detail_webview);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setBuiltInZoomControls(true);
		webview.getSettings().setSavePassword(false);
		// 支持多种分辨率，需要js网页支持
		webview.getSettings().setUserAgentString("mac os");
		webview.getSettings().setDefaultTextEncodingName("utf-8");
		webview.loadUrl(Constant.htmlPath);
		
		reviewlayout.setOnClickListener(this);
		applylayout.setOnClickListener(this);
	}

	private void getBundle() {
		// TODO Auto-generated method stub
		activityId = getIntent().getStringExtra("activityId");
		source = getIntent().getStringExtra("source");
		KeelLog.e(TAG, "activityId::" + activityId + ",source::" + source);
	}

	// 获取活动详情
	public Meeting getMeetingDetail(String responseData) {
		try {
			JSONObject job = new JSONObject(responseData);
			Gson gson = new Gson();
			Meeting meeting = gson.fromJson(job.getString("activity"),
					Meeting.class);
			applyLabelListStr = new JSONObject(job.getString("activity"))
					.getString("applyLabelList");
			KeelLog.e(TAG, "applyLabelListStr::" + applyLabelListStr);
			return meeting;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	// 获取感兴趣列表
	public List<InteresteDao> getInterstList(String responseData) {
		List<InteresteDao> list = new ArrayList<InteresteDao>();
		try {
			JSONObject job = new JSONObject(responseData);
			JSONArray arr = job.getJSONArray("activityList");
			Gson gson = new Gson();
			for (int i = 0; i < arr.length(); i++) {
				InteresteDao dao = gson.fromJson(arr.opt(i).toString(),
						InteresteDao.class);
				list.add(dao);
			}
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	// 获取评论列表
	public List<HotComment> getCommentList(String responseData) {
		List<HotComment> list = new ArrayList<HotComment>();
		try {
			JSONObject job = new JSONObject(responseData);
			if (job.getString("list") == null
					|| "".equals(job.getString("list"))
					|| "null".equals("list")) {
				return null;
			} else {
				JSONArray arr = job.getJSONArray("list");
				Gson gson = new Gson();
				for (int i = 0; i < arr.length(); i++) {
					HotComment dao = gson.fromJson(arr.opt(i).toString(),
							HotComment.class);
					list.add(dao);
				}
			}
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	// 点赞结果解析
	public boolean getBooleanResult(String responseData) {
		try {
			JSONObject job = new JSONObject(responseData);
			return job.getBoolean("succeed");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	private class Gadapter extends BaseAdapter {

		private final Context context;
		private List<InteresteDao> list;
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.default_picture_liebiao) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.default_picture_liebiao) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.default_picture_liebiao) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.build();

		public Gadapter(Context context, List<InteresteDao> list) {
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
		public View getView(int position, View convertView, ViewGroup parent) {
			InterestViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new InterestViewHolder();
				convertView = LayoutInflater.from(ActionDetailActivity.this)
						.inflate(R.layout.activity_actiondetail_glist, null);
				viewHolder.img = (ImageView) convertView
						.findViewById(R.id.activity_actiondetail_glist_img);
				viewHolder.titleText = (TextView) convertView
						.findViewById(R.id.activity_actiondetail_glist_text);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (InterestViewHolder) convertView.getTag();
			}
			InteresteDao dao = list.get(position);
			if (dao != null) {

				if (dao.getTitle().length() > 6) {
					viewHolder.titleText
							.setText(dao.getTitle().substring(0, 6));
				} else {
					viewHolder.titleText.setText(dao.getTitle());
				}

				ImageLoader.getInstance().displayImage(dao.getPicPath(),
						viewHolder.img, options);
			}
			return convertView;
		}

		class InterestViewHolder {
			ImageView img;
			TextView titleText;
		}
	}

	class Ladapter extends BaseAdapter {

		private final Context context;
		private List<HotComment> list;
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.default_people_avatar) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.default_people_avatar) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.default_people_avatar) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.build();

		public Ladapter(Context context, List<HotComment> list) {
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
			final ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.activity_actiondetal_lsitviewitem, null);
				viewHolder.img = (ImageView) convertView
						.findViewById(R.id.activity_actiondetail_listviewItem_reviewerIconImg);
				viewHolder.likeImg = (ImageView) convertView
						.findViewById(R.id.activity_actiondetail_listviewItem_likeImg);
				viewHolder.nameText = (TextView) convertView
						.findViewById(R.id.activity_actiondetail_listviewItem_reviewerNameText);
				viewHolder.createTime = (TextView) convertView
						.findViewById(R.id.activity_actiondetail_listviewItem_reviewerTimeText);
				viewHolder.commentText = (TextView) convertView
						.findViewById(R.id.activity_actiondetail_listviewItem_reviewerContentText);
				viewHolder.lickNoText = (TextView) convertView
						.findViewById(R.id.activity_actiondetail_listviewItem_likeNoText);
				viewHolder.likeImg.setTag(0);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			final HotComment dao = list.get(position);
			KeelLog.e("", "评论 dao ：：" + dao.toString());

			ImageLoader.getInstance().displayImage(dao.getUserPic(),
					viewHolder.img, options);

			viewHolder.nameText.setText(dao.getUserName());
			viewHolder.commentText.setText(dao.getComment());
			viewHolder.createTime.setText(dao.getCreateTime());
			viewHolder.lickNoText.setText(dao.getApprovalCount() + "");
			if (dao.getIsApproval().equals("0")) {
				viewHolder.likeImg.setBackgroundResource(R.drawable.icon_like);
			} else {
				viewHolder.likeImg
						.setBackgroundResource(R.drawable.icon_like_alt);
			}

			viewHolder.likeImg.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					// Log.e(TAG, "position ::"+position);
					if (dao.getIsApproval().equals("0")) {
						new Thread() {
							public void run() {
								String param = "{\"targetId\":\""
										+ list.get(position).getId() + "\"}";
								KeelLog.e(TAG, "点赞 param ::" + param);
								String result = HttpRequestUtil.sendPost(
										Constant.approvalPath, param,
										ActionDetailActivity.this);
								KeelLog.e(TAG, "点赞 result ::" + result);
								ServerResultDao dao = Utils
										.getServerResult(result);

								if (dao != null) {
									if (dao.getNotifyCode().equals("0001")) {
										if (dao.getResponseData() != null
												&& !"".equals(dao
														.getResponseData())) {
											boolean isSuccess = getBooleanResult(dao
													.getResponseData());
											if (isSuccess) {
												Message message = new Message();
												message.what = 5;
												message.obj = position;
												handler.sendMessage(message);
											} else {
												Message message = new Message();
												message.what = 0;
												message.obj = "点赞失败";
												handler.sendMessage(message);
											}
										} else {
											Message message = new Message();
											message.what = 1;
											message.obj = "点赞失败";
											handler.sendMessage(message);
										}

									} else {
										Message message = new Message();
										message.what = 1;
										message.obj = "点赞失败，失败原因："
												+ dao.getNotifyInfo();
										handler.sendMessage(message);
									}
								} else {
									Message message = new Message();
									message.what = 1;
									message.obj = "点赞失败";
									handler.sendMessage(message);
								}
							};
						}.start();
					} else {
						Message message = new Message();
						message.what = 6;
						message.obj = position;
						handler.sendMessage(message);
					}

				}
			});
			return convertView;
		}

		class ViewHolder {
			ImageView img, likeImg;
			TextView nameText, createTime, commentText, lickNoText;
		}
	}

}
