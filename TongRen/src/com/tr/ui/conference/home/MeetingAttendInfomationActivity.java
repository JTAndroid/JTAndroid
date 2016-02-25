package com.tr.ui.conference.home;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.App;
import com.tr.R;
import com.tr.api.ConferenceReqUtil;
import com.tr.api.IMReqUtil;
import com.tr.model.SimpleResult;
import com.tr.model.conference.MMeetingMember;
import com.tr.model.conference.MMeetingQuery;
import com.tr.model.conference.MMeetingTopicQuery;
import com.tr.model.obj.JTContactMini;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.conference.common.BaseActivity;
import com.tr.ui.conference.initiatorhy.InitiatorDataCache;
import com.tr.ui.conference.initiatorhy.InitiatorHYActivity;
import com.tr.ui.conference.square.KnowLedgeDataActivity;
import com.tr.ui.conference.square.RelationshipActivity;
import com.tr.ui.conference.square.RequirmentDataActivity;
import com.tr.ui.conference.utile.PeopleOrgknowleRequirmentLayoutUtil;
import com.tr.ui.home.frg.FrgSociality;
import com.tr.ui.widgets.BasicGridView;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/**
 * 与会信息：由主会场右上角跳转来 或者 由无议题的会议聊天页面的右上角跳转来
 * 
 * @author d.c 2014-11-15
 */

public class MeetingAttendInfomationActivity extends BaseActivity implements IBindData, OnItemClickListener, OnClickListener {
	// 标题栏
	private LinearLayout mIvBackButton = null;
	private ImageView backIv = null;
	private TextView mTvTitle = null;

	private ScrollView sv_main = null;
	private BasicGridView gridView = null;
	private TextView tv_name = null;
	private FrameLayout btn_end = null;

	private LinearLayout ll_barcode = null;
	private LinearLayout ll_search = null;
	private LinearLayout ll_setting = null;

	private MMeetingTopicQuery topicQuery = null;
	private MMeetingQuery mMeetingQuery = null;
	private ImageLoader mImageLoader = null;

	/** 会议分享人脉模块布局 */
	private LinearLayout peopleToggle;
	/** 人脉 容器 --- hy_list_item_tile_contacts(子内容) */
	private LinearLayout attendeeContainer;
	/**人脉标题*/
	private LinearLayout attendeeSubtitleMeeting;

	/** 会议分享组织模块布局 */
	private LinearLayout org_Toggle;
	/** 组织 容器 --- hy_meeting_detail_ll_org(子内容) */
	private LinearLayout orgContainer;
	/**标题：组织*/
	private LinearLayout orgSubtitleMeeting;

	/** 会议分享需求模块布局 */
	private LinearLayout requirmentToggle;
	/** 需求的容器 添加内容 --> hy_list_item_meeting_requirement */
	private LinearLayout requirmentContainer;

	/** 会议分享知识模块布局 */
	private LinearLayout knowledgeToggle;
	/** 知识的容器 添加内容 --> hy_list_item_meeting_general */
	private LinearLayout knowLedgeContainer;

	private GridViewMemberListAdapter adapter;
	private List<MMeetingMember> listMeetingMember = new ArrayList<MMeetingMember>();
	/** 是否显示 GridView中添加和删除按钮 */
	private boolean isShowAddandDeleteButton;

	private int call_for_modify = 0x10001020;
	private LinearLayout lookForMeetingDetialLL;
	/**标题：需求*/
	private LinearLayout requirmentSubtitleMeeting;
	private LinearLayout knowledgeSubtitleMeeting;
	
