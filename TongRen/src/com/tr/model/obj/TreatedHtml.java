package com.tr.model.obj;

public class TreatedHtml {

	private String changeUrl = "";
	private String url = "";
	private String title = "";
	
	
	
	public String getChangeUrl() {
		return changeUrl;
	}
	public void setChangeUrl(String changeUrl) {
		this.changeUrl = changeUrl;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public String toString() {
		return "TreatedHtml [changeUrl=" + changeUrl + ", url=" + url + ", title=" + title + "]";
	}

	
	
}
