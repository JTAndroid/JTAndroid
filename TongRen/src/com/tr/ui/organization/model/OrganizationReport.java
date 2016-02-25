package com.tr.ui.organization.model;

import java.io.Serializable;

public class OrganizationReport implements Serializable {
	/**
	 * {
     "reason":"举报原因",
     "content":"举报内容",
     "customerId":"组织客户Id，long类型"
 }
	 */
	public String reason;
	public String content;
	public Long customerId;
}
