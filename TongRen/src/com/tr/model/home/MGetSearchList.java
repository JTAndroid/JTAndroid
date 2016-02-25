package com.tr.model.home; 

import java.io.Serializable;

import org.json.JSONObject;

import com.tr.model.obj.SearchResult;
import com.tr.model.page.IPageBaseItem;
import com.tr.model.page.IPageBaseItemFactory;
import com.tr.model.page.JTPage;

/**
 * 2.38 ，搜索结果返回
 * @ClassName:     MGetSearchList.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * @author         xuxinjian
 * @version        V1.0  
 * @Date           2014-4-30 上午9:58:59
 */
public class MGetSearchList  implements IPageBaseItemFactory, Serializable{

    private static final long serialVersionUID = 198239944223064422L;
    private JTPage jtPage;
	
	
	public JTPage getJtPage() {
		return jtPage;
	}


	public void setJtPage(JTPage jtPage) {
		this.jtPage = jtPage;
	}


	public static MGetSearchList createFactory(JSONObject jsonObject) {
		MGetSearchList self = null;
		if (jsonObject != null) {
			self = new MGetSearchList();
			self.jtPage = JTPage.createFactory(jsonObject, "listSearchResult", self);
		}
		return self;
	}
	public static MGetSearchList createFactoryforMeeting(JSONObject jsonObject) {
		MGetSearchList self = null;
		if (jsonObject != null) {
			self = new MGetSearchList();
			self.jtPage = JTPage.createFactoryforMeeting(jsonObject, "listMeeting", self);
		}
		return self;
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

}
 