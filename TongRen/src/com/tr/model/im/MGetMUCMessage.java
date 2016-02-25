package com.tr.model.im; 

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import com.tr.model.obj.IMBaseMessage;
import com.tr.model.obj.MUCMessage;

/** 解析群聊聊天记录
 * @author xuxinjian/leon
 */
public class MGetMUCMessage extends MGetMessage{

    private static final long serialVersionUID = 198239433330064422L;

	/**
	 * 解析群聊聊天记录
	 * @param jsonObject
	 * @return
	 */
	public static MGetMUCMessage createFactory(JSONObject jsonObject) {
		MGetMUCMessage self = null;
		try{
			if (jsonObject != null) {
				self = new MGetMUCMessage();
				JSONArray arr = jsonObject.optJSONArray("listMUCMessage");
				if(arr != null){
					self.listMessage = new ArrayList<IMBaseMessage>();
					for(int i = 0; i < arr.length(); i++){
						JSONObject mucObj = arr.getJSONObject(i);
						MUCMessage cm = MUCMessage.createFactory(mucObj);
						if(cm != null){
							self.listMessage.add(cm);
						}
					}
				}
				self.hasMore = jsonObject.optBoolean("hasMore");
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return self;
	}
}
 