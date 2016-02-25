package com.tr.ui.people.model;

import java.io.Serializable;
import java.util.List;

/**
 * 根据标签查询人脉列表条目对象
 * @author zq
 *
 */
public class PersonLabelItem implements Serializable {

	/**
	 * 人脉头像地址
	 */
	public String portrait;
	/**
	 * 人脉的名称
	 */
	public List<PersonName> peopleNameList;
	/**
	 * 公司
	 */
	public String company;
}
