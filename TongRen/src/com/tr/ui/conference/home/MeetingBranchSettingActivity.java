package com.tr.ui.conference.home;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.App;
import com.tr.R;
import com.tr.api.ConferenceReqUtil;
import com.tr.model.SimpleResult;
import com.tr.model.conference.MCalendarSelectDateTime;
import com.tr.model.conference.MMeetingMember;
import com.tr.model.conference.MMeetingQuery;
import com.tr.model.conference.MMeetingTopicQuery;
import com.tr.model.obj.JTContactMini;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.conference.initiatorhy.CalendarActivity;
import com.tr.ui.conference.initiatorhy.ConferenceIntroduceActivity;
import com.tr.ui.conference.initiatorhy.InitiatorDataCache;
import com.tr.ui.conference.initiatorhy.IniviteUtil;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.time.Util;

/**
 * 分会场设置
 * 
 * @author zhongshan
 * 
 */
public class MeetingBranchSettingActivity extends JBaseActivity implements OnClickListener, IBindData, OnItemClickListener {
	private Context mContext;
	private GridView gridView;
	private TextView topicNameTv;
	private TextView topicTimeTv;
	private TextView topicDescTv;
	private MMeetingTopicQuery mMeetingTopicQuery;
	private List<MMeetingMember> listMeetingMember = new ArrayList<MMeetingMember>();
	private FrameLayout endMeetingFl;
	private final int TOPIC_NAME_RESULT_CODE = 10;
	private final int TOPIC_TIME_RESULT_CODE = 11;
	private final int TOPIC_DESC_RESULT_CODE = 12;
	private final int INVITE_MEEETING_MUMBER_RESULT_CODE = 10001;
	private boolean isChanged;
	private GridViewMemberListAdapter adapter;
	private long meetingId;
	private ArrayList<MMeetingMember> addListMeetingMembers = new ArrayList<MMeetingMember>();
	private boolean isFinished;

