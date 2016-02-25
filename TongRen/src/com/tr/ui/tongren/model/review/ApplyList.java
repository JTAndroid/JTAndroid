package com.tr.ui.tongren.model.review;

import java.io.Serializable;
import java.util.List;

public class ApplyList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String end;
	private String index;
	private String pageCount;
	private List<Apply> result;
	private String size;
	private String start;
	private String totalCount;

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getPageCount() {
		return pageCount;
	}

	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}

	public List<Apply> getResult() {
		return result;
	}

	public void setResult(List<Apply> result) {
		this.result = result;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

}
