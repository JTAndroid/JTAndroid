package com.tr.ui.people.contactsdetails.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.tr.App;
import com.tr.R;
import com.tr.model.demand.ASSOData;
import com.tr.model.demand.ASSORPOK;
import com.tr.model.demand.DemandASSOData;
import com.tr.model.obj.MobilePhone;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.common.JointResourceFragment.ResourceType;
import com.tr.ui.connections.revision20150122.detail.RelationHomeActivity;
import com.tr.ui.connections.viewfrg.BaseViewPagerFragment;
import com.tr.ui.demand.IntroduceActivity;
import com.tr.ui.organization.model.meet.CustomerMeetingDetail;
import com.tr.ui.people.contactsdetails.ContactsDetailsActivity;
import com.tr.ui.people.contactsdetails.View.UserDefineScrollView;
import com.tr.ui.people.contactsdetails.adapter.MyListBaseAdapter.ConnectionsGroupAdapter;
import com.tr.ui.people.contactsdetails.adapter.MyListBaseAdapter.KnowledgeGroupAdapter;
import com.tr.ui.people.contactsdetails.adapter.MyListBaseAdapter.RequirementGroupAdapter;
import com.tr.ui.people.model.PeopleDetails;
import com.tr.ui.people.model.Basic;
import com.tr.ui.people.model.Person;
import com.tr.ui.people.model.PersonName;
import com.tr.ui.people.model.PersonalInformation;
import com.tr.ui.widgets.BasicListView2;
import com.tr.ui.widgets.ConnsCallAndSendSmsDialog;
import com.tr.ui.widgets.HorizontalListView;
import com.tr.ui.widgets.viewpagerheaderscroll.delegate.ScrollViewDelegate;
import com.utils.common.GlobalVariable;
import com.utils.common.ViewHolder;
import com.utils.log.KeelLog;

/**
 * 人脉详情资料
 * 
 * @author User OrgMyHomePageDataFragment
 */
