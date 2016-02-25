package com.tr.ui.flow.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.tr.model.demand.ASSOData;
import com.tr.model.demand.ASSORPOK;

public class FlowASSO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public long dynamicId;
	public List<ASSOData> r = new ArrayList<ASSOData>(); // 事件
	public List<ASSOData> p = new ArrayList<ASSOData>();// 人
	public List<ASSOData> o = new ArrayList<ASSOData>();// 组织
	public List<ASSOData> k = new ArrayList<ASSOData>();// 知识

	public long getDynamicId() {
		return dynamicId;
	}

	public void setDynamicId(long dynamicId) {
		this.dynamicId = dynamicId;
	}

	public List<ASSOData> getR() {
		return r;
	}

	public void setR(List<ASSOData> r) {
		this.r = r;
	}

	public List<ASSOData> getP() {
		return p;
	}

	public void setP(List<ASSOData> p) {
		this.p = p;
	}

	public List<ASSOData> getO() {
		return o;
	}

	public void setO(List<ASSOData> o) {
		this.o = o;
	}

	public List<ASSOData> getK() {
		return k;
	}

	public void setK(List<ASSOData> k) {
		this.k = k;
	}

}
