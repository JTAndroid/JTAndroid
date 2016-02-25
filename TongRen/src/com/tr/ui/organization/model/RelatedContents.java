package com.tr.ui.organization.model;

import java.io.Serializable;

public class RelatedContents implements Serializable {

	/**
	 * 关联人脉/组织/知识/事件的具体内容
	 *                               
                                  
	 */
	public static final long serialVersionUID = 1L;
	
	public String id;//  "id":"人脉id",
	
	public String name;//  "name":"人脉名称"
	
	public String ownerid;//  "ownerid":"创建人id",
	
	public String ownerName;//  "owneriame":"创建人姓名"
	
	public String caree;//  "caree":"事件类型"
	
	public String company;//"公司"

	@Override
	public String toString() {
		return "RelatedContents [id=" + id + ", name=" + name + ", ownerid="
				+ ownerid + ", ownerName=" + ownerName + ", caree=" + caree
				+ ", company=" + company + "]";
	}


}
