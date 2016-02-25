package com.tr.ui.share;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.App;
import com.tr.R;
import com.tr.api.ConferenceReqUtil;
import com.tr.api.IMReqUtil;
import com.tr.image.ImageLoader;
import com.tr.model.SendMessageForwardSocial;
import com.tr.model.SendMessages;
import com.tr.model.conference.MListSociality;
import com.tr.model.conference.MMeetingDetail;
import com.tr.model.conference.MMeetingQuery;
import com.tr.model.conference.MMeetingTopic;
import com.tr.model.conference.MMeetingTopicQuery;
import com.tr.model.conference.MSociality;
import com.tr.model.im.ChatDetail;
import com.tr.model.im.MSendMessage;
import com.tr.model.obj.IMBaseMessage;
import com.tr.model.obj.JTFile;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.widgets.BasicListView;
import com.tr.ui.widgets.SocialShareDialog;
import com.tr.ui.widgets.SocialShareDialog.OnDialogClickListener;
import com.utils.common.EUtil;
import com.utils.common.GlobalVariable;
import com.utils.common.Util;
import com.utils.http.EAPIConsts;
import com.utils.http.EAPIConsts.ConferenceReqType;
import com.utils.http.IBindData;
import com.utils.image.AnimateFirstDisplayListener;
import com.utils.string.StringUtils;
import com.utils.time.TimeUtil;

/**
 * 社交分享页面
 */
public class SocialShareActivity extends JBaseFragmentActivity implements IBindData, IXListViewListener, OnDialogClickListener {

	@SuppressWarnings("unused")
	private final String TAG = getClass().getSimpleName(); // 页面标识

	private TextView tipTv; // 提示框
	private EditText searchEt; // 搜索框
	private XListView socialLv; // 社交列表控件（会议、私聊、群聊）
	private SocialShareDialog shareDialog; // 消息弹出框
	private List<MSociality> listSocial; // 社交列表
	private List<MSociality> listSocialSearch; // 搜索到的社交列表
	private SocialListAdapter socialAdapter; // 列表适配器
	private ArrayList<JTFile> listJtFile; // 待分享的对象组

	/** 创建新畅聊 */
	private TextView createNewIMTv;

	// private TextView mGlobalCreatButton;
	/*转发数据集合*/
	private HashMap<String,SendMessages> sendMessagesForwardingList = new HashMap<String, SendMessages>();
	private HashMap<String,SendMessages> sendMessagesForwardingTopicList = new HashMap<String, SendMessages>();
	private JTFile jtFile;
	/*确定转发分享*/
	private MenuItem socialShareConfirm;
	protected Handler mNetHandler = new Handler();
	
