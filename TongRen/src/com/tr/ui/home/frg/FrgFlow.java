package com.tr.ui.home.frg;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.App;
import com.tr.R;
import com.tr.api.ConnectionsReqUtil;
import com.tr.api.HomeReqUtil;
import com.tr.image.ImageLoader;
import com.tr.model.home.MGetDynamic;
import com.tr.model.obj.DynamicComment;
import com.tr.model.obj.DynamicNews;
import com.tr.model.obj.DynamicPraise;
import com.tr.model.page.JTPage;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.adapter.PageViewAdpter;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.connections.revision20150122.detail.RelationHomeActivity;
import com.tr.ui.connections.viewfrg.BaseViewPagerFragment;
import com.tr.ui.demand.util.ChooseDataUtil;
import com.tr.ui.home.DynamicCommentActivity;
import com.tr.ui.home.IndexDynamicOnClickLister;
import com.tr.ui.home.MainActivity;
import com.tr.ui.organization.create_clientele.ClientDetailsActivity;
import com.tr.ui.organization.orgdetails.OrgFinancingActivity;
import com.tr.ui.organization.orgdetails.OrgMyHomePageActivity;
import com.tr.ui.people.contactsdetails.ContactsDetailsActivity;
import com.tr.ui.search.SearchActivity;
import com.tr.ui.widgets.BasicListView;
import com.tr.ui.widgets.CommonSmileyParser;
import com.tr.ui.widgets.CustomRelativeLayout;
import com.tr.ui.widgets.CustomRelativeLayout.OnFlyingListener;
import com.tr.ui.widgets.KnoTagGroupView;
import com.tr.ui.widgets.SmileyParser;
import com.tr.ui.widgets.SmileyView;
import com.tr.ui.widgets.floatmeune.FloatingActionMenu;
import com.tr.ui.widgets.viewpagerheaderscroll.delegate.AbsListViewDelegate;
import com.utils.common.EUtil;
import com.utils.common.Util;
import com.utils.display.DisplayUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.image.AnimateFirstDisplayListener;
import com.utils.image.LoadImage;
import com.utils.string.StringUtils;
import com.utils.time.TimeUtil;

/**
 * @ClassName: FrgFlow.java
 * @Description: 动态页面
 * @author xuxinjian
 * @version V2.0
 * @Date 2014-3-26 下午5:03:36
 */

public class FrgFlow extends BaseViewPagerFragment implements IBindData, View.OnClickListener, OnFlyingListener, OnScrollListener, OnTouchListener, IXListViewListener {
	public static final String TAG = "FrgFlow";
	private static final String LEFTSPECCHAR = ((char) 0X1B) + "";
	private static final String RIGHTSPECCHAR = ((char) 0X11) + "";
	/* 动态展示XListView */
	private XListView mListView;
	/* 动态集合 */
	private List<DynamicNews> mlistDynamicNews = new ArrayList<DynamicNews>();
	/* 动态适配器 */
	private DynamicAdapter mAdapterDynamic;
	/* 分页JTPage */
	private JTPage mPage;
	/* imageload设置参数 */
	/* 文本输入框 */
	private LinearLayout mHomeInputLL;
	/* 发送按钮 */
	private ImageView mHomeFrgSendCommentIv;
	/* 回复文本输入框 */
	private EditText mHomeFrgCommentEt;
	/* 底部导航 */
	private LinearLayout mBottomLL;
	/* true:显示SmileyView；false 显示软键盘 */
	private Boolean isShowface = false;
	/* 表情容器 */
	private LinearLayout mIndustrySmileyLL;
	/* 表情ViewPager */
	private ViewPager mFaceViewPager;
	/* 小图标 */
	private ImageView mSmileyPagerchange;
	private ImageView mHomeFrgPicIv;
	private Activity mActivity;
	private FlowType flowType;
	private IndexDynamicOnClickLister myOnClickListener;
	private FrameLayout mGlobalCreatButton;
	private FloatingActionMenu itemMenu;
	private long mUserID;
	private long mOtherUserID = -1;
	private static RelationHomeActivity mRelationHomeActivity;
	private static ContactsDetailsActivity mContactsDetailsActivity;
	private static OrgMyHomePageActivity mOrgMyHomePageActivity;
	private static ClientDetailsActivity mClient_DetailsActivity;
	private int mIndex;
	private LinearLayout mInletDynamicRemind;
	private DynamicCommentAdapter commentAdapter;
	private DynamicComment newDynamicComment;// 新回复
	private DynamicPraise mDynamicPraise;// 动态赞同对象
	private Context mContext;
	private App mMainApp;
	// 解决头像错位
	private AnimateFirstDisplayListener animateFirstDisplayListener = new AnimateFirstDisplayListener();

	// popupwindow VIew
	private View inflateDeleteView;
	private TextView deleteTv;
	private TextView copyTv;
	private PopupWindow window;

	private int measuredHeigh;
	private int measuredWidth;
	private int linmitHeight;
	private SmileyParser parser;
	private boolean mIsVisibleToUser = false;

	/** 首页动态筛选——all:首页；flow:动态；gintong:金桐脑 */
	public enum FlowSelectType {
		all, flow, gintong
	}

	private FlowSelectType selectType;

	public FlowSelectType getSelectType() {
		return selectType;
	}

	/**
	 * 首页动态的构造函数
	 * 
	 * @param BottomLL
	 *            底部布局
	 * @param activity
	 *            父activity
	 * @param mGlobalCreatButton
	 *            万能键中心按钮
	 * @param itemMenu
	 *            万能键浮动子按钮
	 * @param inletDynamicRemind
	 *            动态提示红点
	 */
	public FrgFlow(LinearLayout BottomLL, Activity activity, FrameLayout mGlobalCreatButton, FloatingActionMenu itemMenu, LinearLayout inletDynamicRemind) {
		FrgFlow fragment = new FrgFlow();
		Bundle args = new Bundle();
		args.putInt(BUNDLE_FRAGMENT_INDEX, 0);
		fragment.setArguments(args);
		this.mBottomLL = BottomLL;
		mActivity = activity;
		this.mGlobalCreatButton = mGlobalCreatButton;
		this.itemMenu = itemMenu;
		this.mInletDynamicRemind = inletDynamicRemind;
	}

	/**
	 * 个人详情页构造函数
	 * 
	 * @param index
	 *            当前frgment在viewpage的索引
	 * @param RelationHomeActivity
	 *            父activity
	 * @param otherUserID
	 *            用户id
	 */
	public FrgFlow(int index, RelationHomeActivity RelationHomeActivity, long otherUserID) {
		FrgFlow fragment = new FrgFlow();
		Bundle args = new Bundle();
		args.putInt(BUNDLE_FRAGMENT_INDEX, index);
		fragment.setArguments(args);
		mRelationHomeActivity = RelationHomeActivity;
		this.mOtherUserID = otherUserID;
	}

	// 人脉详情
	public FrgFlow(int index, ContactsDetailsActivity contactsDetailsActivity, long otherUserID) {
		FrgFlow fragment = new FrgFlow();
		Bundle args = new Bundle();
		args.putInt(BUNDLE_FRAGMENT_INDEX, index);
		fragment.setArguments(args);
		mContactsDetailsActivity = contactsDetailsActivity;
		this.mOtherUserID = otherUserID;
	}

	// 组织详情中的他人主页/我的主页
	public FrgFlow(int index, OrgMyHomePageActivity orgMyHomePageActivity, long otherUserID) {
		FrgFlow fragment = new FrgFlow();
		Bundle args = new Bundle();
		args.putInt(BUNDLE_FRAGMENT_INDEX, index);
		fragment.setArguments(args);
		mOrgMyHomePageActivity = orgMyHomePageActivity;
		this.mOtherUserID = otherUserID;
	}

