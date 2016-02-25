/*
 * create by roffee 
 */
package com.tr.ui.conference.initiatorhy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.App;
import com.tr.R;
import com.tr.api.ConferenceReqUtil;
import com.tr.model.SimpleResult;
import com.tr.model.conference.JTFile2ForHY;
import com.tr.model.conference.MCalendarSelectDateTime;
import com.tr.model.conference.MFillinInfo;
import com.tr.model.conference.MMapAddress;
import com.tr.model.conference.MMeetingData;
import com.tr.model.conference.MMeetingMember;
import com.tr.model.conference.MMeetingOrgan;
import com.tr.model.conference.MMeetingPeople;
import com.tr.model.conference.MMeetingPic;
import com.tr.model.conference.MMeetingQuery;
import com.tr.model.conference.MMeetingSignLabel;
import com.tr.model.conference.MMeetingTime;
import com.tr.model.conference.MMeetingTopic;
import com.tr.model.conference.MMeetingTopicQuery;
import com.tr.model.conference.MPhotoItem;
import com.tr.model.conference.MSpeakerTopic;
import com.tr.model.home.MIndustry;
import com.tr.model.home.MIndustrys;
import com.tr.model.joint.AffairNode;
import com.tr.model.joint.ConnectionNode;
import com.tr.model.joint.KnowledgeNode;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.obj.AffairsMini;
import com.tr.model.obj.Connections;
import com.tr.model.obj.JTContactMini;
import com.tr.model.obj.RequirementMini;
import com.tr.model.user.OrganizationMini;
import com.tr.navigate.ENavigate;
import com.tr.ui.adapter.conference.GridviewFillinInfoSelectedAdapter;
import com.tr.ui.adapter.conference.GridviewFillinInfoSelectedAdapter.FillInfoOnCancelSelectListener;
import com.tr.ui.adapter.conference.GridviewFillinInfoToSelectAdapter;
import com.tr.ui.adapter.conference.GridviewFillinInfoToSelectAdapter.FillInfoOnToSelectListener;
import com.tr.ui.adapter.conference.GridviewInviteAttendeeAdapter;
import com.tr.ui.adapter.conference.GridviewInviteShareAdapter;
import com.tr.ui.adapter.conference.GridviewInviteSpeakerAdapter;
import com.tr.ui.common.JointResourceFragment.ResourceType;
import com.tr.ui.conference.common.BaseActivity;
import com.tr.ui.conference.myhy.ui.MyMeetingActivity;
import com.tr.ui.conference.square.MRoadShowCacheFiles;
import com.tr.ui.widgets.EProgressDialog;
import com.utils.common.EConsts;
import com.utils.common.TaskIDMaker;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;
import com.utils.time.Util;

public class InitiatorHYActivity extends BaseActivity implements IBindData, FillInfoOnToSelectListener, FillInfoOnCancelSelectListener {

	private Context context;
	public final static int requestCode_time = 1;
	public final static int requestCode_address = 2;
	public final static int requestCode_introduce = 3;
	public final static int requestCode_inviteAttend = 4;
	public final static int requestCode_inviteSpeaker = 5;
	public final static int requestCode_share = 6;
	public final static int requestCode_buniess = 7;

	public final static int ATTEND_GV_CLUMWIDTH = 120;
	public final static int ATTEND_GV_WIDTHSPACE = 20;
	// title
	private TextView titleText, titleRightTextBtn1, titleRightTextBtn2;
	// name
	private EditText nameEdit;
	// time
	private TextView timeText1, timeText2, timeText3;
	// 行业
	private TextView busniessTv;
	private MIndustrys mIndustrys;
	// share
	private LinearLayout sharelabel, share4ContentGroupLabel, sharePeoplehubLayout, shareDemandLaout, shareKnowleadgeLayout;
	private GridView inviteAttendeeGridView, inviteShareGridView;
	private GridviewInviteAttendeeAdapter inviteAttendeeGvAdp;
	private GridviewInviteSpeakerAdapter inviteSpeakerGvAdp;
	private TextView peopleHubText, demandText1, demandText2, demandText3, knowleadgeText1, knowleadgeText2, knowleadgeText3;
	// address introduce
	private TextView addressText, introduceText;
	// speacker
	private GridView inviteSpreakerGridView;
	// secrecyHY applyPeopleNum
	private TextView secrecyHYText;
	private EditText applyPeopleNumEdit;
	private ImageView secrecyHYSwitchIv, applyPeopleNumSwitchIv;
	private boolean secrecyHYSwitch = false, applyPeopleNumSwitch = false;
	// fillin info
	private FrameLayout fillInfoLabel;
	private LinearLayout fillInfoSelectedLayout;// fillInfoLayout
	private GridView fillinInfoSelectedGridView, fillinInfoToSelectGridView;
	private GridviewFillinInfoSelectedAdapter fillinInfoSelectedGvAdp;
	private GridviewFillinInfoToSelectAdapter fillinInfoToSelectGvAdp;
	private EditText selfDefineEdit;

	private MMapAddress mapAddress;
	private EProgressDialog prgDialog;
	/** 本类会议控制 0-initiatorHY 发起(创建) 1-save draft 存草 */
	private int createType;
	private MMeetingQuery alterMeetingData;

	/** 会议介绍上传文件的集合 */
	private ArrayList<JTFile2ForHY> mMeetingFileList;
	/** 会议介绍上传的图片的集合 */
	private ArrayList<MMeetingPic> mMeetingPicList;

	private int old_isSign;
	private String old_signDistance;
	private String old_attendMeetTime;
	private long old_my_id = 0;
	private boolean is_mod = false;
	private LinearLayout shareOrgLayout;
	private TextView orgTextTv;
	private ArrayList<String> industrysList;
	private String taskId;

