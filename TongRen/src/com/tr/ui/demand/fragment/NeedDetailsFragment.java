package com.tr.ui.demand.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.tr.App;
import com.tr.R;
import com.tr.api.DemandReqUtil;
import com.tr.model.demand.ASSOData;
import com.tr.model.demand.CustomData;
import com.tr.model.demand.DemandASSO;
import com.tr.model.demand.DemandASSOData;
import com.tr.model.demand.DemandAttFile;
import com.tr.model.demand.DemandData;
import com.tr.model.demand.DemandDetailsData;
import com.tr.model.demand.DemandFile;
import com.tr.model.demand.DemandPvFile;
import com.tr.model.demand.ImageItem;
import com.tr.model.demand.IntroduceData;
import com.tr.model.demand.LableData;
import com.tr.model.knowledge.UserCategory;
import com.tr.model.obj.JTFile;
import com.tr.model.obj.MobilePhone;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.demand.BrowesPhotoVideo;
import com.tr.ui.demand.NeedDetailsActivity;
import com.tr.ui.demand.MyView.CustomView;
import com.tr.ui.demand.MyView.MyGridView;
import com.tr.ui.demand.MyView.MyViewPager;
import com.tr.ui.demand.util.DemandUtil;
import com.tr.ui.demand.util.OnNeedDetails;
import com.tr.ui.demand.util.OnNeedRefresh;
import com.tr.ui.demand.util.TextStrUtil;
import com.tr.ui.widgets.ConnsCallAndSendSmsDialog;
import com.tr.ui.widgets.GuidePopupWindow;
import com.tr.ui.widgets.HorizontalListView;
import com.tr.ui.widgets.KnowledgeDetailsScrollView;
import com.tr.ui.widgets.KnowledgeDetailsScrollView.OnScrollChangedListener;
import com.utils.common.GlobalVariable;
import com.utils.common.ViewHolder;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.log.ToastUtil;
import com.utils.string.StringUtils;
import com.utils.time.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tr.api.DemandReqUtil;
import com.tr.model.demand.DemandComment;
import com.tr.model.demand.DemandCommentListItem;
import com.tr.model.page.JTPage;
import com.tr.ui.common.view.MyXListView;
import com.tr.ui.common.view.MyXListView.IXListViewListener;
/**
 * @ClassName: NeedDetails.java
 * @author ZCS
 * @Date 2015年3月10日 下午2:10:14
 * @Description: 需求详情
 */
