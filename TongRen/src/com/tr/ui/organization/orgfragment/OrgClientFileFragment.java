package com.tr.ui.organization.orgfragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.tr.R;
import com.tr.model.demand.ASSOData;
import com.tr.model.demand.ASSORPOK;
import com.tr.model.demand.DemandASSOData;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.connections.viewfrg.BaseViewPagerFragment;
import com.tr.ui.organization.create_clientele.ClientDetailsActivity;
import com.tr.ui.organization.model.Area;
import com.tr.ui.organization.model.NewsBean;
import com.tr.ui.organization.model.NoticeBean;
import com.tr.ui.organization.model.RelatedContacts;
import com.tr.ui.organization.model.RelatedEvent;
import com.tr.ui.organization.model.RelatedKnowledge;
import com.tr.ui.organization.model.RelatedOrganization;
import com.tr.ui.organization.model.param.ClientDetailsParams;
import com.tr.ui.organization.model.param.ClientDetialsIncomParams;
import com.tr.ui.organization.model.param.CustomerClientParams;
import com.tr.ui.organization.model.param.CustomerDataParam;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;
import com.tr.ui.organization.model.profile.CustomerPhone;
import com.tr.ui.people.contactsdetails.ContactsDetailsActivity;
import com.tr.ui.people.cread.utils.Utils;
import com.tr.ui.people.model.Basic;
import com.tr.ui.widgets.BasicListView2;
import com.tr.ui.widgets.HorizontalListView;
import com.tr.ui.widgets.viewpagerheaderscroll.delegate.ScrollViewDelegate;
import com.utils.common.EUtil;
import com.utils.common.GlobalVariable;
import com.utils.common.ViewHolder;

/**
 * 客户详情 资料
 */

public class OrgClientFileFragment extends BaseViewPagerFragment  {

	/** 适应框架 */
	private ScrollViewDelegate mScrollViewDelegate = new ScrollViewDelegate();
	private ScrollView mScrollView;
	private RelativeLayout rlNewnotice;
	private RelativeLayout rlNews;
	private RelativeLayout rlSenior;
	private RelativeLayout rlGudongyanjiu;
	private RelativeLayout rlTonghangjingzhen;
	private RelativeLayout mHangyedongtai;
	private RelativeLayout mCaiwufenxi;
	private RelativeLayout mYanjiubaogao;

	public String[] types = { "金融机构", "一般企业", "政府组织", "中介机构", "专业媒体", "期刊报纸",
			"研究机构", "电视广播", "互联网媒体", "通用类型" };
	public String[] isListings = { "非上市公司", "上市公司" };// 是否上市 是 否
	private RelativeLayout rl_org;
	private String auth;
	private ASSORPOK relatedInformation;

	private LinearLayout ll_contains;
	private LinearLayout ll_stocknum;

//	private RelativeLayout regi_Rl;
//	private RelativeLayout know_Rl;
//	private RelativeLayout organ_Rl;
//	private RelativeLayout contactsName_Rl;
//	private BasicListView2 regi_Lv;
//	private BasicListView2 know_Lv;
//	private BasicListView2 organ_Lv;
//	private BasicListView2 contactsName_lv;
//	private KnowledgeGroupAdapter knowledgeGroupAdapter;
//	private ConnectionsGroupAdapter peopleGroupAdapter;
//	private RequirementGroupAdapter requirementGroupAdapter;
//	private ConnectionsGroupAdapter organizationGroupAdapter;

	private TextView client_name, client_jname, client_type, client_industrys,
			up_msg, zj_number, phone_num, client_emaill, tv_jianjie;

