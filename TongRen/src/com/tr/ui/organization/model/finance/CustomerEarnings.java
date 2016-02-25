/**
 * 
 */
package com.tr.ui.organization.model.finance;

import java.io.Serializable;

/**
 * 利润表摘要构成
 * @author liubang
 *
 */
public class CustomerEarnings implements Serializable {
	public static final long serialVersionUID = 7884557036412962364L;
	public String data;//报告期
	public String totalProfit;//利润总额
	public String operateProfit;//营业利润
	public String netProfit;//净利润
	public String otherProfit;//其他利润
	public String investProfit;//投资收益
	public String tax;//所得税
	public String financeCosts;//财务费用
	public String otherIncome;//营业外支出
	
	
	
}
