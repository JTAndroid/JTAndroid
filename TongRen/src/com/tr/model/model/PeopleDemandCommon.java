package com.tr.model.model;

import java.util.ArrayList;
import java.util.List;

import com.tr.model.obj.IMGroupCategory;
import com.tr.model.obj.InvestType;
import com.tr.model.obj.Trade;
import com.utils.string.StringUtils;

/**
 * 人脉：投资、融资、专家需求、专家身份
 * @author wenbin
 *
 */
public class PeopleDemandCommon extends BaseObject {

	public static final long serialVersionUID = 3691972358064307751L;
	
	/**主键*/
	public Long id ;
	/**所属模块： 1-投资，2-融资，3-专家需求，4-专家身份*/
	public Integer parentType;
	/**地址*/
	public PeopleAddress address;
	/**行业*/
	public String industryIds;
	/**行业*/
	public String industryNames;
	/**类型*/
	public String typeIds;
	/**类型*/
	public String typeNames;
	/**附加信息*/
	public String otherInformation;
	/**自定义字段*/
	public ArrayList<PeoplePersonalLine> personalLineList;
	
	
	
	public String showIndustryNames(){
		String data="";
		if(!StringUtils.isEmpty(industryNames)){
			data=industryNames.replace(',', ' ');
		}
		return data;
	}
	
	public String showTypeNames(){
		String data="";
		if(!StringUtils.isEmpty(typeNames)){
			data=typeNames.replace(',', ' ');
		}
		return data;
	}
	
	public ArrayList<IMGroupCategory>  industryMake(){
		if(!StringUtils.isEmpty(industryIds)){
			String ids[]=industryIds.split(",");
			String names[]=industryNames.split(",");
			if(ids.length>0){
				ArrayList<IMGroupCategory> tradeList=new ArrayList<IMGroupCategory>();
				for(int i=0;i<ids.length;i++){
					Trade trade=new Trade();
					trade.mID=ids[i];
					trade.mTitle=names[i];
					tradeList.add(trade);
				}
				return tradeList;
			}
		}
		return null;
	}
	
	public void setIndustry(List<IMGroupCategory> listItem){
		if(listItem==null||listItem.size()==0){
			industryIds=null;
			industryNames=null;
			return;
		}
		industryIds=null;
		industryNames=null;
		StringBuffer idsbf=new StringBuffer();
		StringBuffer namesbf=new StringBuffer();
		Trade trade=null;
		for(IMGroupCategory iMGroupCategory:listItem){
			trade=(Trade)iMGroupCategory;
			idsbf.append(trade.getID()).append(",");
			namesbf.append(trade.getName()).append(",");
		}
		industryIds=idsbf.toString();
		industryNames=namesbf.toString();
	}
	
	public ArrayList<IMGroupCategory>  typeMake(){
		if(!StringUtils.isEmpty(typeIds)){
			String ids[]=typeIds.split(",");
			String names[]=typeNames.split(",");
			if(ids.length>0){
				ArrayList<IMGroupCategory> tradeList=new ArrayList<IMGroupCategory>();
				for(int i=0;i<ids.length;i++){
					InvestType trade=new InvestType();
					trade.mID=ids[i];
					trade.mTitle=names[i];
					tradeList.add(trade);
				}
				return tradeList;
			}
		}
		return null;
	}
	
	public void setType(List<IMGroupCategory> listItem){
		if(listItem==null||listItem.size()==0){
			typeIds=null;
			typeNames=null;
			return;
		}
		typeIds=null;
		typeNames=null;
		StringBuffer idsbf=new StringBuffer();
		StringBuffer namesbf=new StringBuffer();
		InvestType trade=null;
		for(IMGroupCategory iMGroupCategory:listItem){
			trade=(InvestType)iMGroupCategory;
			idsbf.append(trade.getID()).append(",");
			namesbf.append(trade.getName()).append(",");
		}
		typeIds=idsbf.toString();
		typeNames=namesbf.toString();
	}
	
}