	// 客户详情
	public FrgFlow(int index, ClientDetailsActivity client_DetailsActivity, long otherUserID) {
		FrgFlow fragment = new FrgFlow();
		Bundle args = new Bundle();
		args.putInt(BUNDLE_FRAGMENT_INDEX, index);
		fragment.setArguments(args);
		mClient_DetailsActivity = client_DetailsActivity;
		this.mOtherUserID = otherUserID;
	}

	public FrgFlow() {
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mContext = activity;
		getActivity();
	}

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = null;
		try {
			mMainApp = App.getApp();
			initFlowType();
			if (flowType.equals(FlowType.MainFlow)) {
				view = inflater.inflate(R.layout.home_frg_flow, container, false);
				mainRootView = (CustomRelativeLayout) view.findViewById(R.id.home_root);
				mainRootView.setOnFlyingListener(this);
			} else if (flowType.equals(FlowType.HomePageFlow)) {// 我的主页
				view = inflater.inflate(R.layout.home_page_frg_flow, container, false);
				root = (RelativeLayout) view.findViewById(R.id.my_home_root);
			} else if (flowType.equals(FlowType.OtherPeopleFlow)) {// 他人及好友主页
				view = inflater.inflate(R.layout.home_other_page_frg_flow, container, false);
				root = (RelativeLayout) view.findViewById(R.id.other_home_root);
			}
			parser = SmileyParser.getInstance(getActivity());
			mHomeInputLL = (LinearLayout) view.findViewById(R.id.homeFrgInputLl);
			mHomeFrgCommentEt = (EditText) view.findViewById(R.id.homeFrgCommentEt);
			mHomeFrgSendCommentIv = (ImageView) view.findViewById(R.id.homeFrgSendCommentIv);
			mIndustrySmileyLL = (LinearLayout) view.findViewById(R.id.industrySmileyLL);
			mFaceViewPager = (ViewPager) view.findViewById(R.id.industrySmileyPager);
			mSmileyPagerchange = ((ImageView) view.findViewById(R.id.smileyPagerchange));
			mHomeFrgPicIv = (ImageView) view.findViewById(R.id.homeFrgPicIv);
			setHasOptionsMenu(true);
			mListView = (XListView) view.findViewById(R.id.home_frg_flow_listview);
			initListViewConfig();
			initSetListener();
			initExpression(view);
			showBottomNormalView();
			startGetData();
			initPopUpWindow();
			return view;
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			return view;
		}
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if(inflateDeleteView != null){
			inflateDeleteView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			measuredWidth = inflateDeleteView.getMeasuredWidth();
			measuredHeigh = inflateDeleteView.getMeasuredHeight();
		}
		Rect frame = new Rect();
		getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		int actionBarHeight = getActivity().getActionBar().getHeight();
		linmitHeight = statusBarHeight + actionBarHeight;
		try {
			initFlowType();// 初始化参数
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 设置点击、触摸、滑动等事件的监听 */
	private void initSetListener() {
		mHomeFrgPicIv.setOnClickListener(this);
		mListView.setOnTouchListener(this);
		mListView.setOnTouchListener(this);
		mListView.setOnScrollListener(this);
		mHomeFrgCommentEt.setOnTouchListener(this);
	}

	/** 初始化Xlistview的配置 */
	private void initListViewConfig() {
		mListView.showFooterView(false);
		mListView.setPullLoadEnable(true);
		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(this);
		mAdapterDynamic = new DynamicAdapter(getParentActivity());
		mAdapterDynamic.setData(mlistDynamicNews);
		mListView.setAdapter(mAdapterDynamic);
	}

	/** 初始化评论删除拷贝的 popupwindow */
	private void initPopUpWindow() {
		inflateDeleteView = View.inflate(getActivity(), R.layout.layout_sociality_delete, null);
		this.window = new PopupWindow(inflateDeleteView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		window.setOutsideTouchable(true);
		deleteTv = (TextView) inflateDeleteView.findViewById(R.id.delete);
		copyTv = (TextView) inflateDeleteView.findViewById(R.id.save);
	}

	/** 设置当前的筛选状态 */
	public void setSelectType(FlowSelectType selectType, boolean needRefresh) {
		try {
			this.selectType = selectType;
			if (needRefresh) {
				initFlowType();// 初始化参数
				if (needRefresh) {
					HomeReqUtil.getDynamicNewList(getParentActivity(), FrgFlow.this, null, 0, 20);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 初始化表情
	 */
	private void initExpression(View view) {
		// 表情切换界面
		mFaceViewPager.setAdapter(new PageViewAdpter(getParentActivity(), smileyViewClickListener));
		mSmileyPagerchange = ((ImageView) view.findViewById(R.id.smileyPagerchange));
		mFaceViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:
					mSmileyPagerchange.setImageResource(R.drawable.chat_biaoqing_1);
					break;
				case 1:
					mSmileyPagerchange.setImageResource(R.drawable.chat_biaoqing_2);
					break;
				case 2:
					mSmileyPagerchange.setImageResource(R.drawable.chat_biaoqing_3);
					break;
				case 3:
					mSmileyPagerchange.setImageResource(R.drawable.chat_biaoqing_4);
					break;
				case 4:
					mSmileyPagerchange.setImageResource(R.drawable.chat_biaoqing_5);
					break;
				case 5:
					mSmileyPagerchange.setImageResource(R.drawable.chat_biaoqing_6);
					break;
				case 6:
					mSmileyPagerchange.setImageResource(R.drawable.chat_biaoqing_7);
					break;
				case 7:
					mSmileyPagerchange.setImageResource(R.drawable.chat_biaoqing_8);
					break;
				default:
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	/**
	 * 表情点击事件
	 */
	private SmileyView.OnItemClickListener smileyViewClickListener = new SmileyView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg) {
			final CommonSmileyParser parser = CommonSmileyParser.getInstance(getParentActivity());
			if (position == SmileyView.MaxSmileyNumber) { // 删除表情
				String text = mHomeFrgCommentEt.getText().toString();
				if (text.length() > 0) {
					if (text.lastIndexOf(RIGHTSPECCHAR) == text.length() - 1) {
						text = text.substring(0, text.lastIndexOf(LEFTSPECCHAR));
					} else {
						text = text.substring(0, text.length() - 1);
					}
					mHomeFrgCommentEt.setText(text);
					mHomeFrgCommentEt.setSelection(text.length());
				}
				return;
			}
			final String text = mHomeFrgCommentEt.getText().toString() + LEFTSPECCHAR + parser.getmSmileyTexts()[(int) arg] + RIGHTSPECCHAR;
			mHomeFrgCommentEt.setText(text);
			mHomeFrgCommentEt.setSelection(text.length());
		}
	};

	/**
	 * 不同页面显示的动态数据
	 * 
	 * @author CJJ
	 */
	public enum FlowType {
		MainFlow(1, "主页面动态"), OtherPeopleFlow(2, "其他人主页动态"), HomePageFlow(3, "自己主页动态");
		private int flag;
		private String des;

		private FlowType(int flag, String des) {
			this.flag = flag;
			this.des = des;
		}
	}

	private void initFlowType() throws Exception {
		if (!TextUtils.isEmpty(App.getUserID())) {
			mUserID = Integer.valueOf(App.getUserID());
		} else {
			mUserID = 0;
		}
		if (mParentActivity instanceof MainActivity || mParentActivity instanceof SearchActivity ||  mParentActivity == null) {
			flowType = FlowType.MainFlow;// 主页动态
			// 首页
			switch (selectType) {
			case all:
				flowType.flag = 1;
				break;
			case flow:
				flowType.flag = 2;
				mUserID = -9;
				break;
			case gintong:
				flowType.flag = 2;
				mUserID = -10;
				break;
			}
		} else if (mOtherUserID != mUserID) {/* 其他人的主页 */// 他人主页动态
			flowType = FlowType.OtherPeopleFlow;
			mUserID = mOtherUserID;
		} else {
			flowType = FlowType.HomePageFlow;// 我的主页动态
		}
	}

	/** 底部显示普通的布局 */
	public void showBottomNormalView() {
		if (null != mBottomLL) {
			mBottomLL.setVisibility(View.VISIBLE);
		}
		if (null != mGlobalCreatButton) {
			mGlobalCreatButton.setVisibility(View.VISIBLE);
		}
		if (null != mRelationHomeActivity && null != mRelationHomeActivity.getOnlineTContact()) {
			mRelationHomeActivity.initBottomView(mRelationHomeActivity.getPerson());
		}
		if (null != mHomeInputLL) {
			mHomeInputLL.setVisibility(View.GONE);
		}
		hideKeyboard();
		if (mainRootView != null && flowType.equals(FlowType.MainFlow)) {
			mainRootView.setNeedInterceptTouch(true);
		}
	}

	/** 底部显示评论的布局 */
	public void showBottomCommentView() {
		if (null != mBottomLL) {
			mBottomLL.setVisibility(View.GONE);
		}
		if (null != mGlobalCreatButton) {
			if (itemMenu.isOpen()) {
				itemMenu.close(true);
			}
			mGlobalCreatButton.setVisibility(View.GONE);
		}
		if (null != mRelationHomeActivity) {
			mRelationHomeActivity.hideAddFriendOrSendMessageBottomView();
		}
		if (null != mHomeInputLL) {
			mHomeInputLL.setVisibility(View.VISIBLE);
		}
		mIndustrySmileyLL.setVisibility(View.GONE);
		isShowface = false;
		editTextCaptureFocus();
		showKeyboard();
		if (mainRootView != null && flowType.equals(FlowType.MainFlow)) {
			mainRootView.setNeedInterceptTouch(false);
		}
	}

	private void editTextCaptureFocus() {
		mHomeFrgCommentEt.setFocusable(true);
		mHomeFrgCommentEt.setFocusableInTouchMode(true);
		mHomeFrgCommentEt.requestFocus();
	}

	/** 隐藏软盘 */
	public void hideKeyboard() {
		try {
			InputMethodManager m = (InputMethodManager) getParentActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			m.hideSoftInputFromWindow(mHomeFrgCommentEt.getApplicationWindowToken(), 0);
		} catch (Exception e) {
		}
	}

	/** 显示软键盘 */
	public void showKeyboard() {
		try {
			InputMethodManager manager = (InputMethodManager) getParentActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			manager.toggleSoftInputFromWindow(mHomeFrgCommentEt.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		showBottomNormalView();
		if (mIsVisibleToUser) {
			JSONObject jb = new JSONObject();
			ConnectionsReqUtil.getNewDynamicCount(getParentActivity(), this, jb, null);
		}
		if (mListView == null && mAdapterDynamic == null) {
		}
	}

	private void showEmptyView(int resid) {
		if (mParentActivity instanceof MainActivity)
			mainRootView.setBackgroundResource(resid);
		else {
			root.setBackgroundResource(resid);
		}
	}

	@Override
	public void bindData(int tag, Object object) {
		try {
			mListView.stopLoadMore();
			mListView.stopRefresh();
			dismissLoadingDialog();
			// 如果页面已暂停，丢弃数据
			if (tag == EAPIConsts.HomeReqType.HOME_REQ_GET_DYNAMIC) {
				if (this.isResumed()) {
					MGetDynamic mGetDynamic = (MGetDynamic) object;
					if (mGetDynamic != null && mGetDynamic.getJtPage() != null && mGetDynamic.getJtPage().getTotal() != 0) {
						mPage = mGetDynamic.getJtPage();
						if (null != mPage && mPage.getIndex() == 0) {
							mlistDynamicNews.clear();
						}
						if ((mPage != null) && (mPage.getLists() != null)) {
							if (mPage.getLists().size() < 1) {
								mAdapterDynamic.setData(mlistDynamicNews);
								mAdapterDynamic.notifyDataSetChanged();
								mListView.setPullLoadEnable(false);
								if (mPage.getIndex() == 0 || mlistDynamicNews.size() < 1) {
									showEmptyView(R.drawable.empty);
								} else {
									showEmptyView(R.color.white);
								}
								return;
							}
							for (int i = 0; i < mPage.getLists().size(); i++) {
								mlistDynamicNews.add((DynamicNews) mGetDynamic.getJtPage().getLists().get(i));
							}
							mAdapterDynamic.setData(mlistDynamicNews);
							mAdapterDynamic.notifyDataSetChanged();
							controlXListBottom();
						}
						mListView.setVisibility(View.VISIBLE);
					} else {
						mlistDynamicNews.clear();
						mAdapterDynamic.setData(mlistDynamicNews);
						mAdapterDynamic.notifyDataSetChanged();
					}
					if (mlistDynamicNews.isEmpty()) {
						// 当动态没数据时显示提示图片
						mListView.setVisibility(View.INVISIBLE);
						showEmptyView(R.drawable.empty);
						mListView.setPullLoadEnable(false);
					} else {
						mListView.setPullLoadEnable(true);
						mListView.setVisibility(View.VISIBLE);
						showEmptyView(R.color.white);
					}
				}

			}

			else if (tag == EAPIConsts.HomeReqType.HOME_REQ_GET_FLOW) {
				// 评论发表成功
				if (isResumed())
					Toast.makeText(getParentActivity(), "测试数据...", 0).show();
			} else if (tag == EAPIConsts.concReqType.im_getNewDynamicCount) {
				if (getParentActivity() != null) {
					if (getParentActivity() instanceof MainActivity) {
						((MainActivity) getParentActivity()).doNewflow(object);
					}
					if (getParentActivity() instanceof RelationHomeActivity) {
						((RelationHomeActivity) getParentActivity()).doNewflow(object);
					}
					return;
				}
			} else if (tag == EAPIConsts.HomeReqType.HOME_REQ_ADD_DYNAMIC_COMMENT) {
				dismissLoadingDialog();
				if (object != null) {
					Long commentid = (Long) object;
					newDynamicComment.setId(commentid);
					mlistDynamicNews.get(mIndex).insertCommentId(commentid);
				}

				mlistDynamicNews.get(mIndex).setCount(mlistDynamicNews.get(mIndex).getCount() + 1);
				mAdapterDynamic.notifyDataSetChanged();
				showBottomNormalView();
			}
			// 赞
			else if (tag == EAPIConsts.HomeReqType.HOME_REQ_ADD_DYNAMIC_PRAISE) {
				if (object != null) {
					long id = (Long) object;
					if (id != 0) {// 点赞成功，插入数据
						TextView tv = (TextView) mListView.findViewWithTag(mlistDynamicNews.get(mIndex).getId());
						if (tv != null) {
							Drawable drawable = getResources().getDrawable(R.drawable.heart_pressed);
							drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
							tv.setCompoundDrawables(drawable, null, null, null);
						}
						mDynamicPraise.setId(id);
						mlistDynamicNews.get(mIndex).getmDynamicPraiseList().add(0, mDynamicPraise);
						mAdapterDynamic.notifyDataSetChanged();
					} else {
						// 失败
						Toast.makeText(mActivity, "失败", 0);
					}
				}
				mIndex = 0;
				mDynamicPraise = null;
			} else if (tag == EAPIConsts.HomeReqType.HOME_REQ_CANCEL_DYNAMIC_PRAISE) {
				if (object != null) {
					String succeed = (String) object;
					if ("true".equals(succeed)) {
						TextView tv = (TextView) mListView.findViewWithTag(mlistDynamicNews.get(mIndex).getId());
						if (tv != null) {
							Drawable drawable = getResources().getDrawable(R.drawable.heart);
							drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
							tv.setCompoundDrawables(drawable, null, null, null);
							mAdapterDynamic.notifyDataSetChanged();
						}
					} else {
						Toast.makeText(mActivity, "失败", 0);
					}
				}
				mIndex = 0;
				mDynamicPraise = null;
			}
			dismissLoadingDialog();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void hideDynamicRemind() {
		if (mInletDynamicRemind != null && mMainApp.getAppData().getmFrgFlowCount() != mlistDynamicNews.size()) {
			mInletDynamicRemind.setVisibility(View.VISIBLE);
		} else if (mInletDynamicRemind != null) {
			mInletDynamicRemind.setVisibility(View.GONE);
		}
	}

	/**
	 * 控制XListViewButtom
	 */
	private void controlXListBottom() {
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

	/**
	 * 获取页数据
	 */
	public void startGetData() {
		int nowIndex = 0;
		if (mPage != null) {
			nowIndex = mPage.getIndex() + 1;
		} else {
			// showLoadingDialog();
		}
		HomeReqUtil.getDynamicNewList(getParentActivity(), FrgFlow.this, null, nowIndex, 20);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}

	public class DynamicAdapter extends BaseAdapter {
		private Context mContext;
		private List<DynamicNews> mData;
		private DynamicNews mDynamicNews;
		private String contentString;

		public DynamicAdapter(Context context) {
			this.mContext = context;
			mData = new ArrayList<DynamicNews>();
		}

		public void setData(List<DynamicNews> mData) {
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
			mDynamicNews = mData.get(position);
			ItemHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.home_frg_flow_listview_item2, null);
				holder = new ItemHolder();
				holder.mImgHead = (ImageView) convertView.findViewById(R.id.flow_user_img_iv);
				holder.mImgHeadTv = (TextView) convertView.findViewById(R.id.flow_user_img_tv);
				holder.mListViewComment = (BasicListView) convertView.findViewById(R.id.homeListViewComment);
				holder.mListViewComment.setHaveScrollbar(false);
				holder.mListViewComment.setItemsCanFocus(true);
				holder.mHomeFlowFigureLl = (LinearLayout) convertView.findViewById(R.id.home_flow_figure_ll);
				holder.mHomeFrgFlowCommentLL = (LinearLayout) convertView.findViewById(R.id.homeFrgFlowCommentLL);
				holder.homeFrgLl = (LinearLayout) convertView.findViewById(R.id.home_frg_ll);
				holder.mLookMoreCommentLL = (LinearLayout) convertView.findViewById(R.id.lookMoreCommentLL);
				holder.mFlowSourceUserNameTv = (TextView) convertView.findViewById(R.id.flow_source_user_name_tv);
				holder.mFlowSourcePowerTv = (TextView) convertView.findViewById(R.id.flow_source_power_tv);
				holder.mHomeFlowUserOpinionTv = (TextView) convertView.findViewById(R.id.home_flow_user_opinion_tv);
				holder.mHomeFlowPublishedContentLl = (LinearLayout) convertView.findViewById(R.id.home_flow_published_content_ll);
				holder.mFlowPublishedImgIv = (ImageView) convertView.findViewById(R.id.flow_published_img_iv);
				holder.mFlowPublishedContentTv = (TextView) convertView.findViewById(R.id.flow_published_content_tv);
				holder.mFlowPublishedCommonHintTimeTv = (TextView) convertView.findViewById(R.id.flow_published_common_hint_time_tv);
				holder.mFlowPublishedCommonHintCommentNumTv = (TextView) convertView.findViewById(R.id.flow_published_common_hint_comment_num_tv);
				holder.mFlowPublishedCommonTitleTv = (TextView) convertView.findViewById(R.id.flow_published_common_title_tv);
				holder.mHomeFlowPublishedCommonHintRl = (RelativeLayout) convertView.findViewById(R.id.home_flow_published_common_hint_rl);
				holder.mHomeFlowPublishedCommonLl = (LinearLayout) convertView.findViewById(R.id.home_flow_published_common_ll);
				holder.mFlowFigureImgIv = (ImageView) convertView.findViewById(R.id.flow_figure_img_iv);
				holder.mFlowFigureImgTv = (TextView) convertView.findViewById(R.id.flow_figure_img_tv);
				holder.mFlowFigureNameTv = (TextView) convertView.findViewById(R.id.flow_figure_name_tv);
				holder.mFlowFigureContentTv = (TextView) convertView.findViewById(R.id.flow_figure_content_tv);
				holder.home_flow_item_head_LL = (LinearLayout) convertView.findViewById(R.id.home_flow_item_head_LL);
				// add heart press
				holder.mFlowHeartPressLL = (LinearLayout) convertView.findViewById(R.id.flowHeartPressLL);
				holder.mFlowHeartPeopleCL = (KnoTagGroupView) convertView.findViewById(R.id.flowHeartPeopleCL);
				holder.mFlowHeartTv = (TextView) convertView.findViewById(R.id.flowHeartTv);
				convertView.setTag(holder);
			} else {
				holder = (ItemHolder) convertView.getTag();
			}
			holder.mFlowHeartTv.setTag(mDynamicNews.getId());
			myOnClickListener = new IndexDynamicOnClickLister(mDynamicNews, getParentActivity());
			String hintTitle = "";

			// 公共部分布局：发布者头像布局
			initDynamicPublisherAvater(mDynamicNews, holder.mImgHead, holder.mImgHeadTv);

			// 初始化 发布者姓名
//			if (mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_CONTACTS || mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_REQUIREMENT
//					|| mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_KNOWLEDGE || mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_CUSTOMER
//					|| mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_ORGANIZATION || mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_CUSTOM) {
//				holder.mFlowSourceUserNameTv.setVisibility(View.GONE);
//			} else {
				holder.mFlowSourceUserNameTv.setText(mDynamicNews.getCreaterName());
				holder.mFlowSourceUserNameTv.setVisibility(View.VISIBLE);
//			}
			// 初始化发布者观点
			if (StringUtils.isEmpty(mDynamicNews.getForwardingContent())) {
				holder.mHomeFlowUserOpinionTv.setVisibility(View.GONE);
			} else {
				holder.mHomeFlowUserOpinionTv.setVisibility(View.VISIBLE);
				holder.mHomeFlowUserOpinionTv.setText(parser.addSmileySpans(mDynamicNews.getForwardingContent()));
			}

			/** 根据不同的数据类型进行相应的布局展示 */
			switch (mDynamicNews.getType()) {
			case DynamicNews.TYPE_FORWARDING_KNOWLEDGE: /* 转发知识 */
				hintTitle = "分享了知识";
				forwardingUI(holder);
				initKnowOrMeetOrDemandLayout(holder);
				break;
			case DynamicNews.TYPE_FORWARDING_REQUIREMENT:/* 转发需求 */
				hintTitle = "分享了需求";
				forwardingUI(holder);
				initKnowOrMeetOrDemandLayout(holder);
				break;
			case DynamicNews.TYPE_FORWARDING_MEETING:/* 转发会议 */
				hintTitle = "分享了会议";
				forwardingUI(holder);
				initKnowOrMeetOrDemandLayout(holder);
				break;
			case DynamicNews.TYPE_CREATE_KNOWLEDGE: /* 发布知识 */
				if (FlowType.HomePageFlow.equals(flowType)) {
					hintTitle = "创建了知识";
				} else if (FlowType.OtherPeopleFlow.equals(flowType)) {
					hintTitle = "创建了知识";
				} else {
					hintTitle = FrgFlow.switchHintTitle(hintTitle, mDynamicNews.getPtype());
				}
				publishUI(holder);
				initKnowOrMeetOrDemandLayout(holder);
				break;
			case DynamicNews.TYPE_RECOMMEND_KNOWLEDGE: /* 金桐推荐知识 */
				hintTitle = "推荐了知识";
				publishUI(holder);
				initKnowOrMeetOrDemandLayout(holder);
				break;
			case DynamicNews.TYPE_RECOMMEND_REQUIREMENT: /* 金桐推荐需求 */
				hintTitle = "推荐了" + switchDemandType(mDynamicNews.getLowType());
				publishUI(holder);
				initKnowOrMeetOrDemandLayout(holder);
				break;
			case DynamicNews.TYPE_CREATE_REQUIREMENT:/* 创建需求 */
				if (FlowType.HomePageFlow.equals(flowType)) {
					hintTitle = "创建了" + FrgFlow.switchDemandType(mDynamicNews.getLowType());
				} else if (FlowType.OtherPeopleFlow.equals(flowType)) {
					hintTitle = "创建了" + FrgFlow.switchDemandType(mDynamicNews.getLowType());
				} else {
					hintTitle = FrgFlow.switchHintTitleDemand(hintTitle, mDynamicNews.getPtype()) + switchDemandType(mDynamicNews.getLowType());
				}
				publishUI(holder);
				initKnowOrMeetOrDemandLayout(holder);
				break;
			case DynamicNews.TYPE_CREATE_MEETING:/* 发布会议 */
				hintTitle = "创建了会议";
				publishUI(holder);
				initKnowOrMeetOrDemandLayout(holder);
				break;
			case DynamicNews.TYPE_USER_CARD:/* 用户名片 */
				hintTitle = "分享了用户";
				forwardingUI(holder);
				initFigureLayout(holder);
				break;
			case DynamicNews.TYPE_RECOMMEND_CUSTOMER:/* 金桐脑推荐的用户 */
				hintTitle = "推荐了用户";
				forwardingUI(holder);
				initFigureLayout(holder);
				break;
			case DynamicNews.TYPE_FORWARDING_ORGANIZATION:/* 转发组织 */
				hintTitle = "分享了组织";
				forwardingUI(holder);
				initFigureLayout(holder);
				break;
			case DynamicNews.TYPE_CREATE_CUSTOM:/* 创建客户 */
				hintTitle = FrgFlow.switchHintTitleDemand(hintTitle, mDynamicNews.getPtype()) + "客户";
				forwardingUI(holder);
				initFigureLayout(holder);
				break;
			case DynamicNews.TYPE_FORWARDING_CUSTOM:/* 转发客户 */
				hintTitle = "分享了客户";
				forwardingUI(holder);
				initFigureLayout(holder);
				break;
			case DynamicNews.TYPE_CREATE_CONTACTS:/* 创建人脉 */
				hintTitle = "";
				hintTitle = FrgFlow.switchHintTitleDemand(hintTitle, mDynamicNews.getPtype()) + "人脉";
				forwardingUI(holder);
				initFigureLayout(holder);
				break;
			case DynamicNews.TYPE_RECOMMEND_CONTACTS:/* 金桐推荐人脉 */
				hintTitle = "推荐了人脉";
				forwardingUI(holder);
				initFigureLayout(holder);
				break;
			case DynamicNews.TYPE_FORWARDING_CONTACTS:/* 转发人脉 */
				hintTitle = "分享了人脉";
				forwardingUI(holder);
				initFigureLayout(holder);
				break;
			case DynamicNews.TYPE_RECOMMEND_ORGANIZATION:// 大数据推送的0客户 1组织　2未注册组织
				hintTitle = "推荐的组织";
				forwardingUI(holder);
				initFigureLayout(holder);
				break;
			case DynamicNews.TYPE_RECOMMEND_CUSTOM:// 大数据推送的0客户 1组织　2未注册组织
				hintTitle = "推荐的客户";
				forwardingUI(holder);
				initFigureLayout(holder);
				break;
			default:
			}
			// 发布者的操作权限描述
			holder.mFlowSourcePowerTv.setText(hintTitle);
			/* 公共部分数据填充 */
			commonInitData(position, holder);
			/* 添加跳转事件 */
			holder.home_flow_item_head_LL.setOnClickListener(myOnClickListener);
			holder.mHomeFlowFigureLl.setOnClickListener(myOnClickListener);
			holder.mFlowPublishedCommonTitleTv.setOnClickListener(myOnClickListener);
			holder.mHomeFlowPublishedCommonLl.setOnClickListener(myOnClickListener);
			if (mParentActivity instanceof MainActivity) {/* 其他人和我的动态都不能跳转 */
				holder.mFlowSourceUserNameTv.setOnClickListener(myOnClickListener);
				holder.mImgHead.setOnClickListener(myOnClickListener);
			}
			mHomeFrgSendCommentIv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					sendComment(mIndex);
					if (mainRootView != null && flowType.equals(FlowType.MainFlow)) {
						mainRootView.setNeedInterceptTouch(true);
					}
				}
			});
			/* 查看更多评论 */
			holder.mLookMoreCommentLL.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getParentActivity(), DynamicCommentActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable(ENavConsts.KEY_FRG_FLOW_COMMENT, mData.get(position));
					bundle.putInt(ENavConsts.KEY_FRG_FLOW_INDEX, position);
					intent.putExtras(bundle);
					getParentActivity().startActivityForResult(intent, ENavConsts.ActivityReqCode.REQUEST_CODE_LOOK_MORE_COMMENT);
				}
			});
			mHomeFrgCommentEt.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				}

				@Override
				public void afterTextChanged(Editable s) {
					if (position >= mData.size())
						return;
					DynamicNews mDynamicNews = mData.get(position);
					mDynamicNews.setInputText(s.toString());
				}
			});

			if (mMainApp.getAppData().ismGinTongDynamicush()) {
				holder.homeFrgLl.setVisibility(View.VISIBLE);
			} else if (mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_CONTACTS || mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_CUSTOMER
					|| mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_KNOWLEDGE || mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_REQUIREMENT
					|| mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_ORGANIZATION) {
				holder.homeFrgLl.setVisibility(View.GONE);
			}
			return convertView;
		}

