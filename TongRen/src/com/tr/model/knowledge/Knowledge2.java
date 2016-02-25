package com.tr.model.knowledge;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tr.model.joint.AffairNode;
import com.tr.model.joint.ConnectionNode;
import com.tr.model.joint.KnowledgeNode;
import com.tr.model.obj.AffairsMini;
import com.tr.model.obj.Connections;
import com.tr.model.obj.JTFile;
import com.tr.model.obj.ResourceBase;

/**
 * 知识javaBean （资讯，资产管理,宏观，观点，文章）
 * @author Administrator
 */
public class Knowledge2  extends ResourceBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// id
	private long id;
	
	// 类型:1-资讯，2-投融工具，3-行业，4-经典案例，5-图书报告，6-资产管理，7-宏观，8-观点，9-判例，10-法律法规，11-文章
	private int type;

	// 标题
	private String title;

	// 作者id
	private long uid;

	// 作者名称
	private String uname;

	// 创建人id
	private long cid;

	// 创建人名称
	private String cname;

	// 来源
	private String source;

	// 来源地址
	private String s_addr;

	// 栏目路徑
	private String cpathid;

	// 封面地址
	private String pic;

	// 描述
	private String desc;

	// 原内容
	private String content;
	
	// 高亮处理后的内容
	private String hcontent;

	// 是否加精
	private int essence;

	// 创建时间
	private String createtime;

	// 最后修改时间
	private String modifytime;

	// 状态（1为草稿，2：待审核 3：审核中 4：审核通过 5：未通过 6：回收站)
	private int status;

	// 举报状态(1:举报，0：未举报)
	private int report_status;

	// 标签
	private ArrayList<String> listTag;

	// 高亮状态（0-未加 1-已加）
	private int ish;
	
	//知识url
	private String knowledgeUrl;
	
	//知识统计对象
	private KnowledgeStatics knowledgeStatics;
	
	//栏目对象
	private Column column;
	
	//目录对象
//	private UserCategory userCategory;
	private ArrayList<UserCategory> listUserCategory;

	//当前用户对接权限
	private boolean isConnectionAble;
	
	//当前用户分享权限
	private boolean isShareAble;
	
	//用户、组织  1为用户 / 2为组织
	private int authorType;
	
	//是否是中乐
	private boolean isZhongLeForMe;
	
	/**
	 * 知识详情页
	 * 1为用户 / 2为组织
	 */
	public enum AuthorType{
		AUTHORTYPE_PEOPLE(1),AUTHORTYPE_ORGANIZATION(2);
		public int value;
		private AuthorType(int v){
			this.value = v;
		}
	}
	// 关联人脉对象组
//	private HashMap<String, ArrayList<Connections>> relatedConnection;
	private ArrayList<Pair<String, ArrayList<Connections>>> listRelatedPeopleGroup; // 0-金桐网人脉;1-用户人脉
	// 关联人脉元数据
	private ArrayList<ConnectionNode> listRelatedConnectionsNode;

	// 关联组织对象
//	private HashMap<String, ArrayList<Connections>> relatedOrganization;
	private ArrayList<Pair<String, ArrayList<Connections>>> listRelatedOrganizationGroup;
	// 关联组织元数据 
	private ArrayList<ConnectionNode> listRelatedOrganizationNode;
	
	// 关联知识
//	private HashMap<String, ArrayList<KnowledgeMini2>> relatedKnowledge;
	private ArrayList<Pair<String, ArrayList<KnowledgeMini2>>> listRelatedKnowledgeGroup;
	// 关联知识元数据
	private ArrayList<KnowledgeNode> listRelatedKnowledgeNode;
	
	// 关联事件
