package com.tr.model.obj;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import com.tr.R;

/** 选项类 */
public abstract class IMGroupCategory implements Serializable {

	private static final long serialVersionUID = 1L;
	boolean isFocuse = false;
	int resFocuse = R.color.item_yellow;
	int resdefault = R.color.item_gray;
	public static int type_moneyType = 0;
	public static int type_moneyRange = 1;
	public static int type_area = 2;
	public static int type_invest = 3;
	public static int type_trade = 4;
	
	int type = 0;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getResFocuse() {
		return resFocuse;
	}

	public void setResFocuse(int resFocuse) {
		this.resFocuse = resFocuse;
	}

	public int getResdefault() {
		return resdefault;
	}
//
//	public void setResdefault(int resdefault) {
//		this.resdefault = resdefault;
//	}

	public void setFocuse(boolean isFocuse) {
		this.isFocuse = isFocuse;
	}

	public boolean isFocuse() {
		return isFocuse;
	}

	public abstract void initWithJson(JSONObject jsonObject) throws JSONException,
	MalformedURLException, ParseException;

	public abstract JSONObject toJSONObject() throws JSONException;
	public abstract String getName() ;

}
