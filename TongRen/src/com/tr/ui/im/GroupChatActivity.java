package com.tr.ui.im;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.MenuItem;

import com.tr.App;
import com.tr.R;
import com.tr.api.CommonReqUtil;
import com.tr.api.IMReqUtil;
import com.tr.model.im.FetchFriends;
import com.tr.model.im.IMCacheUtils;
import com.tr.model.im.IMUtil;
import com.tr.model.im.MGetMUCMessage;
import com.tr.model.im.MNotifyMessageBox;
import com.tr.model.im.MSendMessage;
import com.tr.model.obj.ConnectionsMini;
import com.tr.model.obj.IMBaseMessage;
import com.tr.model.obj.JTFile;
import com.tr.model.obj.MUCDetail;
import com.tr.model.obj.MUCMessage;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.utils.common.EUtil;
import com.utils.common.FileUploader;
import com.utils.http.EAPIConsts;
import com.utils.string.StringUtils;

/**
 * 群聊有几个来源，1-从畅聊列表进入， 2-创建新群聊成功， 3-修改群聊返回 ， 不管哪种， 进入群聊页面时，都需要一个mucdetail对象
 * 
 * @ClassName: GroupChatActivity.java
 * @Description: 群聊页面
 * @author xuxinjian
 * @version V 1.0
 * @Date 2014-4-24 上午8:56:49
 */

public class GroupChatActivity extends ChatBaseActivity {

