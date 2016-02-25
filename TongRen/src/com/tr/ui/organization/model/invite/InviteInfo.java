/**
 * 
 */
package com.tr.ui.organization.model.invite;

import java.io.Serializable;
import java.util.List;

import com.tr.ui.organization.model.invite.InviteUser;

/**
 * 邀请信息
 * @author liubang
 *
 */
public class InviteInfo implements Serializable{

	/**
	 * 
	 */
	public static final long serialVersionUID = 8543529504595705869L;
	public String id;//主键
	public List<InviteUser> inviteUser;//被邀请人
	public String mobileContent;//短信内容
	public String emailContent;//邮箱内容
	public String targetId;//来源id
	public String type;//类型 0 客户 1 人脉
	public long sendUserId;//邀请人id
	public String sendUserName;//邀请人姓名
	
}
