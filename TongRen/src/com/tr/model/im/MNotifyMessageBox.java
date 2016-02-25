package com.tr.model.im;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


import android.text.TextUtils;
import android.util.Pair;

/**
 * @author xuxinjian
 * @E-mail: xuxinjian@gintong.com
 * @version 1.0
 * @创建时间：2014-4-11 上午10:59:28
 * @类说明
 */
public class MNotifyMessageBox implements Serializable {

	private static final long serialVersionUID = 198009944230064422L;

	private static MNotifyMessageBox mInstance = null;

	List<MJTPushMessage> listMessage = new ArrayList<MJTPushMessage>();

	public static MNotifyMessageBox getInstance() {
		if (mInstance == null) {
			mInstance = new MNotifyMessageBox();
		}
		return mInstance;
	}

	/**
	 * 获取发送消息的人数
	 * @return
	 */
	public int getSenderCount() {
		List<String> listSenderID = new ArrayList<String>();
		List<String> listMucSenderID = new ArrayList<String>();
		for (MJTPushMessage msg : listMessage) {
			if (msg.getType() == MJTPushMessage.TYPE_CHAT) { // 私聊
				boolean hasExist = false;
				for (String senderID : listSenderID) {
					if (senderID.equalsIgnoreCase(msg.getSendUserID())) {
						hasExist = true;
						break;
					}
				}
				if (!hasExist) {
					listSenderID.add(msg.getSendUserID());
				}
			}
			else if (msg.getType() == MJTPushMessage.TYPE_MUC) { // 群聊
				boolean hasExist = false;
				for (String mucSenderID : listMucSenderID) {
					if (mucSenderID.equalsIgnoreCase(msg.getSendUserID())) {
						hasExist = true;
						break;
					}
				}
				if (!hasExist) {
					listMucSenderID.add(msg.getSendUserID());
				}
			}
		}
		return listSenderID.size() + listMucSenderID.size();
	}

	/**
	 * 获取畅聊数
	 * @return
	 */
	public int getChatCount() {
		List<String> listSenderID = new ArrayList<String>(); // 私聊列表
		List<String> listMucID = new ArrayList<String>(); // 群聊列表
		for (MJTPushMessage msg : listMessage) {
			if (msg.getType() == MJTPushMessage.TYPE_MUC || msg.getType() == MJTPushMessage.TYPE_COMMUNITY) { // 群聊 社群聊天
				boolean hasExist = false;
				for (String mucID : listMucID) {
					if (mucID.equalsIgnoreCase(msg.getMucID())){
						hasExist = true;
						break;
					}
				}
				if (!hasExist) {
					listMucID.add(msg.getMucID());
				}
			} 
			else if (msg.getType() == MJTPushMessage.TYPE_CHAT) { // 私聊
				boolean hasExist = false;
				for (String senderID : listSenderID) {
					if (senderID.equalsIgnoreCase(msg.getSendUserID())) {
						hasExist = true;
						break;
					}
				}
				if (!hasExist) {
					listSenderID.add(msg.getSendUserID());
				}
			}
		}
		return listSenderID.size() + listMucID.size();
	}

