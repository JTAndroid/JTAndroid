package com.tr.ui.conference.initiatorhy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.tr.model.conference.MAlbumBucket;
import com.tr.model.conference.MCalendarSelectDateTime;
import com.tr.model.conference.MExpFriendContact;
import com.tr.model.conference.MIntroduce;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.obj.Connections;
import com.tr.model.obj.JTContactMini;
import com.tr.model.obj.RequirementMini;
import com.tr.model.user.OrganizationMini;
import com.utils.time.Util;

public class InitiatorDataCache {
	private static InitiatorDataCache instance;

	// public InitiatorDataCache (){
	// }
	public static InitiatorDataCache getInstance() {
		if (instance == null) {
			instance = new InitiatorDataCache();
		}
		return instance;
	}

	public List<Connections> connectionsList;

	public List<MExpFriendContact> friendList;
	/**参会人集合*/
	public LinkedHashMap<String, JTContactMini> inviteAttendSelectedMap = new LinkedHashMap<String, JTContactMini>();
	
	/**主讲人集合*/
	public Map<String, JTContactMini> inviteSpeakerSelectedMap = new TreeMap<String, JTContactMini>();
	public Map<String, JTContactMini> costomFriselectedMap = new HashMap<String, JTContactMini>();

	public List<MExpFriendContact> sharePeoplehubList;
	public Map<String, JTContactMini> sharePeopleHubSelectedMap = new HashMap<String, JTContactMini>();
	
	public List<MExpFriendContact> shareOrghubList;
	public Map<String, OrganizationMini> shareOrgHubSelectedMap = new HashMap<String, OrganizationMini>();

	public List<RequirementMini> shareDemandList = new ArrayList<RequirementMini>();
	public Map<Integer, RequirementMini> shareDemandSelectedMap = new HashMap<Integer, RequirementMini>();

	public List<KnowledgeMini2> shareKnowleadgeList = new ArrayList<KnowledgeMini2>();
	public Map<Long, KnowledgeMini2> shareKnowleadgeSelectedMap = new HashMap<Long, KnowledgeMini2>();

	public ArrayList<MCalendarSelectDateTime> timeSelectetedList = new ArrayList<MCalendarSelectDateTime>();
	public ArrayList<MCalendarSelectDateTime> dateSelectetedTempList = new ArrayList<MCalendarSelectDateTime>();
	
	public List<MAlbumBucket> albumBucketList = new ArrayList<MAlbumBucket>();
	
	public MIntroduce introduce = new MIntroduce();

	/**	是否修改会议	*/
	public boolean isAlterHY = false;

	public long isAlterHyId;
	/**会议上传附件任务id*/
	public String taskId;
	
	public boolean onlyShowMyOrg = false;
	
	/*转发分享组织*/
	public LinkedHashMap<String, OrganizationMini>  forwardingAndSharingOrgMap= new LinkedHashMap<String, OrganizationMini>();
	/*好友全选*/
	public boolean friendCheckAll = false;
	/*组织全选*/
	public boolean  friendOrgCheckAll = false;
	
	public void releaseAll() {
		isAlterHY = false;
		onlyShowMyOrg = false;
		isAlterHyId = 0;
		taskId = "";
		if (!Util.isNull(friendList)) {
			friendList.clear();
		}

		if (!Util.isNull(inviteAttendSelectedMap)) {
			inviteAttendSelectedMap.clear();
		}
		if (!Util.isNull(inviteSpeakerSelectedMap)) {
			inviteSpeakerSelectedMap.clear();
		}
		if(!Util.isNull(costomFriselectedMap)){
		    costomFriselectedMap.clear();
		}
		if (!Util.isNull(sharePeoplehubList)) {
			sharePeoplehubList.clear();
		}
		if (!Util.isNull(shareOrghubList)) {
			shareOrghubList.clear();
		}
		if (!Util.isNull(sharePeopleHubSelectedMap)) {
			sharePeopleHubSelectedMap.clear();
		}
		if (!Util.isNull(shareOrgHubSelectedMap)) {
			shareOrgHubSelectedMap.clear();
		}

		if (!Util.isNull(shareDemandList)) {
			shareDemandList.clear();
		}
		if (!Util.isNull(shareDemandSelectedMap)) {
			shareDemandSelectedMap.clear();
		}

		if (!Util.isNull(shareKnowleadgeList)) {
			shareKnowleadgeList.clear();
		}
		if (!Util.isNull(shareKnowleadgeSelectedMap)) {
			shareKnowleadgeSelectedMap.clear();
		}
		if (!Util.isNull(timeSelectetedList)) {
			timeSelectetedList.clear();
		}
		if (!Util.isNull(dateSelectetedTempList)) {
			dateSelectetedTempList.clear();
		}

		if (!Util.isNull(connectionsList)) {
			connectionsList.clear();
		}
		if (!Util.isNull(introduce.photoList)) {
			introduce.photoList.clear();
		}
		
		if (!Util.isNull(albumBucketList)) {
			albumBucketList.clear();
		}
		
		introduce.contentText = "";
	}
}
