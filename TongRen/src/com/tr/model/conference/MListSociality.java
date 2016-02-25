package com.tr.model.conference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tr.model.home.MGetDynamic;
import com.tr.model.obj.DynamicNews;
import com.tr.model.page.IPageBaseItem;
import com.tr.model.page.IPageBaseItemFactory;
import com.tr.model.page.JTPage;

public class MListSociality implements Serializable {
	private static final long serialVersionUID = -4343595243237326181L;

	private List<MSociality> listSocial;
    private JTPage jtPage;
    private int count;
    
    
	
	public int getCount() {
		return count;
	}


	public void setCount(int count) {
		this.count = count;
	}


	public JTPage getJtPage() {
		return jtPage;
	}


	public void setJtPage(JTPage jtPage) {
		this.jtPage = jtPage;
	}
	
	@Override
	public String toString() {
		return "MListSociality [jtPage=" + jtPage + "]";
	}

	public static Object createFactory(JSONObject jsonObject) {
		if (jsonObject == null) {
			return null;
		}
		MListSociality query = new MListSociality();
		try {
			if (jsonObject != null) {
				MListSociality self = null;
				if (jsonObject != null) {
					self = new MListSociality();
					self.jtPage = JTPage.createSocialityListFactory(jsonObject);
				}
				return self;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return query;
	}
	
	public static Object createFactory(JSONObject jsonObject,Object obj) {
		Gson gson = new Gson();
		if (jsonObject == null) {
			return null;
		}
		MListSociality query = new MListSociality();
		try {
			if (jsonObject != null) {
//				query = JSON.parseObject(jsonObject.toString(), MListSociality.class);
//				query = (MListSociality) Util.getParseJsonObject(jsonObject,  MListSociality.class);
				String strKey = "listSocial";
				String jsonString = jsonObject.getJSONArray(strKey).toString();
				query.listSocial = gson.fromJson(jsonString, new TypeToken<List<MSociality>>(){}.getType());
				strKey = "count";
				if(!jsonObject.isNull(strKey)){
					query.count = jsonObject.optInt(strKey);
				}
			}
			return query;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return query;
	}


	public List<MSociality> getListSocial() {
		if(listSocial == null){
			listSocial = new ArrayList<MSociality>();
		}
		return listSocial;
	}

	public void setListSocial(List<MSociality> listSocial) {
		this.listSocial = listSocial;
	}

}
