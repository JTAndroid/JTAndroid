package com.tr.imservice;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import me.leolin.shortcutbadger.ShortcutBadger;

import org.json.JSONObject;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMMessage;
import com.easemob.exceptions.EaseMobException;
import com.tr.App;
import com.tr.R;
import com.tr.db.ChatRecordDBManager;
import com.tr.db.ConnectionsDBManager;
import com.tr.db.MeetingRecordDBManager;
import com.tr.db.SocialityDBManager;
import com.tr.db.VoiceFileDBManager;
import com.tr.model.conference.MSociality;
import com.tr.model.im.ChatDetail;
import com.tr.model.im.IMConsts;
import com.tr.model.im.MJTPushMessage;
import com.tr.model.im.MNotifyMessageBox;
import com.tr.model.obj.ChatMessage;
import com.tr.model.obj.IMBaseMessage;
import com.tr.model.obj.JTFile;
import com.tr.model.obj.MUCMessage;
import com.tr.model.obj.MeetingMessage;
import com.tr.navigate.ENavConsts;
import com.tr.ui.base.ActivityHolder;
import com.tr.ui.communities.im.CommunityChatActivity;
import com.tr.ui.conference.im.MChatBaseActivity;
import com.tr.ui.home.MainActivity;
import com.tr.ui.im.ChatBaseActivity;
import com.tr.ui.im.IMEditMemberActivity;
import com.tr.ui.im.IMRelationSelectActivity;
import com.tr.ui.im.MeetListActivity;
import com.tr.ui.relationship.MyFriendAllActivity;
import com.tr.ui.relationship.NewConnectionActivity;
import com.tr.ui.user.SplashActivity;
import com.utils.common.EUtil;
import com.utils.common.GlobalVariable;
import com.utils.log.KeelLog;

/**
 * 环信收推送消息
 *
 */
public class HuanXinPushMessageReceiver extends BroadcastReceiver {

	private EMEventListener eventListener;

	private Context context;

	/** TAG to Log */
	public static final String TAG = HuanXinPushMessageReceiver.class.getSimpleName();

	// 聊天记录数据库管理对象
	private ChatRecordDBManager dbManager = new ChatRecordDBManager(App.getApplicationConxt());
	// 会议聊天记录数据库管理对象
	private MeetingRecordDBManager dbMetManager = new MeetingRecordDBManager(App.getApplicationConxt());
	// 社交列表数据库
	private SocialityDBManager dbSocialityManager = SocialityDBManager.getInstance(App.getApplicationConxt());

	@Override
	public void onReceive(Context context, Intent intent) {
		// 消息id
		String msgId = intent.getStringExtra("msgid");
		// 发消息的人的username(userid)
		String msgFrom = intent.getStringExtra("from");
		// 消息类型，文本，图片，语音消息等,这里返回的值为msg.type.ordinal()。
		// 所以消息type实际为是enum类型
		int msgType = intent.getIntExtra("type", 0);
		Log.d("main", "new message id:" + msgId + " from:" + msgFrom + " type:" + msgType);
		// 更方便的方法是通过msgId直接获取整个message
		EMMessage message = EMChatManager.getInstance().getMessage(msgId);
	}

