package com.tr.ui.organization.model;

import java.io.Serializable;

public class RelatedOrgContents implements Serializable {

	/**关联组织
	 * "type":"类型 4组织好友 5 客户", "id":"客户id", "name":"客户名称", "ownerId":"创建人id",
	 * "ownerName":"创建人姓名", "address":"地址", "hy":"行业"
	 */
	private static final long serialVersionUID = 1L;
	
	public String type;
	
	public String id;
	
	public String name;
	
	public String ownerId;
	
	public String ownerName;
	
	public String address;
	
	public String hy;

	@Override
	public String toString() {
		return "RelatedOrgContents [type=" + type + ", id=" + id + ", name="
				+ name + ", ownerId=" + ownerId + ", ownerName=" + ownerName
				+ ", address=" + address + ", hy=" + hy + "]";
	}
	
	
	
	
}
