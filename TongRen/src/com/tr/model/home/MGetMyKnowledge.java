package com.tr.model.home; 

import java.io.Serializable;

import org.json.JSONObject;

import com.tr.model.obj.KnowledgeMini;
import com.tr.model.page.IPageBaseItem;
import com.tr.model.page.IPageBaseItemFactory;
import com.tr.model.page.JTPage;

/** 
 * @author xuxinjian
 * @E-mail: xuxinjian@gintong.com
 * @version 1.0
 * @创建时间：2014-4-11 上午10:59:28 
 * @类说明 获取我 的知识返回
 */
public class MGetMyKnowledge  implements IPageBaseItemFactory, Serializable{

    private static final long serialVersionUID = 1982399423434464422L;
    private JTPage jtPage;
	
	
	public JTPage getJtPage() {
		return jtPage;
	}


	public void setJtPage(JTPage jtPage) {
		this.jtPage = jtPage;
	}


	public static MGetMyKnowledge createFactory(JSONObject jsonObject) {
		MGetMyKnowledge self = null;
		if (jsonObject != null) {
			self = new MGetMyKnowledge();
			self.jtPage = JTPage.createFactory(jsonObject, "listKnowledge", self);
		}
		return self;
	}


	@Override
	public IPageBaseItem paserByObject(JSONObject jsonObj) {
		// TODO Auto-generated method stub
		try{
			KnowledgeMini item = KnowledgeMini.createFactory(jsonObj);
			return item;
		}catch(Exception e){
			
		}
		return null;
	}

}
 