	private final String TAG = getClass().getSimpleName();
	private int eStartIMGroupChatType;
	private boolean isFirstIn = true;
	private boolean isMemberO = false;
//	private boolean isCom;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		isCom = false;
		getParam();
	}

	// 读取传入的参数
	public void getParam() {
		Intent intent = getIntent();
		fromActivityName = intent.getStringExtra(ENavConsts.EFromActivityName);
		mucDetail = (MUCDetail) intent.getSerializableExtra(ENavConsts.EMucDetail);//群详情传递的参数
		thatMucID = intent.getStringExtra(ENavConsts.EMucID);//社交列表传递的参数

		eStartIMGroupChatType = intent.getIntExtra(ENavConsts.EStartIMGroupChatType, 0);
		isMemberO = intent.getBooleanExtra("isMemberO", false);

		if (thatMucID == null && mucDetail == null) {
			finish();
		}
		
		if(mucDetail!=null){
			thatMucID = mucDetail.getId()+"";
			if (!StringUtils.isEmpty(mucDetail.getTitle())) {
				setTitle(mucDetail.getTitle());
			} else {
				setTitle(mucDetail.getSubject());
			}
			getMessageRecord(searchMessagefromIndex+1);
		}else{
			getMucDetail(thatMucID);//节后统一接口
		}
	}
	
	// 通过mucid获取mucDetail
	public void getMucDetail(String mucID) {
		if (!IMReqUtil.getMUCDetail(this, this, null, mucID)) {
			showToast("获取畅聊信息失败");
			finish();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.im_muc_detail_create_next:
			// 从群聊页面切换到聊天详情页
			if (mucDetail != null){
				if (fromActivityName != null
						&& fromActivityName.equalsIgnoreCase(ChatRecordSearchActivity.class.getSimpleName())) {
					ENavigate.startIMEditMumberActivity(this, mucDetail, null, ENavigate.REQUSET_CODE_MUC, true,
							eStartIMGroupChatType, isMemberO);
				} else {
//					if (isCom) {
//						return true;
//					}
					ENavigate.startIMEditMumberActivity(this, mucDetail, null, ENavigate.REQUSET_CODE_MUC, false,
							eStartIMGroupChatType, isMemberO);
				}
			}
			else {
				// 如果之前获取群聊详情失败，则重新获取一次
				getMucDetail(thatMucID);
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (ENavConsts.ActivityReqCode.REQUEST_CODE_FOR_AT_FRIENDS == requestCode) {
			if (data != null) {
				resultCode = RESULT_OK;
			}
		}
		// 可以根据多个请求代码来作相应的操作 从选择联系人页面返回群聊设置页
		if (ENavigate.REQUSET_CODE_MUC == requestCode) {
			if (resultCode == RESULT_OK) {
				// 创建群聊成功后返回
				mucDetail = (MUCDetail) data.getSerializableExtra(ENavConsts.EMucDetail);
				updateMucDetail();

				// 检查是否清空聊天记录
				String checkCleanHistory = (String) data.getSerializableExtra(ENavConsts.EIMNotifyCleanHistory);
				if (checkCleanHistory != null) {
					// 已清空，则清除本地列表
					cleanHistory();
				}
			} else if (resultCode == RESULT_FIRST_USER) {
				// 退出群聊成功,继续退出当前群聊页面
				finish();
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	protected void back() {
		finish();
	}

	/**
	 * 发送文本消息
	 * 
	 * @param text
	 */
	@Override
	public void sendMessage(String text) {
		sendMucMessage(text);
	}

	// 发送其它消息（知识、需求、图片、视频等）
	@Override
	public void sendRichMessage(int type, final JTFile jtFile) {

		// 消息内容错误
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
			sendMucMessage(jtFile.mFileName);
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
		} else if (type == IMBaseMessage.TYPE_REQUIREMENT) {
			msg.setContent(jtFile.mSuffixName);
			if (TextUtils.isEmpty(msg.getContent())) {
//				msg.setContent("[需求]");
				msg.setContent("分享了[需求]");
			}
			IMReqUtil.sendMessage(this, this, mNetHandler, msg);
			return;
		} else if (type == IMBaseMessage.TYPE_JTCONTACT_OFFLINE || type == IMBaseMessage.TYPE_JTCONTACT_ONLINE
				|| type == IMBaseMessage.TYPE_ORG_OFFLINE || type == IMBaseMessage.TYPE_ORG_ONLINE) {
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
			if (msg != null && msg.getJtFile() != null && !StringUtils.isEmpty(msg.getJtFile().mFileName)) {
				cardName = msg.getJtFile().mFileName;
			}
//			msg.setContent(cardName);
			msg.setContent("分享了[组织]");
			if (StringUtils.isEmpty(msg.getJtFile().mTaskId)) {
				msg.getJtFile().mTaskId = msg.getJtFile().getId();
			}
			IMReqUtil.sendMessage(this, this, mNetHandler, msg);
			return;
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
		} else if (type == IMBaseMessage.TYPE_IMAGE) { // 图片

			if (!TextUtils.isEmpty(jtFile.mUrl)) { // 图片已经上传，直接转发
				msg.setContent("[图片]");
				jtFile.mType = JTFile.TYPE_IMAGE;
				IMReqUtil.sendMessage(this, this, mNetHandler, msg);
				return;
			}
			if (TextUtils.isEmpty(jtFile.mLocalFilePath) || !new File(jtFile.mLocalFilePath).exists()) {
				// 更改界面和数据库状态
				msg.setSendType(IMBaseMessage.SEND_TYPE_FAIL);
				setData(listMessage);
				showToast("无法解析图片地址或图片文件已删除");
				return;
			}
			// 获取图片缩略图
			String thumbnailPath = EUtil.getChatImageThumbnail(this, thatMucID, jtFile.mLocalFilePath);
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
			if (!TextUtils.isEmpty(jtFile.mUrl)) {
				IMReqUtil.sendMessage(this, this, mNetHandler, msg);
			} else {
				startNewUploadTask(msg);
			}
		} else if (type == IMBaseMessage.TYPE_AUDIO) { // 语音
			msg.setContent("[语音]");
			jtFile.mType = JTFile.TYPE_AUDIO;
			if (!TextUtils.isEmpty(jtFile.mUrl)) {
				IMReqUtil.sendMessage(this, this, mNetHandler, msg);
			} else {
				startNewUploadTask(msg);
			}
		} else if (type == IMBaseMessage.TYPE_VIDEO) { // 视频

			if (!TextUtils.isEmpty(jtFile.mUrl)) { // 视频已经上传，直接转发
				msg.setContent("[视频]");
				jtFile.mType = JTFile.TYPE_VIDEO;
				IMReqUtil.sendMessage(this, this, mNetHandler, msg);
				return;
			}
			// 发送视频缩略图
			if (TextUtils.isEmpty(jtFile.mLocalFilePath) || !new File(jtFile.mLocalFilePath).exists()) {
				// 更改界面和数据库状态
				msg.setSendType(IMBaseMessage.SEND_TYPE_FAIL);
				setData(listMessage);
				showToast("无法解析视频地址或视频文件已删除");
				return;
			}
			// 获取视频截图
			String screenshotPath = EUtil.getChatVideoScreenshot(this, thatMucID, jtFile.mLocalFilePath);
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
//				msg.setContent("[会议]");
				msg.setContent("分享了[会议]");
			}
			IMReqUtil.sendMessage(this, this, mNetHandler, msg);
		}
	}

	/** 加载更多数据 */
	@Override
	public void getMoreMessage(boolean isBackward) {
		int fromIndex = -1; // 消息起始索引
		if (listMessage != null && listMessage.size() > 0) {
			fromIndex = listMessage.get(0).getIndex();
		}
		IMReqUtil.getMUCMessage(this, this, mNetHandler, thatMucID, fromIndex, MAX_MESSAGE_SIZE, isBackward);
	}
	
	@Override
	public void getMessageRecord(int fromIndex){
		IMReqUtil.getMUCMessage(this, this, mNetHandler, thatMucID, fromIndex, MAX_MESSAGE_SIZE, false);
	}

	/**
	 * 发送文本消息
	 * 
	 * @param text
	 */
	public void sendMucMessage(String text) {
		IMBaseMessage msg = new MUCMessage(text);
		msg.setSendType(IMBaseMessage.SEND_TYPE_SENDING);
		msg.setSenderID(App.getUserID());
		msg.setJtFile(null);
		msg.setType(IMBaseMessage.TYPE_TEXT);
		msg.setRecvID("" + thatMucID);
		msg.setTime(getLatestTime());
		msg.setIndex(getLatestMessageIndex());
		msg.setContent(text.trim());
		msg.setSenderName(App.getNick());
		msg.setAtIds(atConnectionsIds);
		// 将发送中的消息添加到聊天列表中
		listMessage.add(msg);
		setData(listMessage);
		// 定位到最新的消息
		locateToLatestMessage();
		// 发送消息请求
		IMReqUtil.sendMessage(this, this, strangerHandler, msg);
	}

	@Override
	public void bindData(int tag, Object object) {
		if (hasDestroy()) {
			return;
		}
		if (tag == EAPIConsts.IMReqType.IM_REQ_GET_MUC_DETAIL) {
			if (object != null) { // 获取群聊详情返回成功
				mucDetail = (MUCDetail) object;
				updateMucDetail();
				startMergeAndSaveMessage(this, null, null, TASK_READ);
//				IMReqUtil.getMUCMessage(this, this, mNetHandler, thatMucID, -1, MAX_MESSAGE_SIZE, true);//查询最新20条记录
				cleanNotifyBox(thatMucID);
			}
		}else if (tag == EAPIConsts.IMReqType.IM_REQ_GET_MUC_MESSAGE) { // 获取群聊聊天记录
//			isCom = true;
			dismissLoadingDialog();
			// 获取消息列表
			MGetMUCMessage gcm = (MGetMUCMessage) object;
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

		}else if (tag == EAPIConsts.IMReqType.IM_REQ_SEND_MESSAGE) { // 发送消息返回
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
		}else {
			super.bindData(tag, object);
		}
	}

	/**
	 * 获取群聊成员数量
	 * 
	 * @return
	 */
	private String getMemberCount() {
		if (chatDetail != null) {
			return "";
		} else {
			return "(" + mucDetail.getListConnectionsMini().size() + ")";
		}
	}

	/** 更新聊天信息，修改了聊天信息返回，或者第一次获取等修改mucDetail对象的行为后，都调用该函数 */
	public void updateMucDetail() {
		if (mucDetail == null) {
			return;
		}
		if (!StringUtils.isEmpty(mucDetail.getTitle())) {
			setTitle(mucDetail.getTitle());
		} else {
			setTitle(mucDetail.getSubject());
		}
	}

	/**
	 * 获取群聊成员的昵称
	 */
	public String getNickNameByMessage(IMBaseMessage msg) {
		try {
			ConnectionsMini connsMini = mucDetail.getConnectionsMiniByUserId(msg.getSenderID());
			if (connsMini!=null) {
				if (StringUtils.isEmpty(connsMini.getShortName())) {
					return connsMini.getName();
				}
			}
			return connsMini.getShortName();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取群聊成员的头像
	 */
	public String getImageByMessage(IMBaseMessage msg) {
		try {
			if (!TextUtils.isEmpty(msg.getSenderID())) {
				ConnectionsMini connsMini = mucDetail.getConnectionsMiniByUserId(msg.getSenderID());
				return connsMini.getImage();
			}
			return "";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/** 清空消息盒子 */
	@Override
	public void cleanNotifyBox(String id) {
		MNotifyMessageBox.clearMUCMessage(id);
	}

	/**
	 * 获取消息发送人类型
	 */
	@Override
	public int getSenderTypeByMessage(IMBaseMessage msg) {
		try {
			ConnectionsMini connsMini = mucDetail.getConnectionsMiniByUserId(msg.getSenderID());
			return connsMini.getType();
		} catch (Exception e) {
			return 0;
		}
	}
	
	/**
	 * 获取聊天Id
	 * 
	 * @return
	 */
	@Override
	public String getChatId() {
		String id = "";
		if (mucDetail != null) {
			id = mucDetail.getId() + "";
		} else if (!TextUtils.isEmpty(thatMucID)) {
			id = thatMucID;
		}
		return id;
	}
	
//	@Override
//	public void onResume() {
//		super.onResume();
//		if(!isFirstIn){
//			IMReqUtil.getMUCMessage(this, this, mNetHandler, thatMucID, -1, MAX_MESSAGE_SIZE, true);//查询最新20条记录
//		}
//		isFirstIn = false;
//	}

}
