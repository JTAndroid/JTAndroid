package com.tr.ui.user;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tr.R;
import com.tr.api.ConnectionsReqUtil;
import com.tr.model.connections.MGetBlackList;
import com.tr.model.obj.ConnectionsMini;
import com.tr.model.page.JTPage;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;

/**
 * 帐号管理页面
 * 
 * @author gushi
 */
public class BlackListActivity extends JBaseFragmentActivity implements IBindData, OnItemLongClickListener, OnScrollListener, OnItemClickListener {

	private Context context;
	private XListView mainLv;
	private WantConnectionsAdapter wantConnectionsAdapter;
	private ArrayList<ConnectionsMini> connectionsList;
	/* 分页JTPage */
	private JTPage mPage;
	private int removePosition;

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar(), "黑名单", false, null, false, true);
//		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;

		setContentView(R.layout.activity_black_list);

		mainLv = (XListView) findViewById(R.id.mainLv);
		TextView Ll = (TextView) findViewById(R.id.tv);
		setXlistViewConfig();
		initPopWindow();
		connectionsList = new ArrayList<ConnectionsMini>();
		wantConnectionsAdapter = new WantConnectionsAdapter(getContext(), connectionsList);
		mainLv.setAdapter(wantConnectionsAdapter);
		startGetData();
		mainLv.setOnItemLongClickListener(this);
		mainLv.setOnScrollListener(this);
		mainLv.setOnItemClickListener(this);
		Ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismissPopwindow();

			}
		});

		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.knowledge_square_default_image).showImageForEmptyUri(R.drawable.knowledge_square_default_image)
				.showImageOnFail(R.drawable.knowledge_square_default_image).cacheInMemory(true).cacheOnDisc(true).considerExifParams(false).build();
	}

	/** 设置XListView的参数 */
	private void setXlistViewConfig() {
		mainLv.showFooterView(false);
		// 设置xlistview可以加载、刷新
		mainLv.setPullLoadEnable(true);
		mainLv.setPullRefreshEnable(true);

		mainLv.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				startGetData();
			}

			@Override
			public void onLoadMore() {
				startGetData();
			}
		});
	}

	/**
	 * 获取页数据
	 */
	public void startGetData() {
		showLoadingDialog();
		int nowIndex = 1;
		if (mPage != null)
			nowIndex = mPage.getIndex() + 1;
		ConnectionsReqUtil.doGetBlacklist(context, BlackListActivity.this, nowIndex, 10, null);
		showLoadingDialog();
	}

	public Context getContext() {
		return context;
	}

	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

		}
	};
	private int linmitHeight;
	private View contentView;
	private DisplayImageOptions options;

	/** 推荐联系人adapter */
	class WantConnectionsAdapter extends BaseAdapter {

		private Context mContext;
		private ArrayList<ConnectionsMini> mConnectionsMiniList;
		private ImageLoadingListener animateFirstListener = new com.utils.image.AnimateFirstDisplayListener();

		public WantConnectionsAdapter() {
			super();
		}

		public WantConnectionsAdapter(Context mContext, ArrayList<ConnectionsMini> connectionsList) {
			super();
			this.mContext = mContext;
			if (connectionsList == null) {
				this.mConnectionsMiniList = new ArrayList<ConnectionsMini>();
			} else {
				this.mConnectionsMiniList = connectionsList;
			}
		}

		public ArrayList<ConnectionsMini> getConnectionsList() {
			return mConnectionsMiniList;
		}

		public void setConnectionsList(ArrayList<ConnectionsMini> connectionsList) {
			this.mConnectionsMiniList = connectionsList;
		}

		@Override
		public int getCount() {
			return mConnectionsMiniList.size();
			// return 100;
		}

		@Override
		public ConnectionsMini getItem(int position) {
			return mConnectionsMiniList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			String MSG = "getView()";

			ViewHolder holder;

			if (convertView == null) {
				holder = new ViewHolder();

				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.list_item_want_people2, null);

				holder.peopleNameTv = (TextView) convertView.findViewById(R.id.peopleNameTv);
				holder.companyName = (TextView) convertView.findViewById(R.id.companyName);
				holder.companyJob = (TextView) convertView.findViewById(R.id.companyJob);
				holder.imageIv = (ImageView) convertView.findViewById(R.id.imageIv);
				holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
				holder.checkBox.setVisibility(View.GONE);
				holder.friendStateTv = (TextView) convertView.findViewById(R.id.friendStateTv);
				holder.addFriendBtn = (TextView) convertView.findViewById(R.id.addFriendBtn);
				holder.addFriendBtn.setVisibility(View.GONE);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			ConnectionsMini connectionsMini = mConnectionsMiniList.get(position);

			// holder.checkBox.setChecked(connectionsMini.isSelected());

			if (!StringUtils.isEmpty(connectionsMini.getImage())) {
				// try {
				// ApolloUtils.getImageFetcher(context).loadHomeImage(connectionsMini.getImage(),
				// holder.imageIv);
				// } catch (Exception e) {
				// Log.e(TAG, MSG + " e = " + e);
				// }
				ImageLoader.getInstance().displayImage(connectionsMini.getImage(), holder.imageIv, options, animateFirstListener);
			}

			holder.peopleNameTv.setText(connectionsMini.getName());
			holder.companyName.setText(connectionsMini.getCompanyName());
			holder.companyJob.setText(connectionsMini.getCompanyJob());
			holder.friendStateTv.setText(1 == connectionsMini.getFriendState() ? "好友等待中" : "");

			return convertView;
		}

		public final class ViewHolder {
			public CheckBox checkBox;
			public ImageView imageIv;
			public TextView peopleNameTv;
			public TextView companyName;
			public TextView companyJob;
			public TextView friendStateTv;
			public TextView addFriendBtn;
		}

		public void setData(ArrayList<ConnectionsMini> connectionsList) {
			this.mConnectionsMiniList = connectionsList;
		}

	}

	@Override
	public void bindData(int tag, Object object) {
		mainLv.stopLoadMore();
		mainLv.stopRefresh();
		dismissLoadingDialog();
		if (object == null) {
			return;
		}
		if (tag == EAPIConsts.concReqType.blacklist) {
			MGetBlackList blacklist = (MGetBlackList) object;
			if (blacklist != null) {
				mPage = blacklist.getJtPage();
				if ((mPage != null) && (mPage.getLists() != null)) {
					for (int i = 0; i < mPage.getLists().size(); i++) {
						connectionsList.add((ConnectionsMini) blacklist.getJtPage().getLists().get(i));
					}
					/* count = list.size(); */
					wantConnectionsAdapter.setData(connectionsList);
					wantConnectionsAdapter.notifyDataSetChanged();
					controlXListBottom();
				}
			}
		} else if (tag == EAPIConsts.concReqType.editBlack) {
			if (object != null) {
				String str = (String) object;
				if (str.equals("true")) {
					connectionsList.remove(removePosition);
					wantConnectionsAdapter.setData(connectionsList);
					wantConnectionsAdapter.notifyDataSetChanged();
					return;
				} else {
					showToast("移出黑名单失败");
				}
			}
		}
	}

	private int measuredHeigh;
	private PopupWindow window;
	private TextView deleteTv;
	private TextView saveTv;

	private void initPopWindow() {
		Rect frame = new Rect();
		getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		int actionBarHeight = getActionBar().getHeight();
		linmitHeight = statusBarHeight + actionBarHeight;
		contentView = View.inflate(BlackListActivity.this, R.layout.layout_sociality_delete, null);
		deleteTv = (TextView) contentView.findViewById(R.id.delete);
		saveTv = (TextView) contentView.findViewById(R.id.save);
		saveTv.setVisibility(View.GONE);
		window = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		window.setBackgroundDrawable(new ColorDrawable(-000000));
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
		dismissPopwindow();
		int[] location = new int[2];
		view.getLocationInWindow(location);
		int width = getWindowManager().getDefaultDisplay().getWidth();
		if (linmitHeight >= location[1] - measuredHeigh) {
			window.showAtLocation(parent, Gravity.LEFT + Gravity.TOP, width / 2 - 30, location[1] - measuredHeigh + 10);
		} else {
			window.showAtLocation(parent, Gravity.LEFT + Gravity.TOP, width / 2 - 30, location[1] - measuredHeigh);
		}

		deleteTv.setText("移出");
		deleteTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				removePosition = position - 1;// XlistView列表从1开始
				ArrayList<String> listUserId = new ArrayList<String>();
				listUserId.add(connectionsList.get(removePosition).getId());
				ConnectionsReqUtil.doEditBlack(BlackListActivity.this, BlackListActivity.this, listUserId, "2", null);
				showLoadingDialog();

				dismissPopwindow();
			}
		});
		ViewTreeObserver vto = contentView.getViewTreeObserver();

		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			public void onGlobalLayout() {

				measuredHeigh = contentView.getMeasuredHeight();

				contentView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}
		});

		return true;

	}

	private void dismissPopwindow() {
		if (window != null && window.isShowing()) {
			window.dismiss();
			// window = null;
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
		case OnScrollListener.SCROLL_STATE_IDLE:// 空闲状态

			break;
		case OnScrollListener.SCROLL_STATE_FLING:// 滚动状态
			 dismissPopwindow();
			break;
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 触摸后滚动
			dismissPopwindow();
			break;
		}

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		// dismissPopwindow();
	}

	/**
	 * 控制XListViewButtom
	 */
	private void controlXListBottom() {
		int totalPages;
		if ((mPage.getTotal() % 20) == 0) {
			totalPages = mPage.getTotal() / 20;
		} else {
			totalPages = mPage.getTotal() / 20 + 1;
		}
		if ((totalPages == 0) || (mPage.getIndex() >= totalPages)) {
			mainLv.setPullLoadEnable(false);
		} else {
			mainLv.setPullLoadEnable(true);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (window != null) {
			window = null;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		dismissPopwindow();
	}
}
