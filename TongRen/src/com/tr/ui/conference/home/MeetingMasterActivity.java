package com.tr.ui.conference.home;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.App;
import com.tr.R;
import com.tr.api.ConferenceReqUtil;
import com.tr.baidumapsdk.BaiduLoc;
import com.tr.baidumapsdk.BaiduNavi;
import com.tr.model.SimpleResult;
import com.tr.model.conference.MMeetingDetail;
import com.tr.model.conference.MMeetingMember;
import com.tr.model.conference.MMeetingQuery;
import com.tr.model.conference.MMeetingTopicQuery;
import com.tr.model.conference.MTopicChat;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.conference.common.BaseActivity;
import com.tr.ui.conference.im.MeetingChatActivity;
import com.tr.ui.conference.square.KnowLedgeDataActivity;
import com.tr.ui.conference.square.RelationshipActivity;
import com.tr.ui.conference.square.RequirmentDataActivity;
import com.tr.ui.widgets.CircleImageView;
import com.tr.ui.widgets.SmileyParser;
import com.tr.ui.widgets.SmileyParser2;
import com.utils.common.EConsts;
import com.utils.common.GlobalVariable;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;

/**
 * 主会场
 * 
 * @author d.c at 2014/11/10
 */

public class MeetingMasterActivity extends BaseActivity implements IBindData {
	
	private Context context;
	
	// 标题栏
	private LinearLayout mIvBackButton = null;
	private TextView mTvTitle = null;
	private ImageView mIvAttendButton = null;
	@ViewInject(R.id.meeting_master_ll)
	private LinearLayout meetingMasterTitle;
	private ScrollView sv_main = null;
	/**	主会场图片	*/
	private ImageView iv_logo = null;
	private TextView tv_title = null;
	private TextView tv_leader = null;
	private TextView tv_desc = null;
	private ProgressBar pb_checkin = null;
	private TextView tv_checkin = null;
	private Button btn_checkin = null;
	private ImageView iv_cantact = null;
	private ImageView iv_knowledge = null;
	private ImageView iv_requirment = null;
	private ImageView iv_more = null;
	private ImageView iv_note = null;
	/**	会议议题列表根布局	*/
	private LinearLayout ll_topic_session = null;
	private LinearLayout ll_join = null;
	private TextView tv_end_time = null;
	private Button btn_end = null;
	/**	会议议题列表布局	*/
	private LinearLayout ll_topic = null;
	/** 会议详情返回的数据	*/
	private MMeetingQuery mMeetingQuery = null;
	private MMeetingTopicQuery mMeetingTopic = null;

	private ImageLoader mImageLoader = null;

	private int mMid = 0;

	public static int CALL_MEETING_BRANCH = 0x10000003;
	private RelativeLayout hy_meeting_master_rm=null;
	private RelativeLayout hy_meeting_master_xq=null;
	private RelativeLayout hy_meeting_master_zs=null;
	private RelativeLayout hy_meeting_master_hyxg=null;
	private ImageView hy_meeting_master_iv_note=null;
	private ImageView hy_home_tile_meeting_iv_navigation=null;
	private ImageView hy_home_tile_meeting_iv_alarm=null;
	private BaiduNavi mBaiduNavi = null;
	private RelativeLayout hy_home_tile_meeting_rl_alarm=null;
	private RelativeLayout hy_home_tile_meeting_rl_navigation=null;
	
	private boolean meetingTopicDataChanged ;

	private int totalCount;

	private int signCount;
	