	private ClientDetailsActivity client_DetailsActivity;
	private TextView tv_address;
	private LinearLayout customTagContainer;
	private LinearLayout ll_msg;
	private ClientDetailsParams customer;
	private View organization_organizationTitle;
	private View organization_contactsTitle;
	private View organization_organizationLL;
	private View organization_contactsLL;
	private View organization_assoLL;
	private HorizontalListView organization_horizontalListView1;
	private HorizontalListView organization_horizontalListView2;
	private LinearLayout organization_relevanceRelationLl;
	private LinearLayout organization_incidentRelationLl1;
	private LinearLayout organization_incidentRelationLl2;
	private List<DemandASSOData> renList = new ArrayList<DemandASSOData>();
	private List<DemandASSOData> zuList = new ArrayList<DemandASSOData>();
	private ArrayList<DemandASSOData> kList = new ArrayList<DemandASSOData>();
	private ArrayList<DemandASSOData> aList = new ArrayList<DemandASSOData>();
	private MyListAdapter renAdapter;
	private MyListAdapter zuAdapter;
	private ImageLoader loader;
	private DisplayImageOptions options;
	private LinearLayout clientNameContainerForShort;
	private LinearLayout clientType;
	private LinearLayout clientIndustrys;
	private LinearLayout clientPhone;
	private LinearLayout clientEmail;
	private LinearLayout clientDescribe;
	private LinearLayout clientAddress;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(
				R.layout.org_otherclientdetails_fragment_data_view, container,
				false);
		rlNewnotice = (RelativeLayout) view.findViewById(R.id.newnotice);
		rlNews = (RelativeLayout) view.findViewById(R.id.rl_news);
		rlGudongyanjiu = (RelativeLayout) view.findViewById(R.id.gudongyanjiu);
		rlTonghangjingzhen = (RelativeLayout) view
				.findViewById(R.id.tonghangjingzhen);

		mHangyedongtai = (RelativeLayout) view.findViewById(R.id.hangyedongtai);
		mCaiwufenxi = (RelativeLayout) view.findViewById(R.id.caiwufenxi);
		mYanjiubaogao = (RelativeLayout) view.findViewById(R.id.yanjiubaogao);

		relatedInformation = new ASSORPOK();
		mScrollView = (ScrollView) view
				.findViewById(R.id.clientDetails_ScrollView);
		rlSenior = (RelativeLayout) view.findViewById(R.id.gaocengzhili);
		rl_org = (RelativeLayout) view.findViewById(R.id.rl_org);
		ll_contains = (LinearLayout) view.findViewById(R.id.ll_contains);

		// 客户全称
		client_name = (TextView) view.findViewById(R.id.client_other_name);
		// 客户简称
		client_jname = (TextView) view.findViewById(R.id.client_jname);
		// 客户类型
		client_type = (TextView) view.findViewById(R.id.client_type);
		// 客户行业
		client_industrys = (TextView) view.findViewById(R.id.client_industrys);
		// 上市信息
		up_msg = (TextView) view.findViewById(R.id.up_msg);
		// 证券号
		zj_number = (TextView) view.findViewById(R.id.zj_number);
		// 联系电话
		phone_num = (TextView) view.findViewById(R.id.phone_num);
		// 客户邮箱
		client_emaill = (TextView) view.findViewById(R.id.client_emaill);
		// 客户描述
		tv_jianjie = (TextView) view.findViewById(R.id.tv_jianjie);
		// 客户地址
		tv_address = (TextView) view.findViewById(R.id.tv_address);
		clientNameContainerForShort = (LinearLayout) view.findViewById(R.id.ClientNameContainerForShort);//简称的总布局
		clientType = (LinearLayout) view.findViewById(R.id.ClientType);//类型的总布局
		clientIndustrys = (LinearLayout) view.findViewById(R.id.ClientIndustrys);//行业的总布局
		clientPhone = (LinearLayout) view.findViewById(R.id.ClientPhone);//联系电话总布局
		clientEmail = (LinearLayout) view.findViewById(R.id.ClientEmail);//邮箱总布局
		clientDescribe = (LinearLayout) view.findViewById(R.id.ClientDescribe);//描述总布局
		clientAddress = (LinearLayout) view.findViewById(R.id.ClientAddress);//地址总布局
		
