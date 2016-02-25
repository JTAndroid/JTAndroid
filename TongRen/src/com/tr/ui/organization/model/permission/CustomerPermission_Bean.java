package com.tr.ui.organization.model.permission;

import java.io.Serializable;
import java.util.ArrayList;

public class CustomerPermission_Bean implements Serializable{
	public boolean dule;
	public ArrayList<Permission> xiaoles;
	public ArrayList<Permission> zhongles;
	public ArrayList<Permission> dales;
	public ArrayList<String> modelType = new ArrayList<String>();
	public String mento;
}
