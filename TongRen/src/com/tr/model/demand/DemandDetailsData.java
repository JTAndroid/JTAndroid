package com.tr.model.demand;

import java.io.Serializable;
import java.util.List;

import com.tr.model.obj.ResourceBase;

/**
 * @ClassName: DemandDetailsData.java
 * @author fxtx
 * @Date 2015年3月25日 上午9:57:33
 * @Description: 需求详情信息对象
 */
public class DemandDetailsData  implements Serializable {
	/**
	 * 
	 */
	public static final long serialVersionUID = -4371140810961547166L;

	public String id; // 需求id
	public TitleData title;// 需求标题
	public NoteData note; // 附件信息
	public RelationData type;// 类型
	public RelationData industry;// 行业
	public RelationData area;// 地区
	public AmountData amount;// 金额
	public ValueData contact;// 需求联系人
	public ValueData phone;// 需求手机号
	public String createrId;// 创建人id
	public String createrName;// 创建人名称
	public String createTime;// 创建时间
	public int demandType;// 需求类型
	public String tags;// 目录id 逗号分割
	public List<CustomData> customList; // 自定义字段
	public String categoryIds;// 目录
	public DemandASSO asso;// 关联信息
	public List<ChoiceObj> industryObj;// 行业 客户端使用json格式
	public List<ChoiceObj> areaObj;// 地区客户端使用json格式
	public List<ChoiceObj> typeObj;// 类型客户端使用json格式

	public PermissionsData permIds;// 权限控制对象

	public String firstPicPath; // 动态图片路径

}
