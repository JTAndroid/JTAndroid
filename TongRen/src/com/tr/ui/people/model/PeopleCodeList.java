package com.tr.ui.people.model;

import java.util.List;

/*
 * 职业列表查询、分类列表查询 （数据解析）
 */
public class PeopleCodeList {
	public boolean success;
	public ChildPeopleCodeList list;
	
	public class ChildPeopleCodeList{
		public boolean flag;
		public String msg;
		public List<CodeSort> list;
	}
}
