package com.tr.ui.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tr.R;
import com.tr.api.HomeReqUtil;
import com.tr.model.home.MGetSearchList;
import com.tr.model.obj.DynamicNews;
import com.tr.model.obj.SearchResult;
import com.tr.model.page.JTPage;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.conference.myhy.utils.Utils;
import com.tr.ui.home.InviteFriendByQRCodeActivity;
import com.tr.ui.organization.create_clientele.ClientDetailsActivity;
import com.utils.common.Util;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.log.ToastUtil;
import com.utils.string.StringUtils;

/**
 * 首页-搜索进去
 * 
 * @ClassName: FrgSearch.java
 * @Description: TODO(用一句话描述该文件做什么)
 * @author xuxinjian
 * @version V1.0
 * @Date 2014-4-30 上午9:02:54
 */

public class FrgSearch extends JBaseFragment implements IBindData,SwipeRefreshLayout.OnRefreshListener {
	private XListView mListView;
	private List<SearchResult> list = new ArrayList<SearchResult>();
	private SearchResultAdapter adapter;

	public final static int TYPE_REQUIREMENT_OUT = 1;// 投资需求
	public final static int TYPE_REQUIREMENT_IN = 2;// 融资需求
	public final static int TYPE_MEMBER = 4;// 会员
	public final static int TYPE_FRIEND = 5;// 好友
//	public final static int TYPE_CONTACT = 6;// 人脉
	public final static int TYPE_KNOWLEDGE = 8;// 知识
	public final static int TYPE_METTING = 9;// 会议
	public final static int TYPE_DEMAND= 5;//需求
	public final static int TYPE_ORGANDCUSTOMER= 6;//组织和客户

	private int mType = TYPE_REQUIREMENT_OUT; // 0-全部；1-投资需求；2-融资需求;3-机构；4-用户；5-我的好友；6-我的机构客户；7-我的人脉；8-全部知识;9-会议
	public static String mCurKeyword = "";

	private View moreView; // 加载更多页面
	private TextView mvTextView;
	private View mvProgressBar;

	private int lastItem;
	private int count;
	private JTPage mPage;
	private int mState = 0;// 0-正常状态 1-获取更多中 2-刷新中
//	private SwipeRefreshLayout swipeLayout;
	DisplayImageOptions options;
	private boolean isSearch = true;

	private final static String mRecordID = "imrecord";

