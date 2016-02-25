package com.tr.ui.cloudDisk.model;

import java.io.Serializable;

public class FileManagerResponseData implements Serializable {
	/**
	 * succeed":true,"resultType":1,"resultMessage":"该目录已存在
	 */
	public String succeed;
	public String resultType;
	public String resultMessage;
}