	private int INVITE_REQUEST_CODE = 10000;
	private ArrayList<MMeetingMember> addListMeetingMembers = new ArrayList<MMeetingMember>();
	@Override
	public void initView() {
		setContentView(R.layout.hy_activity_meeting_attend_infomation);
		mIvBackButton = (LinearLayout) findViewById(R.id.hy_layoutTitle_backBtn);
		backIv = (ImageView) findViewById(R.id.backIv);
		mTvTitle = (TextView) findViewById(R.id.hy_layoutTitle_title);

		sv_main = (ScrollView) findViewById(R.id.hy_meeting_attend_sl_main);
		gridView = (BasicGridView) findViewById(R.id.attendPeopleGV);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		tv_name = (TextView) findViewById(R.id.hy_attend_infomation_tv_name);
		btn_end = (FrameLayout) findViewById(R.id.hy_attend_infomation_btn_end);

		ll_barcode = (LinearLayout) findViewById(R.id.hy_attend_infomation_ll_barcode);
		ll_search = (LinearLayout) findViewById(R.id.hy_attend_infomation_ll_search);
		ll_setting = (LinearLayout) findViewById(R.id.hy_attend_infomation_ll_setting);
		lookForMeetingDetialLL = (LinearLayout) findViewById(R.id.lookForMeetingDetialLL);

		peopleToggle = (LinearLayout) findViewById(R.id.meeting_people_toggle_ll);
		attendeeContainer = (LinearLayout) findViewById(R.id.hy_meeting_detail_ll_contact);
		attendeeSubtitleMeeting = (LinearLayout) findViewById(R.id.attendee_subtitle_item_meeting);
		
		org_Toggle = (LinearLayout) findViewById(R.id.meeting_org_toggle_ll);
		orgContainer = (LinearLayout) findViewById(R.id.hy_meeting_detail_ll_org);
		orgSubtitleMeeting = (LinearLayout) findViewById(R.id.attendee_org_subtitle_item_meeting);
	
		requirmentToggle = (LinearLayout) findViewById(R.id.requirment_toggle_ll);
		requirmentContainer = (LinearLayout) findViewById(R.id.hy_meeting_detail_ll_requirement);
		requirmentSubtitleMeeting = (LinearLayout) findViewById(R.id.requirment_subtitle_item_meeting);

		knowledgeToggle = (LinearLayout) findViewById(R.id.knowledge_toggle_ll);
		knowLedgeContainer = (LinearLayout) findViewById(R.id.hy_meeting_detail_ll_knowledge);
		knowledgeSubtitleMeeting = (LinearLayout) findViewById(R.id.knowledge_subtitle_item_meeting);
		
		attendeeSubtitleMeeting.setOnClickListener(this);
		orgSubtitleMeeting.setOnClickListener(this);
		requirmentSubtitleMeeting.setOnClickListener(this);
		knowledgeSubtitleMeeting.setOnClickListener(this);
		
		mImageLoader = ImageLoader.getInstance();
	}

