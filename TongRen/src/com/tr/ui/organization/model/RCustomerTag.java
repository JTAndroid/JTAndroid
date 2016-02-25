package com.tr.ui.organization.model;

import java.io.Serializable;

/**
 * 组织标签关系
 * @author hdy
 * @date 2015-1-4
 */
public class RCustomerTag implements Serializable  {
	public static final long serialVersionUID = -4649009967999646047L;
	public long id;
	/**
	 * mongo客户id
	 */
	public long customerId;
	/**
	 * 标签id
	 */
	public long tagId;
	/**
	 * 标签名称
	 */
	public String tagName;
	/**
	 * 创建者id
	 */
	public long creatorId;

}
