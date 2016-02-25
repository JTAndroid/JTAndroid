package com.tr.ui.demand;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tr.App;
import com.tr.AppData;
import com.tr.R;
import com.tr.api.DemandReqUtil;
import com.tr.db.DemandDBManager;
import com.tr.model.demand.AmountData;
import com.tr.model.demand.Metadata;
import com.tr.model.demand.NeedItemData;
import com.tr.model.demand.NeedItemListItem;
import com.tr.model.obj.MoneyType;
import com.tr.model.page.JTPage;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.adapter.FindProjectAdapter;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.demand.util.AmountUtil;
import com.tr.ui.demand.util.ChooseDataUtil;
import com.utils.common.ViewHolder;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.time.TimeUtil;

/**
 * 找项目/投资/融资
 * 
 * @author Administrator
 *
 */
@SuppressLint("ResourceAsColor")
public class FindProjectActivity extends JBaseActivity implements
		OnClickListener, OnCheckedChangeListener, IBindData,
		OnItemClickListener {

	public static TypeEnum projectType = TypeEnum.invest;
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
	private Myadapter adapter;
	private TextView myTitle;
	private List<NeedItemData> needItemList = new ArrayList<NeedItemData>();
	/**
	 * 货币类型
	 */
	private List<MoneyType> listMoneyType;
	private DemandDBManager manager;
	private List<String> listMoneyRange;
	private View customLl;
	private View selectContentLL;
	// private List<Area> listArea;
	private List<Metadata> itemList;
	private FindProjectAdapter findProjectAdapter;
	protected FindProjectAdapter adapter2;
	protected FindProjectAdapter adapter3;
	private AppData appData;
	private FindProjectAdapter findProjectAdapter2;
	private FindProjectAdapter findProjectAdapter3;
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
	private EditText highestEt;// 最高
	private EditText lowEt; // 最低

	// 弹出的三个
	public enum TypeItem {
		item1, item2, item3
	}

	public enum TypeEnum {
		invest, // 投资
		financing, // 融资
	}

	public enum TypeTitle {
		area, type, industry, price
	}

	// 三级筛选
	private String industryId;// 行业id
	private String typeId;// 类型id
	private String areaId;// 地区id
	private AmountData amount = new AmountData();// 金额对象
	private TextView create_Tv;
	private MenuItem findItem;
	private boolean isCreate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demand_find_project);
		WindowManager wm = getWindowManager();
		screenWidth = wm.getDefaultDisplay().getWidth();
		screenHeight = wm.getDefaultDisplay().getHeight();
		manager = new DemandDBManager(this);
		infoLv = (XListView) findViewById(R.id.infoLv);

		ll_title = findViewById(R.id.ll_title);
		// 投资或融资
		Bundle extras = getIntent().getExtras();
		projectType = extras.getInt(ENavConsts.DEMAND_TYPE) == 1 ? TypeEnum.invest
				: TypeEnum.financing;
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
		switch (projectType) {
		case invest:// 投资
			typeI = ChooseDataUtil.CHOOSE_type_OutInvestType;
			myTitle.setText("找项目");// 设置头
			break;

		case financing:// 融资
			typeI = ChooseDataUtil.CHOOSE_type_InInvestType;
			myTitle.setText("找资金");// 设置头
			break;
		}

		startGetData();
		initDate();

	}

	/**
	 * 初始化数据
	 */
	public void initDate() {

		adapter = new Myadapter();

		// 需求详情
		infoLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// NeedItemData data =
				// (NeedItemData)adapter.getItem(position-1);
				NeedItemData data = needItemList.get(position - 1);
				ENavigate.startNeedDetailsActivity(FindProjectActivity.this,
						data.demandId, 3);
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
				startGetData();
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
		DemandReqUtil.getProjectList(FindProjectActivity.this,
				FindProjectActivity.this, index, 20, industryId, typeId,
				areaId, amount.unit, amount.beginAmount, amount.endAmount,
				typeI, null);
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
		searchDemand(nowIndex);

	}

	@Override
	public void onClick(View v) {
		InputMethodManager imm = (InputMethodManager) FindProjectActivity.this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		switch (v.getId()) {
		case R.id.cancelTv:// 取消自定义
			customLl.setVisibility(View.GONE);
			selectContentLL.setVisibility(View.VISIBLE);

			if (imm != null) {
				imm.hideSoftInputFromWindow(FindProjectActivity.this
						.getWindow().getDecorView().getWindowToken(), 0);
			}
			if (itemList2 != null && itemList2.size() > 0) {
				findProjectAdapter2.setSelectItem(0);
			}
			break;
		case R.id.startTv:// 开始筛选

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
				float number = 0;
				float number2 = 0;
				try {
					number = Float.parseFloat(low);
					number2 = Float.parseFloat(highest);
				} catch (Exception e) {
					showToast("金额解析错误");
					return;
				}
				if (number > number2) {
					showToast("起始金额不能大于截止金额");
					return;
				} else {
					amount.beginAmount = number;
					amount.endAmount = number2;
				}
			} else if (isStart) {
				float number = Float.parseFloat(low);
				amount.beginAmount = number;
				amount.endAmount = -1;
			} else if (isFinis) {
				float number2 = Float.parseFloat(highest);
				amount.beginAmount = -1;
				amount.endAmount = number2;
			}

			if (imm != null) {
				imm.hideSoftInputFromWindow(FindProjectActivity.this
						.getWindow().getDecorView().getWindowToken(), 0);
			}
			customLl.setVisibility(View.GONE);
			selectContentLL.setVisibility(View.VISIBLE);
			popW.dismiss();
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
			highestEt = (EditText) customLl.findViewById(R.id.highestEt);
			lowEt = (EditText) customLl.findViewById(R.id.lowEt);
			lowEt.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					if (s.toString().contains(".")) {
						if (s.length() - 1 - s.toString().indexOf(".") > 1) {
							s = s.toString().subSequence(0,
									s.toString().indexOf(".") + 2);
							lowEt.setText(s);
							lowEt.setSelection(s.length());
						}
					}
					if (s.toString().trim().substring(0).equals(".")) {
						s = "0" + s;
						lowEt.setText(s);
						lowEt.setSelection(2);
					}

					if (s.toString().startsWith("0")
							&& s.toString().trim().length() > 1) {
						if (!s.toString().substring(1, 2).equals(".")) {
							lowEt.setText(s.subSequence(0, 1));
							lowEt.setSelection(1);
							return;
						}
					}
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void afterTextChanged(Editable s) {

				}
			});

			highestEt.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					if (s.toString().contains(".")) {
						if (s.length() - 1 - s.toString().indexOf(".") > 1) {
							s = s.toString().subSequence(0,
									s.toString().indexOf(".") + 2);
							highestEt.setText(s);
							highestEt.setSelection(s.length());
						}
					}
					if (s.toString().trim().substring(0).equals(".")) {
						s = "0" + s;
						highestEt.setText(s);
						highestEt.setSelection(2);
					}

					if (s.toString().startsWith("0")
							&& s.toString().trim().length() > 1) {
						if (!s.toString().substring(1, 2).equals(".")) {
							highestEt.setText(s.subSequence(0, 1));
							highestEt.setSelection(1);
							return;
						}
					}
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void afterTextChanged(Editable s) {

				}
			});
			selectContentLL = v.findViewById(R.id.selectContentLL);// 要选择的三级联动
			popW = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			itemLv1 = (ListView) v.findViewById(R.id.lv_item1);
			itemLv2 = (ListView) v.findViewById(R.id.lv_item2);
			itemLv3 = (ListView) v.findViewById(R.id.lv_item3);
			popW.setBackgroundDrawable(new BitmapDrawable());
			popW.setFocusable(true);
			popW.setOutsideTouchable(true);
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
				showLoadingDialog();
				searchDemand(0);
			}
		});

		switch (typeTitle) {
		case area:// 弹出区域
			itemList = showArea();
			break;
		case type:// 弹出类型
			switch (projectType) {
			case invest:
				itemList = showType("1");
				break;
			case financing:
				itemList = showType("8");
				break;
			}
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
			itemList.add(0, new Metadata(null, "不限"));
			break;
		}
		// metadata01 = itemList.get(0); //默认选中 不用添加默认选中
		findProjectAdapter = new FindProjectAdapter(TypeItem.item1, itemList,
				this);
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

	class Myadapter extends BaseAdapter {
		public Myadapter() {
		}

		@Override
		public int getCount() {
			return needItemList == null ? 0 : needItemList.size();
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
			if (convertView == null) {
				convertView = View.inflate(FindProjectActivity.this,
						R.layout.demand_find_project_item_info, null);

			}
			TextView investOrFinancingTitle = ViewHolder.get(convertView,
					R.id.investOrFinancingTitle);
			TextView priceTv = ViewHolder.get(convertView, R.id.priceTv);
			TextView needTypeTv1 = ViewHolder
					.get(convertView, R.id.needTypeTv1);
			TextView needTypeTv2 = ViewHolder
					.get(convertView, R.id.needTypeTv2);
			TextView needTypeTv3 = ViewHolder
					.get(convertView, R.id.needTypeTv3);
			TextView timeTv = ViewHolder.get(convertView, R.id.timeTv);
			View divisionLineV1 = ViewHolder.get(convertView,
					R.id.divisionLineV1);
			View divisionLineV2 = ViewHolder.get(convertView,
					R.id.divisionLineV2);
			divisionLineV1.setVisibility(View.GONE);
			divisionLineV2.setVisibility(View.GONE);
			NeedItemData needItem = needItemList.get(position);
			investOrFinancingTitle.setText(needItem.title.value);// 标题对象
			if (needItem.amount != null) {
				priceTv.setText(needItem.amount.getAmountData());
			} else {
				priceTv.setText("");
			}
			timeTv.setText(TimeUtil.TimeFormat(needItem.createTime));
			if (needItem.area != null && needItem.area.isVisable
					&& needItem.area.list != null
					&& needItem.area.list.size() > 0) {// 地区
				if (needItem.area.list.size() > 1) {
					needTypeTv1.setText(needItem.area.list.get(0).lastName()
							+ "...");
				} else {
					needTypeTv1.setText(needItem.area.list.get(0).lastName());
				}
				needTypeTv1.setVisibility(View.VISIBLE);
				needTypeTv1.setText(needItem.area.list.get(0).lastName());
			} else {
				needTypeTv1.setVisibility(View.GONE);
			}
			if (needItem.type != null && needItem.type.isVisable
					&& needItem.type.list != null
					&& needItem.type.list.size() > 0) {// 类型
				if (needItem.type.list.size() > 1) {
					needTypeTv2.setText(needItem.type.list.get(0).name + "...");
				} else {
					needTypeTv2.setText(needItem.type.list.get(0).name);
				}
				needTypeTv2.setVisibility(View.VISIBLE);
			} else {
				needTypeTv2.setVisibility(View.GONE);
			}
			if (needItem.industry != null && needItem.industry.isVisable
					&& needItem.industry.list != null
					&& needItem.industry.list.size() > 0) {// 行业
				if (needItem.industry.list.size() > 1) {
					needTypeTv3.setText(needItem.industry.list.get(0).name
							+ "...");
				} else {
					needTypeTv3.setText(needItem.industry.list.get(0).name);
				}
				needTypeTv3.setVisibility(View.VISIBLE);
			} else {
				needTypeTv3.setVisibility(View.GONE);
			}
			if (needTypeTv1.getVisibility() == View.VISIBLE
					&& (needTypeTv2.getVisibility() == View.VISIBLE || needTypeTv3
							.getVisibility() == View.VISIBLE)) {
				divisionLineV1.setVisibility(View.VISIBLE);
			}

			if (needTypeTv2.getVisibility() == View.VISIBLE
					&& needTypeTv3.getVisibility() == View.VISIBLE) {
				divisionLineV2.setVisibility(View.VISIBLE);
			}

			return convertView;
		}

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
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(true);
		View mCustomView = getLayoutInflater().inflate(
				R.layout.demand_actionbar_title, null);
		mActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView
				.getLayoutParams();
		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK
				| Gravity.CENTER_HORIZONTAL;
		mActionBar.setCustomView(mCustomView, mP);
		mActionBar.setTitle(" ");
		myTitle = (TextView) mCustomView.findViewById(R.id.titleTv);
		mCustomView.findViewById(R.id.titleIv).setVisibility(View.GONE);
	}

	@Override
	public void bindData(int tag, Object object) {
		dismissLoadingDialog();
		infoLv.stopLoadMore();
		infoLv.stopRefresh();
		infoLv.showFooterView(false);
		if (object == null) {
			return;
		}

		if (tag == EAPIConsts.demandReqType.demand_getProjectList) {
			NeedItemListItem item = (NeedItemListItem) object;
			jtpage = item.getJtPage();
			if (jtpage == null) {
				return;
			}
			if (jtpage.getIndex() == 0) {
				needItemList.clear();
			}
			if ((jtpage != null) && (jtpage.getLists() != null)) {
				for (int i = 0; i < jtpage.getLists().size(); i++) {
					needItemList.add((NeedItemData) jtpage.getLists().get(i));
				}
				adapter.notifyDataSetChanged();
			}
			if (jtpage.getTotalPage() - 1 == jtpage.getIndex()) {
				infoLv.setPullLoadEnable(false);
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home_search, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.search_home) {// 搜索
			ENavigate.startNewSearchActivity(this, 5);
		}else if (item.getItemId() == R.id.create) {
			ENavigate.startDemandActivityForResult(FindProjectActivity.this, 999);
		}
		return super.onOptionsItemSelected(item);
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
						areaId = "国内-" + metadata01.name;
					} else {
						areaId = metadata01.name;
					}
				} else if (item == 2) {
					// 二级地区
					if (!metadata01.id.equals("3418")) {
						areaId = "国内-" + metadata01.name + "-" + metadata.name;
					} else {
						areaId = metadata01.name + "-" + metadata.name;
					}
				} else if (item == 3) {
					// 三级级地区
					if (!metadata01.id.equals("3418")) {
						areaId = "国内-" + metadata01.name + "-"
								+ metadata02.name + "-" + metadata.name;
					} else {
						areaId = metadata01.name + "-" + metadata02.name + "-"
								+ metadata.name;
					}
				}
				areaRb.setText(metadata.name);
			}
			break;
		case industry:// 行业
			if (metadata == null) {
				industryId = null;// 行业不限制
				vocationRb.setText("行业");
			} else {
				industryId = metadata.number;// 行业不限制
				vocationRb.setText(metadata.name);
			}
			break;
		case type:// 类型
			if (metadata == null) {
				typeId = null;// 类型不限制
				typeRb.setText("类型");
			} else {
				typeId = metadata.number;// 类型不限制
				typeRb.setText(metadata.name);
			}
			break;
		}
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
					findProjectAdapter2 = new FindProjectAdapter(
							TypeItem.item2, itemList2, FindProjectActivity.this);
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
				}
			}
			break;
		case R.id.lv_item2:// 条目二
			this.typeItem = TypeItem.item2;
			if (position == 0) {// 点击了二级不限
				selectData(metadata01, 1);
				popW.dismiss();
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
					}
				}
			}
			break;
		case R.id.lv_item3:// 点击第三级
			this.typeItem = TypeItem.item3;
			if (position == 0) {
				selectData(metadata02, 2);
				popW.dismiss();
			} else {
				findProjectAdapter3.setSelectItem(position);
				metadata03 = itemList3.get(position);// 点击第三级的值
				selectData(metadata03, 3);
				popW.dismiss();
			}
			break;
		}

	}

	/**
	 * 第一级下的第二级
	 * 
	 * @param view
	 * @param metadata
	 */
	private FindProjectAdapter showData(Metadata metadata) {
		Metadata data = new Metadata();
		if (typeTitle == TypeTitle.price) {
			data.id = "不限";
			data.name = "不限";
		} else {
			data.id = "全部";
			data.name = "全部";
		}
		FindProjectAdapter adapter = null;
		switch (typeItem) {
		case item1:// 展示第二级
			if (typeTitle == TypeTitle.area) {
				itemList2 = manager.queryArea(metadata.id);
			} else {
				itemList2 = manager.queryInvestType(metadata.id);
			}
			if (itemList2 != null && itemList2.size() > 0) {
				itemList2.add(0, data);
				adapter = new FindProjectAdapter(TypeItem.item2, itemList2,
						FindProjectActivity.this);
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
				adapter = new FindProjectAdapter(TypeItem.item3, itemList3,
						FindProjectActivity.this);
			}

			break;
		case item3:

			break;
		}
		return adapter;
	}

	@Override
	public void onLoadingDialogCancel() {

	}
}
