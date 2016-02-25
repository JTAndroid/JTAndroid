package com.tr.model.demand;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: Metadata.java
 * @author fxtx
 * @Date 2015年3月9日 上午10:20:08
 * @Description: 行业，类型，区域，三级显示元数据
 */
public class Metadata implements Serializable {

	/**
	 * 代表全选
	 */

	/**
	 * 
	 */
	public static final long serialVersionUID = 2406588350895127409L;
	public String parentid;// 父级借点信息id
	public String id; // id值
	public String remark;// 标记
	public String name;// 名称
	public int hasChild;// 是否有子级

	public String number;// 编号

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * 布局
	 */
	/** 0 代表没有选择，1为有部分选择，2为全部选择 */
	public int selectNum;
	public List<Metadata> childs;// 子级信息
	public boolean isSelect;

	public Metadata() {
		// TODO Auto-generated constructor stub
		childs = new ArrayList<Metadata>();
	}

	public Metadata(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public Metadata(String name) {
		// TODO Auto-generated constructor stub
		this.name = name;
		childs = new ArrayList<Metadata>();
	}

	public Metadata(String name, boolean isSelect) {
		super();
		this.name = name;
		this.isSelect = isSelect;
	}

	/**
	 * 添加一个子级
	 * 
	 * @param data
	 */
	public void addChildsOnly(Metadata data) {
		childs.clear();
		childs.add(data);
	}
}
