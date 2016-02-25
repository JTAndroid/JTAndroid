/**
 * 
 */
package com.tr.ui.organization.model.hight;

import java.io.Serializable;
import java.util.List;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;

/**
 * 高层治理——传入参数
 * 
 * @author liubang
 * 
 */
public class CustomerHight_re implements Serializable {
	public static final long serialVersionUID = -6596610241625631995L;
	public long customerId = 2;// 主键/客户id
	@Override
	public String toString() {
		return "CustomerHight_re [customerId=" + customerId + "]";
	}
	
}
