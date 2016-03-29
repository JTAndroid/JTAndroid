package com.tr.ui.knowledge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.App;
import com.tr.R;
import com.tr.api.KnowledgeReqUtil;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.knowledge.UserCategory;
import com.tr.model.obj.JTFile;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.demand.RedactLabelActivity;
import com.tr.ui.knowledge.GlobalKnowledgeTagActivity.OperateType;
import com.tr.ui.people.cread.utils.Utils;
import com.utils.common.EConsts;
import com.utils.common.EUtil;
import com.utils.http.EAPIConsts.KnoReqType;
import com.utils.http.IBindData;
import com.utils.image.LoadImage;
import com.utils.time.TimeUtil;
import com.utils.time.Util;

public class MyKnowledgeActivity extends JBaseActivity implements IBindData{

	public static final String TAG = "MyKnowledgeActivity";
	private Context context;

	private static final int TYPE_ALL = 3; // 全部 我的知识:::修改：将全部我的知识删掉，首页显示我创建的
	private static final int TYPE_FAVORITES = 1; // 我收藏的
	private static final int TYPE_SHARE_TO_ME = 2; // 分享给我
	private static final int TYPE_CREATE = 3; // 我创建的

	private static final int TYPE_CATEGORY = 1004; // 目录返回的
	private static final int TYPE_TAG = 1003; // 标签返回的

	public final static int STATE_NORMAL = 0; // 正常
	public final static int STATE_GETMORE = 1; // 加载更多
	
	public final static int STATE_REFRESH = 2; // 刷新

	public static final int REQUESTCODE_CREATE_KNOWLEDGE_ACTIVITY = 1000;
	public static final int REQUESTCODE_GLOBAL_KNOWLEDGE_CATEGORY_ACTIVITY = 1001;
	public static final int REQUESTCODE_GLOBAL_KNOWLEDGE_TAG_ACTIVITY = 1002;
	public static final int REQUESTCODE_EDIT_KNOWLEDGE_TAG_ACTIVITY = 1003;

	private int type = TYPE_ALL;
	private int deleteSourceType = 0;// 删除知识的类型：来源,1-我收藏的,2-分享给我的,其他默认0

	private String knoTagName = "";// 标签返回的标签名
	private UserCategory category;// 目录返回的目录对象

	private boolean editMode = false; // 编辑状态
	private boolean checkAll = false; // 全选状态
	private HashMap<KnowledgeMini2, Boolean> checkedHm = new HashMap<KnowledgeMini2, Boolean>(); // checked集合

	private LinearLayout headLl;
	private EditText searchEt;
	private ImageView creatKonwledgeIv;
	private PopupWindow pop;
	private LinearLayout editRl;
	private ImageView tagIv;
	private ImageView shareIv;
	private ImageView deleteIv;

	private LinearLayout footLl;
	private TextView categoryTv;
	private TextView tagTv;

	private KnowledgeLvAdapter knowledgeLvAdapter;

	// private SwipeRefreshLayout knowledgeListSrl;
	private XListView knowledgeListLv;
	private ArrayList<KnowledgeMini2> knowledgeList;
	private ArrayList<KnowledgeMini2> knowledgeCheckedList = new ArrayList<KnowledgeMini2>();

	private int index = -1;// 当前页码
	private int size;// 该页内容数
	private int total;// 项目总数

//	private MenuItem editItem;
	private MenuItem typeItme;
	private MenuItem checkAllItem;


	public static String mCurKeyword = ""; 

	private View moreView; // 加载更多页面
	private TextView mvTextView;
	private View mvProgressBar;

	private int lastItem;
	private int count;// 知识缩略对象的总数
	// private JTPage mPage;
	private int mState = 0;// 0-正常状态 1-获取更多中 2-刷新中
//	private SwipeRefreshLayout swipeLayout;
	private TextView titleTv;
	private ImageView titleIv;
	private TextView searchSaveTv;
	private TextView searchMy;
	private TextView collectCollectTv;
	private KnowledgeMini2 DeleteKnowledge;
	@Override
	public void initJabActionBar() {
		ActionBar actionBar = getActionBar();
		
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(true);
		final View mCustomView = getLayoutInflater().inflate(
				R.layout.demand_actionbar_title, null);
		actionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView
				.getLayoutParams();
		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK
				| Gravity.CENTER_HORIZONTAL;
		actionBar.setCustomView(mCustomView, mP);
		actionBar.setTitle(" ");
		titleTv = (TextView) mCustomView.findViewById(R.id.titleTv);
		titleTv.setText("我创建的");
		titleTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				View view = View.inflate(context,
						R.layout.demand_me_need_item, null);
				searchSaveTv = (TextView) view.findViewById(R.id.searchSaveTv);
				searchMy = (TextView) view.findViewById(R.id.searchMy);
				collectCollectTv = (TextView) view.findViewById(R.id.collectCollectTv);
				view.findViewById(R.id.searchAllTv).setVisibility(View.GONE);
				searchSaveTv.setOnClickListener(mOnClickListener);
				searchSaveTv.setText("分享给我的");
				searchMy.setOnClickListener(mOnClickListener);
				searchMy.setText("我创建的");
				collectCollectTv.setOnClickListener(mOnClickListener);
				collectCollectTv.setText("我收藏的");
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
		titleIv = (ImageView) mCustomView.findViewById(R.id.titleIv);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		pop = new PopupWindow();
 		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mWidth = metric.widthPixels;
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		int px = Util.DensityUtil.dip2px(context, 5);
		lp.setMargins(px, 0, px, 0);
		setContentView(R.layout.activity_my_knowledge);
		initComponent();
	}

