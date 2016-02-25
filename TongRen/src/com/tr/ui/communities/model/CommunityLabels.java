package com.tr.ui.communities.model;

import java.io.Serializable;
import java.util.List;

/**
 * 社群标签请求返回的实体
 * 
 */
public class CommunityLabels implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3515319984879240034L;
	private List<Label> userDefinedLabel;// 自定义标签
	private List<Label> hotLabel;// 热门标签

	public List<Label> getUserDefinedLabel() {
		return userDefinedLabel;
	}

	public void setUserDefinedLabel(List<Label> userDefinedLabel) {
		this.userDefinedLabel = userDefinedLabel;
	}

	public List<Label> getHotLabel() {
		return hotLabel;
	}

	public void setHotLabel(List<Label> hotLabel) {
		this.hotLabel = hotLabel;
	}

}
