/**
 * 
 */
package com.tr.ui.organization.model.hight;

import java.io.Serializable;
import java.util.List;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;

/**
 * 高层治理
 * 
 * @author liubang
 * 
 */
public class CustomerHight implements Serializable {
	public static final long serialVersionUID = -6596610241625631995L;
	public long id;// 主键/客户id
	public List<CustomerHightInfo> dshList;// 董事会
	public List<CustomerHightInfo> jshList;// 监事会
	public List<CustomerHightInfo> ggList;// 高管
	public List<CustomerHightInfo> ggjzList;// 高管兼职
	public List<CustomerHightInfo> gczlList;// 高层资料
	public String stockCode; // 股票代码
	public String taskId;// 附件组
	public List<CustomerPersonalLine> personalLineList;// 自定义属性


	@Override
	public String toString() {
		return "CustomerHight [id=" + id + ", dshList=" + dshList
				+ ", jshList=" + jshList + ", ggList=" + ggList + ", ggjzList="
				+ ggjzList + ", gczlList=" + gczlList + ", stockCode="
				+ stockCode + ", taskId=" + taskId + ", personalLineList="
				+ personalLineList + "]";
	}


}
