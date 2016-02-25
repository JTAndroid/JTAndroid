package com.tr.ui.organization.model;

import java.util.ArrayList;
import java.util.List;

import com.tr.model.demand.ASSORPOK;
import com.tr.ui.organization.model.Area;
import com.tr.ui.organization.model.CustomerBase;
import com.tr.ui.organization.model.CustomerGroup;
import com.tr.ui.organization.model.CustomerTag;
import com.tr.ui.organization.model.Relation;
import com.tr.ui.organization.model.permission.CustomerPermission_Bean;
import com.tr.ui.organization.model.privilege.CustomerPermission;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;
import com.tr.ui.organization.model.profile.CustomerPhone;
import com.tr.ui.organization.model.profile.CustomerProfile;

/**
 * 客户类
 * 
 * @author liubang
 * 
 */
public class Customer extends CustomerBase {
	

	/**
	 * "name":"组织全称"， "shotName":"组织简称"， "type":"组织类型(int)"，
	 * 
	 * "industrys":"组织行业，多个行业用逗号隔开"，
	 * 
	 * "isListing":"是否上市 是 否", "stockNum":"证券号"， "， "linkMobile":"联系电话"，
	 * "linkEmail":"联系邮箱"， String discribe;//客户描述
	 */
	public static final long serialVersionUID = -121702000153164463L;
	public Long id;
    public List<Long> columns;;
	public String sqlId;//用于保存的mysqlid
	public String name; 		//客户名称
	public String shotName; //客户名简称
	public String type;//客户类型 金融机构 一般企业 中介机构 政府机构 期刊报纸 研究机构 电视广播 互联网媒体 自定义类型
	public String licenseNo;//营业执照号
	public List<String> industrys;//行业名称，多个行业
	public List<String> industryIds;//行业id，多个行业 
	public Area area;//客户所属地区
	public String isListing;// 是否上市 是 否
	public String stockNum;// 证券号
	// public List<String> lables;//客户标签
	public List<CustomerPhone> phoneList;// 客户电话
	public String email;// 客户邮箱
	public String discribe;// 客户描述
	public List<CustomerGroup> group; // 客户分组
	public String comeId;// 来源组织id
	public String virtual;// 是否是组织 0 客户 1 用户注册组织 2 未注册的组织
	public int auth = -1;// -1 未进行认证 0认证进行中 1认证失败 2认证成功
	public Long userId;// 组织用户id
	public Relation linkName;// 联系人
	public String linkMobile;// 联系手机
	public String linkEmail;// 联系邮箱
	public String picLogo;// 客户头像logo
	public String linkIdPic;// 身份证图片
	public String banner;// 顶部图片路径
	public String taskId;// 附件id
	public String licensePic;// 营业执照副本扫描件
	public boolean friends;
	public List<CustomerPersonalLine> propertyList; // 自定义属性
	public List<CustomerTag> lableList;// 客户标签
	public ASSORPOK relevance; // 关联
	public CustomerPermission_Bean customerPermissions;//权限
	public ArrayList<String> directory; //目录
	public String isOrgChange; //客户类型  2:保存客户 1:组织转为客户的 0:新建客户",
	// 关联人脉
	// TODO

	// 公司详情
	public CustomerProfile profile;

	@Override
	public String toString() {
		return "Customer [id=" + id + ", sqlId=" + sqlId + ", name=" + name
				+ ", shotName=" + shotName + ", type=" + type + ", licenseNo="
				+ licenseNo + ", industrys=" + industrys + ", industryIds="
				+ industryIds + ", area=" + area + ", isListing=" + isListing
				+ ", stockNum=" + stockNum + ", phoneList=" + phoneList
				+ ", email=" + email + ", discribe=" + discribe + ", group="
				+ group + ", comeId=" + comeId + ", virtual=" + virtual
				+ ", auth=" + auth + ", userId=" + userId + ", linkName="
				+ linkName + ", linkMobile=" + linkMobile + ", linkEmail="
				+ linkEmail + ", picLogo=" + picLogo + ", linkIdPic="
				+ linkIdPic + ", banner=" + banner + ", taskId=" + taskId
				+ ", licensePic=" + licensePic + ", friends=" + friends
				+ ", propertyList=" + propertyList + ", lableList=" + lableList
				+ ", relevance=" + relevance + ", customerPermissions="
				+ customerPermissions 
				+ ", profile=" + profile + "]";
	}

	
	/**
	 * //资源需求 public CustomerResource resource; //财务分析摘要 public CustomerFinance
	 * finace; //股东研究 public CustomerStock stock; //高层治理 public CustomerHight
	 * high; //同业竞争 public CustomerPeer peer; //行业动态 public CustomerIndustry
	 * industry; //研究报告 public CustomerReport report;
	 */

}
