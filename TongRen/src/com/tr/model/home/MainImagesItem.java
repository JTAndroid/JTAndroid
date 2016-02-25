package com.tr.model.home;

import java.io.Serializable;

public class MainImagesItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4809846430581708460L;
	private MainImages images;
	private String title;// 标题
	private String alt;// 链接或关联的空间名
	public MainImages getImages() {
		return images;
	}
	public void setImages(MainImages images) {
		this.images = images;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAlt() {
		return alt;
	}
	public void setAlt(String alt) {
		this.alt = alt;
	}
	
}