	@Override
	public void initJabActionBar() {
//		jabGetActionBar().setTitle("到社交");
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "到社交", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}


	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		getMenuInflater().inflate(R.menu.social_share, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.social_share_confirm: // 确定转发
			if((sendMessagesForwardingList != null && sendMessagesForwardingList.size() >= 1)
			|| (sendMessagesForwardingTopicList != null && sendMessagesForwardingTopicList.size() >=1)){
				showLoadingDialog();
				/*if(jtFile.isOnlineUrl){
					String cacheUrl = jtFile.mUrl; // 缓存网址
					CommonReqUtil.doFetchExternalKnowledgeUrl(this, this, cacheUrl, true, null);
				}else{*/
					IMReqUtil.sendMessageForForwardingSocial(this, this, mNetHandler, sendMessagesForwardingList,sendMessagesForwardingTopicList,listJtFile);	
				/*}*/
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu){ MenuItem
	 * menuItem = menu.add(0, 0, 0, "更多");
	 * menuItem.setIcon(R.drawable.add_new_knowledge);
	 * menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS); return true; }
	 * 
	 * @Override public boolean onOptionsItemSelected(MenuItem item){
	 * if(listJtFile.size() == 1){
	 * ENavigate.startIMRelationSelectActivityEx(this, listJtFile.get(0)); }
	 * else if(listJtFile.size() > 1){
	 * ENavigate.startIMRelationSelectActivityEx(this, listJtFile); } return
	 * true; }
	 */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_social_share);
		initVars();
		initControls();
		showLoadingDialog();
		ConferenceReqUtil.getMyForwardingSocial(this, this, null);
	}

	/**
	 * 初始化变量
	 */
	private void initVars() {
		listSocial = new ArrayList<MSociality>();
		listSocialSearch = new ArrayList<MSociality>();
		socialAdapter = new SocialListAdapter(this);
		listJtFile = new ArrayList<JTFile>();
		Intent intent = getIntent();
		// 分享单个资源
		if (intent.hasExtra(ENavConsts.EShareParam)) {
			jtFile = (JTFile) intent.getSerializableExtra(ENavConsts.EShareParam);
			if (jtFile != null) {
				listJtFile.add(jtFile);
			}
		}
		// 分享多个资源
		if (intent.hasExtra(ENavConsts.EShareParamList)) {
			@SuppressWarnings("unchecked")
			ArrayList<JTFile> jtFiles = (ArrayList<JTFile>) intent.getSerializableExtra(ENavConsts.EShareParamList);
			if (jtFiles != null) {
				listJtFile.addAll(jtFiles);
			}
		}
	}

	/**
	 * 初始化控件
	 */
	private void initControls() {
		socialLv = (XListView) findViewById(R.id.socialLv);
		createNewIMTv = (TextView) findViewById(R.id.createNewIM);
		socialLv.setAdapter(socialAdapter);
		socialLv.setPullLoadEnable(false);
		socialLv.setPullRefreshEnable(true);
		socialLv.setXListViewListener(this);
		shareDialog = new SocialShareDialog(this);
		shareDialog.setOnDialogClickListener(this);

		createNewIMTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (listJtFile!=null&&!listJtFile.isEmpty()) {
					
				for (int i = 0; i < listJtFile.size(); i++) {
					JTFile jtFile = listJtFile.get(i);
					if (jtFile != null) {
						switch (jtFile.mType) {
						case JTFile.TYPE_REQUIREMENT:
							jtFile.mSuffixName = "";
							break;
						case JTFile.TYPE_KNOWLEDGE:
						case JTFile.TYPE_KNOWLEDGE2:
						case JTFile.TYPE_CONFERENCE:
							jtFile.mFileName = "";
							break;
						default:
							break;
						}
					}
				}
				}
				ENavigate.startIMRelationSelectActivity(SocialShareActivity.this, null, null, 0, null, listJtFile);
				finish();
			}
		});
		// 搜索
		searchEt = (EditText) findViewById(R.id.searchEt);
		searchEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (TextUtils.isEmpty(s)) {
					tipTv.setVisibility(View.GONE);
					socialAdapter.updateAdapter(listSocial);
				} else {
					if (listSocial.size() <= 0) {
						return;
					}
					listSocialSearch.clear();
					for (MSociality sociality : listSocial) {
						if (sociality.getSocialDetail() != null) {
							if ((!TextUtils.isEmpty(sociality.getSocialDetail().getContent()) && (sociality.getSocialDetail().getContent().contains(s.toString())))
									|| (!TextUtils.isEmpty(sociality.getTitle()) && (sociality.getTitle().contains(s.toString())))) {
								listSocialSearch.add(sociality);
							}
						}
					}
					if (listSocialSearch.size() > 0) {
						tipTv.setVisibility(View.GONE);
					} else {
						tipTv.setVisibility(View.VISIBLE);
					}
					socialAdapter.updateAdapter(listSocialSearch);
				}
			}
		});
		// 提示框
		tipTv = (TextView) findViewById(R.id.tipTv);

		/*
		*//** 万能创建键 */
		/*
		 * mGlobalCreatButton = (TextView)
		 * findViewById(R.id.home_global_creat_buttom); final
		 * FloatMenuPopUpWindow floatMenuPopUpWindow = new
		 * FloatMenuPopUpWindow(this,mGlobalCreatButton);
		 * mGlobalCreatButton.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { if
		 * (floatMenuPopUpWindow.isShowing()) { floatMenuPopUpWindow.dismiss();
		 * floatMenuPopUpWindow.showAnchorRotateAnimation(true); } else {
		 * floatMenuPopUpWindow.showAtLocation();
		 * floatMenuPopUpWindow.showAnchorRotateAnimation(false); } } });
		 */
	}

	/**
	 * 转换为枚举类型
	 * 
	 * @param type
	 * @return
	 */
	private SocialType getSocialType(int type) {
		switch (type) {
		case 1:
			return SocialType.Chat;
		case 2:
			return SocialType.MUCChat;
		case 3:
			return SocialType.InProgressMeeting;
		case 4:
			return SocialType.UnOpenMeeting;
		case 5:
			return SocialType.EndedMeeting;
		case 6:
			return SocialType.Notification;
		case 7:
			return SocialType.Invitation;
		default:
			return SocialType.Unknown;
		}
	}

	/**
	 * 分享到会议或畅聊
	 * 
	 * @param social
	 * @param listJtFile
	 */
	private void doShare(MSociality social, ArrayList<JTFile> listJtFile,ImageView checkboxChatIV) {
		if(!sendMessagesForwardingList.containsKey(social.getId()+"")){
			SendMessages  sendMessages = new SendMessages();
			sendMessages.chatName = social.getTitle();
			setSendMessagesType(social, sendMessages);
//			if (jtFile!=null) {
				sendMessages.type = IMBaseMessage.convertJTFileType2IMBaseMessageType(listJtFile.get(0).mType);
				String filename = listJtFile.get(0).mFileName;
				String shareTitle = "";
				switch(listJtFile.get(0).mType){
				case JTFile.TYPE_KNOWLEDGE:
				case JTFile.TYPE_KNOWLEDGE2:
					if(!TextUtils.isEmpty(filename)){
						shareTitle = "分享了["+filename+"]";
					}else{
						shareTitle = "分享了[知识]";
					}
					break;
				case JTFile.TYPE_CONFERENCE:
					shareTitle = "分享了[会议]";
					break;
				case JTFile.TYPE_DEMAND:
				case JTFile.TYPE_REQUIREMENT:
					shareTitle = "分享了[需求]";
					break;
				case JTFile.TYPE_ORGANIZATION:
					shareTitle = "分享了[组织]";
					break;
				case JTFile.TYPE_CLIENT:
					shareTitle = "分享了[客户]";
					break;
				case JTFile.TYPE_JTCONTACT_ONLINE:
				case JTFile.TYPE_JTCONTACT_OFFLINE:
					shareTitle = "分享了[人脉]";
					break;
				case JTFile.TYPE_TEXT:
					shareTitle = listJtFile.get(0).mFileName;
					break;
				case JTFile.TYPE_COMMUNITY:
					shareTitle = "分享了[社群]";
					break;
					
				}
				sendMessages.text = shareTitle;
//			}
			sendMessages.senderName = App.getNick();
			sendMessages.messageID = EUtil.genMessageID();
			sendMessages.fromTime =  EUtil.getFormatFromDate(new java.util.Date());
			sendMessages.size = 1;
			sendMessagesForwardingList.put(social.getId()+"", sendMessages);
		}else{
			sendMessagesForwardingList.remove(social.getId()+"");
		}
		socialAdapter.notifyDataSetChanged();
	}


	/***
	 * 私聊 与 群聊、会议 传递的数据结构不同
	 * @param social
	 * @param sendMessages
	 */
	private void setSendMessagesType(MSociality social,
			SendMessages sendMessages) {
		SocialType mSocialType = getSocialType(social.getType());
		switch (mSocialType) {
		case Chat: // 私聊
			sendMessages.jtContactID = social.getId();
			break;
		case MUCChat: // 群聊
			sendMessages.mucID = social.getId();
			break;
		case InProgressMeeting: // 进行中的会议
			sendMessages.mucID = social.getId();
			break;
		}
	}
	
	/**
	 * 分享到会议或畅聊
	 * 
	 * @param social
	 * @param listJtFile
	 */
	private void doShare(MSociality social, ArrayList<JTFile> listJtFile) {
		SocialType socialType = getSocialType(social.getType());
		if (socialType == SocialType.Chat) { // 分享到私聊
			ChatDetail chatDetail = new ChatDetail();
			chatDetail.setThatID(social.getId() + "");
			chatDetail.setThatImage(social.getSocialDetail().getListImageUrl().get(0));
			chatDetail.setThatName(social.getTitle());
			ENavigate.startIMActivity(this, chatDetail, listJtFile);
			finish();
		} else if (socialType == SocialType.MUCChat) { // 分享到群聊
			
			SendMessages  sendMessages = new SendMessages();
			sendMessages.chatName = social.getTitle();
			sendMessages.mucID = social.getId();
			if (jtFile!=null) {
				sendMessages.type = IMBaseMessage.convertJTFileType2IMBaseMessageType(jtFile.mType);
				sendMessages.text = jtFile.mFileName;
			}
			
			sendMessages.senderName = App.getNick();
			sendMessages.messageID = EUtil.genMessageID();
			sendMessages.fromTime =  EUtil.getFormatFromDate(new java.util.Date());
			sendMessages.size = 1;
			sendMessagesForwardingList.put(social.getId()+"", sendMessages);
			
			ENavigate.startIMGroupActivity(this, social.getId() + "", listJtFile);
			finish();
		} else if (socialType == SocialType.InProgressMeeting) { // 分享到会议（无主讲）
			showLoadingDialog();
			ConferenceReqUtil.getForwardingMeetingData(this, this, social.getId(), social.getListMeetingTopic().get(0).getId(), null);
		}
	}

	/**
	 * 分享到分会场
	 * 
	 * @param meetingQuery
	 * @param topicQuery
	 * @param listJtFile
	 */
	private void doShare(MMeetingQuery meetingQuery, MMeetingTopicQuery topicQuery, ArrayList<JTFile> listJtFile) {
		ENavigate.startConferenceChatActivity(this, meetingQuery, topicQuery, listJtFile);
		finish();
	}

	/**
	 * 是否需要弹出提示框
	 */
	private boolean needShowDialog(ArrayList<JTFile> listJtFile) {
		boolean result = false;
		if (listJtFile.size() > 0
				&& listJtFile.size() <= 1
				&& (listJtFile.get(0).mType == JTFile.TYPE_KNOWLEDGE || listJtFile.get(0).mType == JTFile.TYPE_KNOWLEDGE2 || listJtFile.get(0).mType == JTFile.TYPE_REQUIREMENT || listJtFile.get(0).mType == JTFile.TYPE_CONFERENCE)) { // 单条的知识、需求、会议
			result = true;
		}
		return result;
	}

	/**
	 * 社交列表适配器
	 * 
	 * @author leon
	 */
	private class SocialListAdapter extends BaseAdapter implements OnClickListener,OnItemClickListener{

		private Context context;
		private List<MSociality> listSocial;
		private MSociality social;
		private ViewHolder holder;
		private Map<Long,Boolean> checkShowMeetingHashMap;
		private TopicListAdapter topicListadapter;
		
		public SocialListAdapter(Context context) {
			this.context = context;
			this.listSocial = new ArrayList<MSociality>();
		}

		/**
		 * 更新适配器
		 * 
		 * @param listSocial
		 */
		public void updateAdapter(List<MSociality> listSocial) {
			if (listSocial != null) {
				this.listSocial = listSocial;
				notifyDataSetChanged();
			}
		}

		@Override
		public int getCount() {
			return listSocial.size();
		}

		@Override
		public Object getItem(int position) {
			return listSocial.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			social = listSocial.get(position);
			if (convertView == null) {
				convertView = View.inflate(context, R.layout.list_item_share_social_item, null);
				holder = new ViewHolder();
				checkShowMeetingHashMap = new HashMap<Long,Boolean>();
				holder.parentLl = (LinearLayout) convertView.findViewById(R.id.parentLl);
//				holder.show_meetings = (LinearLayout) convertView.findViewById(R.id.show_meetings);
//				holder.share_check_response_area = (LinearLayout) convertView.findViewById(R.id.share_check_response_area);
				holder.checkboxChatIV = (ImageView) convertView.findViewById(R.id.checkbox_chat_IV);
				holder.avatarIv = (ImageView) convertView.findViewById(R.id.avatarIv);
				holder.avatarGv = (GridView) convertView.findViewById(R.id.avatarGv);
				holder.titleTv = (TextView) convertView.findViewById(R.id.titleTv);
				holder.contentTv = (TextView) convertView.findViewById(R.id.contentTv);
				holder.updateTimeTv = (TextView) convertView.findViewById(R.id.updateTimeTv);
				holder.topicBlv = (BasicListView) convertView.findViewById(R.id.topicBlv);
				holder.topicBlv.setHaveScrollbar(false);
				holder.folderIv = (ImageView) convertView.findViewById(R.id.folderIv);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// 填充数据
			initWithData(social);
			return convertView;
		}
//		@SuppressLint("SimpleDateFormat")
		public void initWithData(MSociality social) {
			if (social != null) {
				holder.checkboxChatIV.setOnClickListener(this);
//				holder.share_check_response_area.setOnClickListener(this);
//				holder.topicBlv.setOnItemClickListener(this);
				/*展开子会议*/
				holder.folderIv.setOnClickListener(this);
				// 保存social对象
//				holder.share_check_response_area.setTag(social);
				holder.checkboxChatIV.setTag(social);
				holder.folderIv.setTag(social); 

				
				if(social.getListMeetingTopic() == null || social.getListMeetingTopic().size() <=0){
					if(sendMessagesForwardingList.containsKey(social.getId()+"")){//非会议
						holder.checkboxChatIV.setImageResource(R.drawable.hy_check_pressed);
					}else {
						holder.checkboxChatIV.setImageResource(R.drawable.hy_check_norm);
					}		
				}else {
					if(checkMeetingTopic(social)){
						holder.checkboxChatIV.setImageResource(R.drawable.hy_check_norm);
					}else {
						holder.checkboxChatIV.setImageResource(R.drawable.hy_check_pressed);
					}
				}

				/*if(getSocialType(social.getType()) == SocialType.InProgressMeeting 
//						&& social.getMeetingType() == 1
						&& social.getListMeetingTopic() != null 
						&& social.getListMeetingTopic().size() >= 1) { // 有主讲会议
					if(checkMeetingTopic(social)){
						holder.checkboxChatIV.setImageResource(R.drawable.hy_check_norm);
					}else {
						holder.checkboxChatIV.setImageResource(R.drawable.hy_check_pressed);
					}
				}*/
				/*社交根据不同的社交场合展示UI*/
				showCommonUI(social);
			}
		}

		/**
		 * 社交根据不同的社交场合展示UI
		 * @param social
		 */
		private void showCommonUI(final MSociality social) {
			
			// 根据不同类型初始化列表，"type":"1-私聊，2-群聊，3-进行中的会议，4-未开始，5-已结束的会议，6-通知，7-邀请函",
			SocialType socialType = getSocialType(social.getType());
			switch (socialType) {
			case Chat: // 私聊
				if (social.getSocialDetail() != null && social.getSocialDetail().getListImageUrl() != null
					&& social.getSocialDetail().getListImageUrl().size() > 0){
					String picString = social.getSocialDetail().getListImageUrl().get(0);
					Util.initAvatarImage(context, holder.avatarIv, social.getTitle(),picString, 1, 1);
				}
				holder.avatarIv.setVisibility(View.VISIBLE);
				holder.avatarGv.setVisibility(View.GONE);
				holder.titleTv.setText(TextUtils.isEmpty(social.getTitle()) ? "" : social.getTitle());
				holder.contentTv.setText(TextUtils.isEmpty(social.getSocialDetail().getContent()) ? "" : social.getSocialDetail().getContent());
				holder.updateTimeTv.setText(TimeUtil.TimeFormat(social.getTime()));
				// 议题列表
				holder.topicBlv.setVisibility(View.GONE);
				// 展开按钮
				holder.folderIv.setVisibility(View.GONE);
				break;
			case MUCChat: // 群聊
				// 头像
				ImageGridAdapter chatImageGridAdapter = new ImageGridAdapter(context);
				chatImageGridAdapter.updateAdapter(social.getSocialDetail().getListImageUrl(), social);
				holder.avatarGv.setAdapter(chatImageGridAdapter);
				holder.avatarIv.setVisibility(View.GONE);
				holder.avatarGv.setVisibility(View.VISIBLE);
				// 标题
				holder.titleTv.setText(TextUtils.isEmpty(social.getTitle()) ? "" : social.getTitle());
				// 内容
				holder.contentTv.setText(TextUtils.isEmpty(social.getSocialDetail().getContent()) ? "" : social.getSocialDetail().getContent());
				// 更新时间（格式化后）
				holder.updateTimeTv.setText(TimeUtil.TimeFormat(social.getTime()));
				// 议题列表
				holder.topicBlv.setVisibility(View.GONE);
				// 展开按钮
				holder.folderIv.setVisibility(View.GONE);
				break;
			case InProgressMeeting: // 进行中的会议
				// 会议图片
				if (social.getSocialDetail().getListImageUrl() != null && social.getSocialDetail().getListImageUrl().size() > 0) {
					ImageLoader.load(holder.avatarIv, social.getSocialDetail().getListImageUrl().get(0), R.drawable.meeting_logo_a);
				} else {
					holder.avatarIv.setImageResource(R.drawable.meeting_logo_a);
				}
				holder.avatarGv.setVisibility(View.GONE);
				holder.avatarIv.setVisibility(View.VISIBLE);
				// 会议标题
				holder.titleTv.setText(TextUtils.isEmpty(social.getTitle()) ? "" : social.getTitle());
				// 内容
				String time = social.getSocialDetail().getContent();
				if (!TextUtils.isEmpty(time)) {
					try {
						java.util.Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
						time = new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(date);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				holder.contentTv.setText(time);
				// 时间（格式化后）
				holder.updateTimeTv.setText(TimeUtil.TimeFormat(social.getTime()));
				topicListadapter = new TopicListAdapter(context);
				holder.topicBlv.setAdapter(topicListadapter);
//				if (social.getMeetingType() == 1 
//						&& social.getListMeetingTopic() != null 
//						&& social.getListMeetingTopic().size() >= 1) { // 有主讲
					topicListadapter.updateAdapter(social.getListMeetingTopic());
					holder.folderIv.setVisibility(View.VISIBLE);
//				} else { // 无主讲
//					topicListadapter.updateAdapter(new ArrayList<MMeetingTopic>());
//					holder.folderIv.setVisibility(View.GONE);
//				}
				holder.topicBlv.setOnItemClickListener(this);
				// 是否显示列表
				if (!checkShowMeetingHashMap.containsKey(social.getId())) {
					showMeetingTree(true);
				} else {
					showMeetingTree(!checkShowMeetingHashMap.get(social.getId()));
				}
				break;
			default:
				break;
			}
		}

		/**
		 * 展示子会议
		 * @param bool
		 */
		private void showMeetingTree(boolean bool) {
			if(bool){
				holder.folderIv.setImageResource(R.drawable.social_share_arrow_down);
				holder.topicBlv.setVisibility(View.GONE);
			}else {
				holder.folderIv.setImageResource(R.drawable.social_share_arrow_up);
				holder.topicBlv.setVisibility(View.VISIBLE);
			}
		}

		/**
		 * 检测主讲会议中子会议是否有被选中
		 */
		public boolean checkMeetingTopic(MSociality mSociality){
			for (int i = 0; i < mSociality.getListMeetingTopic().size(); i++) {
				String mMeetingTopicIdString =  
						mSociality.getListMeetingTopic() != null ? mSociality.getListMeetingTopic().get(i).getId() + "" : "";
				if(!sendMessagesForwardingTopicList.containsKey(mMeetingTopicIdString)){
					return true;
				}
			}
			return false;
		}
		
		@Override
		public void onClick(View v) {
			 // 转发到畅聊
			switch (v.getId()) {
//			case R.id.share_check_response_area:
			case R.id.checkbox_chat_IV://多选按钮
				MSociality mSociality = (MSociality) v.getTag();
					if (
//							getSocialType(mSociality.getType()) == SocialType.InProgressMeeting 
//							&& mSociality.getMeetingType() == 1
							 mSociality.getListMeetingTopic() != null 
							&& mSociality.getListMeetingTopic().size() >= 1 ) { // 会议
						if(checkMeetingTopic(mSociality)){
							List<MMeetingTopic> listMeetingTopic = mSociality.getListMeetingTopic();
							for (MMeetingTopic mMeetingTopic : listMeetingTopic) {
								SendMessages  sendMessages = new SendMessages();
								sendMessages.chatName = mMeetingTopic.getTopicContent();
								sendMessages.mucID = mSociality.getId();
								sendMessages.topicId = mMeetingTopic.getId();
								sendMessages.type = IMBaseMessage.convertJTFileType2IMBaseMessageType(listJtFile.get(0).mType);
								sendMessages.text = listJtFile.get(0).mFileName;
								sendMessages.senderName = App.getNick();
								sendMessages.messageID = EUtil.genMessageID();
								sendMessages.size = 1;
								sendMessages.fromTime =  EUtil.getFormatFromDate(new java.util.Date());
								sendMessagesForwardingTopicList.put(mMeetingTopic.getId()+"", sendMessages);
							}
						}else{
							List<MMeetingTopic> listMeetingTopic = mSociality.getListMeetingTopic();
							for (MMeetingTopic mMeetingTopic : listMeetingTopic) {
								sendMessagesForwardingTopicList.remove(mMeetingTopic.getId()+"");
							}
						}
						socialAdapter.notifyDataSetChanged();
						return;
					}
//					if (needShowDialog(listJtFile)) { // 弹出对话框
//						shareDialog.show(mSociality, listJtFile.get(0));
						doShare(mSociality, listJtFile,holder.checkboxChatIV); // 直接分享
//					} else {
//						doShare(mSociality, listJtFile,holder.checkboxChatIV); // 直接分享
//					}
//				}else {
//					holder.checkboxChatIV.setImageResource(R.drawable.hy_check_norm);
//			
//					if (getSocialType(mSociality.getType()) == SocialType.InProgressMeeting 
//							&& mSociality.getMeetingType() == 1
//							&& mSociality.getListMeetingTopic() != null 
//							&& mSociality.getListMeetingTopic().size() >= 1) { // 有主讲会议
//						sendMessagesForwardingList.remove(mSociality.getId()+"");
//					}
//					if (needShowDialog(listJtFile)) { // 弹出对话框
////						shareDialog.show(mSociality, listJtFile.get(0));
//						doShare(mSociality, listJtFile,holder.checkboxChatIV); // 直接分享
//					} else {
//						doShare(mSociality, listJtFile,holder.checkboxChatIV); // 直接分享
//					}
//				}

				break;
			case R.id.folderIv: // 展开收起
				MSociality sociality = (MSociality) v.getTag();
				if(!checkShowMeetingHashMap.containsKey(sociality.getId())){//false:隐藏   true:显示
					checkShowMeetingHashMap.put(sociality.getId(), checkShowMeetingHashMap.get(sociality.getId()) != null ? !checkShowMeetingHashMap.get(sociality.getId()) : true);
				}else{
					checkShowMeetingHashMap.put(sociality.getId(), !checkShowMeetingHashMap.get(sociality.getId()));
				}
				socialAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
					
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			 // 转发到分会场
			MMeetingTopic meetingTopic = (MMeetingTopic) parent.getAdapter().getItem(position);
		/*	if (needShowDialog(listJtFile)) { // 弹出对话框
				shareDialog.show(meetingTopic, listJtFile.get(0));
			} else {*/
				// 获取分会场详情
//				showLoadingDialog();
//				ConferenceReqUtil.getForwardingMeetingData(SocialShareActivity.this, SocialShareActivity.this, meetingTopic.getMeetingId(), meetingTopic.getId(), null);
			
				if(!sendMessagesForwardingTopicList.containsKey(meetingTopic.getId()+"")){
					SendMessages  sendMessages = new SendMessages();
					sendMessages.chatName = meetingTopic.getTopicContent();
					sendMessages.mucID = meetingTopic.getMeetingId();
					sendMessages.topicId = meetingTopic.getId();
//					if (jtFile!=null) {
					sendMessages.type = IMBaseMessage.convertJTFileType2IMBaseMessageType(listJtFile.get(0).mType);
//						sendMessages.text = jtFile.mFileName;
//					}
					
					sendMessages.senderName = App.getNick();
					sendMessages.messageID = EUtil.genMessageID();
					sendMessages.size = 1;
					sendMessages.fromTime =  EUtil.getFormatFromDate(new java.util.Date());
					sendMessagesForwardingTopicList.put(meetingTopic.getId()+"", sendMessages);
				}else{
					sendMessagesForwardingTopicList.remove(meetingTopic.getId()+"");
				}
				socialAdapter.notifyDataSetChanged();
			/*}*/
					
		}
		
		private class ViewHolder{
			private LinearLayout parentLl; // 父控件
//			private LinearLayout share_check_response_area; // 多选事件响应区
//			private LinearLayout show_meetings; // 多选事件响应区
			private ImageView avatarIv; // 头像
			private GridView avatarGv; // 头像
			private TextView titleTv; // 标题
			private TextView contentTv; // 内容
			private TextView updateTimeTv; // 更新时间
			private BasicListView topicBlv; // 分会场列表
			private ImageView folderIv; // 展开收起
			private ImageView checkboxChatIV;//多选框
		}
	}

	/**
	 * 图像Grid适配器
	 * 
	 * @author leon
	 */
	private class ImageGridAdapter extends BaseAdapter {

		private Context context;
		private MSociality social;
		private List<String> listImageUrls;
		private String[] names;
		public ImageGridAdapter(Context context) {
			this.context = context;
			listImageUrls = new ArrayList<String>();
		}

		/**
		 * 更新适配器
		 * 
		 * @param listImageUrls
		 * @param socialType
		 */
		public void updateAdapter(List<String> listImageUrls, MSociality social) {
			// 社交类型
			this.social = social;
			names=social.getTitle().split(",");
			// 图片列表
			if (listImageUrls != null && listImageUrls.size() > 0) {
				this.listImageUrls = listImageUrls;
			}
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return Math.max(listImageUrls.size(), 1); // 至少有一个项目
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_share_social_avatar_item, parent, false);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (listImageUrls.size() > position) {
				try {
					holder.initWithData(listImageUrls.get(position), names[position]);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return convertView;
		}

		class ViewHolder {

			ImageView avatarIv;

			public ViewHolder(View container) {
				avatarIv = (ImageView) container.findViewById(R.id.avatarIv);
			}

			public void initWithData(String url, String flag) throws Exception{
				ImageLoader.load(avatarIv, url, R.drawable.ic_default_avatar);
			}
		}
	}

	/**
	 * 议题列表适配器
	 * 
	 * @author leon
	 */
	private class TopicListAdapter extends BaseAdapter {

		private Context context;
		private List<MMeetingTopic> listMeetingTopic;

		public TopicListAdapter(Context context) {
			this.context = context;
			listMeetingTopic = new ArrayList<MMeetingTopic>();
		}

		public void updateAdapter(List<MMeetingTopic> listMeetingTopic) {
			if (listMeetingTopic != null) {
				this.listMeetingTopic = listMeetingTopic;
				notifyDataSetChanged();
			}
		}

		@Override
		public int getCount() {
			return listMeetingTopic.size();
		}

		@Override
		public Object getItem(int position) {
			return listMeetingTopic.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			MMeetingTopic meetingTopic = listMeetingTopic.get(position);
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.list_item_share_social_topic_item, parent, false);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.initWithData(meetingTopic, position);
//			/**分享选择多个议题*/
//			holder.itemShareSocialTopicCheckboxIV.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					//保存多个议题的id
//					
//				}
//			});
			return convertView;
		}

		private class ViewHolder {

			private ImageView typeIv; // 类型
			private TextView titleTv; // 标题
			// private TextView updateTimeTv; // 更新时间
			/*会议列表选择*/
			private ImageView itemShareSocialTopicCheckboxIV;

			public ViewHolder(View container) {
				if (container != null) {
					typeIv = (ImageView) container.findViewById(R.id.typeIv);
					titleTv = (TextView) container.findViewById(R.id.titleTv);
					itemShareSocialTopicCheckboxIV = (ImageView) container.findViewById(R.id.item_share_social_topic_checkbox_IV);
					// updateTimeTv = (TextView)
					// container.findViewById(R.id.updateTimeTv);
				}
			}

			public void initWithData(MMeetingTopic data, int position) {
				if (data != null) {
					typeIv.setImageResource((position + 1) % 2 == 1 ? R.drawable.social_share_topic_type_1 : R.drawable.social_share_topic_type_2);
					titleTv.setText(data.getTopicContent());
					// updateTimeTv.setText(data.getUpdateTime());
						if(sendMessagesForwardingTopicList.containsKey(data.getId()+"")){//子会议
							itemShareSocialTopicCheckboxIV.setImageResource(R.drawable.hy_check_pressed);
						}else {
							itemShareSocialTopicCheckboxIV.setImageResource(R.drawable.hy_check_norm);
						}
				}
			}
		}
	}

	/**
	 * 社交类型
	 */
	private enum SocialType {
		// 1-私聊，2-群聊，3-进行中的会议，4-未开始的会议，5-已结束的会议，6-通知，7-邀请函，8-未知",
		Chat, MUCChat, InProgressMeeting, UnOpenMeeting, EndedMeeting, Notification, Invitation, Unknown
	}

	@Override
	public void bindData(int tag, Object object) {
		if (tag == ConferenceReqType.CONFERENCE_REQ_GET_MY_FORWARDING_SOCIAL) { // 获取转发列表
			dismissLoadingDialog(); // 停止加载
			socialLv.stopRefresh(); // 停止刷新
			if (object != null) {
				MListSociality socialObject = (MListSociality) object;
				if (socialObject.getListSocial() != null) {
					listSocial = socialObject.getListSocial();
					socialAdapter.updateAdapter(listSocial); // 更新列表
				}
			}
		} else if (tag == ConferenceReqType.CONFERENCE_REQ_GET_FORWARD_MEETING_DATA) { // 获取会议详情
			dismissLoadingDialog();
			if (object != null) {
				MMeetingDetail meetingDetail = (MMeetingDetail) object;
				if (meetingDetail != null && meetingDetail.getMeetingQuery() != null && meetingDetail.getMeetingQuery().getListMeetingTopicQuery() != null
						&& meetingDetail.getMeetingQuery().getListMeetingTopicQuery().size() > 0) {
					doShare(meetingDetail.getMeetingQuery(), meetingDetail.getMeetingQuery().getListMeetingTopicQuery().get(0), listJtFile); // 分享
				}
			}
		} else if(tag == EAPIConsts.IMReqType.IM_REQ_SEND_MESSAGE_FOR_FORWARD_SOCIAL ){//(多选)转发到社交
			dismissLoadingDialog(); // 停止加载
			MSendMessage sm = (MSendMessage) object;
			StringBuilder sBuilder = new StringBuilder();
			if (sm != null && sm.isSucceed()) { // 消息发送成功
				for (int i = 0; i < sm.forwardSocialsResponseDataList.size(); i++) {
					SendMessageForwardSocial sendMessageForwardSocial = sm.forwardSocialsResponseDataList.get(i);
					if(sendMessageForwardSocial.responseCode == -1){
						sBuilder.append(sendMessageForwardSocial.errorMessage);
						sBuilder.append("  ");
					}
				}
				if (TextUtils.isEmpty(sBuilder.toString().trim())) {
					Toast.makeText(SocialShareActivity.this,"分享成功", Toast.LENGTH_LONG).show();
				}else {
					Toast.makeText(SocialShareActivity.this,sBuilder.toString() , Toast.LENGTH_LONG).show();
				}
			} else { // 消息发送失败
				Toast.makeText(SocialShareActivity.this, "分享失败",Toast.LENGTH_LONG).show();
			}
			finish();
		}
	}

	/**
	 * 下拉刷新
	 */
	@Override
	public void onRefresh() {
		ConferenceReqUtil.getMyForwardingSocial(this, this, null);
	}

	@Override
	public void onLoadMore() {

	}

	@Override
	public void onCancel() {

	}

	@Override
	public void onOK(Object object, JTFile jtFile) {
		listJtFile.set(0, jtFile);
		if (object instanceof MSociality) {
			doShare((MSociality) object, listJtFile);
		} else if (object instanceof MMeetingTopic) {
			showLoadingDialog();
			ConferenceReqUtil.getForwardingMeetingData(SocialShareActivity.this, SocialShareActivity.this, ((MMeetingTopic) object).getMeetingId(), ((MMeetingTopic) object).getId(), null);
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		ConferenceReqUtil.getMyForwardingSocial(this, this, null);
	}
}
