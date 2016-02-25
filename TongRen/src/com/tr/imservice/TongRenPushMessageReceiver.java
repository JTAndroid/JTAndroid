package com.tr.imservice;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.frontia.api.FrontiaPushMessageReceiver;
import com.tr.App;
import com.tr.api.IMReqUtil;
import com.tr.db.ChatRecordDBManager;
import com.tr.db.ConnectionsCacheData;
import com.tr.db.ConnectionsDBManager;
import com.tr.db.MeetingRecordDBManager;
import com.tr.db.SocialityDBManager;
import com.tr.db.VoiceFileDBManager;
import com.tr.model.im.ChatDetail;
import com.tr.model.im.IMConsts;
import com.tr.model.im.MJTPushMessage;
import com.tr.model.im.MNotifyMessageBox;
import com.tr.model.im.MSetChannelIDResp;
import com.tr.model.obj.ChatMessage;
import com.tr.model.obj.IMBaseMessage;
import com.tr.model.obj.JTFile;
import com.tr.model.obj.MUCMessage;
import com.tr.model.obj.MeetingMessage;
import com.tr.ui.base.ActivityHolder;
import com.tr.ui.conference.im.MChatBaseActivity;
import com.tr.ui.home.MainActivity;
import com.tr.ui.im.ChatBaseActivity;
import com.tr.ui.im.IMRelationSelectActivity;
import com.tr.ui.relationship.MyFriendAllActivity;
import com.tr.ui.relationship.NewConnectionActivity;
import com.utils.common.EUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.log.KeelLog;

/**
 * Push消息处理receiver。请编写您需要的回调函数， 一般来说： onBind是必须的，用来处理startWork返回值；
 * onMessage用来接收透传消息； onSetTags、onDelTags、onListTags是tag相关操作的回调；
 * onNotificationClicked在通知被点击时回调； onUnbind是stopWork接口的返回值回调
 * 
 * 返回值中的errorCode，解释如下： 0 - Success 10001 - Network Problem 30600 - Internal
 * Server Error 30601 - Method Not Allowed 30602 - Request Params Not Valid
 * 30603 - Authentication Failed 30604 - Quota Use Up Payment Required 30605 -
 * Data Required Not Found 30606 - Request Time Expires Timeout 30607 - Channel
 * Token Timeout 30608 - Bind Relation Not Found 30609 - Bind Number Too Many
 * 
 * 当您遇到以上返回错误时，如果解释不了您的问题，请用同一请求的返回值requestId和errorCode联系我们追查问题。
 * 
 */
public class TongRenPushMessageReceiver extends FrontiaPushMessageReceiver implements IBindData {
	/** TAG to Log */
	public static final String TAG = TongRenPushMessageReceiver.class.getSimpleName();

	// public static List<MJTPushMessage> listMessage = new
	// ArrayList<MJTPushMessage>();

	/**
	 * 调用PushManager.startWork后，sdk将对push
	 * server发起绑定请求，这个过程是异步的。绑定请求的结果通过onBind返回。 如果您需要用单播推送，需要把这里获取的channel
	 * id和user id上传到应用server中，再调用server接口用channel id和user id给单个手机或者用户推送。
	 * 
	 * @param context
	 *            BroadcastReceiver的执行Context
	 * @param errorCode
	 *            绑定接口返回值，0 - 成功
	 * @param appid
	 *            应用id。errorCode非0时为null
	 * @param userId
	 *            应用user id。errorCode非0时为null
	 * @param channelId
	 *            应用channel id。errorCode非0时为null
	 * @param requestId
	 *            向服务端发起的请求id。在追查问题时有用；
	 * @return none
	 */

	// 聊天记录数据库管理对象
	private ChatRecordDBManager dbManager = new ChatRecordDBManager(App.getApplicationConxt());
	// 会议聊天记录数据库管理对象
	private MeetingRecordDBManager dbMetManager = new MeetingRecordDBManager(App.getApplicationConxt());
	// 社交列表数据库
	private SocialityDBManager dbSocialityManager = SocialityDBManager.getInstance(App.getApplicationConxt());

