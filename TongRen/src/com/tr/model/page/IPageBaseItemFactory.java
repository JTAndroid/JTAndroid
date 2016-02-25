package com.tr.model.page; 

import org.json.JSONObject;

/** 
 * @author xuxinjian
 * @E-mail: xuxinjian@gintong.com
 * @version 1.0
 * @创建时间：2014-4-12 上午10:34:23 
 * @类说明 所有包含JTPage的类，都需要从该接口继承，并实现创建 IPageBaseItem 子类的方法
 */
public interface IPageBaseItemFactory {
	public IPageBaseItem paserByObject(JSONObject jsonObj);//从json对象生成一个页项对象
}
 