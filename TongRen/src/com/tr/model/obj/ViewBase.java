package com.tr.model.obj;

/**
 * 视图基类
 * @author leon
 */
public class ViewBase {

	protected boolean visiable; // 是否是可见的
	protected boolean selected; // 是否已选择
	
	public boolean isVisiable() {
		return visiable;
	}
	public void setVisiable(boolean visiable) {
		this.visiable = visiable;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
