package com.tr.ui.knowledge;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
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
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.App;
import com.tr.R;
import com.tr.api.KnowledgeReqUtil;
import com.tr.api.OrganizationReqUtil;
import com.tr.model.demand.ASSOData;
import com.tr.model.demand.ASSORPOK;
import com.tr.model.demand.DemandASSO;
import com.tr.model.demand.DemandASSOData;
import com.tr.model.joint.AffairNode;
import com.tr.model.joint.ConnectionNode;
import com.tr.model.joint.KnowledgeNode;
import com.tr.model.knowledge.Knowledge2;
import com.tr.model.knowledge.Knowledge2.AuthorType;
import com.tr.model.knowledge.KnowledgeComment;
import com.tr.model.knowledge.KnowledgeDetailsLv;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.knowledge.KnowledgeStatics;
import com.tr.model.knowledge.UserCategory;
import com.tr.model.obj.AffairsMini;
import com.tr.model.obj.Connections;
import com.tr.model.obj.ConnectionsMini;
import com.tr.model.obj.JTFile;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.common.JointResourceFragment.ResourceType;
import com.tr.ui.common.view.MyXListView;
import com.tr.ui.common.view.MyXListView.IXListViewListener;
import com.tr.ui.home.FrameWorkUtils;
import com.tr.ui.knowledge.CreateKnowledgeActivity.OperateType;
import com.tr.ui.knowledge.KnowledegeCommentDetailsFragment.CommendType;
import com.tr.ui.knowledge.KnowledegeCommentDetailsFragment.CommentDetailsLvAdapter;
import com.tr.ui.knowledge.KnowledegeCommentDetailsFragment.PageViewAdpter;
import com.tr.ui.knowledge.KnowledegeCommentDetailsFragment.ReplyCommentLvAdapter;
import com.tr.ui.knowledge.KnowledegeCommentDetailsFragment.CommentDetailsLvAdapter.ViewHolder;
import com.tr.ui.knowledge.KnowledegeCommentDetailsFragment.ReplyCommentLvAdapter.MsgViewHolder;
import com.tr.ui.knowledge.business.KonwledgeSquareReadedKnowledgeSharedPreferencesBusiness;
import com.tr.ui.knowledge.utils.ActivityCollector;
import com.tr.ui.organization.create_clientele.ClientDetailsActivity;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.widgets.BasicListView;
import com.tr.ui.widgets.CommonSmileyParser;
import com.tr.ui.widgets.ConnsEditDialog;
import com.tr.ui.widgets.GuidePopupWindow;
import com.tr.ui.widgets.HorizontalListView;
import com.tr.ui.widgets.KnowledgeDetailsScrollView;
import com.tr.ui.widgets.SmileyParser;
import com.tr.ui.widgets.SmileyParser2;
import com.tr.ui.widgets.SmileyView;
import com.tr.ui.widgets.KnowledgeDetailsScrollView.OnScrollChangedListener;
import com.tr.ui.widgets.KnowledgeSquareMenuPopupWindow;
import com.tr.ui.widgets.KnowledgeSquareMenuPopupWindow.OnMyHomeMenuItemClickListener;
import com.tr.ui.widgets.PredicateLayout;
import com.utils.common.EConsts;
import com.utils.common.EUtil;
import com.utils.common.GlobalVariable;
import com.utils.common.JTDateUtils;
import com.utils.file.FileUtils;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.http.EAPIConsts.KnoReqType;
import com.utils.log.ToastUtil;
import com.utils.time.Util;

/**
 * @ClassName KnowledgeDetailFragment.java
 * @Description 知识详情页面
 * @Author CJJ
 * @Version v 24
 * @Created 2014-11-03
 * @Updated 2014-11-14
 */
