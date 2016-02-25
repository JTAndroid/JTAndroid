package com.tr.db;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RegionDBHelper extends SQLiteAssetHelper {

	private static RegionDBHelper mInstance; 
	
	public RegionDBHelper(Context context) {
		super(context, "region.db", null, 1);
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
	public synchronized static RegionDBHelper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new RegionDBHelper(context);
		}
		return mInstance;
	};
}
