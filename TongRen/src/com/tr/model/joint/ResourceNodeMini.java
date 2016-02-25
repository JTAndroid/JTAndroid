package com.tr.model.joint;

import java.util.ArrayList;
import java.util.List;

public class ResourceNodeMini{

	private String column; // 栏目名称
	private int type; // 1-事件;2-人脉;3-组织;4-知识
	private List<String> listItemId; // 子项目的id
	
	private String columnType;
	private List<String> listItemType;
	
	
	public List<String> getListItemType() {
		if (listItemType == null) {
			listItemType = new ArrayList<String>();
		}
		return listItemType;
	}

	public void setListItemType(List<String> listItemType) {
		this.listItemType = listItemType;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public List<String> getListItemId() {
		if (listItemId == null) {
			listItemId = new ArrayList<String>();
		}
		return listItemId;
	}

	public void setListItemId(List<String> listItemId) {
		this.listItemId = listItemId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}
}
