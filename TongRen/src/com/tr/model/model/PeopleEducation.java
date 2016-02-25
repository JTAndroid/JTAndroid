package com.tr.model.model;

import java.util.ArrayList;
import java.util.List;

import com.tr.ui.relationship.RelationDetails1Activity.KvAdpter;
import com.tr.ui.relationship.RelationDetails1Activity.ShowData;
import com.utils.string.StringUtils;

/**
 * 人脉：教育经历
 * @author wenbin
 *
 */
public class PeopleEducation extends BaseObject {

	public static final long serialVersionUID = 1018328642974424367L;
	
	/**主键*/
	public Long id;
	/**时间起*/
	public String startTime;
	/**时间止*/
	public String endTime;
	/**学校*/
	public String school;
	/**学院*/
	public String college;
	/**专业类别*/
	public String specialty;
	/**学历*/
	public String educationalBackgroundType;
	/**学位*/
	public String degreeType;
	/**描述*/
	public String description;
	
	
	/**海外学习：0-否，1-是*/
	public int studyAbroadType;
	/**同学关系*/
	public ArrayList<PeopleStudentsRelationship> studentsRelationshipList;
	/**外语语种*/
	public ArrayList<PeopleForeignLanguage> foreignLanguageList;
	/**自定义字段*/
	public ArrayList<PeoplePersonalLine> personalLineList;
	
	
	
	
	public String getStudyAbroadType(int type){
		String data=null;
		if(type==0){
			data="否";
		}else {
			data="是";
		}
		return data;
	}

	public int getStudyAbroadType(String text){
		if(!StringUtils.isEmpty(text)){
			int data;
			if(text.equals("是")){
				data=1;
			}else {
				data=0;
			}
			return data;
		}
		return 0;
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
		
		ShowData schoolData=new ShowData();
		schoolData.key="毕业院校";
		
		if(school==null){
			school="";
		}
		if(college==null){
			college="";
		}
		if(specialty==null){
			specialty="";
		}
		schoolData.value=school + " "+college +" "+specialty;
		schoolData.value1=college;
		showDataArrList.add(schoolData);
		
		ShowData referencesData=new ShowData();
		referencesData.key="学历学位";
		String studyAbroadTypeStr;
		if(studyAbroadType==0){
			studyAbroadTypeStr="无海外学习经验";
		}else{
			studyAbroadTypeStr="有海外学习经验";
		}
		if(educationalBackgroundType==null){
			educationalBackgroundType="";
		}
		if(degreeType==null){
			degreeType="";
		}
		referencesData.value=(educationalBackgroundType + " "+degreeType +" "+studyAbroadTypeStr).trim();
		showDataArrList.add(referencesData);
		
		if(foreignLanguageList!=null){
			StringBuffer sb=new StringBuffer("");
			for(PeopleForeignLanguage peopleCommunityRelationship: foreignLanguageList){
				sb.append(peopleCommunityRelationship.type);
				sb.append(peopleCommunityRelationship.levelType);

				if(foreignLanguageList.indexOf(peopleCommunityRelationship)!=foreignLanguageList.size()-1){//最后一个
						sb.append(" ");
				}
			}
			
			ShowData colleagueRelationshipListData=new ShowData();
			colleagueRelationshipListData.key="外语水平";
			colleagueRelationshipListData.value=sb.toString();
			showDataArrList.add(colleagueRelationshipListData);
		}
		
		ShowData descriptionData=new ShowData();
		descriptionData.key="描述";
		descriptionData.value=description;
		showDataArrList.add(descriptionData);
		
		if(studentsRelationshipList!=null){
			StringBuffer sb=new StringBuffer("");
			for(PeopleStudentsRelationship peopleCommunityRelationship: studentsRelationshipList){
				if(peopleCommunityRelationship.typeTag!=null){
						sb.append(peopleCommunityRelationship.typeTag.name);
						sb.append(":");
						sb.append(peopleCommunityRelationship.content);
				}
				if(studentsRelationshipList.indexOf(peopleCommunityRelationship)!=studentsRelationshipList.size()-1){//最后一个
						sb.append("\n");
				}
			}
			
			ShowData colleagueRelationshipListData=new ShowData();
			colleagueRelationshipListData.key="同学关系";
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
