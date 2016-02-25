package com.tr.model.conference;

import java.io.Serializable;

public class MMediaVideo implements Serializable {
	private static final long serialVersionUID = 2932478335655283950L;
	
	public long id;
	public String title;
	public String mimeType;
	public String path;
	public String thumbPath;
	
	public boolean isAlterMeeting;
    public JTFile2ForHY alterFileAudio;
}
