package com.tr.model.demand;

/**
 * @ClassName: CreateDemandBean.java
 * @author fxtx
 * @Date 2015年3月9日 下午6:56:56
 * @Description: 创建投融资信息
 */
public class CreateDemandBean {
	public String title;
	public int icon;
	public int type;

	public CreateDemandBean(String title, int icon, int type) {
		super();
		this.title = title;
		this.icon = icon;
		this.type = type;
	}

}
