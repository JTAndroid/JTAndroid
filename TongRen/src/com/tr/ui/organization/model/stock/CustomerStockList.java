package com.tr.ui.organization.model.stock;

import java.io.Serializable;
import java.util.List;

public class CustomerStockList implements Serializable {
	
	//流通股东
	public static final long serialVersionUID = 7057752587584051251L;
	public long id;// 主键
	public String stkcd;// 证券号
	public String year;// 报告年份
	public String date;// 报告期
	public String reportDate;// 公告日期
	public List<CustomerStockInfo> stockInfo;
	public String total;// 合计持股数量
	public String percent;// 合计持股比例

	@Override
	public String toString() {
		return "CustomerTenStock [id=" + id + ", stkcd=" + stkcd + ", year="
				+ year + ", date=" + date + ", reportDate=" + reportDate
				+ ", stockInfo=" + stockInfo + ", total=" + total
				+ ", percent=" + percent + "]";
	}
}
