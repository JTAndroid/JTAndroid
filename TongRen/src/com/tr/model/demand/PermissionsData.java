package com.tr.model.demand;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: PermData.java
 * @author fxtx
 * @Date 2015年3月25日 上午10:50:01
 * @Description: 权限控制数据需求创建时上传的权限控制器
 * 
 */
public class PermissionsData implements Serializable {
	/**
	 * 
	 */
	public static final long serialVersionUID = 5389390815620013766L;

	public boolean dule;
	public ArrayList<LeData> xiaoles = new ArrayList<LeData>();
	public ArrayList<LeData> zhongles = new ArrayList<LeData>();
	public ArrayList<LeData> dales = new ArrayList<LeData>();

	public PermissionsData() {

	}

	public PermissionsData(boolean dule, ArrayList<LeData> xiaoles,
			ArrayList<LeData> zhongles, ArrayList<LeData> dales) {
		super();
		this.dule = dule;
		this.xiaoles = xiaoles;
		this.zhongles = zhongles;
		this.dales = dales;
	}

	/**
	 * 清除选择效果
	 */
	public void clearList() {
		if (xiaoles != null)
			xiaoles.clear();
		if (zhongles != null)
			zhongles.clear();
		if (dales != null)
			dales.clear();
	}
	
}
