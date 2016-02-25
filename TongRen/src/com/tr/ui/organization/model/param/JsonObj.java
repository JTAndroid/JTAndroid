package com.tr.ui.organization.model.param;

import java.io.Serializable;
import java.util.List;

/**
 * @Title: JsonObj.java
 * @Package com.ginkgocap.ywxt.demand.form
 * @Description:
 * @author haiyan
 * @date 2015-3-18 下午5:06:06
 */
public class JsonObj implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private String name;

	private List<JsonObj> childs;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the childs
	 */
	public List<JsonObj> getChilds() {
		return childs;
	}

	/**
	 * @param childs
	 *            the childs to set
	 */
	public void setChilds(List<JsonObj> childs) {
		this.childs = childs;
	}

}
