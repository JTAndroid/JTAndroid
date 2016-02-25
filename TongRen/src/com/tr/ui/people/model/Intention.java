package com.tr.ui.people.model;

import java.io.Serializable;
import java.util.List;

/**
 * 投资意向 、融资意向等
 */
public class Intention implements Serializable {
	/**
	 * 父类型
	 */
	public Byte parentType;
	/**
	 * 类型id集合 用 , 分割
	 */
	public String typeIds;
	/**
	 * 类型名称集合 用 , 分割
	 */
	public String typeNames;
	/**
	 * 地址
	 */
	public Address address;
	/**
	 * 行业id集合 用 , 分割
	 */
	public String industryIds;
	/**
	 * 行业名称 用 , 分割
	 */
	public String industryNames;
	/**
	 * 自定义项，修改为数组
	 */
	private List<Basic> custom;


	
}
