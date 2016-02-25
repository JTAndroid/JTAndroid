package com.tr.ui.communities.frag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.App;
import com.tr.R;
import com.tr.api.CommunityReqUtil;
import com.tr.model.demand.ASSOData;
import com.tr.model.demand.ASSORPOK;
import com.tr.model.demand.DemandASSO;
import com.tr.model.demand.DemandASSOData;
import com.tr.model.demand.LableData;
import com.tr.model.joint.AffairNode;
import com.tr.model.joint.ConnectionNode;
import com.tr.model.joint.KnowledgeNode;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.obj.AffairsMini;
import com.tr.model.obj.Connections;
import com.tr.model.obj.JTFile;
import com.tr.model.obj.MUCDetail;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.common.JointResourceFragment.ResourceType;
import com.tr.ui.communities.home.CommumitiesPermissionActivity;
import com.tr.ui.communities.home.CommunitiesDetailsActivity;
import com.tr.ui.communities.home.CommunityLabelsActivity;
import com.tr.ui.communities.home.MyCommunitiesActivity;
import com.tr.ui.communities.model.Community;
import com.tr.ui.communities.model.CommunityDetailRes;
import com.tr.ui.communities.model.CreateSet;
import com.tr.ui.communities.model.ImMucinfo;
import com.tr.ui.communities.model.Label;
import com.tr.ui.demand.CreateLabelActivity;
import com.tr.ui.demand.util.TextStrUtil;
import com.tr.ui.people.cread.RemarkActivity;
import com.tr.ui.people.cread.view.MyEditTextView;
import com.tr.ui.widgets.BasicListView2;
import com.utils.common.EConsts;
import com.utils.common.GlobalVariable;
import com.utils.common.OrganizationPictureUploader;
import com.utils.common.OrganizationPictureUploader.OnOrganizationPictureUploadListener;
import com.utils.http.EAPIConsts.CommunityReqType;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.log.KeelLog;

