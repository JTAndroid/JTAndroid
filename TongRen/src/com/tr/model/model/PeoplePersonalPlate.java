package com.tr.model.model;

import java.util.ArrayList;
import java.util.List;

import com.tr.ui.relationship.RelationDetails1Activity.ShowData;
import com.utils.string.StringUtils;


/**
 * 人脉 自定义板块
 * @author wenbin
 *
 */
public class PeoplePersonalPlate extends BaseObject {

	public static final long serialVersionUID = -991356412866578107L;
	
	
	/**主键*/
	public Long id;
	/**自定义板块代码*/
	public String code;
	/**自定义板块名称*/
	public String name;
	/**内容*/
	public List<PeoplePersonalLine> personalLineList;
	
   
	
	public ArrayList<ShowData>  getListData(){
		ArrayList<ShowData> contactMobileList=new ArrayList<ShowData>();
		
		if(personalLineList!=null&&personalLineList.size()!=0){
			for(PeoplePersonalLine peoplePersonalLine:personalLineList){
				ShowData  showData=new ShowData();
				if(!StringUtils.isEmpty(peoplePersonalLine.name)){
					showData.key=peoplePersonalLine.name;
					showData.value=peoplePersonalLine.content;	
					contactMobileList.add(showData);
				}
			}
		}
		
		return contactMobileList;
	}
	
}
