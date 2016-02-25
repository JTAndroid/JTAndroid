package com.tr.model.model;

import java.io.Serializable;

/**接收人信息
 * @author liuyang
 * @createTime：2013-11-21 14:36:48
 */
public class ReceiversInfo implements Serializable {

	public static final long serialVersionUID = 4341300097772762608L;
	public long userId;//用户id
	public int userStatus;//阅读状态
	public int type;//0 通过权限设置而来，1通过@而来


	
}
