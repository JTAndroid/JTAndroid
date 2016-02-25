package com.tr.model.home; 

import java.io.Serializable;

import org.json.JSONObject;

import com.tr.model.obj.AffairsMini;
import com.tr.model.page.IPageBaseItem;
import com.tr.model.page.IPageBaseItemFactory;
import com.tr.model.page.JTPage;

/** 
 * @author xuxinjian
 * @E-mail: xuxinjian@gintong.com
 * @version 1.0
 * @创建时间：2014-4-11 上午10:59:28 
 * @类说明 获取我 的需求返回
 */
public class GetMyAffairs  implements IPageBaseItemFactory, Serializable{

    private static final long serialVersionUID = 198239942330064422L;
    private JTPage jtPage;
	
	
	public JTPage getJtPage() {
		return jtPage;
	}


	public void setJtPage(JTPage jtPage) {
		this.jtPage = jtPage;
	}


	public static GetMyAffairs createFactory(JSONObject jsonObject) {
		GetMyAffairs self = null;
		if (jsonObject != null) {
			self = new GetMyAffairs();
			self.jtPage = JTPage.createFactory(jsonObject, "listAffairMini", self);
		}
		return self;
	}


	@Override
	public IPageBaseItem paserByObject(JSONObject jsonObj) {
		// TODO Auto-generated method stub
		try{
			AffairsMini item = AffairsMini.createFactory(jsonObj);
			return item;
		}catch(Exception e){
			
		}
		return null;
	}

}
 