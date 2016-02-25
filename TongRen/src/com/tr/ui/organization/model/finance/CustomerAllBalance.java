package com.tr.ui.organization.model.finance;

import java.io.Serializable;

/**
 * 资产负债表明细
 * @author wangfeiliang
 *
 */
public class CustomerAllBalance implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2254338047529667997L;
	public String id;//主键
	public String year;// 报告年份
	public String data;// 报告期
	public String stkcd;//证券号
	public String moneyFund;//货币资金
	public String settleProvisions;//结算备付金
	public String rollOutFund;//拆出资金
	public String jyMoneyAsset;//交易性金额资产
	public String deriveMoneyAsset;//衍生金额资产
	public String receiveBill;//应收票据
	public String receiveCredit;//应收账款
	public String prepayFunds;//预付款项
	public String receivePremium;//应收保费
	public String receiveCessionCredit;//应收分保账款
	public String receiveCessionPact;//应收分保合同准备金
	public String receiveInterest;//应收利息
	public String receiveDividend;//应收股利
	public String otherReceiveMoney;//其他应收款
	public String receiveExitTax;//应收出口退税
	public String receiveSubsidy;//应收补贴款
	public String receiveMargin;//应收保证金
	public String receiveInside;//内部应收款
	public String buyReturnMoney;//买入返售金额资产
	public String stock;//存货
	public String PrepaidMoney;//待摊费用
	public String PendingAsset;//待处理流动资产损益
	public String nonCurrentAsset;//一年内到期的非流动资产
	public String otherCurrentAsset;//其他流动资产
	public String totalCurrentAsset;//流动资产合计
	
	public String grantLoan;//发放贷款及垫款
	public String avaiSellAsset;//可供出售金额资产
	public String holdExpire;//持有自到期投资
	public String longAccount;//长期应收款
	public String longStockInvest;//长期股权投资
	public String otherLongInvest;//其他长期投资
	public String investRealty;//投资性房地产
	public String fixAssetOriginal;//固定资产原值
	public String grandDepre;//累计折旧
	public String fixAssetNet;//固定资产净值
	public String fixAssetDevalue;//固定资产减值准备
	public String fixAssetNetAmount;//固定资产净额
	public String inTheProject;//在建工程
	public String projectGoods;//工程物资 
	public String fixAssetClean;//固定资产清理
	public String produceAsset;//生产性生物资产
	public String benefitAsset;//公益性生物资产
	public String gasAsset;//油气资产
	public String invisibleAsset;//无形资产
	public String developExpend;//开发支出
	public String goodwill;//商誉
	public String longUnapplied;//长期待摊费用
	public String stockBranch;//股权分置流通权
	public String deferTaxAsset;//递延所得税资产
	public String otherFixAsset;//其他非流动资产
	public String totalFixAsset;//非流动资产合计
	public String totalAsset;//资产总计
	
	
	public String shortLoan;// 短期借款
	public String centralBankLoan;//向中央银行借款
	public String absorbDeposit;//吸收存款及同业存放
	public String tearEnterMoney;//拆入资金
	public String dealMoneyDebt;//交易性金额负债
	public String deriveMoneyDebt;//衍生金额负债
	public String payBill;//应付票据
	public String payCredit;//应付账款
	public String payFund;//预收款项
	public String saleBuyBackAsset;//卖出回购金额资产款
	public String payHandingCharge;//应付手续费及佣金
	public String payStaffPayMent;//应付职工薪酬
	public String payTax;//应交税费
	public String payAccrual;//应付利息
	public String payDividend;//应付股利
	public String otherPayAsset;//其他应交款
	public String payMargin;//应付保证金
	public String payInside;//内部应付款
	public String otherPay;//其他应付款
	public String extractCost;//预提费用
	public String predictFlowDebt;//预计流动负债
	public String payReinCredit;//应付分保账款
	public String insurancePactMoney;//保险合同准备金
	public String agencyDealSecurity;//代理买卖证券款
	public String agencySecurity;//代理承销证券款
	public String internationTicket;//国际票证结算
	public String inlandTicket;//国内票证结算
	public String deferIncome;//递延收益
	public String payShortBond;//应付短期债券
	public String expireFixDebt;//一年内到期的非流动负债
	public String otherFlowDebt;//其他流动负债
	public String totalFlowDebt;//流动负债合计
	
	
	public String longLoan;//长期借款
	public String payLoan;//应付债款
	public String longPayMoney;//长期应付款
	public String specialPayMoney;//专项应付款
	public String predictFixLoan;//预计非流动负债
	public String deferTaxLoan;//递延所得税负债
	public String otherFixLoan;//其他非流动负债
	public String totalFixLoan;//非流动负债合计
	public String totalLoan;//负债合计
	
	public String receiveCapital; //实收资本(或股本)
	public String capitalSurplus;//资本公积
	public String repertoryStock;//库存股
	public String specialStore;//专项储备
	public String profitSurplus;//盈余公积
	public String riskPrepare;//一般风险准备
	public String uncertainInvestLoss;//未确定的投资损失
	public String noAllotProfit;//未分配利润
	public String allotDividend;//拟分配现金股利
	public String foreignReport;//外币报表折算差额
	public String totalParentCompanyRights;//归属于母公司股东权益合计
	public String fewShareholderRights;//少数股东权益
	public String TotalOwnerRight;//所有者权益(或股东权益)合计
	public String totalLoanOwnerRight;//负载和所有者权益（或股东权益）合计
	
	
	
	
}
