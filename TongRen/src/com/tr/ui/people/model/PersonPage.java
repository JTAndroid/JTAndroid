package com.tr.ui.people.model;

import java.io.Serializable;
import java.util.List;

/**
 * 人脉首页返回数据对应javaBean
 * @author zq
 *
 */
public class PersonPage implements Serializable {

	private static final long serialVersionUID = -2706563812270452230L;
	
	
	public List<PersonSimple> list;
	public boolean success;
	public int count;
}
