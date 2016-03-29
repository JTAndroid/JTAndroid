package com.tongmeng.alliance.dao;

public class ConferenceClass {

	/**
	 * {"id":129,"title":"测试","picPath":
	 * "http://www.rouduoduo.cn/file//alliance/download?id=159"
	 * ,"createrId":40,"createrName":"我是谁12","createrPic":
	 * "http://www.rouduoduo.cn/file//alliance/download?id=181"
	 * ,"startTime":"2015-12-30 00:00"
	 * ,"endTime":"2015-12-31 00:00","province":"北京"
	 * ,"city":"东城","address":"迭代","fee"
	 * :0.0,"peopleNumber":0,"applyNumber":1,"source":""}
	 * 
	 */

	public String id;
	public String title;
	public String picPath;
	public String createrId;
	public String createrName;
	public String createrPic;
	public String startTime;
	public String endTime;
	public String province;
	public String address;
	public String fee;
	public String city;
	public String peopleNumber;
	public String applyNumber;
	public String source;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public String getCreaterId() {
		return createrId;
	}

	public void setCreaterId(String createrId) {
		this.createrId = createrId;
	}

	public String getCreaterName() {
		return createrName;
	}

	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}

	public String getCreaterPic() {
		return createrPic;
	}

	public void setCreaterPic(String createrPic) {
		this.createrPic = createrPic;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getPeopleNumber() {
		return peopleNumber;
	}

	public void setPeopleNumber(String peopleNumber) {
		this.peopleNumber = peopleNumber;
	}

	public String getApplyNumber() {
		return applyNumber;
	}

	public void setApplyNumber(String applyNumber) {
		this.applyNumber = applyNumber;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}