		customTagContainer = (LinearLayout) view
				.findViewById(R.id.custom_item_client_container);
		ll_msg = (LinearLayout) view.findViewById(R.id.ll_msg);//上市信息的总布局
		ll_stocknum = (LinearLayout) view.findViewById(R.id.ll_stocknum);
		
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
				.build();
		/**
		 * 关联4大组键详情
		 **/
		//关联信息
		organization_organizationTitle = view.findViewById(R.id.organization_organizationTitle);
		organization_contactsTitle = view.findViewById(R.id.organization_contactsTitle);
		organization_organizationLL = view.findViewById(R.id.organization_organizationLl);
		organization_contactsLL = view.findViewById(R.id.organization_contactsLl);

		organization_assoLL = view.findViewById(R.id.organization_assoLL);// 关联关系全部信息
				
		organization_horizontalListView1 = (HorizontalListView) view
						.findViewById(R.id.organization_horizontalListView1);// 获取人脉的横向listView
		

		organization_horizontalListView2 = (HorizontalListView) view
						.findViewById(R.id.organization_horizontalListView2);// 获取组织的横向listView


		organization_relevanceRelationLl = (LinearLayout) view // 知识
				.findViewById(R.id.organization_relevanceRelationLl);
		organization_incidentRelationLl1 = (LinearLayout) view // 知识
						.findViewById(R.id.organization_relevanceRelationLl);
		organization_incidentRelationLl2 = (LinearLayout) view // 事件
						.findViewById(R.id.organization_incidentRelationLl);

