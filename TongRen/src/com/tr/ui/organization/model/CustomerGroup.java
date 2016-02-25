package com.tr.ui.organization.model;

import java.io.Serializable;


/**
 * 客户分组
 * @author liubang
 *
 */
public class CustomerGroup implements Serializable  {
    public static final long serialVersionUID = -8888167310982523166L;
	public long id;
	public String name;
	public String ctime;
	public long creatorId;
	public long parentId;
	public long count;//当前分组下的客户数

}
