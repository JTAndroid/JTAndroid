package com.tr.ui.organization.model.finance;

import java.io.Serializable;
/**
 * 
 * 现金流量表明细
 * @author wangfeiliang
 *
 */
public class CustomerAllFlows implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1082188177390291903L;
	public String id;//主键
	public String year;// 报告年份
	public String data;// 报告期
	public String stkcd;//证券号
	public String saleGoodsMoney;//销售商品，提供劳务收到的现金
	public String clientDeposit;//客户存款和同业存放款项净增加额
	public String centerBankLoan;//向中央银行借款净增加额
	public String financialOrgan;//向其他金融机构拆入资金净增加额
	public String insuranceContract;//收到原保险合同保费取得的现金
	public String insuranceProfession;//收到再保险业务现金净额
	public String insuranceSavings;//保户储金及投资款净增加额
	public String disposeFinancial;//处置交易性金融资产净增加额
	public String takeAccrual;//收取利息，手续费及佣金的现金
	public String capitalNetIncrease;//出入资金净增加额
	public String buyBackBusiness;//回购业务资金净增加额
	public String taxReturn;//收到的税费返还
	public String likeBusinessActivities;//收到的其他与经营活动有关的现金
	public String BusActivitiInflowSubtotal;//经营活动现金流入小计
	public String buyGoodsMoney;//购买商品.接受劳务支付的现金
	public String clientLoanAccrual;//客户贷款及垫款净增加额
	public String storeCenterBankAccrual;//存放中央银行和同业款项净增加额
	public String payInsuranceContract;//支付原保险合同赔付款项的现金
	public String payAccrualHandingCharge;//支付利息.手续费及佣金的现金
	public String payWarranty;//支付保单红利的现金
	public String payStaffMoney;//支付给职工以及为职工支付的现金
	public String paySumTax;//支付的各项税费
	public String payLikeBusinessActivities;//支付的其他与经营活动有关的现金
	public String manageCashSubtotal;//经营活动现金流出小计
	public String manageCashNetAmount;//经营活动产生的现金流量净额
	
	
	public String regainInvestCash;//收回投资所收到的现金
	public String gainInvestCash;//取得投资收益所受到的现金
	public String disposeFixAsset;//处置固定资产.无形资产和其他长期资产所收回的现金净额
	public String disposeSubsidiary;//处置子公司及其他营业单位收到的现金净额
	public String receiveLikeInvestCash;//收到的其他与投资活动有关的现金
	public String reducePledgeDeposit;//减少质押和定期存款所受到的现金
	public String investActivityInSubtotal;//投资活动现金流入小计
	public String buildFixAsset;//构建固定资产.无形资产和其他长期资产所支付的现金
	public String investPayCash;//投资所支付的现金
	public String pledgeLoanNetIncrease;//质押贷款净增加额
	public String gainSubsidiaryNetIncrease;//取得子公司及其他营业单位支付的现金净额
	public String payLikeInvestCash;//支付的其他与投资活动有关的现金
	public String addPledgeDeposit;//增加质押和定期存款所支付的现金
	public String investActivityOutSubtotal;//投资活动现金流出小计
	public String investActivityCash;//投资活动产生的现金流量净额
	
	
	public String absorbInvestCash;//吸收投资收到的现金
	public String fewShareHolderCash;//其中：子公司吸收少数股东投资收到的现金
	public String gainLoanCash;//取得借款收到的现金
	public String issueBondCash;//发行债券收到的现金
	public String receiveOtherFinancingCash;//收到其他与筹资活动有关的现金
	public String financingActivityInSubtotal;//筹资活动现金流入小计
	public String repayDebtCash;//偿还债务支付的现金
	public String allotDividendCash;//分配股利.利润或偿付利息所支付的现金
	//public String 
	public String payOtherFinancingCash;//支付其他与筹资活动有关的现金
	public String financingActivityOutSubtotal;//筹资活动现金流出小计
	public String financingActivityNetAmount;//筹资活动产生的现金流量净额
	
	
	
	
	
	
	public String rateChangeAffect;//汇率变动对现金及现金等价物的影响
	public String cashEquivalent;//现金及现金等价物净增加额
	public String beginCashBalance;//期初现金及现金等价物余额
	public String endCashBalance;//期末现金及现金等价物余额
	public String netMargin;//净利润
	public String fewShareHolderRights;//少数股东权益
	public String unconfirmedInvestLoss;//未确认的投资损失
	public String assetDecreasePrepare;//资产减值准备
	public String fixAssetDepreciation;//固定资产折旧.油气资产折耗.生产性物资折旧
	public String invisibleAssetAmortize;//无形资产摊销
	public String longPrepaidCost;//长期待摊费用摊销
	public String prepaidCostReduce;//待摊费用的减少
	public String withholdingCostAdd;//预提费用的增加
	public String disposeFixAssetLoss;//处置固定资产.无形资产和其他长期资产的损失
	public String fixAssetScrapLoss;//固定资产报废损失
	public String evenhandedValueChangeLoss;//公允价值变动损失
	public String deferIncomeAdd;//递延收益增加（减：减少）
	public String predictDebt;//预计负债
	public String financeCost;//财务费用
	public String investLoss;//投资损失
	public String deferTaxAssetReduce;//递延所得税资产减少
	public String deferTaxAssetAdd;//递延所得税负债增加
	public String stockReduce;//存货的减少
	public String commercialProjectReduce;//经营性应收项目的减少
	public String commercialProjectAdd;//经营性应收项目的增加
	public String completeUnbalancedReduce;//已完工尚未结算款的减少（减:增加）
	public String balancedUncompleteAdd;//已结算尚未完工款的增加（减:减少）
	public String other;//其他
	public String commercialActivityNetAmount;//经营活动产生现金流量净额
	public String debtTurnCapital;//债务转为资本
	public String turnCompanyBond;//一年内到期的可转换公司债券
	public String financingFixAsset;//融资租入固定资产
	public String costEndBalance;//现金的期末余额
	public String costBeginBalance;//现金的期初余额
	public String costEquivalentEndBalance;//现金等价物的期末余额
	public String costEquivalentBeginBalance;//现金等价物的期初余额
	public String costEquivalentNetINcrease;//现金及现金等价物的净增加额
	
	
}
