package com.tr.model.im; 

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tr.model.obj.ChatMessage;
import com.tr.model.obj.MUCDetail;
import com.tr.model.obj.MUCDetailMini;

/** 
 * @author xuxinjian
 * @E-mail: xuxinjian@gintong.com
 * @version 1.0
 * @创建时间：2014-4-11 上午10:59:28 
 * @类说明 获取我的会议
 */
public class MGetListMUC  implements Serializable{

    private static final long serialVersionUID = 198111231110064422L;
    private List<MUCDetailMini> listMUCDetailMini;
	
	public List<MUCDetailMini> getListMUCDetailMini() {
		return listMUCDetailMini;
	}

	public void setListMUCDetail(List<MUCDetailMini> listMUCDetailMini) {
		this.listMUCDetailMini = listMUCDetailMini;
	}

	public static MGetListMUC createFactory(JSONObject jsonObject) {
		MGetListMUC self = null;
		try{
			if (jsonObject != null) {
				self = new MGetListMUC();
				JSONArray arr = jsonObject.optJSONArray("listMUCDetailMini");
				if(arr != null){
					self.listMUCDetailMini = new ArrayList<MUCDetailMini>();
					for(int i = 0; i < arr.length(); i++){
						JSONObject chatObj = arr.getJSONObject(i);
						MUCDetailMini cm = MUCDetailMini.createFactory(chatObj);
						if(cm != null){
							self.listMUCDetailMini.add(cm);
						}
					}
				}
			}
		}catch(Exception e){
			
		}

		return self;
	}
}
 