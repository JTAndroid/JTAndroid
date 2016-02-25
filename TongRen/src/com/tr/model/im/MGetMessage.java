package com.tr.model.im;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.tr.model.obj.IMBaseMessage;

public class MGetMessage implements Serializable{
	
	public MGetMessage(){
		listMessage = new ArrayList<IMBaseMessage>();
	}

	protected boolean isBackward = true; // 是否查找旧聊天记录
	protected List<IMBaseMessage> listMessage; // 消息列表
	protected boolean hasMore = true; // 是否还有消息记录
	
	public boolean isHasMore() {
		return hasMore;
	}
	public void setHasMore(boolean hasMore) {
		this.hasMore = hasMore;
	}
	public boolean isBackward() {
		return isBackward;
	}
	public void setBackward(boolean isBackward) {
		this.isBackward = isBackward;
	}
	public List<IMBaseMessage> getListMessage() {
		return listMessage;
	}
	public void setListMessage(List<IMBaseMessage> listMessage) {
		this.listMessage = listMessage;
	}
}
