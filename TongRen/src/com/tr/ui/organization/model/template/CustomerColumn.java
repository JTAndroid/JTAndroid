package com.tr.ui.organization.model.template;
/**
* <p>Title: CustomerColumn.java<／p> 
* <p>Description: 客户栏目类<／p> 
* @author wfl
* @date 2014-12-26 
* @version 1.0
 */
public class CustomerColumn {

	 public long id;//主键
	 public String name;//名称
	 public int type;//类型 1:专业栏目 2:常用栏目  3:自定义栏目
	 public long customerId;//客户
	 public String ctime;//创建时间
	 public String utime;//跟新时间
	 public int parentId;//父类id
	 public int isSelect; // 是否选择 1:选择 0:未选择
	
}
