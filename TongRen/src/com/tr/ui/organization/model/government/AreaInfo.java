package com.tr.ui.organization.model.government;

import java.io.Serializable;
import java.util.List;

import com.tr.ui.organization.model.Relation;
import com.tr.ui.organization.model.government.DepartMents;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;


/**
 * 地区概况
 * @author Gintong
 *
 */
public class AreaInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public long id;
	public String name;//中文名称
	public String shotName;//别名
	public String area;//面积
	public String type;//类别 省级  市级  县级
	public String population;//人口
	public String parentArea;//上级行政区域
	public String GDP;//区域gdp
	public String resource;//资源
	public String airport;//机场
	public String train;//火车站
	public Relation mainCom;//主要企业
	public String mainColleges;//主要高校	
	public List<Relation> famousList;//当代名人
	public String remark;//地区简介
	public String taskId;//附件id
	public List<DepartMents>  departMentList;
	public List<CustomerPersonalLine> propertyList; //自定义属性
	
}