	@SuppressWarnings("deprecation")
	private void initComponent() {
		headerVi = findViewById(R.id.headerVi);
		headLl = (LinearLayout) findViewById(R.id.headLl);
		searchEt = (EditText) findViewById(R.id.searchEt);
		searchEt.clearComposingText();
		searchEt.addTextChangedListener(mTextWatcher);

		creatKonwledgeIv = (ImageView) findViewById(R.id.creatKonwledgeIv);
		creatKonwledgeIv.setOnClickListener(mOnClickListener);

		editRl = (LinearLayout) findViewById(R.id.editRl);
		tagIv = (ImageView) findViewById(R.id.tagIv);
		tagIv.setOnClickListener(mOnClickListener);
		shareIv = (ImageView) findViewById(R.id.shareIv);
		shareIv.setOnClickListener(mOnClickListener);
		deleteIv = (ImageView) findViewById(R.id.deleteIv);
		deleteIv.setOnClickListener(mOnClickListener);

		footLl = (LinearLayout) findViewById(R.id.footLl);
		categoryTv = (TextView) findViewById(R.id.categoryTv);
		categoryTv.setOnClickListener(mOnClickListener);

		tagTv = (TextView) findViewById(R.id.tagTv);
		tagTv.setOnClickListener(mOnClickListener);

//		swipeLayout = (SwipeRefreshLayout) findViewById(R.id.knowledgeListSrl);
//		swipeLayout.setOnRefreshListener(this);
//		swipeLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);

		moreView = inflater.inflate(R.layout.pulldown_footer, null);
		mvTextView = (TextView) moreView.findViewById(R.id.pulldown_footer_text);
		mvProgressBar = (View) moreView.findViewById(R.id.pulldown_footer_loading);

		// knowledgeListSrl = (SwipeRefreshLayout)
		// findViewById(R.id.knowledgeListSrl);
		knowledgeListLv = (XListView) findViewById(R.id.knowledgeListLv);
		knowledgeLvAdapter = new KnowledgeLvAdapter(context);
		knowledgeLvAdapter.setKnowledgeList(knowledgeList);
//		knowledgeListLv.addFooterView(moreView);
//		knowledgeListLv.showFooterView(false);
		// 设置xlistview可以加载、刷新
		knowledgeListLv.setPullLoadEnable(true);
		knowledgeListLv.setPullRefreshEnable(true);
		knowledgeListLv.setAdapter(knowledgeLvAdapter);
		knowledgeListLv.setOnItemClickListener(mOnItemClickListener);
		knowledgeListLv.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				startGetData(index);
			}
			
			@Override
			public void onLoadMore() {
				loadMoreData();
			}
		});
		
		
		
