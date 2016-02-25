/**
 * 
 */
package com.tr.ui.organization.model.finance;

import java.io.Serializable;
import java.util.List;


import com.tr.ui.organization.model.finance.CustomerBalance;
import com.tr.ui.organization.model.finance.CustomerEarnings;
import com.tr.ui.organization.model.finance.CustomerFlows;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;

/**
 *财务分析
 * @author liubang
 *
 */
public class CustomerFinance implements Serializable {
	/**
	 * 
	 */
	public static final long serialVersionUID = -3341198856314735283L;
	public long id;//主键  客户id
	public String taskId;//附件组
	
	//利润表摘要
	public List<CustomerEarnings> earningsList;
	//资产负债表摘要
	public List<CustomerBalance> balanceList;
	//现金流量表摘要
	public List<CustomerFlows> flowsList;
	
	public List<CustomerPersonalLine> personalLineList;// 自定义属性
	
}
