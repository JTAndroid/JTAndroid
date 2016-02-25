package com.tr.model.demand;

import org.json.JSONObject;

import com.google.gson.Gson;

/**
 * @ClassName: AttachmentData.java
 * @author fxtx
 * @Date 2015年3月26日 下午3:12:03
 * @Description: 介绍内容 附件+图片+文本说明
 */
public class IntroduceData extends DemandSection {

	/**
	 * 
	 */
	public static final long serialVersionUID = -2497756436340698779L;
	public String taskID;
	/**
	 * 附件
	 */
	public DemandAttFile attFile;
	/**
	 * 视频/图片
	 */
	public DemandPvFile pvFile;


	/**
	 * 将json 转换
	 * 
	 * @param object
	 * @return
	 */
	public static IntroduceData createFactory(JSONObject object) {
		IntroduceData bean = new IntroduceData();
		Gson gson = new Gson();
		bean = gson.fromJson(object.toString(), IntroduceData.class); // 将数据以Gson的形式进行处理
		return bean;
	}

}
