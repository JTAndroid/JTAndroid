/**
 * 
 */
package com.tr.ui.organization.model.stock;

import java.io.Serializable;

/**
 * 股东明细
 * @author liubang
 *
 */
public class CustomerStockInfo implements Serializable{
	public static final long serialVersionUID = -5269261932968686308L;
	public int id;//序号
	public String name;//股东名称
	public String stockQty;//持股数量
	public String type;//持股性质
	public String stockPercent;//占总股本比例
	public String stockChange;//持股变动数
	@Override
	public String toString() {
		return "CustomerStockInfo [id=" + id + ", name=" + name + ", stockQty="
				+ stockQty + ", type=" + type + ", stockPercent="
				+ stockPercent + ", stockChange=" + stockChange + "]";
	}
	
	
	
}
