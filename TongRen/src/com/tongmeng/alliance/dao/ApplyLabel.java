package com.tongmeng.alliance.dao;

import java.util.List;

public class ApplyLabel {
	public int limit;
	public int type;
	public String option;
	public String name;

	public int getLimit() {
		return limit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

}
