package com.tr.model.knowledge;

import java.util.ArrayList;
import java.util.List;

/**
 * 目录 对象
 * 
 * @author gushi
 *
 */
public class UserCategory extends KnowledgeCategoryViewBase implements
		Cloneable {

	private static final long serialVersionUID = 1L;
	// 目录id
	public long id;
	// 用户Id
	private long userId;
	// 目录名称
	private String categoryname;
	// 路径Id
	private String sortid;
	// 创建时间
	private String createtime;
	// 路径名称
	private String pathName;
	// 父级id
	private long parentId;
	// 子目录
	private List<UserCategory> listUserCategory = new ArrayList<UserCategory>();
	// 层级数
	private int level;

	public UserCategory() {

	}

	/**
	 * 得到所有层级 为了显示完整目录用
	 * 
	 * @param category
	 * @return
	 */
	public String getAllCategoryname(UserCategory category) {
		String pathStr = category.getCategoryname();
		if (category.getListUserCategory().size() > 0) {
			UserCategory userCategory = category.getListUserCategory().get(0);
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
	public UserCategory getLastLevelCategory(UserCategory category) {
		if (category.getListUserCategory().size() > 0) {
			return getLastLevelCategory(category.getListUserCategory().get(0));
		} else {
			return category;
		}
	}
	
	
	
	
	/**
	 *  递归得到目录id
	 * @param category
	 * @return
	 */
	public static long recursiveGetCategoryId(UserCategory category) {
		long categoryId = category.getId();
		if (category.getListUserCategory().size() > 0) {
			categoryId = recursiveGetCategoryId(category.getListUserCategory()
					.get(0));
		}
		return categoryId;
	}
	
	
	public  long recursiveGetCategoryId() {
		long categoryId = id;
		if (listUserCategory.size() > 0) {
			return listUserCategory.get(0).recursiveGetCategoryId();
		}
		return categoryId;
	}
	

	/**
	 * 初始化假数据时使用
	 * 
	 * @param name
	 * @param level
	 * @param id
	 * @param parentId
	 */
	public UserCategory(String name, int level, long id, long parentId) {
		this.categoryname = name;
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

	public long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getCategoryname() {
		return categoryname ==null?"0":categoryname;
	}

	public void setCategoryname(String categoryname) {
		this.categoryname = categoryname;
	}

	public String getSortid() {
		return sortid;
	}

	public void setSortid(String sortid) {
		this.sortid = sortid;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getPathName() {
		return pathName;
	}

	public void setPathName(String pathName) {
		this.pathName = pathName;
	}

	public Long getParentId() {
		return parentId;
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

	public List<UserCategory> getListUserCategory() {
		if (listUserCategory == null) {
			listUserCategory = new ArrayList<UserCategory>();
		}
		return listUserCategory;
	}

	public void setListUserCategory(List<UserCategory> listUserCategory) {
		this.listUserCategory = listUserCategory;
	}

	// 深拷贝
	public UserCategory lightClone() {
		UserCategory category = new UserCategory();
		category.setId(id);
		category.setUserId(userId);
		category.setCategoryname(categoryname);
		category.setSortid(sortid);
		category.setCreatetime(createtime);
		category.setPathName(pathName);
		category.setParentId(parentId);
		category.setLevel(level);
		return category;
	}

	// 浅拷贝
	public UserCategory deepClone() {
		UserCategory category = new UserCategory();
		category.setId(id);
		category.setUserId(userId);
		category.setCategoryname(categoryname);
		category.setSortid(sortid);
		category.setCreatetime(createtime);
		category.setPathName(pathName);
		category.setParentId(parentId);
		category.setLevel(level);
		category.setListUserCategory(listUserCategory);
		return category;
	}
}
