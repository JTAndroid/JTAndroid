package com.tr.ui.tongren.model.record;

import java.io.Serializable;

public class RecordDetailExtend implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private RecordRule templetInfo;
	private String ipAddrStart;
	private String lonlatEnd;
	private String ipAddrEnd;
	private String lonlatStart;

	public RecordRule getTempletInfo() {
		return templetInfo;
	}

	public void setTempletInfo(RecordRule templetInfo) {
		this.templetInfo = templetInfo;
	}

	public String getIpAddrStart() {
		return ipAddrStart;
	}

	public void setIpAddrStart(String ipAddrStart) {
		this.ipAddrStart = ipAddrStart;
	}

	public String getLonlatEnd() {
		return lonlatEnd;
	}

	public void setLonlatEnd(String lonlatEnd) {
		this.lonlatEnd = lonlatEnd;
	}

	public String getIpAddrEnd() {
		return ipAddrEnd;
	}

	public void setIpAddrEnd(String ipAddrEnd) {
		this.ipAddrEnd = ipAddrEnd;
	}

	public String getLonlatStart() {
		return lonlatStart;
	}

	public void setLonlatStart(String lonlatStart) {
		this.lonlatStart = lonlatStart;
	}

}
