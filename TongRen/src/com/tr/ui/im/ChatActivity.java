package com.tr.ui.im;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.MenuItem;

import com.tr.App;
import com.tr.R;
import com.tr.api.CommonReqUtil;
import com.tr.api.IMReqUtil;
import com.tr.model.im.ChatDetail;
import com.tr.model.im.IMUtil;
import com.tr.model.im.MGetChatMessage;
import com.tr.model.im.MNotifyMessageBox;
import com.tr.model.im.MSendMessage;
import com.tr.model.obj.ChatMessage;
import com.tr.model.obj.IMBaseMessage;
import com.tr.model.obj.JTFile;
import com.tr.model.obj.MUCDetail;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.utils.common.EUtil;
import com.utils.common.FileUploader;
import com.utils.http.EAPIConsts;
import com.utils.string.StringUtils;

public class ChatActivity extends ChatBaseActivity {

	private final String TAG = getClass().getSimpleName();
//	private HashMap<String,SendMessages> sendMessagesForwardingList;
	private boolean isFirstIn = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 屏蔽单聊的@功能 - 该字段位于父类中
		isAvatarLongClickable = false;

		getParam();
	}

	// 从传入页面的参数初始化变量
	public void getParam() {
		Intent intent = getIntent();
		fromActivityName = intent.getStringExtra(ENavConsts.EFromActivityName);
		chatDetail = (ChatDetail) intent.getSerializableExtra(ENavConsts.EChatDetail);
//		sendMessagesForwardingList =  (HashMap<String, SendMessages>) intent.getSerializableExtra(ENavConsts.SendMessagesForwardingList);
		setTitle(chatDetail.getThatName());
		MNotifyMessageBox.clearChatMessage(chatDetail.getThatID());
		// 是否是查看搜索聊天记录结果
		if (searchMessagefromIndex!=-1) {
//			startMergeAndSaveMessage(this, null, null, TASK_SEARCH);
			getMessageRecord(searchMessagefromIndex+1);
		} else {
//			// 加载数据库中的聊天记录
			startMergeAndSaveMessage(this, null, null, TASK_READ);
//			IMReqUtil.getChatMessage(this, this, mNetHandler, chatDetail.getThatID(), -1, MAX_MESSAGE_SIZE, true);//查询最新20条记录
			// 清空通知栏消息
			cleanNotifyBox(chatDetail.getThatID());
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.im_muc_detail_create_next: // 从私聊页面切换到聊天详情页
			if (fromActivityName != null && fromActivityName.equalsIgnoreCase(ChatRecordSearchActivity.class.getSimpleName())) {
				ENavigate.startIMEditMumberActivity(this, null, chatDetail, ENavigate.REQUSET_CODE_CHAT, true);
			} else {
				ENavigate.startIMEditMumberActivity(this, null, chatDetail, ENavigate.REQUSET_CODE_CHAT, false);
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		// 可以根据多个请求代码来作相应的操作 从选择联系人页面返回群聊设置页
		if (ENavigate.REQUSET_CODE_CHAT == requestCode) {
			if (resultCode == RESULT_OK) {
				// 创建群聊成功后返回
				MUCDetail nowDetail = (MUCDetail) data.getSerializableExtra(ENavConsts.EMucDetail);
				if (nowDetail != null) {
					// 从私聊-设置-创建了个群聊，直接跳到群聊页面
					ENavigate.startIMGroupActivity(this, nowDetail);
					finish();
				}
				// 检查是否清空聊天记录
				String checkCleanHistory = (String) data.getSerializableExtra(ENavConsts.EIMNotifyCleanHistory);
				if (checkCleanHistory != null) {
					// 已清空，则清除本地列表
					cleanHistory();
				}
			} else {
				// 在群聊页面没完成操作，继续保留在该页面
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	// 发送消息
	@Override
	public void sendMessage(String text) {
		sendChatMessage(text);
	}

	/**
	 * 发送富媒体消息
	 * @param type
	 * @param jtFile
	 */
	@Override
	public void sendRichMessage(int type, final JTFile jtFile) {

		// 消息内容实体
		if (jtFile == null) {
			showToast("消息内容错误，请重试");
			return;
		}

		if (type == IMBaseMessage.TYPE_KNOWLEDGE) { // 旧知识消息
			cacheUrl = jtFile.mUrl; // 缓存网址
			cacheMessage = jtFile.mFileName; // 缓存消息
			CommonReqUtil.doFetchExternalKnowledgeUrl(this, this, cacheUrl, true, null);
			return;
		} else if (type == IMBaseMessage.TYPE_TEXT) { // 普通文本消息
			sendChatMessage(jtFile.mFileName);
			return;
		}

		// 封装消息对象
		final IMBaseMessage msg = new ChatMessage("");
		msg.setSendType(IMBaseMessage.SEND_TYPE_SENDING);
		msg.setSenderID(App.getUserID());
		msg.setJtFile(jtFile);
		msg.setType(type); // 设置消息类型
		msg.setRecvID(chatDetail.getThatID());
		msg.setTime(getLatestTime());
		msg.setIndex(getLatestMessageIndex());
		msg.setSenderName(App.getNick());
		// 将发送中的消息添加到聊天列表中
		listMessage.add(msg);
		setData(listMessage);
		// 定位到最新的消息
		locateToLatestMessage();
		// 将消息保存到数据库
		// 判断消息类型
		if (type == IMBaseMessage.TYPE_KNOWLEDGE2) { // 新知识
			msg.setContent(jtFile.mFileName);
			if (TextUtils.isEmpty(msg.getContent())) {
				if(TextUtils.isEmpty(jtFile.reserved2)){
					msg.setContent("分享了[知识]");
				}else{
					msg.setContent("分享了["+jtFile.reserved2+"]");
				}
			}
			if(TextUtils.isEmpty(jtFile.mTaskId)){
				msg.setContent(jtFile.mUrl);
				msg.setType(IMBaseMessage.TYPE_TEXT);
			}
			IMReqUtil.sendMessage(this, this, strangerHandler, msg);
			return;
		} else if (type == IMBaseMessage.TYPE_JTCONTACT_OFFLINE || type == IMBaseMessage.TYPE_JTCONTACT_ONLINE || type == IMBaseMessage.TYPE_ORG_OFFLINE
				|| type == IMBaseMessage.TYPE_ORG_ONLINE) { // 人脉
			String cardName = "";
			if (msg != null && msg.getJtFile() != null && !StringUtils.isEmpty(msg.getJtFile().mFileName)) {
				cardName = msg.getJtFile().mFileName;
			}
//			msg.setContent(cardName);
			msg.setContent("分享了[人脉]");
			IMReqUtil.sendMessage(this, this, mNetHandler, msg);
			return;
		}
		// 组织
		else if (type == IMBaseMessage.TYPE_ORGANIZATION) {
			String cardName = "";
			
			if(msg != null && msg.getJtFile() != null && !StringUtils.isEmpty(msg.getJtFile().getReserved1()) && msg.getJtFile().getReserved1().length() > 250) {
				msg.getJtFile().setReserved1(msg.getJtFile().getReserved1().substring(0, 250));
			}
			
			if (msg != null && msg.getJtFile() != null && !StringUtils.isEmpty(msg.getJtFile().mFileName)) {
				cardName = msg.getJtFile().mFileName;
			}
			if (StringUtils.isEmpty(msg.getJtFile().mTaskId)) {
				msg.getJtFile().mTaskId = msg.getJtFile().getId();
			}
//			msg.setContent(cardName);
			msg.setContent("分享了[组织]");
			IMReqUtil.sendMessage(this, this, mNetHandler, msg);
		}
		// 客户
		else if (type == IMBaseMessage.TYPE_CUSTOMER) {
			String cardName = "";
			if (msg != null && msg.getJtFile() != null && !StringUtils.isEmpty(msg.getJtFile().mFileName)) {
				cardName = msg.getJtFile().mFileName;
			}
			if (StringUtils.isEmpty(msg.getJtFile().mTaskId)) {
				msg.getJtFile().mTaskId = msg.getJtFile().getId();
			}
//			msg.setContent(cardName);
			msg.setContent("分享了[客户]");
			IMReqUtil.sendMessage(this, this, mNetHandler, msg);
		} else if (type == IMBaseMessage.TYPE_REQUIREMENT) { // 需求
			msg.setContent(jtFile.mSuffixName);
			if (msg.getContent() == null || msg.getContent().length() <= 0) {
//				msg.setContent("[需求]");
				msg.setContent("分享了[需求]");
			}
			IMReqUtil.sendMessage(this, this, mNetHandler, msg);
			return;
		} else if (type == IMBaseMessage.TYPE_IMAGE) { // 图片

			if (!TextUtils.isEmpty(jtFile.mUrl)) { // 如果转发的图片已经上传，则直接转发
				msg.setContent("[图片]");
				jtFile.mType = JTFile.TYPE_IMAGE;
				IMReqUtil.sendMessage(this, this, mNetHandler, msg);
				return;
			}
			if (TextUtils.isEmpty(jtFile.mLocalFilePath) || !new File(jtFile.mLocalFilePath).exists()) { // 图片地址错误或已删除
				// 更改界面和数据库状态
				msg.setSendType(IMBaseMessage.SEND_TYPE_FAIL);
				setData(listMessage);
				showToast("无法解析图片地址或图片文件已删除");
				return;
			}
			// 获取图片缩略图
			String thumbnailPath = EUtil.getChatImageThumbnail(this, chatDetail.getThatID(), jtFile.mLocalFilePath);
			if (!TextUtils.isEmpty(thumbnailPath)) { // 获取缩略图成功
				jtFile.mLocalFilePath = thumbnailPath;
			}
			// 将本地图片地址保存到服务器
			clfManager.synchronous(App.getUserID(), msg.getMessageID(), jtFile.mLocalFilePath);
			// 设置消息一些信息
			msg.setContent("[图片]");
			jtFile.mType = JTFile.TYPE_IMAGE;
			startNewUploadTask(msg);
		} else if (type == IMBaseMessage.TYPE_FILE) { // 文件

			msg.setContent("[文件]");
			jtFile.mType = JTFile.TYPE_FILE;
			if (!TextUtils.isEmpty(jtFile.mUrl)) { // 如果转发的文件已经上传，则直接转发
				IMReqUtil.sendMessage(this, this, mNetHandler, msg);
			} else {
				startNewUploadTask(msg);
			}
		} else if (type == IMBaseMessage.TYPE_AUDIO) { // 语音
			msg.setContent("[语音]");
			jtFile.mType = JTFile.TYPE_AUDIO;
			if (!TextUtils.isEmpty(jtFile.mUrl)) { // 如果转发的语音已经上传，则直接转发
				IMReqUtil.sendMessage(this, this, mNetHandler, msg);
			} else {
				startNewUploadTask(msg);
			}
		} else if (type == IMBaseMessage.TYPE_VIDEO) { // 视频

			if (!TextUtils.isEmpty(jtFile.mUrl)) { // 如果转发的视频已经上传，则直接转发
				msg.setContent("[视频]");
				jtFile.mType = JTFile.TYPE_VIDEO;
				IMReqUtil.sendMessage(this, this, mNetHandler, msg);
				return;
			}
			if (TextUtils.isEmpty(jtFile.mLocalFilePath) || !new File(jtFile.mLocalFilePath).exists()) {
				// 更改界面和数据库状态
				msg.setSendType(IMBaseMessage.SEND_TYPE_FAIL);
				setData(listMessage);
				showToast("无法解析视频地址或视频文件已删除");
				return;
			}
			// 视频截图
			String screenshotPath = EUtil.getChatVideoScreenshot(ChatActivity.this, chatDetail.getThatID(), jtFile.mLocalFilePath);
			if (TextUtils.isEmpty(screenshotPath)) {
				msg.setSendType(IMBaseMessage.SEND_TYPE_FAIL);
				setData(listMessage);
				showToast("获取视频截图失败");
				return;
			}
			msg.setContent("[视频]");
			jtFile.mType = JTFile.TYPE_VIDEO;
			// 启动上传任务
			startNewUploadTask(msg);
		} else if (type == IMBaseMessage.TYPE_CONFERENCE) { // 会议
			msg.setContent(jtFile.mFileName);
			if (TextUtils.isEmpty(msg.getContent())) {
				msg.setContent("分享了[会议]");
			}
			IMReqUtil.sendMessage(this, this, mNetHandler, msg);
		}
	}

	/**
	 * 发送文本消息
	 * 
	 * @param text
	 */
	public void sendChatMessage(String text) {

		IMBaseMessage msg = new ChatMessage(text);
		msg.setSendType(IMBaseMessage.SEND_TYPE_SENDING);
		msg.setSenderID(App.getUserID());
		msg.setJtFile(null);
		msg.setType(IMBaseMessage.TYPE_TEXT);
		msg.setRecvID(chatDetail.getThatID());
		msg.setTime(getLatestTime());
		msg.setSenderName(App.getNick());
		msg.setIndex(getLatestMessageIndex());
		// 发送消息
		IMReqUtil.sendMessage(this, this, strangerHandler, msg);
		// 将发送中的消息添加到聊天列表中
		listMessage.add(msg);
		setData(listMessage);
		// 定位到最新的消息
		locateToLatestMessage();
	}

	/**
	 * 获取更多信息记录
	 */
	@Override
	public void getMoreMessage(boolean isBackward) {
		int fromIndex = -1; // 消息起始索引
		if (listMessage != null && listMessage.size() > 0) {
			fromIndex = listMessage.get(0).getIndex();
		}
		IMReqUtil.getChatMessage(this, this, mNetHandler, chatDetail.getThatID(), fromIndex, MAX_MESSAGE_SIZE, isBackward);
	}

	protected void back() {
		finish();
	}

	/**
	 * 获取消息发送人的昵称
	 */
	public String getNickNameByMessage(IMBaseMessage msg) {
		if (msg.getSenderID().equalsIgnoreCase(App.getUserID())) { // 自己发的消息
			return App.getNick();
		} else { // 对方发的消息
			return chatDetail.getThatName();
		}
	}

	/**
	 * 获取消息发送人的头像
	 */
	public String getImageByMessage(IMBaseMessage msg) {
		if (msg.getSenderID().equalsIgnoreCase(App.getUserID())) { // 自己发的消息
			return App.getUserAvatar();
		} else { // 对方发的消息
			return chatDetail.getThatImage();
		}
	}

	/**
	 * 清空消息盒子
	 */
	@Override
	public void cleanNotifyBox(String id) {
		MNotifyMessageBox.clearChatMessage(id);
	}

	/**
	 * 获取消息发送人类型
	 */
	@Override
	public int getSenderTypeByMessage(IMBaseMessage msg) {
		if (msg.getSenderID().equalsIgnoreCase(App.getUserID())) {
			return App.getUser().getmUserType() - 1;
		} else {
			return chatDetail.getType() - 1;
		}
	}
	
	@Override
	public void bindData(int tag, Object object) {
		if (tag == EAPIConsts.IMReqType.IM_REQ_SEND_MESSAGE) { // 发送消息返回
			MSendMessage sm = (MSendMessage) object;
			if (sm.isSucceed() != true) { // 消息发送失败
				// 设置该条消息状态
				IMUtil.setMessageSendType(this, listMessage, getChatId(),
						sm.getMessageID(), IMBaseMessage.SEND_TYPE_FAIL);
				// 刷新列表
				setData(listMessage);
				return;
			} else { // 消息发送成功
						// 设置该条消息状态
				IMUtil.setMessageSendType(this, listMessage, getChatId(),
						sm.getMessageID(), IMBaseMessage.SEND_TYPE_SENT);
				// 刷新列表
				setData(listMessage);
				// 合并消息
				List<IMBaseMessage> newList = sm.getListMessage();
				if (newList == null || newList.size() <= 0) {
					return;
				}
				startMergeAndSaveMessage(this, listMessage, newList, TASK_MERGE);
			}
		} else if (tag == EAPIConsts.IMReqType.IM_REQ_GET_CHAT_MESSAGE) { // 获取私聊聊天记录
			dismissLoadingDialog();
			// 获取消息列表
			MGetChatMessage gcm = (MGetChatMessage) object;
			// 是否还有数据
			if (!gcm.isBackward() && !gcm.isHasMore()) {
				listView.setPullLoadEnable(false);
			}
			List<IMBaseMessage> newList = gcm.getListMessage();
			if (newList == null || newList.size() <= 0) {
				if (!gcm.isBackward()) {
					listView.stopRefresh();
				}
				return;
			}

			if(searchMessagefromIndex!=-1){
				listView.setPullRefreshEnable(false);
				listView.setPullLoadEnable(false);
			}
			// 整合消息
			if (!gcm.isBackward()) {
				startMergeAndSaveMessage(this, listMessage, newList, TASK_MORE);
			} else {
				startMergeAndSaveMessage(this, listMessage, newList, TASK_MERGE);
			}

		} else {
			super.bindData(tag, object);
		}
	}
	
	@Override
	public String getChatId() {
		String id = "";
		if (chatDetail != null && !TextUtils.isEmpty(chatDetail.getThatID())) {
			id = chatDetail.getThatID();
		}
		return id;
	}

	@Override
	public void getMessageRecord(int fromIndex) {
		IMReqUtil.getChatMessage(this, this, mNetHandler, chatDetail.getThatID(), fromIndex, MAX_MESSAGE_SIZE, false);
	}
	
//	@Override
//	public void onResume() {
//		super.onResume();
//		if(!isFirstIn){
//			IMReqUtil.getChatMessage(this, this, mNetHandler, chatDetail.getThatID(), -1, MAX_MESSAGE_SIZE, true);//查询最新20条记录
//		}
//		isFirstIn = false;
//	}

}
