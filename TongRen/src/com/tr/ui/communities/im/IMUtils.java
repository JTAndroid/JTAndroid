package com.tr.ui.communities.im;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import com.tr.model.im.IMUtil.IMIndexComparator;
import com.tr.model.obj.IMBaseMessage;
import com.utils.string.StringUtils;

public class IMUtils {
	
	/**
	 * 设置消息发送状态
	 * @param listMsg
	 * @param msgId
	 * @param sendState
	 */
	public static void setMessageSendType(List<IMBaseMessage> listMsg, String msgId, int sendType){
		if(listMsg == null || listMsg.size() <= 0
				|| TextUtils.isEmpty(msgId)){
			return;
		}
		// 更改列表状态
		for(int i = listMsg.size() - 1; i >= 0; i--){
			IMBaseMessage msg = listMsg.get(i);
			if(msg.getMessageID().equalsIgnoreCase(msgId)){
				msg.setSendType(sendType);
				break;
			}
		}
	}
	
	/**
	 * 将新获取的消息list合并到本地list中，主要根据messageId来合并，合并后数据在oldlist中返回
	 * @param oldList
	 * @param newList
	 */
	public static void mergeListMessage(List<IMBaseMessage> oldList, List<IMBaseMessage> newList){
		// 数据是否合法
		if(oldList.size() == 0){
			return;
		}
		if(newList.size() == 0){
			return;
		}
		// 合并，排重
		for(int i = 0; i < newList.size(); i++){
			IMBaseMessage newItem = newList.get(i);
			int oldSize = oldList.size();
			boolean blnFindInOld = false;
			for(int j = oldSize - 1; j >= 0; j--){
				IMBaseMessage oldItem = oldList.get(j);
				if(!StringUtils.isEmpty(newItem.getMessageID()) && oldItem.getMessageID().equalsIgnoreCase(newItem.getMessageID())){ // 如果在老的中间找到了新消息相同的messageId，则用新的取代老的
					oldList.set(j, newItem);
					blnFindInOld = true;
					break;
				}
			}
			if(!blnFindInOld && !StringUtils.isEmpty(newItem.getMessageID())){
				oldList.add(newItem);
			}
		}
		// 排序
		Comparator<IMBaseMessage> comparator = new IMIndexComparator();
		Collections.sort(oldList, comparator);
	}
}
