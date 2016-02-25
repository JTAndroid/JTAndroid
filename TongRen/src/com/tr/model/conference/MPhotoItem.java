package com.tr.model.conference;

import java.io.Serializable;

import android.graphics.Bitmap;

public class MPhotoItem implements Serializable{
	private static final long serialVersionUID = -3984626264903802056L;
	public String path;
	public Bitmap photo;
	
	public String photoId;
	public String thumbnailPath;
	public boolean isSelected = false;
	
	public boolean isAlterMeeting;
	public MMeetingPic alterMeetingPic;
	public JTFile2ForHY alterFilePhoto;
}
