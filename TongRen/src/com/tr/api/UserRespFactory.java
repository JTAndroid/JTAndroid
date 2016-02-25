package com.tr.api;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tr.App;
import com.tr.model.api.DataBox;
import com.tr.model.obj.AffairsMini;
import com.tr.model.obj.Area;
import com.tr.model.obj.Comment;
import com.tr.model.obj.DynamicComment;
import com.tr.model.obj.InvestType;
import com.tr.model.obj.Knowledge;
import com.tr.model.obj.KnowledgeMini;
import com.tr.model.obj.MoneyType;
import com.tr.model.obj.Requirement;
import com.tr.model.obj.RequirementMini;
import com.tr.model.obj.Trade;
import com.tr.model.obj.TreatedHtml;
import com.tr.model.user.JTMember;
import com.tr.ui.organization.model.CustomerProfileVo;
import com.tr.ui.organization.model.OrgInfoVo;
import com.utils.http.EAPIConsts;

/**
 * @ClassName:     UserRespFactory.java
 * @Description:   用户相关的接口回调
 * @Author         leon
 * @Version        v 1.0  
 * @Created        2014-04-11
 * @Updated        2014-04-14
 */
public class UserRespFactory {
	private final static String keyJTMember = "jtMember";

	public static Object createMsgObject(int msgId, JSONObject jsonObject) {

		try {
			DataBox dataBox = new DataBox(); // 返回对象
			Gson gson = new Gson();
			String str_key = ""; // 键值
			switch (msgId) {
			case EAPIConsts.ReqType.LOGIN_CONFIGURATION: { // 登录配置
				// 更新模式
				str_key = "updateMode";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) { 
					dataBox.mUpdateMode = jsonObject.getInt(str_key);
				}
				// 更新信息
				str_key = "updateInfo";
				if(jsonObject.has(str_key) && !jsonObject.isNull(str_key)){ 
					dataBox.mUpdateInfo = jsonObject.getString(str_key);
				}
				// 下载地址
				str_key = "url";
				if(jsonObject.has(str_key) && !jsonObject.isNull(str_key)){ 
					dataBox.mUrl = jsonObject.getString(str_key);
				}
				// 会话ID
				str_key = "sessionID";
				if(jsonObject.has(str_key) && !jsonObject.isNull(str_key)){ 
					dataBox.mSessionID = jsonObject.getString(str_key);
				}
				// 会员对象
				str_key = "jtMember";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					JTMember member = new JTMember();
					String jsonStr = jsonObject.getJSONObject(str_key).toString();
					member.initWithJson(jsonObject.getJSONObject(str_key));
					dataBox.mJTMember = member;
				}
				// 会员对象
				str_key = "JTMember";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					JTMember member = new JTMember();
					member.initWithJson(jsonObject.getJSONObject(str_key));
					dataBox.mJTMember = member;
				}
				// 常量
				str_key = "const";
				if(jsonObject.has(str_key) && ! jsonObject.isNull(str_key)){
					JSONObject subJsonObject = jsonObject.getJSONObject(str_key);
					// 货币类型
					str_key = "listMoneyType";
					if(subJsonObject.has(str_key) && !subJsonObject.isNull(str_key)){
						JSONArray jArray = subJsonObject.getJSONArray(str_key);
						if (jArray != null && jArray.length()>0) {
							dataBox.mListMoneyType = new ArrayList<MoneyType>();
							for (int i = 0; i < jArray.length(); i++) {
								MoneyType m_type = new MoneyType();
								m_type.initWithJson(jArray.getJSONObject(i));
								dataBox.mListMoneyType.add(m_type);
							}
						}
					}
					// 投资金额范围
					str_key = "listMoneyRange";
					if (subJsonObject.has(str_key)
							&& !subJsonObject.isNull(str_key)) {
						JSONArray jArray = subJsonObject.getJSONArray(str_key);
						if (jArray != null && jArray.length() > 0) {
							dataBox.mListMoneyRange = new ArrayList<String>();
							for (int i = 0; i < jArray.length(); i++) {
								dataBox.mListMoneyRange.add(jArray.getString(i));
							}
						}
					}
					// 投资类型
					str_key = "listInInvestType";
					if(subJsonObject.has(str_key) && !subJsonObject.isNull(str_key)){
						JSONArray jArray = subJsonObject.getJSONArray(str_key);
						if (jArray != null && jArray.length() >0) {
							dataBox.mListInInvestType = new ArrayList<InvestType>();;
							for (int i = 0; i < jArray.length(); i++) {
								InvestType type = new InvestType();
								type.initWithJson(jArray.getJSONObject(i));
								dataBox.mListInInvestType.add(type);
							}
						}
					}
					// 融资类型
					str_key = "listOutInvestType";
					if(subJsonObject.has(str_key) && !subJsonObject.isNull(str_key)){
						JSONArray jArray = subJsonObject.getJSONArray(str_key);
						if (jArray != null && jArray.length() >0) {
							dataBox.mListOutInvestType = new ArrayList<InvestType>();;
							for (int i = 0; i < jArray.length(); i++) {
								InvestType type = new InvestType();
								type.initWithJson(jArray.getJSONObject(i));
								dataBox.mListOutInvestType.add(type);
							}
						}
					}
					// 投资区域
					str_key = "listArea";
					if(subJsonObject.has(str_key) && !subJsonObject.isNull(str_key)){
						JSONArray jArray = subJsonObject.getJSONArray(str_key);
						if (jArray != null && jArray.length()>0) {
							dataBox.mListArea = new ArrayList<Area>();
							for (int i = 0; i < jArray.length(); i++) {
								Area area = new Area();
								area.initWithJson(jArray.getJSONObject(i));
								dataBox.mListArea.add(area);
							}
						}
					}
					// 投资行业
					str_key = "listTrade";
					if(subJsonObject.has(str_key) && !subJsonObject.isNull(str_key)){
						JSONArray jArray = subJsonObject.getJSONArray(str_key);
						if (jArray != null && jArray.length()>0) {
							dataBox.mListTrade = new ArrayList<Trade>();
							for (int i = 0; i < jArray.length(); i++) {
								Trade trade = new Trade();
								trade.initWithJson(jArray.getJSONObject(i));
								dataBox.mListTrade.add(trade);
							}
						}
					}
				}
				// "邀请用户加入金桐网的邀请语"
				str_key = "inviteJoinGinTongInfo";
				
				if(jsonObject.has(str_key)){
					dataBox.mInviteJoinGinTongInfo = jsonObject.optString(str_key);
				}
				//Log.v("343", jsonObject.toString());
				break;
			}
			case EAPIConsts.ReqType.LOGIN: // 登录
				// 会员对象
				str_key = "jtMember";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					JTMember member = new JTMember();
					member.initWithJson(jsonObject.getJSONObject(str_key));
					dataBox.mJTMember = member;
				}
				str_key = "JTMember";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					JTMember member = new JTMember();
					member.initWithJson(jsonObject.getJSONObject(str_key));
					dataBox.mJTMember = member;
				}
				break;
			case EAPIConsts.ReqType.LOGIN_OUT: // 登出
				// 会话ID
				str_key = "sessionID";
				if(jsonObject.has(str_key)){ 
					dataBox.mSessionID = jsonObject.optString(str_key);
				}
				break;
			case EAPIConsts.ReqType.REGISTER: { // 注册
				str_key = "jtMember";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					JTMember member = new JTMember();
					member.initWithJson(jsonObject.getJSONObject(str_key));
					dataBox.mJTMember = member;
				}
				str_key = "JTMember";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					JTMember member = new JTMember();
					member.initWithJson(jsonObject.getJSONObject(str_key));
					dataBox.mJTMember = member;
				}
				break;
			}
			case EAPIConsts.ReqType.FINDORG: { // 查询组织信息
				str_key = "orgInfo";
				if (jsonObject.has(str_key)) {
					String jsonStr = jsonObject.getJSONObject(str_key).toString();
					OrgInfoVo orgInfoVo = gson.fromJson(jsonStr, OrgInfoVo.class);
					if (orgInfoVo != null) {
						dataBox.orgInfoVo = orgInfoVo;
					}

				}
				break;
			}
			case EAPIConsts.ReqType.GET_VERIFY_CODE: { // 获取验证码
				str_key = "succeed";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					dataBox.mIsSuccess = jsonObject.getBoolean(str_key);
				}
				str_key = "mobileCode";
				if(jsonObject.has(str_key) && !jsonObject.isNull(str_key)){
					dataBox.mVerifyCode = jsonObject.getString(str_key);
				}
				break;
			}
			case EAPIConsts.ReqType.SEND_VALIDATE_EMAIL:{ // 发送验证邮箱
				str_key = "succeed";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					dataBox.mIsSuccess = jsonObject.getBoolean(str_key);
				}
				break;
			}
			case EAPIConsts.ReqType.SET_NEW_PASSWORD: { // 设置新密码
				str_key = "succeed";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					dataBox.mIsSuccess = jsonObject.getBoolean(str_key);
				}
				str_key = "showInfo";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					dataBox.mMessage = jsonObject.getString(str_key);
				}
				break;
			}
			case EAPIConsts.ReqType.FULL_PERSON_MEMBER_INFO: { // 完善个人信息
				str_key = "jtMember";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					JTMember member = new JTMember();
					member.initWithJson(jsonObject.getJSONObject(str_key));
					dataBox.mJTMember = member;
				}
				str_key = "JTMember";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					JTMember member = new JTMember();
					member.initWithJson(jsonObject.getJSONObject(str_key));
					dataBox.mJTMember = member;
				}
				break;
			}
			case EAPIConsts.ReqType.FULL_CONTACT_INFO: { // 完善机构联系人信息
				str_key ="succeed";
				if (jsonObject.has(str_key)) {
					dataBox.mIsSuccess = jsonObject.optBoolean(str_key);
				}
				break;
			}
			case EAPIConsts.ReqType.FULL_ORGANIZATION_AUTH: { // 完善机构信息
				str_key ="succeed";
				if (jsonObject.has(str_key)) {
					dataBox.mIsSuccess = jsonObject.optBoolean(str_key);
				}
				break;
			}
			case EAPIConsts.ReqType.ADD_REQUIREMENT:{ // 发布需求
				str_key = "requirement"; // 是否成功
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					Requirement req = new Requirement();
					req.initWithJson(jsonObject.getJSONObject(str_key));
					dataBox.mRequirement = req;
				}
				break;
			}
			case EAPIConsts.ReqType.GET_REQUIREMENT_BY_ID:{ // 获取需求详情
				str_key = "requirement"; // 需求对象
				if(jsonObject.has(str_key) && !jsonObject.isNull(str_key)){
					Requirement req = new Requirement();
					req.initWithJson(jsonObject.getJSONObject(str_key));
					dataBox.mRequirement = req;
				}
				break;
			}
			case EAPIConsts.ReqType.FOCUS_REQUIREMENT:{ // 关注需求
				str_key = "succeed"; // 是否成功
				if (jsonObject.has(str_key)) {
					dataBox.mIsSuccess = jsonObject.optBoolean(str_key);
				}
				break;
			}
			case EAPIConsts.ReqType.EDIT_REQUIREMENT:{ // 修改需求
				str_key = "succeed"; // 是否成功
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					dataBox.mIsSuccess = jsonObject.getBoolean(str_key);
				}
				str_key = "info"; // 成功，显示在需求返回页面上方，如果失败，直接弹出提示框保持在需求创建页面
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					dataBox.mMessage = jsonObject.getString(str_key);
				}
				break;
			}
			case EAPIConsts.ReqType.CLOSE_REQUIREMENT:{ // 关闭需求
				str_key = "succeed"; // 是否成功
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					dataBox.mIsSuccess = jsonObject.getBoolean(str_key);
				}
				break;
			}
			case EAPIConsts.ReqType.EDIT_BUSINESS_REQUIREMENT: // 修改业务需求
				str_key = "succeed"; // 是否成功
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					dataBox.mIsSuccess = jsonObject.getBoolean(str_key);
				}
				str_key = "info"; // 成功，显示在需求返回页面上方，如果失败，直接弹出提示框保持在需求创建页面
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					dataBox.mMessage = jsonObject.getString(str_key);
				}
				break;
			case EAPIConsts.ReqType.CLOSE_BUSINESS_REQUIREMENT:{ // 关闭业务需求
				str_key = "succeed"; // 是否成功
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					dataBox.mIsSuccess = jsonObject.getBoolean(str_key);
				}
				break;
			}
			case EAPIConsts.ReqType.EDIT_TASK:{ // 修改任务
				str_key = "succeed"; // 是否成功
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					dataBox.mIsSuccess = jsonObject.getBoolean(str_key);
				}
				str_key = "info"; // 成功，显示在需求返回页面上方，如果失败，直接弹出提示框保持在需求创建页面
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					dataBox.mMessage = jsonObject.getString(str_key);
				}
				break;
			}
			case EAPIConsts.ReqType.EDIT_PROJECT: // 修改项目
				str_key = "succeed"; // 是否成功
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					dataBox.mIsSuccess = jsonObject.getBoolean(str_key);
				}
				str_key = "info"; // 成功，显示在需求返回页面上方，如果失败，直接弹出提示框保持在需求创建页面
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					dataBox.mMessage = jsonObject.getString(str_key);
				}
				break;
			case EAPIConsts.ReqType.ADD_FRIEND:{ // 添加好友
				str_key = "succeed"; // 是否成功
				if (jsonObject.has(str_key) ) {
					dataBox.mIsSuccess = jsonObject.optBoolean(str_key);
				}
				break;
			}
			case EAPIConsts.ReqType.ADD_COMMENT:{ // 发表评论
				str_key = "succeed";
				if(jsonObject.has(str_key)){
					dataBox.mIsSuccess = jsonObject.optBoolean(str_key);
				}
				str_key = "comment";
				if(jsonObject.has(str_key) && !jsonObject.isNull(str_key)){
					Comment comment = new Comment();
					comment.initWithJson(jsonObject.getJSONObject(str_key));
					dataBox.mComment = comment;
				}
				break;
			}
			case EAPIConsts.ReqType.GET_LIST_COMMENT:{ // 获取评论列表
				str_key = "page";
				if(jsonObject.has(str_key) && !jsonObject.isNull(str_key)){
					JSONObject jSubObject = jsonObject.getJSONObject(str_key);
					str_key = "total";
					if(jSubObject.has(str_key) && !jsonObject.isNull(str_key)){
						dataBox.mTotal =  jSubObject.getInt(str_key);
					}
					str_key = "index";
					if(jSubObject.has(str_key) && !jsonObject.isNull(str_key)){
						dataBox.mIndex = jSubObject.optInt(str_key);
					}
					str_key = "size";
					if(jSubObject.has(str_key) && !jsonObject.isNull(str_key)){
						dataBox.mSize = jSubObject.optInt(str_key);
					}
					str_key = "listComment";
					if(jSubObject.has(str_key) && !jSubObject.isNull(str_key)){
						JSONArray jArray = jSubObject.getJSONArray(str_key);
						if (jArray != null && jArray.length() > 0) {
							dataBox.mListComment = new ArrayList<Comment>();
							for (int i = 0; i < jArray.length(); i++) {
								Comment comment = new Comment();
								comment.initWithJson(jArray.getJSONObject(i));
								dataBox.mListComment.add(comment);
							}
						}
					}
				}
				break;
			}
			case EAPIConsts.ReqType.GET_DYNAMIC_LIST_COMMENT:{ // 获取动态评论列表
				str_key = "page";
				if(jsonObject.has(str_key) && !jsonObject.isNull(str_key)){
					JSONObject jSubObject = jsonObject.getJSONObject(str_key);
					str_key = "total";
					if(jSubObject.has(str_key) && !jsonObject.isNull(str_key)){
						dataBox.mTotal =  jSubObject.getInt(str_key);
					}
					str_key = "index";
					if(jSubObject.has(str_key) && !jsonObject.isNull(str_key)){
						dataBox.mIndex = jSubObject.optInt(str_key);
					}
					str_key = "size";
					if(jSubObject.has(str_key) && !jsonObject.isNull(str_key)){
						dataBox.mSize = jSubObject.optInt(str_key);
					}
					str_key = "listDynamicComment";
					if(jSubObject.has(str_key) && !jSubObject.isNull(str_key)){
						JSONArray jArray = jSubObject.getJSONArray(str_key);
						if (jArray != null && jArray.length() > 0) {
//							dataBox.mDynamicCommentList =JSON.parseArray(jSubObject.getJSONArray(str_key).toString(),DynamicComment.class);
							String jsonString = jSubObject.getJSONArray(str_key).toString();
							dataBox.mDynamicCommentList  = gson.fromJson(jsonString, new TypeToken<List<DynamicComment>>(){}.getType());
						}else{
							dataBox.mDynamicCommentList = new ArrayList<DynamicComment>();
						}
					}
				}
				break;
			}
			case EAPIConsts.ReqType.CHANGE_USER_PWD:
				str_key = "succeed";
				if(jsonObject.has(str_key) && !jsonObject.isNull(str_key)){
					dataBox.isChangeSucceed = jsonObject.getBoolean("succeed");
				}
				break;
			case EAPIConsts.ReqType.GET_KNOWLEDGE_DETAIL: // 获取知识详情
			case EAPIConsts.ReqType.GET_KNOWLEDGE_DETAIL_BY_ID:{ // 获取知识详情
				str_key = "knowledge";
				if(jsonObject.has(str_key) && !jsonObject.isNull(str_key)){
					Knowledge know = new Knowledge();
					know.initWithJson(jsonObject.getJSONObject(str_key));
					dataBox.mKnowledge = know;
				}
				break;
			}
			case EAPIConsts.ReqType.GET_MATCH_REQUIREMENT_MINI:{ // 获取匹配需求
				str_key = "listRequirementMini";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					JSONArray jArray = jsonObject.getJSONArray(str_key);
					if (jArray != null && jArray.length() > 0) {
						dataBox.mListRequirementMini = new ArrayList<RequirementMini>();
						for (int i = 0; i < jArray.length(); i++) {
							RequirementMini reqMini = new RequirementMini();
							reqMini.initWithJson(jArray.getJSONObject(i));
							dataBox.mListRequirementMini.add(reqMini);
						}
					}
				}
				break;
			}
			case EAPIConsts.ReqType.GET_MATCH_KNOWLEDGE_MINI:{ // 获取匹配知识
				str_key = "listKnowledgeMini";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					JSONArray jArray = jsonObject.getJSONArray(str_key);
					if (jArray != null && jArray.length() > 0) {
						dataBox.mListKnowledgeMini = new ArrayList<KnowledgeMini>();
						for (int i = 0; i < jArray.length(); i++) {
							KnowledgeMini knoMini = new KnowledgeMini();
							knoMini.initWithJson(jArray.getJSONObject(i));
							dataBox.mListKnowledgeMini.add(knoMini);
						}
					}
				}
				break;
			}
			case EAPIConsts.ReqType.GET_LIST_REQUIREMENT:{ // 需求列表
				str_key = "page";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					JSONObject subJObject = jsonObject.getJSONObject(str_key);
					str_key = "total";
					if (subJObject.has(str_key) && !subJObject.isNull(str_key)) {
						dataBox.mTotal = subJObject.getInt(str_key);
					}
					str_key = "index";
					if (subJObject.has(str_key) && !subJObject.isNull(str_key)) {
						dataBox.mIndex = subJObject.getInt(str_key);
					}
					str_key = "size";
					if (subJObject.has(str_key) && !subJObject.isNull(str_key)) {
						dataBox.mSize = subJObject.getInt(str_key);
					}
					str_key = "listRequirementMini";
					if (subJObject.has(str_key) && !subJObject.isNull(str_key)) {
						JSONArray jArray = subJObject.getJSONArray(str_key);
						if (jArray != null && jArray.length() > 0) {
							dataBox.mListRequirementMini = new ArrayList<RequirementMini>();
							for (int i = 0; i < jArray.length(); i++) {
								RequirementMini reqMini = new RequirementMini();
								reqMini.initWithJson(jArray.getJSONObject(i));
								dataBox.mListRequirementMini.add(reqMini);
							}
						}
					}
				}
				break;
			}
			case EAPIConsts.ReqType.GET_LIST_AFFAIR:{ // 事务列表
				str_key = "page";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					JSONObject subJObject = jsonObject.getJSONObject(str_key);
					str_key = "total";
					if (subJObject.has(str_key) && !subJObject.isNull(str_key)) {
						dataBox.mTotal = subJObject.getInt(str_key);
					}
					str_key = "index";
					if (subJObject.has(str_key) && !subJObject.isNull(str_key)) {
						dataBox.mIndex = subJObject.getInt(str_key);
					}
					str_key = "size";
					if (subJObject.has(str_key) && !subJObject.isNull(str_key)) {
						dataBox.mSize = subJObject.getInt(str_key);
					}
					str_key = "listAffairMini";
					if (subJObject.has(str_key) && !subJObject.isNull(str_key)) {
						JSONArray jArray = subJObject.getJSONArray(str_key);
						if (jArray != null && jArray.length() > 0) {
							dataBox.mListAffairMini = new ArrayList<AffairsMini>();
							for (int i = 0; i < jArray.length(); i++) {
								AffairsMini affMini = new AffairsMini();
								affMini.initWithJson(jArray.getJSONObject(i));
								dataBox.mListAffairMini.add(affMini);
							}
						}
					}
				}
				break;
			}
			case EAPIConsts.ReqType.OPERATE_PROJECT:{ // 操作项目
				str_key = "succeed";
				if(jsonObject.has(str_key) && !jsonObject.isNull(str_key)){
					dataBox.mIsSuccess = jsonObject.optBoolean(str_key);
				}
				break;
			}
			case EAPIConsts.ReqType.MSG_LOGGER: { // 日志数据返回，直接丢弃

				break;
			}
			/*
			case EAPIConsts.ReqType.ADD_MY_KNOWLEDGE:{ // 添加我的知识
				str_key = "succeed";
				if(jsonObject.has(str_key) && !jsonObject.isNull(str_key)){
					dataBox.mIsSuccess = jsonObject.optBoolean(str_key);
				}
				break;
			}
			*/
			case EAPIConsts.ReqType.GET_TREATED_HTML:{ //  获取处理后的网页和标题
				
				dataBox.mTreatedHtml = new TreatedHtml();
				str_key = "change_url";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					dataBox.mTreatedHtml.setChangeUrl(jsonObject.getString(str_key));
				}
				str_key = "url";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					dataBox.mTreatedHtml.setUrl(jsonObject.getString(str_key));
				}
				str_key = "title";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					dataBox.mTreatedHtml.setTitle(jsonObject.getString(str_key));
				}
				
				break;
			}
			case EAPIConsts.ReqType.DELETE_FILE:{ // 删除附件
				str_key = "succeed";
				if(jsonObject.has(str_key) && !jsonObject.isNull(str_key)){
					dataBox.mIsSuccess = jsonObject.getBoolean(str_key);
				}
				break;
			}
			
			case EAPIConsts.ReqType.UpdateUserConfig: {
				str_key = "succeed";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					dataBox.mIsSuccess = jsonObject.getBoolean(str_key);
				}
				str_key = "url";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					dataBox.userQRUrl = jsonObject.getString(str_key);
				}
				break;
			}

			case EAPIConsts.ReqType.UPDATE_USER_CONFIG: {
				str_key = "succeed";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					dataBox.mUpdateSuccess = jsonObject.getBoolean(str_key);
				}
				break;
			}
			// 绑定QQ/Sina微博
			case EAPIConsts.ReqType.SET_BINDING_QQ_WB: 
				str_key = "binding_status";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					dataBox.mBindingStatus = jsonObject.getBoolean(str_key);
				}
				break;
			// 解绑QQ/Sina微博
			case EAPIConsts.ReqType.SET_UNBINDING_QQ_WB: 
				str_key = "succeed";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					dataBox.mIsSuccess = jsonObject.getBoolean(str_key);
				}
				break;
			// 判断手机或邮箱是否可绑
			case EAPIConsts.ReqType.SET_MOBILE_EMAIL_WHETHER_CAN_BINDING: 
				str_key = "succeed";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					dataBox.mIsSuccess = jsonObject.getBoolean(str_key);
				}
				str_key = "msg";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					dataBox.mMessage = jsonObject.getString(str_key);
				}
				break;
				// 判断组织全称和组织邮箱是否被注册
			case EAPIConsts.ReqType.JUDGE_USERANDMAIL: 
				str_key = "success";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					dataBox.mIsSuccess = jsonObject.getBoolean(str_key);
				}
				str_key = "notifInfo";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					dataBox.mMessage = jsonObject.getString(str_key);
				}
				break;
			// 验证新/老邮箱是否验证成功
			case EAPIConsts.ReqType.SET_CHECK_EMAIL_STATUS: 
