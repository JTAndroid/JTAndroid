package com.tr.ui.home;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.App;
import com.tr.R;
import com.tr.api.HomeReqUtil;
import com.tr.api.UserReqUtil;
import com.tr.image.ImageLoader;
import com.tr.model.api.DataBox;
import com.tr.model.obj.DynamicComment;
import com.tr.model.obj.DynamicNews;
import com.tr.model.obj.DynamicPraise;
import com.tr.model.user.JTMember;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.frg.DynamicCommentAdapter;
import com.tr.ui.home.frg.FrgFlow;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.widgets.BasicListView;
import com.tr.ui.widgets.CommonSmileyParser;
import com.tr.ui.widgets.KnoTagGroupView;
import com.tr.ui.widgets.SmileyView;
import com.utils.common.EUtil;
import com.utils.common.Util;
import com.utils.display.DisplayUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.image.LoadImage;
import com.utils.time.TimeUtil;

/**
 * @Filename DynamicCommentActivity.java
 * @Author CJJ
 * @description 动态回复页面
 */
public class DynamicCommentActivity extends JBaseFragmentActivity implements
		View.OnClickListener {
	private final String TAG = "DynamicCommentActivity";
	private DynamicCommentAdapter mDynamicCommentAdapter;
	private List<DynamicComment> mListComment;
	private DynamicNews mDynamicNews;
	private BasicListView mHomeDynamicCommentLv;
	
	private ArrayList<DynamicPraise> mDynamicPraiseList;
	private DynamicPraise mDynamicPraise;
	boolean isPraised;//标记点赞状态
	
	/* 人物头像 */
	private ImageView mImgHead;
	/* 人物头像文字 */
	private TextView mImgHeadTv;
	/**转发中人物头像文字*/
	private TextView mFlowFigureImgTv;
	/* 来源 */
	private TextView mFlowSourceUserNameTv;
	/* 权限 */
	private TextView mFlowSourcePowerTv;
	/* 回复数量 */
	private TextView mHFCommentNum;
	/* 评论输入文本 */
	private EditText mHACommentEt;
	/* 评论内容（发送失败时保存用户输入） */
	private String mComment;
	/* 标记父窗体 */
	private Activity mParentActivity;
	/* "1-需求；2-业务需求；3-任务；4-项目；" */
	private int mType;
	/* true:显示SmileyView；false 显示软键盘 */
	private Boolean isShowface = false;
	private LinearLayout viewPagerCon;
	private ViewPager faceViewPager;
	private ImageView smileyPagerchange;
	private SmileyView smileyViewFirst;
	private SmileyView smileyViewSecond;
	private ImageView homeFrgPicIv;
	private ImageView mFlowFigureImgIv;
	private ImageView mFlowPublishedImgIv;
	private View homeBottomView;
	private IndexDynamicOnClickLister myOnClickListener;
	private LinearLayout mHomeFlowFigureLl;
	private TextView mFlowPublishedCommonTitleTv;
	private TextView mFlowFigureNameTv;
	private TextView mHomeFlowUserOpinionTv;
	private TextView mFlowPublishedContentTv;
	private TextView mFlowPublishedCommonHintTimeTv;
	private TextView mFlowFigureContentTv;
	private LinearLayout mHomeFlowPublishedCommonLl;
	private LinearLayout mHomeFlowPublishedContentLl;
	private int mPosition = 0;
	
	//popupwindow  VIew
		private View inflateDeleteView;
		private TextView deleteTv;
		private TextView copyTv;
		private PopupWindow window;
		
		private int measuredHeigh;
		private int measuredWidth;
		private int linmitHeight;

	public DynamicCommentActivity() {
	}

	public DynamicCommentActivity(List<DynamicComment> mListComment,
			DynamicNews mDynamicNews) {
		this.mListComment = mListComment;
		this.mDynamicNews = mDynamicNews;
		this.mParentActivity = DynamicCommentActivity.this;
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.home_frg_comment);
		commentLL = (LinearLayout) findViewById(R.id.commentLL);
		mImgHead = (ImageView) findViewById(R.id.flow_user_img_iv);
		mImgHeadTv = (TextView) findViewById(R.id.flow_user_img_tv);
		mFlowFigureImgTv = (TextView) findViewById(R.id.flow_figure_img_tv);
		mFlowSourceUserNameTv = (TextView) findViewById(R.id.flow_source_user_name_tv);
		mFlowSourcePowerTv = (TextView) findViewById(R.id.flow_source_power_tv);
		mHFCommentNum = (TextView) findViewById(R.id.flow_published_common_hint_comment_num_tv);
		mHACommentEt = (EditText) findViewById(R.id.homeFrgCommentEt);
		mHomeDynamicCommentLv = (BasicListView) findViewById(R.id.homeListViewComment);
		viewPagerCon = (LinearLayout) findViewById(R.id.industrySmileyLL);
		faceViewPager = (ViewPager) findViewById(R.id.industrySmileyPager);
		smileyPagerchange = ((ImageView) findViewById(R.id.smileyPagerchange));
		homeFrgPicIv = (ImageView) findViewById(R.id.homeFrgPicIv);
		homeBottomView = (View) findViewById(R.id.homeBottomView);
		mHomeFlowFigureLl = (LinearLayout) findViewById(R.id.home_flow_figure_ll);
		mFlowPublishedCommonTitleTv = (TextView) findViewById(R.id.flow_published_common_title_tv);
		mHomeFlowPublishedCommonLl = (LinearLayout) findViewById(R.id.home_flow_published_common_ll);
		mHomeFlowUserOpinionTv = (TextView) findViewById(R.id.home_flow_user_opinion_tv);
		mFlowPublishedImgIv = (ImageView) findViewById(R.id.flow_published_img_iv);
		mFlowPublishedContentTv = (TextView) findViewById(R.id.flow_published_content_tv);
		mHomeFlowPublishedContentLl = (LinearLayout) findViewById(R.id.home_flow_published_content_ll);
		mFlowFigureNameTv = (TextView) findViewById(R.id.flow_figure_name_tv);
		mFlowFigureImgIv = (ImageView) findViewById(R.id.flow_figure_img_iv);
		mFlowFigureContentTv = (TextView) findViewById(R.id.flow_figure_content_tv);
		mFlowPublishedCommonHintTimeTv = (TextView) findViewById(R.id.flow_published_common_hint_time_tv);
		mFlowHeartPressLL = (LinearLayout) findViewById(R.id.flowHeartPressLL);
		mFlowHeartPeopleCL = (KnoTagGroupView) findViewById(R.id.flowHeartPeopleCL);
		mFlowHeartTv = (TextView) findViewById(R.id.flowHeartTv);
		
		inflateDeleteView = View.inflate(this, R.layout.layout_sociality_delete, null);
		this.window = new PopupWindow(inflateDeleteView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		window.setOutsideTouchable(true);
		deleteTv = (TextView) inflateDeleteView.findViewById(R.id.delete);
		copyTv = (TextView) inflateDeleteView.findViewById(R.id.save);
		
		inflateDeleteView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		measuredWidth = inflateDeleteView.getMeasuredWidth();
		measuredHeigh = inflateDeleteView.getMeasuredHeight();
		Rect frame = new Rect();
		this.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		int actionBarHeight = this.getActionBar().getHeight();
		linmitHeight = statusBarHeight + actionBarHeight;

		homeFrgPicIv.setOnClickListener(this);
		homeBottomView.setVisibility(View.GONE);
		this.mDynamicNews = (DynamicNews) getIntent().getSerializableExtra(
				ENavConsts.KEY_FRG_FLOW_COMMENT);
		this.mPosition = (Integer) getIntent().getExtras().get(
				ENavConsts.KEY_FRG_FLOW_INDEX);
		
		if (null != mDynamicNews) {
			mListComment = mDynamicNews.getComments();
			this.mDynamicPraiseList = mDynamicNews.getmDynamicPraiseList();
		} else {
			mDynamicNews = new DynamicNews();
			this.mDynamicPraiseList = new ArrayList<DynamicPraise>();
		}
		
		initDynamicPublisherAvater(mDynamicNews, mImgHead, mImgHeadTv);
		
		mFlowHeartTv.setOnClickListener(this);
		/*点赞功能        ----    需要显示点赞时打开    mFlowHeartPressLL.setVisibility(View.VISIBLE);设为显示*/
//		showDynamicPraise();
		mFlowHeartPressLL.setVisibility(View.GONE);
		if (mListComment == null) {
			mListComment = new ArrayList<DynamicComment>();
		}
		/* 时间间隔 */
		String strTime = "";
		if (!TextUtils.isEmpty(mDynamicNews.getCtime())) {
			strTime = TimeUtil.TimeFormat(mDynamicNews.getCtime());
			mFlowPublishedCommonHintTimeTv.setText(strTime);
		} 
		/* 发布和转发的标题 */
		mFlowPublishedCommonTitleTv.setText(mDynamicNews.getTitle());

		initSourcePower();
		int commentCount = mListComment.size();
		if (commentCount > 0) {
			Drawable drawable= getResources().getDrawable(R.drawable.commet_num_more);
			/// 这一步必须要做,否则不会显示.
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			mHFCommentNum.setCompoundDrawables(drawable,null,null,null);
			mHFCommentNum.setTextColor(getResources().getColor(R.color.home_index_text_on_bg));
			mHFCommentNum.setText("  " + commentCount);
		} else {
			Drawable drawable= getResources().getDrawable(R.drawable.commet_num);
			/// 这一步必须要做,否则不会显示.
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			mHFCommentNum.setCompoundDrawables(drawable,null,null,null);
			mHFCommentNum.setTextColor(getResources().getColor(R.color.find_project_txt_gray));
			mHFCommentNum.setText("  0");
		}
		mType = 1;
		initJabActionBar();

		mHFSendCommentIv = (ImageView) findViewById(R.id.homeFrgSendCommentIv);
		mHFSendCommentIv.setOnClickListener(this);

		mDynamicCommentAdapter = new DynamicCommentAdapter(this,window);
		mDynamicCommentAdapter.setCountLayout(mHFCommentNum);
		mDynamicCommentAdapter.setData(mListComment, mDynamicNews,
				mListComment.size(),
				DynamicCommentAdapter.CommentShowType.CommentShowAll);
		mDynamicCommentAdapter.setPopUpWindowViewConfig(inflateDeleteView,deleteTv,copyTv,measuredHeigh,measuredWidth,linmitHeight);
		mHomeDynamicCommentLv.setAdapter(mDynamicCommentAdapter);
		mHomeDynamicCommentLv.setHaveScrollbar(false);
		mHomeDynamicCommentLv.setItemsCanFocus(false);
		mHomeDynamicCommentLv.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		EUtil.setListViewHeightBasedOnChildren(mHomeDynamicCommentLv);

		mHACommentEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				mDynamicNews.setInputText(s.toString());
			}
		});

		mHACommentEt.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				viewPagerCon.setVisibility(View.GONE);
				isShowface = false;// 显示软盘
				return false;
			}
		});

		initExpression();
		myOnClickListener = new IndexDynamicOnClickLister(mDynamicNews, this);
		mHomeFlowFigureLl.setOnClickListener(myOnClickListener);
		mFlowPublishedCommonTitleTv.setOnClickListener(myOnClickListener);
		mHomeFlowPublishedCommonLl.setOnClickListener(myOnClickListener);

		mHACommentEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				editTextCaptureFocus();
			}
		});

		UserReqUtil.doGetDynamicListComment(
				this,
				mBindData,
				UserReqUtil.getDoGetDynamicListCommentParams(
						mDynamicNews.getId(), 0, 20), null);
		
		commentLL.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (window!=null&&window.isShowing()) {
					window.dismiss();
				}
				return false;
			}
		});
	}

	private void showDynamicPraise() {
		if (mDynamicPraiseList!=null) {
			mFlowHeartPeopleCL.removeAllViews();
			if (mDynamicPraiseList.size()<=0) {
				mFlowHeartPressLL.setVisibility(View.GONE);
			}
			for (int i = 0; i < mDynamicPraiseList.size(); i++) {
				final DynamicPraise dynamicPraise = mDynamicPraiseList.get(i);
				TextView textView = new TextView(this);
				textView.setTextColor(R.color.home_index_dynamic_text_color);
				textView.setText(dynamicPraise.getpName());
				mFlowHeartPeopleCL.addView(textView, 0);
				if (i!=mDynamicPraiseList.size()-1) {
					TextView textView2 = new TextView(this);
					textView2.setTextColor(R.color.home_index_dynamic_text_color);
					textView2.setText(" , ");
					mFlowHeartPeopleCL.addView(textView2,0);
				}
				
				textView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (App.getUserID().equals(String.valueOf(dynamicPraise.getpId()))) {
							ENavigate.startRelationHomeActivity(DynamicCommentActivity.this, String.valueOf(dynamicPraise.getpId()), true, ENavConsts.type_details_member);
						}else {
							ENavigate.startRelationHomeActivity(DynamicCommentActivity.this, String.valueOf(dynamicPraise.getpId()), true, ENavConsts.type_details_other);
						}
					}
				});
				
				if (App.getUserID().equals(String.valueOf(dynamicPraise.getpId()))) {
					//已点赞
					mDynamicPraise = dynamicPraise;
					isPraised=true;
					Drawable drawable = getResources().getDrawable(R.drawable.heart_pressed);
					drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
					mFlowHeartTv.setCompoundDrawables(drawable,null,null,null);
				}
				
			}
			
		}
		else {
			mFlowHeartPressLL.setVisibility(View.GONE);
		}
		
	}
	
	private void initSourcePower() {
		switch (mDynamicNews.getType()) {
		case DynamicNews.TYPE_CREATE_KNOWLEDGE: /* 发布知识 */
			String hintTitle ="";
			hintTitle = FrgFlow.switchHintTitle(hintTitle, mDynamicNews.getPtype());
			mFlowSourcePowerTv.setText(hintTitle);
			publishUI();
			initKnowAndMeetData();
			break;
		case DynamicNews.TYPE_RECOMMEND_KNOWLEDGE: /* 金桐推荐知识 */
			hintTitle = "金桐脑推荐了知识";
			mFlowSourcePowerTv.setText(hintTitle);
			publishGinTongUI();
			initKnowAndMeetData();
			break;
		case DynamicNews.TYPE_CREATE_REQUIREMENT:/* 创建需求 */
			String demandTitle = "";
			demandTitle = FrgFlow.switchHintTitleDemand(demandTitle, mDynamicNews.getPtype()) + FrgFlow.switchDemandType(mDynamicNews.getLowType());
			mFlowSourcePowerTv.setText(demandTitle);
			publishUI();
			initKnowAndMeetData();
			break;
		case DynamicNews.TYPE_FORWARDING_KNOWLEDGE: /* 转发知识 */
			mFlowSourcePowerTv.setText("转发了知识");
			forwardingUI();
			initKnowAndMeetData();
			break;
		case DynamicNews.TYPE_FORWARDING_REQUIREMENT:/* 转发需求 */
			mFlowSourcePowerTv.setText("转发了需求");
			forwardingUI();
			initKnowAndMeetData();
			break;
		case DynamicNews.TYPE_RECOMMEND_REQUIREMENT: /* 金桐推荐需求 */
			hintTitle = "金桐脑推荐了" + FrgFlow.switchDemandType(mDynamicNews.getLowType());
			mFlowSourcePowerTv.setText(hintTitle);
			publishGinTongUI();
			initKnowAndMeetData();
			break;
		case DynamicNews.TYPE_CREATE_MEETING:/* 发布会议 */
			mFlowSourcePowerTv.setText("大乐了会议");
			publishUI();
			initKnowAndMeetData();
			break;
		case DynamicNews.TYPE_FORWARDING_MEETING:/* 转发会议 */
			mFlowSourcePowerTv.setText("转发了会议");
			forwardingUI();
			initKnowAndMeetData();
			break;
		case DynamicNews.TYPE_USER_CARD:/* 用户名片 */
			mFlowSourcePowerTv.setText("转发了用户");
			forwardingUI();
			initFigureData();
			break;
	/*	case DynamicNews.TYPE_CREATE_ORGANIZATION:创建组织
			demandTitle = "";
			demandTitle = FrgFlow.switchHintTitleDemand(demandTitle, mDynamicNews.getPtype()) + FrgFlow.switchDemandType(mDynamicNews.getLowType());
			mFlowSourcePowerTv.setText(demandTitle);
			hintTitle = "";
			if (FlowType.HomePageFlow.equals(flowType)) {
				hintTitle = "创建了组织";
			} else if (FlowType.OtherPeopleFlow.equals(flowType)) {
				hintTitle = "创建了组织";
			} else {
				hintTitle = FrgFlow.switchHintTitleDemand(hintTitle, mDynamicNews.getPtype()) + FrgFlow.switchDemandType(mDynamicNews.getLowType());
			}
			forwardingUI();
			initFigureData();
			break;*/
		case DynamicNews.TYPE_FORWARDING_ORGANIZATION:/*转发组织*/
			mFlowSourcePowerTv.setText("转发了组织");
			forwardingUI();
			initFigureData();
			break;
		case DynamicNews.TYPE_CREATE_CUSTOM:/*创建客户*/
			demandTitle = "";
			demandTitle = FrgFlow.switchHintTitleDemand(demandTitle, mDynamicNews.getPtype()) + "客户";
			mFlowSourcePowerTv.setText(demandTitle);
			forwardingUI();
			initFigureData();
			break;
		case DynamicNews.TYPE_FORWARDING_CUSTOM:/*转发客户*/
			mFlowSourcePowerTv.setText("转发了客户");
			forwardingUI();
			initFigureData();
			break;
		case DynamicNews.TYPE_CREATE_CONTACTS:/*创建人脉*/
			demandTitle = "";
			demandTitle = FrgFlow.switchHintTitleDemand(demandTitle, mDynamicNews.getPtype()) + "人脉";
			mFlowSourcePowerTv.setText(demandTitle);
			forwardingUI();
			initFigureData();
			break;
		case DynamicNews.TYPE_FORWARDING_CONTACTS:/*转发人脉*/
			mFlowSourcePowerTv.setText("转发了人脉");
			forwardingUI();
			initFigureData();
			break;
		case DynamicNews.TYPE_RECOMMEND_CONTACTS:/*金桐推荐人脉*/
			demandTitle = "";
			demandTitle = "金桐脑推荐了人脉";
			mFlowSourcePowerTv.setText(demandTitle);
			forwardingUI();
			initFigureData();
			break;
		/*case DynamicNews.TYPE_ORGANIZATION_CARD: 组织名片 
			mFlowSourcePowerTv.setText("转发组织名片");
			forwardingUI();
			initFigureData();
			break;
		case DynamicNews.TYPE_CUSTOMER_CARD: 客户名片 
			mFlowSourcePowerTv.setText("转发客户名片");
			forwardingUI();
			initFigureData();
			break;*/
		case DynamicNews.TYPE_RECOMMEND_CUSTOMER:/*金桐推荐用户*/
			hintTitle = "金桐脑推荐了用户";
			mFlowSourcePowerTv.setText(hintTitle);
			forwardingUI();
			initFigureData();
			break;
		case DynamicNews.TYPE_RECOMMEND_ORGANIZATION:// 大数据推送的0客户 1组织　2未注册组织
			hintTitle = "金桐脑推荐的组织";
			forwardingUI();
			initFigureData();
			break;
		case DynamicNews.TYPE_RECOMMEND_CUSTOM:// 大数据推送的0客户 1组织　2未注册组织
			hintTitle = "金桐脑推荐的客户";
			forwardingUI();
			initFigureData();
			break;
		default:
		
		}
		/**
		 * 根据不同的数据类型进行相应的布局展示
		 */
//		switch (mDynamicNews.getType()) {
//		case DynamicNews.TYPE_CREATE_KNOWLEDGE: /* 发布知识 */
//			initKnowAndMeetData();
//			break;
//		case DynamicNews.TYPE_FORWARDING_KNOWLEDGE: /* 转发知识 */
//			forwardingUI("转发了知识");
//			initKnowAndMeetData();
//			break;
//		case DynamicNews.TYPE_CREATE_MEETING:/* 发布会议 */
//			publishUI("大乐了会议");
//			initKnowAndMeetData();
//			break;
//		case DynamicNews.TYPE_FORWARDING_MEETING:/* 转发会议 */
//			forwardingUI("转发了会议");
//			initKnowAndMeetData();
//			break;
//		case DynamicNews.TYPE_USER_CARD:/* 用户名片 */
//			forwardingUI("转发用户名片");
//			initFigureData();
//			break;
//		case DynamicNews.TYPE_FORWARDING_REQUIREMENT:/* 用户需求 */
//			forwardingUI("转发用户需求");
//			initDemandData();
//			break;
//		case DynamicNews.TYPE_CREATE_REQUIREMENT:/* 创建用户需求 */
////			forwardingUI("创建用户需求");
//			String hintTitle = "";
//			hintTitle = FrgFlow.switchHintTitleDemand(hintTitle,mDynamicNews.getPtype()) + FrgFlow.switchDemandType(mDynamicNews.getLowType());
//			mFlowSourcePowerTv.setText(hintTitle);
//			initDemandData();
//			break;	
//		case DynamicNews.TYPE_CREATE_ORGANIZATION:/*创建组织*/
//			hintTitle = "";
//			hintTitle = FrgFlow.switchHintTitleDemand(hintTitle, mDynamicNews.getPtype()) + FrgFlow.switchDemandType(mDynamicNews.getLowType());
//			mFlowSourcePowerTv.setText(hintTitle);
//			initFigureData();
//			break;
//		case DynamicNews.TYPE_FORWARDING_ORGANIZATION:/*转发组织*/
//			initFigureData();
//			break;
//		case DynamicNews.TYPE_CREATE_CUSTOM:/*创建客户*/
//			forwardingUI();
//			initFigureData();
//			break;
//		case DynamicNews.TYPE_FORWARDING_CUSTOM:/*转发客户*/
//			forwardingUI();
//			initFigureData();
//			break;
//		case DynamicNews.TYPE_CREATE_CONTACTS:/*创建人脉*/
//			forwardingUI();
//			initFigureData();
//			break;
//		case DynamicNews.TYPE_FORWARDING_CONTACTS:/*转发人脉*/
//			forwardingUI();
//			initFigureData();
//			break;
//		case DynamicNews.TYPE_RECOMMEND_CONTACTS:/*金桐推荐人脉*/
//			forwardingUI();
//			initFigureData();
//			break;
//		case DynamicNews.TYPE_ORGANIZATION_CARD:/* 组织名片 */
//			forwardingUI();
//			initFigureData();
//			break;
//		case DynamicNews.TYPE_RECOMMEND_CUSTOMER:/*金桐推荐客户*/
//			forwardingUI();
//			initFigureData();
//			break;
//		case DynamicNews.TYPE_RECOMMEND_KNOWLEDGE: /* 金桐推荐知识 */
//			initKnowAndMeetData();
//			break;
//		default:
//		}
	}

	/**
	 * 初始化需求数据
	 * 
	 * @param holder
	 */
	private void initDemandData() {
		
		/* 设置头像 */
//		String imgPath = Util.getHeadImgPath(mDynamicNews.getCreaterId(),mDynamicNews.getPicPath());
//		ImageLoader.getInstance().displayImage(mDynamicNews.getPicPath(),
//				mImgHead, LoadImage.mDefaultHead);
		/* 设置来源 */
		mFlowSourceUserNameTv.setText(mDynamicNews.getCreaterName());
		
		//设置介绍内容
		mFlowPublishedContentTv.setText(mDynamicNews.getContent());
		
		/*动态标题态标题题*/
		mFlowPublishedCommonTitleTv.setText(mDynamicNews.getTitle());
		
		/*内容图片*/
		mFlowPublishedImgIv.setVisibility(View.GONE);
	
	}

	/**
	 * 转发UI
	 */
