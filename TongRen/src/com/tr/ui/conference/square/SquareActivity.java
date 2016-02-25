package com.tr.ui.conference.square;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.App;
import com.tr.R;
import com.tr.api.CommonReqUtil;
import com.tr.api.ConferenceReqUtil;
import com.tr.baidumapsdk.BaiduLoc;
import com.tr.baidumapsdk.BaiduMapView;
import com.tr.model.SimpleResult;
import com.tr.model.conference.JTFile2ForHY;
import com.tr.model.conference.MMeetingDetail;
import com.tr.model.conference.MMeetingMember;
import com.tr.model.conference.MMeetingPic;
import com.tr.model.conference.MMeetingQuery;
import com.tr.model.conference.MMeetingRequiredSignupInfo;
import com.tr.model.conference.MMeetingSignLabelDataQuery;
import com.tr.model.conference.MMeetingTopicQuery;
import com.tr.model.demand.VoicePlayer;
import com.tr.model.joint.AffairNode;
import com.tr.model.joint.ConnectionNode;
import com.tr.model.joint.KnowledgeNode;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.obj.AffairsMini;
import com.tr.model.obj.Connections;
import com.tr.model.obj.JTFile;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.adapter.conference.GridViewMeetingProfileAdapter;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.conference.common.LocalPlayer;
import com.tr.ui.conference.im.MeetingChatActivity;
import com.tr.ui.conference.utile.PeopleOrgknowleRequirmentLayoutUtil;
import com.tr.ui.demand.util.DemandUtil;
import com.tr.ui.home.FrameWorkUtils;
import com.tr.ui.knowledge.swipeback.SwipeBackActivity;
import com.tr.ui.widgets.DrawableCenterTextView;
import com.tr.ui.widgets.EProgressDialog;
import com.utils.common.FileDownloader;
import com.utils.common.FileDownloader.OnFileDownloadListener;
import com.utils.common.GlobalVariable;
import com.utils.display.DisplayUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;

/**
 * @author sunjianan 会议详情
 */

public class SquareActivity extends SwipeBackActivity implements IBindData, OnClickListener, OnFileDownloadListener {

	private static final String TAG = SquareActivity.class.getSimpleName();

	/** 返回时的跳转类 */
	private Class goBackJumpClass;

	private final int SIGNUPINFOACTIVITY = 0;
	private final int START_MEETING_BRANCH_RESULT = 100;
	/** 接受邀请1, 拒绝邀请2, 取消参会5 */
	private int whichBtn = 1;
	/** 当前参会人数 */
	int memberCount = 0;

	@ViewInject(R.id.meeting_detail_toggle)
	/**控制页面显示*/
	private ViewGroup viewToggle;

	@ViewInject(R.id.hy_meeting_detail_titlebar)
	/**页面标题*/
	private FrameLayout meetingDetailTitle;
	/** 返回按钮 */
	private ImageView backBtn;
	/** 分享按钮 */
	private ImageView transmitBtn;
	/** 参会人信息按钮 */
	private ImageView attendeesBtn;

	@ViewInject(R.id.hy_meeting_detail_tv_name)
	/**会议标题*/
	private TextView titleTv;

	@ViewInject(R.id.hy_meeting_detail_tv_date)
	/**会议日期*/
	private TextView timeTv;
	@ViewInject(R.id.hy_meeting_detail_tv_author_first)
	/**发起者*/
	private TextView sponsorTv;
	// 暂时删掉--产品经理
	// @ViewInject(R.id.hy_meeting_detail_tv_author_second)
	// private TextView cosponsorTv;
	@ViewInject(R.id.hy_meeting_detail_tv_addr)
	/**地址*/
	private TextView locationTv;
	@ViewInject(R.id.hy_meeting_detail_tv_member_limit)
	/**人数限定*/
	private TextView limitPeopleTv;
	@ViewInject(R.id.hy_meeting_detail_tv_member_accept)
	/**已报名*/
	private TextView registPeopleTv;
	@ViewInject(R.id.hy_meeting_adddress)
	/**会议地址布局*/
	private LinearLayout meetingAdddress;
	/** 地图 */
	private BaiduMapView mapView;
	@ViewInject(R.id.hy_meeting_detail_tv_guide)
	/**介绍文本*/
	private TextView introductionTv;
	@ViewInject(R.id.hy_meeting_detail_gv_image)
	/**图片GridView*/
	private GridView introductionGv;
	// 日程
	// @ViewInject(R.id.list_item_meeting_agenda)
	// private LinearLayout meetingAgendaLv;
	// 日程 --议题 容器
	// @ViewInject(R.id.intraoduction_title_toggle_ll)
	// /**介绍模块布局*/
	// private LinearLayout introductionTitleToggle;
	@ViewInject(R.id.schedule_toggle_ll)
	/**日程模块布局*/
	private LinearLayout scheduleToggle;
	@ViewInject(R.id.hy_meeting_detail_ll_topic)
	/**具体日程容器*/
	private LinearLayout topicContainer;
	// 日程Item中的信息
	/** 议题 */
	private TextView agendaTopicTv;
	/** 议题日期 */
	private TextView agendaTimeTv;
	/** 议题主讲人头像 */
	private ImageView agendaPortraitIv;
	/** 议题主讲人姓名 */
	private TextView agendaNameTv;
	/** 议题主讲人公司 */
	private TextView agendaDescTv;
	/** 议题介绍图片 */
	private ImageView agendaIntroductionIv;
	/** 议题介绍 */
	private TextView agendaProfileTv;

	List<String> jtimgs = new ArrayList<String>();

	/** 介绍图片GridView适配器 */
	private GridViewMeetingProfileAdapter gridViewMeetingProfileAdapter;

	@ViewInject(R.id.meeting_people_toggle_ll)
	/**会议分享人脉模块布局*/
	private LinearLayout peopleToggle;
	@ViewInject(R.id.attendee_subtitle_item_meeting)
	/**标题：人脉*/
	private LinearLayout attendeeSubtitleMeeting;
	@ViewInject(R.id.hy_meeting_detail_ll_contact)
	/**人脉 容器 --- hy_list_item_tile_contacts(子内容)*/
	private LinearLayout attendeeContainer;

	@ViewInject(R.id.meeting_org_toggle_ll)
	/**会议分享组织模块布局*/
	private LinearLayout org_Toggle;
	@ViewInject(R.id.attendee_org_subtitle_item_meeting)
	/**标题：组织*/
	private LinearLayout orgSubtitleMeeting;
	@ViewInject(R.id.hy_meeting_detail_ll_org)
	/**组织 容器 --- hy_meeting_detail_ll_org(子内容)*/
	private LinearLayout orgContainer;

	@ViewInject(R.id.requirment_toggle_ll)
	/**会议分享需求模块布局*/
	private LinearLayout requirmentToggle;
	@ViewInject(R.id.requirment_subtitle_item_meeting)
	/**标题：需求*/
	private LinearLayout requirmentSubtitleMeeting;
	@ViewInject(R.id.hy_meeting_detail_ll_requirement)
	/**需求的容器 添加内容 --> hy_list_item_meeting_requirement*/
	private LinearLayout requirmentContainer;

	@ViewInject(R.id.knowledge_toggle_ll)
	/**会议分享知识模块布局*/
	private LinearLayout knowledgeToggle;
	@ViewInject(R.id.knowledge_subtitle_item_meeting)
	/**标题：知识*/
	private LinearLayout knowledgeSubtitleMeeting;

	@ViewInject(R.id.hy_meeting_detail_ll_knowledge)
	/**知识的容器 添加内容 --> hy_list_item_meeting_general*/
	private LinearLayout knowLedgeContainer;

	/** 相关人员及组织 */
	private LinearLayout ll_more_contact = null;
	/** 相关知识 */
	private LinearLayout ll_more_knowledge = null;
	/** 相关需求 */
	private LinearLayout ll_more_requirement = null;
	/** 相似会议 */
	private LinearLayout ll_more_meeting = null;

	private LinearLayout ll_more_contact_session = null;
	private LinearLayout ll_more_requirement_session = null;
	private LinearLayout ll_more_knowledge_session = null;
	private LinearLayout ll_more_meeting_session = null;

	private MMeetingDetail meetingDetail;
	/** 会议详情 */
	private MMeetingQuery meeting;

	private List<String> imgs;

	private EProgressDialog prgDialog;

	private ArrayList<JTFile> jtFiles;

	private LinearLayout topicToggle;

	private long meetingId;

