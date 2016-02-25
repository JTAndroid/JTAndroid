package com.tr.ui.people.model;

import java.util.List;
/**
 * 人脉首页请求数据格式
 * @author zq
 *
 */
public class PeoplePage {

	private long typeId = -1;
	private long regionId = -1;
	private long careerId = -1;
	private int index = 1;//开始的页数
	private int size = 10;
	public long getTypeId() {
		return typeId;
	}
	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}
	public long getRegionId() {
		return regionId;
	}
	public void setRegionId(long regionId) {
		this.regionId = regionId;
	}
	public long getCareerId() {
		return careerId;
	}
	public void setCareerId(long careerId) {
		this.careerId = careerId;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}	
}