public class KnowledgeDetailFragment extends JBaseFragment implements
		IBindData, OnClickListener ,OnScrollListener{

	private final String TAG = getClass().getSimpleName();
	/* 显示知识详情内容 */
	private WebView contentWv;
	/* 知识详情收藏 */
	private RadioButton collectionRb;
	/* 使用知识详情收藏 */
	private RadioButton useCollectionRb;
	/* 跳转到评论 */
	private RadioButton commentsRb;
	/* 评论数量 */
	private TextView commentNumTv;
	/* 知识详情frg和知识评论frg容器 */
	private ViewPager mViewPager;
	/* 知识详情标题 */
	private TextView knoDetailsTitleTv;
	private TextView knowledgetypeTv;
	/* 知识详情作者 */
	private TextView knoDetailsAuthorTv;
	/* 知识详情时间 */
	private TextView knoDetailsDateTv;
	/* 分割线 */
	/* private ImageView knowDividingLine; */
	/* 生态对接 */
	private TextView ecologicalDockingTv;
	/* 知识详情Head */
	private LinearLayout knowDetailsFrgHead;
	/* 知识详情Bottom */
	private FrameLayout knowDetailsFrgBottom;
	/* 知识详情页返回键 */
	private ImageView KnowDetailsFrgBackIv;
	/* 知识详情页返回键 */
	private LinearLayout know_layoutTitle_backBtn;
	/* 知识详情内容显示的字体大小 */
	private RadioButton webViewTextSizeRb;
	/* 知识详情收藏数目 */
	private TextView collectionNumTv;
	/* 上下文 */
	private Activity mContext;
	/* 显示知识详情listview */
	/* 知识详情评论数 */
	private int mCommentCount;
	/* 知识详情 */
	private Knowledge2 mKnowledge2;
	/* 知识详情适配器 */
	/* 时间 */
	private String mCreatetime;
	/* 网页标题 */
	private String mTitle;
	/* 作者 */
	private String mUname;
	/* 网页加载Url */
	private String mKnowledgeUrl = "";
	/* 评论数 \收藏数 */
	private KnowledgeStatics mKnowStatics;
	/* 存储数据 */
	private List<KnowledgeDetailsLv> mKnowDetails = new ArrayList<KnowledgeDetailsLv>();
	/* 与知识关联的人 */
	public static final int TYPE_CONNECTIONS = 0;
	/* 与知识关联的组织 */
	public static final int TYPE_ORGANIZATION = 1;
	/* 与知识关联的知识 */
	public static final int TYPE_KNOWLEDGE = 2;
	/* 与知识关联的事件对象 */
	public static final int TYPE_AFFAIR = 3;
	/** 来源activity */
	private String fromActivity;
	/* 知识id */
	private long mKnowledgeId;
	/* 知识类型 */
	private int mKnowledgeType;
	/**/
	private KnowledgeOfDetailActivity activity;
	/**/
	private WebSettings webSettings;
	/* 默认网页显示内容字体大小 */
	private int webViewSize = 1;
	/* 默认网页显示内容字体大小 */
	private int webFontSize = 1;
	/* 显示网页字体大小 */
	private TextView webViewSizeTv;
	/* 显示知识标签 */
	private PredicateLayout predicateLayout;
	/* 知识详情显示的Tag */
	private String[] listTag;
	/* 知识详情ScrollView */
	private KnowledgeDetailsScrollView frgKnowDetailsSv;
	/* 分享 */
	private RadioButton knowShareRb;
	/* 分享 */
	private ImageView knowShareIv;
	/* 更多 */
	private ImageView knowledgeMoreIv;
	/* 后退 */
	private ImageView knowledgeHomeBackIv;
	/* 显示菜单 */
	private View know_headerVi;
	/* 是否显示删除按钮 */
	private Boolean isCanDelete = false;
	/* 是否显示编辑按钮 */
	private Boolean isCanUpdate = false;
	
	private RadioButton saveRb;
	private RadioButton forwardShareRb;
	private FrameLayout mSaveRbFL;
	private FrameLayout mForwardShareFL;
	private FrameLayout mCommonTitle;
	private ImageView mknowTitlebar;
	private Drawable mActionBarBackgroundDrawable;
	/* 动态传过来的值，来控制是否显示保存键 */
	private boolean isShowSave;
	private KnowledgeSquareMenuPopupWindow knowledgeSquareMenuPopupWindow;
	private LinearLayout knowledgeTagLl;
	private TextView tagTv;
	private LinearLayout knowledgeCategoryLl;
	private TextView categoryTv;
	//权限
	private LinearLayout knowledgePermissionLl;
//	private TextView noPermissionTv;
	private LinearLayout permissionLl;
	private LinearLayout hightPermissionLl;
	private LinearLayout middlePermissionLl;
	private LinearLayout lowPermissionLl;
	private TextView hightPermissionTv;
	private TextView middlePermissionTv;
	private TextView lowPermissionTv;
	private TextView know_permissiontv;
	private float ratio;
	private boolean createdByMySelf;
	/*是否包含音频或者视频*/
	private boolean isContainsMultiMedia = true;
	private View asso;
	private ImageView knowCommentIv; //评论
	private TextView knowCommentTv; //评论数
	private ImageView knowCommentRedPointIv; //评论数超过一百显示红点，不显示评论数
	private FrameLayout knowCommentRl ;
	private int mCurrentlyShowingFragment;
	private ArrayList<KnowledgeComment> newKnowCommentsList = new ArrayList<KnowledgeComment>();
	private Integer total;
	private LinearLayout KnowDetailsLl;
	private LinearLayout knoDetailsWvLl;
	private LinearLayout asso_Ll;
	private boolean isShowInput = false;
	private ImageView know_titlebar;
	private TextView bottomTv;
	private LinearLayout CommentTitleLL;
	private boolean isShowCollection = false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		context = getActivity();
	}

	public KnowledgeDetailFragment() {
	}

	public KnowledgeDetailFragment(ViewPager viewPager,
			KnowledgeOfDetailActivity activity) {
		this.mViewPager = viewPager;
		this.activity = activity;
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup root,
			Bundle savedInstance) {
		return inflater.inflate(R.layout.fragment_knowledge_details, root,
				false);
	}

	/**
	 * 初始化组件
	 */
	private void initWidget() {
		/*标签、目录、权限*/
		knowledgeTagLl = (LinearLayout) mContext.findViewById(R.id.knowledgeTagLl);
//		tagTv = (TextView) mContext.findViewById(R.id.tagTv);
		knowledgeCategoryLl = (LinearLayout) mContext.findViewById(R.id.knowledgeCategoryLl);
		categoryTv = (TextView) mContext.findViewById(R.id.categoryTv);
		tagTv = (TextView) mContext.findViewById(R.id.tagTv);
		know_permissiontv = (TextView) mContext.findViewById(R.id.know_permissiontv);
		
		
		knowledgePermissionLl = (LinearLayout) mContext.findViewById(R.id.knowledgePermissionLl);
//		noPermissionTv = (TextView) mContext.findViewById(R.id.noPermissionTv);
		permissionLl = (LinearLayout) mContext.findViewById(R.id.permissionLl);
		hightPermissionLl = (LinearLayout) mContext.findViewById(R.id.hightPermissionLl);
		middlePermissionLl = (LinearLayout) mContext.findViewById(R.id.middlePermissionLl);
		lowPermissionLl = (LinearLayout) mContext.findViewById(R.id.lowPermissionLl);
		hightPermissionTv = (TextView) mContext.findViewById(R.id.hightPermissionTv);
		middlePermissionTv = (TextView) mContext.findViewById(R.id.middlePermissionTv);
		lowPermissionTv = (TextView) mContext.findViewById(R.id.lowPermissionTv);
		
		expressionIv = (ImageView) mContext.findViewById(R.id.expressionIv);
		textEt = (EditText)mContext. findViewById(R.id.commentEt);
		sendKnowCommentIv = (ImageView) 
				mContext.findViewById(R.id.sendKnowCommentIv);// 发送/附件按钮
		inputLl = (LinearLayout) mContext.findViewById(R.id.inputLl);
		mContext.findViewById(R.id.switchIv).setVisibility(View.GONE);
		
		knowledgeHomeBackIv = (ImageView) mContext.findViewById(R.id.knowledgeHomeBackIv);
		knowledgeMoreIv = (ImageView) mContext
				.findViewById(R.id.knowledgeMoreIv);
		knowShareIv = (ImageView) mContext
				.findViewById(R.id.knowShareIv);
		knowCommentRl = (FrameLayout) mContext
				.findViewById(R.id.knowCommentRl);
		knowCommentIv = (ImageView) mContext
				.findViewById(R.id.knowCommentIv);
		know_titlebar = (ImageView) mContext
				.findViewById(R.id.know_titlebar);
		knowCommentTv = (TextView)  mContext
				.findViewById(R.id.knowCommentTv);
		knowCommentRedPointIv =  (ImageView) mContext
				.findViewById(R.id.knowCommentRedPointIv);
		
		know_headerVi = mContext.findViewById(R.id.know_headerVi);
		KnowDetailsLl = (LinearLayout)  mContext.findViewById(R.id.KnowDetailsLl);
		knoDetailsWvLl = (LinearLayout)  mContext.findViewById(R.id.knoDetailsWvLl);
		asso_Ll = (LinearLayout)  mContext.findViewById(R.id.asso_Ll);
		contentWv = (WebView) mContext.findViewById(R.id.knoDetailsWv);
		asso = mContext.findViewById(R.id.asso);
		assoLL = (LinearLayout) asso.findViewById(R.id.assoLL);
		contactsTitle = (LinearLayout) asso.findViewById(R.id.contactsTitle);
		contactsLl = (LinearLayout) asso.findViewById(R.id.contactsLl);
		organizationTitle = (LinearLayout) asso.findViewById(R.id.organizationTitle);
		organizationLl = (LinearLayout) asso.findViewById(R.id.organizationLl);
		knowledge_Ll = (LinearLayout) asso.findViewById(R.id.knowledge_Ll);
		affria_Ll = (LinearLayout) asso.findViewById(R.id.affria_Ll);
		affria_needDetailsLabelInfoItem = (BasicListView) asso.findViewById(R.id.affria_needDetailsLabelInfoItem);
		knowledge_needDetailsLabelInfoItem = (BasicListView) asso.findViewById(R.id.knowledge_needDetailsLabelInfoItem);
		organization_horizontalListView = (HorizontalListView) asso.findViewById(R.id.organization_horizontalListView2);
		contacts_horizontalListView = (HorizontalListView) asso.findViewById(R.id.contacts_horizontalListView1);
		peopleAdapter = new ConnectionsGroupAdapter();
		organizationAdapter = new ConnectionsGroupAdapter();
		knowledgeGroupAdapter = new KnowledgeAffairGroupAdapter();
		affriaGroupAdapter = new KnowledgeAffairGroupAdapter();
		affria_needDetailsLabelInfoItem.setAdapter(affriaGroupAdapter);
		knowledge_needDetailsLabelInfoItem.setAdapter(knowledgeGroupAdapter);
		organization_horizontalListView.setAdapter(organizationAdapter);
		contacts_horizontalListView.setAdapter(peopleAdapter);
		affria_needDetailsLabelInfoItem.setHaveScrollbar(false);
		knowledge_needDetailsLabelInfoItem.setHaveScrollbar(false);
		MyOnTouch onTouch = new MyOnTouch();
		organization_horizontalListView.setOnTouchListener(onTouch);
		contacts_horizontalListView.setOnTouchListener(onTouch);
		collectionRb = (RadioButton) mContext.findViewById(R.id.collectionRb);
		commentNumTv = (TextView) mContext.findViewById(R.id.commentNumTv);
		collectionNumTv = (TextView) mContext
				.findViewById(R.id.collectionNumTv);
		useCollectionRb = (RadioButton) mContext
				.findViewById(R.id.useCollectionRb);
		knoDetailsTitleTv = (TextView) mContext
				.findViewById(R.id.knoDetailsTitleTv);
		knowledgetypeTv = (TextView) mContext
				.findViewById(R.id.knowledgetypeTv);
		knoDetailsAuthorTv = (TextView) mContext
				.findViewById(R.id.knoDetailsAuthorTv);
		commentsRb = (RadioButton) mContext.findViewById(R.id.commentsRb);
//		myListViewAdapter = new KnoDetailsLvAdapter(mKnowDetails, mContext);
		ecologicalDockingTv = (TextView) mContext
				.findViewById(R.id.ecologicalDockingTv);
		knowDetailsFrgHead = (LinearLayout) mContext
				.findViewById(R.id.frgKnowDetailsHead);
		knowDetailsFrgBottom = (FrameLayout) mContext
				.findViewById(R.id.frgKnowDetailsBottom);
		KnowDetailsFrgBackIv = (ImageView) mContext
				.findViewById(R.id.frgKnowDetailsBackIv);
		know_layoutTitle_backBtn = (LinearLayout) mContext
				.findViewById(R.id.know_layoutTitle_backBtn);
		webViewTextSizeRb = (RadioButton) mContext
				.findViewById(R.id.webViewTextSizeRb);
		webViewSizeTv = (TextView) mContext.findViewById(R.id.webViewSizeTv);
		frgKnowDetailsSv = (KnowledgeDetailsScrollView) mContext
				.findViewById(R.id.frgKnowDetailsSv);
		knowShareRb = (RadioButton) mContext.findViewById(R.id.knowShareRb);
		forwardShareRb = (RadioButton) mContext
				.findViewById(R.id.forwardShareRb);
		saveRb = (RadioButton) mContext.findViewById(R.id.saveRb);
		predicateLayout = (PredicateLayout) mContext
				.findViewById(R.id.knowFixGridTags);
		mSaveRbFL = (FrameLayout) mContext.findViewById(R.id.saveRbFL);
		mForwardShareFL = (FrameLayout) mContext
				.findViewById(R.id.forwardShareFL);
		mCommonTitle = (FrameLayout) mContext
				.findViewById(R.id.mCommonTitle);
		mknowTitlebar = (ImageView) mContext.findViewById(R.id.know_titlebar);
		mActionBarBackgroundDrawable = getResources().getDrawable(R.drawable.auth_title_back_white);
		
		commentView = View
				.inflate(mContext, R.layout.activity_industry_comment, null);
		KnowDetailsLl.addView(commentView);
		commentView.setVisibility(View.GONE);
		/* 初始化评论控件 */
		initComponents(commentView);
	}
	
	/**
	 * 初始化评论组件
	 */
	private void initComponents(View view) {
		viewPagerCon = (FrameLayout) mContext.findViewById(R.id.industrySmileyFL);
		sendKnowCommentIv = (ImageView) mContext
				.findViewById(R.id.sendKnowCommentIv);// 发送/附件按钮
//		swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshSrl);
		mKnoCommentLv = (MyXListView) view.findViewById(R.id.commentLv);
		CommentTitleLL = (LinearLayout) view.findViewById(R.id.CommentTitleLL);
		mKnoCommentLv.KnowledegeShow = true;
		expressionIv = (ImageView) mContext.findViewById(R.id.expressionIv);
		textEt = (EditText) mContext.findViewById(R.id.commentEt);
		faceViewPager = (ViewPager) mContext.findViewById(R.id.industrySmileyPager);
		if (knowCommentsLvAdapter==null) {
			knowCommentsLvAdapter = new CommentDetailsLvAdapter();
		}
		bottomTv = (TextView) view
				.findViewById(R.id.bottomTv);
		knowIndustryCommentBackLL = (ImageView) view
				.findViewById(R.id.knowIndustryCommentBackLL);
		knowCommentBackLL = (LinearLayout)view.findViewById(R.id.knowCommentBackLL); //标题栏
		inputLl = (LinearLayout)mContext.findViewById(R.id.inputLl); //发送栏
		initData(view);
	}
	
	class MyOnTouch implements View.OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				Log.d("onTouch", activity.layout.isCanSilding+"ACTION_DOWN");
				activity.knowDetailContentVp.isCanScroll = false;
				activity.layout.isCanSilding = false;
				break;
			case MotionEvent.ACTION_MOVE:
				Log.d("onTouch", activity.layout.isCanSilding+"ACTION_MOVE");
				activity.knowDetailContentVp.isCanScroll = false;
				activity.layout.isCanSilding = false;
				break;

			case MotionEvent.ACTION_UP:
				Log.d("onTouch", activity.layout.isCanSilding+"ACTION_UP");
				activity.knowDetailContentVp.isCanScroll = true;
				activity.layout.isCanSilding = true;
				break;
			default:
				Log.d("onTouch", activity.layout.isCanSilding+"default");
				activity.knowDetailContentVp.isCanScroll = true;
				activity.layout.isCanSilding = true;
				break;
			}

			return !activity.knowDetailContentVp.isCanScroll;
		}
	}
	/**
	 * 适配 人脉信息和 组织信息
	 * 
	 * @author Administrator
	 * 
	 */
	class ConnectionsGroupAdapter extends BaseAdapter {
		private List<DemandASSOData> assoList;

		public ConnectionsGroupAdapter(List<DemandASSOData> assoList) {
			this.assoList = assoList;
		}
		
		public List<DemandASSOData> getAssoList() {
			return assoList;
		}

		public void setAssoList(List<DemandASSOData> assoList) {
			this.assoList = assoList;
		}

		public ConnectionsGroupAdapter() {
		}
		@Override
		public int getCount() {
			return assoList == null ? 0 : assoList.size();
		}

		@Override
		public DemandASSOData getItem(int position) {
			return assoList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(getActivity(),
						R.layout.demand_need_details_hori_item, null);
				holder. nameTv = (TextView) convertView.findViewById(R.id.nameTv);
				holder. describeTv = (TextView) convertView.findViewById(R.id.describeTv);
				holder. headImageIv = (ImageView) convertView.findViewById(R.id.headImgIv);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			DemandASSOData assoData = getItem(position);
			
			holder.nameTv.setText(assoData.name); // 姓名
			holder.describeTv.setText(assoData.tag); // 关系
			holder.headImageIv
			.setImageResource(R.drawable.demand_defaultimg);
			/*if (!TextUtils.isEmpty(assoData.picPath)) {
				ImageLoader.getInstance().displayImage(assoData.picPath, holder.headImageIv);// 头像
			} else if(
//					if (assoList.get(position).type ==2||assoList.get(position).type ==3) {
//						holder.headImageIv
//								.setImageResource(R.drawable.ic_default_avatar);
//					} else if (assoList.get(position).type ==4||assoList.get(position).type ==5 ) {
//						holder.headImageIv
//								.setImageResource(R.drawable.default_portrait116);
//					}
					
				assoData.picPath.endsWith(GlobalVariable.ORG_DEFAULT_AVATAR)){
				String last_char = "";
				String org_name = assoData.name;
				if(!TextUtils.isEmpty(org_name)){
					last_char = org_name.substring(org_name.length()-1);
				}
				Bitmap bm = com.utils.common.Util.createBGBItmap(activity, R.drawable.ic_group_default_avatar, R.color.avatar_text_color, R.dimen.avatar_text_size, last_char);
				holder.headImageIv.setImageBitmap(bm);
			}*/
//			
//				if(TextUtils.isEmpty(assoData.picPath)||assoData.picPath.endsWith(GlobalVariable.ORG_DEFAULT_AVATAR)){
//					String last_char = "";
//					String org_name = assoData.name;
//					if(!TextUtils.isEmpty(org_name)){
//						last_char = org_name.substring(org_name.length()-1);
//					}
//					Bitmap bm = com.utils.common.Util.createBGBItmap(activity, R.drawable.ic_group_default_avatar, R.color.avatar_text_color, R.dimen.avatar_text_size, last_char);
//					holder.headImageIv.setImageBitmap(bm);
//				}else{
//					ImageLoader.getInstance().displayImage( assoData.picPath,holder.headImageIv);
//				}
			int type = 0;
			if (assoData.type==2||assoData.type==3) {
				type = 1;
			}else if(assoData.type==4||assoData.type==5) {
				type = 2;
			}
			com.utils.common.Util.initAvatarImage(mContext,holder.headImageIv,assoData.name,assoData.picPath,0, type);


			return convertView;
		}

	}
		
		class ViewHolder {
			TextView nameTv;
			TextView describeTv;
			ImageView headImageIv;
		}
		
	
	
	public class KnowledgeAffairGroupAdapter extends BaseAdapter {
		
		private ArrayList<DemandASSOData> listRelatedKnowledgeNode;
		
		public KnowledgeAffairGroupAdapter() {
			super();
		}
		
		public KnowledgeAffairGroupAdapter(Context context, ArrayList<DemandASSOData> listRelatedKnowledgeNode) {
			super();
			if (listRelatedKnowledgeNode != null) {
				this.listRelatedKnowledgeNode = listRelatedKnowledgeNode;
			} else {
				this.listRelatedKnowledgeNode = new ArrayList<DemandASSOData>();
			}
		}
		
		public ArrayList<DemandASSOData> getListRelatedKnowledgeNode() {
			return listRelatedKnowledgeNode;
		}
		
		public void setListRelatedKnowledgeNode(ArrayList<DemandASSOData> listRelatedKnowledgeNode) {
			if( listRelatedKnowledgeNode != null){
				this.listRelatedKnowledgeNode = listRelatedKnowledgeNode;
			}
		}

		@Override
		public int getCount() {
			if (listRelatedKnowledgeNode!=null) {
				return listRelatedKnowledgeNode.size();
			}else{
				return 0;
			}
		}
		

		@Override
		public Object getItem(int position) {
			return listRelatedKnowledgeNode.get(position);
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
			ViewHolder viewHolder;
			
			if(convertView == null ){
				viewHolder = new ViewHolder();
				
				convertView = LayoutInflater.from(mContext).inflate(R.layout.demand_need_details_label_info_item, null);
				viewHolder.titleTv = (TextView) convertView.findViewById(R.id.titleTv);
				viewHolder.contentsTv = (TextView) convertView.findViewById(R.id.contentsTv);
				convertView.setTag(viewHolder);
			}
			else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			
			DemandASSOData demandASSOData = (DemandASSOData) getItem(position);
			viewHolder.contentsTv.setText( demandASSOData.tag );
			viewHolder.titleTv.setText(demandASSOData.title);
			
			return convertView;
		}
		
		class ViewHolder {
			TextView titleTv;
			TextView contentsTv;
		}
		
	}
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Bundle bundle = this.getArguments();
		fromActivity = bundle.getString(EConsts.Key.FROM_ACTIVITY);
		mKnowledgeId = bundle.getLong(EConsts.Key.KNOWLEDGE_DETAIL_ID);
		mKnowledgeType = bundle.getInt(EConsts.Key.KNOWLEDGE_DETAIL_TYPE);
		isShowSave = bundle.getBoolean(ENavConsts.KEY_FRG_FLOW_FORWARDING_KNOWLEDGE_TYPE, true);
		isShowCollection  = bundle.getBoolean(ENavConsts.KEY_COLLECTION_KNOWLEDGE_TYPE, true);
		mKnowledge2 = (Knowledge2) bundle
				.getSerializable(EConsts.Key.KNOWLEDGE2);// 获取创建知识详情的对象
		
		initWidget();// 初始化控件
		frgKnowDetailsSv.smoothScrollTo(0, 0);// scrollView的初始显示位置
		contentWv.getSettings().setJavaScriptEnabled(true);
		webSettings = contentWv.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setAllowFileAccess(true);
		webSettings.setBuiltInZoomControls(false);// 设置支持缩放
		collectionRb.setOnClickListener(this);// 收藏
		useCollectionRb.setOnClickListener(this);// 取消收藏
		commentsRb.setOnClickListener(this);// 回复
		ecologicalDockingTv.setOnClickListener(this);// 生态对接
		KnowDetailsFrgBackIv.setOnClickListener(this);// 返回
		know_layoutTitle_backBtn.setOnClickListener(this);// 返回
		knowledgetypeTv.setOnClickListener(this);// 返回
		webViewTextSizeRb.setOnClickListener(this);
		knowledgeMoreIv.setOnClickListener(this);// 更多
		knowShareIv.setOnClickListener(this);// 分享
		knowShareRb.setOnClickListener(this);// 分享
		knowCommentRl.setOnClickListener(this);//评论
		saveRb.setOnClickListener(this);
		know_titlebar .setOnClickListener(this);
		forwardShareRb.setOnClickListener(this);
		knoDetailsAuthorTv.setOnClickListener(this);//进入个人主页
		
		if(activity != null)
		{
			activity.showLoadingDialog();
		}

		if (mKnowledge2 == null) {
			 initData(mKnowledgeId, mKnowledgeType);
		} else {
			
			initKnowledgeDetailsData();
		}
		mActionBarBackgroundDrawable.setAlpha(0);
		frgKnowDetailsSv.setOnScrollChangedListener(new OnScrollChangedListener() {
			private int pageIndex = 2;
			
			

			@SuppressLint("NewApi") @Override
			public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
			     	final int headerHeight =knowDetailsFrgHead.getHeight() - mCommonTitle.getHeight();
		            ratio = (float) Math.min(Math.max(t, 0), headerHeight) / headerHeight;
		            final int newAlpha = (int) (ratio * 255);
//		            Log.i("KnowledgeDetailFragment =ratio=", ratio+"");
//		            Log.i("KnowledgeDetailFragment =headerHeight=", headerHeight+"");
//		            Log.i("KnowledgeDetailFragment = newAlpha= ", newAlpha +"");
//		            if (mKnowledegeCommentDetailsFragment.isVisible()) {
//		    			mKnowledegeCommentDetailsFragment.changeSendinputLl(true);
//		    		}else{
//		    			mKnowledegeCommentDetailsFragment.changeSendinputLl(false);
//		    		}
		           float commentHeight =  knoDetailsWvLl.getMeasuredHeight() + asso_Ll.getMeasuredHeight() + knowDetailsFrgHead.getMeasuredHeight()-activity.getWindowManager().getDefaultDisplay().getHeight();
		           if (frgKnowDetailsSv.getScrollY()>commentHeight) {
		        	   if (!isShowInput) {
//		        		   ToastUtil.showToast(context, TAG+"显示"+commentHeight);
		        		   showBottomBar();
		        		   isShowInput= true;
		        	   	}
		           }else if (frgKnowDetailsSv.getScrollY()<commentHeight) {
		        	   if (isShowInput) {
//		        		   ToastUtil.showToast(context, TAG+"隐藏"+commentHeight);
		        	   hideBottomBar();
		        	   isShowInput= false; 
		        	   }
		           }
		            mActionBarBackgroundDrawable.setAlpha(newAlpha);
		            /*Jelly bean版本以下  是不支持setBackground  。所以改为setBackgroundDrawable就可以了*/
		            try {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
							mCommonTitle
									.setBackground(mActionBarBackgroundDrawable);
						} else {
							mCommonTitle.setBackgroundDrawable(mActionBarBackgroundDrawable);
						}
						if (ratio == 1.0) {
							knowShareIv.setImageDrawable(getResources().getDrawable(
													R.drawable.forward_share_white_full));
							knowledgeHomeBackIv.setImageDrawable(getResources().getDrawable(
													R.drawable.relation_home_back_full));
							knowledgeMoreIv.setImageDrawable(getResources().getDrawable(
													R.drawable.ic_action_overflow_full));
							/* 知识评论数 */
							if (null != knowCommentsList&& knowCommentsList.size() > 0) {
								knowCommentIv.setImageDrawable(getResources().getDrawable(R.drawable.comment_underthe100_full));
							}else
							{
								knowCommentIv.setImageResource(R.drawable.comment_overthe100_full);
							}
							knowCommentTv.setTextColor(R.color.auth_title_back);
						} else {
							knowShareIv.setImageDrawable(getResources().getDrawable(
													R.drawable.forward_share_white));
							knowledgeHomeBackIv.setImageDrawable(getResources().getDrawable(
													R.drawable.left_arrow));
							knowledgeMoreIv.setImageDrawable(getResources().getDrawable(
													R.drawable.ic_action_overflow_know));
							/* 知识评论数 */
							if (null != knowCommentsList&& knowCommentsList.size() > 0) {
								knowCommentIv.setImageDrawable(getResources().getDrawable(R.drawable.comment_underthe100));
							}else
							{
								knowCommentIv.setImageResource(R.drawable.comment_overthe100);
							}
						}
					} catch (Exception e) {
					}
			}
		});
		
		SharedPreferences firstuse_sp;
		SharedPreferences.Editor firstuse_edtior;
		
		firstuse_sp = getActivity().getSharedPreferences(GlobalVariable.SHARED_PREFERENCES_FIRST_USE,getActivity().MODE_PRIVATE);
		firstuse_edtior = firstuse_sp.edit();
		if(firstuse_sp.getBoolean(GlobalVariable.KNOWLEDGE_FIRST_USE, true)){
			GuidePopupWindow guidePop = new GuidePopupWindow(getActivity());
			guidePop.setImage(R.drawable.main_guide_08);
			guidePop.show();
			firstuse_edtior.putBoolean(GlobalVariable.KNOWLEDGE_FIRST_USE, false);
			firstuse_edtior.commit();
		}
		
	}


	@Override
	public void onResume() {
//		if (mKnowledgeType != 0 && mKnowledge2 == null) {
//			initData(mKnowledgeId, mKnowledgeType);
//		}
		hideKeyboard();// 隐藏软键盘
		try {
//			initData(mKnowledgeId, mKnowledgeType);
			
		} catch (Exception e) {
			Log.i("com.tr.ui.knowledge.KnowledgeDetailFragment", "啊偶，出错啦！");
		}
		super.onResume();
	}

	/**
	 * 收藏夹的图片变动
	 * 
	 * @param bool
	 *            true 收藏成功 、false 取消收藏
	 */
	private void showCollectionView(Boolean bool) {
		if (bool) {
			collectionRb.setVisibility(View.GONE);
			useCollectionRb.setVisibility(View.VISIBLE);
		} else {
			collectionRb.setVisibility(View.VISIBLE);
			useCollectionRb.setVisibility(View.GONE);
		}
	}

	/**
	 * 获取服务器绑定数据(GetKnoDetails、) return GetKnoDetails--知识详情
	 */
	@Override
	public void bindData(int tag, Object object){
		try {
		Map<String, Object> dataHm = (Map<String, Object>) object;
		if (tag == EAPIConsts.KnoReqType.GetKnoDetails) {
			if (dataHm != null) {
				mKnowledge2 = (Knowledge2) dataHm.get("knowledge2");
				setmKnowledge2(mKnowledge2);
				initKnowledgeDetailsData();
				if("KnowledgeSquareActivity".equals(fromActivity) ){
					KonwledgeSquareReadedKnowledgeSharedPreferencesBusiness readedKnowledgeSharedPreferencesBusiness = new KonwledgeSquareReadedKnowledgeSharedPreferencesBusiness(mContext);
					readedKnowledgeSharedPreferencesBusiness.markReadedKnowledge(mKnowledge2.getId()+"");
				}
				
			}else {
				dismissLoadingDialog();
				getActivity().finish();
			}
		} else if (tag == EAPIConsts.KnoReqType.GetKnoCommentsByType) {
			/** 获取知识或评论的评论 */
			if (dataHm != null) {
				mCommentCount = (Integer) dataHm.get("total");
				if (mCommentCount <= 0) {
					commentNumTv.setVisibility(View.INVISIBLE);
				}
			}
			
		} 
		
		else if (tag == EAPIConsts.KnoReqType.UpdateCollectKnowledge) {
			if (dataHm == null) {
				return;
			}
			/** 更新收藏 */
			boolean bool = (Boolean) dataHm.get("succeed");
			if (bool) {
				if (null != mKnowStatics
						&& mKnowStatics.getCollectioncount() > 0) {
					if (View.VISIBLE == useCollectionRb.getVisibility()) {
						collectionNumTv.setText((mKnowStatics
								.getCollectioncount() != 1 ? (mKnowStatics
								.getCollectioncount() - 1) : "")
								+ "");
						showCollectionView(false);
					} else {
						collectionNumTv.setText((mKnowStatics
								.getCollectioncount() + 1) + "");
					}
				} else {
					if (View.VISIBLE == useCollectionRb.getVisibility()) {
						collectionNumTv.setText("");
						showCollectionView(false);// 取消收藏
					} else {
						collectionNumTv.setText("1");
						showCollectionView(true);// 收藏成功
					}
				}
			}
			showCollectionInfo(bool);// 提示收藏信息
		}else if (tag == EAPIConsts.KnoReqType.DeleteKnowledgeById) {
			Map<String, Object> hm = (Map<String, Object>) object;
			boolean b = (Boolean) hm.get("success");
			if (b) {
				mContext.finish();
				Toast.makeText(mContext, "删除成功", 0).show();
			} else {
				Toast.makeText(mContext, "删除失败", 0).show();
			}
		}
		

		else if (KnoReqType.GetKnowledgeComment == tag) {// 获取知识评论
			if (CommendType.GetKnowCommend.equals(knowActionForward)) {
				if (object != null) {
					commentView.setVisibility(View.VISIBLE);
					CommentTitleLL.setVisibility(View.VISIBLE); 
					bottomTv.setVisibility(View.VISIBLE); 
					Map<String, Object> hm = ((Map<String, Object>) object);
					if (null == knowCommentsList/* || !hasMore()*/) {
						knowCommentsList = (ArrayList<KnowledgeComment>) hm
								.get("listKnowledgeComment");
					} else {
						ArrayList<KnowledgeComment> tempList = (ArrayList<KnowledgeComment>) hm
								.get("listKnowledgeComment");
						knowCommentsList.addAll(tempList);
						tempList = null;
					}
				}else{
					commentView.setVisibility(View.GONE);
					CommentTitleLL.setVisibility(View.GONE); 
					bottomTv.setVisibility(View.GONE);
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
					CommentTitleLL.setVisibility(View.VISIBLE); 
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
					if (ratio == 1.0) {
						/* 知识评论数 */
						if (null != knowCommentsList&& knowCommentsList.size() > 0) {
							knowCommentIv.setImageDrawable(getResources().getDrawable(R.drawable.comment_underthe100_full));
						}else
						{
							knowCommentIv.setImageResource(R.drawable.comment_overthe100_full);
						}
					} else {
						/* 知识评论数 */
						if (null != knowCommentsList&& knowCommentsList.size() > 0) {
							knowCommentIv.setImageDrawable(getResources().getDrawable(R.drawable.comment_underthe100));
						}else
						{
							knowCommentIv.setImageResource(R.drawable.comment_overthe100);
						}
					}
					int knowCommentsListsize = knowCommentsList.size();
					knowCommentTv.setText(knowCommentsListsize+"");
					knowCommentsLvAdapter.notifyDataSetChanged();
					if (addBool) {
						showToast(R.string.str_comment_success_hint);
					} else {
						showToast(R.string.str_comment_failure_hint);
					}
				} catch (Exception ex) {
//					showToast(R.string.str_comment_failure_hint);
					ex.printStackTrace();
				} finally {
					activity.dismissLoadingDialog();
				}
			}
		}
		dismissLoadingDialog();
		stopLoading();	
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	ConnectionsGroupAdapter peopleAdapter ;
	ConnectionsGroupAdapter organizationAdapter ;
	KnowledgeAffairGroupAdapter knowledgeGroupAdapter;
	KnowledgeAffairGroupAdapter affriaGroupAdapter;
	private boolean isBottom = false;
	/**
	 * 数据处理，将关联二级列表处理成一级显示
	 * 
	 * @param list
	 * @return
	 */
	private ArrayList<DemandASSOData> toDemandASSOData(List<ASSOData> list) {
		ArrayList<DemandASSOData> assos = new ArrayList<DemandASSOData>();
		for (ASSOData data : list) {
			for (DemandASSOData conn : data.conn) {
				DemandASSOData demandAsso = new DemandASSOData();
				demandAsso = conn;
				demandAsso.tag = data.tag;
				assos.add(demandAsso);
			}
		}
		return assos;
	}
	public ASSORPOK createNewASSO() {
		DemandASSO asso = new DemandASSO();
		List<com.tr.model.demand.ASSOData> p = new ArrayList<com.tr.model.demand.ASSOData>();
		// 人脉信息
		if (connectionNodeList != null) {
			for (ConnectionNode node : connectionNodeList) {
				List<DemandASSOData> conn = new ArrayList<DemandASSOData>();
				for (Connections obj : node.getListConnections()) {
					DemandASSOData assoData = new DemandASSOData();
					assoData.id = obj.getId();
					assoData.name = obj.getName();
					assoData.ownerid = App.getUserID();
					assoData.ownername = App.getNick();
					assoData.career = obj.getCareer();
					assoData.company = obj.getCompany();
					if (obj.jtContactMini!=null) {
						
					if (obj.jtContactMini. isOnline) {
						assoData.type = 3;
					}else{
						assoData.type = 2;
					}
					assoData.picPath = obj.jtContactMini.image;
					}
					
					conn.add(assoData);
				}
				p.add(new ASSOData(node.getMemo(), conn));
			}
		}
		List<ASSOData> o = new ArrayList<ASSOData>();
		// 组织信息
		if (organizationList != null) {
			for (ConnectionNode node : organizationList) {
				List<DemandASSOData> conn = new ArrayList<DemandASSOData>();
				for (Connections obj : node.getListConnections()) {
					DemandASSOData assoData = new DemandASSOData();
					if (obj.organizationMini!=null) {
					if (obj.organizationMini.isOnline()) {
						assoData.type = 4;
					}else{
						assoData.type = 5;
					} 
					assoData.picPath = obj.organizationMini.mLogo;
					}
					assoData.id = obj.getId();
					assoData.name = obj.getName();
					assoData.ownerid = App.getUserID();
					assoData.ownername = App.getNick();
					// 当前组织没有行业和地址
					// assoData.setAddress(obj.getAttribute());
					// assoData.setHy(obj.getOrganizationMini());
					conn.add(assoData);
				}
				o.add(new ASSOData(node.getMemo(), conn));
			}
		}
		// 知识
		List<ASSOData> k = new ArrayList<ASSOData>();
		if (knowList != null) {
			for (KnowledgeNode node : knowList) {
				List<DemandASSOData> conn = new ArrayList<DemandASSOData>();
				for (KnowledgeMini2 obj : node.getListKnowledgeMini2()) {
					DemandASSOData assoData = new DemandASSOData();
					assoData.type = 6; //
					assoData.id = obj.id + "";
					assoData.title = obj.title;
					// assoData.setOwnerid(App.getUserID()); //创建id 没有
					// assoData.setOwnername(App.getNick());// 创建人 没有
					assoData.columnpath = obj.columnpath;
					assoData.columntype = obj.type + "";//
					conn.add(assoData);
				}
				k.add(new ASSOData(node.getMemo(), conn));
			}
		}
		// 事件 （需求）
		List<ASSOData> r = new ArrayList<ASSOData>();
		if (affairList != null) {
			for (AffairNode node : affairList) {
				List<DemandASSOData> conn = new ArrayList<DemandASSOData>();
				for (AffairsMini obj : node.getListAffairMini()) {
					DemandASSOData assoData = new DemandASSOData();
					if ("1".equals(obj.requirementType)) {
						assoData.type = 0; //
					} else if ("2".equals(obj.requirementType)) {
						assoData.type = 1;
					}

					assoData.id = obj.id + "";
					assoData.title = obj.title;
					assoData.name = obj.name;
					assoData.ownerid = App.getUserID();
					assoData.ownername = App.getNick();
					assoData.requirementtype = obj.reserve; // 事件类型
					conn.add(assoData);
				}
				r.add(new ASSOData(node.getMemo(), conn));
			}
		}
		asso.value = new ASSORPOK(r, p, o, k);
		return asso.value;

	}
	/**
	 * 详情页面数据填充
	 */
	public void initKnowledgeDetailsData() {
		try {
		activity.showLoadingDialog();
		if (mKnowledge2 != null) {
			showKnowledgeDetailsUI();
		} else {
			hideKnowledgeDetailsUI();
			return;
		}
		mKnowledgeId = mKnowledge2.getId();
		mKnowledgeType = mKnowledge2.getType();
		mTitle = mKnowledge2.getTitle();
		mUname = mKnowledge2.getUname();
		mCreatetime = mKnowledge2.getCreatetime();
		/* mKnowledgeUrl = mKnowledge2.getKnowledgeUrl(); */
		mKnowStatics = mKnowledge2.getKnowledgeStatics();
		content = mKnowledge2.getContent();

		if (mKnowDetails == null || mKnowDetails.size() <= 0) {
			String tagStr = mKnowledge2.getListTag() != null ? mKnowledge2.getListTag().toString() : "";
			if (!TextUtils.isEmpty(tagStr) && tagStr.length() > 2) {
				listTag = tagStr.substring(1, tagStr.length() - 1).split(",");
				listTag = FileUtils.deleteRepString(listTag);
				predicateLayout.setDividerLine(Util.DensityUtil.dip2px(
						mContext, 5));
				predicateLayout.setDividerCol(Util.DensityUtil.dip2px(mContext,
						30));
				initTags(predicateLayout);
				knowledgeTagLl.setVisibility(View.VISIBLE);
			} else {
				predicateLayout.setVisibility(View.GONE);
				knowledgeTagLl.setVisibility(View.GONE);
			}
			connectionNodeList = mKnowledge2
					.getListRelatedConnectionsNode();
			organizationList = mKnowledge2
					.getListRelatedOrganizationNode();
			knowList = mKnowledge2
					.getListRelatedKnowledgeNode();
			affairList = mKnowledge2
					.getListRelatedAffairNode();
			
			ASSORPOK newASSO = createNewASSO();
			if (!connectionNodeList.isEmpty()) {
				assoLL.setVisibility(View.VISIBLE);
				contactsTitle.setVisibility(View.VISIBLE);
				contactsLl.setVisibility(View.VISIBLE);
				peopleAdapter.setAssoList(toDemandASSOData(newASSO.p));
				peopleAdapter.notifyDataSetChanged();
			}
			if (!organizationList.isEmpty()) {
				assoLL.setVisibility(View.VISIBLE);
				organizationTitle.setVisibility(View.VISIBLE);
				organizationLl.setVisibility(View.VISIBLE);
				organizationAdapter.setAssoList(toDemandASSOData(newASSO.o));
				organizationAdapter.notifyDataSetChanged();
				
			}
			if (!knowList.isEmpty()) {
				assoLL.setVisibility(View.VISIBLE);
				knowledge_Ll.setVisibility(View.VISIBLE);
				knowledgeGroupAdapter.setListRelatedKnowledgeNode(toDemandASSOData(newASSO.k));
				knowledgeGroupAdapter.notifyDataSetChanged();
				
			}
			if (!affairList.isEmpty()) {
				assoLL.setVisibility(View.VISIBLE);
				affria_Ll.setVisibility(View.VISIBLE);
				affriaGroupAdapter.setListRelatedKnowledgeNode(toDemandASSOData(newASSO.r));
				affriaGroupAdapter.notifyDataSetChanged();
			}
			contacts_horizontalListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					DemandASSOData assoData = peopleAdapter.getItem(position);
					if (assoData.type == 2) {
						long assoDataID = TextUtils.isEmpty(assoData.id) ? 0L
								: Long.parseLong(assoData.id);
						ENavigate.startContactsDetailsActivity(getActivity(), 2,
								assoDataID, 0, 1);
					}
					if (assoData.type == 3) {
						ENavigate.startRelationHomeActivity(getActivity(),
								assoData.id, true, ENavConsts.type_details_other);
					}
				}
			});
			organization_horizontalListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					DemandASSOData org_assoData = organizationAdapter.getItem(position);
					if (org_assoData.type == 4) {
						ENavigate.startOrgMyHomePageActivity(getActivity(),
								Long.parseLong(org_assoData.id),
								Long.parseLong(org_assoData.ownerid), false, 2);
					} else if (org_assoData.type == 5) {
						ENavigate.startClientDedailsActivity(getActivity(),
								Long.parseLong(org_assoData.id), 1, 6);
					}
				}
			});
			knowledge_needDetailsLabelInfoItem.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					DemandASSOData knowledgeNode = (DemandASSOData) knowledgeGroupAdapter.getItem(arg2);
					// 知识
					ENavigate.startKnowledgeOfDetailActivity(getActivity(),
							Long.parseLong(knowledgeNode.id),1
							/*Integer.parseInt(asso.columntype)*/);
				}
			});
			affria_needDetailsLabelInfoItem.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					DemandASSOData affairNode = (DemandASSOData) affriaGroupAdapter.getItem(arg2);
					ENavigate.startNeedDetailsActivity(getActivity(),
							affairNode.id, 1); // 跳转到需求详情界面
				}
			});

			
			
