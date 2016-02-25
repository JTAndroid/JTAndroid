package com.tr.ui.knowledge.business;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.tr.App;
import com.utils.common.EConsts;

/**
 * 知识广场已读过的知识id SharedPreferences 业务类
 * @author gushi
 *
 */
public class KonwledgeSquareReadedKnowledgeSharedPreferencesBusiness {
	
	/** 知识广场已读过的知识id SharedPreferences 文件 name  */
	public  final String share_knowledgeSquare_readed_knowledge = "knowledgeSquare_readed_knowledge";
	SharedPreferences sp;
	
	
	/**
	 * 构造函数
	 * @param context
	 */
	public KonwledgeSquareReadedKnowledgeSharedPreferencesBusiness(Context context) {
		super();
		sp = context.getSharedPreferences(EConsts.share_knowledgeSquare_readed_knowledge, Activity.MODE_PRIVATE);
	}
	
	
	/**
	 * 标记知识已读状态
	 * @param id  知识id
	 */
	public  void markReadedKnowledge(String knowledgeId) {
		String MSG = "markReadedKnowledgeId()";
		Editor  editor = sp.edit();
		editor.putBoolean(App.getUserID()+ "&" +knowledgeId , true);
		editor.commit();
	}
	
	
	/**
	 * 查询知识已读状态
	 * @param knowledgeId  知识ID
	 * @return  是否已读
	 */
	public boolean queryKnowledgeState(String knowledgeId){
		return sp.getBoolean(App.getUserID()+ "&" +knowledgeId, false);
	}
	
	
	/**
	 * 清除知识广场全部已读知识状态
	 */
	public void clear(){
		Editor  editor = sp.edit();
		editor.clear();
		editor.commit();
	}

}
