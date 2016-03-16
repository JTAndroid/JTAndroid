package com.tr.model.obj;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.helper.StringUtil;

import com.tr.App;
import com.tr.model.demand.ASSORPOK;
import com.tr.model.page.IPageBaseItem;
import com.tr.model.user.JTMember;
import com.tr.ui.flow.model.Topic;
import com.utils.common.EUtil;

public class DynamicNews implements IPageBaseItem {
	private static final long serialVersionUID = 1L;

	/** 创建了知识 */
	public final static int TYPE_CREATE_KNOWLEDGE = 10;
	/** 转发了知识 */
	public final static int TYPE_FORWARDING_KNOWLEDGE = 11;
	/** 金桐脑推荐了知识 */
	public final static int TYPE_RECOMMEND_KNOWLEDGE = 12;
	/** 创建了需求 */
	public final static int TYPE_CREATE_REQUIREMENT = 20;
	/** 转发了需求 */
	public final static int TYPE_FORWARDING_REQUIREMENT = 21;
	/** 金桐脑推荐了需求 */
	public final static int TYPE_RECOMMEND_REQUIREMENT = 22;
	/** 创建了人脉 */
	public final static int TYPE_CREATE_CONTACTS = 30;
	/** 转发了人脉 */
	public final static int TYPE_FORWARDING_CONTACTS = 31;
	/** 金桐脑推荐了人脉 */
	public final static int TYPE_RECOMMEND_CONTACTS = 32;
	/** 创建了会议 */
	public final static int TYPE_CREATE_MEETING = 40;
	/** 转发了会议 */
	public final static int TYPE_FORWARDING_MEETING = 41;
	/** 转发了用户 */
	public final static int TYPE_USER_CARD = 50;
	/** 金桐脑推荐了用户 */
	public final static int TYPE_RECOMMEND_CUSTOMER = 51;
	// public final static int TYPE_RECOMMEND_CUSTOMER = 54;
	/** 转发了组织 */
	public final static int TYPE_FORWARDING_ORGANIZATION = 61;
	/** 创建了客户 */
	public final static int TYPE_CREATE_CUSTOM = 62;
	/** 转发了客户 */
	public final static int TYPE_FORWARDING_CUSTOM = 63;
	/** 金桐脑推荐了组织 */
	public final static int TYPE_RECOMMEND_ORGANIZATION = 64;
	/** 金桐脑推荐了客户 */
	public final static int TYPE_RECOMMEND_CUSTOM = 42;
	/** 普通说说 */
	public final static int TYPE_DYNAMIC = 99;
	// /**组织名片*/
	// public final static int TYPE_ORGANIZATION_CARD = 51;
	// /**创建组织*/
	// public final static int TYPE_CREATE_ORGANIZATION = 60;
	// /**人脉名片*/
	// public final static int TYPE_CONTACTS_CARD = 52;
	// /**客户名片*/
	// public final static int TYPE_CUSTOMER_CARD = 53;
	// /**大数据推送*/
	// public final static int TYPE_BIG_DATA_PUSH = 00;
	// /**创建事务*/
	// public final static int TYPE_CREAT_AFFAIRS = 65;
	// /**转发事务*/
	// public final static int TYPE_FORWARDING_AFFAIRS = 66;
	// /**金桐脑推荐事务*/
	// public final static int TYPE_RECOMMEND_AFFAIRS = 67;
	/** 动态id */
	private long id;
	/** 动态标题 */
	private String title;
	/**当前动态的类型*/
	private int type;
	/* 动态内容 */
	private String content;
	/* 清除格式后的动态内容 */
	private String clearContent;
	/* 子评论数 */
	private long count;
	/* 创建者ID */
	private long createrId;
	/* 创建人名字 */
	private String createrName;
	/* 创建时间，格式如2015-01-14 15:51:38 */
	private String ctime;
	/* "删除状态 0 正常状态，-1：逻辑删除" */
	private int delstatus;
	/* 源图片地址 */
	private String imgPath;
	/* 是否可见，0可见，-1不可见 */
	private int isVisable;
	/* 子评论id列表 */
	private String leastComments;
	/* 动态回复列表 */
	private ArrayList<DynamicComment> comments = new ArrayList<DynamicComment>();
	/* 创建者头像地址 */
	private String picPath;
	/* "子类型，如：不同知识类型，转发会议0表示邀请函，1表示会议" */
	private int lowType;
	/* 源id，如知识id */
	private long targetId;
	/* 转发内容 */
	private String forwardingContent;
	/* "ptype" : "权限类型2大乐，3中乐，4小乐，5独乐" */
	private int ptype;
	/** 0 未知，1 男，2 女*/
	private int gender;
	/**创建者类型） 0 代表金桐脑 ，1 代表个人用户，2 代表组织*/
	public int createType;

