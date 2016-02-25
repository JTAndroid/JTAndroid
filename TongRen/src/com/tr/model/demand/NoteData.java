package com.tr.model.demand;

/**
 * @ClassName: NoteData.java
 * @author fxtx
 * @Date 2015年3月25日 上午9:59:34
 * @Description: 需求介绍说明
 */
public class NoteData extends DemandSection {
	/**
	 * 
	 */
	public static final long serialVersionUID = -6610629235182173528L;
	public String taskId;// 附件id

	public NoteData() {

	}

	public NoteData(boolean isVisable) {
		this.isVisable = isVisable;
	}

	public NoteData(String taskId, boolean isVisable) {
		super();
		this.isVisable = isVisable;
		this.taskId = taskId;
	}

	public NoteData(String taskId, String values, boolean isVisable) {
		super();
		this.value = values;
		this.isVisable = isVisable;
		this.taskId = taskId;
	}

}
