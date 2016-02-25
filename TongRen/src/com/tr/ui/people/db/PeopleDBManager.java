package com.tr.ui.people.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tr.db.DemandDBHelper;
import com.tr.model.demand.Metadata;

public class PeopleDBManager {

	// 常量
		private final String TAG = getClass().getSimpleName();

		// 变量
		private PeopleDBHelper helper;
		private SQLiteDatabase db;

		public PeopleDBManager(Context context) {
			helper = PeopleDBHelper.getInstance(context);
			synchronized (helper) {
				db = helper.getWritableDatabase();
			}
		}

		/**
		 * 获取所有的数据 8为融资需求，1为投资需求，15为行业
		 * 
		 * @return
		 */
		public List<Metadata> queryInvestType(int id) {
			List<Metadata> list = new ArrayList<Metadata>();
			synchronized (helper) {
				if (!db.isOpen()) {
					db = helper.getWritableDatabase();
				}
				Cursor c = db.rawQuery("select * from tb_code_sort where pid= ?",
						new String[] { id+"" });
				try {
					while (c.moveToNext()) {
						System.out.println("cursor里面有数据");
						Metadata data = new Metadata();
						data.id =c.getString(c.getColumnIndex("id"));
						data.name =c.getString(c.getColumnIndex("name"));
						data.parentid =c.getString(c.getColumnIndex("pid"));
						System.out.println("data.id=="+data.id);
						System.out.println("data.name=="+data.name);
						System.out.println("data.parentid=="+data.parentid);
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
		public List<Metadata> queryArea(String parentId) {
			List<Metadata> list = new ArrayList<Metadata>();
			synchronized (helper) {
				if (!db.isOpen()) {
					db = helper.getWritableDatabase();
				}
				Cursor c = db.rawQuery(
						"select * from tb_code_region where parentId= ?",
						new String[] { parentId });
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
		
		/**
		 * 通过传入的地区名字查询地区对应的id值
		 * @param name
		 * @return
		 */
		public int queryIdByName(String name){
			int id = -100;
			synchronized (helper) {
				if (!db.isOpen()) {
					db = helper.getWritableDatabase();
				}
				Cursor c = db.rawQuery("select id from tb_code_region where cname= ?",
						new String[] {name});
				if(c.moveToNext()){
					id = c.getInt(0);
				}
			    return id;
		   }
			
		}
		
		/**
		 * 通过传入的行业类名查询对应的id
		 */
		public int queryIdByIndustry(String name){
			int id = -100;
			synchronized (helper) {
				if (!db.isOpen()) {
					db = helper.getWritableDatabase();
				}
				Cursor c = db.rawQuery("select id from tb_code_sort where name= ?",
						new String[] {name});
				if(c.moveToNext()){
					id = c.getInt(0);
				}
			    return id;
		    }
		}
}

