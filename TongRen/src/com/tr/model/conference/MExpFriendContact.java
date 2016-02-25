package com.tr.model.conference;

import java.util.List;

import com.tr.model.obj.JTContactMini;
import com.tr.model.user.OrganizationMini;

public class MExpFriendContact {
	public int ch;
	public String nameCh;
	public List<JTContactMini> contactList;
	//add by zhongshan 兼容组织
	public List<OrganizationMini> organizationList;
}
