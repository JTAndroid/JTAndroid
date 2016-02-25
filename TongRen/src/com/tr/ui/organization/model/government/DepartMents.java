package com.tr.ui.organization.model.government;

import java.io.Serializable;


import com.tr.ui.organization.model.government.DepartMentsInfo;

/**
 * 职能部门
 * @author tanghuihuang
 *
 * 2014-10-29
 */
public class DepartMents implements Serializable {

		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		public long id;//主键/客户id
		public DepartMentsInfo cztDepart;//财政厅、财政局
		public DepartMentsInfo fgwDepart;//发改委
		public DepartMentsInfo gzwDepart;//国资委
		public DepartMentsInfo sjrbDepart;//省金融办，金融办(局)
		public DepartMentsInfo ghjDepart;//规划局
		public DepartMentsInfo gtzytDepart;//国土资源厅，国土局
		public DepartMentsInfo swtDepart;//商务厅/商务局
		public DepartMentsInfo gxwDepart;//工信委/工信委(局)
		public DepartMentsInfo houseDepart;//住房和城乡建设厅，市建委(住建局)
		public DepartMentsInfo sftDepart;//司法厅。司法局
		public String taskId;//附件id
		
		public String areaType;//区域类型  临时字段
		
		
}
