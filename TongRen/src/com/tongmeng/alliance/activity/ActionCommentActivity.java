package com.tongmeng.alliance.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tongmeng.alliance.adapter.CommentLickAdapter;
import com.tongmeng.alliance.dao.HotComment;
import com.tongmeng.alliance.dao.ServerResultDao;
import com.tongmeng.alliance.util.Constant;
import com.tongmeng.alliance.util.HttpRequestUtil;
import com.tongmeng.alliance.util.Utils;
import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.common.view.XListView;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.log.KeelLog;

public class ActionCommentActivity extends JBaseActivity implements
		CommentLickAdapter.Callback {

	// 控件
	XListView listview;
	EditText edittext;
	TextView noresultText;
	ProgressBar progressBar;

	String activityId;
	int page = 0;
	// Message message;
	MyAdapter adapter;
	List<HotComment> list = new ArrayList<HotComment>(),
			adapterList = new ArrayList<HotComment>();

	Map<String, String> map = new HashMap<String, String>();
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if (progressBar.getVisibility() == View.VISIBLE) {
					progressBar.setVisibility(View.GONE);
				}
				setAdapterData();
				break;
			case 1:
				if (progressBar.getVisibility() == View.VISIBLE) {
					progressBar.setVisibility(View.GONE);
				}
				if (listview.getVisibility() == View.VISIBLE) {
					listview.setVisibility(View.GONE);
				}
				if (noresultText.getVisibility() == View.GONE) {
					noresultText.setVisibility(View.VISIBLE);
				}
				break;
			case 2:// 点赞成功
				int position = Integer.parseInt(msg.obj + "");
				KeelLog.e(TAG, "position::" + position);
				int num = list.get(position).getApprovalCount() + 1;
				list.get(position).setApprovalCount(num);
				list.get(position).setIsApproval(1 + "");
				adapter.notifyDataSetChanged();
				break;
			case 3:// 取消点赞
				int position1 = Integer.parseInt(msg.obj + "");
				KeelLog.e(TAG, "position::" + position1);
				int num1 = list.get(position1).getApprovalCount();
				if (num1 - 1 >= 0) {
					list.get(position1).setApprovalCount(num1 - 1);
				} else {
					list.get(position1).setApprovalCount(0);
				}
				list.get(position1).setIsApproval(0 + "");
				adapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void myClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.activity_actiondetail_listviewItem_likeImg:
			Log.e(TAG, "say hello");
			break;

		default:
			break;
		}
	}

	@Override
	public void initJabActionBar() {
		// TODO Auto-generated method stub
		setContentView(R.layout.action_comment);
		initTitle();
		initView();

		activityId = getIntent().getStringExtra("activityId");
		KeelLog.e(TAG, "activityId::" + activityId);
		if (activityId == null || "".equals(activityId)) {
			if (noresultText.getVisibility() == View.GONE) {
				noresultText.setVisibility(View.VISIBLE);
			}
		} else {
			initValue();
		}
	}

	private void initTitle() {
		// TODO Auto-generated method stub
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "评论",
				false, null, false, true);
	}

	public void initView() {

		listview = (XListView) findViewById(R.id.comment_listview);
		listview.setPullLoadEnable(true);// 设置让它上拉，FALSE为不让上拉，便不加载更多数据
		edittext = (EditText) findViewById(R.id.comment_edittext);
		noresultText = (TextView) findViewById(R.id.comment_noresult);
		progressBar = (ProgressBar) findViewById(R.id.comment_progressBar);
	}

	public void initValue() {
		new Thread() {
			public void run() {
				map = getMap();
				String param = Utils.simpleMapToJsonStr(map);
				KeelLog.e(TAG, "评论param：：" + param);
				String result = HttpRequestUtil.sendPost(Constant.commentPath,
						param, ActionCommentActivity.this);
				KeelLog.e(TAG, "评论result：：" + result);
				ServerResultDao dao = Utils.getServerResult(result);
				// message = new Message();
				if (dao.getNotifyCode().equals("0001")) {
					if (dao.getResponseData() != null
							&& !"".equals(dao.getResponseData())) {
						list = getCommentList(dao.getResponseData());
						KeelLog.e(TAG, "list.size::" + list.size());
						if (list == null || list.size() == 0) {
							Message msg = new Message();
							msg.what = 1;
							msg.obj = "获取评论列表失败,请重试！";
							handler.sendMessage(msg);
						} else {
							handler.sendEmptyMessage(0);
						}
					} else {
						Message msg = new Message();
						msg.what = 1;
						msg.obj = "获取评论列表失败,请重试！";
						handler.sendMessage(msg);
					}
				} else {
					Message msg = new Message();
					msg.what = 1;
					msg.obj = "获取评论列表失败,失败原因:" + dao.getNotifyInfo() + ",请重试！";
					handler.sendMessage(msg);
				}
			};
		}.start();

	}

	public Map<String, String> getMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("activityId", activityId);
		map.put("page", page + "");
		map.put("size", 10 + "");
		return map;
	}

	public void setAdapterData() {
		if (noresultText.getVisibility() == View.VISIBLE) {
			noresultText.setVisibility(View.GONE);
		}
		if (listview.getVisibility() == View.GONE) {
			listview.setVisibility(View.VISIBLE);
		}

		adapter = new MyAdapter(this, list);
		listview.setAdapter(adapter);
	}

	class MyAdapter extends BaseAdapter {

		private final Context context;
		private List<HotComment> list;
		private DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.default_picture_liebiao) // 设置图片下载期间显示的图片
		.showImageForEmptyUri(R.drawable.default_picture_liebiao) // 设置图片Uri为空或是错误的时候显示的图片
		.showImageOnFail(R.drawable.default_picture_liebiao) // 设置图片加载或解码过程中发生错误显示的图片
		.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
		.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
		.build();


		public MyAdapter(Context context, List<HotComment> list) {
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
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
										ActionCommentActivity.this);
								KeelLog.e(TAG, "点赞 result ::" + result);
								ServerResultDao dao = Utils
										.getServerResult(result);

								if (dao != null) {
									if (dao.getNotifyCode().equals("0001")) {
										if (dao.getResponseData() != null
												&& !"".equals(dao
														.getResponseData())) {
											boolean isSuccess = getApprovalResult(dao
													.getResponseData());
											if (isSuccess) {
												Message message = new Message();
												message.what = 2;
												message.obj = position;
												handler.sendMessage(message);
											} else {
												Message message = new Message();
												message.what = 1;
												message.obj = "点赞失败，请重试";
												handler.sendMessage(message);
											}
										} else {
											Message message = new Message();
											message.what = 1;
											message.obj = "点赞失败，请重试";
											handler.sendMessage(message);
										}

									} else {
										Message message = new Message();
										message.what = 1;
										message.obj = "点赞失败，失败原因："
												+ dao.getNotifyInfo() + ",请重试！";
										handler.sendMessage(message);
									}
								} else {
									Message message = new Message();
									message.what = 1;
									message.obj = "点赞失败，请重试";
									handler.sendMessage(message);
								}
							};
						}.start();
					} else {
						Message message = new Message();
						message.what = 3;
						message.obj = position;
						handler.sendMessage(message);
					}

				}
			});
			return convertView;
		}
	}

	class ViewHolder {
		ImageView img, likeImg;
		TextView nameText, createTime, commentText, lickNoText;
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
			}
			JSONArray arr = job.getJSONArray("list");
			Gson gson = new Gson();
			for (int i = 0; i < arr.length(); i++) {
				HotComment dao = gson.fromJson(arr.opt(i).toString(),
						HotComment.class);
				list.add(dao);
			}
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	public boolean getApprovalResult(String responseData) {
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