//			/* 与知识关联的人对象组 */
//			for (int i = 0; i < connectionList.size(); i++) {
//				KnowledgeDetailsLv knowledgeDetailsLv = new KnowledgeDetailsLv();
//				knowledgeDetailsLv.setType(TYPE_CONNECTIONS);
//				knowledgeDetailsLv.setRelatedPeopleList(connectionList.get(i));
//				mKnowDetails.add(knowledgeDetailsLv);
//			}
//			/* 与知识关联的组织对象组 */
//			for (int i = 0; i < organizationList.size(); i++) {
//				KnowledgeDetailsLv knowledgeDetailsLv = new KnowledgeDetailsLv();
//				knowledgeDetailsLv.setType(TYPE_ORGANIZATION);
//				knowledgeDetailsLv.setRelatedOrganizationList(organizationList
//						.get(i));
//				mKnowDetails.add(knowledgeDetailsLv);
//			}
//			/* 与知识关联的知识对象 */
//			for (int i = 0; i < knowList.size(); i++) {
//				KnowledgeDetailsLv knowledgeDetailsLv = new KnowledgeDetailsLv();
//				knowledgeDetailsLv.setType(TYPE_KNOWLEDGE);
//				knowledgeDetailsLv.setRelatedKnowledgeList(knowList.get(i));
//				mKnowDetails.add(knowledgeDetailsLv);
//			}
//			/* 与知识关联的事件对象组 */
//			for (int i = 0; i < affairList.size(); i++) {
//				KnowledgeDetailsLv knowledgeDetailsLv = new KnowledgeDetailsLv();
//				knowledgeDetailsLv.setType(TYPE_AFFAIR);
//				knowledgeDetailsLv.setRelatedAffairList(affairList.get(i));
//				mKnowDetails.add(knowledgeDetailsLv);
//			}
		}

		/* 知识评论数 */
		if (null != mKnowStatics&& mKnowStatics.getCommentcount() > 0) {
			
		if ( mKnowStatics.getCommentcount()<100) {
			commentNumTv.setText(mKnowStatics.getCommentcount() + "");
			knowCommentTv.setText(mKnowStatics.getCommentcount() + "");
			knowCommentIv.setImageResource(R.drawable.comment_underthe100);
			knowCommentTv.setVisibility(View.VISIBLE);
			knowCommentRedPointIv.setVisibility(View.GONE);
		} else {
			commentNumTv.setText("");
			knowCommentTv.setVisibility(View.GONE);
			knowCommentRedPointIv.setVisibility(View.VISIBLE);
		}
		}else
		{
			knowCommentIv.setImageResource(R.drawable.comment_overthe100);
		}
		/* 知识收藏数 */
		if (null != mKnowStatics && mKnowStatics.getCollectioncount() > 0) {
			collectionNumTv.setText(mKnowStatics.getCollectioncount() + "");
		} else {
			collectionNumTv.setText("");
		}
		/* 设置知识详情标题 */
		if (!TextUtils.isEmpty(mTitle) && !"null".equals(mTitle)) {
			knoDetailsTitleTv.setText(mTitle);
		} else {
			knoDetailsTitleTv.setText("");
		}

		StringBuilder strBuilder = new StringBuilder();
		/* 设置作者 */
		if (!TextUtils.isEmpty(mUname) && !"null".equals(mUname)) {
			if (!TextUtils.isEmpty(mCreatetime) && !"null".equals(mCreatetime)) {strBuilder.append(JTDateUtils.formatDate(mCreatetime, JTDateUtils.DATE_FORMAT_6 + "     分享者:"));
			} else {
				strBuilder.append("");
			}
			strBuilder.append(mUname);
			knoDetailsAuthorTv.setText(strBuilder.toString());
		} else {
			knoDetailsAuthorTv.setText(strBuilder.toString());
		}

		/* 是否收藏该知识 */
		if (mKnowledge2.isCollected()) {
			showCollectionView(true);// 已经收藏
		} else {
			showCollectionView(false);
		}

		if(mKnowledge2.isZhongLeForMe()){
			if (isShowSave || mKnowledge2.isSaved() || mKnowledge2.getCid() == 0) {//自己创建的都能转发
				knowShareIv.setVisibility(View.VISIBLE);
				isShowSave = true;
			} else{//不是自己创建的中乐不能转发
				knowShareIv.setImageDrawable((getResources().getDrawable(R.drawable.forward_share_white_gray)));
				knowShareIv.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Toast.makeText(mContext, "您暂未获得分享该知识的权限", 1).show();
						return;
					}
				});
				isShowSave = false;
			}
		}else{//不是中乐能转发
			if (isShowSave || mKnowledge2.isSaved() || mKnowledge2.getCid() == 0) {//自己创建的都能转发
				knowShareIv.setVisibility(View.VISIBLE);
				isShowSave = true;
			}
		}
		/* 加载需要显示的网页 */
		if (TextUtils.isEmpty(content) || "null".equals(content)
				|| content.contains("null")) {
			content = "";
		}else{
			if(content.contains("<audio") ||  content.contains("<embed")){
				isContainsMultiMedia  = true;
			}
		}
		if (URLUtil.isNetworkUrl(content.trim())) {// 判断url的格式是否有效
			contentWv.loadUrl(content.trim());
		} else if (URLUtil.isNetworkUrl(filterHtml(content.trim()))) {
			contentWv.loadUrl(filterHtml(content.trim()));
		} else {
			contentWv.loadDataWithBaseURL(mKnowledgeUrl, content, "text/html",
					"utf-8", null);
		}
		if (App.getUserID().equals(String.valueOf(mKnowledge2.getUid()))) {
			createdByMySelf = true;
		}
		
		///////////////////////////////目录和权限设置////////////////////////////////
		if (App.getUserID().equals(String.valueOf(mKnowledge2.getUid()))) {
			updateKnowledgeCategoryUi();
			updateKnowledgePermission();
		}
		
		// 所有附件对象
		LinearLayout knowledge_ll = (LinearLayout) mContext.findViewById(R.id.knowledge_ll);
