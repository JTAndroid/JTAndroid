package com.tr.ui.organization.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.tr.model.demand.ASSORPOK;
import com.tr.ui.organization.model.permission.CustomerPermission_Bean;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;
import com.tr.ui.organization.model.profile.CustomerPhone;
import com.tr.ui.organization.model.profile.CustomerProfile;

/**
 * 
 * @author sunjianan
 *
 */
public class RegistOrgInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6029486911489999603L;
	
	public String orgType;//客户类型 金融机构 一般企业 中介机构 政府机构 期刊报纸 研究机构 电视广播 互联网媒体 自定义类型
	public String isListing;// 是否上市 1.是 0.否
	public String stockNum;// 证券号
	public String name; 		//客户名称
	public String shotName; //客户名简称
	public String licensePic;// 营业执照副本扫描件
	public String linkIdPic;// 身份证图片
	public Relation linkName;// 联系人
	public String linkMobile;// 联系手机
	
	
	//----------------------------------------------
	
	public Long id;
	public String sqlId;//用于保存的mysqlid
	public String licenseNo;//营业执照号
	public List<String> industrys;//行业名称，多个行业
	public List<String> industryIds;//行业id，多个行业 
	public Area area;//客户所属地区
	// public List<String> lables;//客户标签
	public List<CustomerPhone> phoneList;// 客户电话
	public String email;// 客户邮箱
	public String discribe;// 客户描述
	public List<CustomerGroup> group; // 客户分组
	public String comeId;// 来源组织id
	public String virtual;// 是否是组织 0 客户 1 用户注册组织 2 未注册的组织
	public int auth = -1;// -1 未进行认证 0认证进行中 1认证失败 2认证成功
	public Long userId;// 组织用户id
	public String linkEmail;// 联系邮箱
	public String picLogo;// 客户头像logo
	public String banner;// 顶部图片路径
	public String taskId;// 附件id
	public boolean friends;
	public List<CustomerPersonalLine> propertyList; // 自定义属性
	public List<CustomerTag> lableList;// 客户标签
	public ASSORPOK relevance; // 关联
	public CustomerPermission_Bean customerPermissions;//权限
	public ArrayList<String> directory; //标签
	// 关联人脉
	// TODO

	// 公司详情
	public CustomerProfile profile;

}
