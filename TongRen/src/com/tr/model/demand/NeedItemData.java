package com.tr.model.demand;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.tr.model.page.IPageBaseItem;

/**
 * @ClassName: NeedItemList.java
 * @author zcs
 * @Date 2015年3月21日 下午2:15:50
 * @Description: 需求列表  对应后台的demandRelation  需求操作对象
 */
public class NeedItemData implements IPageBaseItem {
	/**
	 * 
	 */
	public static final long serialVersionUID = -1630833642511935422L;
	
	public String demandId; //需求id
	public String userId;//用户id
	public String createTime;//创建时间
	public AmountData amount;// 金额信息信息
	public TitleData title;// 标题对象 
	public RelationData type;// 类型
	public RelationData industry;// 行业类型
	public RelationData area;// 地区信息
	public int demandType;// 需求类型 1-投资 2-融资 
	
	
	

	public NeedItemData() {
	}


	/**
	 * 解析json
	 * 
	 * @param jsonObject
	 * @return
	 */
	public static NeedItemData createFactory(JSONObject jsonObject) {
		try {
			NeedItemData bean = new NeedItemData();
			Gson gson = new Gson();
			bean = gson.fromJson(jsonObject.toString(), NeedItemData.class); // 将数据以Gson的形式进行处理
			return bean;
		} catch (Exception e) {
			return null;
		}
	}

}
