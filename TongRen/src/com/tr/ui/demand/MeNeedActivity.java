package com.tr.ui.demand;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.common.constvalue.EnumConst.ModuleType;
import com.tr.R;
import com.tr.api.DemandReqUtil;
import com.tr.model.demand.LableData;
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
import com.tr.ui.demand.util.TextStrUtil;
import com.utils.common.EConsts;
import com.utils.common.ViewHolder;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.time.TimeUtil;

/**
 * @ClassName: MeNeedActivity.java
 * @author ZCS
 * @Date 2015年3月6日 上午11:45:58
 * @Description: 我的需求
 */
public class MeNeedActivity extends JBaseActivity implements OnClickListener,
		IBindData {
	public static final int REQUEST_CODE_LABLE_ACTIVITY = 1001;// 启动标签页的回调
	public static final int REQUEST_CODE_TREE_ACTIVITY = 1002; // 启动目录页的回调 单选

	private boolean eidtItem;
	NeedAdapter myAdapter;
	private EditText searchEt;
	private View treeTreeLableV;// 目录和标签
	private View treeLabelDeleteV;// 目录和标签和删除
	private View treeLl2;// 目录
	private View labelLl2;// 标签
	private View treeLl;// 目录
	private View labelLl;// 标签
	private View deleteItme;// 删除
	private JTPage jtpage;// 分页加载对象
	protected PopupWindow pop;
	private XListView meNeedLv;
	private TextView titleTv;
	private ImageView titleIv;
	private ActionBar mActionBar;
	private List<String> selectedId;
	private List<NeedItemData> needItemList;
	private MenuItem findItem;
	private Dialog dialog;
	private int typeIndex;
	private String mCurKeyword = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demand_me_need);
		pop = new PopupWindow();
		meNeedLv = (XListView) findViewById(R.id.meNeedLv);
		findViewById(R.id.searchLl).setOnClickListener(this);
		treeTreeLableV = findViewById(R.id.treeTreeLableV);
		treeLabelDeleteV = findViewById(R.id.treeLabelDeleteV);
		selectedId = new ArrayList<String>();
		searchEt = (EditText) findViewById(R.id.searchEt);// 搜索
		searchEt.setOnEditorActionListener(editorAction);
		searchEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				mCurKeyword = s.toString();
				startGetData();
			}
		});
		treeLl2 = findViewById(R.id.treeLl2);
		labelLl2 = findViewById(R.id.labelLl2);
		treeLl = findViewById(R.id.treeLl);
		labelLl = findViewById(R.id.labelLl);
		deleteItme = findViewById(R.id.deleteItme);
		treeLl2.setOnClickListener(this);
		labelLl2.setOnClickListener(this);
		treeLl.setOnClickListener(this);
		labelLl.setOnClickListener(this);
		deleteItme.setOnClickListener(this);
		needItemList = new ArrayList<NeedItemData>();
		initData();
		startGetData();// 加载第一位数据
		IntentFilter filter = new IntentFilter();
		filter.addAction(DemandAction.DEMAND_DETAILS_ACTION);
		this.registerReceiver(broad, filter);
	}

	private OnEditorActionListener editorAction = new OnEditorActionListener() {

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if ((actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_SEARCH) && event != null) {
				// 先隐藏键盘
				((InputMethodManager) v.getContext().getSystemService(
						Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
						MeNeedActivity.this.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
				jtpage = null;
				mCurKeyword = searchEt.getText().toString().trim();
				startGetData();
				return true;
			}
			return false;
		}
	};

	

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
						// 刷新接口数据
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
				if (!eidtItem) {// 需求详情
					if (needItemList == null || needItemList.size() == 0) {
						return;
					}
					String type = needItemList.get(position - 1).demandId;
					ENavigate.startNeedDetailsActivity(MeNeedActivity.this,
							type, 1);
				} else {
					if (selectedId.contains(needItemList.get(position - 1).demandId)) {
						selectedId.remove(needItemList.get(position - 1).demandId);
					} else {
						selectedId.add(needItemList.get(position - 1).demandId);
					}
					myAdapter.notifyDataSetChanged();
				}
			}
		});
		// 长按
		meNeedLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				/*
				 * if(selectedId.contains(needItemList.get(position-1).getId())){
				 * selectedId.remove(needItemList.get(position-1).getId());
				 * }else{ selectedId.add(needItemList.get(position-1).getId());
				 * }
				 */
				myAdapter.notifyDataSetChanged();
				eidtItem = true;
				treeLabelDeleteV.setVisibility(View.VISIBLE);
				treeTreeLableV.setVisibility(View.GONE);
				titleIv.setVisibility(View.GONE);
				titleTv.setText("批量选择");
				mActionBar.setTitle("取消");
				findItem.setTitle("全选");
				isCreate = false;
				return true;
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
				DemandReqUtil.getMyNeedList(MeNeedActivity.this,
						MeNeedActivity.this, 0, 20, typeIndex, 2, mCurKeyword,
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

		DemandReqUtil.getMyNeedList(MeNeedActivity.this, MeNeedActivity.this,
				nowIndex, 20, typeIndex, 2, mCurKeyword, null);
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_CODE_LABLE_ACTIVITY: // 标签回调
				// 批量打标签
				// 选择的标签对象
				ArrayList<LableData> lable = (ArrayList<LableData>) data
						.getSerializableExtra(ENavConsts.DEMAND_LABEL_DATA);
				if (lable != null) {
					ArrayList<String> lableids = new ArrayList<String>();
					for (LableData tag : lable) {
						lableids.add(tag.id + "");
					}
					showLoadingDialog();
					DemandReqUtil.createDemandTag(this, this,
							TextStrUtil.getStringAppend(selectedId, ","),
							TextStrUtil.getStringAppend(lableids, ","), null);
				}
				break;
			case REQUEST_CODE_TREE_ACTIVITY:
				// 批量打目录
				ArrayList<UserCategory> treeData = (ArrayList<UserCategory>) data
						.getSerializableExtra(EConsts.Key.KNOWLEDGE_CATEGORY_LIST);
				if (treeData != null) {
					ArrayList<String> treeids = new ArrayList<String>();
					for (UserCategory tag : treeData) {
						treeids.add(TextStrUtil.checkCategoryId(tag));
					}
					showLoadingDialog();
					DemandReqUtil.createDemandCategory(this, this, null,
							TextStrUtil.getStringAppend(selectedId, ","),
							TextStrUtil.getStringAppend(treeids, ","));
				}
				break;
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.treeLl2:// 进入目录
			ENavigate.startKnowledgeCategoryActivityForResult(
					MeNeedActivity.this, REQUEST_CODE_TREE_ACTIVITY, null,
					ModuleType.DEMAND, false,titleTv.getText().toString()); // 单选
			break;
		case R.id.labelLl2:// 进入标签
			ENavigate.startRedactLabelActivity(MeNeedActivity.this,RedactLabelActivity.ModulesType.DemandModules, true); // 进入标签界面
			break;
		case R.id.treeLl:// 编辑目录
			if (selectedId == null || selectedId.size() <= 0) {
				showToast("请至少选择一个需求");
				return;
			}
			ENavigate.startKnowledgeCategoryActivityForResult(
					MeNeedActivity.this, REQUEST_CODE_TREE_ACTIVITY, null,
					ModuleType.DEMAND, true,titleTv.getText().toString()); // 多选
			break;
		case R.id.labelLl:// 编辑标签
			if (selectedId == null || selectedId.size() <= 0) {
				showToast("请至少选择一个需求");
				return;
			}
			ENavigate.startRedactLabelActivity(MeNeedActivity.this,
					REQUEST_CODE_LABLE_ACTIVITY, RedactLabelActivity.ModulesType.DemandModules,false);// 标签回调
			break;
		case R.id.deleteItme:// 删除
			if (selectedId == null || selectedId.size() <= 0) {
				showToast("请至少选择一个需求");
				return;
			}
			View view = View.inflate(MeNeedActivity.this,
					R.layout.demand_user_setting_dialog1, null);
			TextView infoTv = (TextView) view.findViewById(R.id.infoTv);
			infoTv.setText("确认删除这" + selectedId.size() + "条需求?");
			view.findViewById(R.id.cancelTv).setOnClickListener(this);
			view.findViewById(R.id.confirmTv).setOnClickListener(this);
			showDialog(view);
			break;
		case R.id.searchAllTv:// 搜索全部
			// jtpage.setIndex(0);
			jtpage = null;
			typeIndex = 0;
			startGetData();
			titleTv.setText("我的需求");
			pop.dismiss();
			break;
		case R.id.searchMy:// 搜索我的创建
			// jtpage.setIndex(0);
			jtpage = null;
			typeIndex = 2;
			startGetData();
			titleTv.setText("我创建");
			pop.dismiss();
			break;
		case R.id.searchSaveTv:// 搜索我的保存
			// jtpage.setIndex(0);
			jtpage = null;
			typeIndex = 2;
			startGetData();
			titleTv.setText("已保存");
			pop.dismiss();
			break;
		case R.id.collectCollectTv:// 搜索我的收藏
			// jtpage.setIndex(0);
			jtpage = null;
			typeIndex = 3;
			startGetData();
			titleTv.setText("已收藏");
			pop.dismiss();
			break;
		case R.id.cancelTv:
			dialog.dismiss();
			break;
		case R.id.confirmTv:// 删除已选的条目
			dialog.dismiss();
			deleteNeedList();
			break;
		}
	}

	private List<NeedItemData> deleteNeed;
	private boolean isCreate;

	private void deleteNeedList() {
		// 删除选择的数据
		deleteNeed = new ArrayList<NeedItemData>();// 需要删除的对象
		for (NeedItemData item : needItemList) {
			for (String string : selectedId) {
				if (item.demandId.equals(string)) {
					deleteNeed.add(item);
				}
			}
		}
		showLoadingDialog();
		DemandReqUtil.deleteDemandList(this, this, null,
				TextStrUtil.getStringAppend(selectedId, ","));
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
		public Object getItem(int position) {

			return selectedId.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
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

			try {
				timeTv.setText(TimeUtil.TimeFormat(needItem.createTime));
			} catch (Exception exception) {
				timeTv.setText("时间解析错误");
			}
			titleTv.setText(needItem.title.value);// 标题对象
			if (needItem.amount != null
					&& !TextUtils.isEmpty(needItem.amount.unit)) {
				priceTv.setText(needItem.amount.getAmountData()); // 显示金额信息
			} else {
				priceTv.setText("");// 没有金额信息
			}
			// 显示选择的状态
			if (selectedId.contains(needItem.demandId)) {
				editSateIv.setSelected(true);
			} else {
				editSateIv.setSelected(false);
			}
			editSateIv.setVisibility(eidtItem ? View.VISIBLE : View.GONE);
			if (needItem.demandType == ChooseDataUtil.CHOOSE_type_OutInvestType) {
				typeIv.setImageResource(R.drawable.demand_me_need01);
			} else {
				typeIv.setImageResource(R.drawable.demand_me_need02);
			}
			return convertView;
		}
	}

	@Override
	public void initJabActionBar() {
		mActionBar = jabGetActionBar();
		/*
		 * mActionBar.setTitle("动态");
		 * mActionBar.setSplitBackgroundDrawable(getResources
		 * ().getDrawable(R.drawable.auth_title_back));
		 */
		// 设置actionbar的背景图
		/*
		 * Drawable myDrawable =
		 * getResources().getDrawable(R.drawable.auth_title_back);
		 * mActionBar.setBackgroundDrawable(myDrawable); // 设置背景图片
		 * mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		 */

		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(true);
		View customView = mActionBar.getCustomView();
		final View mCustomView = getLayoutInflater().inflate(
				R.layout.demand_actionbar_title, null);
		titleTv = (TextView) mCustomView.findViewById(R.id.titleTv);
		titleIv = (ImageView) mCustomView.findViewById(R.id.titleIv);

		mActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView
				.getLayoutParams();
		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK
				| Gravity.CENTER_HORIZONTAL;
		mActionBar.setCustomView(mCustomView, mP);
		mActionBar.setTitle(" ");
		// 搜索
		titleTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (eidtItem) {
					return;
				}
				View view = View.inflate(MeNeedActivity.this,
						R.layout.demand_me_need_item, null);
				view.findViewById(R.id.searchAllTv).setOnClickListener(
						MeNeedActivity.this);
				view.findViewById(R.id.searchMy).setOnClickListener(
						MeNeedActivity.this);
				view.findViewById(R.id.searchSaveTv).setVisibility(View.GONE);
				view.findViewById(R.id.collectCollectTv).setOnClickListener(
						MeNeedActivity.this);

				pop.setWidth(LayoutParams.WRAP_CONTENT);
				pop.setHeight(LayoutParams.WRAP_CONTENT);
				pop.setContentView(view);
				pop.setBackgroundDrawable(new BitmapDrawable());
				pop.setFocusable(true);
				pop.setOutsideTouchable(true);
				pop.setAnimationStyle(R.style.demand_me_need_popwin_anim_style);

				pop.showAsDropDown(mCustomView, -7, -2);

			}
		});

	}

	/**
	 * 弹出dialog
	 * 
	 * @param view
	 */
	private void showDialog(View view) {
		dialog = new Dialog(MeNeedActivity.this, R.style.MyDialog);

		// dialog.setCancelable(false);//是否允许返回
		dialog.addContentView(view, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		dialog.show();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (eidtItem && keyCode == KeyEvent.KEYCODE_BACK) {
			cancelSelect();
			// titleTv = (TextView) mCustomView.findViewById(R.id.titleTv);
			// titleIv = (ImageView) mCustomView.findViewById(R.id.titleIv);
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void cancelSelect() {
		eidtItem = false;
		myAdapter.notifyDataSetChanged();
		treeLabelDeleteV.setVisibility(View.GONE);
		treeTreeLableV.setVisibility(View.VISIBLE);
		titleIv.setVisibility(View.VISIBLE);
		titleTv.setText("我的需求");
		mActionBar.setTitle(" ");
		isAll = false;
		selectedId.clear();
		findItem.setTitle("创建");
		isCreate = true;
	}

	@Override
	public void bindData(int tag, Object object) {
		meNeedLv.showFooterView(false);
		meNeedLv.stopLoadMore();
		meNeedLv.stopRefresh();
		dismissLoadingDialog();
		if (object == null) {
			return;
		}
		if (tag == EAPIConsts.demandReqType.demand_getMyNeedList) {
			NeedItemListItem item = (NeedItemListItem) object;
			jtpage = item.getJtPage();

			// ArrayList<NeedItemData> lists = jtPage2.getLists();
			if (jtpage == null) {
				return;
			}
			if (jtpage.getIndex() == 1) {
				needItemList.clear();
			}
			if ((jtpage != null) && (jtpage.getLists() != null)) {
				for (int i = 0; i < jtpage.getLists().size(); i++) {
					needItemList.add((NeedItemData) jtpage.getLists().get(i));
				}
				myAdapter.notifyDataSetChanged();
			}
			if (jtpage.getIndex() >= jtpage.getTotalPage()) {
				meNeedLv.setPullLoadEnable(false);
			}
		} else if (tag == EAPIConsts.demandReqType.demand_rTagSave
				|| tag == EAPIConsts.demandReqType.demand_rCategorySave) {
			boolean rTag = (Boolean) object;
			if (rTag) {// 批量打标签或目录成功
				isAll= false;
				selectedId.clear();
				myAdapter.notifyDataSetChanged();
				Toast.makeText(this, "操作成功", Toast.LENGTH_SHORT).show();
			}
		} else if (tag == EAPIConsts.demandReqType.demand_mydemandDelete) {
			// 删除我的需求
			boolean rTag = (Boolean) object;
			if (rTag) {//
				Toast.makeText(this, "操作成功", Toast.LENGTH_SHORT).show();
				isAll= false;
				selectedId.clear();
				needItemList.removeAll(deleteNeed);
				myAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.demand_select_all_project, menu);
		findItem = menu.findItem(R.id.select_all);
		findItem.setTitle("创建");
		isCreate = true;
		return true;
	}

	private boolean isAll = false;// 全选按钮 判断全选按钮状态

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.select_all:
			if (findItem.getTitle().length() > 0) {
				if (isCreate) {
					ENavigate.startDemandActivityForResult(MeNeedActivity.this, 999);
				}else{
				
				if (isAll) {
					isAll = false;
					selectedId.clear();
				} else {
					isAll = true;
					for (NeedItemData itemList : needItemList) {
						if (!selectedId.contains(itemList.demandId)) {
							selectedId.add(itemList.demandId);
						}
					}
				}
				myAdapter.notifyDataSetChanged();
				}
			}
			break;
		
		case android.R.id.home:
			if (eidtItem) {
				cancelSelect();
			} else {
				finish();
			}
			/*
			 * if (demand_save==0) { finishBack(); }else{ finish(); }
			 */
			break;
		default:
			break;
		}

		return true;
	}

	@Override
	protected void onDestroy() {
		if (broad != null)
			this.unregisterReceiver(broad);
		super.onDestroy();
	}

	@Override
	public void onLoadingDialogCancel() {

	}
}
