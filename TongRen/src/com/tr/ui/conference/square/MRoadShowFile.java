package com.tr.ui.conference.square;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;

import com.hp.hpl.sparta.xpath.ThisNodeTest;
import com.tr.model.conference.JTFile2ForHY;
import com.tr.model.conference.MPhotoItem;

/**
 * 
 * @author sunjianan
 * 
 */
public class MRoadShowFile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7938164124011937574L;
	
	public String qulifiedAudio;
	public String qulifiedVideo;
	public List<MPhotoItem> qulifiedPhotos = new ArrayList<MPhotoItem>();
	
	public boolean isAlterAudio;
	public boolean isAlterMeeting;
	public JTFile2ForHY alterFileAudio;
	
	public MRoadShowFile() {
		super();
	}

//	public MRoadShowFile(String qulifiedAudio, String qulifiedVideo, List<MPhotoItem> qulifiedPhotos) {
//		super();
//		this.qulifiedAudio = qulifiedAudio;
//		this.qulifiedVideo = qulifiedVideo;
//		this.qulifiedPhotos = qulifiedPhotos;
//	}

	public String getQulifiedVideo() {
		return qulifiedVideo;
	}

	public void setQulifiedVideo(String qulifiedVideo) {
		this.qulifiedVideo = qulifiedVideo;
	}

	public String getQulifiedAudio() {
		return qulifiedAudio;
	}

	public void setQulifiedAudio(String qulifiedAudio) {
		this.qulifiedAudio = qulifiedAudio;
	}

	public List<MPhotoItem> getQulifiedPhotos() {
		return qulifiedPhotos;
	}

	public void setQulifiedPhotos(List<MPhotoItem> qulifiedPhotos) {
		this.qulifiedPhotos = qulifiedPhotos;
	}

}
