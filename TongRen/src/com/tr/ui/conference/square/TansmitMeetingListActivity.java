package com.tr.ui.conference.square;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tr.R;
import com.tr.api.ConferenceReqUtil;
import com.tr.model.conference.MListMeetingQuery;
import com.tr.model.conference.MMeetingQuery;
import com.tr.model.obj.JTFile;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.adapter.conference.ExpListViewMeetingHomeTransmitAdapter;
import com.tr.ui.conference.common.BaseActivity;
import com.tr.ui.widgets.EProgressDialog;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

public class TansmitMeetingListActivity extends BaseActivity implements OnClickListener, IBindData {

	@ViewInject(R.id.hy_transmit_meeting_home_titlebar)
	private FrameLayout transmitTitle;
	private LinearLayout backBtn;

	@ViewInject(R.id.meeting_home_elv)
	private ExpandableListView meetingHomeLv;

	private ExpListViewMeetingHomeTransmitAdapter mMeetingHomeListAdapter = null;
	private List<MMeetingQuery> meetingQuerys;
	private ArrayList<JTFile> jtFiles;
	private EProgressDialog prgDialog;
	private long meetingId;

	@Override
	public void initView() {
		setContentView(R.layout.hy_activity_transmit2meeting);
		ViewUtils.inject(this);
		((TextView) transmitTitle.findViewById(R.id.hy_layoutTitle_title)).setText("转发到");
		backBtn = ((LinearLayout) transmitTitle.findViewById(R.id.hy_layoutTitle_backBtn));
		prgDialog = new EProgressDialog(this);
		prgDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {

			}
		});
		prgDialog.setMessage("加载中...");
		prgDialog.setCancelable(false);
		prgDialog.setCanceledOnTouchOutside(false);
		prgDialog.show();
		backBtn = ((LinearLayout) transmitTitle.findViewById(R.id.hy_layoutTitle_backBtn));
		Intent transIntent = getIntent();
		jtFiles = (ArrayList<JTFile>) transIntent.getSerializableExtra(ENavConsts.EJTFile);
		meetingId = transIntent.getLongExtra(ENavConsts.EMeetingId, 0);
		ConferenceReqUtil.doGetMyForwardingMeeting(TansmitMeetingListActivity.this, TansmitMeetingListActivity.this, null);

	}

	@Override
	public void initData() {
		// 返回键
		backBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.hy_layoutTitle_backBtn:
			finish();
			break;
		}
	}

	@Override
	public void bindData(int tag, Object object) {
		if (tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_GETMYFORWARDINGMEETING && null != object) {
			MListMeetingQuery mListMeetingQuery = (MListMeetingQuery) object;
			meetingQuerys = mListMeetingQuery.getListMeetingQuery();
			prgDialog.dismiss();
			init();
		}
	}

	private void init() {
		mMeetingHomeListAdapter = new ExpListViewMeetingHomeTransmitAdapter(this, meetingQuerys, meetingId);
		meetingHomeLv.setGroupIndicator(null);
		meetingHomeLv.setAdapter(mMeetingHomeListAdapter);
		if (meetingQuerys != null && meetingQuerys.size() > 0) {
			if(meetingQuerys.get(0).getMeetingType() != 0){
			meetingHomeLv.expandGroup(0);
			}
		}
		else{
			AlertDialog.Builder dialog=new Builder(this);
			dialog.setMessage("您还没有参加的会议哦！");
			
			dialog.setNegativeButton("确定",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					
				}
			});
			dialog.show();
			//Toast.makeText(this, "您还没有参加的会议哦！", 0).show();
		}

		meetingHomeLv.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				if (meetingQuerys.get(groupPosition).getMeetingType() == 0) {
					// TODO
					ENavigate.startMeetingChatActivity(TansmitMeetingListActivity.this, meetingQuerys.get(groupPosition),
							meetingQuerys.get(groupPosition).getListMeetingTopicQuery().get(0), jtFiles);
				} else {
					if (meetingHomeLv.isGroupExpanded(groupPosition)) {
						meetingHomeLv.collapseGroup(groupPosition);
					} else {
						meetingHomeLv.expandGroup(groupPosition);
					}
				}
				return true;
			}
		});

		meetingHomeLv.setOnChildClickListener(new OnChildClickListener() {

			// private EditText content;

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, final int groupPosition, final int childPosition, long id) {
				// final Dialog dialog = new
				// Dialog(TansmitMeetingListActivity.this,
				// R.style.transmeeting_dialog);
				// RelativeLayout dialogView = (RelativeLayout)
				// View.inflate(TansmitMeetingListActivity.this,
				// R.layout.hy_dialog_meeting_regist_transmit,
				// null);
				// content = (EditText)
				// dialogView.findViewById(R.id.content_et);
//				 ((TextView)
//				 dialogView.findViewById(R.id.cancel_btn)).setOnClickListener(new
//				 OnClickListener() {
//				 @Override
//				 public void onClick(View v) {
//				 dialog.dismiss();
//				 }
//				 });
//				 ((TextView)
//				 dialogView.findViewById(R.id.confirm_btn)).setOnClickListener(new
//				 OnClickListener() {
//				
//				 @Override
//				 public void onClick(View v) {
				// // TODO 确定键
				//
				// if (null != meetingQuerys) {
				// if (null != meetingQuerys.get(groupPosition)) {
				// List<MMeetingTopic> aList =
				// meetingQuerys.get(groupPosition).getListMeetingTopic();
				// if (null != aList) {
				// if (aList.size() > 0) {
				// MMeetingTopic aTopic = aList.get(0);
				// if (null != aTopic) {
				//
				// jtFile.mFileName = content.getText().toString();
				//
				// Intent intent = new Intent(TansmitMeetingListActivity.this,
				// MeetingChatActivity.class);
				// intent.putExtra(ENavConsts.EMeetingDetail,
				// meetingQuerys.get(groupPosition));
				// intent.putExtra(ENavConsts.EMeetingTopicDetail, aTopic);
				// startActivity(intent);
				// }
				// }
				// }
				// }
				// }
				//
				// Toast.makeText(TansmitMeetingListActivity.this, "转发成功",
				// 0).show();
				// dialog.dismiss();
				// }
				// });
				// LayoutParams params = new
				// LayoutParams(LayoutParams.WRAP_CONTENT,
				// LayoutParams.WRAP_CONTENT);
				// dialog.setContentView(dialogView, params);
				// dialog.show();
				ENavigate.startMeetingChatActivity(TansmitMeetingListActivity.this, meetingQuerys.get(groupPosition), meetingQuerys
						.get(groupPosition).getListMeetingTopicQuery().get(childPosition), jtFiles);
				return true;
			}
		});
	}
}
