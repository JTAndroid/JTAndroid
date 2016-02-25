package com.tr.ui.organization.model.privilege;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户权限表
 * @author hdy
 * @date 2014-12-25
 */
public class CustomerPermission implements Serializable  {
	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;
	/**
	 * ID
	 */
    public long id;
	/**
	 * 接收者Id
	 */
    public long receiveUserId;
    /**
     * 客户id（为客户权限）(0为金桐脑,-1 全平台)
     */
    public long customerId;
    /**
     * 发起者id
     */
    public long sendUserId;
    /**
     * 权限模块类型，多选以逗号隔开 
     * 默认为所有模块 1 基础资料 2 高层治理 3 财务分析 4股东研究 5地区概况 6主要行政部门
     */
    public String modelType;
    
	/**
     * 权限值类型（2-大乐、3-中乐、4小乐、5独乐） 客户默认独乐,组织默认中乐
     */
    public int type;
    /**
     * 分享留言
     */
    public String mento;
    /**
     * 创建时间
     */
    public Date createtime;
    /**
     * 是否是组织，0客户，1组织
     */
    public int virtual;
    /**
     * 是否金桐脑推荐的
     */
    public int isRecommend;
    
    /**
     *被分配者 用户名(前端显示)
     */
    public String name;
    
 
    
    

}
