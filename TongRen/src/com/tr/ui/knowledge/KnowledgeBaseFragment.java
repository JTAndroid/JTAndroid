package com.tr.ui.knowledge;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tr.App;
import com.tr.R;
import com.tr.api.KnowledgeReqUtil;
import com.tr.model.knowledge.Column;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.knowledge.business.KonwledgeSquareReadedKnowledgeSharedPreferencesBusiness;
import com.tr.ui.widgets.MenuItemView;
import com.tr.ui.widgets.MyAnimations;
import com.tr.ui.widgets.MyAnimations.OnItemClickListener;
import com.utils.http.EAPIConsts.KnoReqType;
import com.utils.http.IBindData;
import com.utils.image.AnimateFirstDisplayListener;
import com.utils.string.StringUtils;
import com.utils.time.TimeUtil;

public class KnowledgeBaseFragment extends JBaseFragment implements IBindData , SwipeRefreshLayout.OnRefreshListener{
	public static final String TAG = "KnowledgeBaseFragment";
	
	private KnowledgeSquareActivity knowledgeSquareActivity;
//	private KnowledgeSquareActivity knowledgeSquareActivity;
	
	private Column column;
	
	private String fragmentName;
	private XListView mainLv;

	private ImageView imageIv;
	private TextView titleTv;
	private TextView contentTv;

	private MainAdapter mainAdapter;
	private ArrayList<KnowledgeMini2> knowledgeList;

	private long columnId;
	private int source;
	private int columnType;

	// 刷新,更多 start......
	private int index = -1;
	private int size = 20;
	private int total;
	
	/**	上一次获取的知识实际个数	用于判断有没有更多知识了  */
	private int lastActualSize;
	
	public final static int STATE_NORMAL = 0; // 正常
	public final static int STATE_GETMORE = 1; // 加载更多
	public final static int STATE_REFRESH = 2; // 刷新中
	
	private int mState = STATE_NORMAL; 
//	private SwipeRefreshLayout swipeLayout;
	
	private View moreView; // 加载更多页面
	private TextView mvTextView;
	private View mvProgressBar;
	
	private int lastItem;
	private int count;// 知识缩略对象的总数
	// 刷新,更多 end......
	
	// 圆形菜单 start....
	private MenuItemView myViewRB;//圆形菜单
	private ImageView imgPlusRB;//圆形菜单按钮
	
	// 圆形菜单 end....
	
	private DisplayImageOptions options;
	
	

	public String getFragmentName() {
		return column.getColumnname();
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		knowledgeSquareActivity = (KnowledgeSquareActivity) activity;
	}

	public Column getColumn() {
		return column;
	}

