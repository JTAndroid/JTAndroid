package com.tr.model.demand;

/**
 * @ClassName: TitleData.java
 * @author fxtx
 * @Date 2015年3月24日 下午5:34:21
 * @Description: 需求列表对象
 */
public class TitleData extends DemandSection {

	/**
	 * 
	 */
	public static final long serialVersionUID = -2357988471610340966L;


	public TitleData() {
	}
	public TitleData(boolean isVisable) {
		this.isVisable = isVisable;
	}

	public TitleData(String title, boolean isVisable) {
		this.value = title;
		this.isVisable = isVisable;
	}
}
