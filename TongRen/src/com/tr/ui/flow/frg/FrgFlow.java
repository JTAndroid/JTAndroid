package com.tr.ui.flow.frg;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import m.framework.utils.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.App;
import com.tr.R;
import com.tr.api.HomeReqUtil;
import com.tr.image.ImageLoader;
import com.tr.model.home.MGetDynamic;
import com.tr.model.obj.DynamicApprove;
import com.tr.model.obj.DynamicComment;
import com.tr.model.obj.DynamicLocation;
import com.tr.model.obj.DynamicNews;
import com.tr.model.obj.DynamicPeopleRelation;
import com.tr.model.obj.DynamicPicturePath;
import com.tr.model.page.JTPage;
import com.tr.model.user.JTMember;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.adapter.PageViewAdpter;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.connections.viewfrg.BaseViewPagerFragment;
import com.tr.ui.demand.util.ChooseDataUtil;
import com.tr.ui.flow.FlowAssoActivity;
import com.tr.ui.flow.FlowLocationActivity;
import com.tr.ui.flow.ImageBrowserActivity;
import com.tr.ui.home.HomePageActivity;
import com.tr.ui.home.IndexDynamicOnClickLister;
import com.tr.ui.home.frg.FrgFlow.FlowType;
import com.tr.ui.people.model.Person;
import com.tr.ui.widgets.CommonSmileyParser;
import com.tr.ui.widgets.EditOrDeletePopupWindow;
import com.tr.ui.widgets.FlowLayout;
import com.tr.ui.widgets.MessageDialog;
import com.tr.ui.widgets.SmileyParser;
import com.tr.ui.widgets.SmileyView;
import com.tr.ui.widgets.EditOrDeletePopupWindow.OnMeetingOptItemClickListener;
import com.tr.ui.widgets.MessageDialog.OnDialogFinishListener;
import com.tr.ui.widgets.viewpagerheaderscroll.delegate.AbsListViewDelegate;
import com.tr.ui.widgets.NoScrollGridView;
import com.tr.ui.widgets.NoScrollListview;
import com.utils.common.EConsts;
import com.utils.common.EUtil;
import com.utils.common.Util;
import com.utils.common.Util.DensityUtil;
import com.utils.display.DisplayUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;
import com.utils.time.TimeUtil;

public class FrgFlow extends BaseViewPagerFragment implements IBindData{

	public static final String FLOW_TYPE = "flow_type";
	public static final String FLOW_USERID = "flow_userid";
	public static final int FLOW_PERSON = 1;
	public static final int FLOW_FRIEND = 2;
	public static final int FLOW_GT = 3;
	private static final String LEFTSPECCHAR = ((char) 0X1B) + "";
	private static final String RIGHTSPECCHAR = ((char) 0X11) + "";
	/* 动态展示XListView */
	public XListView mListView;
	/* 文本输入框 */
	public LinearLayout mHomeInputLL;
	private ImageView mHomeFrgPicIv;
	/* 回复文本输入框 */
	public EditText mHomeFrgCommentEt;
	/* 发送按钮 */
	private Button mHomeFrgSendCommentIv;
	/* 表情容器 */
	private LinearLayout mIndustrySmileyLL;
	/* true:显示SmileyView；false 显示软键盘 */
	private Boolean isShowface = false;
	/* 表情ViewPager */
	private ViewPager mFaceViewPager;
	/* 小图标 */
	private ImageView mSmileyPagerchange;
	
	private DynamicAdapter mDynamicAdapter;
	private DynamicCommentAdapter commentAdapter;
	/* 动态集合 */
	private List<DynamicNews> mlistDynamicNews = new ArrayList<DynamicNews>();
	private DynamicApprove mDynamicApprove;// 动态赞同对象
	private DynamicComment mDynamicComment;// 动态评论对象
	public int mIndex;//被操作的动态的下标
	public int mIndexComment;//被操作的评论的下标
	public String flag;//区分动态和评论
	private RelativeLayout mainRootView;
	public DynamicNews mDynamicNews;
	
	int mWidth;     // 屏幕宽度（像素）  
	int mHeight;   // 屏幕高度（像素）
	private int index = 0;

	public int type = FLOW_PERSON;
	public long userId;
	private IndexDynamicOnClickLister myOnClickListener;
	
	public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;
	private SmileyParser parser;
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_frg_flow_new, container, false);
		DisplayMetrics metric = new DisplayMetrics();  
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);  
		mWidth = metric.widthPixels;
		mHeight = metric.heightPixels;
		initView(view);
		initExpression(view);
		startGetData();
		parser = SmileyParser.getInstance(getActivity());
		return view;
	}
	
	public void initView(View view){
		mainRootView = (RelativeLayout) view.findViewById(R.id.home_root);
		mListView = (XListView) view.findViewById(R.id.home_frg_flow_listview);
		mHomeInputLL = (LinearLayout) view.findViewById(R.id.homeFrgInputLl);
		mHomeFrgPicIv = (ImageView) view.findViewById(R.id.homeFrgPicIv);
		mHomeFrgCommentEt = (EditText) view.findViewById(R.id.homeFrgCommentEt);
		mHomeFrgSendCommentIv = (Button) view.findViewById(R.id.homeFrgSendCommentIv);
		mIndustrySmileyLL = (LinearLayout) view.findViewById(R.id.industrySmileyLL);
		mFaceViewPager = (ViewPager) view.findViewById(R.id.industrySmileyPager);
		mSmileyPagerchange = ((ImageView) view.findViewById(R.id.smileyPagerchange));
		
		mHomeInputLL.setVisibility(View.GONE);
		mHomeFrgPicIv.setVisibility(View.GONE);
		mDynamicAdapter = new DynamicAdapter(getActivity());
		mListView.setAdapter(mDynamicAdapter);
		
		mHomeFrgCommentEt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (StringUtils.isEmpty(s.toString())) {
					mHomeFrgSendCommentIv.setBackgroundResource(R.drawable.comment_send);
					mHomeFrgSendCommentIv.setClickable(false);
				} else {
					mHomeFrgSendCommentIv.setBackgroundResource(R.drawable.comment_send1);
					mHomeFrgSendCommentIv.setClickable(true);
				}
			}
		});
		
		mListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				mHomeInputLL.setVisibility(View.GONE);
				mHomeFrgCommentEt.setText("");
				mHomeFrgCommentEt.setHint("");
				hideKeyboard();
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		});
		
		mListView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mHomeInputLL.setVisibility(View.GONE);
				mHomeFrgCommentEt.setText("");
				mHomeFrgCommentEt.setHint("");
				hideKeyboard();
				return false;
			}
		});
		
		mListView.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				index = 0;
