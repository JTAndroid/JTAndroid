package com.tr.model.page; 

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tr.model.conference.MSociality;
import com.tr.model.obj.SearchResult;


/** 
 * @author xuxinjian
 * @E-mail: xuxinjian@gintong.com
 * @version 1.0
 * @创建时间：2014-4-12 上午10:10:22 
 * @类说明 
 */
public class JTPage implements Serializable {

	private static final long serialVersionUID = -7755047784444645672L;
	
	private int total;// 总元素个数
	private int size;// 该页内容数
	private int index;// 页码，页码从0开始
	private int totalPage; // 页面总数

	private ArrayList<IPageBaseItem> lists;//具体页容器存放的数据内容，内容需要从 IPageBaseItem继承
	
	public List<MSociality>  listSocial = new ArrayList<MSociality>();

	/**
	 * @param jsonObject 页的json对象
	 * @param strPageName 业内元素名称， 如  "listMessage"
	 * @param IPageBaseItemFactory 解析页内listObject 中的Object 的方法
	 * @return 返回页对象
	 */
	public static JTPage createFactory(JSONObject jsonObjectIn, String strPageName, IPageBaseItemFactory factory) {
		JTPage self = null;
		
		try {
			JSONObject jsonObject = jsonObjectIn.optJSONObject("page");
			if(jsonObject == null)
				jsonObject = jsonObjectIn.optJSONObject("Page");
			if (jsonObject != null) {
				self = new JTPage();
				self.total 		= jsonObject.optInt("total");
				self.index 		= jsonObject.optInt("index");
				self.size  		= jsonObject.optInt("size");
				self.totalPage 	= jsonObject.optInt("totalPage");
				self.lists = new ArrayList<IPageBaseItem>();
				JSONArray arr = jsonObject.optJSONArray(strPageName);
				if(arr != null){
					for(int i = 0; i < arr.length(); i++){
						JSONObject obj = arr.getJSONObject(i);
						if(obj != null){
							IPageBaseItem item = factory.paserByObject(obj);
							if(item != null)
								self.lists.add(item);
						}
					}
				}
			}

			return self;

		} catch (Exception e) {
		}
		return null;
	}
	
	
	
