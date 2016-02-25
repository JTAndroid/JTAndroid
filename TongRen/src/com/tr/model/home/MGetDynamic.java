package com.tr.model.home;

import java.io.Serializable;

import org.json.JSONObject;

import android.util.Log;

import com.tr.model.obj.DynamicNews;
import com.tr.model.page.IPageBaseItem;
import com.tr.model.page.IPageBaseItemFactory;
import com.tr.model.page.JTPage;

public class MGetDynamic implements IPageBaseItemFactory, Serializable{

    private static final long serialVersionUID = 198239944230064422L;
    private JTPage jtPage;
	
	
	public JTPage getJtPage() {
		return jtPage;
	}


	public void setJtPage(JTPage jtPage) {
		this.jtPage = jtPage;
	}


	public static MGetDynamic createFactory(JSONObject jsonObject) {
		MGetDynamic self = null;
		if (jsonObject != null) {
			self = new MGetDynamic();
			self.jtPage = JTPage.createDynamicFactory(jsonObject, "listDynamicNews", self);
		}
		return self;
		
	}
	
	public static MGetDynamic createNewFactory(JSONObject jsonObject) {
		MGetDynamic self = null;
		if (jsonObject != null) {
			self = new MGetDynamic();
			self.jtPage = JTPage.createDynamicFactory(jsonObject, "result", self);
		}
		return self;
		
	}

	@Override
	public String toString() {
		return "MGetDynamic [jtPage=" + jtPage + "]";
	}


	@Override
	public IPageBaseItem paserByObject(JSONObject jsonObj) {
		// TODO Auto-generated method stub
		try{
			DynamicNews flow = DynamicNews.createFactory(jsonObj);
			return flow;
		}catch(Exception e){
			
		}
		return null;
	}
	
	

}