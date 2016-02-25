package com.tr.ui.people.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class PeopleDBHelper extends SQLiteAssetHelper {

    private static PeopleDBHelper mInstance; 
	
	public PeopleDBHelper(Context context) {
		super(context, "contacts.db", null, 1);
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
	public synchronized static PeopleDBHelper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new PeopleDBHelper(context);
		}
		return mInstance;
	};
}

