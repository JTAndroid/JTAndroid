package com.tr.model.model;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import com.tr.model.obj.IMGroupCategory;

/**
 * @author wenbin
 *
 */
public class BaseObject implements Serializable{

	public static final long serialVersionUID = 5391806681280589889L;
	
	public Long createUserId;
	public Long updateUserId;
	public String createUserName;
	public String updateUserName;
	public String createTime;
	public String updateTime;
	
	public boolean isFocuse = false;
	
		
}