	Context getContext(){
		return context;
	}
	
	
	@Override
	public void initView() {
		setContentView(R.layout.hy_activity_meeting_master);
		mIvBackButton = (LinearLayout) findViewById(R.id.hy_layoutTitle_backBtn);
		mTvTitle = (TextView) findViewById(R.id.hy_layoutTitle_title);
		mIvAttendButton = (ImageView) findViewById(R.id.hy_layoutTitle_rightIconBtn);
		ViewUtils.inject(this);
		sv_main = (ScrollView) findViewById(R.id.hy_meeting_master_sl_main);
		iv_logo = (ImageView) findViewById(R.id.hy_meeting_master_iv_logo);
		tv_title = (TextView) findViewById(R.id.hy_meeting_master_tv_title);
		tv_leader = (TextView) findViewById(R.id.hy_meeting_master_tv_leader);
		tv_desc = (TextView) findViewById(R.id.hy_meeting_master_tv_desc);
		pb_checkin = (ProgressBar) findViewById(R.id.hy_meeting_master_pb_checkin);
		tv_checkin = (TextView) findViewById(R.id.hy_meeting_master_tv_checkin);
		btn_checkin = (Button) findViewById(R.id.hy_meeting_master_btn_checkin);
		iv_cantact = (ImageView) findViewById(R.id.hy_meeting_master_iv_cantact);
		iv_knowledge = (ImageView) findViewById(R.id.hy_meeting_master_iv_knowledge);
		iv_requirment = (ImageView) findViewById(R.id.hy_meeting_master_iv_requirment);
		iv_more = (ImageView) findViewById(R.id.hy_meeting_master_iv_more);
		iv_note = (ImageView) findViewById(R.id.hy_meeting_master_iv_note);
		ll_topic_session = (LinearLayout) findViewById(R.id.hy_meeting_master_ll_topic_session);
		ll_join = (LinearLayout) findViewById(R.id.hy_meeting_master_ll_join_end);
		tv_end_time = (TextView) findViewById(R.id.hy_meeting_master_tv_end_time);
		btn_end = (Button) findViewById(R.id.hy_meeting_master_btn_end);
		ll_topic = (LinearLayout) findViewById(R.id.hy_meeting_master_ll_topic);
	    hy_meeting_master_rm=(RelativeLayout) findViewById(R.id.hy_meeting_master_rm);
		hy_meeting_master_xq=(RelativeLayout) findViewById(R.id.hy_meeting_master_xq);
		hy_meeting_master_zs=(RelativeLayout) findViewById(R.id.hy_meeting_master_zs);
		hy_meeting_master_hyxg=(RelativeLayout) findViewById(R.id.hy_meeting_master_hyxg);
		hy_meeting_master_iv_note=(ImageView) findViewById(R.id.hy_meeting_master_iv_note);
		
		hy_home_tile_meeting_rl_alarm=(RelativeLayout) findViewById(R.id.hy_home_tile_meeting_rl_alarm);
		hy_home_tile_meeting_rl_navigation=(RelativeLayout) findViewById(R.id.hy_home_tile_meeting_rl_navigation);
		findViewById(R.id.hy_meeting_master_dotlint_checkin).setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		mImageLoader = ImageLoader.getInstance();
	}

	@Override
	public void initData() {
		mBaiduNavi = new BaiduNavi((MeetingMasterActivity) this);
		mMid = (int) getIntent().getLongExtra("meeting_id", 0);
		mTvTitle.setText("主会场");
		mIvAttendButton.setBackgroundResource(R.drawable.hy_selector_ic_action_relation);
		ll_topic_session.setVisibility(View.VISIBLE);
		ll_join.setVisibility(View.GONE);
		sv_main.setVisibility(View.GONE);
		//hy_home_tile_meeting_rl_alarm.setVisibility(View.GONE);
		//hy_home_tile_meeting_rl_navigation.setVisibility(View.GONE);
		hy_meeting_master_rm.setVisibility(View.GONE);
		hy_meeting_master_xq.setVisibility(View.GONE);
		hy_meeting_master_zs.setVisibility(View.GONE);
		hy_meeting_master_hyxg.setVisibility(View.GONE);
		showLoadingDialog();
		ConferenceReqUtil.doGetMeetingDetail(this, this, mMid, App.getUserID(), null);
	
		mIvBackButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (meetingTopicDataChanged) {
					setBackResult();
				}
				finish();
			}
		});
		mIvAttendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MeetingMasterActivity.this, MeetingAttendInfomationActivity.class);
				intent.putExtra(ENavConsts.EMeetingDetail, mMeetingQuery);
				startActivityForResult(intent, EConsts.CALL_MEETING_ATTEND_INFOMATION);
			}
