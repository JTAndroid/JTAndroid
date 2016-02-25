package com.tr.model.im; 

import java.io.Serializable;

import org.json.JSONObject;

import com.tr.model.obj.MUCDetail;



/** 
 * @author xuxinjian
 * @E-mail: xuxinjian@gintong.com
 * @version 1.0
 * @创建时间：2014-4-11 上午10:59:28 
 * @类说明 获取聊天记录，发送消息
 */
public class MCreateMUC  implements Serializable{

    private static final long serialVersionUID = 198111231110064422L;
    private MUCDetail mucDetail;
	

	public MUCDetail getMucDetail() {
		return mucDetail;
	}


	public void setMucDetail(MUCDetail mucDetail) {
		this.mucDetail = mucDetail;
	}


	public static MCreateMUC createFactory(JSONObject jsonObject) {
		MCreateMUC self = null;
		try{
			if (jsonObject != null) {
				self = new MCreateMUC();
				self.mucDetail = MUCDetail.createFactory(jsonObject);
			}
		}catch(Exception e){
			
		}

		return self;
	}
}
 