	public void setColumn(Column column) {
		this.column = column;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View view = initComponent(inflater, container);
//		initSimulateData();
		initData();
		
		return view;
	}
	
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		setMenuItemView();
		
	}

	

	@Override
	public void onResume() {
		super.onResume();
//		onRefresh();
		// 通知刷新 看过的知识 置灰
		mainAdapter.notifyDataSetChanged();
	}


	private View initComponent(LayoutInflater inflater, ViewGroup root) {
		View view = inflater.inflate(R.layout.fragment_knowledge_base, null);

		mainLv = (XListView) view.findViewById(R.id.mainLv);
		
//		swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.knowledgeListSrl);
//		swipeLayout.setOnRefreshListener(this);
//		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,android.R.color.holo_green_light,android.R.color.holo_orange_light,android.R.color.holo_red_light);

		moreView = inflater.inflate(R.layout.pulldown_footer, null);
		mvTextView = (TextView) moreView.findViewById(R.id.pulldown_footer_text);
		mvProgressBar = (View) moreView.findViewById(R.id.pulldown_footer_loading);
		
		mainAdapter = new MainAdapter(knowledgeSquareActivity, knowledgeList);
//		mainLv.addFooterView(moreView);
		mainLv.setAdapter(mainAdapter);
		mainLv.setOnItemClickListener(mOnItemClickListener);
//		mainLv.setOnScrollListener(this);
		mainLv.showFooterView(false);
		// 设置xlistview可以加载、刷新
		mainLv.setPullLoadEnable(true);
		mainLv.setPullRefreshEnable(true);

		mainLv.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				/* 访问服务器 */
				KnowledgeBaseFragment.this.onRefresh();
			}

			@Override
			public void onLoadMore() {
				loadMoreData();
			}
		});
		mainLv.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				return false;
			}
		});

		mainLv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});
		

		return view;
	}
	
	

	private void initSimulateData() {

		knowledgeList = new ArrayList<KnowledgeMini2>();
		for (int i = 0; i < 20; i++) {
			KnowledgeMini2 knowledgeMini2 = new KnowledgeMini2();

			knowledgeList.add(knowledgeMini2);
		}

		mainAdapter.setKnowledgeList(knowledgeList);
		mainAdapter.notifyDataSetChanged();

	}

	private void initData() {
		source = 0;
		if(column != null){
			columnId = column.getId();
			columnType = column.getType();	
		}
		
		if (knowledgeList != null) {
			count = knowledgeList.size();
		} else {
			knowledgeList = new ArrayList<KnowledgeMini2>();
		}
		
		mState = STATE_NORMAL;

		onRefresh();
		
		
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.knowledge_square_default_image)
		.showImageForEmptyUri(R.drawable.knowledge_square_default_image)
		.showImageOnFail(R.drawable.knowledge_square_default_image)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(false)
		.build();
	}
	
	private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (knowledgeList != null && knowledgeList.size() > 0 && position < knowledgeList.size()) {
				KnowledgeMini2 knowledgeMini2 = knowledgeList.get(position - 1);
				ENavigate.startKnowledgeOfDetailActivitys(knowledgeSquareActivity, knowledgeMini2.id, knowledgeMini2.type,false);
			}
			if (position >= knowledgeList.size()) {
				loadMoreData();
				return;
			}
		}

	};
	
	
	
	/**
	 * //圆形菜单
	 */
	private void setMenuItemView() {
		//出现菜单
				myViewRB = (MenuItemView) this.getView().findViewById(R.id.myViewRB);
				this.getView().findViewById(R.id.relLayRB).setOnClickListener(new OnClickListener(){
					public void onClick(View v) {
						int vid = v.getId();
						switch (vid) {
						case R.id.relLayRB:
							MyAnimations.getRotateAnimation(imgPlusRB, 0f, 270f, 300);
							MyAnimations.startAnimations(KnowledgeBaseFragment.this.getActivity(),onPathMenuItemClickListener, myViewRB, 300);
							break;
						}
					}
				});
				
				
				
//				WindowManager wm = (WindowManager) getActivity()
//	                    .getSystemService(Context.WINDOW_SERVICE);
//		    	 
//				int radiussize= wm.getDefaultDisplay().getWidth()/15*7;
//				radiussize=90;
				
				
				//圆形菜单的半径
				
				imgPlusRB = (ImageView)this.getView(). findViewById(R.id.imgPlusRB);
				myViewRB.addItem("全部");
				myViewRB.addItem("金桐脑");
				myViewRB.addItem("全平台");
				myViewRB.addItem("好友");
				myViewRB.addItem("自己");
	}
	
//	private int currentIndex=0;
	OnItemClickListener onPathMenuItemClickListener=new OnItemClickListener(){
		@Override
		public void onclick(int item) {
			String MSG = "onclick";
			Log.i(TAG, MSG + " item = " + item);
			if (source == item) {
				return;
			} else {
				source = item;
			}
			
			onRefresh();
		}
	};

	private int nowIndex = 0;

	class MainAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<KnowledgeMini2> knowledgeList;
		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

		public MainAdapter(Context context, ArrayList<KnowledgeMini2> knowledgeList) {
			super();
			this.context = context;
			if (knowledgeList != null) {
				this.knowledgeList = knowledgeList;
			} else {
				this.knowledgeList = new ArrayList<KnowledgeMini2>();
			}
		}

		public ArrayList<KnowledgeMini2> getKnowledgeList() {
			return knowledgeList;
		}

		public void setKnowledgeList(ArrayList<KnowledgeMini2> knowledgeList) {
			this.knowledgeList = knowledgeList;
		}

		@Override
		public int getCount() {
			return knowledgeList.size();
		}

		@Override
		public KnowledgeMini2 getItem(int position) {
			return knowledgeList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			String MSG = "getView()";
			ViewHolder viewHolder = null;

			if (convertView == null) {
				viewHolder = new ViewHolder();

				convertView = LayoutInflater.from(context).inflate(R.layout.item_knowledge_base_fragment_lv, null);
				viewHolder.imageIv = (ImageView) convertView.findViewById(R.id.imageIv);
				viewHolder.titleTv = (TextView) convertView.findViewById(R.id.titleTv);
				viewHolder.contentTv = (TextView) convertView.findViewById(R.id.contentTv);
				viewHolder.sourceTv = (TextView) convertView.findViewById(R.id.sourceTv);
				viewHolder.modifyTimeTv = (TextView) convertView.findViewById(R.id.modifyTimeTv);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			KnowledgeMini2 knowledgeMini2 = knowledgeList.get(position);
//			 viewHolder.imageIv.setBackgroundDrawable(Drawable.createFromPath(knowledgeMini2.getPic()));
			 viewHolder.titleTv.setText(knowledgeMini2.title);
			 viewHolder.contentTv.setText(knowledgeMini2.desc);
			 if(!StringUtils.isEmpty(knowledgeMini2.pic)){
//					try{
						viewHolder.imageIv.setVisibility(View.VISIBLE);
//						ApolloUtils.getImageFetcher(context).loadHomeImage(knowledgeMini2.getPic(), viewHolder.imageIv);
						ImageLoader.getInstance().displayImage(knowledgeMini2.pic, viewHolder.imageIv, options, animateFirstListener);
//					}catch(Exception e){
//						Log.e (TAG, MSG + " e = " + e );
//					}
				}
			 else {
				 viewHolder.imageIv.setVisibility(View.GONE);
			 }
			 
			 viewHolder.sourceTv.setText(knowledgeMini2.source);
			 viewHolder.modifyTimeTv.setText(TimeUtil.TimeFormat(knowledgeMini2.modifytime));
			 
//			 SharedPreferences sp = context.getSharedPreferences(App.share_knowledgeSquare_readed_knowledge, Activity.MODE_PRIVATE);
//			 boolean isReaded = sp.getBoolean(knowledgeMini2.id+"", false);
			 KonwledgeSquareReadedKnowledgeSharedPreferencesBusiness readedKnowledgeSharedPreferencesBusiness = new KonwledgeSquareReadedKnowledgeSharedPreferencesBusiness(context);
			 boolean isReaded = readedKnowledgeSharedPreferencesBusiness.queryKnowledgeState(knowledgeMini2.id + "");
//			 Log.i(TAG, MSG + "knowledgeMini2.getId() = " + knowledgeMini2.getId() +  " isReaded = " + isReaded);
			 if(isReaded){
				 viewHolder.titleTv.setTextColor(getResources().getColor(R.color.gray));
			 }
			 else {
				 viewHolder.titleTv.setTextColor(getResources().getColor(android.R.color.black));
			 }
			 

			return convertView;
		}

		class ViewHolder {
			ImageView imageIv;
			TextView titleTv;
			TextView contentTv;
			TextView sourceTv;
			TextView modifyTimeTv;
			
		}

	}

	
	
	
	/**  图片加载完成监听者  */
