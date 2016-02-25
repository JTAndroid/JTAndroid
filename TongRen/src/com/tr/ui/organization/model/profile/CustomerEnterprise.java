package com.tr.ui.organization.model.profile;

import java.io.Serializable;
import java.util.List;

import com.tr.ui.organization.model.Relation;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;
/**
 * 个人客户概况关联企业
 * @author liubang
 *
 */
public class CustomerEnterprise implements Serializable  {
    public static final long serialVersionUID = 8709217265402617431L;
    public String id;//序号
    //public String name;//'企业名称',
    public Relation relation;//'关联企业关系',
    public String relationship;//关联关系
    public String stockPercent;//'持股比例',
    public String stockQty;//'持股数量',
    public String ifControl;//'是否控制0未知1否2是',
    public List<CustomerPersonalLine> propertyList; //自定义属性
  
	
}
