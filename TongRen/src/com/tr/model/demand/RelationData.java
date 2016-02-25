package com.tr.model.demand;

import java.util.List;

/**
 * @ClassName: TypeData.java
 * @author fxtx
 * @Date 2015年3月24日 下午5:35:46
 * @Description: 主要用来保存三级类型的界面数据
 */
public class RelationData extends DemandSection{
	/**
	 * 
	 */
	public static final long serialVersionUID = -1284781307830048056L;
	public List<DemandData> list;//类型列表数据

	public List<DemandData> getList() {
		return list;
	}

	public void setList(List<DemandData> list) {
		this.list = list;
	}


}
