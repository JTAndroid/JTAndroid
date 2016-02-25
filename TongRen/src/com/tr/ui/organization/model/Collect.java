package com.tr.ui.organization.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Collect implements Serializable {
	/**
	 *   "type":"1:收藏 2:取消收藏",
     "customerIds":"组织客户Id，数组"
	 */
	public String type;
	public ArrayList<Long> customerIds;
}