//		LinearLayout	listJtFile_Ll = (LinearLayout) mContext.findViewById(R.id.listJtFile_Ll);
//		listJtFile_Ll.removeAllViews();
//		ArrayList<JTFile> listJtFile = mKnowledge2.getListJtFile();
//		if(listJtFile != null){
//			for(final JTFile file:listJtFile){
//				View v = View.inflate(mContext,
//						R.layout.demand_need_details_document_item, null);
//				TextView documentTv = (TextView) v
//						.findViewById(R.id.documentTv);
//				documentTv.setText(file.fileName);
//				documentTv.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						showLoadingDialog();
//						// Toast.makeText(getActivity().getApplicationContext(),
//						// file.fileTitle, Toast.LENGTH_SHORT).show();
//						new DownLoadAndOpen(file.url, file.suffixName)
//								.execute();
//					}
//				});
//				listJtFile_Ll.addView(v);
//			}
//		}
		
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			activity.dismissLoadingDialog();
		}
	}

	/**
	 * 显示知识详情UI界面
	 */
	private void showKnowledgeDetailsUI() {
		int[] detailscolors = new int[]{R.color.knowledge_details_bgcolor1,R.color.knowledge_details_bgcolor2,R.color.knowledge_details_bgcolor3,R.color.knowledge_details_bgcolor4,R.color.knowledge_details_bgcolor5,R.color.knowledge_details_bgcolor6}; 
		knowDetailsFrgHead.setVisibility(View.VISIBLE);
		knowDetailsFrgHead.setBackgroundColor(mContext.getResources().getColor(detailscolors[new Random().nextInt(6)]));
		knowDetailsFrgBottom.setVisibility(View.GONE);
	}

	/**
	 * 隐藏知识详情UI界面
	 */
	private void hideKnowledgeDetailsUI() {
		knowDetailsFrgHead.setVisibility(View.GONE);
		knowDetailsFrgBottom.setVisibility(View.INVISIBLE);
	}

	/**
	 * 获取知识详情对象和知识评论对象
	 */
	private void initData(long mKnowledgeId, int mKnowledgeType) {
		KnowledgeReqUtil.doGetKnoDetails(mContext, this, mKnowledgeId,
				mKnowledgeType, null);
	}

	
	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View view) {
		String MSG = "onClick()";
		
		if (useCollectionRb.getId() == view.getId()) {
			if (mKnowledge2 != null) {
//				KnowledgeReqUtil.doUpdateCollectKnowledge(mContext, this,mKnowledgeId, mKnowledgeType, "true", null);
			} else {
				showToast("取消失败");
			}
		} else if (collectionRb.getId() == view.getId()) {
			if (mKnowledge2 != null) {
//				KnowledgeReqUtil.doUpdateCollectKnowledge(mContext, this,mKnowledgeId, mKnowledgeType, "", null);
			} else {
				showToast("收藏失败");
			}
		} else if (knowShareRb.getId() == view.getId() ) {// 分享
			doShareKnow();// 分享知识
		}else if (knoDetailsAuthorTv.getId() == view.getId()) {
			if(mKnowledge2.getCid() <= 0)
			{
				Toast.makeText(mContext, "该用户暂无个人主页", 0).show();
			}else if(mKnowledge2.getAuthorType() == AuthorType.AUTHORTYPE_PEOPLE.value){
				ENavigate.startRelationHomeActivity(mContext,mKnowledge2.getCid()+"",true,ENavConsts.type_details_other);
			}else if(mKnowledge2.getAuthorType() == AuthorType.AUTHORTYPE_ORGANIZATION.value){
				ENavigate.startOrgMyHomePageActivity(mContext, mKnowledge2.getId(),mKnowledge2.getCid(), true, ENavConsts.type_details_org);
			}
		}else if(knowCommentRl.getId() == view.getId()){
			if (!isBottom  ) {
				frgKnowDetailsSv.scrollTo(0, knoDetailsWvLl.getHeight() + asso_Ll.getHeight() + knowDetailsFrgHead.getHeight());
				showBottomBar();
				isShowInput = true;
				isBottom = true;
			}else{
				frgKnowDetailsSv.fullScroll(ScrollView.FOCUS_UP);
				isBottom = false;
			}
			
		}else if(know_titlebar.getId() == view.getId()){
			frgKnowDetailsSv.fullScroll(ScrollView.FOCUS_UP);
			isBottom = false;
		}
		else if (saveRb.getId() == view.getId()) {// 保存(编辑)知识
			if (mKnowledge2 != null) {
				// 暂时取消标签过滤
				// String str = filterHtml(mKnowledge2.getContent());
				// mKnowledge2.setContent(str);
			}
			ENavigate.startCreateKnowledgeActivity(mContext,
					true, EConsts.ReqCode.CreateKnowledgeForResult, mKnowledge2, OperateType.Save);
			mContext.finish();
		} else if (forwardShareRb.getId() == view.getId() || knowShareIv.getId() == view.getId() ) {// 分享
			// FrameWorkUtils.showSharePopupWindowKnow(mContext,mKnowledge2);
			if (null != mKnowledge2 && null != mKnowledge2.toJTFile()) {
				String mSuffixName = mKnowledge2.toJTFile().getmSuffixName();
				mKnowledge2.setDesc(filterHtml(mSuffixName));
				JTFile jtFile = mKnowledge2.toJTFile();
//				jtFile.mFileName =App.getNick()+"分享了[知识] ";//+mKnowledge2.getTitle();
				jtFile.mFileName =mKnowledge2.getTitle();//+mKnowledge2.getTitle();
				jtFile.virtual = mKnowledge2.getType()+"";
				FrameWorkUtils.showSharePopupWindow2(mContext,
						jtFile);
			}
			/*
			 * if (mKnowledge2 != null) { //
			 * ENavigate.startShareActivity(mContext, mKnowledge2.toJTFile());
			 * // 分享到畅聊 ENavigate.startSocialShareActivity(mContext,
			 * mKnowledge2.toJTFile()); }
			 */
		} else if (commentsRb.getId() == view.getId()) {
			/** 跳转到回复页 0:知识详情页 1：知识回复页 */
			mViewPager.setCurrentItem(1);
		} else if (ecologicalDockingTv.getId() == view.getId()) {// 跳转到生态对接
			ENavigate.startJointResourceActivity(mContext,
					ResourceType.Knowledge, mKnowledge2.toKnowledgeMini2());
		} else if (KnowDetailsFrgBackIv.getId() == view.getId() || know_layoutTitle_backBtn.getId() == view.getId() || knowledgetypeTv.getId() == view.getId()) {
			mContext.onBackPressed();
		} 
		else if (view.getId() == webViewTextSizeRb.getId()) {
			changeFontSize();
		} 
		else if (knowledgeMoreIv.getId() == view.getId()) {
			if (App.getUserID().equals(String.valueOf(mKnowledge2.getUid()))) {
				Log.i("isCanDelete", "App="+App.getUserID()+";mKnowledge2="+mKnowledge2+";mKnowledge2.getUid()="+mKnowledge2.getUid());
				isCanDelete = true;
				isCanUpdate = true;
			}
			//所有知识都可以收藏
			knowledgeSquareMenuPopupWindow = new KnowledgeSquareMenuPopupWindow(mContext,webFontSize,isShowSave,isCanDelete, isCanUpdate, true);
			knowledgeSquareMenuPopupWindow.showAsDropDown(know_headerVi);
			knowledgeSquareMenuPopupWindow.setOnItemClickListener(new OnMyHomeMenuItemClickListener() {
						@Override
						public void comment() {
							mViewPager.setCurrentItem(1);
						}

						@Override
						public void save() {
							if (mKnowledge2.getPic() != null && mKnowledge2.getPic().length()>255) {
								mKnowledge2.setPic("");
							}
							ENavigate.startCreateKnowledgeActivity(mContext,
									true, EConsts.ReqCode.CreateKnowledgeForResult,
									mKnowledge2, OperateType.Save);
						}

						@Override
						public void collection() {
							if (!mKnowledge2.isCollected()) {
									KnowledgeReqUtil.doUpdateCollectKnowledge(
											mContext,
											KnowledgeDetailFragment.this,
											mKnowledgeId, mKnowledgeType, "",
											null);
//									mKnowledge2.setCollected(true);
							} else {
								Toast.makeText(mContext, "已经收藏过该知识", 0).show();
							}
						}

						@Override
						public void ecologicalDocking() {
							ENavigate.startJointResourceActivity(mContext,
									ResourceType.Knowledge,
									mKnowledge2.toKnowledgeMini2());
						}

						@Override
						public void bigfont() {
							changeFontSize2(2);
							webFontSize = 2;
						}
						@Override
						public void middlefont() {
							changeFontSize2(1);
							webFontSize = 1;
						}

						@Override
						public void smallfont() {
							changeFontSize2(0);
							webFontSize = 0;
						}

						@Override
						public void delete() {
							View view = View.inflate(mContext, R.layout.demand_user_setting_dialog1, null);
							((TextView) view.findViewById(R.id.infoTv)).setText("确认删除知识吗？");
							final Dialog dialog = new Dialog(mContext, R.style.MyDialog);
							// dialog.setCancelable(false);//是否允许返回
							dialog.addContentView(view, new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
							dialog.show();
							// 确定
							view.findViewById(R.id.confirmTv).setOnClickListener(
									new OnClickListener() {

										@Override
										public void onClick(View v) {
											// 确定
											showLoadingDialog();
											ArrayList<Long> idList = new ArrayList<Long>();
											idList.add(mKnowledge2.getId());
											ArrayList<Integer> typeList = new ArrayList<Integer>();
											typeList.add(mKnowledge2.getType());
											KnowledgeReqUtil.doDeleteKnowledgeById(mContext, KnowledgeDetailFragment.this, App.getUserID(),idList , typeList,0, null);
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
						}
						@Override
						public void update() {
							ENavigate.startCreateKnowledgeActivity(mContext,false, EConsts.ReqCode.CreateKnowledgeForResult,mKnowledge2, OperateType.Update);
							getActivity().finish();
							ActivityCollector.finishAll();
							ActivityCollector.activities.clear();
						}

						@Override
						public void change2work() {
							KnowledgeNode inKnowledgeNode = new KnowledgeNode();
							ArrayList<KnowledgeMini2> listKnowledgeMini2 = new ArrayList<KnowledgeMini2>();
							listKnowledgeMini2.add(mKnowledge2.toKnowledgeMini2());
							inKnowledgeNode.setListKnowledgeMini2(listKnowledgeMini2);
							inKnowledgeNode.setMemo("知识");
							ENavigate.startNewAffarActivityByRelation(mContext, null, null, inKnowledgeNode, null);
						}

					});
		}
	}

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
	 * 分享知识
	 */
	private void doShareKnow() {
		// 我正在使用金桐app，一款集商务社交、投融资项目对接、个人资源管理的商务应用神器！ 推荐给你，快来哦，轻点
		// http://app.gintong.com 即可下载。
		Intent share = new Intent(Intent.ACTION_SEND);
		share.setType("text/plain");
		if (null != mKnowledge2) {
			share.putExtra(Intent.EXTRA_SUBJECT, mKnowledge2.getTitle());
			share.putExtra(Intent.EXTRA_TEXT, mKnowledge2.getDesc()
					+ " (分享自金桐网)");
		} else {
			share.putExtra(Intent.EXTRA_TEXT, " (分享自金桐网)");
		}
		share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(share);
	}

	/**
	 * 修改网页中的字体大小
	 */
	private void changeFontSize2(int size) {
		
		switch (size) {
		case 0: 
			webSettings.setTextSize(WebSettings.TextSize.SMALLER);
			break;
		case 1:
			webSettings.setTextSize(WebSettings.TextSize.NORMAL);
			break;
		case 2:
			webSettings.setTextSize(WebSettings.TextSize.LARGEST);
			break;
		default:
			break;
		}
	}
	/**
	 * 修改网页中的字体大小
	 */
	private void changeFontSize() {
		webViewSize++;
//		webViewSize++;
		switch (webViewSize % 3) {
		case 0:
			webSettings.setTextSize(WebSettings.TextSize.SMALLER);
			webViewSizeTv.setText("小");
			break;
		case 1:
			webSettings.setTextSize(WebSettings.TextSize.NORMAL);
			webViewSizeTv.setText("中");
			break;
		case 2:
			webSettings.setTextSize(WebSettings.TextSize.LARGEST);
			webViewSizeTv.setText("大");
			break;
		default:
			break;
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (isVisibleToUser) {
			// 隐藏软键盘
			hideKeyboard();
		}
		super.setUserVisibleHint(isVisibleToUser);
	}

	/**
	 * 隐藏软键盘
	 */
	public void hideKeyboard() {
		try {
			if (null == mContext) {
				return;
			}
			InputMethodManager imm = (InputMethodManager) mContext
					.getSystemService(mContext.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getView().getWindowToken(), 0); // 强制隐藏键盘
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setCollectionNum(String collectionNum) {
		this.collectionNumTv.setText(collectionNum);
	}

	public void setCommentNum(String commentNum) {
		this.commentNumTv.setText(commentNum);
		knowCommentTv.setText(commentNum);
	}

	/**
	 * 显示收藏提示信息
	 * 
	 * @param bool
	 */
	public void showCollectionInfo(boolean bool) {
//		if (View.VISIBLE == useCollectionRb.getVisibility()) {
			if (bool) {
				showToast("收藏成功");
				mKnowledge2.setCollected(true);
			} else {
				showToast("收藏失败");
			}
//		} else {
//			if (!bool) {
//				showToast("已经收藏过该知识");
//			} else {
//				showToast("取消收藏");
//			}
//		}
	}

	/**
	 * 创建标签显示的TextView
	 * 
	 * @param tag
	 * @return
	 */
	private TextView newTagTv(String tag) {
		TextView tagTv = new TextView(mContext);
		tagTv.setTextColor(getResources().getColor(R.color.text_gray));
		tagTv.setSingleLine(true);
		int height = getResources().getDimensionPixelSize(
				R.dimen.know_detials_text_height);
		tagTv.setHeight(height);
		tagTv.setPadding(
				getResources().getDimensionPixelSize(
						R.dimen.know_detials_tags_Padding_left),
				0,
				getResources().getDimensionPixelSize(
						R.dimen.know_detials_tags_Padding_right), 0);
		tagTv.setBackgroundResource(R.drawable.taggv_item_bg_shape);
		tagTv.setGravity(Gravity.CENTER);
		tagTv.setText(tag);
		return tagTv;
	}

	/**
	 * 在将创建的TextView中填充数据,并且添加到predicateLayout
	 * 
	 * @param predicateLayout
	 */
	private void initTags(PredicateLayout predicateLayout) {
		predicateLayout.removeAllViews();//把之前的viewremove防止引起左右滑动时标签view不停增加现象
		for (int i = 0; i < listTag.length; i++) {
			TextView tagTv = newTagTv(listTag[i].trim());
			measureView(tagTv);
			tagTv.setWidth(tagTv.getMeasuredWidth()
					+ Util.DensityUtil.dip2px(mContext, 20));
			tagTv.setHeight(tagTv.getMeasuredHeight()
					+ Util.DensityUtil.dip2px(mContext, 5));
			predicateLayout.addView(tagTv);
		}
	}

	/**
	 * 测量View的的自身大小
	 * 
	 * @param view
	 */
	private void measureView(View view) {
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		view.measure(w, h);
	}

	/**
	 * 标记已读知识id
	 */
	private void markReadedKnowledgeId(String id) {
		String MSG = "markReadedKnowledgeId()";
		Log.i(TAG, MSG);
		// 标记知识已读状态 start
		SharedPreferences sp = getActivity().getSharedPreferences(
				EConsts.share_knowledgeSquare_readed_knowledge,
				Activity.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean(id, true);
		editor.commit();
		// 标记知识已读状态 end
	}
	
	private void updateKnowledgeCategoryUi() {
		ArrayList<UserCategory> categoryList = mKnowledge2.getListUserCategory();
		if (categoryList != null) {
			StringBuilder categorySb = new StringBuilder();

			for (int i = 0; i < categoryList.size(); i++) {
				UserCategory category = categoryList.get(i);
				categorySb.append(category.getAllCategoryname(category));
				if (i != categoryList.size() - 1) {
					categorySb.append("\n");
				}
			}

			categoryTv.setText(categorySb.toString());
		}
		if (categoryList != null && categoryList.size() != 0) {
			knowledgeCategoryLl.setVisibility(View.VISIBLE);
		}
	}
	
private String listPermission2Str(ArrayList<Connections> listPermission){
		
		if(listPermission == null){
			return "";
		}
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < listPermission.size(); i++) {
			Connections connections = listPermission.get(i);
			sb.append(connections.getName());
			if (i != listPermission.size() - 1 ) {
				sb.append("、");
			}
		}
		return sb.toString();
	}
	
boolean noPermission =false;
private String content;
private LinearLayout assoLL;
private LinearLayout contactsTitle;
private LinearLayout contactsLl;
private LinearLayout organizationTitle;
private LinearLayout organizationLl;
private LinearLayout knowledge_Ll;
private LinearLayout affria_Ll;
private BasicListView affria_needDetailsLabelInfoItem;
private BasicListView knowledge_needDetailsLabelInfoItem;
private HorizontalListView organization_horizontalListView;
private HorizontalListView contacts_horizontalListView;
private ArrayList<ConnectionNode> connectionNodeList;
private ArrayList<ConnectionNode> organizationList;
private ArrayList<KnowledgeNode> knowList;
private ArrayList<AffairNode> affairList;
	
	private void updateKnowledgePermission() {
			know_permissiontv.setText("权限");
			ArrayList<Connections> listHightPermission = mKnowledge2.getListHighPermission();
			ArrayList<Connections> listMiddlePermission = mKnowledge2.getListMiddlePermission();
			ArrayList<Connections> listLowPermission = mKnowledge2.getListLowPermission();
			if((listHightPermission == null || listHightPermission.size() <=0 )&&
					(listMiddlePermission == null || listMiddlePermission.size() <=0 ) &&
							(listLowPermission == null || listLowPermission.size() <=0) ){
				noPermission = true;
			}
			if(listHightPermission == null){
				listHightPermission = new ArrayList<Connections>();
			}
			if(listMiddlePermission == null){
				listMiddlePermission = new ArrayList<Connections>();
			}
			if(listLowPermission == null){
				listLowPermission = new ArrayList<Connections>();
			}
			
			if( noPermission || ( listHightPermission.size() == 0 & listMiddlePermission.size() == 0 & listLowPermission.size() == 0 ) ) {
				knowledgePermissionLl.setVisibility(View.GONE);
			}
			else {
				knowledgePermissionLl.setVisibility(View.VISIBLE);
				if (listHightPermission != null && listHightPermission.size() > 0) {
					hightPermissionLl.setVisibility(View.VISIBLE);
					hightPermissionTv.setText(listPermission2Str(mKnowledge2.getListHighPermission()));
				}
				else {
					hightPermissionLl.setVisibility(View.GONE);
				}
				
				if (listMiddlePermission != null && listMiddlePermission.size() > 0 ) {
					middlePermissionLl.setVisibility(View.VISIBLE);
					middlePermissionTv.setText(listPermission2Str(mKnowledge2.getListMiddlePermission()));
				}
				else {
					middlePermissionLl.setVisibility(View.GONE);
				}
				if (listLowPermission != null  && listLowPermission.size() > 0) {
					lowPermissionLl.setVisibility(View.VISIBLE);
					lowPermissionTv.setText(listPermission2Str(mKnowledge2.getListLowPermission()));
				}
				else {
					lowPermissionLl.setVisibility(View.GONE);
				}
				permissionLl.setVisibility(View.VISIBLE);
			}
	}

	@Override
	public void onPause() {
//		stopMultiMedia();
		popupWindowDismiss();
		super.onPause();
	}
	
	public void stopMultiMedia(){
		if(contentWv != null && isContainsMultiMedia){
			contentWv.reload();
		}
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	public WebView getContentWv() {
		return contentWv;
	}

	public void setContentWv(WebView contentWv) {
		this.contentWv = contentWv;
	}

	public Knowledge2 getmKnowledge2() {
		return mKnowledge2;
	}

	public void setmKnowledge2(Knowledge2 mKnowledge2) {
		this.mKnowledge2 = mKnowledge2;
	}
	
	private DownLoadAndOpen downLoadAndOpen;
	class DownLoadAndOpen extends AsyncTask<String, Void, File> {
		private String fileUrl;
		private String fileType;

		public DownLoadAndOpen(String fileUrl, String fileType) {
			this.fileUrl = fileUrl;
			this.fileType = fileType;
		}

		@Override
		protected File doInBackground(String... paramsArr) {
			if (null != downLoadAndOpen) {
				downLoadAndOpen.cancel(false);
			}
			downLoadAndOpen = this;

			File fileCache = null;
			File uploadFileDir = new File(Environment.getExternalStorageDirectory(), "/jt/fileCache");
			if (!uploadFileDir.exists()) {
				uploadFileDir.mkdirs();
			}
			String fileName = UUID.randomUUID().toString();
			try {
				fileCache = new File(uploadFileDir, fileName);
				if (!fileCache.exists()) {
					fileCache.createNewFile();
				}

				URL url = new URL(fileUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestProperty("Accept-Encoding", "identity");
				conn.connect();

				if (conn.getResponseCode() == 200) {
					// 创建输入流
					InputStream inputStream = conn.getInputStream();

					OutputStream outputStream = new FileOutputStream(fileCache);
					byte[] data = new byte[2048];
					int length = 0;
					while ((length = inputStream.read(data)) != -1) {
						outputStream.write(data, 0, length);
					}
					inputStream.close();
					outputStream.close();
				} else {
					fileCache = null;
				}

			} catch (Exception e) {
				fileCache = null;
			}
			return fileCache;
		}

		@Override
		protected void onPostExecute(File openFile) {
			if (!isCancelled()) {
				dismissLoadingDialog();
				if (null == openFile) {
					Toast.makeText(getActivity(), "附件下载失败", Toast.LENGTH_SHORT).show();
					return;
				}
				Intent intent = null;
				String temp = fileType.replaceAll("^([\\s\\S]*)([tT][xX][tT])$", "$2");
				if (fileType.replaceAll("^([\\s\\S]*)([tT][xX][tT]) *$", "$2").length() == 3) {
					intent = new Intent("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Uri uri2 = Uri.fromFile(openFile);
					intent.setDataAndType(uri2, "text/plain");
				} else if (fileType.replaceAll("^([\\s\\S]*)([pP][dD][fF]) *$", "$2").length() == 3) {
					intent = new Intent("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Uri uri = Uri.fromFile(openFile);
					intent.setDataAndType(uri, "application/vnd.ms-powerpoint");

				} else if (fileType.replaceAll("^([\\s\\S]*)([dD][oO][cC]) *$", "$2").length() == 3) {
					intent = new Intent("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Uri uri = Uri.fromFile(openFile);
					intent.setDataAndType(uri, "application/msword");

				} else if (fileType.replaceAll("^([\\s\\S]*)([dD][oO][cD][xX]) *$", "$2").length() == 4) {
					intent = new Intent("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Uri uri = Uri.fromFile(openFile);
					intent.setDataAndType(uri, "application/msword");

				} else if (fileType.replaceAll("^([\\s\\S]*)([xX][lL][sS]) *$", "$2").length() == 3) {
					intent = new Intent("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Uri uri = Uri.fromFile(openFile);
					intent.setDataAndType(uri, "application/vnd.ms-excel");

				} else if (fileType.replaceAll("^([\\s\\S]*)([xX][lL][sS][xX]) *$", "$2").length() == 4) {
					intent = new Intent("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Uri uri = Uri.fromFile(openFile);
					intent.setDataAndType(uri, "application/vnd.ms-excel");

				} else if (fileType.replaceAll("^([\\s\\S]*)([pP][pP][tT]) *$", "$2").length() == 3) {
					intent = new Intent("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Uri uri = Uri.fromFile(openFile);
					intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
				} else if (fileType.replaceAll("^([\\s\\S]*)([pP][nN][gG]) *$", "$2").length() == 3 || fileType.replaceAll("^([\\s\\S]*)([jJ][pP][gG]) *$", "$2").length() == 3
						|| fileType.replaceAll("^([\\s\\S]*)([jJ][pP][eE][gG]) *$", "$2").length() == 4) {

					intent = new Intent("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Uri uri = Uri.fromFile(openFile);
					intent.setDataAndType(uri, "image/*");
				} else {
					intent = new Intent("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Uri uri = Uri.fromFile(openFile);
					intent.setDataAndType(uri, "*/*");
				}
				startActivity(intent);
			}
		}
	}
	
	
	/**
	 * 评论代码
	 */
	
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
	/* 父评论id */
	private long mParentId = 0L;
	private ArrayList<KnowledgeComment> knowCommentsList;
	private CommentDetailsLvAdapter knowCommentsLvAdapter;
	private KnowledgeComment mKnowledgeComment;
	/* viewPager滑到当前fragment之前，已经加载了一次数据 */
	private int index = 0;
	private int size = 9999;//默认将所有评论加载出来。
	
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


	private LinearLayout knowCommentBackLL;

	private LinearLayout inputLl;
	
/*	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup root,
			Bundle savedInstance) {
		
		
		View view = inflater
		.inflate(R.layout.activity_industry_comment, root, false);
		
		return view;
	}*/

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

	
	public void changeSendinputLl(Boolean isVisible){
			
			if (isVisible) {
				mKnoCommentLv.CommentVisible = true;
			}else{
				mKnoCommentLv.CommentVisible = false;
			}
		
	}
	public void initData(View view) {
		
//		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
//				android.R.color.holo_green_light,
//				android.R.color.holo_orange_light,
//				android.R.color.holo_red_light);
//		swipeLayout.setOnRefreshListener(this);
		moreView = View.inflate(context,R.layout.pulldown_footer, null);
		mvTextView = (TextView) moreView
				.findViewById(R.id.pulldown_footer_text);
		mvProgressBar = (View) moreView
				.findViewById(R.id.pulldown_footer_loading);
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
		mSmileyPagerchange = ((ImageView) mContext
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
		mKnoCommentLv.setPullLoadEnable(false);
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
				this, knowledgeId, parentId,
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
		startCommentGetData(nowIndex);
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
	IBindData commentBindData = new IBindData() {
		
		@Override
		public void bindData(int tag, Object object) {
				
		}
	};
	private View commentView;
	private boolean mIsGone = true;
	private boolean mIsAnim = false;

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
									KnowledgeDetailFragment.this,
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
	public void onDestroy() {
		super.onDestroy();
		hideKeyboard();
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
//		if (lastItem == count && scrollState == this.SCROLL_STATE_IDLE) {
//			moreView.setVisibility(view.VISIBLE);
//			loadMoreData();
//		}
	}

	// 出发下拉刷新
	public void onRefresh() {
			startCommentGetData(index);
	}

	/**
	 * 获取页数据
	 */
	public boolean startCommentGetData(int pageIndex) {
//		knowActionForward = CommendType.GetKnowCommend;
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
	/**
	 * 显示底部view
	 */
	public void showBottomBar() {

        if (mIsGone&&!mIsAnim) {
        	inputLl.setVisibility(View.INVISIBLE);
            Animation translateAnimation = new TranslateAnimation(0, 0,inputLl.getHeight(), 0);
            translateAnimation.setDuration(300);
            translateAnimation.setInterpolator(new OvershootInterpolator(0.6f));
            inputLl.startAnimation(translateAnimation);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                	mIsAnim=true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                	inputLl.setVisibility(View.VISIBLE);
                    mIsGone=false;
                    mIsAnim=false;
                }
            });
        }
    }
	/**
	 * 隐藏底部view
	 */
	private void hideBottomBar() {

		if (!mIsGone&&!mIsAnim) {
			Animation translateAnimation = new TranslateAnimation(0, 0, 0, inputLl.getHeight());
			translateAnimation.setDuration(300);
			translateAnimation.setInterpolator(new OvershootInterpolator(0.6f));
			inputLl.startAnimation(translateAnimation);
			translateAnimation.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
					mIsAnim=true;
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					inputLl.setVisibility(View.GONE);
					mIsGone=true;
					mIsAnim=false;
				}
			});
		}
	}

	public void setCommentIcon() {
		if (ratio == 1.0) {
			/* 知识评论数 */
			if (null != knowCommentsList&& knowCommentsList.size() > 0) {
				knowCommentIv.setImageResource(R.drawable.comment_underthe100_full);
			}else{
				knowCommentIv.setImageResource(R.drawable.comment_overthe100_full);
			}
			knowCommentTv.setTextColor(R.color.auth_title_back);
		} else {
			/* 知识评论数 */
			if (null != knowCommentsList&& knowCommentsList.size() > 0) {
				knowCommentIv.setImageResource(R.drawable.comment_underthe100);
			}else
			{
				knowCommentIv.setImageResource(R.drawable.comment_overthe100);
			}
		}
	}
}
