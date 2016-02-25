package com.tr.model.home;

import java.io.Serializable;
import java.util.List;

/** @author sunjianan */
public class MIndustrys implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3214298362372344379L;

	private List<MIndustry> listIndustry;

	private int index;
	private int size;
	private int total;

	public List<MIndustry> getListIndustry() {
		return listIndustry;
	}

	public void setListIndustry(List<MIndustry> listIndustry) {
		this.listIndustry = listIndustry;
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

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

}