//		knowledgeListLv.setOnScrollListener(this); // 设置listview的滚动事件

		if (knowledgeList != null) {
			count = knowledgeList.size();
		} else {
			knowledgeList = new ArrayList<KnowledgeMini2>();
		}

		mState = STATE_NORMAL;
		mCurKeyword = "";
		onRefresh();
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 创建知识
			if (creatKonwledgeIv == v) {
				ENavigate.startCreateKnowledgeActivity(MyKnowledgeActivity.this, true, REQUESTCODE_CREATE_KNOWLEDGE_ACTIVITY);
//				ENavigate.startCreateOrgNullActivityForResult(MyKnowledgeActivity.this);
				
			}
			// 目录
			else if (categoryTv == v) {
				ENavigate.startKnowledgeCategoryActivityForResult(MyKnowledgeActivity.this, REQUESTCODE_GLOBAL_KNOWLEDGE_CATEGORY_ACTIVITY,null,false,titleTv.getText().toString());
//				ENavigate.startGlobalKnowledgeCategoryActivityForResult(MyKnowledgeActivity.this, REQUESTCODE_GLOBAL_KNOWLEDGE_CATEGORY_ACTIVITY,null);
			}
			// 标签
			else if (tagTv == v) {
//				ENavigate.startGlobalKnowledgeTagActivityForResult(MyKnowledgeActivity.this, REQUESTCODE_GLOBAL_KNOWLEDGE_TAG_ACTIVITY,OperateType.ClickBack);
				
				ENavigate.startRedactLabelActivity(MyKnowledgeActivity.this,RedactLabelActivity.ModulesType.KnowledgeModules, true); // 进入标签界面
			}
			// 编辑选中知识标签
			else if (tagIv == v) {
				if (getListKnowledgeId(checkedHm).size() == 0) {
					showToast("请选择所编辑的知识");
				} else {
					ENavigate.startEditKnowledgeTagActivityForResult(MyKnowledgeActivity.this, REQUESTCODE_EDIT_KNOWLEDGE_TAG_ACTIVITY, knowledgeCheckedList);
				}
			}
			// 分享选中知识
			else if (shareIv == v) {

				if (getListKnowledgeId(checkedHm).size() == 0) {
					showToast("请选择要分享的知识");
				} else {
					ArrayList<KnowledgeMini2> listKnowledge = traversalHMap(checkedHm);
					if (listKnowledge.size() > 0) {

						// if(listKnowledge.size() == 1){
						// ENavigate.startShareActivity(MyKnowledgeActivity.this,
						// listKnowledge.get(0).toJTFile());
						// }
						// else{
						/**
						 * String mSuffixName = mKnowledge2.toJTFile().getmSuffixName();
				mKnowledge2.setDesc(filterHtml(mSuffixName));
				JTFile jtFile = mKnowledge2.toJTFile();
				jtFile.mFileName = "[知识] "+mKnowledge2.getTitle();
				jtFile.virtual = mKnowledge2.getType()+"";
						 */
						ArrayList<JTFile> listJtFile = new ArrayList<JTFile>();
						for (KnowledgeMini2 knowledge : listKnowledge) {
							String mSuffixName = knowledge.toJTFile().getmSuffixName();
							knowledge.setDesc(filterHtml(mSuffixName));
							JTFile jtFile = knowledge.toJTFile();
							jtFile.messageID=EUtil.genMessageID();
//							jtFile.mFileName ="分享了[知识] ";//+knowledge.getTitle();
							jtFile.mFileName =knowledge.getTitle();
							jtFile.virtual = knowledge.getType()+"";
							listJtFile.add(jtFile);
						}
						// ENavigate.startShareActivity(MyKnowledgeActivity.this,
						// JTFile.TYPE_KNOWLEDGE2, listJtFile);
						ENavigate.startSocialShareActivity(MyKnowledgeActivity.this, listJtFile);
						checkedHm.clear();//清空 避免造成选中数据迭代
						// }
					}
				}
			}
			// 删除选中知识
			else if (deleteIv == v) {
				if (getListKnowledgeId(checkedHm).size() > 0) {
					Builder builder = new AlertDialog.Builder(context);
					builder.setTitle("提示：");
					builder.setMessage("确定要删除知识吗？");
					builder.setNegativeButton("取消", null);
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							showLoadingDialog();
							// swipeLayout.setRefreshing(true);

							KnowledgeReqUtil.doDeleteKnowledgeById(context, MyKnowledgeActivity.this, App.getUserID(), getListKnowledgeId(checkedHm), getListKnowledgeType(checkedHm),deleteSourceType, null);
						}
					});
					builder.show();
				} else {
					showToast("请选择要删除的知识！");
				}
			}
			// 我收藏的
			else if (v==collectCollectTv){
						type = TYPE_FAVORITES;
						deleteSourceType = TYPE_FAVORITES;
						clearResponseData();
						reSetActionBarTitle();
						reSetLayout();
						onRefresh();
						pop.dismiss();
			}
					// 分享给我的
			else if (v==searchSaveTv){
						type = TYPE_SHARE_TO_ME;
						deleteSourceType = TYPE_SHARE_TO_ME;
						clearResponseData();
						reSetActionBarTitle();
						reSetLayout();
						onRefresh();
						pop.dismiss();
			}
					// 我创建的
			else if (v==searchMy){
						type = TYPE_CREATE;
						clearResponseData();
						reSetActionBarTitle();
						reSetLayout();
						onRefresh();
						pop.dismiss();
			}
		}
	};
	/**
	 * @param html
	 * @return
	 */
	public String filterHtml(String html) {
		Pattern pattern = Pattern.compile("<style[^>]*?>[\\D\\d]*?<\\/style>");
		Matcher matcher = pattern.matcher(html);
		String htmlStr = matcher.replaceAll("");
		pattern = Pattern.compile("<[^>]+>");
		String filterStr = pattern.matcher(htmlStr).replaceAll("");
		filterStr.replace("&nbsp;", " ");
		return filterStr;
	}
	/**
	 * 遍历出选中的条目
	 * 
	 * @param checkedHm
	 * @return 返回被选中的知识列表
	 */
	public ArrayList<KnowledgeMini2> traversalHMap(HashMap<KnowledgeMini2, Boolean> checkedHm) {
		ArrayList<KnowledgeMini2> listKno = new ArrayList<KnowledgeMini2>();
		// Set set = checkedHm.entrySet();
		Iterator<Entry<KnowledgeMini2, Boolean>> it = checkedHm.entrySet().iterator();
		while (it.hasNext()) {
			Entry entry = (Entry) it.next();
			KnowledgeMini2 knoledge = (KnowledgeMini2) entry.getKey();
			boolean b = (Boolean) entry.getValue();
			if (b) {
				listKno.add(knoledge);
			}
		}
		return listKno;
	}

	/**
	 * 根据集合获取listKnowledgeId
	 * 
	 * @param checkedHm
	 * @return
	 */
	public ArrayList<Long> getListKnowledgeId(HashMap<KnowledgeMini2, Boolean> checkedHm) {
		ArrayList<KnowledgeMini2> Knowledge = traversalHMap(checkedHm);
		ArrayList<Long> listKnowledgeId = new ArrayList<Long>();
		for (int i = 0; i < Knowledge.size(); i++) {
			if (deleteSourceType == TYPE_SHARE_TO_ME) {
				listKnowledgeId.add(Knowledge.get(i).shareMeId);
			} else {
				listKnowledgeId.add(Knowledge.get(i).id);
			}
		}
		return listKnowledgeId;
	}

	/**
	 * 根据集合获取listKnowledgeType
	 * 
	 * @param checkedHm
	 * @return
	 */
	public ArrayList<Integer> getListKnowledgeType(HashMap<KnowledgeMini2, Boolean> checkedHm) {
		ArrayList<KnowledgeMini2> Knowledge = traversalHMap(checkedHm);
		ArrayList<Integer> listKnowledgeType = new ArrayList<Integer>();
		for (int i = 0; i < Knowledge.size(); i++) {
			listKnowledgeType.add(Knowledge.get(i).type);
		}
		return listKnowledgeType;
	}

	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (position==0) {
				return;
			}
			if (editMode) {// 屏蔽进入知识详情
				KnowledgeMini2 knowledgeMini2 = knowledgeList.get(position-1);
				if (checkedHm.get(knowledgeMini2) != null) {
					boolean b = checkedHm.get(knowledgeMini2);
					checkedHm.put(knowledgeMini2, !b);
				} else {
					checkedHm.put(knowledgeMini2, true);
				}
				knowledgeLvAdapter.notifyDataSetChanged();
			}else{
				if ( knowledgeList.get(position-1)!=null) {
					ENavigate.startCollectionKnowledgeOfDetailActivity(MyKnowledgeActivity.this, knowledgeList.get(position-1).id, knowledgeList.get(position-1).type, true);
				}else {
					Toast.makeText(context, "该知识已删除", 1).show();
				}
			}
		}

	};
	
	private TextWatcher mTextWatcher = new TextWatcher() {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void afterTextChanged(Editable s) { // 关键字改变后更新列表
			mCurKeyword = s.toString();
			clearResponseData();
			onRefresh();
		}
	};
	private LayoutParams lp;
	private int mWidth;
	private View headerVi;
	private boolean firstIn = true;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		String MSG = "onCreateOptionsMenu()";

		getMenuInflater().inflate(R.menu.my_knowledge_activity_actions, menu);

