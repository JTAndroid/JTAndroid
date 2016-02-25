package com.tr.ui.organization.model;

import java.io.Serializable;

public class OrgInfoVo implements Serializable{
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String picLogo;//组织logo
	public String name;//组织全称
	public String shotName;// 简称
	public String orgType;// 组织类型 1.金融机构 2一般企业 3.政府组织 4.中介机构 5.专业媒体 6.期刊报纸 7.研究机构 8.电视广播 9.互联网媒体
	public String isListing;// 是否上市 1:是 0:非上市
	public String stockNum;// 证券号
	public String licensePic;// 营业执照副本扫描件
	public String linkIdPic;// 身份证正面图片
	public String linkIdPicReverse;// 身份证反面图片
	public Relation linkName;//联系人姓名
	public String linkMobile;// 联系人手机
	public String mobileCode;//手机验证码
	
	@Override
	public String toString() {
		return "OrgInfoVo [orgType=" + orgType + ", isListing=" + isListing
				+ ", stockNum=" + stockNum + ", name=" + name + ", shotName="
				+ shotName + ", licensePic=" + licensePic + ", linkIdPic="
				+ linkIdPic + ", linkName=" + linkName + ", linkMobile="
				+ linkMobile + ", mobileCode=" + mobileCode + "]";
	}
//	public String getOrgType() {
//		return orgType;
//	}
//
//	public void setOrgType(String orgType) {
//		this.orgType = orgType;
//	}
//
//	public String getIsListing() {
//		return isListing;
//	}
//
//	public void setIsListing(String isListing) {
//		this.isListing = isListing;
//	}
//
//	public String getStockNum() {
//		return stockNum;
//	}
//
//	public void setStockNum(String stockNum) {
//		this.stockNum = stockNum;
//	}
//
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public String getShotName() {
//		return shotName;
//	}
//
//	public void setShotName(String shotName) {
//		this.shotName = shotName;
//	}
//
//	public String getLicensePic() {
//		return licensePic;
//	}
//
//	public void setLicensePic(String licensePic) {
//		this.licensePic = licensePic;
//	}
//
//	public String getLinkIdPic() {
//		return linkIdPic;
//	}
//
//	public void setLinkIdPic(String linkIdPic) {
//		this.linkIdPic = linkIdPic;
//	}
//
//	public Relation getLinkName() {
//		return linkName;
//	}
//
//	public void setLinkName(Relation linkName) {
//		this.linkName = linkName;
//	}
//
//	public String getLinkMobile() {
//		return linkMobile;
//	}
//
//	public void setLinkMobile(String linkMobile) {
//		this.linkMobile = linkMobile;
//	}
//
//	public String getMobileCode() {
//		return mobileCode;
//	}
//
//	public void setMobileCode(String mobileCode) {
//		this.mobileCode = mobileCode;
//	}
    
}
