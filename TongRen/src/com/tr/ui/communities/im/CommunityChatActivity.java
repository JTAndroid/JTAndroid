package com.tr.ui.communities.im;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.tr.App;
import com.tr.R;
import com.tr.api.CommonReqUtil;
import com.tr.api.CommunityReqUtil;
import com.tr.model.im.MGetMUCMessage;
import com.tr.model.im.MSendMessage;
import com.tr.model.obj.Connections;
import com.tr.model.obj.IMBaseMessage;
import com.tr.model.obj.JTFile;
import com.tr.model.obj.MUCDetail;
import com.tr.model.obj.MUCMessage;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.communities.home.CommunityChatSettingActivity;
import com.tr.ui.communities.model.CommunityDetailRes;
import com.tr.ui.communities.model.ImMucinfo;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.im.ChatRecordSearchActivity;
import com.utils.common.EUtil;
import com.utils.common.GlobalVariable;
import com.utils.http.EAPIConsts;
import com.utils.http.EAPIConsts.CommunityReqType;
import com.utils.string.StringUtils;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

public class CommunityChatActivity extends ChatBaseActivity {

	private int applyType;//加入社群权限:1是所有人 2申请加入
	private ArrayList<Connections> Connections;
	private boolean isNumIn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		community = (ImMucinfo) getIntent().getSerializableExtra("community");
		applyType = getIntent().getIntExtra("applyType", 1);
		isNumIn = getIntent().getBooleanExtra(GlobalVariable.COMMUNITY_ISNUMIN, false);
		if (mucDetail != null) {
			thatMucID = mucDetail.getId() + "";
			HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(),
					mucDetail.getTitle(), false, null, true, true);
			adapter.setData(mucDetail, this, thatMucID);
			getChatHistory(thatMucID, searchMessagefromIndex + 1, isBackward);// 获取群聊聊天记录
		} else if(community != null){
			CommunityReqUtil.doGetCommunityMemberList(this, community.getId(),
					this, handler);
		} else{
			long communityId = getIntent().getLongExtra("communityId", 0);
			CommunityReqUtil.doGetCommunityDetail(this, communityId, Long.parseLong(App.getApp().getUserID()), this, null);
		}
	}

	/**
	 * 设置列表适配器
	 * 
	 * @param messageList
	 */
	public void setData(List<IMBaseMessage> messageList) {
		if (adapter != null) {
			adapter.setList(messageList);
		}
		refreshList();
	}

	@Override
	public void sendMessage(String text) {
		IMBaseMessage msg = new MUCMessage(text);
		msg.setSendType(IMBaseMessage.SEND_TYPE_SENDING);
		msg.setSenderID(App.getUserID());
		msg.setJtFile(null);
		msg.setType(IMBaseMessage.TYPE_TEXT);
		msg.setRecvID(thatMucID);
		msg.setTime(getLatestTime());
		msg.setIndex(getLatestMessageIndex());
		msg.setContent(text.trim());
		msg.setSenderName(App.getNick());
		msg.setAtIds(atConnectionsIds);
		// 将发送中的消息添加到聊天列表中
		listMessage.add(msg);
		adapter.notifyDataSetChanged();
		// 定位到最新的消息
		locateToLatestMessage();
		// 发送消息请求
		CommunityReqUtil.sendMessage(this, this, handler, msg);
	}

	@Override
	public void sendRichMessage(int type, JTFile jtFile) {

		// 消息内容错误
		if (jtFile == null) {
			showToast("消息内容错误，请重试");
			return;
		}

		if (type == IMBaseMessage.TYPE_KNOWLEDGE) { // 旧知识消息
			cacheUrl = jtFile.mUrl; // 缓存网址
			CommonReqUtil.doFetchExternalKnowledgeUrl(this, this, cacheUrl,
					true, null);
			return;
		} else if (type == IMBaseMessage.TYPE_TEXT) { // 普通文本消息
			sendMessage(jtFile.mFileName);
			return;
		}

		// 封装成消息对象
		final IMBaseMessage msg = new MUCMessage("");
		msg.setSendType(IMBaseMessage.SEND_TYPE_SENDING);
		msg.setSenderID(App.getUserID());
		msg.setJtFile(jtFile);
		msg.setType(type);
		msg.setRecvID(thatMucID);
		msg.setTime(getLatestTime());
		msg.setIndex(getLatestMessageIndex());
		msg.setSenderName(App.getNick());

		// 将发送中的消息添加到聊天列表中
		listMessage.add(msg);
		setData(listMessage);
		locateToLatestMessage();
		// 判断消息类型
		if (type == IMBaseMessage.TYPE_KNOWLEDGE2) {
			msg.setContent(jtFile.mFileName);
			if (TextUtils.isEmpty(msg.getContent())) {
				if (TextUtils.isEmpty(jtFile.reserved2)) {
					msg.setContent("分享了[知识]");
				} else {
					msg.setContent("分享了[" + jtFile.reserved2 + "]");
				}
			}
			if (TextUtils.isEmpty(jtFile.mTaskId)) {
				msg.setContent(jtFile.mUrl);
				msg.setType(IMBaseMessage.TYPE_TEXT);
			}
			CommunityReqUtil.sendMessage(this, this, handler, msg);
			return;
		} else if (type == IMBaseMessage.TYPE_REQUIREMENT) {
			msg.setContent(jtFile.mSuffixName);
			if (TextUtils.isEmpty(msg.getContent())) {
				// msg.setContent("[需求]");
				msg.setContent("分享了[需求]");
			}
			CommunityReqUtil.sendMessage(this, this, handler, msg);
			return;
		} else if (type == IMBaseMessage.TYPE_JTCONTACT_OFFLINE
				|| type == IMBaseMessage.TYPE_JTCONTACT_ONLINE
				|| type == IMBaseMessage.TYPE_ORG_OFFLINE
				|| type == IMBaseMessage.TYPE_ORG_ONLINE) {
			msg.setContent("分享了[人脉]");
			CommunityReqUtil.sendMessage(this, this, handler, msg);
			return;
		}
		// 组织
		else if (type == IMBaseMessage.TYPE_ORGANIZATION) {
			msg.setContent("分享了[组织]");
			if (StringUtils.isEmpty(msg.getJtFile().mTaskId)) {
				msg.getJtFile().mTaskId = msg.getJtFile().getId();
			}
			CommunityReqUtil.sendMessage(this, this, handler, msg);
			return;
		}
		// 客户
		else if (type == IMBaseMessage.TYPE_CUSTOMER) {
			msg.setContent("分享了[组织]");
			CommunityReqUtil.sendMessage(this, this, handler, msg);
		} else if (type == IMBaseMessage.TYPE_IMAGE) { // 图片

			if (!TextUtils.isEmpty(jtFile.mUrl)) { // 图片已经上传，直接转发
				msg.setContent("[图片]");
				jtFile.mType = JTFile.TYPE_IMAGE;
				CommunityReqUtil.sendMessage(this, this, handler, msg);
				return;
			}
			if (TextUtils.isEmpty(jtFile.mLocalFilePath)
					|| !new File(jtFile.mLocalFilePath).exists()) {
				// 更改界面和数据库状态
				msg.setSendType(IMBaseMessage.SEND_TYPE_FAIL);
				setData(listMessage);
				showToast("无法解析图片地址或图片文件已删除");
				return;
			}
			// 获取图片缩略图
			String thumbnailPath = EUtil.getChatImageThumbnail(this, thatMucID,
					jtFile.mLocalFilePath);
			if (!TextUtils.isEmpty(thumbnailPath)) { // 获取缩略图成功
				jtFile.mLocalFilePath = thumbnailPath;
			}
			// 将本地图片地址保存到服务器
			clfManager.synchronous(App.getUserID(), msg.getMessageID(),
					jtFile.mLocalFilePath);
			// 设置消息一些信息
			msg.setContent("[图片]");
			jtFile.mType = JTFile.TYPE_IMAGE;
			startNewUploadTask(msg);
		} else if (type == IMBaseMessage.TYPE_FILE) { // 文件
			msg.setContent("[文件]");
			jtFile.mType = JTFile.TYPE_FILE;
			if (!TextUtils.isEmpty(jtFile.mUrl)) {
				CommunityReqUtil.sendMessage(this, this, handler, msg);
			} else {
				startNewUploadTask(msg);
			}
		} else if (type == IMBaseMessage.TYPE_AUDIO) { // 语音
			msg.setContent("[语音]");
			jtFile.mType = JTFile.TYPE_AUDIO;
			if (!TextUtils.isEmpty(jtFile.mUrl)) {
				CommunityReqUtil.sendMessage(this, this, handler, msg);
			} else {
				startNewUploadTask(msg);
			}
		} else if (type == IMBaseMessage.TYPE_VIDEO) { // 视频

			if (!TextUtils.isEmpty(jtFile.mUrl)) { // 视频已经上传，直接转发
				msg.setContent("[视频]");
				jtFile.mType = JTFile.TYPE_VIDEO;
				CommunityReqUtil.sendMessage(this, this, handler, msg);
				return;
			}
			// 发送视频缩略图
			if (TextUtils.isEmpty(jtFile.mLocalFilePath)
					|| !new File(jtFile.mLocalFilePath).exists()) {
				// 更改界面和数据库状态
				msg.setSendType(IMBaseMessage.SEND_TYPE_FAIL);
				setData(listMessage);
				showToast("无法解析视频地址或视频文件已删除");
				return;
			}
			// 获取视频截图
			String screenshotPath = EUtil.getChatVideoScreenshot(this,
					thatMucID, jtFile.mLocalFilePath);
			if (TextUtils.isEmpty(screenshotPath)) {
				msg.setSendType(IMBaseMessage.SEND_TYPE_FAIL);
				setData(listMessage);
				showToast("获取视频截图失败");
				return;
			}
			msg.setContent("[视频]");
			jtFile.mType = JTFile.TYPE_VIDEO;
			startNewUploadTask(msg);
		} else if (type == IMBaseMessage.TYPE_CONFERENCE) { // 会议
			msg.setContent(jtFile.mFileName);
			if (TextUtils.isEmpty(msg.getContent())) {
				msg.setContent("分享了[会议]");
			}
			CommunityReqUtil.sendMessage(this, this, handler, msg);
		}
	}

	@Override
	public void sendMessage(IMBaseMessage msg) {
		// 重新设置发送状态
		msg.setSendType(IMBaseMessage.SEND_TYPE_SENDING);
		// 刷新列表
		setData(listMessage);

		if (msg.getType() == IMBaseMessage.TYPE_TEXT
				|| msg.getType() == IMBaseMessage.TYPE_REQUIREMENT
				|| msg.getType() == IMBaseMessage.TYPE_KNOWLEDGE
				|| msg.getType() == IMBaseMessage.TYPE_JTCONTACT_OFFLINE
				|| msg.getType() == IMBaseMessage.TYPE_JTCONTACT_ONLINE
				|| msg.getType() == IMBaseMessage.TYPE_ORG_OFFLINE
				|| msg.getType() == IMBaseMessage.TYPE_ORG_ONLINE
				|| msg.getType() == IMBaseMessage.TYPE_ORGANIZATION) {
			CommunityReqUtil.sendMessage(this, this, handler, msg); // 直接重发
		} else { // 发送文件（上传失败还是消息发送失败）
			if (!TextUtils.isEmpty(msg.getJtFile().mUrl)) { // 文件已上传，直接重发
				CommunityReqUtil.sendMessage(this, this, handler, msg);
			} else {
				if (msg.getJtFile().mType == JTFile.TYPE_VIDEO
						&& TextUtils.isEmpty(msg.getJtFile().reserved2)) { // 视频截图未上传
					String screenshotPath = EUtil.getChatVideoScreenshot(this,
							thatMucID, msg.getJtFile().mLocalFilePath);
					if (TextUtils.isEmpty(screenshotPath)) {
						msg.setSendType(IMBaseMessage.SEND_TYPE_FAIL);
						setData(listMessage);
						showToast("获取视频截图失败");
						return;
					}
					startNewUploadTask(msg);
				} else {
					startNewUploadTask(msg);
				}
			}
		}
	}

	@Override
	public void bindData(int tag, Object object) {
		if (object != null) {
			List<IMBaseMessage> newList = new ArrayList<IMBaseMessage>();
			switch (tag) {
			case CommunityReqType.TYPE_GET_COMMUNITY_DETAIL:// 社群详情
				HashMap<String, Object> community_detail = (HashMap<String, Object>) object;
				if (null != community_detail) {
//					exist = (Boolean) community_detail.get("exist");
					CommunityDetailRes communityDetailRes = (CommunityDetailRes) community_detail.get("result");
					community = communityDetailRes.getCommunity();
					applyType = communityDetailRes.getSet().getApplayType();
					CommunityReqUtil.doGetCommunityMemberList(this, community.getId(),
							this, handler);
				}
				break;
			case EAPIConsts.CommunityReqType.TYPE_GET_COMMUNITY_MEMBER_LIST:
				HashMap<String, Object> dataBox = (HashMap<String, Object>) object;
				mucDetail = (MUCDetail) dataBox.get("mucDetail");
				HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(),
						mucDetail.getTitle(), false, null, true, true);
				thatMucID = mucDetail.getId() + "";
				adapter.setData(mucDetail, this, thatMucID);
				getChatHistory(thatMucID, fromIndex, isBackward);// 获取群聊聊天记录
				break;
			case EAPIConsts.IMReqType.IM_REQ_GET_MUC_MESSAGE:// 获取群聊聊天记录
				chatXlv.stopRefresh();
				MGetMUCMessage gcm = (MGetMUCMessage) object;
				// 是否还有数据
				if (!gcm.isBackward() && !gcm.isHasMore()) {
					chatXlv.setPullLoadEnable(false);
				}
				newList = gcm.getListMessage();
				if (newList == null || newList.size() <= 0) {
					return;
				}
				// 整合消息
				if (!gcm.isBackward()) {
					listMessage.addAll(0, newList);
				} else {
					IMUtils.mergeListMessage(listMessage, newList);
				}
				adapter.notifyDataSetChanged();

				if(Connections != null){
					for(Connections conn:Connections){
						JTFile jtFile = new JTFile();
						// 用户
						jtFile.mType = JTFile.TYPE_JTCONTACT_ONLINE;
						jtFile.mTaskId = conn.getId();

						jtFile.fileName = conn.getName();
						jtFile.mSuffixName = conn.getCompany();
						jtFile.mUrl = conn.getImage();
						jtFile.reserved1 = "";
						sendRichMessage(IMBaseMessage.TYPE_JTCONTACT_ONLINE, jtFile);
					}
				}
				if(isNumIn){//群号加群发送名片
					isNumIn = false;
					sendCard();
				}
				if(fromActivityName!=null){//申请进群、详情进群发送名片
					if(fromActivityName.equals("CommumitiesNotificationActivity") || fromActivityName.equals("CommunitiesDetailsActivity") || isNumIn){
						fromActivityName=null;
						sendCard();
					}
				}
				break;
			case EAPIConsts.IMReqType.IM_REQ_SEND_MESSAGE:// 发送消息返回
				MSendMessage sm = (MSendMessage) object;
				if (!sm.isSucceed()) { // 消息发送失败
					// 设置该条消息状态
					IMUtils.setMessageSendType(listMessage, sm.getMessageID(),
							IMBaseMessage.SEND_TYPE_FAIL);
					// 刷新列表
					setData(listMessage);
					return;
				} else { // 消息发送成功
					// 合并消息
					newList = sm.getListMessage();
					IMUtils.mergeListMessage(listMessage, newList);
					adapter.notifyDataSetChanged();
				}
				break;
			case EAPIConsts.IMReqType.IM_REQ_CLIENTDELETEMESSAGE://
				if (object != null) {
					int responseCode = (Integer) object;
					// 删除失败
					if (responseCode == -1) {
						showToast("删除失败");
					} else {
						adapter.notifyDataSetChanged(); // 更新列表
						showToast("记录已删除");
					}
				}
			}

		}
	}

	public void getChatHistory(String mucId, int fromIndex, boolean isBackward) {
		CommunityReqUtil.getMUCMessage(this, this, handler, mucId, fromIndex,
				PAGE_SIZE, isBackward);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.im_muc_detail_create_next:
			Intent intent = new Intent(this, CommunityChatSettingActivity.class);
			intent.putExtra("community", community);
			intent.putExtra("applyType", applyType);
			intent.putExtra(ENavConsts.EMucDetail, mucDetail);
			startActivityForResult(intent, ENavigate.REQUSET_CODE_MUC);
			break;
		case android.R.id.home:
			setResult(RESULT_OK);
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void getMoreMessage(boolean isBackward) {
		int fromIndex = -1; // 消息起始索引
		if (listMessage != null && listMessage.size() > 0) {
			fromIndex = listMessage.get(0).getIndex();
		}
		getChatHistory(thatMucID, fromIndex, isBackward);// 获取群聊聊天记录
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (ENavigate.REQUSET_CODE_MUC == requestCode) {
			if (resultCode == RESULT_FIRST_USER) {
				// 退出群聊成功,继续退出当前群聊页面
				Intent intent = new Intent();
				setResult(RESULT_FIRST_USER, intent);
				finish();
			} else if (resultCode == RESULT_OK) {
				fromIndex = -1;
				listMessage.clear();
				mucDetail = (MUCDetail) data.getExtras().getSerializable(
						ENavConsts.EMucDetail);
				Connections = (ArrayList<Connections>) data.getSerializableExtra(ENavConsts.redatas);
				HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(),
						mucDetail.getTitle(), false, null, true, true);
				adapter.setData(mucDetail, this, community.getId() + "");
				getChatHistory(community.getId() + "", fromIndex, isBackward);// 获取群聊聊天记录
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	//发送子弟的名片
	private void sendCard(){
		JTFile jtFile = new JTFile();
		// 用户
		jtFile.mType = JTFile.TYPE_JTCONTACT_ONLINE;
		jtFile.mTaskId = App.getUserID();

		jtFile.fileName = App.getNick();
		jtFile.mSuffixName = "";
		jtFile.mUrl = App.getUserAvatar();
		jtFile.reserved1 = "";
		sendRichMessage(IMBaseMessage.TYPE_JTCONTACT_ONLINE, jtFile);
	}
}