		/**
		 * 转发UI
		 */
		public void forwardingUI(ItemHolder holder) {
			if (mDynamicNews.getType() == DynamicNews.TYPE_USER_CARD || mDynamicNews.getType() == DynamicNews.TYPE_FORWARDING_ORGANIZATION || mDynamicNews.getType() == DynamicNews.TYPE_CREATE_CUSTOM
					|| mDynamicNews.getType() == DynamicNews.TYPE_FORWARDING_CUSTOM || mDynamicNews.getType() == DynamicNews.TYPE_CREATE_CONTACTS
					|| mDynamicNews.getType() == DynamicNews.TYPE_FORWARDING_CONTACTS || mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_CONTACTS
					|| mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_CUSTOMER || mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_ORGANIZATION) {
				holder.mHomeFlowFigureLl.setVisibility(View.VISIBLE);
				holder.mHomeFlowPublishedCommonLl.setVisibility(View.GONE);
			} else {
				holder.mHomeFlowFigureLl.setVisibility(View.GONE);
				holder.mHomeFlowPublishedCommonLl.setVisibility(View.VISIBLE);
			}
		}

		/** 初始化 动态发布人 头像 */
		private void initDynamicPublisherAvater(DynamicNews mDynamicNews, ImageView avatarIv, TextView avatarTv) {
			// 金桐脑发布
//			if (mDynamicNews.getCreateType() == 0) {
//				// 金桐脑
//				avatarIv.setImageDrawable(getResources().getDrawable(R.drawable.gintong_smart_brain));
//			}
//			// 个人或组织用户发布
//			else {
//				// initPeopleOrOrgAvatarTextLayout(avatarIv, avatarTv,
//				// mDynamicNews.getCreateType(), picPath, createrNameFlag);
//			}
			String picPath = mDynamicNews.getPicPath();
			String createrNameFlag = mDynamicNews.getCreaterName();
			Util.initAvatarImage(mContext, avatarIv, createrNameFlag, picPath, mDynamicNews.getGender(), mDynamicNews.getCreateType());
		}