//	public void forwardingUI(String SourcePowerStr) {
////		mHomeFlowUserOpinionLl.setVisibility(View.VISIBLE);
////		mFlowSourcePowerTv.setText(SourcePowerStr);
////
////		if (mDynamicNews.getType() == DynamicNews.TYPE_USER_CARD) {/* 用户名片 */
////			mHomeFlowFigureLl.setVisibility(View.VISIBLE);
////			mHomeFlowPublishedCommonLl.setVisibility(View.GONE);
////		} else {
////			mHomeFlowFigureLl.setVisibility(View.GONE);
////			mHomeFlowPublishedCommonLl.setVisibility(View.VISIBLE);
////		}
//
//		mHomeFlowUserOpinionLl.setVisibility(View.VISIBLE);
//		if (mDynamicNews.getType() == DynamicNews.TYPE_USER_CARD || /* 用户名片 */
//		mDynamicNews.getType() == DynamicNews.TYPE_FORWARDING_ORGANIZATION || mDynamicNews.getType() == DynamicNews.TYPE_CREATE_ORGANIZATION || mDynamicNews.getType() == DynamicNews.TYPE_CREATE_CUSTOM || mDynamicNews.getType() == DynamicNews.TYPE_FORWARDING_CUSTOM || mDynamicNews.getType() == DynamicNews.TYPE_CREATE_CONTACTS || mDynamicNews.getType() == DynamicNews.TYPE_FORWARDING_CONTACTS || mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_CONTACTS || mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_CUSTOMER || mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_ORGANIZATION/**
//		 * 
//		 * 
//		 * 金桐脑推荐的组织或客户
//		 */
//		|| mDynamicNews.getType() == DynamicNews.TYPE_CREAT_AFFAIRS || mDynamicNews.getType() == DynamicNews.TYPE_FORWARDING_AFFAIRS || mDynamicNews.getType() == DynamicNews.TYPE_CUSTOMER_CARD) {
//			mHomeFlowFigureLl.setVisibility(View.VISIBLE);
//			mHomeFlowPublishedCommonLl.setVisibility(View.GONE);
//		} else {
//			mHomeFlowFigureLl.setVisibility(View.GONE);
//			mHomeFlowPublishedCommonLl.setVisibility(View.VISIBLE);
//		}
//	
//	}

	/**
	 * 发布UI
	 */
	public void publishUI(/*String SourcePowerStr*/) {
//		mHomeFlowUserOpinionLl.setVisibility(View.GONE);
//		mFlowSourcePowerTv.setText(SourcePowerStr);
//		mHomeFlowPublishedCommonLl.setVisibility(View.VISIBLE);
//		mHomeFlowFigureLl.setVisibility(View.GONE);
		
		mHomeFlowUserOpinionTv.setVisibility(View.GONE);
		mHomeFlowPublishedCommonLl.setVisibility(View.VISIBLE);
		mHomeFlowFigureLl.setVisibility(View.GONE);
		mFlowSourceUserNameTv.setVisibility(View.VISIBLE);
	}
	/**
	 * 发布UI
	 */
	public void publishGinTongUI() {
		mHomeFlowUserOpinionTv.setVisibility(View.GONE);
		mHomeFlowPublishedCommonLl.setVisibility(View.VISIBLE);
		mHomeFlowFigureLl.setVisibility(View.GONE);
		mFlowSourceUserNameTv.setVisibility(View.GONE);
	}
	/**
	 * 转发UI
	 */
	public void forwardingUI() {
		mHomeFlowUserOpinionTv.setVisibility(View.VISIBLE);
		if (mDynamicNews.getType() == DynamicNews.TYPE_USER_CARD || /* 用户名片 */
		mDynamicNews.getType() == DynamicNews.TYPE_FORWARDING_ORGANIZATION 
				|| mDynamicNews.getType() == DynamicNews.TYPE_CREATE_CUSTOM || mDynamicNews.getType() == DynamicNews.TYPE_FORWARDING_CUSTOM
				|| mDynamicNews.getType() == DynamicNews.TYPE_CREATE_CONTACTS || mDynamicNews.getType() == DynamicNews.TYPE_FORWARDING_CONTACTS
				|| mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_CONTACTS || mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_CUSTOMER
				|| mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_ORGANIZATION) {
			mHomeFlowFigureLl.setVisibility(View.VISIBLE);
			mHomeFlowPublishedCommonLl.setVisibility(View.GONE);
		} else {
			mHomeFlowFigureLl.setVisibility(View.GONE);
			mHomeFlowPublishedCommonLl.setVisibility(View.VISIBLE);
		}
	
	}
	
	/**
	 * 初始化人脉和名片的数据
	 * 
	 * @param holder
	 */
	private void initFigureData() {
		/* 设置头像 */
//		String imgPath = Util.getHeadImgPath(mDynamicNews.getCreaterId(),mDynamicNews.getPicPath());
//		ImageLoader.getInstance().displayImage(mDynamicNews.getPicPath(),
//				mImgHead, LoadImage.mDefaultHead);
//		/* 设置来源 */
//		mFlowSourceUserNameTv.setText(mDynamicNews.getCreaterName());
//		/* 设置名片头像 */
//		ImageLoader.getInstance().displayImage(mDynamicNews.getImgPath(),
//				mFlowFigureImgIv, LoadImage.mDefaultHead);
//		/* 人脉和名片姓名 */
//		mFlowFigureNameTv.setText(mDynamicNews.getTitle());
//		/* 人脉和名片内容 */
//		if(DynamicNews.TYPE_CREATE_CUSTOM == mDynamicNews.getType()){
//			String contentString = mDynamicNews.getContent().replace("#"," ");
//			mFlowFigureContentTv.setText(contentString);
//		}else{
//			/* 人脉和名片内容 */
//			mFlowFigureContentTv.setText(mDynamicNews.getContent());
//		}
//		// 转发者的观点
//		if (TextUtils.isEmpty(mDynamicNews.getForwardingContent())) {
//			mHomeFlowUserOpinionLl.setVisibility(View.GONE);
//		} else {
//			mHomeFlowUserOpinionTv.setText(EUtil.commonSmileyParser(mDynamicNews.getForwardingContent(),this));
//		}
		

//		if (mDynamicNews.getCreaterId() != 0) {
//			/* 设置个人头像 */
//			HomeReqUtil.getDynamicNewLowType((Context) mActivity, FrgFlow.this, null, mDynamicNews.getCreaterId());
//		}
//		if (type == 2) {// 组织跳转
//			ImageLoader.getInstance().displayImage(mDynamicNews.getPicPath(), holder.mImgHead, LoadImage.mOrganizationDefaultHead);
//			// ImageLoader.load(holder.mImgHead, mDynamicNews.getPicPath(),
//			// R.drawable.default_portrait116);
//
//		} else {// 个人主页
//			ImageLoader.getInstance().displayImage(mDynamicNews.getPicPath(), holder.mImgHead, LoadImage.mDefaultHead);
//			// ImageLoader.load(holder.mImgHead, mDynamicNews.getPicPath(),
//			// R.drawable.ic_default_avatar);
//		}
		/* 设置来源 */
		// holder.mFlowSourceUserNameTv.setText(mDynamicNews.getCreaterName());
		if (mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_CONTACTS || mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_REQUIREMENT
				|| mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_KNOWLEDGE || mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_CUSTOMER) {
			mFlowSourceUserNameTv.setVisibility(View.GONE);
		} else {
			/* 设置来源 */
			mFlowSourceUserNameTv.setText(mDynamicNews.getCreaterName());
			mFlowSourceUserNameTv.setVisibility(View.VISIBLE);
		}

		/* 设置头像 */
		if (mDynamicNews.getType() == DynamicNews.TYPE_FORWARDING_ORGANIZATION
				|| mDynamicNews.getType() == DynamicNews.TYPE_CREATE_CUSTOM || mDynamicNews.getType() == DynamicNews.TYPE_FORWARDING_CUSTOM
				|| mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_ORGANIZATION) {
			/* 客户组织设置头像 */
			Util.initAvatarImage(this, mFlowFigureImgIv, mDynamicNews.getTitle(),  mDynamicNews.getImgPath(), 0, 2);
		} else if (mDynamicNews.getType() == DynamicNews.TYPE_CREATE_CONTACTS
				|| mDynamicNews.getType() == DynamicNews.TYPE_FORWARDING_CONTACTS || mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_CONTACTS
				|| mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_CUSTOMER
				|| mDynamicNews.getType() == DynamicNews.TYPE_USER_CARD) {
			/* 人脉好友设置头像 */
			Util.initAvatarImage(this, mFlowFigureImgIv, mDynamicNews.getTitle(),  mDynamicNews.getImgPath(), 0, 1);
		}

		/* 人脉和名片姓名 */
		mFlowFigureNameTv.setText(mDynamicNews.getTitle());
		if (DynamicNews.TYPE_RECOMMEND_ORGANIZATION == mDynamicNews.getType()) {
			if (!"#".equals(mDynamicNews.getContent())) {
				String contentString = mDynamicNews.getContent().replace("#", " ");
				mFlowFigureContentTv.setText(contentString);
			}
		} else {
			/* 人脉和名片内容 */
			mFlowFigureContentTv.setText(mDynamicNews.getContent());
		}

		// 转发者的观点
		if (EUtil.isEmpty(mDynamicNews.getForwardingContent())) {
			mHomeFlowUserOpinionTv.setVisibility(View.GONE);
		} else {
			mHomeFlowUserOpinionTv.setText(EUtil.commonSmileyParser(mDynamicNews.getForwardingContent(),this));
		}

	
	}

	/**
	 * 初始化知识和会议的数据
	 * 
	 * @param holder
	 */
	private void initKnowAndMeetData() {
//		ImageLoader.getInstance().displayImage(mDynamicNews.getPicPath(), mImgHead, LoadImage.mDefaultFlowHead);
		if (mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_CONTACTS || mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_REQUIREMENT
				|| mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_KNOWLEDGE || mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_CONTACTS
				|| mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_CUSTOMER) {
			mFlowSourceUserNameTv.setVisibility(View.GONE);
		} else {
			/* 设置来源 */
			mFlowSourceUserNameTv.setText(mDynamicNews.getCreaterName());
			mFlowSourceUserNameTv.setVisibility(View.VISIBLE);
		}

		/* 设置权限描述 */
		// holder.mFlowSourcePowerTv.setText(mDynamicNews.get);
		/* 人物的观点 */
		if (TextUtils.isEmpty(mDynamicNews.getForwardingContent())) {
			mHomeFlowUserOpinionTv.setVisibility(View.GONE);
		} else {
			mHomeFlowUserOpinionTv.setText(EUtil.commonSmileyParser(mDynamicNews.getForwardingContent(),this));
			mHomeFlowUserOpinionTv.setVisibility(View.VISIBLE);
		}
		/* 发布和转发内容的标题 */
		if (!EUtil.isEmpty(mDynamicNews.getTitle())) {
			mFlowPublishedCommonTitleTv.setText(mDynamicNews.getTitle());
			mFlowPublishedCommonTitleTv.setVisibility(View.VISIBLE);
		} else {
			mFlowPublishedCommonTitleTv.setVisibility(View.GONE);
		}
		/* 发布或转发内容图片 */
		if (!EUtil.isEmpty(mDynamicNews.getImgPath().trim())) {
			ImageLoader.load(mFlowPublishedImgIv, mDynamicNews.getImgPath(), R.drawable.hy_chat_right_pic);
			mFlowPublishedImgIv.setVisibility(View.VISIBLE);
		} else {
			mFlowPublishedImgIv.setVisibility(View.GONE);
			android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
			int dip10 = DisplayUtil.dip2px(DynamicCommentActivity.this, 10);
			params.setMargins(dip10, dip10, dip10, dip10);
			mHomeFlowPublishedContentLl.setLayoutParams(params);
		}
		/* 发布或转发内容 */
		if (!EUtil.isEmpty(mDynamicNews.getContent())) {
			mFlowPublishedContentTv.setText(mDynamicNews.getContent());
			mFlowPublishedContentTv.setVisibility(View.VISIBLE);
		} else {
			mFlowPublishedContentTv.setVisibility(View.GONE);
		}

		if (EUtil.isEmpty(mDynamicNews.getContent())&&EUtil.isEmpty(mDynamicNews.getTitle())) {
			mHomeFlowPublishedContentLl.setVisibility(View.GONE);
		} else {
			mHomeFlowPublishedContentLl.setVisibility(View.VISIBLE);
		}
	
		}
	
	

	/**
	 * 文本编辑获取焦点
	 */
	private void editTextCaptureFocus() {
		mHACommentEt.setFocusable(true);
		mHACommentEt.setFocusableInTouchMode(true);
		mHACommentEt.requestFocus();
	}
	
	private ArrayList<SmileyView> listSmileyViews;
	
	/**
	 * 初始化表情
	 */
	private void initExpression() {
		// 表情view
		smileyViewFirst = new SmileyView(this, true);
		smileyViewSecond = new SmileyView(this, false);
		
		listSmileyViews = new ArrayList<SmileyView>();
		int totalPage = (int) Math.ceil(CommonSmileyParser.mEnhancedIconIds.length * 1.0 / SmileyView.MaxSmileyNumber);
		for(int i = 0; i < totalPage; i++){
			SmileyView sv = new SmileyView(this, i);
			listSmileyViews.add(sv);
			sv.setOnItemClickListener(smileyViewClickListener);
		}
		// 表情切换界面
		faceViewPager.setAdapter(new PageViewAdpter());
		smileyPagerchange = ((ImageView) findViewById(R.id.smileyPagerchange));

		faceViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:
					smileyPagerchange
					.setImageResource(R.drawable.chat_biaoqing_1);
					break;
				case 1:
					smileyPagerchange
					.setImageResource(R.drawable.chat_biaoqing_2);
					break;
				case 2:
					smileyPagerchange
					.setImageResource(R.drawable.chat_biaoqing_3);
					break;
				case 3:
					smileyPagerchange
					.setImageResource(R.drawable.chat_biaoqing_4);
					break;
				case 4:
					smileyPagerchange
					.setImageResource(R.drawable.chat_biaoqing_5);
					break;
				case 5:
					smileyPagerchange
					.setImageResource(R.drawable.chat_biaoqing_6);
					break;
				case 6:
					smileyPagerchange
					.setImageResource(R.drawable.chat_biaoqing_7);
					break;
				case 7:
					smileyPagerchange
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

			}

		});

	}
	
	private static final String LEFTSPECCHAR = ((char) 0X1B) + "";
	private static final String RIGHTSPECCHAR = ((char) 0X11) + "";
	/**
	 * 表情点击事件
	 */
	private SmileyView.OnItemClickListener smileyViewClickListener = new SmileyView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg) {
			final CommonSmileyParser parser = CommonSmileyParser.getInstance(DynamicCommentActivity.this);
			if (position == SmileyView.MaxSmileyNumber) { // 删除表情
				String text = mHACommentEt.getText().toString();
				
				if (text.length() > 0) {
					
					if(text.lastIndexOf(RIGHTSPECCHAR) == text.length() - 1){
						text = text.substring(0, text.lastIndexOf(LEFTSPECCHAR));
					}
					else{
						text = text.substring(0, text.length() - 1);
					}
					mHACommentEt.setText(text);
					mHACommentEt.setSelection(text.length());
				}
				return;
			}
			
			final String text = mHACommentEt.getText().toString() + LEFTSPECCHAR + parser.getmSmileyTexts()[(int) arg] + RIGHTSPECCHAR;
			mHACommentEt.setText(text);
			mHACommentEt.setSelection(text.length());
		}
	};

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


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.homeFrgPicIv:
			if (!isShowface) {
				viewPagerCon.setVisibility(View.VISIBLE);
				isShowface = true;
				hideKeyboard();// 隐藏软盘，加了之后gridView起不来
			} else {
				viewPagerCon.setVisibility(View.GONE);
				isShowface = false;
				// 显示软盘
				showKeyboard();
			}
			break;
		case R.id.homeFrgCommentEt:
			if (isShowface) {
				viewPagerCon.setVisibility(View.GONE);
				isShowface = false;// 显示软盘
			}
			break;
		case R.id.homeFrgSendCommentIv:
			mComment = mHACommentEt.getText().toString();
			if (TextUtils.isEmpty(mComment)) {
				mComment = mDynamicNews.getInputText();
			}
			if (sendCommend(mDynamicNews, mComment)) {
				insertComment(mDynamicNews.getInputText());
			}
			mDynamicNews.setInputText("");
			if (mHACommentEt != null) {
				mHACommentEt.setText("");
			}
			changeUIData();
			break;
			
		case R.id.flowHeartTv:
			//未点赞
			if (!isPraised) {
				mDynamicPraise = new DynamicPraise();
				mDynamicPraise.setpId(Long.valueOf(App.getUserID()));
				mDynamicPraise.setDynamicId(mDynamicNews.getId());
				mDynamicPraise.setpName(App.getNick());
				mDynamicPraise.setPtype(1);
				HomeReqUtil.addDynamicPraise(DynamicCommentActivity.this, mBindData, null, /*Long.valueOf(App.getUserID()),*/ mDynamicNews.getId(), 1);
			}
			//已点赞
			else {
				HomeReqUtil.cancelDynamicPraise(this, mBindData, null, mDynamicPraise.getId());
			}
			showLoadingDialogWithoutOnCancelListener();
			break;

		default:
			break;
		}
	}
	

	/**
	 * 隐藏软盘
	 */
	public void hideKeyboard() {
		try {
			InputMethodManager m = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			m.hideSoftInputFromWindow(mHACommentEt.getApplicationWindowToken(),
					0);
		} catch (Exception e) {
		}
	}

	/**
	 * 显示软盘
	 */
	public void showKeyboard() {
		try {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(mHACommentEt, InputMethodManager.SHOW_FORCED);
		} catch (Exception e) {
		}
	}

	public int getmType() {
		return mType;
	}

	public void setmType(int mType) {
		this.mType = mType;
	}

	public void showToast(final String message) {
		Toast.makeText(App.getApp(), message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 更新界面UI
	 */
	public void changeUIData() {
		mDynamicCommentAdapter.setData(mListComment, mDynamicNews,
				mListComment.size(),
				DynamicCommentAdapter.CommentShowType.CommentShowAll);
		mDynamicCommentAdapter.notifyDataSetChanged();
//		mListComment = mDynamicCommentAdapter.getmListComment();
		int commentCount = mListComment.size();
		if (commentCount > 0) {
			mHFCommentNum.setText("  " + commentCount);
		} else {
			mHFCommentNum.setText("");
		}
		hideKeyboard();
		viewPagerCon.setVisibility(View.GONE);
		isShowface = false;
	}

	public boolean sendCommend(DynamicNews dynamicNews, String content) {
		if (TextUtils.isEmpty(content)) {
			Toast.makeText(DynamicCommentActivity.this, "评论内容不能为空", 0).show();
			return false;
		}
		if (dynamicNews != null) {
			return HomeReqUtil.addDynamicComment(DynamicCommentActivity.this,
					mBindData, null, dynamicNews.getType(),
					dynamicNews.getId(), content);
		} else {
			return false;
		}
	}

	public void insertComment(String commentContent) {
		mDynamicComment = new DynamicComment();
		JTMember jtm = App.getUser();
		if (jtm != null) {
			mDynamicComment.setUserName(App.getNick());
		}
		mDynamicComment.setComment(commentContent);
		mDynamicComment.setUserId(Long.valueOf(App.getUserID()));
		mListComment = mDynamicCommentAdapter.getmListComment();
		mListComment.add(0, mDynamicComment);
	}

	private IBindData mBindData = new IBindData() {
		@Override
		public void bindData(int tag, Object object) {
			if (tag == EAPIConsts.HomeReqType.HOME_REQ_ADD_DYNAMIC_COMMENT) {
				if (object != null) {
				} else {
				}
				if (object!=null) {
					Long commentid = (Long) object;
					mDynamicComment.setId(commentid);
				}
			} else if (tag == EAPIConsts.ReqType.GET_DYNAMIC_LIST_COMMENT) {
				if (object != null) {
					DataBox dataBox = (DataBox) object;
					if (null != dataBox.mDynamicCommentList) {
						mListComment.clear();
						mListComment = dataBox.mDynamicCommentList;
						DynamicCommentActivity.this.mListComment = mListComment;
					}
				}
			}
			//add
			else if (tag == EAPIConsts.HomeReqType.HOME_REQ_ADD_DYNAMIC_PRAISE) {
				if (object!=null) {
					long id = (Long) object;
					if (id!=0) {//点赞成功，插入数据
						if (mFlowHeartTv!=null) {
							mFlowHeartPressLL.setVisibility(View.VISIBLE);
							Drawable drawable = getResources().getDrawable(R.drawable.heart_pressed);
							drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
							mFlowHeartTv.setCompoundDrawables(drawable,null,null,null);
						}
						mDynamicPraise.setId(id);
						mDynamicNews.getmDynamicPraiseList().add(0,mDynamicPraise);
//						showDynamicPraise();
						isPraised = true;
					}
					else {
						//失败
						isPraised = false;
					}
				}
			}
			//cancel
			else if (tag == EAPIConsts.HomeReqType.HOME_REQ_CANCEL_DYNAMIC_PRAISE) {
				if (object!=null) {
					String succeed = (String) object;
					if ("true".equals(succeed)) {
						mDynamicPraiseList.remove(mDynamicPraise);
						if (mFlowHeartTv!=null) {
							Drawable drawable = getResources().getDrawable(R.drawable.heart);
							drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
							mFlowHeartTv.setCompoundDrawables(drawable,null,null,null);
						}
//						showDynamicPraise();
						isPraised = false;
					}
					else {
						isPraised = true;
					}
				}
			}

			mDynamicCommentAdapter.setData(mListComment, mDynamicNews,
					mListComment.size(),
					DynamicCommentAdapter.CommentShowType.CommentShowAll);
			mDynamicCommentAdapter.notifyDataSetChanged();
			changeUIData();
			dismissLoadingDialog();
		}
	};
	private ImageView mHFSendCommentIv;
	private LinearLayout commentLL;
	private DynamicComment mDynamicComment;
	private LinearLayout mFlowHeartPressLL;
	private KnoTagGroupView mFlowHeartPeopleCL;
	private TextView mFlowHeartTv;

	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "动态", false, null,true, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		getMenuInflater().inflate(R.menu.activity_new_main, menu);
		menu.findItem(R.id.home_new_menu_more).setVisible(false);
		menu.findItem(R.id.home_new_menu_search).setVisible(false);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		viewPagerCon.setVisibility(View.GONE);
		hideKeyboard();
	}

	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void finish() {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable(ENavConsts.KEY_FRG_CHANGE_COMMENTS,
				(Serializable) mDynamicCommentAdapter.getmListComment());
		bundle.putInt(ENavConsts.KEY_FRG_FLOW_INDEX, mPosition);
		bundle.putSerializable(ENavConsts.KEY_FRG_FLOW_DYNAMIC_PRAISES, mDynamicNews.getmDynamicPraiseList());
		intent.putExtras(bundle);
		setResult(RESULT_OK, intent);
		super.finish();
	}
	
	/** 初始化 动态发布人 头像 */
	private void initDynamicPublisherAvater(DynamicNews mDynamicNews,ImageView avatarIv, TextView avatarTv) {
		// 金桐脑发布
		if (mDynamicNews.getCreateType() == 0) {
			// 金桐脑
			avatarIv.setImageDrawable(getResources().getDrawable(R.drawable.gintong_smart_brain));
		}
		// 个人或组织用户发布
		else {
			String picPath = mDynamicNews.getPicPath();
			String createrNameFlag = mDynamicNews.getCreaterName();
			Util.initAvatarImage(this, avatarIv, createrNameFlag, picPath, mDynamicNews.getGender(), mDynamicNews.getCreateType());
		}
	}
}
