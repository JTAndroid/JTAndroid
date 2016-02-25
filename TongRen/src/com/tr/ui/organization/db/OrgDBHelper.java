package com.tr.ui.organization.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.tr.db.DemandDBHelper;

public class OrgDBHelper extends SQLiteAssetHelper {

private static OrgDBHelper mInstance; 
	
	public OrgDBHelper(Context context) {
		super(context, "organization.db", null, 1);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * 单例访问
	 * @param context
	 * @return
	 */
	public synchronized static OrgDBHelper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new OrgDBHelper(context);
		}
		return mInstance;
	};
}
