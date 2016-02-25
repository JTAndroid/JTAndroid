package com.tr.ui.people.model;

import java.io.Serializable;
import java.util.List;

public class Education implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6281594739347532091L;
	/**
	 * 开始时间
	 */
	public  String stime;
	/**
	 * 结束时间
	 */
	public  String etime;
	/**
	 * 学校
	 */
	public  String school;
	/**
	 * 专业
	 */
	public  String specialty;
	/**
	 * 描述
	 */
	public  String desc;
	/**
	 * 海外学习经历（0无1有）
	 */
	public  int studyAbroadType;
	/**
	 * 同学关系
	 */
	public  List<Basic> studentsRelationshipList;
	/**
	 * 外语能力
	 */
	public  List<ForeignLanguage> foreignLanguageList;
	/**学院*/ 
	public String college;
	/**
	 * 学历：Byte修改为String
	 */
	public String educationalBackgroundType;
	/**学位*/ 
	public String degreeType;
	/**
	 * 教育自定义项，改为数组
	 */
	public List<Basic> custom;
	
}
