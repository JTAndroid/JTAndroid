package com.tr.model.knowledge;

import java.util.ArrayList;

import android.util.Pair;

import com.tr.model.joint.AffairNode;
import com.tr.model.joint.ConnectionNode;
import com.tr.model.joint.KnowledgeNode;
import com.tr.model.obj.AffairsMini;
import com.tr.model.obj.Connections;

/**
 * 知识详情
 * @author CJJ
 *
 */
public class KnowledgeDetailsLv {
	private Integer type;
	private ConnectionNode relatedPeopleList;// 0-金桐网人脉;1-用户人脉
	private ConnectionNode relatedOrganizationList;
	private KnowledgeNode  relatedKnowledgeList;
	private AffairNode relatedAffairList;
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public ConnectionNode getRelatedPeopleList() {
		return relatedPeopleList;
	}
	public void setRelatedPeopleList(ConnectionNode relatedPeopleList) {
		this.relatedPeopleList = relatedPeopleList;
	}
	public ConnectionNode getRelatedOrganizationList() {
		return relatedOrganizationList;
	}
	public void setRelatedOrganizationList(ConnectionNode relatedOrganizationList) {
		this.relatedOrganizationList = relatedOrganizationList;
	}
	public KnowledgeNode getRelatedKnowledgeList() {
		return relatedKnowledgeList;
	}
	public void setRelatedKnowledgeList(KnowledgeNode relatedKnowledgeList) {
		this.relatedKnowledgeList = relatedKnowledgeList;
	}
	public AffairNode getRelatedAffairList() {
		return relatedAffairList;
	}
	public void setRelatedAffairList(AffairNode relatedAffairList) {
		this.relatedAffairList = relatedAffairList;
	}
}
