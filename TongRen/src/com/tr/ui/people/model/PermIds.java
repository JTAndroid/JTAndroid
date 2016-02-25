package com.tr.ui.people.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Title: PermIds.java
 * @Package com.ginkgocap.ywxt.demand.form.property
 * @Description:
 * @author haiyan
 * @date 2015-3-6 下午3:21:12
 */
public class PermIds implements Serializable {
	/**
	 *
	 */
	public static final long serialVersionUID = -4893442325385620545L;
	public Boolean dule = true;
	public List<PersonPermDales> dales = new ArrayList<PersonPermDales>();
	public List<PersonPermZhongles> zhongles = new ArrayList<PersonPermZhongles>();
	public List<PersonPermXiaoles> xiaoles = new ArrayList<PersonPermXiaoles>();


}