//		editItem = menu.findItem(R.id.action_edit);
		typeItme = menu.findItem(R.id.action_type);
		checkAllItem = menu.findItem(R.id.action_check_all);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
//		// 编辑
//		case R.id.action_edit:
//			editMode = true;
//			actionBar.setTitle("编辑");
//			reSetEditModeLayout();
//
//			break;

		// 返回 或 取消编缉按钮
		case android.R.id.home:
			// 编辑状态
			if (editMode) {
				editMode = false;
				reSetActionBarTitle();
				reSetEditModeLayout();
				reSetLayout();
			} else if (type == TYPE_CATEGORY) {
				// 当处于目录||标签的知识列表下时，点击返回，应再次打开选择目录/知识页面
//				ENavigate.startGlobalKnowledgeCategoryActivityForResult(MyKnowledgeActivity.this, REQUESTCODE_GLOBAL_KNOWLEDGE_CATEGORY_ACTIVITY);
				ENavigate.startKnowledgeCategoryActivityForResult(MyKnowledgeActivity.this, REQUESTCODE_GLOBAL_KNOWLEDGE_CATEGORY_ACTIVITY,null,false,titleTv.getText().toString());
			} else if (type == TYPE_TAG) {
				ENavigate.startGlobalKnowledgeTagActivityForResult(MyKnowledgeActivity.this, REQUESTCODE_GLOBAL_KNOWLEDGE_TAG_ACTIVITY,OperateType.ClickBack);
			}
			// 返回状态
			else {
				return super.onOptionsItemSelected(item);
			}
			break;

		// checkbox全选
		case R.id.action_check_all:
			checkAll = checkAll ? false : true;
			// checkedHm.clear();
			reSetCheckedAllstate();
			knowledgeLvAdapter.notifyDataSetChanged();

			break;
			
		case R.id.action_type: //更多
			
			final PopupWindow popupWindow = new PopupWindow(MyKnowledgeActivity.this);
			popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
			popupWindow.setWidth(LayoutParams.WRAP_CONTENT);
			popupWindow.setFocusable(true);
			popupWindow.setOutsideTouchable(true);
			ColorDrawable dw = new ColorDrawable(0000000);
            popupWindow.setBackgroundDrawable(dw);
			popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
			View convertView = View.inflate(MyKnowledgeActivity.this, R.layout.main_menu_more_popupwindow, null);
			LinearLayout home_scan = (LinearLayout) convertView.findViewById(R.id.home_scan);
			LinearLayout invite_friends = (LinearLayout) convertView.findViewById(R.id.invite_friends);
			
			convertView.findViewById(R.id.onekeyback).setVisibility(View.GONE);
			convertView.findViewById(R.id.line_3).setVisibility(View.GONE);
			convertView.findViewById(R.id.scan_Iv).setVisibility(View.GONE);
			convertView.findViewById(R.id.invite_friends_Iv).setVisibility(View.GONE);
			convertView.findViewById(R.id.onekeyback_Iv).setVisibility(View.GONE);
			
			TextView scan_Tv = (TextView) convertView.findViewById(R.id.scan_Tv);
			TextView invite_friends_Tv = (TextView) convertView.findViewById(R.id.invite_friends_Tv);
			scan_Tv.setText("创建知识");
			invite_friends_Tv.setText("编辑");
			home_scan.setOnClickListener(new View.OnClickListener() {// 创建知识

				@Override
				public void onClick(View v) {
					ENavigate.startCreateKnowledgeActivity(context);
					popupWindow.dismiss();
				}
			});
			invite_friends.setOnClickListener(new View.OnClickListener() {// 编辑

				@Override
				public void onClick(View v) {
					editMode = true;
					reSetEditModeLayout();
					popupWindow.dismiss();
				}
			});
			popupWindow.setContentView(convertView);
			popupWindow.showAsDropDown(getActionBar().getCustomView(),(int)Utils.convertDpToPixel(120),0);
			break;
		/*case R.id.createKnowledge: //创建知识
			ENavigate.startCreateKnowledgeActivity(context);
			break; 
		case R.id.editKnowledge: //编辑
			editMode = true;
			titleTv.setText("编辑");
			reSetEditModeLayout();
			break; */
		default:
			break;
		}

		return false;
	}

	private void clearResponseData() {
		count = 0;
		index = -1;
		size = 0;
		total = 0;
		knowledgeList.clear();
		knowledgeLvAdapter.notifyDataSetChanged();
	}

	private void reSetCheckedAllstate() {
		for (KnowledgeMini2 knowledge : knowledgeList) {
			checkedHm.put(knowledge, checkAll);
		}
	}

	private void reSetActionBarTitle() {
		String str = "";
		// 将全部我的知识删除掉，修改为我创建的
		if (type == TYPE_ALL) {
			str = "我创建的";
		}

		switch (type) {
		case TYPE_CREATE:
			str = "我创建的";
			break;
		case TYPE_CATEGORY:// 目录返回
			if (category != null) {
				str = category.getCategoryname();
			} else {
				str = "目录";
			}
			break;
		case TYPE_FAVORITES:
			str = "我收藏的";
			break;
		case TYPE_SHARE_TO_ME:
			str = "分享给我的";
			break;
		case TYPE_TAG:// 标签返回
			str = knoTagName;
			break;

		default:
			break;
		}
		titleTv.setText(str);
	}

	/**
	 * 重设编辑状态布局
	 */
	private void reSetEditModeLayout() {
		knowledgeLvAdapter.setEditMode(editMode);
		knowledgeLvAdapter.notifyDataSetChanged();

		headLl.setVisibility(editMode ? View.GONE : View.VISIBLE);
		footLl.setVisibility(editMode ? View.GONE : View.VISIBLE);
		editRl.setVisibility(editMode ? View.VISIBLE : View.GONE);
		titleIv.setVisibility(editMode?  View.GONE : View.VISIBLE);
//		editItem.setVisible(editMode ? false : true);
		typeItme.setVisible(editMode ? false : true);
		checkAllItem.setVisible(editMode ? true : false);
	}

	/**
	 * 在非编辑状态下重新布局
	 */
	private void reSetLayout() {
		if(typeItme != null){
			typeItme.setVisible(type == TYPE_CATEGORY || type == TYPE_TAG ? false : true);
		}
	}

	class KnowledgeLvAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<KnowledgeMini2> knowledgeList = new ArrayList<KnowledgeMini2>();

		private boolean editMode;
		private int tagLlWidth;

		public KnowledgeLvAdapter(Context context) {
			super();
			this.context = context;
			if (checkedHm == null) {
				for (KnowledgeMini2 knowledge : knowledgeList) {
					checkedHm.put(knowledge, false);
				}
			}
		}

		public void setEditMode(boolean editMode) {
			this.editMode = editMode;
		}

		public void setKnowledgeList(ArrayList<KnowledgeMini2> knowledgeList) {
			if (knowledgeList != null) {
				this.knowledgeList = knowledgeList;
				if (checkedHm == null) {
					for (KnowledgeMini2 knowledge : knowledgeList) {
						checkedHm.put(knowledge, false);
					}
				}
			}
		}

		@Override
		public int getCount() {
			return knowledgeList.size();
		}

		@Override
		public KnowledgeMini2 getItem(int position) {
			// if (position>=knowledgeList.size()) {
			// return null;
			// }
			// else {
			return knowledgeList.get(position);
			// }
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.item_my_knowledge_lv, null);
				viewHolder.cb = (CheckBox) convertView.findViewById(R.id.cb);
				viewHolder.cb.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						KnowledgeMini2 knowledgeMini2 = (KnowledgeMini2) v.getTag();
						// long id = knowledgeMini2.getId();
						if (checkedHm.get(knowledgeMini2) != null) {
							boolean b = checkedHm.get(knowledgeMini2);
							checkedHm.put(knowledgeMini2, !b);
						} else {
							checkedHm.put(knowledgeMini2, true);
						}
						notifyDataSetChanged();
					}
				});
				viewHolder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						KnowledgeMini2 knowledgeMini2 = (KnowledgeMini2) buttonView.getTag();
						if (isChecked) {
							knowledgeCheckedList.add(knowledgeMini2);
						} else {
							knowledgeCheckedList.remove(knowledgeMini2);
						}
					}
				});
				

				viewHolder.titleTv = (TextView) convertView.findViewById(R.id.titleTv);