	public void setType(int type) {
		mType = type;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		mCurKeyword = "";
		View view = inflater.inflate(R.layout.search_frg, container, false);

//		swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.search_frg_requirement_swipe_container);
//		swipeLayout.setOnRefreshListener(this);
//		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
//				android.R.color.holo_green_light,
//				android.R.color.holo_orange_light,
//				android.R.color.holo_red_light);

		moreView = inflater.inflate(R.layout.pulldown_footer, null);
		this.mvTextView = (TextView) moreView.findViewById(R.id.pulldown_footer_text);
		this.mvProgressBar = (View) moreView.findViewById(R.id.pulldown_footer_loading);

		mListView = (XListView) view.findViewById(R.id.search_frg_requirement_listview);
		
		
		
		
		adapter = new SearchResultAdapter(this.getActivity());
		// list = IMCacheUtils.readIMRecordList(mRecordID);

		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (arg2 >= list.size()) {
					loadMoreData();
					return;
				}
				SearchResult item = list.get(arg2 -1);
				switch (mType) {
				case TYPE_REQUIREMENT_OUT:
				case TYPE_REQUIREMENT_IN:
					break;
				case TYPE_MEMBER:
					if(2 == item.getType()){//人脉
						Long idLong = 0L;
						if(!TextUtils.isEmpty(item.getId())){
							idLong = Long.valueOf(item.getId());
						}
//						ENavigate.startContactsDetailsActivity(getActivity(), 2, idLong, 0x123);
						ENavigate.startRelationHomeActivity(getActivity(), String.valueOf(idLong),false,ENavConsts.TYPE_CONNECTIONS_HOME_PAGE);
					}else{
						Intent intent = new Intent(getActivity(), InviteFriendByQRCodeActivity.class);
						intent.putExtra("friendId", item.getSource());
						getActivity().startActivity(intent);
					}
					break;
				case TYPE_ORGANDCUSTOMER:
					//组织和客户跳转  ：0客户　1组织  2 大数据推的组织"
					if(item.getType() == 1||item.getType() == 2){
						Intent intent = new Intent(getActivity(), InviteFriendByQRCodeActivity.class);
						intent.putExtra("type", 1);
						intent.putExtra("OrgId", item.getId());
						getActivity().startActivity(intent);
//						ENavigate.startOrgMyHomePageActivity(getActivity(), Long.valueOf(item.getId()), 0, true, ENavConsts.type_details_org);
					}else if(item.getType() == 0 ){
						/*Intent intent = new Intent(getActivity(), Client_DetailsActivity.class);
						intent.putExtra("customerId", item.getId());
						intent.putExtra("label", 3);
						getActivity().startActivity(intent);*/
						ENavigate.startClientDedailsActivity(getActivity(),Long.valueOf(item.getId()));
					}
					break;
				case TYPE_KNOWLEDGE:
					// ENavigate.startKnowledgeDetailActivity(getActivity(),
					// item.getId());
					ENavigate.startKnowledgeOfDetailActivity(getActivity(),
							Integer.valueOf(item.getId().trim()), 1/*item.getType()*/,false);
					break;
				case TYPE_METTING:
					ENavigate.startSquareActivity(getActivity(),
							Long.valueOf(item.getId()),0);
					break;
				default:
					ENavigate.startNeedDetailsActivity(getActivity(),item.getId(), 1);
//					showToast("此功能尚未开放,敬请等待");
					break;
				}
			}

		});
		
		mListView.showFooterView(false);
		// 设置xlistview可以加载、刷新
		mListView.setPullLoadEnable(true);
		mListView.setPullRefreshEnable(true);

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
		mListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				return false;
			}
		});

		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});
		
		
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.hy_home_tile_meeting_default)
		.showImageForEmptyUri(R.drawable.hy_home_tile_meeting_default)
		.showImageOnFail(R.drawable.hy_home_tile_meeting_default)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(false)
		.build();

		// addLists(20);

		adapter.setData(list);
//		mListView.addFooterView(moreView);
		mListView.setAdapter(adapter);
//		mListView.setOnScrollListener(this); // 设置listview的滚动事件
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
		//XlistView结束刷新时状态复位
		mListView.stopLoadMore();
		mListView.stopRefresh();
		if (this.isHasPause()) {
			stopLoading();
			return;
		}

		if (object == null) {
			stopLoading();
			return;
		}

