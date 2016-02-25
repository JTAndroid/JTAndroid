package com.tr.ui.demand.util; 
/**
 * @ClassName:     OnNeedDetails.java
 * @author         fxtx
 * @Date           2015年3月28日 下午1:48:05 
 * @Description:   TODO(用一句话描述该文件做什么) 
 */
public interface OnNeedDetails {
	/**
	 * 
	 * @param type:1 当前用户权限，2评论个数，3需求对象
	 * @param obj
	 */
	public void toNeedDetail(int type,Object obj);//传递到上一级界面
	
	/**
	 * 获取Fragment 的刷新接口对象
	 * @param refresh
	 */
	public void getNeedRefresh(OnNeedRefresh refresh,int index);
}
 