//	private HashMap<String, ArrayList<RequirementMini>> relatedRequirement;
	private ArrayList<Pair<String, ArrayList<AffairsMini>>> listRelatedAffairGroup;
	// 关联事件元数据
	private ArrayList<AffairNode> listRelatedAffairNode;
	
	//用户权限
	
	//大乐权限人脉对象列表
	private ArrayList<Connections> listHightPermission;
	
	//中乐权限人脉对象列表
	private ArrayList<Connections> listMiddlePermission;
	
	//小乐权限人脉对象列表
	private ArrayList<Connections> listLowPermission;
	
	// 附件索引
	private String taskid = "";
	
	// 所有附件对象
	private ArrayList<JTFile> listJtFile;
	
	// 知识图片地址（大数据解析网址时使用
	private ArrayList<String> listImageUrl;
	
	//是否已收藏
	private boolean collected;
	
	/*"isSaved":"当前用户保存权限"*/
	private boolean isSaved;
	
	public boolean isSaved() {
		return isSaved;
	}

	public void setSaved(boolean isSaved) {
		this.isSaved = isSaved;
	}

	public ArrayList<String> getListImageUrl() {
		return listImageUrl;
	}

	public void setListImageUrl(ArrayList<String> listImageUrl) {
		this.listImageUrl = listImageUrl;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getKnowledgeUrl() {
		return knowledgeUrl;
	}

	public void setKnowledgeUrl(String knowledgeUrl) {
		this.knowledgeUrl = knowledgeUrl;
	}

	public boolean isConnectionAble() {
		return isConnectionAble;
	}

	public void setConnectionAble(boolean isConnectionAble) {
		this.isConnectionAble = isConnectionAble;
	}

	public boolean isShareAble() {
		return isShareAble;
	}

	public void setShareAble(boolean isShareAble) {
		this.isShareAble = isShareAble;
	}


	public Column getColumn() {
		return column;
	}

	public void setColumn(Column column) {
		this.column = column;
	}

	public KnowledgeStatics getKnowledgeStatics() {
		return knowledgeStatics;
	}

	public void setKnowledgeStatics(KnowledgeStatics knowledgeStatics) {
		this.knowledgeStatics = knowledgeStatics;
	}

	public ArrayList<Connections> getListHighPermission() {
		return listHightPermission;
	}

	public void setListHighPermission(ArrayList<Connections> listHighPermission) {
		this.listHightPermission = listHighPermission;
	}

	public ArrayList<Connections> getListMiddlePermission() {
		return listMiddlePermission;
	}

	public void setListMiddlePermission(ArrayList<Connections> listMiddlePermission) {
		this.listMiddlePermission = listMiddlePermission;
	}

	public ArrayList<Connections> getListLowPermission() {
		return listLowPermission;
	}

	public void setListLowPermission(ArrayList<Connections> listLowPermission) {
		this.listLowPermission = listLowPermission;
	}

	public ArrayList<String> getListTag() {
		return listTag;
	}

	public void setListTag(ArrayList<String> listTag) {
		this.listTag = listTag;
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

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public long getCid() {
		return cid;
	}

	public void setCid(long cid) {
		this.cid = cid;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getS_addr() {
		return s_addr;
	}

	public void setS_addr(String s_addr) {
		this.s_addr = s_addr;
	}

	public String getCpathid() {
		return cpathid;
	}

	public void setCpathid(String cpathid) {
		this.cpathid = cpathid;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getHcontent() {
		return hcontent;
	}

	public void setHcontent(String hcontent) {
		this.hcontent = hcontent;
	}

	public int getEssence() {
		return essence;
	}

	public void setEssence(int essence) {
		this.essence = essence;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getModifytime() {
		return modifytime;
	}

	public void setModifytime(String modifytime) {
		this.modifytime = modifytime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getReport_status() {
		return report_status;
	}

	public void setReport_status(int report_status) {
		this.report_status = report_status;
	}

	public int getIsh() {
		return ish;
	}

	public void setIsh(int ish) {
		this.ish = ish;
	}
	
	public String getTaskId() {
		return taskid;
	}

	public void setTaskId(String taskid) {
		this.taskid = taskid;
	}
	
	public ArrayList<JTFile> getListJtFile() {
		return listJtFile;
	}

	public void setListJtFile(ArrayList<JTFile> listJtFile) {
		this.listJtFile = listJtFile;
	}
	
	public ArrayList<Pair<String, ArrayList<Connections>>> getListRelatedPeopleGroup() {
		return listRelatedPeopleGroup;
	}

	public void setListRelatedPeopleGroup(ArrayList<Pair<String, ArrayList<Connections>>> listRelatedPeopleGroup) {
		this.listRelatedPeopleGroup = listRelatedPeopleGroup;
	}

	public ArrayList<Pair<String, ArrayList<Connections>>> getListRelatedOrganizationGroup() {
		return listRelatedOrganizationGroup;
	}

	public void setListRelatedOrganizationGroup(
			ArrayList<Pair<String, ArrayList<Connections>>> listRelatedOrganizationGroup) {
		this.listRelatedOrganizationGroup = listRelatedOrganizationGroup;
	}

	public ArrayList<Pair<String, ArrayList<KnowledgeMini2>>> getListRelatedKnowledgeGroup() {
		return listRelatedKnowledgeGroup;
	}

	public void setListRelatedKnowledgeGroup(
			ArrayList<Pair<String, ArrayList<KnowledgeMini2>>> listRelatedKnowledgeGroup) {
		this.listRelatedKnowledgeGroup = listRelatedKnowledgeGroup;
	}

	public ArrayList<Pair<String, ArrayList<AffairsMini>>> getListRelatedAffairGroup() {
		return listRelatedAffairGroup;
	}

	public void setListRelatedAffairGroup(
			ArrayList<Pair<String, ArrayList<AffairsMini>>> listRelatedAffairGroup) {
		this.listRelatedAffairGroup = listRelatedAffairGroup;
	}
	
	public ArrayList<UserCategory> getListUserCategory() {
		return listUserCategory;
	}

	public void setListUserCategory(ArrayList<UserCategory> listUserCategory) {
		this.listUserCategory = listUserCategory;
	}

	public ArrayList<ConnectionNode> getListRelatedConnectionsNode() {
		if(listRelatedConnectionsNode == null){
			listRelatedConnectionsNode = new ArrayList<ConnectionNode>();
		}
		return listRelatedConnectionsNode;
	}

	public void setListRelatedConnectionsNode(
			ArrayList<ConnectionNode> listRelatedConnectionsNode) {
		this.listRelatedConnectionsNode = listRelatedConnectionsNode;
	}

	public ArrayList<ConnectionNode> getListRelatedOrganizationNode() {
		if(listRelatedOrganizationNode == null){
			listRelatedOrganizationNode = new ArrayList<ConnectionNode>();
		}
		return listRelatedOrganizationNode;
	}

	public void setListRelatedOrganizationNode(
			ArrayList<ConnectionNode> listRelatedOrganizationNode) {
		this.listRelatedOrganizationNode = listRelatedOrganizationNode;
	}

	public ArrayList<KnowledgeNode> getListRelatedKnowledgeNode() {
		if(listRelatedKnowledgeNode == null){
			listRelatedKnowledgeNode = new ArrayList<KnowledgeNode>();
		}
		return listRelatedKnowledgeNode;
	}

	public void setListRelatedKnowledgeNode(
			ArrayList<KnowledgeNode> listRelatedKnowledgeNode) {
		this.listRelatedKnowledgeNode = listRelatedKnowledgeNode;
	}

	public ArrayList<AffairNode> getListRelatedAffairNode() {
		if(listRelatedAffairNode == null){
			listRelatedAffairNode = new ArrayList<AffairNode>();
		}
		return listRelatedAffairNode;
	}

	public void setListRelatedAffairNode(ArrayList<AffairNode> listRelatedAffairNode) {
		this.listRelatedAffairNode = listRelatedAffairNode;
	}

	// 转为KnowledgeMini2对象
	public KnowledgeMini2 toKnowledgeMini2(){
		KnowledgeMini2 knowledgeMini2 = new KnowledgeMini2();
		knowledgeMini2.id = id;
		knowledgeMini2.type = type;
		knowledgeMini2.title = title;
		knowledgeMini2.desc = desc;
		knowledgeMini2.source = source;
		knowledgeMini2.modifytime = modifytime;
		knowledgeMini2.pic = pic;
		knowledgeMini2.listTag = listTag;
		knowledgeMini2.content = content;
		return knowledgeMini2;
	}
	
	// 转为JTFile对象
	public JTFile toJTFile() {
		JTFile jtFile = new JTFile();
		jtFile.mUrl = pic;
		jtFile.mSuffixName = desc;
		jtFile.mTaskId = id + "";
		jtFile.mType = JTFile.TYPE_KNOWLEDGE2;
		jtFile.reserved1 = type + "";
		jtFile.reserved2 = title;
		jtFile.reserved3 = knowledgeUrl;
		return jtFile;
	}
	
	/**
	 * 知识对象转成json的string对象 供http请求时用
	 * @param knowledge
	 * @return  
	 */
	public static String knowledge2JsonString(Knowledge2 knowledge) {
		String requestStr;
		GsonBuilder builder = new GsonBuilder(); 
		Gson gson = builder.serializeNulls().create();
		HashMap<String, Knowledge2> hash = new HashMap<String, Knowledge2>(); 
		hash.put("knowledge2", knowledge); 
		requestStr = gson.toJson(hash);
		
		try {
			
			JSONObject requestJsonObject = new JSONObject(requestStr);
			JSONObject knowledgeJsonObject = (JSONObject) requestJsonObject.get("knowledge2");
			
			JSONArray jsonArrayListHighPermission = new JSONArray();
			ArrayList<Connections> listHighPermission = knowledge.getListHighPermission();
			if(listHighPermission != null){
				for (int i = 0; i < listHighPermission.size(); i++) {
					jsonArrayListHighPermission.put( i , listHighPermission.get(i).toJSONObject() );
				}
			}
			knowledgeJsonObject.put("listHightPermission", jsonArrayListHighPermission);
			
			
			JSONArray jsonArrayListMiddlePermission = new JSONArray();
			ArrayList<Connections> listMiddlePermission = knowledge.getListMiddlePermission();
			if(listMiddlePermission != null){
				for (int i = 0; i < listMiddlePermission.size(); i++) {
					jsonArrayListMiddlePermission.put( i , listMiddlePermission.get(i).toJSONObject() );
				}
			}
			knowledgeJsonObject.put("listMiddlePermission", jsonArrayListMiddlePermission);
			
			JSONArray jsonArrayListLowPermission = new JSONArray();
			ArrayList<Connections> listLowPermission = knowledge.getListLowPermission();
			if(listLowPermission != null){
				for (int i = 0; i < listLowPermission.size(); i++) {
					jsonArrayListLowPermission.put( i , listLowPermission.get(i).toJSONObject() );
				}
			}
			knowledgeJsonObject.put("listLowPermission", jsonArrayListLowPermission);
			
			JSONArray jsonArrayKnow = new JSONArray();
			ArrayList<ConnectionNode> listConnectionNode = knowledge.getListRelatedConnectionsNode();
			if(listConnectionNode != null){
				for (int i = 0; i < listConnectionNode.size(); i++) {
					JSONObject jsonArrayConnNode = listConnectionNode.get(i).toJSONObject();
					jsonArrayKnow.put(i,jsonArrayConnNode);
				}
			}
			knowledgeJsonObject.put("listRelatedConnectionsNode", jsonArrayKnow);
			
			JSONArray jsonArrayOrg = new JSONArray();
			ArrayList<ConnectionNode> listOrgNode = knowledge.getListRelatedOrganizationNode();
			if(listOrgNode != null){
				for (int i = 0; i < listOrgNode.size(); i++) {
					JSONObject jsonArrayConnNode = listOrgNode.get(i).toJSONObject();
					jsonArrayOrg.put(i,jsonArrayConnNode);
				}
			}
			knowledgeJsonObject.put("listRelatedOrganizationNode", jsonArrayOrg);
			
			
			requestStr = requestJsonObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return requestStr;
	}

	public boolean isCollected() {
		return collected;
	}

	public void setCollected(boolean collected) {
		this.collected = collected;
	}

	public int getAuthorType() {
		return authorType;
	}

	public void setAuthorType(int authorType) {
		this.authorType = authorType;
	}

	public boolean isZhongLeForMe() {
		return isZhongLeForMe;
	}

	public void setZhongLeForMe(boolean isZhongLeForMe) {
		this.isZhongLeForMe = isZhongLeForMe;
	}
	
	
}
