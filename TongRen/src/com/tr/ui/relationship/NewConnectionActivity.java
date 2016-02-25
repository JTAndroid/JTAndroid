package com.tr.ui.relationship;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tr.App;
import com.tr.R;
import com.tr.api.ConnectionsReqUtil;
import com.tr.db.ConnectionsDBManager;
import com.tr.image.ImageLoader;
import com.tr.model.connections.FriendRequest;
import com.tr.model.obj.Connections;
import com.tr.model.page.JTPage;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.home.MainActivity;
import com.tr.ui.home.frg.FrgConnections2;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.picture.PorterShapeImageView;

/**
 * @Filename MainActivity.java
 * @Author xuxinjian
 * @Date 2014-3-17
 * @description 添加新关系
 */
public class NewConnectionActivity extends JBaseFragmentActivity implements IBindData, OnScrollListener, OnClickListener {

	public static final String TAG = "NewConnectionActivity";

	private XListView mListView;
	private List<FriendRequest> list = new ArrayList<FriendRequest>();
	private IMAdapter adapter;
	// private Handler mHandler = new Handler();
	private int maxAount = 50;// 设置了最大数据值

	private View moreView; // 加载更多页面
	private TextView mvTextView;
	private View mvProgressBar;

	private int lastItem;
	private int count;
	private JTPage mPage;
	private int mState = 0;// 0-正常状态 1-获取更多中 2-刷新中
	private FriendRequest currentfriendRequest;
	// private SwipeRefreshLayout swipeLayout;

	private TextView mTxtNoContent;
	private View mViewBG;

