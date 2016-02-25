package com.tr.ui.organization.firstpage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tr.R;
import com.tr.api.DemandReqUtil;
import com.tr.api.OrganizationReqUtil;
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
import com.tr.ui.organization.create_clientele.ClientDetailsActivity;
import com.tr.ui.organization.firstpage.OrganizationAndCustomerActivity.MyAdapter;
import com.tr.ui.organization.model.Contacts;
import com.tr.ui.organization.model.cusandorg.CusAndOrg_Page;
import com.tr.ui.organization.model.cusandorg.PageItem;
import com.tr.ui.widgets.CircleImageView;
import com.utils.common.Util;
import com.utils.common.ViewHolder;
import com.utils.display.DisplayUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/**
 * @ClassName: DemandTypeActivity.java
 * @author fxtx
 * @Date 2015年3月27日 下午1:06:14
 * @Description: 需求列表中点击目录后跳转的界面
 */
public class OrganizationCategoryActivity extends JBaseActivity implements IBindData {
	OrgAndCustAdapter myAdapter;
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
	}

	private String CreateOrCollecttype;
	private CusAndOrg_Page cusandorg_page;
	private List<PageItem> pageItemList;
	private ArrayList<Contacts> lists = new ArrayList<Contacts>();


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
		myAdapter = new OrgAndCustAdapter(lists,this);
		// 点击
		meNeedLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Contacts data = lists.get(position - 1);
				long customerId = data.getCustomerId();
				Intent intent = new Intent(OrganizationCategoryActivity.this, ClientDetailsActivity.class);
				intent.putExtra("customerId", customerId);
				intent.putExtra("label", 2);
				startActivityForResult(intent, 10003);
			}
		});

		meNeedLv.showFooterView(false);
		// 设置xlistview可以加载、刷新
		meNeedLv.setPullLoadEnable(false);
		meNeedLv.setPullRefreshEnable(false);
		meNeedLv.setXListViewListener(new IXListViewListener() {

			@Override
			public void onLoadMore() {
				startGetData();
			}

			@Override
			public void onRefresh() {
			}
		});
		meNeedLv.setAdapter(myAdapter);
	}

	/**
	 * 获取页数据
	 */
	public void startGetData() {
		int nowIndex = 0;
			showLoadingDialog();
		CreateOrCollecttype = -1+"";
		OrganizationReqUtil.doGetCusAndOrg(this, this, category.id+"", "0",CreateOrCollecttype , "1000","", "",
				null);
		
	}

	/**
	 * 我的组织客户列表
	 * 
	 * 
	 */
	class OrgAndCustAdapter extends BaseAdapter {
		private List<Contacts> contactsItemLists;
		private Context context;

		public OrgAndCustAdapter(List<Contacts> contactsItemLists,
				Context context) {
			super();
			this.contactsItemLists = contactsItemLists;
			this.context = context;
		}

		public List<Contacts> getContactsItemLists() {
			return contactsItemLists;
		}

		public void setContactsItemList(List<Contacts> contactsItemLists) {
			this.contactsItemLists = contactsItemLists;
		}

		public Context getContext() {
			return context;
		}

		public void setContext(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return contactsItemLists == null ? 0 : contactsItemLists.size();
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Contacts contacts = contactsItemLists.get(position);
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(context,
						R.layout.org_orgorcustomer_listviewitem, null);
//				holder.org_RL = (RelativeLayout) convertView
//						.findViewById(R.id.org_RL);// 首字母所在的布局
				holder.tv_word = (TextView) convertView
						.findViewById(R.id.tv_word);// 开头首字母
				holder.org_tv_name = (TextView) convertView
						.findViewById(R.id.org_tv_name);// 名称
				holder.iv_message = (ImageView) convertView
						.findViewById(R.id.iv_message);// 短信图标
				holder.iv_dial = (ImageView) convertView
						.findViewById(R.id.iv_dial);// 电话图标
				holder.org_iv_headprotrait = (CircleImageView) convertView
						.findViewById(R.id.org_iv_headprotrait);// Logo
				holder.org_iv_head = (ImageView) convertView
						.findViewById(R.id.org_iv_head);// 组织 客户图标
				holder.org_tv_location = (TextView) convertView
						.findViewById(R.id.org_tv_location);// 地区
				holder.org_tv_work = (TextView) convertView
						.findViewById(R.id.org_tv_work);// 行业

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
				
			}
			if (!TextUtils.isEmpty(contacts.getShortName())) {// 设置名称
				holder.org_tv_name.setText(contacts.getShortName());
			} else {
				holder.org_tv_name.setText(contacts.getName());
			}
			String org_name = holder.org_tv_name.getText().toString();
			Util.initAvatarImage(context, holder.org_iv_headprotrait, org_name, contacts.getPicLogo(), 1, 2);
			
			holder.org_tv_name.setCompoundDrawables(null,null, null, null);
			Drawable drawable = null;
			if (contacts.getVirtual() == 0) {// 设置是否是组织/客户
				drawable = getResources().getDrawable(R.drawable.contactclienttag);
			} else if (contacts.getVirtual() == 1
					|| contacts.getVirtual() == 2) {
				drawable = getResources().getDrawable(R.drawable.contactorganizationtag);
				
			}
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
			holder.org_tv_name.setCompoundDrawables(drawable,null, null, null);
			holder.org_tv_name.setCompoundDrawablePadding(DisplayUtil.dip2px(OrganizationCategoryActivity.this, 10));
			holder.iv_message.setVisibility(View.GONE);
			holder.iv_dial.setVisibility(View.GONE);

			return convertView;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}
	}
	
	class ViewHolder {
		public RelativeLayout org_RL;
		public TextView tv_word;// 顶部的字母
		public TextView org_tv_name;// 每个listView子条目的名称
		public ImageView iv_message;// 每个listView子条目的短信图标
		public ImageView iv_dial;// 每个listView子条目的电话图标
		public CircleImageView org_iv_headprotrait;// 每个listView子条目的Logo图标
		public ImageView org_iv_head;// 每个listView子条目的组织/客户的图标
		public TextView org_tv_location;// 每个ListView的子条目的地区
		public TextView org_tv_work;// 每个ListView的子条目的行业
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
		mActionBar.setTitle("  ");
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
	if (tag == EAPIConsts.OrganizationReqType.ORAGANIZATION_REQ_GETCUSTOMANDORG){
		if (object == null) {
			return;
		}
		Map<String, Object> dataHm = (Map<String, Object>) object;
		cusandorg_page = (CusAndOrg_Page) dataHm.get("page");
		if (cusandorg_page != null && cusandorg_page.listResults != null
				) {
			pageItemList = cusandorg_page.listResults;
			lists.clear();
			fillList();
			// for(int i = 0;i<lists.size();i++){
			// listss.add(lists.get(i));
			// }
			Collections.sort(lists);
			myAdapter.notifyDataSetChanged();
		}
	}
	}
	// 将请求回来的数据填充到集合中
	private void fillList() {
		// if(cusandorg_page != null && cusandorg_page.listResults!= null &&
		// cusandorg_page.listResults.size()>0){
		for (int i = 0; i < pageItemList.size(); i++) {
			Contacts contacts = new Contacts();
			contacts.setCity(pageItemList.get(i).city);
			contacts.setCustomerId(pageItemList.get(i).customerId);
			contacts.setNameFirst(pageItemList.get(i).nameFirst);
			contacts.setIndustrys(pageItemList.get(i).industrys);
			contacts.setName(pageItemList.get(i).name);
			contacts.setPicLogo(pageItemList.get(i).picLogo);
			contacts.setVirtual(pageItemList.get(i).virtual);
			contacts.setLinkMobile(pageItemList.get(i).linkMobile);
			contacts.setShortName(pageItemList.get(i).shotName);
			lists.add(contacts);
			// }
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