//			@Override
//			public void onClick(View v) {
//				if (null != mMeetingQuery) {
//					Intent intent = new Intent(MeetingMasterActivity.this, AttendeesActivity.class);
//					intent.putExtra(ENavConsts.EMeetingDetail, mMeetingQuery);
//					// 参会人的情况 0代表 主会场中的参会人 1代表其他的参会人
//					intent.putExtra("AttendeeType", 0);
//					startActivity(intent);
//				}
//			}
		});
		
		btn_checkin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BaiduLoc aLoc = App.getApp().getBaiduLoc();
				if (aLoc.isLocationValid(aLoc.getLongitude(), aLoc.getLatitude())) {
					double endLatitude = 0;
					double endLongitude = 0;
					if (null != mMeetingQuery.getMeetingAddressPosY()) {
						if (false == mMeetingQuery.getMeetingAddressPosY().isEmpty()) {
							endLatitude = Double.valueOf(mMeetingQuery.getMeetingAddressPosY());
						}
					}
					if (null != mMeetingQuery.getMeetingAddressPosX()) {
						if (false == mMeetingQuery.getMeetingAddressPosX().isEmpty()) {
							endLongitude = Double.valueOf(mMeetingQuery.getMeetingAddressPosX());
						}
					}
					if (aLoc.isLocationValid(endLongitude, endLatitude)) {
						int distance = (int) DistanceUtil.getDistance(new LatLng(aLoc.getLatitude(), aLoc.getLongitude()), new LatLng(endLatitude, endLongitude));
						if (distance < 2000) {
							String dis = distance + "m";
							showLoadingDialog();
							ConferenceReqUtil.doSignInMeeting(MeetingMasterActivity.this, MeetingMasterActivity.this, mMid, App.getApp().getUserID(), dis, null);
						} else {
							showLoadingDialog();
							ConferenceReqUtil.doSignInMeeting(MeetingMasterActivity.this, MeetingMasterActivity.this, mMid, App.getApp().getUserID(), aLoc.getLocation().getCity(), null);
						}
					} else {
						showLoadingDialog();
						ConferenceReqUtil.doSignInMeeting(MeetingMasterActivity.this, MeetingMasterActivity.this, mMid, App.getApp().getUserID(), aLoc.getLocation().getCity(), null);
					}
				} else {
					showLoadingDialog();
					ConferenceReqUtil.doSignInMeeting(MeetingMasterActivity.this, MeetingMasterActivity.this, mMid, App.getApp().getUserID(), "未知", null);
				}
			}
		});

		iv_cantact.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != mMeetingQuery) {
					Intent intent = new Intent(MeetingMasterActivity.this, RelationshipActivity.class);
					intent.putExtra(ENavConsts.EMeetingDetail, mMeetingQuery);
					startActivity(intent);
				}
			}
		});
		iv_knowledge.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != mMeetingQuery) {
					Intent intent = new Intent(MeetingMasterActivity.this, KnowLedgeDataActivity.class);
					intent.putExtra(ENavConsts.EMeetingDetail, mMeetingQuery);
					startActivity(intent);
				}
			}
		});
		iv_requirment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != mMeetingQuery) {
					Intent intent = new Intent(MeetingMasterActivity.this, RequirmentDataActivity.class);
					intent.putExtra(ENavConsts.EMeetingDetail, mMeetingQuery);
					startActivity(intent);
				}
			}
		});
		hy_home_tile_meeting_rl_alarm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					if (false == mMeetingQuery.getStartTime().isEmpty()) {
						Date tmp = (fmt.parse(mMeetingQuery.getStartTime()));
						Calendar cal = Calendar.getInstance();
						Intent intent = new Intent(Intent.ACTION_EDIT);
						intent.setType("vnd.android.cursor.item/event");
						intent.putExtra("beginTime", tmp.getTime());
						intent.putExtra("allDay", false);
						intent.putExtra("rrule", "FREQ=DAILY");
						intent.putExtra("endTime", tmp.getTime() + 60 * 60 * 1000);
						intent.putExtra("title", mMeetingQuery.getMeetingName());
						intent.putExtra("description", mMeetingQuery.getMeetingDesc());
						intent.putExtra(Events.EVENT_LOCATION, mMeetingQuery.getMeetingAddress());
						startActivity(intent);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		});
		hy_home_tile_meeting_rl_navigation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != mBaiduNavi) {
					BaiduLoc aLoc = App.getApp().getBaiduLoc();
					if (aLoc.isLocationValid(aLoc.getLongitude(), aLoc.getLatitude())) {
						double endLatitude = 0;
						double endLongitude = 0;
						if (null != mMeetingQuery.getMeetingAddressPosY()) {
							endLatitude = Double.valueOf(mMeetingQuery.getMeetingAddressPosY());
						}
						if (null != mMeetingQuery.getMeetingAddressPosX()) {
							endLongitude = Double.valueOf(mMeetingQuery.getMeetingAddressPosX());
						}
						if (aLoc.isLocationValid(endLongitude, endLatitude)) {
							mBaiduNavi.naviGuide(aLoc.getLatitude(), aLoc.getLongitude(), aLoc.getAddress(), endLatitude, endLongitude, mMeetingQuery.getMeetingAddress());
						} else {
							Toast.makeText(MeetingMasterActivity.this, "会议地址未知", 0).show();
						}
					} else {
						Toast.makeText(MeetingMasterActivity.this, "您的地址未知", 0).show();
					}
				}
			}
		});
		iv_more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MeetingMasterActivity.this, MeetingRelativeActivity.class);
				intent.putExtra(ENavConsts.EMeetingDetail, mMeetingQuery);
				startActivity(intent);
			}
		});
		iv_note.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//跳转到会议笔记
