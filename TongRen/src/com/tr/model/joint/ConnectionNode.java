package com.tr.model.joint;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tr.model.demand.ASSOData;
import com.tr.model.demand.DemandASSOData;
import com.tr.model.obj.AffairsMini;
import com.tr.model.obj.Connections;

/**
 * 人脉或组织对接组
 * 
 * @author leon
 */
public class ConnectionNode extends ResourceNode {

	private static final long serialVersionUID = 1L;
	// 人脉列表
	private ArrayList<Connections> listConnections;

	public ArrayList<Connections> getListConnections() {
		if (listConnections == null) {
			listConnections = new ArrayList<Connections>();
		}
		return listConnections;
	}

	public void setListConnections(ArrayList<Connections> listConnections) {
		this.listConnections = listConnections;
	}

	@Override
	public ResourceNodeMini toResourceNodeMini() {
		ResourceNodeMini resNodeMini = new ResourceNodeMini();
		resNodeMini.setColumn(memo);
		resNodeMini.setColumnType(type);
//		resNodeMini.setType(type);
		for (Connections connections : getListConnections()) {
			resNodeMini.getListItemId().add(connections.getId());
			resNodeMini.getListItemType().add(connections.getType());
			if (connections.getJtContactMini()==null) {//组织
				resNodeMini.setType(3);// 1-事件;2-人脉;3-组织;4-知识
			}else if(connections.getOrganizationMini()==null){
				resNodeMini.setType(2);// 1-事件;2-人脉;3-组织;4-知识
			}
		}
		return resNodeMini;
	}

	public JSONObject toJSONObject() throws JSONException {
		JSONObject jObject = new JSONObject();
		jObject.put("type", getType());
		jObject.put("memo", getMemo());
		JSONArray jsonArray2 = new JSONArray();
		if (listConnections != null) {
			for (int i = 0; i < listConnections.size(); i++) {
				jsonArray2.put(i, listConnections.get(i).toJSONObject());
			}
		}
		jObject.put("listConnections", jsonArray2);
		return jObject;
	}
	public ASSOData toASSOData(){
		ASSOData note=new ASSOData();
		note.tag=memo;
		 ArrayList<DemandASSOData> listConnections=new ArrayList<DemandASSOData>();
		for(Connections data:this.listConnections){
			listConnections.add(data.toDemandASSOData());
		}
		note.conn=listConnections;
		return note;
	}
}