public class PeopleContactsdetailFragment extends BaseViewPagerFragment
		implements OnClickListener {
	/** 适应框架 */
	private ScrollViewDelegate mScrollViewDelegate = new ScrollViewDelegate();
	private ScrollView mScrollView;

	public static final int STATE_ADD = 0;
	public static final int STATE_EDIT = 1;
	public int currentRequestCode = 0;
	public int currentRequestState = STATE_ADD;
	public int currentRequestEditPosition = -1;

	public static final int REQUEST_CODE_KNOWLEDGE_CONTENT_ACTIVITY = 1001;
	public static final int REQUEST_CODE_RELATED_RESOURCE_ACTIVITY = 1002;
	public static final int REQUEST_CODE_GLOBAL_KNOWLEDGE_CATEGORY_ACTIVITY = 1003;
	public static final int REQUEST_CODE_GLOBAL_KNOWLEDGE_TAG_ACTIVITY = 1004;
	public static final int REQUEST_CODE_KNOWLEDGE_PERMISSION_ACTIVITY = 1005;

	public static final int REQUEST_CODE_RELATED_RESOURCE_PEOPLE = 2001;
	public static final int REQUEST_CODE_RELATED_RESOURCE_ORGANIZATION = 2002;
	public static final int REQUEST_CODE_RELATED_RESOURCE_KNOWLEDGE = 2003;
	public static final int REQUEST_CODE_RELATED_RESOURCE_AFFAIR = 2004;

	private TextView nameView, kindView, positionView, phoneView, mailView,
			companyView, areaView, detailAddrView;
	private LinearLayout englishNameContainer, customTagContainer;

	/** 民族 */
	private TextView nationView;
	/** 国籍 */
	private TextView nationalityView;
	/** 籍贯 */
	private TextView nativePlaceView;
	/** 信仰 */
	private TextView faithView;
	/** 身体状况 */
	private TextView healthView;
	/** 爱好 */
	private TextView hobbyView;
	/** 生活习惯 */
	private TextView habitView;
	/** 生日 */
	private TextView birthView;
	/** 配偶 */
	private TextView coupleView;
	/** 地区 */
	private TextView addrView;
	/** 行业 */
	private TextView industryView;
	/** 类型 */
	private TextView typeView;
	/** 附加信息 */
	private TextView otherView;

	private LinearLayout ll_beizhu;
	private ContactsDetailsActivity contactsDetailsActivity;
	private RelationHomeActivity relationHomeActivity;
	private LinearLayout ll_individualstatus;
	private TextView beizhuView;
	/** 分类*/
	private LinearLayout classificationLl;

	// 关联信息展示
	private HorizontalListView horizontalListView1;
	private HorizontalListView horizontalListView2;
	/** 关联关系(不包括人脉和组织)*/
	private LinearLayout relevanceRelationLl; 
	/** 关联关系(知识)*/
	private LinearLayout incidentRelationLl1;
	/** 关联关系(事件)*/
	private LinearLayout incidentRelationLl2;
	private View people_organizationTitle;
	private View people_contactsTitle;
	private View people_organizationLL;
	private View people_contactsLL;
	private View people_assoLL;

	private List<DemandASSOData> renList = new ArrayList<DemandASSOData>();
	private List<DemandASSOData> zuList = new ArrayList<DemandASSOData>();
	private MyListAdapter renAdapter;
	private MyListAdapter zuAdapter;
	private ImageLoader loader;
	private DisplayImageOptions options;
	/** 职位*/
	private LinearLayout position_Ll;
	/** 电话*/
	private LinearLayout phone_Ll;
	/** 邮件*/
	private LinearLayout mail_Ll;
	/** 公司*/
	private LinearLayout company_Ll;
	/** 人脉地区*/
	private LinearLayout area_people_Ll;
	/** 详细地址*/
	private LinearLayout detail_addr_Ll;
	/** 发送短信*/
	private ImageView people_send_sms;
	/** 拨打电话*/
	private ImageView people_call;
	private ArrayList<MobilePhone> mobilePhoneList = new ArrayList<MobilePhone>();
	private TextView english_name;
	/** 姓名*/
	private LinearLayout chinaNameContainer;

	public static PeopleContactsdetailFragment newInstance(int index) {
		PeopleContactsdetailFragment fragment = new PeopleContactsdetailFragment();
		Bundle args = new Bundle();
		args.putInt(BUNDLE_FRAGMENT_INDEX, index);
		fragment.setArguments(args);
		return fragment;
	}

	/**
	 * 添加自定义项
	 */
	private void showCustomItem(List<Basic> customTagList) {
		try {
			int count = customTagList.size();
			if (customTagContainer != null) {
				customTagContainer.removeAllViews();
			}
			for (int i = 0; i < count; i++) {
				View customTagView = LayoutInflater
						.from(getActivity())
						.inflate(
								R.layout.people_contactsdetail_custom_item_viewl,
								null);
				TextView nameLabel = (TextView) customTagView
						.findViewById(R.id.name_label);
				TextView contentView = (TextView) customTagView
						.findViewById(R.id.content);

				Basic customItem = customTagList.get(i);
				if (!TextUtils.isEmpty(customItem.name)) {
					nameLabel.setText(customItem.name);
				}
				if (!TextUtils.isEmpty(customItem.content)) {
					contentView.setText(customItem.content);
					customTagContainer.addView(customTagView);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void refreshView(PeopleDetails peopleDetails) {
		if (peopleDetails != null && peopleDetails.people != null) {

			if (peopleDetails.people.customTagList != null
					&& !peopleDetails.people.customTagList.isEmpty()) {
				showCustomItem(peopleDetails.people.customTagList);
				customTagContainer.setVisibility(View.VISIBLE);
			} else {
				customTagContainer.setVisibility(View.GONE);
			}
			// 姓名
			List<PersonName> personNames = peopleDetails.people.peopleNameList;
			if (personNames != null && !personNames.isEmpty()) {
				// 姓名
				String chineseName = "";
				PersonName chinese = personNames.get(0);
				if (!TextUtils.isEmpty(chinese.lastname)) {
					chineseName += chinese.lastname;
				}
//				if (!TextUtils.isEmpty(chinese.firstname)) {
//					chineseName += chinese.firstname;
//				}
				if (!TextUtils.isEmpty(chineseName)) {
					nameView.setText(chineseName);
					chinaNameContainer.setVisibility(View.VISIBLE);
				} else {
					chinaNameContainer.setVisibility(View.GONE);
				}
//				//英文名,产品又不要了。
//				if (personNames.size() > 1) {
//					PersonName english = personNames.get(1);
//					String englishName = "";
//
//					if (!TextUtils.isEmpty(english.firstname)) {
//						englishName += english.firstname;
//					}
//					englishName += " ";
//					if (!TextUtils.isEmpty(english.lastname)) {
//						englishName += english.lastname;
//					}
//
//					if (!TextUtils.isEmpty(englishName.trim())) {
//						english_name.setText(englishName);
//						englishNameContainer.setVisibility(View.VISIBLE);
//					} else {
//						englishNameContainer.setVisibility(View.GONE);
//					}
//				}
			}
			//备注
			if (!TextUtils.isEmpty(peopleDetails.people.remark)) {
				beizhuView.setText(peopleDetails.people.remark);
				ll_beizhu.setVisibility(View.VISIBLE);
			} else {
				ll_beizhu.setVisibility(View.GONE);
			}

			// mail
			if (!TextUtils.isEmpty(peopleDetails.people.email)) {
				mailView.setText(peopleDetails.people.email);
				mail_Ll.setVisibility(View.VISIBLE);
			} else {
				mail_Ll.setVisibility(View.GONE);
			}
			if ("1".equals(peopleDetails.people.virtual)) {
				// kind
				if (!TextUtils.isEmpty(peopleDetails.people.peopleType)) {
					kindView.setText(peopleDetails.people.peopleType);
					classificationLl.setVisibility(View.VISIBLE);
				} else {
					kindView.setText("其他人物");
					classificationLl.setVisibility(View.VISIBLE);
				}
			} else {
				classificationLl.setVisibility(View.GONE);
			}
			// position
			if (!TextUtils.isEmpty(peopleDetails.people.position)) {
				positionView.setText(peopleDetails.people.position);
				position_Ll.setVisibility(View.VISIBLE);
			} else {
				position_Ll.setVisibility(View.GONE);
			}
			// phone
			if (!TextUtils.isEmpty(peopleDetails.people.telephone)) {
				mobilePhoneList.clear();
				phoneView.setText(peopleDetails.people.telephone);
				MobilePhone mobilePhone = new MobilePhone();
				mobilePhone.mobile = peopleDetails.people.telephone;
				mobilePhone.name = "手机";
				mobilePhoneList.add(mobilePhone);
				OnClickListener mOnClickListener = new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (people_call == v) {
							new ConnsCallAndSendSmsDialog(getActivity(),
									ConnsCallAndSendSmsDialog.TYPE_CALL,
									mobilePhoneList, null).show();
						} else if (people_send_sms == v) {
							new ConnsCallAndSendSmsDialog(getActivity(),
									ConnsCallAndSendSmsDialog.TYPE_SEND_SMS,
									mobilePhoneList, null).show();
						}
					}
				};
				people_send_sms.setOnClickListener(mOnClickListener);
				people_call.setOnClickListener(mOnClickListener);
				phone_Ll.setVisibility(View.VISIBLE);
			} else {
				phone_Ll.setVisibility(View.GONE);
			}

			// 公司
			if (!TextUtils.isEmpty(peopleDetails.people.company)) {
				companyView.setText(peopleDetails.people.company);
				company_Ll.setVisibility(View.VISIBLE);
			} else {
				company_Ll.setVisibility(View.GONE);
			}
			// area
			String area = "";
			if (!TextUtils.isEmpty(peopleDetails.people.locationCountry)) {
				area += peopleDetails.people.locationCountry;
			}
			if (!TextUtils.isEmpty(peopleDetails.people.locationCity)
					&& !peopleDetails.people.locationCity
							.equalsIgnoreCase(peopleDetails.people.locationCountry)) {
				area += peopleDetails.people.locationCity;
			}
			if (!TextUtils.isEmpty(peopleDetails.people.locationCounty)) {
				area += peopleDetails.people.locationCounty;
			}

			if (!TextUtils.isEmpty(area)) {
				areaView.setText(area);
				area_people_Ll.setVisibility(View.VISIBLE);
			} else {
				area_people_Ll.setVisibility(View.GONE);
			}
			// detail
			if (!TextUtils.isEmpty(peopleDetails.people.address)) {
				detailAddrView.setText(peopleDetails.people.address);
				detail_addr_Ll.setVisibility(View.VISIBLE);
			} else {
				detail_addr_Ll.setVisibility(View.GONE);
			}
			showAsso(peopleDetails.asso);// 获取关联信息
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * 滑动的过程中会走这个方法
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				R.layout.people_contactsdetails_fragment_data_view, container,
				false);
		Bundle arguments = getArguments();
		mScrollView = (ScrollView) view
				.findViewById(R.id.contactsDetails_ScrollView);

		ll_beizhu = (LinearLayout) view.findViewById(R.id.ll_beizhu);

		ll_individualstatus = (LinearLayout) view
				.findViewById(R.id.ll_individualstatus);// 人脉详情页的子模块

		// 基本信息
		nameView = (TextView) view.findViewById(R.id.name);
		englishNameContainer = (LinearLayout) view
				.findViewById(R.id.englishNameContainer);
		kindView = (TextView) view.findViewById(R.id.kind);
		positionView = (TextView) view.findViewById(R.id.position);
		phoneView = (TextView) view.findViewById(R.id.phone);
		mailView = (TextView) view.findViewById(R.id.mail);
		english_name = (TextView) view.findViewById(R.id.english_name);
		companyView = (TextView) view.findViewById(R.id.company);
		areaView = (TextView) view.findViewById(R.id.area_people);
		detailAddrView = (TextView) view.findViewById(R.id.detail_addr);
		customTagContainer = (LinearLayout) view
				.findViewById(R.id.custom_item_container);

		people_send_sms = (ImageView) view.findViewById(R.id.people_send_sms);
		people_call = (ImageView) view.findViewById(R.id.people_call);

		position_Ll = (LinearLayout) view.findViewById(R.id.position_Ll);
		phone_Ll = (LinearLayout) view.findViewById(R.id.phone_Ll);
		mail_Ll = (LinearLayout) view.findViewById(R.id.mail_Ll);
		company_Ll = (LinearLayout) view.findViewById(R.id.company_Ll);
		area_people_Ll = (LinearLayout) view.findViewById(R.id.area_people_Ll);
		detail_addr_Ll = (LinearLayout) view.findViewById(R.id.detail_addr_Ll);
		chinaNameContainer = (LinearLayout) view
				.findViewById(R.id.chinaNameContainer);

		// 关联信息
		people_organizationTitle = view
				.findViewById(R.id.people_organizationTitle);
		people_contactsTitle = view.findViewById(R.id.people_contactsTitle);
		people_organizationLL = view.findViewById(R.id.people_organizationLl);
		people_contactsLL = view.findViewById(R.id.people_contactsLl);

		people_assoLL = view.findViewById(R.id.people_assoLL);// 关联关系全部信息

		horizontalListView1 = (HorizontalListView) view
				.findViewById(R.id.people_horizontalListView1);// 获取人脉的横向listView

		horizontalListView2 = (HorizontalListView) view
				.findViewById(R.id.people_horizontalListView2);// 获取组织的横向listView

		relevanceRelationLl = (LinearLayout) view
				.findViewById(R.id.people_relevanceRelationLl);/** 关联关系(不包括人脉和组织)*/

		incidentRelationLl1 = (LinearLayout) view // 知识
				.findViewById(R.id.people_relevanceRelationLl);
		incidentRelationLl2 = (LinearLayout) view // 事件
				.findViewById(R.id.people_incidentRelationLl);

		/** 分类布局 */
		classificationLl = (LinearLayout) view
				.findViewById(R.id.ClassificationLl);

		// 个人情况
		nationView = (TextView) view.findViewById(R.id.nation);
		nationalityView = (TextView) view.findViewById(R.id.nationality);
		nativePlaceView = (TextView) view.findViewById(R.id.native_place);
		faithView = (TextView) view.findViewById(R.id.faith);
		healthView = (TextView) view.findViewById(R.id.health);
		hobbyView = (TextView) view.findViewById(R.id.hobby);
		habitView = (TextView) view.findViewById(R.id.habit);
		birthView = (TextView) view.findViewById(R.id.birth);
		coupleView = (TextView) view.findViewById(R.id.couple);
		addrView = (TextView) view.findViewById(R.id.address);
		industryView = (TextView) view.findViewById(R.id.industry);
		typeView = (TextView) view.findViewById(R.id.type);
		otherView = (TextView) view.findViewById(R.id.other);
		beizhuView = (TextView) view.findViewById(R.id.beizhu);
		initData();
		return view;
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
	/**
	 * 显示关联关系
	 * @param asso
	 */
	private void showAsso(ASSORPOK asso) {
		people_assoLL.setVisibility(View.GONE);
		if (asso != null) {// 获取到关联信息
			// 人脉信息展示
			relevanceRelationLl.removeAllViews();
			if (asso.p == null || asso.p.size() == 0) {
				people_contactsTitle.setVisibility(View.GONE);
				people_contactsLL.setVisibility(View.GONE);
			} else {
				people_contactsTitle.setVisibility(View.VISIBLE);
				people_contactsLL.setVisibility(View.VISIBLE);
				people_assoLL.setVisibility(View.VISIBLE);
				horizontalListView1.setVisibility(View.VISIBLE);
				renList.clear();
				renList.addAll(toDemandASSOData(asso.p));
				renAdapter.notifyDataSetChanged();
			}
			// 组织信息
			if (asso.o == null || asso.o.size() == 0) {
				people_organizationTitle.setVisibility(View.GONE);
				people_organizationLL.setVisibility(View.GONE);
			} else {
				people_organizationTitle.setVisibility(View.VISIBLE);
				people_organizationLL.setVisibility(View.VISIBLE);
				people_assoLL.setVisibility(View.VISIBLE);
				horizontalListView2.setVisibility(View.VISIBLE);
				zuList.clear();
				zuList.addAll(toDemandASSOData(asso.o));
				zuAdapter.notifyDataSetChanged();
			}

			// 知识信息
			if (asso.k != null && asso.k.size() > 0) {
				people_assoLL.setVisibility(View.VISIBLE);
				relevanceRelation("知识", toDemandASSOData(asso.k));
			}
			// 事件信息
			if (asso.r != null && asso.r.size() > 0) {
				people_assoLL.setVisibility(View.VISIBLE);
				relevanceRelation("事件", toDemandASSOData(asso.r));
			}

		}
	}
	/**
	 * 初始化数据
	 */
	private void initData() {
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
				// .considerExifParams(true)
				// 设置图片以如何的编码方式显示
				.imageScaleType(ImageScaleType.EXACTLY)
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
							Long.parseLong(org_assoData.id),
							Long.parseLong(org_assoData.ownerid), false, 2);
				} else if (org_assoData.type == 5) {
					ENavigate.startClientDedailsActivity(getActivity(),
							Long.parseLong(org_assoData.id), 1, 6);
				}
			}
		});
	}
	/**
	 * 人脉/组织关联关系左右滑动时对contactsDetailsActivity滑动的控制
	 * @author John
	 *
	 */
	class MyOnTouch implements View.OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				contactsDetailsActivity.mViewPager.isCanScroll = false;
				contactsDetailsActivity.layout.isCanSilding = false;
				break;
			case MotionEvent.ACTION_MOVE:
				contactsDetailsActivity.mViewPager.isCanScroll = false;
				contactsDetailsActivity.layout.isCanSilding = false;
				break;

			case MotionEvent.ACTION_UP:
				contactsDetailsActivity.mViewPager.isCanScroll = true;
				contactsDetailsActivity.layout.isCanSilding = true;
				break;
			default:
				contactsDetailsActivity.mViewPager.isCanScroll = true;
				contactsDetailsActivity.layout.isCanSilding = true;
				break;
			}

			return !contactsDetailsActivity.mViewPager.isCanScroll;
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
			headImageIv.setImageResource(R.drawable.demand_defaultimg);
