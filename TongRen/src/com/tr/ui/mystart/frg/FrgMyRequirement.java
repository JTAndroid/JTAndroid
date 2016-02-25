package com.tr.ui.mystart.frg;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tr.R;
import com.tr.api.DemandReqUtil;
import com.tr.api.HomeReqUtil;
import com.tr.model.demand.NeedItemData;
import com.tr.model.demand.NeedItemListItem;
import com.tr.model.home.GetMyAffairs;
import com.tr.model.home.MGetMyKnowledge;
import com.tr.model.home.MGetMyRequirement;
import com.tr.model.obj.AffairsMini;
import com.tr.model.obj.JTFile;
import com.tr.model.obj.KnowledgeMini;
import com.tr.model.obj.RequirementMini;
import com.tr.model.page.JTPage;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.demand.MeNeedActivity;
import com.utils.common.EConsts;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/** @ClassName: FrgIM.java
 * @Description: 首页-聊天界面
 * @author xuxinjian
 * @version V1.0
 * @Date 2014-3-28 上午7:52:34 */

public class FrgMyRequirement extends JBaseFragment implements IBindData, SwipeRefreshLayout.OnRefreshListener, OnScrollListener {
	private ListView mListView;
	private List<MystartItem> list = new ArrayList<MystartItem>();
	private MystartAdapter adapter;

	public final static int TYPE_REQUIREMENT = 1;// 需求
	public final static int TYPE_AFFAIR = 2;// 事务
	public final static int TYPE_KNOWLEDGE = 3;// 知识

	private int mType = TYPE_REQUIREMENT;

	private View moreView; // 加载更多页面
	private TextView mvTextView;
	private View mvProgressBar;

	private int lastItem;
	private int count;
	private JTPage mPage;
	private int mState = 0;// 0-正常状态 1-获取更多中 2-刷新中
	private SwipeRefreshLayout swipeLayout;
	private Handler mHandler = new Handler();

	private final static String mRecordID = "imrecord";
	String userID;

	// add by leon
	private boolean mReturnData = false;

	// 是否返回数据
	public void setReturnData(boolean returnData) {
		this.mReturnData = returnData;
	}

	public void setType(int type, String userID) {
		mType = type;
		this.userID = userID;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.mystart_frg_requirement, container, false);

		swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.mystart_frg_requirement_swipe_container);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);

		moreView = inflater.inflate(R.layout.pulldown_footer, null);
		this.mvTextView = (TextView) moreView.findViewById(R.id.pulldown_footer_text);
		this.mvProgressBar = (View) moreView.findViewById(R.id.pulldown_footer_loading);

		mListView = (ListView) view.findViewById(R.id.mystart_frg_requirement_listview);
		adapter = new MystartAdapter(this.getActivity());
		// list = IMCacheUtils.readIMRecordList(mRecordID);

		mListView.setOnItemClickListener(new OnItemClickListener() {
			@SuppressLint("SimpleDateFormat")
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (arg2 >= list.size()) {
					loadMoreData();
					return;
				}

				final MystartItem item = list.get(arg2);

				switch (mType) {
				case TYPE_REQUIREMENT:
					if (!mReturnData) {
					}
					else {

						// 弹出分享提示框
						/*
						 * EUtil.showShareDialog(getActivity(), item.title, new
						 * CustomDlgClickListener() {
						 * @Override public void onPositiveClick(String message)
						 * { Intent intent = new Intent(); JTFile jtFile = new
						 * JTFile(); jtFile.mFileName = item.title; // 需求标题
						 * jtFile.mSuffixName = message; // 消息内容
						 * jtFile.reserved1 = item.time; jtFile.mTaskId =
						 * item.id; intent.putExtra(EConsts.Key.JT_FILE,
						 * jtFile); getActivity().setResult(Activity.RESULT_OK,
						 * intent); getActivity().finish(); } });
						 */

						Intent intent = new Intent();
						JTFile jtFile = new JTFile();
						jtFile.fileName = item.title; // 需求标题
						jtFile.mType = JTFile.TYPE_REQUIREMENT; // 类型
						jtFile.reserved1 = item.time;
						jtFile.mTaskId = item.id;
						intent.putExtra(EConsts.Key.JT_FILE, jtFile);
						getActivity().setResult(Activity.RESULT_OK, intent);
						getActivity().finish();
					}
					break;
				case TYPE_AFFAIR:
					// showToast("去业务需求详情页:" + flowItem.getId());
					if (item.getType() == AffairsMini.TYPE_TASK) {
						showToast("此功能尚未开放，敬请等待");
					}
					else {
					}
					break;
				case TYPE_KNOWLEDGE:
					KnowledgeMini knowledgeMini = new KnowledgeMini();
					knowledgeMini.mID = Integer.parseInt(item.getId());
					knowledgeMini.setmTitle(item.getTitle());
					knowledgeMini.setmUrl(item.getUrl());
					knowledgeMini.setTime(item.getTime());
					ENavigate.startShareDetailActivity(getActivity(), knowledgeMini);
					break;
				}
			}

		});

		// addLists(20);

		adapter.setData(list);
		mListView.addFooterView(moreView);
		mListView.setAdapter(adapter);
		mListView.setOnScrollListener(this); // 设置listview的滚动事件
		count = list.size();

		mState = STATE_NORMAL;
		onRefresh();

		return view;
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void bindData(int tag, Object object) {
		// TODO Auto-generated method stub

		if (this.isHasPause()) {
			stopLoading();
			return;
		}

		if (object == null) {
			stopLoading();
			return;
		}

		if (tag == EAPIConsts.demandReqType.demand_getMyNeedList) {
			//MGetMyRequirement getMyRequirement = (MGetMyRequirement) object;
			NeedItemListItem getMyRequirement = (NeedItemListItem) object;
			mPage = getMyRequirement.getJtPage();
			if (mState == STATE_REFRESH) {
				list.clear();// 如果当前刷新中，则清除之前数据
			}

			for (int i = 0; i < mPage.getLists().size(); i++) {
				MystartItem item = new MystartItem();
				NeedItemData mini = (NeedItemData) mPage.getLists().get(i);
				
				item.setId(mini.demandId + "");
				item.setTime(mini.createTime);
				item.setTitle(mini.title.value);
				list.add(item);
			}
		}
		else if (tag == EAPIConsts.HomeReqType.HOME_REQ_GET_MY_AFFAIR) {
			GetMyAffairs getMyAffairs = (GetMyAffairs) object;

			mPage = getMyAffairs.getJtPage();
			if (mState == STATE_REFRESH) {
				list.clear();// 如果当前刷新中，则清除之前数据
			}

			for (int i = 0; i < mPage.getLists().size(); i++) {
				MystartItem item = new MystartItem();
				AffairsMini mini = (AffairsMini) mPage.getLists().get(i);
				item.setId(mini.id + "");
				item.setTime(mini.time);
				item.setTitle(mini.title);
				item.setType(mini.type);
				list.add(item);
			}
		}
		else if (tag == EAPIConsts.HomeReqType.HOME_REQ_GET_MY_KNOWLEDGE) {
			MGetMyKnowledge getMyKnowledge = (MGetMyKnowledge) object;
			mPage = getMyKnowledge.getJtPage();
			if (mState == STATE_REFRESH) {
				list.clear();// 如果当前刷新中，则清除之前数据
			}

			for (int i = 0; i < mPage.getLists().size(); i++) {
				MystartItem item = new MystartItem();
				KnowledgeMini mini = (KnowledgeMini) mPage.getLists().get(i);
				item.setId(mini.getmID() + "");
				item.setTime(mini.getTime());
				item.setTitle(mini.getmTitle());
				item.setUrl(mini.getmUrl());
				list.add(item);
			}
		}
		count = list.size();
		adapter.setData(list);
		adapter.notifyDataSetChanged();

		stopLoading();
	}

	public class MystartItem {
		private String title;
		private String time;
		private String id;
		private int type; // 事务类型，只有事务有
		private String url;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public void setType(int type) {
			this.type = type;
		}

		public int getType() {
			return type;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}
	}

	public class MystartAdapter extends BaseAdapter {

		private Context mContext;
		private List<MystartItem> mData;

		public MystartAdapter(Context context) {
			this.mContext = context;
			mData = new ArrayList<MystartItem>();
		}

		public void setData(List<MystartItem> mData) {
			this.mData = mData;
		}

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public Object getItem(int i) {
			return mData.get(i);
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public View getView(final int position, View convertView, final ViewGroup parent) {
			ItemHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.mystart_frg_requirement_listview_item, parent, false);
				holder = new ItemHolder();
				holder.mTitle = (TextView) convertView.findViewById(R.id.txtMystart_listitem_title);
				holder.mTime = (TextView) convertView.findViewById(R.id.txtMystartPublishTime);
				convertView.setTag(holder);
			}
			else {
				holder = (ItemHolder) convertView.getTag();
			}

			// Retrieve the data holder
			final MystartItem dataHolder = mData.get(position);
			holder.mTitle.setText(dataHolder.getTitle());
			holder.mTime.setText(dataHolder.getTime());
			// ApolloUtils.getImageFetcher((Activity)
			// mContext).loadHomeImage(dataHolder.getmImageUrl(), holder.mImg);
			return convertView;
		}

		private class ItemHolder {
			public TextView mTitle;
			public TextView mTime;
		}
	}

	private void addLists(int n) {
		n += list.size();
		for (int i = list.size(); i < n; i++) {
			MystartItem imItemData = new MystartItem();
			switch (mType) {
			case TYPE_REQUIREMENT:
				imItemData.setTitle("需求标题" + i);
				break;
			case TYPE_AFFAIR:
				imItemData.setTitle("事务标题" + i);
				break;
			case TYPE_KNOWLEDGE:
				imItemData.setTitle("知识标题" + i);
				break;
			}

			imItemData.setTime(i + "小时前");
			imItemData.setId(i + "");
			list.add(imItemData);
		}
	}

	public void stopLoading() {
		mState = STATE_NORMAL;
		swipeLayout.setRefreshing(false);
		mvProgressBar.setVisibility(View.GONE);

		if (mPage != null && !mPage.hasMore()) {
			mvTextView.setText("没有更多");
		}
		else {
			mvTextView.setText("更多");
		}
	}

	// 触发下拉刷新
	public void onRefresh() {
		if (mState == STATE_NORMAL) {
			if (startGetData(0))
				this.mState = STATE_REFRESH;
			else
				stopLoading();
		}
	}

	/** 获取页数据 */
	public boolean startGetData(int pageIndex) {
		boolean ret = false;
		switch (this.mType) {
		case TYPE_REQUIREMENT: {
			try {
				// ret = HomeReqUtil.getListRequirementByUserID(getActivity(),
				// this, mHandler, userID, pageIndex, 20);
				ret = DemandReqUtil.getMyNeedList(getActivity(), this, 0, 20, pageIndex, 2, "", null);
			} catch (Exception e) {

			}
		}
			break;
		case TYPE_AFFAIR:
			ret = HomeReqUtil.getMyAffair(getActivity(), this, mHandler, pageIndex, 20);
			break;
		case TYPE_KNOWLEDGE:
			ret = HomeReqUtil.getMyKnowledge(getActivity(), this, mHandler, pageIndex, 20);
			break;
		}
		return true;
	}

	private void loadMoreData() { // 加载更多数据
		if (mPage != null && !mPage.hasMore())
			return;

		if (mState == STATE_NORMAL) {
			mvProgressBar.setVisibility(View.VISIBLE);
			mvTextView.setText("正在加载");
			this.mState = STATE_GETMORE;
			int nowIndex = 0;
			if (mPage != null)
				nowIndex = mPage.getIndex() + 1;
			if (!startGetData(nowIndex))
				stopLoading();

		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

		Log.i(TAG, "firstVisibleItem=" + firstVisibleItem + "\nvisibleItemCount=" + visibleItemCount + "\ntotalItemCount" + totalItemCount);
		lastItem = firstVisibleItem + visibleItemCount - 1; // 减1是因为上面加了个addFooterView
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		Log.i(TAG, "scrollState=" + scrollState);
		// 下拉到空闲是，且最后一个item的数等于数据的总数时，进行更新
		if (lastItem == count && scrollState == this.SCROLL_STATE_IDLE) {
			Log.i(TAG, "拉到最底部");
			moreView.setVisibility(view.VISIBLE);
			loadMoreData();
		}
	}

}
