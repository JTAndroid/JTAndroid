/**
 * 
 */
package com.tr.ui.organization.model.finance;

import java.io.Serializable;

/**
 * 利润表明细
 * 
 * @author liubang
 * 
 */
public class CustomerAllEarnings implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8367384530298715935L;
	public String id;//主键
	public String year;// 报告年份
	public String data;// 报告期
	public String stkcd;//证券号
	public String totalIncome;// 营业总收入
	public String operateIncome;// 营业收入
	public String interestIncome;// 利息收入
	public String earnedPremium;// 已赚保费
	public String handlingChargeIncome;// 手续费以及佣金
	public String realEstateIncome;// 房地产销售收入
	public String otherBusIncomed;// 其他业务收入
	public String totalCost;// 营业总成本
	public String businessCost;// 营业成本
	public String accrualExpend;// 利息支出
	public String handlingChargeExpend;// 手续费以及佣金支出
	public String realEstateCost;// 房地产销售成本
	public String researchCost;// 研发费用
	public String surrenderValue;// 退保金
	public String compensateAmount;// 赔付支出净额
	public String insuranceAmount;// 提取保险合同准备金净额
	public String guaranteeCost;// 保单红利支出
	public String cessionCost;// 分保费用
	public String otherBusinessCost;// 其他业务成本
	public String businessTax;// 营业税金及附加
	public String sellCost;// 销售费用
	public String manageCost;// 管理费用
	public String financeCost;// 财务费用
	public String assetLoss;// 资产减值损失
	public String valueChangeIncome;// 公允价值变动收益
	public String investIncome;// 投资收益
	public String chainsInvestIncome;// 其中：对联营企业和合营企业的投资收益
	public String exchangeIncome;// 汇兑收益
	public String futuresProfit;// 期货损益
	public String collocation;// 托管收益
	public String subsidyIncome;// 补贴收入
	public String otherBusIncome;// 其他业务利润
	public String businessIncome;// 营业利润
	public String nonBusinessIncome;// 营业外利润
	public String nonBusinessExpend;// 营业外支出
	public String quickAssetsLoss;// 非流动资产处置损失
	public String totalProfit;// 利润总额
	public String incomeTaxCost;// 所得税费用
	public String unconfirmedInvestCost;// 未确认投资损失
	public String retainedProfits;// 净利润
	public String ownerRetainedProfits;// 归属于母公司所有者的净利润
	public String shareholderLoss;// 少数股东损益
	public String perShareIncome;// 每股收益
	public String basicPerShareIncome;// 基本每股收益
	public String anhPerShareIncome;// 稀释每股收益
	public String otherTotalIncome;// 其他综合收益
	public String totalcomIncome;// 综合收益总额
	public String ownerTotalcomIncome;// 归属于母公司所有者的综合收益总额
	public String shareholderIncome;// 归属于少数股东的综合收益总额


	

}