	/** 是否显示 GridView中添加和删除按钮 */
	private boolean isShowAddandDeleteButton;
	private MMeetingQuery meetingQuery;

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "分会场设置", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
//		getActionBar().setTitle("分会场设置");
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		initView();
		getParamer();
		initData();
	}

	private void initView() {
		setContentView(R.layout.activity_meetingbranch_setting_activity);
		gridView = (GridView) findViewById(R.id.attendPeopleGV);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		LinearLayout topicLayout = (LinearLayout) findViewById(R.id.topicLayout);
		topicLayout.findViewById(R.id.topicNameRl).setOnClickListener(this);
		topicLayout.findViewById(R.id.topicTimeRl).setOnClickListener(this);
		topicLayout.findViewById(R.id.topicDescRl).setOnClickListener(this);
		endMeetingFl = (FrameLayout) findViewById(R.id.endMeetingFl);
		topicNameTv = (TextView) topicLayout.findViewById(R.id.topicNameTv);
		topicTimeTv = (TextView) topicLayout.findViewById(R.id.topicTimeTv);
		topicDescTv = (TextView) topicLayout.findViewById(R.id.topicDescTv);
		endMeetingFl.setOnClickListener(this);
	}

	private void getParamer() {
		mMeetingTopicQuery = (MMeetingTopicQuery) getIntent().getSerializableExtra(ENavConsts.EMeetingTopicDetail);
		meetingQuery = (MMeetingQuery) getIntent().getSerializableExtra(ENavConsts.EMeetingDetail);
		initMemberData(meetingQuery);
		meetingId = meetingQuery.getId();
		if (mMeetingTopicQuery != null) {
			isFinished = mMeetingTopicQuery.getIsFinished() == 0 ? false : true;
		}
	}

	private void initData() {
		topicNameTv.setText(mMeetingTopicQuery.getTopicContent());
		MCalendarSelectDateTime sdt = IniviteUtil.getMSDT(mMeetingTopicQuery);
		topicTimeTv.setText(IniviteUtil.formatDateMeeting(sdt));
		topicDescTv.setText(mMeetingTopicQuery.getTopicDesc());
		adapter = new GridViewMemberListAdapter(listMeetingMember, mContext);
		adapter.setListMeetingMember(listMeetingMember);
		adapter.setShowAddandDeleteButton(isShowAddandDeleteButton);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);
		long cid = Long.valueOf(App.getUserID());

		// 创建者是自己 并且 议题未结束 并且 会以未结束
		if (meetingQuery.getCreateId() == cid && mMeetingTopicQuery.getIsFinished() != 1 && meetingQuery.getMeetingStatus() != 3) {
			endMeetingFl.setVisibility(View.VISIBLE);
		} else {
			endMeetingFl.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		if (!isFinished) {
			switch (v.getId()) {
			case R.id.topicNameRl:
				ENavigate.startEditTextContentActivity(this, mMeetingTopicQuery.getTopicContent(), TOPIC_NAME_RESULT_CODE);
				break;
			case R.id.topicTimeRl:
				InitiatorDataCache.getInstance().dateSelectetedTempList.clear();
				MCalendarSelectDateTime sdt = IniviteUtil.getMSDT(mMeetingTopicQuery);
				if (sdt != null) {
					InitiatorDataCache.getInstance().dateSelectetedTempList.add(sdt);
				}
				Bundle b = new Bundle();
				b.putInt(Util.IK_VALUE, 1);
				Util.forwardTargetActivityForResult(this, CalendarActivity.class, b, TOPIC_TIME_RESULT_CODE);
				break;
			case R.id.topicDescRl:
				Intent intent = new Intent();
				intent.putExtra("mMeetingTopicQuery", mMeetingTopicQuery);
				intent.setClass(this, ConferenceIntroduceActivity.class);
				startActivityForResult(intent, TOPIC_DESC_RESULT_CODE);
				break;
			case R.id.endMeetingFl:
				if (null != mMeetingTopicQuery)
					ConferenceReqUtil.doFinishTopic(this, this, mMeetingTopicQuery.getId(), null);
				showLoadingDialog();
				break;
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (/* resultCode == RESULT_OK && */data != null) {
			isChanged = true;
			switch (requestCode) {
			case TOPIC_NAME_RESULT_CODE:
				String stringExtra = data.getStringExtra("editTextInfo");
				mMeetingTopicQuery.setTopicContent(stringExtra);
				topicNameTv.setText(stringExtra);
				break;
			case TOPIC_TIME_RESULT_CODE:
				MCalendarSelectDateTime dt = InitiatorDataCache.getInstance().dateSelectetedTempList.get(0);
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				if (dt != null) {
					mMeetingTopicQuery.setTopicStartTime(dateFormat.format(new Date(dt.startDate)));
					mMeetingTopicQuery.setTopicEndTime(dateFormat.format(new Date(dt.endDate)));
				}
				break;
			case TOPIC_DESC_RESULT_CODE:
				mMeetingTopicQuery = (MMeetingTopicQuery) data.getSerializableExtra("mMeetingTopicQuery");
				topicDescTv.setText(mMeetingTopicQuery.getTopicDesc());
				break;
			case INVITE_MEEETING_MUMBER_RESULT_CODE:
				listMeetingMember.removeAll(addListMeetingMembers);
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
						mMeetingMember.setMeetingId(meetingId);
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
				break;
			}
		}
	}

	private boolean isHasAttendMeetingMumber(JTContactMini jtContactMini) {
		boolean isHasAttendMeetingMumber = false;
		for (MMeetingMember mmMember : listMeetingMember) {
			if (jtContactMini.getId().equals(mmMember.getMemberId() + "")) {
				isHasAttendMeetingMumber = true;
				break;
			}
		}
		return isHasAttendMeetingMumber;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem menuItem = menu.add(0, 100, 0, "完成");
		menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 100) {
			// 如果新增参会人：参会人逐个添加，成功了删除当前的再添加下一个，直到添加完，再去请求更新介绍
			// 如果没有新增参会人：直接更新介绍
			if (addListMeetingMembers.size() > 0 && addListMeetingMembers.get(0) != null) {
				requestAddMeetingMumber(addListMeetingMembers.get(0));
			} else if (isChanged && addListMeetingMembers.size() < 1) {
				showLoadingDialog();
				ConferenceReqUtil.doUpdateTopic(this, this, mMeetingTopicQuery, null);
			} else {
				finishWithResult();
			}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		adapter.onItemClickOpt(parent, view, position, id, meetingId, meetingQuery, addListMeetingMembers, INVITE_MEEETING_MUMBER_RESULT_CODE);
	}

	@Override
	public void bindData(int tag, Object object) {
		dismissLoadingDialog();
		if (object == null) {
			return;
		}
		switch (tag) {
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_UPDATETOPIC:
			boolean b = (Boolean) object;
			if (b) {
				finishWithResult();
			} else {
				showToast("更新失败");
			}
			break;
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_INVITATIONBYFACETOFACE:
			SimpleResult flag = (SimpleResult) object;
			if (flag != null && flag.isSucceed()) {
				// 添加成功，删除当前的 再去添加下一个
				if (addListMeetingMembers != null && addListMeetingMembers.size() > 0) {
					addListMeetingMembers.remove(0);
					if (addListMeetingMembers.size() > 0 && addListMeetingMembers.get(0) != null) {
						requestAddMeetingMumber(addListMeetingMembers.get(0));
					} else {
						if (isChanged) {
							showLoadingDialog();
							ConferenceReqUtil.doUpdateTopic(this, this, mMeetingTopicQuery, null);
						} else {
							finishWithResult();
						}
					}
				}
			} else {
				showToast("邀请参会人 " + addListMeetingMembers.get(0).getMemberName() + " 失败");
			}
			break;
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_FINISH_TOPIC:
			boolean result = (Boolean) object;
				if (result) {
					endMeetingFl.setVisibility(View.GONE);
					mMeetingTopicQuery.setIsFinished(1);
					isShowAddandDeleteButton = false;
					adapter.notifyDataSetChanged();
				} else {
					Toast.makeText(this, "会议结束失败", 0).show();
				}
			break;
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
				
				if (App.getUserID().equals(mMeetingMember.getMemberId() + "")) {
					if (mMeetingMember.getMemberType() == 2&&mMeetingTopicQuery.getIsFinished()==0) {
						isShowAddandDeleteButton = true;
					}
				}
			}
		}
	}

	private void requestAddMeetingMumber(MMeetingMember mMeetingMember) {
		showLoadingDialog();
		ConferenceReqUtil.doInvitationFaceToFace(this, this, mMeetingMember, null);
	}

	@Override
	protected void onDestroy() {
		InitiatorDataCache.getInstance().inviteAttendSelectedMap.clear();
		super.onDestroy();
	}

	private void finishWithResult() {
		Intent intent = new Intent();
		intent.putExtra("MeetingTopicQuery", mMeetingTopicQuery);
		intent.putExtra("meetingStatus", meetingQuery.getMeetingStatus());
		setResult(RESULT_OK, intent);
		finish();
	}

}
