package com.tr.api;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

public class CloudDiskReqUtil extends ReqBase {

	private final static String TAG = CloudDiskReqUtil.class.getSimpleName();

	/**
	 * 删除文件和目录
	 * @param context
	 * @param bind
	 * @param handler
	 * @param id
	 */
	public static void deleteFileDoucment(Context context, IBindData bind, Handler handler, String categoryIds ,String fileIds,String categoryId)
	{
		String requestStr = "";
		try {
			JSONObject jObject = new JSONObject();
			jObject.put("categoryIds", categoryIds);
			jObject.put("fileIds", fileIds);
			jObject.put("categoryId", categoryId);
			requestStr = jObject.toString();
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.fileDocumentDelete;
		doExecute(context, bind, EAPIConsts.CloudDiskType.fileDocumentDelete, url, requestStr, handler);
	}
	
	/**
	 * 文件管理搜索文件和目录
	 * @param context
	 * @param bind
	 * @param handler
	 * @param pid 父目录id
	 * @param type 搜索类型  -1-全局搜索(all),0-视频(video),1-音频(audio),2-文件(file),3-图片(image),4-其它(other)
	 * @param keyWord 关键字
	 * @param page 当前页
	 * @param size 每页大小，"每页大小，为-1时返回全部",
	 * @param id 目录id
	 * 
	 * 
	 */
	
	public static void searchFileDocument(Context context, IBindData bind, Handler handler, String sortId ,String type,String keyWord,String page,String size,String id)
	{
		
		
//	    "type":"类型:-1:all,0-视频(video),1-音频(audio),2-文件(file),3-图片(image)",
//	    "keyWord":"关键词",
//	    "sortId":"排序id",
//	    "page":"当前页",
//	    "size":"每页大小，为-1时返回全部",
//	    "id":"目录id"
	
		String requestStr = "";
		try {
			JSONObject jObject = new JSONObject();
			jObject.put("sortId", sortId);
			jObject.put("type", type);
			jObject.put("keyWord", keyWord);
			jObject.put("page", page);
			jObject.put("size", size);
			jObject.put("id", id);
			requestStr = jObject.toString();
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.searchFileDocument;
		doExecute(context, bind, EAPIConsts.CloudDiskType.searchFileDocument, url, requestStr, handler);

	}
	/**
	 * //1.4 查询文件目录关系 fileManager/queryAllRCategory.json
	 * @param context
	 * @param bind
	 * @param handler
	 * @param page 当前页
	 * @param size  每页大小
	 * @param id 目录id
	 */
	
	public static void queryAllRCategory(Context context, IBindData bind, Handler handler,String page,String size ,String id){
		String requestStr = "";
		try {
			JSONObject jObject = new JSONObject();
			jObject.put("page", page);
			jObject.put("size", size);
			jObject.put("id", id);
			requestStr = jObject.toString();
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.queryAllRCategory;
		doExecute(context, bind, EAPIConsts.CloudDiskType.queryAllRCategory, url, requestStr, handler);
	}
	
	/**
	 * 保存文件目录关系
	 * @param context
	 * @param bind
	 * @param handler
	 * @param fileIds 多个文件id用逗号分隔
	 * @param categoryIds 多个目录id用逗号分隔
	 */
	public static void doChatSaveCategory(Context context, IBindData bind, Handler handler,String fileIds ,String categoryIds){
		String requestStr = "";
		try {
			JSONObject jObject = new JSONObject();
			jObject.put("fileIds", fileIds);
			jObject.put("categoryIds", categoryIds);
			requestStr = jObject.toString();
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.CHAT_SAVE_CATEGORY;
		doExecute(context, bind, EAPIConsts.CloudDiskType.CHAT_SAVE_CATEGORY, url, requestStr, handler);
	}
	
	/**
	 *新增或者修改用户目录
	 * @param context
	 * @param bind
	 * 
	 * 
        "categoryName":"标签名称",
        "pid":"父目录id",
        "id": "目录id:''或者0为新增 ;>0为修改",
	 */
	public static void getCategorySaveOrUpdate(Context context, IBindData bind, Handler handler,String categoryName,String pid,String id) {
		String requestStr = "";
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("categoryName", categoryName);
		params.put("pid", pid);
		params.put("id", id);
		requestStr = new Gson().toJson(params);
		String url = EAPIConsts.getTMSUrl() + EAPIConsts.ReqUrl.CATEGORY_SAVEORUPDATE;
		doExecute(context, bind, EAPIConsts.CloudDiskType.categorySaveOrUpdate, url, requestStr, handler);
	}
	/**
	 *重命名文件
	 * @param context
	 * @param bind
	 * 
	 */
	public static void renameFile(Context context, IBindData bind, Handler handler,String fileId,String fileName,String categoryId,String suffixName) {
		String requestStr = "";
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("fileId", fileId);
		params.put("fileName", fileName);
		params.put("categoryId", categoryId);
		params.put("suffixName", suffixName);
		requestStr = new Gson().toJson(params);
		String url = EAPIConsts.getTMSUrl() + EAPIConsts.ReqUrl.RENAME_FILE;
		doExecute(context, bind, EAPIConsts.CloudDiskType.renamefile, url, requestStr, handler);
	}
	
	
	/**
	 * 获取所有文件的大小
	 * @param context
	 * @param bind
	 * @param handler
	 */
	public static void getSumBytes(Context context, IBindData bind, Handler handler) {
		String requestStr = "";
		HashMap<String, Object> params = new HashMap<String, Object>();
		requestStr = new Gson().toJson(params);
		String url = EAPIConsts.getTMSUrl() + EAPIConsts.ReqUrl.CATEGORY_DOCUMENT_SUM;
		doExecute(context, bind, EAPIConsts.CloudDiskType.categoryDocumentSum, url, requestStr, handler);
	}
}