		/**
		 * 初始化个人或组织的头像
		 * 
		 * @param avatarIv
		 *            头像ImageView
		 * @param avatarTv
		 *            头像TextView
		 * @param peopleOrOrg
		 *            组织/客户：2 ；好友/人脉：1
		 * @param picPath
		 *            头像路径
		 * @param name
		 *            姓名
		 */
		private void initPeopleOrOrgAvatarTextLayout(ImageView avatarIv, TextView avatarTv, int peopleOrOrg, String picPath, String name) {
			// 如果头像路径为空，或为默认头像，则显示文字+背景色的样式
			if (StringUtils.isEmpty(picPath) || (!StringUtils.isEmpty(picPath) && "default.jpeg".equals(picPath.split("/")[picPath.split("/").length - 1]))) {
				// 显示默认图片
				if (peopleOrOrg == 2) {
					// 组织
					avatarIv.setImageDrawable(getResources().getDrawable(R.drawable.no_avatar_client_organization));
				} else if (peopleOrOrg == 1) {
					// 个人
					avatarIv.setImageDrawable(getResources().getDrawable(R.drawable.no_avatar_but_gender));
				}
				// 添加文字
				if (!StringUtils.isEmpty(name)) {
					char[] chs = name.toCharArray();
					name = String.valueOf(chs[chs.length - 1]);
				}
				avatarTv.setText(name);
			}
			// 否则显示头像本身
			else {
				if (peopleOrOrg == 2) {
					Util.initAvatarImage(mContext, avatarIv, "", picPath, 0, 2);
				} else if (peopleOrOrg == 1) {
					Util.initAvatarImage(mContext, avatarIv, "", picPath, 0, 1);
				}
				avatarTv.setText("");
			}
		}

