/**
 * 
 */
package com.tr.ui.organization.model.report;

import java.io.Serializable;

/**
 * 研究报告详情
 * @author liubang
 *
 */
public class CustomerReportInfo implements Serializable {
	/**
	 * 
	 */
	public static final long serialVersionUID = -2380416111704299192L;
	public long id;//序号
	public String title;//报告标题
	public String type;//报告类型
	public String ctime;//报告日期
	public String creatorOrg;//报告机构
	public String creator;//报告人
}
