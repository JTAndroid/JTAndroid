package com.tr.model.home;

import java.io.Serializable;

/** @author sunjianan */
public class MShareContent implements Serializable {

	/**
	 * QQ: 分享链接setTitleUrl  微信,朋友圈：setUrl  新浪setText
	 */
	private static final long serialVersionUID = 2514325640750980946L;

	private String title;
	private String titleUrl;
	private String shareText;
	private String imageUrl;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleUrl() {
		return titleUrl;
	}

	public void setTitleUrl(String titleUrl) {
		this.titleUrl = titleUrl;
	}

	public String getShareText() {
		return shareText;
	}

	public void setShareText(String shareText) {
		this.shareText = shareText;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

}
