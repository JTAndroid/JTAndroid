package com.tr.ui.conference.square;

import java.util.ArrayList;

import android.content.Intent;
import android.provider.MediaStore.Audio.Genres.Members;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tr.App;
import com.tr.R;
import com.tr.model.conference.MMeetingMember;
import com.tr.model.conference.MMeetingQuery;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.adapter.conference.GridViewMeetingAttendeesAdapter;
import com.tr.ui.conference.common.BaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;

/**
 * 点击会议详情页面 右上角参会人按钮：参会人信息
 * 
 * @author sunjianan
 */
public class AttendeesActivity extends BaseActivity implements OnClickListener {

	@ViewInject(R.id.attendees_container)
	private MeetingGridView gridView;

	private GridViewMeetingAttendeesAdapter adapter;

	@ViewInject(R.id.attendees_title)
	private FrameLayout layoutTitle;
	// 标题返回键
	private LinearLayout backBtn;
	// 标题题目
	private TextView titleTv;

	private MMeetingQuery meeting;

	// TODO
	@ViewInject(R.id.fragment_container)
	private RelativeLayout fragmentContainer;

	private ArrayList<MMeetingMember> members;

	// 0 主会场 1其他
	private int attendeeType;

	// 初始化界面
	@Override
	public void initView() {
		setContentView(R.layout.hy_activity_meeting_attendees);
		ViewUtils.inject(this);
		Intent attendeesIntent = getIntent();
		meeting = (MMeetingQuery) attendeesIntent.getSerializableExtra(ENavConsts.EMeetingDetail);
		attendeeType = attendeesIntent.getIntExtra("AttendeeType", 1);

		backBtn = (LinearLayout) layoutTitle.findViewById(R.id.hy_layoutTitle_backBtn);
		titleTv = (TextView) layoutTitle.findViewById(R.id.hy_layoutTitle_title);

		if (meeting != null && meeting.getListMeetingMember() != null) {
			members = new ArrayList<MMeetingMember>();
			long id = 0;
			MMeetingMember meetingMember = null;

			for (MMeetingMember member : meeting.getListMeetingMember()) {
				if (member.getMemberId() == meeting.getCreateId() || (member.getAttendMeetType() == 0 && member.getAttendMeetStatus() == 1)
						|| (member.getAttendMeetType() == 1 && member.getAttendMeetStatus() == 4 && member.getExcuteMeetSign() == 1)) {
					members.add(member);
//<<<<<<< HEAD
//					if(member.getMemberType() == 2){//如果是创建人
//						id = member.getMeetingId();//存储创建人id
//						meetingMember = member;
//					}else{
//						if(member.getMeetingId() == id &&meetingMember!=null){ //参会人的id和存储的id相同，说明这个参会人在集合中出现了两次，去重
//							members.remove(meetingMember);
//						}
//=======
					if (member.getMemberType() == 2) {// 如果是创建人
						id = member.getMeetingId();// 存储创建人id
						meetingMember = member;
					} else {
						if (member.getMeetingId() == id && meetingMember != null) { // 参会人的id和存储的id相同，说明这个参会人在集合中出现了两次，去重
							members.remove(meetingMember);
						}
					}

				}
			}
			// }
			adapter = new GridViewMeetingAttendeesAdapter(this, members, attendeeType);
			gridView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// ENavigate.startUserDetailsActivity(AttendeesActivity.this,
					// "" + members.get(position).getMemberId(), true,
					// ENavConsts.type_details_other);
					ENavigate.startRelationHomeActivity(AttendeesActivity.this, "" + members.get(position).getMemberId());
				}
			});
		}
		gridView.setAdapter(adapter);

		setListener();
	}

	private void setListener() {
		backBtn.setOnClickListener(this);
	}

	@Override
	public void initData() {
		if (meeting != null && members != null) {
			// HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(),
			// "参会人", false, null, true);
			titleTv.setText("参会人");
			if (members.size() == 0) {
				gridView.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.hy_layoutTitle_backBtn:
			finish();
			break;
		}
	}

}