//				viewHolder.numTv = (TextView) convertView.findViewById(R.id.numTv);
				viewHolder.tagTv = (TextView) convertView.findViewById(R.id.tagTv);
				viewHolder.timeTv = (TextView) convertView.findViewById(R.id.timeTv);
				viewHolder.knowledgeIv = (ImageView) convertView.findViewById(R.id.knowledgeIv);
				viewHolder.moreIv = (ImageView) convertView.findViewById(R.id.moreIv);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			final KnowledgeMini2 knowledge = getItem(position);
			viewHolder.moreIv.setOnClickListener(new OnClickListener() {
				
				private PopupWindow popupWindow;

				@Override
				public void onClick(View v) {
					View view = View.inflate(context, R.layout.moreknowledgepop, null);
					popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
					popupWindow.setAnimationStyle(R.style.PupwindowAnimation);
					popupWindow.setBackgroundDrawable(new BitmapDrawable());
					popupWindow.setOutsideTouchable(true);
					popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
					LinearLayout fenxiangLl = (LinearLayout) view.findViewById(R.id.fenxiangLl);
					LinearLayout biaoqianLl = (LinearLayout) view.findViewById(R.id.biaoqianLl);
					LinearLayout shanchuLl = (LinearLayout) view.findViewById(R.id.shanchuLl);
					TextView cancel = (TextView)view.findViewById(R.id.cancel);
					cancel.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							popupWindow.dismiss();
							
						}
					});
					fenxiangLl.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							popupWindow.dismiss();

									// if(listKnowledge.size() == 1){
									// ENavigate.startShareActivity(MyKnowledgeActivity.this,
									// listKnowledge.get(0).toJTFile());
									// }
									// else{
									/**
									 * String mSuffixName = mKnowledge2.toJTFile().getmSuffixName();
							mKnowledge2.setDesc(filterHtml(mSuffixName));
							JTFile jtFile = mKnowledge2.toJTFile();
							jtFile.mFileName = "[知识] "+mKnowledge2.getTitle();
							jtFile.virtual = mKnowledge2.getType()+"";
									 */
										String mSuffixName = knowledge.toJTFile().getmSuffixName();
										knowledge.setDesc(filterHtml(mSuffixName));
										JTFile jtFile = knowledge.toJTFile();
										jtFile.messageID=EUtil.genMessageID();
