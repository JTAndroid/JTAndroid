package com.tr.ui.conference.home;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tr.App;
import com.tr.R;
import com.tr.api.ConferenceReqUtil;
import com.tr.model.SimpleResult;
import com.tr.model.conference.MMeetingNoticeQuery;
import com.tr.model.conference.MMeetingNoticeRelation;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.time.TimeUtil;
import com.utils.time.Util;

/**
 * 新版——会议通知
 * 
 * @author zhongshan
 */
public class MeetingNoticeDetialsActivity extends JBaseActivity implements IBindData {

	private boolean updateNoticeStatus;
	private boolean updateNoticeList;
	private int position;
	private Context mContext;
	private ActionBar actionBar;
	private ListView listView;
	private ArrayList<MMeetingNoticeQuery> mMeetingNoticeQueryList;
	private MeetingNoticeDetialsAdapter adapter;
	private ArrayList<MMeetingNoticeRelation> meetingNoticeRelationList;
	private long meetingId;
	private long noticeId;

	@Override
	public void initJabActionBar() {
		actionBar = getActionBar();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hy_meeting_notice_detial_activity_layout);
		getParamers();
		listView = (ListView) findViewById(R.id.listView);
		adapter = new MeetingNoticeDetialsAdapter();
		listView.setAdapter(adapter);
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (meetingId != 0) {
			/** 更新通知为已读 */
			showLoadingDialog();
			ConferenceReqUtil.doUpdateNoticeReadState(mContext, this, meetingId, noticeId, null);
		}
	}

	private void getParamers() {
		this.mContext = this;
		MMeetingNoticeRelation mMeetingNoticeRelation = (MMeetingNoticeRelation) getIntent().getSerializableExtra("MMeetingNoticeRelation");
		position = getIntent().getIntExtra("position", -1);
		HomeCommonUtils.initLeftCustomActionBar(this, actionBar, mMeetingNoticeRelation.getMeetingName(), false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
		mMeetingNoticeQueryList = new ArrayList<MMeetingNoticeQuery>();
		if (mMeetingNoticeRelation != null && mMeetingNoticeRelation.getMeetingNoticeQuery() != null && mMeetingNoticeRelation.getMeetingNoticeQuery().size() > 0) {
			meetingId = mMeetingNoticeRelation.getMeetingId();
			TreeSet<MMeetingNoticeQuery> meetingNoticeQueryTreeSet = mMeetingNoticeRelation.getMeetingNoticeQuery();
			mMeetingNoticeQueryList = treeSetToArrayList(meetingNoticeQueryTreeSet);
		}
	}

	/** treeset转为ArrayList */
	private ArrayList<MMeetingNoticeQuery> treeSetToArrayList(TreeSet<MMeetingNoticeQuery> meetingNoticeQueryTreeSet) {
		Iterator<MMeetingNoticeQuery> iteratorSet = meetingNoticeQueryTreeSet.iterator();
		ArrayList<MMeetingNoticeQuery> mMeetingNoticeQueryList = new ArrayList<MMeetingNoticeQuery>();
		while (iteratorSet.hasNext()) {
			MMeetingNoticeQuery meetingNoticeQuery = iteratorSet.next();
			if (meetingNoticeQuery != null) {
				mMeetingNoticeQueryList.add(meetingNoticeQuery);
			}
		}
		return mMeetingNoticeQueryList;

	}

	private class MeetingNoticeDetialsAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return mMeetingNoticeQueryList.size();
		}

		@Override
		public MMeetingNoticeQuery getItem(int position) {
			return mMeetingNoticeQueryList != null && mMeetingNoticeQueryList.size() > 0 ? mMeetingNoticeQueryList.get(position) : null;
		}

		@Override
		public long getItemId(int position) {
			return mMeetingNoticeQueryList != null && mMeetingNoticeQueryList.size() > 0 ? mMeetingNoticeQueryList.get(position).getId() : null;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = View.inflate(mContext, R.layout.hy_meeting_notice_detial_item_layout, null);
				viewHolder = new ViewHolder();
				viewHolder.nameTv = (TextView) convertView.findViewById(R.id.nameTv);
				viewHolder.sexIv = (ImageView) convertView.findViewById(R.id.sexIv);
				viewHolder.doingTv = (TextView) convertView.findViewById(R.id.doingTv);
				viewHolder.sometingTv = (TextView) convertView.findViewById(R.id.sometingTv);
				viewHolder.timeTv = (TextView) convertView.findViewById(R.id.timeTv);
				viewHolder.OperateLl = (LinearLayout) convertView.findViewById(R.id.OperateLl);
				viewHolder.rejectBtn = (Button) convertView.findViewById(R.id.rejectBtn);
				viewHolder.acceptBtn = (Button) convertView.findViewById(R.id.acceptBtn);
				convertView.setTag(viewHolder);
			}
			viewHolder = (ViewHolder) convertView.getTag();
			MMeetingNoticeQuery mMeetingNoticeQuery = mMeetingNoticeQueryList.get(position);
			if (mMeetingNoticeQuery != null) {
				final Long memberId = mMeetingNoticeQuery.getAttendMeetingId();
				Integer noticeType = mMeetingNoticeQuery.getNoticeType();
				viewHolder.nameTv.setText(App.getUserID().equals(mMeetingNoticeQuery.getCreateId() + "") ? "您" : mMeetingNoticeQuery.getCreateName());
				if (mMeetingNoticeQuery.getCreateSex() == 2) {
					viewHolder.sexIv.setImageResource(R.drawable.sex_women);
				}
				String doSomeThing = formatDoSomeThing(mMeetingNoticeQuery.getListMeetingField(), ",");
				viewHolder.timeTv.setText(TimeUtil.TimeFormat(mMeetingNoticeQuery.getUpdateTime()));
				viewHolder.sometingTv.setText(doSomeThing);
				viewHolder.rejectBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (isLoadingDialogShowing()) {
							return;
						}
						showLoadingDialog();
						ConferenceReqUtil.doSignUpReview(MeetingNoticeDetialsActivity.this, MeetingNoticeDetialsActivity.this, meetingId, memberId, 2, null);
					}
				});
				viewHolder.acceptBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (isLoadingDialogShowing()) {
							return;
						}
						showLoadingDialog();
						ConferenceReqUtil.doSignUpReview(MeetingNoticeDetialsActivity.this, MeetingNoticeDetialsActivity.this, meetingId, memberId, 1, null);
					}
				});

				viewHolder.OperateLl.setVisibility(View.GONE);

				/**
				 * 0：修改了会议，1：申请报名，2：通过了您的报名，3：拒绝了您的报名，4：接受了您的邀请，5：拒绝了您的邀请，6：同意了（
				 * 报名），7：拒绝了（报名），8：取消参会，9：新增了议题，10：修改了议题，11：删除了议题,12：退出了会议，13：
				 * 取消了报名,14：删除了会议
				 */
				switch (noticeType) {
				case 0:
					viewHolder.doingTv.setText("修改了会议");
					break;
				case 1:
					viewHolder.doingTv.setText("申请报名");
					viewHolder.OperateLl.setVisibility(View.VISIBLE);
					break;
				case 2:
					viewHolder.doingTv.setText("通过了您的报名");
					break;
				case 3:
					viewHolder.doingTv.setText("拒绝了您的报名");
					break;
				case 4:
					viewHolder.doingTv.setText("接受了您的邀请");
					break;
				case 5:
					viewHolder.doingTv.setText("拒绝了您的邀请");
					break;
				case 6:
					viewHolder.doingTv.setText("同意了");
					viewHolder.sometingTv.setText(mMeetingNoticeQuery.getAttendMeetingName() + "的报名");
					break;
				case 7:
					viewHolder.doingTv.setText("拒绝了");
					viewHolder.sometingTv.setText(mMeetingNoticeQuery.getNoticeContent() + "的报名");
					break;
				case 8:
					viewHolder.doingTv.setText("取消了参会");
					break;
				case 9:
					viewHolder.doingTv.setText("新增了议题");
					break;
				case 10:
					viewHolder.doingTv.setText("修改了议题");
					break;
				case 11:
					viewHolder.doingTv.setText("删除了议题");
					break;
				case 12:
					viewHolder.doingTv.setText("退出了会议");
					break;
				case 13:
					viewHolder.doingTv.setText("取消了报名");
					break;
				case 14:
					viewHolder.doingTv.setText("删除了会议");
					break;
				}

			}
			return convertView;
		}
	}

	class ViewHolder {
		/** 姓名 */
		TextView nameTv;
		/** 性别 */
		ImageView sexIv;
		/** 做了XXX对应12种类型 */
		TextView doingTv;
		/** 什么事（具体的操作） */
		TextView sometingTv;
		/** 时间 */
		TextView timeTv;
		/** 接受Or拒绝布局 */
		LinearLayout OperateLl;
		/** 拒绝 */
		Button rejectBtn;
		/** 接受 */
		Button acceptBtn;
	}

	private String formatDoSomeThing(List<String> listMeetingField, String style) {
		if (Util.isNull(listMeetingField) || Util.isNull(listMeetingField)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < listMeetingField.size(); i++) {
			if (i == listMeetingField.size() - 1) {
				sb.append(listMeetingField.get(i));
			} else {
				sb.append(listMeetingField.get(i) + style);
			}
		}
		return sb.toString();
	}

	@Override
	public void bindData(int tag, Object object) {
		dismissLoadingDialog();
		if (object == null) {
			return;
		}
		if (tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_UPDATE_NOTICE_READSTATE) {
			boolean b = (Boolean) object;
			if (b) {
				updateNoticeStatus = true;
			}
		} else if (tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_SIGNUPREVIEW) {
			SimpleResult flag = (SimpleResult) object;
			if (null != flag) {
				if (flag.isSucceed()) {
					ConferenceReqUtil.doGetMeetingNoticeList(this, this, App.getUserID(), null);
				} else {
					showToast("提交失败");
				}
			}
		} else if (tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_MY_NOYICE && null != object) {
			meetingNoticeRelationList = (ArrayList<MMeetingNoticeRelation>) object;
			if (null == meetingNoticeRelationList || meetingNoticeRelationList.size() <= 0) {
				return;
			}
			for (MMeetingNoticeRelation mMeetingNoticeRelation : meetingNoticeRelationList) {
				if (meetingId == mMeetingNoticeRelation.getMeetingId()) {
					TreeSet<MMeetingNoticeQuery> meetingNoticeQuery = mMeetingNoticeRelation.getMeetingNoticeQuery();
					ArrayList<MMeetingNoticeQuery> treeSetToArrayList = treeSetToArrayList(meetingNoticeQuery);
					mMeetingNoticeQueryList.clear();
					mMeetingNoticeQueryList.addAll(treeSetToArrayList);
					adapter.notifyDataSetChanged();
					updateNoticeList = true;
					break;
				}
			}
		}
	}

	@Override
	public void onBackPressed() {
		setResultData();
		super.onBackPressed();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		setResultData();
		return super.onOptionsItemSelected(item);
	}

	private void setResultData() {
		Intent intent = new Intent();
		
		/**只要进行了拒绝和同意参会操作，(就一定会议更新通知为已读状态)，就传递整个集合*/
		if (updateNoticeList) {
			intent.putExtra("MeetingNoticeRelationList", meetingNoticeRelationList);
		}
		/**只有更新过通知状态，且，未进行拒绝和同意参会操作时，才传递position*/
		else if (updateNoticeStatus) {
			intent.putExtra("position", position);
		}
		setResult(RESULT_OK, intent);
	}
}