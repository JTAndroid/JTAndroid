package com.tr.model.demand;

import java.io.Serializable;

import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.obj.AffairsMini;
import com.tr.model.obj.Connections;

/**
 * @ClassName: DemandPeople.java
 * @author fxtx
 * @Date 2015年3月25日 上午11:40:14
 * @Description: 关联信息数据
 */
public class DemandASSOData implements Serializable {

	/**
	 * 
	 */
	public static final long serialVersionUID = -790179278438715812L;
	public int type; // 2人脉，3，好友，4组织，5,客户，6知识，1融资事件，0投资事件
	public String id;
	public String picPath;//头像路径
	public String title;
	public String ownerid;// 拥有者id
	public String ownername;// 拥有者名称
	public String requirementtype; // 需求类型
	public String career;// 职业
	public String company;// 公司
	public String address;// 地址
	public String hy;// 行业
	public String columnpath;// 栏目路径
	public String columntype;// 栏目类型
	public String name;// 人名

	public String tag;// 标签关系

	/**
	 * 将关联信息转成组织信息
	 * 
	 * @return
	 */
	public Connections toConnection() {
		Connections conn = new Connections();
		conn.setID(id);
		conn.setName(name);
		conn.setCareer(career);
		conn.setCompany(company);
		conn.setImage(picPath);
		conn.setType(String.valueOf(type));
		return conn;
	}

	/**
	 * 将信息转成 知识信息对象
	 * 
	 * @return
	 */
	public KnowledgeMini2 toKnowledgeMini2() {
		KnowledgeMini2 knowledgeMini2 = new KnowledgeMini2();
		knowledgeMini2.id = Long.parseLong(id);
		knowledgeMini2.title=title;
		knowledgeMini2.columnpath = columnpath;
		knowledgeMini2.type= type;
		return knowledgeMini2;
	}
	/**
	 * 将信息转成 事件对象
	 * @return
	 */
	public AffairsMini toAffaitrsMini() {
		AffairsMini conn = new AffairsMini();
		conn.id = Integer.parseInt(id);
		conn.title = title;
		conn.name = name;
		conn.reserve = requirementtype;
		return conn;
	}
}