		/**
		 * 公共区域数据初始化
		 * 
		 * @param position
		 * @param holder
		 */
		private void commonInitData(final int position, final ItemHolder holder) {
			/* 时间间隔 */
			String strTime = "";
			if (!TextUtils.isEmpty(mDynamicNews.getCtime())) {
				strTime = TimeUtil.TimeFormat(mDynamicNews.getCtime());
				holder.mFlowPublishedCommonHintTimeTv.setText(strTime);
			}
			/* 评论数 */
			holder.mFlowPublishedCommonHintCommentNumTv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mIndex = position;
					showBottomCommentView();
				}
			});
			ArrayList<DynamicComment> mDynamicComments = mDynamicNews.getComments();

			/* 判断是否显示评论 */
			if (null == mDynamicComments || mDynamicComments.size() <= 0) {
				holder.mHomeFrgFlowCommentLL.setVisibility(View.GONE);
			} else {
				holder.mHomeFrgFlowCommentLL.setVisibility(View.VISIBLE);
			}

			// 显示点赞：
			// 判断点赞人集合是否null，布局显隐
			final ArrayList<DynamicPraise> mDynamicPraiseList = mDynamicNews.getmDynamicPraiseList();
			if (mDynamicPraiseList != null && mDynamicPraiseList.size() > 0) {// 显示点赞，则必须显示评论布局
				// holder.mHomeFrgFlowCommentLL.setVisibility(View.VISIBLE);
				// holder.mFlowHeartPressLL.setVisibility(View.VISIBLE);
				holder.mFlowHeartPressLL.setVisibility(View.GONE);
			} else {
				holder.mFlowHeartPressLL.setVisibility(View.GONE);
			}

			if (mDynamicComments != null) {
				long commentCount = mDynamicNews.getCount() < 3 ? mDynamicNews.getComments().size() : mDynamicNews.getCount();
				if (commentCount > 0) {
					holder.mFlowPublishedCommonHintCommentNumTv.setTextColor(getResources().getColor(R.color.home_index_text_on_bg));
					Drawable drawable = getResources().getDrawable(R.drawable.commet_num_more);
					// / 这一步必须要做,否则不会显示.
					drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
					holder.mFlowPublishedCommonHintCommentNumTv.setCompoundDrawables(drawable, null, null, null);
					holder.mFlowPublishedCommonHintCommentNumTv.setText("  " + commentCount);
					holder.mLookMoreCommentLL.setVisibility(View.VISIBLE);
				} else {
					holder.mFlowPublishedCommonHintCommentNumTv.setTextColor(getResources().getColor(R.color.find_project_txt_gray));
					Drawable drawable = getResources().getDrawable(R.drawable.commet_num);
					// / 这一步必须要做,否则不会显示.
					drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
					holder.mFlowPublishedCommonHintCommentNumTv.setCompoundDrawables(drawable, null, null, null);
					holder.mFlowPublishedCommonHintCommentNumTv.setText("  0");
					holder.mLookMoreCommentLL.setVisibility(View.GONE);
				}

				commentAdapter = new DynamicCommentAdapter(getParentActivity(), window);
				commentAdapter.setData(mDynamicComments, mDynamicNews, mDynamicComments.size() > 2 ? 2 : mDynamicComments.size(), DynamicCommentAdapter.CommentShowType.CommentShowOneLine);
				holder.mListViewComment.setAdapter(commentAdapter);
				commentAdapter.setPopUpWindowViewConfig(inflateDeleteView, deleteTv, copyTv, measuredHeigh, measuredWidth, linmitHeight);
				commentAdapter.setParentAdapter(mAdapterDynamic);
				commentAdapter.setIndex(position);
				commentAdapter.notifyDataSetChanged();
				// setCommentDeleteByItemLongClick(holder,position,commentAdapter);
			}
			/**********************************/
			boolean heartIsPressed = false;
			if (mDynamicPraiseList != null) {
				holder.mFlowHeartPeopleCL.removeAllViews();
				for (int i = 0; i < mDynamicPraiseList.size(); i++) {
					final DynamicPraise dynamicPraise = mDynamicPraiseList.get(i);
					TextView textView1 = new TextView(mContext);
					textView1.setTextColor(getResources().getColor(R.color.home_index_dynamic_text_color));
					textView1.setText(dynamicPraise.getpName());
					holder.mFlowHeartPeopleCL.addView(textView1, 0);
					if (i != mDynamicPraiseList.size() - 1) {
						TextView textView2 = new TextView(mContext);
						textView2.setTextColor(getResources().getColor(R.color.home_index_dynamic_text_color));
						textView2.setText(" , ");
						holder.mFlowHeartPeopleCL.addView(textView2, 0);
					}
					textView1.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if (App.getUserID().equals(String.valueOf(dynamicPraise.getpId()))) {
								ENavigate.startRelationHomeActivity(mContext, String.valueOf(dynamicPraise.getpId()), true, ENavConsts.type_details_member);
							} else {
								ENavigate.startRelationHomeActivity(mContext, String.valueOf(dynamicPraise.getpId()), true, ENavConsts.type_details_other);
							}
						}
					});
					if (App.getUserID().equals(String.valueOf(dynamicPraise.getpId()))) {
						heartIsPressed = true;
					}
				}
			}
			if (heartIsPressed) {
				Drawable drawable = getResources().getDrawable(R.drawable.heart_pressed);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				holder.mFlowHeartTv.setCompoundDrawables(drawable, null, null, null);
			} else {
				Drawable drawable = getResources().getDrawable(R.drawable.heart);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				holder.mFlowHeartTv.setCompoundDrawables(drawable, null, null, null);
			}
			/**********************************/
			holder.mFlowHeartTv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mIndex = position;
					ArrayList<DynamicPraise> praiseList = mData.get(mIndex).getmDynamicPraiseList();
					// 判断当前用户是否点赞
					boolean isPraised = false;
					if (praiseList != null) {
						for (DynamicPraise dynamicPraise : praiseList) {
							if (App.getUserID().equals(String.valueOf(dynamicPraise.getpId()))) {
								isPraised = true;
								mDynamicPraise = dynamicPraise;
							}
						}
					}
					// 已点赞--按钮置为点赞状态，执行取消点赞操作
					if (isPraised) {
						if (mDynamicPraise != null && mDynamicPraise.getId() != 0) {
							HomeReqUtil.cancelDynamicPraise(mContext, FrgFlow.this, null, mDynamicPraise.getId());
							// 从集合中移出
							mDynamicPraiseList.remove(mDynamicPraise);
						}
					}
					// 未点赞--按钮置为未赞状态，执行点赞操作
					else {
						if (mDynamicPraise == null) {
							mDynamicPraise = new DynamicPraise();
							mDynamicPraise.setpId(Long.valueOf(App.getUserID()));
							mDynamicPraise.setDynamicId(mData.get(mIndex).getId());
							mDynamicPraise.setpName(App.getNick());
							mDynamicPraise.setPtype(1);
							HomeReqUtil.addDynamicPraise(mContext, FrgFlow.this, null, mData.get(mIndex).getId(), 1);
						}
					}
				}
			});
		}

		/**
		 * 发布UI（非人物布局，去掉对动态描述）
		 */
		public void publishUI(ItemHolder holder) {
			holder.mHomeFlowPublishedCommonLl.setVisibility(View.VISIBLE);
			holder.mHomeFlowFigureLl.setVisibility(View.GONE);
//			if (mDynamicNews.getCreateType() == 0) {// 金桐脑发布
//				holder.mFlowSourceUserNameTv.setVisibility(View.GONE);
//			} else {
//				holder.mFlowSourceUserNameTv.setVisibility(View.VISIBLE);
//			}
		}

		/** 初始化 人脉/用户/组织/客户 布局 */
		private void initFigureLayout(ItemHolder holder) {
			/* 设置头像 */
			if (mDynamicNews.getType() == DynamicNews.TYPE_FORWARDING_ORGANIZATION || mDynamicNews.getType() == DynamicNews.TYPE_CREATE_CUSTOM
					|| mDynamicNews.getType() == DynamicNews.TYPE_FORWARDING_CUSTOM || mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_ORGANIZATION) {
				/* 客户组织设置头像 */
				// initPeopleOrOrgAvatarTextLayout(holder.mFlowFigureImgIv,
				// holder.mFlowFigureImgTv, 2, mDynamicNews.getImgPath(),
				// mDynamicNews.getTitle());
				Util.initAvatarImage(mContext, holder.mFlowFigureImgIv, mDynamicNews.getTitle(), mDynamicNews.getImgPath(), 0, 2);
			} else if (mDynamicNews.getType() == DynamicNews.TYPE_CREATE_CONTACTS || mDynamicNews.getType() == DynamicNews.TYPE_FORWARDING_CONTACTS
					|| mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_CONTACTS || mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_CUSTOMER
					|| mDynamicNews.getType() == DynamicNews.TYPE_USER_CARD) {
				/* 人脉好友设置头像 */
				Util.initAvatarImage(mContext, holder.mFlowFigureImgIv, mDynamicNews.getTitle(), mDynamicNews.getImgPath(), 0, 1);
				// initPeopleOrOrgAvatarTextLayout(holder.mFlowFigureImgIv,
				// holder.mFlowFigureImgTv, 1, mDynamicNews.getImgPath(),
				// mDynamicNews.getTitle());
			}

			/* 人脉和名片姓名 */
			holder.mFlowFigureNameTv.setText(mDynamicNews.getTitle());
			if (!TextUtils.isEmpty(mDynamicNews.getContent())) {

				/* 人脉和名片内容 */
				if (mDynamicNews.getContent().contains("#")) {
					contentString = mDynamicNews.getContent().replace("#", " ");
				} else {
					contentString = mDynamicNews.getContent();
				}
				holder.mFlowFigureContentTv.setText(contentString);
			}else {
				holder.mFlowFigureContentTv.setText("");
			}
		}

		/**
		 * 初始化 会议/知识/需求 布局
		 * 
		 * @param holder
		 */
		private void initKnowOrMeetOrDemandLayout(ItemHolder holder) {
			// 会议/知识/需求的标题
			if (!EUtil.isEmpty(mDynamicNews.getTitle())) {
				holder.mFlowPublishedCommonTitleTv.setText(mDynamicNews.getTitle());
				holder.mFlowPublishedCommonTitleTv.setVisibility(View.VISIBLE);
			} else {
				holder.mFlowPublishedCommonTitleTv.setVisibility(View.GONE);
			}
			// 会议/知识/需求的图片
			if (!EUtil.isEmpty(mDynamicNews.getImgPath().trim())) {
				ImageLoader.load(holder.mFlowPublishedImgIv, mDynamicNews.getImgPath(), R.drawable.hy_chat_right_pic);
				holder.mFlowPublishedImgIv.setVisibility(View.VISIBLE);
			} else {
				holder.mFlowPublishedImgIv.setVisibility(View.GONE);
				android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
						android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
				int dip10 = DisplayUtil.dip2px(mContext, 10);
				params.setMargins(dip10, dip10, dip10, dip10);
				holder.mHomeFlowPublishedContentLl.setLayoutParams(params);
			}

			// 会议/知识/需求的内容简介
			if (!EUtil.isEmpty(mDynamicNews.getContent())) {
				holder.mFlowPublishedContentTv.setText(mDynamicNews.getContent());
				holder.mFlowPublishedContentTv.setVisibility(View.VISIBLE);
			} else {
				holder.mFlowPublishedContentTv.setVisibility(View.GONE);
			}
			if (EUtil.isEmpty(mDynamicNews.getContent()) && EUtil.isEmpty(mDynamicNews.getTitle())) {
				holder.mHomeFlowPublishedContentLl.setVisibility(View.GONE);
			} else {
				holder.mHomeFlowPublishedContentLl.setVisibility(View.VISIBLE);
			}
		}

		public void sendComment(final int position) {
			// 发送回复信息
			DynamicNews mDynamicNews = mData.get(position);

			if (mDynamicNews != null) {

				String content = mHomeFrgCommentEt.getText().toString();
				if (TextUtils.isEmpty(content)) {
					content = mDynamicNews.getInputText();
				}
				if (sendCommend(mDynamicNews, content)) {
					newDynamicComment = new DynamicComment();
					newDynamicComment.setUserName(App.getNick());
					newDynamicComment.setUserId(Long.valueOf(App.getUserID()));
					newDynamicComment.setComment(mDynamicNews.getInputText());
					mDynamicNews.insertComment(mDynamicNews.getInputText());
				}
			}
			mDynamicNews.setInputText("");
			if (mHomeFrgCommentEt != null) {
				mHomeFrgCommentEt.setText("");
			}
			hideKeyboard();
			mAdapterDynamic.notifyDataSetChanged();
		}

		private class ItemHolder {
			public LinearLayout home_flow_item_head_LL;
			public ImageView mImgHead;// 个人头像
			public TextView mImgHeadTv;// 个人头像上的文字
			public BasicListView mListViewComment;// 评论列表
			public LinearLayout mHomeFrgFlowCommentLL;
			public LinearLayout mHomeFlowPublishedCommonLl;// 知识和会议
			private LinearLayout mLookMoreCommentLL;// 查看全部评论
			// private LinearLayout mHomeFlowUserOpinionLl;// 人物知识描述
			/** 动态发布人姓名 */
			private TextView mFlowSourceUserNameTv;
			private TextView mFlowSourcePowerTv;// 权限描述
			/** 发布者对此动态的描述 */
			private TextView mHomeFlowUserOpinionTv;
			private LinearLayout mHomeFlowPublishedContentLl;// 发布或者转发内容
			private ImageView mFlowPublishedImgIv;
			/** 会议或知识的内容简介 */
			private TextView mFlowPublishedContentTv;
			private TextView mFlowPublishedCommonHintTimeTv;// 时间间隔
			private TextView mFlowPublishedCommonHintCommentNumTv;// 评论数
			/** 转发内容标题（知识/会议） */
			private TextView mFlowPublishedCommonTitleTv;
			private RelativeLayout mHomeFlowPublishedCommonHintRl;// 回复点击区域
			/** 人物布局 */
			private LinearLayout mHomeFlowFigureLl;
			/** 转发人物的头像 */
			private ImageView mFlowFigureImgIv;
			public TextView mFlowFigureImgTv;
			private TextView mFlowFigureNameTv;// 人脉和名片姓名
			private TextView mFlowFigureContentTv;// 人脉和名片内容

			private LinearLayout mFlowHeartPressLL;// 动态点赞布局(默认隐藏)
			private KnoTagGroupView mFlowHeartPeopleCL;// 动态点赞的人 自定义LinearLayout
			private TextView mFlowHeartTv;// 点赞Tv
			private LinearLayout homeFrgLl;//
		}

	}

	public boolean sendCommend(DynamicNews dynamicNews, String content) {
		if (TextUtils.isEmpty(content.trim())) {
			Toast.makeText(getParentActivity(), "评论内容不能为空", 0).show();
			return false;
		}
		if (dynamicNews != null) {
			showLoadingDialogWithoutOnCancelListener();
			return HomeReqUtil.addDynamicComment(getParentActivity(), this, null, dynamicNews.getType(), dynamicNews.getId(), content);
		} else {
			return false;
		}
	}

	public Activity getParentActivity() {
		if (mParentActivity != null) {
			if (mParentActivity instanceof MainActivity) {
				return (MainActivity) mParentActivity;
			} else if (mParentActivity instanceof RelationHomeActivity) {
				return (RelationHomeActivity) mParentActivity;
			} else if (mParentActivity instanceof SearchActivity) {
				return (SearchActivity) mParentActivity;
			} else if (mParentActivity instanceof ContactsDetailsActivity) {
				return (ContactsDetailsActivity) mParentActivity;
			} else if (mParentActivity instanceof ClientDetailsActivity) {
				return (ClientDetailsActivity) mParentActivity;
			} else if (mParentActivity instanceof OrgMyHomePageActivity) {
				return (OrgMyHomePageActivity) mParentActivity;
			} else if (mParentActivity instanceof OrgFinancingActivity) {
				return (OrgFinancingActivity) mParentActivity;
			}else {
				return (MainActivity) mParentActivity;
			}
		} else {
			return null;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.homeFrgPicIv:
			if (mGlobalCreatButton != null) {
				mGlobalCreatButton.clearAnimation();
				mGlobalCreatButton.setVisibility(View.GONE);
			}
			if (!isShowface) {
				mIndustrySmileyLL.setVisibility(View.VISIBLE);
				isShowface = true;
				hideKeyboard();// 隐藏软盘，加了之后gridView起不来
			} else {
				mIndustrySmileyLL.setVisibility(View.GONE);
				isShowface = false;
				// 显示软盘
				showKeyboard();
			}
			break;
		case R.id.homeFrgCommentEt:
			mIndustrySmileyLL.setVisibility(View.GONE);
			isShowface = false;// 显示软盘
			break;
		default:
			break;
		}
	}

	/** 适应框架 返回触摸事件的状态，必须要返回一个使用适应框架的Activity,否则listview刷新时候和scrollview发生冲突 */
	private AbsListViewDelegate mAbsListViewDelegate = new AbsListViewDelegate();
	private RelativeLayout root;
	private CustomRelativeLayout mainRootView;

	public View getRootView() {
		return root;
	}

	@Override
	public boolean isViewBeingDragged(MotionEvent event) {
		// 重要的方法
		if (getParentActivity() != null) {
			if (getParentActivity() instanceof RelationHomeActivity || getParentActivity() instanceof OrgMyHomePageActivity || getParentActivity() instanceof ContactsDetailsActivity
					|| getParentActivity() instanceof OrgFinancingActivity) {
//				return mAbsListViewDelegate.isViewBeingDragged(event, mListView);
			}
		}
		return false;
	}

	/**
	 * 新增的点赞功能
	 * 
	 * @param mDynamicPraises
	 * @param listComment
	 * @param index
	 */
	public void changeDynamicListUI(ArrayList<DynamicPraise> mDynamicPraises, List<DynamicComment> listComment, int index) {
		if (mDynamicPraises != null) {
			mlistDynamicNews.get(index).setmDynamicPraiseList(mDynamicPraises);
		}
		if (null != listComment) {
			mlistDynamicNews.get(index).setCount(listComment.size());
			ArrayList<DynamicComment> comments = new ArrayList<DynamicComment>();
			if (listComment.size() > 2) {
				comments.add(listComment.get(0));
				comments.add(listComment.get(1));
			} else {
				comments.addAll(listComment);
			}
			mlistDynamicNews.get(index).setComments(comments);
		}
		mAdapterDynamic.notifyDataSetChanged();
	}

	/**
	 * 2大乐，3中乐，4小乐，5独乐
	 * 
	 * @param hintTitle
	 * @param switchType
	 * @return
	 */
	public static String switchHintTitle(String hintTitle, int switchType) {
		switch (switchType) {
		case 0:
		case 1:
			hintTitle = "创建了知识";
			break;
		case 2:
			hintTitle = "大乐了知识";
			break;
		case 3:
			hintTitle = "中乐了知识";
			break;
		case 4:
			hintTitle = "小乐了知识";
			break;
		case 5:
			hintTitle = "独乐了知识";
			break;
		default:
			break;
		}
		return hintTitle;
	}

	/**
	 * 需求的四个等级状态
	 * 
	 * @param hintTitle
	 * @param switchType
	 * @return
	 */
	public static String switchHintTitleDemand(String hintTitle, int switchType) {
		hintTitle = "";
		switch (switchType) {
		case 0:
		case 1:
			hintTitle = "创建了";
			break;
		case 2:
			hintTitle = "大乐了";
			break;
		case 3:
			hintTitle = "中乐了";
			break;
		case 4:
			hintTitle = "小乐了";
			break;
		case 5:
			hintTitle = "独乐了";
			break;
		default:
			break;
		}
		return hintTitle;
	}

	/**
	 * 需求类型
	 * 
	 * @return
	 */
	public static String switchDemandType(int switchType) {
		String demandType = "需求";
		switch (switchType) {
		case ChooseDataUtil.CHOOSE_type_OutInvestType:
			demandType = "投资需求";
			break;
		case ChooseDataUtil.CHOOSE_type_InInvestType:
			demandType = "融资需求";
			break;
		}
		return demandType;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			mIsVisibleToUser = true;
		} else {
			mIsVisibleToUser = false;
		}
	}

	@Override
	public void onFlaying(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		if (mParentActivity instanceof MainActivity) {
//			((MainActivity) mParentActivity).onFlaying(e1, e2, velocityX, velocityY);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		showBottomNormalView();
		if (commentAdapter != null && window != null) {
			window.dismiss();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.home_frg_flow_listview:
			if (commentAdapter != null && window != null) {
				window.dismiss();
			}
			break;
		case R.id.homeFrgCommentEt:
			mIndustrySmileyLL.setVisibility(View.GONE);
			isShowface = false;// 显示软盘
			break;
		}
		return false;
	}

	@Override
	public void onRefresh() {
		if (mParentActivity == null) {// 处理系统崩溃问题
			try {
				initFlowType();// 初始化参数
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		HomeReqUtil.getDynamicNewList(getParentActivity(), FrgFlow.this, null, 0, 20);
	}

	@Override
	public void onLoadMore() {
		startGetData();
	}

	@Override
	public void onPause() {
		super.onPause();
		hideKeyboard();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
