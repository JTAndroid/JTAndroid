package com.tr.model.joint;

import java.util.ArrayList;

import com.tr.model.demand.ASSOData;
import com.tr.model.demand.DemandASSOData;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.obj.AffairsMini;
import com.tr.model.obj.Connections;

/**
 * 知识对接组
 * @author leon
 */
public class KnowledgeNode extends ResourceNode{

	private static final long serialVersionUID = 1L;
	// 知识列表
	private ArrayList<KnowledgeMini2> listKnowledgeMini2;

	public ArrayList<KnowledgeMini2> getListKnowledgeMini2() {
		if(listKnowledgeMini2 == null){
			listKnowledgeMini2 = new ArrayList<KnowledgeMini2>();
		}
		return listKnowledgeMini2;
	}

	public void setListKnowledgeMini2(ArrayList<KnowledgeMini2> listKnowledgeMini2) {
		this.listKnowledgeMini2 = listKnowledgeMini2;
	}

	@Override
	public ResourceNodeMini toResourceNodeMini() {
		ResourceNodeMini resNodeMini= new ResourceNodeMini();
		resNodeMini.setColumn(memo);
		resNodeMini.setColumnType(type);
		for(KnowledgeMini2 knowledge : getListKnowledgeMini2()){
			resNodeMini.getListItemId().add(knowledge.id + "");
			resNodeMini.getListItemType().add(knowledge.type+"");
			resNodeMini.setType(4);// 1-事件;2-人脉;3-组织;4-知识
		}
		return resNodeMini;
	}
	public ASSOData toASSOData(){
		ASSOData note=new ASSOData();
		note.tag=memo;
		 ArrayList<DemandASSOData> listConnections=new ArrayList<DemandASSOData>();
		for(KnowledgeMini2 data:this.listKnowledgeMini2){
			listConnections.add(data.toDemandASSOData());
		}
		note.conn=listConnections;
		return note;
	}
}
