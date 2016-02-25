package com.tr.ui.cloudDisk.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;


public class UserCategory implements Serializable{
	public String id;
	public String categoryname;
	public String sortid;
	public String createtime;
	public String pathName;
	public String parentId;
	public List<UserCategory>  listUserCategory;
	public String level;
}