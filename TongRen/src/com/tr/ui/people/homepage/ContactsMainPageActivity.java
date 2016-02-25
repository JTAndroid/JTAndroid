package com.tr.ui.people.homepage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tr.App;
import com.tr.AppData;
import com.tr.R;
import com.tr.api.PeopleReqUtil;
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
import com.tr.ui.people.db.PeopleDBManager;
import com.tr.ui.people.homepage.adapter.FindProjectAdapter_People;
import com.tr.ui.people.model.PeoplePage;
import com.tr.ui.people.model.PersonPage;
import com.tr.ui.people.model.PersonSimple;
import com.utils.common.GlobalVariable;
import com.utils.common.Util;
import com.utils.display.DisplayUtil;
import com.utils.http.EAPIConsts.PeopleRequestType;
import com.utils.http.IBindData;
import com.utils.image.AnimateFirstDisplayListener;

/**
 *  发现/人脉页面
 */
@SuppressLint("ResourceAsColor")
public class ContactsMainPageActivity extends JBaseActivity implements OnClickListener, OnCheckedChangeListener, IBindData, OnItemClickListener {

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
	private List<PersonSimple> lists = new ArrayList<PersonSimple>();
	/**
	 * 货币类型
	 */
	private List<MoneyType> listMoneyType;
	private PeopleDBManager manager;
	private List<String> listMoneyRange;
	private View customLl;
	private View selectContentLL;
	private List<Metadata> itemList;
	private FindProjectAdapter_People findProjectAdapter;
	private AppData appData;
	private FindProjectAdapter_People findProjectAdapter2;
	private FindProjectAdapter_People findProjectAdapter3;
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
	private PersonPage personPage;
	// private BitmapUtils bitmapUtils;
	private ImageView search;
	private int typeId = 0;
	private int regionId = 0;
	private int careerId = 0;
	private int index = 1;
	private int size = 10;// 分页加载每页默认的条数

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
	private String typeid;// 类型id
	private String areaId;// 地区id
	private AmountData amount = new AmountData();// 金额对象
	private TextView categoryTv;
	private TextView tagTv;
	private UserCategory category;
	private TextView create_Tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_contactsmainpage);
		// bitmapUtils = new BitmapUtils(this);
		WindowManager wm = getWindowManager();
		screenWidth = wm.getDefaultDisplay().getWidth();
		screenHeight = wm.getDefaultDisplay().getHeight();
		manager = new PeopleDBManager(this);
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

		myTitle.setText("人脉");
		startGetData(1);
		initDate();
	}

	/**
	 * 初始化数据
	 */
	public void initDate() {
		infoLv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position > 0) {
					int personType = lists.get(position - 1).persontype;
					if (personType == 1) {// 个人中心/他人主页 用户
//						ENavigate.startRelationHomeActivity(ContactsMainPageActivity.this, String.valueOf(lists.get(position - 1).personid));
							ENavigate.startRelationHomeActivity(
									ContactsMainPageActivity.this, String.valueOf(lists.get(position - 1).personid),false,ENavConsts.TYPE_CONNECTIONS_HOME_PAGE,false);
					} else if (personType == 2) {// 人脉详情
//						ENavigate.startContactsDetailsActivity(ContactsMainPageActivity.this, 2, lists.get(position - 1).personid, 0);
						ENavigate.startRelationHomeActivity(
								ContactsMainPageActivity.this, String.valueOf(lists.get(position - 1).personid),false,ENavConsts.TYPE_CONNECTIONS_HOME_PAGE,false);
					}
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
				index = 1;
				searchDemand(1);
			}

			@Override
			public void onLoadMore() {
				if (index > personPage.count / size) {
					Toast.makeText(ContactsMainPageActivity.this, "没有更多数据显示", Toast.LENGTH_SHORT).show();
				}
				index++;
				startGetData(index);
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
		PeoplePage p = new PeoplePage();
		p.setTypeId(typeId);
		p.setRegionId(regionId);
		p.setCareerId(careerId);
		p.setIndex(index);
		PeopleReqUtil.doRequestWebAPI(this, this, p, null, PeopleRequestType.PEOPLE_REQ_HOME);
	}

	/**
	 * 获取页数据
	 */
	public void startGetData(int index) {
		if (index == 1) {
			showLoadingDialog("  ");
		}
		searchDemand(index);
	}

	@Override
	public void onClick(View v) {
		InputMethodManager imm = (InputMethodManager) ContactsMainPageActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
		switch (v.getId()) {
		case R.id.cancelTv:// 取消自定义
			customLl.setVisibility(View.GONE);
			selectContentLL.setVisibility(View.VISIBLE);

			if (imm != null) {
				imm.hideSoftInputFromWindow(ContactsMainPageActivity.this.getWindow().getDecorView().getWindowToken(), 0);
			}
			if (itemList2 != null && itemList2.size() > 0) {
				findProjectAdapter2.setSelectItem(0);
			}
			break;

		case R.id.startTv:// 开始筛选
			EditText highestEt = (EditText) customLl.findViewById(R.id.highestEt);
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
				imm.hideSoftInputFromWindow(ContactsMainPageActivity.this.getWindow().getDecorView().getWindowToken(), 0);
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
	 *            区域，分类，职业，金额
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
			View v = View.inflate(this, R.layout.demand_find_project_listview_item, null);
			customLl = v.findViewById(R.id.customLl);// 自定义金额
			selectContentLL = v.findViewById(R.id.selectContentLL);// 要选择的三级联动
			popW = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
		case type:// 弹出分类
			itemList = showType(500);
			break;
		case industry:// 弹出职业
			itemList = showType(0);
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
		findProjectAdapter = new FindProjectAdapter_People(ContactsMainPageActivity.TypeItem.item1, itemList, this);
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
	 * 分类，职业 添加数据 65535 0
	 */
	private List<Metadata> showType(int id) {
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
	private TranslateAnimation createAnimation(View v, float fromXDelta, float toXDelta, float fromYDelta, float toYDelta, int leng) {
		TranslateAnimation ta = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta);
		ta.setDuration(leng);
		v.setAnimation(ta);
		ta.start();
		return ta;
	}

	class MyAdapter extends BaseAdapter {
		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

		private Context context;

		public MyAdapter(Context context) {
			this.context = context;
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
			PersonSimple personSimple = lists.get(position);
			String picpath = personSimple.picpath;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(context, R.layout.person_contactsmainpage_item, null);
				holder.imageview = (ImageView) convertView.findViewById(R.id.imageview);// 头像Logo
				holder.name = (TextView) convertView.findViewById(R.id.name);// 姓名
				holder.work = (TextView) convertView.findViewById(R.id.work);// 职务
				holder.person = (ImageView) convertView.findViewById(R.id.person);// 人脉
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.name.setText(personSimple.name2 /*+ personSimple.name2*/);// 设置名字
			try {
				Util.initAvatarImage(context, holder.imageview, holder.name.getText().toString(), picpath, 1, 1);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			holder.work.setText(personSimple.company + " " + personSimple.position);// 设置公司和职务
			Drawable drawable = null;
			if (personSimple.persontype == 1) {// 是用户 不显示
				drawable = getResources().getDrawable(R.drawable.contactusertag);
			} else {// 是人脉 显示
				drawable = getResources().getDrawable(R.drawable.contactpeopletag);
			}
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
			holder.name.setCompoundDrawables(drawable,null, null, null);
			holder.name.setCompoundDrawablePadding(DisplayUtil.dip2px(ContactsMainPageActivity.this, 10));
			return convertView;
		}
		class ViewHolder {
			ImageView imageview;// 头像Logo
			TextView name;// 姓名
			TextView work;// 职务
			ImageView person;// 人脉 客户logo
			ImageView contacts_main_page_dial;// 电话Logo
			ImageView contacts_main_page_message;// 短信Logo
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
		mActionBar.setDisplayShowHomeEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(true);
		View mCustomView = getLayoutInflater().inflate(R.layout.org_firstpage_actionbar_title, null);
		mActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView.getLayoutParams();
		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK | Gravity.CENTER_HORIZONTAL;
		mActionBar.setCustomView(mCustomView, mP);
		mActionBar.setTitle(" ");
		myTitle = (TextView) mCustomView.findViewById(R.id.titleTv);
		create_Tv = (TextView) mCustomView.findViewById(R.id.create_Tv);
		create_Tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ENavigate.startNewConnectionsActivity(ContactsMainPageActivity.this, 1, null, 11);
			}
		});
		search = (ImageView) mCustomView.findViewById(R.id.titleIv);
		search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				ENavigate.startSearchActivity(ContactsMainPageActivity.this,1);
				ENavigate.startNewSearchActivity(ContactsMainPageActivity.this);
			}
		});
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

		switch (tag) {
		case PeopleRequestType.PEOPLE_REQ_HOME:
			if (object == null) {
				return;
			}
			personPage = (PersonPage) object;
			if (index == 1) {
				lists.clear();
			}
			if (personPage.list != null && personPage.list.size() > 0) {
				lists.addAll(personPage.list);
				if (adapter == null) {
					adapter = new MyAdapter(this);
					infoLv.setAdapter(adapter);
				} else {
					adapter.notifyDataSetChanged();
				}
			} else {
				if (adapter == null) {
					adapter = new MyAdapter(this);
					infoLv.setAdapter(adapter);
				} else {
					adapter.notifyDataSetChanged();
				}
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
						areaId = "国内-" + metadata01.name + "-" + metadata02.name + "-" + metadata.name;
					} else {
						areaId = metadata01.name + "-" + metadata02.name + "-" + metadata.name;
					}
				}
				areaRb.setText(metadata.name);
			}
			break;
		case industry:// 行业
			if (metadata == null) {
				industryId = "0";// 行业不限制
				vocationRb.setText("职位");
			} else {
				industryId = metadata.id;// 行业不限制
				vocationRb.setText(metadata.name);
			}
			break;
		case type:// 类型
			if (metadata == null) {
				typeid = "0";// 类型不限制
				typeRb.setText("分类");
			} else {
				typeid = metadata.id;// 类型不限制
				typeRb.setText(metadata.name);
			}
			break;
		}
	}

	private int getType() {
		String typeName = typeRb.getText().toString();
		if ("分类".equals(typeName)) {
			return 0;
		} else if ("娱乐人物".equals(typeName)) {
			return 99;
		} else if ("政治人物".equals(typeName)) {
			return 100;
		} else if ("体育人物".equals(typeName)) {
			return 101;
		} else if ("历史人物".equals(typeName)) {
			return 102;
		} else if ("文化人物".equals(typeName)) {
			return 103;
		} else if ("科学家".equals(typeName)) {
			return 104;
		} else if ("虚拟人物".equals(typeName)) {
			return 105;
		} else if ("行业人物".equals(typeName)) {
			return 106;
		} else if ("话题人物".equals(typeName)) {
			return 107;
		}
		return 108;

	}

	/**
	 * 三级选择
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
					findProjectAdapter2 = new FindProjectAdapter_People(TypeItem.item2, itemList2, this);
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
						amount = AmountUtil.strToAmount(metadata01.id, metadata01.name, metadata02.name);
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
			ENavigate.startSearchActivity(this,1);
			break;
		}
	}

	private int getIndustry() {
		if ("职位".equals(vocationRb.getText().toString())) {
			return 0;
		} else {
			String choose = vocationRb.getText().toString();
			int id = manager.queryIdByIndustry(choose);
			return id;
		}
	}

	private int getLocation() {
		if ("区域".equals(areaRb.getText().toString())) {
			return 0;
		} else {
			String chooseData = areaRb.getText().toString();
			int id = manager.queryIdByName(chooseData);
			return id;
		}
	}

	/**
	 * 第一级下的第二级
	 * 
	 * @param view
	 * @param metadata
	 */
	private FindProjectAdapter_People showData(Metadata metadata) {
		Metadata data = new Metadata();
		data.id = "全部";
		data.name = "全部";
		FindProjectAdapter_People adapter = null;
		switch (typeItem) {
		case item1:// 展示第二级
			if (typeTitle == TypeTitle.area) {
				itemList2 = manager.queryArea(metadata.id);
			} else {
				itemList2 = manager.queryInvestType(Integer.parseInt(metadata.id));
			}
			if (itemList2 != null && itemList2.size() > 0) {
				itemList2.add(0, data);
				adapter = new FindProjectAdapter_People(TypeItem.item2, itemList2, this);
			}

			break;

		case item2:// 展示第三级
			if (typeTitle == TypeTitle.area) {
				itemList3 = manager.queryArea(metadata.id);
			} else {
				itemList3 = manager.queryInvestType(Integer.parseInt(metadata.id));
			}
			if (itemList3 != null && itemList3.size() > 0) {
				itemList3.add(0, data);
				adapter = new FindProjectAdapter_People(TypeItem.item3, itemList3, this);
			}
			break;
		case item3:

			break;
		}
		return adapter;
	}

	private void chooseData() {
		lists.clear();
		regionId = getLocation();
		typeId = getType();
		careerId = getIndustry();
		index = 1;
		PeoplePage p = new PeoplePage();
		p.setRegionId(regionId);
		p.setTypeId(typeId);
		p.setCareerId(careerId);
		p.setIndex(index);
		p.setSize(10);
		showLoadingDialog();
		PeopleReqUtil.doRequestWebAPI(this, this, p, null, PeopleRequestType.PEOPLE_REQ_HOME);
	}

}
