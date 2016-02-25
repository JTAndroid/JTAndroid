package com.utils.common;

public class DownloadInfo {

	private long startPos;// 开始点
	private long completeSize; // 完成度
	private String url; // 下载器网络标识
	// private String name; // 文件名（网络返回的JTFile对象有可能没有此字段）

	public DownloadInfo(){
		this.startPos = 0;
		this.completeSize = 0;
		this.url = "";
		// this.name = "";
	}
	
	public DownloadInfo(long startPos, long completeSize, String url) {
		this.startPos = startPos;
		this.completeSize = completeSize;
		this.url = url;
	}

	public long getStartPos() {
		return startPos;
	}

	public void setStartPos(long startPos) {
		this.startPos = startPos;
	}

	public long getCompleteSize() {
		return completeSize;
	}

	public void setCompleteSize(long completeSize) {
		this.completeSize = completeSize;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	/*
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	*/
}
