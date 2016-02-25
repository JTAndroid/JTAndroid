package com.tr.ui.knowledge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tr.R;
import com.tr.api.KnowledgeReqUtil;
import com.tr.model.knowledge.KnowledgeComment;
import com.tr.model.obj.ConnectionsMini;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.common.view.MyXListView;
import com.tr.ui.common.view.MyXListView.IXListViewListener;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.widgets.BasicListView;
import com.tr.ui.widgets.CommonSmileyParser;
import com.tr.ui.widgets.ConnsEditDialog;
import com.tr.ui.widgets.SmileyParser;
import com.tr.ui.widgets.SmileyParser2;
import com.tr.ui.widgets.SmileyView;
import com.utils.common.EUtil;
import com.utils.common.JTDateUtils;
import com.utils.http.EAPIConsts.KnoReqType;
import com.utils.http.IBindData;

/**
 * @ClassName: KnowledegeCommentDetailsFragment.java
 * @Description: 获取各种详情页的评论列表， 主要包括需求、业务需求、任务、项目
 * @author CJJ
 * @version V24
 * @Date 2014-10-30
 */
public class KnowledegeCommentDetailsFragment extends JBaseFragment implements
		IBindData, SwipeRefreshLayout.OnRefreshListener, OnScrollListener {

	private final String TAG = getClass().getSimpleName();

	/* 展示评论列表 */
	public MyXListView mKnoCommentLv;
	/* Google 下拉刷新功能 */
//	private SwipeRefreshLayout swipeLayout;
	/* 表情ViewPager */
	private ViewPager faceViewPager;
	private SmileyView smileyViewFirst;
	private SmileyView smileyViewSecond;
	private ImageView mSmileyPagerchange;
	/* true:显示SmileyView；false 显示软键盘 */
	private Boolean isShowface = false;
	/* 表情界面布局 */
	private FrameLayout viewPagerCon;
	public ImageView expressionIv;
	/* 回复文本输入框 */
	private EditText textEt;
	/* 知识展示ViewPager */
	private ViewPager mViewPager;
	/* 子评论的气泡提示 */
	private PopupWindow popupWindow;
	/* 回复评论内容 */
	private TextView commentIndustryTv;
	/* 发送回复 */
	private ImageView sendKnowCommentIv;
	/* 评论内容 */
	private String mComment = null;
	/* 回复评论 */
	private List<KnowledgeComment> replyCommentList;
	/* 记录是知识评论，还是回复评论评论 */
	/* private String addKnowCommentType; */
	/* 获取知识评论的Type */
	private CommendType knowActionForward;
	/* 标记回复下标 */
	private int indexComment;
	/* 存储查看全部评论的下标 */
	private ArrayList<Integer> indexArr = new ArrayList<Integer>();
	@SuppressWarnings("unused")
	private long countComment;
	/* 上下文 */
	private Activity context;
	/* 知识id */
	private long mKnowledgeId = 0L;
	/* 父评论id */
	private long mParentId = 0L;
	private ArrayList<KnowledgeComment> knowCommentsList;
	private CommentDetailsLvAdapter knowCommentsLvAdapter;
	private KnowledgeComment mKnowledgeComment;
	private KnowledgeOfDetailActivity activity;
	/* viewPager滑到当前fragment之前，已经加载了一次数据 */
	private int index = 1;
	private int size = 20;
	private int total;
	private int lastItem;
	private int count;
	private int mState = 0;// 0-正常状态 1-获取更多中 2-刷新中
	public final static int STATE_NORMAL = 0; // 正常
	public final static int STATE_GETMORE = 1; // 加载更多
	public final static int STATE_REFRESH = 2; // 刷新
	/** 加载更多页面 */
	private View moreView;
	private TextView mvTextView;
	private View mvProgressBar;
	private ImageView knowIndustryCommentBackLL;

	private int type = 0; //默认是评论

	private LinearLayout knowCommentBackLL;

	private LinearLayout inputLl;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
	}
	/**
	 * 
	 * @param type  1详情，0评论
	 */
	public KnowledegeCommentDetailsFragment(int type,KnowledgeOfDetailActivity activity) {
		this.type = type;
		this.activity = activity;
	}
	public KnowledegeCommentDetailsFragment() {
	}
	public KnowledegeCommentDetailsFragment(ViewPager viewPager,
			KnowledgeOfDetailActivity activity) {
		this.mViewPager = viewPager;
		this.activity = activity;
	}

	public KnowledegeCommentDetailsFragment(long mKnowledgeId) {
		this.mKnowledgeId = mKnowledgeId;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup root,
			Bundle savedInstance) {
		
		moreView = inflater.inflate(R.layout.pulldown_footer, null);
		mvTextView = (TextView) moreView
				.findViewById(R.id.pulldown_footer_text);
		mvProgressBar = (View) moreView
				.findViewById(R.id.pulldown_footer_loading);
		View view = inflater
		.inflate(R.layout.activity_industry_comment, root, false);
		/* 初始化控件 */
		initComponents(view);
		return view;
	}

	public void setNewKnowCommentsList(
			ArrayList<KnowledgeComment> knowCommentsList, int total,
			long knowledgeId) {
		indexArr.clear();
		this.knowCommentsList = knowCommentsList;
		if (knowCommentsList != null && knowCommentsList.get(0) != null) {
			knowCommentsLvAdapter.notifyDataSetChanged();
			count = knowCommentsList.size();
		} else {
			knowCommentsList = new ArrayList<KnowledgeComment>();
		}
		this.total = total;
		stopLoading();
		mKnowledgeId = knowledgeId;
	}

	/**
	 * 初始化组件
	 */
	private void initComponents(View view) {
		viewPagerCon = (FrameLayout) view.findViewById(R.id.industrySmileyLL);
		sendKnowCommentIv = (ImageView) view
				.findViewById(R.id.sendKnowCommentIv);// 发送/附件按钮
//		swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshSrl);
		mKnoCommentLv = (MyXListView) view.findViewById(R.id.commentLv);
		if(type==1){
			mKnoCommentLv.KnowledegeShow = true;
		}
		expressionIv = (ImageView) view.findViewById(R.id.expressionIv);
		textEt = (EditText) view.findViewById(R.id.commentEt);
		faceViewPager = (ViewPager) view.findViewById(R.id.industrySmileyPager);
		if (knowCommentsLvAdapter==null) {
			knowCommentsLvAdapter = new CommentDetailsLvAdapter();
		}
		knowIndustryCommentBackLL = (ImageView) view
				.findViewById(R.id.knowIndustryCommentBackLL);
		knowCommentBackLL = (LinearLayout)view.findViewById(R.id.knowCommentBackLL); //标题栏
		inputLl = (LinearLayout)view.findViewById(R.id.inputLl); //发送栏
	}
	public void changeSendinputLl(Boolean isVisible){
		if(type==1){
			
			if (isVisible) {
				mKnoCommentLv.CommentVisible = true;
			}else{
				mKnoCommentLv.CommentVisible = false;
			}
		}
		
	}
	@SuppressWarnings("deprecation")
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
//		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
//				android.R.color.holo_green_light,
//				android.R.color.holo_orange_light,
//				android.R.color.holo_red_light);
//		swipeLayout.setOnRefreshListener(this);
		textEt.setOnClickListener(listener);
		if (knowCommentsList != null) {
			count = knowCommentsList.size();
		} else {
			knowCommentsList = new ArrayList<KnowledgeComment>();
		}
		initExpression(view);// 初始化表情

		textEt.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				viewPagerCon.setVisibility(View.GONE);
				isShowface = false;// 显示软盘
				return false;
			}
		});
		knowIndustryCommentBackLL.setOnClickListener(listener);
	}
	private ArrayList<SmileyView> listSmileyViews;
	private static final String LEFTSPECCHAR = ((char) 0X1B) + "";
	private static final String RIGHTSPECCHAR = ((char) 0X11) + "";
	/**
	 * 表情点击事件
	 */
	private SmileyView.OnItemClickListener smileyViewClickListener = new SmileyView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg) {
			final CommonSmileyParser parser = CommonSmileyParser.getInstance(getActivity());
			if (position == SmileyView.MaxSmileyNumber) { // 删除表情
				String text = textEt.getText().toString();
				
				if (text.length() > 0) {
					
					if(text.lastIndexOf(RIGHTSPECCHAR) == text.length() - 1){
						text = text.substring(0, text.lastIndexOf(LEFTSPECCHAR));
					}
					else{
						text = text.substring(0, text.length() - 1);
					}
					textEt.setText(text);
					textEt.setSelection(text.length());
				}
				return;
			}
			
			final String text = textEt.getText().toString() + LEFTSPECCHAR + parser.getmSmileyTexts()[(int) arg] + RIGHTSPECCHAR;
			textEt.setText(text);
			textEt.setSelection(text.length());
		}
	};
	/**
	 * 初始化表情
	 */
	private void initExpression(View view) {
		// 表情view
		smileyViewFirst = new SmileyView(getActivity(), true);
		smileyViewSecond = new SmileyView(getActivity(), false);
		
		listSmileyViews = new ArrayList<SmileyView>();
		int totalPage = (int) Math.ceil(CommonSmileyParser.mEnhancedIconIds.length * 1.0 / SmileyView.MaxSmileyNumber);
		for(int i = 0; i < totalPage; i++){
			SmileyView sv = new SmileyView(getActivity(), i);
			listSmileyViews.add(sv);
			sv.setOnItemClickListener(smileyViewClickListener);
		}
		
		// 表情切换界面
		faceViewPager.setAdapter(new PageViewAdpter());
		mSmileyPagerchange = ((ImageView) view
				.findViewById(R.id.smileyPagerchange));
		sendKnowCommentIv.setOnClickListener(listener);
		expressionIv.setOnClickListener(listener);

		faceViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:
					mSmileyPagerchange
					.setImageResource(R.drawable.chat_biaoqing_1);
					break;
				case 1:
					mSmileyPagerchange
					.setImageResource(R.drawable.chat_biaoqing_2);
					break;
				case 2:
					mSmileyPagerchange
					.setImageResource(R.drawable.chat_biaoqing_3);
					break;
				case 3:
					mSmileyPagerchange
					.setImageResource(R.drawable.chat_biaoqing_4);
					break;
				case 4:
					mSmileyPagerchange
					.setImageResource(R.drawable.chat_biaoqing_5);
					break;
				case 5:
					mSmileyPagerchange
					.setImageResource(R.drawable.chat_biaoqing_6);
					break;
				case 6:
					mSmileyPagerchange
					.setImageResource(R.drawable.chat_biaoqing_7);
					break;
				case 7:
					mSmileyPagerchange
					.setImageResource(R.drawable.chat_biaoqing_8);
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
				popupWindowDismiss();
			}

		});

		mKnoCommentLv.setAdapter(knowCommentsLvAdapter);
		mKnoCommentLv.showFooterView(false);
		// 设置xlistview可以加载、刷新
		mKnoCommentLv.setPullLoadEnable(true);
		mKnoCommentLv.setPullRefreshEnable(false);

		mKnoCommentLv.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				/* 访问服务器 */
				loadMoreData();
				
			}
			
			@Override
			public void onLoadMore() {
				/* 访问服务器 */
				loadMoreData();
				
			}
		});
		mKnoCommentLv.startRefresh();
		mKnoCommentLv.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				return false;
			}
		});

		mKnoCommentLv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});
		
		
		
		mKnoCommentLv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
			}
		});
	}


	/**
	 * 显示回复PopupWindow
	 * 
	 * @param parent
	 * @param view
	 */
	private void showPopupWindow(ViewGroup parent, View view) {
		View contentView = View.inflate(context, R.layout.popup_comment_item,
				null);
		popupWindow = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		int[] location = new int[2];
		view.getLocationInWindow(location);
		popupWindow.showAtLocation(parent, Gravity.LEFT + Gravity.TOP,
				view.getWidth() / 2, location[1] - 40);

		ScaleAnimation scaleAnimation = new ScaleAnimation(0.2f, 1.2f, 0.2f,
				1.2f, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnimation.setDuration(500);
		TranslateAnimation translateAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
				0.1f, Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0);
		AnimationSet animationSet = new AnimationSet(false);
		animationSet.addAnimation(scaleAnimation);
		animationSet.addAnimation(translateAnimation);
		contentView.setAnimation(animationSet);
		commentIndustryTv = (TextView) contentView
				.findViewById(R.id.commentIndustryTv);
		commentIndustryTv.setOnClickListener(listener);
	}

	/**
	 * 发送回复
	 * 
	 * @param strComment
	 */
	private void sendKnowCommend(long knowledgeId, long parentId,
			String comment, int index, int size) {
		KnowledgeReqUtil.addKnowledgeComment(context,
				KnowledegeCommentDetailsFragment.this, knowledgeId, parentId,
				comment, index, size, null);
	}
	

	/**
	 * 检测提交文本
	 * @param text
	 * @return
	 */
	private boolean onCheckSubmitText(String text){
		if (TextUtils.isEmpty(text)) {
			showToast("评论内容不能为空");
			return false;
		}
		return true;
	}

	/**
	 * 隐藏PopupWindow
	 */
	public void popupWindowDismiss() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow = null;
		}
	}

	/**
	 * OnClickListener
	 */
	public OnClickListener listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			popupWindowDismiss();
			if (v.getId() == textEt.getId()) {
				if (isShowface) {
					viewPagerCon.setVisibility(View.GONE);
					isShowface = false;// 显示软盘
				}
			} else if (v.getId() == expressionIv.getId()) { // 笑脸按钮
				if (!isShowface) {
					faceViewPager.setScrollY((int)expressionIv.getY()-expressionIv.getHeight());
					viewPagerCon.setVisibility(View.VISIBLE);
					isShowface = true;
					hideKeyboard();// 隐藏软盘，加了之后gridView起不来
				} else {
					viewPagerCon.setVisibility(View.GONE);
					isShowface = false;
					// 显示软盘
					showKeyboard();
				}
			}
			/*
			 * else if (v.getId() == commentBack.getId()) {
			 * mViewPager.setCurrentItem(0); }
			 */else if (v.getId() == sendKnowCommentIv.getId()) {// 发送回复知识(一级评论)
				mComment = textEt.getText().toString().trim();
				if(onCheckSubmitText(mComment)){
					sendKnowCommend(mKnowledgeId, 0L, mComment, index - 1, size);
					knowActionForward = CommendType.AddKnowCommend;
					hideKeyboard();
					textEt.setText("");
				}
			} else if (v.getId() == knowIndustryCommentBackLL.getId()) {/* title返回键 */
				onBackPressed();
			} else if (v.getId() == commentIndustryTv.getId()) {// 评论回复评论(二级评论)
				ConnsEditDialog.OnFinishListener onFinishListenernew = new ConnsEditDialog.OnFinishListener() {
					@Override
					public void onFinish(View view, String content) {
						// 回复评论
						long parentId = mKnowledgeComment.id;
						if(onCheckSubmitText(content)){
							sendKnowCommend(mKnowledgeId, parentId, content, index,
									size);
							knowActionForward = CommendType.AddReplyCommend;
//							activity.showLoadingDialog();
						}
					}
				};
				ConnsEditDialog connsEditDialog = new ConnsEditDialog(context,
						null, "", onFinishListenernew);
				if(mKnowledgeComment != null && mKnowledgeComment.connectionsMini != null){
					connsEditDialog.setHint("回复:"+mKnowledgeComment.connectionsMini.getName());
				}
				connsEditDialog.show();
			}
		}
	};

	/**
	 * 判断分页有没有更多
	 * 
	 * @return
	 */
	public boolean hasMore() {
		if (index != -1 && (index) * size >= total)
			return false;
		else
			return true;
	}

	/**
	 * 加载更多数据
	 */
	private void loadMoreData() {
		int nowIndex = index;
		startGetData(nowIndex);
	}

	/**
	 * 评论类型
	 */
	public enum CommendType {
		GetKnowCommend, GetReplyCommend, AddReplyCommend, AddKnowCommend
	}

	/**
	 * 访问服务器回调
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void bindData(int tag, Object object) {
		//XlistView结束刷新时状态复位
		mKnoCommentLv.stopLoadMore();
		mKnoCommentLv.stopRefresh();
		if (object == null && KnoReqType.GetKnowledgeComment != tag) {
			stopLoading();
			return;
		}

		if (KnoReqType.GetKnowledgeComment == tag) {// 获取知识评论
			if (CommendType.GetKnowCommend.equals(knowActionForward)) {
				if (object != null) {
					Map<String, Object> hm = ((Map<String, Object>) object);
					if (null == knowCommentsList || !hasMore()) {
						knowCommentsList = (ArrayList<KnowledgeComment>) hm
								.get("listKnowledgeComment");
					} else {
						ArrayList<KnowledgeComment> tempList = (ArrayList<KnowledgeComment>) hm
								.get("listKnowledgeComment");
						knowCommentsList.addAll(tempList);
						tempList = null;
					}
					if (knowCommentsList != null) {
						total = (Integer) hm.get("total");
						index = (Integer) hm.get("index");
						size = (Integer) hm.get("size");
						count = knowCommentsList.size();
						if (size>20) {
							mKnoCommentLv.setPullLoadEnable(false);
						}
					}
				}
			} else if (CommendType.GetReplyCommend.equals(knowActionForward)) {/* 查看全部评论 */
				if (object != null) {
					Map<String, Object> hm = (Map<String, Object>) object;
					replyCommentList = (ArrayList<KnowledgeComment>) hm
							.get("listKnowledgeComment");
					knowCommentsList.get(indexComment).listKnowledgeComment = 
							replyCommentList;
				}
			}
			knowCommentsLvAdapter.notifyDataSetChanged();
		} else if (KnoReqType.AddKnowledgeComment == tag) {// 添加知识评论
			if (object != null) {
				Map<String, Object> hm = (Map<String, Object>) object;
				try {
					boolean addBool = (Boolean) hm.get("success");
					if (CommendType.AddReplyCommend.equals(knowActionForward)) {
						replyCommentList = (ArrayList<KnowledgeComment>) hm
								.get("listKnowledgeComment");
						knowCommentsList.get(indexComment)
								.listKnowledgeComment = replyCommentList;
					} else if (CommendType.AddKnowCommend
							.equals(knowActionForward)) {
						knowCommentsList = (ArrayList<KnowledgeComment>) hm
								.get("listKnowledgeComment");
					}
					knowCommentsLvAdapter.notifyDataSetChanged();
					if (addBool) {
						showToast(R.string.str_comment_success_hint);
					} else {
						showToast(R.string.str_comment_failure_hint);
					}
				} catch (Exception ex) {
					showToast(R.string.str_comment_failure_hint);
					ex.printStackTrace();
				} finally {
//					activity.dismissLoadingDialog();
				}
			}
		}
		stopLoading();
	}

	/**
	 * 显示知识回复详情适配器
	 */
	class CommentDetailsLvAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (knowCommentsList != null) {
				return knowCommentsList.size();
			} else {
				return 0;
			}
		}

		@Override
		public Object getItem(int position) {
			if (knowCommentsList != null) {
				return knowCommentsList.get(position);
			} else {
				return null;
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				final ViewGroup parent) {
			final ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.industry_comment_detail_list_item, null);
				viewHolder.namePicIv = (ImageView) convertView
						.findViewById(R.id.namePicIv);
				viewHolder.commentNameTv = (TextView) convertView
						.findViewById(R.id.commentNameTv);
				viewHolder.commentDataTv = (TextView) convertView
						.findViewById(R.id.commentDataTv);
				viewHolder.commentContentTv = (TextView) convertView
						.findViewById(R.id.commentContentTv);
				viewHolder.knowCommentLL = (LinearLayout) convertView
						.findViewById(R.id.knowCommentLL);
				viewHolder.commentMessageLv = (BasicListView) convertView
						.findViewById(R.id.commentMessageLv);
				viewHolder.commentMessageLv.setHaveScrollbar(false);
				viewHolder.commentMessageLv.setItemsCanFocus(false);
				viewHolder.commentMoreDataTV = (TextView) convertView
						.findViewById(R.id.commentMoreDataTV);
				viewHolder.commDividingLineIv = (ImageView) convertView
						.findViewById(R.id.commDividingLineIv);
				viewHolder.commentFooterLL = (LinearLayout) convertView
						.findViewById(R.id.commentFooterLL);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			try {
				/* 初始化知识回复 */
				KnowledgeComment knowComment = initKnowComment(position,
						viewHolder);
				/* 初始化回复评论 */
				ReplyCommentLvAdapter replyCommentLvAdapter = new ReplyCommentLvAdapter();
				replyCommentList = knowComment.listKnowledgeComment ;
				String name = knowComment.connectionsMini != null ? knowComment.connectionsMini.getName() : "";
				replyCommentLvAdapter.setData(replyCommentList,
						replyCommentList != null ? replyCommentList.size() : 0,
						name);
				if (indexArr.contains(position) 
						|| knowComment.count <= 2 || replyCommentList == null || replyCommentList.size() > 2) {
					viewHolder.commentMoreDataTV.setVisibility(View.GONE);
					viewHolder.commDividingLineIv.setVisibility(View.GONE);
				} else {
					viewHolder.commentMoreDataTV.setVisibility(View.VISIBLE);
					viewHolder.commDividingLineIv.setVisibility(View.VISIBLE);
				}
				viewHolder.commentMessageLv.setAdapter(replyCommentLvAdapter);
				EUtil.setListViewHeightBasedOnChildren(viewHolder.commentMessageLv);
			} catch (Exception ex) {
				Log.e(TAG, "获取评论评论失败");
				ex.printStackTrace();
			}
			viewHolder.knowCommentLL
					.setOnLongClickListener(new OnLongClickListener() {
						@Override
						public boolean onLongClick(View view) {
							popupWindowDismiss();
							showPopupWindow(parent, view);
							mKnowledgeComment = knowCommentsList.get(position);
							mParentId = mKnowledgeComment.id;
							indexComment = position;
							countComment = mKnowledgeComment.count;
							return false;
						}
					});
			viewHolder.knowCommentLL.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					viewPagerCon.setVisibility(View.GONE);
				}
			});

			viewHolder.commentMoreDataTV
					.setOnClickListener(new OnClickListener() {/* 查看全部评论 */
						@Override
						public void onClick(View arg0) {
							knowActionForward = CommendType.GetReplyCommend;
							mKnowledgeComment = knowCommentsList.get(position);
							viewHolder.commentMoreDataTV
									.setVisibility(View.GONE);
							viewHolder.commDividingLineIv
									.setVisibility(View.GONE);
							indexArr.add(position);
							mParentId = mKnowledgeComment.id;
							indexComment = position;
							countComment = mKnowledgeComment.count;
							/* 访问网络获 取全部的二级评论 */
							KnowledgeReqUtil.doGetKnowledgeComment(context,
									KnowledegeCommentDetailsFragment.this,
									mKnowledgeId, mParentId, 0, 20, null);
						}
					});
			return convertView;
		}

		/**
		 * 初始化知识回复数据
		 * 
		 * @param position
		 * @param viewHolder
		 * @return
		 */
		private KnowledgeComment initKnowComment(final int position,
				final ViewHolder viewHolder) {
			/* 回复知识详情 */
			KnowledgeComment knowComment = knowCommentsList.get(position);
			ConnectionsMini connectionsMini = knowComment.connectionsMini;
			String picUrl = connectionsMini != null ? connectionsMini
					.getImage() : "";
			if (!TextUtils.isEmpty(picUrl)) { /* 加载图片 */
				ImageLoader.getInstance().displayImage(picUrl,
						viewHolder.namePicIv,new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
						.bitmapConfig(Bitmap.Config.RGB_565)
						.showImageOnFail(R.drawable.ic_know_people).build());
			}
			viewHolder.commentNameTv
					.setText(connectionsMini != null ? connectionsMini
							.getName() : "");/* 评论知识详情的人名 */
			if (!TextUtils.isEmpty(knowComment.createtime)) {
				viewHolder.commentDataTv
						.setText(JTDateUtils.formatDate(
								knowComment.createtime,
								JTDateUtils.DATE_FORMAT_2));// 评论知识详情的时间
			}
			viewHolder.commentContentTv.setText(smileyParser(knowComment.content));/* 评论知识详情的内容 */
			return knowComment;
		}

		class ViewHolder {
			ImageView namePicIv;
			TextView commentNameTv;
			TextView commentDataTv;
			TextView commentContentTv;
			LinearLayout knowCommentLL;
			BasicListView commentMessageLv;
			TextView commentMoreDataTV;// 加载更多回复数据
			ImageView commDividingLineIv;
			LinearLayout commentFooterLL;
		}
	}

	/**
	 * 表情转换
	 * 
	 * @param str
	 * @return
	 */
	private CharSequence smileyParser(String str) {
		SmileyParser smileyParser;// 表情匹配
		SmileyParser2 smileyParser2;// 表情匹配
		smileyParser = SmileyParser.getInstance(context);
		smileyParser2 = SmileyParser2.getInstance(context);
		CharSequence charSequence1 = smileyParser.addSmileySpans(str);
		CharSequence charSequence2 = smileyParser2
				.addSmileySpans(charSequence1);
		return charSequence2;
	}

	/**
	 * 评论回复评论适配器
	 */
	class ReplyCommentLvAdapter extends BaseAdapter {
		private List<KnowledgeComment> replyCommentList;
		private int maxNum;
		private String commentName;

		private void setData(List<KnowledgeComment> replyCommentList,
				int maxNum, String commentName) {
			if (null == replyCommentList) {
				this.replyCommentList = new ArrayList<KnowledgeComment>();
			} else {
				this.replyCommentList = replyCommentList;
			}
			this.maxNum = maxNum;
			this.commentName = commentName;
		}

		@Override
		public int getCount() {
			if (replyCommentList.size() > 0) {
				return maxNum;
			}
			return 0;
		}

		@Override
		public Object getItem(int arg0) {
			return replyCommentList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MsgViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new MsgViewHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.comment_industry_list_item, null);
				viewHolder.commentMessageTv = (TextView) convertView
						.findViewById(R.id.reply_comment_content);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (MsgViewHolder) convertView.getTag();
			}
			KnowledgeComment knowComment = replyCommentList.get(position);
			ConnectionsMini connectionsMini = knowComment.connectionsMini;
			String replyCommentName = connectionsMini != null ? connectionsMini
					.getName() : "";// 评论知识详情人的姓名
			if (knowComment != null) {
				SpannableStringBuilder styleReply = doReplyCommentStyle(
						knowComment, replyCommentName);
				viewHolder.commentMessageTv.setText(styleReply);// 回复评论的内容
			}
			return convertView;
		}

		/**
		 * 处理子回复内容样式
		 * @param knowComment
		 * @param replyCommentName
		 * @return
		 */
		private SpannableStringBuilder doReplyCommentStyle(
				KnowledgeComment knowComment, String replyCommentName) {
			String content;
			SpannableStringBuilder styleReply;
			if (replyCommentName.equals(commentName)) {
				content = "    " + replyCommentName + " :  "
						+ knowComment.content;
				styleReply = new SpannableStringBuilder(content);
				styleReply.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.common_orange)), 0,
						content.indexOf(":") - 1,
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			} else {
				content = "    " + replyCommentName + " 回复 " + commentName
						+ "  :  " + knowComment.content;
				styleReply = new SpannableStringBuilder(content);
				styleReply.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.common_orange)), 0,
						content.indexOf("回"),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				styleReply.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.common_orange)),
						content.indexOf("复") + 1, content.indexOf(":"),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			return styleReply;
		}

		class MsgViewHolder {
			TextView commentMessageTv;
		}
	}	

	@Override
	public void onPause() {
		popupWindowDismiss();
		super.onPause();
	}

	@Override
	public void onResume() {
		hideKeyboard();// 隐藏软键盘
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 隐藏软键盘
	 */
	public void hideKeyboard() {
		try {
			InputMethodManager imm = (InputMethodManager) context
					.getSystemService(context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(textEt.getWindowToken(), 0);
		} catch (Exception e) {
		}
	}

	/**
	 * 显示软键盘
	 */
	public void showKeyboard() {
		try {
			InputMethodManager manager = (InputMethodManager) textEt
					.getContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
		}
	}

	/**
	 * 显示表情适配器
	 */
	class PageViewAdpter extends PagerAdapter {

		@Override
		public int getCount() {
			return listSmileyViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(listSmileyViews.get(position));
			return listSmileyViews.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(listSmileyViews.get(position));
		}
	}

	/**
	 * 处理系统Back键
	 */
	public void onBackPressed() {
		getActivity().finish();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		lastItem = firstVisibleItem + visibleItemCount - 1; // 减1是因为上面加了个addFooterView
		popupWindowDismiss();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (lastItem == count && scrollState == this.SCROLL_STATE_IDLE) {
			moreView.setVisibility(view.VISIBLE);
			loadMoreData();
		}
	}

	// 出发下拉刷新
	public void onRefresh() {
			startGetData(index);
	}

	/**
	 * 获取页数据
	 */
	public boolean startGetData(int pageIndex) {
		knowActionForward = CommendType.GetKnowCommend;
		if (mKnowledgeId <= 0) {
			return false;
		}
		KnowledgeReqUtil.doGetKnowledgeComment(context, this, mKnowledgeId, 0L,
				pageIndex, size, null);
		return true;
	}
	
	/**
	 * 加载完显示
	 */
	public void stopLoading() {}
}
