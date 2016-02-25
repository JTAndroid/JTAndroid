package com.tr.ui.conference.im;

import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import com.tr.App;
import com.tr.api.ConferenceReqUtil;
import com.tr.model.conference.MMeetingMember;
import com.tr.model.conference.MMeetingQuery;
import com.tr.model.conference.MMeetingTopicQuery;
import com.tr.model.obj.IMBaseMessage;
import com.tr.model.obj.JTFile;
import com.tr.model.obj.MeetingMessage;
import com.tr.navigate.ENavConsts;
import com.utils.common.EUtil;
import com.utils.common.HyChatFileUploader;
import com.utils.common.HyChatFileUploader.OnFileUploadListener;
import com.utils.string.StringUtils;

/**
 * 会议畅聊页面
 * 
 * @author leon
 */
public class MeetingChatActivity extends MChatBaseActivity {

	private final String TAG = getClass().getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getParam();
	}

	// 读取传入的参数
	public void getParam() {

		Intent intent = getIntent();
		fromActivityName = intent.getStringExtra(ENavConsts.EFromActivityName);
		meetingDetail = (MMeetingQuery) intent.getSerializableExtra(ENavConsts.EMeetingDetail);
		topicDetail = (MMeetingTopicQuery) intent.getSerializableExtra(ENavConsts.EMeetingTopicDetail);
		searchMessageId = intent.getStringExtra(ENavConsts.EMessageID);
		String meetingId = intent.getStringExtra(ENavConsts.EMeetingId);
		String topicId = intent.getStringExtra(ENavConsts.ETopicId);
		if (meetingDetail != null && topicDetail != null) {
			this.meetingId = meetingDetail.getId();
			this.topicId = topicDetail.getId();
			// 初始化
			doInit();
		} else if (!TextUtils.isEmpty(meetingId) && !TextUtils.isEmpty(topicId)) {
			this.meetingId = Integer.parseInt(meetingId);
			this.topicId = Integer.parseInt(topicId);
			showLoadingDialog();
			ConferenceReqUtil.doGetMeetingDetail(this, this, this.meetingId, App.getUserID(), null);
		} else {
			showToast("参数错误");
			finish();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	protected void back() {
		finish();
	}

	@Override
	public void sendMessage(String text) {
		sendMeetingMessage(text);
	}

	// 发送普通文本消息
	public void sendMeetingMessage(String text) {

		MeetingMessage msg = new MeetingMessage(text);
		msg.setSendType(IMBaseMessage.SEND_TYPE_SENDING);
		msg.setSenderID(App.getUserID());
		msg.setJtFile(null);
		msg.setType(IMBaseMessage.TYPE_TEXT);
		msg.setRecvID("" + meetingId);
		msg.setTopicID("" + topicId);
		msg.setTime(getLatestTime());
		msg.setContent(text);
		msg.setSenderName(App.getNick());

		// 发送消息
		ConferenceReqUtil.doSendMeetingChat(this, this, msg, mNetHandler);

		// 将发送中的消息添加到聊天列表中
		listMessage.add(msg);
		setData(listMessage);

		// 缓存消息
		meetingRecordDBManager.insert(App.getUserID(), msg);
	}

	// 重发消息
	@Override
	public void resendMessage(final MeetingMessage msg) {
		// 设置发送状态
		msg.setSendType(IMBaseMessage.SEND_TYPE_SENDING);
		// 刷新列表
		setData(listMessage);
		if (msg.getType() == IMBaseMessage.TYPE_TEXT || msg.getType() == IMBaseMessage.TYPE_REQUIREMENT || msg.getType() == IMBaseMessage.TYPE_KNOWLEDGE
				|| msg.getType() == IMBaseMessage.TYPE_CONFERENCE || msg.getType() == IMBaseMessage.TYPE_JTCONTACT_OFFLINE || msg.getType() == IMBaseMessage.TYPE_JTCONTACT_ONLINE
				|| msg.getType() == IMBaseMessage.TYPE_ORG_OFFLINE || msg.getType() == IMBaseMessage.TYPE_ORG_ONLINE || msg.getType() == IMBaseMessage.TYPE_CUSTOMER) {
			// 直接重发
			ConferenceReqUtil.doSendMeetingChat(this, this, msg, mNetHandler);
		} else { // 文件消息重新
			if (!TextUtils.isEmpty(msg.getJtFile().mUrl)) { // 重新发送消息
				ConferenceReqUtil.doSendMeetingChat(this, this, msg, mNetHandler);
			} else { // 重新上传文件
				if (msg.getJtFile().mType == JTFile.TYPE_VIDEO) {

					// 缩略图已经上传
					if (TextUtils.isEmpty(msg.getJtFile().reserved2)) {
						startNewUploadTask(msg);
						return;
					}

					String fileName = getUserAndTimeBasedFormatName(".jpg");
					String scrennshotPath = getVideoScreenshotCachePath(msg.getJtFile().mLocalFilePath, fileName, meetingId, topicId);
					JTFile screenshotJtFile = EUtil.createJTFileFromLocalFile(scrennshotPath);
					if (screenshotJtFile != null) {
						HyChatFileUploader uploader = new HyChatFileUploader(this, screenshotJtFile, msg.getMessageID());
						uploader.setOnFileUploadListener(new OnFileUploadListener() {

							@Override
							public void onPrepared(String messageId) {

							}

							@Override
							public void onStarted(String messageId) {

							}

							@Override
							public void onUpdate(String messageId, int value) {

							}

							@Override
							public void onSuccess(String messageId, JTFile jtFile) {
								removeUploadTask(messageId); // 从上传任务中移除
								msg.getJtFile().reserved2 = jtFile.mUrl; // 视频缩略图地址
								startNewUploadTask(msg); // 上传视频
							}

							@Override
							public void onError(String messageId, int code, String message) {
								setMessageSendFail(listMessage, messageId); // 设置消息发送状态
								removeUploadTask(messageId); // 从上传任务中移除
							}

							@Override
							public void onCanceled(String messageId) {
								setMessageSendFail(listMessage, messageId); // 设置消息发送状态
								removeUploadTask(messageId); // 从上传任务中移除
							}
						});
						uploader.start();
					} else {
						startNewUploadTask(msg);
					}
				} else {
					startNewUploadTask(msg);
				}
			}
		}
	}

	// 发送分享过来的消息
	/*
	 * @Override public void sendShareMessage(MeetingMessage imMsg) {
	 * 
	 * MeetingMessage msg = imMsg;
	 * msg.setSendType(IMBaseMessage.SEND_TYPE_SENDING);
	 * msg.setSenderID(App.getUserID()); msg.setRecvID(meetingId + ""); // 主会场id
	 * msg.setTopicID(topicId + ""); // 分会场id msg.setTime(getLatestTime());
	 * msg.setSenderName(App.getNick()); switch (imMsg.getType()) { case
	 * IMBaseMessage.TYPE_TEXT: // 文本
	 * msg.setContent(imMsg.getJtFile().mFileName); break; case
	 * IMBaseMessage.TYPE_REQUIREMENT: // 需求 if
	 * (TextUtils.isEmpty(msg.getContent())) { msg.setContent("[需求]"); } break;
	 * case IMBaseMessage.TYPE_KNOWLEDGE: // 知识 case
	 * IMBaseMessage.TYPE_KNOWLEDGE2: // 新知识 if
	 * (TextUtils.isEmpty(msg.getContent())) { msg.setContent("[知识]"); } break;
	 * case IMBaseMessage.TYPE_AUDIO: // 语音 msg.setContent("[语音]"); break; case
	 * IMBaseMessage.TYPE_JTCONTACT_OFFLINE: // 人脉 case
	 * IMBaseMessage.TYPE_JTCONTACT_ONLINE: // 用户 case
	 * IMBaseMessage.TYPE_ORG_OFFLINE: // 线下机构 case
	 * IMBaseMessage.TYPE_ORG_ONLINE: // 线上机构 if (imMsg.getType() ==
	 * IMBaseMessage.TYPE_JTCONTACT_OFFLINE || imMsg.getType() ==
	 * IMBaseMessage.TYPE_JTCONTACT_ONLINE) { // 个人 String cardName = ""; if
	 * (msg != null && msg.getJtFile() != null &&
	 * !StringUtils.isEmpty(msg.getJtFile().mFileName)) { cardName =
	 * msg.getJtFile().mFileName; } msg.setContent("分享了" + cardName); } else if
	 * (imMsg.getType() == IMBaseMessage.TYPE_ORG_OFFLINE || imMsg.getType() ==
	 * IMBaseMessage.TYPE_ORG_ONLINE) { // 机构 String cardName = ""; if (msg !=
	 * null && msg.getJtFile() != null &&
	 * !StringUtils.isEmpty(msg.getJtFile().mFileName)) { cardName =
	 * msg.getJtFile().mFileName; } msg.setContent("分享了" + cardName); } break; }
	 * 
	 * // 发送消息 ConferenceReqUtil.doSendMeetingChat(this, this, msg,
	 * mNetHandler);
	 * 
	 * // 将发送中的消息添加到聊天列表中 listMessage.add(msg); setData(listMessage);
	 * 
	 * // 将消息缓存到数据库 meetingRecordDBManager.insert(App.getUserID(), msg); }
	 */

	// 发送消息（知识、需求、图片、视频、文件、语音等）
	@Override
	public void sendFile(final JTFile jtFile) {

		if (jtFile.mType == JTFile.TYPE_TEXT) {
			sendMessage(jtFile.mFileName);
			return;
		}

		final MeetingMessage msg = new MeetingMessage("");
		msg.setSendType(IMBaseMessage.SEND_TYPE_SENDING);
		msg.setSenderID(App.getUserID());
		msg.setJtFile(jtFile);
		msg.setType(jtFile);
		msg.setRecvID(meetingId + "");
		msg.setTopicID(topicId + "");
		msg.setTime(getLatestTime());
		msg.setSenderName(App.getNick());

		// 将发送中的消息添加到聊天列表中
		listMessage.add(msg);
		setData(listMessage);

		// 将数据存入数据库
		meetingRecordDBManager.insert(App.getUserID(), msg);

		if (msg.getType() == IMBaseMessage.TYPE_KNOWLEDGE) { // 知识
			msg.setContent(jtFile.mFileName);
			if (TextUtils.isEmpty(msg.getContent())) {
				msg.setContent("[知识]");
			}
			ConferenceReqUtil.doSendMeetingChat(this, this, msg, mNetHandler);
			return;
		} else if (msg.getType() == IMBaseMessage.TYPE_KNOWLEDGE2) { // 新知识
			msg.setContent(jtFile.mFileName);
			if (TextUtils.isEmpty(msg.getContent())) {
				msg.setContent("[知识]");
			}
			ConferenceReqUtil.doSendMeetingChat(this, this, msg, mNetHandler);
			return;
		} else if (msg.getType() == IMBaseMessage.TYPE_REQUIREMENT) { // 需求
			msg.setContent(jtFile.mSuffixName);
			if (TextUtils.isEmpty(msg.getContent())) {
				msg.setContent("[需求]");
			}
			ConferenceReqUtil.doSendMeetingChat(this, this, msg, mNetHandler);
			return;
//<<<<<<< HEAD
//		} 
//		else if (msg.getType() == IMBaseMessage.TYPE_JTCONTACT_OFFLINE 
//				|| msg.getType() == IMBaseMessage.TYPE_JTCONTACT_ONLINE 
//				|| msg.getType() == IMBaseMessage.TYPE_ORG_OFFLINE 
//				|| msg.getType() == IMBaseMessage.TYPE_ORG_ONLINE
//				||msg.getType() == IMBaseMessage.TYPE_CUSTOMER
//				||msg.getType() == IMBaseMessage.TYPE_ORGANIZATION) { // 关系
//				String typeMsg = "";
//				if (msg.getType() == IMBaseMessage.TYPE_JTCONTACT_OFFLINE ) {
//					typeMsg = "[人脉]";
//				}else if ( msg.getType() == IMBaseMessage.TYPE_JTCONTACT_ONLINE ) {
//					typeMsg = "[用户]";
//				}else if (msg.getType() == IMBaseMessage.TYPE_ORG_OFFLINE||msg.getType() == IMBaseMessage.TYPE_CUSTOMER) {
//					typeMsg = "[客户]";
//				}else if(msg.getType() == IMBaseMessage.TYPE_ORG_ONLINE||msg.getType() == IMBaseMessage.TYPE_ORGANIZATION){
//					typeMsg = "[组织]";
//				}
//				String cardName = "";
//				if (msg != null && msg.getJtFile() != null && !StringUtils.isEmpty(msg.getJtFile().mFileName)) {
//					cardName = msg.getJtFile().mFileName;
//				}
//				msg.setContent(typeMsg);
//=======
		} else if (msg.getType() == IMBaseMessage.TYPE_JTCONTACT_OFFLINE || msg.getType() == IMBaseMessage.TYPE_JTCONTACT_ONLINE || msg.getType() == IMBaseMessage.TYPE_ORG_OFFLINE
				|| msg.getType() == IMBaseMessage.TYPE_ORG_ONLINE || msg.getType() == IMBaseMessage.TYPE_CUSTOMER || msg.getType() == IMBaseMessage.TYPE_ORGANIZATION) { // 关系

			String typeMsg = "";
			if (msg.getType() == IMBaseMessage.TYPE_JTCONTACT_OFFLINE) {
				typeMsg = "[人脉]";
			} else if (msg.getType() == IMBaseMessage.TYPE_JTCONTACT_ONLINE) {
				typeMsg = "[用户]";
			} else if (msg.getType() == IMBaseMessage.TYPE_ORG_OFFLINE || msg.getType() == IMBaseMessage.TYPE_CUSTOMER) {
				typeMsg = "[客户]";
			} else if (msg.getType() == IMBaseMessage.TYPE_ORG_ONLINE || msg.getType() == IMBaseMessage.TYPE_ORGANIZATION) {
				typeMsg = "[组织]";
			}

			String cardName = "";
			if (msg != null && msg.getJtFile() != null && !StringUtils.isEmpty(msg.getJtFile().mFileName)) {
				cardName = msg.getJtFile().mFileName;
			}
			msg.setContent(typeMsg);
			ConferenceReqUtil.doSendMeetingChat(this, this, msg, mNetHandler);
			return;
		} else if (msg.getType() == IMBaseMessage.TYPE_IMAGE) { // 图片
			// 消息类型标识
			msg.setContent("[图片]");
			if (!TextUtils.isEmpty(msg.getJtFile().mUrl)) {
				ConferenceReqUtil.doSendMeetingChat(this, this, msg, mNetHandler);
			} else if (!TextUtils.isEmpty(msg.getJtFile().mLocalFilePath)) {
				// 保存文件的本地地址
				localFileDBManager.synchronous(App.getUserID(), msg.getMessageID(), msg.getJtFile().mLocalFilePath);
				// 消息类型
				jtFile.mType = JTFile.TYPE_IMAGE;
				// 开始新的上传任务
				startNewUploadTask(msg);
			} else {
				showToast("无法解析图片地址");
			}
		} else if (msg.getType() == IMBaseMessage.TYPE_FILE) { // 文件
			// 消息类型标识
			msg.setContent("[文件]");
			if (!TextUtils.isEmpty(msg.getJtFile().mUrl)) {
				ConferenceReqUtil.doSendMeetingChat(this, this, msg, mNetHandler);
			} else if (!TextUtils.isEmpty(msg.getJtFile().mLocalFilePath)) {
				// 保存文件的本地地址
				localFileDBManager.synchronous(App.getUserID(), msg.getMessageID(), msg.getJtFile().mLocalFilePath);
				// 文件类型
				jtFile.mType = JTFile.TYPE_FILE;
				// 开始新的上传任务
				startNewUploadTask(msg);
			} else {
				showToast("无法解析文件地址");
			}
		} else if (msg.getType() == IMBaseMessage.TYPE_AUDIO) { // 语音（存放在指定路径下）
			msg.setContent("[语音]");
			if (!TextUtils.isEmpty(msg.getJtFile().mUrl)) {
				ConferenceReqUtil.doSendMeetingChat(this, this, msg, mNetHandler);
			} else if (!TextUtils.isEmpty(msg.getJtFile().mLocalFilePath)) {
				jtFile.mType = JTFile.TYPE_AUDIO;
				startNewUploadTask(msg);
			} else {
				showToast("无法解析语音文件地址");
			}
		} else if (msg.getType() == IMBaseMessage.TYPE_VIDEO) { // 视频
			// 消息类型标识
			msg.setContent("[视频]");
			if (!TextUtils.isEmpty(msg.getJtFile().mUrl)) {
				ConferenceReqUtil.doSendMeetingChat(this, this, msg, mNetHandler);
			} else if (!TextUtils.isEmpty(msg.getJtFile().mLocalFilePath)) {
				// 保存文件的本地地址
				localFileDBManager.synchronous(App.getUserID(), msg.getMessageID(), msg.getJtFile().mLocalFilePath);
				// 文件类型
				jtFile.mType = JTFile.TYPE_VIDEO;
				// 视频缩略图
				String fileName = getUserAndTimeBasedFormatName(".jpg");
				String scrennshotPath = getVideoScreenshotCachePath(jtFile.mLocalFilePath, fileName, meetingDetail.getId(), topicDetail.getId());
				JTFile screenshotJtFile = EUtil.createJTFileFromLocalFile(scrennshotPath);
				if (screenshotJtFile != null) {
					HyChatFileUploader uploader = new HyChatFileUploader(this, screenshotJtFile, msg.getMessageID());
					uploader.setOnFileUploadListener(new OnFileUploadListener() {

						@Override
						public void onPrepared(String messageId) {

						}

						@Override
						public void onStarted(String messageId) {

						}

						@Override
						public void onUpdate(String messageId, int value) {

						}

						@Override
						public void onSuccess(String messageId, JTFile jtFile) {
							removeUploadTask(messageId); // 从上传任务中移除
							msg.getJtFile().reserved2 = jtFile.mUrl; // 视频缩略图地址
							startNewUploadTask(msg); // 上传视频
						}

						@Override
						public void onError(String messageId, int code, String message) {
							setMessageSendFail(listMessage, messageId); // 设置消息发送状态
							removeUploadTask(messageId); // 从上传任务中移除
						}

						@Override
						public void onCanceled(String messageId) {
							setMessageSendFail(listMessage, messageId); // 设置消息发送状态
							removeUploadTask(messageId); // 从上传任务中移除
						}
					});
					uploader.start();
				} else {
					showToast("获取视频截图失败");
					startNewUploadTask(msg);
				}
			} else {
				showToast("无法解析视频文件地址");
			}
		} else if (msg.getType() == IMBaseMessage.TYPE_CONFERENCE) { // 会议
			msg.setContent(jtFile.mFileName);
			if (TextUtils.isEmpty(msg.getContent())) {
				msg.setContent("[会议]");
			}
			ConferenceReqUtil.doSendMeetingChat(this, this, msg, mNetHandler);
			return;
		}
	}

	// 加载缓存的记录
	@Override
	public List<MeetingMessage> loadCacheMessage() {
		List<MeetingMessage> listMsg = meetingRecordDBManager.query(App.getUserID(), meetingId + "", topicId + "");
		return listMsg;
	}

	@Override
	public List<MeetingMessage> loadCacheMessage(String messageId, String keyword, int span) {
		List<MeetingMessage> listMsg = meetingRecordDBManager.query(App.getUserID(), meetingId + "", topicId + "");
		return listMsg;
	}

	@Override
	public void saveCacheMessage(List<MeetingMessage> listMsg) {
		meetingRecordDBManager.update(App.getUserID(), listMsg);
	}

	@Override
	public void updateCacheMessage(MeetingMessage msg) {
		meetingRecordDBManager.update(App.getUserID(), msg);
	}

	@Override
	public void deleteCacheMessage(String msgId) {
		meetingRecordDBManager.delete(App.getUserID(), msgId);
	}

	// 获取更多消息记录
	@Override
	public void getMoreMessage() {
		String strTime = getLatestTime();
		boolean forward = true;
		// 当前没有聊天记录
		if (TextUtils.isEmpty(strTime)) {
			forward = false;
			strTime = EUtil.getFormatFromDate(new Date());
		}
		// 请求聊天记录
		ConferenceReqUtil.doGetMeetingMessage(this, this, meetingId, topicId, strTime, forward, mNetHandler);
		// 重置请求事件
		resetGetTime();
	}

	@Override
	public void bindData(int tag, Object object) {
		if (hasDestroy()) {
			return;
		}
		super.bindData(tag, object);
	}

	// 获取与会人数量
	private String getMemberCount() {
		return "(" + meetingDetail.getListMeetingMember().size() + ")";
	}

	// 更新会议详情
	public void updateMeetingDetail() {
		if (meetingDetail == null) {
			return;
		}
		setTitle(meetingDetail.getMeetingName() + getMemberCount());
	}

	// 查找消息发送人昵称
	public String getNickNameByMessage(MeetingMessage msg) {
		String nick = "";
		MMeetingMember member = meetingDetail.getMeetingMemberByUserId(msg.getSenderID());
		if (member != null) {
			nick = member.getMemberName();
		}
		return nick;
	}

	// 查找消息发送人头像
	public String getImageByMessage(MeetingMessage msg) {
		String avatar = "";
		MMeetingMember member = meetingDetail.getMeetingMemberByUserId(msg.getSenderID());
		if (member != null) {
			avatar = member.getMemberPhoto();
		}
		return avatar;
	}

	@Override
	public void onUpdate(String id, int value) {
		super.onUpdate(id, value);
	}

	@Override
	public void onCanceled(String id) {
		super.onCanceled(id);
	}

	@Override
	public void onSuccess(String id, JTFile jtFile) {
		super.onSuccess(id, jtFile);
	}

	@Override
	public void onError(String id, int errCode, String errMsg) {
		super.onError(id, errCode, errMsg);
	}
}
