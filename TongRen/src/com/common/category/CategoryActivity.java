package com.common.category;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.common.category.CategoryAdapter.CategoryViewHolder;
import com.common.constvalue.EnumConst.ModuleType;
import com.common.evebusmodel.AddCategoryEvent;
import com.tr.App;
import com.tr.R;
import com.tr.api.DemandReqUtil;
import com.tr.api.KnowledgeReqUtil;
import com.tr.api.OrganizationReqUtil;
import com.tr.api.PeopleReqUtil;
import com.tr.model.demand.NeedItemData;
import com.tr.model.demand.NeedItemListItem;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.knowledge.UserCategory;
import com.tr.model.obj.Connections;
import com.tr.model.page.JTPage;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.organization.create_clientele.ClientDetailsActivity;
import com.tr.ui.organization.model.Contacts;
import com.tr.ui.organization.model.cusandorg.CusAndOrg_Page;
import com.tr.ui.organization.model.cusandorg.PageItem;
import com.tr.ui.people.model.MyContacts;
import com.tr.ui.people.model.PersonSimple;
import com.tr.ui.people.model.PersonSimpleList;
import com.tr.ui.widgets.HorizontalListView;
import com.tr.ui.widgets.KnoCategoryAlertDialog;
import com.tr.ui.widgets.KnoCategoryAlertDialog.OnDialogClickListener;
import com.tr.ui.widgets.KnoCategoryAlertDialog.OperType;
import com.tr.ui.widgets.KnoCategoryOperateDialog;
import com.tr.ui.widgets.KnoCategoryOperateDialog.OnSelectListener;
import com.utils.common.EConsts;
import com.utils.http.EAPIConsts;
import com.utils.http.EAPIConsts.KnoReqType;
import com.utils.http.EAPIConsts.PeopleRequestType;
import com.utils.http.IBindData;

/**
 *  这个类是一个四大组件<人脉/好友、组织、需求、知识>关联目录的展示、管理的页面
 * < p >四大组件页面跳转到该页面时区别类型{@link com.common.constvalue.EnumConst.ModuleType }，默认是知识</p>
 * < p >启动关联的目录，调用ENavigate.startKnowledgeCategoryActivityForResult( )方法  </p>
 * < t >知识 ，传入参数如下< /t >
 *<pre>
 *  &lt; @param Activity activity 当前调用者XXXActivity.this
 *	&lt; @param Integer requestCode 请求码
 *	&lt; @param ArrayList<UserCategory> listCategory 创建/编辑时选择目录关联时传入，其他为null；
 *	&lt; @param boolean isSelect 创建/编辑时选择目录关联时传true，其他false
 *	&lt; @param String activityName 目录列表上的小标题
 *</pre>
 * < t >组织/需求 ，传入参数如下< /t >
 *<pre>
 *  &lt; @param Activity activity 当前调用者XXXActivity.this
 *	&lt; @param Integer requestCode 请求码
 *	&lt; @param ArrayList<UserCategory> listCategory 创建/编辑时选择目录关联时传入，其他为null；
 *	&lt; @param ModuleType categoryType ModuleType对象，类型
 *	&lt; @param boolean isSelect 创建/编辑时选择目录关联时传true，其他false
 *	&lt; @param String activityName 目录列表上的小标题
 *</pre>
 * < t >人脉 ，传入参数如下< /t >
 *<pre>
 *  &lt; @param Activity activity 当前调用者XXXActivity.this
 *  &lt; @param Fragment fragment 不为null是从fragment页面进行跳转的
 *	&lt; @param Integer requestCode 请求码
 *	&lt; @param ArrayList<UserCategory> listCategory 创建/编辑时选择目录关联时传入，其他为null；
 *	&lt; @param ModuleType categoryType ModuleType对象，类型
 *	&lt; @param boolean isSelect 创建/编辑时选择目录关联时传true，其他false
 *	&lt; @param String activityName 目录列表上的小标题
 *</pre>
 *   
 */

	

public class CategoryActivity extends JBaseActivity implements
		IBindData {

	private final String TAG = getClass().getSimpleName();

//	private final int SELECT_ITEM_COLOR = 0XFFFFA500; // 选中项目颜色
//	private final int NORMAL_TEXT_COLOR = 0XFF000000; // 默认文字颜色

	private EditText keywordEt; // 搜索关键字
	private ImageView addIv; // 添加
	private XListView categoryLv; // 目录列表
//	private SwipeRefreshLayout refreshSrl; // 刷新
	private KnoCategoryOperateDialog operDialog; // 操作对话框
	private KnoCategoryAlertDialog alertDialog; // 提示框
	private List<UserCategory> mListSelectCategory; // 用户选择的知识目录
	private ArrayList<UserCategory> mListCategory; // 目录列表，将原来的多层级目录结构转换为单层级的结构目录
	private CategoryAdapter mAdapter; // 列表适配器
	private String mKeyword = ""; // 搜索关键字
	private String mFromActivity; // 页面来源
	private int maxCategory; //点击条目数
	private UserCategory Clickcategory = new UserCategory();//目录点击时拿到的目录对象
	private ArrayList<UserCategory> clicklistUserCategory = new ArrayList<UserCategory>();
	
	/**
	 * 公共组件 开发库 将目录修改成能支持多
	 */
	private ModuleType categoryType = ModuleType.KNOWLEDGE;
	private boolean IsChangeCategory = false; //目录列表是否发生改变
	/**
	 * <p>长按目录时的三种状态</p>
	 *CREATE<创建目录>,EDIT<编辑修改目录名称>,DELETE<删除该目录>
	 */
	public static enum LongClickCategoryType {
		CREATE,
		EDIT,
		DELETE;
	}
	public LongClickCategoryType clickCategoryType = LongClickCategoryType.CREATE;
	private UserCategory itemLongClickcategory;//长按事件的UserCategory对象
	private boolean IsClickcategory;
	private boolean isCategorySelect = true;// 目录是否支持多选 默认支持
	ArrayList<UserCategory> linearListCategory1 = new ArrayList<UserCategory>();
//	private Intent intentNoGroup = new Intent();

	private JTPage jtpage;

	private ArrayList<NeedItemData> needItemList = new ArrayList<NeedItemData>();

	private CusAndOrg_Page cusandorg_page;

	private List<PageItem> pageItemList;

	private ArrayList<Contacts> lists = new ArrayList<Contacts>();

	private PersonSimpleList personSimpleList;//服务器拿到的人脉列表数据对象

//	private boolean editMode = false;

//	private LinearLayout editRl; // 编辑模式底部栏

	private ArrayList<String> ClickcategoryName = new ArrayList<String>();
	
	private ArrayList<UserCategory> clickedlistUserCategory = new  ArrayList<UserCategory>();
	private ArrayList<UserCategory> mClickListCategory = new  ArrayList<UserCategory>();
	ArrayList<UserCategory> newClicklistUserCategory = new ArrayList<UserCategory>();
	/**
	 * 存放拿到的知识条目集合
	 */
	ArrayList<KnowledgeMini2> mKnowledgeList = new ArrayList<KnowledgeMini2>();
	/**
	 * 存放拿到的人脉条目集合
	 */
	ArrayList<Connections> mConnections = new ArrayList<Connections>();
	/**
	 * 存放拿到的需求条目集合
	 */
	ArrayList<NeedItemData> mItemDatas = new ArrayList<NeedItemData>();
	/**
	 * 存放拿到的组织条目集合
	 */
	ArrayList<Contacts> mContasts = new ArrayList<Contacts>();
	
	public enum Operate{
		BACK,
		TAB_CLICK,
		CLICK;
	}
	public Operate operate = Operate.CLICK;
	private EventBus controlBus;
	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar(),"目录" ,false,null,false, true);
