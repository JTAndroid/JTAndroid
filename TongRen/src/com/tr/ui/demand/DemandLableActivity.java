package com.tr.ui.demand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.App;
import com.tr.R;
import com.tr.api.DemandReqUtil;
import com.tr.api.KnowledgeReqUtil;
import com.tr.api.OrganizationReqUtil;
import com.tr.api.PeopleReqUtil;
import com.tr.model.demand.LableData;
import com.tr.model.demand.NeedItemData;
import com.tr.model.demand.NeedItemListItem;
import com.tr.model.page.JTPage;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.demand.util.ChooseDataUtil;
import com.tr.ui.demand.util.DemandAction;
import com.tr.ui.demand.util.TextStrUtil;
import com.tr.ui.organization.create_clientele.ClientDetailsActivity;
import com.tr.ui.organization.firstpage.OrganizationAndCustomerActivity;
import com.tr.ui.organization.model.Contacts;
import com.tr.ui.organization.model.cusandorg.CusAndOrg_Page;
import com.tr.ui.organization.model.cusandorg.PageItem;
import com.tr.ui.organization.model.parameters.OrganizationDetialsIncomingParameters;
import com.tr.ui.people.model.Person;
import com.tr.ui.people.model.PersonLabelItem;
import com.utils.common.EConsts;
import com.utils.common.ViewHolder;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.http.EAPIConsts.OrganizationReqType;
import com.utils.image.AnimateFirstDisplayListener;

/**
 * @author fxtx
 * @Date 2015年3月27日 下午1:06:14
 * @Description: 需求列表中点击标签后跳转的界面
 */
