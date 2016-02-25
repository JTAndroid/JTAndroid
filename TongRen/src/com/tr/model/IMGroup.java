package com.tr.model;

import java.util.ArrayList;
/**聊天组、会议*/
public class IMGroup {
	public static int grouptypemeeting=1;
	public static int grouptypegroup=2; 
	
	/**群组类型*/
	private int groupType=0;
	/**群组的人*/
	private ArrayList<IMPerson> persons=null;
	/**会议时间*/
	private String time=null;
	/**项目资金*/
	private String bankroll=null;
	/**类型*/
	private String category=null;
	/**地区类型*/
	private String area_type=null;
	/**地区id*/
	private String area_id=null;
	/**描述*/
	private String description=null;
	
	public IMGroup(){
	
	}
}
