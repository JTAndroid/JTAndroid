//package com.tr.model.demand;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//
//import com.tr.model.knowledge.UserCategory;
//
///**
// * @ClassName: DemandCategory.java
// * @author fxtx
// * @Date 2015年3月29日 下午1:35:16
// * @Description: 需求目录对象
// */
//public class DemandCategory implements Serializable {
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1256535089144181728L;
//	// 目录id
//	public long id;
//	// 用户Id
//	public long userId;
//	// 目录名称
//	public String name;
//	// 路径Id
//	public String sortId;
//	// 创建时间
//	public String createtime;
//	// 路径名称
//	public String pathName;
//	// 父级id
//	public long parentId;
//	// 子目录
//	public List<DemandCategory> list;
//	// 层级数
//	public int level;
//
//	/**
//	 * 将需求 目录对象 转成 控件支持的目录对象
//	 * 
//	 * @param demand
//	 * @return
//	 */
//	public static UserCategory toUserCategory(DemandCategory demand) {
//		UserCategory user = new UserCategory();
//		user.setId(demand.id);
//		user.setUserId(demand.userId);
//		user.setCategoryname(demand.name);
//		user.setSortid(demand.sortId);
//		user.setParentId(demand.parentId);
//		if (demand.list != null && demand.list.size() > 0) {
//			user.setListUserCategory(toListCategory(demand.list));
//		}
//		return user;
//	}
//
//	public static List<UserCategory> toListCategory(List<DemandCategory> list) {
//
//		List<UserCategory> catrgory = new ArrayList<UserCategory>();
//		if (list != null) {
//			for (DemandCategory demand : list) {
//				catrgory.add(toUserCategory(demand));
//			}
//		}
//		return catrgory;
//	}
//
//}
