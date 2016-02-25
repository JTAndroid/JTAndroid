package com.tr.ui.tongren.model.task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



/**
 * 树的节点
 * @author hanxifa
 *
 */
public abstract class  Node implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Node> children=new ArrayList<Node>();
	/**
	 * 返回节点的id
	 * @return
	 */
	public abstract long getId();
	
	/**
	 * 返回节点的父ID
	 * @return
	 */
	public abstract long getPid();
	
	public void addChild(Node child){
		if(child!=null){
			children.add(child);
		}
	}

	public  List<Node>  getChildren(){
		return children;
	}
	 
	public void clearChildren(){
		children.clear();
	}
}