//				str_key = "succeed";
//				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
//					dataBox.mIsSuccess = jsonObject.getBoolean(str_key);
//				}
				str_key = "msg";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					dataBox.mMessage = jsonObject.getString(str_key);
				}
				break;
			// 验证新/老手机是否验证成功
			case EAPIConsts.ReqType.SET_CHECK_MOBILE_STATUS: 
				str_key = "succeed";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					dataBox.mIsSuccess = jsonObject.getBoolean(str_key);
				}
				// str_key = "msg";
				// if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
				// dataBox.mMessage = jsonObject.getString(str_key);
				// }
				break;
				// 刷新账号信息
			case EAPIConsts.ReqType.SET_REFRESH_ACCOUNT_INFO:
				str_key = "succeed";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					dataBox.mIsSuccess = jsonObject.getBoolean(str_key);
				}
				str_key = "jtMember";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					JTMember member = new JTMember();
					String jsonStr = jsonObject.getJSONObject(str_key).toString();
					member.initWithJson(jsonObject.getJSONObject(str_key));
					dataBox.mJTMember = member;
				}
				break;
				// 第三方登录
			case EAPIConsts.ReqType.THIRD_LOGIN: 
				// 会话ID
				str_key = "sessionID";
				if(jsonObject.has(str_key) && !jsonObject.isNull(str_key)){ 
					dataBox.mSessionID = jsonObject.getString(str_key);
				}
				// 会员对象
				str_key = "jtMember";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					JTMember member = new JTMember();
					member.initWithJson(jsonObject.getJSONObject(str_key));
					dataBox.mJTMember = member;
				}
				
				// 常量
				str_key = "const";
				if(jsonObject.has(str_key) && ! jsonObject.isNull(str_key)){
					JSONObject subJsonObject = jsonObject.getJSONObject(str_key);
					// 货币类型
					str_key = "listMoneyType";
					if(subJsonObject.has(str_key) && !subJsonObject.isNull(str_key)){
						JSONArray jArray = subJsonObject.getJSONArray(str_key);
						if (jArray != null && jArray.length()>0) {
							dataBox.mListMoneyType = new ArrayList<MoneyType>();
							for (int i = 0; i < jArray.length(); i++) {
								MoneyType m_type = new MoneyType();
								m_type.initWithJson(jArray.getJSONObject(i));
								dataBox.mListMoneyType.add(m_type);
							}
						}
					}
					// 投资金额范围
					str_key = "listMoneyRange";
					if (subJsonObject.has(str_key)
							&& !subJsonObject.isNull(str_key)) {
						JSONArray jArray = subJsonObject.getJSONArray(str_key);
						if (jArray != null && jArray.length() > 0) {
							dataBox.mListMoneyRange = new ArrayList<String>();
							for (int i = 0; i < jArray.length(); i++) {
								dataBox.mListMoneyRange.add(jArray.getString(i));
							}
						}
					}
					// 投资类型
					str_key = "listInInvestType";
					if(subJsonObject.has(str_key) && !subJsonObject.isNull(str_key)){
						JSONArray jArray = subJsonObject.getJSONArray(str_key);
						if (jArray != null && jArray.length() >0) {
							dataBox.mListInInvestType = new ArrayList<InvestType>();;
							for (int i = 0; i < jArray.length(); i++) {
								InvestType type = new InvestType();
								type.initWithJson(jArray.getJSONObject(i));
								dataBox.mListInInvestType.add(type);
							}
						}
					}
					// 融资类型
					str_key = "listOutInvestType";
					if(subJsonObject.has(str_key) && !subJsonObject.isNull(str_key)){
						JSONArray jArray = subJsonObject.getJSONArray(str_key);
						if (jArray != null && jArray.length() >0) {
							dataBox.mListOutInvestType = new ArrayList<InvestType>();;
							for (int i = 0; i < jArray.length(); i++) {
								InvestType type = new InvestType();
								type.initWithJson(jArray.getJSONObject(i));
								dataBox.mListOutInvestType.add(type);
							}
						}
					}
					// 投资区域
					str_key = "listArea";
					if(subJsonObject.has(str_key) && !subJsonObject.isNull(str_key)){
						JSONArray jArray = subJsonObject.getJSONArray(str_key);
						if (jArray != null && jArray.length()>0) {
							dataBox.mListArea = new ArrayList<Area>();
							for (int i = 0; i < jArray.length(); i++) {
								Area area = new Area();
								area.initWithJson(jArray.getJSONObject(i));
								dataBox.mListArea.add(area);
							}
						}
					}
					// 投资行业
					str_key = "listTrade";
					if(subJsonObject.has(str_key) && !subJsonObject.isNull(str_key)){
						JSONArray jArray = subJsonObject.getJSONArray(str_key);
						if (jArray != null && jArray.length()>0) {
							dataBox.mListTrade = new ArrayList<Trade>();
							for (int i = 0; i < jArray.length(); i++) {
								Trade trade = new Trade();
								trade.initWithJson(jArray.getJSONObject(i));
								dataBox.mListTrade.add(trade);
							}
						}
					}
				}
				// "邀请用户加入金桐网的邀请语"
				str_key = "inviteJoinGinTongInfo";
				
				if(jsonObject.has(str_key)){
					dataBox.mInviteJoinGinTongInfo = jsonObject.optString(str_key);
				}
				str_key = "binding_status";
				
				if(jsonObject.has(str_key)){
					dataBox.mBindingStatus = jsonObject.getBoolean(str_key);
				}
				break;
		
		}
		return dataBox;
		} 
		catch (JSONException e) {
			e.printStackTrace();
		} 
		catch (MalformedURLException e) {
			e.printStackTrace();
		} 
		catch (ParseException e) {
			e.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
