package com.tr.model.knowledge;

import java.io.Serializable;

import android.util.Log;

import com.utils.pinyin.PingYinUtil;


/**
 * 
 * @author gushi
 *
 */
public class UserTag implements Serializable {
	
	String TAG = "UserTag";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6172744307813200155L;
	
	private int id;
	private String name;
	private String count;
	private String spell; //汉语拼音拼写
	private boolean checked = false;
	
	public UserTag(String name, String count) {
		super();
		String MSG = "UserTag()";
		this.name = name;
		this.count = count;
		spell = PingYinUtil.getPingYin(name).toUpperCase();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getSpell() {
		return spell;
	}

	public void setSpell(String spell) {
		this.spell = spell;
	}

	
	
}
