package com.tr.ui.im;

import com.tr.model.obj.MUCDetail;

/** @author SunJianan */
public class IMChatMessageCache {

	// 群聊详情
	private static MUCDetail mucDetailCache;
	private static IMChatMessageCache cache;

	private IMChatMessageCache() {
	}

	public static IMChatMessageCache getInstance() {
		if (cache == null) {
			cache = new IMChatMessageCache();
		}
		return cache;
	}

	public MUCDetail getMucDetailCache() {
		return mucDetailCache;
	}

	public void setMucDetailCache(MUCDetail mucDetailCache) {
		IMChatMessageCache.mucDetailCache = mucDetailCache;
	}
	
	/**
	 * 释放缓存
	 */
	public void releaseCache() {
		cache = null;
		mucDetailCache = null;
	}

}
