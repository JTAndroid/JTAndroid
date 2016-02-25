/**
 * 
 */
package com.tr.ui.organization.model.resource;

import java.io.Serializable;
import java.util.List;

import com.tr.ui.organization.model.resource.CustomerDemandCommon;

/**
 * 资源需求
 * 
 * @author liubang
 * 
 */
public class CustomerResource implements Serializable {
	/**
	 * 
	 */
	public static final long serialVersionUID = -5546915466590934373L;
	/** 投资需求 */
	public List<CustomerDemandCommon> investmentdemandList;
	/** 融资需求 */
	public List<CustomerDemandCommon> financingdemandList;
	/** 专家需求 */
	public List<CustomerDemandCommon> expertdemandList;
	/** 专家身份 */
	public List<CustomerDemandCommon> expertIdentitydemandList;
	public String remark;// 整体描述
	public String taskId;// 附件组id
	public long id; // 主键/客户id

	@Override
	public String toString() {
		return "CustomerResource [investmentdemandList=" + investmentdemandList
				+ ", financingdemandList=" + financingdemandList
				+ ", expertdemandList=" + expertdemandList
				+ ", expertIdentitydemandList=" + expertIdentitydemandList
				+ ", remark=" + remark + ", taskId=" + taskId + ", id=" + id
				+ "]";
	}

}