//										jtFile.mFileName ="分享了[知识] ";//+knowledge.getTitle();
										jtFile.mFileName =knowledge.getTitle();
										jtFile.virtual = knowledge.getType()+"";
									// ENavigate.startShareActivity(MyKnowledgeActivity.this,
									// JTFile.TYPE_KNOWLEDGE2, listJtFile);
									ENavigate.startSocialShareActivity(MyKnowledgeActivity.this, jtFile);
									// }
							
						}
					});
					biaoqianLl.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							popupWindow.dismiss();
							ArrayList<Long> listKnowledgeId=  new ArrayList<Long>();
							if (deleteSourceType == TYPE_SHARE_TO_ME) {
								listKnowledgeId.add(knowledge.shareMeId);
							} else {
								listKnowledgeId.add(knowledge.id);
							}
							ArrayList<Integer> listKnowledgeType = new ArrayList<Integer>();
							listKnowledgeType.add(knowledge.type);
							if(knowledgeCheckedList!=null)
								knowledgeCheckedList.clear();
							knowledgeCheckedList.add(knowledge);
//								ENavigate.startEditKnowledgeTagActivityForResult(MyKnowledgeActivity.this, REQUESTCODE_EDIT_KNOWLEDGE_TAG_ACTIVITY, listKnowledgeId, listKnowledgeType);
								ENavigate.startEditKnowledgeTagActivityForResult(MyKnowledgeActivity.this, REQUESTCODE_EDIT_KNOWLEDGE_TAG_ACTIVITY, knowledgeCheckedList);
						}
					});
					shanchuLl.setOnClickListener(new OnClickListener() {
	
						@Override
						public void onClick(View v) {
							popupWindow.dismiss();
								Builder builder = new AlertDialog.Builder(context);
								builder.setTitle("提示：");
								builder.setMessage("确定要删除知识吗？");
								builder.setNegativeButton("取消", null);
								builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
									

									@Override
									public void onClick(DialogInterface dialog, int which) {
										showLoadingDialog();
										// swipeLayout.setRefreshing(true);
										ArrayList<Long> listKnowledgeId=  new ArrayList<Long>();
										if (deleteSourceType == TYPE_SHARE_TO_ME) {
											listKnowledgeId.add(knowledge.shareMeId);
										} else {
											listKnowledgeId.add(knowledge.id);
										}
										ArrayList<Integer> listKnowledgeType = new ArrayList<Integer>();
										listKnowledgeType.add(knowledge.type);
										DeleteKnowledge = knowledge;
										KnowledgeReqUtil.doDeleteKnowledgeById(context, MyKnowledgeActivity.this, App.getUserID(), listKnowledgeId, listKnowledgeType,deleteSourceType, null);
									}
								});
								builder.show();
						}
					});
				}
			});
			
			if (knowledge.tag.equals("")) {
				viewHolder.tagTv.setVisibility(View.GONE);
			}else{
				viewHolder.tagTv.setVisibility(View.VISIBLE);
				viewHolder.tagTv.setText(knowledge.tag);
			}

