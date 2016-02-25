package com.tr.ui.organization.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tr.db.DemandDBHelper;
import com.tr.model.demand.Metadata;

public class OrgDBManager {

	// 常量
		private final String TAG = getClass().getSimpleName();

		// 变量
		private OrgDBHelper helper;
		private SQLiteDatabase db;


		public OrgDBManager(Context context) {
			helper = OrgDBHelper.getInstance(context);
			synchronized (helper) {
				db = helper.getWritableDatabase();
			}
		}

		/**
		 * 获取所有的数据 8为融资需求，1为投资需求，15为行业
		 * 
		 * @return
		 */
		public List<Metadata> queryInvestType(String id) {
			List<Metadata> list = new ArrayList<Metadata>();
			synchronized (helper) {
				if (!db.isOpen()) {
					db = helper.getWritableDatabase();
				}
				Cursor c;
				if (id == "15") {
					c = db.rawQuery("select * from tb_code where type= ?and useType= ?",
							new String[] { id ,"0.0"});
				}else{
					c = db.rawQuery("select * from tb_code where type= ?",
							new String[] { id });
				}
				try {
					while (c.moveToNext()) {
						Metadata data = new Metadata();
						data.id =c.getString(c.getColumnIndex("id"));
						data.name =c.getString(c.getColumnIndex("name"));
						data.number =c.getString(c.getColumnIndex("number"));
						data.parentid =c.getString(c.getColumnIndex("type"));
						data.hasChild=c.getInt(c.getColumnIndex("hasChild"));
						list.add(data);//当前的对象列表
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					c.close();
				}
			}
			return list;
		}

		/**
		 * 查询当前地区的子级
		 * 
		 * @param id
		 * @return
		 */
		public List<Metadata> queryArea(String id) {
			List<Metadata> list = new ArrayList<Metadata>();
			synchronized (helper) {
				if (!db.isOpen()) {
					db = helper.getWritableDatabase();
				}
				Cursor c = db.rawQuery(
						"select * from tb_code_region where parentId= ?",
						new String[] { id });
				try {
					while (c.moveToNext()) {
						Metadata data = new Metadata();
						data.id =c.getString(c.getColumnIndex("id"));
						data.name =c.getString(c.getColumnIndex("cname"));
						data.parentid =c.getString(c.getColumnIndex("parentId"));
						list.add(data);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					c.close();
				}
			}
			return list;
		}

		/**
		 * 获取第一级信息 国内+国外
		 * 
		 * @param id
		 * @return
		 */
		public List<Metadata> queryArea() {
			List<Metadata> list = new ArrayList<Metadata>();
			synchronized (helper) {
				if (!db.isOpen()) {
					db = helper.getWritableDatabase();
				}
				Cursor c = db
						.rawQuery(
								"select * from tb_code_region where id= ? or parentid= ? ",
								new String[] {"3418","1"});
				try {
					while (c.moveToNext()) {
						Metadata data = new Metadata();
						data.id = c.getString(c.getColumnIndex("id"));
						data.name=c.getString(c.getColumnIndex("cname"));
						data.parentid = c.getString(c.getColumnIndex("parentId"));
						list.add(data);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					c.close();
				}
			}
			return list;
		}
}
