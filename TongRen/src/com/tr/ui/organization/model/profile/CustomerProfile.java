package com.tr.ui.organization.model.profile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;


/**
 * 公司详情
 * 
 * @author liubang
 * 
 */
public class CustomerProfile implements Serializable {

	/**
	 * 
	 */
	public long id;
	public static final long serialVersionUID = -7736370967212335490L;
	// 公司概况
	public CustomerInfo info;
	// 上市信息
	public CustomerListing listing;
	// 分支结构
	public List<CustomerBranch> branchList;
	// 关联企业
	public List<CustomerEnterprise> enterpriseList;
	// 保荐机构
	public List<CustomerBranch> sponsorBranchList;
	// 会计事务所
	public CustomerBranch accountingFirm;
	// 律师事务所
	public CustomerBranch lawFirm;
	// 主办银行
	public CustomerBranch sponsorBank;
	// 合作银行
	public List<CustomerBranch> teamBankList;
	// 业务分析
	public List<CustomerBusiness> businessList;
	// 金融产品
	public List<CustomerProdect> prodectList;
	// 合伙人
	public List<CustomerPartner> partnerList;
	// 专业团队
	public List<CustomerPartner> teamList;
	// 执业资质
	public List<CustomerRemark> practiceList;
	// 经济特色
	public CustomerRemark incomeFeature;
	// 主要客户
	public List<CustomerRemark> sponsorCustomerList;
	// 支柱产业
	public CustomerRemark braceIndustry;
	// 主要刊物 /人文特色
	public CustomerRemark publication;
	// 联系人资料
	public List<CustomerLinkMan> linkMans;
	// 公司概况的相关附件
	public String taskId;
	// 自定义板块 
	public List<CustomerPersonalPlate> personalPlateList;
	public Map<Integer, Long> columnOrder; // 栏目的顺序 key(1,2,3顺序)
											// value(栏目Id_栏目类型)
	// 自定义模板
	public Map<Long, CustomerPersonalPlate> personalPlateMap; // 栏目对应--自定义模板

	public List<Long> selectCoumnIds;// 所选栏目Id



}