//		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kno_act_category);
		controlBus = new EventBus();
		controlBus.register(CategoryActivity.this);
		initVars();
		initControls();
		doUpdate();

	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		if(isCategorySelect){
			MenuItem item = menu.add(0, 101, 0, "完成");
			item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) { // 取消选择
			finish();
		} 
//		else if(item.getItemId() == 102){//进入编辑模式
//			if (editMode ) {
//				mAdapter.setEditMode(false);
//				item.setTitle("编辑");
//				editMode = false;
//				mAdapter.notifyDataSetChanged();
//				editRl.setVisibility(View.GONE);
//			}else{
//				mAdapter.setEditMode(true);
//				item.setTitle("取消");
//				editMode = true;
//				mAdapter.notifyDataSetChanged();
//				editRl.setVisibility(View.VISIBLE);
//			}
			
//		}else if (item.getItemId() == 101) { // 完成选择
//			if (infoIntegrityCheck(true)) {
//				// 选择的目录结构
//				ArrayList<UserCategory> listCategory = new ArrayList<UserCategory>();
//				for (int i = 0; i < mListCategory.size(); i++) {
//					if (mListCategory.get(i).isSelected()) {
//						UserCategory category = mListCategory.get(i)
//								.lightClone(); // 浅拷贝，忽略listUserCategory
//						for (int j = i - 1; j >= 0; j--) {
//							// 停止遍历
//							if (j < 0 || category.getParentId() <= 0) {
//								break;
//							}
//							// 查找父目录
//							if (category.getParentId() == mListCategory.get(j)
//									.getId()) {
//								UserCategory tempCategory = category
//										.deepClone();
//								category = mListCategory.get(j).lightClone();
//								category.getListUserCategory().clear(); // 清空缓存数据
//								category.getListUserCategory()
//										.add(tempCategory);
//							}
//						}
//						listCategory.add(category);
//					}
//				}
//				
//				intentNoGroup.putExtra(EConsts.Key.KNOWLEDGE_CATEGORY_LIST, listCategory);
//				setResult(Activity.RESULT_OK, intentNoGroup);
//				finish();
//			}
//		}
		return true;
	}
	/*private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 编辑选中知识标签
			 if (tagIv == v) {
			}
			 //移动
			 else if (moveIv == v) {
			}
			// 分享选中知识
			else if (shareIv == v) {

			}
			// 删除选中知识
			else if (deleteIv == v) {
			}
		}
	};*/

	private boolean IsLoad;

	private int index = 0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			if (editMode) {
//				mAdapter.setEditMode(false);
////				edit.setTitle("编辑");
//				editMode = false;
//				mAdapter.notifyDataSetChanged();
//				editRl.setVisibility(View.GONE);
//			}  
		}
		return super.onKeyDown(keyCode, event);
	}

//	private ImageView shareIv;
//
//	private ImageView deleteIv;
//
//	private ImageView tagIv;
//
//	private ImageView moveIv;

//	private MenuItem edit;
/**
 * 目录列表标题控件
 */
	private TextView tabTv;
/**
 * 横向目录名称展示控件
 */
	private HorizontalListView tabLv;
/**
 * 目录列表标题
 */
	private String mActivityName;
/**
 * 横向目录名称适配器
 */
	private TabAdapter tabAdapter;

/**
 * 当前页面目录个数
 */
	private TextView categorySumTv;
/**
 * 当前目录所拥有的四大组件的条目数
 */
	private TextView contentSumTv;

	private ArrayList<Connections> toConnection;

	private LinearLayout rootLl;

	private int colorState;
	@SuppressWarnings("unchecked")
	@SuppressLint("UseSparseArrays")
	private void initVars() {
		//获取背景默认颜色
		colorState = CategoryActivity.this.getResources().getColor(R.color.project_bg);
		mKeyword = "";
		mListCategory = new ArrayList<UserCategory>();
		// 页面来源
		mFromActivity = getIntent().getStringExtra(EConsts.Key.FROM_ACTIVITY);
		// 这里传入已选择的目录
		mListSelectCategory = (List<UserCategory>) getIntent()
				.getSerializableExtra(EConsts.Key.KNOWLEDGE_CATEGORY_LIST);
		if (mListSelectCategory == null) {
			mListSelectCategory = new ArrayList<UserCategory>();
		}
		categoryType = (ModuleType) getIntent().getSerializableExtra(ENavConsts.Category_ENUM_TYPE);
		// 测试用语句
//		categoryType = CategoryType.Organization;
				
				
		isCategorySelect = getIntent().getBooleanExtra(
				ENavConsts.Category_SELECT_ACTION, true);
		if (categoryType == null) {
			categoryType = ModuleType.KNOWLEDGE;
		}
		// 列表适配器
		mAdapter = new CategoryAdapter(this,ModuleType.CATEGORY,mKeyword,isCategorySelect);
		// 使用测试数据初始化列表
		// initVarsWithDemoData();
	}


	// 信息完整性检查
