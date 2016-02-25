package com.tr.ui.flow.model;

import java.io.Serializable;
import java.util.ArrayList;

import com.tr.model.obj.DynamicNews;

public class DynamicNewsRequest implements Serializable {
	 //“scope”：0，所有好友，1，部分好友，2,私密
	public  int scope ;
	public ArrayList<Long> receiverIds;
	public DynamicNews dynamicNews ;
}
