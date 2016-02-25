package com.tr.model.knowledge;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户分组列表  (组织目录) 对象
 * 
 * @author gushi
 *
 */
public class OrganizationCategory extends KnowledgeCategoryViewBase implements
		Cloneable {

	private static final long serialVersionUID = 1L;
	/** 客户分组id  */
	private long id;
//	// 用户Id
//	private long userId;
	/** 客户分组名称  */
	private String name;
//	// 路径Id
//	private String sortid;
//	// 创建时间
//	private String createtime;
//	// 路径名称
//	private String pathName;
	/** 客户分组下客户总数量  */
	private int count;
	/** 父级id  */
	private long parentId;
	// 层级数
	private int level;
	// 子目录
	private List<OrganizationCategory> child = new ArrayList<OrganizationCategory>();

	public OrganizationCategory() {

	}
	
	
	/**
	 * 转成知识型目录
	 */
	public UserCategory ToKnowledgeCategory() {
		
		UserCategory userCategory = new UserCategory();
		userCategory.setId(id);
		userCategory.setCategoryname(name);
		userCategory.setParentId(parentId);
		userCategory.setLevel(level);
		if(child != null & child.size() > 0){
			ArrayList<UserCategory> knowledgeList = new ArrayList<UserCategory>();
			for ( OrganizationCategory organizationCategory : child ){
				UserCategory userCategory2 = organizationCategory.ToKnowledgeCategory();
				knowledgeList.add(userCategory2);
			}
			userCategory.setListUserCategory(knowledgeList);
			
		}
		
		return userCategory;
			
	}
	
	
	

	/**
	 * 得到所有层级 为了显示完整目录用
	 * 
	 * @param category
	 * @return
	 */
	public String getAllCategoryname(OrganizationCategory category) {
		String pathStr = category.getName();
		if (category.getChild().size() > 0) {
			OrganizationCategory userCategory = category.getChild().get(0);
			pathStr += "/";
			return pathStr + getAllCategoryname(userCategory);
		} else {
			return pathStr;
		}

	}


	/**
	 * 得到最后一级目目录对象
	 * 
	 * @return
	 */
	public OrganizationCategory getLastLevelCategory(OrganizationCategory category) {
		if (category.getChild().size() > 0) {
			return getLastLevelCategory(category.getChild().get(0));
		} else {
			return category;
		}
	}

	/**
	 * 初始化假数据时使用
	 * 
	 * @param name
	 * @param level
	 * @param id
	 * @param parentId
	 */
	public OrganizationCategory(String name, int level, long id, long parentId) {
		this.name = name;
		this.level = level;
		this.id = id;
		this.parentId = parentId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getParentId() {
		return parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<OrganizationCategory> getChild() {
		if (child == null) {
			child = new ArrayList<OrganizationCategory>();
		}
		return child;
	}

	public void setChild(List<OrganizationCategory> child) {
		this.child = child;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}


	// 深拷贝
	public OrganizationCategory lightClone() {
		OrganizationCategory category = new OrganizationCategory();
		category.setId(id);
//		category.setUserId(userId);
		category.setName(name);
//		category.setSortid(sortid);
//		category.setCreatetime(createtime);
//		category.setPathName(pathName);
		category.setParentId(parentId);
		category.setLevel(level);
		return category;
	}

	// 浅拷贝
	public OrganizationCategory deepClone() {
		OrganizationCategory category = new OrganizationCategory();
		category.setId(id);
//		category.setUserId(userId);
		category.setName(name);
//		category.setSortid(sortid);
//		category.setCreatetime(createtime);
//		category.setPathName(pathName);
		category.setParentId(parentId);
		category.setLevel(level);
		category.setChild(child);
		return category;
	}
}
