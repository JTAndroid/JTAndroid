package com.tr.ui.organization.firstpage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.tr.App;
import com.tr.AppData;
import com.tr.R;
import com.tr.api.OrganizationReqUtil;
import com.tr.image.ImageLoader;
import com.tr.model.demand.AmountData;
import com.tr.model.demand.Metadata;
import com.tr.model.demand.NeedItemData;
import com.tr.model.knowledge.UserCategory;
import com.tr.model.obj.MoneyType;
import com.tr.model.page.JTPage;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.demand.util.AmountUtil;
import com.tr.ui.organization.create_clientele.ClientDetailsActivity;
import com.tr.ui.organization.db.OrgDBManager;
import com.tr.ui.organization.model.CustomerProfileVo;
import com.tr.ui.organization.model.firstpage.Item;
import com.tr.ui.organization.model.firstpage.Page;
import com.tr.ui.organization.model.param.CustomerOrganizatianParams;
import com.tr.ui.organization.model.parameters.OrganizationDetialsIncomingParameters;
import com.tr.ui.people.homepage.ContactsMainPageActivity;
import com.utils.common.GlobalVariable;
import com.utils.common.Util;
import com.utils.display.DisplayUtil;
import com.utils.http.EAPIConsts.OrganizationReqType;
import com.utils.http.IBindData;

/**
 * 组织首页
 * 
 * @author Administrator
 * 
 */