//	private boolean infoIntegrityCheck(boolean showTip) {
//		boolean integral = false;
//		UserCategory categoryTmp = new UserCategory();
//		for (UserCategory category : mListCategory) {
//			if ("未分组".equals(category.getCategoryname())) {
//				categoryTmp = category;
//			}
//			if (category.isSelected()) {
//				intentNoGroup.putExtra(EConsts.Key.KNOWLEDGE_CATEGORY_GROUP, true);
//				integral = true;
//			}
//		}
//		if (!integral) {
////			showToast("请至少选择一个目录");
//			categoryTmp.setSelected(true);
//			intentNoGroup.putExtra(EConsts.Key.KNOWLEDGE_CATEGORY_GROUP, false);
//			integral = true;
//		}
//		return integral;
//	}

	// 递归得到目录id
	private long recursiveGetCategoryId(UserCategory category) {
		long categoryId = category.getId();
		if (category.getListUserCategory().size() > 0) {
			categoryId = recursiveGetCategoryId(category.getListUserCategory()
					.get(0));
		}
		return categoryId;
	}

	// 将递归结构转为线性结构
	private List<UserCategory> convertRecursive2Linear(
			UserCategory recCategory, int level) {
		List<UserCategory> listLin = new ArrayList<UserCategory>();
		recCategory.setLevel(level);
		listLin.add(recCategory);
		for (UserCategory category : recCategory.getListUserCategory()) {
			int tempLevel = level;
			listLin.addAll(convertRecursive2Linear(category, ++tempLevel));
		}
		return listLin;
	}
	class TabAdapter extends BaseAdapter{
		
		@Override
		public int getCount() {
			return clickedlistUserCategory.size();
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
			convertView = View.inflate(CategoryActivity.this, R.layout.category_tabview, null);
			TextView tabLv_Tv = (TextView) convertView.findViewById(R.id.tabLv_Tv);
			tabLv_Tv.setText(clickedlistUserCategory.get(position).getCategoryname());
			return convertView;
		}
		public void notifyChanged(){
			this.notifyDataSetChanged();
			tabLv.setSelection(tabLv.getLastVisiblePosition());
		}
	}	
	@SuppressWarnings("deprecation")
	private void initControls() {
		keywordEt = (EditText) findViewById(R.id.keywordEt);
//		editRl =  (LinearLayout) findViewById(R.id.editRl);
		rootLl  = (LinearLayout) findViewById(R.id.rootLl);
//		shareIv =  (ImageView) findViewById(R.id.shareIv);
//		deleteIv =  (ImageView) findViewById(R.id.deleteIv);
//		tagIv =  (ImageView) findViewById(R.id.tagIv);
//		moveIv =  (ImageView) findViewById(R.id.moveIv);
		tabTv = (TextView) findViewById(R.id.tabTv);
		categorySumTv = (TextView) findViewById(R.id.categorySumTv);
		contentSumTv = (TextView) findViewById(R.id.contentSumTv);
		tabLv = (HorizontalListView) findViewById(R.id.tabLv);
		tabAdapter = new TabAdapter();
		tabLv.setAdapter(tabAdapter);

		/*if(categoryType == CategoryType.demand){
			contentSumTv.setText("0个需求");
		}else if(categoryType == CategoryType.Organization){组织/客户
			contentSumTv.setText("0个组织/客户");
		}else if(categoryType == CategoryType.People){好友人脉
			contentSumTv.setText("0个好友人脉");
		}else {
			contentSumTv.setText("0篇知识");
		}*/
		
		// 页面来源
		mActivityName = getIntent().getStringExtra(EConsts.Key.ACTIVITY_NAME);
		if (mActivityName!=null) {
			tabTv.setText(mActivityName);
		}
//		moveIv.setOnClickListener(mOnClickListener);
//		tagIv.setOnClickListener(mOnClickListener);
//		deleteIv.setOnClickListener(mOnClickListener);
//		shareIv.setOnClickListener(mOnClickListener);
		tabLv.setOnItemClickListener(new NoDoubleItemClickListener() {
			UserCategory category  = null;
			@Override
			void onNoDoubleItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				keywordEt.setText("");
				if (!clickedlistUserCategory.isEmpty()) {
					category = clickedlistUserCategory.get(position);
				if (category!=null) {
					final Long categoryId = category.getId();
					if (categoryId!=0) {
//						if (linearListCategory1!=null&&!linearListCategory1.isEmpty()) {
							new AsyncTask<Void, Void, Void>(){
								private ArrayList<UserCategory> deleteUserCategoryList;
								@Override
								protected Void doInBackground(Void... params) {
//									for (int i = 0; i < linearListCategory1.size(); i++) {
//										UserCategory userCategory = linearListCategory1.get(i);
//										if (categoryId.equals(userCategory.getId())) {
											int userCategoryindexOf = clickedlistUserCategory.indexOf(category);
											deleteUserCategoryList = new ArrayList<UserCategory>();
											for (int j = userCategoryindexOf+1; j < clickedlistUserCategory.size(); j++) {
												deleteUserCategoryList.add(clickedlistUserCategory.get(j));
											}
											
											mClickListCategory = (ArrayList<UserCategory>) category.getListUserCategory();
											mAdapter.setmListCategory(mClickListCategory,1);
//										}
//									}
									return null;
								}
								protected void onPostExecute(Void result) {
									clickedlistUserCategory.removeAll(deleteUserCategoryList);
									categorySumTv.setText(mClickListCategory.size()+"个目录");
									tabAdapter.notifyChanged();
								};
							}.execute();
							
//						}
						
							if (categoryType == ModuleType.KNOWLEDGE) {//请求知识数据
								IsLoad= true ;
								KnowledgeReqUtil.doGetKnowledgeByUserCategoryAndKeyword(CategoryActivity.this, CategoryActivity.this, App.getUserID(), categoryId, "", index , 20, null);
							}else if (categoryType == ModuleType.PEOPLE) {//请求人脉数据
								IsLoad= true ;
									requestJson(categoryId,0);
								
							}else if (categoryType == ModuleType.ORG) {//请求组织数据
								IsLoad= true ;
								OrganizationReqUtil.doGetCusAndOrg(CategoryActivity.this, CategoryActivity.this, categoryId+"", index+"",-1+"" , "20","", "",
										null);
							}else if (categoryType == ModuleType.DEMAND) {//请求需求数据
								IsLoad= true ;
								DemandReqUtil.getDemandCategory(CategoryActivity.this,
										CategoryActivity.this, 20, index, categoryId,
										null);
							}
						
						
							operate = Operate.TAB_CLICK ;
					}
				}
				}
				super.onNoDoubleItemClick(parent, view, position, id);
			}
		});
		tabTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				keywordEt.setText("");
				mAdapter.setmListCategory(mCategoryListData,1);
				mAdapter.getConnections().clear();
				mAdapter.getContacts().clear();
				mAdapter.getItemDatas().clear();
				mAdapter.getmListKnowledgeMini2s().clear();
				Clickcategory = null; 
				clickedlistUserCategory.clear();
				tabAdapter.notifyChanged();
				mAdapter.notifyDataSetChanged();
				categorySumTv.setText(mCategoryListData.size()+"个目录");
				IsChangeCategory = false;
				IsClickcategory = false;
				if (mAdapter.isEmpty()) {
					rootLl.setBackgroundResource(R.drawable.empty);
				}else{
					rootLl.setBackgroundColor(colorState);
				}
			}
		});
		keywordEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void afterTextChanged(Editable s) { // 关键字改变后更新列表
				mKeyword = s.toString();
				// 设置显示、隐藏标记
				if (mKeyword.length() == 0) {
					for (UserCategory category : mListCategory) {
						category.setVisiable(true); // 全部显示
						category.setFolded(false); // 全部展开
					}
					doUpdate();
				} else {
					searchCatetoryByKeyword(mKeyword);
				}
				mAdapter.setmKeyword(mKeyword);
				// 更新数据
				mAdapter.notifyDataSetChanged();
				IsChangeCategory = false; 
			}
		});
		// 添加新目录
		addIv = (ImageView) findViewById(R.id.addIv);
		addIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.show(OperType.Create, Clickcategory);
			}
		});
		// 目录列表
		categoryLv = (XListView) findViewById(R.id.categoryLv);
		categoryLv.setAdapter(mAdapter);
		categoryLv.setOnItemLongClickListener(new OnItemLongClickListener() { // 长按事件

					

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						int itemViewType = mAdapter.getItemViewType(position-1);
						if (itemViewType == 0) {
							itemLongClickcategory = (UserCategory) mAdapter.getItem(position - 1);
							if (itemLongClickcategory.getCategoryname().equals("未分组")&& itemLongClickcategory.getLevel() == 0) {
								// showToast("不能操作此目录");
							} else {
								operDialog.setAttachViewAndCategory(view,itemLongClickcategory,itemLongClickcategory.getLevel());
								operDialog.show();
							}
						}
						return true;
					}
				});
		categoryLv.setOnItemClickListener(new NoDoubleItemClickListener() { // 点击事件

					private KnowledgeMini2 knowledgeMini2;
					private Connections connections;
					private Contacts contacts;
					private NeedItemData needItemData;
				

					@Override
					public void onNoDoubleItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						if (IsLoad) {
							return;
						}
						
						if (position!=0) {
						int itemViewType = mAdapter.getItemViewType(position-1);
						if (itemViewType == 1) {
							knowledgeMini2 = (KnowledgeMini2) mAdapter.getItem(position - 1);
							ENavigate.startKnowledgeOfDetailActivity(CategoryActivity.this, knowledgeMini2.id, knowledgeMini2.type);
						}else if (itemViewType == 2) {
							connections = (Connections) mAdapter.getItem(position - 1);
							ENavigate.startContactsDetailsActivity(CategoryActivity.this, 2, Long.valueOf(connections.jtContactMini.id), 0);
						}else if (itemViewType == 3) {
							contacts = (Contacts) mAdapter.getItem(position - 1);
							long customerId = contacts.getCustomerId();
							Intent intent = new Intent(CategoryActivity.this, ClientDetailsActivity.class);
							intent.putExtra("customerId", customerId);
							intent.putExtra("label", 2);
							startActivityForResult(intent, 10003);
						}else if (itemViewType == 4) {
							needItemData = (NeedItemData) mAdapter.getItem(position - 1);
							ENavigate.startNeedDetailsActivity(CategoryActivity.this,
									needItemData.demandId, 1);
						}else {
							showLoadingDialog();
							Clickcategory = (UserCategory) mAdapter.getItem(position - 1);
							if (isCategorySelect) {
								if (isCategoryEmpty||Clickcategory.getListUserCategory().isEmpty()) {
									dismissLoadingDialog();
									return;
								}
							}
							
							
							if (Clickcategory==null) {
								return;
							}
							if (!IsChangeCategory) {
								IsChangeCategory = true;
								
							if (!TextUtils.isEmpty(Clickcategory.getCategoryname())) {
								ClickcategoryName.add(Clickcategory.getCategoryname());
							}
							IsClickcategory = true;
							clicklistUserCategory = (ArrayList<UserCategory>) Clickcategory.getListUserCategory();
							operate = Operate.CLICK ;
							if (categoryType == ModuleType.KNOWLEDGE) {//请求知识数据
								IsLoad= true ;
								KnowledgeReqUtil.doGetKnowledgeByUserCategoryAndKeyword(CategoryActivity.this, CategoryActivity.this, App.getUserID(), Clickcategory.id, "", index, 20, null);
							}else if (categoryType == ModuleType.PEOPLE) {//请求人脉数据
								IsLoad= true ;
								requestJson(Clickcategory.getId(),0);
							}else if (categoryType == ModuleType.ORG) {//请求组织数据
								IsLoad= true ;
								OrganizationReqUtil.doGetCusAndOrg(CategoryActivity.this, CategoryActivity.this, Clickcategory.id+"", index+"",-1+"" , "20","", "",
										null);
							}else if (categoryType == ModuleType.DEMAND) {//请求需求数据
								IsLoad= true ;
								DemandReqUtil.getDemandCategory(CategoryActivity.this,
										CategoryActivity.this, 20, index, Clickcategory.getId(),
										null);
							}
							
							clickedlistUserCategory.add(Clickcategory);
							
							
						}
						}
						
//						ENavigate.startPeopleCategoryLabelActivity(GlobalKnowledgeCategoryActivity.this, 0, 0);						
						if (isCategorySelect) { // 多选
							if (itemViewType ==5) {
								
								CategoryViewHolder holder = (CategoryViewHolder) view.getTag();
								if (holder != null) {
									holder.selectCb.setChecked(!holder.selectCb.isChecked());
									// 设置选择状态
									Clickcategory.setSelected(holder.selectCb.isChecked());
									Log.d(TAG, Clickcategory.getCategoryname()+ "状态手动更改为"+ holder.selectCb.isChecked());
								}
							}
							
							} 
							else {
								// 单选
/*<<<<<<< HEAD
								if (mFromActivity.equalsIgnoreCase(MeNeedActivity.class.getSimpleName())) { // 如果是我的需求列表中的
									// 单选效果 就不要关闭当前界面
									startDemandCategory(category.lightClone(),CategoryType.demand);
								} 
								else if (mFromActivity.equalsIgnoreCase(MyFriendAllActivity.class.getSimpleName())) { // 如果是我的人脉好友
									ENavigate.startPeopleCategoryLabelActivity(GlobalKnowledgeCategoryActivity.this, category.getId(),category.getCategoryname(), null , null);
								}
								else if (mFromActivity.equalsIgnoreCase(OrganizationAndCustomerActivity.class.getSimpleName())) { // 如果是我的组织列表中的
									// 单选效果 就不要关闭当前界面
									startOrganizationCategory(category.lightClone(),CategoryType.Organization);
								}else if(mFromActivity.equalsIgnoreCase(MyKnowledgeActivity.class.getSimpleName())) {
									ENavigate.startKnowledgeCategoryLabelActivity(GlobalKnowledgeCategoryActivity.this, category.getId(),category.getCategoryname(), null , null);

								}
=======*/
//								if (mFromActivity.equalsIgnoreCase(MeNeedActivity.class.getSimpleName())) { // 如果是我的需求列表中的
//									// 单选效果 就不要关闭当前界面
//									startDemandCategory(category.lightClone(),CategoryType.demand);
//								} 
//								else if (mFromActivity.equalsIgnoreCase(MyFriendAllActivity.class.getSimpleName())) { // 如果是我的人脉好友
//									// 单选效果不关闭当前界面
//									// startDemandCategory(mAdapter.getItem(position).lightClone());
//									// 这里要跳到 新关系列表筛选页
////									ENavigate.startPeopleCategoryLabelActivity(GlobalKnowledgeCategoryActivity.this, mAdapter.getItem(position).getId(), null);
//									ENavigate.startPeopleCategoryLabelActivity(GlobalKnowledgeCategoryActivity.this, category.getId(),category.getCategoryname(), null , null);
//								}
//								else if (mFromActivity.equalsIgnoreCase(OrganizationAndCustomerActivity.class.getSimpleName())) { // 如果是我的组织列表中的
//									// 单选效果 就不要关闭当前界面
//									startOrganizationCategory(category.lightClone(),CategoryType.Organization);
//								}else if(mFromActivity.equalsIgnoreCase(MyKnowledgeActivity.class.getSimpleName())) {
//									ENavigate.startKnowledgeCategoryLabelActivity(GlobalKnowledgeCategoryActivity.this, category.getId(),category.getCategoryname(), null , null);
//
//								}
								
						}
					}
						
					
				}


				});
		operDialog = new KnoCategoryOperateDialog(this,maxCategory);
		operDialog.setOnSelectListener(new OnSelectListener() {
			@Override
			public void onSelect(OperType operType, UserCategory category) {
				alertDialog.show(operType, category);
			}
		});
		// 弹出框操作
		alertDialog = new KnoCategoryAlertDialog(this);
		if(categoryType==ModuleType.DEMAND){ //如果是需求
			alertDialog.deleteMessage="目录删除后，需求自动放到未分组目录哦";
		}
		else if(categoryType==ModuleType.ORG){ //如果是组织
			alertDialog.deleteMessage="目录删除后，组织或客户自动放到未分组目录";
		}else if (categoryType==ModuleType.PEOPLE) {
			alertDialog.deleteMessage="目录删除后，人脉自动放到未分组目录";
		}
		alertDialog.setOnDialogClickListener(new OnDialogClickListener() {
			@Override
			public void onClick(OperType operType, int which, long categoryId,
					String categoryName, Integer level) {
				if (which == 0) { // 取消
					return;
				}
				showLoadingDialog();
				switch (operType) {
				case Create: // 创建
					if (!TextUtils.isEmpty(categoryName.trim())) {
						if (checkCategoryName(categoryName, categoryId, level,false)) {
							createCategory(categoryType, categoryId, categoryName);
						} else {
							dismissLoadingDialog();
							showToast("该目录已存在");
						}
					}else{
						dismissLoadingDialog();
						showToast("目录名称不能为空");

					}
					break;
				case Modify: // 修改
					if (!TextUtils.isEmpty(categoryName.trim())) {
						if (checkCategoryName(categoryName, categoryId, level,true)) {
							editCategory(categoryType, categoryId, categoryName);
						} else {
							dismissLoadingDialog();
							showToast("该目录已存在");
						}
					}else{
						dismissLoadingDialog();
						showToast("目录名称不能为空");

					}
					
					break;
				case Delete: // 删除
					deleteCategory(categoryType, categoryId);
					break;
				}
			}
		});
		
		categoryLv.showFooterView(false);
		// 设置xlistview可以加载、刷新
		categoryLv.setPullLoadEnable(false);
		categoryLv.setPullRefreshEnable(true);

		categoryLv.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				/* 访问服务器 */
				doUpdate();
			}

			@Override
			public void onLoadMore() {
				index ++;
				getContentData();
			}
		});
		
		categoryLv.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				return false;
			}
		});

		categoryLv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});
		
	}
	
	
	public void getContentData(){
		if (categoryType == ModuleType.KNOWLEDGE) {//请求知识数据
			IsLoad= true ;
			KnowledgeReqUtil.doGetKnowledgeByUserCategoryAndKeyword(CategoryActivity.this, CategoryActivity.this, App.getUserID(), Clickcategory.id, "", index, 20, null);
		}else if (categoryType == ModuleType.PEOPLE) {//请求人脉数据
			IsLoad= true ;
			requestJson(Clickcategory.getId(),0);
		}else if (categoryType == ModuleType.ORG) {//请求组织数据
			IsLoad= true ;
			OrganizationReqUtil.doGetCusAndOrg(CategoryActivity.this, CategoryActivity.this, Clickcategory.id+"", index+"",-1+"" , "20","", "",
					null);
		}else if (categoryType == ModuleType.DEMAND) {//请求需求数据
			IsLoad= true ;
			DemandReqUtil.getDemandCategory(CategoryActivity.this,
					CategoryActivity.this, 20, index, Clickcategory.getId(),
					null);
		}
	}
	 public abstract  class NoDoubleItemClickListener implements OnItemClickListener {

         public static final int MIN_CLICK_DELAY_TIME = 200;
         public   long lastClickTime = 0;

         @Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
        	 long currentTime = Calendar.getInstance().getTimeInMillis();
             if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                 lastClickTime = currentTime;
                 onNoDoubleItemClick(parent,view,position,id);
             } 
         }
         void onNoDoubleItemClick(AdapterView<?> parent, View view,
					int position, long id) {}
     }
	private void requestJson(long cid, long tid) {
		MyContacts myContacts = new MyContacts();
		myContacts.cid = cid;
		myContacts.tid = tid;
		PeopleReqUtil.doRequestWebAPI(this, this, myContacts, null,
				PeopleRequestType.PEOPLE_REQ_PEOPLELIST);
	}
	/**
	 * 启动需求详情界面
	 * 
	 * @param category
	 */
	private void startDemandCategory(UserCategory category,ModuleType categoryType) {

		if (category != null) {
			ENavigate.startDemandCategoryActivity(
					CategoryActivity.this, category,categoryType);
		}
	}
	private void startOrganizationCategory(
			UserCategory category, ModuleType organization) {
		if (category != null) {
			ENavigate.startOrganizationCategoryActivity(
					CategoryActivity.this, category,organization);
		}
	}
	/**
	 * 检测目录名不能重复
	 * 
	 * 检测同名的规则是 只判断同一个父目录id下的目录名不重复 
	 * 
	 * @param categoryName 要创建的目录名
	 * @param parentCategoryId	要创建目录的父目录id
	 * @param parentLevel	要创建目录的父目录Level(这里没有用到,之前想用level判断 后来发现 父id判断更简单, 没有删这个参数是为了以后扩展)
	 * @param isEdit   true:修改 false:创建
	 * @return 如果有同名 返回 false   没有返true
	 * @author gushi 最后修改
	 */
	private boolean checkCategoryName(String categoryName,long parentCategoryId, Integer parentLevel,boolean isEdit) {
		int length = mListCategory.size();
		//  *****  原李昂代码	start  *****
//		for (int i = 0; i < (length + 1) / 2; i++) {
//			if ((mListCategory.get(i).getLevel() == 0 && categoryName.equals(mListCategory.get(i).getCategoryname().trim())) 
//					|| (mListCategory.get(length - 1 - i).getLevel() == 0 && categoryName.equals(mListCategory.get(length - 1 - i).getCategoryname().trim()))) {
//				return false;
//			}
//		}
		//  *****  原李昂代码	end  *****
		
		for (int i = 0; i < length ; i++) {
			UserCategory  currentUserCategory = mListCategory.get(i);
			String currentName = currentUserCategory.getCategoryname();
			long currentParentId = currentUserCategory.getParentId();
			
			// 如果当前目录父id 等于 传进来的 parentCategoryId  是 同一级目录 然后 判断名字是否相同就可以了
			if(currentParentId == parentCategoryId && categoryName.equals(currentName.trim())){
				return false;
			}else if(isEdit&& categoryName.equals(currentName.trim()))
				return false;
		}
		
		return true;
	}

	private void doUpdate() {
		doGetCategory(categoryType);

	}

	/**
	 * 获取目录列表
	 */
	private void doGetCategory(ModuleType categoryType) {
		switch (categoryType) {
		case DEMAND:// 需求
			DemandReqUtil
					.getTreeCategory(CategoryActivity.this,
							CategoryActivity.this,
							App.getUserID(), null);
			break;
		case KNOWLEDGE:
			// 知识
			KnowledgeReqUtil
					.doGetKnoCategory(CategoryActivity.this,
							CategoryActivity.this,
							App.getUserID(), null);
			break;
			
		case ORG:	// 组织
			OrganizationReqUtil.doCustomerGroupListQuery(CategoryActivity.this, CategoryActivity.this, null);
			break;
			
		case PEOPLE:	// 人脉
			PeopleReqUtil.doRequestWebAPI(CategoryActivity.this, CategoryActivity.this, new Object() , null, PeopleRequestType.PEOPLE_REQ_FINDCATEGORY);
			break;
			
		default:
			break;
		}
	}

	/**
	 * 创建目录
	 * 
	 * @param categoryType
	 */
	private void createCategory(ModuleType categoryType, final long parentId,
			String categoryName) {
		clickCategoryType = LongClickCategoryType.CREATE;
		switch (categoryType) {
		case DEMAND:// 需求
			DemandReqUtil.createCategory(CategoryActivity.this,
					CategoryActivity.this, null, categoryName,
					parentId, 0);
			break;
		case KNOWLEDGE:
			// 知识
			KnowledgeReqUtil.doAddKnoCategory(
					CategoryActivity.this,
					CategoryActivity.this, parentId,
					categoryName, null);
			break;
		case ORG:	// 组织
			OrganizationReqUtil.doCustomerAddGroup(CategoryActivity.this, CategoryActivity.this, categoryName, parentId+"", null);
			
			
			break;
			
		case PEOPLE:	//	人脉 
			/**
			 "	id":修改时有值,
        		"pid":新增非顶级目录时有值,
        		"categoryname":目录名称
			 */
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("pid", parentId);
			map.put("name", categoryName);
			map.put("id", 0);
			PeopleReqUtil.doRequestWebAPI(CategoryActivity.this, CategoryActivity.this, map , null, PeopleRequestType.PEOPLE_REQ_SAVEORUPDATECATEGORY);
			
			break;
		default:
			break;
		}
	}

	/**
	 * 删除目录
	 * 
	 * @param categoryType
	 */
	private void deleteCategory(ModuleType categoryType, final long categoryId) {
		clickCategoryType = LongClickCategoryType.DELETE;
		switch (categoryType) {
		case DEMAND:// 需求
			DemandReqUtil.deletCategory(CategoryActivity.this,
					CategoryActivity.this, categoryId, null);
			
			break;
		case KNOWLEDGE:
			// 知识
			KnowledgeReqUtil.doDelKnoCategory(
					CategoryActivity.this,
					CategoryActivity.this, categoryId, null);
			break;
			
		case ORG:	// 组织
			OrganizationReqUtil.doCustomerDeleteGroup(CategoryActivity.this, CategoryActivity.this, categoryId+"", null);
			break;
			
		case PEOPLE:	//	人脉 
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", categoryId);

			PeopleReqUtil.doRequestWebAPI(CategoryActivity.this, CategoryActivity.this, map , null, PeopleRequestType.PEOPLE_REQ_REMOVECATEGORY);
			
			break;
			
		default:
			break;
		}
		
	}

	/**
	 * 修改目录名称
	 * 
	 * @param categoryType
	 */
	private void editCategory(ModuleType categoryType, final long categoryId,
			final String categoryName) {
		clickCategoryType = LongClickCategoryType.EDIT;
		switch (categoryType) {
		case DEMAND:// 需求
			DemandReqUtil.createCategory(CategoryActivity.this,
					CategoryActivity.this, null, categoryName,
					0, categoryId);
			
			break;
			
		case KNOWLEDGE:
			// 知识
			KnowledgeReqUtil.doEditKnoCategory(
					CategoryActivity.this,
					CategoryActivity.this, categoryId,
					categoryName, null);
			break;
			
		case ORG:  // 组织
			OrganizationReqUtil.doCustomerUpdaetGroup(CategoryActivity.this, CategoryActivity.this, categoryId+"", categoryName, null);
			break;
		
		case PEOPLE:	//	人脉 
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", categoryId);
			map.put("name", categoryName);

			PeopleReqUtil.doRequestWebAPI(CategoryActivity.this, CategoryActivity.this, map , null, PeopleRequestType.PEOPLE_REQ_SAVEORUPDATECATEGORY);
			
			break;
		
		default:
			break;
		}
//		new AsyncTask<Void, Void, Void>(){
//			@Override
//			protected Void doInBackground(Void... params) {
//				for (int i = 0; i < clickedlistUserCategory.get(clickedlistUserCategory.size()-1).getListUserCategory().size(); i++) {
//					UserCategory userCategory = clickedlistUserCategory.get(clickedlistUserCategory.size()-1).getListUserCategory().get(i);
//					if (categoryId == userCategory.getId()) {
//						userCategory.setCategoryname(categoryName);
//					}
//				}
//				return null;
//			}
//			protected void onPostExecute(Void result) {
//			};
//		}.execute();
		
	}

	// 根据关键字搜索目录
	private void searchCatetoryByKeyword(String keyword) {

		// 将全部项目置为隐藏和收起
		for (UserCategory category : mListCategory) {
			category.setVisiable(false);
			category.setFolded(true);
		}

		// 将包含关键字的目录及其父目录的显示Map表都置为显示，其它置为隐藏
		for (int i = 0; i < mListCategory.size(); i++) { // 遍历用户目录
			if (mListCategory.get(i).getCategoryname().contains(keyword)) {
				// 设置状态
				long categoryId = mListCategory.get(i).getId();
				mListCategory.get(i).setVisiable(true);
				// 如果不是一级目录，则继续遍历其父目录
				if (mListCategory.get(i).getLevel() > 0) {
					for (int j = i - 1; j >= 0; j--) {
						if (mListCategory.get(j).getLevel() >= 0) {
							if (mListCategory.get(j).getLevel() < mListCategory
									.get(i).getLevel()) {
								for (UserCategory category : mListCategory.get(
										j).getListUserCategory()) {
									if (category.getId() == categoryId) {
										categoryId = mListCategory.get(j)
												.getId();
										mListCategory.get(j).setVisiable(true);
										mListCategory.get(j).setFolded(false);
										break;
									}
								}
							}
						} else { // 如果遍历到根目录
							break;
						}
					}
				}
			}
		}
	}

	// 将请求回来的数据填充到集合中
		private void fillList() {
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
				lists .add(contacts);
			}
		}
		private boolean isContentEmpty;
		private boolean isCategoryEmpty;

		private ArrayList<UserCategory> mCategoryListData;  //请求回来的目录数据集合
	@SuppressWarnings("unchecked")
	@Override
	public void bindData(int tag, Object object) {
		//XlistView结束刷新时状态复位
		categoryLv.stopLoadMore();
		categoryLv.stopRefresh();
		dismissLoadingDialog();
		if (object!=null) {
		if (tag == EAPIConsts.demandReqType.demand_rCategoryQuery) {
			needItemList.clear();
			// 查询获取的列表
			NeedItemListItem item = (NeedItemListItem) object;
			jtpage = item.getJtPage();
			if (jtpage == null)
				return;
			// TODO 需要添加代码处
			if ((jtpage != null) && (jtpage.getLists() != null)) {
				new AsyncTask<Void, Void, ArrayList<NeedItemData>>(){

					
					@Override
					protected ArrayList<NeedItemData> doInBackground(Void... params) {
						for (int i = 0; i < jtpage.getLists().size(); i++) {
							needItemList.add((NeedItemData) jtpage.getLists().get(i));
						}
						return needItemList;
					}
					@Override
					protected void onPostExecute(ArrayList<NeedItemData> result) {
						
						mAdapter.setCategoryType(categoryType);
						mAdapter.setItemDatas(result);
						if(result.size() > 0){
							contentSumTv.setText(result.size()+"个需求");
						}else {
							contentSumTv.setText("");
						}
						if(operate == Operate.CLICK){
							categorySumTv.setText(clicklistUserCategory.size()+"个目录");
							mAdapter.setmListCategory(clicklistUserCategory,1);
						}
						
						mAdapter.notifyDataSetChanged();
						tabAdapter.notifyChanged();
						IsChangeCategory = false;
						IsLoad= false;
						if (mAdapter.isEmpty()) {
							rootLl.setBackgroundResource(R.drawable.empty);
							isContentEmpty  = true;
						}else{
							rootLl.setBackgroundColor(colorState);
							isContentEmpty  = false; 
						}
						super.onPostExecute(result);
					}
				}.execute();
				dismissLoadingDialog();
				
			}
		}
		else if (tag == EAPIConsts.OrganizationReqType.ORAGANIZATION_REQ_GETCUSTOMANDORG){
			lists.clear();
			if (object == null) {
				return;
			}
			Map<String, Object> dataHm = (Map<String, Object>) object;
			cusandorg_page = (CusAndOrg_Page) dataHm.get("page");
			if (cusandorg_page != null && cusandorg_page.listResults != null
					) {
				pageItemList = cusandorg_page.listResults;
				new AsyncTask<Void, Void, Void>(){

					@Override
					protected Void doInBackground(Void... params) {
						fillList();
						Collections.sort(lists);
						return null;
					}
					@Override
					protected void onPostExecute(Void result) {
						mAdapter.setCategoryType(categoryType);
						mAdapter.setContacts(lists);
						if(lists.size() > 0){
							contentSumTv.setText(lists.size()+"个组织");
						}else{
							contentSumTv.setText("");
						}
						if(operate == Operate.CLICK){
							categorySumTv.setText(clicklistUserCategory.size()+"个目录");
							mAdapter.setmListCategory(clicklistUserCategory,1);
						}
						mAdapter.notifyDataSetChanged();
						tabAdapter.notifyChanged();
						IsChangeCategory = false;
						IsLoad= false;
						if (mAdapter.isEmpty()) {
							rootLl.setBackgroundResource(R.drawable.empty);
							isContentEmpty  = true;
						}else{
							rootLl.setBackgroundColor(colorState);
							isContentEmpty  = false;
						}
						super.onPostExecute(result);
					}
				}.execute();
				
				dismissLoadingDialog();
				
			}
		}
		else if (tag == EAPIConsts.PeopleRequestType.PEOPLE_REQ_PEOPLELIST){
			mConnections.clear();
		if (object == null) {
			return;
		}
		personSimpleList = (PersonSimpleList) object;
		if (personSimpleList != null && personSimpleList.list != null&&personSimpleList.success) {
			new AsyncTask<Void, Void, Void>(){

				private int colorState;
				@Override
				protected Void doInBackground(Void... params) {
					toConnection = PersonSimpleListToConnection(personSimpleList);
					return null;
				}
				@Override
				protected void onPostExecute(Void result) {
					mConnections.addAll(toConnection);
					mAdapter.setCategoryType(categoryType);
					mAdapter.setConnections(mConnections);
					if(mConnections.size() > 0 ){
						contentSumTv.setText(mConnections.size()+"个人脉");
					}else {
						contentSumTv.setText("");
					}
					if(operate == Operate.CLICK){
						categorySumTv.setText(clicklistUserCategory.size()+"个目录");
						mAdapter.setmListCategory(clicklistUserCategory,1);
					}
					mAdapter.notifyDataSetChanged();
					tabAdapter.notifyChanged();
					IsChangeCategory = false;
					IsLoad= false;
					if (mAdapter.isEmpty()) {
						rootLl.setBackgroundResource(R.drawable.empty);
						isContentEmpty  = true;
					}else{
						rootLl.setBackgroundColor(colorState);
						isContentEmpty  = true;
					}
					super.onPostExecute(result);
				}
			}.execute();
			
			
			dismissLoadingDialog();
			
		}
	}
		
		else if (KnoReqType.GetKnowledgeByTypeAndKeyword == tag || KnoReqType.GetKnowledgeByTagAndKeyword == tag || KnoReqType.GetKnowledgeByUserCategoryAndKeyword == tag) {
			Map<String, Object> hm = (Map<String, Object>) object;
			mKnowledgeList.clear();
			if (object != null) {
			ArrayList<KnowledgeMini2> newKnowledgeList = (ArrayList<KnowledgeMini2>) hm.get("listKnowledgeMini");
			
			if (newKnowledgeList != null) {
				mKnowledgeList.addAll(newKnowledgeList);
				mAdapter.setCategoryType(categoryType);
				mAdapter.setmListKnowledgeMini2s(mKnowledgeList);
				if(mKnowledgeList.size() > 0){
					contentSumTv.setText(mKnowledgeList.size()+"篇知识");
				}else{
					contentSumTv.setText("");
				}
				if(operate == Operate.CLICK){
					categorySumTv.setText(clicklistUserCategory.size()+"个目录");
					mAdapter.setmListCategory(clicklistUserCategory,1);
				}
				mAdapter.notifyDataSetChanged();
				tabAdapter.notifyChanged();
				IsChangeCategory = false;
				// reSetActionBarTitle();
				dismissLoadingDialog();
				IsLoad= false;
				if (mAdapter.isEmpty()) {
					rootLl.setBackgroundResource(R.drawable.empty);
					isContentEmpty  = true;
				}else{
					rootLl.setBackgroundColor(colorState);
					isContentEmpty  = true;
				}
			}
			}
		}else{
			
		
		HashMap<String, Object> dataMap = null;
		if (object != null) {
			dataMap = (HashMap<String, Object>) object;
		
		
		switch (tag) {
		
		case EAPIConsts.KnoReqType.GetUserCategory: // 获取用户目录
		case EAPIConsts.demandReqType.demand_categoryQueryTree:// 获取需求用户目录
		case EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_CUSTOMER_LIST_QUERY:// 客户分组列表查询
		case EAPIConsts.PeopleRequestType.PEOPLE_REQ_FINDCATEGORY:// 人脉目录列表查询
		case EAPIConsts.KnoReqType.AddUserCategory: // 添加用户目录
		case EAPIConsts.KnoReqType.EditUserCategory: // 编辑知识目录
		case EAPIConsts.KnoReqType.DelUserCategory: // 删除用户目录
		case EAPIConsts.demandReqType.demand_deleteCategory:// 删除用户目录
		case EAPIConsts.demandReqType.demand_createCategory:// 添加或修改用户目录
		case EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_ADD_GROUPING:// 组织 添加分组
		case EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_UPDAET_GROUPING:// 组织 添加分组
		case EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_DELETE_GROUPING:// 组织 删除分组
		case EAPIConsts.PeopleRequestType.PEOPLE_REQ_SAVEORUPDATECATEGORY:// 人脉 新增\修改目录 
		case EAPIConsts.PeopleRequestType.PEOPLE_REQ_REMOVECATEGORY:// 人脉 删除目录
			dismissLoadingDialog();
			if (dataMap != null) {
				if (dataMap.containsKey("success")) {
					boolean success = (Boolean) dataMap.get("success");
					if (!success) {
						showToast("操作失败,请重试!");
					}
				}
				if (dataMap.containsKey("listUserCategory")) {

					// 递归转线性
					final ArrayList<UserCategory> linearListCategory = (ArrayList<UserCategory>) dataMap
							.get("listUserCategory");
//					ArrayList<UserCategory> linearListCategory = new ArrayList<UserCategory>();
//					for (UserCategory category : recursiveListCategory) {
//						linearListCategory.addAll(convertRecursive2Linear(
//								category, 0));
//					}
					for (UserCategory category : linearListCategory) {
						linearListCategory1.addAll(convertRecursive2Linear(
								category, 0));
					}
					// 初始化状态
					for (UserCategory category : linearListCategory) {
						category.setSelected(false); // 默认未选中
						category.setVisiable(true); // 默认显示
						category.setFolded(true); // 默认展开
					}
					// 保存状态，刷新列表
					for (UserCategory category : linearListCategory) {
						for (UserCategory subCategory : mListCategory) {
							if (category.getId() == subCategory.getId()) {
								category.setVisiable(subCategory.isVisiable());
								category.setFolded(subCategory.isFolded());
								category.setSelected(subCategory.isSelected());
								break;
							}
						}
					}
					mListCategory = linearListCategory1;
					mCategoryListData =  linearListCategory;
					if (mListSelectCategory.size() > 0) { // 用户已选择的目录
						for (UserCategory sCategory : mListSelectCategory) {
							long categoryId = recursiveGetCategoryId(sCategory);
							for (UserCategory category : mListCategory) {
								if (categoryId == category.getId()) {
									category.setSelected(true);
									break;
								}
							}
						}
						mListSelectCategory.clear();
					}
					// 是否需要根据关键字重新筛选
					if (!TextUtils.isEmpty(keywordEt.getText())) {
						searchCatetoryByKeyword(keywordEt.getText() + "");
					} else {
						//将递归模式改成平行模式，然后去点击的目录比对。得到所点击目录下的目录结构
						if (Clickcategory!=null&&Clickcategory.id!=0) {
						new AsyncTask<Void, Void, Void>() {
							@Override
							protected Void doInBackground(Void... params) {
								
									if (linearListCategory1!=null&&!linearListCategory1.isEmpty()) {
										for (int i = 0; i < linearListCategory1.size(); i++) {
											UserCategory userCategory = linearListCategory1.get(i);
											if (Clickcategory.getId().equals(userCategory.getId())&&Clickcategory.getParentId().equals(userCategory.getParentId())) {
												mClickListCategory = (ArrayList<UserCategory>) userCategory.getListUserCategory();
											}
//											for (int j = 0; j < clickedlistUserCategory.size(); j++) {
//												UserCategory userClickCategory = clickedlistUserCategory.get(j);
//												if (userClickCategory.getId().equals(userCategory.getId())&&userClickCategory.getParentId().equals(userCategory.getParentId())) {
//													userClickCategory = userCategory;
//												}
//											}
										}
									}
								return null;
							}
							@Override
							protected void onPostExecute(Void result) {
								super.onPostExecute(result);
								mAdapter.setmListCategory(mClickListCategory,1);
								categorySumTv.setText(mClickListCategory.size()+"个目录");
//								clickedlistUserCategory.clear();
//								clickedlistUserCategory  = newClicklistUserCategory;
								clickedlistUserCategory.get(clickedlistUserCategory.size()-1).setListUserCategory(mClickListCategory);
								tabAdapter.notifyChanged();
								mAdapter.notifyDataSetChanged();
								if (mAdapter.isEmpty()) {
									isCategoryEmpty =  true;
									rootLl.setBackgroundResource(R.drawable.empty);
								}else{
									rootLl.setBackgroundColor(colorState);
									isCategoryEmpty =  false;
								}
								IsChangeCategory = false;
							}
						}.execute();
						}else{
							mAdapter.setmListCategory(mCategoryListData,1);
							categorySumTv.setText(mCategoryListData.size()+"个目录");
							mAdapter.notifyDataSetChanged();
							IsChangeCategory = false;
							if (mAdapter.isEmpty()) {
								isCategoryEmpty =  true;
								rootLl.setBackgroundResource(R.drawable.empty);
							}else{
								rootLl.setBackgroundColor(colorState);
								isCategoryEmpty =  false;
							}
						}
						
						
					}
				}
			}
			break;
		}
		}}}
		if (mAdapter.isEmpty()) {
			rootLl.setBackgroundResource(R.drawable.empty);
		}else{
			rootLl.setBackgroundColor(colorState);
		}
	}
