/**
 * 
 */
package com.tr.ui.organization.model.profile;

import java.io.Serializable;
import java.util.List;

import com.tr.ui.organization.model.Area;
import com.tr.ui.organization.model.Relation;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;

/**
 * 公司概况
 * @author liubang
 *
 */
public class CustomerInfo implements Serializable{
	/**
	 * 
	 */
	public static final long serialVersionUID = 5020722399048046551L;
	public String id;
	public String capital;//'注册资金',
    public String cur;//币种
    public String stkcd;//'证券代码',
    public Relation controler;//实际控股人
    public Relation corpn;//'法人',
    public String address;//'通讯地址'/政府驻地
	public String phone;//'电话',
    public String fax;//'传真',
    public String website;//'网址',
    public String profile;//'公司概况',
    public String product;//'产品描述',
    public String history;//'历史沿革',
    
    public Relation relation;//主办单位/频道名称/研究机构名称/期刊名称
    public List<String> columnList;//主要栏目
    public List<String> typesList;//网站类型 娱乐 体育 经济 影视 儿童/频道性质
    public Relation leader;//负责人/主编
    public String famous;//知名节目
    public Relation hostess;//著名节目主持人/社长
    public Area area;//发行区域
    public Relation parentOrg;//上级主s管单位及企业
    public List<Relation> expertList;//专家/主要领导
    public String number;//刊号
    public String circulation;//发行量
    public String cycle;//出版周期 周刊 月刊 双月刊
    
	
	public String childArea;//下辖地区
	public String devArea;//开发地区
	
	public List<CustomerPersonalLine> propertyList; //自定义属性
	
	
}
