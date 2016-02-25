package com.tr.ui.conference.initiatorhy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.App;
import com.tr.R;
import com.tr.model.conference.MCalendarSelectDateTime;
import com.tr.model.conference.MMeetingTopicQuery;
import com.tr.model.obj.JTContactMini;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.conference.utile.PeopleOrgknowleRequirmentLayoutUtil;
import com.tr.ui.home.MainActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.string.StringUtils;
import com.utils.time.Util;

public class MeetingSpeakerTopicSettingActivity extends JBaseActivity implements OnClickListener {

	private Context mContext;
	private ImageView speakerPicIv;
	private TextView speakerNameTv;
	private TextView addSpeakTopicBt;
	private List<MMeetingTopicQuery> lisMeetingTopicQuery;
	private ListView speakerTopicListView;
	private final int TOPIC_NAME_RESULT_CODE = 10;
	private final int TOPIC_TIME_RESULT_CODE = 11;
	private final int TOPIC_DESC_RESULT_CODE = 12;
	private SpeakerTopicListAdaper adapter;
	private int currentClickPosition = -1;
	private JTContactMini speakerContact;

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "主讲人设置", false, null,true, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.hy_speaker_topic_setting_activity);
		initView();
		initData();
	}

	private void initView() {
		speakerPicIv = (ImageView) findViewById(R.id.speakerPicIv);
		speakerNameTv = (TextView) findViewById(R.id.speakerNameTv);
		addSpeakTopicBt = (TextView) findViewById(R.id.addSpeakTopicBt);
		speakerTopicListView = (ListView) findViewById(R.id.speakerTopicListView);
	}

	private void initData() {
		speakerContact = (JTContactMini) getIntent().getSerializableExtra("JTContactMini");
		if (speakerContact != null) {
			lisMeetingTopicQuery = speakerContact.lisMeetingTopicQuery;
			com.utils.common.Util.initAvatarImage(mContext, speakerPicIv,speakerContact.name, speakerContact.image, speakerContact.getGender(),1);
			speakerNameTv.setText(speakerContact.name);
		}
		if (lisMeetingTopicQuery != null && lisMeetingTopicQuery.size() == 0) {// 如果主讲人没有设置过议题，那么给他初始化一个默认议题
			MMeetingTopicQuery meetingTopicQuery = new MMeetingTopicQuery();
			lisMeetingTopicQuery.add(meetingTopicQuery);
		}
		addSpeakTopicBt.setOnClickListener(this);
		adapter = new SpeakerTopicListAdaper();
		adapter.setData(lisMeetingTopicQuery);
		speakerTopicListView.setAdapter(adapter);
	}

	private class SpeakerTopicListAdaper extends BaseAdapter {
		private List<MMeetingTopicQuery> lisMeetingTopicQuery;

		private void setData(List<MMeetingTopicQuery> lisMeetingTopicQuery) {
			this.lisMeetingTopicQuery = lisMeetingTopicQuery;
		}

		public SpeakerTopicListAdaper() {
			super();
		}

		@Override
		public int getCount() {
			return lisMeetingTopicQuery.size();
		}

		@Override
		public Object getItem(int position) {
			return lisMeetingTopicQuery.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = View.inflate(mContext, R.layout.hy_speaker_topic_item_layout, null);
				viewHolder = new ViewHolder();
				viewHolder.topicNameTv = (TextView) convertView.findViewById(R.id.topicNameTv);
				viewHolder.topicTimeTv = (TextView) convertView.findViewById(R.id.topicTimeTv);
				viewHolder.topicDescTv = (TextView) convertView.findViewById(R.id.topicDescTv);
				viewHolder.topicNameRl = (RelativeLayout) convertView.findViewById(R.id.topicNameRl);
				viewHolder.topicTimeRl = (RelativeLayout) convertView.findViewById(R.id.topicTimeRl);
				viewHolder.topicDescRl = (RelativeLayout) convertView.findViewById(R.id.topicDescRl);
				viewHolder.deleteSpeakTopicBt = (TextView) convertView.findViewById(R.id.deleteSpeakTopicBt);
				convertView.setTag(viewHolder);
			}
			viewHolder = (ViewHolder) convertView.getTag();
			MMeetingTopicQuery mMeetingTopicQuery = lisMeetingTopicQuery.get(position);
			viewHolder.topicNameTv.setText(mMeetingTopicQuery.getTopicContent());
			if (!StringUtils.isEmpty(mMeetingTopicQuery.getTopicStartTime()) && !StringUtils.isEmpty(mMeetingTopicQuery.getTopicEndTime())) {
				MCalendarSelectDateTime sdt = IniviteUtil.getMSDT(mMeetingTopicQuery);
				viewHolder.topicTimeTv.setText(IniviteUtil.formatDateMeeting(sdt));
			} else {
				viewHolder.topicTimeTv.setText("");
			}
			viewHolder.topicDescTv.setText(mMeetingTopicQuery.getTopicDesc());
			if (position == 0) {
				viewHolder.deleteSpeakTopicBt.setVisibility(View.GONE);
			} else {
				viewHolder.deleteSpeakTopicBt.setVisibility(View.VISIBLE);
			}
			OnTopicClickListener onTopicClickListener = new OnTopicClickListener(mMeetingTopicQuery, position);
			viewHolder.topicNameRl.setOnClickListener(onTopicClickListener);
			viewHolder.topicTimeRl.setOnClickListener(onTopicClickListener);
			viewHolder.topicDescRl.setOnClickListener(onTopicClickListener);
			viewHolder.deleteSpeakTopicBt.setOnClickListener(onTopicClickListener);
			return convertView;
		}
	}

	private class OnTopicClickListener implements OnClickListener {
		private MMeetingTopicQuery mMeetingTopicQuery;
		private int position;

		public OnTopicClickListener(MMeetingTopicQuery mMeetingTopicQuery, int position) {
			super();
			this.mMeetingTopicQuery = mMeetingTopicQuery;
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.topicNameRl:
				currentClickPosition = position;
				ENavigate.startEditTextContentActivity(MeetingSpeakerTopicSettingActivity.this, mMeetingTopicQuery.getTopicContent(), TOPIC_NAME_RESULT_CODE);
				break;
			case R.id.topicTimeRl:
				currentClickPosition = position;
				InitiatorDataCache.getInstance().dateSelectetedTempList.clear();
				MCalendarSelectDateTime sdt = IniviteUtil.getMSDT(mMeetingTopicQuery);
				if (sdt != null) {
					InitiatorDataCache.getInstance().dateSelectetedTempList.add(sdt);
				}
				Bundle b = new Bundle();
				b.putInt(Util.IK_VALUE, 1);
				Util.forwardTargetActivityForResult(MeetingSpeakerTopicSettingActivity.this, CalendarActivity.class, b, TOPIC_TIME_RESULT_CODE);
				break;
			case R.id.topicDescRl:
				currentClickPosition = position;
				Intent intent = new Intent();
				intent.putExtra("mMeetingTopicQuery", mMeetingTopicQuery);
				intent.setClass(MeetingSpeakerTopicSettingActivity.this, ConferenceIntroduceActivity.class);
				startActivityForResult(intent, TOPIC_DESC_RESULT_CODE);
				break;
			case R.id.deleteSpeakTopicBt:
				lisMeetingTopicQuery.remove(position);
				adapter.setData(lisMeetingTopicQuery);
				adapter.notifyDataSetChanged();
				break;
			}
		}

	}

	private class ViewHolder {
		TextView topicNameTv;
		TextView topicTimeTv;
		TextView topicDescTv;
		TextView deleteSpeakTopicBt;
		RelativeLayout topicNameRl;
		RelativeLayout topicTimeRl;
		RelativeLayout topicDescRl;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (/* resultCode == RESULT_OK && */data != null) {
			MMeetingTopicQuery mMeetingTopicQuery = null;
			if (currentClickPosition != -1) {
				mMeetingTopicQuery = lisMeetingTopicQuery.get(currentClickPosition);
				if (mMeetingTopicQuery != null) {
					switch (requestCode) {
					case TOPIC_NAME_RESULT_CODE:
						String stringExtra = data.getStringExtra("editTextInfo");
						mMeetingTopicQuery.setTopicContent(stringExtra);
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
						MMeetingTopicQuery topicQuery = (MMeetingTopicQuery) data.getSerializableExtra("mMeetingTopicQuery");
						if (topicQuery != null) {
							lisMeetingTopicQuery.set(currentClickPosition, topicQuery);
						}
						break;
					}
				}
			}
			currentClickPosition = -1;
			adapter.notifyDataSetChanged();
		}
	}

	

	@Override
	public void onClick(View v) {
		MMeetingTopicQuery meetingTopicQuery = new MMeetingTopicQuery();
		lisMeetingTopicQuery.add(meetingTopicQuery);
		adapter.setData(lisMeetingTopicQuery);
		adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem item = menu.add(0, 101, 0, "完成");
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (101 == item.getItemId()) {
			for (int i = 0; i < lisMeetingTopicQuery.size(); i++) {
				MMeetingTopicQuery mMeetingTopicQuery = lisMeetingTopicQuery.get(i);
				if (StringUtils.isEmpty(mMeetingTopicQuery.getTopicContent())) {
					showToast("请填写议题名称");
					return true;
				}else {
					mMeetingTopicQuery.setCreateId(Long.valueOf(App.getUserID()));
					mMeetingTopicQuery.setCreateName(App.getUserName());
					mMeetingTopicQuery.setMemberId(Long.valueOf(speakerContact.getId()));
					mMeetingTopicQuery.setMemberName(speakerContact.getName());
					mMeetingTopicQuery.setMemberPic(speakerContact.getImage());
					mMeetingTopicQuery.setMemberDesc(speakerContact.getCompany() + speakerContact.getCareer());
				}
			}
			speakerContact.lisMeetingTopicQuery = lisMeetingTopicQuery;
			Intent intent = new Intent(this,MeetingSpeakerListActivity.class);
			intent.putExtra("speakerContact", speakerContact);
			setResult(RESULT_OK, intent);
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

}
