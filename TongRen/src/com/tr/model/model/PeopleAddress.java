package com.tr.model.model;

import com.utils.string.StringUtils;

/**
 * 地址
 * @author wenbin
 *
 */
public class PeopleAddress extends BaseObject {

	public static final long serialVersionUID = -3376130394695782352L;
	
	/**主键*/
	public Long id;
	/**通讯类型： 1-联系方式，2-投资需求，3-融资需求，4-专家需求，5-专家身份*/
	public int parentType;
	/**联系方式类型：1-住宅地址，2-商务地址，N-自定义， 非联系方式时为空*/
	public PeopleSelectTag typeTag;
	/**地区类型：0-国内，1-国外*/
	public int areaType;
//	/**国家*/
//	public Integer country;
//	/**省*/
//	public Integer state;
//	/**城市*/
//	public Integer city;
//	/**县区*/
//	public Integer county;	
	///**国家*/
//	public String countryName;
	/**省/洲*/
	public String stateName;
	/**城市/国*/
	public String cityName;
	/**县区*/
	public String countyName;
	/**地址*/
	public String address;
	/**邮编*/
	public String postalCode;
	
	/**************** 人脉详情需要显示字段start*******************/
	/**地区类型：1-国内，2-国外*/
	public String areaTypeName;
	/**************** 人脉详情需要显示字段end*******************/
	
	public final static int type_peopleDemandCommon=1;//需求相关，获取地址
	public final static int type_peopleAddress=2;//联系方式的地址
	public String getAddressString(int type){
		StringBuffer data=new StringBuffer("");
		if(type==type_peopleDemandCommon){
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
				data.append(" ");
			}
			
			if(!StringUtils.isEmpty(address)){
				data.append(address);
			}
		}else if(type_peopleAddress==type){
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
		}
		
		
		
		return data.toString();
		
	}
	
	
	
	
}
