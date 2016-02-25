/**
 * 
 */
package com.tr.ui.organization.model.report;

import java.io.Serializable;
import java.util.List;


import com.tr.ui.organization.model.profile.CustomerPersonalLine;
import com.tr.ui.organization.model.report.CustomerReportInfo;

/**
 * 研究报告
 * @author liubang
 *
 */
public class CustomerReport implements Serializable {
	/**
	 * 
	 */
	public static final long serialVersionUID = -3735444541056195932L;
	public long id;//主键/客户id
	public List<CustomerReportInfo> infoList;//研究报告详情list
	public String taskId;//附件组id
	public List<CustomerPersonalLine> personalLineList;// 自定义属性
	@Override
	public String toString() {
		return "CustomerReport [id=" + id + ", infoList=" + infoList
				+ ", taskId=" + taskId + ", personalLineList="
				+ personalLineList + "]";
	}
	
}	
