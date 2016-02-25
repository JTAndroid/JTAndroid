package com.tr.ui.tongren.model.message;

import java.io.Serializable;

public class TongRenMessageOperate implements Serializable{
	/**
	 * status 类型 1（同意） 2（忽略）
messageReceiveId 接收消息ID
	 */
	public String messageReceiveId;
	public String status;
}
