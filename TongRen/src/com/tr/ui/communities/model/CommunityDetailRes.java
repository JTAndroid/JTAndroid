package com.tr.ui.communities.model;

import java.io.Serializable;
import java.util.List;

import com.tr.model.demand.ASSORPOK;
/**
 * 社群详情对象
 * @author Administrator
 *
 */
public class CommunityDetailRes implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8708031678010773131L;
	
	/**
	 * 创建社群的社群实体
	 */
	private ImMucinfo community;
	/**
	 * 关联
	 */
	private ASSORPOK asso;
	/**
	 * 标签id
	 */
	private List<Label> labels;
	/**
	 * 社群设置
	 */
	private CreateSet set;

	public ImMucinfo getCommunity() {
		return community;
	}

	public void setCommunity(ImMucinfo community) {
		this.community = community;
	}

	public ASSORPOK getAsso() {
		return asso;
	}

	public void setAsso(ASSORPOK asso) {
		this.asso = asso;
	}

	public List<Label> getLabels() {
		return labels;
	}

	public void setLabels(List<Label> labels) {
		this.labels = labels;
	}

	public CreateSet getSet() {
		return set;
	}

	public void setSet(CreateSet set) {
		this.set = set;
	}

}