public class DemandLableActivity extends JBaseActivity implements
		OnClickListener, IBindData {
	NeedAdapter myAdapter;
	OrgAndCustAdapter orgAndCustAdapter;

	PeopleAdapter peopleAdapter;
	private JTPage jtpage;// 分页加载对象
	/** 组织客户分页加载对象 */
	private CusAndOrg_Page cusandorg_page;
	protected PopupWindow pop;
	private XListView meNeedLv;
	private TextView titleTv;
	private ActionBar mActionBar;
	private List<NeedItemData> needItemList;
	/** 组织客户page列表 */
	private List<PageItem> pageItemList;
	/** 组织客户对象列表 */
	private List<Contacts> contactsItemLists = new ArrayList<Contacts>();
	/** 人脉对象列表 */
	private ArrayList<Person> peopleItemLists = new ArrayList<Person>();
	/** 人脉标签对象列表 */
	private List<PersonLabelItem> personItemLists = new ArrayList<PersonLabelItem>();
	/** 知识标签对象列表 */
	private List<PersonLabelItem> knowledgeItemLists = new ArrayList<PersonLabelItem>();

	private MenuItem SaveItem;
	private Dialog dialog;
	private LableData lable;// 标签对象
	private boolean isType;// true 为长按，false 为点击

	private View lableView;// 编辑

	private View deleteView;// 删除
	private EditText lableEdit;// 标签编辑对象
	private ImageView deleteIv;// 删除标签信息
	DisplayImageOptions options;

	/** 区分来自不同模块 */
	private ModulesType mModulesType;

	public enum ModulesType {
		/** 知识模块 */
		KnowledgeModules,
		/** 组织/客户模块 */
		OrgAndCustomModules,
		/** 人脉模块 */
		PeopleModules,
		/** 需求模块 */
		DemandModules
	}

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
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_default_avatar)
		.showImageForEmptyUri(R.drawable.ic_default_avatar)
		.showImageOnFail(R.drawable.ic_default_avatar)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(false)
		.build();
	}

	BroadcastReceiver broad = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (DemandAction.DEMAND_DETAILS_ACTION.equals(intent.getAction())) {
				switch (mModulesType) {
				case DemandModules:
					if (needItemList != null) {
						jtpage = null;
						startGetData();
						if (!isType) {
							lable.num--;
							titleTv.setText(TextStrUtil.getCommentNum(lable.tag, lable.num));// 将标签名称显示出来
						}
					}
					break;
				case OrgAndCustomModules:
					if (contactsItemLists != null) {
						cusandorg_page = null;
						startGetData();
						if (!isType) {
							lable.num--;
							titleTv.setText(TextStrUtil.getCommentNum(lable.tag, lable.num));// 将标签名称显示出来
						}
					}
					break;
				case PeopleModules:
					if (peopleItemLists != null) {
						cusandorg_page = null;
						startGetData();
						if (!isType) {
							lable.num--;
							titleTv.setText(TextStrUtil.getCommentNum(lable.tag, lable.num));// 将标签名称显示出来
						}
					}
					break;
				case KnowledgeModules:
					if (knowledgeItemLists != null) {
						cusandorg_page = null;
						startGetData();
						if (!isType) {
							lable.num--;
							titleTv.setText(TextStrUtil.getCommentNum(lable.tag, lable.num));// 将标签名称显示出来
						}
					}
					break;
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
		deleteIv = (ImageView) findViewById(R.id.lableDelete);
		deleteIv.setOnClickListener(this);
		meNeedLv = (XListView) findViewById(R.id.meNeedLv);
		needItemList = new ArrayList<NeedItemData>();
		deleteView = findViewById(R.id.demandtypeDeleteLl);
		deleteView.setOnClickListener(this);
		lableView = findViewById(R.id.demandtypeIl);
		lableEdit = (EditText) findViewById(R.id.lable_edit);
		lableEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() > 10) {
					showToast("最多输入10个字符");
					lableEdit.setText(s.subSequence(0, 10));
					lableEdit.setSelection(lableEdit.getText().toString()
							.length());
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		if (isType) {
			deleteView.setVisibility(View.VISIBLE);
			lableView.setVisibility(View.VISIBLE);
			lableEdit.setText(lable.tag);
			titleTv.setText("标签");
		}
	}

	private void getParam() {
		Intent intent = getIntent();
		lable = (LableData) intent
				.getSerializableExtra(ENavConsts.DEMAND_LABEL_DATA);
		isType = intent.getBooleanExtra(ENavConsts.DEMAND_LABEL_TYPE, false);
		mModulesType = (ModulesType) getIntent().getSerializableExtra(
				EConsts.Key.MODULES_TYPE);
		if (lable == null) {
			lable = new LableData(0, 0, "标签");
		}
		titleTv.setText(TextStrUtil.getCommentNum(lable.tag, lable.num));// 将标签名称显示出来
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		switch (mModulesType) {
		case DemandModules:
			myAdapter = new NeedAdapter(needItemList, this);
			break;
		case OrgAndCustomModules:
			orgAndCustAdapter = new OrgAndCustAdapter(contactsItemLists, this);
			break;
		case PeopleModules:
			peopleAdapter = new PeopleAdapter(personItemLists, this);

			break;
		case KnowledgeModules:

			break;
		}
		// 点击
		meNeedLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (mModulesType) {
				case DemandModules:
					NeedItemData data = needItemList.get(position - 1);
					ENavigate.startNeedDetailsActivity(
							DemandLableActivity.this, data.demandId, 1);
					break;
				case PeopleModules:
					Person person = peopleItemLists.get(position - 1);
					ENavigate.startContactsDetailsActivity(
							DemandLableActivity.this, 2, person.id, 1);
					break;
				case OrgAndCustomModules:
					if (cusandorg_page.listResults.get(position - 1).virtual == 1
							|| cusandorg_page.listResults.get(position - 1).virtual == 2) {// 进入组织
						long customerId = cusandorg_page.listResults
								.get(position - 1).customerId;
						long createById = cusandorg_page.listResults
								.get(position - 1).createById;
						ENavigate.startOrgMyHomePageActivity(
								DemandLableActivity.this, customerId,
								createById, true, ENavConsts.type_details_org);
					} else if (cusandorg_page.listResults.get(position - 1).virtual == 0) {
						long customerId = cusandorg_page.listResults
								.get(position - 1).customerId;
						ENavigate.startClientDedailsActivity(DemandLableActivity.this, customerId);
					}
					break;
				case KnowledgeModules:

					break;
				}
			}
		});

		meNeedLv.showFooterView(false);
		// 设置xlistview可以加载、刷新
		meNeedLv.setPullLoadEnable(true);
		if (mModulesType.equals(ModulesType.OrgAndCustomModules)) {
			meNeedLv.setPullLoadEnable(false);
		}
		if (mModulesType.equals(ModulesType.PeopleModules)) {
			meNeedLv.setPullLoadEnable(false);
		}
		meNeedLv.setPullRefreshEnable(true);
		meNeedLv.setXListViewListener(new IXListViewListener() {

			@Override
			public void onLoadMore() {
				startGetData();
			}

			@Override
			public void onRefresh() {
				switch (mModulesType) {
				case DemandModules:
					DemandReqUtil.getTagDemandList(DemandLableActivity.this,
							DemandLableActivity.this, 20, 0, lable.id, null);
					break;
				case PeopleModules:
					PeopleReqUtil.getTagPeopleList(DemandLableActivity.this,
							DemandLableActivity.this, 20, 0, lable.id, null);
					break;
				case OrgAndCustomModules:
					OrganizationReqUtil.doGetCusAndOrg(
							DemandLableActivity.this, DemandLableActivity.this,
							0 + "", 0 + "",-1 + "" ,Integer.MAX_VALUE+"","", lable.id + "", null);
					break;
				case KnowledgeModules:

					break;
				}
			}
		});
		switch (mModulesType) {
		case DemandModules:
			meNeedLv.setAdapter(myAdapter);
			break;
		case OrgAndCustomModules:
			meNeedLv.setAdapter(orgAndCustAdapter);
			break;
		case PeopleModules:
			meNeedLv.setAdapter(peopleAdapter);
			break;
		case KnowledgeModules:

			break;
		}
	}

	/**
	 * 获取页数据
	 */
	public void startGetData() {
		int nowIndex = 0;
		switch (mModulesType) {
		case DemandModules:
			if (jtpage != null) {
				nowIndex = jtpage.getIndex() + 1;
			} else {
				showLoadingDialog();
			}
			DemandReqUtil.getTagDemandList(DemandLableActivity.this,
					DemandLableActivity.this, 20, nowIndex, lable.id, null);
			break;
		case PeopleModules:
			PeopleReqUtil.getTagPeopleList(DemandLableActivity.this,
					DemandLableActivity.this, 20, nowIndex, lable.id, null);
			break;
		case OrgAndCustomModules:
			OrganizationReqUtil.doGetCusAndOrg(
					DemandLableActivity.this, DemandLableActivity.this,
					0 + "", 0 + "",-1 + "" ,Integer.MAX_VALUE+"","", lable.id + "", null);
			break;
		case KnowledgeModules:
//			KnowledgeReqUtil.doGetKnowledgeTagList(DemandLableActivity.this, this, App.getUserID(), null);
			break;
		}
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

	/**
	 * 我的组织客户列表
	 * 
	 * @author 钟山
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
			Contacts contactsItem = contactsItemLists.get(position);
			if (convertView == null) {
				convertView = View.inflate(context,
						R.layout.demand_me_need_listview, null);
			}
			ImageView typeIv = ViewHolder.get(convertView, R.id.typeIv);
			TextView titleTv = ViewHolder.get(convertView, R.id.titleTv);
			ImageView editSateIv = ViewHolder.get(convertView, R.id.editSateIv);
			TextView priceTv = ViewHolder.get(convertView, R.id.priceTv);
			TextView timeTv = ViewHolder.get(convertView, R.id.timeTv);

			// timeTv.setText("");// 时间
			timeTv.setVisibility(View.INVISIBLE);
			if (!TextUtils.isEmpty(contactsItem.getShortName())) {
				titleTv.setText(contactsItem.getShortName());// 标题对象
			}else{
				titleTv.setText(contactsItem.getName());
			}
			if (contactsItem.getIndustrys() != null) {
				priceTv.setText(contactsItem.getIndustrys());
			}
			editSateIv.setVisibility(View.GONE);
			typeIv.setVisibility(View.INVISIBLE);
			return convertView;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}
	}

	/**
	 * 我的好友/人脉列表
	 * 
	 */
	class PeopleAdapter extends BaseAdapter {

		private List<PersonLabelItem> personItemLists;
		private Context context;

		public PeopleAdapter(List<PersonLabelItem> personItemLists,
				Context context) {
			super();
			this.personItemLists = personItemLists;
			this.context = context;
		}

		public List<PersonLabelItem> getPersonItemLists() {
			return personItemLists;
		}

		public void setContactsItemList(List<PersonLabelItem> personItemLists) {
			this.personItemLists = personItemLists;
		}

		public Context getContext() {
			return context;
		}

		public void setContext(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return personItemLists == null ? 0 : personItemLists.size();
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			PersonLabelItem personLabelItem = personItemLists.get(position);
			if (convertView == null) {
				convertView = View.inflate(context,
						R.layout.im_relationcontactmain_item, null);
			}
			ImageView contactitem_avatar_iv = ViewHolder.get(convertView,
					R.id.contactAvatarIv);
			TextView imcontactname = ViewHolder.get(convertView,
					R.id.contactNameTv);
			TextView companyTv = ViewHolder.get(convertView, R.id.contactCompanyOfferTv);
			ImageView sendSmsIv = ViewHolder.get(convertView, R.id.sendSmsIv);
			ImageView callIv = ViewHolder.get(convertView, R.id.callIv);
			

			imcontactname.setText(personLabelItem.peopleNameList.get(0).lastname+personLabelItem.peopleNameList.get(0).firstname);
			companyTv.setText(personLabelItem.company);
			ImageLoader.getInstance().displayImage(personLabelItem.portrait, contactitem_avatar_iv,options,new AnimateFirstDisplayListener());
			sendSmsIv.setVisibility(View.GONE);
			callIv.setVisibility(View.GONE);

			return convertView;
		}

		@Override
		public Object getItem(int position) {
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

	/**
	 * 弹出dialog
	 * 
	 * @param view
	 */
	private void showDialog(View view) {
		dialog = new Dialog(DemandLableActivity.this, R.style.MyDialog);

		// dialog.setCancelable(false);//是否允许返回
		dialog.addContentView(view, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		dialog.show();

	}

	@Override
	public void bindData(int tag, Object object) {
		meNeedLv.showFooterView(false);
		dismissLoadingDialog();
		meNeedLv.stopLoadMore();
		meNeedLv.stopRefresh();
		if (object == null) {
			return;
		}

		if (tag == EAPIConsts.demandReqType.demand_rTagQueryByTagId) {
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
			if (jtpage.getIndex() >= jtpage.getTotalPage()) {
				meNeedLv.setPullLoadEnable(false);
			}
		} else if (tag == EAPIConsts.demandReqType.demand_saveTag) {
			// 修改标签
			if (object instanceof String) {
				// object的实例是一个String 的话那么这就是错误信息打印出来
				Toast.makeText(this, (String) object, Toast.LENGTH_SHORT)
						.show();
			} else {
				// 正常返回解析成功，把object变成一个对象，并添加上去
				lable = (LableData) object;
				this.sendBroadcast(new Intent(
						DemandAction.DEMAND_LABLE_ACTIVITY)); // 发送广播
				showToast("修改成功");
				finish();
			}
		} else if (tag == EAPIConsts.demandReqType.demand_rTagDelete) {
			// 删除标签关系
			if ((Boolean) object) {
				showToast("删除成功");
				this.sendBroadcast(new Intent(
						DemandAction.DEMAND_LABLE_ACTIVITY));
				finish();
			} else {
				showToast("删除失败");
			}
		}
		// 获取组织/客户的列表数据
		else if (tag == EAPIConsts.OrganizationReqType.ORAGANIZATION_REQ_GETCUSTOMANDORG) {
			if (object != null) {
				Map<String, Object> dataHm = (Map<String, Object>) object;
				cusandorg_page = (CusAndOrg_Page) dataHm.get("page");
				if (cusandorg_page == null)
					return;
				if (cusandorg_page.index == 1) {
					contactsItemLists.clear();
				}
				if (cusandorg_page != null && cusandorg_page.listResults != null && cusandorg_page.listResults.size() > 0) {
					pageItemList = cusandorg_page.listResults;
					contactsItemLists.clear();
					fillList();
					Collections.sort(contactsItemLists);
					if (orgAndCustAdapter == null) {
						orgAndCustAdapter = new OrgAndCustAdapter(contactsItemLists, DemandLableActivity.this);
						meNeedLv.setAdapter(orgAndCustAdapter);
					} else {
						orgAndCustAdapter.notifyDataSetChanged();
					}
				}
			}
		}
		// 获取好友/人脉的列表数据
		else if (tag == EAPIConsts.PeopleRequestType.PEOPLE_REQ_LIST_TAG) {
			if (object != null) {
				Map<String, Object> dataHm = (Map<String, Object>) object;
				peopleItemLists = (ArrayList<Person>) dataHm.get("obj");
				if (peopleItemLists == null)
					return;
				personItemLists.clear();
				personfillList();
				if (peopleAdapter == null) {
					peopleAdapter = new PeopleAdapter(personItemLists,DemandLableActivity.this);
					meNeedLv.setAdapter(peopleAdapter);
				} else {
					peopleAdapter.notifyDataSetChanged();
				}
			}
		}
	}

	/** 组织客户将请求回来的数据填充到集合中 */
	private void fillList() {
		for (int i = 0; i < pageItemList.size(); i++) {
			Contacts contacts = new Contacts();
			contacts.setCity(pageItemList.get(i).city);
			contacts.setCustomerId(pageItemList.get(i).customerId);
			contacts.setNameFirst(pageItemList.get(i).nameFirst);
			contacts.setIndustrys(pageItemList.get(i).industrys);
			contacts.setName(pageItemList.get(i).name);
			contacts.setShortName(pageItemList.get(i).shotName);
			contacts.setPicLogo(pageItemList.get(i).picLogo);
			contacts.setVirtual(pageItemList.get(i).virtual);
			contactsItemLists.add(contacts);
		}
	}

	/** 人脉将请求回来的数据填充到集合中 */
	private void personfillList() {
		for (int i = 0; i < peopleItemLists.size(); i++) {
			PersonLabelItem personLabelItem = new PersonLabelItem();
			personLabelItem.portrait = peopleItemLists.get(i).portrait;
			personLabelItem.company = peopleItemLists.get(i).company;
			personLabelItem.peopleNameList = peopleItemLists.get(i).peopleNameList;
			personItemLists.add(personLabelItem);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.demand_save_menu, menu);
		SaveItem = menu.findItem(R.id.demandSave);
		SaveItem.setVisible(false);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (lable != null && isType) {
			SaveItem.setVisible(true);
		}

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:// 返回键 就取消
			finish();
			break;
		case R.id.demandSave:
			// 完成按钮点击 调用接口 保存标签的修改
			String changeStr = lableEdit.getText().toString().trim();

			if (TextUtils.isEmpty(changeStr)) {
				showToast("请输入修改后的标签名");
			} else if (lable.tag.equals(changeStr)) {
				showToast("标签未修改");
			} else {
				showLoadingDialog();
				switch (mModulesType) {
				case DemandModules:
					DemandReqUtil.saveTag(this, this, lableEdit.getText()
							.toString(), lable.id, null);
					break;
				case OrgAndCustomModules:

					break;
				case PeopleModules:
					// PeopleReqUtil.saveTag(this, this,
					// lableEdit.getText().toString(),
					// lable.id, null);
					break;
				case KnowledgeModules:

					break;
				}
			}
			break;
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.demandtypeDeleteLl:
			// 删除需求与 界面的关系 调用接口
			if (needItemList.size() == 0) {
				Toast.makeText(this, "没有删除的对象", Toast.LENGTH_SHORT).show();
				;
				return;
			}

			View view = View.inflate(this,
					R.layout.demand_user_setting_dialog1, null);
			String msg = String.format("带有此标签的%d篇文章都将删除此标签,您确定要删除吗？",
					needItemList.size());
			((TextView) view.findViewById(R.id.infoTv)).setText(msg);
			showDialog(view);
			// 确定
			view.findViewById(R.id.confirmTv).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// 确定修改
							// 删除
							deleteLable();
							dialog.dismiss();
						}
					});
			view.findViewById(R.id.containerLl).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});

			break;
		case R.id.lableDelete:
			lableEdit.setText("");
			break;
		}
	}

	@Override
	protected void onDestroy() {
		this.unregisterReceiver(broad);
		super.onDestroy();
	}

	private void deleteLable() {
		List<String> list = new ArrayList<String>();
		for (NeedItemData data : needItemList) {
			list.add(data.demandId);
		}
		showLoadingDialog();
		switch (mModulesType) {
		case DemandModules:
			DemandReqUtil.deleteDemandTag(this, this,
					TextStrUtil.getStringAppend(list, ","), lable.id, null);
			break;
		case PeopleModules:
			// PeopleReqUtil.deletePeopleTag(this, this,
			// TextStrUtil.getStringAppend(list, ","), lable.id, null);
			break;
		case OrgAndCustomModules:

			break;
		case KnowledgeModules:

			break;
		}
	}
}
