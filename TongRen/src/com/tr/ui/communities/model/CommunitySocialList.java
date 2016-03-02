package com.tr.ui.communities.model;

import java.io.Serializable;
import java.util.List;

public class CommunitySocialList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int count;
	private List<CommunitySocial> listCommunity;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<CommunitySocial> getListCommunity() {
		return listCommunity;
	}

	public void setListCommunity(List<CommunitySocial> listCommunity) {
		this.listCommunity = listCommunity;
	}

}