//				mlistDynamicNews.clear();
				startGetData();
			}
			
			@Override
			public void onLoadMore() {
				startGetData();
			}
		});
		
		mHomeFrgSendCommentIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(flag.equals("dynamic")){//回复动态
					mDynamicComment = new DynamicComment();
					mDynamicComment.setDynamicId(mlistDynamicNews.get(mIndex).getId());
//					mDynamicComment.setTargetUserId(mlistDynamicNews.get(mIndex).getCreaterId());
//					mDynamicComment.setTargetUserName(mlistDynamicNews.get(mIndex).getCreaterName());
					mDynamicComment.setUserId(Long.valueOf(App.getUserID()));
					mDynamicComment.setUserName(App.getUser().getmNick());
					mDynamicComment.setComment(mHomeFrgCommentEt.getText().toString());
					mDynamicComment.setCtime(new Date().getTime());
					addComment(mDynamicComment);
				}else if(flag.equals("comment")){//回复评论
					mDynamicComment = new DynamicComment();
					mDynamicComment.setDynamicId(mDynamicNews.getId());
					mDynamicComment.setTargetUserId(mDynamicNews.getComments().get(mIndexComment).getUserId());
					mDynamicComment.setTargetUserName(mDynamicNews.getComments().get(mIndexComment).getUserName());
					mDynamicComment.setUserId(Long.valueOf(App.getUserID()));
					mDynamicComment.setUserName(App.getUser().getmNick());
					mDynamicComment.setComment(mHomeFrgCommentEt.getText().toString());
					mDynamicComment.setCtime(new Date().getTime());
					addComment(mDynamicComment);
				}
			}
		});
		
		mHomeFrgPicIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!isShowface) {
					mIndustrySmileyLL.setVisibility(View.VISIBLE);
					isShowface = true;
					hideKeyboard();// 隐藏软盘，加了之后gridView起不来
				} else {
					mIndustrySmileyLL.setVisibility(View.GONE);
					isShowface = false;
					mHomeInputLL.setVisibility(View.VISIBLE);
					mHomeFrgCommentEt.setHint("添加评论");
					showKeyboard();
				}
			}
		});
	}

	public void editTextCaptureFocus() {
		mHomeFrgCommentEt.setFocusable(true);
		mHomeFrgCommentEt.setFocusableInTouchMode(true);
		mHomeFrgCommentEt.requestFocus();
		showKeyboard();
	}
	
	private void initExpression(View view) {
		// 表情切换界面
		mFaceViewPager.setAdapter(new PageViewAdpter(getActivity(), smileyViewClickListener));
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
			final CommonSmileyParser parser = CommonSmileyParser.getInstance(getActivity());
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

	/** 隐藏软盘 */
	public void hideKeyboard() {
		try {
			InputMethodManager m = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			m.hideSoftInputFromWindow(mHomeFrgCommentEt.getApplicationWindowToken(), 0);
		} catch (Exception e) {
		}
	}

	/** 显示软键盘 */
	public void showKeyboard() {
		try {
			InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			manager.toggleSoftInputFromWindow(mHomeFrgCommentEt.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
		}
	}
	/**
	 * 获取页数据
	 */
	public void startGetData() {
		switch(type){
		case FLOW_PERSON:
			if(userId==0){
				userId = Long.valueOf(App.getUserID());
			}
			HomeReqUtil.getListMyDynamicNews(getActivity(), this, null, userId, index ++, 20);
			break;
		case FLOW_FRIEND:
			HomeReqUtil.getDynamicNewList(getActivity(), this, null, index ++, 20);
			break;
		case FLOW_GT:
			HomeReqUtil.getListGTDynamicNews(getActivity(), this, null, 1, index ++, 20);
			break;
		}
	}
	
	public class DynamicAdapter extends BaseAdapter {
		private Context mContext;
		private List<DynamicNews> mData;

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
		public Object getItem(int position) {
			return mData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final DynamicNews mDynamicNew = mData.get(position);
			final ItemHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.home_frg_flow_item, null);
				holder = new ItemHolder();
				holder.flow_user_img_iv = (ImageView) convertView.findViewById(R.id.flow_user_img_iv);
				holder.flow_user_name_tv = (TextView) convertView.findViewById(R.id.flow_user_name_tv);
				holder.flow_source_power_tv = (TextView) convertView.findViewById(R.id.flow_source_power_tv);
				holder.timeTv = (TextView) convertView.findViewById(R.id.timeTv);
				holder.addressTv = (TextView) convertView.findViewById(R.id.addressTv);
				holder.contentTv = (TextView) convertView.findViewById(R.id.contentTv);
				holder.moreTv = (TextView) convertView.findViewById(R.id.moreTv);
				holder.flowIv = (ImageView) convertView.findViewById(R.id.flowIv);
				holder.flowGv = (NoScrollGridView) convertView.findViewById(R.id.flowGv);
				holder.atperson_ll = (LinearLayout) convertView.findViewById(R.id.atperson_ll);
				holder.atpersonFF = (FlowLayout) convertView.findViewById(R.id.atpersonFF);
//				holder.atpersonTv = (TextView) convertView.findViewById(R.id.atpersonTv);
				holder.link_ll = (LinearLayout) convertView.findViewById(R.id.link_ll);
				holder.linkTv = (TextView) convertView.findViewById(R.id.linkTv);
				holder.topic_ll = (LinearLayout) convertView.findViewById(R.id.topic_ll);
				holder.topicTv = (TextView) convertView.findViewById(R.id.topicTv);
				holder.midLine = (View) convertView.findViewById(R.id.mid_line);
				holder.home_flow_published_common_ll = (LinearLayout) convertView.findViewById(R.id.home_flow_published_common_ll);
				holder.yes_num_tv = (TextView) convertView.findViewById(R.id.yes_num_tv);
				holder.comment_num_tv = (TextView) convertView.findViewById(R.id.comment_num_tv);
				holder.yesTv = (TextView) convertView.findViewById(R.id.yesTv);
				holder.commentLv = (NoScrollListview) convertView.findViewById(R.id.commentLv);
				holder.commentTv = (TextView) convertView.findViewById(R.id.commentTv);
				holder.home_frg_flow_comment_LL = (LinearLayout) convertView.findViewById(R.id.home_frg_flow_comment_LL);
				
				
				holder.home_flow_figure_ll = (LinearLayout) convertView.findViewById(R.id.home_flow_figure_ll);
				holder.home_flow_figure_content_ll = (LinearLayout) convertView.findViewById(R.id.home_flow_figure_content_ll);
				holder.home_flow_share_common = (LinearLayout) convertView.findViewById(R.id.home_flow_share_common);
				holder.home_flow_published_content_ll = (LinearLayout) convertView.findViewById(R.id.home_flow_published_content_ll);
				
				holder.flow_figure_name_tv = (TextView) convertView.findViewById(R.id.flow_figure_name_tv);
				holder.flow_figure_content_tv = (TextView) convertView.findViewById(R.id.flow_figure_content_tv);
				holder.flow_published_common_title_tv = (TextView) convertView.findViewById(R.id.flow_published_common_title_tv);
				holder.flow_published_content_tv = (TextView) convertView.findViewById(R.id.flow_published_content_tv);
				
				holder.flow_published_img_iv = (ImageView) convertView.findViewById(R.id.flow_published_img_iv);
				holder.flow_figure_img_iv = (ImageView) convertView.findViewById(R.id.flow_figure_img_iv);
				
				convertView.setTag(holder);
			}else{
				holder = (ItemHolder) convertView.getTag();
			}
			holder.yes_num_tv.setTag(mDynamicNew.getId());
			holder.flowIv.setMinimumWidth(Utils.dipToPx(mContext, 80));
			holder.flowIv.setMinimumHeight(Utils.dipToPx(mContext, 80));
			holder.flowIv.setMaxWidth(mWidth/2);
			holder.flowIv.setMaxHeight(mHeight/2);
			holder.flowIv.setScaleType(ScaleType.CENTER_CROP);
//			holder.flowIv.setImageResource(R.drawable.third_splash_pager);
			holder.flow_user_img_iv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ENavigate.startRelationHomeActivity(mContext, mDynamicNew.getCreaterId()+"",true,1);
				}
			});
			
			holder.moreTv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(holder.moreTv.getText().toString().equals("查看更多")){
						mDynamicNew.setShowAllcontent(true);
						holder.moreTv.setText("收起");
						holder.contentTv.setEllipsize(null);
						holder.contentTv.setSingleLine(false);
					}else{
						mDynamicNew.setShowAllcontent(false);
						holder.moreTv.setText("查看更多");
						holder.contentTv.setEllipsize(TextUtils.TruncateAt.END); // 收缩
						holder.contentTv.setMaxLines(3);
					}
					
				}
			});
			
			holder.addressTv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, FlowLocationActivity.class);
					intent.putExtra("location", mDynamicNew.getLocation());
					startActivity(intent);
				}
			});
			
			holder.link_ll.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), FlowAssoActivity.class);
					intent.putExtra("id", mDynamicNew.getId());
					startActivity(intent);
				}
			});
			
			holder.yes_num_tv.setOnClickListener(new OnClickListener() {
	            
				@Override
				public void onClick(View v) {
					long currentTime = Calendar.getInstance().getTimeInMillis();
	                if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {//防止快速点击
	                	lastClickTime = currentTime;
	                	
	                	mIndex = position;
						ArrayList<DynamicApprove> mDynamicApproves = mDynamicNew
								.getApproves();
						// 判断当前用户是否点赞
						boolean isPraised = false;
						if (mDynamicApproves != null) {
							for (DynamicApprove da : mDynamicApproves) {
								if (da.getUserId() == Long.valueOf(App.getUserID())) {
									isPraised = true;
									mDynamicApprove = da;
									break;
								}
							}
						}
						Drawable leftdb = null;
						if (isPraised) {
							leftdb = getResources().getDrawable(R.drawable.feed_like);
							leftdb.setBounds(0, 0, leftdb.getMinimumWidth(), leftdb.getMinimumHeight());
							holder.yes_num_tv.setCompoundDrawables(leftdb, null, null, null);
							if(type == FLOW_GT){
								HomeReqUtil.cancelDynamicGTApprove(getActivity(), FrgFlow.this, mDynamicApprove.getDynamicId(), mDynamicApprove.getId(), null);
								mDynamicApproves.remove(mDynamicApprove);
							}else{
								HomeReqUtil.cancelDynamicApprove(getActivity(), FrgFlow.this, mDynamicApprove.getDynamicId(), mDynamicApprove.getId(), null);
								mDynamicApproves.remove(mDynamicApprove);
							}
						} else {
							leftdb = getResources().getDrawable(R.drawable.feed_liked);
							leftdb.setBounds(0, 0, leftdb.getMinimumWidth(), leftdb.getMinimumHeight());
							holder.yes_num_tv.setCompoundDrawables(leftdb, null, null, null);
							mDynamicApprove = new DynamicApprove();
							mDynamicApprove.setDynamicId(mDynamicNew.getId());
							mDynamicApprove.setUserId(Long.valueOf(App.getUserID()));
							mDynamicApprove.setUserName(App.getNick());
							mDynamicApprove.setCtime(new Date().getTime());
							addApporve(mDynamicApprove);
						}
	                }
				}
			});
			
			holder.comment_num_tv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mIndex = position;
					flag = "dynamic";
					mHomeInputLL.setVisibility(View.VISIBLE);
					mHomeFrgCommentEt.setHint("添加评论");
					editTextCaptureFocus();
					mDynamicNews = mDynamicNew;
				}
			});
			
			holder.commentTv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(holder.commentTv.getText().toString().equals("全部评论")){
						mDynamicNew.setShowAllcomment(true);
						holder.commentTv.setText("收起");
						commentAdapter = (DynamicCommentAdapter) holder.commentLv.getAdapter();
						commentAdapter.setLen(commentAdapter.getmListComment().size());
						commentAdapter.notifyDataSetChanged();
					}else{
						mDynamicNew.setShowAllcomment(false);
						holder.commentTv.setText("全部评论");
						commentAdapter = (DynamicCommentAdapter) holder.commentLv.getAdapter();
						commentAdapter.setLen(commentAdapter.getmListComment().size()>5?5:commentAdapter.getmListComment().size());
						commentAdapter.notifyDataSetChanged();
					}
				}
			});
			
			convertView.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					EditOrDeletePopupWindow pupwindow = new EditOrDeletePopupWindow(getActivity());
					if(!(mDynamicNew.getCreaterId()+"").equals(App.getUserID())){
						pupwindow.hideDeleteButton("举报");
					}else{
						pupwindow.setBTText("举报", "删除");
					}
					pupwindow.setOnItemClickListener(new OnMeetingOptItemClickListener() {
						
						@Override
						public void edit(String editStr) {
							Person person = new Person();
							person.id = mDynamicNew.getCreaterId();
							ENavigate.startReportActivity(getActivity(),person);
						}
						
						@Override
						public void delete(String deleteStr) {
							MessageDialog messageDialog = new MessageDialog(getActivity());
							messageDialog.setContent("确定删除吗？");
							messageDialog.show();
							messageDialog.setOnDialogFinishListener(new OnDialogFinishListener(){
								@Override
								public void onFinish(String content) {
									mIndex = position;
									HomeReqUtil.delFlow(getActivity(), FrgFlow.this, null, mlistDynamicNews.get(mIndex).getId());
								}

								@Override
								public void onCancel(String content) {
									// TODO Auto-generated method stub
									
								}
							});
						}
					});
					pupwindow.showAtLocation(mListView, Gravity.CENTER, 0, 0);
					return false;
				}
			});
			
			myOnClickListener = new IndexDynamicOnClickLister(mDynamicNew, getActivity());
			
			holder.home_flow_figure_ll.setOnClickListener(myOnClickListener);
			holder.home_flow_share_common.setOnClickListener(myOnClickListener);
			
			if(mDynamicNew!=null){
				initData(holder, mDynamicNew);
			}
			return convertView;
		}

		private void initData(ItemHolder holder, final DynamicNews dn) {
			//头像、名称
			if(type == FLOW_GT){
				holder.flow_user_img_iv.setImageResource(R.drawable.gintong_smart_brain);
			}else{
				String avatar = dn.getPicPath();
				if(dn.getPicPath().indexOf("/web1/pic/avatar")!=-1){//web端发布的动态
					avatar = EAPIConsts.FILE_URL_WEB_AVATAR + dn.getPicPath();
				}else{
					avatar = dn.getPicPath();
				}
				ImageLoader.load(holder.flow_user_img_iv, avatar, R.drawable.ic_default_avatar);
			}
			holder.flow_user_name_tv.setText(TextUtils.isEmpty(dn.getCreaterName())?"":dn.getCreaterName());
			//时间
			if (!TextUtils.isEmpty(dn.getCtime())) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				String time = sdf.format(new Date(Long.valueOf(dn.getCtime())));
				holder.timeTv.setText(TimeUtil.TimeFormat(time));
			}
			//地址
			if(dn.getLocation()!=null){
					DynamicLocation location = dn.getLocation();
					if(location!=null){
						if(!TextUtils.isEmpty(location.getSecondLevel())&&!TextUtils.isEmpty(location.getName())){
							holder.addressTv.setText(location.getSecondLevel()+"·"+location.getName());
						}else if(!TextUtils.isEmpty(location.getSecondLevel())){
							holder.addressTv.setText(location.getSecondLevel());
						}else{
							holder.addressTv.setText(location.getName());
						}
					}
			}else{
				holder.addressTv.setText("");
			}
			//动态内容
			holder.contentTv.setVisibility(View.GONE);
			if(!TextUtils.isEmpty(dn.getContent())){
				holder.contentTv.setVisibility(View.VISIBLE);
				CharSequence dd = parser.addSmileySpans(dn.getContent());
				holder.contentTv.setText(dd);
			}
			if(dn.getContent().length()>100){
				holder.moreTv.setVisibility(View.VISIBLE);
			}else{
				holder.moreTv.setVisibility(View.GONE);
			}
			//动态图片
			holder.flowIv.setVisibility(View.GONE);
			holder.flowGv.setVisibility(View.GONE);
			if(dn.getPicturePaths()!=null){
				if(dn.getPicturePaths().size()>1){
					holder.flowIv.setVisibility(View.GONE);
					holder.flowGv.setVisibility(View.VISIBLE);
					if(dn.getPicturePaths().size()==2 || dn.getPicturePaths().size()==4){
						holder.flowGv.setNumColumns(2);
						holder.flowGv.setColumnWidth((mWidth-Utils.dipToPx(mContext, 36))/3);
						LinearLayout.LayoutParams gridParams =new LinearLayout.LayoutParams((mWidth-Utils.dipToPx(mContext, 36))/3*2, LayoutParams.WRAP_CONTENT);
						holder.flowGv.setLayoutParams(gridParams); //使设置好的布局参数应用到控件
					}else{
						holder.flowGv.setNumColumns(3);
						holder.flowGv.setColumnWidth((mWidth-Utils.dipToPx(mContext, 36))/3);
						LinearLayout.LayoutParams gridParams =new LinearLayout.LayoutParams((mWidth-Utils.dipToPx(mContext, 36)), LayoutParams.WRAP_CONTENT);
						holder.flowGv.setLayoutParams(gridParams); //使设置好的布局参数应用到控件
					}
					holder.flowGv.setAdapter(new GridViewAdapter(dn.getPicturePaths()));
				}else if(dn.getPicturePaths().size() == 1){
					holder.flowGv.setVisibility(View.GONE);
					holder.flowIv.setVisibility(View.VISIBLE);
					ImageLoader.load(holder.flowIv, dn.getPicturePaths().get(0).getSourcePath(), R.drawable.frgflow_img_null);
					holder.flowIv.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(getActivity(), ImageBrowserActivity.class);
							intent.putExtra("DynamicPicturePaths", dn.getPicturePaths());
							intent.putExtra("index", 0);
							getActivity().startActivity(intent);
						}
					});
					
					holder.flowIv.setOnTouchListener(onTouchListener);
				}
			}
			/** 根据不同的数据类型进行相应的布局展示 */
			String hintTitle = "";
			switch (dn.getType()) {
			case DynamicNews.TYPE_FORWARDING_KNOWLEDGE: /* 转发知识 */
				hintTitle = "分享了知识";
				forwardingUI(holder, dn);
				initKnowOrMeetOrDemandLayout(holder, dn);
				break;
			case DynamicNews.TYPE_FORWARDING_REQUIREMENT:/* 转发需求 */
				hintTitle = "分享了需求";
				forwardingUI(holder, dn);
				initKnowOrMeetOrDemandLayout(holder, dn);
				break;
			case DynamicNews.TYPE_FORWARDING_MEETING:/* 转发会议 */
				hintTitle = "分享了会议";
				forwardingUI(holder, dn);
				initKnowOrMeetOrDemandLayout(holder, dn);
				break;
			case DynamicNews.TYPE_CREATE_KNOWLEDGE: /* 发布知识 */
				hintTitle = FrgFlow.switchHintTitle(hintTitle, dn.getPtype());
				forwardingUI(holder, dn);
				initKnowOrMeetOrDemandLayout(holder, dn);
				break;
			case DynamicNews.TYPE_RECOMMEND_KNOWLEDGE: /* 金桐推荐知识 */
				hintTitle = "推荐了知识";
				forwardingUI(holder, dn);
				initKnowOrMeetOrDemandLayout(holder, dn);
				break;
			case DynamicNews.TYPE_RECOMMEND_REQUIREMENT: /* 金桐推荐需求 */
				hintTitle = "推荐了" + switchDemandType(dn.getLowType());
				forwardingUI(holder, dn);
				initKnowOrMeetOrDemandLayout(holder, dn);
				break;
			case DynamicNews.TYPE_CREATE_REQUIREMENT:/* 创建需求 */
				hintTitle = FrgFlow.switchHintTitleDemand(hintTitle, dn.getPtype()) + switchDemandType(dn.getLowType());
				forwardingUI(holder, dn);
				initKnowOrMeetOrDemandLayout(holder, dn);
				break;
			case DynamicNews.TYPE_CREATE_MEETING:/* 发布会议 */
				hintTitle = "创建了会议";
				forwardingUI(holder, dn);
				initKnowOrMeetOrDemandLayout(holder, dn);
				break;
			case DynamicNews.TYPE_USER_CARD:/* 用户名片 */
				hintTitle = "分享了用户";
				forwardingUI(holder, dn);
				initFigureLayout(holder, dn);
				break;
			case DynamicNews.TYPE_RECOMMEND_CUSTOMER:/* 金桐脑推荐的用户 */
				hintTitle = "推荐了用户";
				forwardingUI(holder, dn);
				initFigureLayout(holder, dn);
				break;
			case DynamicNews.TYPE_FORWARDING_ORGANIZATION:/* 转发组织 */
				hintTitle = "分享了组织";
				forwardingUI(holder, dn);
				initFigureLayout(holder, dn);
				break;
			case DynamicNews.TYPE_CREATE_CUSTOM:/* 创建客户 */
				hintTitle = FrgFlow.switchHintTitleDemand(hintTitle, dn.getPtype()) + "客户";
				forwardingUI(holder, dn);
				initFigureLayout(holder, dn);
				break;
			case DynamicNews.TYPE_FORWARDING_CUSTOM:/* 转发客户 */
				hintTitle = "分享了客户";
				forwardingUI(holder, dn);
				initFigureLayout(holder, dn);
				break;
			case DynamicNews.TYPE_CREATE_CONTACTS:/* 创建人脉 */
				hintTitle = FrgFlow.switchHintTitleDemand(hintTitle, dn.getPtype()) + "人脉";
				break;
			case DynamicNews.TYPE_RECOMMEND_CONTACTS:/* 金桐推荐人脉 */
				hintTitle = "推荐了人脉";
				forwardingUI(holder, dn);
				initFigureLayout(holder, dn);
				break;
			case DynamicNews.TYPE_FORWARDING_CONTACTS:/* 转发人脉 */
				hintTitle = "分享了人脉";
				forwardingUI(holder, dn);
				initFigureLayout(holder, dn);
				break;
			case DynamicNews.TYPE_RECOMMEND_ORGANIZATION:// 大数据推送的0客户 1组织　2未注册组织
				hintTitle = "推荐的组织";
				forwardingUI(holder, dn);
				initFigureLayout(holder, dn);
				break;
			case DynamicNews.TYPE_RECOMMEND_CUSTOM:// 大数据推送的0客户 1组织　2未注册组织
				hintTitle = "推荐的客户";
				forwardingUI(holder, dn);
				initFigureLayout(holder, dn);
				break;
			default:
			}
			holder.flow_source_power_tv.setText(hintTitle);
			if(TextUtils.isEmpty(hintTitle)){
				holder.home_flow_figure_ll.setVisibility(View.GONE);
				holder.home_flow_share_common.setVisibility(View.GONE);
			}
			//@其他人
			holder.atperson_ll.setVisibility(View.GONE);
			holder.atpersonFF.removeAllViews();
			if(dn.getPeopleRelation()!=null){
				if(dn.getPeopleRelation().size()>0){
					holder.atperson_ll.setVisibility(View.VISIBLE);
					for(int i=0;i<dn.getPeopleRelation().size();i++){
						final DynamicPeopleRelation dpr = dn.getPeopleRelation().get(i);
						LinearLayout.LayoutParams lp =new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
						TextView tv = new TextView(mContext);
						tv.setLayoutParams(lp);
						tv.setText(dpr.getUserName());
						tv.setTextColor(0xff569ee2);
						tv.setBackgroundResource(R.drawable.flow_bg);
						
						TextView tv_dot = new TextView(mContext);
						tv_dot.setLayoutParams(lp);
						tv_dot.setText(";  ");
						tv_dot.setTextColor(0xff569ee2);
						
						tv.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								ENavigate.startRelationHomeActivity(mContext, dpr.getUserId()+"",true,1);
							}
						});
						holder.atpersonFF.addView(tv);
						if((i+1)<dn.getPeopleRelation().size()){
							holder.atpersonFF.addView(tv_dot);
						}
					}
				}
			}
			//关联
			holder.link_ll.setVisibility(View.GONE);
			String linkStr = "";
			if(dn.getPeopleCount()>0){
				holder.link_ll.setVisibility(View.VISIBLE);
				linkStr += "人脉("+dn.getPeopleCount()+")、";
			}
			if(dn.getOrgCount()>0){
				holder.link_ll.setVisibility(View.VISIBLE);
				linkStr += "组织("+dn.getOrgCount()+")、";
			}
			if(dn.getKnowledgeCount()>0){
				holder.link_ll.setVisibility(View.VISIBLE);
				linkStr += "知识("+dn.getKnowledgeCount()+")、";
			}
			if(dn.getDemandCount()>0){
				holder.link_ll.setVisibility(View.VISIBLE);
				linkStr += "事件("+dn.getDemandCount()+")、";
			}
			holder.linkTv.setText(linkStr);
			if(!TextUtils.isEmpty(linkStr)){
				holder.linkTv.setText(linkStr.substring(0, linkStr.length()-1));
			}
			//话题
			holder.topic_ll.setVisibility(View.GONE);
			holder.topicTv.setText("");
			//点赞、评论
			holder.midLine.setVisibility(View.GONE);
			holder.yesTv.setVisibility(View.GONE);
			holder.home_frg_flow_comment_LL.setVisibility(View.GONE);
			//点赞
			Drawable leftdb = null;
			leftdb = getResources().getDrawable(R.drawable.feed_like);
			leftdb.setBounds(0, 0, leftdb.getMinimumWidth(), leftdb.getMinimumHeight());
			holder.yes_num_tv.setCompoundDrawables(leftdb, null, null, null);
			
			if(dn.getApproves()!=null){
				holder.midLine.setVisibility(View.VISIBLE);
				holder.yes_num_tv.setText(dn.getApproves().size()+"");
				if(dn.getApproves().size()>0){
					
					for (DynamicApprove da : dn.getApproves()) {
						if (da.getUserId() == Long.valueOf(App.getUserID())) {
							leftdb = getResources().getDrawable(R.drawable.feed_liked);
							leftdb.setBounds(0, 0, leftdb.getMinimumWidth(), leftdb.getMinimumHeight());
							holder.yes_num_tv.setCompoundDrawables(leftdb, null, null, null);
							break;
						}
					}
					
					//点赞的人
					String yesStr="";
					for(DynamicApprove da:dn.getApproves()){
						yesStr += da.getUserName()+"、";
					}
					holder.yesTv.setText("");
					if(!TextUtils.isEmpty(yesStr)){
						holder.home_frg_flow_comment_LL.setVisibility(View.VISIBLE);
						holder.yesTv.setVisibility(View.VISIBLE);
						String yesStrs = yesStr.substring(0, yesStr.length()-1)+"赞过";
						SpannableString spanstr = new SpannableString(yesStrs);
						spanstr.setSpan(new ForegroundColorSpan(0xff7d9ca9), yesStr.substring(0, yesStr.length()-1).length(), yesStrs.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						holder.yesTv.append(spanstr);
//						holder.yesTv.setText(yesStr.substring(0, yesStr.length()-1)+"赞过");
					}
				}
			}
			//评论
			holder.commentLv.setVisibility(View.GONE);
			holder.commentTv.setVisibility(View.GONE);
			if(dn.getComments()!=null){
				holder.midLine.setVisibility(View.VISIBLE);
				holder.comment_num_tv.setText(dn.getComments().size()+"");
				if(dn.getComments().size()>0){
					holder.home_frg_flow_comment_LL.setVisibility(View.VISIBLE);
					holder.commentLv.setVisibility(View.VISIBLE);
					if(dn.getComments().size()>5){
						holder.commentTv.setVisibility(View.VISIBLE);
					}
					
					commentAdapter = new DynamicCommentAdapter(mContext, FrgFlow.this);
					if(dn.isShowAllcomment()){
						commentAdapter.setData(dn.getComments(), dn, dn.getComments().size());
					}else{
						commentAdapter.setData(dn.getComments(), dn, dn.getComments().size() > 5 ? 5 : dn.getComments().size());
					}
					holder.commentLv.setAdapter(commentAdapter);
					commentAdapter.notifyDataSetChanged();
				}
			}
			
		}
		
		private class GridViewAdapter extends BaseAdapter {
			ArrayList<DynamicPicturePath> dpps;

			private GridViewAdapter(ArrayList<DynamicPicturePath> dpps) {
				this.dpps = dpps;
			}

			@Override
			public int getCount() {
				return dpps.size();
			}

			@Override
			public Object getItem(int position) {
				return dpps.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				ImageView view = new ImageView(mContext);
				ListView.LayoutParams params = new ListView.LayoutParams((mWidth-Utils.dipToPx(mContext, 36))/3, (mWidth-Utils.dipToPx(mContext, 36))/3);
				view.setLayoutParams(params);
				view.setPadding(Utils.dipToPx(mContext, 1), Utils.dipToPx(mContext, 1), Utils.dipToPx(mContext, 1), Utils.dipToPx(mContext, 1));
				view.setCropToPadding(true);
				view.setScaleType(ScaleType.CENTER_CROP);
				try {
					ImageLoader.load(view, dpps.get(position).getSourcePath(), R.drawable.frgflow_img_null);
				} catch (Exception e) {
					e.printStackTrace();
				}
				view.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(), ImageBrowserActivity.class);
						intent.putExtra("DynamicPicturePaths", dpps);
						intent.putExtra("index", position);
						getActivity().startActivity(intent);
					}
				});
				
				view.setOnTouchListener(onTouchListener);  
				return view;
			}
		}
		
	}
	
	/** 初始化 人脉/用户/组织/客户 布局 */
	private void initFigureLayout(ItemHolder holder, DynamicNews mDynamicNews) {
		/* 设置头像 */
		if (mDynamicNews.getType() == DynamicNews.TYPE_FORWARDING_ORGANIZATION || mDynamicNews.getType() == DynamicNews.TYPE_CREATE_CUSTOM
				|| mDynamicNews.getType() == DynamicNews.TYPE_FORWARDING_CUSTOM || mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_ORGANIZATION) {
			/* 客户组织设置头像 */
			Util.initAvatarImage(getActivity(), holder.flow_figure_img_iv, mDynamicNews.getTitle(), mDynamicNews.getImgPath(), 0, 2);
		} else if (mDynamicNews.getType() == DynamicNews.TYPE_CREATE_CONTACTS || mDynamicNews.getType() == DynamicNews.TYPE_FORWARDING_CONTACTS
				|| mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_CONTACTS || mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_CUSTOMER
				|| mDynamicNews.getType() == DynamicNews.TYPE_USER_CARD) {
			/* 人脉好友设置头像 */
			Util.initAvatarImage(getActivity(), holder.flow_figure_img_iv, mDynamicNews.getTitle(), mDynamicNews.getImgPath(), 0, 1);
		}

		/* 人脉和名片姓名 */
		holder.flow_figure_name_tv.setText(mDynamicNews.getTitle());
		holder.flow_figure_content_tv.setText(mDynamicNews.getClearContent());
	}
	
	public void forwardingUI(ItemHolder holder, DynamicNews mDynamicNews) {
		if (mDynamicNews.getType() == DynamicNews.TYPE_USER_CARD || mDynamicNews.getType() == DynamicNews.TYPE_FORWARDING_ORGANIZATION || mDynamicNews.getType() == DynamicNews.TYPE_CREATE_CUSTOM
				|| mDynamicNews.getType() == DynamicNews.TYPE_FORWARDING_CUSTOM || mDynamicNews.getType() == DynamicNews.TYPE_CREATE_CONTACTS
				|| mDynamicNews.getType() == DynamicNews.TYPE_FORWARDING_CONTACTS || mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_CONTACTS
				|| mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_CUSTOMER || mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_ORGANIZATION) {
			holder.home_flow_figure_ll.setVisibility(View.VISIBLE);
			holder.home_flow_share_common.setVisibility(View.GONE);
		} else {
			holder.home_flow_figure_ll.setVisibility(View.GONE);
			holder.home_flow_share_common.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 初始化 会议/知识/需求 布局
	 * 
	 * @param holder
	 */
	private void initKnowOrMeetOrDemandLayout(ItemHolder holder, DynamicNews mDynamicNews) {
		// 会议/知识/需求的标题
		if (!EUtil.isEmpty(mDynamicNews.getTitle())) {
			holder.flow_published_common_title_tv.setText(mDynamicNews.getTitle());
			holder.flow_published_common_title_tv.setVisibility(View.VISIBLE);
		} else {
			holder.flow_published_common_title_tv.setVisibility(View.GONE);
		}
		// 会议/知识/需求的图片
		if (!EUtil.isEmpty(mDynamicNews.getImgPath().trim())) {
			ImageLoader.load(holder.flow_published_img_iv, mDynamicNews.getImgPath(), R.drawable.frgflow_img_null);
			holder.flow_published_img_iv.setVisibility(View.VISIBLE);
		} else {
			holder.flow_published_img_iv.setVisibility(View.GONE);
		}

		// 会议/知识/需求的内容简介
		if (!EUtil.isEmpty(mDynamicNews.getClearContent())) {
			holder.flow_published_content_tv.setText(mDynamicNews.getClearContent());
			holder.flow_published_content_tv.setVisibility(View.VISIBLE);
		} else {
			holder.flow_published_content_tv.setVisibility(View.GONE);
		}
	}
	
	private class ItemHolder {
		public ImageView flow_user_img_iv;//头像
		public TextView flow_user_name_tv;//名称
		public TextView flow_source_power_tv;//权限
		public TextView timeTv;//时间
		public TextView addressTv;//地址
		public TextView contentTv;//动态内容
		public TextView moreTv;//查看更多
		public ImageView flowIv;//动态单张图片
		public NoScrollGridView flowGv;//动态多张图片
		public LinearLayout atperson_ll;//@布局
		public FlowLayout atpersonFF;
		public TextView atpersonTv;//@的人
		public LinearLayout link_ll;//关联布局
		public TextView linkTv;//关联的内容
		public LinearLayout topic_ll;//话题布局
		public TextView topicTv;//话题内容
		public View midLine;
		public LinearLayout home_flow_published_common_ll;//点赞、评论数量布局
		public TextView yes_num_tv;//点赞数量
		public TextView comment_num_tv;//评论数量
		public TextView yesTv;//点赞的人
		public NoScrollListview commentLv;//评论
		public TextView commentTv;//全部评论
		public LinearLayout home_frg_flow_comment_LL;
		
		public LinearLayout home_flow_figure_ll, home_flow_figure_content_ll, home_flow_share_common, home_flow_published_content_ll;
		public TextView flow_figure_name_tv, flow_figure_content_tv, flow_published_common_title_tv, flow_published_content_tv;
		public ImageView flow_published_img_iv, flow_figure_img_iv;
		
	}
	

	
	public OnTouchListener onTouchListener = new View.OnTouchListener() {  
        @Override  
        public boolean onTouch(View view, MotionEvent event) {  
            switch (event.getAction()) {  
            case MotionEvent.ACTION_UP:  
                changeLight((ImageView) view, 0);  
                // onclick  
                break;  
            case MotionEvent.ACTION_DOWN:  
                changeLight((ImageView) view, -80);  
                break;  
            case MotionEvent.ACTION_MOVE:  
                // changeLight(view, 0);  
                break;  
            case MotionEvent.ACTION_CANCEL:  
                changeLight((ImageView) view, 0);  
                break;  
            default:  
                break;  
            }  
            return false;  
        }  
  
    };
	
	private void changeLight(ImageView imageview, int brightness) {  
	    ColorMatrix matrix = new ColorMatrix();  
	    matrix.set(new float[] { 1, 0, 0, 0, brightness, 0, 1, 0, 0,  
	            brightness, 0, 0, 1, 0, brightness, 0, 0, 0, 1, 0 });  
	    imageview.setColorFilter(new ColorMatrixColorFilter(matrix));  
	  
	} 
	
	/* 分页JTPage */
	private JTPage mPage;

	@Override
	public void bindData(int tag, Object object) {
		if (tag == EAPIConsts.HomeReqType.HOME_REQ_GET_MY_DYNAMIC
				|| tag == EAPIConsts.HomeReqType.HOME_REQ_GET_DYNAMIC
				|| tag == EAPIConsts.HomeReqType.HOME_REQ_GET_GT_DYNAMIC) {

			mListView.stopLoadMore();
			mListView.stopRefresh();
			
			MGetDynamic mGetDynamic = (MGetDynamic) object;
			if (mGetDynamic != null && mGetDynamic.getJtPage() != null /*&& mGetDynamic.getJtPage().getTotal() != 0*/) {
				mPage = mGetDynamic.getJtPage();
				if (null != mPage && mPage.getIndex() == 0) {
					mlistDynamicNews.clear();
				}
				if ((mPage != null) && (mPage.getLists() != null)) {
					if (mPage.getLists().size() < 1) {
						mDynamicAdapter.setData(mlistDynamicNews);
						mDynamicAdapter.notifyDataSetChanged();
						mListView.setPullLoadEnable(false);
						if (mPage.getIndex() == 0 || mlistDynamicNews.size() < 1) {
							mainRootView.setBackgroundResource(R.drawable.empty);
						} else {
							mainRootView.setBackgroundColor(0xffffffff);
						}
						return;
					}
					for (int i = 0; i < mPage.getLists().size(); i++) {
						mlistDynamicNews.add((DynamicNews) mGetDynamic.getJtPage().getLists().get(i));
					}
					mDynamicAdapter.setData(mlistDynamicNews);
					mDynamicAdapter.notifyDataSetChanged();
				} else {
					mlistDynamicNews.clear();
					mDynamicAdapter.setData(mlistDynamicNews);
					mDynamicAdapter.notifyDataSetChanged();
				}

				if (mlistDynamicNews.isEmpty()) {
					// 当动态没数据时显示提示图片
					mListView.setVisibility(View.INVISIBLE);
					mainRootView.setBackgroundResource(R.drawable.empty);
					mListView.setPullLoadEnable(false);
				} else {
					mListView.setPullLoadEnable(true);
					mListView.setVisibility(View.VISIBLE);
					mainRootView.setBackgroundColor(0xffffffff);
				}
			}else{
				// 当动态没数据时显示提示图片
				mListView.setVisibility(View.INVISIBLE);
				mainRootView.setBackgroundResource(R.drawable.empty);
				mListView.setPullLoadEnable(false);
			}
		}else if(tag == EAPIConsts.HomeReqType.HOME_REQ_ADD_APPORVE || tag == EAPIConsts.HomeReqType.HOME_REQ_ADD_GT_APPORVE){//点赞
			if(object != null){
				long approveId = (Long) object;
				mDynamicApprove.setId(approveId);
				mlistDynamicNews.get(mIndex).getApproves().add(0, mDynamicApprove);
				mDynamicAdapter.notifyDataSetChanged();
			}
		}else if(tag == EAPIConsts.HomeReqType.HOME_REQ_DEL_APPORVE || tag == EAPIConsts.HomeReqType.HOME_REQ_DEL_GT_APPORVE){//取消点赞
			if(object != null){
				String code = (String)object;
				if(!code.equals("6007")){
					mDynamicAdapter.notifyDataSetChanged();
				}else{
					Toast.makeText(getActivity(), "取消点赞失败！", Toast.LENGTH_SHORT).show();
				}
			}
		}else if(tag == EAPIConsts.HomeReqType.HOME_REQ_ADD_DYNAMIC_COMMENT_NEW || tag == EAPIConsts.HomeReqType.HOME_REQ_ADD_GT_DYNAMIC_COMMENT){//添加评论
			mHomeInputLL.setVisibility(View.GONE);
			mHomeFrgCommentEt.setText("");
			mHomeFrgCommentEt.setHint("");
			hideKeyboard();
			if(object != null){
				long commendId = (Long) object;
				mDynamicComment.setId(commendId);
				mDynamicNews.getComments().add(mDynamicComment);
				mDynamicAdapter.notifyDataSetChanged();
			}
		}else if(tag == EAPIConsts.HomeReqType.HOME_REQ_DELETE_DYNAMIC_COMMENT_NEW || tag == EAPIConsts.HomeReqType.HOME_REQ_DELETE_GT_DYNAMIC_COMMENT){//删除评论
			if(object != null){
				String code = (String)object;
				if(!code.equals("6009")){
					mDynamicAdapter.notifyDataSetChanged();
				}else{
					Toast.makeText(getActivity(), "删除评论失败！", Toast.LENGTH_SHORT).show();
				}
			}
		}else if(tag == EAPIConsts.HomeReqType.HOME_REQ_DEL_FLOW){
			if(object != null){
				String code = (String)object;
				if(!code.equals("6005")){
					mlistDynamicNews.remove(mIndex);
					mDynamicAdapter.notifyDataSetChanged();
				}else{
					Toast.makeText(getActivity(), "删除动态失败！", Toast.LENGTH_SHORT).show();
				}
			}
			if (mlistDynamicNews.isEmpty()) {
				// 当动态没数据时显示提示图片
				mListView.setVisibility(View.INVISIBLE);
				mainRootView.setBackgroundResource(R.drawable.empty);
				mListView.setPullLoadEnable(false);
			}
		}
	}
	
	//点赞
	public boolean addApporve(DynamicApprove dynamicApprove) {
		if (dynamicApprove != null) {
			if(type == FLOW_GT){
				return HomeReqUtil.addDynamicGTApprove(getActivity(), this, dynamicApprove, null);
			}else{
				return HomeReqUtil.addDynamicApporve(getActivity(), this, dynamicApprove, null);
			}
		} else {
			return false;
		}
	}
	
	//评论
	public boolean addComment(DynamicComment DynamicComment) {
		if (DynamicComment != null) {
			if(type == FLOW_GT){
				return HomeReqUtil.addDynamicGTComment(getActivity(), this, DynamicComment, null);
			}else{
				return HomeReqUtil.addDynamicComment(getActivity(), this, DynamicComment, null);
			}
		} else {
			return false;
		}
	}
	
	//评论
	public boolean delComment(DynamicComment comment) {
		if (comment != null) {
			if(type == FLOW_GT){
				return HomeReqUtil.deleteDynamicGTComment(getActivity(), this, null, comment.getDynamicId(), comment.getId());
			}else{
				return HomeReqUtil.deleteNewDynamicComment(getActivity(), this, null, comment.getDynamicId(), comment.getId());
			}
		} else {
			return false;
		}
	}

	private AbsListViewDelegate mAbsListViewDelegate = new AbsListViewDelegate();
	@Override
	public boolean isViewBeingDragged(MotionEvent event) {
		return mAbsListViewDelegate.isViewBeingDragged(event, mListView);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data!=null) {
			if (9==requestCode&&999==resultCode) {
				DynamicNews dynamicNews = (DynamicNews) data.getSerializableExtra("CreateDynamicNews");
				if(dynamicNews != null){
					mlistDynamicNews.add(0,dynamicNews);
					mDynamicAdapter.setData(mlistDynamicNews);
					mDynamicAdapter.notifyDataSetChanged();
					
					mListView.setPullLoadEnable(true);
					mListView.setVisibility(View.VISIBLE);
					mainRootView.setBackgroundColor(0xffffffff);
				}
			}
		}
	}
	
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
	}/**
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
	
	public void setSelection(){
		mListView.setSelection(0);
	}
}