//	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
//
//		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
//
//		@Override
//		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//			if (loadedImage != null) {
//				ImageView imageView = (ImageView) view;
//				boolean firstDisplay = !displayedImages.contains(imageUri);
//				if (firstDisplay) {
//					FadeInBitmapDisplayer.animate(imageView, 500);
//					displayedImages.add(imageUri);
//				}
//			}
//		}
//	}
	
	
	
	
	
	
	
	@Override
	public void bindData(int tag, Object object) {
		String MSG = "bindData()";
		//XlistView结束刷新时状态复位
		 mainLv.stopLoadMore();
		 mainLv.stopRefresh();
		if (object == null) {
			return;
		}
		
		if (mState == STATE_REFRESH) {
			if (knowledgeList != null) {
				knowledgeList.clear(); // 如果当前刷新中，则清除之前数据
			}
		}

		if (KnoReqType.GetKnowledgeByColumnAndSource == tag) {
			
			Map<String, Object> hm = (Map<String, Object>) object;
			ArrayList<KnowledgeMini2> newKnowledgeList = (ArrayList<KnowledgeMini2>) hm.get("listKnowledgeMini");

			total = (Integer) hm.get("total");
			index = (Integer) hm.get("index");
			size = (Integer) hm.get("size");
			
			if(newKnowledgeList != null ){
				lastActualSize = newKnowledgeList.size();
			}
			if (total <= 20) {
				mainLv.setPullLoadEnable(false);
			}else {
				mainLv.setPullLoadEnable(true);
			}
			
			knowledgeList.addAll(newKnowledgeList);
			mainAdapter.setKnowledgeList(knowledgeList);
			mainAdapter.notifyDataSetChanged();

		}

		
		count = knowledgeList.size();
	}


//	@Override
//	public void onScrollStateChanged(AbsListView view, int scrollState) {
//		// 下拉到空闲是，且最后一个item的数等于数据的总数时，进行更新
//				if (lastItem == count && scrollState == this.SCROLL_STATE_IDLE) {
//					Log.i(TAG, "拉到最底部");
//					moreView.setVisibility(view.VISIBLE);
//					loadMoreData();
//				}
//	}
//
//
//	@Override
//	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//		lastItem = firstVisibleItem + visibleItemCount - 1; // 减1是因为上面加了个addFooterView
//	}


	
	
	/**
	 * 获取页数据
	 */
	public boolean startGetData() {
		KnowledgeReqUtil.doGetKnowledgeByColumnAndSource(knowledgeSquareActivity, KnowledgeBaseFragment.this, column.getId(), column.getType(), source+"", nowIndex, 20, null);
		return true;
	}
	
	/**
	 * 判断分页有没有更多
	 * 
	 * @return
	 */
	public boolean hasMore() {
//		if (index != -1 && (index + 1) * size >= total)
		if (lastActualSize < size )
			return false;
		else
			return true;
	}
	
	/**
	 * // 加载更多数据
	 */
	private void loadMoreData() { 
		nowIndex++;
		mState = STATE_GETMORE;
		startGetData();
	}
	@Override
	public void onRefresh() {
		nowIndex = 0;
		startGetData();
		mState = STATE_REFRESH;
	}
	

}
