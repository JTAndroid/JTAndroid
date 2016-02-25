package com.tr.model.demand; 

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.tr.model.joint.AffairNode;
import com.tr.model.joint.ConnectionNode;
import com.tr.model.joint.KnowledgeNode;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.obj.AffairsMini;
import com.tr.model.obj.Connections;

/**
 * @ClassName:     ASSOData.java
 * @author         fxtx
 * @param <K>
 * @Date           2015年3月25日 上午11:21:09 
 * @Description:   权限控制中 具体人的信息
 */
public class ASSOData implements Serializable{
	/**
	 * 
	 */
	public static final long serialVersionUID = -8604637568909567613L;
	public String tag;//标签名
	public List<DemandASSOData> conn;
	
	public ASSOData(String tag, List<DemandASSOData> conn) {
		super();
		this.tag = tag;
		this.conn = conn;
	}
	public ASSOData() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ConnectionNode toConnectionNode(){
		ConnectionNode note=new ConnectionNode();
		note.setMemo(this.tag);
		 ArrayList<Connections> listConnections=new ArrayList<Connections>();
		for(DemandASSOData data:this.conn){
			listConnections.add(data.toConnection());
		}
		note.setListConnections(listConnections);
		return note;
	}
	public KnowledgeNode toKnowledgeNode(){
		KnowledgeNode note=new KnowledgeNode();
		note.setMemo(this.tag);
		ArrayList<KnowledgeMini2> listKnowledgeMini2s=new ArrayList<KnowledgeMini2>();
		for(DemandASSOData data:this.conn){
			listKnowledgeMini2s.add(data.toKnowledgeMini2());
		}
		note.setListKnowledgeMini2(listKnowledgeMini2s);
		return note;
	}
	public AffairNode toAffairNode(){
		AffairNode note=new AffairNode();
		note.setMemo(this.tag);
		ArrayList<AffairsMini> listAffairsMini=new ArrayList<AffairsMini>();
		for(DemandASSOData data:this.conn){
			listAffairsMini.add(data.toAffaitrsMini());
		}
		note.setListAffairMini(listAffairsMini);
		return note;
	}
}
 
