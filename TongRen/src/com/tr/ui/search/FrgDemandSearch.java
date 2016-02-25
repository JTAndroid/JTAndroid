package com.tr.ui.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tr.R;
import com.tr.api.DemandReqUtil;
import com.tr.model.demand.NeedItemData;
import com.tr.model.demand.NeedItemListItem;
import com.tr.model.obj.SearchResult;
import com.tr.model.page.JTPage;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.demand.util.ChooseDataUtil;
import com.utils.common.ViewHolder;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.time.TimeUtil;

/**
 * 首页-搜索进去
 * 
 * @ClassName: FrgSearch.java
 * @Description: TODO(用一句话描述该文件做什么)
 * @author xuxinjian
 * @version V1.0
 * @Date 2014-4-30 上午9:02:54
 */

public class FrgDemandSearch extends JBaseFragment implements IBindData,
		SwipeRefreshLayout.OnRefreshListener, OnScrollListener {
	private XListView mListView;

	public static String mCurKeyword = "";
//
	private View moreView; // 加载更多页面
	private TextView mvTextView;
	private View mvProgressBar;

	private int lastItem;
	private int count;
	private JTPage mPage;
	private int mState = 0;// 0-正常状态 1-获取更多中 2-刷新中
//	private SwipeRefreshLayout swipeLayout;
	private List<NeedItemData> list = new ArrayList<NeedItemData>();

	private DemandResultAdapter demandAdapter;
	private boolean isSearch = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mCurKeyword = "";
		View view = inflater.inflate(R.layout.search_frg, container, false);

//		swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.search_frg_requirement_swipe_container);
//		swipeLayout.setOnRefreshListener(this);
//		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
//				android.R.color.holo_green_light,
//				android.R.color.holo_orange_light,
//				android.R.color.holo_red_light);

		//上拉加载
				moreView = inflater.inflate(R.layout.pulldown_footer, null);

				this.mvTextView = (TextView) moreView
						.findViewById(R.id.pulldown_footer_text);
				this.mvProgressBar = (View) moreView
						.findViewById(R.id.pulldown_footer_loading);
				

		mListView = (XListView) view
				.findViewById(R.id.search_frg_requirement_listview);

		demandAdapter = new DemandResultAdapter(this.getActivity());

		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				NeedItemData item = list.get(arg2-1);
				ENavigate.startNeedDetailsActivity(getActivity(),
						item.demandId, 1);
			}

		});

		// addLists(20);
		demandAdapter.setData(list);
		mListView.setAdapter(demandAdapter);
		mListView.setOnScrollListener(this); // 设置listview的滚动事件
		count = list.size();
		mListView.showFooterView(false);
		// 设置xlistview可以加载、刷新
		mListView.setPullLoadEnable(true);
		mListView.setPullRefreshEnable(true);
		mState = STATE_NORMAL;
		mListView.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				/* 访问服务器 */
				loadMoreData();
			}

			@Override
			public void onLoadMore() {
				loadMoreData();
			}
		});
		
		mListView.startRefresh();

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
			mListView.stopRefresh();
			mListView.stopLoadMore();
		/*if (this.isHasPause()) {
			stopLoading();
			return;
		}*/
		if (object == null) {
			return;
		}
		if (tag == EAPIConsts.demandReqType.demand_getMyNeedList) {
			NeedItemListItem item = (NeedItemListItem) object;
			mPage = item.getJtPage();

			if (mPage == null) {
				return;
			}
			// ArrayList<NeedItemData> lists = jtPage2.getLists();
//			if (/*mPage.getIndex() == 0 */mState == STATE_REFRESH) {
//				list.clear();
//			}
			
			if ((mPage != null) && (mPage.getLists() != null)) {
				for (int i = 0; i < mPage.getLists().size(); i++) {
					list.add((NeedItemData) mPage.getLists().get(i));
				}
				demandAdapter.setData(list);
				count = list.size();
				demandAdapter.notifyDataSetChanged();
			}
			
		}

		// 服务器改排序之前 先用着 ，服务器改了之后注释掉
		/* Collections.sort(list, new SortByTime()); */

		
	}

	/**
	 * 按时间从近到远排序
	 * 
	 * @author gushi
	 * 
	 */

	class SortByTime implements Comparator {
		public int compare(Object o1, Object o2) {
			SearchResult s1 = (SearchResult) o1;
			SearchResult s2 = (SearchResult) o2;
			int sortInt = s2.getTime().compareTo(s1.getTime());
			return sortInt;
		}
	}

	public class DemandResultAdapter extends BaseAdapter {

		private Context mContext;
		private List<NeedItemData> mData;
		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

		public DemandResultAdapter(Context context) {
			this.mContext = context;
			mData = new ArrayList<NeedItemData>();
		}

		public void setData(List<NeedItemData> mData) {
			this.mData = mData;
		}

		@Override
		public int getCount() {
			return mData == null ? 0 : mData.size();
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
		public View getView(final int position, View convertView,
				final ViewGroup parent) {
			NeedItemData needItem = mData.get(position);
			if (convertView == null) {
				convertView = View.inflate(getActivity(),
						R.layout.demand_me_need_listview, null);
			}
			ImageView typeIv = ViewHolder.get(convertView, R.id.typeIv);
			TextView titleTv = ViewHolder.get(convertView, R.id.titleTv);
			TextView priceTv = ViewHolder.get(convertView, R.id.priceTv);
			TextView timeTv = ViewHolder.get(convertView, R.id.timeTv);

			timeTv.setText(TimeUtil.TimeFormat(needItem.createTime));// 时间
			if (needItem.title != null
					&& !TextUtils.isEmpty(needItem.title.value)) {
				titleTv.setText(needItem.title.value);// 标题对象
			}
			if (needItem.amount != null) {
				priceTv.setText(needItem.amount.getAmountData());
			}
			if (needItem.demandType == ChooseDataUtil.CHOOSE_type_OutInvestType) {
				typeIv.setImageResource(R.drawable.demand_me_need01);
			} else {
				typeIv.setImageResource(R.drawable.demand_me_need02);
			}
			return convertView;
		}
	}

	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	// 触发下拉刷新
	public void onRefresh() {
		if (mState == STATE_NORMAL) {
			startGetData(0,false);
		}
	}

	/**
	 * 获取页数据
	 */
	public boolean startGetData(int pageIndex,boolean bool) {
		if (pageIndex == 0) {
			mState = STATE_REFRESH;
			list.clear();
		}
		this.isSearch = bool;
//		swipeLayout.setRefreshing(true);
		
		DemandReqUtil.getMyNeedList(getActivity(), this, pageIndex, 20, 0, 1,
				mCurKeyword, null);
		return true;
	}

	private void loadMoreData() { // 加载更多数据
		if (mPage != null && !mPage.hasMore())
			return;

			mvProgressBar.setVisibility(View.VISIBLE);
			mvTextView.setText("正在加载");
			int nowIndex = 0;
			if (mPage != null)
				nowIndex = mPage.getIndex() + 1;
			startGetData(nowIndex,false);

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		Log.i(TAG, "firstVisibleItem=" + firstVisibleItem
				+ "\nvisibleItemCount=" + visibleItemCount + "\ntotalItemCount"
				+ totalItemCount);
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
