package com.tr.ui.home.frg;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.App;
import com.tr.R;
import com.tr.api.CommonReqUtil;
import com.tr.api.ConnectionsReqUtil;
import com.tr.api.PeopleReqUtil;
import com.tr.model.connections.FriendRequest;
import com.tr.model.home.MUserQRUrl;
import com.tr.model.im.ChatDetail;
import com.tr.model.obj.ConnectionsMini;
import com.tr.model.obj.JTFile;
import com.tr.model.obj.UserComment;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.adapter.EducationExperienceAdapter;
import com.tr.ui.adapter.WorkExperienceAdapter;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.connections.revision20150122.detail.RelationHomeActivity;
import com.tr.ui.flow.frg.FrgFlow;
import com.tr.ui.home.FrameWorkUtils;
import com.tr.ui.home.HomePageActivity;
import com.tr.ui.people.cread.EditDescActivity;
import com.tr.ui.people.cread.EditEducationExperienceActivity;
import com.tr.ui.people.cread.EditWorkExperienceActivity;
import com.tr.ui.people.cread.OtherActivity;
import com.tr.ui.people.model.Basic;
import com.tr.ui.people.model.Education;
import com.tr.ui.people.model.PeopleDetails;
import com.tr.ui.people.model.Person;
import com.tr.ui.people.model.PersonalInformation;
import com.tr.ui.people.model.WorkExperience;
import com.tr.ui.people.model.params.PeopleDetialsIncomingParameters;
import com.tr.ui.widgets.AddEvaluationEditDialog;
import com.tr.ui.widgets.AddEvaluationEditDialog.OnDialogFinishListener;
import com.tr.ui.widgets.DoubleTextViewTagLayout;
import com.tr.ui.widgets.GuidePopupWindow;
import com.tr.ui.widgets.KnoTagGroupView;
import com.utils.common.EConsts;
import com.utils.common.EUtil;
import com.utils.common.GlobalVariable;
import com.utils.http.EAPIConsts;
import com.utils.http.EAPIConsts.PeopleRequestType;
import com.utils.http.IBindData;
import com.utils.image.LoadImage;
import com.utils.string.StringUtils;