	@Override
	public void initData() {
		mTvTitle.setText("与会信息");
		sv_main.setVisibility(View.GONE);
		ll_search.setVisibility(View.GONE);
		mMeetingQuery = (MMeetingQuery) getIntent().getSerializableExtra(ENavConsts.EMeetingDetail);
		topicQuery = (MMeetingTopicQuery) getIntent().getSerializableExtra(ENavConsts.EMeetingTopicDetail);
		if (null == mMeetingQuery) {
			Toast.makeText(this, "无效的会议数据", 0).show();
			finish();
		}
		fillData();

		backIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (addListMeetingMembers.size() > 0) {
					for (int i = 0; i < addListMeetingMembers.size(); i++) {
						requestAddMeetingMumber(addListMeetingMembers.get(i));
					}
				}
				InitiatorDataCache.getInstance().inviteAttendSelectedMap.clear();
				finish();
			}
		});

		btn_end.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				android.content.DialogInterface.OnClickListener lis = new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				};
				new AlertDialog.Builder(MeetingAttendInfomationActivity.this).setTitle("与会信息").setMessage("确认结束会议吗").setPositiveButton("取消", lis)
						.setNegativeButton("确定", new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if (null != mMeetingQuery){
									ConferenceReqUtil.doChangeMeetingStatus(MeetingAttendInfomationActivity.this, MeetingAttendInfomationActivity.this, mMeetingQuery.getId(), 3, null);
									IMReqUtil.doclearUnreadMessageNumber(MeetingAttendInfomationActivity.this, MeetingAttendInfomationActivity.this, Long.valueOf(App.getUserID()), 0, mMeetingQuery.getId(), 3, null, true);
								}
								dialog.dismiss();
								showLoadingDialog();
							}
						}).show();
			}
		});

		ll_barcode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MeetingAttendInfomationActivity.this, MeetingInviteFaceActivity.class);
				intent.putExtra(ENavConsts.EMeetingDetail, mMeetingQuery);
				startActivity(intent);
			}
		});
		ll_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ENavigate.startMeetingRecordSearchActivity(MeetingAttendInfomationActivity.this, mMeetingQuery, topicQuery);
			}
		});
		ll_setting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MeetingAttendInfomationActivity.this, InitiatorHYActivity.class);
				intent.putExtra("meetingData", mMeetingQuery);
				startActivityForResult(intent, call_for_modify);
			}
		});

		lookForMeetingDetialLL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ENavigate.startSquareActivity(MeetingAttendInfomationActivity.this, mMeetingQuery.getId(), 0);
			}
		});

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void bindData(int tag, Object object) {
		dismissLoadingDialog();
		if (EAPIConsts.ConferenceReqType.CONFERENCE_REQ_CHANGEMEETINGSTATUS == tag && null != object) {
			SimpleResult flag = (SimpleResult) object;
			if (null != flag) {
				if (flag.isSucceed()) {
					btn_end.setVisibility(View.GONE);
				} else {
					btn_end.setVisibility(View.VISIBLE);
					Toast.makeText(this, "会议结束失败", 0).show();
				}
			}
		}
		if (tag == EAPIConsts.IMReqType.IM_REQ_CLEAR_UNREAD_MESSAGENUMBER) {
			if (object!=null) {
				int responseCode = (Integer) object;
				//删除失败
				if (responseCode== -1) {
					btn_end.setVisibility(View.VISIBLE);
					Toast.makeText(this, "会议结束失败", 0).show();
				}
				//删除成功
				else if (responseCode==0) {
					btn_end.setVisibility(View.GONE);
				}
			}
		}
		if (EAPIConsts.ConferenceReqType.CONFERENCE_REQ_INVITATIONBYFACETOFACE== tag && null != object) {
			SimpleResult flag = (SimpleResult) object;
			if (flag != null && flag.isSucceed()) {
				
			}else {
				Toast.makeText(getApplicationContext(), "邀请参会人失败", 0).show();
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		if (requestCode == call_for_modify) {
			Intent data2 = new Intent();
			setResult(RESULT_OK, data2);
			finish();
		}
		if (INVITE_REQUEST_CODE == requestCode) {
			addListMeetingMembers.clear();
			Iterator<Entry<String, JTContactMini>> iterator = InitiatorDataCache.getInstance().inviteAttendSelectedMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, JTContactMini> entry = iterator.next();
				JTContactMini jtContactMini = entry.getValue();
				boolean hasAttendMeetingMumber = isHasAttendMeetingMumber(jtContactMini);
				if (!hasAttendMeetingMumber) {
					MMeetingMember mMeetingMember = new MMeetingMember();
					mMeetingMember.setAttendMeetStatus(0);
					mMeetingMember.setAttendMeetType(0);
					mMeetingMember.setMeetingId(mMeetingQuery.getId());
					mMeetingMember.setMemberId(Long.valueOf(jtContactMini.id));
					mMeetingMember.setMemberMeetStatus(0);
					mMeetingMember.setMemberName(jtContactMini.name);
					mMeetingMember.setMemberPhoto(jtContactMini.image);
					mMeetingMember.setMemberType(1);
					
					mMeetingMember.setAttendInvite(true);
					addListMeetingMembers.add(mMeetingMember);
				}
			}
			listMeetingMember.addAll(addListMeetingMembers);
			adapter.setListMeetingMember(listMeetingMember);
			adapter.notifyDataSetChanged();
		}
	}

	private void fillData() {
		if (null == mMeetingQuery) {
			return;
		}

		tv_name.setText(mMeetingQuery.getMeetingName());
		if (null != gridView) {
			initMemberData(mMeetingQuery);
			adapter = new GridViewMemberListAdapter(listMeetingMember, this);
			adapter.setListMeetingMember(listMeetingMember);
			adapter.setShowAddandDeleteButton(isShowAddandDeleteButton);
			gridView.setAdapter(adapter);
			gridView.setOnItemClickListener(this);
		}

		/*** 人 组织 知识 事件 */
		// 邀请参会人初始化
		intAllPeople();
		// 组织初始化
		initAllOrgan();
		// 需求信息初始化
		initAllRequirments();
		// 知识初始化
		initAllKnowledges();

		if (null != btn_end) {
			long cid = Long.valueOf(App.getApp().getUserID());
			/** 创建者不是自己 或 结束的会议 隐藏结束按钮(其他条件不用判断) */
			if (mMeetingQuery.getCreateId() != cid) {
				btn_end.setVisibility(View.GONE);
			} else if (mMeetingQuery.getMeetingStatus() == 3) {
				btn_end.setVisibility(View.GONE);
			}
		}

		if (null != ll_setting) {
			long cid = Long.valueOf(App.getApp().getUserID());
			if (mMeetingQuery.getCreateId() != cid /*
													 * || mMeetingQuery.
													 * getMeetingStatus()>1
													 */) {
				ll_setting.setVisibility(View.GONE);
			}
		}
		sv_main.setVisibility(View.VISIBLE);
	}

	/**
	 * 初始化5个人脉
	 */
	private void intAllPeople() {
		attendeeContainer.removeAllViews();
		if (mMeetingQuery != null && mMeetingQuery.getListMeetingPeople() != null && mMeetingQuery.getListMeetingPeople().size() != 0) {
			for (int i = 0; i < 5; i++) {
				ViewGroup people = PeopleOrgknowleRequirmentLayoutUtil.initPeople(mMeetingQuery.getListMeetingPeople(), i, this);
				if (i < mMeetingQuery.getListMeetingPeople().size()) {
					attendeeContainer.addView(people);
				}
			}
		} else {
			peopleToggle.setVisibility(View.GONE);
		}
	}

	/**
	 * 初始化5个组织
	 */
	private void initAllOrgan() {
		orgContainer.removeAllViews();
		if (mMeetingQuery != null && mMeetingQuery.getListMeetingOrgan() != null && mMeetingQuery.getListMeetingOrgan().size() != 0) {
			for (int i = 0; i < 5; i++) {
				ViewGroup org = PeopleOrgknowleRequirmentLayoutUtil.initOrgan(mMeetingQuery.getListMeetingOrgan(), i, this);
				if (i < mMeetingQuery.getListMeetingOrgan().size() && org != null) {
					orgContainer.addView(org);
				}
			}
		} else {
			org_Toggle.setVisibility(View.GONE);
		}
	}

	/**
	 * 这里仅展示2条 需求信息
	 */
	private void initAllRequirments() {
		requirmentContainer.removeAllViews();
		if (mMeetingQuery != null && mMeetingQuery.getListMeetingRequirement() != null && mMeetingQuery.getListMeetingRequirement().size() != 0) {
			for (int i = 0; i < 2; i++) {
				if (i < mMeetingQuery.getListMeetingRequirement().size()) {
					ViewGroup requirment = PeopleOrgknowleRequirmentLayoutUtil.initRequirment(mMeetingQuery.getListMeetingRequirement(), i, this);
					requirmentContainer.addView(requirment);
				}
			}
		} else {
			requirmentToggle.setVisibility(View.GONE);
		}
	}

	/**
	 * 这里仅展示2条 知识信息
	 */
	private void initAllKnowledges() {
		knowLedgeContainer.removeAllViews();
		if (mMeetingQuery != null && mMeetingQuery.getListMeetingKnowledge() != null && mMeetingQuery.getListMeetingKnowledge().size() != 0) {
			for (int i = 0; i < 2; i++) {
				if (i < mMeetingQuery.getListMeetingKnowledge().size()) {
					ViewGroup knowledge = PeopleOrgknowleRequirmentLayoutUtil.initKnowledge(mMeetingQuery.getListMeetingKnowledge(), i, this);
					knowLedgeContainer.addView(knowledge);
				}
			}
		} else {
			knowledgeToggle.setVisibility(View.GONE);
		}
	}

	/**
	 * 初始化参会人数据：根据参会人的报名及邀请状态判断是否显示参会人
	 * 
	 * @param meetingQuery
	 */
	private void initMemberData(MMeetingQuery meetingQuery) {
		long id = 0;
		MMeetingMember meetingMember = null;
		List<MMeetingMember> members = meetingQuery.getListMeetingMember();
		for (int i = 0; i < members.size(); i++) {
			MMeetingMember mMeetingMember = members.get(i);
			if (mMeetingMember != null) {
				/** 参会方式 0：邀请 1：报名 */
				switch (mMeetingMember.getAttendMeetType()) {
				case 0:
					/** 参会状态 0：未答复 1：接受邀请 2：拒绝邀请 4：报名 5：取消报名 */
					if (mMeetingMember.getAttendMeetStatus() == 1) {
						listMeetingMember.add(mMeetingMember);
					}
					break;
				case 1:
					/** 处理会议报名：0：未处理 1：同意报名 2：拒绝报名 */
					if (mMeetingMember.getAttendMeetStatus() == 4 && mMeetingMember.getExcuteMeetSign() == 1) {
						listMeetingMember.add(mMeetingMember);
					}
					break;
				}
				
				if(mMeetingMember.getMemberType() == 2){//如果是创建人
					id = mMeetingMember.getMeetingId();//存储创建人id
					meetingMember = mMeetingMember;
				}else{
					if(mMeetingMember.getMeetingId() == id &&meetingMember!=null){ //参会人的id和存储的id相同，说明这个参会人在集合中出现了两次，去重
						listMeetingMember.remove(meetingMember);
					}
				}
				
//<<<<<<< HEAD
//				if (App.getUserID().equals(mMeetingMember.getMemberId()+"")) {
//=======
				if (App.getUserID().equals(mMeetingMember.getMemberId() + "")) {
					if (mMeetingMember.getMemberType() == 2) {
						isShowAddandDeleteButton = true;
					}
				}
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		adapter.onItemClickOpt(parent, view, position, id, mMeetingQuery.getId(),mMeetingQuery,addListMeetingMembers,INVITE_REQUEST_CODE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 人脉
		case R.id.attendee_subtitle_item_meeting:
			Intent relationshipIntent = new Intent(this, RelationshipActivity.class);
			relationshipIntent.putExtra(ENavConsts.EMeetingDetail, mMeetingQuery);
			relationshipIntent.putExtra("relationType", 1);
			startActivity(relationshipIntent);
			break;
		// 组织
		case R.id.attendee_org_subtitle_item_meeting:
			Intent intent = new Intent(this, RelationshipActivity.class);
			intent.putExtra(ENavConsts.EMeetingDetail, mMeetingQuery);
			intent.putExtra("relationType", 2);
			startActivity(intent);
			break;
		// 需求标题
		case R.id.requirment_subtitle_item_meeting:
			Intent requirmentDataIntent = new Intent(this, RequirmentDataActivity.class);
			requirmentDataIntent.putExtra(ENavConsts.EMeetingDetail, mMeetingQuery);
			startActivity(requirmentDataIntent);
			break;
		// 知识标题
		case R.id.knowledge_subtitle_item_meeting:
			Intent knowledgeDataIntent = new Intent(this, KnowLedgeDataActivity.class);
			knowledgeDataIntent.putExtra(ENavConsts.EMeetingDetail, mMeetingQuery);
			startActivity(knowledgeDataIntent);
			break;}
	}
	private boolean isHasAttendMeetingMumber(JTContactMini jtContactMini) {
		boolean isHasAttendMeetingMumber = false;
		for (MMeetingMember mmMember : listMeetingMember) {
			if (jtContactMini.getId().equals(mmMember.getMemberId()+"")) {
				isHasAttendMeetingMumber = true;
				break;
			}
		}
		return isHasAttendMeetingMumber;
	}
	
	@Override
	public void onBackPressed() {
		if (addListMeetingMembers.size() > 0) {
			for (int i = 0; i < addListMeetingMembers.size(); i++) {
				requestAddMeetingMumber(addListMeetingMembers.get(i));
			}
		}
		InitiatorDataCache.getInstance().inviteAttendSelectedMap.clear();
		super.onBackPressed();
	}
	
	private void requestAddMeetingMumber(MMeetingMember mMeetingMember) {
		if (mMeetingMember!=null) {
			showLoadingDialog();
			ConferenceReqUtil.doInvitationFaceToFace(this, this, mMeetingMember, null);
		}
	}
}