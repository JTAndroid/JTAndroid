package com.tr.model.demand;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: ASSORPOK.java
 * @author fxtx
 * @Date 2015年3月25日 上午11:17:44
 * @Description: TODO(用一句话描述该文件做什么)
 */
public class ASSORPOK implements Serializable {

	/**
	 * 
	 */
	public static final long serialVersionUID = 4268594227057062934L;

	public List<ASSOData> r = new ArrayList<ASSOData>(); // 事件
	public List<ASSOData> p= new ArrayList<ASSOData>();// 人
	public List<ASSOData> o= new ArrayList<ASSOData>();// 组织
	public List<ASSOData> k= new ArrayList<ASSOData>();// 知识
	public ASSORPOK() {
		// TODO Auto-generated constructor stub
	}
	public ASSORPOK(List<ASSOData> r, List<ASSOData> p, List<ASSOData> o,
			List<ASSOData> k) {
		super();
		this.r = r;
		this.p = p;
		this.o = o;
		this.k = k;
	}

}
