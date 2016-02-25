package com.tr.api;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tr.model.joint.ConnectionNode;
import com.tr.model.knowledge.Column;
import com.tr.model.knowledge.Knowledge2;
import com.tr.model.knowledge.KnowledgeComment;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.knowledge.UserCategory;
import com.tr.model.obj.Connections;
import com.utils.common.QJsonParser;
import com.utils.common.QJsonParser.DataType;
import com.utils.http.EAPIConsts.KnoReqType;

public class KnowledgeRespFactory {

	private static final String TAG = KnowledgeRespFactory.class
			.getSimpleName();

	public static Object createMsgObject(int msgId, JSONObject jsonObject)
			throws JSONException {
		Gson gson = new GsonBuilder().serializeNulls().setVersion(1.0).create();
		if (jsonObject == null) {
			return null;
		}

		Map<String, Object> dataMap = new HashMap<String, Object>(); // 返回数据
		String strKey = "";
		String jsonStr = "";

		switch (msgId) {
		case KnoReqType.updateKnowledge: // 更新用户知识
			strKey = "knowledge2";
			if (jsonObject.has(strKey)) {
				jsonStr = jsonObject.getJSONObject(strKey).toString();
//				string2File(jsonStr);
				Knowledge2 knowledge =  new Gson().fromJson(jsonStr, Knowledge2.class);
				if(knowledge != null){
					for (ConnectionNode listNodes : knowledge.getListRelatedConnectionsNode()) {
						for (Connections connection : listNodes.getListConnections()) {
							connection.doCompatible();
						}
					}
					for (ConnectionNode OrgNodes : knowledge.getListRelatedOrganizationNode()) {
						for (Connections orgItem : OrgNodes.getListConnections()) {
							orgItem.doCompatible();
						}
					}
					dataMap.put(strKey, knowledge);
				}
				
			}
			break;
		case KnoReqType.GetUserCategory: // 获取用户目录
		case KnoReqType.AddUserCategory: // 添加知识目录
		case KnoReqType.EditUserCategory: // 编辑知识目录
		case KnoReqType.DelUserCategory: // 删除知识目录
//			System.out.println(jsonObject.toString());
			strKey = "success";
			if (!jsonObject.isNull(strKey)) {
				dataMap.put(strKey, jsonObject.optBoolean(strKey));
			}
			strKey = "listUserCategory";
			if (!jsonObject.isNull(strKey)) {
//				List<UserCategory> listCategory = JSON.parseArray(jsonObject.getJSONArray(strKey).toString(), UserCategory.class);
				String jsonString = jsonObject.getJSONArray(strKey).toString();
				List<UserCategory> listCategory = gson.fromJson(jsonString, new TypeToken<List<UserCategory>>(){}.getType());
				if (listCategory != null) {
					dataMap.put(strKey, listCategory);
				}
			}
			return dataMap;
		case KnoReqType.GetColumnByUserId: // 获取用户栏目
			strKey = "column";
			if (jsonObject.has(strKey)) {
				jsonStr = jsonObject.getJSONObject(strKey).toString();
//				Column column = JSON.parseObject(jsonStr, Column.class);
				Column column = gson.fromJson(jsonStr,Column.class);
				return column;
			}
			break;

		case KnoReqType.GetSubscribedColumnByUserId: // 获取用户订阅的栏目
			strKey = "listColumn";
			if (jsonObject.has(strKey)) {
//				jsonStr = jsonObject.getJSONArray(strKey).toString();
				/*List<Column> listColumn = JSON
						.parseArray(jsonStr, Column.class);*/
				
				String jsonString = jsonObject.getJSONArray(strKey).toString();
				List<Column> listColumn = gson.fromJson(jsonString, new TypeToken<List<Column>>(){}.getType());
				// return listColumn;
				dataMap.put(strKey, listColumn);
			}
			break;
		case KnoReqType.EditSubscribedColumn: // 订阅/取消订阅
			strKey = "success";
			if (jsonObject.has(strKey)) {
				boolean success = jsonObject.getBoolean(strKey);
				return success;
			}
			break;
		case KnoReqType.UpdateSubscribedColumn: // 更新订阅的栏目
			strKey = "succeed";
			if (jsonObject.has(strKey)) {
				boolean success = jsonObject.getBoolean(strKey);
				return success;
			}
			break;
			
		//解析Url类型的知识
		case KnoReqType.FetchExternalKnowledgeUrl:
		// 创建知识
		case KnoReqType.CreateKnowledge:
		case KnoReqType.GetKnoDetails:// 获取知识详情
			strKey = "knowledge2";
			if (jsonObject.has(strKey)) {
				jsonStr = jsonObject.getJSONObject(strKey).toString();
				Knowledge2 knowledge =  new Gson().fromJson(jsonStr, Knowledge2.class);
				if(knowledge != null){
					for (ConnectionNode listNodes : knowledge.getListRelatedConnectionsNode()) {
						for (Connections connection : listNodes.getListConnections()) {
							connection.doCompatible();
						}
					}
					for (ConnectionNode OrgNodes : knowledge.getListRelatedOrganizationNode()) {
						for (Connections orgItem : OrgNodes.getListConnections()) {
							orgItem.doCompatible();
						}
					}
					dataMap.put(strKey, knowledge);
				}
			}
			break;
		// 获取知识标签
		case KnoReqType.GetKnowledgeTagList:
			strKey = "listCount";
			if (jsonObject.has(strKey)) {
			/*	String listCountStr = jsonObject.getJSONArray(strKey)
						.toString();
				List<Integer> listCount = JSON.parseArray(listCountStr,
						Integer.class);*/
				String jsonString = jsonObject.getJSONArray(strKey)
						.toString();
				List<Integer> listCount = gson.fromJson(jsonString, new TypeToken<List<Integer>>(){}.getType());
				dataMap.put(strKey, listCount);
			}

			strKey = "listTag";
			if (jsonObject.has(strKey)) {
				/*String listTagStr = jsonObject.getJSONArray(strKey).toString();
				List<String> listTag = JSON
						.parseArray(listTagStr, String.class);*/
				String jsonString = jsonObject.getJSONArray(strKey).toString();
				List<String> listTag = gson.fromJson(jsonString, new TypeToken<List<String>>(){}.getType());
				dataMap.put(strKey, listTag);
			}
			break;
		
		//编辑用户的知识标签
		case KnoReqType.EditUserKnowledgeTag:
			strKey = "success";
			if (!jsonObject.isNull(strKey)) {
				dataMap.put(strKey, jsonObject.optBoolean(strKey));
			}
			strKey = "listCount";
			if (jsonObject.has(strKey)) {
				String jsonString = jsonObject.getJSONArray(strKey)
						.toString();
				List<Integer> listCount = gson.fromJson(jsonString, new TypeToken<List<Integer>>(){}.getType());
				dataMap.put(strKey, listCount);
			}

			strKey = "listTag";
			if (jsonObject.has(strKey)) {
				String jsonString = jsonObject.getJSONArray(strKey).toString();
				List<String> listTag = gson.fromJson(jsonString, new TypeToken<List<String>>(){}.getType());
				dataMap.put(strKey, listTag);
			}
			
			
//			QJsonParser.parse("success", DataType.BOOLEAN, dataMap, jsonObject);
//			QJsonParser.parseArray("listTag", String.class, dataMap, jsonObject);
//			QJsonParser.parseArray("listCount", Integer.class, dataMap, jsonObject);
			
//			QJsonParser.parseArray("listTag", DataType.STRING, dataMap, jsonObject);
//			QJsonParser.parseArray("listCount", DataType.INTEGER, dataMap, jsonObject);
			
			break;
			

		/*// 创建知识
		case KnoReqType.CreateKnowledge:
			strKey = "knowledge2";
			if (jsonObject.has(strKey)) {
				jsonStr = jsonObject.getJSONObject(strKey).toString();
				Knowledge2 knowledge = new Gson().fromJson(jsonStr, Knowledge2.class);
				if(knowledge != null){
					for (ConnectionNode listNodes : knowledge.getListRelatedConnectionsNode()) {
						for (Connections connection : listNodes.getListConnections()) {
							connection.doCompatible();
						}
					}
					dataMap.put(strKey, knowledge);
				}
			}
			break;*/

		// 编辑指定知识的标签
		case KnoReqType.EditKnoTagByKnoId:
			strKey = "success";
			if (jsonObject.has(strKey)) {
				boolean success = jsonObject.getBoolean(strKey);
				return success;
			}
			break;

		// 根据栏目和来源获取知识列表 (和下面解析一样)
		case KnoReqType.GetKnowledgeByColumnAndSource:
			// 根据标签名和关键字分页获取知识列表
		case KnoReqType.GetKnowledgeByTagAndKeyword:
			// 根据目录id和关键字分页获取知识列表
		case KnoReqType.GetKnowledgeByUserCategoryAndKeyword:
			// 根据类型（全部、我收藏的、分享给我的、我创建的）和关键字分页获取知识列表
		case KnoReqType.GetKnowledgeByTypeAndKeyword:
			strKey = "page";
			if (jsonObject.has(strKey)) {
				JSONObject jSubObject = jsonObject.getJSONObject(strKey);
				strKey = "listKnowledgeMini";
				
				if (jSubObject.has(strKey)) {
					JSONArray jsonArray = jSubObject.getJSONArray(strKey);
					List<KnowledgeMini2> list = new ArrayList<KnowledgeMini2>();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jObject = (JSONObject) jsonArray.get(i);
						
						String ownerName = jObject.optString("ownerName");
						String desc = jObject.optString("desc");
						int ownerId = jObject.optInt("ownerId");
						ArrayList<String> listTag=new ArrayList<String>();
						JSONArray tag = jObject.optJSONArray("listTag");
						String tagText = jObject.optString("tag");
						if (tag!=null) {
							for (int j = 0; j < tag.length(); j++) {
								String str=(String) tag.opt(j);
								listTag.add(str);
							}
							
						}
						String pic = jObject.optString("pic");
						int type = jObject.optInt("type");
						String url = jObject.optString("url");
						String modifytime=jObject.optString("modifytime");
						int id = jObject.optInt("id");
						String title = jObject.optString("title");
						String source = jObject.optString("source");
						String name = jObject.optString("name");
						int shareMeId = jObject.optInt("shareMeId");
						String columnpath = jObject.optString("columnpath");
						JSONObject JbConnections = jObject.optJSONObject("Connections");
						Connections connections = new Connections(); 
						if(JbConnections!=null){
							try {
								connections.initWithJson(JbConnections);
							} catch (MalformedURLException e) {
								e.printStackTrace();
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
						KnowledgeMini2 knowledgeMini2 = new KnowledgeMini2(id, type, title, desc, modifytime, pic, listTag,columnpath, connections, shareMeId,tagText);
						list.add(knowledgeMini2);
					}
//					String jsonString = jSubObject.getJSONArray(strKey).toString();
//					List<KnowledgeMini2> list = gson.fromJson(jsonString, new TypeToken<List<KnowledgeMini2>>(){}.getType());
					dataMap.put(strKey, list);
				}
				strKey = "total";
				if (jSubObject.has(strKey)) {
					int obj = jSubObject.getInt(strKey);
					dataMap.put(strKey, obj);
				}
				strKey = "index";
				if (jSubObject.has(strKey)) {
					int obj = jSubObject.getInt(strKey);
					dataMap.put(strKey, obj);
				}
				strKey = "size";
				if (jSubObject.has(strKey)) {
					int obj = jSubObject.getInt(strKey);
					dataMap.put(strKey, obj);
				}
			}
			break;

		// 删除指定的知识
		case KnoReqType.DeleteKnowledgeById:
			strKey = "success";
			if (jsonObject.has(strKey)) {
				boolean obj = jsonObject.getBoolean(strKey);
				dataMap.put(strKey, obj);
			}

			strKey = "page";
			if (jsonObject.has(strKey)) {
				JSONObject jSubObject = jsonObject.getJSONObject(strKey);

				strKey = "listKnowledgeMini";
				if (jSubObject.has(strKey)) {
					/*jsonStr = jSubObject.getJSONArray(strKey).toString();
					List<KnowledgeMini2> list = JSON.parseArray(jsonStr,
							KnowledgeMini2.class);*/
					
					String jsonString = jSubObject.getJSONArray(strKey).toString();
					List<KnowledgeMini2> list = gson.fromJson(jsonString, new TypeToken<List<KnowledgeMini2>>(){}.getType());
					dataMap.put(strKey, list);
				}
				strKey = "total";
				if (jSubObject.has(strKey)) {
					int obj = jSubObject.getInt(strKey);
					dataMap.put(strKey, obj);
				}
				strKey = "index";
				if (jSubObject.has(strKey)) {
					int obj = jSubObject.getInt(strKey);
					dataMap.put(strKey, obj);
				}
				strKey = "size";
				if (jSubObject.has(strKey)) {
					int obj = jSubObject.getInt(strKey);
					dataMap.put(strKey, obj);
				}
			}

			break;
		/** 获取各种详情页的评论列表， 主要包括需求、业务需求、任务、项目 */
		case KnoReqType.GetKnoCommentsByType:
			strKey = "page";
			if (jsonObject.has(strKey)) {
				JSONObject jSubObject = jsonObject.getJSONObject(strKey);

				strKey = "total";
				if (jSubObject.has(strKey)) {
					int obj = jSubObject.getInt(strKey);
					dataMap.put(strKey, obj);
				}

				strKey = "listComment";
				if (jSubObject.has(strKey)) {
					/*jsonStr = jSubObject.getJSONArray(strKey).toString();
					List<KnowledgeComment> listComments = JSON.parseArray(
							jsonStr, KnowledgeComment.class);*/
					String jsonString = jSubObject.getJSONArray(strKey).toString();
					List<KnowledgeComment> listComments = gson.fromJson(jsonString, new TypeToken<List<KnowledgeComment>>(){}.getType());
					// 添加测试数据
					for (int i = 0; i < 5; i++) {
						KnowledgeComment comment = new KnowledgeComment();
						comment.content = "comment ... ";
						listComments.add(comment);
					}
					dataMap.put(strKey, listComments);
				}

				strKey = "index";
				if (jSubObject.has(strKey)) {
					int obj = jSubObject.getInt(strKey);
					dataMap.put(strKey, obj);
				}
				strKey = "size";
				if (jSubObject.has(strKey)) {
					int obj = jSubObject.getInt(strKey);
					dataMap.put(strKey, obj);
				}
			}
			break;
		/** 查询是否收藏该知识 */
		case KnoReqType.GetCollectKnowledgeState:
			strKey = "succeed";
			if (jsonObject.has(strKey)) {
				JSONObject jSubObject = jsonObject.getJSONObject(strKey);
				dataMap.put(strKey, jSubObject);
			}
			break;
		/** 编辑收藏知识状态 */
		case KnoReqType.UpdateCollectKnowledge:
			strKey = "succeed";
			if (jsonObject.has(strKey)) {
				boolean success = jsonObject.getBoolean(strKey);
				dataMap.put(strKey, success);
			}
			break;
		case KnoReqType.AddKnowledgeComment:/**对知识或评论发表评论*/
			strKey = "success";
			if (jsonObject.has(strKey)) {
				boolean obj = jsonObject.getBoolean(strKey);
				dataMap.put(strKey, obj);
			}
			strKey = "page";
			if (jsonObject.has(strKey)) {
				JSONObject jSubObject = jsonObject.getJSONObject(strKey);

				strKey = "listKnowledgeComment";
				if (jSubObject.has(strKey)) {
					/*jsonStr = jSubObject.getJSONArray(strKey).toString();
					List<KnowledgeComment> listComments = JSON.parseArray(jsonStr,
							KnowledgeComment.class);*/
					
					String jsonString = jSubObject.getJSONArray(strKey).toString();
					List<KnowledgeComment> listComments = gson.fromJson(jsonString, new TypeToken<List<KnowledgeComment>>(){}.getType());
					dataMap.put(strKey, listComments);
				}

				strKey = "total";
				if (jSubObject.has(strKey)) {
					int obj = jSubObject.optInt(strKey);
					dataMap.put(strKey, obj);
				}

				/*strKey = "index";
				if (jSubObject.has(strKey)) {
					int obj = jSubObject.optInt(strKey);
					dataMap.put(strKey, obj);
				}
				strKey = "size";
				if (jSubObject.has(strKey)) {
					int obj = jSubObject.optInt(strKey);
					dataMap.put(strKey, obj);
				}*/
			}
			break;
		case KnoReqType.GetKnowledgeComment:// 获取知识或评论的评论
			strKey = "page";
			if (jsonObject.has(strKey)) {
				JSONObject jSubObject = jsonObject.getJSONObject(strKey);

				strKey = "listKnowledgeComment";
				if (jSubObject.has(strKey)) {
					/*jsonStr = jSubObject.getJSONArray(strKey).toString();
					List<KnowledgeComment> listKnowledgeComments = JSON.parseArray(jsonStr,
							KnowledgeComment.class);*/
					String jsonString = jSubObject.getJSONArray(strKey).toString();
					List<KnowledgeComment> listKnowledgeComments = gson.fromJson(jsonString, new TypeToken<List<KnowledgeComment>>(){}.getType());
					dataMap.put(strKey, listKnowledgeComments);
				}

				strKey = "total";
				if (jSubObject.has(strKey)) {
					if(!jSubObject.isNull(strKey)){
					    int obj= jSubObject.optInt(strKey);
						dataMap.put(strKey, obj);
					}else{
						dataMap.put(strKey, 0);
					}
				}

				strKey = "index";
				if (jSubObject.has(strKey)) {
					int obj = jSubObject.optInt(strKey);
					dataMap.put(strKey, obj);
				}
				strKey = "size";
				if (jSubObject.has(strKey)) {
					int obj = jSubObject.optInt(strKey);
					dataMap.put(strKey, obj);
				}
			}
			break;
		}
		return dataMap;
	}	
}