	/**
	 * @param jsonObject 页的json对象
	 * @param strPageName 业内元素名称， 如  "listMessage"
	 * @param IPageBaseItemFactory 解析页内listObject 中的Object 的方法
	 * @return 返回页对象
	 */
	public static JTPage createDynamicFactory(JSONObject jsonObjectIn, String strPageName, IPageBaseItemFactory factory) {
		JTPage self = null;
		
		try {
			JSONObject jsonObject = jsonObjectIn.optJSONObject("page");
			if(jsonObject == null)
				jsonObject = jsonObjectIn.optJSONObject("Page");
				self = new JTPage();
				self.total 		= jsonObject.optInt("total");
				self.index 		= jsonObject.optInt("index");
				self.size  		= jsonObject.optInt("size");
				self.totalPage 	= jsonObject.optInt("totalPage");
				self.lists = new ArrayList<IPageBaseItem>();
				JSONArray arr = jsonObject.optJSONArray(strPageName);
				if(arr != null){
					for(int i = 0; i < arr.length(); i++){
						JSONObject obj = arr.getJSONObject(i);
						if(obj != null){
							IPageBaseItem item = factory.paserByObject(obj);
							if(item != null)
								self.lists.add(item);
						}
					}
			}
			Log.v("ADD", "self---->"+self.toString());
			return self;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @param jsonObject 页的json对象
	 * @param strPageName 业内元素名称， 如  "listMessage"
	 * @param IPageBaseItemFactory 解析页内listObject 中的Object 的方法
	 * @return 返回页对象
	 */
	public static JTPage createSocialityListFactory(JSONObject jsonObjectIn) {
		JTPage self = null;
		Gson gson = new Gson();
		try {
			JSONObject jsonObject = jsonObjectIn.optJSONObject("page");
			if(jsonObject == null){
				jsonObject = jsonObjectIn.optJSONObject("Page");
			}
				self = new JTPage();
				self.total 		= jsonObject.optInt("total");
				self.index 		= jsonObject.optInt("index");
				self.size  		= jsonObject.optInt("size");
				self.totalPage 	= jsonObject.optInt("totalPage");

				String strKey = "listSocial";
				String jsonString = jsonObject.getJSONArray(strKey).toString();
				self.listSocial = gson.fromJson(jsonString, new TypeToken<List<MSociality>>(){}.getType());
			return self;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @param jsonObject 页的json对象
	 * @param strPageName 业内元素名称， 如
	 * @param IPageBaseItemFactory 解析页内listObject 中的Object 的方法
	 * @return 返回页对象
	 */
	public static JTPage createDemandList(JSONObject jsonObjectIn, String strPageName, IPageBaseItemFactory factory) {
		JTPage self = null;
		
		try {
			JSONObject jsonObject = jsonObjectIn.optJSONObject("page");
			if(jsonObject == null)
				jsonObject = jsonObjectIn.optJSONObject("Page");
			if (jsonObject != null) {
				self = new JTPage();
				self.total 		= jsonObject.optInt("total");
				self.index 		= jsonObject.optInt("index");
				self.size  		= jsonObject.optInt("size");
				self.totalPage 	=self.total%self.size==0? self.total/self.size:(self.total/self.size+1);
				self.lists = new ArrayList<IPageBaseItem>();
				JSONArray arr = jsonObject.optJSONArray(strPageName);
				if(arr != null){
					int length = arr.length();
					for(int i = 0; i < length; i++){
						JSONObject obj = arr.getJSONObject(i);
						if(obj != null){
							IPageBaseItem item = factory.paserByObject(obj);
							if(item != null)
								self.lists.add(item);
						}
					}
				}
			}
			return self;
		} catch (Exception e) {
			return null;
		}
		
	}
	
	/**
	 * @param jsonObject 页的json对象
	 * @param strPageName 业内元素名称， 如  "listMessage"
	 * @param IPageBaseItemFactory 解析页内listObject 中的Object 的方法
	 * @return 返回页对象
	 */
	public static JTPage createFactoryforMeeting(JSONObject jsonObjectIn, String strPageName, IPageBaseItemFactory factory) {
		JTPage self = null;
		
		try {
			JSONObject jsonObject = jsonObjectIn.optJSONObject("page");
			if(jsonObject == null)
				jsonObject = jsonObjectIn.optJSONObject("Page");
			if (jsonObject != null) {
				self = new JTPage();
				self.total 		= jsonObject.optInt("total");
				self.index 		= jsonObject.optInt("index");
				self.size  		= jsonObject.optInt("size");
				self.totalPage 	= (self.total%self.size) == 0 || self.size == 0 ? self.total/self.size : (self.total/self.size+1);
				self.lists = new ArrayList<IPageBaseItem>();
				JSONArray arr = jsonObject.optJSONArray(strPageName);
				if(arr != null){
					for(int i = 0; i < arr.length(); i++){
						JSONObject obj = arr.getJSONObject(i);
						if(obj != null){
							//IPageBaseItem item = factory.paserByObject(obj);
							SearchResult item = new SearchResult();
							item.setId(obj.optString("id"));
							item.setTitle(obj.optString("meeting_name"));
							item.setContent(obj.optString("meeting_desc"));
							item.setSource(obj.optString("source"));
							item.setTime(obj.optString("time"));
							item.setImage(obj.optString("pic"));
							if(item != null)
								self.lists.add(item);
						}
					}
				}
			}
			
			return self;
			
		} catch (Exception e) {
		}
		return null;
	}
	
	
	
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public ArrayList<IPageBaseItem> getLists() {
		return lists;
	}

	public void setLists(ArrayList<IPageBaseItem> lists) {
		this.lists = lists;
	}
	
	public boolean hasMore(){
		if((getIndex()+1)*getSize() >= getTotal())
			return false;
		else
			return true;
	}
	@Override
	public String toString() {
		return "JTPage [total=" + total + ", size=" + size + ", index=" + index
				+ ", totalPage=" + totalPage + ", lists=" + lists + "]";
	}
	
	
}
 