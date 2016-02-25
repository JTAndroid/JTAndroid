package com.tr.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.tr.model.home.MSuggestionType;
import com.tr.ui.cloudDisk.model.FileCategoryManager;
import com.google.gson.reflect.TypeToken;
import com.tr.ui.cloudDisk.model.FileManagerResponseData;
import com.tr.ui.cloudDisk.model.UserCategory;
import com.tr.ui.cloudDisk.model.UserDocument;
import com.utils.http.EAPIConsts;

public class CloudDiskRespFactory {

	private static Gson gson;

	public static Object createMsgObject(int msgId, JSONObject response) throws JSONException {
		Gson gson = new Gson();
		Map<String, Object> dataMap = new HashMap<String, Object>(); // 返回数据
		String strKey = "";
		
		switch (msgId) {
		// 删除文件目录
		case EAPIConsts.CloudDiskType.fileDocumentDelete:
			if (response.has("succeed")) {
				String success = null;
				try {
					success = response.getString("succeed");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return success;
			}
			break;
		case EAPIConsts.CloudDiskType.queryAllRCategory://查询文件目录关系
			FileCategoryManager fileCategoryManager = new Gson().fromJson(response.toString(), FileCategoryManager.class);
			return fileCategoryManager;
		case EAPIConsts.CloudDiskType.searchFileDocument: 
				if (response.has("page")) {
					try {
						JSONObject jsonPageStr = (JSONObject) response.optJSONObject("page");
						if (jsonPageStr.has("list")) {
							String jsonStr = jsonPageStr.getJSONArray("list").toString();
							List<UserDocument> customerNoticeList = gson.fromJson(jsonStr, new TypeToken<List<UserDocument>>() {}.getType());
							if (customerNoticeList != null) {
								dataMap.put("list", customerNoticeList);
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					return dataMap;
				}
			break;
		case EAPIConsts.CloudDiskType.CHAT_SAVE_CATEGORY:
			strKey = "succeed";
			if (response.has(strKey)) {
				boolean success = response.getBoolean(strKey);
				dataMap.put(strKey, success);
				return dataMap;
			}
			break;
		case EAPIConsts.CloudDiskType.renamefile:
			strKey = "succeed";
			if (response.has(strKey)) {
				boolean success = response.getBoolean(strKey);
				dataMap.put(strKey, success);
				return dataMap;
			}
			break;
		case EAPIConsts.CloudDiskType.categoryDocumentSum:
			strKey = "succeed";
			if (response.has(strKey)) {
				if (response.has("sum")) {
					try {
						String sum = response.getString("sum");
						dataMap.put("sum", sum);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					return dataMap;
				}
			}
			break;
			
		case EAPIConsts.CloudDiskType.categorySaveOrUpdate:  //新增或者修改用户目录 fileManager/saveOrUpdateCategory.json
			FileManagerResponseData fileManagerResponseData = new FileManagerResponseData();
			if (response.has("succeed")) {
				fileManagerResponseData.succeed = response.getString("succeed");
			}
			if (response.has("resultType")) {
				fileManagerResponseData.resultType = response.getString("resultType");
			}
			if (response.has("resultMessage")) {
				fileManagerResponseData.resultMessage = response.getString("resultMessage");
			}
			return fileManagerResponseData;
		}
		return response;

	}
}
