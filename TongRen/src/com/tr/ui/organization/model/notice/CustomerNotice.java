package com.tr.ui.organization.model.notice;

import java.io.Serializable;
import java.util.Date;

/**
 * 最新公告
 * 
 * @author hdy
 * @date 2015-1-8
 */
public class CustomerNotice implements Serializable {
	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;
	/**
	 * id
	 */
	public long id;
	/**
	 * 知识id
	 */
	public long kownledgeId;
	/**
	 * 知识类型
	 */
	public int type;

	/**
	 * 标题
	 */
	public String title;
	/**
	 * 证券代码号
	 */
	public String stkcd;
	/**
	 * 创建日期
	 */
//	public Date createDate;
	@Override
	public String toString() {
		return "CustomerNotice [id=" + id + ", kownledgeId=" + kownledgeId
				+ ", type=" + type + ", title=" + title + ", stkcd=" + stkcd
				+ "]";
	}

	
}
