package com.tr.db;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.tr.model.obj.JTRegion;

public class RegionDBManager {

	// 常量
	private final String TAG = getClass().getSimpleName();

	// 变量
	private RegionDBHelper helper;
	private SQLiteDatabase db;

	public RegionDBManager(Context context) {
		helper = RegionDBHelper.getInstance(context);
		synchronized (helper) {
			db = helper.getWritableDatabase();
		}
	}

	// 查询
	public List<JTRegion> query(String parent_id) {

		List<JTRegion> listRegion = new ArrayList<JTRegion>();
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			Cursor c = queryTheCursor(parent_id);
			try {
				while (c.moveToNext()) {
					JTRegion jtRegion = new JTRegion();
					jtRegion.setId(c.getInt(c.getColumnIndex("id")));
					jtRegion.setParentId(c.getInt(c.getColumnIndex("parentId")));
					jtRegion.setCname(c.getString(c.getColumnIndex("cname")));
					jtRegion.setEname(c.getString(c.getColumnIndex("ename")));
					jtRegion.setOrders(c.getInt(c.getColumnIndex("orders")));
					if(listRegion == null){
						listRegion = new ArrayList<JTRegion>();
					}
					listRegion.add(jtRegion);
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
			} 
			finally {
				c.close();
			}
		}
		return listRegion;
	}

	private Cursor queryTheCursor(String parent_id) {

		Cursor c = db.rawQuery("SELECT * FROM tb_code_region WHERE parentId = ?", new String[] { parent_id });
		return c;
	}
}
