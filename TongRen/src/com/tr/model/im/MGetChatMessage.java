package com.tr.model.im; 

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import com.tr.model.obj.ChatMessage;
import com.tr.model.obj.IMBaseMessage;

/** 
 * 聊天记录返回解析类
 * @author xuxinjian/leon
 * @version 1.0
 */
public class MGetChatMessage extends MGetMessage{

    private static final long serialVersionUID = 198239941999064422L;
    
	/**
	 * 解析聊天记录
	 * @param jsonObject
	 * @return
	 */
	public static MGetChatMessage createFactory(JSONObject jsonObject) {
		MGetChatMessage self = null;
		try{
			if (jsonObject != null) {
				self = new MGetChatMessage();
				JSONArray arr = jsonObject.optJSONArray("listChatMessage");
				if(arr != null){
					self.listMessage = new ArrayList<IMBaseMessage>();
					for(int i = 0; i < arr.length(); i++){
						JSONObject chatObj = arr.getJSONObject(i);
						ChatMessage cm = ChatMessage.createFactory(chatObj);
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
 