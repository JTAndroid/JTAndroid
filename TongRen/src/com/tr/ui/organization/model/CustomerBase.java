package com.tr.ui.organization.model;

import java.io.Serializable;
/**
 * 客户基础类
 * @author liubang
 *
 */
public class CustomerBase implements Serializable{

	/**
	 * 
	 */
	public static final long serialVersionUID = -842795978942841778L;
	public String nameFirst;	//拼音第一个字母
	public String nameIndex;	//拼音首字母
	public String nameIndexAll;//全拼音
	public long createById;	//'创建人id',
	public String utime;		//更新时间
	public String ctime;		//创建时间
	@Override
	public String toString() {
		return "CustomerBase [nameFirst=" + nameFirst + ", nameIndex="
				+ nameIndex + ", nameIndexAll=" + nameIndexAll
				+ ", createById=" + createById + ", utime=" + utime
				+ ", ctime=" + ctime + "]";
	}


}