	/**
	 * 获取畅聊消息数
	 * @return
	 */
	public int getChatMessageCount() {
		int count = 0;
		for (MJTPushMessage msg : listMessage) {
			if (msg.getType() == MJTPushMessage.TYPE_CHAT
					|| msg.getType() == MJTPushMessage.TYPE_MUC) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 获取会议通知消息数
	 * 
	 * @return
	 */
	public int getConferenceNotificationCount() {
		int count = 0;
		for (MJTPushMessage msg : listMessage) {
			if (msg.getType() == MJTPushMessage.TYPE_CONF_NOTI) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 获取会议畅聊消息数
	 * 
	 * @return
	 */
	public int getConferenceMessageCount() {
		int count = 0;
		for (MJTPushMessage message : listMessage) {
			if (message.getType() == MJTPushMessage.TYPE_CONF) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 获取发送会议消息的人数
	 * 
	 * @return
	 */
	public int getConferenceParticipantCount() {
		List<String> listSender = new ArrayList<String>();
		for (MJTPushMessage msg : listMessage) {
			if (msg.getType() == MJTPushMessage.TYPE_CONF) {
				boolean hasExist = false;
				for (String nowID : listSender) {
					if (nowID.equalsIgnoreCase(msg.getSendUserID())) {
						hasExist = true;
						break;
					}
				}
				if (!hasExist) {
					listSender.add(msg.getSendUserID());
				}
			}
		}
		return listSender.size();
	}

	/**
	 * 获取消息所属的会议数
	 * 
	 * @return
	 */
	public int getConferenceCount() {
		List<String> listSender = new ArrayList<String>();
		for (MJTPushMessage msg : listMessage) {
			if (msg.getType() == MJTPushMessage.TYPE_CONF) {
				boolean hasExist = false;
				for (String nowID : listSender) {
					if (nowID.equalsIgnoreCase(msg.getMucID())) {
						hasExist = true;
						break;
					}
				}
				if (!hasExist) {
					listSender.add(msg.getMucID());
				}
			}
		}
		return listSender.size();
	}

	/**
	 * 获取消息所属的议题数
	 * 
	 * @return
	 */
	public int getConferenceTopicCount() {
		List<String> listSender = new ArrayList<String>();
		for (MJTPushMessage msg : listMessage) {
			if (msg.getType() == MJTPushMessage.TYPE_CONF && !TextUtils.isEmpty(msg.getTopicID())) {
				boolean hasExist = false;
				for (String nowID : listSender) {
					if (nowID.equalsIgnoreCase(msg.getTopicID())) {
						hasExist = true;
						break;
					}
				}
				if (!hasExist) {
					listSender.add(msg.getTopicID());
				}
			}
		}
		return listSender.size();
	}

	public static void setInstance(MNotifyMessageBox box) {
		mInstance = box;
	}

	public List<MJTPushMessage> getListMessage() {
		return listMessage;
	}

	public void setListMessage(List<MJTPushMessage> listMessage) {
		this.listMessage = listMessage;
	}

	public static void clear() {
		getInstance().getListMessage().clear();
	}

	/**
	 * 获取会议的新消息数
	 * 
	 * @return
	 */
	public static HashMap<String, Integer> getMeetingMessageMap() {
		HashMap<String, Integer> notifications = new HashMap<String, Integer>();
		return notifications;
	}

	/**
	 * 获取会议通知数和最新的内容
	 * 
	 * @return
	 */
	public static Pair<Integer, String> getMeetingNotice() {
		Pair<Integer, String> notification = null;
		int count = 0;
		String content = "";
		for (int i = getInstance().getListMessage().size() - 1; i >= 0; i++) {
			if (getInstance().getListMessage().get(i).getType() == MJTPushMessage.TYPE_CONF_NOTI) {
				if (TextUtils.isEmpty(content)) {
					content = getInstance().getListMessage().get(i).getContent();
				}
				count++;
			}
		}
		if (count > 1) {
			notification = new Pair<Integer, String>(count, content);
		}
		return notification;
	}
	
	/**
	 * 清空所有畅聊消息
	 */
	public static void clearMessage() {
		Iterator<MJTPushMessage> iter = getInstance().getListMessage().iterator();
		while (iter.hasNext()) {
			MJTPushMessage msg = iter.next();
			if (msg.getType() == MJTPushMessage.TYPE_CHAT 
					|| msg.getType() == MJTPushMessage.TYPE_MUC) {
				iter.remove();
			}
		}
	}
	
	/**
	 * 清空社群畅聊消息
	 */
	public static void clearCommuNityMessage() {
		Iterator<MJTPushMessage> iter = getInstance().getListMessage().iterator();
		while (iter.hasNext()) {
			MJTPushMessage msg = iter.next();
			if (msg.getType() == MJTPushMessage.TYPE_COMMUNITY) {
				iter.remove();
			}
		}
	}
	
	/**
	 * 清空私聊类型的畅聊消息
	 * @param chatId
	 */
	public static void clearChatMessage(String chatId) {
		Iterator<MJTPushMessage> iter = getInstance().getListMessage().iterator();
		while (iter.hasNext()) {
			MJTPushMessage msg = iter.next();
			if (msg.getType() == MJTPushMessage.TYPE_CHAT 
					&& msg.getSendUserID().equals(chatId)) {
				iter.remove();
			}
		}
	}
	
	/**
	 * 清空群聊类型的畅聊消息
	 * @param mucID
	 */
	public static void clearMUCMessage(String mucID){
		Iterator<MJTPushMessage> iter = getInstance().getListMessage().iterator();
		while (iter.hasNext()) {
			MJTPushMessage msg = iter.next();
			if (msg.getType() == MJTPushMessage.TYPE_MUC 
					&& msg.getMucID().equals(mucID)) {
				iter.remove();
			}
		}
	}

	/**
	 * 清空会议消息
	 */
	public static void clearConferenceMessage() {
		Iterator<MJTPushMessage> iter = getInstance().getListMessage().iterator();
		while (iter.hasNext()) {
			MJTPushMessage msg = iter.next();
			if (msg.getType() == MJTPushMessage.TYPE_CONF) {
				iter.remove();
			}
		}
	}

	/**
	 * 清空会议通知消息
	 */
	public static void clearConferenceNotification() {
		Iterator<MJTPushMessage> iter = getInstance().getListMessage().iterator();
		while (iter.hasNext()) {
			MJTPushMessage msg = iter.next();
			if (msg.getType() == MJTPushMessage.TYPE_CONF_NOTI) {
				iter.remove();
			}
		}
	}
}