	private ImageView conn_iv, invite_iv;
	private TextView conn_Tv, invite_Tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.newconnectionlist);
		startGetData(0);
		moreView = mInflater.inflate(R.layout.newconnectionlist, null);
		this.mvTextView = (TextView) moreView.findViewById(R.id.pulldown_footer_text);
		this.mvProgressBar = (View) moreView.findViewById(R.id.pulldown_footer_loading);

		mListView = (XListView) this.findViewById(R.id.newconnectionlist);
		setXlistViewConfig();
		adapter = new IMAdapter(this);

		mTxtNoContent = (TextView) findViewById(R.id.NCTxtNoContent);
		mViewBG = findViewById(R.id.NCBG);

		conn_iv = (ImageView) findViewById(R.id.conn_iv);
		conn_Tv = (TextView) findViewById(R.id.conn_Tv);
		invite_iv = (ImageView) findViewById(R.id.invite_iv);
		invite_Tv = (TextView) findViewById(R.id.invite_Tv);

		conn_iv.setOnClickListener(this);
		conn_Tv.setOnClickListener(this);
		invite_iv.setOnClickListener(this);
		invite_Tv.setOnClickListener(this);

		mListView.setAdapter(adapter);
		mListView.setOnScrollListener(this);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2!=0) {
					final FriendRequest dataHolder = (FriendRequest) adapter.getItem(arg2 - 1);// xListViewitem从1开始
					if (dataHolder.state == FriendRequest.state_agreed) {
						String id = dataHolder.getUserID();
						if (dataHolder.userType == FriendRequest.type_persion) {//个人
							ENavigate.startRelationHomeActivity(NewConnectionActivity.this, id, true, ENavConsts.type_details_other);
						} else if (dataHolder.userType == FriendRequest.type_org) {//组织
							ENavigate.startOrgMyHomePageActivityByUseId(NewConnectionActivity.this, Long.valueOf(id));
						}
					}
				}
			}
		});
		count = list.size();

		// showLoadingDialog();
	}

	/** 设置XListView的参数 */
	private void setXlistViewConfig() {
		mListView.showFooterView(false);
		// 设置xlistview可以加载、刷新
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(false);

		mListView.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				startGetData(0);
			}

			@Override
			public void onLoadMore() {
				startGetData(0);
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void bindData(int tag, Object object) {
		try {
			dismissLoadingDialog();
			if (tag == EAPIConsts.concReqType.im_getnewConnections) {
				// stopLoading();
				mListView.stopLoadMore();
				mListView.stopRefresh();
				if (object != null) {
					list = (ArrayList<FriendRequest>) object;
					adapter.setData(list);
					adapter.notifyDataSetChanged();
					// showToast("获取新的关系成功");
					if (list.size() <= 0) {
						mViewBG.setVisibility(View.GONE);
						mTxtNoContent.setVisibility(View.VISIBLE);
					}
				}
			} else if (tag == EAPIConsts.concReqType.im_allowConnectionsRequest) {

				{
					String str = (String) object;
					String netokstr = NewConnectionActivity.this.getString(R.string.net_return_ok);
					if (str.equals(netokstr)) {
						showLongToast("正在更新金桐关系列表");
						// 通过成功， 更新缓存数据
//						startGetConnections();
						if (currentfriendRequest != null&&currentfriendRequest.userType == FriendRequest.type_persion) {
							currentfriendRequest.state = FriendRequest.state_agreed;
							Connections conneections = currentfriendRequest.toConneections();
							ConnectionsDBManager.getInstance(App.getApplicationConxt()).insert(conneections);
						}
						
					} else {
						showToast(NewConnectionActivity.this.getString(R.string.neterr_sync));
						dismissLoadingDialog();
					}
					if (currentfriendRequest != null) {
						currentfriendRequest.state = FriendRequest.state_agreed;
					}
					adapter.notifyDataSetChanged();
				}
			} else if (tag == EAPIConsts.concReqType.CONNECTIONSLIST) {
				if (object != null) {
					ArrayList<Connections> connArr = (ArrayList<Connections>) object;
					FrgConnections2.contactAdapter.dataChange();
					// ConnectionsDBManager.getInstance(App.getApplicationConxt()).insert(connArr);
					showLongToast("更新金桐关系列表完成");
				} else {
					showToast("更新金桐关系列表失败");
				}
				this.dismissLoadingDialog();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// controlXListBottom();
	}

	private void startGetConnections() {
		JSONObject jb = new JSONObject();
		try {
			jb.put("type", "0");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ConnectionsReqUtil.doGetConnectionsList(this, this, jb, null);
	}

	public class IMAdapter extends BaseAdapter {

		private Context mContext;
		private List<FriendRequest> mData;

		public IMAdapter(Context context) {
			this.mContext = context;
			mData = new ArrayList<FriendRequest>();
		}

		public void setData(List<FriendRequest> mData) {
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
				convertView = LayoutInflater.from(mContext).inflate(R.layout.newconnection, parent, false);
				holder = new ItemHolder();
				holder.mImg = (ImageView) convertView.findViewById(R.id.HFI_IMAGE_Head);
				holder.mTitle = (TextView) convertView.findViewById(R.id.NC_NAME);
				holder.mAction = (TextView) convertView.findViewById(R.id.NC_STATE);
				holder.mSource = (TextView) convertView.findViewById(R.id.NC_SOURCEFROM);

				convertView.setTag(holder);
			} else {
				holder = (ItemHolder) convertView.getTag();
			}
			final FriendRequest dataHolder = mData.get(position);
			ImageLoader.load(holder.mImg, dataHolder.getImage(), R.drawable.ic_default_avatar);
			holder.mTitle.setText(dataHolder.name);
			holder.mSource.setText(dataHolder.getSourceFrom());
			if (dataHolder.state == FriendRequest.state_agreed) {
				holder.mAction.setText("已添加");
				// holder.mAction.setBackgroundDrawable(new BitmapDrawable());
				holder.mAction.setBackgroundResource(R.drawable.new_connection_angree_button_bg_transparent);
				holder.mAction.setTextColor(getResources().getColor(R.color.home_dt_font_comment_content));
			}

			else if (dataHolder.state == FriendRequest.state_request) {
				holder.mAction.setText(" 接受 ");
				holder.mAction.setBackgroundResource(R.drawable.new_connection_angree_button_bg_selector);
				holder.mAction.setTextColor(getResources().getColor(R.color.white));
				holder.mAction.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						currentfriendRequest = dataHolder;
						// Toast.makeText(NewConnectionActivity.this, "23232",
						// Toast.LENGTH_SHORT);
						ConnectionsReqUtil.doallowConnectionsRequest(NewConnectionActivity.this, NewConnectionActivity.this,
								ConnectionsReqUtil.getallowConnectionsRequestJson(dataHolder.getId(), dataHolder.getUserID(), dataHolder.getType()), null);
//						showLoadingDialog();
					}
				});
			}
			return convertView;
		}

		private class ItemHolder {
			public ImageView mImg;
			public TextView mTitle;
			public TextView mSource;
			public TextView mAction;
		}
	}

	// public void stopLoading() {
	// swipeLayout.setRefreshing(false);
	// mState = STATE_NORMAL;
	// // mvProgressBar.setVisibility(View.GONE);
	// // mvTextView.setText("更多");
	// }

	// 触发下拉刷新
	public void onRefresh() {
		if (mState == STATE_NORMAL) {
			startGetData(0);

		}
	}

	/**
	 * 获取页数据
	 */
	public void startGetData(int pageIndex) {
		// sendMessage();
		// IMReqUtil.getListIMRecord(getActivity(), this, null, pageIndex, 20);
		// swipeLayout.setRefreshing(true);

		this.mState = STATE_REFRESH;
		JSONObject jos = new JSONObject();
		showLoadingDialog();
		ConnectionsReqUtil.dogetnewConnections(NewConnectionActivity.this, this, jos, null);
	}

	private void loadMoreData() { // 加载更多数据
		if (mState == STATE_NORMAL) {
			mvProgressBar.setVisibility(View.VISIBLE);
			mvTextView.setText("正在加载");
			int nowIndex = 0;
			if (mPage != null)
				nowIndex = mPage.getIndex();
			startGetData(nowIndex);
			this.mState = STATE_GETMORE;
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

	@Override
	public void initJabActionBar() {
		ActionBar actionBar = jabGetActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle("新的关系");
		actionBar.hide();
	}

	/**
	 * 用于外界通知刷新界面
	 */
	public void updateUI() {
		String MSG = "updateUI()";
		Log.i(TAG, MSG);
		onRefresh();
	}

	/**
	 * 控制XListViewButtom
	 */
	private void controlXListBottom() {
		if (mPage != null) {
			int totalPages;
			if ((mPage.getTotal() % 20) == 0) {
				totalPages = mPage.getTotal() / 20;
			} else {
				totalPages = mPage.getTotal() / 20 + 1;
			}
			if ((totalPages == 0) || (mPage.getIndex() >= totalPages)) {
				mListView.setPullLoadEnable(false);
			} else {
				mListView.setPullLoadEnable(true);
			}

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.conn_iv:
		case R.id.conn_Tv:
			finish();
			break;
		case R.id.invite_iv:
		case R.id.invite_Tv:
			ENavigate.startMeetingInviteFriendsActivity(NewConnectionActivity.this);
			break;
		}
	}
}