/*<<<<<<< HEAD
		if (tag == EAPIConsts.HomeReqType.HOME_REQ_GET_SEARCH_LIST||tag == EAPIConsts.HomeReqType.HOME_REQ_GET_SEARCH_LIST_MEETING || 
				tag == EAPIConsts.HomeReqType.HOME_REQ_GET_SEARCH_INDEX_LIST) {
=======*/
		if (tag == EAPIConsts.HomeReqType.HOME_REQ_GET_SEARCH_LIST
				|| tag == EAPIConsts.HomeReqType.HOME_REQ_GET_SEARCH_LIST_PERSON
				|| tag == EAPIConsts.HomeReqType.HOME_REQ_GET_SEARCH_LIST_ORG
				|| tag == EAPIConsts.HomeReqType.HOME_REQ_GET_SEARCH_LIST_KNOWLEDGE
				|| tag == EAPIConsts.HomeReqType.HOME_REQ_GET_SEARCH_LIST_MEET
				|| tag == EAPIConsts.HomeReqType.HOME_REQ_GET_SEARCH_LIST_DEMEND
				|| tag == EAPIConsts.HomeReqType.HOME_REQ_GET_SEARCH_LIST_MEETING) {
			MGetSearchList searchList = (MGetSearchList) object;

			mPage = searchList.getJtPage();
			if (mPage!=null) {
					
				if(/*mPage.getIndex() == 0 ||*/ mState == STATE_REFRESH){
					list.clear();// 如果当前刷新中，则清除之前数据
				}
				if(mPage.getLists() == null || mPage.getLists().size() <= 0){
					ToastUtil.showToast(getActivity(), "暂无搜索结果");
					mListView.setPullLoadEnable(false);
				}else {
					mListView.setPullLoadEnable(true);
				}
			}
			
			if ((mPage != null) && (mPage.getLists() != null)) {
				for (int i = 0; i < mPage.getLists().size(); i++) {
					list.add((SearchResult) mPage.getLists().get(i));
				}
				count = list.size();

				// 服务器改排序之前 先用着 ，服务器改了之后注释掉
				/*Collections.sort(list, new SortByTime());*/

				adapter.setData(list);
				adapter.notifyDataSetChanged();
			}
		}
	

		stopLoading();
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

	public class SearchResultAdapter extends BaseAdapter {

		private Context mContext;
		private List<SearchResult> mData;
		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

		public SearchResultAdapter(Context context) {
			this.mContext = context;
			mData = new ArrayList<SearchResult>();
		}

		public void setData(List<SearchResult> mData) {
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
			final ItemHolder holder;
			// Retrieve the data holder
			final SearchResult dataHolder = mData.get(position);
			final String imageUrl = dataHolder.getImage();
			if (convertView == null) {
				holder = new ItemHolder();
				if(mType == TYPE_MEMBER || mType == TYPE_ORGANDCUSTOMER){
					convertView = View.inflate(mContext, R.layout.home_search_relation_item_new, null);
					holder.imageIv = (ImageView) convertView.findViewById(R.id.searchAvatarIv);
					holder.mTime = (TextView) convertView.findViewById(R.id.contactTimeTv);
					holder.mTitle = (TextView) convertView.findViewById(R.id.contactNameTv);
				}else if (mType == TYPE_METTING || mType == TYPE_DEMAND) {
					convertView = View.inflate(mContext, R.layout.home_search_relation_item_new, null);
					holder.mTitle = (TextView) convertView.findViewById(R.id.contactNameTv);
					holder.contentTv = (TextView) convertView.findViewById(R.id.contactTimeTv);
					holder.imageIv = (ImageView) convertView.findViewById(R.id.searchAvatarIv);
				}else{
					convertView = LayoutInflater.from(mContext).inflate(R.layout.search_frg_listview_item, parent, false);
					holder.mTitle = (TextView) convertView.findViewById(R.id.txtsearch_listitem_title);
					holder.contentTv = (TextView) convertView.findViewById(R.id.contentTv);
					holder.mTime = (TextView) convertView.findViewById(R.id.txtsearchPublishTime);
				}
				convertView.setTag(holder);
			} else {
				holder = (ItemHolder) convertView.getTag();
			}
			if (TextUtils.isEmpty(dataHolder.getTitle())|dataHolder.getTitle().contains("null"))
				holder.mTitle.setText("");
			else
				holder.mTitle.setText(dataHolder.getTitle());
			
			/** 好友/人脉 */
			if(mType == TYPE_MEMBER){
				//设置标签图片
				Drawable drawable = null;
				if (dataHolder != null && dataHolder.getType() == 1) {
					drawable = getResources().getDrawable(R.drawable.contactusertag);
				} else {
					drawable = getResources().getDrawable(R.drawable.contactpeopletag);
				}
				if (drawable != null) {
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
					if (holder.mTitle != null) {
						holder.mTitle.setCompoundDrawables(drawable,null, null, null);
						holder.mTitle.setCompoundDrawablePadding(10);
					}
				}
				
				Util.initAvatarImage(mContext, holder.imageIv, "", imageUrl, 0, 1);
				holder.mTime.setText(dataHolder.getCompany());
			}else if(mType == TYPE_ORGANDCUSTOMER){
				//设置标签图片
				Drawable drawable = null;
				if (dataHolder != null && dataHolder.getType() == 0) {
					//客户标签
					drawable = getResources().getDrawable(R.drawable.contactclienttag);
				} else {
					//组织标签
					drawable = getResources().getDrawable(R.drawable.contactorganizationtag);
				}
				if (drawable != null) {
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
					if (holder.mTitle != null) {
						holder.mTitle.setCompoundDrawables(drawable,null, null, null);
						holder.mTitle.setCompoundDrawablePadding(10);
					}
				}
				Util.initAvatarImage(mContext, holder.imageIv, "", imageUrl, 0, 2);
				holder.mTime.setText(dataHolder.getIndustrys());
			}else if (TYPE_METTING == mType) {
				holder.mTitle.setCompoundDrawables(null, null, null, null);
				holder.imageIv.setImageResource( R.drawable.meeting_logo_a);
//				com.tr.image.ImageLoader.load(holder.imageIv, imageUrl, R.drawable.meeting_logo_a);
				holder.contentTv.setVisibility(View.VISIBLE);
				holder.contentTv.setText(dataHolder.getContent());
			}else if (TYPE_KNOWLEDGE == mType) {
				if (StringUtils.isEmpty(dataHolder.getContent())) {
					holder.contentTv.setVisibility(View.GONE);
				}else {
					holder.contentTv.setVisibility(View.VISIBLE);
					holder.contentTv.setText(dataHolder.getContent());
				}
			}else if(mType == TYPE_DEMAND){
				//设置标签图片
//				Drawable drawable = null;
//				if (dataHolder != null && dataHolder.getType() == 0) {
//					//客户标签
//					drawable = getResources().getDrawable(R.drawable.contactclienttag);
//				} else {
//					//组织标签
//					drawable = getResources().getDrawable(R.drawable.contactorganizationtag);
//				}
//				if (drawable != null) {
//					drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
//					if (holder.mTitle != null) {
//						holder.mTitle.setCompoundDrawables(drawable,null, null, null);
//						holder.mTitle.setCompoundDrawablePadding(10);
//					}
//				}
				if (dataHolder.getType() == 2) {// 融资事件
					initMyimage(holder.imageIv, "融资", 2);
//					people_type.setImageResource(R.drawable.demand_me_need02);
				}else{// 投资事件
					initMyimage(holder.imageIv, "投资", 0);
//					people_type.setImageResource(R.drawable.demand_me_need01);
				}
				holder.mTitle.setCompoundDrawables(null, null, null, null);
//				Util.initAvatarImage(mContext, holder.imageIv, "", imageUrl, 0, 2);
				if (TextUtils.isEmpty(dataHolder.getIndustrys())|dataHolder.getIndustrys().contains("null"))
					holder.contentTv.setText("");
				else
					holder.contentTv.setText(dataHolder.getIndustrys());
			}
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					switch (mType) {
					case TYPE_MEMBER:
						Long idLong = 0L;
						if(!TextUtils.isEmpty(dataHolder.getId())){
							idLong = Long.valueOf(dataHolder.getId());
						}
						if(2 == dataHolder.getType()){//人脉
							ENavigate.startRelationHomeActivity(mContext, String.valueOf(idLong),false,ENavConsts.TYPE_CONNECTIONS_HOME_PAGE);
//							ENavigate.startContactsDetailsActivity(mContext, 2, idLong, 0x123);
						}else{
//							ENavigate.startRelationHomeActivity(mContext, String.valueOf(idLong), true, ENavConsts.type_details_other);
							Intent intent = new Intent(mContext, InviteFriendByQRCodeActivity.class);
							intent.putExtra("friendId", dataHolder.getSource());
							mContext.startActivity(intent);
						}
						break;
					case TYPE_ORGANDCUSTOMER:
						//组织和客户跳转  ：0客户　1组织  2 大数据推的组织"
						if(dataHolder.getType() == 1||dataHolder.getType() == 2){
							Intent intent = new Intent(mContext, InviteFriendByQRCodeActivity.class);
							intent.putExtra("type", 1);
							intent.putExtra("OrgId", dataHolder.getId());
							mContext.startActivity(intent);
						}else if(dataHolder.getType() == 0 ){
							ENavigate.startClientDedailsActivity(mContext,Long.valueOf(dataHolder.getId()));
						}
						break;
					case TYPE_KNOWLEDGE:
						ENavigate.startKnowledgeOfDetailActivity(getActivity(),
								Integer.valueOf(dataHolder.getId().trim()), dataHolder.getType(),false);
						break;
					case TYPE_METTING:
						ENavigate.startSquareActivity(mContext,
								Long.valueOf(dataHolder.getId()),0);
						break;
					default:
						ENavigate.startNeedDetailsActivity(mContext,dataHolder.getId(), 1);
//						showToast("此功能尚未开放,敬请等待");
						break;
					}
				}
			});
			return convertView;
		}

		private class ItemHolder {
			public TextView mTitle;
			public TextView contentTv;
			public TextView mTime;
			public ImageView imageIv;
		}
	}
	private void initMyimage(ImageView avatarIv, String name, int type) {
		Bitmap bm = null;
		int resid = 0;
		if (type == 2) {// 融资
			resid = R.drawable.no_avatar_but_gender;// 粉色
		} else {// 投资
			resid = R.drawable.no_avatar_client_organization;// 浅蓝色
		}
		bm = Util.createBGBItmap(getActivity(), resid, R.color.avatar_text_color, R.dimen.avatar_text_size, name);
		avatarIv.setImageBitmap(bm);
	}
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
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

	public void stopLoading() {
//		mState = STATE_NORMAL;
////		swipeLayout.setRefreshing(false);
//		mvProgressBar.setVisibility(View.GONE);
//
//		if (mPage != null && !mPage.hasMore()) {
//			//moreView.setVisibility(View.GONE);
//			if(list.size() <= 0 && this.isSearch){
//				mvTextView.setText("暂无搜索结果");
//			}else{
//				mvTextView.setText("没有更多");
//			}
//			moreView.setClickable(false);
//		} else {
//			moreView.setVisibility(View.VISIBLE);
//			mvTextView.setText("更多");
//			moreView.setClickable(true);
//		}
	}

	// 触发下拉刷新
	public void onRefresh() {
		if (mState == STATE_NORMAL) {
			if (startGetData(0,false))
				this.mState = STATE_REFRESH;
			else
				stopLoading();
		}
	}

	/**
	 * 获取页数据
	 */
	public boolean startGetData(int pageIndex,boolean bool) {
		if (pageIndex == 0) {
			mState = STATE_REFRESH;
		}else{
			mState = STATE_GETMORE;
		}
		this.isSearch = bool;
//		swipeLayout.setRefreshing(true);
		HomeReqUtil.getSearchList(getActivity(), this, null, mCurKeyword,this.mType, pageIndex, 20);
		return true;
	}

	private void loadMoreData() { // 加载更多数据
		if (mPage != null && !mPage.hasMore())
			return;

//		if (mState == STATE_NORMAL) {
//			mvProgressBar.setVisibility(View.VISIBLE);
//			mvTextView.setText("正在加载");
//			this.mState = STATE_GETMORE;
			int nowIndex = 0;
			if (mPage != null)
				nowIndex = mPage.getIndex() + 1;
			if (!startGetData(nowIndex,false))
				stopLoading();

		}
//	}

//	@Override
//	public void onScroll(AbsListView view, int firstVisibleItem,
//			int visibleItemCount, int totalItemCount) {
//
//		Log.i(TAG, "firstVisibleItem=" + firstVisibleItem
//				+ "\nvisibleItemCount=" + visibleItemCount + "\ntotalItemCount"
//				+ totalItemCount);
//		lastItem = firstVisibleItem + visibleItemCount - 1; // 减1是因为上面加了个addFooterView
//	}
//
//	@Override
//	public void onScrollStateChanged(AbsListView view, int scrollState) {
//		Log.i(TAG, "scrollState=" + scrollState);
//		// 下拉到空闲是，且最后一个item的数等于数据的总数时，进行更新
//		if (lastItem == count && scrollState == this.SCROLL_STATE_IDLE) {
//			Log.i(TAG, "拉到最底部");
//			moreView.setVisibility(view.VISIBLE);
//			loadMoreData();
//		}
//	}

}
