package com.tr.db;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DemandDBHelper extends SQLiteAssetHelper {

	private static DemandDBHelper mInstance; 
	
	public DemandDBHelper(Context context) {
		super(context, "demand.db", null, 1);
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
	public synchronized static DemandDBHelper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new DemandDBHelper(context);
		}
		return mInstance;
	};
}
