package com.tr.model.im; 

import java.io.Serializable;

import org.json.JSONObject;

import com.tr.model.obj.IMRecord;
import com.tr.model.page.IPageBaseItem;
import com.tr.model.page.IPageBaseItemFactory;
import com.tr.model.page.JTPage;

import android.text.TextUtils;

/** 
 * @author xuxinjian
 * @E-mail: xuxinjian@gintong.com
 * @version 1.0
 * @创建时间：2014-4-11 上午10:59:28 
 * @类说明 
 */
public class MGetListIMRecord  implements IPageBaseItemFactory, Serializable{

    private static final long serialVersionUID = 198239944230064422L;
    private JTPage jtPage;
	
	
	public JTPage getJtPage() {
		return jtPage;
	}


	public void setJtPage(JTPage jtPage) {
		this.jtPage = jtPage;
	}


	public static MGetListIMRecord createFactory(JSONObject jsonObject) {
		MGetListIMRecord self = null;

		if (jsonObject != null) {
			self = new MGetListIMRecord();
			self.jtPage = JTPage.createFactory(jsonObject, "listIMRecord", self);
		}

		return self;
	}


	@Override
	public IPageBaseItem paserByObject(JSONObject jsonObj) {
		// TODO Auto-generated method stub
		try{
			IMRecord record = IMRecord.createFactory(jsonObj);
			return record;
		}catch(Exception e){
			return null;
		}
		
	}

}
 