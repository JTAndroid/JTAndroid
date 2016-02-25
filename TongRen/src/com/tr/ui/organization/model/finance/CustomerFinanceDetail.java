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
 *财务分析
 * @author liubang
 *
 */
public class CustomerFinanceDetail implements Serializable {
	/**
	 * 
	 */
	public static final long serialVersionUID = -3341198856314735283L;
	public String id;//
	public String year;//年份
	public String stockCode;//证券号
	public String date;//日期
	public int quarter;//季度
	public List<FinanceRow> data;//数据
	@Override
	public String toString() {
		return "CustomerFinanceDetail [id=" + id + ", year=" + year
				+ ", stockCode=" + stockCode + ", date=" + date + ", quarter="
				+ quarter + ", data=" + data + "]";
	}
	

	
}
