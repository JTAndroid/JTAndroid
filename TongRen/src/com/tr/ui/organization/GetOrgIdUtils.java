package com.tr.ui.organization;

import com.tr.App;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class GetOrgIdUtils {

	private SharedPreferences sp;
	private Editor edit;

	public GetOrgIdUtils(Context context) {
		
		super();
		sp = context.getSharedPreferences("Organization", Context.MODE_PRIVATE);
		edit = sp.edit();
	}
	public void putOrg_Id(Long org_Id){
	
		edit.putLong("Get_Id", org_Id);
		edit.commit();
	}
	public Long getOrg_Id(){
		long id = sp.getLong("Get_Id", 0);
		return id;
	}
}