public class NeedDetailsFragment extends JBaseFragment implements
		OnClickListener, IBindData, OnNeedRefresh {
	/**
	 * 属性
	 */
	/**
	 * 项目描述
	 */
	private LinearLayout projectDescribeLl;
	/**
	 * 关联关系(不包括人脉和组织)
	 */
	private LinearLayout relevanceRelationLl;
	private Context cxt;
	// private View replyLl;
	private MyViewPager mPager;
	private OnNeedDetails onNeed;
	private String demandId;// 当前需求id
	private int from;// 来源 需求首页 ，和其他类型
	private int type;// 来源类型 创建需求，投资需求
	private DemandDetailsData detailsData;// 需求详情对象
	// private List<DemandFile> imgList;

	private ImageLoader loader;

	private DisplayImageOptions options;


	DemandDetailsData details;// 需求详情
	// 对象View
	private TextView title;// 需求标题信息
	private View videoRl;

	private View gl_organizationTitle;// 人脉标题
	private View gl_contactsTitle;// 组织标题
	private View organizationLL;// 人脉
	private View contactsLL;// 组织
	private View lableLl;// 标签对象
	// private View finanLl;// 金额信息

	private List<DemandASSOData> renList = new ArrayList<DemandASSOData>();
	private List<DemandASSOData> zuList = new ArrayList<DemandASSOData>();
	private MyListAdapter renAdapter;
	private MyListAdapter zuAdapter;// 组织适配

	private DemandAttFile attFile; // 附件
	private DemandPvFile pvFile; // 图片视频

	private ArrayList<String> pictureList = new ArrayList<String>(); // 图片路径地址
	private MyAdapter pictureListMyAdapter = new MyAdapter();
	private ArrayList<ImageItem> pictureAndVideo = new ArrayList<ImageItem>(); // 图片和视频集合
	private ArrayList<LableData> lableList;
	private DemandASSO asso;

	private ArrayList<UserCategory> category;

	// private MyPopupWindow popupW;
	private View rootView;
	private HorizontalListView horizontalListView1; // 人脉信息
	private HorizontalListView horizontalListView2;// 组织信息
	private CustomView LabelCv;// 标签View
	private MyGridView myGV;
	private TextView financingTv;
	private TextView financingContentTv;
	private TextView introduceTv;
	private LinearLayout incidentRelationLl1; // 知识信息
	private LinearLayout incidentRelationLl2; // 事件信息

	private TextView contactsName;
	private String phone;

	private boolean isUser = false;// 是否是本人
	private LayoutInflater lif;

	// 介绍 关联关系
	/**关联关系*/
	private View demand_assoLL;
	/**需求详情--图片，视频*/
	private View demandIntroduceLL;
	/**项目介绍*/
	private View demandIntroduceLLTitle;
	private LinearLayout NeedDetailsLl;
	public LinearLayout NeedDetailsInformationLl;
	public KnowledgeDetailsScrollView NeedDetailsSv;
	private LinearLayout demandLinkmanTitle;
	private TextView Linkman_Tv;
	private ImageView messageIv;
	private ImageView phoneIv;

	public NeedDetailsFragment(Context cxt, MyViewPager mPager,
			OnNeedDetails onNeed, int from, int type, String demandId) {
		detailsActivity = (NeedDetailsActivity) cxt;

		this.mPager = mPager;
		this.cxt = cxt;
		this.onNeed = onNeed;
		this.from = from;
		this.demandId = demandId;
		this.onNeed.getNeedRefresh(this, 0);
		this.type = type;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.demand_need_details_view_pager,
				container, false);
		lif = inflater;
		
		initView();
		startGetData();
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
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

	private void initView() {

		title = (TextView) rootView.findViewById(R.id.needTitle);// 标题

		// finanLl = rootView.findViewById(R.id.finanLl);// 金额布局

		// financingTv = (TextView) rootView.findViewById(R.id.financingTv);//
		// 金额

		// financingContentTv = (TextView) rootView
		// .findViewById(R.id.financingContentTv);// 金额信息

		introduceTv = (TextView) rootView.findViewById(R.id.introduceTv);// 介绍的文字

		myGV = (MyGridView) rootView.findViewById(R.id.myGridView); // 项目介绍的图片自定义GridView
		myGV.setAdapter(pictureListMyAdapter);
		// 图片预览
		myGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				showImage(pictureList, position);

			}

		});
		
		demandLinkmanTitle = (LinearLayout)rootView.findViewById(R.id.demandLinkmanTitle);
		Linkman_Tv = (TextView)rootView.findViewById(R.id.Linkman_Tv);
		messageIv = (ImageView)rootView.findViewById(R.id.messageIv);
		phoneIv = (ImageView)rootView.findViewById(R.id.phoneIv);
		
		videoRl = rootView.findViewById(R.id.videoRl);// 项目介绍的 视频
		videoRl.setVisibility(View.GONE);
		NeedDetailsLl = (LinearLayout)rootView
				.findViewById(R.id.NeedDetailsLl);
		NeedDetailsInformationLl = (LinearLayout)rootView
				.findViewById(R.id.NeedDetailsInformationLl);
		NeedDetailsSv = (KnowledgeDetailsScrollView)rootView
				.findViewById(R.id.NeedDetailsSv);
		horizontalListView1 = (HorizontalListView) rootView
				.findViewById(R.id.horizontalListView1);// 获取人脉的横向listView

		horizontalListView2 = (HorizontalListView) rootView
				.findViewById(R.id.horizontalListView2);// 获取组织的横向listView

		relevanceRelationLl = (LinearLayout) rootView
				.findViewById(R.id.relevanceRelationLl);// 关联关系(不包括人脉和组织)

		incidentRelationLl1 = (LinearLayout) rootView // 知识
				.findViewById(R.id.relevanceRelationLl);
		incidentRelationLl2 = (LinearLayout) rootView // 事件
				.findViewById(R.id.incidentRelationLl);

		contactsName = (TextView) rootView.findViewById(R.id.contactsName);
		LabelCv = (CustomView) rootView.findViewById(R.id.MyView);// 标签
		lableLl = rootView.findViewById(R.id.lableLl);// 标签
		projectDescribeLl = (LinearLayout) rootView
				.findViewById(R.id.projectDescribeLl);
		rootView.findViewById(R.id.botemDefaultLl).setVisibility(View.VISIBLE);// 消息及电话
		rootView.findViewById(R.id.replyLl).setVisibility(View.GONE);// 回复
		rootView.findViewById(R.id.messageIv).setOnClickListener(this);// 消息
		rootView.findViewById(R.id.phoneIv).setOnClickListener(this);// 电话
		rootView.findViewById(R.id.submitTv).setOnClickListener(this);// 回复按钮
		rootView.findViewById(R.id.contactsLl).setOnClickListener(this);// 人脉
		rootView.findViewById(R.id.organizationLl).setOnClickListener(this);// 组织
		gl_organizationTitle = rootView.findViewById(R.id.gl_organizationTitle);
		gl_contactsTitle = rootView.findViewById(R.id.gl_contactsTitle);
		organizationLL = rootView.findViewById(R.id.gl_organizationLl);
		contactsLL = rootView.findViewById(R.id.gl_contactsLl);
		demand_assoLL = rootView.findViewById(R.id.demand_assoLL);// 关联关系全部信息
		demandIntroduceLL = rootView.findViewById(R.id.demandIntroduceLL);
		demandIntroduceLLTitle = rootView
				.findViewById(R.id.demandIntroduceLLTitle);

		lableLl.setVisibility(View.GONE);// 将当前信息全部关闭不显示
		demandIntroduceLLTitle.setVisibility(View.GONE); // 不显示
		demandIntroduceLL.setVisibility(View.GONE);// 不显示
		
		// 方式
		myGV.setFocusable(false);
		initDate();
		initCommentView();
		
		NeedDetailsSv.setOnScrollChangedListener(new OnScrollChangedListener() {
			

			@Override
			public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
				int commentHeight = NeedDetailsInformationLl.getHeight()-detailsActivity.getWindowManager().getDefaultDisplay().getHeight();
				 if (NeedDetailsSv.getScrollY()>commentHeight) {
			      	   if (!isShowInput ) {
//			      		   ToastUtil.showToast(cxt, TAG+"显示"+commentHeight);
			      		   showBottomBar();
			      		   isShowInput= true;
//			      		 placeholderView.setVisibility(View.VISIBLE);
			      	   	}
			         }else if(NeedDetailsSv.getScrollY()<commentHeight){
			      	   if (isShowInput) {
//			      		   ToastUtil.showToast(cxt, TAG+"隐藏"+commentHeight);
			      		   hideBottomBar();
			      		   isShowInput= false; 
//			      		 placeholderView.setVisibility(View.GONE);
			      	   }
			         }
			}
		});
		
	}
	private void initDate() {
		//TODO
		loader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
		// 设置图片在加载中显示的图片
				.showImageOnLoading(R.drawable.demand_defaultimg)
				// 设置图片Uri为空或是错误的时候显示的图片
				.showImageForEmptyUri(R.drawable.demand_defaultimg)
				// 设置图片加载/解码过程中错误时候显示的图片
				.showImageOnFail(R.drawable.demand_defaultimg)
				// 设置下载的图片是否缓存在内存中
				.cacheInMemory(true)
				// 设置下载的图片是否缓存在SD卡中
				// .cacheOnDisk(true)
				// 保留Exif信息
				.considerExifParams(false)
				// 设置图片以如何的编码方式显示
				.imageScaleType(ImageScaleType.EXACTLY)
//				.displayer(new RoundedBitmapDisplayer(10)) // 设置成圆角图片
				// 设置图片的解码类型
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		renAdapter = new MyListAdapter(renList);// 人脉适配
		zuAdapter = new MyListAdapter(zuList);// 人脉适配
		horizontalListView1.setAdapter(renAdapter);
		horizontalListView2.setAdapter(zuAdapter);
		MyOnTouch myonTouch = new MyOnTouch();
		horizontalListView1.setOnTouchListener(myonTouch);// 滑动事件
		horizontalListView2.setOnTouchListener(myonTouch);// 滑动事件
		horizontalListView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				DemandASSOData assoData = renAdapter.getItem(position);
				if (assoData.type == 2) {
					long assoDataID = TextUtils.isEmpty(assoData.id) ? 0L
							: Long.parseLong(assoData.id);
					ENavigate.startContactsDetailsActivity(getActivity(), 2,
							assoDataID, 0, 1);
					
					/*ENavigate.startContactsDetailsActivity(getActivity(),
							2,Long.parseLong(assoData.id), 0);*/
				}
				if (assoData.type == 3) {
					ENavigate.startRelationHomeActivity(getActivity(),
							assoData.id, true, ENavConsts.type_details_other);
				}
			}
		});
		horizontalListView2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				DemandASSOData org_assoData = zuAdapter.getItem(position);
				if (org_assoData.type == 4) {
					ENavigate.startOrgMyHomePageActivity(getActivity(),
							Long.parseLong(org_assoData.id),Long.parseLong(org_assoData.ownerid),false,2);
				}else if(org_assoData.type == 5){
					ENavigate.startClientDedailsActivity(getActivity(),
							Long.parseLong(org_assoData.id),2,6);
				}
			}
		});
	}

	/**
	 * 区域，类型，行业、金额
	 */
	private void showTypeInfo() {
		projectDescribeLl.setVisibility(View.GONE);
		if (detailsData == null) {
			return;
		}
		if (detailsData.area != null) {
			if (isUser || detailsData.area.isVisable) {
				projectDescribeLl.setVisibility(View.VISIBLE);
				projectDescribeLl.addView(setData("区域:",
						parseData(detailsData.area.list, true), false));
			}
		}
		if (detailsData.type != null) {
			if (isUser || detailsData.type.isVisable) {
				projectDescribeLl.setVisibility(View.VISIBLE);
				projectDescribeLl.addView(setData("类型:",
						parseData(detailsData.type.list, false), false));
			}
		}
		if (detailsData.industry != null) {
			if (isUser || detailsData.industry.isVisable) {
				projectDescribeLl.setVisibility(View.VISIBLE);
				projectDescribeLl.addView(setData("行业:",
						parseData(detailsData.industry.list, false), false));
			}
		}
		// 金额
		if (detailsData.amount != null
				&& !TextUtils.isEmpty(detailsData.amount.unit)) {
			if (isUser || detailsData.amount.isVisable) {
				projectDescribeLl.setVisibility(View.VISIBLE);
				projectDescribeLl.addView(setData(
						"金额:",
						detailsData.amount.unit + "-"
								+ detailsData.amount.getAmountData(), true));
			}
		}
	}

	/**
	 * 显示区域/类型/行业的简介
	 * 
	 * @param title
	 *            标题
	 * @param content
	 *            简介
	 * @return
	 */
	private View setData(String title, String content, boolean isMoney) {
		// #3996FD
		View v = View.inflate(cxt, R.layout.demand_need_details_item, null);
		LinearLayout demandDetailsconContent = (LinearLayout) v.findViewById(R.id.demandDetailsconContent);
		TextView titleTv = (TextView) v.findViewById(R.id.titleTv);

		TextView contentTv = (TextView) v.findViewById(R.id.contentTv);
		titleTv.setText(title);
		if (isMoney) {
			contentTv.setTextColor(getResources().getColor(
					R.color.need_etails_document_txt));
		} else {
			contentTv.setTextColor(getResources().getColor(
					R.color.need_etails_label));
		}
		if (!content.isEmpty()) {
			contentTv.setText(content);
		}else {
			demandDetailsconContent.setVisibility(View.GONE);
		}
		
		return v;
	}

	/**
	 * 添加一条关联关系(不包括人脉和组织)
	 */
	private void addRelevanceRelation(View v) {
		relevanceRelationLl.addView(v);// 添加一个
	}

	/**
	 * 适配 人脉信息和 组织信息
	 * 
	 * @author Administrator
	 * 
	 */
	class MyListAdapter extends BaseAdapter {
		private List<DemandASSOData> assoList;

		public MyListAdapter(List<DemandASSOData> assoList) {
			this.assoList = assoList;
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
			if (convertView == null) {
				convertView = View.inflate(getActivity(),
						R.layout.demand_need_details_hori_item, null);
			}
			DemandASSOData assoData = getItem(position);
			TextView nameTv = ViewHolder.get(convertView, R.id.nameTv);
			TextView describeTv = ViewHolder.get(convertView, R.id.describeTv);
			ImageView headImageIv = ViewHolder.get(convertView, R.id.headImgIv);
			nameTv.setText(assoData.name); // 姓名
			describeTv.setText(assoData.tag); // 关系
		//	headImageIv.setImageResource(R.drawable.demand_defaultimg);
//			if(TextUtils.isEmpty(assoData.picPath)||assoData.picPath.endsWith(GlobalVariable.PERSON_DEFAULT_AVATAR)){
//				String last_char = "";
//				String org_name = assoData.name;
//				if(!TextUtils.isEmpty(org_name)){
//					last_char = org_name.substring(org_name.length()-1);
//				}
//				Bitmap bm = com.utils.common.Util.createBGBItmap(cxt, R.drawable.ic_group_default_avatar, R.color.avatar_text_color, R.dimen.avatar_text_size, last_char);
//				headImageIv.setImageBitmap(bm);
//			}else{
//				ImageLoader.getInstance().displayImage( assoData.picPath,headImageIv);
//			}
			int type = 0;
			if (assoData.type==2||assoData.type==3) {
				type = 1;
			}else if(assoData.type==4||assoData.type==5) {
				type = 2;
			}
			com.utils.common.Util.initAvatarImage(cxt,headImageIv,assoData.name,assoData.picPath,0, type);
			//loader.displayImage(assoData.picPath, headImageIv, options);// 头像
			return convertView;
		}

	}

	/**
	 * 关联关系(不包括人脉和组织)
	 * 
	 * @param headTxt
	 *            标题
	 */
	private void relevanceRelation(String headTxt,
			ArrayList<DemandASSOData> list) {
		View v = (View) View.inflate(cxt,
				R.layout.demand_need_details_relation_item, null);
		TextView headTitleTv = (TextView) v.findViewById(R.id.headTitleTv);
		headTitleTv.setText(headTxt);
		LinearLayout ll = (LinearLayout) v
				.findViewById(R.id.needDetailsLabelInfoItem);
		// "知识"/"事件"的内容的点击事件
		LinearLayout Ll = (LinearLayout) v.findViewById(R.id.Ll);
		for (int i = 0; i < list.size(); i++) {
			DemandASSOData asso = list.get(i);
			if (asso == null)
				break;
			View item = View.inflate(cxt,
					R.layout.demand_need_details_label_info_item, null);
			TextView titleTv = (TextView) item.findViewById(R.id.titleTv);
			TextView contentsTv = (TextView) item.findViewById(R.id.contentsTv);
			// TextView createTimeTv = (TextView) item
			// .findViewById(R.id.createTimeTv);
			// createTimeTv.setText(text);//设置时间
			titleTv.setText(asso.title);
			contentsTv.setText(asso.tag);
			item.setTag(asso);
			item.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					DemandASSOData asso = (DemandASSOData) v.getTag();
					if (asso.type == 1) {
						// 事件
						// ENavigate.startRequirementDetailActivity(getActivity(),
						// asso.id);
						ENavigate.startNeedDetailsActivity(getActivity(),
								asso.id, 1); // 跳转到需求详情界面
					} else if (asso.type == 6) {
						// 知识
						ENavigate.startKnowledgeOfDetailActivity(getActivity(),
								Long.parseLong(asso.id),
								Integer.parseInt(asso.columntype));
					}
				}
			});

			ll.addView(item);// 添加条目（"知识"/"事件"的内容）
		}
		addRelevanceRelation(v);

	}

	/**
	 * 刷新接口，只有编辑后的时候才会调用刷新
	 */
	@Override
	public void getRefresh(String demandId) {
		this.demandId = demandId;
		showLoadingDialog();
		relevanceRelationLl.removeAllViews();// 清除所有显示
		projectDescribeLl.removeAllViews();// 清除 所有的View 信息
		DemandReqUtil.getDemandDetail(cxt, this, null, demandId, 1);
		this.demandId = demandId;
		getRefresh();//刷新
	}

	/**
	 * 
	 * @param imgList
	 *            要显示的集合
	 * @param index
	 *            默认显示的
	 */
	private void showImage(ArrayList<String> imgList, int index) {
		if (imgList != null && imgList.size() <= 0) {
			return;
		}
		ArrayList<JTFile> list = new ArrayList<JTFile>();
		for (String imageItem : imgList) {
			JTFile jtFile = new JTFile();
			jtFile.mLocalFilePath = imageItem;
			jtFile.mType = TextStrUtil.isVideo(imageItem) ? 2 : 1;
			list.add(jtFile);
		}
		Intent intent = new Intent(getActivity(), BrowesPhotoVideo.class);
		intent.putExtra("index", index);
		intent.putExtra(ENavConsts.DEMAND_INTENT_SELECTED_PICTURE, list);
		startActivity(intent);
	}

	private void showVideo(final String url, View v) {
		// iMediaPlayer = new IMediaPlayer(videoLL, null,
		// "http://192.168.101.131/test123.mp4",false);
		// iMediaPlayer = new IMediaPlayer(getActivity(), videoLL, null, url,
		// false); // 调用视频对象控件并显示
		ImageView videoPlayIv = (ImageView) v.findViewById(R.id.videoPlayIv);
		videoPlayIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.e("resultURL", url);
				// 判断当前的网络
				if (!DemandUtil.isVideo(getActivity())) {
					Toast.makeText(getActivity(), "已设置为当前的网络不能播放视频", 0).show();
					return;
				}
				// 调用系统播放器
				Intent video = new Intent(Intent.ACTION_VIEW);
				if (!(new File(url).isFile())) {
					video.setDataAndType(Uri.parse(url), "video/*");
				} else {
					video.setDataAndType(Uri.parse("file://" + url), "video/*");
				}
				startActivity(video);

			}
		});
	}

	private void sendIntroduce() {
		if (detailsData == null) {
			demandIntroduceLL.setVisibility(View.GONE);
			demandIntroduceLLTitle.setVisibility(View.GONE);
			return;
		}
		if (detailsData.note != null && (detailsData.note.isVisable || isUser)) { // 不等于空
			// 并且数据有值
			// 才调用
			demandIntroduceLLTitle.setVisibility(View.VISIBLE);
			demandIntroduceLL.setVisibility(View.VISIBLE);
			introduceTv.setText(detailsData.note.value);// 显示介绍内容
			if (!StringUtils.isEmpty(detailsData.note.taskId)) {
				DemandReqUtil.getDemandFile(cxt, this, detailsData.note.taskId,
						null);
			}
		} else {
			demandIntroduceLL.setVisibility(View.GONE);
			demandIntroduceLLTitle.setVisibility(View.GONE);
		}
	}

	private void showCustomer() {
		if (detailsData == null) {
			return;
		}
		// 如果有自定义的那么就添加到行业下面去
		if (detailsData.customList != null) {
			projectDescribeLl.setVisibility(View.VISIBLE);
			for (CustomData customData : detailsData.customList) {
				if (customData.isVisable || isUser) {// 如果显示 就添加进去
					projectDescribeLl.addView(setData(customData.key + ":",
							customData.value, false));
				}
			}
		}
	}

	private void getDemandContact() {
		// contactsName
		boolean isContact = false;
		if (detailsData != null) {
			if (detailsData.contact != null) {
				contactsName.setText(detailsData.contact.value);
				Linkman_Tv.setText(detailsData.contact.value);
				if (!TextUtils.isEmpty(detailsData.contact.value)) {
					isContact = true;
					
				}
			}

			// 获取联系人的号码

			if (detailsData.phone != null) {
				phone = detailsData.phone.value;
				if (!TextUtils.isEmpty(phone)) {
					isContact = true;
					
					mobilePhoneList = new ArrayList<MobilePhone>();
					MobilePhone mobilePhone = new MobilePhone();
					mobilePhone.mobile =  phone;
					mobilePhone.name = "手机";
					mobilePhoneList.add(mobilePhone);
					phoneIv.setOnClickListener(mOnClickListener);
					messageIv.setOnClickListener(mOnClickListener);
					
				}
			}
		}
		if (isContact) {
			demandLinkmanTitle.setVisibility(View.VISIBLE);
		}else{
			demandLinkmanTitle.setVisibility(View.GONE);
		}
	}
	OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.messageIv:
				new ConnsCallAndSendSmsDialog(getActivity(),ConnsCallAndSendSmsDialog.TYPE_SEND_SMS,mobilePhoneList, null).show();
				break;
			case R.id.phoneIv:
				new ConnsCallAndSendSmsDialog(getActivity(),ConnsCallAndSendSmsDialog.TYPE_CALL,mobilePhoneList, null).show();
				break;
			}
		}
	};
	/**
	 * 获取页数据
	 */
	public void startGetData() {
		showLoadingDialog();
		DemandReqUtil.getDemandDetail(cxt, this, null, demandId, from);
	}

	/** 界面数据初始化 */
	private void layoutData() {
		if(detailsData == null){
			// 设置标题
			title.setText("");
		}else {
			// 设置标题
			title.setText(detailsData.title != null ? detailsData.title.value:"");
		}
		// 获取区域、类型、行业
		showTypeInfo();
		// 获取自定义的条目
		showCustomer();
		// 获取需求联系人
		getDemandContact();

		// 发送获取介绍信息请求
		sendIntroduce();
		
		
		showLabelInfo();// 标签
		showAsso(asso);// 获取关联信息
	}

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

	private void showAsso(DemandASSO asso) {
		demand_assoLL.setVisibility(View.GONE);
		if (asso != null && asso.value != null) {// 获取到关联信息
			if (asso.isVisable || isUser) {// 如){
				// 人脉信息展示
				if (asso.value.p == null || asso.value.p.size() == 0) {
					gl_contactsTitle.setVisibility(View.GONE);
					contactsLL.setVisibility(View.GONE);
				} else {
					gl_contactsTitle.setVisibility(View.VISIBLE);
					contactsLL.setVisibility(View.VISIBLE);
					demand_assoLL.setVisibility(View.VISIBLE);
					horizontalListView1.setVisibility(View.VISIBLE);
					renList.clear();
					renList.addAll(toDemandASSOData(asso.value.p));
					renAdapter.notifyDataSetChanged();
				}
				// 组织信息
				if (asso.value.o == null || asso.value.o.size() == 0) {
					gl_organizationTitle.setVisibility(View.GONE);
					organizationLL.setVisibility(View.GONE);
				} else {
					gl_organizationTitle.setVisibility(View.VISIBLE);
					organizationLL.setVisibility(View.VISIBLE);
					demand_assoLL.setVisibility(View.VISIBLE);
					horizontalListView2.setVisibility(View.VISIBLE);
					zuList.clear();
					zuList.addAll(toDemandASSOData(asso.value.o));
					zuAdapter.notifyDataSetChanged();
				}

				// 知识信息
				if (asso.value.k != null && asso.value.k.size() > 0) {
					demand_assoLL.setVisibility(View.VISIBLE);
					relevanceRelation("知识", toDemandASSOData(asso.value.k));
				}
				// 事件信息
				if (asso.value.r != null && asso.value.r.size() > 0) {
					demand_assoLL.setVisibility(View.VISIBLE);
					relevanceRelation("事件", toDemandASSOData(asso.value.r));
				}
			}
		
		}
	}

	// 显示图片和视频
	private void showPicVidAccessory() {
		demandIntroduceLLTitle.setVisibility(View.VISIBLE);
		demandIntroduceLL.setVisibility(View.VISIBLE);
		String video = "";
		LinearLayout documentCatalogLl = (LinearLayout) rootView
				.findViewById(R.id.documentCatalogLl);
		documentCatalogLl.removeAllViews();
		// 设置显示附件
		// 添加附件
		if (attFile != null) {
			for (final DemandFile file : attFile.fileList) {
				View v = View.inflate(cxt,
						R.layout.demand_need_details_document_item, null);
				TextView documentTv = (TextView) v
						.findViewById(R.id.documentTv);
				documentTv.setText(file.fileTitle);

				documentTv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						showLoadingDialog();
						// Toast.makeText(getActivity().getApplicationContext(),
						// file.fileTitle, Toast.LENGTH_SHORT).show();
						new DownLoadAndOpen(file.filePath, file.fileTitle)
								.execute();
					}
				});
				documentCatalogLl.addView(v);
			}
		}
		// 设置显示图片和视频
		if (pictureAndVideo != null && pictureAndVideo.size() > 0) {
			pictureList.clear();
			for (ImageItem imageItem : pictureAndVideo) {
				if (!imageItem.isVideo) {
					pictureList.add(imageItem.path);
				} else {
					video = imageItem.path;
				}
			}

			pictureListMyAdapter.notifyDataSetChanged();
			if (!TextUtils.isEmpty(video)) {
				videoRl.setVisibility(View.VISIBLE);// 显示视频对象
				showVideo(video, videoRl);
			} else {
				videoRl.setVisibility(View.GONE);

			}
			/*
			 * if (TextStrUtil.isVideo(video)) { // 有视频对象
			 * videoLL.setVisibility(View.VISIBLE);// 显示视频对象 showVideo(video); }
			 * else { videoLL.setVisibility(View.GONE);// 显示视频对象 }
			 */
		}
	}

	/**
	 * 项目介绍
	 * 
	 * @author Administrator
	 * 
	 */
	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// return imgList == null ? 0 : imgList.size();
			return pictureList == null ? 0 : pictureList.size();
		}

		@Override
		public String getItem(int position) {
			// return imgList.get(position).thumbnailspath;
			return pictureList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (null == convertView) {
				convertView = lif.inflate(R.layout.demand_item_pic, null);
			}
			ImageView imageView = (ImageView) convertView
					.findViewById(R.id.picImage);

			// DemandReqUtil.getFullUrl(imgList.get(position).filePath);
			// String Url = DemandReqUtil.getFullUrl(pictureList.get(position));
			loader.displayImage(getItem(position), imageView, options);

			return convertView;
		}
	}

	public static String parseData(List<DemandData> list, boolean isArea) {
		boolean isOne = false;
		StringBuffer sb = new StringBuffer();
		if (list != null) {
			for (DemandData demandData : list) {
				if (demandData != null) {
					if (isOne) {
						sb.append(",");
					}
					isOne = true;
					if (isArea) {
						sb.append(demandData.lastName());
					} else {
						sb.append(demandData.name);
					}
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 显示标签中的按钮
	 */
	private void showLabelInfo() {
		if (lableList == null || lableList.size() <= 0) {
			lableLl.setVisibility(View.GONE);
		} else {
			lableLl.setVisibility(View.VISIBLE);
			LabelCv.removeAllViews();// 清除所有标签显示对象
			for (LableData lableData : lableList) {
				TextView tv = (TextView) View.inflate(cxt,
						R.layout.demand_need_details_tv, null);
				tv.setText(lableData.tag);
				LabelCv.addView(tv);
			}
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.messageIv:
			Uri smsToUri = Uri.parse("smsto:" + phone);
			Intent smsIntent = new Intent(Intent.ACTION_SENDTO, smsToUri);
			smsIntent.putExtra("sms_body", "");
			startActivity(smsIntent);
			break;

		case R.id.phoneIv:
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
					+ phone));
			startActivity(intent);
			break;
		/*case R.id.submitTv:// 回复按钮
			// Toast.makeText(cxt.getApplicationContext(), "回复",
			// Toast.LENGTH_SHORT).show();
			
			 * botemDefaultLl.setVisibility(View.VISIBLE);
			 * replyLl.setVisibility(View.GONE);
			 
			break;*/
		case R.id.contactsLl:// 人脉
			// ENavigate.startRelatedResourceActivityForResult(getActivity(), 0,
			// "测试", ResourceType.People, null);
			break;
		case R.id.organizationLl:// 组织
			// ENavigate.startRelatedResourceActivityForResult(getActivity(), 0,
			// "测试", ResourceType.Organization, null);
			break;
		case R.id.submitTv:// 回复按钮
				showLoadingDialog("");
				InputMethodManager m = (InputMethodManager) getActivity()
						.getSystemService(Activity.INPUT_METHOD_SERVICE);
				m.hideSoftInputFromWindow(
						demandCommonEt.getApplicationWindowToken(), 0);
				DemandReqUtil.addDemandComment(cxt, CommentBindData, demandId, visableCb
						.isChecked() == true ? 1 : 0, demandCommonEt.getText()
						.toString(), null);
				break;
		}

	}

	class MyOnTouch implements View.OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mPager.isCanScroll = false;
				detailsActivity.layout .isCanSilding = false;
				break;
			case MotionEvent.ACTION_MOVE:
				mPager.isCanScroll = false;
				detailsActivity.layout .isCanSilding = false;
				break;

			case MotionEvent.ACTION_UP:
				mPager.isCanScroll = true;
				detailsActivity.layout .isCanSilding = true;
				break;
			default:
				mPager.isCanScroll = true;
				detailsActivity.layout .isCanSilding = true;
				break;
			}

			return !mPager.isCanScroll;
		}
	}

	private void getIntroduce() {
		if (isAdded()) {
			if (pvFile != null) {
				pictureAndVideo.clear();
				// 获取视频和图片的地址
				for (DemandFile pvFiles : pvFile.getFileList()) {
					pictureAndVideo
							.add(new ImageItem(pvFiles.filePath, TextStrUtil
									.isVideo(pvFiles.fileTitle), null, null));
				}
			}
			showPicVidAccessory();
		}

	}

	@Override
	public void bindData(int tag, Object object) {
		dismissLoadingDialog();
		if (object == null) {
			return;
		}
		if (tag == EAPIConsts.demandReqType.demand_getDemandDetail) { // 获取详情成功
			relevanceRelationLl.removeAllViews();// 清除所有显示
			projectDescribeLl.removeAllViews();// 清除 所有的View 信息
			Map<Integer, Object> map = (Map<Integer, Object>) object;
			if ((Boolean) map.get(1)) { // 成功
				int dule = (Integer) map.get(3); // 权限控制
				String peopleorgtype = (String) map.get(7);
				detailsData = (DemandDetailsData) map.get(2); // 基本需求对象信息
				asso = (DemandASSO) map.get(4);// asso
				if (detailsData.createrId.equals(App.getUserID())) {
					isUser = true;// 本人
				}
				category = (ArrayList<UserCategory>) map.get(5);// 目录
				lableList = (ArrayList<LableData>) map.get(6);// 标签
				onNeed.toNeedDetail(1, dule);
				onNeed.toNeedDetail(3, detailsData);
				onNeed.toNeedDetail(4, asso);
				onNeed.toNeedDetail(5, lableList);
				onNeed.toNeedDetail(6, category);
				onNeed.toNeedDetail(7, peopleorgtype);
			} else {
				String error = (String) map.get(-1);
				if (TextUtils.isEmpty(error)) {
					error = "未知错误";
				}
				showToast(error);// 显示错误信息
			}
			if (isAdded()) {
				layoutData();
			}
		} else if (tag == EAPIConsts.demandReqType.demand_findDemandFile) {
			// 查询需求介绍内容
			IntroduceData data = (IntroduceData) object;
			attFile = data.attFile;// 附件
			pvFile = data.pvFile;// 视频/图片
			// 获取介绍内容,封装到集合里面去
			getIntroduce();
		}

	}

	private DownLoadAndOpen downLoadAndOpen;
	private NeedDetailsActivity detailsActivity;

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
			File uploadFileDir = new File(
					Environment.getExternalStorageDirectory(), "/jt/fileCache");
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
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
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
					Toast.makeText(cxt, "附件下载失败", Toast.LENGTH_SHORT).show();
					return;
				}
				Intent intent = null;
				String temp = fileType.replaceAll(
						"^([\\s\\S]*)([tT][xX][tT])$", "$2");
				if (fileType.replaceAll("^([\\s\\S]*)([tT][xX][tT]) *$", "$2")
						.length() == 3) {
					intent = new Intent("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Uri uri2 = Uri.fromFile(openFile);
					intent.setDataAndType(uri2, "text/plain");
				} else if (fileType.replaceAll("^([\\s\\S]*)([pP][dD][fF]) *$",
						"$2").length() == 3) {
					intent = new Intent("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Uri uri = Uri.fromFile(openFile);
					intent.setDataAndType(uri, "application/vnd.ms-powerpoint");

				} else if (fileType.replaceAll("^([\\s\\S]*)([dD][oO][cC]) *$",
						"$2").length() == 3) {
					intent = new Intent("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Uri uri = Uri.fromFile(openFile);
					intent.setDataAndType(uri, "application/msword");

				} else if (fileType.replaceAll(
						"^([\\s\\S]*)([dD][oO][cD][xX]) *$", "$2").length() == 4) {
					intent = new Intent("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Uri uri = Uri.fromFile(openFile);
					intent.setDataAndType(uri, "application/msword");

				} else if (fileType.replaceAll("^([\\s\\S]*)([xX][lL][sS]) *$",
						"$2").length() == 3) {
					intent = new Intent("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Uri uri = Uri.fromFile(openFile);
					intent.setDataAndType(uri, "application/vnd.ms-excel");

				} else if (fileType.replaceAll(
						"^([\\s\\S]*)([xX][lL][sS][xX]) *$", "$2").length() == 4) {
					intent = new Intent("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Uri uri = Uri.fromFile(openFile);
					intent.setDataAndType(uri, "application/vnd.ms-excel");

				} else if (fileType.replaceAll("^([\\s\\S]*)([pP][pP][tT]) *$",
						"$2").length() == 3) {
					intent = new Intent("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Uri uri = Uri.fromFile(openFile);
					intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
				} else if (fileType.replaceAll("^([\\s\\S]*)([pP][nN][gG]) *$",
						"$2").length() == 3
						|| fileType.replaceAll("^([\\s\\S]*)([jJ][pP][gG]) *$",
								"$2").length() == 3
						|| fileType.replaceAll(
								"^([\\s\\S]*)([jJ][pP][eE][gG]) *$", "$2")
								.length() == 4) {

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

	@Override
	public void onDestroy() {
		if (null != downLoadAndOpen) {
			downLoadAndOpen.cancel(false);
		}
		
		super.onDestroy();
	}



	/**
	 * @ClassName: NeedCommentFragment.java
	 * @author ZCS
	 * @Date 2015年3月10日 下午2:10:14
	 * @Description: 需求评论
	 */
		private View commentrootView;
		private MyXListView infoLv;
		private JTPage jtpage;// 分页加载对象
		
		private MyCommentadapter adapter;
		/**
		 * 评论总数
		 */
		private TextView commentCountTv;
		private List<DemandComment> demandComment = new ArrayList<DemandComment>();
		private EditText demandCommonEt;
		private CheckBox visableCb;// 仅创建者可见
		private Button submitTv;
		private TextView bottomTv;
		
		@Override
		public void setUserVisibleHint(boolean isVisibleToUser) {
			super.setUserVisibleHint(isVisibleToUser);
			if (!isVisibleToUser && getActivity() != null) {
				InputMethodManager imm = (InputMethodManager) getActivity()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm != null) {
					imm.hideSoftInputFromWindow(getActivity().getWindow()
							.getDecorView().getWindowToken(), 0);
				}
			}
		}

		public View initCommentView() {
			
			this.onNeed.getNeedRefresh(this, 1);
			commentrootView = View.inflate(cxt,R.layout.demand_need_comment_view_pager,
					 null);
			NeedDetailsLl.addView(commentrootView);
			commentCountTv = (TextView) commentrootView.findViewById(R.id.commentCountTv);
			infoLv = (MyXListView) commentrootView.findViewById(R.id.infoLv);
				infoLv.KnowledegeShow = true;
				bottomTv =(TextView) commentrootView.findViewById(R.id.bottomTv);
			demandCommonEt = (EditText) rootView.findViewById(R.id.demandCommonEt);
			rootView.findViewById(R.id.phoneIv).setOnClickListener(this);// 电话
			rootView.findViewById(R.id.botemDefaultLl).setVisibility(View.GONE);// 消息及电话
			replyLl = rootView.findViewById(R.id.replyLl);
			submitTv = (Button) rootView.findViewById(R.id.submitTv);
			submitTv.setOnClickListener(this);
			submitTv.setEnabled(false);
			submitTv.setPadding(12, 4, 12, 4);
			visableCb = (CheckBox) rootView.findViewById(R.id.visableCb);
			visableCb.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (visableCb.isChecked()) {
						ToastUtil.showToast(getActivity(), "仅创建者可见");
					}
				}
			});
			initData();
			demandCommonEt.addTextChangedListener(new TextWatcher() {

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
					if (isComment) { // true 的时候能发评论，再判断是否输入了数据
						String keyWordStr = demandCommonEt.getText().toString()
								.trim();
						if (TextUtils.isEmpty(keyWordStr)) {
							submitTv.setEnabled(false);
							submitTv.setBackgroundResource(R.drawable.comment_send);
						} else {
							submitTv.setEnabled(true);
							submitTv.setBackgroundResource(R.drawable.comment_send1);
						}
					}
				}
			});
			return rootView;
		}

		private void initData() {
			adapter = new MyCommentadapter();
			commentCountTv.setText("评论");
			infoLv.showFooterView(false);
			// 设置xlistview可以加载、刷新
			infoLv.setPullLoadEnable(true);
			infoLv.setPullRefreshEnable(true);
			infoLv.setXListViewListener(new IXListViewListener() {
				@Override
				public void onRefresh() {
					DemandReqUtil.getDemandCommentList(cxt,
							CommentBindData, demandId, 0, 20, null);
				}

				@Override
				public void onLoadMore() {
					startCommentGetData();
				}
			});

			infoLv.setAdapter(adapter);
			startCommentGetData();
		}

		/**
		 * 获取页数据
		 */
		public void startCommentGetData() {
			int nowIndex = 0;
			if (jtpage != null) {
				nowIndex = jtpage.getIndex() + 1;
			} else {
				showLoadingDialog("");
			}
			DemandReqUtil.getDemandCommentList(cxt, CommentBindData, demandId, nowIndex, 20,
					null);
		}

		class MyCommentadapter extends BaseAdapter {

			@Override
			public int getCount() {
				return demandComment != null ? demandComment.size() : 0;
			}

			@Override
			public DemandComment getItem(int position) {
				return demandComment.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					convertView = View.inflate(getActivity(),
							R.layout.demand_need_details_discuss, null);
				}
				ImageView headIv = ViewHolder.get(convertView, R.id.headIv);
				TextView nameTv = ViewHolder.get(convertView, R.id.nameTv);
				TextView contentTv = ViewHolder.get(convertView, R.id.contentTv);
				TextView commentTimeTv = ViewHolder.get(convertView,
						R.id.commentTimeTv);
				DemandComment comment = getItem(position);
				commentTimeTv.setText(TimeUtil.TimeFormat(comment.createTime));
				contentTv.setText(comment.content);
				ImageLoader.getInstance().displayImage(
						comment.picPath,
						headIv,
						new DisplayImageOptions.Builder()
								.bitmapConfig(Bitmap.Config.RGB_565)
								.cacheInMemory(true).cacheOnDisc(true)
								/*.displayer(new RoundedBitmapDisplayer(10))*/
								.showImageOnFail(R.drawable.ic_default_avatar)
								.build());
				nameTv.setText(comment.createrName);
				return convertView;
			}
		}


		private boolean isComment = true;// 是否允许发送
		IBindData CommentBindData = new IBindData() {
			
		
		@Override
		public void bindData(int tag, Object object) {
			infoLv.stopLoadMore();
			infoLv.stopRefresh();
			infoLv.showFooterView(false);
			dismissLoadingDialog();
			if (object == null) {
				bottomTv.setVisibility(View.GONE);
//				isComment = false;// 不允许
				return;
			}
			if (tag == EAPIConsts.demandReqType.demand_DemandCommentList) {
				
				Map<Integer, Object> map = (Map<Integer, Object>) object;
//				isComment = (Boolean) map.get(1);
				DemandCommentListItem mGetDynamic = (DemandCommentListItem) map
						.get(2);
				if (mGetDynamic != null) {
					bottomTv.setVisibility(View.VISIBLE);
					jtpage = mGetDynamic.getJtPage();
					if (jtpage == null) {
						return;
					}
					if (jtpage.getIndex() == 0) {
						demandComment.clear();
					}
					if ((jtpage != null) && (jtpage.getLists() != null)) {
						for (int i = 0; i < jtpage.getLists().size(); i++) {
							demandComment.add((DemandComment) jtpage.getLists().get(i));
						}
						commentCountTv.setText(TextStrUtil.getCommentNum("评论",
								jtpage.getTotal()));
						onNeed.toNeedDetail(2, jtpage.getTotal());
						adapter.notifyDataSetChanged();
					}
					// 如果没有下一页就停止下拉刷新效果
					if (jtpage.getIndex() >= jtpage.getTotalPage() - 1) {
						infoLv.setPullLoadEnable(false);
					}
				}else{
					bottomTv.setVisibility(View.GONE);
				}
			} else if (tag == EAPIConsts.demandReqType.demand_addDemandComment) {
				if (object instanceof Boolean) {
					demandCommonEt.setText("");
					getRefresh();
				} else {
					showToast((String) object);
				}
			}
		}
		};
		private View replyLl;
		private boolean mIsGone = true;
		private boolean mIsAnim  = false;
		public boolean isShowInput = false;
		private ArrayList<MobilePhone> mobilePhoneList;
		public void getRefresh() {
			jtpage = null;
			startCommentGetData();
		}

		/**
		 * 显示底部view
		 */
		public void showBottomBar() {

	        if (mIsGone&&!mIsAnim) {
	        	replyLl.setVisibility(View.INVISIBLE);
	            Animation translateAnimation = new TranslateAnimation(0,0,replyLl.getHeight(), 0);
	            translateAnimation.setDuration(300);
	            translateAnimation.setInterpolator(new OvershootInterpolator(0.6f));
	            replyLl.startAnimation(translateAnimation);
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
	                	replyLl.setVisibility(View.VISIBLE);
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
				Animation translateAnimation = new TranslateAnimation(0, 0, 0, replyLl.getHeight());
				translateAnimation.setDuration(300);
				translateAnimation.setInterpolator(new OvershootInterpolator(0.6f));
				replyLl.startAnimation(translateAnimation);
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
						replyLl.setVisibility(View.GONE);
						mIsGone=true;
						mIsAnim=false;
					}
				});
			}
		}

}