//			if (!TextUtils.isEmpty(assoData.picPath)) {
//				loader.displayImage(assoData.picPath, headImageIv, options);// 头像
//			} else {
//				if (assoList.get(position).type == 2
//						|| assoList.get(position).type == 3) {
//					headImageIv.setImageResource(R.drawable.ic_default_avatar);
//				} else if (assoList.get(position).type == 4
//						|| assoList.get(position).type == 5) {
//					headImageIv
//							.setImageResource(R.drawable.default_portrait116);
//				}
//
//			}
//			if(TextUtils.isEmpty(assoData.picPath)||assoData.picPath.endsWith(GlobalVariable.ORG_DEFAULT_AVATAR)){
//				String last_char = "";
//				String org_name = assoData.name;
//				if(!TextUtils.isEmpty(org_name)){
//					last_char = org_name.substring(org_name.length()-1);
//				}
//				Bitmap bm = com.utils.common.Util.createBGBItmap(contactsDetailsActivity, R.drawable.ic_group_default_avatar, R.color.avatar_text_color, R.dimen.avatar_text_size, last_char);
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
			com.utils.common.Util.initAvatarImage(contactsDetailsActivity,headImageIv,assoData.name,assoData.picPath,0, type);
			return convertView;
		}

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {

		super.onViewCreated(view, savedInstanceState);
		if (ContactsDetailsActivity.class.getSimpleName().equals(
				getActivity().getClass().getSimpleName())) {
			contactsDetailsActivity = (ContactsDetailsActivity) getActivity();
			refreshView(contactsDetailsActivity.peopleDetails);
		} else if (RelationHomeActivity.class.getSimpleName().equals(
				getActivity().getClass().getSimpleName())) {
			relationHomeActivity = (RelationHomeActivity) getActivity();
			refreshView(relationHomeActivity.peopleDetails);
		}

	}

	@Override
	public boolean isViewBeingDragged(MotionEvent event) {
		return mScrollViewDelegate.isViewBeingDragged(event, mScrollView);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

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
					if (asso.type == 1 || asso.type == 0) {
						// 融资事件
						ENavigate.startNeedDetailsActivity(getActivity(),
								asso.id, 1); // 跳转到需求详情界面
					} else if (asso.type == 6) {
						// 知识
						ENavigate.startKnowledgeOfDetailActivity(getActivity(),
								Long.parseLong(asso.id), 1
						/* Integer.parseInt(asso.columntype) */);
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
		relevanceRelationLl.addView(v);// 添加一个
	}

}
