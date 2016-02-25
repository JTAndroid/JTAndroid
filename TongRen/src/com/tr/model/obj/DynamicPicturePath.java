package com.tr.model.obj;

import java.io.Serializable;

import org.json.JSONObject;

public class DynamicPicturePath implements Serializable {

	private static final long serialVersionUID = 1L;

	// 缩略图路径
	private String shrinkPath;
	// 缩略图宽度
	private int shrinkWidth;
	// 缩略图高度
	private int shrinkHight;
	// 原图路径
	private String sourcePath;
	// 原图宽度
	private int sourceWidth;
	// 原图高度
	private int sourceHight;

	public static DynamicPicturePath createFactory(JSONObject jsonObject) {
		try {
			DynamicPicturePath self = new DynamicPicturePath();
			self.shrinkPath = jsonObject.optString("shrinkPath");
			self.sourcePath = jsonObject.optString("sourcePath");
			self.shrinkWidth = jsonObject.optInt("shrinkWidth");
			self.shrinkHight = jsonObject.optInt("shrinkHight");
			self.sourceWidth = jsonObject.optInt("sourceWidth");
			self.sourceHight = jsonObject.optInt("sourceHight");
			return self;
		} catch (Exception e) {
			return null;
		}
	}

	public String getShrinkPath() {
		return shrinkPath;
	}

	public void setShrinkPath(String shrinkPath) {
		this.shrinkPath = shrinkPath;
	}

	public String getSourcePath() {
		return sourcePath;
	}

	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}

	public int getShrinkWidth() {
		return shrinkWidth;
	}

	public void setShrinkWidth(int shrinkWidth) {
		this.shrinkWidth = shrinkWidth;
	}

	public int getShrinkHight() {
		return shrinkHight;
	}

	public void setShrinkHight(int shrinkHight) {
		this.shrinkHight = shrinkHight;
	}

	public int getSourceWidth() {
		return sourceWidth;
	}

	public void setSourceWidth(int sourceWidth) {
		this.sourceWidth = sourceWidth;
	}

	public int getSourceHight() {
		return sourceHight;
	}

	public void setSourceHight(int sourceHight) {
		this.sourceHight = sourceHight;
	}

}
