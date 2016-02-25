package com.tr.ui.communities.model;

import java.io.Serializable;

public class CommunityKickFromForBatch implements Serializable {
	
	/**
	 *    "userId": 12,
                 "type": 1,
                 "tipMessage": "此用户不在该社群中",
                "code": "0002"
	 */
	public String userId;
	public String type;
	public String tipMessage;
	public String code;
	
}