public class FragStartCreatCommunity extends JBaseFragment implements
		OnClickListener, OnItemClickListener, IBindData,
		OnOrganizationPictureUploadListener {

	@ViewInject(R.id.circle_avatar_edit)
	private ImageView circle_avatar_edit;// 头像

	@ViewInject(R.id.commumitiesSurnameEtv)
	private MyEditTextView commumitiesSurnameEtv;// 社群名称
	@ViewInject(R.id.commumitiesNumberEtv)
	private MyEditTextView commumitiesNumberEtv;// 社群号
	@ViewInject(R.id.commumitiesIntroduceEtv)
	private MyEditTextView commumitiesIntroduceEtv;// 社群介绍
	@ViewInject(R.id.commumitiesCommonalityEtv)
	private MyEditTextView commumitiesCommonalityEtv;// 社群公告
	@ViewInject(R.id.commumitiesTagEtv)
	private MyEditTextView commumitiesTagEtv;// 社群标签
	@ViewInject(R.id.commumitiesRelevanceEtv)
	private MyEditTextView commumitiesRelevanceEtv;// 社群关联
	@ViewInject(R.id.commumitiesPermissionEtv)
	private MyEditTextView commumitiesPermissionEtv;// 权限设置

	@ViewInject(R.id.people_communities_conn)
	private BasicListView2 people;// //四大组件中的人脉ListView
	@ViewInject(R.id.organization_communities_conn)
	private BasicListView2 organization;// //四大组件中的组织ListView
	@ViewInject(R.id.knowledge_communities_conn)
	private BasicListView2 knowledge;// //四大组件中的知识ListView
	@ViewInject(R.id.requirement_communities_conn)
	private BasicListView2 requirement;// //四大组件中的事务ListView

	@ViewInject(R.id.people_communities_Ll)
	private LinearLayout people_Ll;// /四大组件中的人脉LinearLayout
	@ViewInject(R.id.organization_communities_Ll)
	private LinearLayout organization_Ll;// 四大组件中的组织LinearLayout
	@ViewInject(R.id.knowledge_communities_Ll)
	private LinearLayout knowledge_Ll;// 四大组件中的知识LinearLayout
	@ViewInject(R.id.requirement_communities_Ll)
	private LinearLayout requirement_Ll;// 四大组件中的事务LinearLayout

	@ViewInject(R.id.person_communities_Line)
	private View person_Line;//
	@ViewInject(R.id.organization_communities_Line)
	private View organization_Line;//
	@ViewInject(R.id.knowledge_communities_Line)
	private View knowledge_Line;//
	@ViewInject(R.id.requirement_communities_Line)
	private View requirement_Line;//

	@ViewInject(R.id.commumitiesLabelIC)
	private View commumitiesLabelIC;//

	@ViewInject(R.id.view_et_edit)
	private TextView view_Label_edit;//
	@ViewInject(R.id.view_tv_name)
	private TextView view_Label_name;//

	/**
	 * 判断关联数据是否点击子条目中的类型
	 */
	public int currentRequestCode = 0;
	/**
	 * 判断关联数据是否点击子条目中的人脉
	 */
	public static final int REQUEST_CODE_RELATED_RESOURCE_PEOPLE = 2001;
	/**
	 * 判断关联数据是否点击子条目中的组织
	 */
	public static final int REQUEST_CODE_RELATED_RESOURCE_ORGANIZATION = 2002;
	/**
	 * 判断关联数据是否点击子条目中的知识
	 */
	public static final int REQUEST_CODE_RELATED_RESOURCE_KNOWLEDGE = 2003;
	/**
	 * 判断关联数据是否点击子条目中的事务
	 */
	public static final int REQUEST_CODE_RELATED_RESOURCE_AFFAIR = 2004;
	/** 判断关联是否增加新的条目 */
	public static final int STATE_ADD = 0;
	/** 判断关联是否编辑旧的条目 */
	public static final int STATE_EDIT = 1;
	public int currentRequestState = STATE_ADD;
	private Map connectionNodeMap=new HashMap<String, Object>();
	public int currentRequestEditPosition = -1;
	private ArrayList<ConnectionNode> connectionNodePeopleList = new ArrayList<ConnectionNode>(); // 四大组件中的人脉数据集合
	private ArrayList<ConnectionNode> connectionNodeOrgList = new ArrayList<ConnectionNode>();// 四大组件中的组织数据集合
	private ArrayList<KnowledgeNode> knowledgeNodeList = new ArrayList<KnowledgeNode>();// 四大组件中的知识数据集合
	private ArrayList<AffairNode> affairNodeList = new ArrayList<AffairNode>();// 四大组件中的事务数据集合

	private ConnectionsGroupAdapter peopleGroupAdapter; // 四大组件中的人脉Adapter
	private ConnectionsGroupAdapter organizationGroupAdapter;// 四大组件中的组织Adapter
	private KnowledgeGroupAdapter knowledgeGroupAdapter;// 四大组件中的知识Adapter
	private RequirementGroupAdapter requirementGroupAdapter;// 四大组件中的事务Adapter

	private static String decollatorStr = "";
	public static final int REQUEST_CODE_ADD_ACTIVITY = 1002; // 启动选择相片的回调
	private ArrayList<JTFile> picture; // 选择相片返回的值
	private String avatarUrl = null;// 头像URL
	private String newAvatarUrl = null;// 编辑更新拿到的头像URL
	private String remark;
	private List<Label> lableData = new ArrayList<Label>();// 标签对象数据
	private List<Long> tid = new ArrayList<Long>();// 标签id集合

	private long communityId;// 创建成功返回的社群id；
	private int CREAT_SATATE = 0;// 0 为创建 1为编辑
	private boolean exist = false;// "exist":该成员是否存在该社群中，true存在，false 不存在
	private CommunityDetailRes communityDetailRes = new CommunityDetailRes();// 社群详情实体对象
	/**
	 * 社群的社群实体
	 */
	private ImMucinfo community;
	/**
	 * 关联
	 */
	private ASSORPOK asso;
	/**
	 * 标签id
	 */
	private List<Label> labels;

	private List<ASSOData> r = new ArrayList<ASSOData>(); // 事件
	private List<ASSOData> p = new ArrayList<ASSOData>();// 人
	public List<ASSOData> o = new ArrayList<ASSOData>();// 组织
	public List<ASSOData> k = new ArrayList<ASSOData>();// 知识

	private CreateSet createSet = new CreateSet(Long.parseLong(String
			.valueOf(App.getApp().getUserID())));// 群权限
	private int rcount = 0,kcount = 0,ocount = 0,pcount = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		getBundle();
		super.onCreate(savedInstanceState);
	}

	/**
	 * 获取传过来的数据
	 */
	private void getBundle() {
		// TODO 获取传过来的数据
		communityId = getArguments().getLong(GlobalVariable.COMMUNITY_ID);
		CREAT_SATATE = getArguments().getInt("CREAT_SATATE");
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_createcommunities, null);
		ViewUtils.inject(this, view); // 注入view和事件..
		setTextsizeAndColor();
		initViews();
		initListViewData();
		getData();
		return view;
	}

	private void getData() {
		if (CREAT_SATATE == 1)
			CommunityReqUtil.doGetCommunityDetail(getActivity(), communityId,
					Long.parseLong(App.getApp().getUserID()), this, null);
	}
	private void setTextsizeAndColor() {
		//社群名称
		commumitiesSurnameEtv.getMyEditTextView_Tv().setTextColor(getResources().getColor(R.color.text_flow_content));
		commumitiesSurnameEtv.getMyEditTextView_Tv().setTextSize(16);
		commumitiesSurnameEtv.getEditText().setTextColor(getResources().getColor(R.color.text_search_gray));
		commumitiesSurnameEtv.getEditText().setTextSize(12);
		//社群号
		commumitiesNumberEtv.getMyEditTextView_Tv().setTextColor(getResources().getColor(R.color.text_flow_content));
		commumitiesNumberEtv.getMyEditTextView_Tv().setTextSize(16);
		commumitiesNumberEtv.getEditText().setTextColor(getResources().getColor(R.color.text_search_gray));
		commumitiesNumberEtv.getEditText().setTextSize(12);
		//社群介绍
		commumitiesIntroduceEtv.getMyEditTextView_Tv().setTextColor(getResources().getColor(R.color.text_flow_content));
		commumitiesIntroduceEtv.getMyEditTextView_Tv().setTextSize(16);
		commumitiesIntroduceEtv.getEditText().setTextColor(getResources().getColor(R.color.text_search_gray));
		commumitiesIntroduceEtv.getEditText().setTextSize(12);
		//社群公告
		commumitiesCommonalityEtv.getMyEditTextView_Tv().setTextColor(getResources().getColor(R.color.text_flow_content));
		commumitiesCommonalityEtv.getMyEditTextView_Tv().setTextSize(16);
		commumitiesCommonalityEtv.getEditText().setTextColor(getResources().getColor(R.color.text_search_gray));
		commumitiesCommonalityEtv.getEditText().setTextSize(12);
		//社群标签
		commumitiesTagEtv.getMyEditTextView_Tv().setTextColor(getResources().getColor(R.color.text_flow_content));
		commumitiesTagEtv.getMyEditTextView_Tv().setTextSize(16);
		commumitiesTagEtv.getEditText().setTextColor(getResources().getColor(R.color.text_search_gray));
		commumitiesTagEtv.getEditText().setTextSize(12);
		//社群关联
		commumitiesRelevanceEtv.getMyEditTextView_Tv().setTextColor(getResources().getColor(R.color.text_flow_content));
		commumitiesRelevanceEtv.getMyEditTextView_Tv().setTextSize(16);
		commumitiesRelevanceEtv.getEditText().setTextColor(getResources().getColor(R.color.text_search_gray));
		commumitiesRelevanceEtv.getEditText().setTextSize(12);
		//社群权限设置
		commumitiesPermissionEtv.getMyEditTextView_Tv().setTextColor(getResources().getColor(R.color.text_flow_content));
		commumitiesPermissionEtv.getMyEditTextView_Tv().setTextSize(16);
		commumitiesPermissionEtv.getEditText().setTextColor(getResources().getColor(R.color.text_search_gray));
		commumitiesPermissionEtv.getEditText().setTextSize(12);
		
	}
	private void initViews() {
		if (CREAT_SATATE == 1) {
			commumitiesNumberEtv.setChooseNoIcon(true);// //社群号不可以更改
			commumitiesSurnameEtv.setChoose(true);// 社群名称
			commumitiesSurnameEtv.setOnClickListener(this);

			commumitiesIntroduceEtv.setChoose(true);// 社群介绍
			commumitiesIntroduceEtv.setOnClickListener(this);
			
			commumitiesCommonalityEtv.setChoose(true);// 社群公告
			commumitiesCommonalityEtv.setOnClickListener(this);
		}
		commumitiesSurnameEtv.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});// 社群名称
		commumitiesPermissionEtv.setOnClickListener(this);
		commumitiesRelevanceEtv.setOnClickListener(this);
		commumitiesTagEtv.setOnClickListener(this);
		circle_avatar_edit.setOnClickListener(this);
		// view_Label_edit = (TextView)
		// commumitiesLabelIC.findViewById(R.id.view_et_edit);
		// TextView view_Label_name = (TextView)
		// commumitiesLabelIC.findViewById(R.id.view_tv_name);
		view_Label_name.setText("标签");
		commumitiesLabelIC.setOnClickListener(this);

		commumitiesNumberEtv.getEditText().setKeyListener(
				new DigitsKeyListener() {
					@Override
					public int getInputType() {
						return InputType.TYPE_TEXT_VARIATION_PASSWORD;
					}

					@Override
					protected char[] getAcceptedChars() {
						char[] data = getStringData(
								R.string.login_only_can_input).toCharArray();
						return data;
					}
				});
	}

	public String getStringData(int id) {
		return getResources().getString(id);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.commumitiesTagEtv:// 标签
			intent = new Intent(getActivity(), CommunityLabelsActivity.class);
			intent.putExtra(ENavConsts.DEMAND_LABEL_DATA, (Serializable)lableData);
			if (CREAT_SATATE == 1)
				intent.putExtra(GlobalVariable.COMMUNITY_ID, communityId);
			startActivityForResult(intent, 103);
			break;
		case R.id.commumitiesLabelIC:// 标签
			intent = new Intent(getActivity(), CommunityLabelsActivity.class);
			intent.putExtra(ENavConsts.DEMAND_LABEL_DATA, (Serializable)lableData);
			if (CREAT_SATATE == 1)
				intent.putExtra(GlobalVariable.COMMUNITY_ID, communityId);
			startActivityForResult(intent, 103);
			break;
		case R.id.commumitiesCommonalityEtv:// 社群公告
			intent = new Intent(getActivity(), RemarkActivity.class);
			intent.putExtra("Remark_Activity", commumitiesCommonalityEtv
					.getText().toString());
			intent.putExtra("remark", "社群公告");
			if (CREAT_SATATE == 1)
				intent.putExtra(GlobalVariable.COMMUNITY_ID, communityId);
			startActivityForResult(intent, 1006);
			break;
		case R.id.circle_avatar_edit:// 头像
			ENavigate.startSelectPictureActivityforSingleSelection(
					getActivity(), REQUEST_CODE_ADD_ACTIVITY, picture, true);
			break;
		case R.id.commumitiesRelevanceEtv:// 关联
			currentRequestCode = 0;
			if (CREAT_SATATE == 1)
				ENavigate.startRelatedResourceActivityForResult(getActivity(), 101, null, ResourceType.People, connectionNodeMap, communityId, community.getCreateUserId());
			else
				ENavigate.startRelatedResourceActivityForResult(getActivity(), 101, null, ResourceType.People, null, null);
			break;
		case R.id.commumitiesPermissionEtv:// 权限
			Intent permissionintent = new Intent(getActivity(),
					CommumitiesPermissionActivity.class);
			permissionintent.putExtra("permission", createSet);
			if (CREAT_SATATE == 1)
				permissionintent.putExtra(GlobalVariable.COMMUNITY_ID,
						communityId);
			startActivityForResult(permissionintent, 1007);
			break;
		case R.id.commumitiesSurnameEtv:// 社群名称
			intent = new Intent(getActivity(), RemarkActivity.class);
			intent.putExtra("Remark_Activity", commumitiesSurnameEtv.getText()
					.toString());
			intent.putExtra("remark", "社群名称");
			intent.putExtra(GlobalVariable.COMMUNITY_ID, communityId);
			startActivityForResult(intent, 1008);
			break;
		case R.id.commumitiesIntroduceEtv:// 社群介绍
			intent = new Intent(getActivity(), RemarkActivity.class);
			intent.putExtra("Remark_Activity", commumitiesIntroduceEtv
					.getText().toString());
			intent.putExtra("remark", "社群介绍");
			intent.putExtra(GlobalVariable.COMMUNITY_ID, communityId);
			startActivityForResult(intent, 1009);
			break;
		default:
			break;
		}
	}

	public void handerRequsetCode(int requestCode, Intent data) {
		switch (requestCode) {
		case 1008:// 社群名称
			String name = data.getStringExtra("Remark_Activity");
			if (!TextUtils.isEmpty(name)) {
				commumitiesSurnameEtv.setText(name);
			}
			break;
		case 1009:// 社群介绍
			String introduce = data.getStringExtra("Remark_Activity");
//			if (!TextUtils.isEmpty(introduce)) {
				commumitiesIntroduceEtv.setText(introduce);
//			}
			break;
		case REQUEST_CODE_ADD_ACTIVITY:// 头像
			picture = (ArrayList<JTFile>) data
					.getSerializableExtra(ENavConsts.DEMAND_INTENT_SELECTED_PICTURE);
			if (picture != null && !picture.isEmpty() && picture.size() > 0) {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory
						.decodeFile(picture.get(0).mLocalFilePath, options);

				// Calculate inSampleSize
				options.inSampleSize = calculateInSampleSize(options, 480, 800);

				// Decode bitmap with inSampleSize set
				options.inJustDecodeBounds = false;

				Bitmap bm = BitmapFactory.decodeFile(
						picture.get(0).mLocalFilePath, options);
				circle_avatar_edit.setImageBitmap(bm);
				// 调上传头像接口
				OrganizationPictureUploader uploader = new OrganizationPictureUploader(
						this);
				JTFile jtFile = new JTFile();
				jtFile.setId(String.valueOf(picture.get(0).mCreateTime));
				jtFile.mLocalFilePath = picture.get(0).mLocalFilePath;
				jtFile.mType = 4;
				uploader.startNewUploadTask(jtFile);
			}

			break;
		case 101:// 关联资源
			ConnectionNode connectionNode = null,connectionNodeOrg=null;
			KnowledgeNode knowledgeNode=null;
			AffairNode affairNode=null;
			if (data.hasExtra(EConsts.Key.RELATED_PEOPLE_NODE)) {
				// 数据去重
				connectionNode = (ConnectionNode) data
						.getSerializableExtra(EConsts.Key.RELATED_PEOPLE_NODE);
				if (currentRequestState == STATE_EDIT
						& currentRequestCode == REQUEST_CODE_RELATED_RESOURCE_PEOPLE) {
					connectionNodePeopleList.set(currentRequestEditPosition,
							connectionNode);
				} else {
					connectionNodePeopleList.add(connectionNode);
				}
//				refreshPeople();

			}
			// 相关资源
			if (data.hasExtra(EConsts.Key.RELATED_ORGANIZATION_NODE)) {// 组织
				// 数据去重
				connectionNodeOrg = (ConnectionNode) data
						.getSerializableExtra(EConsts.Key.RELATED_ORGANIZATION_NODE);

				if (currentRequestState == STATE_EDIT
						& currentRequestCode == REQUEST_CODE_RELATED_RESOURCE_ORGANIZATION) {
					connectionNodeOrgList.set(currentRequestEditPosition,
							connectionNodeOrg);
				} else {
					// 加入列表
					connectionNodeOrgList.add(connectionNodeOrg);
				}
//				refreshOrg();
			}

			if (data.hasExtra(EConsts.Key.RELATED_KNOWLEDGE_NODE)) {
				// 数据去重
				knowledgeNode = (KnowledgeNode) data
						.getSerializableExtra(EConsts.Key.RELATED_KNOWLEDGE_NODE);

				if (currentRequestState == STATE_EDIT
						& currentRequestCode == REQUEST_CODE_RELATED_RESOURCE_KNOWLEDGE) {
					knowledgeNodeList.set(currentRequestEditPosition,
							knowledgeNode);
				} else {
					knowledgeNodeList.add(knowledgeNode);
				}
//				refreshKnowledge();
			}

			if (data.hasExtra(EConsts.Key.RELATED_AFFAIR_NODE)) {
				// 数据去重\
				affairNode = (AffairNode) data
						.getSerializableExtra(EConsts.Key.RELATED_AFFAIR_NODE);
				if (currentRequestState == STATE_EDIT
						& currentRequestCode == REQUEST_CODE_RELATED_RESOURCE_AFFAIR) {
					affairNodeList.set(currentRequestEditPosition, affairNode);
				} else {
					affairNodeList.add(affairNode);
				}
//				refreshAffair();
			}
			if(connectionNodeMap!=null)
				connectionNodeMap.clear();
			connectionNodeMap.put("p", connectionNode);
			connectionNodeMap.put("o", connectionNodeOrg);
			connectionNodeMap.put("k", knowledgeNode);
			connectionNodeMap.put("r", affairNode);
			if (connectionNode != null)
				pcount = null != connectionNode.getListConnections() ? connectionNode.getListConnections().size() : 0;
			if (connectionNodeOrg != null)
				ocount = null != connectionNodeOrg.getListConnections() ? connectionNodeOrg.getListConnections().size() : 0;
			if (knowledgeNode != null)
				kcount = null != knowledgeNode.getListKnowledgeMini2() ? knowledgeNode.getListKnowledgeMini2().size() : 0;
			if (affairNode != null)
				rcount = null != affairNode.getListAffairMini() ? affairNode.getListAffairMini().size() : 0;
			commumitiesRelevanceEtv.setText("人脉("+pcount+")"+",组织("+ocount+")"+",知识("+kcount+")"+",事件("+rcount+")");
			break;

		case 103:// 标签
			lableData = (ArrayList<Label>) data
					.getSerializableExtra(ENavConsts.DEMAND_LABEL_DATA);
			showLabels();
			break;

		case 1006:// 群公告
			remark = data.getStringExtra("Remark_Activity");
//			if (!TextUtils.isEmpty(remark)) {
				commumitiesCommonalityEtv.setText(remark);
//			}
			break;
		case 1007:// 群权限
			createSet = (CreateSet) data.getSerializableExtra("permission");
			break;
		default:
			break;
		}
	}

	/**
	 * 展示标签
	 */
	private void showLabels() {
		tid.clear();
		ArrayList<String> tagStringList = new ArrayList<String>();
		if (lableData != null && !lableData.isEmpty()) {
			for (Label userTag : lableData) {
				tagStringList.add(userTag.getName());
				tid.add((long) userTag.getId());
			}

			commumitiesLabelIC.setVisibility(View.VISIBLE);
			view_Label_edit.setText(TextStrUtil.getComunityLableDataSize(9, lableData));
		} else {
			commumitiesLabelIC.setVisibility(View.GONE);
		}
	}

	/**
	 * 刷新事务
	 */
	private void refreshAffair() {
		if (affairNodeList != null && !affairNodeList.isEmpty()) {
			if (affairNodeList.size() == 1) {
				requirement_Line.setVisibility(View.GONE);
			} else {
				requirement_Line.setVisibility(View.VISIBLE);
			}
			requirement_Ll.setVisibility(View.VISIBLE);
			requirementGroupAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 刷新知识
	 */
	private void refreshKnowledge() {
		if (knowledgeNodeList != null && !knowledgeNodeList.isEmpty()) {
			if (knowledgeNodeList.size() == 1) {
				knowledge_Line.setVisibility(View.GONE);
			} else {
				knowledge_Line.setVisibility(View.VISIBLE);
			}
			knowledge_Ll.setVisibility(View.VISIBLE);
			knowledgeGroupAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 刷新组织
	 */
	private void refreshOrg() {
		if (connectionNodeOrgList != null && !connectionNodeOrgList.isEmpty()) {
			if (connectionNodeOrgList.size() == 1) {
				organization_Line.setVisibility(View.GONE);
			} else {
				organization_Line.setVisibility(View.VISIBLE);
			}
			organization_Ll.setVisibility(View.VISIBLE);
			organizationGroupAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 刷新人脉
	 */
	private void refreshPeople() {
		if (connectionNodePeopleList != null
				&& !connectionNodePeopleList.isEmpty()) {
			if (connectionNodePeopleList.size() == 1) {
				person_Line.setVisibility(View.GONE);
			} else {
				person_Line.setVisibility(View.VISIBLE);
			}
			people_Ll.setVisibility(View.VISIBLE);
			peopleGroupAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			handerRequsetCode(requestCode, data);
		}
	}

	/**
	 * 计算裁剪尺寸
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
		}

		return inSampleSize;
	}

	/**
	 * 将知识返回的关联对象转换为ASSORPOK对象
	 * 
	 * @return
	 */
	public ASSORPOK createNewASSO() {
		DemandASSO asso = new DemandASSO();
		List<com.tr.model.demand.ASSOData> p = new ArrayList<com.tr.model.demand.ASSOData>();
		// 人脉信息
		if (connectionNodePeopleList != null) {
			for (ConnectionNode node : connectionNodePeopleList) {
				List<DemandASSOData> conn = new ArrayList<DemandASSOData>();
				for (Connections obj : node.getListConnections()) {
					DemandASSOData assoData = new DemandASSOData();
					assoData.id = obj.getId();
					assoData.name = obj.getName();
					assoData.ownerid = App.getUserID();
					assoData.ownername = App.getNick();
					assoData.career = obj.getCareer();
					assoData.company = obj.getCompany();
					if (obj.jtContactMini.isOnline()) {
						assoData.type = 3;
					} else {
						assoData.type = 2;
					}

					assoData.picPath = obj.jtContactMini.image;
					conn.add(assoData);
				}
				p.add(new ASSOData(node.getMemo(), conn));
			}
		}
		List<ASSOData> o = new ArrayList<ASSOData>();
		// 组织信息
		if (connectionNodeOrgList != null) {
			for (ConnectionNode node : connectionNodeOrgList) {
				List<DemandASSOData> conn = new ArrayList<DemandASSOData>();
				for (Connections obj : node.getListConnections()) {
					DemandASSOData assoData = new DemandASSOData();
					if (obj.organizationMini.isOnline()) {
						assoData.type = 4;
					} else {
						assoData.type = 5;
					}

					assoData.id = obj.getId();
					assoData.name = obj.getName();
					assoData.ownerid = App.getUserID();
					assoData.ownername = App.getNick();
					assoData.picPath = obj.organizationMini.mLogo;
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
		if (knowledgeNodeList != null) {
			for (KnowledgeNode node : knowledgeNodeList) {
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
		if (affairNodeList != null) {
			for (AffairNode node : affairNodeList) {
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
	 * 将 ASSORPOK对象转换为知识需要的关联对象
	 * 
	 * @return
	 */
	public void createKnowNewASSO(ASSORPOK relatedInformation2) {
		if (!relatedInformation2.p.isEmpty()) {

			List<ASSOData> Pass = relatedInformation2.p;
			for (ASSOData assoData : Pass) {
				ConnectionNode Pnode = new ConnectionNode();
				Pnode.setMemo(assoData.tag);
				Connections Pobj = null;
				List<DemandASSOData> conn2 = assoData.conn;
				for (int j = 0; j < conn2.size(); j++) {
					Pobj = new Connections();
					Pobj.setType(Connections.type_persion + "");
					Pobj.setID(conn2.get(j).id);
					Pobj.setName(conn2.get(j).name);
					Pobj.setCareer(conn2.get(j).career);
					Pobj.setCompany(conn2.get(j).company);
					if (conn2.get(j).type == 2)// type=2人脉
						Pobj.jtContactMini.isOnline = false;
					if (conn2.get(j).type == 3)// type=3好友
						Pobj.jtContactMini.isOnline = true;
					Pobj.jtContactMini.image = conn2.get(j).picPath;
					Pnode.getListConnections().add(Pobj);
				}
				connectionNodePeopleList.add(Pnode);
			}
			peopleGroupAdapter.notifyDataSetChanged();
		}
		if (!relatedInformation2.o.isEmpty()) {

			List<ASSOData> Oass = relatedInformation2.o;
			for (ASSOData assoData : Oass) {
				ConnectionNode Onode = new ConnectionNode();
				List<DemandASSOData> conn2 = assoData.conn;
				Onode.setMemo(assoData.tag);
				Connections Oobj = null;
				for (int j = 0; j < conn2.size(); j++) {
					Oobj = new Connections();
					Oobj.setType(Connections.type_org + "");
					Oobj.setID(conn2.get(j).id);
					Oobj.setName(conn2.get(j).name);
					Oobj.setCareer(conn2.get(j).career);
					Oobj.setCompany(conn2.get(j).company);
					Oobj.organizationMini.logo = conn2.get(j).picPath;
					Onode.getListConnections().add(Oobj);
				}
				connectionNodeOrgList.add(Onode);
			}
			organizationGroupAdapter.notifyDataSetChanged();
		}
		if (!relatedInformation2.k.isEmpty()) {

			List<ASSOData> Kass = relatedInformation2.k;
			for (ASSOData assoData : Kass) {
				KnowledgeNode Knode = new KnowledgeNode();
				List<DemandASSOData> conn2 = assoData.conn;
				Knode.setMemo(assoData.tag);
				KnowledgeMini2 Kobj = null;
				for (int j = 0; j < conn2.size(); j++) {
					Kobj = new KnowledgeMini2();
					String name = conn2.get(j).title;
					Kobj.id = Long.parseLong(conn2.get(j).id);
					Kobj.title = name;
					Knode.getListKnowledgeMini2().add(Kobj);
				}
				knowledgeNodeList.add(Knode);
			}
			knowledgeGroupAdapter.notifyDataSetChanged();

		}
		if (!relatedInformation2.r.isEmpty()) {

			List<ASSOData> Aass = relatedInformation2.r;
			for (ASSOData assoData : Aass) {
				AffairNode Anode = new AffairNode();
				AffairsMini Aobj = null;
				List<DemandASSOData> conn2 = assoData.conn;
				Anode.setMemo(assoData.tag);
				for (int j = 0; j < conn2.size(); j++) {
					Aobj = new AffairsMini();
					String name = conn2.get(j).title;
					Aobj.id = Integer.parseInt(conn2.get(j).id);
					Aobj.title = name;
					Anode.getListAffairMini().add(Aobj);
				}
				affairNodeList.add(Anode);
			}
			requirementGroupAdapter.notifyDataSetChanged();
		}
	}

	// 四大组件ListView设置
	private void initListViewData() {
		people.setOnItemClickListener(this);
		peopleGroupAdapter = new ConnectionsGroupAdapter(getActivity(),
				connectionNodePeopleList);
		people.setAdapter(peopleGroupAdapter);
		organization.setOnItemClickListener(this);
		organizationGroupAdapter = new ConnectionsGroupAdapter(getActivity(),
				connectionNodeOrgList);
		organization.setAdapter(organizationGroupAdapter);
		knowledge.setOnItemClickListener(this);
		knowledgeGroupAdapter = new KnowledgeGroupAdapter(getActivity(),
				knowledgeNodeList);
		knowledge.setAdapter(knowledgeGroupAdapter);
		requirement.setOnItemClickListener(this);
		requirementGroupAdapter = new RequirementGroupAdapter(getActivity(),
				affairNodeList);
		requirement.setAdapter(requirementGroupAdapter);
	}

	public void updateAllUI() {
		peopleGroupAdapter
				.setListRelatedConnectionsNode(connectionNodePeopleList);
		peopleGroupAdapter.notifyDataSetChanged();
		organizationGroupAdapter
				.setListRelatedConnectionsNode(connectionNodeOrgList);
		organizationGroupAdapter.notifyDataSetChanged();
		knowledgeGroupAdapter.setListRelatedKnowledgeNode(knowledgeNodeList);
		knowledgeGroupAdapter.notifyDataSetChanged();
		requirementGroupAdapter.setListRelatedAffairNode(affairNodeList);
		requirementGroupAdapter.notifyDataSetChanged();
	}

	/**
	 * 人脉，组织适配器
	 * 
	 * @author John
	 * 
	 */
	public class ConnectionsGroupAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<ConnectionNode> listRelatedConnectionsNode;
		public ConnectionsGroupAdapter() {
			super();
		}

		public ConnectionsGroupAdapter(Context context,
				ArrayList<ConnectionNode> listRelatedConnectionsNode) {
			super();
			this.context = context;
			if (listRelatedConnectionsNode != null) {
				this.listRelatedConnectionsNode = listRelatedConnectionsNode;
			} else {
				this.listRelatedConnectionsNode = new ArrayList<ConnectionNode>();
			}
		}

		public ArrayList<ConnectionNode> getListRelatedConnectionsNode() {
			return listRelatedConnectionsNode;
		}

		public void setListRelatedConnectionsNode(
				ArrayList<ConnectionNode> listRelatedConnectionsNode) {
			if (listRelatedConnectionsNode != null) {
				this.listRelatedConnectionsNode = listRelatedConnectionsNode;
			}
		}

		@Override
		public int getCount() {
			return listRelatedConnectionsNode.size();
		}

		@Override
		public Object getItem(int position) {
			return listRelatedConnectionsNode.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				final ViewGroup parent) {

			ViewHolder viewHolder;

			if (convertView == null) {
				viewHolder = new ViewHolder();

				convertView = LayoutInflater.from(context).inflate(
						R.layout.list_item_connections_group, null);
				viewHolder.keyTv = (TextView) convertView
						.findViewById(R.id.keyTv);
				viewHolder.valueTv = (TextView) convertView
						.findViewById(R.id.valueTv);
				viewHolder.deleteIv = (ImageView) convertView
						.findViewById(R.id.deleteIv);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.deleteIv.setVisibility(View.GONE);
			viewHolder.deleteIv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					listRelatedConnectionsNode.remove(position);
					notifyDataSetChanged();
					if (listRelatedConnectionsNode.isEmpty()) {
						LinearLayout layout = (LinearLayout) parent.getParent();
						layout.setVisibility(View.GONE);
					}
				}
			});
		

			ConnectionNode connectionNode = listRelatedConnectionsNode
					.get(position);
			String key = connectionNode.getMemo();
			viewHolder.keyTv.setText(key + " : ");
			ArrayList<Connections> listConnections = connectionNode
					.getListConnections();
			StringBuilder valueSb = new StringBuilder();
			for (int i = 0; i < listConnections.size(); i++) {
				valueSb.append(listConnections.get(i).getName()+"，");
				if (i != listConnections.size() - 1) {
					valueSb.append(decollatorStr);
				}
			}

			viewHolder.valueTv.setText(valueSb.toString());

			return convertView;
		}

		class ViewHolder {
			TextView keyTv;
			TextView valueTv;
			ImageView deleteIv;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		currentRequestEditPosition = position;
		currentRequestState = STATE_EDIT;
	
		if (parent == people) { // 编辑关联人脉
			currentRequestCode = REQUEST_CODE_RELATED_RESOURCE_PEOPLE;
			ENavigate.startRelatedResourceActivityForResult(getActivity(), 101,
					null, ResourceType.People,
					connectionNodePeopleList.get(position));
		} else if (parent == organization) { // 编辑关联组织
			currentRequestCode = REQUEST_CODE_RELATED_RESOURCE_ORGANIZATION;
			ENavigate.startRelatedResourceActivityForResult(getActivity(), 101,
					null, ResourceType.Organization,
					connectionNodeOrgList.get(position));
		} else if (parent == knowledge) { // 编辑关联知识
			currentRequestCode = REQUEST_CODE_RELATED_RESOURCE_KNOWLEDGE;
			ENavigate.startRelatedResourceActivityForResult(getActivity(), 101,
					null, ResourceType.Knowledge,
					knowledgeNodeList.get(position));
		} else if (parent == requirement) { // 编辑关联事件
			currentRequestCode = REQUEST_CODE_RELATED_RESOURCE_AFFAIR;
			ENavigate.startRelatedResourceActivityForResult(getActivity(), 101,
					null, ResourceType.Affair, affairNodeList.get(position));
		}
	
	}

	/**
	 * 知识适配器
	 * */
	public class KnowledgeGroupAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<KnowledgeNode> listRelatedKnowledgeNode;
		public KnowledgeGroupAdapter() {
			super();
		}
		public KnowledgeGroupAdapter(Context context,
				ArrayList<KnowledgeNode> listRelatedKnowledgeNode) {
			super();
			this.context = context;
			if (listRelatedKnowledgeNode != null) {
				this.listRelatedKnowledgeNode = listRelatedKnowledgeNode;
			} else {
				this.listRelatedKnowledgeNode = new ArrayList<KnowledgeNode>();
			}
		}

		public ArrayList<KnowledgeNode> getListRelatedKnowledgeNode() {
			return listRelatedKnowledgeNode;
		}

		public void setListRelatedKnowledgeNode(
				ArrayList<KnowledgeNode> listRelatedKnowledgeNode) {
			if (listRelatedKnowledgeNode != null) {
				this.listRelatedKnowledgeNode = listRelatedKnowledgeNode;
			}
		}

		@Override
		public int getCount() {
			return listRelatedKnowledgeNode.size();
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
		public View getView(final int position, View convertView,
				final ViewGroup parent) {

			ViewHolder viewHolder;

			if (convertView == null) {
				viewHolder = new ViewHolder();

				convertView = LayoutInflater.from(context).inflate(
						R.layout.list_item_connections_group, null);
				viewHolder.keyTv = (TextView) convertView
						.findViewById(R.id.keyTv);
				viewHolder.valueTv = (TextView) convertView
						.findViewById(R.id.valueTv);
				viewHolder.deleteIv = (ImageView) convertView
						.findViewById(R.id.deleteIv);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.deleteIv.setVisibility(View.GONE);
			viewHolder.deleteIv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					listRelatedKnowledgeNode.remove(position);
					notifyDataSetChanged();
					if (listRelatedKnowledgeNode.isEmpty()) {
						LinearLayout layout = (LinearLayout) parent.getParent();
						layout.setVisibility(View.GONE);
					}
				}
			});
			

			KnowledgeNode knowledgeNode = listRelatedKnowledgeNode
					.get(position);
			String key = knowledgeNode.getMemo();
			viewHolder.keyTv.setText(key + " : ");
			ArrayList<KnowledgeMini2> listKnowledgeMini2 = knowledgeNode
					.getListKnowledgeMini2();
			StringBuilder valueSb = new StringBuilder();
			for (int i = 0; i < listKnowledgeMini2.size(); i++) {
				valueSb.append(listKnowledgeMini2.get(i).title + "，");
				if (i != listKnowledgeMini2.size() - 1) {
					valueSb.append(decollatorStr);
				}
			}

			viewHolder.valueTv.setText(valueSb.toString());

			return convertView;
		}

		class ViewHolder {
			TextView keyTv;
			TextView valueTv;
			ImageView deleteIv;
		}

	}

	/**
	 * 
	 * 
	 * 事务适配器
	 * */
	public class RequirementGroupAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<AffairNode> listRelatedAffairNode;
		public RequirementGroupAdapter() {
			super();
		}
		public RequirementGroupAdapter(Context context,
				ArrayList<AffairNode> listRelatedAffairNode) {
			super();
			this.context = context;
			if (listRelatedAffairNode != null) {
				this.listRelatedAffairNode = listRelatedAffairNode;
			} else {
				this.listRelatedAffairNode = new ArrayList<AffairNode>();
			}
		}

		public ArrayList<AffairNode> getListRelatedAffairNode() {
			return listRelatedAffairNode;
		}

		public void setListRelatedAffairNode(
				ArrayList<AffairNode> listRelatedAffairNode) {
			if (listRelatedAffairNode != null) {
				this.listRelatedAffairNode = listRelatedAffairNode;
			}
		}

		@Override
		public int getCount() {
			return listRelatedAffairNode.size();
		}

		@Override
		public Object getItem(int position) {
			return listRelatedAffairNode.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				final ViewGroup parent) {

			ViewHolder viewHolder;

			if (convertView == null) {
				viewHolder = new ViewHolder();

				convertView = LayoutInflater.from(context).inflate(
						R.layout.list_item_connections_group, null);
				viewHolder.keyTv = (TextView) convertView
						.findViewById(R.id.keyTv);
				viewHolder.valueTv = (TextView) convertView
						.findViewById(R.id.valueTv);
				viewHolder.deleteIv = (ImageView) convertView
						.findViewById(R.id.deleteIv);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.deleteIv.setVisibility(View.GONE);
			viewHolder.deleteIv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					listRelatedAffairNode.remove(position);
					notifyDataSetChanged();
					if (listRelatedAffairNode.isEmpty()) {
						LinearLayout layout = (LinearLayout) parent.getParent();
						layout.setVisibility(View.GONE);
					}
				}
			});

			AffairNode affairNode = listRelatedAffairNode.get(position);
			String key = affairNode.getMemo();
			viewHolder.keyTv.setText(key + " : ");
			ArrayList<AffairsMini> listaAffairsMini = affairNode
					.getListAffairMini();
			StringBuilder valueSb = new StringBuilder();
			for (int i = 0; i < listaAffairsMini.size(); i++) {
				valueSb.append(listaAffairsMini.get(i).title + "，");
				if (i != listaAffairsMini.size() - 1) {
					valueSb.append(decollatorStr);
				}
			}
			viewHolder.valueTv.setText(valueSb.toString());

			return convertView;
		}

		class ViewHolder {
			TextView keyTv;
			TextView valueTv;
			ImageView deleteIv;
		}

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		if (CREAT_SATATE == 0) {
			inflater.inflate(R.menu.menu_createflow, menu);
			menu.findItem(R.id.flow_create).setTitle("完成");
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.flow_create) {
			if (!TextUtils.isEmpty(commumitiesSurnameEtv.getText().trim())) {
				if (!TextUtils.isEmpty(commumitiesNumberEtv.getText().trim())) {
					if (CREAT_SATATE == 0)
						CommunityReqUtil.existCommunityNo(getActivity(), FragStartCreatCommunity.this, commumitiesNumberEtv.getText().trim(), null);
					else
						// TODO 编辑完成
						finishParentActivity();
				} else
					showToast("社群号不能为空");
			} else
				showToast("社群名称不能为空");
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 开始创建社群
	 */
	private void onStartCreatCommunity() {
		showLoadingDialog();
		Community community = new Community();
		community.setStatus(1);// 1-正常召开；2-预约中，等待开始；3-已解散，解散后用户不可见.
		community.setAutoSaveType(1);// 是否保存聊天记录，0否，1是，目前是自动保存.
		community.setType(4);// 创建的社群type=4；
		community.setPicPath(avatarUrl);// 头像
		community.setTitle(commumitiesSurnameEtv.getText());// 社群名称
		community.setCommunityNo(commumitiesNumberEtv.getText());// 社群号
		community.setSubject(commumitiesIntroduceEtv.getText());// 社群介绍
		community.setContent(commumitiesCommonalityEtv.getText());// 社群公告
		community.setOwnerId(Long.parseLong(String.valueOf(App.getApp()
				.getUserID())));// 群主id
		community.setCreateUserId(Long.parseLong(String.valueOf(App.getApp()
				.getUserID())));// 社群创建者id
		// if (null == createSet)
		// createSet = new
		// CreateSet(Long.parseLong(String.valueOf(App.getApp().getUserID())));

		CommunityReqUtil.doCreateCommunity(getActivity(), community,
				createNewASSO(), tid, createSet, this, null);
	}

	@Override
	public void bindData(int tag, Object object) {
		HashMap<String, Object> dataMap = (HashMap<String, Object>) object;
		switch (tag) {
		case CommunityReqType.TYPE_EXIST_COMMUNITYNO:
			if (dataMap != null) {
				boolean isExist = (Boolean) dataMap.get("isExist");
				if(isExist){
					showToast("群号重复");
				}else{
					onStartCreatCommunity();
				}
			}else{
				onStartCreatCommunity();
			}
			break;
		case CommunityReqType.TYPE_CREATE_COMMUNITY:// 创建社群
			if (null != dataMap) {
				communityId = (Long) dataMap.get("communityId");
				List<Long> list = new ArrayList<Long>();
				list.add(Long.parseLong(App.getApp().getUserID()));
				CommunityReqUtil.doInvite2Muc(getActivity(), communityId, list,
						this, null);

			} else {
				showToast("创建失败");
			}
			break;

		case CommunityReqType.TYPE_INVITE2MUC:// 创建完后得邀请创建者进来才是真正创建成功
			if (null != dataMap) {
				MUCDetail mucDetail = (MUCDetail) dataMap.get("mucDetail");
				Intent intent = new Intent(getActivity(),
						CommunitiesDetailsActivity.class);
				intent.putExtra(GlobalVariable.COMMUNITY_ID, communityId);
				intent.putExtra(GlobalVariable.COMMUNITY_TITLE,
						commumitiesSurnameEtv.getText());
				startActivity(intent);
				finishParentActivity();
				// dismissLoadingDialog();
			} else {
				showToast("创建不成功");
			}
			break;
		case CommunityReqType.TYPE_GET_COMMUNITY_DETAIL:// 社群详情
			if (null != dataMap) {
				exist = (Boolean) dataMap.get("exist");
				communityDetailRes = (CommunityDetailRes) dataMap.get("result");
				upDateDetailUi();
			}
			break;
		case EAPIConsts.CommunityReqType.TYPE_MODIFY_COMMUNITYINFO:// 更改基本消息
			if (null != dataMap) {
				String notifCode = (String) dataMap.get("notifCode");
				// if(notifCode.contains("1"))
				// ImageLoader.geti
			}
			break;
		default:
			break;
		}
		dismissLoadingDialog();
	}

	private void upDateDetailUi() {
		community = communityDetailRes.getCommunity();
		asso = communityDetailRes.getAsso();
		labels = communityDetailRes.getLabels();
		if(null!=labels){
			lableData=labels;
			showLabels();
		}
		createSet = communityDetailRes.getSet();
		
		if (null != asso) {
			r = asso.r;// 事件
			k = asso.k;// 知识
			o = asso.o;// 组织即企业
			p = asso.p;// 人脉
			AffairNode affairNode=null;
			for (int i = 0; null != r && i < r.size(); i++) {
				ASSOData assoData = r.get(i);
				affairNode = assoData.toAffairNode();
				if (affairNode != null)
					affairNodeList.add(affairNode);
			}
			KnowledgeNode knowledgeNode =null;
			for (int i = 0; null != k && i < k.size(); i++) {
				ASSOData assoData = k.get(i);
				knowledgeNode = assoData.toKnowledgeNode();
				if (knowledgeNode != null)
					knowledgeNodeList.add(knowledgeNode);
			}
			ConnectionNode orgnote =null;
			for (int i = 0; null != o && i < o.size(); i++) {
				ASSOData assoData = o.get(i);
				orgnote = assoData.toConnectionNode();
				if (orgnote != null)
					connectionNodeOrgList.add(orgnote);

			}
			ConnectionNode peoplenote =null;
			for (int i = 0; null != p && i < p.size(); i++) {
				ASSOData assoData = p.get(i);
				peoplenote = assoData.toConnectionNode();
				if (peoplenote != null)
					connectionNodePeopleList.add(peoplenote);

			}
			if(connectionNodeMap!=null)
				connectionNodeMap.clear();
			if (peoplenote != null)
				pcount = null != peoplenote.getListConnections() ? peoplenote.getListConnections().size() : 0;
			if (orgnote != null)
				ocount = null != orgnote.getListConnections() ? orgnote.getListConnections().size() : 0;
			if (knowledgeNode != null)
				kcount = null != knowledgeNode.getListKnowledgeMini2() ? knowledgeNode.getListKnowledgeMini2().size() : 0;
			if (affairNode != null)
				rcount = null != affairNode.getListAffairMini() ? affairNode.getListAffairMini().size() : 0;
			connectionNodeMap.put("p", peoplenote);
			connectionNodeMap.put("o", orgnote);
			connectionNodeMap.put("k", knowledgeNode);
			connectionNodeMap.put("r", affairNode);
		}
		ImageLoader.getInstance().displayImage(community.getPicPath(),
				circle_avatar_edit);// 头像
		commumitiesSurnameEtv.setText(community.getTitle());// 群名称
		commumitiesNumberEtv.setText(community.getCommunityNo());// 社群号
		commumitiesIntroduceEtv.setText(community.getSubject());// 社群介绍
		commumitiesCommonalityEtv.setText(community.getContent());// 社群公告
		commumitiesRelevanceEtv.setText("人脉("+pcount+")"+",组织("+ocount+")"+",知识("+kcount+")"+",事件("+rcount+")");
//		refreshPeople();
//		refreshOrg();
//		refreshKnowledge();
//		refreshAffair();
	}

	/**
	 * 修改基本信息
	 * 
	 * @param “key”:社群的属性:头像，名称，公告，社群介绍
	 * @param "value":属性值,
	 */
	private void doModifyCommunityInfo(String key, String value) {
		showLoadingDialog();
		CommunityReqUtil.doModifyCommunityInfo(getActivity(), this,
				communityId, "picPath", newAvatarUrl, null);
	}

	// /********上传头像回调方法***********///
	@Override
	public void onPrepared(String id) {

	}

	@Override
	public void onStarted(String id) {

	}

	@Override
	public void onUpdate(String id, int progress) {

	}

	@Override
	public void onCanceled(String id) {

	}

	@Override
	public void onSuccess(String id, Map<String, String> result) {
		final String url = result.get("url"); // 拿到的是绝对路径
		KeelLog.d("===>>onSuccess", url);

		if (!TextUtils.isEmpty(url)) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (CREAT_SATATE == 1) {
						newAvatarUrl = url;
						doModifyCommunityInfo("picPath", newAvatarUrl);
					}
					Toast.makeText(getActivity(), "上传成功", 0).show();
				}
			});

		}
		// if (CREAT_SATATE == 1) {
		// newAvatarUrl = url;
		// doModifyCommunityInfo("picPath", newAvatarUrl);
		// } else {
		// avatarUrl = result.get("urlToSql");
		// //result.get("urlToSql")拿到的是相对路径
		avatarUrl = url;
		KeelLog.d("===>>onSuccess", url);
		// }

	}

	@Override
	public void onError(String id, int code, final String message) {
		KeelLog.d("===>>OnError", message);
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT)
						.show();
			}
		});

	}
	
	// /********上传头像回调方法***********///
}
