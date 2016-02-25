package com.tr.ui.organization.model.template;

import java.io.Serializable;
import java.util.List;

public class CustomerColumnVo implements Serializable {
	
	public class Results{
		public long id;
		public String name;
		public int type;// 类型 1:专业栏目 2:常用栏目 3:自定义栏目
		 public String isSelect; // 是否选择 1:选择 0:未选择
		public List<Results> child;
		@Override
		public String toString() {
			return "Results [id=" + id + ", name=" + name + ", type=" + type
					+ ", isSelect=" + isSelect + ", child=" + child + "]";
		}
		
	}
	
	public List<Results> results;
	public String msg;
	public boolean success;
	@Override
	public String toString() {
		return "CustomerColumnVo [results=" + results + ", msg=" + msg
				+ ", success=" + success + "]";
	}
	
}

