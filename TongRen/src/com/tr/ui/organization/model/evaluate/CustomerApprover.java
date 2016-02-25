package com.tr.ui.organization.model.evaluate;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * <p>
 * Title: CustomerApprove.java<／p>
 * <p>
 * Description: 组织客户赞成类<／p>
 * 
 * @author wfl
 * @date 2015-3-13
 * @version 1.0
 */
public class CustomerApprover implements Serializable {

	public static final long serialVersionUID = -7756926916190651916L;

	public long id; // 主键
	public long ueid; // 用户评价实体id
	public long userId; // 用户赞同者id
	public Timestamp agreeTime; // 赞同者同意时间

}
