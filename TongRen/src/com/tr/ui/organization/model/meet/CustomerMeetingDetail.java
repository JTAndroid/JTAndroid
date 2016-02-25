package com.tr.ui.organization.model.meet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.tr.model.demand.ASSORPOK;
import com.tr.ui.organization.model.CustomerTag;



public class CustomerMeetingDetail implements Serializable {

	/**
	 * 
	 */
	public static final long serialVersionUID = 6777190171450787734L;
	
	public Long id;//主键id  
	public Long customerId;//客户id
	public Long personId; // 人脉详情ID
	public String meetDate;//会面时间
	public String time;//会面日期
	public String minTime;//小时
	public String color;//颜色  0无颜色   1绿色   2蓝色  3橙色  4红色
	public String repeadType;//提醒类型 0:不提醒 1:每天提醒 2:每周提醒 3:每月提醒  4:每年提醒
	public String remindTime;//:提醒时间"
	public String remindType;//,"提醒类型  0不提醒  1分钟  2小时  3天"
	public String title;//标题
	public String content;//内容
	public String address;//会面地点
	public List<CustomerTag> tags =  new ArrayList<CustomerTag>();//标签
	public String ctime;//创建时间
	public String taskId;//附件组id
	public String picId;//图片视频id",
	public String remark;//备注
	public ASSORPOK relevance = new ASSORPOK();//关联信息
	public Long personType;  	
}
