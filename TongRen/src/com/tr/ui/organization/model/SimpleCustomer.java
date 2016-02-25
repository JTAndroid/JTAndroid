/**
 * 
 */
package com.tr.ui.organization.model;

/**
 * mysql 客户类
 * @author liubang
 *
 */
public class SimpleCustomer {

	/**
	 * 
	 */
	public static final long serialVersionUID = -5004527834135982483L;
	public long id; 			//id
	public String name; 		//客户名称
	public String nameFirst;	//拼音第一个字母
	public String nameIndex;	//拼音首字母
	public String nameIndexAll;//全拼音
	public long createById;	//'创建人id'(当是组织用户时,是组织用户的userId，当是客户时是创建者userId),
	public String utime;		//更新时间
	public String ctime;		//创建时间
	public long customerId;//客户mongoid
	public String shotName; //客户名简称
	public String type;//客户类型 金融机构 一般企业 中介机构 政府机构 期刊报纸 研究机构 电视广播 互联网媒体
	public String country; //国家 0 国内 1国外
    public String province;//省份
    public String city;//城市
    public String county;//县城
    public String address;//具体地址
    public String industrys;//行业名称，多个行业用逗号隔开
	public String industryIds;//行业id，多个行业用逗号隔开 
	public String picLogo;//客户头像logo
	public String remark;	//简介
	
	public String linkMobile;// 联系手机(多个用豆号隔开)
	public int virtual;// 是否是组织 0 客户 1 用户注册组织 2 未注册的组织
	
	
	
}