	/** 音频 文件 集合 */
	private ArrayList<JTFile> listVoiceJtfile = new ArrayList<JTFile>();
	/** 视频 文件 集合 */
	private ArrayList<JTFile> listVideoJtfile = new ArrayList<JTFile>();
	/** 附件 文件 集合 */
	private ArrayList<JTFile> listAppendixJtfile = new ArrayList<JTFile>();
	/***/
	private ArrayList<VoicePlayer> voiceList = new ArrayList<VoicePlayer>();
	private SimpleDateFormat simpleData = new SimpleDateFormat("mm:ss");
	/** 正在播放的对象 */
	private VoicePlayer indexPlay = null;
	/** 播放器 */
	private LocalPlayer mPlayer = new LocalPlayer();
	private Map<Long, View> voiceView = new HashMap<Long, View>();

	@ViewInject(R.id.meeting_people_limit_ll)
	private LinearLayout meeting_people_limit_ll;
	@ViewInject(R.id.meeting_people_sign_ll)
	private LinearLayout meeting_people_sign_ll;

	private LinearLayout documentCatalogLl;

	/*** 详情页面底部操作 */
	FrameLayout meetingOpStudesFl;
	/** 报名——报名审核中——会议已结束 ——进入会议 */
	private DrawableCenterTextView signUpCommenOptTv;
	/** 进入会议——取消报名布局 */
	private LinearLayout signinCancleOptLL;
	/** 进入会议Tv */
	private DrawableCenterTextView signInOptTv;
	/** 取消报名 */
	private DrawableCenterTextView cancleOptTv;
	/** signUpCommenOptTv操作的状态：0报名，1报名审核中，2会议已结束,3进入会议 */
	private int SIGNUP_COMMEN_OPT_STATUS = 0;

	/** 默认为单议题会议——无主讲人 */
	private boolean isSingleTopic = true;

	private MMeetingTopicQuery aTopic;

	private int windowHeight;

	private int windowWidth;
	
	/**1,邀请函详情页面 ；2，会议详情页面*/
	private int type;

	private TextView activityTitleTv;

	private LinearLayout invitationCardOptLl;

	private TextView accepteInvitationTv;

