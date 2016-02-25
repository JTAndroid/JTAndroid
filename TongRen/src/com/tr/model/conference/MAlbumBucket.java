package com.tr.model.conference;

import java.io.Serializable;
import java.util.List;


public class MAlbumBucket implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8079790223257950949L;
	public int count = 0;
	public String bucketName;
	public List<MPhotoItem> photoList;
}
