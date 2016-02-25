package com.tr.ui.tongren.model.project;

import java.io.Serializable;

public class ProjectId implements Serializable{
	public String projectId;
	public int type ;
	public ProjectId(String projectId) {
		super();
		this.projectId = projectId;
	}
	
}
