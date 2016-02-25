package com.tr.model.work;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.tr.App;
import com.tr.model.joint.AffairNode;
import com.tr.model.joint.ConnectionNode;
import com.tr.model.joint.KnowledgeNode;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.obj.AffairsMini;
import com.tr.model.obj.Connections;

public class BUAffar implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4942438490980246280L;

	public long id; // "id": "主键",
	public String title = ""; // "title":"事务名称，如果类型为文本，则直接显示，如果为语音，则此字段为语音文件id",
	public String titleType = "t"; // "titleType": "名称类型 t:文本，v：语音",
	public String startTime = ""; // "startTime": "开始时间",
	public String endTime = ""; // ":"结束时间 ",
	public String remindType = "o"; // ": "提醒类型 m:分钟，h：小时，d：天，其他为不提醒",
	public int remindAhead = 0; // ": "提醒提前时间",
	public String repeatType = "o"; // ": "提醒重复类型， d:天，w:周，m：月，y:年，其他为不重复",
	public List<BUAffarMember> memebers; // " :[{affairMember}..], "type":"成员类型，c:创建者，o：负责人，m：成员"
	public int color = 0; // ":"颜色 0 无色 1:红，2:黄,3:绿,4:蓝",
	public double longitude = 0; // ":"经度",
	public double latitude = 0; // ":"纬度",
	public String location = ""; // ":"地理位置",
	public String infoStr = ""; // 文本备注
	public List<BUAffarInfo> infos; //
	public List<BUAffarRelation> relations;
	public List<BUAffarLog> logs; // 日志
	// public String status = "0"; // "状态，0：未完成，1：已完成",
	public int duration = 0; // 标题时常
	public int logTotal = 0;
	public String expired = "0";
	public String finished = "0";

	// --下面是控制数据
	private int mShowType = 0; // 0 正常数据 1 日期数据
	public int mListRemindType = 1; // "0:不提醒，1：提醒",
	public String mLogDesc; // 列表备注
	
	public String isNew;//"是否有新的事物通知,1:有新事务，0：没有新事务"
	
	public String logIsNew;//是否有新事物通知，"0"是已读，"1"是未读
	public String mucIsNew;//"是否有新的畅聊，1：新的畅聊，0：没有新的畅聊"

	

	public BUAffar() {
		memebers = new ArrayList<BUAffarMember>();
		infos = new ArrayList<BUAffarInfo>();
		relations = new ArrayList<BUAffarRelation>();
		logs = new ArrayList<BUAffarLog>();

		Date vDate = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		startTime = df.format(vDate);

		// demoData();
	}

	public static BUAffar getAffar(String inMemberID, String inMemberName) {
		BUAffar vAffar = new BUAffar();
		vAffar.setAffarCreateUser(inMemberID, inMemberName);

		return vAffar;

	}

	public void addDefaulLeader() {
		BUAffarMember vMemItem = new BUAffarMember();
		vMemItem.type = "o";
		vMemItem.memeberId = Long.parseLong(App.getUserID());
		vMemItem.name = App.getNick();
		vMemItem.picUrl = App.getUserAvatar();

		vMemItem.deviceType = "1";
		memebers.add(vMemItem);
	}

	public int getMemberCount(String inType) {
		int i;
		int vCount;
		vCount = 0;
		for (i = 0; i < memebers.size(); i++) {
			BUAffarMember vMember = memebers.get(i);
			if (vMember.getType().equals(inType)) {
				vCount = vCount + 1;
			}
		}
		return vCount;
	}

	public int getRelationCount(String inType) {
		int i;
		int vCount;
		vCount = 0;
		for (i = 0; i < relations.size(); i++) {
			BUAffarRelation vRelation = relations.get(i);
			if (vRelation.getType().equals(inType)) {
				vCount = vCount + 1;
			}
		}
		return vCount;
	}

	public List<BUAffarRelation> getRelationList(String inType) {
		int i;
		List<BUAffarRelation> vRelations;
		vRelations = new ArrayList<BUAffarRelation>();
		for (i = 0; i < relations.size(); i++) {
			BUAffarRelation vRelation = relations.get(i);
			if (vRelation.getType().equals(inType)) {
				vRelations.add(vRelation);
			}
		}
		return vRelations;
	}

	public void delRelationType(String inType) {
		int i;
		i = relations.size() - 1;
		while (i >= 0) {
			BUAffarRelation vRelation = relations.get(i);
			if (vRelation.getType().equals(inType)) {
				relations.remove(vRelation);
			}
			i = i - 1;
		}
	}

	public String getRelationTitle(String inType, String inLabel) {
		int i;
		String vStr;
		vStr = "";
		if (!relations.isEmpty()) {
			
		for (i = 0; i < relations.size(); i++) {
			BUAffarRelation vRelation = relations.get(i);
			if (vRelation.getType().equals(inType)
					&& vRelation.getLabel().equals(inLabel)) {
				if (TextUtils.isEmpty(vStr))
					vStr = vRelation.getTitle();
				else
					vStr = vStr + "、" + vRelation.getTitle();
			}
		}
		}
		return vStr;
	}

	public void deleteMemberByType(String inType) {
		int i;
		i = memebers.size() - 1;
		while (i >= 0) {

			BUAffarMember vMemItem = memebers.get(i);
			if (vMemItem.type.equals(inType)) {
				memebers.remove(i);
			}
			i = i - 1;
		}
	}

	public void deleteMemberByLeader() {
		int i;
		BUAffarMember vMemberLead = null;
		for (i = 0; i < memebers.size(); i++) {
			BUAffarMember vMemItem = memebers.get(i);
			if (vMemItem.type.equals("o")) {
				vMemberLead = vMemItem;
			}
		}
		if (vMemberLead != null) {
			i = memebers.size() - 1;
			while (i >= 0) {
				BUAffarMember vMemItem = memebers.get(i);
				if (vMemItem.type.equals("m")) {
					if (vMemItem.memeberId == vMemberLead.memeberId) {
						memebers.remove(i);
					}
				}
				i = i - 1;
			}
		}
	}

	public void deleteLeaderByMember() {
		int i;
		int vLeaderIndex = -1;
		BUAffarMember vMemberLead = null;
		for (i = 0; i < memebers.size(); i++) {
			BUAffarMember vMemItem = memebers.get(i);
			if (vMemItem.type.equals("o")) {
				vMemberLead = vMemItem;
				vLeaderIndex = i;
			}
		}
		boolean isDel = false;
		if (vMemberLead != null) {
			i = memebers.size() - 1;
			while (i >= 0) {
				BUAffarMember vMemItem = memebers.get(i);
				if (vMemItem.type.equals("m")) {
					if (vMemItem.memeberId == vMemberLead.memeberId) {
						isDel = true;
					}
				}
				i = i - 1;
			}
		}

		if (isDel && vLeaderIndex > 0 && vLeaderIndex < memebers.size()) {
			memebers.remove(vLeaderIndex);
		}
	}

	public void addMemberByList(String inType, ArrayList<Connections> inList) {
		if (inList == null)
			return;
		int i;
		for (i = 0; i < inList.size(); i++) {
			Connections vItem = inList.get(i);
			BUAffarMember vMemItem = new BUAffarMember();
			vMemItem.type = inType;
			vMemItem.memeberId = Long.parseLong(vItem.getId());
			vMemItem.name = vItem.getName();
			vMemItem.picUrl = vItem.getImage();
			vMemItem.deviceType = vItem.getType();
			memebers.add(vMemItem);
			Log.d("xmx", "adduser id:" + vMemItem.memeberId + ",name:"
					+ vMemItem.name);

		}
	}

	public void deleteRelationTitle(String inType, String inLabel) {
		int i;
		i = relations.size() - 1;
		while (i >= 0) {
			BUAffarRelation vRelation = relations.get(i);
			if (vRelation.getType().equals(inType)
					&& vRelation.getLabel().equals(inLabel)) {
				relations.remove(i);
			}
			i = i - 1;
		}
	}

	public ConnectionNode genConnectonNodeWithMemberType(String inType) {
		int i;
		ConnectionNode vNode = new ConnectionNode();
		for (i = 0; i < memebers.size(); i++) {
			BUAffarMember vMember = memebers.get(i);
			if (vMember.getType().equals(inType)) {
				Connections vConnect = new Connections();
				if (vMember.deviceType.equals("1"))
					vConnect.setType(Connections.type_persion + "");
				else
					vConnect.setType(Connections.type_org + "");
				vNode.setMemo("");
				vConnect.setID(vMember.memeberId + "");

				vNode.getListConnections().add(vConnect);
			}
		}
		return vNode;
	}

	public ConnectionNode genConnectonNodeByTypeLabel(String inType,
			String inLabel) {
		int i;
		ArrayList<Connections> vList = new ArrayList<Connections>();
		ConnectionNode vNode = new ConnectionNode();

		for (i = 0; i < relations.size(); i++) {
			BUAffarRelation vRelation = relations.get(i);
			if (vRelation.getType().equals(inType)
					&& vRelation.getLabel().equals(inLabel)) {
				Connections vConnect = new Connections();
				vNode.setMemo(vRelation.label);
				if (vRelation.userOrType.equals("u")) // 用户
					vConnect.jtContactMini.isOnline = true;
				else if (vRelation.userOrType.contains("p"))// 人脉
					vConnect.jtContactMini.isOnline = false;

				if (inType.equals("p"))
					vConnect.setType(Connections.type_persion + "");
				else
					vConnect.setType(Connections.type_org + "");

				vConnect.setID(vRelation.relateId + "");
				vConnect.setName(vRelation.title);

				// vConnect.setCareer(vRelation.career);
				// vConnect.setCompany(vRelation.company);

				vList.add(vConnect);

			}
		}
		vNode.setListConnections(vList);
		return vNode;
	}

	// 事务组织/客户参数
	public void addRelationWithConnectionNode(ConnectionNode inNode,String inType)
	{
		int i;
		if (inNode == null)
			return;
		ArrayList<Connections> vConnectList = inNode.getListConnections();
		for (i = 0; i < vConnectList.size(); i++) {
			Connections vConnections = vConnectList.get(i);
			BUAffarRelation vRelation = new BUAffarRelation();
			vRelation.type = inType;
			
			vRelation.label = inNode.getMemo();
			

			if (inType.equals("p")) // 人脉
			{
				vRelation.picUrl = vConnections.jtContactMini.image;
				vRelation.relateId =  Long.parseLong(TextUtils.isEmpty(vConnections.jtContactMini.id)?-1+"":vConnections.jtContactMini.id);
				vRelation.title = vConnections.jtContactMini.name;
				if (vConnections.isOnline())
					vRelation.userOrType = "u"; // 用户
				else
					vRelation.userOrType = "p"; // 人脉
			} else {
				vRelation.picUrl = vConnections.organizationMini.logo;
				vRelation.relateId =  Long.parseLong(TextUtils.isEmpty(vConnections.organizationMini.id)?-1+"":vConnections.organizationMini.id);
				vRelation.title = TextUtils.isEmpty(vConnections.organizationMini.shortName)?vConnections.organizationMini.fullName:vConnections.organizationMini.shortName;
				// 组织
				if (vConnections.isOnline())
					vRelation.userOrType="o";		//组织
				else
					vRelation.userOrType="c";		//客户
				
			}

			
			// vRelation.career=vConnections.getCareer();
			// vRelation.company=vConnections.getCompany();

			relations.add(vRelation);
			
			Log.d("xmx", "add type:" + inType + ",id:" + vRelation.relateId
					+ ",picUrl:" + vRelation.picUrl + ",label:"
					+ vRelation.label + ",title:" + vRelation.title+",utype:"+vRelation.userOrType);
		}
	}

	public KnowledgeNode genKnowledgeNodeByTypeLabel(String inType,
			String inLabel) {
		int i;

		// ArrayList<KnowledgeMini2> vList= new ArrayList<KnowledgeMini2>();
		KnowledgeNode vNode = new KnowledgeNode();

		for (i = 0; i < relations.size(); i++) {
			BUAffarRelation vRelation = relations.get(i);
			if (vRelation.getType().equals(inType)
					&& vRelation.getLabel().equals(inLabel)) {
				KnowledgeMini2 vKnowItem = new KnowledgeMini2();

				vNode.setMemo(vRelation.label);
				vKnowItem.id = vRelation.relateId;
				vKnowItem.type = Integer.parseInt(vRelation.userOrType);
				vKnowItem.title = vRelation.title;
				// vKnowItem.columnpath=vRelation.columnpath;
				vNode.getListKnowledgeMini2().add(vKnowItem);

			}
		}
		// vNode.setListKnowledgeMini2(vList);
		return vNode;
	}

	public void addRelationWithKnowledgeNode(KnowledgeNode inNode, String inType) {
		int i;
		if (inNode == null)
			return;

		ArrayList<KnowledgeMini2> vKnowledgeList = inNode
				.getListKnowledgeMini2();
		for (i = 0; i < vKnowledgeList.size(); i++) {
			KnowledgeMini2 vKnowledge = vKnowledgeList.get(i);
			BUAffarRelation vRelation = new BUAffarRelation();
			vRelation.type = inType;
			vRelation.relateId = vKnowledge.id;
			vRelation.picUrl = "";
			vRelation.label = inNode.getMemo();
			vRelation.title = vKnowledge.title;
			vRelation.userOrType = vKnowledge.type + "";
			vRelation.relateTime = vKnowledge.modifytime;
			// vRelation.columnpath=vKnowledge.columnpath;

			Log.d("xmx", "add type:" + inType + ",id:" + vRelation.relateId
					+ ",picUrl:" + vRelation.picUrl + ",label:"
					+ vRelation.label + ",relateTime:" + vRelation.relateTime);

			relations.add(vRelation);
		}
	}

	public AffairNode genAffairNodeByTypeLabel(String inType, String inLabel) {
		int i;
		ArrayList<AffairsMini> vList = new ArrayList<AffairsMini>();
		AffairNode vNode = new AffairNode();
		for (i = 0; i < relations.size(); i++) {
			BUAffarRelation vRelation = relations.get(i);
			if (vRelation.getType().equals(inType)
					&& vRelation.getLabel().equals(inLabel)) {
				AffairsMini vAffarMiniItem = new AffairsMini();
				vAffarMiniItem.id = (int) vRelation.relateId;
				vAffarMiniItem.title = vRelation.title;
				vAffarMiniItem.name = "";
				vAffarMiniItem.reserve = "";
				vNode.setMemo(vRelation.label);
				vList.add(vAffarMiniItem);

			}
		}
		vNode.setListAffairMini(vList);
		return vNode;
	}

	public void addRelationWithAffairNode(AffairNode inNode, String inType) {
		int i;
		if (inNode == null)
			return;
		ArrayList<AffairsMini> vKnowledgeList = inNode.getListAffairMini();
		for (i = 0; i < vKnowledgeList.size(); i++) {
			AffairsMini vAffarMini = vKnowledgeList.get(i);
			BUAffarRelation vRelation = new BUAffarRelation();
			vRelation.type = inType;
			vRelation.relateId = vAffarMini.id;
			vRelation.picUrl = "";
			vRelation.label = inNode.getMemo();
			vRelation.title = vAffarMini.title;

			Log.d("xmx", "add type:" + inType + ",id:" + vRelation.relateId
					+ ",picUrl:" + vRelation.picUrl + ",label:"
					+ vRelation.label + ",title:" + vRelation.title);

			relations.add(vRelation);
		}
	}

	public String getRelationLabel(String inType) {
		int i;
		for (i = 0; i < relations.size(); i++) {
			BUAffarRelation vRelation = relations.get(i);
			if (vRelation.getType().equals(inType)) {
				return vRelation.getLabel();
			}
		}
		return "";
	}

	public List<String> getRelationLabelListLabel(String inType) {
		int i;
		List<String> vStrList = new ArrayList<String>();
		String vstr = "";
		for (i = 0; i < relations.size(); i++) {
			BUAffarRelation vRelation = relations.get(i);
			if (vRelation.getType().equals(inType)) {
				if (!vRelation.getLabel().equals(vstr)) {
					vStrList.add(vRelation.getLabel());
					vstr = vRelation.getLabel();
				}
			}
		}
		return vStrList;
	}

	public static String getRepeatTypeName(String inRepeatType) {
		if (inRepeatType.equals("y"))
			return "每年";
		if (inRepeatType.equals("m"))
			return "每月";
		if (inRepeatType.equals("w"))
			return "每周";
		if (inRepeatType.equals("d"))
			return "每天";
		return "不重复";
	}

	public static String getColorName(int inColorId) {
		if (inColorId == 1 || inColorId == 2)
			return "非常重要";
		if (inColorId == 3 || inColorId == 4)
			return "重要";
		if (inColorId == 0)
			return "普通";
		return "普通";
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

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getRemindType() {
		return remindType;
	}

	public void setRemindType(String remindType) {
		this.remindType = remindType;
	}

	public int getRemindAhead() {
		return remindAhead;
	}

	public void setRemindAhead(int remindAhead) {
		this.remindAhead = remindAhead;
	}

	public String getRepeatType() {
		return repeatType;
	}

	public void setRepeatType(String repeatType) {
		this.repeatType = repeatType;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getIsNew() {
		return logIsNew;
	}

	public void setIsNew(String isNew) {
		this.logIsNew = isNew;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getInfoStr() {
		return infoStr;
	}

	public void setInfoStr(String infoStr) {
		this.infoStr = infoStr;
	}

	public int getmShowType() {
		return mShowType;
	}

	public void setmShowType(int mShowType) {
		this.mShowType = mShowType;
	}

	public static BUAffar genAffarMinByJson(JSONObject inJSONObject) {
		BUAffar vAffar = new BUAffar();

		try {
			//Log.d("xmx", "minAffar:" + inJSONObject.toString());

			vAffar.id = inJSONObject.getLong("id");
			vAffar.title = inJSONObject.getString("title");
			vAffar.titleType = inJSONObject.getString("titleType");
			vAffar.duration = inJSONObject.getInt("duration");
			vAffar.mListRemindType = inJSONObject.getInt("remindType");
			vAffar.color = inJSONObject.getInt("color");
			vAffar.startTime = inJSONObject.getString("ctime");

			vAffar.expired = inJSONObject.getString("expired");
			vAffar.finished = inJSONObject.getString("finished");
			vAffar.isNew = inJSONObject.getString("isNew");

			vAffar.mLogDesc = "";
			if (inJSONObject.has("latestLog")) {
				JSONObject vLog = inJSONObject.getJSONObject("latestLog");
				if (vLog.has("info"))
					vAffar.mLogDesc = vLog.getString("info");
			}

			Log.d("xmx", "Affar:" + vAffar.getId() + " vAffar.startTime:"
					+ vAffar.startTime + ",titletype:" + vAffar.titleType);
			
			

		} catch (JSONException e) {
			e.printStackTrace();
			Log.d("xmx", "genAffarMinByJson cahs");
		}

		return vAffar;
	}

	public static BUAffar genAffarDetailByJson(JSONObject inJSONObject) {
		int i, j;
		BUAffar vAffar = new BUAffar();
		Log.d("xmx", "inJSONObject:" + inJSONObject.toString());
		try {
			if (inJSONObject != null && inJSONObject.has("affairDetail")) {
				JSONObject vaffairDetail = inJSONObject
						.getJSONObject("affairDetail");
				Log.d("xmx", "vaffairDetail:" + vaffairDetail.toString());
				vAffar.id = vaffairDetail.getLong("id");

				Log.d("xmx", "vAffar.id:" + vAffar.id);

				vAffar.duration = vaffairDetail.getInt("duration");

				vAffar.title = vaffairDetail.getString("title");
				vAffar.titleType = vaffairDetail.getString("titleType");
				vAffar.startTime = vaffairDetail.getString("startTime");
				vAffar.endTime = vaffairDetail.getString("endTime");
				Log.d("xmx", "endTime:" + vAffar.id);
				vAffar.remindType = vaffairDetail.getString("remindType");
				vAffar.repeatType = vaffairDetail.getString("repeatType");
				vAffar.remindAhead = vaffairDetail.getInt("remindAhead");
				vAffar.color = vaffairDetail.getInt("color");
				Log.d("xmx", "color:" + vAffar.id);
				vAffar.longitude = vaffairDetail.getDouble("longitude");
				vAffar.latitude = vaffairDetail.getDouble("latitude");
				vAffar.location = vaffairDetail.getString("location");

				vAffar.logTotal = vaffairDetail.getInt("logTotal");
				vAffar.infoStr = vaffairDetail.getString("detail");

				vAffar.expired = vaffairDetail.getString("expired");
				vAffar.finished = vaffairDetail.getString("finished");
				
				vAffar.logIsNew = vaffairDetail.getString("logIsNew");//是否有新事物通知，"0"是已读，"1"是未读
				vAffar.mucIsNew = vaffairDetail.getString("mucIsNew");//"是否有新的畅聊，1：新的畅聊，0：没有新的畅聊"
				

				Log.d("xmx", "memebers begin");
				if (vaffairDetail.has("memebers")) {
					JSONArray vmemebers = vaffairDetail
							.getJSONArray("memebers");
					// Log.d("xmx", "vmemebers:" + vmemebers.toString());
					for (i = 0; i < vmemebers.length(); i++) {
						JSONObject vmemebersItem = (JSONObject) vmemebers
								.get(i);
						BUAffarMember vMember = new BUAffarMember();
						vMember.memeberId = vmemebersItem.getLong("memeberId");

						vMember.channelId = vmemebersItem
								.getString("channelId");
						vMember.type = vmemebersItem.getString("type");
						vMember.name = vmemebersItem.getString("name");
						vMember.picUrl = vmemebersItem.getString("picUrl");
						Log.d("xmx", "pic:" + vMember.picUrl);
						vMember.deviceType = vmemebersItem
								.getString("deviceType");

						if (vmemebersItem.has("phones")) {
							JSONArray vphones = vmemebersItem
									.getJSONArray("phones");
							for (j = 0; j < vphones.length(); j++) {
								JSONObject vPhoneItem = (JSONObject) vphones
										.get(j);
								BUPhone vPhone = new BUPhone();
								vPhone.type = vPhoneItem.getString("type");
								vPhone.phoneNo = vPhoneItem
										.getString("phoneNo");
								vMember.phones.add(vPhone);
								Log.d("xmx", "phones:" + vPhone.phoneNo);
							}
						}

						vAffar.memebers.add(vMember);
					}
				}

				if (vaffairDetail.has("infos")) {
					JSONArray vinfos = vaffairDetail.getJSONArray("infos");
					Log.d("xmx", "infos:" + vinfos.toString());
					for (i = 0; i < vinfos.length(); i++) {
						JSONObject vinfosItem = (JSONObject) vinfos.get(i);
						BUAffarInfo vInfo = new BUAffarInfo();
						vInfo.affairId = vinfosItem.getLong("affairId");

						vInfo.info = vinfosItem.getString("info");
						vAffar.infos.add(vInfo);
					}
				}

				if (vaffairDetail.has("relations")) {
					JSONArray vrelations = vaffairDetail
							.getJSONArray("relations");
					Log.d("xmx", "relations:" + vrelations.toString());
					for (i = 0; i < vrelations.length(); i++) {
						JSONObject vrelationsItem = (JSONObject) vrelations
								.get(i);
						BUAffarRelation vRelaItem = new BUAffarRelation();
						vRelaItem.id = vrelationsItem.getLong("id");
						vRelaItem.affairId = vrelationsItem.getLong("affairId");
						vRelaItem.relateId = vrelationsItem.getLong("relateId");

						vRelaItem.label = vrelationsItem.getString("label");
						vRelaItem.type = vrelationsItem.getString("type");
						vRelaItem.title = vrelationsItem.getString("title");
						vRelaItem.picUrl = vrelationsItem.getString("picUrl");
						vRelaItem.relateTime = vrelationsItem
								.getString("relateTime");
						vRelaItem.userOrType = vrelationsItem
								.getString("userOrType");

						vAffar.relations.add(vRelaItem);
					}
				}
				if (vaffairDetail.has("logIsNew")) {
					JSONObject mIsNew = vaffairDetail.getJSONObject("logIsNew");
					vAffar.logIsNew= mIsNew.toString();				
				}
				if (vaffairDetail.has("mucIsNew")) {
					JSONObject mIsNew = vaffairDetail.getJSONObject("mucIsNew");
					vAffar.mucIsNew= mIsNew.toString();				
				}

			}

		} catch (JSONException e) {
			e.printStackTrace();
			Log.d("xmx", "cash");
		}

		Log.d("xmx", "vAffar.id:" + vAffar.id);
		return vAffar;
	}

	public JSONObject getAffarJson() {
		int i;
		JSONObject vJsonAffar = new JSONObject();

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd hh:mm:ss");
		String vDate = df.format(new Date());

		try {
			vJsonAffar.put("id", id);

			vJsonAffar.put("duration", duration);
			vJsonAffar.put("title", title);
			vJsonAffar.put("titleType", titleType);
			vJsonAffar.put("startTime", startTime);
			vJsonAffar.put("endTime", endTime);
			vJsonAffar.put("remindType", remindType);
			vJsonAffar.put("remindAhead", remindAhead);
			vJsonAffar.put("color", color);
			vJsonAffar.put("longitude", longitude);
			vJsonAffar.put("latitude", latitude);
			vJsonAffar.put("location", location);
			vJsonAffar.put("repeatType", repeatType);
			vJsonAffar.put("detail", infoStr);
			
			
			
			// vJsonAffar.put("expired", expired);
			// vJsonAffar.put("finished", finished);

			if (memebers.size() > 0) {
				JSONArray vmemebers = new JSONArray();
				for (i = 0; i < memebers.size(); i++) {
					BUAffarMember vMember = memebers.get(i);
					JSONObject vJsonMember = new JSONObject();

					vJsonMember.put("memeberId", vMember.memeberId);
					vJsonMember.put("type", vMember.type);
					vJsonMember.put("name", vMember.name);
					vJsonMember.put("channelId", vMember.channelId);
					vJsonMember.put("deviceType", vMember.deviceType);

					vmemebers.put(vJsonMember);
				}
				vJsonAffar.put("memebers", vmemebers);
			}

			if (infos.size() > 0) {
				JSONArray vinfos = new JSONArray();
				for (i = 0; i < infos.size(); i++) {
					BUAffarInfo vInfo = infos.get(i);
					JSONObject vinfosItem = new JSONObject();

					vinfosItem.put("affairId", id);
					vinfosItem.put("info", vInfo.info);

					vinfos.put(vinfosItem);
				}
				vJsonAffar.put("infos", vinfos);
			}

			if (relations.size() > 0) {
				JSONArray vrelations = new JSONArray();
				for (i = 0; i < relations.size(); i++) {
					BUAffarRelation vRelaItem = relations.get(i);
					JSONObject vrelationsItem = new JSONObject();

					vrelationsItem.put("affairId", id);
					vrelationsItem.put("id", vRelaItem.id);

					vrelationsItem.put("relateId", vRelaItem.relateId);
					vrelationsItem.put("label", vRelaItem.label);
					vrelationsItem.put("type", vRelaItem.type);
					vrelationsItem.put("title", vRelaItem.title);
					vrelationsItem.put("relateTime", vDate);
					vrelationsItem.put("picUrl", vRelaItem.picUrl);
					vrelationsItem.put("userOrType", vRelaItem.userOrType);

					vrelations.put(vrelationsItem);
				}
				vJsonAffar.put("relations", vrelations);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return vJsonAffar;
	}

	public JSONObject getCharJson(long inUserId) {
		int i;
		JSONObject vJsonAffar = new JSONObject();

		try {
			vJsonAffar.put("affairId", id);
			vJsonAffar.put("title", title);
			vJsonAffar.put("JtUserId", inUserId);
			
			

			if (memebers.size() > 0) {
				JSONArray vmemebers = new JSONArray();
				for (i = 0; i < memebers.size(); i++) {
					BUAffarMember vMember = memebers.get(i);
					if (!vMember.type.equals("c"))
					{
						vmemebers.put(vMember.memeberId + "");
						Log.d("xmx", "id:" + vMember.memeberId + ",name:"
								+ vMember.name);
					}
				}
				vJsonAffar.put("listJTContactID", vmemebers);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return vJsonAffar;
	}

	public String getCharTitle() {
		int i;
		int vCount = 0;
		String vStr = "";
		for (i = 0; i < memebers.size(); i++) {
			BUAffarMember vMember = memebers.get(i);
			if (!vMember.type.equals("c")) {
				if (vCount == 0)
					vStr = vMember.name;
				else
					vStr = vStr + "、" + vMember.name;
				vCount = vCount + 1;
			}
		}
		return vStr;
	}

	public void setAffarCreateUser(String inMemberID, String inMemberName) {
		BUAffarMember vMember = new BUAffarMember();
		vMember.type = "c";
		vMember.name = inMemberName;
		vMember.memeberId = Long.parseLong(inMemberID);
		vMember.deviceType = "1";
		memebers.add(vMember);
	}

	public void deleteAffarMember(String inType) {
		int i;
		int vCount;
		vCount = memebers.size() - 1;
		while (vCount >= 0) {
			BUAffarMember vMember = memebers.get(vCount);
			if (vMember.getType().equals(inType)) {
				memebers.remove(vCount);
			}
			vCount = vCount - 1;
		}
	}

	public List<BUAffarMember> getAffarMember(String inType) {
		List<BUAffarMember> vMembers = new ArrayList<BUAffarMember>();
		int vCount;
		vCount = memebers.size() - 1;
		while (vCount >= 0) {
			BUAffarMember vMember = memebers.get(vCount);
			if (vMember.getType().equals(inType)) {
				vMembers.add(vMember);
			}
			vCount = vCount - 1;
		}
		return vMembers;
	}

	public String isCreateAffar(long inUserId) {
		int i;
		for (i = 0; i < memebers.size(); i++) {
			BUAffarMember vMember = memebers.get(i);
			Log.d("xmx", "id:" + vMember.memeberId + " type:" + vMember.type
					+ " inId:" + inUserId);

			if (vMember.memeberId == inUserId
					&& (vMember.type.equals("c") || vMember.type.equals("o"))) {
				return "0";
			}
		}

		return "1";
	}
}