	/**
	 * 全局事件监听 因为可能会有UI页面先处理到这个消息，所以一般如果UI页面已经处理，这里就不需要再次处理 activityList.size()
	 * <= 0 意味着所有页面都已经在后台运行，或者已经离开Activity Stack
	 */
	public void initEventListener() {
		eventListener = new EMEventListener() {

			private MJTPushMessage pm;

			@Override
			public void onEvent(EMNotifierEvent event) {
				EMMessage message = (EMMessage) event.getData();

				try {
					Calendar c1 = Calendar.getInstance(Locale.CHINA);
					Calendar c2 = Calendar.getInstance();

					EMChatOptions chatOptions = EMChatManager.getInstance().getChatOptions();

					c2.set(Calendar.HOUR_OF_DAY, c1.get(Calendar.HOUR_OF_DAY));
					c2.set(Calendar.MINUTE, c1.get(Calendar.MINUTE));

					c1.setTimeInMillis(App.getApp().getAppData().getmNotDisturbStartTimes());
					c1.getTime();

					if (App.getApp().getAppData().isDisturbable()
							&& c2.getTimeInMillis() > App.getApp().getAppData().getmNotDisturbStartTimes()
							&& c2.getTimeInMillis() < App.getApp().getAppData().getmNotDisturbEndTimes()) {
						chatOptions.setNotifyBySoundAndVibrate(false); // 默认为true
																		// 开启新消息提醒
						chatOptions.setShowNotificationInBackgroud(false); // 默认为true
					} else {
						chatOptions.setNotifyBySoundAndVibrate(true); // 默认为true
																		// 开启新消息提醒
						chatOptions.setShowNotificationInBackgroud(true); // 默认为true
					}
					JSONObject jsonArrayAttribute = message.getJSONObjectAttribute("jtPushMessage");

					pm = MJTPushMessage.createFactory(jsonArrayAttribute.toString());

					pushMessage(App.getApplicationConxt(), pm);
				} catch (EaseMobException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};

		EMChatManager.getInstance().registerEventListener(eventListener);
	}

	private void pushMessage(Context context, MJTPushMessage pm) {
		if (pm != null) {
			if (pm.getType() == MJTPushMessage.TYPE_CHAT) { // 私聊消息
				ChatMessage chatMessage = pm.getChatMessage();
				if (chatMessage == null) {
					return;
				}
				// 设置消息类型
				chatMessage.setSendType(IMBaseMessage.SEND_TYPE_PUSH);
				// 如果是语音消息，则自动下载语音文件
				if (chatMessage.getType() == IMBaseMessage.TYPE_AUDIO) {
					File dir = EUtil.getChatVoiceCacheDir(context, pm.getSendUserID());
					if (dir != null) {
						Request request = new Request(Uri.parse(chatMessage.getJtFile().mUrl));
						request.setNotificationVisibility(Request.VISIBILITY_HIDDEN); // 不显示下载进度
						request.setDestinationUri(Uri.fromFile(new File(dir, chatMessage.getJtFile().mFileName))); // 设置文件下载位置
						VoiceFileDBManager dbManager = new VoiceFileDBManager(context);
						dbManager
								.insert(chatMessage.getJtFile().mUrl, ((DownloadManager) context
										.getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request));
					} else {
						Log.d(TAG, "没有SD卡，无法下载语音文件");
					}
				}
				// 将消息存入数据库（如果此前的消息都已收到，则将消息发送状态置为SENT）
				// 数据库中消息不存在（有可能存在，刷新列表后）
				if (!dbManager.queryExistence(App.getUserID(), pm.getSendUserID(), chatMessage.getMessageID())) {
					chatMessage.setSendType(IMBaseMessage.SEND_TYPE_PUSH);
					if (chatMessage.getSequence() > 1) { // 如果不是畅聊中的第一条消息
						IMBaseMessage earlyMsg = dbManager.queryMessageBySequence(App.getUserID(), pm.getSendUserID(),
								chatMessage.getSequence() - 1, true);
						if (earlyMsg != null) {
							chatMessage.setSendType(IMBaseMessage.SEND_TYPE_SENT);
						}
					} else if (chatMessage.getSequence() == 1) {
						chatMessage.setSendType(IMBaseMessage.SEND_TYPE_SENT);
					}
					dbManager.insert(App.getUserID(), pm.getSendUserID(), chatMessage);
				}
				// 更新社交列表数据库
				dbSocialityManager.pushDBData(App.getUserID(), pm.toMSociality());
			} else if (pm.getType() == MJTPushMessage.TYPE_MUC) { // 群聊消息
				MUCMessage mucMessage = pm.getMucMessage();
				if (mucMessage == null) {
					return;
				}
				// 设置消息类型
				mucMessage.setSendType(IMBaseMessage.SEND_TYPE_PUSH);
				// 判断是否语音消息
				if (mucMessage.getType() == IMBaseMessage.TYPE_AUDIO) {
					File dir = EUtil.getChatVoiceCacheDir(context, pm.getMucID());
					if (dir != null) {
						Request request = new Request(Uri.parse(mucMessage.getJtFile().mUrl));
						request.setNotificationVisibility(Request.VISIBILITY_HIDDEN); // 不显示下载进度
						request.setDestinationUri(Uri.fromFile(new File(dir, mucMessage.getJtFile().mFileName))); // 设置文件下载位置
						VoiceFileDBManager dbManager = new VoiceFileDBManager(context);
						dbManager
								.insert(mucMessage.getJtFile().mUrl, ((DownloadManager) context
										.getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request));
					} else {
						Log.d(TAG, "没有SD卡，无法下载语音文件");
					}
				}
				// 将消息存入数据库
				// 数据库中消息不存在（有可能存在，刷新列表后）
				if (!dbManager.queryExistence(App.getUserID(), pm.getMucID(), mucMessage.getMessageID())) {
					mucMessage.setSendType(IMBaseMessage.SEND_TYPE_PUSH);
					if (mucMessage.getSequence() > 1) { // 如果不是畅聊中的第一条消息
						IMBaseMessage earlyMsg = dbManager.queryMessageBySequence(App.getUserID(), pm.getMucID(),
								mucMessage.getSequence() - 1, true);
						if (earlyMsg != null) {
							mucMessage.setSendType(IMBaseMessage.SEND_TYPE_SENT);
						}
					} else if (mucMessage.getSequence() == 1) {
						mucMessage.setSendType(IMBaseMessage.SEND_TYPE_SENT);
					}
					// 不是事物
					dbManager.insert(App.getUserID(), pm.getMucID(), mucMessage);
				}
				// 更新社交列表数据库
				if (pm.getType() != 7) {
					dbSocialityManager.pushDBData(App.getUserID(), pm.toMSociality());
				}
			} else if (pm.getType() == MJTPushMessage.TYPE_CONF) { // 会议消息
				MeetingMessage meetingMessage = pm.getMeetingMessage();
				if (meetingMessage == null) {
					return;
				}
				// 设置消息类型
				meetingMessage.setSendType(IMBaseMessage.SEND_TYPE_PUSH);
				// 判断是否语音消息
				if (meetingMessage.getType() == IMBaseMessage.TYPE_AUDIO) {
					if (TextUtils.isEmpty(pm.getMucID()) || TextUtils.isEmpty(pm.getTopicID())) {
						return;
					}
					File dir = EUtil.getMeetingChatFileDir(context, JTFile.TYPE_AUDIO, Long.parseLong(pm.getMucID()),
							Long.parseLong(pm.getTopicID()));
					if (dir != null) {
						Request request = new Request(Uri.parse(meetingMessage.getJtFile().mUrl));
						request.setNotificationVisibility(Request.VISIBILITY_HIDDEN); // 不显示下载进度
						request.setDestinationUri(Uri.fromFile(new File(dir, meetingMessage.getJtFile().mFileName))); // 设置文件下载位置
						VoiceFileDBManager dbManager = new VoiceFileDBManager(context);
						dbManager
								.insert(meetingMessage.getJtFile().mUrl, ((DownloadManager) context
										.getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request));
					} else {
						Log.d(TAG, "没有SD卡，无法下载语音文件");
					}
				}
				// 将消息保存到数据库
				dbMetManager.insert(App.getUserID(), meetingMessage);
				// 更新社交列表数据库
				dbSocialityManager.pushDBData(App.getUserID(), pm.toMSociality());
			} else if (pm.getType() == MJTPushMessage.TYPE_CONF_NOTI) { // 会议通知
				// 更新社交列表数据库
				dbSocialityManager.pushDBData(App.getUserID(), pm.toMSociality());
			} else if (pm.getType() == MJTPushMessage.TYPE_APPLY_FRIEND) { // 申请添加好友通知
				// 不做处理
			} else if (pm.getType() == MJTPushMessage.TYPE_AGREE_FRIEND) { // 同意添加好友通知
				// 将好友信息写到数据库
				ConnectionsDBManager.getInstance(App.getApplicationConxt()).insert(pm.getConnections());
			} else if (pm.getType() == MJTPushMessage.TYPE_AFFAIR) { // 事物
				MUCMessage mucMessage = pm.getMucMessage();
				if (mucMessage == null) {
					return;
				}
				// 设置消息类型
				mucMessage.setSendType(IMBaseMessage.SEND_TYPE_PUSH);
				// 判断是否语音消息
				if (mucMessage.getType() == IMBaseMessage.TYPE_AUDIO) {
					File dir = EUtil.getChatVoiceCacheDir(context, pm.getMucID());
					if (dir != null) {
						Request request = new Request(Uri.parse(mucMessage.getJtFile().mUrl));
						request.setNotificationVisibility(Request.VISIBILITY_HIDDEN); // 不显示下载进度
						request.setDestinationUri(Uri.fromFile(new File(dir, mucMessage.getJtFile().mFileName))); // 设置文件下载位置
						VoiceFileDBManager dbManager = new VoiceFileDBManager(context);
						dbManager
								.insert(mucMessage.getJtFile().mUrl, ((DownloadManager) context
										.getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request));
					} else {
						Log.d(TAG, "没有SD卡，无法下载语音文件");
					}
				}
				// 将消息存入数据库
				// 数据库中消息不存在（有可能存在，刷新列表后）
				if (!dbManager.queryExistence(App.getUserID(), pm.getMucID(), mucMessage.getMessageID())) {
					mucMessage.setSendType(IMBaseMessage.SEND_TYPE_PUSH);
					if (mucMessage.getSequence() > 1) { // 如果不是畅聊中的第一条消息
						IMBaseMessage earlyMsg = dbManager.queryMessageBySequence(App.getUserID(), pm.getMucID(),
								mucMessage.getSequence() - 1, true);
						if (earlyMsg != null) {
							mucMessage.setSendType(IMBaseMessage.SEND_TYPE_SENT);
						}
					} else if (mucMessage.getSequence() == 1) {
						mucMessage.setSendType(IMBaseMessage.SEND_TYPE_SENT);
					}
					dbManager.insert(App.getUserID(), pm.getMucID(), mucMessage);
				}
			}else if(pm.getType() == MJTPushMessage.TYPE_COMMUNITY) { // 社群畅聊
				
			}

			// 发送广播
			sendBroadCast();
			// 检查当前聊天页面是否正在打开中
			try {
				Activity nowActivity = ActivityHolder.getInstance().getTop();
				if (nowActivity != null) {
					String nowName = nowActivity.getClass().getSimpleName();
					String chatName = "ChatActivity"; // 私聊界面
					String mucName = "GroupChatActivity"; // 群聊界面
					String mainName = MainActivity.class.getSimpleName(); // 主界面
					String meetingName = "MeetingChatActivity"; // 会议畅聊页面
					String meetingHome = "MeetingHomeActivity"; // 会议列表界面
					String meetListActivity = "MeetListActivity"; // 新会议列表界面
					String communityChatActivity = "CommunityChatActivity"; // 社群畅聊
					String imEditName = IMEditMemberActivity.class.getSimpleName(); // 畅聊详情界面
					if (nowName.equals(chatName)) {
						// 当前正在进行私聊
						ChatBaseActivity imActivity = (ChatBaseActivity) nowActivity;
						if (TextUtils.isEmpty(pm.getMucID())) {
							// 推送来的消息是私聊消息
							ChatDetail detail = imActivity.getChatDetail();
							if (detail.getThatID().equals(pm.getSendUserID())) {
								// 当前推送的私聊和正在进行的私聊是一个私聊，发送一个广播通知，然后返回
								KeelLog.d(TAG, "当前聊天正在进行");
								imActivity.notifyGetMessage();
								return;
							}
						}
					} else if (nowName.equals(mucName)) {
						// 当前正在进行群聊
						ChatBaseActivity imActivity = (ChatBaseActivity) nowActivity;
						if (!TextUtils.isEmpty(pm.getMucID())) {
							// 推送来的消息是私聊消息
							String thatMucID = imActivity.getThatMucID();
							if (thatMucID.equals(pm.getMucID())) {
								// 当前推送的群聊和正在进行的群聊是一个群聊，发送一个广播通知，然后返回
								KeelLog.d(TAG, "当前聊天正在进行");
								imActivity.notifyGetMessage();
								return;
							}
						}
					} else if(nowName.equals(communityChatActivity)){
						CommunityChatActivity imActivity = (CommunityChatActivity) nowActivity;
						if (!TextUtils.isEmpty(pm.getMucID())) {
							// 推送来的消息是私聊消息
							String thatMucID = imActivity.getThatMucID();
							if (thatMucID.equals(pm.getMucID())) {
								// 当前推送的群聊和正在进行的群聊是一个群聊，发送一个广播通知，然后返回
								KeelLog.d(TAG, "当前聊天正在进行");
								imActivity.notifyGetMessage();
								return;
							}
						}
					} else if (nowName.equals(meetingName)) {
						// 当前正在进行会议
						MChatBaseActivity imActivity = (MChatBaseActivity) nowActivity;
						if (!TextUtils.isEmpty(pm.getTopicID())) {
							// 推送来的消息是会议消息
							String topicID = imActivity.getTopicID();
							if (topicID.equals(pm.getTopicID())) {
								// 当前推送的会议和正在进行的会议是同一个，发送一个广播通知，然后返回
								KeelLog.d(TAG, "当前会议正在进行");
								// imActivity.notifyGetMessage(pm.getMeetingMessage());
								imActivity.notifyGetMessage();
								return;
							}
						}
					} else if (nowName.equals(meetingHome)) {
//						// 更新会议列表通知数
//						MeetingHomeActivity imActivity = (MeetingHomeActivity) nowActivity;
//						imActivity.funcNotice(MNotifyMessageBox.getMeetingMessageMap(),
//								MNotifyMessageBox.getMeetingNotice());
//						KeelLog.d(TAG, "当前正在会议列表");
						return;
					} else if(nowName.equals(meetListActivity)){
						MeetListActivity imActivity = (MeetListActivity)nowActivity;
						KeelLog.d(TAG, "当前正在新的会议列表");
						imActivity.startGetData();
					} else if (nowName.equals(mainName)) {
						// 主界面（重新排序，刷新消息显示）
						MainActivity mainActivity = (MainActivity) nowActivity;
						if (pm.getType() != 7) {
							mainActivity.pushMessage(App.getUserID(), pm.toMSociality());
						}
						// KeelLog.d(TAG, "当前界面为主界面");
						return;
					} else if (nowName.equalsIgnoreCase(NewConnectionActivity.class.getSimpleName())) {
						if (pm.getType() == MJTPushMessage.TYPE_APPLY_FRIEND) {
							// 请求添加好友
							NewConnectionActivity activity = (NewConnectionActivity) nowActivity;
							// 重新请求
							activity.updateUI();
							return;
						}
					} else if (nowName.equals(IMRelationSelectActivity.class.getSimpleName())) {
						if (pm.getType() == MJTPushMessage.TYPE_AGREE_FRIEND) {
							// 同意了好友的申请
							IMRelationSelectActivity activity = (IMRelationSelectActivity) nowActivity;
							// 更新列表
							activity.updateUI(pm.getConnections());
							return;
						}
					} else if (nowName.equals(MyFriendAllActivity.class.getSimpleName())) { // 同意
						if (pm.getType() == MJTPushMessage.TYPE_AGREE_FRIEND) {
							// 同意了好友的申请
							MyFriendAllActivity activity = (MyFriendAllActivity) nowActivity;
							// 更新列表
							activity.updateUI(pm.getConnections());
							return;
						}
					}
					// 畅聊详情界面
					else if (nowName.equals(imEditName)) {
						return;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 将消息添加到全局消息管理器
			MNotifyMessageBox.getInstance().getListMessage().add(pm);

			// 将数据写入数据库
			/*
			 * if(pm.getType() == MJTPushMessage.TYPE_CHAT){ // 私聊
			 * dbManager.insert(App.getUserID(), pm.getMucID(),
			 * pm.getChatMessage()); } else if(pm.getType() ==
			 * MJTPushMessage.TYPE_MUC){ // 群聊 dbManager.insert(App.getUserID(),
			 * pm.getMucID(), pm.getMucMessage()); } else if(pm.getType() ==
			 * MJTPushMessage.TYPE_CONF){ // 会议 MeetingMessage msg =
			 * MeetingMessage.createFactory(pm.getMeetingMessage()); if(msg !=
			 * null){ dbMetManager.insert(App.getUserID(), msg); } } else
			 * if(pm.getType() == MJTPushMessage.TYPE_CONF_NOTI){ // 会议通知 //
			 * 直接忽略 }
			 */
			List<MSociality> listMSocialityFromDB = dbSocialityManager.getRandomListMSocialityFromDB(App.getUserID());
			int count = 0;
			for (MSociality mSociality : listMSocialityFromDB) {
				count += mSociality.getNewCount();
			}
			// BadgeUtil.setBadgeCount(App.getApp().getApplicationContext(),
			// count);
			if (count > 0) {
				ShortcutBadger.with(App.getApp().getApplicationContext()).count(count > 99 ? 99 : count);
			} else {
				ShortcutBadger.with(App.getApp().getApplicationContext()).remove();
			}

			// 判断推送过来的消息类型，在通知栏上显示相应类型的消息
			switch (pm.getType()) {
			case MJTPushMessage.TYPE_CHAT: // 私聊
			case MJTPushMessage.TYPE_MUC: // 群聊
			case MJTPushMessage.TYPE_COMMUNITY: //社群聊天
				if (MNotifyMessageBox.getInstance().getChatCount() == 1) { // 一个畅聊发来的消息
					int msgCount = MNotifyMessageBox.getInstance().getChatMessageCount(); // 消息数目
					int senderCount = MNotifyMessageBox.getInstance().getSenderCount(); // 发送人数目
					if (msgCount <= 1) { // 单条消息
						if (pm.getType() == MJTPushMessage.TYPE_CHAT) { // 私聊
							pushInfo(pm.getTitle(), pm.getContent(),pm.getType());
						} else if (pm.getType() == MJTPushMessage.TYPE_MUC || pm.getType() == MJTPushMessage.TYPE_COMMUNITY) { // 群聊 社群聊天
							String content = "";
							if (pm.getMucMessage() != null && pm.getMucMessage().getType() == IMBaseMessage.TYPE_TEXT) {
								if(!TextUtils.isEmpty(pm.getSendName())){
									content = pm.getSendName() + ":" + pm.getContent().replace(pm.getSendName(), "");
								}else{
									content = pm.getContent();
								}
							} else if(pm.getMucMessage() != null) {
								if(!TextUtils.isEmpty(pm.getSendName())){
									content = pm.getSendName() + ":" + pm.getMucMessage().getContent().replace(pm.getSendName(), "");
								}else{
									content = pm.getMucMessage().getContent();
								}
							}else if(pm.getType() == MJTPushMessage.TYPE_COMMUNITY){
								if(!TextUtils.isEmpty(pm.getSendName()) && !pm.getSendName().equals("admin")){
									content = pm.getSendName() + ":" + pm.getContent();
								}else{
									content = pm.getContent();
								}
							}
							pushInfo(pm.getTitle(), content, pm.getType());
						}
					} else { // 多条消息
						if (senderCount <= 1) { // 一个消息发送人
							if (pm.getType() == MJTPushMessage.TYPE_CHAT) { // 私聊
								pm.setTitle("新消息通知");
								String content = pm.getSendName() + "有" + msgCount + "条消息给你";
								pushInfo(pm.getTitle(), content, MJTPushMessage.TYPE_CHAT);
							} else if (pm.getType() == MJTPushMessage.TYPE_MUC || pm.getType() == MJTPushMessage.TYPE_COMMUNITY) { // 群聊
								String content = "有" + msgCount + "条新消息";
								pushInfo(pm.getTitle(), content, pm.getType());
							}
						} else if (senderCount > 1) { // 多个消息发送人
							String content = "有" + msgCount + "条新消息";
							pushInfo(pm.getTitle(), content, pm.getType());
						}
					}
				} else if (MNotifyMessageBox.getInstance().getChatCount() > 1) { // 多个畅聊发来的消息
					pm.setTitle("新消息通知");
					String content = MNotifyMessageBox.getInstance().getChatCount() + "个畅聊发来了"
							+ MNotifyMessageBox.getInstance().getChatMessageCount() + "条新消息";
					pushInfo(pm.getTitle(), content, pm.getType());
				}
				break;
			case MJTPushMessage.TYPE_CONF: // 会议
				if (MNotifyMessageBox.getInstance().getConferenceCount() == 1) { // 单个会议发送的消息
					pm.setTitle("会议消息通知");
					String content = "";
					if (MNotifyMessageBox.getInstance().getConferenceMessageCount() <= 1) {
						content = pm.getContent();
					} else {
						content = "有" + MNotifyMessageBox.getInstance().getConferenceMessageCount() + "条消息给你";
					}
					pushInfo(pm.getTitle(), content, MJTPushMessage.TYPE_CONF);
				} else if (MNotifyMessageBox.getInstance().getConferenceCount() > 1) { // 多个会议发送的消息
					pm.setTitle("会议消息通知");
					String content = MNotifyMessageBox.getInstance().getConferenceCount() + "个会议发来了"
							+ MNotifyMessageBox.getInstance().getConferenceMessageCount() + "条新消息";
					pushInfo(pm.getTitle(), content, MJTPushMessage.TYPE_CONF);
				}
				break;
			case MJTPushMessage.TYPE_CONF_NOTI: // 会议通知
				pm.setTitle("会议通知");
				String content = "有" + MNotifyMessageBox.getInstance().getConferenceNotificationCount() + "条新通知";
				pushInfo(pm.getTitle(), content, MJTPushMessage.TYPE_CONF_NOTI);
				break;
			case MJTPushMessage.TYPE_APPLY_FRIEND: // 申请添加好友
				pm.setTitle("通知");
				pushInfo(pm.getTitle(), pm.getContent(), MJTPushMessage.TYPE_APPLY_FRIEND);
				break;
			case MJTPushMessage.TYPE_AGREE_FRIEND: // 同意添加好友
				pm.setTitle("通知");
				pushInfo(pm.getTitle(), pm.getContent(), MJTPushMessage.TYPE_AGREE_FRIEND);
				break;
			case MJTPushMessage.TYPE_AFFAIR: // 推送事物
				pm.setTitle("通知");
				pushInfo(pm.getTitle(), pm.getContent(), MJTPushMessage.TYPE_AFFAIR);
				break;
			default:
				break;
			}
		}
	}

	private void sendBroadCast() {
		Intent intent = new Intent();
		intent.setAction(IMConsts.IM_BROADCAST);
		App.getApp().sendBroadcast(intent);
	}

	@SuppressWarnings("deprecation")
	public static void pushInfo(String title, String info) {

		// 获得通知管理器
		if (title == null) {
			title = "消息通知";
		}
		if (info == null) {
			return;
		}

		NotificationManager manager = (NotificationManager) App.getApp().getSystemService(Context.NOTIFICATION_SERVICE);
		// 构建一个通知对象(需要传递的参数有三个,分别是图标,标题和 时间)
		Notification notification = new Notification(R.drawable.ic_launcher, "新消息通知", System.currentTimeMillis());
		Intent intent = new Intent(App.getApplicationConxt(), SplashActivity.class);
		intent.putExtra("message", "info");
		// 将消息存放在intent中传递给loginActivity
		intent.putExtra(ENavConsts.ENotifyParam, MNotifyMessageBox.getInstance());
		// 消息类型
		intent.putExtra(ENavConsts.EPushMessageType, "");
		// 指定一个跳转的intent
		PendingIntent pendingIntent = PendingIntent.getActivity(App.getApplicationConxt(), 0, intent,
				PendingIntent.FLAG_ONE_SHOT);
		// 这就是对通知的具体设置了
		notification.setLatestEventInfo(App.getApplicationConxt(), title, info, pendingIntent);
		// 点击后自动消失
		notification.flags = Notification.FLAG_AUTO_CANCEL;

		// 免打扰时间判断
		boolean notDisturble = false;
		Calendar calendar = Calendar.getInstance();
		long currentTime = calendar.getTimeInMillis();
		if (App.getApp().getAppData().getmNotDisturbStartTimes() < currentTime
				&& App.getApp().getAppData().getmNotDisturbEndTimes() > currentTime) {
			notDisturble = true;
		}
		// Log.e("aaaaaa",
		// "currentTime="+currentTime+";;"+"/n"+"start="+getAppData().getmNotDisturbStartTimes()+";;"+"/n"+"end"+getAppData().getmNotDisturbEndTimes());
		// modified by zhongshan 开启新消息提示音
		if (App.getApp().getAppData().isNewMessageAlertVoice()) {
			if (App.getApp().getAppData().isDisturbable()) {// 并开启免打扰
				if (!notDisturble) {// 并不在免打扰时间内
					notification.defaults = Notification.DEFAULT_SOUND;
				}
			} else {
				notification.defaults = Notification.DEFAULT_SOUND;
			}
		}

		// 开启新消息提醒
		if (App.getApp().getAppData().isNewMessageAlert()) {
			// 发动通知
			manager.notify(0, notification);
		}
	}

	/**
	 * 发送会议消息（通知和畅聊）
	 * 
	 * @param title
	 * @param info
	 * @param type
	 *            1-私聊;2-群聊;3-会议消息;4-会议通知
	 */
	@SuppressWarnings("deprecation")
	public static void pushInfo(String title, String info, int pushMessageType) {

		// 获得通知管理器
		if (title == null) {
			title = "消息通知";
		}
		if (info == null) {
			return;
		}

		NotificationManager manager = (NotificationManager) App.getApp().getSystemService(Context.NOTIFICATION_SERVICE);
		// 构建一个通知对象(需要传递的参数有三个,分别是图标,标题和 时间)
		Notification notification = new Notification(R.drawable.ic_launcher, "新消息通知", System.currentTimeMillis());
		Intent intent = new Intent(App.getApplicationConxt(), SplashActivity.class);
		intent.putExtra("message", "info");
		// 将消息存放在intent中传递给loginActivity
		intent.putExtra(ENavConsts.ENotifyParam, MNotifyMessageBox.getInstance());
		// 推送消息类型
		intent.putExtra(ENavConsts.EPushMessageType, pushMessageType);
		switch(pushMessageType){
		case MJTPushMessage.TYPE_MUC:
		case MJTPushMessage.TYPE_CHAT:
		case MJTPushMessage.TYPE_COMMUNITY:
			SharedPreferences social_sp = App.getApplicationConxt().getSharedPreferences(
					GlobalVariable.SHARED_PREFERENCES_SOCIAL_ISFISTLOAD,
					App.getApplicationConxt().MODE_PRIVATE);
			SharedPreferences.Editor social_edtior = social_sp.edit();
			social_edtior.putBoolean(GlobalVariable.SOCIAL_ISFISTLOAD, true);
			social_edtior.commit();
			break;
		}
		// 指定一个跳转的intent
		// PendingIntent pendingIntent =
		// PendingIntent.getActivity(getApplicationContext(), 0, intent,
		// PendingIntent.FLAG_ONE_SHOT);
		// 指定一个跳转的intent
		// 通知新方法 加不同请求码
		PendingIntent pendingIntent = PendingIntent.getActivity(App.getApplicationConxt(), pushMessageType, intent,
				PendingIntent.FLAG_ONE_SHOT);

		// 这就是对通知的具体设置了
		notification.setLatestEventInfo(App.getApplicationConxt(), title, info, pendingIntent);
		// 点击后自动消失
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		// 声音默认
		// notification.defaults = Notification.DEFAULT_SOUND;
		// 免打扰时间判断
		boolean notDisturble = false;
		Calendar calendar = Calendar.getInstance();
		int hours = calendar.getTime().getHours();
		int minutes = calendar.getTime().getMinutes();
		calendar.set(Calendar.HOUR_OF_DAY, hours);
		calendar.set(Calendar.MINUTE, minutes);
		long currentTime = calendar.getTimeInMillis();
		if (App.getApp().getAppData().getmNotDisturbStartTimes() < currentTime
				&& App.getApp().getAppData().getmNotDisturbEndTimes() > currentTime) {
			notDisturble = true;
		}
		// Log.e("aaaaaa",
		// "currentTime="+currentTime+";;"+"/n"+"start="+getAppData().getmNotDisturbStartTimes()+";;"+"/n"+"end"+getAppData().getmNotDisturbEndTimes());
		// modified by zhongshan 开启新消息提示音
		if (App.getApp().getAppData().isNewMessageAlertVoice()) {
			if (App.getApp().getAppData().isDisturbable()) {// 并开启免打扰
				if (!notDisturble) {// 并不在免打扰时间内
					notification.defaults = Notification.DEFAULT_SOUND;
				}
			} else {
				notification.defaults = Notification.DEFAULT_SOUND;
			}
		}

		// 开启新消息提醒
		if (App.getApp().getAppData().isNewMessageAlert()) {
			// 发动通知
			manager.notify(pushMessageType, notification);
		}
	}

}
