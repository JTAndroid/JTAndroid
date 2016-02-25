package com.tr.ui.tongren.model.project;

import java.io.Serializable;
import java.util.List;

import android.R.integer;

public class RecommendPagePublish implements Serializable{
	public int end ; 
	public int index ; 
	public int pageCount ; 
	public int size ; 
	public int start ; 
	public int totalCount ; 
	public List<Publish> result;
}
