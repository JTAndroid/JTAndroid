package com.tr.model.demand;

/**
 * @ClassName: AmountData.java
 * @author fxtx
 * @Date 2015年3月24日 下午5:32:38
 * @Description: 金额信息封装对象
 */
public class AmountData extends DemandSection {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2026141360175753954L;
	public float beginAmount; // 起始金额
	public float endAmount;// 结束金额
	public String unit;// 金额单位
	public String unitName;// 金额名称

	public AmountData() {
		// TODO Auto-generated constructor stub
	}

	public AmountData(int beginAmount, int endAmount) {
		super();
		this.beginAmount = beginAmount;
		this.endAmount = endAmount;
	}

	/**
	 * 将金额进行转换并显示
	 * 
	 * @param beginAmount
	 * @param endAmount
	 * @return
	 */
	public String getAmountData() {
		StringBuffer sb = new StringBuffer();
		// 规范固定的参数值
		if (beginAmount == endAmount && endAmount == -1) {
			sb.append("不限");
		} else if (beginAmount == -1 && endAmount == 1000) {
			sb.append("1000万以下");
		} else if (beginAmount == 1000 && endAmount == 5000) {
			sb.append("1000万-5000万");
		} else if (beginAmount == 5000 && endAmount == 10000) {
			sb.append("5000万-1亿");
		} else if (beginAmount == 10000 && endAmount == 100000) {
			sb.append("1亿-10亿");
		} else if (beginAmount == 100000 && endAmount == -1) {
			sb.append("10亿以上");
		} else if (beginAmount == -1) {
			sb.append(endAmount + "万以下");
		} else if (endAmount == -1) {
			sb.append(beginAmount + "万以上");
		} else if (endAmount == 0 && beginAmount == 0) {
			sb.append("");
		} else {
			sb.append(beginAmount).append("万").append("-").append(endAmount).append("万");
		}
		return sb.toString();
	}
}