//				Intent intent = new Intent(MeetingMasterActivity.this, MeetingNoteActivity.class);
//				intent.putExtra("meeting_id", mMid);
//				intent.putExtra("meeting_id", mMid);
//				startActivity(intent);
				Intent intent = new Intent(MeetingMasterActivity.this, RTMeetingNoteActivity.class);
				intent.putExtra("meeting_id", mMid);
				intent.putExtra("is_edit", true);
				startActivity(intent);
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
				new AlertDialog.Builder(MeetingMasterActivity.this).setTitle("主会场").setMessage("确认结束会议吗").setPositiveButton("取消", lis).setNegativeButton("确定", new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ConferenceReqUtil.doChangeMeetingStatus(MeetingMasterActivity.this, MeetingMasterActivity.this, mMid, 3, null);
						dialog.dismiss();
						showLoadingDialog();
					}
				}).show();
			}
		});
		// by sunjianan
		meetingMasterTitle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ENavigate.startSquareActivity(MeetingMasterActivity.this, mMeetingQuery.getId(),0);
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// add by leon
		// 是否来自推送
		if (getIntent().hasExtra(ENavConsts.EFromPush)) {
			String meetingId = getIntent().getStringExtra(ENavConsts.EMeetingId);
			String topicId = getIntent().getStringExtra(ENavConsts.ETopicId);
			if (!TextUtils.isEmpty(meetingId) && !TextUtils.isEmpty(topicId)) {
				ENavigate.startMeetingChatActivity(this, meetingId, topicId);
			}
		}
		
		context = this;
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	public void bindData(int tag, Object object) {
		try {
		if (EAPIConsts.ConferenceReqType.CONFERENCE_REQ_MEETING_DETAIL == tag && null != object) {
			MMeetingDetail aDetail = (MMeetingDetail) object;
			
			if (null != aDetail) {
				mMeetingQuery = aDetail.getMeeting();
				if (null != mMeetingQuery) {
					fillData();
				}else{
					Toast.makeText(this,"发起者已删除该会议",0).show();
					finish();
					}
			}
			dismissLoadingDialog();
		} else if (EAPIConsts.ConferenceReqType.CONFERENCE_REQ_SIGNINMEETING == tag && null != object) {
			dismissLoadingDialog();
			SimpleResult flag = (SimpleResult) object;
			if (null != flag) {
				if (flag.isSucceed()) {
					Toast.makeText(this, "签到成功", 0).show();
					btn_checkin.setEnabled(false);
					btn_checkin.setText("已签");
					signCount = signCount+1;
					if (null != pb_checkin && totalCount != 0) {
						pb_checkin.setMax(totalCount);
						pb_checkin.setProgress(signCount);
					}
					if (null != tv_checkin) {
						tv_checkin.setText(String.valueOf(signCount) + "/" + String.valueOf(totalCount));
					}
					for (int i = 0; i < mMeetingQuery.getListMeetingMember().size(); i++) {
						MMeetingMember mMeetingMember = mMeetingQuery.getListMeetingMember().get(i);
						if (App.getUserID().equals(mMeetingMember.getMemberId()+"")) {
							mMeetingQuery.getListMeetingMember().get(i).setIsSign(1);
						}
					}
				} else {
					Toast.makeText(this, "签到失败", 0).show();
				}
			}
		} else if (EAPIConsts.ConferenceReqType.CONFERENCE_REQ_CHANGEMEETINGSTATUS == tag && null != object) {
			dismissLoadingDialog();
			SimpleResult flag = (SimpleResult) object;
			if (null != flag) {
				if (flag.isSucceed()) {
					Toast.makeText(this, "会议结束成功", 0).show();
				} else {
					Toast.makeText(this, "会议结束失败", 0).show();
				}
				Intent data = new Intent();
				setResult(RESULT_OK, data);
				finish();
			}
		}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode!=RESULT_OK){
			return;
		}
		if (requestCode == CALL_MEETING_BRANCH) {
			meetingTopicDataChanged = true;
			showLoadingDialog();
			ConferenceReqUtil.doGetMeetingDetail(this, this, mMid, App.getApp().getUserID(), null);
//			Intent data2 = new Intent();
//			setResult(RESULT_OK, data2);
//			finish();
		}
	}
	
	@Override
	public void onBackPressed() {
		if (meetingTopicDataChanged) {
			setBackResult();
		}
		super.onBackPressed();
	}
	
	private void setBackResult() {
		Intent intent = new Intent();
		intent.putExtra("meetingDetial", mMeetingQuery);
		setResult(RESULT_OK, intent);
	}
	/**
	 * 议题列表填充数据
	 */
	private void fillData() {

		if (null == mMeetingQuery) {
			return;
		}

		if (null != iv_logo) {
			if (mMeetingQuery.getPath().isEmpty()) {
				iv_logo.setImageResource(R.drawable.meeting_logo_a);
			} else {
				if (null != mImageLoader) {
					mImageLoader.displayImage(mMeetingQuery.getPath(), iv_logo);
				}
			}
		}
		if (null != tv_title) {
			tv_title.setText(mMeetingQuery.getMeetingName());
		}
		if (null != tv_leader) {
			tv_leader.setText(mMeetingQuery.getCreateName());
		}
		if (null != tv_desc) {
			tv_desc.setText(mMeetingQuery.getMeetingDesc());
		}
		totalCount = initMemberData(mMeetingQuery).size();
//		totalCount = mMeetingQuery.getListMeetingMember().size();;
		if (App.getUserID().equals(mMeetingQuery.getCreateId()+"")) {//创建人是自己
			for (MMeetingMember meetingMember : mMeetingQuery.getListMeetingMember()) {
				if (meetingMember.getMemberType()==0) {//并且自己也是主讲人
					//去重
					totalCount = totalCount-1;
					break;
				}
			}
		}
		signCount = mMeetingQuery.getSignInCount();
//		if (mMeetingQuery.getListMeetingMember()!=null) {
//			totalCount = mMeetingQuery.getListMeetingMember().size();
//			signCount = getAcceptInviteAndSignInMumberCount();
//		}
		if (null != pb_checkin && totalCount != 0) {
			pb_checkin.setMax(totalCount);
			pb_checkin.setProgress(signCount);
		}
		if (null != tv_checkin) {
			tv_checkin.setText(String.valueOf(signCount) + "/" + String.valueOf(totalCount));
		}

		//*********** 	议题列表	 start	**********/
		if (null != ll_topic) {
			ll_topic.removeAllViews();
			List<MMeetingTopicQuery> aList = mMeetingQuery.getListMeetingTopicQuery();
			if (null != aList) {
				int iCnt = aList.size();
				if (iCnt > 0) {
					for (int i = 0; i < iCnt; ++i) {
						final MMeetingTopicQuery aTopic = aList.get(i);
						if (null != aTopic) {
							View aView = LayoutInflater.from(this).inflate(R.layout.hy_list_item_meeting_topic_tile, null);
							/**	分割 view	*/
							View divisionView = aView.findViewById(R.id.divisionView);
							TextView finishedView = (TextView)aView.findViewById(R.id.meeting_topic_finished);
							if (aTopic.getIsFinished()==0) {
								finishedView.setVisibility(View.GONE);
							}else if (aTopic.getIsFinished()==1) {
								finishedView.setVisibility(View.VISIBLE);
							}
							divisionView.setVisibility(View.VISIBLE);
							
							RelativeLayout topicHeadRl = (RelativeLayout) aView.findViewById(R.id.hy_meeting_topic_head_rl);
							topicHeadRl.setOnClickListener(new View.OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// 用户详情跳转  
									ENavigate.startRelationHomeActivity(getContext() , String.valueOf(aTopic.getMemberId()));
								}
							});
							
							
							TextView tvName = (TextView) aView.findViewById(R.id.hy_meeting_topic_tile_tv_name);
							TextView tvTime = (TextView) aView.findViewById(R.id.hy_meeting_topic_tile_tv_time);
							CircleImageView ivAvatar = (CircleImageView) aView.findViewById(R.id.hy_meeting_topic_tile_iv_avatar);
							TextView tvSpeaker = (TextView) aView.findViewById(R.id.hy_meeting_topic_tile_tv_speaker);
							TextView tvCompany = (TextView) aView.findViewById(R.id.hy_meeting_topic_tile_tv_company);
							/**	议题介绍图片	*/
							final ImageView ivLogo = (ImageView) aView.findViewById(R.id.hy_meeting_topic_tile_iv_logo);
							TextView tvContent = (TextView) aView.findViewById(R.id.hy_meeting_topic_tile_tv_content);
							/** 聊天列表	*/
							LinearLayout llChat = (LinearLayout) aView.findViewById(R.id.hy_meeting_topic_tile_ll_chat);
							LinearLayout llChatSession = (LinearLayout) aView.findViewById(R.id.hy_meeting_topic_tile_ll_chat_session);
							LinearLayout llJoin = (LinearLayout) aView.findViewById(R.id.hy_meeting_topic_tile_ll_join);
							LinearLayout llTitle = (LinearLayout) aView.findViewById(R.id.hy_meeting_home_title);
							Button btnJoin = (Button) aView.findViewById(R.id.hy_meeting_topic_tile_btn_join);
							aView.findViewById(R.id.hy_meeting_topic_tile_tv_dotline).setLayerType(View.LAYER_TYPE_SOFTWARE, null);
							View endDivisionLine = aView.findViewById(R.id.endDivisionLine);
							endDivisionLine.setVisibility(View.VISIBLE);
							
							tvName.setText(aTopic.getTopicContent());
							ivLogo.setVisibility(View.GONE);
							
							//******	议题开始及结束时间	start	*********/
							SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							SimpleDateFormat dateOutFormat = new SimpleDateFormat("yyyy年MM月dd日 EEEE HH:mm");
							try {
								if (false == aTopic.getTopicStartTime().isEmpty()) {
									Date startTmp = (fmt.parse(aTopic.getTopicStartTime()));
									Date endTmp = (fmt.parse(aTopic.getTopicEndTime()));
									tvTime.setText(dateOutFormat.format(startTmp) + "  -  " + dateOutFormat.format(endTmp));
								}
								else{
									tvTime.setVisibility(View.GONE);
								}

							} catch (ParseException e) {
								e.printStackTrace();
							}
							//******	议题开始及结束时间	end	*********/

							String img = aTopic.getMemberImage();
							String name = aTopic.getMemberName();
							
							com.utils.common.Util.initAvatarImage(this, ivAvatar, name, img, 0, 1);
							
							/*if (!StringUtils.isEmpty(img)&&!img.endsWith(GlobalVariable.PERSON_DEFAULT_AVATAR)&&!img.endsWith(GlobalVariable.ORG_DEFAULT_AVATAR)&&null != mImageLoader) {
								mImageLoader.displayImage(img, ivAvatar);
							}else {
								String lastchar = "";
								if(!TextUtils.isEmpty(name)){
									lastchar = name.substring(name.length()-1);
								}
								Bitmap bm = null;
								int resid =0;
//								if (type == 1) {
								resid = R.drawable.ic_person_default_avatar_gray;
//								}else if(type == 2){
//									resid = R.drawable.no_avatar_client_organization;
//								}
								bm = com.utils.common.Util.createBGBItmap(this, resid, R.color.avatar_text_color, R.dimen.avatar_text_size, lastchar);
								ivAvatar.setImageBitmap(bm);
							}*/
							
							// 议题介绍图片展示
							String introduceImageUrl = aTopic.getPath();
							if (!StringUtils.isEmpty(introduceImageUrl)) {
								if (null != mImageLoader) {
									ivLogo.setVisibility(View.VISIBLE);
									mImageLoader.displayImage(introduceImageUrl, ivLogo);
								}
							}
							
							//******	原议题附件 展示逻辑	start	*********/
//							List<JTFile2ForHY> aFileList = aTopic.getListJTFile();
//							if (null != aFileList) {
//								if (aFileList.size() > 0) {
//									for (int j = 0; j < aFileList.size(); j++) {
//										JTFile2ForHY aFile = aFileList.get(j);
//										if (null != aFile) {
//											String aTopicFile = aFile.getUrl();
//											String aFileExt = aFile.getSuffixName();
//											if (null != aTopicFile && null != aFileExt) {
//												if (false == aTopicFile.isEmpty() && false == aFileExt.isEmpty()) {
//													if (aFileExt.equals("jpg") || aFileExt.equals("jpeg") || aFileExt.equals("bmp") || aFileExt.equals("png") || aFileExt.equals("JPG") || aFileExt.equals("JPEG") || aFileExt.equals("BMP") || aFileExt.equals("PNG")) {
//														if (null != mImageLoader) {
//															ivLogo.setVisibility(View.VISIBLE);
//															mImageLoader.displayImage(aTopicFile, ivLogo);
//														}
//														break;
//													} else if (aFileExt.equals("mp4") || aFileExt.equals("3gp") || aFileExt.equals("MP4") || aFileExt.equals("3GP")) {
//														MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//														try {
//															if (Build.VERSION.SDK_INT >= 14) {
//																retriever.setDataSource(aTopicFile, new HashMap<String, String>());
//															} else {
//																retriever.setDataSource(aTopicFile);
//															}
//															Bitmap bitmap = retriever.getFrameAtTime();
//															if (null != bitmap) {
//																BitmapDrawable bd = new BitmapDrawable(getResources(), bitmap);
//																ivLogo.setVisibility(View.VISIBLE);
//																ivLogo.setImageDrawable(bd);
//															}
//														} catch (IllegalArgumentException ex) {
//														} catch (RuntimeException ex) {
//														} finally {
//															try {
//																retriever.release();
//															} catch (RuntimeException ex) {
//
//															}
//														}
//														break;
//													}
//												}
//											}
//										}
//									}
//								}
//							}
							//******	原议题附件 展示逻辑	end	 *********/
							
							
							//******	聊天列表		start	*********/
							List<MTopicChat> aChatList = aTopic.getListTopicChat();
							if (null != aChatList) {
								if (aChatList.size() > 0) {
									int count = 0;
									for (int j = 0; j < aChatList.size(); j++) {
										MTopicChat aChat = aChatList.get(j);
										if (null == aChat) {
											continue;
										}
										if (null == aChat.getChatType()) {
											continue;
										}
										// 如果 聊天类型不 等于 文本
//										if (0 != aChat.getChatType().intValue()) {
//											continue;
//										}
										
										// 如果发送者 是 系统  就跳过
										if (0 == aChat.getSenderType().intValue()) {
											continue;
										}
										
										
										// 聊天最多展示3条
										count++;
										if (count > 3) {
											break;
										}
										if (null != aChat.getSenderType()) {
											//**0-系统用户发送，此时userID无效；1-普通用户发送，具体用户此时参考meeting_id*/
											if (0 == aChat.getSenderType().intValue()) {
												View aView2 = LayoutInflater.from(this).inflate(R.layout.im_chat_message_system2, null);
												TextView timeTv = (TextView) aView2.findViewById(R.id.timeTv);
												TextView textTv = (TextView) aView2.findViewById(R.id.messageTv);
												// 系统消息 现在不显示
//												llChat.addView(aView2);

												if (null != aChat.getChatContent()) {
													textTv.setText(aChat.getChatContent());
												}
												if (null != aChat.getPublishTime()) {
													timeTv.setText(aChat.getPublishTime());
												}
											} 
											//	1-普通用户发送
											else {
												View aView2 = LayoutInflater.from(this).inflate(R.layout.im_chat_message_left_text3, null);
												TextView timeTv = (TextView) aView2.findViewById(R.id.timeTv);
												CircleImageView avatarIv = (CircleImageView) aView2.findViewById(R.id.avatarIv);
												TextView userNameTv = (TextView) aView2.findViewById(R.id.userNameTv);
												TextView textTv = (TextView) aView2.findViewById(R.id.textTv);

												llChat.addView(aView2);
												
												/** 聊天类型 0：text，1：audio，2：image，3：video，4：file，5：JTContact(人脉），6：knowledge(知识），7：requirement（需求）*/
												int chatType = aChat.getChatType().intValue();
												// 设置 除文本以外其它类型展示
												if(0 != chatType ){
													if(null != aChat.getChatContent()){
														textTv.setText(aChat.getChatContent());
													}
												}
												// chatType = 0  是本文聊天
												else if (null != aChat.getChatContent()) {
													String body = aChat.getChatContent();
													SmileyParser parser = null;
													SmileyParser2 parser2 = null;
													parser = SmileyParser.getInstance(MeetingMasterActivity.this);
													parser2 = SmileyParser2.getInstance(MeetingMasterActivity.this);
													CharSequence dd = parser.addSmileySpans(body);
													CharSequence dd1 = parser2.addSmileySpans(dd);
													textTv.setText(dd1);
												}
												if (null != aChat.getPublishTime()) {
													timeTv.setText(aChat.getPublishTime());
												}

												if (null != aChat.getMemberId()) {
													long id = aChat.getMemberId().longValue();
													List<MMeetingMember> aMembers = mMeetingQuery.getListMeetingMember();
													if (null != aMembers) {
														if (aMembers.size() > 0) {
															for (int k = 0; k < aMembers.size(); k++) {
																MMeetingMember aMember = aMembers.get(k);
																if (id == aMember.getMemberId()) {
																	if (null != aMember.getMemberName()) {
																		userNameTv.setText(aMember.getMemberName());
																	}
																	String aMemberImage = aMember.getMemberPhoto();
																	if (null != aMemberImage) {
																		if (false == aMemberImage.isEmpty()) {
																			if (null != mImageLoader) {
																				mImageLoader.displayImage(aMemberImage, avatarIv);
																			}
																		}
																	}
																	break;
																}
															}
														}
													}
												}
											}
										} 
										// 发送者类型 等于 null 时   这下面 还没改   可能 是 用上面的 抄一遍  如何遇到要改的问题  需要 把上面的东西 抽个方法
										else {
											View aView2 = LayoutInflater.from(this).inflate(R.layout.im_chat_message_left_text3, null);
											TextView timeTv = (TextView) aView2.findViewById(R.id.timeTv);
											ImageView avatarIv = (ImageView) aView2.findViewById(R.id.avatarIv);
											TextView userNameTv = (TextView) aView2.findViewById(R.id.userNameTv);
											TextView textTv = (TextView) aView2.findViewById(R.id.textTv);

											llChat.addView(aView2);

											if (null != aChat.getChatContent()) {
												textTv.setText(aChat.getChatContent());
											}
											if (null != aChat.getPublishTime()) {
												timeTv.setText(aChat.getPublishTime());
											}

											if (null != aChat.getMemberId()) {
												long id = aChat.getMemberId().longValue();
												List<MMeetingMember> aMembers = mMeetingQuery.getListMeetingMember();
												if (null != aMembers) {
													if (aMembers.size() > 0) {
														for (int k = 0; k < aMembers.size(); k++) {
															MMeetingMember aMember = aMembers.get(k);
															if (id == aMember.getMemberId()) {
																if (null != aMember.getMemberName()) {
																	userNameTv.setText(aMember.getMemberName());
																}
																String aMemberImage = aMember.getMemberPhoto();
																if (null != aMemberImage) {
																	if (false == aMemberImage.isEmpty()) {
																		if (null != mImageLoader) {
																			mImageLoader.displayImage(aMemberImage, avatarIv);
																		}
																	}
																}
																break;
															}
														}
													}
												}
											}
										}
									}
									llChatSession.setVisibility(View.VISIBLE);
									llChat.setVisibility(View.VISIBLE);
									llChat.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
										}
									});
								}
								else
								{
									llChatSession.setVisibility(View.GONE);
								}
							}
							else
							{
								llChatSession.setVisibility(View.GONE);
							}
							//******	聊天列表		end	*********/
							
							
							tvSpeaker.setText(aTopic.getMemberName());
							tvCompany.setText(aTopic.getMemberDesc());
							if(aTopic.getTopicDesc().isEmpty()){
								tvContent.setVisibility(View.GONE);
							}
							else{
								tvContent.setText(aTopic.getTopicDesc());
							}
							llJoin.setVisibility(View.VISIBLE);

							tvName.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									mMeetingTopic = aTopic;
									Intent intent = new Intent(MeetingMasterActivity.this, MeetingChatActivity.class);
									intent.putExtra(ENavConsts.EMeetingDetail, mMeetingQuery);
									intent.putExtra(ENavConsts.EMeetingTopicDetail, mMeetingTopic);
									startActivityForResult(intent, CALL_MEETING_BRANCH);
								}
							});
							llJoin.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									mMeetingTopic = aTopic;
									Intent intent = new Intent(MeetingMasterActivity.this, MeetingChatActivity.class);
									intent.putExtra(ENavConsts.EMeetingDetail, mMeetingQuery);
									intent.putExtra(ENavConsts.EMeetingTopicDetail, mMeetingTopic);
									startActivityForResult(intent, CALL_MEETING_BRANCH);
								}
							});
							btnJoin.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									mMeetingTopic = aTopic;
									Intent intent = new Intent(MeetingMasterActivity.this, MeetingChatActivity.class);
									intent.putExtra(ENavConsts.EMeetingDetail, mMeetingQuery);
									intent.putExtra(ENavConsts.EMeetingTopicDetail, mMeetingTopic);
									startActivityForResult(intent, CALL_MEETING_BRANCH);
								}
							});
							llTitle.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									ENavigate.startSquareActivity(MeetingMasterActivity.this, aTopic.getMeetingId(),0);
								}
							});

							ll_topic.addView(aView);
						}
					}
				}
			}
		}
		//*********** 	议题列表	 end	**********/

		if (null != tv_end_time) {
			
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日 HH:mm");
			try {
				if (false == mMeetingQuery.getEndTime().isEmpty()) {
					Date tmp = (fmt.parse(mMeetingQuery.getEndTime()));
					tv_end_time.setText(dateFormat.format(tmp) + "会议结束");
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		long cid = Long.valueOf(App.getApp().getUserID());
		if (null != btn_end) {
			if (3 == mMeetingQuery.getMeetingStatus()) {
				btn_end.setVisibility(View.GONE);
			}
			if (mMeetingQuery.getCreateId() != cid) {
				btn_end.setVisibility(View.GONE);
			}
		}
		if (null != btn_checkin) {
			if (3 == mMeetingQuery.getMeetingStatus()) {
				btn_checkin.setEnabled(false);
			}
			List<MMeetingMember> listMeetingMember = mMeetingQuery.getListMeetingMember();
			if (null != listMeetingMember) {
				int iCnt = listMeetingMember.size();
				if (iCnt > 0) {
					for (int i = 0; i < iCnt; ++i) {
						MMeetingMember aMember = listMeetingMember.get(i);
						if (App.getApp().getUserID().equals(String.valueOf(aMember.getMemberId()))) {
							if (1 == aMember.getIsSign()) {
								btn_checkin.setEnabled(false);
								btn_checkin.setText("已签");
							}
						}
					}
				}
			}
		}

		sv_main.setVisibility(View.VISIBLE);
	}


	private int getAcceptInviteAndSignInMumberCount() {
		int count = 0;
		for (MMeetingMember meetingMember : mMeetingQuery.getListMeetingMember()) {
			/**参会方式 0：邀请 1：报名*/
			if (meetingMember.getAttendMeetType()==0) {
				/**参会状态 0：未答复 1：接受邀请 2：拒绝邀请 4：报名 5：取消报名*/
				if (meetingMember.getAttendMeetStatus()==1) {
					count++;
				}
			}else if (meetingMember.getAttendMeetType()==1) {
				/**处理会议报名：0：未处理 1：同意报名 2：拒绝报名*/
				if (meetingMember.getExcuteMeetSign()==1) {
					count++;
				}
			}
		}
		return count;
	}

	
	/**
	 * 初始化参会人数据：根据参会人的报名及邀请状态判断是否显示参会人
	 * 
	 * @param meetingQuery
	 */
	private List<MMeetingMember> initMemberData(MMeetingQuery meetingQuery) {
		List<MMeetingMember> listMeetingMember = new ArrayList<MMeetingMember>();
		//自己去重
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
					if (mMeetingMember.getMemberType() == 2) {
//						isShowAddandDeleteButton = true;
					}
				}
			}
		}
		return listMeetingMember;
	}
	
}
