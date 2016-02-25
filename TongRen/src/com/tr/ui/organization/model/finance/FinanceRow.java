/**
 * 
 */
package com.tr.ui.organization.model.finance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.tr.ui.organization.model.profile.CustomerPersonalLine;
/**
 * 财务分析数据行
 * @author hdy
 * @date 2015-3-25
 */
public class FinanceRow implements Serializable {
	/**
	 * 
	 */
	public static final long serialVersionUID = -3341198856314735283L;
	public String id;//主键 
	public String pid;//父id
	public String rowName;//行标题
	public String rowValue;//行值
	@Override
	public String toString() {
		return "FinanceRow [id=" + id + ", pid=" + pid + ", rowName=" + rowName
				+ ", rowValue=" + rowValue + "]";
	}


	
	
}
