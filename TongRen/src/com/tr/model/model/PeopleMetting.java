package com.tr.model.model;

import java.util.List;

import com.utils.string.StringUtils;


/**
 * 人脉 会面情况
 * @author wenbin
 *
 */
public class PeopleMetting extends BaseObject {

	public static final long serialVersionUID = 1659523155523241696L;
	
	
	/**主键*/
	public Long id;
	/**主题*/
	public String subject;
	/**时间起*/
	public String startTime;
	/**时间止*/
	public String endTime;
	/**地区类型：0-国内，1-国外*/
	public Integer areaType;
	/**省*/
	public String stateName;
	/**城市*/
	public String cityName;
	/**区县*/
	public String countyName;
	/**地址*/
	public String address;
	/**描述*/
	public String description;
	/**参与人*/
	public String participater;
	
	/**自定义字段*/
	public List<PeoplePersonalLine> personalLineList;
	
	public PeopleMetting(){
		
	}
	
	public PeopleMetting(String startTime){
		this.startTime = startTime;
	}

	public String getAddressString(){
		StringBuffer data=new StringBuffer("");
		if(areaType==0){//国内
				if(!StringUtils.isEmpty(stateName)){
					data.append(stateName);
					data.append(" ");
				}
				if(!StringUtils.isEmpty(cityName)){
					data.append(cityName);
					data.append(" ");
				}
				if(!StringUtils.isEmpty(countyName)){
					data.append(countyName);
				}
			}else {
				if(!StringUtils.isEmpty(stateName)){
					data.append(stateName);
					data.append(" ");
				}
				if(!StringUtils.isEmpty(cityName)){
					data.append(cityName);
				}
			}
		
		return data.toString();
	}
	
	
	
	
	
}