public class HomePageFrag extends JBaseFragment implements OnClickListener,
		IBindData {
	private Context mContext;
	/** 用户id */
	private String userId;
	/** 用来区分当前人的类型 1:用户 2 人脉 */
	private int type = 1;
	/** 线上：用户or线下：人脉 */
	private boolean isonline;

	private PeopleDetails peopleDetails = new PeopleDetails();
	private Person person;
	private boolean friendIsToPeople = false;
	/** 人脉id */
	private long peopleId;
	/** 编辑标志 **/
	public static final int result_edit = 5;
	public static final int WorkExperience = 6;
	public static final int EducationExperience = 7;
	public static final int Other = 8;
	public static final int Description = 9;

	private ArrayList<WorkExperience> worklist = new ArrayList<WorkExperience>();
	private ArrayList<Education> educationlist = new ArrayList<Education>();
	/** 评价内容列表 */
	private ArrayList<UserComment> userCommentlists;
	private DoubleTextViewTagLayout evaluationNewTag;
	private String evaluationContent;
	private DoubleTextViewTagLayout selectUserCommentLayout;

	@ViewInject(R.id.scroll_view)
	private ScrollView scroll_view;//
	@ViewInject(R.id.text_edit_card_info)
	private TextView text_edit_card_info;// 名片信息编辑

	@ViewInject(R.id.text_name)
	private TextView text_name;// 用户名称

	@ViewInject(R.id.home_page_company_name_tv)
	private TextView home_page_company_name_tv;// 信用等级

	@ViewInject(R.id.circle_avatar)
	private ImageView circle_avatar;// 头像
	@ViewInject(R.id.text_edit_nickname)
	private TextView text_edit_nickname;// 昵称
	@ViewInject(R.id.text_phone_number)
	private TextView text_phone_number;// 手机号
	@ViewInject(R.id.text_uesr_email)
	private TextView text_uesr_email;// 邮箱
	@ViewInject(R.id.text_work_area)
	private TextView text_work_area;// 工作区域

	@ViewInject(R.id.item_to_docking)
	private RelativeLayout item_to_docking;// 与 他对接item（查看他人主页时显示）
	@ViewInject(R.id.item_dynamic)
	private RelativeLayout item_dynamic;// 动态（我的/他的）item
	@ViewInject(R.id.text_dynamic)
	private TextView text_dynamic;// 动态（我的/他的）text
	@ViewInject(R.id.text_dynamic_number)
	private TextView text_dynamic_number;// 动态（我的/他的）数量

	@ViewInject(R.id.text_personal_description_edit)
	private TextView text_personal_description_edit;// 个人描述item
	@ViewInject(R.id.text_personal_description_LL)
	private LinearLayout text_personal_description_LL;
	@ViewInject(R.id.description_ll)
	private LinearLayout description_ll;
	@ViewInject(R.id.description_whole_ll)
	private LinearLayout description_whole_ll;

	@ViewInject(R.id.text_add_personal_description)
	private TextView text_add_personal_description;// 添加个人描述
	@ViewInject(R.id.text_add_personal_description_Tv)
	private TextView text_add_personal_description_Tv;// 个人描述展示

	@ViewInject(R.id.text_personal_description_IV)
	private ImageView text_personal_description_IV;// 添加个人描述图标

	@ViewInject(R.id.item_maxcard)
	private RelativeLayout item_maxcard;// 二维码名片item
	@ViewInject(R.id.item_label)
	private RelativeLayout item_label;// 评价标题

	@ViewInject(R.id.text_label_edit)
	private TextView text_label_edit;// (标签)评价item（编辑）
	@ViewInject(R.id.add_evaluation_Tv)
	private TextView add_evaluation_Tv;// 添加标签
	@ViewInject(R.id.tag_edit_TV)
	private TextView tag_edit_TV;// 标签中列表中的编辑，隐藏
	@ViewInject(R.id.add_new_evaluation_gv)
	private KnoTagGroupView evaluationGv;
	@ViewInject(R.id.moreEvaluationLayout)
	private LinearLayout moreEvaluationLayout;// 标签中列表中的查看评价人

	@ViewInject(R.id.text_edit_wordexp)
	private TextView text_edit_wordexp;// 工作经历item
	@ViewInject(R.id.text_edit_wordexp_LL)
	private LinearLayout text_edit_wordexp_LL;// 工作经历item

	@ViewInject(R.id.list_work_experience)
	private ListView list_work_experience;// 工作经历list

	@ViewInject(R.id.text_edit_educationexp)
	private TextView text_edit_educationexp;// 教育item

	@ViewInject(R.id.text_edit_educationexp_LL)
	private LinearLayout text_edit_educationexp_LL;// 教育item

	@ViewInject(R.id.item_personal_description)
	private RelativeLayout item_personal_description;

	@ViewInject(R.id.item_work_experience)
	private RelativeLayout item_work_experience;// 工作经历

	@ViewInject(R.id.item_education_experience)
	private RelativeLayout item_education_experience;// 教育经历

	@ViewInject(R.id.list_education_experience)
	private ListView list_education_experience;// 教育经历list

	@ViewInject(R.id.other_layout)
	private LinearLayout other_layout;// 其他layout
	@ViewInject(R.id.text_edit_other)
	private TextView text_edit_other;// 其他item
	@ViewInject(R.id.text_birthdate)
	private TextView text_birthdate;// 出生日期
	@ViewInject(R.id.birthdate_layout)
	private LinearLayout birthdate_layout;//出生日期layout
	@ViewInject(R.id.my_native_place)
	private EditText native_place;// 籍贯
	@ViewInject(R.id.native_place_layout)
	private LinearLayout native_place_layout;//籍贯layout
	@ViewInject(R.id.hobbies)
	private EditText hobbies;// 爱好
	@ViewInject(R.id.hobbies_layout)
	private LinearLayout hobbies_layout;//爱好layout
	@ViewInject(R.id.be_skilled_in)
	private EditText be_skilled_in;// 擅长技能
	@ViewInject(R.id.be_skilled_in_layout)
	private LinearLayout be_skilled_in_layout;//擅长技能layout
	
	@ViewInject(R.id.relation_home_addfriend)
	private FrameLayout relation_home_addfriend;// 添加好友
	@ViewInject(R.id.relation_home_sendmessage)
	private FrameLayout relation_home_sendmessage;// 发消息
	@ViewInject(R.id.relation_home_waiting)
	private FrameLayout relation_home_waiting;// 添加好友确认状态
	
	
	private RelativeLayout layoutItemDynamic;
	private WorkExperienceAdapter workExperienceAdapter;
	private EducationExperienceAdapter educationExperienceAdapter;
	private String personName;
	private String roleString;
	private String myQrString;
	private String area;
	private boolean is_self_bool = false;

	/* 我创建的人脉 */
	private static final int MY_CREATE_PEOPLE = 1;// 我创建的人脉

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mContext = getActivity();
		// getmArguments();
		getDate();
	}

	public HomePageFrag() {
		super();
	}

	private HomePageActivity mHomePageActivity;
	private String url; //从编辑名片回调的全路径

	public HomePageFrag(HomePageActivity thisActivity) {
		mHomePageActivity = thisActivity;
	}

	private void getDate() {
		// TODO 网络数据请求
		showLoadingDialog();
		PeopleDetialsIncomingParameters peopleDetialParam = new PeopleDetialsIncomingParameters();
		if (TextUtils.isEmpty(userId)) {
			userId = "0";
		}
		peopleDetialParam.id = Long.valueOf(userId);// ==id260931
		peopleDetialParam.personType = type;// = personType//用户2
		peopleDetialParam.view = 1;
		PeopleReqUtil.doRequestWebAPI(mContext, this, peopleDetialParam, null,
				PeopleRequestType.PEOPLE_REQ_GETPEOPLE);
	}

	public void setData(Bundle data) {
		// Bundle data = getArguments();
		userId = data.getString(EConsts.Key.ID);
		roleString = data.getString(EConsts.Key.HOME_PAGE_ROLE);
		if (StringUtils.isEmpty(userId)) {
			userId = App.getUserID();
		}
		if(userId.equals(App.getUserID())){
			is_self_bool = true;
		}
		type = data.getInt(ENavConsts.EFromActivityType,
				ENavConsts.type_details_other);
		isonline = data.getBoolean(EConsts.Key.isOnline, false); // online
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_home_page, container, false);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initView(view);

		SharedPreferences firstuse_sp;
		SharedPreferences.Editor firstuse_edtior;

		firstuse_sp = getActivity().getSharedPreferences(
				GlobalVariable.SHARED_PREFERENCES_FIRST_USE,
				getActivity().MODE_PRIVATE);
		firstuse_edtior = firstuse_sp.edit();
		if (firstuse_sp.getBoolean(GlobalVariable.KNOWLEDGE_FIRST_USE, true)) {
			GuidePopupWindow guidePop = new GuidePopupWindow(getActivity());
			guidePop.setImage(R.drawable.main_guide_08);
			guidePop.show();
			firstuse_edtior.putBoolean(GlobalVariable.KNOWLEDGE_FIRST_USE,
					false);
			firstuse_edtior.commit();
		}
		if (App.getUserID().equals(userId)) {// 自己
			text_dynamic.setText("我的动态");
		} else {// 他人主页
			text_dynamic.setText("他的动态");
		}
	}

	private void setMyVisibility(View view, int visibility) {
		view.setVisibility(visibility);
	}

	private void setMyClick(View view) {
		view.setOnClickListener(this);
	}
	private void initView(View view) {
		relation_home_waiting.setVisibility(View.GONE);
		relation_home_addfriend.setVisibility(View.GONE);
		relation_home_sendmessage.setVisibility(View.GONE);
		native_place.setFocusable(false);
		hobbies.setFocusable(false);
		be_skilled_in.setFocusable(false);
		tag_edit_TV.setVisibility(View.GONE);
		if (roleString != null
				|| GlobalVariable.HomePageInformation.organization.roleStr
						.equals(roleString)
				|| GlobalVariable.HomePageInformation.Customer.roleStr
						.equals(roleString)) {
			item_dynamic.setVisibility(View.GONE);
		} else {
			item_dynamic.setVisibility(View.VISIBLE);
		}
		editPermissionsUI();
		// 名片信息编辑
		setMyClick(text_edit_card_info);
		// 昵称编辑
		// setMyClick(text_edit_nickname);
		// 与他对接
		setMyClick(item_to_docking);
		// 个人描述编辑
		setMyClick(text_personal_description_edit);
		// 添加个人描述
		setMyClick(text_personal_description_LL);
		// 动态
		setMyClick(item_dynamic);
		// 二维码
		setMyClick(item_maxcard);
		// 评价编辑
		setMyClick(text_label_edit);
		// 添加评价
		setMyClick(add_evaluation_Tv);
		// 查看评价人
		setMyClick(moreEvaluationLayout);
		// 工作经历
		setMyClick(text_edit_wordexp);
		// 添加工作经历
		setMyClick(text_edit_wordexp_LL);
		// 教育经历
		setMyClick(text_edit_educationexp);
		// 教育经历
		setMyClick(text_edit_educationexp_LL);
		// 其他
		setMyClick(text_edit_other);
          
		workExperienceAdapter = new WorkExperienceAdapter(mContext, worklist);
		list_work_experience.setAdapter(workExperienceAdapter);
		educationExperienceAdapter = new EducationExperienceAdapter(mContext,
				educationlist);
		list_education_experience.setAdapter(educationExperienceAdapter);
		if (userCommentlists == null) {
			userCommentlists = new ArrayList<UserComment>();
		}
		if (type == 2) {// 人脉动态
			item_dynamic.setVisibility(View.GONE);
		} else {
			item_dynamic.setVisibility(View.VISIBLE);
		}
		// 请求用户二维码
		CommonReqUtil.doGetUserQRUrl(getActivity(), HomePageFrag.this, userId,
				null);
	}

	
	/**
	 * 编辑权限UI控制
	 */
	private void editPermissionsUI() {
		boolean bool = checkEditPermissions();
		if (bool && type != 2) {// 自己 人脉有对接
			setMyVisibility(item_to_docking, View.GONE);// 对接
		} else {
			setMyVisibility(item_to_docking, View.VISIBLE);// 对接
		}
		// 评价UI
		evaluateUI(bool);
		/* 个人描述 */
		descriptionEditUI(bool);
		/* 工作经历和教育经历具有编辑权限显示UI */
		workExperienceAndEducationShowUI(bool);
		otherUI(bool);
	}

	/**
	 * 名片信息编辑 其他资料
	 * 
	 * @param bool
	 */
	public void otherUI(boolean bool) {
		if (bool) {
			setMyVisibility(text_edit_card_info, View.VISIBLE);// 名片信息编辑
			setMyVisibility(text_edit_other, View.VISIBLE);// 其他
		} else {
			setMyVisibility(text_edit_card_info, View.GONE);// 名片信息编辑
			setMyVisibility(text_edit_other, View.GONE);// 其他
		}
	}

	/**
	 * 个人主页和自己创建的人脉具有编辑权限的显示工作经历和教育经历的UI界面控制 工作经历和教育经历具有编辑权限显示UI
	 */
	private void workExperienceAndEducationShowUI(boolean bool) {
		if (bool) {
			// 工作经历
			if (worklist != null && worklist.size() <= 0) {
				text_edit_wordexp_LL.setVisibility(View.VISIBLE);
				text_edit_wordexp.setVisibility(View.GONE);
			} else {
				text_edit_wordexp_LL.setVisibility(View.GONE);
				text_edit_wordexp.setVisibility(View.VISIBLE);
			}

			// 教育经历
			if (educationlist != null && educationlist.size() <= 0) {
				text_edit_educationexp_LL.setVisibility(View.VISIBLE);
				text_edit_educationexp.setVisibility(View.GONE);
			} else {
				text_edit_educationexp_LL.setVisibility(View.GONE);
				text_edit_educationexp.setVisibility(View.VISIBLE);
			}
		} else {
			setMyVisibility(text_edit_wordexp, View.GONE);// 工作经历
			setMyVisibility(text_edit_wordexp_LL, View.GONE);// 工作经历添加图标和点击事件
			setMyVisibility(text_edit_educationexp, View.GONE);// 教育经历
			setMyVisibility(text_edit_educationexp_LL, View.GONE);// 教育经历
		}
	}

	/**
	 * 编辑个人描述UI
	 */
	private void descriptionEditUI(boolean bool) {
		if (bool) {
			if (person != null && !TextUtils.isEmpty(person.remark)) {
				setMyVisibility(text_personal_description_edit, View.VISIBLE);// 个人描述编辑
				setMyVisibility(text_personal_description_IV, View.GONE);// 添加个人描述图标
				setMyVisibility(text_add_personal_description, View.GONE);// 添加个人描述
			} else {
				setMyVisibility(text_personal_description_edit, View.GONE);// 个人描述编辑
				setMyVisibility(text_personal_description_IV, View.VISIBLE);// 添加个人描述图标
				setMyVisibility(text_add_personal_description, View.VISIBLE);// 添加个人描述
			}
		} else {
			setMyVisibility(text_personal_description_edit, View.GONE);// 个人描述编辑
			setMyVisibility(text_personal_description_IV, View.GONE);// 添加个人描述图标
			setMyVisibility(text_add_personal_description, View.GONE);// 添加个人描述
		}
	}

	/**
	 * 评价整体UI
	 */
	private void evaluateUI(boolean bool) {
	/*	if (type == 2 && mHomePageActivity.is_good_friend) {// 人脉目前不评价
			evaluationGv.setVisibility(View.GONE);// 内容
			moreEvaluationLayout.setVisibility(View.GONE);// 查看评价人
			setMyVisibility(item_label, View.GONE);// 标题
		} else {*/
			evaluationGv.setVisibility(View.VISIBLE);
			moreEvaluationLayout.setVisibility(View.VISIBLE);
			setMyVisibility(item_label, View.VISIBLE);// 标题
//		}
		if (bool) {
			setMyVisibility(text_label_edit, View.VISIBLE);
		} else {
			setMyVisibility(text_label_edit, View.GONE);// 评价编辑
		}
	}

	public void handerRequsetCode(int requestCode, Intent intent) {
		showLoadingDialog();
		switch (requestCode) {
		case result_edit:// 编辑名片信息
			person = (Person) intent.getSerializableExtra("people");
			url = intent.getStringExtra("url");
			if(App.getUserID().equals(userId)){
				App.getUser().setImage(url);
			}
			peopleDetails.people = person;
			updateCardUI();
			dismissLoadingDialog();
			break;
		case WorkExperience:// 编辑工作经历
			person = (Person) intent.getSerializableExtra("people");
			peopleDetails.people = person;
			updateWorkList();
			dismissLoadingDialog();
			break;
		case EducationExperience:// 编辑教育经历
			person = (Person) intent.getSerializableExtra("people");
			peopleDetails.people = person;
			updateEducationList();
			dismissLoadingDialog();
			break;
		case Other:// 编辑其他
			person = (Person) intent.getSerializableExtra("people");
			peopleDetails.people = person;
			updateOtherUI();
			dismissLoadingDialog();
			break;
		case Description:// 编辑
			person = (Person) intent.getSerializableExtra("people");
			peopleDetails.people = person;
			updateDesciption();
			dismissLoadingDialog();
			break;
		case ENavConsts.ActivityReqCode.REQUEST_CODE_FOR_EDIT_EVALUATION://编辑标签
		case ENavConsts.ActivityReqCode.REQUEST_CODE_FOR_MORE_EVALUATION: // 查看评价人
			boolean isChange = intent.getBooleanExtra("isChange", false);
			if (isChange) {
				if (evaluationGv != null && userCommentlists != null) {
					evaluationGv.removeViews(0, userCommentlists.size());
					if (!userCommentlists.isEmpty()) {
						userCommentlists.clear();
					}
				}
//				ConnectionsReqUtil.doFindEvaluate(getActivity(), this,
//						Long.valueOf(userId), false, null);
				ConnectionsReqUtil.doFindEvaluate(getActivity(), this,
						Long.valueOf(userId), null, type);
			}
			dismissLoadingDialog();
			break;
		default:
			break;
		}
		/**
		 * 更新教育\个人描述\工作经历UI
		 */
		editPermissionsUI();
		showOtherHomePageUIControl();

	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.text_edit_card_info:// 名片信息编辑
			// startActivity(new Intent(mContext,
			// EditBusinessCardActivity.class));
			if (person != null) { // 编辑“我”的资料
				ENavigate.startEditBusinessCardActivity(mContext,
						peopleDetails, result_edit,is_self_bool);
				// fromActivitytype：1创建；2编辑；3-编辑"我"
				// ENavigate.startNewConnectionsActivity(mContext, 3,
				// peopleDetails, result_edit);
			}
			break;
		case R.id.text_edit_nickname:// 昵称编辑
			showToast("昵称编辑");
			break;
		case R.id.text_personal_description_edit:// 个人描述编辑
		case R.id.text_personal_description_LL:// 个人描述编辑
			intent = new Intent(mContext, EditDescActivity.class);
			intent.putExtra(ENavConsts.datas, peopleDetails);
			getActivity().startActivityForResult(intent, Description);
			break;
		case R.id.item_to_docking:// 与他对接
			ConnectionsMini connsMini = new ConnectionsMini();
			connsMini.setId(userId);
			connsMini.setType(1);
			connsMini.setName(personName);
			connsMini.setImage(person.portrait);
			// ENavigate.startJointResourceActivity(getActivity(),
			// ResourceType.User, connsMini);
			HomePageActivity activity = (HomePageActivity) getActivity();
			activity.setCurrentPage(1);
			break;
		case R.id.item_dynamic:// 动态
			if (App.getUserID().equals(userId)) {
				ENavigate.startFlowActivity(getActivity(), FrgFlow.FLOW_PERSON,
						0);
			} else {
				ENavigate.startFlowActivity(getActivity(), FrgFlow.FLOW_PERSON,
						Long.valueOf(userId));
			}
			break;
		case R.id.item_maxcard:// 二维码名片
			ENavigate.startQRCodeActivity(mContext, myQrString, personName,
					person.portrait);
			break;
		case R.id.text_label_edit:// 评价
			ENavigate.EditRelationEvaluationTagActivity(getActivity(), userId, type);
			break;
		case R.id.moreEvaluationLayout:// 查看评价人
			if (userCommentlists.size() < 1) {
				Toast.makeText(getActivity(), "没有更多了", 0).show();
				return;
			}
			ENavigate.startRelationMoreEvaluationActivityForResult(
					getActivity(), userId, type);
			break;
		case R.id.add_evaluation_Tv:// 添加评价
			addNewEvaluation();
			break;
		case R.id.text_edit_wordexp:// 工作经历
		case R.id.text_edit_wordexp_LL:// 添加工作经历
			intent = new Intent(mContext, EditWorkExperienceActivity.class);
			intent.putExtra(ENavConsts.datas, peopleDetails);
			getActivity().startActivityForResult(intent, WorkExperience);
			break;
		case R.id.text_edit_educationexp:// 教育经历
		case R.id.text_edit_educationexp_LL:// 教育经历
			intent = new Intent(mContext, EditEducationExperienceActivity.class);
			intent.putExtra(ENavConsts.datas, peopleDetails);
			getActivity().startActivityForResult(intent, EducationExperience);
			// startActivity(new Intent(mContext, EducationActivity.class));
			break;
		case R.id.text_edit_other:// 其他
			intent = new Intent(mContext, OtherActivity.class);
			intent.putExtra(ENavConsts.datas, peopleDetails);
			getActivity().startActivityForResult(intent, Other);
			break;
		case R.id.relation_home_addfriend:// 加好友
			JSONObject jb = ConnectionsReqUtil.getReqNewFriend(String.valueOf(userId), FriendRequest.type_persion);
			ConnectionsReqUtil.doReqNewFriend(mContext, this, jb, null);
			showLoadingDialog();
			break;
		case R.id.relation_home_sendmessage:// 发消息
			ChatDetail chatDetail = new ChatDetail();
			chatDetail.setThatID(String.valueOf(userId));
			chatDetail.setThatImage(person.portrait);
			chatDetail.setThatName(personName);
			ENavigate.startIMActivity(getActivity(), chatDetail);
			break;
		default:
			break;
		}

	}

	/** 添加新评价 */
	private void addNewEvaluation() {
		evaluationNewTag = new DoubleTextViewTagLayout(getActivity(), "", "1", true);
		// 弹出ConnsEditDialog加入自定义
		showEvaluationDialog(evaluationNewTag);
		evaluationTagSetListener(evaluationNewTag);
	}

	/** 弹出添加评价Dialog */
	private void showEvaluationDialog(
			final DoubleTextViewTagLayout evaluationTag) {
		new AddEvaluationEditDialog(getActivity(), evaluationTag, "",
				new OnDialogFinishListener() {

					@Override
					public void onFinish(View view, String content) {
						if (content != null
								&& view instanceof DoubleTextViewTagLayout) {
							evaluationContent = content;
							((DoubleTextViewTagLayout) view)
									.setContent(content);
						}
						if (!((DoubleTextViewTagLayout) view)
								.getContentText().trim().isEmpty()) {// 不为为空
							if (hasEvaluationTag(userCommentlists, content)) {
								Toast.makeText(getActivity(), "评价已存在", 0)
										.show();
							} else if (content.length() > 10) {
								Toast.makeText(getActivity(), "评价不大于20字符", 0)
										.show();
							} else {
								// 请求添加评价
//								ConnectionsReqUtil.doAddEvaluate(getActivity(),
//										HomePageFrag.this,
//										Long.valueOf(userId), content, null);
								ConnectionsReqUtil.doAddEvaluate(getActivity(),
										HomePageFrag.this,
										Long.valueOf(userId), content, null,type);
								// showLoadingDialog();
							}
						} else {
							Toast.makeText(getActivity(), "请输入评价", 0).show();
						}
					}
				}).show();

	}

	private boolean hasEvaluationTag(ArrayList<UserComment> userCommentlists,
			String content) {
		for (int i = 0; i < userCommentlists.size(); i++) {
			if (userCommentlists.get(i).getUserComment().equals(content)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 展示数据
	 * 
	 * @param person
	 */
	private void showData() {
		dismissLoadingDialog();

		updateCardUI();
		updateDesciption();
		updateWorkList();
		updateEducationList();
		/**
		 * 更新教育、描述UI
		 */
		editPermissionsUI();
		showOtherHomePageUIControl();
		updateOtherUI();
	}

	private void updateOtherUI() {
		List<PersonalInformation> personalInformationList = person.personalInformationList;
		String birthdate = "";
		String birthPlace = "";
		String goodAt = "";
		String hobby = "";
		for (int i = 0; personalInformationList != null
				&& i < personalInformationList.size(); i++) {
			List<Basic> keyDate = personalInformationList.get(i).keyDate;
			for (int j = 0; keyDate != null && j < keyDate.size(); j++) {
				if (keyDate.get(j).type.contains("1")) {
					birthdate = keyDate.get(j).content;
				}
			}
			birthPlace = personalInformationList.get(i).getBirthCountry()
					+ personalInformationList.get(i).getBirthCity()
					+ personalInformationList.get(i).getBirthCounty();
			goodAt = personalInformationList.get(i).getGoodAt();
			hobby = personalInformationList.get(i).getHobby();
		}
		if(TextUtils.isEmpty(birthdate)&&TextUtils.isEmpty(birthPlace)&&TextUtils.isEmpty(goodAt)&&TextUtils.isEmpty(hobby)&&!checkEditPermissions()){
			other_layout.setVisibility(View.GONE);
		}else{
			other_layout.setVisibility(View.VISIBLE);
		}
		if (!TextUtils.isEmpty(birthdate))
			text_birthdate.setText(birthdate);// 出生日期
		else{
			if(checkEditPermissions()){
				birthdate_layout.setVisibility(View.VISIBLE);
			}else{
				birthdate_layout.setVisibility(View.GONE);
			}
		}
		if (!TextUtils.isEmpty(birthPlace))
			native_place.setText(birthPlace);// 籍贯
		else{
			if(checkEditPermissions()){
				native_place_layout.setVisibility(View.VISIBLE);
			}else{
				native_place_layout.setVisibility(View.GONE);
			}
		}
		if (!TextUtils.isEmpty(hobby))
			hobbies.setText(hobby);// 爱好
		else{
			if(checkEditPermissions()){
				hobbies_layout.setVisibility(View.VISIBLE);
			}else{
				hobbies_layout.setVisibility(View.GONE);
			}
		}
		if (!TextUtils.isEmpty(goodAt))
			be_skilled_in.setText(goodAt);// 擅长技能
		else{
			if(checkEditPermissions()){
				be_skilled_in_layout.setVisibility(View.VISIBLE);
			}else{
				be_skilled_in_layout.setVisibility(View.GONE);
			}
		}

	}

	/**
	 * 跟新教育经历数据
	 */
	private void updateEducationList() {
		educationlist = (ArrayList<Education>) person.getEducationList();
		educationExperienceAdapter.addEducationExperience(educationlist);
	}

	/**
	 * 更新工作UI
	 */
	private void updateWorkList() {
		worklist = (ArrayList<WorkExperience>) person.getWorkExperienceList();
		workExperienceAdapter.addWorkExperience(worklist);

	}

	/**
	 * 更新描述数据
	 */
	private void updateDesciption() {
		if (!TextUtils.isEmpty(person.remark)) {
			text_add_personal_description_Tv.setVisibility(View.VISIBLE);
			text_add_personal_description_Tv.setText(EUtil.filterHtml(person.remark));
			text_add_personal_description_Tv.setTextColor(getResources()
					.getColor(R.color.projecttextcolor1));
		} else {
			text_add_personal_description_Tv.setVisibility(View.GONE);
		}
	}

	/**
	 * 编辑权限 peopleDetails.fromType 需要网络请求后回归判断
	 * 
	 * @return
	 */
	public boolean checkEditPermissions() {
		boolean bool = false;
		if (App.getUserID().equals(userId)
				|| peopleDetails.fromType == MY_CREATE_PEOPLE) {
			bool = true;
		}
		return bool;
	}

	/**
	 * 其他人UI显示控制
	 */
	private void showOtherHomePageUIControl() {
		if (!checkEditPermissions()) {// 他人主页
			if (TextUtils.isEmpty(person.remark)) {
				description_whole_ll.setVisibility(View.GONE);
			}
			if (worklist != null && worklist.size() <= 0) {// 工作经历
				item_work_experience.setVisibility(View.GONE);// 工作经历
			}
			if (educationlist != null && educationlist.size() <= 0) {
				item_education_experience.setVisibility(View.GONE);// 教育经历
			}
			text_personal_description_LL.setClickable(false);// 个人描述点击事件
			if (!TextUtils.isEmpty(person.remark)) {// 个人描述
				text_add_personal_description_Tv.setText(EUtil.filterHtml(person.remark));// 个人描述
			}else {
				text_add_personal_description_Tv.setText("");
			}
			text_personal_description_edit.setVisibility(View.GONE);
		} else {
			description_whole_ll.setVisibility(View.VISIBLE);
			item_work_experience.setVisibility(View.VISIBLE);// 个人描述点击事件
			item_education_experience.setVisibility(View.VISIBLE);// 教育经历
			if (person.remark != null && TextUtils.isEmpty(person.remark.trim())) {// 个人描述
				text_personal_description_LL.setClickable(true);// 个人描述点击事件
				text_personal_description_LL.setVisibility(View.VISIBLE);// 个人描述布局
				text_add_personal_description_Tv.setVisibility(View.GONE);
			} else {
				text_add_personal_description_Tv.setText(EUtil.filterHtml(person.remark.trim()));
				text_personal_description_LL.setClickable(false);// 个人描述点击事件
				text_personal_description_LL.setVisibility(View.GONE);// 个人描述布局
				text_personal_description_edit.setVisibility(View.VISIBLE);// 有个人描述且具有编辑权限显示
			}
			workExperienceAndEducationShowUI(checkEditPermissions());
		}
	}

	private void updateCardUI() {
		String firstName = "";
		if (person.peopleNameList != null
				&& person.peopleNameList.size() > 0
				&& person.peopleNameList.get(0) != null) {
			personName = person.peopleNameList.get(0).lastname;
			firstName = person.peopleNameList.get(0).firstname;
		}
		// 个人用户
		if (!StringUtils.isEmpty(personName)) {
			text_name.setText(personName);
		}
//		else if (!StringUtils.isEmpty(firstName)) {
//			text_name.setText(firstName);
//		} 
		else {
			text_name.setText("");
		}
		home_page_company_name_tv.setText(person.getCompany()+" "+person.position);// 公司名称
		// text_name.setText(personName);
		if (url!=null&&!TextUtils.isEmpty(url)) {
			ImageLoader.getInstance().displayImage(url, circle_avatar,LoadImage.mDefaultHead);// 设置头像
		}else{
			ImageLoader.getInstance().displayImage(person.portrait, circle_avatar,LoadImage.mDefaultHead);// 设置头像
		}
		// text_name.setText(person.);//设置昵称
		for (int i = 0; i < person.contactInformationList.size(); i++) {
			if (person.contactInformationList.get(i).type.equalsIgnoreCase("1")) {
//				if (person.contactInformationList.get(i).subtype.equalsIgnoreCase("1")) {
					text_phone_number.setText(person.contactInformationList.get(i).content);
//				}
			}
			if (person.contactInformationList.get(i).type.equalsIgnoreCase("4")) {
//				if (person.contactInformationList.get(i).subtype.equalsIgnoreCase("1")) {
					text_uesr_email.setText(person.contactInformationList.get(i).content);
//				}
			}
		}
//		text_phone_number.setText(person.getTelephone());
//		text_uesr_email.setText(person.getEmail());
		if (person.getLocationCountry().equalsIgnoreCase(
				person.getLocationCountry())) {
			area = person.getLocationCountry() + person.getLocationCounty();
		} else {
			area = person.getLocationCountry() + person.getLocationCity()
					+ person.getLocationCounty();
		}

		text_work_area.setText(area);
	}
	public void initBottomView() {
		if (!App.getUserID().equals(userId)) {// 非自己
			// 7-我的好友，8-待验证的好友，9-非好友，10-我自己
			if (peopleDetails.type == 7) {// 我的好友、发消息
				relation_home_waiting.setVisibility(View.GONE);
				relation_home_addfriend.setVisibility(View.GONE);
				relation_home_sendmessage.setVisibility(View.VISIBLE);
				relation_home_sendmessage.setOnClickListener(this);
			} else if (peopleDetails.type == 8) {// 等待验证
				relation_home_addfriend.setVisibility(View.GONE);
				relation_home_sendmessage.setVisibility(View.GONE);
				relation_home_waiting.setVisibility(View.VISIBLE);
			} else if (peopleDetails.type == 9) {// 非好友、加好友
				relation_home_sendmessage.setVisibility(View.GONE);
				relation_home_waiting.setVisibility(View.GONE);
				relation_home_addfriend.setVisibility(View.VISIBLE);
				relation_home_addfriend.setOnClickListener(this);
			}
		}
	}
	@Override
	public void bindData(int tag, Object object) {
		switch (tag) {
		case PeopleRequestType.PEOPLE_REQ_GETPEOPLE:// 获取人脉详情

			if (object != null) {
				peopleDetails = (PeopleDetails) object;
				if (mHomePageActivity != null) {
					mHomePageActivity.peopleDetails = peopleDetails;
					mHomePageActivity.mContactsDetailsPageUtils
							.setPeopleDetails(peopleDetails);
				}
				if (peopleDetails != null) {
					person = peopleDetails.people;
					if(App.getUserID().equals(userId)){
						App.getUser().setImage(person.getPortrait());
					}
					if (peopleDetails.personIdAfterConvert != null
							&& peopleDetails.personIdAfterConvert != 0) {
						friendIsToPeople = true;
						peopleId = peopleDetails.personIdAfterConvert;
					}
					initBottomView();
					showData();
				}

				ConnectionsReqUtil.doFindEvaluate(getActivity(), this,
						Long.valueOf(userId), null, type);
			}
//			ConnectionsReqUtil.doFindEvaluate(getActivity(), this,
//					Long.valueOf(userId), false, null);

			break;
		case EAPIConsts.CommonReqType.getUserQRUrl:
			MUserQRUrl qrcode = (MUserQRUrl) object;
			if (qrcode != null && !TextUtils.isEmpty(qrcode.getUrl())) {
				String qRCodeStr = qrcode.getUrl();
				myQrString = qRCodeStr;
				item_maxcard.setVisibility(View.VISIBLE);
			} else {
				item_maxcard.setVisibility(View.GONE);
			}
			break;
		case EAPIConsts.concReqType.findEvaluate:
			if (object != null) {
				ArrayList arr = (ArrayList) object;
				String str = (String) arr.get(0);
				userCommentlists = (ArrayList<UserComment>) arr.get(1);
				evaluationGv.addTagViews(userCommentlists, listener, null, true, false);
			}
			break;
		case EAPIConsts.concReqType.feedbackEvaluate:
			// dismissLoadingDialog();
			if (object != null) {
				String str = (String) object;
				if (str.equals("true") && selectUserCommentLayout != null) {
					int provateNumber = selectUserCommentLayout.getNumber() + 1;
					selectUserCommentLayout.setNumber(provateNumber + "");
					selectUserCommentLayout.setChecked(true);
					selectUserCommentLayout.changeBackground(true);
					Toast.makeText(getActivity(), "赞同成功", 0).show();
				}
			}
			break;
		case EAPIConsts.concReqType.addEvaluate:
			// dismissLoadingDialog();
			if (object != null) {
				ArrayList arr = (ArrayList) object;
				String str = (String) arr.get(0);
				if (str.equals("true") && evaluationNewTag != null
						&& !TextUtils.isEmpty(evaluationContent)) {
					
					UserComment comment = new UserComment();
					comment.setUserComment(evaluationContent);
					comment.setCount(1);
					comment.setEvaluateStatus(true);
					userCommentlists.add(comment);
					evaluationGv.addTagView(comment, listener, null, true, true);
					// Toast.makeText(getActivity(), "评价成功", 0).show();
				} else {
					// Toast.makeText(getActivity(), "评价失败", 0).show();
				}
			}
			break;
		case EAPIConsts.concReqType.im_addFriend:
			 // 添加好友
			dismissLoadingDialog();
			if (object != null) {
				String sur = (String) object;
				if (sur.equals("true")) {
					// showToast("请求发送成功");
					relation_home_addfriend.setVisibility(View.GONE);
					relation_home_waiting.setVisibility(View.VISIBLE);
				} else {
					showToast("请求发送失败");
				}
			}
		
			break;
		default:
			break;
		}
	}

	/** 为评价标签设置点击事件 */
	private void evaluationTagSetListener(
			final DoubleTextViewTagLayout evaluationTag) {
		evaluationTag.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/** 首先判断用户是否有评价权限 */
				// if (!isEvaluated) {
				// Toast.makeText(getActivity(), "对方设置非好友不能评价赶快去加好友吧",
				// 0).show();
				// } else {
				if (evaluationTag.isChecked()) {// true
					// :此人已经赞同;false:此人未赞同
					 Toast.makeText(getActivity(), "您已赞同该评价", 0).show();
				} else {
					// 赞同该评价接口
					selectUserCommentLayout = evaluationTag;
					ConnectionsReqUtil.doFeedbackEvaluate(mContext,
							HomePageFrag.this, Long.valueOf(userId),
							((UserComment) (evaluationTag.getTag())).getId(), true, null);
					// showLoadingDialog();
				}
				// }
			}
		});
	}
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			DoubleTextViewTagLayout evaluationTag = (DoubleTextViewTagLayout) v;
			if (evaluationTag.isChecked()) {// true
				// :此人已经赞同;false:此人未赞同
				 Toast.makeText(getActivity(), "您已赞同该评价", 0).show();
			} else {
				// 赞同该评价接口
				selectUserCommentLayout = evaluationTag;
				ConnectionsReqUtil.doFeedbackEvaluate(mContext,
						HomePageFrag.this, Long.valueOf(userId),
						((UserComment) (evaluationTag.getTag())).getId(), true, null);
				// showLoadingDialog();
			}
		}
	};

	/**
	 * 分享
	 */
	public void doshare() {
		if (person != null) {
			JTFile jtFile = new JTFile();
			if(type == 1){//用户
				jtFile.mType = JTFile.TYPE_JTCONTACT_ONLINE;
			}else {//人脉
				jtFile.mType = JTFile.TYPE_JTCONTACT_OFFLINE;
			}
			jtFile.mTaskId = String.valueOf(userId);
			// jtFile.mFileName =App.getNick()+"分享了[人脉] ";//bug4653
			// 分享到社交时在社交页面显示的信息 显示规则是：不用分好友人脉，都显示为人脉，不用分组织客户，都显示为客户
			// jtFile.mFileName =personName;
			if(TextUtils.isEmpty(person.peopleNameList.get(0).lastname)){
				jtFile.fileName = person.peopleNameList.get(0).firstname;
			}else {
				jtFile.fileName = person.peopleNameList.get(0).lastname;
			}
			jtFile.mSuffixName = person.company;
			jtFile.mUrl = person.portrait;
			jtFile.reserved1 = person.position;
			if ("0".equals(person.virtual)) {// 后台获取详情根据1、2判断，但给客户端是0、1
				jtFile.virtual = "1";
			} else {
				jtFile.virtual = "2";
			}
			FrameWorkUtils.showSharePopupWindow2(getActivity(), jtFile);
		}
	}
}
