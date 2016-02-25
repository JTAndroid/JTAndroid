package com.tr.ui.communities.model;

import java.io.Serializable;
import java.util.List;

public class CommunityKickFromForBatchRequest implements Serializable {
	
	/**
	 *   List<Long> userIds, String operatorUserId, Long mucId
	 */
	public  List<Long> userIds;
	public String operatorUserId;
	public  Long mucId;
	
}
