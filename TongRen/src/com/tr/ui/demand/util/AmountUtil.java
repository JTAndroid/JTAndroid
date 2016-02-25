package com.tr.ui.demand.util;

import java.util.HashMap;
import java.util.Map;

import com.tr.model.demand.AmountData;

/**
 * @ClassName: AmountUtil.java
 * @author fxtx
 * @Date 2015年3月30日 下午12:57:22
 * @Description: 金额工具 主要是将系统提供的几种金额信息转换成接口需要的字段效果
 */
public class AmountUtil {
	private static Map<String, AmountData> amount = new HashMap<String, AmountData>();
	static {
		amount.put("不限", new AmountData(-1, -1));
		amount.put("1000万以下", new AmountData(-1, 1000));
		amount.put("1000万-5000万", new AmountData(1000, 5000));
		amount.put("5000万-1亿", new AmountData(5000, 10000));
		amount.put("1亿-10亿", new AmountData(10000, 100000));
		amount.put("10亿以上", new AmountData(100000, -1));
	}
	

	/**
	 * 通过上面的字段 进行获取值
	 * @param unit
	 * @param unitName
	 * @param str
	 * @return
	 */
	public static AmountData strToAmount(String unit, String unitName, String str) {
		// 规范固定的参数值
		AmountData data = amount.get(str);
		if (data != null) {
			data.unit = unit;
			data.unitName = unitName;
		} else {
			data = new AmountData();
			data.beginAmount = -1;
			data.endAmount = -1;
			data.unit = unit;
			data.unitName = unitName;
		}
		return data;
	}
}
