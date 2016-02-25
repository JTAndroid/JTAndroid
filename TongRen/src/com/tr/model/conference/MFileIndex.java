package com.tr.model.conference;

import java.io.Serializable;

public class MFileIndex implements Serializable{

	/**
	 * sunjianan
	 */
	private static final long serialVersionUID = 8820203644267300242L;
	
	/**
	 	"id":"主键、string",
        "filePath":"文件存放的物理路径",
        "fileTitle":"源文件的名称",
        "fileSize":"文件大小 long",
        "status":"文件状态",
        "author":"创建人",
        "md5":"加密形式",
        "taskId":"taskId",
        "ctime":"创建时间",
        "moduleType":"类型",
        "authorName":"创建人姓名",
        "crc32":"解压缩",
	 */
	
	/**
     * id       db_column: id 
     */	
	private java.lang.String id;
    /**
     * 文件存放物的理路径       db_column: file_path 
     */	
	private java.lang.String filePath;
    /**
     * 文件标题       db_column: file_title 
     */	
	private java.lang.String fileTitle;
    /**
     * 文件大小       db_column: file_size 
     */	
	
	private java.lang.Long fileSize;
    /**
     * 文件使用状态       db_column: status 
     */	
	
	private java.lang.Boolean status;
    /**
     * 创建者ID       db_column: author_id 
     */	
	
	private java.lang.Long authorId;
    /**
     * 加密形式       db_column: md5 
     */	
	private java.lang.String md5;
    /**
     * 标示ID       db_column: task_id 
     */	
	private java.lang.String taskId;
    /**
     * 模块类型       db_column: module_type 
     */	
	
	private java.lang.Integer moduleType;
    /**
     * 创建时间       db_column: ctime 
     */	
	
	private java.util.Date ctime;
    /**
     * 创建人名称       db_column: author_name 
     */	
	private java.lang.String authorName;
    /**
     * 解压编码       db_column: crc32 
     */	
	
	
	
	private java.lang.String crc32;
	//columns END
	public java.lang.String getId() {
		return id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}
	public java.lang.String getFilePath() {
		return filePath;
	}
	public void setFilePath(java.lang.String filePath) {
		this.filePath = filePath;
	}
	public java.lang.String getFileTitle() {
		return fileTitle;
	}
	public void setFileTitle(java.lang.String fileTitle) {
		this.fileTitle = fileTitle;
	}
	public java.lang.Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(java.lang.Long fileSize) {
		this.fileSize = fileSize;
	}
	public java.lang.Boolean getStatus() {
		return status;
	}
	public void setStatus(java.lang.Boolean status) {
		this.status = status;
	}
	public java.lang.Long getAuthorId() {
		return authorId;
	}
	public void setAuthorId(java.lang.Long authorId) {
		this.authorId = authorId;
	}
	public java.lang.String getMd5() {
		return md5;
	}
	public void setMd5(java.lang.String md5) {
		this.md5 = md5;
	}
	public java.lang.String getTaskId() {
		return taskId;
	}
	public void setTaskId(java.lang.String taskId) {
		this.taskId = taskId;
	}
	public java.lang.Integer getModuleType() {
		return moduleType;
	}
	public void setModuleType(java.lang.Integer moduleType) {
		this.moduleType = moduleType;
	}
	public java.util.Date getCtime() {
		return ctime;
	}
	public void setCtime(java.util.Date ctime) {
		this.ctime = ctime;
	}
	public java.lang.String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(java.lang.String authorName) {
		this.authorName = authorName;
	}
	public java.lang.String getCrc32() {
		return crc32;
	}
	public void setCrc32(java.lang.String crc32) {
		this.crc32 = crc32;
	}

	
}