	@Override
	public void onBind(Context context, int errorCode, String appid, String userId, String channelId, String requestId) {
		String MSG = "onBind()";

		String responseString = "onBind errorCode=" + errorCode + " appid=" + appid + " userId=" + userId
				+ " channelId=" + channelId + " requestId=" + requestId;
		Log.d(TAG, responseString);

		// 绑定成功，设置已绑定flag，可以有效的减少不必要的绑定请求
		if (errorCode == 0) {
			// Utils.setBind(context, true);
			// EUtil.showLongToast(responseString);
			IMReqUtil.setChannelID(App.getApplicationConxt(), this, null, App.getUserID(), channelId, userId, appid);
		} else {
			//
			// EUtil.showLongToast("bind fail!");
			Log.i(TAG, MSG + "bind fail!");
		}
		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
		updateContent(context, responseString);
	}

	/**
	 * 接收透传消息的函数。
	 * 
	 * @param context
	 *            上下文
	 * @param message
	 *            推送的消息
	 * @param customContentString
	 *            自定义内容,为空或者json字符串
	 */
	@Override
	public void onMessage(Context context, String message, String customContentString) {

		// // 新消息不提醒,直接返回 deleted by zhongshan
		// if(!App.getApp().getAppData().isNewMessageAlert()){
		// return;
		// }

		// 免打扰模式在22:00-08:00不发送推送信息
		if (App.getApp().getAppData().isDisturbable()) {
			Date now = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(now);
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			/*
			 * if ((hour < 8) || (hour >= 22)) { return; }
			 */
		}

		String messageString = "透传消息 message=\"" + message + "\" customContentString=" + customContentString;
		Log.d(TAG, messageString);

		// 自定义内容获取方式，mykey和myvalue对应透传消息推送时自定义内容中设置的键和值
		if (customContentString != null & TextUtils.isEmpty(customContentString)) {
			JSONObject customJson = null;
			try {
				customJson = new JSONObject(customContentString);
				String myvalue = null;
				if (customJson.isNull("mykey")) {
					myvalue = customJson.getString("mykey");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		// 推送的消息对象
		MJTPushMessage pm = MJTPushMessage.createFactory(message);
		Log.d("Push Message", message);

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
			} else if (pm.getType() == MJTPushMessage.TYPE_MUC || pm.getType() == MJTPushMessage.TYPE_AFFAIR) { // 群聊消息
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
				// 更新社交列表数据库
				dbSocialityManager.pushDBData(App.getUserID(), pm.toMSociality());
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
			} else if (pm.getType() != MJTPushMessage.TYPE_AFFAIR) { // 事物
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
					} else if (nowName.equals(mainName)) {
						// 主界面（重新排序，刷新消息显示）
						MainActivity mainActivity = (MainActivity) nowActivity;
						mainActivity.pushMessage(App.getUserID(), pm.toMSociality());
						KeelLog.d(TAG, "当前界面为主界面");
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

			// 判断推送过来的消息类型，在通知栏上显示相应类型的消息
			switch (pm.getType()) {
			case MJTPushMessage.TYPE_CHAT: // 私聊
			case MJTPushMessage.TYPE_MUC: // 群聊
				if (MNotifyMessageBox.getInstance().getChatCount() == 1) { // 一个畅聊发来的消息
					int msgCount = MNotifyMessageBox.getInstance().getChatMessageCount(); // 消息数目
					int senderCount = MNotifyMessageBox.getInstance().getSenderCount(); // 发送人数目
					if (msgCount <= 1) { // 单条消息
						if (pm.getType() == MJTPushMessage.TYPE_CHAT) { // 私聊
							HuanXinPushMessageReceiver.pushInfo(pm.getTitle(), pm.getContent(), MJTPushMessage.TYPE_CHAT);
						} else if (pm.getType() == MJTPushMessage.TYPE_MUC) { // 群聊
							String content = "";
							if (pm.getMucMessage() != null && pm.getMucMessage().getType() == IMBaseMessage.TYPE_TEXT) {
								content = pm.getSendName() + ":" + pm.getContent();
							} else {
								content = pm.getContent();
							}
							HuanXinPushMessageReceiver.pushInfo(pm.getTitle(), content, MJTPushMessage.TYPE_CHAT);
						}
					} else { // 多条消息
						if (senderCount <= 1) { // 一个消息发送人
							if (pm.getType() == MJTPushMessage.TYPE_CHAT) { // 私聊
								pm.setTitle("新消息通知");
								String content = pm.getSendName() + "有" + msgCount + "条消息给你";
								HuanXinPushMessageReceiver.pushInfo(pm.getTitle(), content, MJTPushMessage.TYPE_CHAT);
							} else if (pm.getType() == MJTPushMessage.TYPE_MUC) { // 群聊
								String content = "有" + msgCount + "条新消息";
								HuanXinPushMessageReceiver.pushInfo(pm.getTitle(), content, MJTPushMessage.TYPE_CHAT);
							}
						} else if (senderCount > 1) { // 多个消息发送人
							String content = "有" + msgCount + "条新消息";
							HuanXinPushMessageReceiver.pushInfo(pm.getTitle(), content, MJTPushMessage.TYPE_CHAT);
						}
					}
				} else if (MNotifyMessageBox.getInstance().getChatCount() > 1) { // 多个畅聊发来的消息
					pm.setTitle("新消息通知");
					String content = MNotifyMessageBox.getInstance().getChatCount() + "个畅聊发来了"
							+ MNotifyMessageBox.getInstance().getChatMessageCount() + "条新消息";
					HuanXinPushMessageReceiver.pushInfo(pm.getTitle(), content, MJTPushMessage.TYPE_CHAT);
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
					HuanXinPushMessageReceiver.pushInfo(pm.getTitle(), content, MJTPushMessage.TYPE_CONF);
				} else if (MNotifyMessageBox.getInstance().getConferenceCount() > 1) { // 多个会议发送的消息
					pm.setTitle("会议消息通知");
					String content = MNotifyMessageBox.getInstance().getConferenceCount() + "个会议发来了"
							+ MNotifyMessageBox.getInstance().getConferenceMessageCount() + "条新消息";
					HuanXinPushMessageReceiver.pushInfo(pm.getTitle(), content, MJTPushMessage.TYPE_CONF);
				}
				break;
			case MJTPushMessage.TYPE_CONF_NOTI: // 会议通知
				pm.setTitle("会议通知");
				String content = "有" + MNotifyMessageBox.getInstance().getConferenceNotificationCount() + "条新通知";
				HuanXinPushMessageReceiver.pushInfo(pm.getTitle(), content, MJTPushMessage.TYPE_CONF_NOTI);
				break;
			case MJTPushMessage.TYPE_APPLY_FRIEND: // 申请添加好友
				pm.setTitle("通知");
				HuanXinPushMessageReceiver.pushInfo(pm.getTitle(), pm.getContent(), MJTPushMessage.TYPE_APPLY_FRIEND);
				break;
			case MJTPushMessage.TYPE_AGREE_FRIEND: // 同意添加好友
				pm.setTitle("通知");
				HuanXinPushMessageReceiver.pushInfo(pm.getTitle(), pm.getContent(), MJTPushMessage.TYPE_AGREE_FRIEND);
				break;
			case MJTPushMessage.TYPE_AFFAIR: // 推送事物
				pm.setTitle("通知");
				HuanXinPushMessageReceiver.pushInfo(pm.getTitle(), pm.getContent(), MJTPushMessage.TYPE_AFFAIR);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 接收通知点击的函数。注：推送通知被用户点击前，应用无法通过接口获取通知的内容。
	 * 
	 * @param context
	 *            上下文
	 * @param title
	 *            推送的通知的标题
	 * @param description
	 *            推送的通知的描述
	 * @param customContentString
	 *            自定义内容，为空或者json字符串
	 */
	@Override
	public void onNotificationClicked(Context context, String title, String description, String customContentString) {
		String notifyString = "通知点击 title=\"" + title + "\" description=\"" + description + "\" customContent="
				+ customContentString;
		Log.d(TAG, notifyString);

		// 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值
		if (customContentString != null & TextUtils.isEmpty(customContentString)) {
			JSONObject customJson = null;
			try {
				customJson = new JSONObject(customContentString);
				String myvalue = null;
				if (customJson.isNull("mykey")) {
					myvalue = customJson.getString("mykey");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
		updateContent(context, notifyString);
	}

	/**
	 * setTags() 的回调函数。
	 * 
	 * @param context
	 *            上下文
	 * @param errorCode
	 *            错误码。0表示某些tag已经设置成功；非0表示所有tag的设置均失败。
	 * @param successTags
	 *            设置成功的tag
	 * @param failTags
	 *            设置失败的tag
	 * @param requestId
	 *            分配给对云推送的请求的id
	 */
	@Override
	public void onSetTags(Context context, int errorCode, List<String> sucessTags, List<String> failTags,
			String requestId) {
		String responseString = "onSetTags errorCode=" + errorCode + " sucessTags=" + sucessTags + " failTags="
				+ failTags + " requestId=" + requestId;
		Log.d(TAG, responseString);

		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
		updateContent(context, responseString);
	}

	/**
	 * delTags() 的回调函数。
	 * 
	 * @param context
	 *            上下文
	 * @param errorCode
	 *            错误码。0表示某些tag已经删除成功；非0表示所有tag均删除失败。
	 * @param successTags
	 *            成功删除的tag
	 * @param failTags
	 *            删除失败的tag
	 * @param requestId
	 *            分配给对云推送的请求的id
	 */
	@Override
	public void onDelTags(Context context, int errorCode, List<String> sucessTags, List<String> failTags,
			String requestId) {
		String responseString = "onDelTags errorCode=" + errorCode + " sucessTags=" + sucessTags + " failTags="
				+ failTags + " requestId=" + requestId;
		Log.d(TAG, responseString);

		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
		updateContent(context, responseString);
	}

	/**
	 * listTags() 的回调函数。
	 * 
	 * @param context
	 *            上下文
	 * @param errorCode
	 *            错误码。0表示列举tag成功；非0表示失败。
	 * @param tags
	 *            当前应用设置的所有tag。
	 * @param requestId
	 *            分配给对云推送的请求的id
	 */
	@Override
	public void onListTags(Context context, int errorCode, List<String> tags, String requestId) {
		String responseString = "onListTags errorCode=" + errorCode + " tags=" + tags;
		Log.d(TAG, responseString);

		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
		updateContent(context, responseString);
	}

	/**
	 * PushManager.stopWork() 的回调函数。
	 * 
	 * @param context
	 *            上下文
	 * @param errorCode
	 *            错误码。0表示从云推送解绑定成功；非0表示失败。
	 * @param requestId
	 *            分配给对云推送的请求的id
	 */
	@Override
	public void onUnbind(Context context, int errorCode, String requestId) {
		String responseString = "onUnbind errorCode=" + errorCode + " requestId = " + requestId;
		Log.d(TAG, responseString);

		// 解绑定成功，设置未绑定flag，
		if (errorCode == 0) {
			// Utils.setBind(context, false);
		}
		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
		updateContent(context, responseString);
	}

	private void updateContent(Context context, String content) {
		Log.d(TAG, "updateContent");
		String logText = "";// + Utils.logStringCache;

		if (!logText.equals("")) {
			logText += "\n";
		}

	}

	@Override
	public void bindData(int tag, Object object) {
		if (object == null) {
			KeelLog.d("bind baidu pushservice failed");
			return;
		}
		if (tag == EAPIConsts.IMReqType.IM_REQ_SET_CHANNELID) {
			MSetChannelIDResp resp = (MSetChannelIDResp) object;
			if (resp.isSucceed()) {
				KeelLog.d("bind baidu pushservice success");
			}
		}
	}

	private void sendBroadCast() {
		Intent intent = new Intent();
		intent.setAction(IMConsts.IM_BROADCAST);
		App.getApp().sendBroadcast(intent);
	}

	private void sendMessageBroadCast(Intent intent) {
		App.getApp().sendBroadcast(intent);
	}
}
