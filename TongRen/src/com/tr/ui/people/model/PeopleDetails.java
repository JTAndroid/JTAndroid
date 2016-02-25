package com.tr.ui.people.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.tr.model.demand.ASSORPOK;

public class PeopleDetails implements Serializable{
	
	public List<PersonTagRelation> tid;  
	public int type;	
	public ArrayList<Category> categoryList = new ArrayList<Category>() ;
	public int fromType ; //"人脉来源类型：1-我创建的；2-我保存的；3-我收藏的；9-其他",
	public Person people;
	public Boolean success;
	/**如果people对象代表用户，且该用户已经被我保存为人脉则此字段表示保存后的人脉的id；如果此字段为空，则说明我没有把该用户保存为我的人脉*/
	public Long personIdAfterConvert;// add by zhongshan
	public ASSORPOK asso ;	
	public boolean isOrg;
	
	
	public Person getPeople() {
		if(people == null){
			people = new Person();
		}
		return people;
	}
	public void setPeople(Person people) {
		this.people = people;
	}
	

}
