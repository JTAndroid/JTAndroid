package com.tr.ui.people.model;

import java.util.List;

/**
 * 查询目录 数据解析
 * @author user
 *
 */
public class CategoryList {
	public boolean success;
	public List<ChildCategory> list;
	
	
	public class ChildCategory{
		public int id;
		public String name;
		public List<Category> list;
	}
	
	public class Category{
		public int id;
		public String name;
	}
}
