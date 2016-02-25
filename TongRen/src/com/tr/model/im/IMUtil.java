package com.tr.model.im; 

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.tr.App;
import com.tr.db.ChatRecordDBManager;
import com.tr.model.obj.Connections;
import com.tr.model.obj.IMBaseMessage;
import com.utils.pinyin.PingYinUtil;
import com.utils.pinyin.PinyinComparator;
import com.utils.string.StringUtils;

/** 
 * @author xuxinjian
 * @E-mail: xuxinjian@gintong.com
 * @version 1.0
 * @创建时间：2014-4-22 下午1:41:02 
 * @类说明 处理im的 一些工具类
 */
public class IMUtil {
	
	/**
	 * 将listMsg中的指定messageID的消息设置为发送失败
	 * @param listMsg
	 * @param msgId
	 */
	/*
	public static void setMessageSendFail(List<IMBaseMessage> listMsg, String msgId){
		if(listMsg == null || listMsg.size() <= 0){
			return;
		}
		if(TextUtils.isEmpty(msgId)){
			return;
		}
		for(int i = listMsg.size() - 1; i >= 0; i--){
			IMBaseMessage msg = listMsg.get(i);
			if(msg.getMessageID().equalsIgnoreCase(msgId)){
				msg.setSendType(IMBaseMessage.SEND_TYPE_FAIL);
				break;
			}
		}
	}
	*/
	
	/**
	 * 将listMsg中的指定messageID的消息设置为发送成功
	 * @param listMsg
	 * @param msgId
	 */
	/*
	public static void setMessageSendSent(List<IMBaseMessage> listMsg, String msgId){
		if(listMsg == null || listMsg.size() <= 0){
			return;
		}
		if(TextUtils.isEmpty(msgId)){
			return;
		}
		for(int i = listMsg.size() - 1; i >= 0; i--){
			IMBaseMessage msg = listMsg.get(i);
			if(msg.getMessageID().equalsIgnoreCase(msgId)){
				msg.setSendType(IMBaseMessage.SEND_TYPE_SENT);
				break;
			}
		}
	}
	*/
	
	/**
	 * 设置消息发送状态（包括数据库的修改）
	 * @param context
	 * @param listMsg
	 * @param chatId
	 * @param msgId
	 * @param sendState
	 */
	public static void setMessageSendType(Context context, List<IMBaseMessage> listMsg, String chatId, String msgId, int sendType){
		if(listMsg == null || listMsg.size() <= 0
				|| TextUtils.isEmpty(chatId)
				|| TextUtils.isEmpty(msgId)
				|| context == null){
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
		// 更改数据库状态
		ChatRecordDBManager chatManager = new ChatRecordDBManager(context);
		chatManager.setMessageSendType(App.getUserID(), chatId, msgId, sendType);
	}
	
	/**
	 * time1 > time2 ->return >1; time1=time2 >0;time1<time2 >-1
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static int compareTime(String time1, String time2){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date1 = sdf.parse(time1);
			Date date2 = sdf.parse(time2);
			long ret = (date1.getTime() - date2.getTime());
			if(ret > 0)
				return 1;
			else if(ret < 0)
				return -1;
			else
				return 0;
			
		}catch(Exception e)
		{
		}
		return 0;
	}
	
	/**
	 * 将新获取的消息list合并到本地list中，主要根据messageId来合并，合并后数据在oldlist中返回
	 * @param oldList
	 * @param newList
	 */
	public static List<IMBaseMessage> mergeListMessage(Context context, List<IMBaseMessage> oldList, List<IMBaseMessage> newList){
		
		// 数据是否合法
		if(oldList.size() == 0){
			return newList;
		}
		if(newList.size() == 0){
			return oldList;
		}
		// 合并，排重
		for(int i = 0; i < newList.size(); i++){
			IMBaseMessage newItem = newList.get(i);
			// 系统消息，直接添加到队尾
//			if(newItem.getSenderID().equalsIgnoreCase("0")){ 
//				oldList.add(newItem); 
//				continue; // break;
//			}
			// 消息是否可见（匹配本地数据库）
			ChatRecordDBManager dbManager = new ChatRecordDBManager(context);
			if(dbManager.queryExistence(App.getUserID(), newItem.getRecvID(), newItem.getMessageID())
					&& dbManager.isHide(App.getUserID(), newItem.getRecvID(), newItem.getMessageID())){ 
				continue;
			}
			/*
			int oldSize = oldList.size();
			boolean blnFindInOld = false;
			for(int j = 0; j < oldSize; j++){
				IMBaseMessage oldItem = oldList.get(j);
				if(oldItem.getMessageID().equalsIgnoreCase(newItem.getMessageID())){ // 如果在老的中间找到了新消息相同的messageId，则用新的取代老的
					oldList.set(j, newItem);
					blnFindInOld = true;
					break;
				}
			}
			if(!blnFindInOld){
				oldList.add(newItem);
			}
			*/
			int oldSize = oldList.size();
			boolean blnFindInOld = false;
			for(int j = oldSize - 1; j >= 0; j--){
				IMBaseMessage oldItem = oldList.get(j);
				if(!StringUtils.isEmpty(newItem.getMessageID()) && oldItem.getMessageID().equalsIgnoreCase(newItem.getMessageID())){ // 如果在老的中间找到了新消息相同的messageId，则用新的取代老的
					oldList.set(j, newItem);
					blnFindInOld = true;
					break;
				}
				/*
				 * 暂时去掉
				if(oldItem.getIndex() < newItem.getIndex() 
						&& (oldItem.getSendType() == IMBaseMessage.SEND_TYPE_SENT || oldItem.getSendType() == IMBaseMessage.SEND_TYPE_PUSH)
						&& !newItem.getSenderID().equals(App.getUserID())){
					break;
				}
				*/
			}
			if(!blnFindInOld && !StringUtils.isEmpty(newItem.getMessageID())){
				oldList.add(newItem);
			}
		}
		// 排序
		Comparator<IMBaseMessage> comparator = new IMIndexComparator();
		Collections.sort(oldList, comparator);
		return oldList;
	}
	
	/**
	 * 自定义排序类（按索引）
	 * @author leon
	 */
	public static class IMIndexComparator implements Comparator<IMBaseMessage>{
		
		@Override
		public int compare(IMBaseMessage lhs, IMBaseMessage rhs) {
			return lhs.getIndex() - rhs.getIndex();
		}
	}
	
	/**
	 * 自定义排序类（按时间）
	 * @author leon
	 */
	public static class IMTimeComparator implements Comparator<IMBaseMessage>{
		
		@Override
		public int compare(IMBaseMessage lhs, IMBaseMessage rhs) {
			Date d1 = lhs.getDateTime();
			Date d2 = rhs.getDateTime();
			return d1.compareTo(d2);
		}
	}
}
 