	/** 对接资源所用到的关键字 */
	private String peopleRelatedkeyWord = "";
	private String orgRelatedkeyWord = "";
	private String knowledgeRelatedkeyWord = "";
	private String requarementRelatedkeyWord = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hy_activity_initiaror);
		context = this;
		InitiatorDataCache.getInstance().releaseAll();
		InitiatorDataCache.getInstance().onlyShowMyOrg = false;
		findAndInitViews();
	}

	private void findAndInitViews() {
		findAndInitTitleViews();
		findAndInitNameViews();
		findAndInitTimeViews();
		// 行业布局
		findAndInitBusniessViews();
		findAndInitInviteAttendeeViews();
		findAndInitShare4ConentViews();
		findAndInitAddressViews();
		findAndInitIntroduceViews();
		findAndInitInviteSpeakerViews();
		findAndInitSecrecyHYViews();
		findAndInitApplyPeopleNumViews();
		findAndInitFillinInfoViews();
		alterHYIntentData();
	}

	private Context getContext() {
		return context;
	}

	private void findAndInitTitleViews() {
		LinearLayout backBtn = (LinearLayout) findViewById(R.id.hy_layoutTitle_backBtn);
		titleText = (TextView) findViewById(R.id.hy_layoutTitle_title);
		titleRightTextBtn1 = (TextView) findViewById(R.id.hy_layoutTitle_rightTextBtn1);
		titleRightTextBtn2 = (TextView) findViewById(R.id.hy_layoutTitle_rightTextBtn2);
		titleText.setText("发起活动");
		backBtn.setOnClickListener(new MyOnClickListener(ClickType.TYPE_CLICK_NORMAL));
		titleRightTextBtn1.setOnClickListener(new MyOnClickListener(ClickType.TYPE_CLICK_NORMAL));
		titleRightTextBtn2.setOnClickListener(new MyOnClickListener(ClickType.TYPE_CLICK_NORMAL));
	}

	private void findAndInitNameViews() {
		LinearLayout label = (LinearLayout) findViewById(R.id.hy_actInitiaror_inputName_layout);
		TextView title = (TextView) label.findViewById(R.id.hy_layout_input_1line_edittext_title);
		nameEdit = (EditText) label.findViewById(R.id.hy_layout_input_1line_edittext);
		nameEdit.setSingleLine(true);

		title.setText("名称");
	}

	private void findAndInitTimeViews() {
		LinearLayout label = (LinearLayout) findViewById(R.id.hy_actInitiaror_pickTime_layout);
		TextView title = (TextView) label.findViewById(R.id.hy_layout_picker_3line_text_title);
		timeText1 = (TextView) label.findViewById(R.id.hy_layout_picker_3line_text_1);
		timeText2 = (TextView) label.findViewById(R.id.hy_layout_picker_3line_text_2);
		timeText3 = (TextView) label.findViewById(R.id.hy_layout_picker_3line_text_3);
		LinearLayout labelClick = (LinearLayout) label.findViewById(R.id.hy_layout_picker_3line_text_labelclick);
		title.setText("时间");
		labelClick.setOnClickListener(new MyOnClickListener(ClickType.TYPE_CLICK_TIME_LABEL));
	}

	private void findAndInitBusniessViews() {
		LinearLayout label = (LinearLayout) findViewById(R.id.hy_actInitiaror_busniess_layout);
		TextView title = (TextView) label.findViewById(R.id.hy_layout_picker_3line_text_title);
		busniessTv = (TextView) label.findViewById(R.id.hy_layout_picker_3line_text_1);
		LinearLayout labelClick = (LinearLayout) label.findViewById(R.id.hy_layout_picker_3line_text_labelclick);
		title.setText("行业");
		labelClick.setOnClickListener(new MyOnClickListener(ClickType.TYPE_CLICK_BUSNIESS_LABEL));
	}

	private void findAndInitInviteAttendeeViews() {
		LinearLayout label = (LinearLayout) findViewById(R.id.hy_actInitiaror_pickerInviteAttendee_layout);
		mScrollView1 = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
		TextView title = (TextView) label.findViewById(R.id.hy_layout_picker_1line_images_title);
		TextView subtitle = (TextView) label.findViewById(R.id.hy_layout_picker_1line_images_subtitle);
		inviteAttendeeGridView = (GridView) label.findViewById(R.id.hy_layout_picker_1line_images_gridview);
		inviteAttendeeGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		title.setText("邀请参会人");
		subtitle.setText("");

		setGridViewParam(inviteAttendeeGridView, Util.DensityUtil.dip2px(InitiatorHYActivity.this, 60), Util.DensityUtil.dip2px(InitiatorHYActivity.this, 8), 1);
		inviteAttendeeGvAdp = new GridviewInviteAttendeeAdapter(this);
		inviteAttendeeGridView.setAdapter(inviteAttendeeGvAdp);
		inviteAttendeeGridView.setOnItemClickListener(new MyOnItemClickListener(ItemClickType.TYPE_ITEM_CLICK_ATTENDEE));
	}

	private void findAndInitShare4ConentViews() {
		share4ContentGroupLabel = (LinearLayout) findViewById(R.id.hy_actInitiaror_pickerShare_4content_layout);
		// peoplehub
		sharePeoplehubLayout = (LinearLayout) share4ContentGroupLabel.findViewById(R.id.hy_layout_pickerSharePeopleHub_layout);
		TextView ptitle = (TextView) sharePeoplehubLayout.findViewById(R.id.hy_layout_picker_1line_text_title);
		peopleHubText = (TextView) sharePeoplehubLayout.findViewById(R.id.hy_layout_picker_1line_text);
		LinearLayout plabelClick = (LinearLayout) sharePeoplehubLayout.findViewById(R.id.hy_layout_picker_1line_text_labelclick);
		ptitle.setText("人");
		peopleHubText.setText("");
		plabelClick.setOnClickListener(new MyOnClickListener(ClickType.TYPE_CLICK_SHARE_PEOPLEHUB_LABEL));

		shareOrgLayout = (LinearLayout) share4ContentGroupLabel.findViewById(R.id.hy_layout_pickerShareOrg_layout);
		TextView orgtitleTv = (TextView) shareOrgLayout.findViewById(R.id.hy_layout_picker_1line_text_title);
		orgTextTv = (TextView) shareOrgLayout.findViewById(R.id.hy_layout_picker_1line_text);
		LinearLayout orgabelClick = (LinearLayout) shareOrgLayout.findViewById(R.id.hy_layout_picker_1line_text_labelclick);
		orgtitleTv.setText("组织");
		orgTextTv.setText("");
		orgabelClick.setOnClickListener(new MyOnClickListener(ClickType.TYPE_CLICK_SHARE_ORG_LABEL));

		// DEMAND
		shareDemandLaout = (LinearLayout) share4ContentGroupLabel.findViewById(R.id.hy_layout_pickerShareDemand_layout);
		TextView dtitle = (TextView) shareDemandLaout.findViewById(R.id.hy_layout_picker_3line_text_title);
		demandText1 = (TextView) shareDemandLaout.findViewById(R.id.hy_layout_picker_3line_text_1);
		demandText2 = (TextView) shareDemandLaout.findViewById(R.id.hy_layout_picker_3line_text_2);
		demandText3 = (TextView) shareDemandLaout.findViewById(R.id.hy_layout_picker_3line_text_3);
		LinearLayout dlabelClick = (LinearLayout) shareDemandLaout.findViewById(R.id.hy_layout_picker_3line_text_labelclick);
		dtitle.setText("事件");
		demandText1.setText("");
		dlabelClick.setOnClickListener(new MyOnClickListener(ClickType.TYPE_CLICK_SHARE_DEMAND_LABEL));
		// DEMAND
		shareKnowleadgeLayout = (LinearLayout) share4ContentGroupLabel.findViewById(R.id.hy_layout_pickerShareKnowleadge_layout);
		TextView ktitle = (TextView) shareKnowleadgeLayout.findViewById(R.id.hy_layout_picker_3line_text_title);
		knowleadgeText1 = (TextView) shareKnowleadgeLayout.findViewById(R.id.hy_layout_picker_3line_text_1);
		knowleadgeText2 = (TextView) shareKnowleadgeLayout.findViewById(R.id.hy_layout_picker_3line_text_2);
		knowleadgeText3 = (TextView) shareKnowleadgeLayout.findViewById(R.id.hy_layout_picker_3line_text_3);
		LinearLayout klabelClick = (LinearLayout) shareKnowleadgeLayout.findViewById(R.id.hy_layout_picker_3line_text_labelclick);
		ktitle.setText("知识");
		knowleadgeText1.setText("");
		klabelClick.setOnClickListener(new MyOnClickListener(ClickType.TYPE_CLICK_SHARE_KNOWLEADGE_LABEL));
	}

	private void findAndInitAddressViews() {
		LinearLayout label = (LinearLayout) findViewById(R.id.hy_actInitiaror_pickerAddress_layout);
		TextView title = (TextView) label.findViewById(R.id.hy_layout_picker_1line_text_title);
		addressText = (TextView) label.findViewById(R.id.hy_layout_picker_1line_text);
		LinearLayout labelClick = (LinearLayout) label.findViewById(R.id.hy_layout_picker_1line_text_labelclick);
		title.setText("地点");
		addressText.setText("");
		labelClick.setOnClickListener(new MyOnClickListener(ClickType.TYPE_CLICK_ADDRESS_LABEL));
	}

	private void findAndInitIntroduceViews() {
		LinearLayout label = (LinearLayout) findViewById(R.id.hy_actInitiaror_pickerIntroduce_layout);
		TextView title = (TextView) label.findViewById(R.id.hy_layout_picker_1line_text_title);
		introduceText = (TextView) label.findViewById(R.id.hy_layout_picker_1line_text);
		LinearLayout labelClick = (LinearLayout) label.findViewById(R.id.hy_layout_picker_1line_text_labelclick);
		title.setText("介绍");
		introduceText.setText("");
		labelClick.setOnClickListener(new MyOnClickListener(ClickType.TYPE_CLICK_INTRODUCE_LABEL));
	}

	private void findAndInitInviteSpeakerViews() {
		LinearLayout label = (LinearLayout) findViewById(R.id.hy_actInitiaror_pickerInviteSpeaker_layout);
		mScrollView2 = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
		TextView title = (TextView) label.findViewById(R.id.hy_layout_picker_1line_images_title);
		TextView subtitle = (TextView) label.findViewById(R.id.hy_layout_picker_1line_images_subtitle);
		inviteSpreakerGridView = (GridView) label.findViewById(R.id.hy_layout_picker_1line_images_gridview);
		inviteSpreakerGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		title.setText("邀请主讲人");
		subtitle.setText("");

		setGridViewParam(inviteSpreakerGridView, Util.DensityUtil.dip2px(InitiatorHYActivity.this, 60), Util.DensityUtil.dip2px(InitiatorHYActivity.this, 8), 1);

		inviteSpeakerGvAdp = new GridviewInviteSpeakerAdapter(this);
		inviteSpreakerGridView.setAdapter(inviteSpeakerGvAdp);
		inviteSpreakerGridView.setOnItemClickListener(new MyOnItemClickListener(ItemClickType.TYPE_ITEM_CLICK_SPEAKER));
	}

	private void findAndInitSecrecyHYViews() {
		LinearLayout label = (LinearLayout) findViewById(R.id.hy_actInitiaror_pickerSecrecyHY_layout);
		TextView title = (TextView) label.findViewById(R.id.hy_layout_switch_1line_text_title);
		secrecyHYText = (TextView) label.findViewById(R.id.hy_layout_switch_1line_text);
		secrecyHYSwitchIv = (ImageView) label.findViewById(R.id.hy_layout_switch_1line_text_switchicon);
		title.setText("保密会议");
		secrecyHYText.setText("");
		secrecyHYSwitchIv.setOnClickListener(new MyOnClickListener(ClickType.TYPE_CLICK_SECRECYHY_LABEL));
	}

	private void findAndInitApplyPeopleNumViews() {
		LinearLayout label = (LinearLayout) findViewById(R.id.hy_actInitiaror_pickerApplyPeopleNum_layout);
		TextView title = (TextView) label.findViewById(R.id.hy_layout_switch_1line_text_title);
		applyPeopleNumEdit = (EditText) label.findViewById(R.id.hy_layout_switch_1line_edittext);
		applyPeopleNumSwitchIv = (ImageView) label.findViewById(R.id.hy_layout_switch_1line_text_switchicon); // CheckBox
																												// switchIcon
																												// =
		title.setText("报名人数");
		applyPeopleNumEdit.setText("100");
		applyPeopleNumEdit.setEnabled(false);
		applyPeopleNumSwitchIv.setOnClickListener(new MyOnClickListener(ClickType.TYPE_CLICK_APPLUPEOPLENUM_LABEL));
	}

	private void findAndInitFillinInfoViews() {
		fillInfoLabel = (FrameLayout) findViewById(R.id.hy_actInitiaror_fillin_info_layout);
		fillinInfoSelectedGridView = (GridView) fillInfoLabel.findViewById(R.id.hy_layout_fillinInfo_selected_gridview);
		fillinInfoToSelectGridView = (GridView) fillInfoLabel.findViewById(R.id.hy_layout_fillinInfo_toselect_gridview);
		fillInfoSelectedLayout = (LinearLayout) fillInfoLabel.findViewById(R.id.hy_layout_fillinInfo_selected_layout);
		LinearLayout addEditLabel = (LinearLayout) findViewById(R.id.hy_layout_fillinInfo_add_layout);
		selfDefineEdit = (EditText) addEditLabel.findViewById(R.id.hy_layout_input_1line_add_edittext);
		ImageView addBtn = (ImageView) addEditLabel.findViewById(R.id.hy_layout_input_1line_add_rightIconBtn);

		addBtn.setOnClickListener(new MyOnClickListener(ClickType.TYPE_CLICK_FILLINFO_LABEL));
		fillinInfoSelectedGvAdp = new GridviewFillinInfoSelectedAdapter(this);
		fillinInfoSelectedGridView.setAdapter(fillinInfoSelectedGvAdp);

		fillinInfoToSelectGvAdp = new GridviewFillinInfoToSelectAdapter(this);
		fillinInfoToSelectGridView.setAdapter(fillinInfoToSelectGvAdp);
	}

	private void setGridViewParam(GridView gridview, int itemWidth, int horizontalSpacing, int size) {
		itemWidth = itemWidth - 2;
		LayoutParams params = new LayoutParams(size * (itemWidth + horizontalSpacing), LayoutParams.WRAP_CONTENT);
		gridview.setLayoutParams(params);
		gridview.setColumnWidth(itemWidth);
		gridview.setHorizontalSpacing(horizontalSpacing);
		gridview.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		gridview.setNumColumns(size);
		gridview.setGravity(Gravity.CENTER);
	}

	public void refreshInviteGV(int type) {
		if (type == 0) {
			setGridViewParam(inviteAttendeeGridView, Util.DensityUtil.dip2px(InitiatorHYActivity.this, 60), Util.DensityUtil.dip2px(InitiatorHYActivity.this, 8), inviteAttendeeGvAdp.getListSize());
			inviteAttendeeGvAdp.notifyDataSetChanged();
			Message msg = Message.obtain(mHandler, 1);
			mHandler.sendMessage(msg);
		} else {
			setGridViewParam(inviteSpreakerGridView, Util.DensityUtil.dip2px(InitiatorHYActivity.this, 60), Util.DensityUtil.dip2px(InitiatorHYActivity.this, 8), inviteSpeakerGvAdp.getListSize());
			inviteSpeakerGvAdp.notifyDataSetChanged();
			Message msg = Message.obtain(mHandler, 2);
			mHandler.sendMessage(msg);
		}
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				mScrollView1.fullScroll(ScrollView.FOCUS_RIGHT);
				mScrollView1.invalidate();
				break;
			case 2:
				mScrollView2.fullScroll(ScrollView.FOCUS_RIGHT);
				mScrollView2.invalidate();
				break;
			}
		}
	};

	public void releaseCacheData() {
		InitiatorDataCache.getInstance().releaseAll();
		MRoadShowCacheFiles.getInstance().releaseAll();
	}

	public void finishActivity() {
		releaseCacheData();
		this.finish();
	}

	private enum ClickType {
		TYPE_CLICK_NORMAL, TYPE_CLICK_TIME_LABEL, TYPE_CLICK_BUSNIESS_LABEL, TYPE_CLICK_SHARE_PEOPLEHUB_LABEL, TYPE_CLICK_SHARE_ORG_LABEL, TYPE_CLICK_SHARE_DEMAND_LABEL, TYPE_CLICK_SHARE_KNOWLEADGE_LABEL, TYPE_CLICK_ADDRESS_LABEL, TYPE_CLICK_INTRODUCE_LABEL, TYPE_CLICK_SECRECYHY_LABEL, TYPE_CLICK_APPLUPEOPLENUM_LABEL, TYPE_CLICK_FILLINFO_LABEL
	}

	private enum ItemClickType {
		TYPE_ITEM_CLICK_ATTENDEE, TYPE_ITEM_CLICK_SHARE, TYPE_ITEM_CLICK_SPEAKER, TYPE_ITEM_CLICK_FILLINFO_SELECTED, TYPE_ITEM_CLICK_FILLINFO_TOSELECT
	}

	private int retry = 50;
	private MMeetingQuery meetingQuery;
	private HorizontalScrollView mScrollView1;
	private HorizontalScrollView mScrollView2;
	/**结束的会议重新发起*/
	private boolean overMeetingReCreate;

	/** 准备发起会议 */
	private void prepareCreateHY() {
		if (Util.isNull(nameEdit.getEditableText().toString())) {
			dismissPrgDialog();
			titleRightTextBtnSetClickable(true);
			Toast.makeText(this, "会议名称必填", Toast.LENGTH_SHORT).show();
			return;
		}
		if (nameEdit.getEditableText().toString().length() > 255) {
			dismissPrgDialog();
			titleRightTextBtnSetClickable(true);
			Toast.makeText(this, "会议名称长度超出255字数限制", Toast.LENGTH_SHORT).show();
			return;
		}
		if (Util.isNull(InitiatorDataCache.getInstance().timeSelectetedList) || (timeText1.getText().equals("必填"))) {
			dismissPrgDialog();
			titleRightTextBtnSetClickable(true);
			Toast.makeText(this, "会议时间必填", Toast.LENGTH_SHORT).show();
			return;
		}
		if (mIndustrys == null || (mIndustrys != null && mIndustrys.getListIndustry() != null && mIndustrys.getListIndustry().size() <= 0)) {
			dismissPrgDialog();
			titleRightTextBtnSetClickable(true);
			Toast.makeText(this, "行业必填", Toast.LENGTH_SHORT).show();
			return;
		}
		if (Util.isNull(applyPeopleNumEdit.getText().toString())) {
			dismissPrgDialog();
			titleRightTextBtnSetClickable(true);
			Toast.makeText(this, "报名人数不能为0", Toast.LENGTH_SHORT).show();
			return;
		}
		createHY(createType);
	}
	
	private void dismissPrgDialog(){
		if (prgDialog!=null&&prgDialog.isShowing()) {
			prgDialog.dismiss();
		}
	}
	
	/**
	 * 更新会议发起前检查
	 */
	private void prepareUpdateHY() {
		if (Util.isNull(nameEdit.getEditableText().toString())) {
			dismissPrgDialog();
			titleRightTextBtnSetClickable(true);
			Toast.makeText(this, "会议名称必填", Toast.LENGTH_SHORT).show();
			return;
		}
		if (nameEdit.getEditableText().toString().length() > 255) {
			dismissPrgDialog();
			titleRightTextBtnSetClickable(true);
			Toast.makeText(this, "会议名称长度超出255字数限制", Toast.LENGTH_SHORT).show();
			return;
		}
		if (Util.isNull(InitiatorDataCache.getInstance().timeSelectetedList) || (timeText1.getText().equals("必填"))) {
			dismissPrgDialog();
			titleRightTextBtnSetClickable(true);
			Toast.makeText(this, "会议时间必填", Toast.LENGTH_SHORT).show();
			return;
		}
		if (mIndustrys == null || (mIndustrys != null && mIndustrys.getListIndustry() != null && mIndustrys.getListIndustry().size() <= 0)) {
			dismissPrgDialog();
			titleRightTextBtnSetClickable(true);
			Toast.makeText(this, "行业必填", Toast.LENGTH_SHORT).show();
			return;
		}
		if (Util.isNull(applyPeopleNumEdit.getText().toString())) {
			dismissPrgDialog();
			titleRightTextBtnSetClickable(true);
			Toast.makeText(this, "报名人数不能为0", Toast.LENGTH_SHORT).show();
			return;
		}
		
		updateAlterHY();
	}

	/**
	 * 发起会议
	 * 
	 * @param createType
	 * @return
	 */
	private int createHY(int createType) {
		meetingQuery = new MMeetingQuery();
		long uid = Long.valueOf(App.getUserID());
		/** meetingName 会议名称 */
		meetingQuery.setMeetingName(nameEdit.getEditableText().toString());
		/** 会议地点信息 */
		if (null != mapAddress) {
			meetingQuery.setMeetingAddress(mapAddress.address);
			meetingQuery.setMeetingAddressPosX(String.valueOf(mapAddress.longitude));
			meetingQuery.setMeetingAddressPosY(String.valueOf(mapAddress.latitude));
			meetingQuery.setProvince(mapAddress.province);
			meetingQuery.setCity(mapAddress.city);
			meetingQuery.setTown(mapAddress.town);
		}

		/** 会议开始时间&会议结束时间 */
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (!Util.isNull(InitiatorDataCache.getInstance().timeSelectetedList)) {
			long aaa = 0;
			long bbb = 0;
			for (int nimabide = 0; nimabide < InitiatorDataCache.getInstance().timeSelectetedList.size(); nimabide++) {
				MCalendarSelectDateTime fisrtDT = InitiatorDataCache.getInstance().timeSelectetedList.get(nimabide);
				if (0 == aaa) {
					aaa = fisrtDT.startDate;
				}
				if (aaa > fisrtDT.startDate) {
					aaa = fisrtDT.startDate;
				}
				if (0 == bbb) {
					bbb = fisrtDT.endDate;
				}
				if (bbb < fisrtDT.endDate) {
					bbb = fisrtDT.endDate;
				}
				if (0 != aaa && 0 != bbb) {
					meetingQuery.setStartTime(dateFormat.format(new Date(aaa)));
					meetingQuery.setEndTime(dateFormat.format(new Date(bbb)));
				}
			}
		}

		/** 创建时间 */
		// meetingQuery.setCreateImage(App.getUser().getImage());
		/** 创建人id */
		meetingQuery.setCreateId(uid);
		/** 创建人名称 */
		meetingQuery.setCreateName(App.getNick());

		/** 会议行业 */
		// meetingQuery.setListIndustry(mIndustrys.getListIndustry());
		meetingQuery.setListIndustry(industrysList);

		/** 会议图片 */
		if (mMeetingPicList != null) {
			meetingQuery.setListMeetingPic(mMeetingPicList);
		}
		/** 会议音频视频等附件 */
		if (mMeetingFileList != null) {
			meetingQuery.setListMeetingFile(mMeetingFileList);
		}

		/** isSecrecy 是否保密 */
		meetingQuery.setIsSecrecy(secrecyHYSwitch);
		if (false == secrecyHYSwitch) {
			String applyPeopleNum = applyPeopleNumEdit.getText().toString();
			if (Util.isNull(applyPeopleNum)) {
				applyPeopleNum = "0";
			}
			int i = Integer.valueOf(applyPeopleNum);
			/** 参会人数 */
			meetingQuery.setMemberCount(i);

		}
		// country 01 固定01
		meetingQuery.setCountry(0L);
		// meetingStatus 会议状态 0：草稿，1：发起,2会议进行中，3会议结束
		if (createType == 0) {
			// 发起会议
			meetingQuery.setMeetingStatus(1);
		} else {
			// 存草稿
			meetingQuery.setMeetingStatus(0);
		}

		/** 会议描述 */
		meetingQuery.setMeetingDesc(InitiatorDataCache.getInstance().introduce.contentText);
		// 将介绍文本清楚
		InitiatorDataCache.getInstance().introduce.contentText = "";
		/** meetingType 会议类型 */
		meetingQuery.setMeetingType(0);
		// taskId 会议 附件id
		meetingQuery.setTaskId(taskId);

		Calendar c = Calendar.getInstance();
		/** 会议 创建时间 */
		meetingQuery.setCreateTime(dateFormat.format(new Date(c.getTimeInMillis())));

		// listMeetingTime
		List<MMeetingTime> listMeetingTime = new ArrayList<MMeetingTime>();
		if (!Util.isNull(InitiatorDataCache.getInstance().timeSelectetedList)) {
			for (MCalendarSelectDateTime item : InitiatorDataCache.getInstance().timeSelectetedList) {
				MMeetingTime mt = new MMeetingTime();
				mt.setStartTime(dateFormat.format(new Date(item.startDate)));
				mt.setEndTime(dateFormat.format(new Date(item.endDate)));
				listMeetingTime.add(mt);
			}
		}

		/** 参会人列表：自己+参会人+主讲人 */
		List<MMeetingMember> listMeetingMember = new ArrayList<MMeetingMember>();
		// 自己
		MMeetingMember mmMyself = new MMeetingMember();
		mmMyself.setMemberId(uid);
		mmMyself.setMemberName(App.getNick());
		mmMyself.setMemberPhoto(App.getUser().getImage());
		mmMyself.setMemberType(2);
		mmMyself.setAttendMeetStatus(1);
		mmMyself.setAttendMeetType(0);
		mmMyself.setExcuteMeetSign(0);
		mmMyself.setMemberMeetStatus(0);
		mmMyself.setIsShowInvitation(1);
		mmMyself.setIsSign(0);
		listMeetingMember.add(mmMyself);
		// 参会人
		Iterator<Entry<String, JTContactMini>> attendSelMap = InitiatorDataCache.getInstance().inviteAttendSelectedMap.entrySet().iterator();
		while (attendSelMap.hasNext()) {
			Map.Entry entry = (Map.Entry) attendSelMap.next();
			JTContactMini item = (JTContactMini) entry.getValue();
			MMeetingMember mmAttend = new MMeetingMember();
			if (null != item.id) {
				if (false == item.id.isEmpty()) {
					mmAttend.setMemberId(Long.valueOf(item.id));
				}
			}
			mmAttend.setMemberName(item.name);
			mmAttend.setMemberPhoto(item.image);
			mmAttend.setMemberType(1);
			mmAttend.setAttendMeetStatus(0);
			mmAttend.setAttendMeetType(0);
			mmAttend.setExcuteMeetSign(0);
			mmAttend.setMemberMeetStatus(0);
			mmAttend.setIsShowInvitation(1);
			mmAttend.setIsSign(0);
			listMeetingMember.add(mmAttend);
		}
		/** 主讲人 */
		Iterator<Entry<String, JTContactMini>> speakerdSleMap = InitiatorDataCache.getInstance().inviteSpeakerSelectedMap.entrySet().iterator();
		while (speakerdSleMap.hasNext()) {
			Map.Entry entry = (Map.Entry) speakerdSleMap.next();
			JTContactMini item = (JTContactMini) entry.getValue();
			MMeetingMember mmSpeaker = new MMeetingMember();
			if (null != item.id) {
				if (false == item.id.isEmpty()) {
					mmSpeaker.setMemberId(Long.valueOf(item.id));
				}
			}
			mmSpeaker.setMemberName(item.name);
			mmSpeaker.setMemberPhoto(item.image);
			mmSpeaker.setMemberType(0);
			mmSpeaker.setAttendMeetStatus(1);
			mmSpeaker.setAttendMeetType(0);
			mmSpeaker.setExcuteMeetSign(0);
			mmSpeaker.setMemberMeetStatus(0);
			mmSpeaker.setIsShowInvitation(1);
			mmSpeaker.setIsSign(0);
			listMeetingMember.add(mmSpeaker);
		}

		/** List<MMeetingData>存放关联的知识和需求 */
		List<MMeetingData> listMeetingData = new ArrayList<MMeetingData>();

		/** 关联的需求 */
		Iterator<Entry<Integer, RequirementMini>> demandSleMap = InitiatorDataCache.getInstance().shareDemandSelectedMap.entrySet().iterator();
		while (demandSleMap.hasNext()) {
			Map.Entry entry = (Map.Entry) demandSleMap.next();
			RequirementMini item = (RequirementMini) entry.getValue();
			MMeetingData md = new MMeetingData();
			md.setDataId(item.mID);
			md.setDataName(item.mTitle);
			md.setDataReqType(item.getReqType() + 12);
			md.setDataType(0);
			listMeetingData.add(md);
		}
		/** 关联的知识 */
		Iterator<Entry<Long, KnowledgeMini2>> knowleadgeSelMap = InitiatorDataCache.getInstance().shareKnowleadgeSelectedMap.entrySet().iterator();
		while (knowleadgeSelMap.hasNext()) {
			Map.Entry<Long, KnowledgeMini2> entry = knowleadgeSelMap.next();
			KnowledgeMini2 item = entry.getValue();
			MMeetingData md = new MMeetingData();
			md.setDataId(item.id);
			md.setDataName(item.title);
			md.setDataReqType(item.type);
			md.setDataUrl(item.source);
			md.setDataType(1);
			listMeetingData.add(md);
		}

		/** 关联的人脉 */
		List<MMeetingPeople> listMeetingPeople = new ArrayList<MMeetingPeople>();
		Iterator<Entry<String, JTContactMini>> peoplehubSleMap = InitiatorDataCache.getInstance().sharePeopleHubSelectedMap.entrySet().iterator();
		while (peoplehubSleMap.hasNext()) {
			Map.Entry entry = (Map.Entry) peoplehubSleMap.next();
			JTContactMini item = (JTContactMini) entry.getValue();
			MMeetingPeople mp = new MMeetingPeople();
			if (null != item.id) {
				if (false == item.id.isEmpty()) {
					mp.setPeopleId(Long.valueOf(item.id));
				}
			}
			if (item.isOnline) {
				mp.setPeopleType(2);
			}else {
				mp.setPeopleType(1);
			}
			mp.setPeopleName(item.name);
			mp.setPeopleDesc(item.getCompany());
			mp.setPeoplePhoto(item.image);
			listMeetingPeople.add(mp);
		}

		/** 关联的组织 */
		List<MMeetingOrgan> listMeetingOrgans = new ArrayList<MMeetingOrgan>();
		Iterator<Entry<String, OrganizationMini>> orghubSleMap = InitiatorDataCache.getInstance().shareOrgHubSelectedMap.entrySet().iterator();
		while (orghubSleMap.hasNext()) {
			Map.Entry entry = (Map.Entry) orghubSleMap.next();
			OrganizationMini item = (OrganizationMini) entry.getValue();
			MMeetingOrgan mo = new MMeetingOrgan();
			if (null != item.id) {
				if (false == item.id.isEmpty()) {
					mo.setOrganId(item.id);
				}
			}
			if (item.isOnline) {
				mo.setOrganType(1);
			}else {
				mo.setOrganType(2);
			}
			mo.setOrganName(item.fullName);
			mo.setOrganPhoto(item.logo);
			listMeetingOrgans.add(mo);
		}
		meetingQuery.setListMeetingOrgan(listMeetingOrgans);

		/** 议题详情列表(详细) */
		List<MMeetingTopicQuery> listMeetingTopicQuery = new ArrayList<MMeetingTopicQuery>();
		/** 主讲人Map集合 */
		Iterator<Entry<String, JTContactMini>> speakerMap = InitiatorDataCache.getInstance().inviteSpeakerSelectedMap.entrySet().iterator();
		/** 遍历添加所有主讲人的议题集合 */
		while (speakerMap.hasNext()) {
			/** 设置为 有议题会议 */
			meetingQuery.setMeetingType(1);
			Map.Entry entry = (Map.Entry) speakerMap.next();
			JTContactMini jtContactMini = (JTContactMini) entry.getValue();
			/** 此主讲人的议题集合 */
			List<MMeetingTopicQuery> speakerTopicQuery = jtContactMini.lisMeetingTopicQuery;
			listMeetingTopicQuery.addAll(speakerTopicQuery);
			meetingQuery.setMeetingType(1);
		}
		/** 设置主讲人议题详细列表 */
		meetingQuery.setListMeetingTopicQuery(listMeetingTopicQuery);

		List<MMeetingTopic> listMeetingTopic = new ArrayList<MMeetingTopic>();
		Iterator<Entry<String, JTContactMini>> speakerSelMap = InitiatorDataCache.getInstance().inviteSpeakerSelectedMap.entrySet().iterator();
		while (speakerSelMap.hasNext()) {
			meetingQuery.setMeetingType(1);
			Map.Entry entry = (Map.Entry) speakerSelMap.next();
			JTContactMini item = (JTContactMini) entry.getValue();
			for (MSpeakerTopic citem : item.inviteSpeakerTopicList) {
				MMeetingTopic mt = new MMeetingTopic();
				if (null != item.id) {
					if (false == item.id.isEmpty()) {
						mt.setMemberId(Long.valueOf(item.id));
					}
				}
				mt.setMemberName(item.name);
				mt.setMemberDesc(item.getCompany());
				mt.setMemberPic(item.image);
				mt.setCreateId(meetingQuery.getCreateId());
				mt.setCreateName(meetingQuery.getCreateName());
				mt.setTaskId(citem.topicTaskId);
				mt.setTopicContent(citem.topicTitle);
				mt.setTopicDesc(citem.topicIntroduce);
				if (null != citem.topicTime) {
					mt.setTopicStartTime(dateFormat.format(new Date(citem.topicTime.startDate)));
					mt.setTopicEndTime(dateFormat.format(new Date(citem.topicTime.endDate)));
				}
				mt.setCreateTime(dateFormat.format(new Date(c.getTimeInMillis())));
				mt.setUpdateTime(dateFormat.format(new Date(c.getTimeInMillis())));
				listMeetingTopic.add(mt);
			}
		}
		// MMeetingSignLabel报名字段对象 需要填写的字段
		// labelName 标签名字
		// isCustom 是否自定义
		List<MMeetingSignLabel> listMeetingSignLabel = new ArrayList<MMeetingSignLabel>();
		for (MFillinInfo item : fillinInfoSelectedGvAdp.getFillinfoList()) {
			MMeetingSignLabel msl = new MMeetingSignLabel();
			msl.setLabelName(item.name);
			msl.setIsCustom(item.isCustom);
			listMeetingSignLabel.add(msl);
		}
		/** 分享关联的知识和需求 */
		meetingQuery.setListMeetingData(listMeetingData);
		/** 参会人员 */
		meetingQuery.setListMeetingMember(listMeetingMember);
		/** 分享关联的人脉 */
		meetingQuery.setListMeetingPeople(listMeetingPeople);
		/** 会议时间列表 */
		meetingQuery.setListMeetingTime(listMeetingTime);
		// meetingQuery.setListMeetingTopic(listMeetingTopic);会议改版去掉
		/** 自定义信息 */
		meetingQuery.setListMeetingSignLabel(listMeetingSignLabel);
		ConferenceReqUtil.doCreateMeeting(InitiatorHYActivity.this, InitiatorHYActivity.this, meetingQuery, null);
		return 0;
	}

	private void updateAlterHY() {
		meetingQuery = new MMeetingQuery();
		long dc_mid = alterMeetingData.getId();
		long dc_uid = Long.valueOf(App.getUserID());
		if (overMeetingReCreate) {//重新创建会议
			dc_mid = 0;
		}
		if (InitiatorDataCache.getInstance().isAlterHY) {
			meetingQuery.setId(dc_mid);
		}
		// meetingName 名称
		meetingQuery.setMeetingName(nameEdit.getEditableText().toString());
		if (null != mapAddress) {
			meetingQuery.setMeetingAddress(mapAddress.address);
			meetingQuery.setMeetingAddressPosX(String.valueOf(mapAddress.longitude));
			meetingQuery.setMeetingAddressPosY(String.valueOf(mapAddress.latitude));
			meetingQuery.setProvince(mapAddress.province);
			meetingQuery.setCity(mapAddress.city);
			meetingQuery.setTown(mapAddress.town);
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (!Util.isNull(InitiatorDataCache.getInstance().timeSelectetedList)) {
			long aaa = 0;
			long bbb = 0;
			for (int nimabide = 0; nimabide < InitiatorDataCache.getInstance().timeSelectetedList.size(); nimabide++) {
				MCalendarSelectDateTime fisrtDT = InitiatorDataCache.getInstance().timeSelectetedList.get(nimabide);
				if (0 == aaa) {
					aaa = fisrtDT.startDate;
				}
				if (aaa > fisrtDT.startDate) {
					aaa = fisrtDT.startDate;
				}
				if (0 == bbb) {
					bbb = fisrtDT.endDate;
				}
				if (bbb < fisrtDT.endDate) {
					bbb = fisrtDT.endDate;
				}
				if (0 != aaa) {
					Date startData = new Date(aaa);
					// :标记会议最先开始的时间，在发起会议时与当前时间对比。（不能早于当前时间）
					Date curDate = new Date(System.currentTimeMillis());
//					if (null != startData && null != curDate) {
//						if (startData.before(curDate)) {//发起时间早于当前时间
//							Toast.makeText(this, "会议发起时间不能早于当前时间", Toast.LENGTH_SHORT).show();
//							return;
//						}
//					}
					meetingQuery.setStartTime(dateFormat.format(startData));
				}
				if (0 != bbb) {
					meetingQuery.setEndTime(dateFormat.format(new Date(bbb)));
				}
			}
		}
		meetingQuery.setCreateTime(alterMeetingData.getCreateTime());
		meetingQuery.setCreateId(alterMeetingData.getCreateId());
		// createName 发起者名称
		meetingQuery.setCreateName(App.getNick());
		meetingQuery.setListIndustry(industrysList);
		meetingQuery.setListMeetingPic(mMeetingPicList);
		meetingQuery.setListMeetingFile(mMeetingFileList);
		meetingQuery.setIsSecrecy(secrecyHYSwitch);
		if (false == secrecyHYSwitch) {
			String applyPeopleNum = applyPeopleNumEdit.getText().toString();
			if (Util.isNull(applyPeopleNum)) {
				applyPeopleNum = "0";
			}
			int i = Integer.valueOf(applyPeopleNum);
			meetingQuery.setMemberCount(i);
		}
		meetingQuery.setCountry(alterMeetingData.getCountry());
		if (0 == createType) {//发起
			meetingQuery.setMeetingStatus(1);
		} else if (1 == createType) {//存草稿
			meetingQuery.setMeetingStatus(0);
		}
		meetingQuery.setMeetingDesc(introduceText.getText().toString());
		InitiatorDataCache.getInstance().introduce.contentText = "";
		meetingQuery.setMeetingType(0);
		meetingQuery.setTaskId(InitiatorDataCache.getInstance().taskId);
		// listMeetingTime
		List<MMeetingTime> listMeetingTime = new ArrayList<MMeetingTime>();
		if (!Util.isNull(InitiatorDataCache.getInstance().timeSelectetedList)) {
			for (MCalendarSelectDateTime item : InitiatorDataCache.getInstance().timeSelectetedList) {
				MMeetingTime mt = new MMeetingTime();
				mt.setStartTime(dateFormat.format(new Date(item.startDate)));
				mt.setEndTime(dateFormat.format(new Date(item.endDate)));
				if (InitiatorDataCache.getInstance().isAlterHY&&!overMeetingReCreate) {//结束的会议重新发起不要保存id
					mt.setMeetingId(dc_mid);
				}
				if (item.isAlterMeeting&&!overMeetingReCreate) {//结束的会议从新发起不要保存id

					if (!Util.isNull(item.alterMeetingTime)) {
						if (InitiatorDataCache.getInstance().isAlterHY) {
							mt.setId(item.alterMeetingTime.getId());
						}
					}
				}
				listMeetingTime.add(mt);
			}
		}
		/** 参会人列表：自己+参会人+主讲人 */
		List<MMeetingMember> listMeetingMember = new ArrayList<MMeetingMember>();
		// 自己
		MMeetingMember mmMyself = new MMeetingMember();
		mmMyself.setMemberId(dc_uid);
		mmMyself.setMemberName(App.getNick());
		mmMyself.setMemberPhoto(App.getUser().getImage());
		mmMyself.setMemberType(2);
		mmMyself.setAttendMeetStatus(1);
		mmMyself.setAttendMeetType(0);
		mmMyself.setExcuteMeetSign(0);
		mmMyself.setMemberMeetStatus(0);
		mmMyself.setIsShowInvitation(1);
		mmMyself.setIsSign(this.old_isSign);

		if (InitiatorDataCache.getInstance().isAlterHY&&!overMeetingReCreate) {//结束的会议重新发起不要保存id
			mmMyself.setMeetingId(dc_mid);
		}
		if (InitiatorDataCache.getInstance().isAlterHY&&!overMeetingReCreate) {//结束的会议重新发起不要保存id
			for (MMeetingMember thisMeetingMember : alterMeetingData.getListMeetingMember()) {
				if (thisMeetingMember.getMemberId() == Long.valueOf(App.getUserID())) {
					mmMyself.setId(thisMeetingMember.getId());
					break;
				}
			}
		}
		mmMyself.setAttendMeetTime(this.old_attendMeetTime);
		mmMyself.setSignDistance(this.old_signDistance);
		listMeetingMember.add(mmMyself);
		// 参会人
		Iterator<Entry<String, JTContactMini>> attendSelMap = InitiatorDataCache.getInstance().inviteAttendSelectedMap.entrySet().iterator();
		while (attendSelMap.hasNext()) {
			Map.Entry entry = (Map.Entry) attendSelMap.next();
			JTContactMini item = (JTContactMini) entry.getValue();
			MMeetingMember mmAttend = new MMeetingMember();
			if (!Util.isNull(item.id)) {
				mmAttend.setMemberId(Long.valueOf(item.id));
			}
			if (InitiatorDataCache.getInstance().isAlterHY&&!overMeetingReCreate) {//结束的会议重新发起不要保存id
				for (MMeetingMember thisMeetingMember : alterMeetingData.getListMeetingMember()) {
					if (thisMeetingMember.getMemberId() == Long.valueOf(item.getId())) {
						mmAttend.setId(thisMeetingMember.getId());
						break;
					}
				}
			}
			// mmAttend.setId(0);
			mmAttend.setMemberName(item.name);
			mmAttend.setMemberPhoto(item.image);
			mmAttend.setMemberType(1);
			mmAttend.setAttendMeetStatus(0);
			mmAttend.setAttendMeetType(0);
			mmAttend.setExcuteMeetSign(0);
			mmAttend.setMemberMeetStatus(0);
			mmAttend.setIsShowInvitation(1);
			if (InitiatorDataCache.getInstance().isAlterHY&&!overMeetingReCreate) {//结束的会议重新发起不要保存id
				mmAttend.setMeetingId(dc_mid);
			}
			if (item.isAlterMeeting) {
				if (!Util.isNull(item.alterMeetingMember)) {
					if (InitiatorDataCache.getInstance().isAlterHY) {
						mmAttend = item.alterMeetingMember;
					}
				}
			}
			listMeetingMember.add(mmAttend);
		}
		// 主讲人
		Iterator<Entry<String, JTContactMini>> speakerdSleMap = InitiatorDataCache.getInstance().inviteSpeakerSelectedMap.entrySet().iterator();
		while (speakerdSleMap.hasNext()) {
			Map.Entry entry = (Map.Entry) speakerdSleMap.next();
			JTContactMini item = (JTContactMini) entry.getValue();
			
			//检查议题时间
			for (MMeetingTopicQuery topicquery : item.lisMeetingTopicQuery) {
				String topicStartTime = topicquery.getTopicStartTime();
				if (!StringUtils.isEmpty(topicStartTime)) {
					try {
						Date tmp = (dateFormat.parse(topicStartTime));
						Date curDate = new Date(System.currentTimeMillis());
						if (null != tmp && null != curDate) {
							if (tmp.before(curDate)) {//议题时间早于当前时间
								Toast.makeText(this, "议题开始时间不能早于当前时间", Toast.LENGTH_SHORT).show();
								return;
							}
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
			MMeetingMember mmSpeaker = new MMeetingMember();
			if (!Util.isNull(item.id)) {
				mmSpeaker.setMemberId(Long.valueOf(item.id));
			}
			if (InitiatorDataCache.getInstance().isAlterHY&&!overMeetingReCreate) {//结束的会议重新发起不要保存id
				for (MMeetingMember thisMeetingMember : alterMeetingData.getListMeetingMember()) {
					if (thisMeetingMember.getMemberId() == Long.valueOf(item.getId())) {
						mmSpeaker.setId(thisMeetingMember.getId());
						break;
					}
				}
			}
			mmSpeaker.setMemberName(item.name);
			mmSpeaker.setMemberPhoto(item.image);
			mmSpeaker.setMemberType(0);
			mmSpeaker.setAttendMeetStatus(1);
			mmSpeaker.setAttendMeetType(0);
			mmSpeaker.setExcuteMeetSign(0);
			mmSpeaker.setMemberMeetStatus(0);
			mmSpeaker.setIsShowInvitation(1);
			if (InitiatorDataCache.getInstance().isAlterHY&&!overMeetingReCreate) {//结束的会议重新发起不要保存id
				mmSpeaker.setMeetingId(dc_mid);
			}
			if (item.isAlterMeeting) {
				if (!Util.isNull(item.alterMeetingMember)) {
					if (InitiatorDataCache.getInstance().isAlterHY) {
						mmSpeaker = item.alterMeetingMember;
					}
				}
			}
			listMeetingMember.add(mmSpeaker);
		}
		/** List<MMeetingData>存放关联的知识和需求 */
		List<MMeetingData> listMeetingData = new ArrayList<MMeetingData>();
		/** 关联的需求 */
		Iterator<Entry<Integer, RequirementMini>> demandSleMap = InitiatorDataCache.getInstance().shareDemandSelectedMap.entrySet().iterator();
		while (demandSleMap.hasNext()) {
			Map.Entry entry = (Map.Entry) demandSleMap.next();
			RequirementMini item = (RequirementMini) entry.getValue();
			MMeetingData md = new MMeetingData();
			md.setDataId(item.getmID());
			md.setId(0);
			md.setDataName(item.mTitle);
			md.setDataReqType(item.getReqType() + 12);
			md.setDataType(0);
			if (InitiatorDataCache.getInstance().isAlterHY&&!overMeetingReCreate) {//结束的会议重新发起不要保存id
				md.setMeetingId(dc_mid);
			}
			if (item.isAlterMeeting) {
				if (!Util.isNull(item.alterMeetingData)) {
					if (InitiatorDataCache.getInstance().isAlterHY) {
						md = item.alterMeetingData;
					}
				}
			}
			listMeetingData.add(md);
		}
		/** 关联的知识 */
		Iterator<Entry<Long, KnowledgeMini2>> knowleadgeSelMap = InitiatorDataCache.getInstance().shareKnowleadgeSelectedMap.entrySet().iterator();
		while (knowleadgeSelMap.hasNext()) {
			Map.Entry<Long, KnowledgeMini2> entry = knowleadgeSelMap.next();
			KnowledgeMini2 item = entry.getValue();
			MMeetingData md = new MMeetingData();
			md.setDataId(item.id);
			md.setId(0);
			md.setDataName(item.title);
			md.setDataReqType(item.type);
			md.setDataUrl(item.source);
			md.setDataType(1);
			if (InitiatorDataCache.getInstance().isAlterHY&&!overMeetingReCreate) {//结束的会议重新发起不要保存id
				md.setMeetingId(dc_mid);
			}
			if (item.isAlterMeeting) {
				if (!Util.isNull(item.alterMeetingData)) {
					if (InitiatorDataCache.getInstance().isAlterHY) {
						md = item.alterMeetingData;
					}
				}
			}
			listMeetingData.add(md);
		}
		/** 关联的人脉 */
		List<MMeetingPeople> listMeetingPeople = new ArrayList<MMeetingPeople>();
		Iterator<Entry<String, JTContactMini>> peoplehubSleMap = InitiatorDataCache.getInstance().sharePeopleHubSelectedMap.entrySet().iterator();
		while (peoplehubSleMap.hasNext()) {
			Map.Entry entry = (Map.Entry) peoplehubSleMap.next();
			JTContactMini item = (JTContactMini) entry.getValue();
			MMeetingPeople mp = new MMeetingPeople();
			if (!Util.isNull(item.id)) {
				mp.setPeopleId(Long.valueOf(item.id));
			}
			if (item.isOnline) {
				mp.setPeopleType(2);
			}else {
				mp.setPeopleType(1);
			}
			mp.setId(0L);
			mp.setPeopleName(item.name);
			mp.setPeopleDesc(item.getCompany());
			mp.setPeoplePhoto(item.image);
			if (InitiatorDataCache.getInstance().isAlterHY&&!overMeetingReCreate) {//结束的会议重新发起不要保存id
				mp.setMeetingId(dc_mid);
			}
			if (item.isAlterMeeting) {
				if (!Util.isNull(item.alterMeetingPeople)) {
					if (InitiatorDataCache.getInstance().isAlterHY) {
						mp = item.alterMeetingPeople;
					}
				}
			}
			listMeetingPeople.add(mp);
		}

		/** 关联的组织 */
		List<MMeetingOrgan> listMeetingOrgans = new ArrayList<MMeetingOrgan>();
		Iterator<Entry<String, OrganizationMini>> orghubSleMap = InitiatorDataCache.getInstance().shareOrgHubSelectedMap.entrySet().iterator();
		while (orghubSleMap.hasNext()) {
			Map.Entry entry = (Map.Entry) orghubSleMap.next();
			OrganizationMini item = (OrganizationMini) entry.getValue();
			MMeetingOrgan mo = new MMeetingOrgan();
			if (null != item.id) {
				if (false == item.id.isEmpty()) {
					mo.setOrganId(item.id);
				}
			}
			
			if (item.isOnline) {
				mo.setOrganType(1);
			}else {
				mo.setOrganType(2);
			}
			
			mo.setId(0L);
			mo.setOrganName(item.fullName);
			mo.setOrganPhoto(item.logo);
			if (InitiatorDataCache.getInstance().isAlterHY&&!overMeetingReCreate) {//结束的会议重新发起不要保存id
				mo.setMeetingId(dc_mid);
			}
			if (item.isAlterMeeting) {
				if (!Util.isNull(item.alterMMeetingOrgan)) {
					if (InitiatorDataCache.getInstance().isAlterHY) {
						mo = item.alterMMeetingOrgan;
					}
				}
			}
			listMeetingOrgans.add(mo);
		}
		meetingQuery.setListMeetingOrgan(listMeetingOrgans);

		/** 议题详情列表(详细) */
		List<MMeetingTopicQuery> listMeetingTopicQuery = new ArrayList<MMeetingTopicQuery>();
		/** 主讲人Map集合 */
		Iterator<Entry<String, JTContactMini>> speakerMap = InitiatorDataCache.getInstance().inviteSpeakerSelectedMap.entrySet().iterator();
		/** 遍历添加所有主讲人的议题集合 */
		while (speakerMap.hasNext()) {
			/** 设置为 有议题会议 */
			meetingQuery.setMeetingType(1);
			Map.Entry entry = (Map.Entry) speakerMap.next();
			JTContactMini jtContactMini = (JTContactMini) entry.getValue();
			/** 此主讲人的议题集合 */
			List<MMeetingTopicQuery> speakerTopicQuery = jtContactMini.lisMeetingTopicQuery;
			if (!overMeetingReCreate) {//结束的会议重新发起不要保存id
				for (MMeetingTopicQuery mMeetingTopicQuery : speakerTopicQuery) {
					// 议题设置会议的id
					mMeetingTopicQuery.setMeetingId(alterMeetingData.getId());
				}
			}
			listMeetingTopicQuery.addAll(speakerTopicQuery);
			meetingQuery.setMeetingType(1);
		}
		/** 设置主讲人议题详细列表 */
		meetingQuery.setListMeetingTopicQuery(listMeetingTopicQuery);

		List<MMeetingSignLabel> listMeetingSignLabel = new ArrayList<MMeetingSignLabel>();
		for (MFillinInfo item : fillinInfoSelectedGvAdp.getFillinfoList()) {
			MMeetingSignLabel msl = new MMeetingSignLabel();
			msl.setLabelName(item.name);
			msl.setIsCustom(item.isCustom);
			listMeetingSignLabel.add(msl);
		}
		/** 自定义信息 */
		meetingQuery.setListMeetingSignLabel(listMeetingSignLabel);
		/** 会议时间列表 */
		meetingQuery.setListMeetingTime(listMeetingTime);
		/** 分享关联的人脉 */
		meetingQuery.setListMeetingPeople(listMeetingPeople);
		/** 参会人员 */
		meetingQuery.setListMeetingMember(listMeetingMember);
		/** 分享关联的知识和需求 */
		meetingQuery.setListMeetingData(listMeetingData);
		// meetingQuery.setListMeetingTopic(listMeetingTopic);
		if (InitiatorDataCache.getInstance().isAlterHY&&!overMeetingReCreate) {//更新会议
			ConferenceReqUtil.doUpdate(InitiatorHYActivity.this, InitiatorHYActivity.this, meetingQuery, null);
		} else {//创建会议(结束重发起的会议，相当于重新创建一个会议)
			ConferenceReqUtil.doCreateMeeting(InitiatorHYActivity.this, InitiatorHYActivity.this, meetingQuery, null);
		}
	}

	/**
	 * 修改会议初始化数据
	 */
	private void alterHYIntentData() {
		Bundle b = getIntent().getExtras();
		if (b == null) {
			return;
		}
		alterMeetingData = (MMeetingQuery) b.getSerializable("meetingData");
		overMeetingReCreate = b.getBoolean("OverMeetingReCreate",false);
		if (alterMeetingData == null) {
			return;
		}
		prgDialog = new EProgressDialog(this);
		prgDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {

			}
		});
		prgDialog.setCancelable(false);
		prgDialog.setCanceledOnTouchOutside(false);
		prgDialog.show();
		InitiatorDataCache.getInstance().isAlterHY = true;
		InitiatorDataCache.getInstance().isAlterHyId = alterMeetingData.getId();
		// //////////////title
		// add by d.c 修改草稿标题栏不同
		if (0 == alterMeetingData.getMeetingStatus()) {
			is_mod = false;
			titleText.setText("编辑会议");
			titleRightTextBtn1.setVisibility(View.VISIBLE);
			titleRightTextBtn2.setText("发起");
		} else {
			is_mod = true;
			titleText.setText("编辑会议");
			titleRightTextBtn1.setVisibility(View.GONE);
			titleRightTextBtn2.setText("完成");
		}
		if (!Util.isNull(alterMeetingData.getMeetingName())) {
			nameEdit.setText(alterMeetingData.getMeetingName());
		}

		industrysList = (ArrayList<String>) alterMeetingData.getListIndustry();
		List<MIndustry> lisIndustries = new ArrayList<MIndustry>();
		String buniessName = "";
		if (industrysList != null) {
			for (int i = 0; i < industrysList.size(); i++) {
				MIndustry industry = new MIndustry();
				industry.setName(industrysList.get(i));
				buniessName += industrysList.get(i) + " ";
				lisIndustries.add(industry);
			}
		}
		/** 行业 */
		if (mIndustrys == null) {
			mIndustrys = new MIndustrys();
		}
		if (!StringUtils.isEmpty(buniessName)) {
			busniessTv.setText(buniessName);
		}
		mIndustrys.setListIndustry(lisIndustries);

		mMeetingPicList = (ArrayList<MMeetingPic>) alterMeetingData.getListMeetingPic();
		mMeetingFileList = (ArrayList<JTFile2ForHY>) alterMeetingData.getListMeetingFile();
		
		if (overMeetingReCreate) {
			mMeetingPicList = null;
			mMeetingFileList=null;
			alterMeetingData.setListMeetingFile(null);
			alterMeetingData.setListMeetingPic(null);
			alterMeetingData.setListMeetingTime(null);
			alterMeetingData.setListMeetingMember(null);
			alterMeetingData.setListMeetingData(null);
			alterMeetingData.setListMeetingKnowledge(null);
			alterMeetingData.setListMeetingOrgan(null);
			alterMeetingData.setListMeetingPeople(null);
			alterMeetingData.setListMeetingTopicQuery(null);
			alterMeetingData.setMeetingDesc("");
		}

		InitiatorDataCache.getInstance().taskId = alterMeetingData.getTaskId();

		//time   
		if (!Util.isNull(alterMeetingData.getListMeetingTime())&&!overMeetingReCreate) {//重新发起的会议不初始化会议时间
			MCalendarSelectDateTime sdt;
			Date d1 = null, d2 = null;
			Calendar c = Calendar.getInstance();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// ("yyyy-MM-dd HH:mm:ss");
			// 转化
			for (MMeetingTime item : alterMeetingData.getListMeetingTime()) {
				sdt = new MCalendarSelectDateTime();
				sdt.isAlterMeeting = true;
				sdt.alterMeetingTime = item;
				// sdt.meetingId = item.getMeetingId();
				try {
					if (!Util.isNull(item.getStartTime()) && !Util.isNull(item.getEndTime())) {
						d1 = dateFormat.parse(item.getStartTime());
						d2 = dateFormat.parse(item.getEndTime());
						sdt.startDate = d1.getTime();
						sdt.endDate = d2.getTime();
						c.setTimeInMillis(sdt.startDate);
						sdt.year = c.get(Calendar.YEAR);
						sdt.month = c.get(Calendar.MONTH) + 1;
						sdt.day = c.get(Calendar.DAY_OF_MONTH);
						sdt.weekIndex = c.get(Calendar.WEEK_OF_MONTH);

						sdt.startHour = c.get(Calendar.HOUR_OF_DAY);
						sdt.startMinute = c.get(Calendar.MINUTE);
						sdt.startTime = sdt.startHour + ":" + sdt.startMinute;

						c.setTimeInMillis(sdt.endDate);
						sdt.endHour = c.get(Calendar.HOUR_OF_DAY);
						sdt.endMinute = c.get(Calendar.MINUTE);
						sdt.endTime = sdt.endHour + ":" + sdt.endMinute;
						InitiatorDataCache.getInstance().timeSelectetedList.add(sdt);
					}
				} catch (ParseException e) {
					//
					e.printStackTrace();
				}
			}
			// 填写
			if (!Util.isNull(InitiatorDataCache.getInstance().timeSelectetedList)) {
				int index = 0;
				timeText2.setVisibility(View.GONE);
				timeText3.setVisibility(View.GONE);
				for (MCalendarSelectDateTime dt : InitiatorDataCache.getInstance().timeSelectetedList) {
					if (index == 0) {
						timeText1.setText(IniviteUtil.formatDate(dt));
					} else if (index == 1) {
						timeText2.setVisibility(View.VISIBLE);
						timeText2.setText(IniviteUtil.formatDate(dt));
					} else if (index == 2) {
						timeText3.setVisibility(View.VISIBLE);
						if (InitiatorDataCache.getInstance().timeSelectetedList.size() > 3) {
							timeText3.setText(IniviteUtil.formatDate(dt));// +
																			// "...");
						} else {
							timeText3.setText(IniviteUtil.formatDate(dt));
						}
						break;
					}
					index++;
				}
			}
		}
		// 参会人、主讲人
		if (!Util.isNull(alterMeetingData.getListMeetingMember())) {
			JTContactMini jtc;
			// 转化
			for (MMeetingMember item : alterMeetingData.getListMeetingMember()) {
				// add by d.c 过滤自己
				if (item.getMemberId() == Long.valueOf(App.getApp().getUserID()).longValue() || item.getMemberType() == 2) {
					old_my_id = item.getId();
					old_isSign = item.getIsSign();
					old_signDistance = item.getSignDistance();
					old_attendMeetTime = item.getAttendMeetTime();
					continue;
				}
				jtc = new JTContactMini();
				jtc.name = item.getMemberName();
				jtc.image = item.getMemberPhoto();
				jtc.id = String.valueOf(item.getMemberId());
				// jtc.setId(String.valueOf(item.getId()));
				jtc.isAlterMeeting = true;
				jtc.alterMeetingMember = item;
			/*	if (item.getMemberType() == 0) {
					InitiatorDataCache.getInstance().inviteSpeakerSelectedMap.put(jtc.id, jtc);
				} else */if (item.getMemberType() == 1) {// by d.c 过滤无效的0xff
					InitiatorDataCache.getInstance().inviteAttendSelectedMap.put(jtc.id, jtc);
				}
			}
		}

		// //////////////分享
		if (!Util.isNull(alterMeetingData.getListMeetingData())) {
			RequirementMini demand;
			KnowledgeMini2 knowleadge;
			for (MMeetingData item : alterMeetingData.getListMeetingData()) {
				if (item.getDataType() == 0) {
					demand = new RequirementMini();
					demand.mID = (int) item.getDataId();
					demand.mTitle = item.getDataName();
					demand.setReqType(item.getDataReqType() - 12);
					demand.isAlterMeeting = true;
					demand.alterMeetingData = item;
					InitiatorDataCache.getInstance().shareDemandSelectedMap.put(demand.mID, demand);
				} else {
					// TODO sunjianan modified
					knowleadge = new KnowledgeMini2();
					knowleadge.id = item.getDataId();
					knowleadge.title = item.getDataName();
					knowleadge.type = item.getDataReqType();
					knowleadge.source = item.getDataUrl();
					knowleadge.modifytime = item.getCreateTime();
					knowleadge.isAlterMeeting = true;
					knowleadge.alterMeetingData = item;
					InitiatorDataCache.getInstance().shareKnowleadgeSelectedMap.put(knowleadge.id, knowleadge);
				}
			}
		}
		// 关联的人脉
		if (!Util.isNull(alterMeetingData.getListMeetingPeople())) {
			JTContactMini jtc;
			for (MMeetingPeople item : alterMeetingData.getListMeetingPeople()) {
				jtc = new JTContactMini();
				jtc.id = String.valueOf(item.getPeopleId());
				jtc.setId(String.valueOf(item.getId()));
				jtc.name = item.getPeopleName();
				jtc.setCompany(item.getPeopleDesc());
				jtc.image = item.getPeoplePhoto();
				jtc.isAlterMeeting = true;
				jtc.alterMeetingPeople = item;
				InitiatorDataCache.getInstance().sharePeopleHubSelectedMap.put(jtc.id, jtc);

			}
		}
		// 关联的知识
		if (!Util.isNull(alterMeetingData.getListMeetingKnowledge())) {
			KnowledgeMini2 knowledgeMini2;
			for (MMeetingData item : alterMeetingData.getListMeetingKnowledge()) {
				knowledgeMini2 = new KnowledgeMini2();
				knowledgeMini2.id = item.getDataId();
				knowledgeMini2.alterMeetingData = item;
				knowledgeMini2.isAlterMeeting = true;
				knowledgeMini2.title = item.getDataName();
				InitiatorDataCache.getInstance().shareKnowleadgeSelectedMap.put(knowledgeMini2.id, knowledgeMini2);
			}
		}
		// 关联的组织
		if (!Util.isNull(alterMeetingData.getListMeetingOrgan())) {
			OrganizationMini org;
			for (MMeetingOrgan item : alterMeetingData.getListMeetingOrgan()) {
				org = new OrganizationMini();
				org.id = item.getOrganId();
				org.alterMMeetingOrgan = item;
				org.isAlterMeeting = true;
				org.fullName = item.getOrganName();
				InitiatorDataCache.getInstance().shareOrgHubSelectedMap.put(org.id, org);
			}
		}
		// 关联界面
		if (!Util.isNull(InitiatorDataCache.getInstance().sharePeopleHubSelectedMap) || !Util.isNull(InitiatorDataCache.getInstance().shareOrgHubSelectedMap)
				|| Util.isNull(InitiatorDataCache.getInstance().shareDemandSelectedMap) || Util.isNull(InitiatorDataCache.getInstance().shareKnowleadgeSelectedMap)) {
			share4ContentGroupLabel.setVisibility(View.VISIBLE);

			if (!Util.isNull(InitiatorDataCache.getInstance().sharePeopleHubSelectedMap)) {
				sharePeoplehubLayout.setVisibility(View.VISIBLE);
				String sharePeoplehubStr = IniviteUtil.format(InitiatorDataCache.getInstance().sharePeopleHubSelectedMap, "，");
				peopleHubText.setText(sharePeoplehubStr);
			} else {
				sharePeoplehubLayout.setVisibility(View.GONE);
				peopleHubText.setText("");
			}
			if (!Util.isNull(InitiatorDataCache.getInstance().shareOrgHubSelectedMap)) {
				shareOrgLayout.setVisibility(View.VISIBLE);
				String sharePeoplehubStr = IniviteUtil.formatOrg(InitiatorDataCache.getInstance().shareOrgHubSelectedMap, "，");
				orgTextTv.setText(sharePeoplehubStr);
			} else {
				shareOrgLayout.setVisibility(View.GONE);
				orgTextTv.setText("");
			}

			if (!Util.isNull(InitiatorDataCache.getInstance().shareDemandSelectedMap)) {
				shareDemandLaout.setVisibility(View.VISIBLE);
				int index = 0;
				demandText1.setVisibility(View.INVISIBLE);
				demandText2.setVisibility(View.GONE);
				demandText3.setVisibility(View.GONE);
				// for (RequirementMini item :
				// InitiatorDataCache.getInstance().shareDemandSelectedList) {
				Iterator<Entry<Integer, RequirementMini>> seledMap = InitiatorDataCache.getInstance().shareDemandSelectedMap.entrySet().iterator();
				while (seledMap.hasNext()) {
					Map.Entry entry = (Map.Entry) seledMap.next();
					RequirementMini item = (RequirementMini) entry.getValue();
					if (index == 0) {
						demandText1.setVisibility(View.VISIBLE);
						demandText1.setText(item.mTitle);
					} else if (index == 1) {
						demandText2.setVisibility(View.VISIBLE);
						demandText2.setText(item.mTitle);
					} else if (index == 2) {
						demandText3.setVisibility(View.VISIBLE);
						demandText3.setText(item.mTitle);
						break;
					} else {
						break;
					}
					index++;
				}
			} else {
				shareDemandLaout.setVisibility(View.GONE);
			}
			if (!Util.isNull(InitiatorDataCache.getInstance().shareKnowleadgeSelectedMap)) {
				shareKnowleadgeLayout.setVisibility(View.VISIBLE);
				int index = 0;
				knowleadgeText1.setVisibility(View.INVISIBLE);
				knowleadgeText2.setVisibility(View.GONE);
				knowleadgeText3.setVisibility(View.GONE);
				// for (KnowledgeMini item :
				// InitiatorDataCache.getInstance().shareKnowleadgeSelectedList)
				// {
				Iterator<Entry<Long, KnowledgeMini2>> iter = InitiatorDataCache.getInstance().shareKnowleadgeSelectedMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry<Long, KnowledgeMini2> entry = iter.next();
					KnowledgeMini2 item = entry.getValue();
					if (index == 0) {
						knowleadgeText1.setVisibility(View.VISIBLE);
						knowleadgeText1.setText(item.title);
					} else if (index == 1) {
						knowleadgeText2.setVisibility(View.VISIBLE);
						knowleadgeText2.setText(item.title);
					} else if (index == 2) {
						knowleadgeText3.setVisibility(View.VISIBLE);
						knowleadgeText3.setText(item.title);
						break;
					} else {
						break;
					}
					index++;
				}
			} else {
				shareKnowleadgeLayout.setVisibility(View.GONE);
			}
		}

		// 主讲人+主题
		if (!Util.isNull(alterMeetingData.getListMeetingTopicQuery())&&alterMeetingData.getMeetingType()==1) {
			MCalendarSelectDateTime sdt;
			for (MMeetingTopicQuery item : alterMeetingData.getListMeetingTopicQuery()) {
				//主讲人加入    主讲人集合inviteSpeakerSelectedMap
				JTContactMini	contactMini = new JTContactMini();
				contactMini.name = item.getMemberName();
				contactMini.image = item.getMeberPic();
				contactMini.id = String.valueOf(item.getMemberId());
				contactMini.isAlterMeeting = true;
				contactMini.lisMeetingTopicQuery.add(item);
				InitiatorDataCache.getInstance().inviteSpeakerSelectedMap.put(contactMini.id, contactMini);
			}
		}
		
		// 填写
		if (!Util.isNull(InitiatorDataCache.getInstance().inviteAttendSelectedMap)) {
			inviteAttendeeGvAdp.update(InitiatorDataCache.getInstance().inviteAttendSelectedMap);
			refreshInviteGV(0);
		}
		if (!Util.isNull(InitiatorDataCache.getInstance().inviteSpeakerSelectedMap)) {
			inviteSpeakerGvAdp.update(InitiatorDataCache.getInstance().inviteSpeakerSelectedMap);
			refreshInviteGV(1);
		}

		// //////////////地址
		if (alterMeetingData.getMeetingAddress().isEmpty() == false) {
			mapAddress = new MMapAddress();
			mapAddress.address = alterMeetingData.getMeetingAddress();
			mapAddress.longitude = Double.valueOf(alterMeetingData.getMeetingAddressPosX());
			mapAddress.latitude = Double.valueOf(alterMeetingData.getMeetingAddressPosY());
			mapAddress.province = alterMeetingData.getProvince();
			mapAddress.city = alterMeetingData.getCity();
			mapAddress.town = alterMeetingData.getTown();
			addressText.setText(mapAddress.address);
		}
		// //////////////介绍
		InitiatorDataCache.getInstance().introduce.contentText = alterMeetingData.getMeetingDesc();
		introduceText.setText(alterMeetingData.getMeetingDesc());
		
		// //////////////安全会议
		secrecyHYSwitch = alterMeetingData.getIsSecrecy();
		if (secrecyHYSwitch) {
			secrecyHYSwitchIv.setImageResource(R.drawable.hy_switch_on);
			// 设置报名人数的状态
			applyPeopleNumSwitchIv.setImageResource(R.drawable.hy_switch_off);
			applyPeopleNumSwitchIv.setClickable(false);
			applyPeopleNumEdit.setText("安全会议下不可报名");
			secrecyHYText.setText("不可分享、转发");
		} else {
			applyPeopleNumSwitchIv.setClickable(true);
		}
		// //////////////参会人数
		fillInfoLabel.setVisibility(View.GONE);
		applyPeopleNumEdit.setText(String.valueOf(alterMeetingData.getMemberCount()));
		if (alterMeetingData.getMemberCount() > 0 && false == secrecyHYSwitch) {
			applyPeopleNumSwitch = true;
			applyPeopleNumEdit.setEnabled(true);
			applyPeopleNumSwitchIv.setClickable(true);
			fillinInfoToSelectGvAdp.notifyDataSetChanged();
		}

		prgDialog.dismiss();
	}

	private class MyOnClickListener implements View.OnClickListener {
		private ClickType type;

		public MyOnClickListener(ClickType type) {
			this.type = type;
		}

		@Override
		public void onClick(View v) {
			int clickId = v.getId();
			if (type == ClickType.TYPE_CLICK_NORMAL) {
				try {
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					boolean isOpen = imm.isActive();
					if (isOpen) {
						imm.hideSoftInputFromWindow(InitiatorHYActivity.this.getCurrentFocus().getWindowToken(), 0);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

				switch (clickId) {
				case R.id.hy_layoutTitle_backBtn: {
					finishActivity();
				}
					break;

				// 存草稿
				case R.id.hy_layoutTitle_rightTextBtn1: {
					titleRightTextBtnSetClickable(false);
					createType = 1;
					showPrgDialog();
					if (InitiatorDataCache.getInstance().isAlterHY) {
						prepareUpdateHY();
					} else {
						prepareCreateHY();
					}
				}
					break;

				// 发起
				case R.id.hy_layoutTitle_rightTextBtn2: {
					titleRightTextBtnSetClickable(false);
					createType = 0;
					showPrgDialog();
					if (InitiatorDataCache.getInstance().isAlterHY) {
						prepareUpdateHY();
					} else {
						prepareCreateHY();
					}
				}
					break;
				}
			} else if (type == ClickType.TYPE_CLICK_TIME_LABEL) {
				if (clickId == R.id.hy_layout_picker_3line_text_labelclick) {
					Bundle b = new Bundle();
					b.putInt(Util.IK_VALUE, 0);
					Util.forwardTargetActivityForResult(InitiatorHYActivity.this, CalendarActivity.class, b, requestCode_time);
				}
			} else if (type == ClickType.TYPE_CLICK_BUSNIESS_LABEL) {
				if (clickId == R.id.hy_layout_picker_3line_text_labelclick) {
					ENavigate.startChooseProfessionActivityForResult(InitiatorHYActivity.this, requestCode_buniess, 0, mIndustrys);
				}
			}
			// 关联人脉
			else if (type == ClickType.TYPE_CLICK_SHARE_PEOPLEHUB_LABEL) {
				// 将sharePeopleHubSelectedMap数据转换成connectionNode，以对接关联资源
				ConnectionNode connectionNode = null;
				if (InitiatorDataCache.getInstance().sharePeopleHubSelectedMap != null) {
					connectionNode = new ConnectionNode();
					// listConnections
					ArrayList<Connections> listConnections = new ArrayList<Connections>();
					// 封装ResourceNode
					Iterator<Entry<String, JTContactMini>> peoplehubSleMap = InitiatorDataCache.getInstance().sharePeopleHubSelectedMap.entrySet().iterator();
					while (peoplehubSleMap.hasNext()) {
						Map.Entry entry = (Map.Entry) peoplehubSleMap.next();
						JTContactMini item = (JTContactMini) entry.getValue();
						// 封装connection
						Connections connections = new Connections(item);
						listConnections.add(connections);
					}
					connectionNode.setListConnections(listConnections);
					connectionNode.setMemo(peopleRelatedkeyWord);
				}
				ENavigate.startRelatedResourceActivityForResult(InitiatorHYActivity.this, requestCode_share, "", ResourceType.People, connectionNode);
			}
			// 关联组织
			else if (type == ClickType.TYPE_CLICK_SHARE_ORG_LABEL) {

				// 将shareOrgHubSelectedMap数据转换成connectionNode，以对接关联资源
				ConnectionNode connectionNode = null;
				if (InitiatorDataCache.getInstance().shareOrgHubSelectedMap != null) {
					connectionNode = new ConnectionNode();
					// listConnections
					ArrayList<Connections> listConnections = new ArrayList<Connections>();
					// 封装ResourceNode
					Iterator<Entry<String, OrganizationMini>> orghubSleMap = InitiatorDataCache.getInstance().shareOrgHubSelectedMap.entrySet().iterator();
					while (orghubSleMap.hasNext()) {
						Map.Entry entry = (Map.Entry) orghubSleMap.next();
						OrganizationMini item = (OrganizationMini) entry.getValue();
						// 封装connection
						Connections connections = new Connections(item);
						listConnections.add(connections);
					}
					connectionNode.setListConnections(listConnections);
					connectionNode.setMemo(orgRelatedkeyWord);
				}

				ENavigate.startRelatedResourceActivityForResult(InitiatorHYActivity.this, requestCode_share, "", ResourceType.Organization, connectionNode);
			}
			// 关联需求
			else if (type == ClickType.TYPE_CLICK_SHARE_DEMAND_LABEL) {
				// 将shareDemandSelectedMap数据转换成connectionNode，以对接关联资源
				AffairNode affairNode = null;
				if (InitiatorDataCache.getInstance().shareDemandSelectedMap != null) {
					affairNode = new AffairNode();
					// listAffairMini
					ArrayList<AffairsMini> listAffairMini = new ArrayList<AffairsMini>();
					// 封装ResourceNode
					Iterator<Entry<Integer, RequirementMini>> demandhubSleMap = InitiatorDataCache.getInstance().shareDemandSelectedMap.entrySet().iterator();
					while (demandhubSleMap.hasNext()) {
						Map.Entry entry = (Map.Entry) demandhubSleMap.next();
						RequirementMini requirementMini = (RequirementMini) entry.getValue();
						// 封装affairsmini
						AffairsMini affairsMini = requirementMini.toAffairMini();
						listAffairMini.add(affairsMini);
					}
					affairNode.setListAffairMini(listAffairMini);
					affairNode.setMemo(requarementRelatedkeyWord);
				}

				ENavigate.startRelatedResourceActivityForResult(InitiatorHYActivity.this, requestCode_share, "", ResourceType.Affair, affairNode);
			}
			// 关联知识
			else if (type == ClickType.TYPE_CLICK_SHARE_KNOWLEADGE_LABEL) {

				KnowledgeNode knowledgeNode = null;
				if (InitiatorDataCache.getInstance().shareKnowleadgeSelectedMap != null) {
					knowledgeNode = new KnowledgeNode();
					ArrayList<KnowledgeMini2> listKnowledgeMini2 = new ArrayList<KnowledgeMini2>();
					// 封装ResourceNode
					Iterator<Entry<Long, KnowledgeMini2>> knowleadgehubSleMap = InitiatorDataCache.getInstance().shareKnowleadgeSelectedMap.entrySet().iterator();
					while (knowleadgehubSleMap.hasNext()) {
						Map.Entry entry = (Map.Entry) knowleadgehubSleMap.next();
						KnowledgeMini2 knowledgeMini2 = (KnowledgeMini2) entry.getValue();
						listKnowledgeMini2.add(knowledgeMini2);
					}
					knowledgeNode.setListKnowledgeMini2(listKnowledgeMini2);
					knowledgeNode.setMemo(knowledgeRelatedkeyWord);
				}

				ENavigate.startRelatedResourceActivityForResult(InitiatorHYActivity.this, requestCode_share, "", ResourceType.Knowledge, knowledgeNode);

			} else if (type == ClickType.TYPE_CLICK_ADDRESS_LABEL) {
				if (clickId == R.id.hy_layout_picker_1line_text_labelclick) {
					Intent intent = new Intent(InitiatorHYActivity.this, LocationActivity.class);
					intent.putExtra("address", mapAddress);
					startActivityForResult(intent, requestCode_address);
				}
			} else if (type == ClickType.TYPE_CLICK_INTRODUCE_LABEL) {
				if (clickId == R.id.hy_layout_picker_1line_text_labelclick) {
					ENavigate.startIntroduceActivity(InitiatorHYActivity.this, mMeetingPicList, mMeetingFileList, requestCode_introduce);
				}
			} else if (type == ClickType.TYPE_CLICK_SECRECYHY_LABEL) {
				if (clickId == R.id.hy_layout_switch_1line_text_switchicon) {
					if (secrecyHYSwitch) {
						secrecyHYSwitch = false;
					} else {
						secrecyHYSwitch = true;
					}
					ImageView iv = (ImageView) v;
					if (secrecyHYSwitch) {
						iv.setImageResource(R.drawable.hy_switch_on);

						// 设置报名人数的状态
						applyPeopleNumSwitchIv.setImageResource(R.drawable.hy_switch_off);
						applyPeopleNumSwitchIv.setClickable(false);
						secrecyHYText.setText("不可分享、转发");
						applyPeopleNumEdit.setText("安全会议下不可报名");
					} else {

						applyPeopleNumSwitchIv.setClickable(true);
						iv.setImageResource(R.drawable.hy_switch_off);
						secrecyHYText.setText("");
						applyPeopleNumEdit.setText("100");
					}
				}
			} else if (type == ClickType.TYPE_CLICK_APPLUPEOPLENUM_LABEL) {
				if (clickId == R.id.hy_layout_switch_1line_text_switchicon) {
					applyPeopleNumSwitch = !applyPeopleNumSwitch;
					ImageView iv = (ImageView) v;
					if (applyPeopleNumSwitch) {
						iv.setImageResource(R.drawable.hy_switch_on);
						applyPeopleNumEdit.setEnabled(true);
						applyPeopleNumEdit.setText("100");
						fillInfoLabel.setVisibility(View.VISIBLE);
						fillinInfoToSelectGvAdp.notifyDataSetChanged();
					} else {
						iv.setImageResource(R.drawable.hy_switch_off);
						applyPeopleNumEdit.setEnabled(false);
						applyPeopleNumEdit.setText("100");
						fillInfoLabel.setVisibility(View.GONE);
					}
				}
			} else if (type == ClickType.TYPE_CLICK_FILLINFO_LABEL) {
				if (clickId == R.id.hy_layout_input_1line_add_rightIconBtn) {
					String sdEdit = selfDefineEdit.getText().toString().trim();
					if (Util.isNull(sdEdit)) {
						Toast.makeText(InitiatorHYActivity.this, "自定义内容为空", Toast.LENGTH_SHORT).show();
						return;
					}
					MFillinInfo fi = new MFillinInfo();
					fi.id = (int) System.currentTimeMillis();// datalist.size();
					fi.selected = false;
					fi.name = sdEdit;
					fi.isCustom = 1;
					fillinInfoToSelectGvAdp.getFillinfoList().add(fi);
					fillinInfoToSelectGvAdp.notifyDataSetChanged();
					selfDefineEdit.setText("");
				}
			}

		}

		private void showPrgDialog() {
			prgDialog = new EProgressDialog(InitiatorHYActivity.this);
			prgDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {

				}
			});
			prgDialog.setMessage("正在提交...");
			prgDialog.setCancelable(false);
			prgDialog.setCanceledOnTouchOutside(false);
			prgDialog.show();
		}
	}
	
	
	private void titleRightTextBtnSetClickable(boolean clickable){
		titleRightTextBtn1.setClickable(clickable);
		titleRightTextBtn2.setClickable(clickable);
	}

	public void showModifyLocation() {
		final EditText et = new EditText(this);
		new AlertDialog.Builder(this).setTitle("地点").setIcon(R.drawable.ic_launcher).setView(et).setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String newLocation = et.getText().toString();
				addressText.setText(newLocation);

			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		}).show();
	}

	private class MyOnItemClickListener implements OnItemClickListener {
		private ItemClickType type;

		public MyOnItemClickListener(ItemClickType type) {
			this.type = type;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (type == ItemClickType.TYPE_ITEM_CLICK_ATTENDEE) {
				// if(position == inviteAttendeeGvAdp.getListSize() - 1){
				Bundle b = new Bundle();
				b.putInt(Util.IK_VALUE, InviteFriendActivity.TYPE_INVITE_ATTEND_FRIEND);
				Util.forwardTargetActivityForResult(InitiatorHYActivity.this, InviteFriendActivity.class, b, requestCode_inviteAttend);
				// }
			} else if (type == ItemClickType.TYPE_ITEM_CLICK_SHARE) {
				Bundle b = new Bundle();
				b.putInt(Util.IK_VALUE, ShareActivity.TAB_PEOPLEHUB);
				Util.forwardTargetActivityForResult(InitiatorHYActivity.this, ShareActivity.class, b, requestCode_share);
			} else if (type == ItemClickType.TYPE_ITEM_CLICK_SPEAKER) {
				// if(position == inviteSpeakerGvAdp.getListSize() - 1){
				Bundle b = new Bundle();
				b.putInt(Util.IK_VALUE, InviteFriendActivity.TYPE_INVITE_SPEAKER_FRIEND);
				Util.forwardTargetActivityForResult(InitiatorHYActivity.this, InviteFriendActivity.class, b, requestCode_inviteSpeaker);
			} else if (type == ItemClickType.TYPE_ITEM_CLICK_FILLINFO_SELECTED) {

			} else if (type == ItemClickType.TYPE_ITEM_CLICK_FILLINFO_TOSELECT) {

			}

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			boolean isOpen = imm.isActive();
			if (isOpen) {
				imm.hideSoftInputFromWindow(InitiatorHYActivity.this.getCurrentFocus().getWindowToken(), 0);
			}
		} catch (Exception e) {
		}

		if (data == null || data.getExtras() == null) {
			return;
		}

		switch (requestCode) {
		/** 会议时间 */
		case requestCode_time: {
			timeText1.setText("必填");
			timeText2.setVisibility(View.GONE);
			timeText3.setVisibility(View.GONE);
			if (!Util.isNull(InitiatorDataCache.getInstance().timeSelectetedList)) {
				int index = 0;
				timeText2.setVisibility(View.GONE);
				timeText3.setVisibility(View.GONE);
				for (MCalendarSelectDateTime dt : InitiatorDataCache.getInstance().timeSelectetedList) {
					if (index == 0) {
						timeText1.setText(IniviteUtil.formatDate(dt));
					} else if (index == 1) {
						timeText2.setVisibility(View.VISIBLE);
						timeText2.setText(IniviteUtil.formatDate(dt));
					} else if (index == 2) {
						timeText3.setVisibility(View.VISIBLE);
						if (InitiatorDataCache.getInstance().timeSelectetedList.size() > 3) {
							timeText3.setText(IniviteUtil.formatDate(dt));// +
																			// "...");
						} else {
							timeText3.setText(IniviteUtil.formatDate(dt));
						}
						break;
					}
					index++;
				}
			}
		}
			break;
		/** 选择行业 */
		case requestCode_buniess:
			Bundle bundle = data.getExtras();
			if (bundle != null) {
				mIndustrys = (MIndustrys) bundle.getSerializable(EConsts.Key.INDUSTRYS);
			}
			if (industrysList == null) {
				industrysList = new ArrayList<String>();
			} else {
				industrysList.clear();
			}
			String buniessName = "";
			if (mIndustrys != null && mIndustrys.getListIndustry() != null && mIndustrys.getListIndustry().size() > 0) {
				List<MIndustry> listIndustry = mIndustrys.getListIndustry();
				for (int i = 0; i < listIndustry.size(); i++) {
					MIndustry mIndustry = listIndustry.get(i);
					if (!StringUtils.isEmpty(mIndustry.getName())) {
						buniessName += mIndustry.getName() + " ";
						industrysList.add(mIndustry.getName());
					}
				}
			}
			if (StringUtils.isEmpty(buniessName)) {
				buniessName = "必填";
			}
			busniessTv.setText(buniessName);
			break;
		/** 选择地址 */
		case requestCode_address: {
			mapAddress = (MMapAddress) data.getExtras().get(Util.IK_VALUE);
			addressText.setText(mapAddress.address);
		}
			break;
		/** 填写介绍 */
		case requestCode_introduce: {
			introduceText.setText(InitiatorDataCache.getInstance().introduce.contentText);
			if (mMeetingFileList != null) {
				mMeetingFileList.clear();
			} else {
				mMeetingFileList = new ArrayList<JTFile2ForHY>();
			}
			if (mMeetingPicList != null) {
				mMeetingPicList.clear();
			} else {
				mMeetingPicList = new ArrayList<MMeetingPic>();
			}
			mMeetingFileList = (ArrayList<JTFile2ForHY>) data.getSerializableExtra("mMeetingFileForList");
			mMeetingPicList = (ArrayList<MMeetingPic>) data.getSerializableExtra("mMeetingPicForList");
			taskId = data.getStringExtra("taskId");
		}
			break;
		/** 邀请参会人 */
		case requestCode_inviteAttend: {
			inviteAttendeeGvAdp.update(InitiatorDataCache.getInstance().inviteAttendSelectedMap);
			refreshInviteGV(0);
		}
			break;
		/** 邀请主讲人 */
		case requestCode_inviteSpeaker: {
			inviteSpeakerGvAdp.update(InitiatorDataCache.getInstance().inviteSpeakerSelectedMap);
			refreshInviteGV(1);

		}
			break;

		// 关联资源返回
		case requestCode_share:
			if (resultCode == Activity.RESULT_OK) {
				// 关联人脉：
				if (data.hasExtra(EConsts.Key.RELATED_PEOPLE_NODE)) {
					ConnectionNode connectionNode = (ConnectionNode) data.getSerializableExtra(EConsts.Key.RELATED_PEOPLE_NODE);
					peopleRelatedkeyWord = connectionNode.getMemo();
					if (connectionNode != null && connectionNode.getListConnections() != null && connectionNode.getListConnections().size() > 0) {
						ArrayList<Connections> listConnections = connectionNode.getListConnections();
						InitiatorDataCache.getInstance().sharePeopleHubSelectedMap.clear();
						for (int i = 0; i < listConnections.size(); i++) {
							Connections connections = listConnections.get(i);
							if (connections != null && connections.getJtContactMini() != null) {
								JTContactMini jtContactMini = connections.getJtContactMini();
								// 把关联的数据塞进sharePeopleHubSelectedMap
								InitiatorDataCache.getInstance().sharePeopleHubSelectedMap.put(jtContactMini.getId(), jtContactMini);
							}
						}
						// 将选择显示在界面上
						if (!Util.isNull(InitiatorDataCache.getInstance().sharePeopleHubSelectedMap)) {
							String sharePeoplehubStr = IniviteUtil.format(InitiatorDataCache.getInstance().sharePeopleHubSelectedMap, "，");
							peopleHubText.setText(sharePeoplehubStr);
						}
					}
				}

				// 关联组织
				if (data.hasExtra(EConsts.Key.RELATED_ORGANIZATION_NODE)) {
					ConnectionNode connectionNode = (ConnectionNode) data.getSerializableExtra(EConsts.Key.RELATED_ORGANIZATION_NODE);
					orgRelatedkeyWord = connectionNode.getMemo();
					if (connectionNode != null && connectionNode.getListConnections() != null && connectionNode.getListConnections().size() > 0) {
						ArrayList<Connections> listConnections = connectionNode.getListConnections();
						InitiatorDataCache.getInstance().shareOrgHubSelectedMap.clear();
						for (int i = 0; i < listConnections.size(); i++) {
							Connections connections = listConnections.get(i);
							if (connections != null && connections.getOrganizationMini() != null) {
								OrganizationMini organizationMini = connections.getOrganizationMini();
								// 把关联的数据塞进shareOrgHubSelectedMap
								InitiatorDataCache.getInstance().shareOrgHubSelectedMap.put(organizationMini.getId(), organizationMini);
							}
						}
						// 将选择显示在界面上
						if (!Util.isNull(InitiatorDataCache.getInstance().shareOrgHubSelectedMap)) {
							String shareOrghubStr = IniviteUtil.formatOrg(InitiatorDataCache.getInstance().shareOrgHubSelectedMap, "，");
							orgTextTv.setText(shareOrghubStr);
						}
					}
				}
				// 关联知识
				if (data.hasExtra(EConsts.Key.RELATED_KNOWLEDGE_NODE)) {
					KnowledgeNode knowledgeNode = (KnowledgeNode) data.getSerializableExtra(EConsts.Key.RELATED_KNOWLEDGE_NODE);
					knowledgeRelatedkeyWord = knowledgeNode.getMemo();
					if (knowledgeNode != null && knowledgeNode.getListKnowledgeMini2() != null && knowledgeNode.getListKnowledgeMini2().size() > 0) {
						ArrayList<KnowledgeMini2> listKnowledgeMini2 = knowledgeNode.getListKnowledgeMini2();
						InitiatorDataCache.getInstance().shareKnowleadgeSelectedMap.clear();
						for (int i = 0; i < listKnowledgeMini2.size(); i++) {
							KnowledgeMini2 knowledgeMini2 = listKnowledgeMini2.get(i);
							if (knowledgeMini2 != null) {
								InitiatorDataCache.getInstance().shareKnowleadgeSelectedMap.put(knowledgeMini2.id, knowledgeMini2);
							}
						}
						if (!Util.isNull(InitiatorDataCache.getInstance().shareKnowleadgeSelectedMap)) {
							int index = 0;
							knowleadgeText1.setVisibility(View.INVISIBLE);
							knowleadgeText2.setVisibility(View.GONE);
							knowleadgeText3.setVisibility(View.GONE);
							Iterator<Entry<Long, KnowledgeMini2>> iter = InitiatorDataCache.getInstance().shareKnowleadgeSelectedMap.entrySet().iterator();
							while (iter.hasNext()) {
								Map.Entry<Long, KnowledgeMini2> entry = iter.next();
								KnowledgeMini2 item = entry.getValue();
								if (index == 0) {
									knowleadgeText1.setVisibility(View.VISIBLE);
									knowleadgeText1.setText(item.title);
								} else if (index == 1) {
									knowleadgeText2.setVisibility(View.VISIBLE);
									knowleadgeText2.setText(item.title);
								} else if (index == 2) {
									knowleadgeText3.setVisibility(View.VISIBLE);
									knowleadgeText3.setText(item.title);
									break;
								} else {
									break;
								}
								index++;
							}
						}
					}
				}
				// 关联需求
				if (data.hasExtra(EConsts.Key.RELATED_AFFAIR_NODE)) {
					AffairNode affairNode = (AffairNode) data.getSerializableExtra(EConsts.Key.RELATED_AFFAIR_NODE);
					requarementRelatedkeyWord = affairNode.getMemo();
					if (affairNode != null && affairNode.getListAffairMini() != null && affairNode.getListAffairMini().size() > 0) {
						ArrayList<AffairsMini> listAffairMini = affairNode.getListAffairMini();
						InitiatorDataCache.getInstance().shareDemandSelectedMap.clear();
						for (int i = 0; i < listAffairMini.size(); i++) {
							AffairsMini affairsMini = listAffairMini.get(i);
							RequirementMini requirementMini = affairsMini.toRequirementMini();
							if (requirementMini != null) {
								InitiatorDataCache.getInstance().shareDemandSelectedMap.put(requirementMini.getmID(), requirementMini);
							}
						}

					}
					if (!Util.isNull(InitiatorDataCache.getInstance().shareDemandSelectedMap)) {
						int index = 0;
						demandText1.setVisibility(View.INVISIBLE);
						demandText2.setVisibility(View.GONE);
						demandText3.setVisibility(View.GONE);
						Iterator<Entry<Integer, RequirementMini>> iter = InitiatorDataCache.getInstance().shareDemandSelectedMap.entrySet().iterator();
						while (iter.hasNext()) {
							Map.Entry entry = (Map.Entry) iter.next();
							RequirementMini item = (RequirementMini) entry.getValue();
							if (index == 0) {
								demandText1.setVisibility(View.VISIBLE);
								demandText1.setText(item.mTitle);
							} else if (index == 1) {
								demandText2.setVisibility(View.VISIBLE);
								demandText2.setText(item.mTitle);
							} else if (index == 2) {
								demandText3.setVisibility(View.VISIBLE);
								demandText3.setText(item.mTitle);
								break;
							} else {
								break;
							}
							index++;
						}
					}
				}

			}
			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		releaseCacheData();
	}

	@Override
	public void bindData(int tag, Object object) {
		prgDialog.dismiss();
		titleRightTextBtnSetClickable(true);
		try {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			boolean isOpen = imm.isActive();
			if (isOpen) {
				imm.hideSoftInputFromWindow(InitiatorHYActivity.this.getCurrentFocus().getWindowToken(), 0);
			}
		} catch (Exception e) {
		}
		// 发起会议 & 存草稿
		if (tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_CREATE_MEETING && null != object) {
			SimpleResult simpleResult = (SimpleResult) object;
			if (null != simpleResult) {
				if (simpleResult.isSucceed()) {
					// TODO sunjianan 改过这
					if (meetingQuery.getMeetingStatus() == 1) {
						Toast.makeText(InitiatorHYActivity.this, "会议已创建", Toast.LENGTH_SHORT).show();

						Intent data = new Intent();
						setResult(RESULT_OK, data);
						// 创建成功后 跳转到 会议详情
						// ENavigate.startMeetingDetailsSquareActivity(getContext(),
						// simpleResult.getMeetingid());
						ENavigate.startMeetingDetailsSquareActivity(getContext(), simpleResult.getMeetingid(), MyMeetingActivity.class);
					} else if (meetingQuery.getMeetingStatus() == 0) {
						Toast.makeText(InitiatorHYActivity.this, "会议草稿保存成功", Toast.LENGTH_SHORT).show();
					}
					// 创建/保存成功 清空缓存
					InitiatorDataCache.getInstance().releaseAll();
					MRoadShowCacheFiles.getInstance().releaseAll();

					finishActivity();

				} else {
					Toast.makeText(InitiatorHYActivity.this, "创建失败", Toast.LENGTH_SHORT).show();
				}
			}
		}
		// 修改会议
		else if (tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_UPDATE && null != object) {
			SimpleResult flag = (SimpleResult) object;
			if (null != flag) {
				if (flag.isSucceed()) {
					// TODO sunjianan 改过这
					// 创建/保存成功 清空缓存
					InitiatorDataCache.getInstance().releaseAll();
					MRoadShowCacheFiles.getInstance().releaseAll();
					Intent data = new Intent();
					setResult(RESULT_OK, data);
					if (createType == 0) {
						Toast.makeText(InitiatorHYActivity.this, "会议已创建", Toast.LENGTH_SHORT).show();
						ENavigate.startMeetingDetailsSquareActivity(getContext(), alterMeetingData.getId(), MyMeetingActivity.class);
					} else {
						Toast.makeText(InitiatorHYActivity.this, "修改草稿成功", Toast.LENGTH_SHORT).show();
					}
					finishActivity();
				} else {
					Toast.makeText(InitiatorHYActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	@Override
	public void initView() {

	}

	@Override
	public void initData() {
	}

	@Override
	public void fillInfoOnToSelectListener(MFillinInfo fi) {
		List<MFillinInfo> datalist = fillinInfoSelectedGvAdp.getFillinfoList();
		if (Util.isNull(datalist)) {
			fillInfoSelectedLayout.setVisibility(View.VISIBLE);
		}
		datalist.add(fi);
		fillinInfoSelectedGvAdp.notifyDataSetChanged();
	}

	@Override
	public void fillInfoOnCancelSelectListener(MFillinInfo fi) {

		List<MFillinInfo> datalist = fillinInfoSelectedGvAdp.getFillinfoList();
		if (Util.isNull(datalist)) {
			fillInfoSelectedLayout.setVisibility(View.GONE);
		}
		for (MFillinInfo item : fillinInfoToSelectGvAdp.getFillinfoList()) {
			if (item.id == fi.id) {
				item.selected = false;
				break;
			}
		}
		fillinInfoToSelectGvAdp.notifyDataSetChanged();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 将介绍文本清楚
			// MRoadShowCacheFiles.getInstance().releaseAll();
			// InitiatorDataCache.getInstance().introduce.contentText = "";
			releaseCacheData();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		inviteSpeakerGvAdp.update(InitiatorDataCache.getInstance().inviteSpeakerSelectedMap);
		setGridViewParam(inviteSpreakerGridView, Util.DensityUtil.dip2px(InitiatorHYActivity.this, 60), Util.DensityUtil.dip2px(InitiatorHYActivity.this, 8), inviteSpeakerGvAdp.getListSize());
		inviteSpeakerGvAdp.notifyDataSetChanged();
		super.onResume();
	}
}
