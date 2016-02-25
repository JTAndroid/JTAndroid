package com.tr.model.joint;

import java.util.ArrayList;

import com.tr.model.demand.ASSOData;
import com.tr.model.demand.DemandASSOData;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.obj.AffairsMini;

/**
 * 事件对接组
 * @author leon
 */
public class AffairNode extends ResourceNode{

	private static final long serialVersionUID = 1L;
	// 事件列表
	private ArrayList<AffairsMini> listAffairMini;
	
	public ArrayList<AffairsMini> getListAffairMini() {
		if(listAffairMini == null){
			listAffairMini = new ArrayList<AffairsMini>();
		}
		return listAffairMini;
	}
	public void setListAffairMini(ArrayList<AffairsMini> listAffairMini) {
		this.listAffairMini = listAffairMini;
	}
	
	@Override
	public ResourceNodeMini toResourceNodeMini() {
		ResourceNodeMini resNodeMini= new ResourceNodeMini();
		resNodeMini.setColumn(memo);
		resNodeMini.setColumnType(type);
//		resNodeMini.setType(type);
		for(AffairsMini affair : getListAffairMini()){
			resNodeMini.getListItemId().add(affair.id + "");
			resNodeMini.getListItemType().add(affair.connections.getType());
			resNodeMini.setType(1);// 1-事件;2-人脉;3-组织;4-知识
		}
		return resNodeMini;
	}	
	public ASSOData toASSOData(){
		ASSOData note=new ASSOData();
		note.tag=memo;
		 ArrayList<DemandASSOData> listConnections=new ArrayList<DemandASSOData>();
		for(AffairsMini data:this.listAffairMini){
			listConnections.add(data.toDemandASSOData());
		}
		note.conn=listConnections;
		return note;
	}
}
