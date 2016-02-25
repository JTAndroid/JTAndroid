package com.tr.ui.tongren.model.project;

import java.io.Serializable;

public class ProjectFile implements Serializable{
	/**
	 * "createTime": 1450427024541,
          "createrId": 36,
          "fileIndex": {}
           "status": 0,
      "updateTime": 1450427024529,
      "validityEndTime": 1451550242000,
      "validityStartTime": 1450427040000


	 */
	public long createTime;
	public long createrId;
	public ProjectFileIndex fileIndex;
	public String status;
	public long updateTime;
	public long validityStartTime;
	public long validityEndTime;
	
}