//			ArrayList<String> lsitTag = knowledge.listTag;// 标签集合
//			if (null != lsitTag && !lsitTag.isEmpty() /*&& type != TYPE_ALL*/) {// 在我创建的下不显示标签:::
//				viewHolder.tagTv.setVisibility(View.VISIBLE);
//				viewHolder.tagTv.setText(lsitTag.toString());
//			} else {
//				viewHolder.tagTv.setVisibility(View.GONE);
//				viewHolder.cb.setGravity(Gravity.CENTER_VERTICAL);
//			}

			if (editMode) {
				viewHolder.cb.setVisibility(View.VISIBLE);
//				viewHolder.numTv.setVisibility(View.GONE);
				if (checkedHm.get(knowledge) != null) {
					boolean b = checkedHm.get(knowledge);
					viewHolder.cb.setChecked(b);
				} else {
					viewHolder.cb.setChecked(false);
				}
			} else {
				viewHolder.cb.setVisibility(View.GONE);
//				viewHolder.numTv.setVisibility(View.VISIBLE);
//				viewHolder.numTv.setText(position + 1 + "");
			}

			viewHolder.cb.setTag(knowledge);
			viewHolder.titleTv.setText(knowledge.title);
			
			viewHolder.timeTv.setText(TextUtils.isEmpty(knowledge.modifytime)?"":TimeUtil.TimeFormat(knowledge.modifytime));
			if(TextUtils.isEmpty(knowledge.pic)){
				viewHolder.knowledgeIv.setVisibility(View.GONE);
			}else{
//				viewHolder.knowledgeIv.setVisibility(View.VISIBLE);
				viewHolder.knowledgeIv.setVisibility(View.GONE);
				ImageLoader.getInstance().displayImage( knowledge.pic, viewHolder.knowledgeIv, LoadImage.mHomeDefaultHead);
			}

			// TagGvAdper tagGvAdaper = new TagGvAdper(context);
			// viewHolder.tagLl.setAdapter(tagGvAdaper);
			// tagGvAdaper.setTagList(lsitTag);
			// tagGvAdaper.notifyDataSetChanged();

			return convertView;
		}

		class ViewHolder {
			public TextView tagTv;
			public ImageView moreIv;
			CheckBox cb;
			TextView numTv;
			TextView titleTv;
			TextView timeTv;
			ImageView knowledgeIv;
		}

	}

	// android:layout_width="wrap_content"
	// android:layout_height="wrap_content"
	// android:layout_gravity="center"
	// android:ellipsize="end"
	// android:padding="1dp"
	private TextView newTagTv(String tag) {
		TextView tagTv = new TextView(context);
		tagTv.setTextSize(12);
		tagTv.setTextColor(R.color.text_gray);
		tagTv.setSingleLine(true);
		tagTv.setPadding(8, 0, 8, 0);
		tagTv.setBackgroundResource(R.drawable.taggv_item_bg_shape);
		tagTv.setGravity(Gravity.CENTER);
		tagTv.setText(tag);
		return tagTv;
	}

	/**
	 * 通知父布局，占用的宽，高；
	 * 
	 * @param view
	 */
	private void measureView(View view) {
		// ViewGroup.LayoutParams p = view.getLayoutParams();
		// if (p == null) {
		// p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
		// ViewGroup.LayoutParams.WRAP_CONTENT);
		// }
		// int width = ViewGroup.getChildMeasureSpec(0, 0, p.width);
		// int height;
		// int tempHeight = p.height;
		// if (tempHeight > 0) {
		// height = MeasureSpec.makeMeasureSpec(tempHeight,
		// MeasureSpec.EXACTLY);
		// } else {
		// height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		// }
		int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		view.measure(w, h);
	}

	// 触发下拉刷新
	public void onRefresh() {
		if (mState == STATE_NORMAL) {
			if (startGetData(0))
				this.mState = STATE_REFRESH;
			else
				stopLoading();
		}
	}

	/**
	 * 获取页数据
	 */
	public boolean startGetData(int pageIndex) {
		if (pageIndex == 0) {
//			swipeLayout.setRefreshing(true);
			mState = STATE_REFRESH;
		}

		if (type == TYPE_TAG) {
			KnowledgeReqUtil.doGetKnowledgeByTagAndKeyword(context, this, App.getUserID(), knoTagName, mCurKeyword, pageIndex, 20, null);
		} else if (type == TYPE_CATEGORY) {
			KnowledgeReqUtil.doGetKnowledgeByUserCategoryAndKeyword(context, this, App.getUserID(), category.getId(), mCurKeyword, pageIndex, 20, null);
		} else {
			// 根据类型（全部、我收藏的、分享给我的、我创建的）和关键字分页获取知识列表
			KnowledgeReqUtil.doGetKnowledgeByTypeAndKeyword(context, this, App.getUserID(), type, mCurKeyword, pageIndex, 20, null);
		}
		return true;
	}

	public void stopLoading() {
		mState = STATE_NORMAL;
//		swipeLayout.setRefreshing(false);
		mvProgressBar.setVisibility(View.GONE);

		if (!hasMore()) {
			mvTextView.setText("没有更多");
			moreView.setClickable(false);
		} else {
			mvTextView.setText("更多");
			moreView.setClickable(true);
		}
	}

	/**
	 * 判断分页有没有更多
	 * 
	 * @return
	 */
	public boolean hasMore() {
		if (index != -1 && (index + 1) * size >= total)
			return false;
		else
			return true;
	}

	private void loadMoreData() { // 加载更多数据
		if (!hasMore())
			return;

//		if (mState == STATE_NORMAL) {
//			mvProgressBar.setVisibility(View.VISIBLE);
//			mvTextView.setText("正在加载");
//			this.mState = STATE_GETMORE;
			int nowIndex = 0;
			if (index != -1){
				nowIndex = index + 1;
			}
			startGetData(nowIndex);
//			if (!startGetData(nowIndex))
//				stopLoading();
//		}
	}

	@Override
	public void bindData(int tag, Object object) {
		knowledgeListLv.stopLoadMore();
		knowledgeListLv.stopRefresh();
		dismissLoadingDialog();
		if (object == null) {
			stopLoading();
			return;
		}

		if (mState == STATE_REFRESH) {
			if (knowledgeList != null) {
				knowledgeList.clear(); // 如果当前刷新中，则清除之前数据
			}
		}
		// 根据 类型/目录返回/标签返回 和 关键字获得知识
		if (KnoReqType.GetKnowledgeByTypeAndKeyword == tag || KnoReqType.GetKnowledgeByTagAndKeyword == tag || KnoReqType.GetKnowledgeByUserCategoryAndKeyword == tag) {

			Map<String, Object> hm = (Map<String, Object>) object;
			ArrayList<KnowledgeMini2> newKnowledgeList = (ArrayList<KnowledgeMini2>) hm.get("listKnowledgeMini");
			if (newKnowledgeList != null) {

				total = (Integer) hm.get("total");
				index = (Integer) hm.get("index");
				size = (Integer) hm.get("size");
				if(newKnowledgeList.size() < 20){
					knowledgeListLv.setPullLoadEnable(false);
				}else{
					knowledgeListLv.setPullLoadEnable(true);
				}

				knowledgeList.addAll(newKnowledgeList);
				knowledgeLvAdapter.setKnowledgeList(knowledgeList);
				knowledgeLvAdapter.notifyDataSetChanged();
				// reSetActionBarTitle();
			}
		}
		// 根据id删除知识
		else if (KnoReqType.DeleteKnowledgeById == tag) {

			Map<String, Object> hm = (Map<String, Object>) object;
			boolean b = (Boolean) hm.get("success");
			if (b) {
				// TODO:
				knowledgeList.removeAll(traversalHMap(checkedHm));
				if (DeleteKnowledge!=null) {
					knowledgeList.remove(DeleteKnowledge);
				}
				checkedHm.clear();
				checkAll = false;
				reSetCheckedAllstate();
				knowledgeLvAdapter.notifyDataSetChanged();
				Toast.makeText(context, "删除成功", 0).show();
			} else {
				Toast.makeText(context, "删除失败", 0).show();
			}
		}

		count = knowledgeList.size();
		stopLoading();
	}

