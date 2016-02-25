package com.tr.ui.people.model;

import java.util.List;

/*
 * 查询会面情况列表（数据解析） 关联relevance字段
 * /关联 r:事件,p:人脉,o:组织,k:知识 type:1 需求 2 人脉 3 全平台普通用户 4 组织(全平台组织用户) 5 客户 6 知识
 */
public class Relevance {
	public List<R> r;
	public List<P> p;
	public List<O> o;
	public List<K> k;
	
	public class R{
		public List<Rrelevance> conn;
	}
	public class P{
		public List<Prelevance> conn;
	}
	public class O{
		public List<Orelevance> conn;
	}
	public class K extends BaseChild{
		public List<Krelevance> conn;
	}

	//事件
	public class Rrelevance extends BaseRelevance{
		public String name;
		public String requirementtype;
	}
	//人脉
	public class Prelevance extends BaseRelevance{
		public String name;
		public String caree;
		public String company;
	}
	//组织
	public class Orelevance extends BaseRelevance{
		public String name;
		public String address;
		public String hy;
	}
	//知识
	public class Krelevance extends BaseRelevance{
		public String title;
		public String columntype;
		public String columnpath;
	}
	
	public class BaseRelevance{
		public String type;
		public String id;
		//public String name;
		public String ownerId;
		public String ownerName;
	}

	public class BaseChild{
		public String tag;
	}
}
