package com.tr.ui.demand;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tr.R;
import com.tr.api.DemandReqUtil;
import com.tr.model.demand.NeedItemData;
import com.tr.model.demand.NeedItemListItem;
import com.tr.model.knowledge.UserCategory;
import com.tr.model.page.JTPage;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.demand.util.ChooseDataUtil;
import com.tr.ui.demand.util.DemandAction;
import com.utils.common.ViewHolder;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/**
 * @ClassName: DemandTypeActivity.java
 * @author fxtx
 * @Date 2015年3月27日 下午1:06:14
 * @Description: 需求列表中点击目录后跳转的界面
 */
public class DemandCategoryActivity extends JBaseActivity implements IBindData {
	NeedAdapter myAdapter;
	private JTPage jtpage;// 分页加载对象
	private XListView meNeedLv;
	private TextView titleTv;
	private ActionBar mActionBar;
	private List<NeedItemData> needItemList;
	private UserCategory category;// 标签对象

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getParam();
		setContentView(R.layout.activity_demand_typelist);
		initView();
		initData();
		startGetData();// 加载第一位数据
		IntentFilter filter = new IntentFilter();
		filter.addAction(DemandAction.DEMAND_DETAILS_ACTION);
		this.registerReceiver(broad, filter);
	}

	BroadcastReceiver broad = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (DemandAction.DEMAND_DETAILS_ACTION.equals(intent.getAction())) {
				if (needItemList != null) {
					NeedItemData data = getDeleteData(intent
							.getStringExtra(ENavConsts.DEMAND_DETAILS_ID));
					if (data != null) {
						needItemList.remove(data); // 发生了删除数据操作
						myAdapter.notifyDataSetChanged();

					} else {
						jtpage = null;
						startGetData();
					}
				}
			}
		}
	};

	/**
	 * 获取删除的id
	 * 
	 * @return
	 */
	private NeedItemData getDeleteData(String demandid) {
		for (NeedItemData data : needItemList) {
			if (data.demandId.equals(demandid)) {
				return data;
			}
		}
		return null;
	}

	private void initView() {
		meNeedLv = (XListView) findViewById(R.id.meNeedLv);
		needItemList = new ArrayList<NeedItemData>();

	}

	private void getParam() {
		Intent intent = getIntent();
		category = (UserCategory) intent
				.getSerializableExtra(ENavConsts.DEMAND_LABEL_DATA);
		if (category != null)
			titleTv.setText(category.getCategoryname());//
		else
			titleTv.setText("目录");
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		myAdapter = new NeedAdapter(needItemList, this);
		// 点击
		meNeedLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				NeedItemData data = needItemList.get(position - 1);
				ENavigate.startNeedDetailsActivity(DemandCategoryActivity.this,
						data.demandId, 1);
			}
		});

		meNeedLv.showFooterView(false);
		// 设置xlistview可以加载、刷新
		meNeedLv.setPullLoadEnable(true);
		meNeedLv.setPullRefreshEnable(true);
		meNeedLv.setXListViewListener(new IXListViewListener() {

			@Override
			public void onLoadMore() {
				startGetData();
			}

			@Override
			public void onRefresh() {
				DemandReqUtil.getDemandCategory(DemandCategoryActivity.this,
						DemandCategoryActivity.this, 20, 0, category.getId(),
						null);
			}
		});
		meNeedLv.setAdapter(myAdapter);
	}

	/**
	 * 获取页数据
	 */
	public void startGetData() {
		int nowIndex = 0;
		if (jtpage != null) {
			nowIndex = jtpage.getIndex() + 1;
		} else {
			showLoadingDialog();
		}
		DemandReqUtil.getDemandCategory(DemandCategoryActivity.this,
				DemandCategoryActivity.this, 20, nowIndex, category.getId(),
				null);
	}

	/**
	 * 我的需求列表
	 * 
	 * @author Administrator
	 *
	 */
	class NeedAdapter extends BaseAdapter {
		private List<NeedItemData> needItemList;
		private Context context;

		public NeedAdapter(List<NeedItemData> needItemList, Context context) {
			super();
			this.needItemList = needItemList;
			this.context = context;
		}

		public List<NeedItemData> getNeedItemList() {
			return needItemList;
		}

		public void setNeedItemList(List<NeedItemData> needItemList) {
			this.needItemList = needItemList;
		}

		public Context getContext() {
			return context;
		}

		public void setContext(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return needItemList == null ? 0 : needItemList.size();
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			NeedItemData needItem = needItemList.get(position);
			if (convertView == null) {
				convertView = View.inflate(context,
						R.layout.demand_me_need_listview, null);
			}
			ImageView typeIv = ViewHolder.get(convertView, R.id.typeIv);
			TextView titleTv = ViewHolder.get(convertView, R.id.titleTv);
			ImageView editSateIv = ViewHolder.get(convertView, R.id.editSateIv);
			TextView priceTv = ViewHolder.get(convertView, R.id.priceTv);
			TextView timeTv = ViewHolder.get(convertView, R.id.timeTv);

			timeTv.setText(needItem.createTime);// 时间
			if (needItem.title != null
					&& !TextUtils.isEmpty(needItem.title.value)) {
				titleTv.setText(needItem.title.value);// 标题对象
			}
			if (needItem.amount != null) {
				priceTv.setText(needItem.amount.getAmountData());
			}
			editSateIv.setVisibility(View.GONE);
			if (needItem.demandType == ChooseDataUtil.CHOOSE_type_OutInvestType) {
				typeIv.setImageResource(R.drawable.demand_me_need01);
			} else {
				typeIv.setImageResource(R.drawable.demand_me_need02);
			}
			return convertView;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}
	}

	@Override
	public void initJabActionBar() {
		mActionBar = jabGetActionBar();
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(true);
		final View mCustomView = getLayoutInflater().inflate(
				R.layout.demand_actionbar, null);
		titleTv = (TextView) mCustomView.findViewById(R.id.titleTv);

		mActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView
				.getLayoutParams();
		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK
				| Gravity.CENTER_HORIZONTAL;
		mActionBar.setCustomView(mCustomView, mP);
		mActionBar.setTitle(" ");
	}

	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	//
	// switch (item.getItemId()) {
	// case android.R.id.home:// 返回键 就取消
	// ENavigate.startKnowledgeCategoryActivityForResult(
	// DemandCategoryActivity.this, 10,
	// null, CategoryType.demand,false); //单选
	// finish();
	// break;
	// default:
	// break;
	// }
	// return true;
	// }

	@Override
	public void bindData(int tag, Object object) {
		meNeedLv.showFooterView(false);
		dismissLoadingDialog();
		meNeedLv.stopLoadMore();
		meNeedLv.stopRefresh();
		if (object == null) {
			return;
		}

		if (tag == EAPIConsts.demandReqType.demand_rCategoryQuery) {
			// 查询获取的列表
			NeedItemListItem item = (NeedItemListItem) object;
			jtpage = item.getJtPage();
			if (jtpage == null)
				return;
			if (jtpage.getIndex() == 1) {
				needItemList.clear();
			}
			// TODO 需要添加代码处
			if ((jtpage != null) && (jtpage.getLists() != null)) {
				for (int i = 0; i < jtpage.getLists().size(); i++) {
					needItemList.add((NeedItemData) jtpage.getLists().get(i));
				}
				myAdapter.notifyDataSetChanged();
			}
			if (jtpage.getIndex() >= jtpage.getTotalPage() - 1) {
				meNeedLv.setPullLoadEnable(false);
			}
		}
	}

	@Override
	protected void onDestroy() {
		this.unregisterReceiver(broad);
		super.onDestroy();
	}

}
