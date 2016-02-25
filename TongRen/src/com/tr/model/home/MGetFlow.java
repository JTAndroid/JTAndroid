package com.tr.model.home; 

import java.io.Serializable;

import org.json.JSONObject;

import com.tr.model.page.IPageBaseItem;
import com.tr.model.page.IPageBaseItemFactory;
import com.tr.model.page.JTPage;

/** 
 * @author xuxinjian
 * @E-mail: xuxinjian@gintong.com
 * @version 1.0
 * @创建时间：2014-4-11 上午10:59:28 
 * @类说明 首页获取动态返回
 */
public class MGetFlow  implements IPageBaseItemFactory, Serializable{

    private static final long serialVersionUID = 198239944230064422L;
    private JTPage jtPage;
	
	
	public JTPage getJtPage() {
		return jtPage;
	}


	public void setJtPage(JTPage jtPage) {
		this.jtPage = jtPage;
	}


	public static MGetFlow createFactory(JSONObject jsonObject) {
		MGetFlow self = null;
		if (jsonObject != null) {
			self = new MGetFlow();
			self.jtPage = JTPage.createFactory(jsonObject, "listFlow", self);
		}
		return self;
	}


	@Override
	public IPageBaseItem paserByObject(JSONObject jsonObj) {
		// TODO Auto-generated method stub
		try{
//			Flow flow = Flow.createFactory(jsonObj);
//			return flow;
		}catch(Exception e){
			
		}
		return null;
	}

}
 