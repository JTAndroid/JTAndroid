package com.tr.ui.organization.model.param;

import java.io.Serializable;
/**
* <p>Title: CustomerDataParam.java<／p> 
* <p>Description:web项目保存客户接受参数对象 <／p> 
* @author wfl
* @date 2015-1-13 
* @version 1.0
 */
public class CustomerDataParam implements Serializable{

	 /**
	 * 
	 */
	public static final long serialVersionUID = 1L;
	public long id;
	public long customerId;//客户id
	public String groupId;//目录id，多个以，隔开
	public String groupName;//目录名称,多个以，隔开
	public String lableId;//标签id，多个以,隔开
	public String lableName;//标签名称,多个以,隔开
	
	//权限参数
	 /**
     * 权限模块类型，多选以逗号隔开 
     * 默认为所有模块 1 基础资料 2 高层治理 3 财务分析 4股东研究 5地区概况 6主要行政部门
     */
    public String modelType;
	public String p2;//p2 大乐 好友的id
	public String p3;//param p3  中乐  好友的id
	public String p4;//param p4 小乐 好友的id
	public String p5;//param p5 独乐   自已id
	@Override
	public String toString() {
		return "CustomerDataParam [id=" + id + ", customerId=" + customerId
				+ ", groupId=" + groupId + ", groupName=" + groupName
				+ ", lableId=" + lableId + ", lableName=" + lableName
				+ ", modelType=" + modelType + ", p2=" + p2 + ", p3=" + p3
				+ ", p4=" + p4 + ", p5=" + p5 + "]";
	}
	
	
	
}
