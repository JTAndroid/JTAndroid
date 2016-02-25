package com.tr.model.model;

import java.util.ArrayList;
import java.util.List;

import com.tr.model.obj.IMGroupCategory;
import com.tr.model.obj.InvestType;
import com.tr.model.obj.Trade;
import com.tr.ui.relationship.RelationDetails1Activity.KvAdpter;
import com.tr.ui.relationship.RelationDetails1Activity.ShowData;
import com.utils.string.StringUtils;

/**
 * 人脉：工作经历
 * @author wenbin
 *
 */
public class PeopleWorkExperience extends BaseObject {

	public static final long serialVersionUID = 2485688432817917896L;
	
	/**主键*/
	public Long id;
	/**时间起*/
	public String startTime;
	/**时间止*/
	public String endTime;
	/**公司名称*/
	public String company;
	/**公司行业*/
	public String companyIndustry;
	/**证明人*/
	public String references;
	/**联系电话*/
	public String tel;
	/**部门*/
	public String department;
	/**职务*/
	public String position;
	/**描述*/
	public String description;
	
	/**将本经历作为当前身份*/
	public boolean currentStatus;
	/**同事关系*/
	public ArrayList<PeopleColleagueRelationship> colleagueRelationshipList;
	/**自定义字段*/
	public ArrayList<PeoplePersonalLine> personalLineList;
	
	
	
	
	public ArrayList<IMGroupCategory>  companyIndustryMake(){
		if(!StringUtils.isEmpty(companyIndustry)){
			String names[]=companyIndustry.split(",");
			if(names.length>0){
				ArrayList<IMGroupCategory> tradeList=new ArrayList<IMGroupCategory>();
				for(int i=0;i<names.length;i++){
					if(!StringUtils.isEmpty(names[i])){
						Trade trade=new Trade();
						trade.mID="";
						trade.mTitle=names[i];
						tradeList.add(trade);
					}
				}
				return tradeList;
			}
		}
		return null;
	}
	
	public String showCompanyIndustry(){
		String data="";
		if(!StringUtils.isEmpty(companyIndustry)){
			data=companyIndustry.replace(',', ' ');
		}
		return data;
	}
	
	

	
	public void setCompanyIndustry(List<IMGroupCategory> listItem){
		if(listItem==null||listItem.size()==0){
			return;
		}
		companyIndustry=null;
		StringBuffer namesbf=new StringBuffer();
		Trade trade=null;
		for(IMGroupCategory iMGroupCategory:listItem){
			trade=(Trade)iMGroupCategory;
			namesbf.append(trade.getName()).append(",");
		}
		companyIndustry=namesbf.toString();
	}
	
	public ArrayList<ShowData> getListData(){
		ArrayList<ShowData> showDataArrList=new ArrayList<ShowData>();
		ShowData timeShowData=new ShowData();
		timeShowData.key=KvAdpter.TAG_TIME;
		if(!StringUtils.isEmpty(startTime)){
			timeShowData.value=startTime;
		}
		if(!StringUtils.isEmpty(endTime)){
			timeShowData.value1=endTime;
		}
		showDataArrList.add(timeShowData);
		
		ShowData companyData=new ShowData();
		companyData.key="公司名称";
		companyData.value=company;
		showDataArrList.add(companyData);
		
		ShowData companyIndustryData=new ShowData();
		companyIndustryData.key="公司行业";
		companyIndustryData.value=companyIndustry;
		showDataArrList.add(companyIndustryData);
		
		ShowData referencesData=new ShowData();
		referencesData.key="证明人";
		referencesData.value=references;
		showDataArrList.add(referencesData);
		
		ShowData telData=new ShowData();
		telData.key="联系电话";
		telData.value=tel;
		showDataArrList.add(telData);
		
		ShowData departmentData=new ShowData();
		departmentData.key="部门";
		departmentData.value=department;
		showDataArrList.add(departmentData);
		
		ShowData positionData=new ShowData();
		positionData.key="职务";
		positionData.value=position;
		showDataArrList.add(positionData);
		
		ShowData descriptionData=new ShowData();
		descriptionData.key="描述";
		descriptionData.value=description;
		showDataArrList.add(descriptionData);
	
		
		if(colleagueRelationshipList!=null){
			StringBuffer sb=new StringBuffer("");
			for(PeopleColleagueRelationship peopleCommunityRelationship: colleagueRelationshipList){
				if(peopleCommunityRelationship.typeTag!=null){
						sb.append(peopleCommunityRelationship.typeTag.name);
						sb.append(":");
						sb.append(peopleCommunityRelationship.content);
				}
				if(colleagueRelationshipList.indexOf(peopleCommunityRelationship)!=colleagueRelationshipList.size()-1){//最后一个
						sb.append("\n");
				}
			}
			
			ShowData colleagueRelationshipListData=new ShowData();
			colleagueRelationshipListData.key="关系";
			colleagueRelationshipListData.value=sb.toString();
			showDataArrList.add(colleagueRelationshipListData);
		}

		if(personalLineList!=null){
			for(PeoplePersonalLine peopleAddress:personalLineList){
				ShowData showData=new ShowData();
				showData.key=peopleAddress.name;
				showData.value=peopleAddress.content;
				showDataArrList.add(showData);
			}
		}
		
		return showDataArrList;
	}
	
}
