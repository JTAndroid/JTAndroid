package com.tr.model.conference;

import com.tr.ui.conference.square.MRoadShowFile;

public class MSpeakerTopic {
	public String topicTitle;// 议题
	public String topicIntroduce;// 介绍
	public MCalendarSelectDateTime topicTime;// 时间
	public MMediaVideo topicVideo;// 视频文件
	public MRoadShowFile topicDemo;// 路演文件
	public String topicTaskId = "nimabide";
	public boolean isAlterMeeting;
	public MMeetingTopicQuery alterMeetingTopicQuery;
}
