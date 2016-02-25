package com.tr.ui.conference.square;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MRoadShowCacheFiles {

	private static Map<String, MRoadShowFile> mRoadViewFiles = new HashMap<String, MRoadShowFile>();
	private static MRoadShowCacheFiles instance = new MRoadShowCacheFiles();
	private String key;

	public void release(String Tag) {
		mRoadViewFiles.remove(Tag);
	}

	// /**
	// * 清空发起会议中 议题对应的路演文件的缓存
	// */
	// public void releaseCreateHyAllCacheTopics() {
	// for (int groupPosition = 0; groupPosition < 100; groupPosition++) {
	// for (int childPosition = 0; childPosition < 100; childPosition++) {
	// key = (groupPosition + 1) * 1000 + childPosition;
	// if (mRoadViewFiles.containsKey(key)) {
	// mRoadViewFiles.remove(key);
	// }
	// }
	// }
	// }

	/**
	 * 删除路演文件缓存,主讲人Id+位置
	 */
	public void releaseTopicDemo(String topicSpeakerId, int childPosition) {
		String locationId = topicSpeakerId + childPosition;
		mRoadViewFiles.remove(locationId);
	}

	/**
	 * 清空所有路演缓存
	 */
	public void releaseAll() {
		mRoadViewFiles.clear();
	}

	public static MRoadShowCacheFiles getInstance() {
		return instance;
	}

	/**
	 * 返回路演文件的实例对象
	 * 
	 * @return
	 */
	public MRoadShowFile getMRoadViewFile(String Tag) {
		return mRoadViewFiles.get(Tag);
	}

	// 加入缓存
	public void setMRoadViewFileCache(MRoadShowFile file, String Tag) {
		mRoadViewFiles.put(Tag, file);
	}

}