	private String inputText;// 评论回复框输入的文本

	private DynamicComment mDynamicComment;

	private ArrayList<DynamicPraise> dynamicPraises = new ArrayList<DynamicPraise>();
	
	//新增属性
	public ArrayList<DynamicPicturePath> picturePaths = new ArrayList<DynamicPicturePath>();//动态图片
	private String contentPath;//内容的连接地址，外来转发和分享
	private DynamicLocation location;
	private ArrayList<DynamicTargetRelation> targetRelations = new ArrayList<DynamicTargetRelation>();
	private ArrayList<DynamicApprove> approves = new ArrayList<DynamicApprove>();
	public ArrayList<DynamicPeopleRelation> peopleRelation = new ArrayList<DynamicPeopleRelation>(); //提及相关人 @功能
	private int peopleCount;//关联人脉数量，
	private int orgCount;//关联组织数量,
	private int knowledgeCount;//关联知识数量，
	private int demandCount;//关联需求数量
	public ArrayList<DynamicIndustry> industrys = new ArrayList<DynamicIndustry>();//行业
	//  //关联
	  public ASSORPOK asso ; 
	 
	  public int scope;
	  public ArrayList<Topic> topic =new ArrayList<Topic>(); //[name:话题名称，createrId:话题创建者，type:话题类型 如音乐]
	
	private boolean showAllcomment = false;
	private boolean showAllcontent = false;
	
	public boolean isShowAllcomment() {
		return showAllcomment;
	}

	public void setShowAllcomment(boolean showAllcomment) {
		this.showAllcomment = showAllcomment;
	}

	public boolean isShowAllcontent() {
		return showAllcontent;
	}

	public void setShowAllcontent(boolean showAllcontent) {
		this.showAllcontent = showAllcontent;
	}

	public ArrayList<DynamicPraise> getmDynamicPraiseList() {
		return dynamicPraises;
	}

	public void setmDynamicPraiseList(ArrayList<DynamicPraise> mDynamicPraiseList) {
		this.dynamicPraises = mDynamicPraiseList;
	}

