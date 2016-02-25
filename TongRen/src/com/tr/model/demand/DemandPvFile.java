package com.tr.model.demand; 

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName:     DemandPvFile.java
 * @author         fxtx
 * @Date           2015年3月26日 下午3:45:21 
 * @Description:   TODO(用一句话描述该文件做什么) 
 */
public class DemandPvFile  implements Serializable{
	public List<DemandFile>  fileList; //文件信息

	public List<DemandFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<DemandFile> fileList) {
		this.fileList = fileList;
	}
}