/**
 * 拿到人脉的数据转换成人脉/好友的数据类型
 * @param personSimpleList
 * @return
 */
	private ArrayList<Connections> PersonSimpleListToConnection(PersonSimpleList personSimpleList) {
		ArrayList<Connections> connectionsList = new ArrayList<Connections>();
		for (int i = 0; i < personSimpleList.list.size(); i++) {
			PersonSimple personSimple = personSimpleList.list.get(i);
			Connections connections = new Connections();
			connections.type = Connections.type_persion;
			connections.jtContactMini.id =  personSimple.personid+"";
			connections.jtContactMini.image = personSimple.picpath;
			connections.jtContactMini.name = personSimple.name1+personSimple.name2;
		    if (personSimple.persontype == 1) {
				connections.jtContactMini.isOnline = true;
			}else{
				connections.jtContactMini.isOnline = false;
			}
		    connections.jtContactMini.company =  personSimple.company;
		    connections.jtContactMini.setCareer(personSimple.position) ;
		    connections.jtContactMini.listFixedPhone = personSimple.listFixedPhone;
		    connections.jtContactMini.listMobilePhone = personSimple.listMobilePhone;
		    connectionsList.add(connections);
		}
		return connectionsList;
	}
	
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEventMainThread(AddCategoryEvent event) {
		if(event.isBool()){
			doUpdate();//添加目录更新UI
		}
	}
	@Subscribe
	public void onEventPostThread() {

	}
	@Subscribe
	public void onEventBackgroundThread() {

	}
	@Subscribe
	public void onEventAsync() {

	}
	
	@Override
	protected void onDestroy() {
		controlBus.unregister(CategoryActivity.this);
		super.onDestroy();
	}
}