		renAdapter = new MyListAdapter(renList);// 人脉适配
		zuAdapter = new MyListAdapter(zuList);// 人脉适配
		organization_horizontalListView1.setAdapter(renAdapter);
		organization_horizontalListView2.setAdapter(zuAdapter);
		MyOnTouch myonTouch = new MyOnTouch();
		organization_horizontalListView1.setOnTouchListener(myonTouch);// 滑动事件
		organization_horizontalListView2.setOnTouchListener(myonTouch);// 滑动事件
		organization_horizontalListView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				DemandASSOData assoData = renAdapter.getItem(position);
				if (assoData.type == 2) {
					ENavigate.startContactsDetailsActivity(getActivity(),
							2,Long.parseLong(assoData.id), 0,1);
				}
				if (assoData.type == 3) {
					ENavigate.startRelationHomeActivity(getActivity(),
							assoData.id, true, ENavConsts.type_details_other);
				}
			}
		});
		organization_horizontalListView2.setOnItemClickListener(new OnItemClickListener() {

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
		
		

		return view;
	}
	class MyOnTouch implements View.OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				client_DetailsActivity.mViewPager.isCanScroll = false;
				client_DetailsActivity.layout.isCanSilding = false;
				break;
			case MotionEvent.ACTION_MOVE:
				client_DetailsActivity.mViewPager.isCanScroll = false;
				client_DetailsActivity.layout.isCanSilding = false;
				break;

			case MotionEvent.ACTION_UP:
				client_DetailsActivity.mViewPager.isCanScroll = true;
				client_DetailsActivity.layout.isCanSilding = true;
				break;
			default:
				client_DetailsActivity.mViewPager.isCanScroll = true;
				client_DetailsActivity.layout.isCanSilding = true;
				break;
			}

			return !client_DetailsActivity.mViewPager.isCanScroll;
		}
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
	private void showAsso(ASSORPOK asso) {
		organization_assoLL.setVisibility(View.GONE);
		if (asso != null ) {// 获取到关联信息
			organization_relevanceRelationLl.removeAllViews();
				// 人脉信息展示
				if (asso.p == null || asso.p.size() == 0) {
					organization_contactsTitle.setVisibility(View.GONE);
					organization_contactsLL.setVisibility(View.GONE);
				} else {
					organization_contactsTitle.setVisibility(View.VISIBLE);
					organization_contactsLL.setVisibility(View.VISIBLE);
					organization_assoLL.setVisibility(View.VISIBLE);
					organization_horizontalListView1.setVisibility(View.VISIBLE);
					renList.clear();
					renList.addAll(toDemandASSOData(asso.p));
					renAdapter.notifyDataSetChanged();
				}
				// 组织信息
				if (asso.o == null || asso.o.size() == 0) {
					organization_organizationTitle.setVisibility(View.GONE);
					organization_organizationLL.setVisibility(View.GONE);
				} else {
					organization_organizationTitle.setVisibility(View.VISIBLE);
					organization_organizationLL.setVisibility(View.VISIBLE);
					organization_assoLL.setVisibility(View.VISIBLE);
					organization_horizontalListView2.setVisibility(View.VISIBLE);
					zuList.clear();
					zuList.addAll(toDemandASSOData(asso.o));
					zuAdapter.notifyDataSetChanged();
				}

				// 知识信息
				if (asso.k != null && asso.k.size() > 0) {
					organization_assoLL.setVisibility(View.VISIBLE);
					relevanceRelation("知识",  toDemandASSOData(asso.k));
				}
				// 事件信息
				if (asso.r != null && asso.r.size() > 0) {
					organization_assoLL.setVisibility(View.VISIBLE);
					relevanceRelation("事件", toDemandASSOData(asso.r));
				}
		
		}
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

//			if(assoData.picPath.endsWith(GlobalVariable.ORG_DEFAULT_AVATAR)||TextUtils.isEmpty(assoData.picPath)){
//				String last_char = "";
//				String org_name = assoData.name;
//				if(!TextUtils.isEmpty(org_name)){
//					last_char = org_name.substring(org_name.length()-1);
//				}
//				Bitmap bm = com.utils.common.Util.createBGBItmap(client_DetailsActivity, R.drawable.ic_group_default_avatar, R.color.avatar_text_color, R.dimen.avatar_text_size, last_char);
//				headImageIv.setImageBitmap(bm);
//			}else{
//				ImageLoader.getInstance().displayImage( assoData.picPath,headImageIv,options);
//			}
			int type = 0;
			if (assoData.type==2||assoData.type==3) {
				type = 1;
			}else if(assoData.type==4||assoData.type==5) {
				type = 2;
			}
			com.utils.common.Util.initAvatarImage(client_DetailsActivity,headImageIv,assoData.name,assoData.picPath,0,type);
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
		View v = (View) View.inflate(getActivity(),
				R.layout.demand_need_details_relation_item, null);
		TextView headTitleTv = (TextView) v.findViewById(R.id.headTitleTv);
		headTitleTv.setText(headTxt);
		LinearLayout ll = (LinearLayout) v
				.findViewById(R.id.needDetailsLabelInfoItem);
		ll.removeAllViews();
		// "知识"/"事件"的内容的点击事件
		LinearLayout Ll = (LinearLayout) v.findViewById(R.id.Ll);
		for (int i = 0; i < list.size(); i++) {
			DemandASSOData asso = list.get(i);
			if (asso == null)
				break;
			View item = View.inflate(getActivity(),
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
						// 融资事件
						// ENavigate.startRequirementDetailActivity(getActivity(),
						// asso.id);
						 ENavigate.startNeedDetailsActivity(getActivity(),
									asso.id, 2); // 跳转到需求详情界面
					}else if(asso.type == 0) { //投资事件
//						 ENavigate.startNeedDetailsActivity(getActivity(),
//						 asso.id,2);
						 ENavigate.startNeedDetailsActivity(getActivity(),
									asso.id, 2); // 跳转到需求详情界面
					}else if (asso.type == 6) {
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
	 * 添加一条关联关系(不包括人脉和组织)
	 */
	private void addRelevanceRelation(View v) {
		if(organization_relevanceRelationLl != null){
			organization_relevanceRelationLl.addView(v);// 添加一个
		}
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (ClientDetailsActivity.class.getSimpleName().equals(
				getActivity().getClass().getSimpleName())) {
			client_DetailsActivity = (ClientDetailsActivity) getActivity();
			refreshView(client_DetailsActivity.mjCustomer);
		}
	}

	/**
	 * 判断是我的客户还是他人客户
	 */
	private void initclient() {
		if (auth != null && auth.equals("3")) {
			// inVisible();
		} else if (auth.equals("")) {
			visible();
		}
	}

	private void inVisible() {
		ll_contains.setVisibility(View.INVISIBLE);

	}

	private void visible() {
		ll_contains.setVisibility(View.VISIBLE);

	}

	@Override
	public boolean isViewBeingDragged(MotionEvent event) {

		return mScrollViewDelegate.isViewBeingDragged(event, mScrollView);
	}

	public static OrgClientFileFragment newInstance(int index) {
		OrgClientFileFragment fragment = new OrgClientFileFragment();
		Bundle args = new Bundle();
		args.putInt(BUNDLE_FRAGMENT_INDEX, index);
		fragment.setArguments(args);
		return fragment;
	}


	/**
	 * 添加自定义项
	 */
	private void showCustomItem(List<CustomerPersonalLine> customTagList) {
		try {
			int count = customTagList.size();
			customTagContainer.removeAllViews();
			View customTagView;
			for (int i = 0; i < count; i++) {
				if(LayoutInflater.from(getActivity()) != null ){
					customTagView = LayoutInflater.from(getActivity()).inflate(
							R.layout.people_contactsdetail_custom_item_viewl, null);
					TextView nameLabel = (TextView) customTagView
							.findViewById(R.id.name_label);
					TextView contentView = (TextView) customTagView
							.findViewById(R.id.content);
					CustomerPersonalLine customItem = customTagList.get(i);
					if (!TextUtils.isEmpty(customItem.name)) {
						nameLabel.setText(customItem.name);
					}else {
						nameLabel.setText("自定义");
					}
					if (!TextUtils.isEmpty(customItem.content)) {
						contentView.setText(customItem.content);
						customTagContainer.addView(customTagView);
					}
			}
		}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * 获取详细地址
	 * @param area_result
	 * @return
	 */
	public String getAreaStr(Area area_result) {
		String area = "";
		if (area_result != null) {
			String province = TextUtils.isEmpty(area_result.province) ? ""
					: area_result.province;
			String city = TextUtils.isEmpty(area_result.city) ? ""
					: area_result.city;
			String county = TextUtils.isEmpty(area_result.county) ? ""
					: area_result.county;
			String address = TextUtils.isEmpty(area_result.address) ? ""
					: area_result.address;

			// 主要解决直辖市地址显示问题
			if (province.trim().equalsIgnoreCase(city.trim())) {
				area = province + county + address;
			} else {
				area = province + city + county + address;
			}

		}
		return area;
	}
	/**
	 * 赋值对应的控件
	 * @param mjCustomer
	 */
	public void refreshView(CustomerClientParams mjCustomer) {
		if (mjCustomer != null) {
			customer = mjCustomer.customer;
			if (customer != null) {
				auth = mjCustomer.customer.auth;
				// 判断是我的客户还是他人客户
				if (auth != null && auth.equals("3")) {
					// inVisible();
				} else if (auth.equals("")) {
					visible();
				}
				// 客户地址
				if (!getAreaStr(client_DetailsActivity.address).isEmpty()) {
					clientAddress.setVisibility(View.VISIBLE);
					tv_address.setText(getAreaStr(client_DetailsActivity.address));
				}else{
					clientAddress.setVisibility(View.GONE);
				}
				
				Log.e("LOG", "详情客户地址：" + client_DetailsActivity.address);

				client_name.setText(customer.name);
				
				//自定义
				if (customer.propertyList != null && customer.propertyList.size() > 0) {
						showCustomItem(customer.propertyList);
				}

				// 简称
				if (!mjCustomer.customer.shotName.isEmpty()) {
					clientNameContainerForShort.setVisibility(View.VISIBLE);
					client_jname.setText(mjCustomer.customer.shotName);
				}else{
					clientNameContainerForShort.setVisibility(View.GONE);
				}
				

				// 客户类型
				if (!TextUtils.isEmpty(customer.type)) {
					clientType.setVisibility(View.VISIBLE);
					Log.e("LOG", "客户类型" + customer.type);
					if (customer.type.equals("1")) {
						Log.e("LOG", "客户类型$$$$$" + customer.type);
						client_type.setText(types[0]);
					} else if (customer.type.equals("2")) {
						client_type.setText(types[1]);
					} else if (customer.type.equals("3")) {
						client_type.setText(types[2]);
						ll_msg.setVisibility(View.GONE);
						ll_stocknum.setVisibility(View.GONE);
					} else if (customer.type.equals("4")) {
						client_type.setText(types[3]);

					} else if (customer.type.equals("5")) {
						client_type.setText(types[4]);
					} else if (customer.type.equals("6")) {
						client_type.setText(types[5]);
					} else if (customer.type.equals("7")) {
						client_type.setText(types[6]);
					} else if (customer.type.equals("8")) {
						client_type.setText(types[7]);
					} else if (customer.type.equals("9")) {
						client_type.setText(types[8]);
					} else if (customer.type.equals("10")) {
						client_type.setText(types[9]);
					}
				} else {
					clientType.setVisibility(View.GONE);
//					client_type.setText(types[9]);
				}

				// 客户行业
				if (!customer.industrys.isEmpty()) {
					clientIndustrys.setVisibility(View.VISIBLE);
					if (customer.industrys.toString().equals("[(null)]")) {
						client_industrys.setText("");
					} else {
						String industry = mjCustomer.customer.industrys
								.toString();
						String industrys = industry.substring(
								industry.indexOf("[") + 1,
								industry.indexOf("]"));
						client_industrys.setText(industrys);

					}
				}else {
					clientIndustrys.setVisibility(View.GONE);
				}

				// 上市信息
				if (!customer.isListing.equals("")) {
					ll_msg.setVisibility(View.VISIBLE);
					switch (Integer.parseInt(customer.isListing)) {
					case 0:
						up_msg.setText(isListings[0]);
						ll_stocknum.setVisibility(View.GONE);
						break;
					case 1:
						up_msg.setText(isListings[1]);
						ll_stocknum.setVisibility(View.VISIBLE);
						zj_number.setText(mjCustomer.customer.stockNum);
						break;
					}

				}else {
					ll_msg.setVisibility(View.GONE);
				}
				if (customer.phoneList != null) {
					clientPhone.setVisibility(View.VISIBLE);
					ArrayList<CustomerPhone> phoneList = (ArrayList<CustomerPhone>) customer.phoneList;
					String phone = "";
					if (phoneList != null && phoneList.size() > 0) {
						System.out.println("联系电话:" + phoneList.size());
						for (int i = 0; i < phoneList.size(); i++) {
							CustomerPhone customerPhone = phoneList.get(i);
							phone = customerPhone.phone;
						}
						if (!phone.isEmpty()) {
							phone_num.setText(phone);
						}else {
							clientPhone.setVisibility(View.GONE);
						}
					}
					
				}
				
				if (!customer.linkEmail.isEmpty()) {
					clientEmail.setVisibility(View.VISIBLE);
					client_emaill.setText(customer.linkEmail);
				}else {
					clientEmail.setVisibility(View.GONE);
				}
				
				if (!customer.discribe.isEmpty()) {
					clientDescribe.setVisibility(View.VISIBLE);
					tv_jianjie.setText(EUtil.filterHtml(customer.discribe));
				}else {
					clientDescribe.setVisibility(View.GONE);
				}
				

				// 关联
				relatedInformation = mjCustomer.relevance;
				if (relatedInformation != null) {
					showAsso(relatedInformation);
				}

			}
		}

	}



}