//	@Override
//	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//		lastItem = firstVisibleItem + visibleItemCount - 1; // 减1是因为上面加了个addFooterView
//	}
//
//	@Override
//	public void onScrollStateChanged(AbsListView view, int scrollState) {
//		// 下拉到空闲是，且最后一个item的数等于数据的总数时，进行更新
//		if (lastItem == count && scrollState == this.SCROLL_STATE_IDLE) {
//			Log.i(TAG, "拉到最底部");
//			moreView.setVisibility(view.VISIBLE);
//			loadMoreData();
//		}
//	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (editMode) {
				editMode = false;
				reSetActionBarTitle();
				reSetEditModeLayout();
				reSetLayout();
			} else if (type == TYPE_CATEGORY) {
//				ENavigate.startGlobalKnowledgeCategoryActivityForResult(MyKnowledgeActivity.this, REQUESTCODE_GLOBAL_KNOWLEDGE_CATEGORY_ACTIVITY);
				ENavigate.startKnowledgeCategoryActivityForResult(MyKnowledgeActivity.this, REQUESTCODE_GLOBAL_KNOWLEDGE_CATEGORY_ACTIVITY,null,false,titleTv.getText().toString());
			} else if (type == TYPE_TAG) {
				ENavigate.startGlobalKnowledgeTagActivityForResult(MyKnowledgeActivity.this, REQUESTCODE_GLOBAL_KNOWLEDGE_TAG_ACTIVITY,OperateType.ClickBack);
			} else {
				return super.onKeyDown(keyCode, event);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String MSG = "onActivityResult()";

		if (REQUESTCODE_CREATE_KNOWLEDGE_ACTIVITY == requestCode) {

			Log.i(TAG, MSG + " REQUESTCODE_CREATE_KNOWLEDGE_ACTIVITY");
			if (Activity.RESULT_OK == resultCode) {// 创建完成
				KnowledgeMini2 knowledgeMini2 = (KnowledgeMini2) data.getSerializableExtra(EConsts.Key.KNOWLEDGE_MINI2);// 获取到新创建的知识对象，插入到
				if (type == TYPE_CREATE) {
					knowledgeList.add(0, knowledgeMini2);
					knowledgeLvAdapter.notifyDataSetChanged();
				}
			}

		}
		if (REQUESTCODE_EDIT_KNOWLEDGE_TAG_ACTIVITY == requestCode) {
			Log.i(TAG, MSG + " REQUESTCODE_EDIT_KNOWLEDGE_TAG_ACTIVITY");
			if (Activity.RESULT_OK == resultCode) {
				// 编辑标签完成，刷新界面
				checkAll = false;
				reSetCheckedAllstate();
				// knowledgeLvAdapter.notifyDataSetChanged();
				onRefresh();
			}

		}
		if (REQUESTCODE_GLOBAL_KNOWLEDGE_CATEGORY_ACTIVITY == requestCode) {
			Log.i(TAG, MSG + " REQUESTCODE_GLOBAL_KNOWLEDGE_CATEGORY_ACTIVITY");
			if (Activity.RESULT_OK == resultCode) {
				// 获取相应的目录
				type = TYPE_CATEGORY;
				category = (UserCategory) data.getSerializableExtra(EConsts.Key.KNOWLEDGE_CATEGORY);
				clearResponseData();
				reSetActionBarTitle();
				reSetLayout();
				onRefresh();
			} else {
				type = TYPE_ALL;
				clearResponseData();
				reSetActionBarTitle();
				reSetLayout();
				onRefresh();
			}

		}
		if (REQUESTCODE_GLOBAL_KNOWLEDGE_TAG_ACTIVITY == requestCode) {
			Log.i(TAG, MSG + " REQUESTCODE_GLOBAL_KNOWLEDGE_TAG_ACTIVITY");
			if (Activity.RESULT_OK == resultCode) {
				type = TYPE_TAG;
				knoTagName = data.getStringExtra("tag");
				clearResponseData();
				reSetActionBarTitle();
				reSetLayout();
				onRefresh();
			} else {
				type = TYPE_ALL;
				clearResponseData();
				reSetActionBarTitle();
				reSetLayout();
				onRefresh();
			}

		}

	}

	@Override
	public void onResume() {
		if(!firstIn){
			clearResponseData();
			reSetActionBarTitle();
			reSetLayout();
			onRefresh();
		}
		if (editMode) {
			reSetEditModeLayout();
		}
		firstIn = false;
		super.onResume();
	}

}
