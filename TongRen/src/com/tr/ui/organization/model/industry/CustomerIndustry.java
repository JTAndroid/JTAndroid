/**
 * 
 */
package com.tr.ui.organization.model.industry;

import java.io.Serializable;
import java.util.List;


import com.tr.ui.organization.model.CustomerTag;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;

/**
 * 行业动态
 * 
 * @author liubang
 * 
 */
public class CustomerIndustry implements Serializable {

	public static final long serialVersionUID = 2674728182332573871L;
	public long id;// 主键
	public String industry;// 行业
	public List<CustomerTag> lableList;// 行业标签
	public List<CustomerPersonalLine> personalLineList;// 自定义属性
	@Override
	public String toString() {
		return "CustomerIndustry [id=" + id + ", industry=" + industry
				+ ", lableList=" + lableList + ", personalLineList="
				+ personalLineList + "]";
	}

	

}
