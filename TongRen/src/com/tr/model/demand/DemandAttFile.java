package com.tr.model.demand; 

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName:     DemandAttFile.java
 * @author         fxtx
 * @Date           2015年3月26日 下午3:43:42 
 * @Description:   媒体文件信息
 */
public class DemandAttFile implements Serializable{
	public List<DemandFile>  fileList; //文件信息

	public List<DemandFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<DemandFile> fileList) {
		this.fileList = fileList;
	}
}
 
