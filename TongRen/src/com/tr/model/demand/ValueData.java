package com.tr.model.demand;

/**
 * @ClassName: ValueData.java
 * @author fxtx
 * @Date 2015年3月25日 上午10:04:09
 * @Description: TODO(用一句话描述该文件做什么)
 */
public class ValueData extends DemandSection {

	/**
	 * 
	 */
	public static final long serialVersionUID = 5285969539845705493L;

	public ValueData() {
	}
	
	public ValueData(boolean isVisable) {
		this.isVisable = isVisable;
	}

	public ValueData(String values, boolean isVisable) {
		this.value = values;
		this.isVisable = isVisable;
	}

}
