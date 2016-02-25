package com.tr.model.home; 

import java.io.Serializable;

import org.json.JSONObject;

import com.tr.model.obj.RequirementMini;
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
public class MGetMyRequirement  implements IPageBaseItemFactory, Serializable{

    private static final long serialVersionUID = 198239942330064422L;
    private JTPage jtPage;
	
	
	public JTPage getJtPage() {
		return jtPage;
	}


	public void setJtPage(JTPage jtPage) {
		this.jtPage = jtPage;
	}


	public static MGetMyRequirement createFactory(JSONObject jsonObject) {
		MGetMyRequirement self = null;
		if (jsonObject != null) {
			self = new MGetMyRequirement();
			self.jtPage = JTPage.createFactory(jsonObject, "listRequirementMini", self);
		}
		return self;
	}


	@Override
	public IPageBaseItem paserByObject(JSONObject jsonObj) {
		// TODO Auto-generated method stub
		try{
			RequirementMini item = RequirementMini.createFactory(jsonObj);
			return item;
		}catch(Exception e){
			
		}
		return null;
	}

}
 