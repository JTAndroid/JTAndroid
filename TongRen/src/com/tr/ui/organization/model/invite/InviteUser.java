/**
 * 
 */
package com.tr.ui.organization.model.invite;

import java.io.Serializable;

/**
 * 被邀请人对象
 * @author liubang
 *
 */
public class InviteUser implements Serializable{

	/**
	 * 
	 */
	public static final long serialVersionUID = -173568294648267290L;
	public String name;//被邀请人姓名
	public String mobileOrEmail;//手机号或者邮箱
}