@SuppressLint("ResourceAsColor")
public class FindProjectActivityOrg extends JBaseActivity implements
		OnClickListener, OnCheckedChangeListener, IBindData,
		OnItemClickListener {

	// public static TypeEnum projectType = TypeEnum.invest;
	private XListView infoLv;
	private JTPage jtpage;// 分页加载对象
	private View ll_title;
	private PopupWindow popW;
	private ListView itemLv1;
	private ListView itemLv2;
	private ListView itemLv3;
	private TypeItem typeItem;
	private TypeTitle typeTitle;
	int screenWidth;
	int screenHeight;
	private RadioButton checkRB;
	private MyAdapter adapter;
	private TextView myTitle;
	private List<NeedItemData> needItemList = new ArrayList<NeedItemData>();
	private int index = 1;
	private String location = "";
	private int type = 0;
	private String industry = "";
	/**
	 * 货币类型
	 */
	private List<MoneyType> listMoneyType;
	private OrgDBManager manager;
	private List<String> listMoneyRange;
	private View customLl;
	private View selectContentLL;
	private List<Metadata> itemList;
	private FindProjectAdapterOrg findProjectAdapter;
	protected FindProjectAdapterOrg adapter2;
	protected FindProjectAdapterOrg adapter3;
	private AppData appData;
	private FindProjectAdapterOrg findProjectAdapter2;
	private FindProjectAdapterOrg findProjectAdapter3;
	private List<Metadata> itemList2;
	private List<Metadata> itemList3;
	private Metadata metadata01;
	private Metadata metadata02;
	private Metadata metadata03;
	private int typeI;
	private RadioButton areaRb;
	private RadioButton typeRb;
	private RadioButton vocationRb;
	private RadioButton priceRb;
	private Page page;
	private BitmapUtils bitmapUtils;
	private ImageView search;

	// 弹出的三个
	public enum TypeItem {
		item1, item2, item3
	}

	public enum TypeEnum {
		invest, // 投资
		financing, // 融资
	}

	enum TypeTitle {
		area, type, industry, price
	}

	// 三级筛选
	private String industryId;// 行业id
	private String typeId;// 类型id
	private String areaId = "";// 地区id
	private AmountData amount = new AmountData();// 金额对象
	private TextView categoryTv;
	private TextView tagTv;
	private UserCategory category;
	private int label = 1;

	private long customerId;
	private long createById;
	private Map<String, Object> map;
	private TextView create_Tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.organization_firstpage);
		bitmapUtils = new BitmapUtils(this);
		WindowManager wm = getWindowManager();
		screenWidth = wm.getDefaultDisplay().getWidth();
		screenHeight = wm.getDefaultDisplay().getHeight();
		manager = new OrgDBManager(this);
		infoLv = (XListView) findViewById(R.id.infoLv);
		ll_title = findViewById(R.id.ll_title);
		RadioGroup rg_item = (RadioGroup) findViewById(R.id.itemRg);
		areaRb = (RadioButton) findViewById(R.id.areaRb);
		typeRb = (RadioButton) findViewById(R.id.typeRb);
		vocationRb = (RadioButton) findViewById(R.id.vocationRb);
		priceRb = (RadioButton) findViewById(R.id.priceRb);

		vocationRb.setSingleLine(true);
		areaRb.setSingleLine(true);
		typeRb.setSingleLine(true);
		priceRb.setSingleLine(true);

		areaRb.setOnCheckedChangeListener(this);
		typeRb.setOnCheckedChangeListener(this);
		vocationRb.setOnCheckedChangeListener(this);
		priceRb.setOnCheckedChangeListener(this);
		myTitle.setText("组织");

		adapter = new MyAdapter(this);
		infoLv.setAdapter(adapter);

		startGetData(0);

		initDate();
	}

	/**
	 * 初始化数据
	 */
	public void initDate() {

		infoLv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Item item = adapter.lists.get(position - 1);

				if (item.virtual == 1 || item.virtual == 2) {// 1,2跳组织
					customerId = item.customerId;
					createById = item.createById;
					ENavigate.startOrgMyHomePageActivity(
							FindProjectActivityOrg.this, customerId,
							createById, true, ENavConsts.type_details_org);
				} else if (item.virtual == 0) {// 0,跳客户
					long customerId = item.customerId;
					Intent intent = new Intent(FindProjectActivityOrg.this,
							ClientDetailsActivity.class);
					intent.putExtra("customerId", customerId);
					intent.putExtra("label", label);
					Log.e("TAG", "发现-组织列表" + customerId);
					startActivity(intent);
				}
			}
		});

		infoLv.showFooterView(false);
		// 设置xlistview可以加载、刷新
		infoLv.setPullLoadEnable(true);
		infoLv.setPullRefreshEnable(true);
		infoLv.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				searchDemand(0);
			}

			@Override
			public void onLoadMore() {
				if (index > page.total / page.size) {
					Toast.makeText(FindProjectActivityOrg.this, "没有更多数据显示",
							Toast.LENGTH_SHORT).show();
				}
				startGetData(index);
				index++;
			}
		});

		infoLv.setAdapter(adapter);

	}

	/**
	 * * 找项目/找投资
	 * 
	 * @param context
	 * @param bind
	 * @param index
	 *            起始页，第一页为1
	 * @param size
	 *            页大小，默认为20
	 * @param industryId
	 *            行业ID 可以为null
	 * @param typeId
	 *            类型ID 可以为null
	 * @param areaId
	 *            地区ID 可以为null
	 * @param beginAmount
	 *            需求开始金额
	 * @param endAmount
	 *            需求结束金额 默认和不选择起始和结束都传0即可
	 * @param unit
	 *            金额类型字段
	 * @param demandType
	 *            需求类型（1-投资 2-融资）
	 * @param index
	 */
	public void searchDemand(int index) {
		OrganizationReqUtil.doOrganizationPage(this, this, areaId, type,
				industry, index + "", "20", null);
	}

	/**
	 * 获取页数据
	 */
	public void startGetData(int index) {
		if (index == 0) {
			showLoadingDialog("  ");
		}
		searchDemand(index);

	}

	@Override
	public void onClick(View v) {
		InputMethodManager imm = (InputMethodManager) FindProjectActivityOrg.this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		switch (v.getId()) {
		case R.id.cancelTv:// 取消自定义
			customLl.setVisibility(View.GONE);
			selectContentLL.setVisibility(View.VISIBLE);

			if (imm != null) {
				imm.hideSoftInputFromWindow(FindProjectActivityOrg.this
						.getWindow().getDecorView().getWindowToken(), 0);
			}
			if (itemList2 != null && itemList2.size() > 0) {
				findProjectAdapter2.setSelectItem(0);
			}
			break;

		case R.id.startTv:// 开始筛选
			EditText highestEt = (EditText) customLl
					.findViewById(R.id.highestEt);
			EditText lowEt = (EditText) customLl.findViewById(R.id.lowEt);
			String highest = highestEt.getText().toString();
			String low = lowEt.getText().toString();
			boolean isStart = false;
			boolean isFinis = false;
			if (!TextUtils.isEmpty(low)) {
				isStart = true;
			}
			if (!TextUtils.isEmpty(highest)) {
				isFinis = true;
			}
			if (isStart && isFinis) {
				int number = Integer.parseInt(low);
				int number2 = Integer.parseInt(highest);
				if (number > number2) {
					showToast("起始金额不能大于截止金额");
					return;
				} else {
					amount.beginAmount = number;
					amount.endAmount = number2;
				}
			} else if (isStart) {
				int number = Integer.parseInt(low);
				amount.beginAmount = number;
				amount.endAmount = -1;
			} else if (isFinis) {
				int number2 = Integer.parseInt(highest);
				amount.beginAmount = -1;
				amount.endAmount = number2;
			}

			if (imm != null) {
				imm.hideSoftInputFromWindow(FindProjectActivityOrg.this
						.getWindow().getDecorView().getWindowToken(), 0);
			}
			customLl.setVisibility(View.GONE);
			selectContentLL.setVisibility(View.VISIBLE);
			popW.dismiss();
			break;
		case R.id.imageview:
			finish();
			break;
		}

	}

	/**
	 * 
	 * @param typeIndex
	 *            区域，类型，行业，金额
	 */
	public void showPropu(final TypeTitle typeTitle) {
		this.typeTitle = typeTitle; //
		this.typeItem = TypeItem.item1; // 当前显示的枚举对象
		metadata02 = null;
		metadata03 = null;
		itemList = new ArrayList<Metadata>();
		itemList2 = new ArrayList<Metadata>();
		itemList3 = new ArrayList<Metadata>();
		appData = App.getApp().getAppData();
		if (popW == null) {
			View v = View.inflate(this,
					R.layout.demand_find_project_listview_item, null);
			customLl = v.findViewById(R.id.customLl);// 自定义金额
			selectContentLL = v.findViewById(R.id.selectContentLL);// 要选择的三级联动
			popW = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			itemLv1 = (ListView) v.findViewById(R.id.lv_item1);
			itemLv2 = (ListView) v.findViewById(R.id.lv_item2);
			itemLv3 = (ListView) v.findViewById(R.id.lv_item3);
			popW.setBackgroundDrawable(new BitmapDrawable());
			popW.setFocusable(true);
			popW.setOutsideTouchable(true);
			// popW.setOnDismissListener(onDismiss);
		}
		customLl.setVisibility(View.GONE);
		selectContentLL.setVisibility(View.VISIBLE);
		customLl.findViewById(R.id.cancelTv).setOnClickListener(this);// 取消自定义
		customLl.findViewById(R.id.startTv).setOnClickListener(this);// 开始筛选
		itemLv2.setVisibility(View.GONE);
		itemLv3.setVisibility(View.GONE);
		popW.update();
		popW.showAsDropDown(ll_title, 0, 2);
		// pop关闭时
		popW.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				checkRB.setChecked(false);
				checkRB.setSelected(false);
				// showLoadingDialog("  ");
				// searchDemand(1);
			}
		});

		switch (typeTitle) {
		case area:// 弹出区域
			itemList = showArea();
			break;
		case type:// 弹出类型
			itemList = showType("1");
			break;
		case industry:// 弹出行业
			itemList = showType("15");
			break;
		case price:// 弹出金额
			listMoneyType = appData.getListMoneyType();
			listMoneyRange = appData.getListMoneyRange();
			for (MoneyType moneyType : listMoneyType) {
				Metadata metadata = new Metadata();
				metadata.id = moneyType.tag;
				metadata.name = moneyType.tag + "-" + moneyType.getName();
				itemList.add(metadata);
			}
			itemList.add(0, new Metadata(null, "全部"));
			break;
		}
		// metadata01 = itemList.get(0); //默认选中 不用添加默认选中
		findProjectAdapter = new FindProjectAdapterOrg(TypeItem.item1,
				itemList, this);
		findProjectAdapter.setSelectItem(-1);// 默认都不选中
		itemLv1.setAdapter(findProjectAdapter);
		itemLv1.setOnItemClickListener(this);// 条目1点击事件
		itemLv2.setOnItemClickListener(this);// 条目2点击事件
		itemLv3.setOnItemClickListener(this);// 条目3点击事件
	}

	/**
	 * 区域数据
	 */
	private List<Metadata> showArea() {
		List<Metadata> itemList = manager.queryArea();
		itemList.add(0, new Metadata("0", "全部")); // 区域第一级添加不限
		return itemList;
	}

	/**
	 * 类型，行业 添加数据
	 */
	private List<Metadata> showType(String id) {
		List<Metadata> itemList = manager.queryInvestType(id);
		itemList.add(0, new Metadata("0", "全部"));
		return itemList;
	}

	/**
	 * view进入动画
	 * 
	 * @param v
	 *            执行动画的view
	 * @param fromXDelta
	 * @param toXDelta
	 * @param fromYDelta
	 * @param toYDelta
	 * @param leng
	 *            时长
	 * @return 返回当前view
	 */
	private TranslateAnimation createAnimation(View v, float fromXDelta,
			float toXDelta, float fromYDelta, float toYDelta, int leng) {
		TranslateAnimation ta = new TranslateAnimation(fromXDelta, toXDelta,
				fromYDelta, toYDelta);
		ta.setDuration(leng);
		v.setAnimation(ta);
		ta.start();
		return ta;
	}

	class MyAdapter extends BaseAdapter {
		private Context context;
		public List<Item> lists;

		public MyAdapter(Context context) {
			this.context = context;
			lists = new ArrayList<Item>();
		}

		@Override
		public int getCount() {
			return lists.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(context, R.layout.listview_item,
						null);
				holder.listview_iv = (ImageView) convertView
						.findViewById(R.id.listview_iv);
				holder.listview_tv_company = (TextView) convertView
						.findViewById(R.id.listview_tv_company);
				holder.listview_tv_region = (TextView) convertView
						.findViewById(R.id.listview_tv_region);
				holder.listview_tv_type = (TextView) convertView
						.findViewById(R.id.listview_tv_type);
				holder.listview_iv_head = (ImageView) convertView
						.findViewById(R.id.listview_iv_head);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Item item = lists.get(position);

			// 公司名称
			if (!TextUtils.isEmpty(item.shotName)) {
				holder.listview_tv_company.setText(item.shotName);
			}
			else{
				holder.listview_tv_company.setText(item.name);
			}
			Util.initAvatarImage(context, holder.listview_iv, holder.listview_tv_company.getText().toString(), item.picLogo, 1, 2);

			// 城市
			if (!TextUtils.isEmpty(item.city)) {
				holder.listview_tv_region.setText(item.city);
			}

			// 行业
			if (!TextUtils.isEmpty(item.industrys)) {
				if (!"(null)".equals(item.industrys)) {
					holder.listview_tv_type.setText(item.industrys);
				}
			}
			Drawable drawable = null;
			if (item.virtual == 0) {
				drawable = getResources().getDrawable(R.drawable.contactclienttag);
			} else {
				drawable = getResources().getDrawable(R.drawable.contactorganizationtag);
			}
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
			holder.listview_tv_company.setCompoundDrawables(drawable,null, null, null);
			holder.listview_tv_company.setCompoundDrawablePadding(DisplayUtil.dip2px(context, 10));
			return convertView;
		}
	}

	class ViewHolder {
		ImageView listview_iv;
		TextView listview_tv_company;
		TextView listview_tv_region;
		TextView listview_tv_type;
		ImageView listview_iv_head;
	}

	// 点击标题时调用
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			checkRB = (RadioButton) buttonView;
			checkRB.setSelected(isChecked);
			switch (buttonView.getId()) {
			case R.id.areaRb://
				showPropu(TypeTitle.area); // 显示区域
				break;
			case R.id.typeRb:
				showPropu(TypeTitle.type);// 显示类型
				break;
			case R.id.vocationRb:
				showPropu(TypeTitle.industry);// 显示 行业
				break;
			case R.id.priceRb:// 显示金额
				showPropu(TypeTitle.price);
				break;
			}

		}

	}

	/**
	 * 加入条件后更新数据
	 */
	@Override
	public void initJabActionBar() {

		ActionBar mActionBar = jabGetActionBar();
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(true);
		View mCustomView = getLayoutInflater().inflate(
				R.layout.org_firstpage_actionbar_title, null);
		mActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView
				.getLayoutParams();
		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK
				| Gravity.CENTER_HORIZONTAL;
		mActionBar.setCustomView(mCustomView, mP);
		mActionBar.setTitle(" ");
		myTitle = (TextView) mCustomView.findViewById(R.id.titleTv);
		create_Tv = (TextView) mCustomView.findViewById(R.id.create_Tv);
		search = (ImageView) mCustomView.findViewById(R.id.titleIv);
		search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				ENavigate.startSearchActivity(FindProjectActivityOrg.this,2);
				ENavigate.startNewSearchActivity(FindProjectActivityOrg.this);

			}
		});
		create_Tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ENavigate.startCreateClienteleActivity(FindProjectActivityOrg.this, null, 1, 0L);
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public void bindData(int tag, Object object) {
		dismissLoadingDialog();
		infoLv.stopLoadMore();
		infoLv.stopRefresh();
		infoLv.showFooterView(false);
		if (object == null) {
			return;
		}
		switch (tag) {
		case OrganizationReqType.ORGANIZATION_REQ_GETDISCOVERLIST:
			Map<String, Object> dataHm = (Map<String, Object>) object;
			if (dataHm != null) {
				page = (Page) dataHm.get("page");
				if (page != null) {
					int index = page.index;
					if (index == 0) {
						adapter.lists.clear();
					}

					List<Item> items = page.listResults;
					if (items != null && items.size() > 0) {
						adapter.lists.addAll(items);
					}
				}
				adapter.notifyDataSetChanged();
			}
			break;


		}
	}

	/**
	 * 修改当前选择的数据
	 */
	private void selectData(Metadata metadata, int item) {
		switch (typeTitle) {
		case price:// 金额 //不限制
			if (metadata == null) {
				amount.unit = null;
				amount.beginAmount = -1;
				amount.endAmount = -1;
				priceRb.setText("金额");
			} else {
				amount.unit = metadata.id;
				amount.beginAmount = -1;
				amount.endAmount = -1;
				priceRb.setText(metadata.name);
			}
			break;
		case area:// 地区
			if (metadata == null) {
				areaId = null;// 地区不限制
				areaRb.setText("区域");
			} else {
				if (item == 1) {
					// 一级地区
					if (!metadata01.id.equals("3418")) {
						areaId =  metadata01.name;
					} else {
						areaId = metadata01.name;
					}
				} else if (item == 2) {
					// 二级地区
					if (!metadata01.id.equals("3418")) {
						areaId = metadata01.name + "_" + metadata.name;
					} else {
						areaId = metadata01.name + "_" + metadata.name;
					}
				} else if (item == 3) {
					// 三级级地区
					if (!metadata01.id.equals("3418")) {
						areaId =metadata01.name + "_"
								+ metadata02.name + "_" + metadata.name;
					} else {
						areaId = metadata01.name + "_" + metadata02.name + "_"
								+ metadata.name;
					}
				}
				areaRb.setText(metadata.name);
			}
			break;
		case industry:// 行业
			if (metadata == null) {
				industryId = "0";// 行业不限制
				vocationRb.setText("行业");
			} else {
				industryId = metadata.id;// 行业不限制
				vocationRb.setText(metadata.name);
			}
			break;
		case type:// 类型
			if (metadata == null) {
				typeId = "0";// 类型不限制
				typeRb.setText("类型");
			} else {
				typeId = metadata.id;// 类型不限制
				typeRb.setText(metadata.name);
			}
			break;
		}
	}

	private int getType() {
		String typeName = typeRb.getText().toString();
		if ("类型".equals(typeName)) {
			return 0;
		} else if ("金融机构".equals(typeName)) {
			return 1;
		} else if ("一般企业".equals(typeName)) {
			return 2;
		} else if ("中介机构".equals(typeName)) {
			return 3;
		} else if ("政府机构".equals(typeName)) {
			return 4;
		} else if ("期刊报纸".equals(typeName)) {
			return 5;
		} else if ("研究机构".equals(typeName)) {
			return 6;
		} else if ("电视广播".equals(typeName)) {
			return 7;
		}
		return 8;
	}

	/**
	 * 三级选择
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.lv_item1:// 如果是条目一的点击
			this.typeItem = TypeItem.item1;
			metadata01 = itemList.get(position);// 点击第一级的值
			itemList2.clear();
			if (position == 0) {
				// 一级数据 不限制
				selectData(null, 0);
				popW.dismiss();// 选完就关闭
				chooseData();
			} else {
				findProjectAdapter.setSelectItem(position);
				if (TypeTitle.price == typeTitle) {// 如是点击的是金额
					listMoneyRange = appData.getListMoneyRange();
					for (String moneyType : listMoneyRange) {
						Metadata metadata = new Metadata();
						metadata.id = moneyType;
						metadata.name = moneyType;
						itemList2.add(metadata);
					}
					priceRb.setText(metadata01.name);
					Metadata metadata = new Metadata();
					metadata.id = "自定义";
					metadata.name = "自定义";
					itemList2.add(metadata);
					findProjectAdapter2 = new FindProjectAdapterOrg(
							TypeItem.item2, itemList2,
							FindProjectActivityOrg.this);
				} else {
					findProjectAdapter2 = showData(itemList.get(position));// 查询第二级列表数据
				}
				// 如果有下一级
				if (itemList2 != null && itemList2.size() > 0) {
					itemLv2.setVisibility(View.VISIBLE);
					itemLv3.setVisibility(View.GONE);
					itemLv2.setAdapter(findProjectAdapter2);
					createAnimation(itemLv2, screenWidth / 2, 0, 0, 0, 200);
				} else {// 没有下级
					itemLv2.setVisibility(View.GONE);
					// 就是当前点击的对象
					selectData(metadata01, 1);
					popW.dismiss();
					chooseData();
				}
			}
			break;
		case R.id.lv_item2:// 条目二
			this.typeItem = TypeItem.item2;
			if (position == 0) {// 点击了二级不限
				selectData(metadata01, 1);
				popW.dismiss();
				chooseData();

			} else {
				metadata02 = itemList2.get(position);// 点击第二级的值
				itemList3.clear();
				findProjectAdapter2.setSelectItem(position);
				// 如果是金额
				if (TypeTitle.price == typeTitle) {
					if (position == findProjectAdapter2.getCount() - 1) {// 自定义
						customLl.setVisibility(View.VISIBLE);
						selectContentLL.setVisibility(View.GONE);
					} else {// 金额
						amount = AmountUtil.strToAmount(metadata01.id,
								metadata01.name, metadata02.name);
						popW.dismiss();
					}
				} else {
					// 如果有下一级
					findProjectAdapter3 = showData(itemList2.get(position));
					if (itemList3 != null && itemList3.size() > 0) {
						itemLv3.setAdapter(findProjectAdapter3);
						createAnimation(itemLv3, screenWidth / 3, 0, 0, 0, 200);
						itemLv3.setVisibility(View.VISIBLE);
					} else {
						// 没有下级
						itemLv3.setVisibility(View.GONE);
						selectData(metadata02, 2);
						popW.dismiss();
						chooseData();
					}
				}
			}
			break;
		case R.id.lv_item3:// 点击第三级
			this.typeItem = TypeItem.item3;
			if (position == 0) {
				selectData(metadata02, 2);
				popW.dismiss();
				chooseData();
			} else {
				findProjectAdapter3.setSelectItem(position);
				metadata03 = itemList3.get(position);// 点击第三级的值
				selectData(metadata03, 3);
				popW.dismiss();
				chooseData();
			}
			break;
		case R.id.titleIv:
			ENavigate.startSearchActivity(this,2);
			break;
		}
	}

	// 当三级选择弹窗关闭的时候，拿到三级的选择字段，去选择数据
	private void chooseData() {
		// lists.clear();
		location = getLocation();
		type = getType();
		industry = getIndustry();
		index = 1;
		showLoadingDialog();
		OrganizationReqUtil.doOrganizationPage(this, this, areaId, type,
				industry, "0", "20", null);
		System.out.println("请求的数据location==" + location + "type==" + type
				+ "industry===" + industry);
	}

	private String getIndustry() {
		if ("行业".equals(vocationRb.getText().toString())) {
			return "";
		} else {
			return vocationRb.getText().toString();
		}
	}

	private String getLocation() {
		if ("区域".equals(areaRb.getText().toString())) {
			return "";
		} else {
			return areaRb.getText().toString();
		}
	}

	/**
	 * 第一级下的第二级
	 * 
	 * @param view
	 * @param metadata
	 */
	private FindProjectAdapterOrg showData(Metadata metadata) {
		Metadata data = new Metadata();
		data.id = "全部";
		data.name = "全部";
		FindProjectAdapterOrg adapter = null;
		switch (typeItem) {
		case item1:// 展示第二级
			if (typeTitle == TypeTitle.area) {
				itemList2 = manager.queryArea(metadata.id);
			} else {
				itemList2 = manager.queryInvestType(metadata.id);
			}
			if (itemList2 != null && itemList2.size() > 0) {
				itemList2.add(0, data);
				adapter = new FindProjectAdapterOrg(TypeItem.item2, itemList2,
						FindProjectActivityOrg.this);
			}

			break;

		case item2:// 展示第三级
			if (typeTitle == TypeTitle.area) {
				itemList3 = manager.queryArea(metadata.id);
			} else {
				itemList3 = manager.queryInvestType(metadata.id);
			}
			if (itemList3 != null && itemList3.size() > 0) {
				itemList3.add(0, data);
				adapter = new FindProjectAdapterOrg(TypeItem.item3, itemList3,
						FindProjectActivityOrg.this);
			}

			break;
		case item3:

			break;
		}
		return adapter;
	}

}
