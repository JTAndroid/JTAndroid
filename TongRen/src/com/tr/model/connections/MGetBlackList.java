package com.tr.model.connections;

import java.io.Serializable;

import org.json.JSONObject;

import com.tr.model.obj.ConnectionsMini;
import com.tr.model.page.IPageBaseItem;
import com.tr.model.page.IPageBaseItemFactory;
import com.tr.model.page.JTPage;

public class MGetBlackList implements IPageBaseItemFactory, Serializable{

    private static final long serialVersionUID = 198239944230064422L;
    private JTPage jtPage;
	
	
	public JTPage getJtPage() {
		return jtPage;
	}


	public void setJtPage(JTPage jtPage) {
		this.jtPage = jtPage;
	}


	public static MGetBlackList createFactory(JSONObject jsonObject) {
		MGetBlackList self = null;
		if (jsonObject != null) {
			self = new MGetBlackList();
			self.jtPage = JTPage.createFactory(jsonObject, "listUserBlack", self);
		}
		return self;
	}


	@Override
	public IPageBaseItem paserByObject(JSONObject jsonObj) {
		// TODO Auto-generated method stub
		try{
			ConnectionsMini flow = ConnectionsMini.createFactory(jsonObj);
			return flow;
		}catch(Exception e){
			
		}
		return null;
	}

}