	private TextView refuseInvitationTv;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		if (intent.hasExtra("goBackJumpClass")) {
			goBackJumpClass = (Class) intent.getSerializableExtra("goBackJumpClass");
		}
		type = intent.getIntExtra("type", -1);
		initView();
		initData();
	}

	public void initView() {
		setContentView(R.layout.hy_activity_meeting_detail);
		windowHeight = this.getWindowManager().getDefaultDisplay().getHeight();
		windowWidth = this.getWindowManager().getDefaultDisplay().getWidth();
		ViewUtils.inject(this);
		ll_more_contact = (LinearLayout) findViewById(R.id.hy_meeting_detail_ll_more_contact);
		ll_more_knowledge = (LinearLayout) findViewById(R.id.hy_meeting_detail_ll_more_knowledge);
		ll_more_requirement = (LinearLayout) findViewById(R.id.hy_meeting_detail_ll_more_requirement);
		ll_more_meeting = (LinearLayout) findViewById(R.id.hy_meeting_detail_ll_more_meeting);

		ll_more_contact_session = (LinearLayout) findViewById(R.id.hy_meeting_detail_ll_more_contact_session);
		ll_more_requirement_session = (LinearLayout) findViewById(R.id.hy_meeting_detail_ll_more_requirement_session);
		ll_more_knowledge_session = (LinearLayout) findViewById(R.id.hy_meeting_detail_ll_more_knowledge_session);
		ll_more_meeting_session = (LinearLayout) findViewById(R.id.hy_meeting_detail_ll_more_meeting_session);

		/** 视频布局 */
		videoRl = (RelativeLayout) findViewById(R.id.videoRl);
		/** 播放按钮 */
		videoPlayIv = (ImageView) findViewById(R.id.videoPlayIv);
		/** 音频布局 */
		voiceRecordLinearlayout = (LinearLayout) findViewById(R.id.voiceRecordLinearlayout);
		/** 附件布局 */
		documentCatalogLl = (LinearLayout) findViewById(R.id.documentCatalogLl);

		meetingOpStudesFl = (FrameLayout) findViewById(R.id.hy_meeting_detail_opt_fl);
		signUpCommenOptTv = (DrawableCenterTextView) findViewById(R.id.hy_meeting_detail_status);
		signinCancleOptLL = (LinearLayout) findViewById(R.id.hy_meeting_detial_signin_opt_ll);
		signInOptTv = (DrawableCenterTextView) findViewById(R.id.hy_meeting_detail_signin);
		cancleOptTv = (DrawableCenterTextView) findViewById(R.id.hy_meeting_detail_cancle);
		invitationCardOptLl = (LinearLayout) findViewById(R.id.hy_meeting_invitation_card_opt_ll);
		accepteInvitationTv = (TextView) findViewById(R.id.hy_meeting_accept_invitation);
		refuseInvitationTv = (TextView) findViewById(R.id.hy_meeting_adjust_invitation);

		transmitBtn = ((ImageView) meetingDetailTitle.findViewById(R.id.hy_layoutTitle_rightIconBtn1));
		attendeesBtn = ((ImageView) meetingDetailTitle.findViewById(R.id.hy_layoutTitle_rightIconBtn2));
		backBtn = ((ImageView) meetingDetailTitle.findViewById(R.id.hy_layoutTitle_backBtn));
		activityTitleTv = (TextView) meetingDetailTitle.findViewById(R.id.hy_layoutTitle_title);
		transmitBtn.setBackgroundResource(R.drawable.forward_share);
		attendeesBtn.setBackgroundResource(R.drawable.hy_ic_action_relation_pressed);
		// 返回键
		backBtn.setOnClickListener(this);
		// 子标题--邀请参会人组织
		// 地图
		mapView = new BaiduMapView(this, R.id.hy_meeting_detail_ll_map, false, false);

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

		// 标题获取焦点
		activityTitleTv.setFocusable(true);
		activityTitleTv.setFocusableInTouchMode(true);
		activityTitleTv.requestFocus();

		meetingOpStudesFl.setVisibility(View.GONE);
		Intent meetingIntent = getIntent();
		meetingId = meetingIntent.getLongExtra("meeting_id", 0);

		// 需求的标题设置 TODO
		ConferenceReqUtil.doGetMeetingDetail(this, this, meetingId, App.getUserID(), null);

		findViewById(R.id.intraoduction_title_toggle_dotline).setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		findViewById(R.id.intraoduction_schenule_dotline).setLayerType(View.LAYER_TYPE_SOFTWARE, null);
	}

	public void initData() {
		if (type == 1) {
			activityTitleTv.setText("邀请函");
		}else {
			activityTitleTv.setText("会议详情");
			
		}
		CommonReqUtil.doGetJointResource(this, this, String.valueOf(meetingId), 7, 0, 20,0 , null);
	}

	// 设置事件监听
	private void setListener() {
		// 转发
		transmitBtn.setOnClickListener(this);
		// 参会人详情
		attendeesBtn.setOnClickListener(this);
		// 报名会议
		// attendMeetingBtn.setOnClickListener(this);
		// 取消参加
		// cancelMeetingBtn.setOnClickListener(this);
		// 参会人详情
		attendeeSubtitleMeeting.setOnClickListener(this);
		orgSubtitleMeeting.setOnClickListener(this);
		// 需求
		requirmentSubtitleMeeting.setOnClickListener(this);
		// 知识
		knowledgeSubtitleMeeting.setOnClickListener(this);
		// 接受邀请
		// acceptIBtn.setOnClickListener(this);
		// 拒绝邀请
		// rejectIBtn.setOnClickListener(this);

		/** 进入会议 */
		signInOptTv.setOnClickListener(this);
		/** 取消参会 */
		cancleOptTv.setOnClickListener(this);
		/** 报名——报名审核中——会议已结束 ——进入会议 */
		signUpCommenOptTv.setOnClickListener(this);
		/** 接受邀请-拒绝邀请 */
		accepteInvitationTv.setOnClickListener(this);
		refuseInvitationTv.setOnClickListener(this);
	}

	private void fillData(HashMap<String, Object> dataBox) {

		if (null != ll_more_contact) {
			ll_more_contact.removeAllViews();
			List<ConnectionNode> listNode = (List<ConnectionNode>) dataBox.get("listJointPeopleNode");
			if (null != listNode) {
				if (listNode.size() > 0) {
					int count = listNode.size();
					for (int i = 0; i < count; i++) {
						ConnectionNode aNode = listNode.get(i);
						if (null != aNode) {
							ArrayList<Connections> aList = aNode.getListConnections();
							if (null != aList) {
								int count2 = aList.size();
								for (int j = 0; j < count2; j++) {
									Connections conn = aList.get(j);
									if (null != conn) {
										String name = conn.getName();
										String image = conn.getImage();
										View aView = LayoutInflater.from(this).inflate(R.layout.hy_list_item_title_contact, null);
										TextView tvName = (TextView) aView.findViewById(R.id.hy_contact_tv_name);
										ImageView ivLogo = (ImageView) aView.findViewById(R.id.hy_contact_iv_logo);
										tvName.setText(name);
										if (null == image) {
											ivLogo.setImageResource(R.drawable.hy_ic_default_friend_avatar);
										} else {
											if (image.isEmpty()) {
												ivLogo.setImageResource(R.drawable.hy_ic_default_friend_avatar);
											} else {
												ImageLoader.getInstance().displayImage(image, ivLogo);
											}
										}
										ll_more_contact.addView(aView);
										aView.setTag(conn);
										aView.setOnClickListener(peopleClickListener);
										if (ll_more_contact_session.getVisibility() != View.VISIBLE) {
											ll_more_contact_session.setVisibility(View.VISIBLE);
										}
									}
								}
							}
						}
					}
				}
			}

			listNode = (List<ConnectionNode>) dataBox.get("listJointOrganizationNode");
			if (null != listNode) {
				if (listNode.size() > 0) {
					int count = listNode.size();
					for (int i = 0; i < count; i++) {
						ConnectionNode aNode = listNode.get(i);
						if (null != aNode) {
							ArrayList<Connections> aList = aNode.getListConnections();
							if (null != aList) {
								int count2 = aList.size();
								for (int j = 0; j < count2; j++) {
									Connections conn = aList.get(j);
									if (null != conn) {
										String name = conn.getName();
										String image = conn.getImage();
										View aView = LayoutInflater.from(this).inflate(R.layout.hy_list_item_title_contact, null);
										TextView tvName = (TextView) aView.findViewById(R.id.hy_contact_tv_name);
										ImageView ivLogo = (ImageView) aView.findViewById(R.id.hy_contact_iv_logo);
										tvName.setText(name);
										if (null == image) {
											ivLogo.setImageResource(R.drawable.hy_ic_default_friend_avatar);
										} else {
											if (image.isEmpty()) {
												ivLogo.setImageResource(R.drawable.hy_ic_default_friend_avatar);
											} else {
												ImageLoader.getInstance().displayImage(image, ivLogo);
											}
										}
										ll_more_contact.addView(aView);
										aView.setTag(conn);
										aView.setOnClickListener(peopleClickListener);
										if (ll_more_contact_session.getVisibility() != View.VISIBLE) {
											ll_more_contact_session.setVisibility(View.VISIBLE);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if (null != ll_more_knowledge) {
			ll_more_knowledge.removeAllViews();
			List<KnowledgeNode> listNode = (List<KnowledgeNode>) dataBox.get("listJointKnowledgeNode");
			if (null != listNode) {
				if (listNode.size() > 0) {
					int count = listNode.size();
					for (int i = 0; i < count; i++) {
						KnowledgeNode aNode = listNode.get(i);
						if (null != aNode) {
							ArrayList<KnowledgeMini2> aList = aNode.getListKnowledgeMini2();
							if (null != aList) {
								int count2 = aList.size();
								for (int j = 0; j < count2; j++) {
									KnowledgeMini2 conn = aList.get(j);
									if (null != conn) {
										String title = conn.title;
										String time = conn.modifytime;
										View aView = LayoutInflater.from(this).inflate(R.layout.hy_list_item_meeting_knowledge, null);
										TextView tvContent = (TextView) aView.findViewById(R.id.hy_knowledge_tv_content);
										TextView tvTime = (TextView) aView.findViewById(R.id.hy_knowledge_tv_time);
										tvContent.setText(title);
										tvTime.setText(time);
										ll_more_knowledge.addView(aView);
										aView.setTag(conn);
										aView.setOnClickListener(knowledgeClickListener);
										if (ll_more_knowledge_session.getVisibility() != View.VISIBLE) {
											ll_more_knowledge_session.setVisibility(View.VISIBLE);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if (null != ll_more_requirement) {
			ll_more_requirement.removeAllViews();
			List<AffairNode> listNode = (List<AffairNode>) dataBox.get("listJointAffairNode");
			if (null != listNode) {
				if (listNode.size() > 0) {
					int count = listNode.size();
					for (int i = 0; i < count; i++) {
						AffairNode aNode = listNode.get(i);
						if (null != aNode) {
							ArrayList<AffairsMini> aList = aNode.getListAffairMini();
							if (null != aList) {
								int count2 = aList.size();
								for (int j = 0; j < count2; j++) {
									AffairsMini conn = aList.get(j);
									if (null != conn) {
										String title = conn.title;
										String time = conn.time;
										View aView = LayoutInflater.from(this).inflate(R.layout.hy_list_item_meeting_knowledge, null);
										TextView tvContent = (TextView) aView.findViewById(R.id.hy_knowledge_tv_content);
										TextView tvTime = (TextView) aView.findViewById(R.id.hy_knowledge_tv_time);
										tvContent.setText(title);
										tvTime.setText(time);
										ll_more_requirement.addView(aView);
										aView.setTag(conn);
										aView.setOnClickListener(affairClickListener);
										if (ll_more_requirement_session.getVisibility() != View.VISIBLE) {
											ll_more_requirement_session.setVisibility(View.VISIBLE);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if (null != ll_more_meeting) {
			ll_more_meeting_session.setVisibility(View.GONE);
		}
	}

	private OnClickListener knowledgeClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			KnowledgeMini2 knoMini2 = (KnowledgeMini2) v.getTag();
			ENavigate.startKnowledgeOfDetailActivity(SquareActivity.this, knoMini2.id, knoMini2.type);
		}
	};

	private OnClickListener peopleClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Connections conns = (Connections) v.getTag();
		}
	};

	private OnClickListener affairClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			AffairsMini affairsMini = (AffairsMini) v.getTag();
		}
	};

	private RelativeLayout videoRl;

	private ImageView videoPlayIv;

	private LinearLayout voiceRecordLinearlayout;

	// 回调请求服务器的数据
	@Override
	public void bindData(int tag, Object object) {

		/** 获取对接资源 */
		if (EAPIConsts.CommonReqType.GetJointResource == tag && null != object) {
			HashMap<String, Object> dataBox = (HashMap<String, Object>) object;
			if (null != dataBox) {
				if (dataBox.size() > 0) {
					fillData(dataBox);
				}
			}
		}

		/** 获取会议详情 */
		if (tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_MEETING_DETAIL && object != null) {
			meetingDetail = (MMeetingDetail) object;
			meeting = meetingDetail.getMeeting();
			if (meeting != null) {
				init();
				prgDialog.dismiss();
				viewToggle.setVisibility(View.VISIBLE);
			} else {
				Toast.makeText(this, "发起者已删除该会议", 0).show();
				finish();
			}
		}
		/** 报名操作——获取用户必填信息 */
		if (tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_REQUIREDSIGNUPINFO && object != null) {
			MMeetingRequiredSignupInfo signInfo = (MMeetingRequiredSignupInfo) object;
			// 必填信息不为空的情况下可能需要跳转
			if (signInfo != null && signInfo.getListMeetingSignLabelDataQuery().size() > 0) {
				// 有必填信息,并且某个必填信息为空 需要跳转
				boolean cansign = false;
				for (MMeetingSignLabelDataQuery signData : signInfo.getListMeetingSignLabelDataQuery()) {
					if (StringUtils.isEmpty(signData.getLabelContent())) {
						// 跳转到填写报名信息
						cansign = false;
						break;
					}
				}
				if (cansign) {
					ConferenceReqUtil.doGetSignUpMeeting(this, this, meeting.getId(), String.valueOf(App.getUserID()), App.getNick(), App.getUser().getImage(), null);
				} else {
					Toast.makeText(this, "请先完善您的报名信息", 0).show();
					Intent signInfoIntent = new Intent(this, SignupInfoActivity.class);
					signInfoIntent.putExtra("MMeetingRequiredSignupInfo", signInfo);
					startActivityForResult(signInfoIntent, SIGNUPINFOACTIVITY);
				}
			} else {
				ConferenceReqUtil.doGetSignUpMeeting(this, this, meeting.getId(), String.valueOf(App.getUserID()), App.getNick(), App.getUser().getImage(), null);
			}
			// 返回数据后,点击按钮回复可以点击的状态
		}
		/** 报名操作——true/false是否报名成功 */
		if (tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_SIGN_UP_MEETING && object != null) {
			SimpleResult flag = (SimpleResult) object;
			if (flag.isSucceed()) {
				// 报名成功
				Toast.makeText(this, "报名成功,请等待审核通过..", 0).show();

				// 显示报名审核中
				SIGNUP_COMMEN_OPT_STATUS = 1;
				signUpCommenOptTv.setCompoundDrawables(null, null, null, null);
				signUpCommenOptTv.setTextColor(Color.GRAY);
				signUpCommenOptTv.setGravity(Gravity.CENTER);
				signUpCommenOptTv.setText("报名审核中");
				signUpCommenOptTv.setVisibility(View.VISIBLE);
				signinCancleOptLL.setVisibility(View.GONE);
				meetingOpStudesFl.setVisibility(View.VISIBLE);

			} else {
				// 报名失败
				Toast.makeText(this, "报名失败", 0).show();
				signUpCommenOptTv.setClickable(true);
			}
		}
		// 完善报名信息
		if (tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_IMPROVESIGNINFORMATION && object != null) {
			SimpleResult flag = (SimpleResult) object;
			if (flag.isSucceed()) {
				ConferenceReqUtil.doGetSignUpMeeting(this, this, meeting.getId(), String.valueOf(App.getUserID()), App.getUserName(), App.getUser().getImage(), null);
			} else {
				Toast.makeText(this, "报名失败", 0).show();
				signUpCommenOptTv.setClickable(true);
			}
		}
		// 1.接受邀请 2.拒绝邀请 5.取消参会
		if (tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_MEETING_MEMBER_REPORT && object != null) {
			SimpleResult flag = (SimpleResult) object;
			switch (whichBtn) {
			// 接受邀请
			case 1:
				if (flag.isSucceed()) {// 成功： 显示 进入会议/取消参会

					invitationCardOptLl.setVisibility(View.GONE);
					signinCancleOptLL.setVisibility(View.VISIBLE);
					signUpCommenOptTv.setVisibility(View.GONE);
					meetingOpStudesFl.setVisibility(View.VISIBLE);

				} else {
					Toast.makeText(this, "报名失败", 0).show();
				}
				break;
			// 拒绝邀请
			case 2: // 拒绝邀请 显示报名
				if (flag.isSucceed()) {
					invitationCardOptLl.setVisibility(View.GONE);
					signinCancleOptLL.setVisibility(View.GONE);
					signUpCommenOptTv.setVisibility(View.VISIBLE);
					meetingOpStudesFl.setVisibility(View.VISIBLE);
				} else {
					Toast.makeText(this, "报名失败", 0).show();
				}
				break;
			// 取消参会
			case 5:
				if (flag.isSucceed()) {
					Toast.makeText(this, "您已取消参会", 0).show();
					Drawable drawable = getResources().getDrawable(R.drawable.meetingsignup);
					drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
					signUpCommenOptTv.setCompoundDrawables(drawable, null, null, null);
					signUpCommenOptTv.setGravity(Gravity.CENTER_VERTICAL);
					signUpCommenOptTv.setText("报名");
					signUpCommenOptTv.setCompoundDrawablePadding(20);
					signUpCommenOptTv.setVisibility(View.VISIBLE);
					signinCancleOptLL.setVisibility(View.GONE);
					meetingOpStudesFl.setVisibility(View.VISIBLE);
				} else {
					if (meeting.getMeetingStatus() == 2) {
						Toast.makeText(this, "会议进行中,不能取消参会", 0).show();
					} else {
						Toast.makeText(this, "取消参会失败", 0).show();
					}
				}
				break;
			}
		}
	}

	/***
	 * 初始化界面数据
	 */
	private void init() {
		if (meeting.getMeetingName() != null) {
			titleTv.setText(meeting.getMeetingName());
		}
		if (meeting.getStartTime() != null) {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyy年MM月dd日 E HH:mm");
			try {
				if (false == meeting.getStartTime().isEmpty()) {
					Date tmp = (fmt.parse(meeting.getStartTime()));
					timeTv.setText(dateFormat3.format(tmp));
				}
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		if (meeting.getCreateName() != null) {
			sponsorTv.setText(meeting.getCreateName());
		}
		if (!StringUtils.isEmpty(meeting.getMeetingAddress())) {
			locationTv.setText(meeting.getMeetingAddress());
		} else {
			locationTv.setText("未知");
		}
		// // 设置百度地图
		if (null != mapView && !StringUtils.isEmpty(meeting.getMeetingAddress())) {

			mapView.clearPoi();

			Double lng = 0.0;
			Double lat = 0.0;

			if (null != meeting.getMeetingAddressPosY()) {
				if (false == meeting.getMeetingAddressPosY().isEmpty()) {
					lat = Double.valueOf(meeting.getMeetingAddressPosY());
				}
			}
			if (null != meeting.getMeetingAddressPosX()) {
				if (false == meeting.getMeetingAddressPosX().isEmpty()) {
					lng = Double.valueOf(meeting.getMeetingAddressPosX());
				}
			}
			BaiduLoc aLoc = App.getApp().getBaiduLoc();
			if (aLoc.isLocationValid(lng, lat)) {
				mapView.setCenter(lat, lng, R.drawable.hy_img_location);
			}
		} else {
			(findViewById(R.id.hy_meeting_detail_ll_map)).setVisibility(View.GONE);
		}
		limitPeopleTv.setText("" + meeting.getMemberCount());

		if (meeting.getListMeetingMember() != null) {
			memberCount = 0;
			for (MMeetingMember member : meeting.getListMeetingMember()) {
				if (member.getAttendMeetType() == 1 && member.getAttendMeetStatus() == 4 && member.getExcuteMeetSign() == 1) {
					memberCount++;

				}
			}
			registPeopleTv.setText("" + memberCount);
		}
		if (!StringUtils.isEmpty(meeting.getMeetingDesc())) {
			introductionTv.setText(meeting.getMeetingDesc());
		} else {
			introductionTv.setVisibility(View.GONE);
		}
		if (meeting.getListMeetingPic() != null && meeting.getListMeetingPic().size() != 0) {
			gridViewMeetingProfileAdapter = new GridViewMeetingProfileAdapter(this, meeting.getListMeetingPic());
			if (introductionGv != null && windowWidth != 0) {
				LayoutParams layoutParams = new LayoutParams(windowWidth * 4 / 5, LayoutParams.WRAP_CONTENT);
				layoutParams.setMargins(DisplayUtil.dip2px(this, 10), DisplayUtil.dip2px(this, 10), 0, DisplayUtil.dip2px(this, 4));
				introductionGv.setLayoutParams(layoutParams);
			}
			introductionGv.setAdapter(gridViewMeetingProfileAdapter);

			imgs = new ArrayList<String>();
			if (meeting.getListMeetingPic() != null && meeting.getListMeetingPic().size() > 0) {
				for (MMeetingPic pic : meeting.getListMeetingPic()) {
					imgs.add(pic.getPicPath());
				}
			}
			introductionGv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					PhotoBrowseFragment browseFragment = new PhotoBrowseFragment(imgs, position);
					getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slow_fade_in, R.anim.slow_fade_out, R.anim.slow_fade_in, R.anim.slow_fade_out)
							.replace(R.id.hy_meeting_detail_container, browseFragment).addToBackStack(null).commit();
				}
			});
		} else {
			introductionGv.setVisibility(View.GONE);
		}
		if (meeting.getIsSecrecy()) {
			meeting_people_limit_ll.setVisibility(View.GONE);
			meeting_people_sign_ll.setVisibility(View.GONE);
			transmitBtn.setVisibility(View.INVISIBLE);
		}
		// 议题初始化
		initAllTopics(topicContainer);

		if (null != meeting) {
			if (null != meeting.getListMeetingTopic()) {
				List<MMeetingTopicQuery> aList = meeting.getListMeetingTopicQuery();
				if (null != aList) {
					if (aList.size() > 0) {
						aTopic = aList.get(0);
					}
				}
			}
		}

		// 邀请参会人初始化
		intAllPeople();
		// 组织初始化
		initAllOrgan();
		// 需求信息初始化
		initAllRequirments();
		// 知识初始化
		initAllKnowledges();

		if (3 == meeting.getMeetingStatus()) {
			SIGNUP_COMMEN_OPT_STATUS = 2;
			signUpCommenOptTv.setCompoundDrawables(null, null, null, null);
			signUpCommenOptTv.setGravity(Gravity.CENTER);
			signUpCommenOptTv.setText("会议已结束");
			meetingOpStudesFl.setVisibility(View.VISIBLE);
			transmitBtn.setVisibility(View.INVISIBLE);
			attendeesBtn.setVisibility(View.INVISIBLE);
		} else {
			// button初始化
			initButtonStatus();
			setListener();
		}

		/** 会议 音频、视频、附件 都在这里 */
		List<JTFile2ForHY> listMeetingFile = meeting.getListMeetingFile();
		for (JTFile2ForHY jtFile2ForHY : listMeetingFile) {
			JTFile jtfile = jtFile2ForHY.toJtfile();
			/** 视频 */
			if (jtfile.mType == 2) {
				listVideoJtfile.add(jtfile);
			}
			/** 附件 */
			if (jtfile.mType == 3) {
				listAppendixJtfile.add(jtfile);
			}
			/** 音频 */
			if (jtfile.mType == 4) {
				listVoiceJtfile.add(jtfile);
			}
		}
		/** 显示视频布局 */
		if (listVideoJtfile != null && listVideoJtfile.size() > 0) {
			videoRl.setVisibility(View.VISIBLE);
			JTFile videoJtFile = listVideoJtfile.get(0);// 只能有一个视频
			String videoPath = videoJtFile.getmUrl();
			showVideo(videoPath, videoRl);
		} else {
			videoRl.setVisibility(View.GONE);
		}
		/** 显示音频布局 */
		if (listVoiceJtfile != null && listVoiceJtfile.size() > 0) {
			voiceRecordLinearlayout.setVisibility(View.VISIBLE);
			for (JTFile voiceJtfile : listVoiceJtfile) {
				// 下载文件
				FileDownloader fileDownloader = new FileDownloader(this, voiceJtfile, this);
				fileDownloader.start();
			}
		} else {
			voiceRecordLinearlayout.setVisibility(View.GONE);
		}
		/** 显示附件布局 */
		if (listAppendixJtfile != null && listAppendixJtfile.size() > 0) {
			documentCatalogLl.setVisibility(View.VISIBLE);
			for (final JTFile file : listAppendixJtfile) {
				View v = View.inflate(this, R.layout.demand_need_details_document_item, null);
				TextView documentTv = (TextView) v.findViewById(R.id.documentTv);
				documentTv.setText(file.mFileName);
				if (StringUtils.isEmpty(file.getmSuffixName())) {
					file.mSuffixName = file.mFileName.substring(file.mFileName.lastIndexOf(".") + 1);
				}
				documentTv.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						new DownLoadAndOpen(file.getmUrl(), file.mSuffixName).execute();
					}
				});
				documentCatalogLl.addView(v);
			}
		} else {
			documentCatalogLl.setVisibility(View.GONE);
		}
		// 当会议介绍完全没有内容时,gone掉
		if (StringUtils.isEmpty(meeting.getMeetingDesc()) && meeting.getListMeetingFile().size() < 1 && meeting.getListMeetingPic().size() < 1) {
			findViewById(R.id.intrducetvtitle).setVisibility(View.GONE);
			findViewById(R.id.intrduceLine).setVisibility(View.GONE);
		}
	}

	private DownLoadAndOpen downLoadAndOpen;

	private void showVideo(final String url, View v) {
		ImageView videoPlayIv = (ImageView) v.findViewById(R.id.videoPlayIv);
		videoPlayIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.e("resultURL", url);
				// 判断当前的网络
				if (!DemandUtil.isVideo(SquareActivity.this)) {
					showToast("已设置为当前的网络不能播放视频");
					return;
				}
				// 调用系统播放器
				Intent video = new Intent(Intent.ACTION_VIEW);
				if (!(new File(url).isFile())) {
					video.setDataAndType(Uri.parse(url), "video/*");
				} else {
					video.setDataAndType(Uri.parse("file://" + url), "video/*");
				}
				startActivity(video);
			}
		});
	}

	/**
	 * 创建一个音频播放器对象到界面中
	 * 
	 * @param voice
	 */
	private void addPlayView(VoicePlayer play, JTFile jtFile) {
		if (voiceList.add(play)) {
			View convertView = View.inflate(this, R.layout.demand_play_item, null);
			ImageView playIv = (ImageView) convertView.findViewById(R.id.play_start_iv); // 播放按钮
			TextView timeTv = (TextView) convertView.findViewById(R.id.play_time_tv);// 播放时间进度
			SeekBar seekBar = (SeekBar) convertView.findViewById(R.id.play_seekBar);// 播放时间长度
			ImageView deleteIv = (ImageView) convertView.findViewById(R.id.play_delete_iv);// 删除按钮
			TextView btnTv = (TextView) convertView.findViewById(R.id.play_btn_tv);//
			convertView.findViewById(R.id.view).setVisibility(View.GONE);
			deleteIv.setVisibility(View.GONE);
			playIv.setTag(play);
			timeTv.setTag(play);
			seekBar.setTag(play);
			btnTv.setTag(play);
			deleteIv.setTag(play);

			deleteIv.setOnClickListener(deleteBtnListener); // 删除方法
			playIv.setOnClickListener(playBtnListener);
			seekBar.setOnSeekBarChangeListener(seekBarChange);
			play.id = System.currentTimeMillis();
			seekBar.setProgress(0);
			date.setTime(play.time);
			timeTv.setText(simpleData.format(date));// 分秒
			voiceRecordLinearlayout.addView(convertView);
		}
	}

	/**
	 * 删除 方法，停止正在播放的对象，并 删除界面效果
	 */
	private OnClickListener deleteBtnListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			VoicePlayer player = (VoicePlayer) v.getTag();
			JTFile jtFile = (JTFile) v.getTag(v.getId());
			if (player != null) {
				if (indexPlay != null && indexPlay.id == player.id) {
					reductionView();
				}
				View view = voiceView.get(player.id);
				if (view != null) {
					voiceRecordLinearlayout.removeView(view);
				}
				voiceList.remove(player);// 从数据中删除
			}
		}
	};

	private OnSeekBarChangeListener seekBarChange = new OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// 停止
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// 按下
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			// 滑动
		}
	};
	/**
	 * 播放器控制按钮
	 */
	private OnClickListener playBtnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			VoicePlayer player = (VoicePlayer) v.getTag();
			if (indexPlay != null && player.id == indexPlay.id) {
				// 是自己在播放
				if (mPlayer.isPlay()) { // 暂停
					v.setBackgroundResource(R.drawable.recordplay);
					mPlayer.pause();
					mTimerTask.cancel();
				} else {// 播放
					v.setBackgroundResource(R.drawable.recordpause);
					mPlayer.start();
					startTime();
				}
			} else {
				if (mPlayer.isPlay())
					reductionView();
				indexPlay = player;
				// 自己开始进行播放 修改自己的状态
				v.setBackgroundResource(R.drawable.recordpause);
				mPlayer.startPlay(player.file.getPath(), onCompletion);
				getView(v);
				startTime();// 开始计时器
			}
		}
	};
	private long timer;
	/** 进度条 */
	private SeekBar seekBar;
	private TimerTask mTimerTask;
	/** 正在播放的对象按钮 */
	private ImageView playIv;
	/** 播放时间 */
	private TextView time;
	/** 按钮状态 */
	private TextView play_btn_tv;
	/** 记录进度条的 */
	private Timer mTimer;
	private Date date = new Date();

	/**
	 * 播放器控制事件
	 */
	private OnCompletionListener onCompletion = new OnCompletionListener() {

		@Override
		public void onCompletion(MediaPlayer mp) {
			reductionView();
		}
	};

	/**
	 * 播放器控制计时器
	 */
	private void startTime() {
		mTimer = new Timer();
		mTimerTask = new TimerTask() {
			@Override
			public void run() {
				if (null != seekBar && null != mPlayer) {
					seekBar.setProgress(mPlayer.getProgress());
					timer += 10;
					handler.sendEmptyMessage(1);
				}
			}
		};
		if (null != mPlayer && null != mTimer && null != mTimerTask) {
			mTimer.schedule(mTimerTask, 0, 10);
		}
	}

	private Handler handler = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			if (msg.what == 1) {
				date.setTime(timer);
				time.setText(simpleData.format(date));
			} else if (msg.what == 2) {
				// 下载成功
				JTFile jtFile = (JTFile) msg.obj;
				// 点击确定按钮
				VoicePlayer voPlayer = new VoicePlayer();
				File voiceFile = new File(jtFile.mLocalFilePath);
				voPlayer.file = voiceFile;
				MediaPlayer mediaPlayer = new MediaPlayer();
				try {
					mediaPlayer.setDataSource(jtFile.getmUrl());
					mediaPlayer.prepare();
					int duration = mediaPlayer.getDuration();
					voPlayer.time = duration;
					mediaPlayer.release();
					mediaPlayer = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
				addPlayView(voPlayer, jtFile);
			}
		};
	};
	
	
	/**
	 * 获取当前播放的对象 的控件信息
	 */
	private void getView(View childs) {
		View view = (View) childs.getParent();
		playIv = (ImageView) view.findViewById(R.id.play_start_iv);
		time = (TextView) view.findViewById(R.id.play_time_tv);
		seekBar = (SeekBar) view.findViewById(R.id.play_seekBar);
		play_btn_tv = (TextView) view.findViewById(R.id.play_btn_tv);
	}

	/**
	 * 状态还原
	 */
	private void reductionView() {
		if (mPlayer != null) {
			mPlayer.stopPlay();// 停止播放
		}
		if (indexPlay != null) {
			timer = 0;
			seekBar.setProgress(0);
			mTimerTask.cancel();
			date.setTime(indexPlay.time);
			time.setText(simpleData.format(date));
			playIv.setBackgroundResource(R.drawable.recordplay);
			indexPlay = null;
		}
	}

	/**
	 * 判断当前是否为邀请状态
	 * 
	 * @return 0.未答复 1接受邀请 2拒绝邀请，3.取消会议 4 报名 5取消报名",
	 */
	public int isInvitation() {
		MMeetingMember meetingMember = null;
		for (MMeetingMember member : meeting.getListMeetingMember()) {
			if (String.valueOf(member.getMemberId()).equals(App.getUserID())) {
				meetingMember = member;
				break;
			}
		}
		if (App.getUserID().equals("" + meeting.getCreateId())) {
			return 6;
		}
		// "attendMeetStatus":"参会状态 0.未答复 1接受邀请2拒绝邀请，3.取消会议 4 报名 5取消报名"
		// "attendMeetType":"参会方式 0邀请，1报名"
		// "excuteMeetSign":"报名审核：0：未处理：1：同意报名，2：拒绝报名"
		if (meeting != null && meeting.getListMeetingMember() != null && meetingMember != null) {
			// 参会状态：0：未答复 1：接受邀请 2：拒绝邀请 4：报名 5：取消报名
			if (meetingMember.getAttendMeetStatus() == 4) {// 报名
				// 处理会议报名：0：未处理 1：同意报名 2：拒绝报名
				if (meetingMember.getExcuteMeetSign() == 2) {
					return 2;
				} else if (meetingMember.getExcuteMeetSign() == 0) {
					return 7;// 报名未处理：显示报名审核中
				} else if (meetingMember.getExcuteMeetSign() == 1) {
					return 1;// 报名被同意，显示进入会议 和 取消参会
				}
			}
			return meetingMember.getAttendMeetStatus();
		}
		return 5;
	}

	/**
	 * 初始化button的显示状态
	 * 
	 * @param flag
	 */
	private void initButtonStatus() {

		switch (isInvitation()) {
		// 1.接受邀请状态
		case 1:
			// 4.报名
		case 4:
			// 显示进入会议 和 取消参会
			signUpCommenOptTv.setVisibility(View.GONE);
			signinCancleOptLL.setVisibility(View.VISIBLE);
			meetingOpStudesFl.setVisibility(View.VISIBLE);
			break;

		// 0.未答复
		case 0:
			// 2拒绝邀请
		case 2:
			// 5.取消报名
		case 5:
			// 显示报名
			SIGNUP_COMMEN_OPT_STATUS = 0;
			Drawable drawable = getResources().getDrawable(R.drawable.meetingsignup);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			signUpCommenOptTv.setCompoundDrawables(drawable, null, null, null);
			signUpCommenOptTv.setGravity(Gravity.CENTER_VERTICAL);
			signUpCommenOptTv.setText("报名");
			signUpCommenOptTv.setCompoundDrawablePadding(20);
			signUpCommenOptTv.setVisibility(View.VISIBLE);
			signinCancleOptLL.setVisibility(View.GONE);
			meetingOpStudesFl.setVisibility(View.VISIBLE);
			break;

		// 6. 当前用户为发起人
		case 6:
			// 进入会议
			SIGNUP_COMMEN_OPT_STATUS = 3;
			Drawable drawable1 = getResources().getDrawable(R.drawable.meetingsignin);
			drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
			signUpCommenOptTv.setCompoundDrawables(drawable1, null, null, null);
			signUpCommenOptTv.setCompoundDrawablePadding(20);
			signUpCommenOptTv.setText("进入会议");
			meetingOpStudesFl.setVisibility(View.VISIBLE);
			break;
		case 7:
			SIGNUP_COMMEN_OPT_STATUS = 1;
			signUpCommenOptTv.setCompoundDrawables(null, null, null, null);
			signUpCommenOptTv.setGravity(Gravity.CENTER);
			signUpCommenOptTv.setTextColor(Color.GRAY);
			signUpCommenOptTv.setText("报名审核中");
			meetingOpStudesFl.setVisibility(View.VISIBLE);
			break;
		}

		// 如果是邀请函，显示接受/拒绝邀请
		if (type == 1 && isInvitation() != 1 && isInvitation() != 2 || isInvitation()==0) {// 如果当前不为接受邀请状态，并且不为拒绝邀请状态，说明这个邀请函还没有操作
			signinCancleOptLL.setVisibility(View.GONE);
			signUpCommenOptTv.setVisibility(View.GONE);
			invitationCardOptLl.setVisibility(View.VISIBLE);
		}
	}

	// 初始化 所有议题
	private void initAllTopics(ViewGroup container) {
		container.removeAllViews();
		if (meeting.getMeetingType() == 0) {
			scheduleToggle.setVisibility(View.GONE);
			isSingleTopic = true;
			return;
		}

		if (meeting != null && meeting.getListMeetingTopic() != null && meeting.getListMeetingTopic().size() != 0) {
			isSingleTopic = false;
			for (int i = 0; i < meeting.getListMeetingTopic().size(); i++) {
				ViewGroup llTopic = initTopic(i);
				container.addView(llTopic);
			}
		} else {
			scheduleToggle.setVisibility(View.GONE);
		}
	}

	// 初始化一个议题
	private ViewGroup initTopic(final int position) {
		LinearLayout llTopic = (LinearLayout) View.inflate(this, R.layout.hy_list_item_meeting_topic_tile, null);
		llTopic.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

		// 日程初始化

		agendaTopicTv = (TextView) llTopic.findViewById(R.id.hy_meeting_topic_tile_tv_name);
		agendaTimeTv = (TextView) llTopic.findViewById(R.id.hy_meeting_topic_tile_tv_time);
		agendaPortraitIv = (ImageView) llTopic.findViewById(R.id.hy_meeting_topic_tile_iv_avatar);
		agendaNameTv = (TextView) llTopic.findViewById(R.id.hy_meeting_topic_tile_tv_speaker);
		agendaDescTv = (TextView) llTopic.findViewById(R.id.hy_meeting_topic_tile_tv_company);
		agendaIntroductionIv = (ImageView) llTopic.findViewById(R.id.hy_meeting_topic_tile_iv_logo);
		agendaProfileTv = (TextView) llTopic.findViewById(R.id.hy_meeting_topic_tile_tv_content);
		topicToggle = (LinearLayout) llTopic.findViewById(R.id.hy_meeting_topic_toggle);
		LinearLayout enterMeetingToggle = (LinearLayout) llTopic.findViewById(R.id.hy_meeting_topic_tile_ll_join);
		enterMeetingToggle.setVisibility(View.GONE);
		LinearLayout chatMeetingToggle = (LinearLayout) llTopic.findViewById(R.id.hy_meeting_topic_tile_ll_chat_session);
		chatMeetingToggle.setVisibility(View.GONE);
		if (!StringUtils.isEmpty(meeting.getListMeetingTopic().get(position).getTopicContent())) {
			agendaTopicTv.setText(meeting.getListMeetingTopic().get(position).getTopicContent());
		}
		if (!StringUtils.isEmpty(meeting.getListMeetingTopic().get(position).getTopicStartTime())) {
			agendaTimeTv.setText(meeting.getListMeetingTopic().get(position).getTopicStartTime());
		} else {
			agendaTimeTv.setVisibility(View.GONE);
		}
		String memberPic = meeting.getListMeetingTopic().get(position).getMemberPic();
		com.utils.common.Util.initAvatarImage(this, agendaPortraitIv, meeting.getListMeetingTopic().get(position).getMemberName(), memberPic, 0, 1);
		agendaPortraitIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ENavigate.startRelationHomeActivity(SquareActivity.this,meeting.getListMeetingTopic().get(position).getMemberId()+"");
			}
		});
		/*if (!StringUtils.isEmpty(memberPic)&&!memberPic.endsWith(GlobalVariable.PERSON_DEFAULT_AVATAR)&&!memberPic.endsWith(GlobalVariable.ORG_DEFAULT_AVATAR)) {
			ImageLoader.getInstance().displayImage(meeting.getListMeetingTopic().get(position).getMemberPic(), agendaPortraitIv);
		} else {
			String lastchar = "";
			String memberName = meeting.getListMeetingTopic().get(position).getMemberName();
			if(!TextUtils.isEmpty(memberName)){
				lastchar = memberName.substring(memberName.length()-1);
			}
			Bitmap bm = null;
			int resid = R.drawable.ic_person_default_avatar_gray;
			bm = com.utils.common.Util.createBGBItmap(this, resid, R.color.avatar_text_color, R.dimen.avatar_text_size, lastchar);
			agendaPortraitIv.setImageBitmap(bm);
		}*/
		if (!StringUtils.isEmpty(meeting.getListMeetingTopic().get(position).getMemberName())) {
			agendaNameTv.setText(meeting.getListMeetingTopic().get(position).getMemberName());
		} else {
			agendaNameTv.setVisibility(View.GONE);
		}
		if (!StringUtils.isEmpty(meeting.getListMeetingTopic().get(position).getMemberDesc())) {
			agendaDescTv.setText(meeting.getListMeetingTopic().get(position).getMemberDesc());
		} else {
			agendaDescTv.setVisibility(View.GONE);
		}
		if (!StringUtils.isEmpty(meeting.getListMeetingTopicQuery().get(position).getPath())) {
			ImageLoader.getInstance().displayImage(meeting.getListMeetingTopicQuery().get(position).getPath(), agendaIntroductionIv);
			agendaIntroductionIv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					PhotoBrowseFragment browseFragment = new PhotoBrowseFragment(jtimgs, 0);
					getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slow_fade_in, R.anim.slow_fade_out, R.anim.slow_fade_in, R.anim.slow_fade_out)
							.replace(R.id.hy_meeting_detail_container, browseFragment).addToBackStack(null).commit();
				}
			});
		} else {
			agendaIntroductionIv.setVisibility(View.GONE);
		}
		if (!StringUtils.isEmpty(meeting.getListMeetingTopic().get(position).getTopicDesc())) {
			agendaProfileTv.setText(meeting.getListMeetingTopic().get(position).getTopicDesc());
		} else {
			agendaProfileTv.setVisibility(View.GONE);
		}
		return llTopic;
	}

	/**
	 * 初始化5个人脉
	 */
	private void intAllPeople() {
		attendeeContainer.removeAllViews();
		if (meeting != null && meeting.getListMeetingPeople() != null && meeting.getListMeetingPeople().size() != 0) {
			for (int i = 0; i < 5; i++) {
				ViewGroup people = PeopleOrgknowleRequirmentLayoutUtil.initPeople(meeting.getListMeetingPeople(), i, this);
				if (i < meeting.getListMeetingPeople().size()) {
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
		if (meeting != null && meeting.getListMeetingOrgan() != null && meeting.getListMeetingOrgan().size() != 0) {
			for (int i = 0; i < 5; i++) {
				ViewGroup org = PeopleOrgknowleRequirmentLayoutUtil.initOrgan(meeting.getListMeetingOrgan(), i, this);
				if (i < meeting.getListMeetingOrgan().size() && org != null) {
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
		if (meeting != null && meeting.getListMeetingRequirement() != null && meeting.getListMeetingRequirement().size() != 0) {
			for (int i = 0; i < 2; i++) {
				if (i < meeting.getListMeetingRequirement().size()) {
					ViewGroup requirment = PeopleOrgknowleRequirmentLayoutUtil.initRequirment(meeting.getListMeetingRequirement(), i, this);
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
		if (meeting != null && meeting.getListMeetingKnowledge() != null && meeting.getListMeetingKnowledge().size() != 0) {
			for (int i = 0; i < 2; i++) {
				if (i < meeting.getListMeetingKnowledge().size()) {
					ViewGroup knowledge = PeopleOrgknowleRequirmentLayoutUtil.initKnowledge(meeting.getListMeetingKnowledge(), i, this);
					knowLedgeContainer.addView(knowledge);
				}
			}
		} else {
			knowledgeToggle.setVisibility(View.GONE);
		}
	}

	/**
	 * 监听Back键按下事件 注意: super.onBackPressed()会自动调用finish()方法,关闭 当前Activity.
	 * 若要屏蔽Back键盘,注释该行代码即可
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		goBackJump();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 回退
		case R.id.hy_layoutTitle_backBtn:
			onBackPressed();
			break;
		// 进入会议
		case R.id.hy_meeting_detail_signin:
			if (isSingleTopic) {// 单议题；无主讲
				if (null != aTopic) {
					Intent intent = new Intent(SquareActivity.this, MeetingChatActivity.class);
					intent.putExtra(ENavConsts.EMeetingDetail, meeting);
					intent.putExtra(ENavConsts.EMeetingTopicDetail, aTopic);
					startActivity(intent);
				}
			} else {
				ENavigate.startMeetingMasterActivityForResult(this, meetingId ,START_MEETING_BRANCH_RESULT);
			}
			break;
		// 报名——报名审核中——会议已结束——进入会议
		case R.id.hy_meeting_detail_status:
			if (SIGNUP_COMMEN_OPT_STATUS == 0) {// 报名
				ConferenceReqUtil.doGetRequiredSignupInfo(this, this, meeting.getId(), null);
				signUpCommenOptTv.setClickable(false);
			} else if (SIGNUP_COMMEN_OPT_STATUS == 3) {// 进入会议
				// TODO: 单议题/多议题
				if (isSingleTopic) {// 单议题
					if (null != aTopic) {
						Intent intent = new Intent(SquareActivity.this, MeetingChatActivity.class);
						intent.putExtra(ENavConsts.EMeetingDetail, meeting);
						intent.putExtra(ENavConsts.EMeetingTopicDetail, aTopic);
						startActivity(intent);
					}
				} else {
					ENavigate.startMeetingMasterActivityForResult(this, meetingId,START_MEETING_BRANCH_RESULT);
				}

			}
			break;
		// 取消参会
		case R.id.hy_meeting_detail_cancle:
			whichBtn = 5;
			ConferenceReqUtil.doSetMeetingMemberReport(this, this, meeting.getId(), String.valueOf(App.getApp().getUserID()), 5, null);
			break;
		// 人脉
		case R.id.attendee_subtitle_item_meeting:
			Intent relationshipIntent = new Intent(this, RelationshipActivity.class);
			relationshipIntent.putExtra(ENavConsts.EMeetingDetail, meeting);
			relationshipIntent.putExtra("relationType", 1);
			startActivity(relationshipIntent);
			break;
		// 组织
		case R.id.attendee_org_subtitle_item_meeting:
			Intent intent = new Intent(this, RelationshipActivity.class);
			intent.putExtra(ENavConsts.EMeetingDetail, meeting);
			intent.putExtra("relationType", 2);
			startActivity(intent);
			break;
		// 需求标题
		case R.id.requirment_subtitle_item_meeting:
			Intent requirmentDataIntent = new Intent(this, RequirmentDataActivity.class);
			requirmentDataIntent.putExtra(ENavConsts.EMeetingDetail, meeting);
			startActivity(requirmentDataIntent);
			break;
		// 知识标题
		case R.id.knowledge_subtitle_item_meeting:
			Intent knowledgeDataIntent = new Intent(this, KnowLedgeDataActivity.class);
			knowledgeDataIntent.putExtra(ENavConsts.EMeetingDetail, meeting);
			startActivity(knowledgeDataIntent);
			break;
		// 转发
		case R.id.hy_layoutTitle_rightIconBtn1:
			// / showTransmitDialog();
			JTFile jtFile = new JTFile();
//			jtFile.mFileName = App.getNick()+"分享了[会议]";
			jtFile.fileName = meeting.getMeetingName();
			jtFile.mUrl = meeting.getPath();
			if ((meeting.getMeetingName()).length() > 50) {
				jtFile.mSuffixName = (meeting.getMeetingName()).substring(0, 50);
			} else {
				jtFile.mSuffixName = meeting.getMeetingName();
			}
			jtFile.mType = JTFile.TYPE_CONFERENCE;
			jtFile.mModuleType = 1;// 会议类型
			jtFile.mTaskId = meeting.getId() + "";
			if ((meeting.getMeetingDesc()).length() > 50) {
				jtFile.reserved1 = (meeting.getMeetingDesc()).substring(0, 50);
			} else {
				jtFile.reserved1 = meeting.getMeetingDesc();
			}
			// 弹出分享对话框
			FrameWorkUtils.showSharePopupWindow2(SquareActivity.this, jtFile);
			break;
		// 参会人 TODO RoadShowEditActivity
		case R.id.hy_layoutTitle_rightIconBtn2:
			Intent attendeesIntent = new Intent(this, AttendeesActivity.class);
			// Intent attendeesIntent = new Intent(this, TestFragment.class);
			attendeesIntent.putExtra(ENavConsts.EMeetingDetail, meeting);
			attendeesIntent.putExtra("AttendeeType", 1);
			startActivity(attendeesIntent);
			break;
		// 接受邀请
		case R.id.hy_meeting_accept_invitation:
			whichBtn = 1;
			ConferenceReqUtil.doSetMeetingMemberReport(this, this, meeting.getId(), String.valueOf(App.getUserID()), 1, null);
			break;
		// 拒绝邀请
		case R.id.hy_meeting_adjust_invitation:
			whichBtn = 2;
			ConferenceReqUtil.doSetMeetingMemberReport(this, this, meeting.getId(), String.valueOf(App.getUserID()), 2, null);
			break;
		}
	}

	/**
	 * 按返回键及返回箭头 跳转传进来的class
	 */
	private void goBackJump() {
		if (goBackJumpClass != null) {
			startActivity(new Intent(SquareActivity.this, goBackJumpClass));
		}
	}

	/**
	 * 转发到
	 */
	/*
	 * private void showTransmitDialog() { final Dialog dialog = new
	 * Dialog(this, R.style.transmeeting_dialog); View dialogBg =
	 * View.inflate(this, R.layout.hy_dialog_meeting_regist, null);
	 * 
	 * dialog.setContentView(dialogBg); jtFiles = new ArrayList<JTFile>(); //
	 * 转发到会议 ((TextView)
	 * dialogBg.findViewById(R.id.transmit_to_meeting_tv)).setOnClickListener
	 * (new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { JTFile jtFile = new JTFile();
	 * jtFile.mFileName = ""; jtFile.mUrl = meeting.getPath(); if
	 * ((meeting.getMeetingName()).length() > 50) { jtFile.mSuffixName =
	 * (meeting.getMeetingName()).substring(0, 50); } else { jtFile.mSuffixName
	 * = meeting.getMeetingName(); } jtFile.mType = JTFile.TYPE_CONFERENCE;
	 * jtFile.mTaskId = meeting.getId() + ""; if
	 * ((meeting.getMeetingDesc()).length() > 50) { jtFile.reserved1 =
	 * (meeting.getMeetingDesc()).substring(0, 50); } else { jtFile.reserved1 =
	 * meeting.getMeetingDesc(); } jtFiles.add(jtFile); //
	 * Toast.makeText(SquareActivity.this, "转发到会议", 0).show(); //
	 * ENavigate.startTransmitMeetingList(SquareActivity.this, jtFiles,
	 * meetingId); // dialog.dismiss();
	 * 
	 * FrameWorkUtils.showSharePopupWindow2(SquareActivity.this, jtFile); } });
	 * 
	 * // 转发到畅聊 ((TextView)
	 * dialogBg.findViewById(R.id.transmit_to_chat_tv)).setOnClickListener(new
	 * OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { JTFile jtFile = new JTFile();
	 * jtFile.mFileName = ""; jtFile.mUrl = meeting.getPath(); if
	 * ((meeting.getMeetingName()).length() > 50) { jtFile.mSuffixName =
	 * (meeting.getMeetingName()).substring(0, 50); } else { jtFile.mSuffixName
	 * = meeting.getMeetingName(); } jtFile.mType = 14; jtFile.mTaskId =
	 * String.valueOf(meeting.getId()); if ((meeting.getMeetingDesc()).length()
	 * > 50) { jtFile.reserved1 = (meeting.getMeetingDesc()).substring(0, 50); }
	 * else { jtFile.reserved1 = meeting.getMeetingDesc(); }
	 * ENavigate.startShareActivity(SquareActivity.this, jtFile); //
	 * Toast.makeText(SquareActivity.this, "转发到畅聊", 0).show(); dialog.dismiss();
	 * } });
	 * 
	 * dialog.show(); // WindowManager.LayoutParams params = //
	 * dialog.getWindow().getAttributes(); // params.width = 536; //
	 * dialog.getWindow().setAttributes(params); }
	 */

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 从signupInfoActivity返回的结果
		if (requestCode == SIGNUPINFOACTIVITY && data != null) {
			MMeetingRequiredSignupInfo signInfoResult = (MMeetingRequiredSignupInfo) data.getSerializableExtra("signInfoResult");
			if (signInfoResult != null) {
				ConferenceReqUtil.doImproveSignInformation(this, this, signInfoResult, null);
			}
		}else if(data!=null&&requestCode==START_MEETING_BRANCH_RESULT&&data.hasExtra("meetingDetial")){
			meeting = (MMeetingQuery) data.getSerializableExtra("meetingDetial");
			init();
		}
	}

	@Override
	public void initJabActionBar() {
		getActionBar().hide();
	}

	@Override
	public void onPrepared(String url) {

	}

	@Override
	public void onStarted(String url) {

	}

	@Override
	public void onSuccess(String url, JTFile jtFile) {
		Message msg = handler.obtainMessage();
		msg.what = 2;
		msg.obj = jtFile;
		handler.sendMessage(msg);
	}

	@Override
	public void onError(String url, int code, String errMsg) {

	}

	@Override
	public void onUpdate(String url, int progress) {

	}

	@Override
	public void onCanceled(String url) {

	}

	@Override
	protected void onDestroy() {
		if (downLoadAndOpen != null) {
			downLoadAndOpen.cancel(true);
		}
		handler.removeCallbacksAndMessages(null);
		super.onDestroy();
	}

	class DownLoadAndOpen extends AsyncTask<String, Void, File> {
		private String fileUrl;
		private String fileType;

		public DownLoadAndOpen(String fileUrl, String fileType) {
			this.fileUrl = fileUrl;
			this.fileType = fileType;
		}

		@Override
		protected File doInBackground(String... paramsArr) {
			if (null != downLoadAndOpen) {
				downLoadAndOpen.cancel(false);
			}
			downLoadAndOpen = this;

			File fileCache = null;
			File uploadFileDir = new File(Environment.getExternalStorageDirectory(), "/jt/fileCache");
			if (!uploadFileDir.exists()) {
				uploadFileDir.mkdirs();
			}
			String fileName = UUID.randomUUID().toString();
			try {
				fileCache = new File(uploadFileDir, fileName);
				if (!fileCache.exists()) {
					fileCache.createNewFile();
				}

				URL url = new URL(fileUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestProperty("Accept-Encoding", "identity");
				conn.connect();

				if (conn.getResponseCode() == 200) {
					// 创建输入流
					InputStream inputStream = conn.getInputStream();

					OutputStream outputStream = new FileOutputStream(fileCache);
					byte[] data = new byte[2048];
					int length = 0;
					while ((length = inputStream.read(data)) != -1) {
						outputStream.write(data, 0, length);
					}
					inputStream.close();
					outputStream.close();
				} else {
					fileCache = null;
				}

			} catch (Exception e) {
				fileCache = null;
			}
			return fileCache;
		}

		@Override
		protected void onPostExecute(File openFile) {
			if (!isCancelled()) {
				dismissLoadingDialog();
				if (null == openFile) {
					Toast.makeText(SquareActivity.this, "附件下载失败", Toast.LENGTH_SHORT).show();
					return;
				}
				Intent intent = null;
				String temp = fileType.replaceAll("^([\\s\\S]*)([tT][xX][tT])$", "$2");
				if (fileType.replaceAll("^([\\s\\S]*)([tT][xX][tT]) *$", "$2").length() == 3) {
					intent = new Intent("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Uri uri2 = Uri.fromFile(openFile);
					intent.setDataAndType(uri2, "text/plain");
				} else if (fileType.replaceAll("^([\\s\\S]*)([pP][dD][fF]) *$", "$2").length() == 3) {
					intent = new Intent("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Uri uri = Uri.fromFile(openFile);
					intent.setDataAndType(uri, "application/vnd.ms-powerpoint");

				} else if (fileType.replaceAll("^([\\s\\S]*)([dD][oO][cC]) *$", "$2").length() == 3) {
					intent = new Intent("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Uri uri = Uri.fromFile(openFile);
					intent.setDataAndType(uri, "application/msword");

				} else if (fileType.replaceAll("^([\\s\\S]*)([dD][oO][cD][xX]) *$", "$2").length() == 4) {
					intent = new Intent("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Uri uri = Uri.fromFile(openFile);
					intent.setDataAndType(uri, "application/msword");

				} else if (fileType.replaceAll("^([\\s\\S]*)([xX][lL][sS]) *$", "$2").length() == 3) {
					intent = new Intent("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Uri uri = Uri.fromFile(openFile);
					intent.setDataAndType(uri, "application/vnd.ms-excel");

				} else if (fileType.replaceAll("^([\\s\\S]*)([xX][lL][sS][xX]) *$", "$2").length() == 4) {
					intent = new Intent("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Uri uri = Uri.fromFile(openFile);
					intent.setDataAndType(uri, "application/vnd.ms-excel");

				} else if (fileType.replaceAll("^([\\s\\S]*)([pP][pP][tT]) *$", "$2").length() == 3) {
					intent = new Intent("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Uri uri = Uri.fromFile(openFile);
					intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
				} else if (fileType.replaceAll("^([\\s\\S]*)([pP][nN][gG]) *$", "$2").length() == 3 || fileType.replaceAll("^([\\s\\S]*)([jJ][pP][gG]) *$", "$2").length() == 3
						|| fileType.replaceAll("^([\\s\\S]*)([jJ][pP][eE][gG]) *$", "$2").length() == 4) {

					intent = new Intent("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Uri uri = Uri.fromFile(openFile);
					intent.setDataAndType(uri, "image/*");
				} else {
					intent = new Intent("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Uri uri = Uri.fromFile(openFile);
					intent.setDataAndType(uri, "*/*");
				}
				startActivity(intent);
			}
		}

	}
}
