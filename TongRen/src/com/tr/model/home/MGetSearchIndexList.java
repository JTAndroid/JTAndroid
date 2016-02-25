package com.tr.model.home; 

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tr.model.obj.SearchResult;
import com.tr.model.obj.SearchResultList;
import com.tr.model.page.IPageBaseItem;
import com.tr.model.page.IPageBaseItemFactory;

public class MGetSearchIndexList  implements IPageBaseItemFactory, Serializable{

    private static final long serialVersionUID = 198239944223064422L;

	public static SearchResultList createFactory(JSONObject jsonObject) {
		SearchResultList srl = new SearchResultList();
		if (jsonObject != null) {
			srl.setOrganList(getList(jsonObject, "organList"));
			
			srl.setPersonList(getList(jsonObject, "personList"));
			
			srl.setDemandList(getList(jsonObject, "demandList"));
			
			srl.setMeetingList(getList(jsonObject, "meetingList"));
			
			srl.setKnowledgeList(getList(jsonObject, "knowledgeList"));
		}
		return srl;
	}

	@Override
	public IPageBaseItem paserByObject(JSONObject jsonObj) {
		try{
			SearchResult result = SearchResult.createFactory(jsonObj);
			return result;
		}catch(Exception e){
			
		}
		return null;
	}
	
	public static SearchResult paserObject(JSONObject jsonObj) {
		try{
			SearchResult result = SearchResult.createFactory(jsonObj);
			return result;
		}catch(Exception e){
			
		}
		return null;
	}
	
	public static ArrayList<SearchResult> getList(JSONObject jsonObject,
			String tagName) {
		ArrayList<SearchResult> lists = new ArrayList<SearchResult>();
		JSONArray arr = jsonObject.optJSONArray(tagName);
		try {
			if (arr != null) {
				for (int i = 0; i < arr.length(); i++) {
					if (!arr.isNull(i)) {
						SearchResult item = paserObject((JSONObject)(arr.get(i)));
						if (item != null)
							lists.add(item);
					}
				}
			}
		} catch (Exception e) {
		}
		return lists;
	}

}
 