	public String getInputText() {
		if (inputText == null)
			return "";
		else
			return inputText;
	}
	

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}
	/**创建者类型） 0 代表金桐脑 ，1 代表个人用户，2 代表组织*/
	public int getCreateType() {
		return createType;
	}

	public void setCreateType(int createType) {
		this.createType = createType;
	}

	public static DynamicNews createFactory(JSONObject jsonObject) {
		try {
			DynamicNews self = new DynamicNews();
			self.id = Long.parseLong(jsonObject.getString("id"));
			self.title = jsonObject.optString("title");
			self.type = jsonObject.optInt("type");
			self.content = jsonObject.optString("content");
			self.clearContent = jsonObject.optString("clearContent");
			self.count = jsonObject.optLong("count");
			self.createrId = jsonObject.optLong("createrId");
			self.createrName = jsonObject.optString("createrName");
			self.ctime = jsonObject.optString("ctime");
			self.delstatus = jsonObject.optInt("delstatus");
			self.imgPath = jsonObject.optString("imgPath");
			self.isVisable = jsonObject.optInt("isVisable");
			self.leastComments = jsonObject.optString("leastComments");
			self.picPath = jsonObject.optString("picPath");
			self.lowType = jsonObject.optInt("lowType");
			self.targetId = jsonObject.optLong("targetId");
			self.forwardingContent = jsonObject.optString("forwardingContent");
			self.ptype = jsonObject.optInt("ptype");
			self.gender = jsonObject.optInt("gender");
			self.createType = jsonObject.optInt("createType");
			self.contentPath = jsonObject.optString("contentPath");
			self.peopleCount = jsonObject.optInt("peopleCount");
			self.orgCount = jsonObject.optInt("orgCount");
			self.knowledgeCount = jsonObject.optInt("knowledgeCount");
			self.demandCount = jsonObject.optInt("demandCount");

			JSONArray array = null;
			if (!jsonObject.isNull("comments")){
				array = jsonObject.getJSONArray("comments");
				if (array != null) {
					for (int i = 0; i < array.length(); i++) {
						JSONObject objComment = array.getJSONObject(i);
						DynamicComment cmt = DynamicComment.createFactory(objComment);
						if (cmt != null)
							self.comments.add(cmt);
					}
				}
			}
			JSONArray array2 = null;
			// if (jsonObject.has("dynamicPraises")) {
			if (!jsonObject.isNull("dynamicPraises")) {
				array2 = jsonObject.getJSONArray("dynamicPraises");
			}
			if (array2 != null) {
				for (int i = 0; i < array2.length(); i++) {
					JSONObject objPraise = array2.getJSONObject(i);
					DynamicPraise dp = DynamicPraise.createFactory(objPraise);
					if (dp != null) {
						self.dynamicPraises.add(dp);
					}
				}
			}
			JSONArray array3 = null;
			if (!jsonObject.isNull("picturePaths")) {
				array3 = jsonObject.getJSONArray("picturePaths");
			}
			if(array3 != null){
				for (int i = 0; i < array3.length(); i++) {
					JSONObject objPraise = array3.getJSONObject(i);
					DynamicPicturePath picPath = DynamicPicturePath.createFactory(objPraise);
					if (picPath != null) {
						self.picturePaths.add(picPath);
					}
				}
			}
			if(!jsonObject.isNull("location")){
				JSONObject jsonlocation = jsonObject.getJSONObject("location");
				if (jsonlocation!=null) {
					self.location = DynamicLocation.createFactory(jsonlocation);
				}
			}
			JSONArray array5 = null;
			if (!jsonObject.isNull("targetRelations")) {
				array5 = jsonObject.getJSONArray("targetRelations");
			}
			if(array5 != null){
				for(int i=0;i<array5.length();i++){
					JSONObject objPraise = array5.getJSONObject(i);
					DynamicTargetRelation dr = DynamicTargetRelation.createFactory(objPraise);
					if(dr != null){
						self.targetRelations.add(dr);
					}
				}
			}
			JSONArray array6 = null;
			if (!jsonObject.isNull("approves")) {
				array6 = jsonObject.getJSONArray("approves");
			}
			if(array6 != null){
				for(int i=0;i<array6.length();i++){
					JSONObject objPraise = array6.getJSONObject(i);
					DynamicApprove da = DynamicApprove.createFactory(objPraise);
					if(da != null){
						self.approves.add(da);
					}
				}
			}
			JSONArray array7 = null;
			if (!jsonObject.isNull("peopleRelation")) {
				array7 = jsonObject.getJSONArray("peopleRelation");
			}
			if(array7 != null){
				for(int i=0;i<array7.length();i++){
					JSONObject objPraise = array7.getJSONObject(i);
					DynamicPeopleRelation dp = DynamicPeopleRelation.createFactory(objPraise);
					if(dp != null){
						self.peopleRelation.add(dp);
					}
				}
			}
			JSONArray array8 = null;
			if (!jsonObject.isNull("industrys")) {
				array8 = jsonObject.getJSONArray("industrys");
			}
			if(array8 != null){
				for(int i=0;i<array8.length();i++){
					JSONObject objPraise = array8.getJSONObject(i);
					DynamicIndustry di = DynamicIndustry.createFactory(objPraise);
					if(di != null){
						self.industrys.add(di);
					}
				}
			}
			
			return self;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void insertComment(String commentContent) {
		if (mDynamicComment != null) {
			mDynamicComment = null;
		}
		mDynamicComment = new DynamicComment();
		mDynamicComment.setUserName(App.getNick());
		mDynamicComment.setComment(commentContent);
		mDynamicComment.setUserId(Long.valueOf(App.getUserID()));
		comments.add(0, mDynamicComment);
		/*
		 * if(comments.size() > 2){ comments.remove(comments.size()-1); }
		 */
	}

	public void setInputText(String inputText) {
		this.inputText = inputText;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
		if (!EUtil.isEmpty(content)) {
			content = EUtil.filterHtml(content);
		}
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getClearContent() {
		return clearContent;
	}

	public void setClearContent(String clearContent) {
		this.clearContent = clearContent;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getCreaterId() {
		return createrId;
	}

	public void setCreaterId(long createrId) {
		this.createrId = createrId;
	}

	public String getCreaterName() {
		if (createrName==null) {
			createrName = "";
		}
		return createrName;
	}

	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public int getDelstatus() {
		return delstatus;
	}

	public void setDelstatus(int delstatus) {
		this.delstatus = delstatus;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public int getIsVisable() {
		return isVisable;
	}

	public void setIsVisable(int isVisable) {
		this.isVisable = isVisable;
	}

	public String getLeastComments() {
		return leastComments;
	}

	public void setLeastComments(String leastComments) {
		this.leastComments = leastComments;
	}

	public ArrayList<DynamicComment> getComments() {
		return comments;
	}

	public void setComments(ArrayList<DynamicComment> comments) {
		this.comments = comments;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public int getLowType() {
		return lowType;
	}

	public void setLowType(int lowType) {
		this.lowType = lowType;
	}

	public long getTargetId() {
		return targetId;
	}

	public void setTargetId(long targetId) {
		this.targetId = targetId;
	}

	public String getForwardingContent() {
		return forwardingContent;
	}

	public void setForwardingContent(String forwardingContent) {
		this.forwardingContent = forwardingContent;
	}

	public int getPtype() {
		return ptype;
	}

	public void setPtype(int ptype) {
		this.ptype = ptype;
	}

	public void insertCommentId(Long commentid) {
		if (mDynamicComment != null) {
			mDynamicComment.setId(commentid);
		}
	}

	public DynamicComment getmDynamicComment() {
		return mDynamicComment;
	}

	public void setmDynamicComment(DynamicComment mDynamicComment) {
		this.mDynamicComment = mDynamicComment;
	}

	public ArrayList<DynamicPraise> getDynamicPraises() {
		return dynamicPraises;
	}

	public void setDynamicPraises(ArrayList<DynamicPraise> dynamicPraises) {
		this.dynamicPraises = dynamicPraises;
	}

	public String getContentPath() {
		return contentPath;
	}

	public void setContentPath(String contentPath) {
		this.contentPath = contentPath;
	}

	public DynamicLocation getLocation() {
		return location;
	}

	public void setLocation(DynamicLocation location) {
		this.location = location;
	}

	public ArrayList<DynamicTargetRelation> getTargetRelations() {
		return targetRelations;
	}

	public void setTargetRelations(ArrayList<DynamicTargetRelation> targetRelations) {
		this.targetRelations = targetRelations;
	}

	public ArrayList<DynamicApprove> getApproves() {
		return approves;
	}

	public void setApproves(ArrayList<DynamicApprove> approves) {
		this.approves = approves;
	}

	public ArrayList<DynamicPeopleRelation> getPeopleRelation() {
		return peopleRelation;
	}

	public void setPeopleRelations(ArrayList<DynamicPeopleRelation> peopleRelation) {
		this.peopleRelation = peopleRelation;
	}

	public int getPeopleCount() {
		return peopleCount;
	}

	public void setPeopleCount(int peopleCount) {
		this.peopleCount = peopleCount;
	}

	public int getOrgCount() {
		return orgCount;
	}

	public void setOrgCount(int orgCount) {
		this.orgCount = orgCount;
	}

	public int getDemandCount() {
		return demandCount;
	}

	public void setDemandCount(int demandCount) {
		this.demandCount = demandCount;
	}

	public ArrayList<DynamicPicturePath> getPicturePaths() {
		return picturePaths;
	}

	public void setPicturePaths(ArrayList<DynamicPicturePath> picturePaths) {
		this.picturePaths = picturePaths;
	}

	public int getKnowledgeCount() {
		return knowledgeCount;
	}

	public void setKnowledgeCount(int knowledgeCount) {
		this.knowledgeCount = knowledgeCount;
	}

	@Override
	public String toString() {
		return "DynamicNews [id=" + id + ", title=" + title + ", type=" + type + ", content=" + content + ", clearContent=" + clearContent + ", count=" + count + ", createrId=" + createrId
				+ ", createrName=" + createrName + ", ctime=" + ctime + ", delstatus=" + delstatus + ", imgPath=" + imgPath + ", isVisable=" + isVisable + ", leastComments=" + leastComments
				+ ", comments=" + comments + ", picPath=" + picPath + ", lowType=" + lowType + ", targetId=" + targetId + ", forwardingContent=" + forwardingContent + ", ptype=" + ptype
				+ ", inputText=" + inputText + ", mDynamicComment=" + mDynamicComment + ", dynamicPraises=" + dynamicPraises + "]";
	}

}