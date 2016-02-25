package com.tr.model.conference;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MMeetingNoticeQuery implements Serializable, Comparable<MMeetingNoticeQuery> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7702084551545006113L;
	/** 通知ID */
	private java.lang.Long id;
	/** 接收人类型 0:发起者1：参会人 */
	private java.lang.Integer receiverType;
	/** 接收人ID */
	private java.lang.Long receiver;
	/** 接收人名字 */
	private java.lang.String receiverName;
	/** 参会人Id */
	private java.lang.Long attendMeetingId;
	/** 参会人名字 */
	private String attendMeetingName;
	/** 通知类型：0：修改议题，1：报名申请，2：报名通过，3：报名未通过，4：接受邀请，5：拒绝邀请，6同意报名，7拒绝报名 ,8：取消参会，9：新增议题，10：修改议题，11：删除议题,12：退出会议，13：取消报名，14：删除会议*/
	private java.lang.Integer noticeType;
	/** 是否显示：0：不显示，1：显示 */
	private java.lang.Integer isShow;
	/** 通知内容 */
	private java.lang.String noticeContent;
	/** 会议ID */
	private java.lang.Long meetingId;
	/** 创建者ID */
	private java.lang.Long createId;
	/** 创建者名字 */
	private java.lang.String createName;
	/** 创建者头像 */
	private java.lang.String createPic;
	/**创建者性别 1：男 2：女*/
	private int createSex;
	/** 创建者名字 */
	private java.lang.String meetingCreateName;
	/** 成员报名信息列表 */
	private List<MMeetingSignLabelDataQuery> listMeetingSignLabelDataQuery;
	/** 通知 修改会议的字段列表 */
	private List<String> listMeetingField;
	/** 创建时间 */
	private String createTime;
	/** 修改时间 */
	private String updateTime;
	/**是否已读取 0：未读，1：已读*/
	private int isRead;
	/**读取时间*/
	private String readTime;
	
	
	
	
	
	
	public void setId(java.lang.Long arg0) {
		this.id = arg0;
	}

	public java.lang.Long getId() {
		return this.id;
	}

	public void setReceiverType(java.lang.Integer arg0) {
		this.receiverType = arg0;
	}

	public java.lang.Integer getReceiverType() {
		return this.receiverType;
	}

	public void setReceiver(java.lang.Long arg0) {
		this.receiver = arg0;
	}

	public java.lang.Long getReceiver() {
		return this.receiver;
	}

	public void setReceiverName(java.lang.String arg0) {
		this.receiverName = arg0;
	}

	public java.lang.String getReceiverName() {
		if (null == this.receiverName) {
			return "";
		}
		return this.receiverName;
	}

	public java.lang.Long getAttendMeetingId() {
		return attendMeetingId;
	}

	public void setAttendMeetingId(Long signUperId) {
		this.attendMeetingId = signUperId;
	}

	public void setAttendMeetingName(java.lang.String arg0) {
		this.attendMeetingName = arg0;
	}

	public java.lang.String getAttendMeetingName() {
		if (null == this.attendMeetingName) {
			return "";
		}
		if (attendMeetingName.equals("null")) {
			return "";
		}
		return this.attendMeetingName;
	}

	public void setNoticeType(java.lang.Integer arg0) {
		this.noticeType = arg0;
	}

	public java.lang.Integer getNoticeType() {
		return this.noticeType;
	}

	public void setIsShow(java.lang.Integer arg0) {
		this.isShow = arg0;
	}

	public java.lang.Integer getIsShow() {
		return this.isShow;
	}

	public void setNoticeContent(java.lang.String arg0) {
		this.noticeContent = arg0;
	}

	public java.lang.String getNoticeContent() {
		if (null == this.noticeContent) {
			return "";
		}
		return this.noticeContent;
	}

	public void setMeetingId(java.lang.Long arg0) {
		this.meetingId = arg0;
	}

	public java.lang.Long getMeetingId() {
		return this.meetingId;
	}

	public void setCreateId(java.lang.Long arg0) {
		this.createId = arg0;
	}

	public java.lang.Long getCreateId() {
		return this.createId;
	}

	public void setCreateName(java.lang.String arg0) {
		this.createName = arg0;
	}

	public java.lang.String getCreateName() {
		if (null == this.createName) {
			return "";
		}
		return this.createName;
	}

	public void setMeetingCreateName(java.lang.String arg0) {
		this.meetingCreateName = arg0;
	}

	public java.lang.String getMeetingCreateName() {
		if (null == this.meetingCreateName) {
			return "";
		}
		if (meetingCreateName.equals("null")) {
			return "";
		}
		return this.meetingCreateName;
	}

	public void setListMeetingSignLabelDataQuery(List<MMeetingSignLabelDataQuery> arg0) {
		this.listMeetingSignLabelDataQuery = arg0;
	}

	public List<MMeetingSignLabelDataQuery> getListMeetingSignLabelDataQuery() {
		if (null == this.listMeetingSignLabelDataQuery) {
			return new ArrayList<MMeetingSignLabelDataQuery>();
		}
		return this.listMeetingSignLabelDataQuery;
	}

	public void setListMeetingField(List<String> arg0) {
		this.listMeetingField = arg0;
	}

	public List<String> getListMeetingField() {
		if (null == this.listMeetingField) {
			return new ArrayList<String>();
		}
		return this.listMeetingField;
	}

	public void setCreateTime(String arg0) {
		this.createTime = arg0;
	}

	public String getCreateTime() {
		if (null == this.createTime) {
			return "";
		}
		return this.createTime;
	}

	public void setUpdateTime(String arg0) {
		this.updateTime = arg0;
	}

	public String getUpdateTime() {
		if (null == this.updateTime) {
			return "";
		}
		return this.updateTime;
	}
	
	

	public java.lang.String getCreatePic() {
		return createPic;
	}

	public void setCreatePic(java.lang.String createPic) {
		this.createPic = createPic;
	}

	public int getCreateSex() {
		return createSex;
	}

	public void setCreateSex(int createSex) {
		this.createSex = createSex;
	}

	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

	public String getReadTime() {
		return readTime;
	}

	public void setReadTime(String readTime) {
		this.readTime = readTime;
	}



	public static String sJsonArrayKeyName = "setMeetingNoticeQuery";

	public static Object createFactoryByArray(JSONArray jsonArray) {
		if (null == jsonArray) {
			return null;
		}
		TreeSet<MMeetingNoticeQuery> arraylistRet = new TreeSet<MMeetingNoticeQuery>();
		int iArrSize = jsonArray.length();
		if (iArrSize <= 0) {
			return null;
		}
		MMeetingNoticeQuery aObject = null;
		JSONObject aObj = null;
		for (int i = 0; i < iArrSize; i++) {
			try {
				aObj = jsonArray.getJSONObject(i);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (null == aObj) {
				continue;
			}
			aObject = new MMeetingNoticeQuery();
			if (null == aObject) {
				continue;
			}
			
			aObject.id = aObj.optLong("id");
			aObject.receiverType = aObj.optInt("receiverType");
			aObject.receiver = aObj.optLong("receiver");
			aObject.receiverName = aObj.optString("receiverName");
			aObject.attendMeetingId = aObj.optLong("attendMeetingId");
			aObject.attendMeetingName = aObj.optString("attendMeetingName");
			aObject.noticeType = aObj.optInt("noticeType");
			aObject.isShow = aObj.optInt("isShow");
			aObject.noticeContent = aObj.optString("noticeContent");
			aObject.meetingId = aObj.optLong("meetingId");
			aObject.createId = aObj.optLong("createId");
			aObject.createName = aObj.optString("createName");
			aObject.meetingCreateName = aObj.optString("meetingCreateName");
			
			
			aObject.createPic = aObj.optString("createPic");
			aObject.readTime = aObj.optString("readTime");
			aObject.createSex = aObj.optInt("createSex");
			aObject.isRead = aObj.optInt("isRead");
			
			
			JSONArray aAry = aObj.optJSONArray(MMeetingSignLabelDataQuery.sJsonArrayKeyName);
			if (null != aAry) {
				aObject.listMeetingSignLabelDataQuery = (List<MMeetingSignLabelDataQuery>) MMeetingSignLabelDataQuery.createFactoryByArray(aAry);
			}
			aAry = aObj.optJSONArray("listMeetingField");
			aObject.listMeetingField = new ArrayList<String>();
			if (null != aAry) {
				int cnt = aAry.length();
				for (int j = 0; j < cnt; ++j) {
					String sVal = "";
					try {
						sVal = aAry.getString(j);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					aObject.listMeetingField.add(sVal);
				}
			}
			aObject.createTime = aObj.optString("createTime");
			aObject.updateTime = aObj.optString("updateTime");
			arraylistRet.add(aObject);
		}
		return arraylistRet;
	}

	public static Object createFactory(JSONObject jsonObject) {
		if (null == jsonObject) {
			return null;
		}
		TreeSet<MMeetingNoticeQuery> treeSetRet = new TreeSet<MMeetingNoticeQuery>();
		if (!jsonObject.has(sJsonArrayKeyName)) {
			return null;
		}
		JSONArray joReqArry = null;
		try {
			joReqArry = jsonObject.getJSONArray(sJsonArrayKeyName);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (null == joReqArry) {
			return null;
		}

		int iArrSize = joReqArry.length();
		MMeetingNoticeQuery aObject = null;
		JSONObject aObj = null;
		for (int i = 0; i < iArrSize; i++) {
			try {
				aObj = joReqArry.getJSONObject(i);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (null == aObj) {
				continue;
			}
			aObject = new MMeetingNoticeQuery();
			if (null == aObject) {
				continue;
			}
			aObject.id = aObj.optLong("id");
			aObject.receiverType = aObj.optInt("receiverType");
			aObject.receiver = aObj.optLong("receiver");
			aObject.receiverName = aObj.optString("receiverName");
			aObject.attendMeetingId = aObj.optLong("attendMeetingId");
			aObject.attendMeetingName = aObj.optString("attendMeetingName");
			aObject.noticeType = aObj.optInt("noticeType");
			aObject.isShow = aObj.optInt("isShow");
			aObject.noticeContent = aObj.optString("noticeContent");
			aObject.meetingId = aObj.optLong("meetingId");
			aObject.createId = aObj.optLong("createId");
			aObject.createName = aObj.optString("createName");
			aObject.meetingCreateName = aObj.optString("meetingCreateName");
			JSONArray aAry = aObj.optJSONArray(MMeetingSignLabelDataQuery.sJsonArrayKeyName);
			if (null != aAry) {
				aObject.listMeetingSignLabelDataQuery = (List<MMeetingSignLabelDataQuery>) MMeetingSignLabelDataQuery.createFactoryByArray(aAry);
			}
			try {
				aAry = aObj.getJSONArray("listMeetingField");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			aObject.listMeetingField = new ArrayList<String>();
			if (null != aAry) {
				int cnt = aAry.length();
				for (int j = 0; j < cnt; ++j) {
					String sVal = "";
					try {
						sVal = aAry.getString(j);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					aObject.listMeetingField.add(sVal);
				}
			}
			aObject.createTime = aObj.optString("createTime");
			aObject.updateTime = aObj.optString("updateTime");
			treeSetRet.add(aObject);
		}
		return treeSetRet;
	}

	@Override
	public int compareTo(MMeetingNoticeQuery another) {
		if ((null == this.updateTime) || (null == another.updateTime)) {
			return 1;
		} else {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			try {
				Date dt1 = fmt.parse(this.updateTime);
				Date dt2 = fmt.parse(another.updateTime);
				if (dt2.before(dt1)) {
					return -1;
				} else if (dt2.after(dt1)) {
					return 1;
				} else {
					return 1;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

			return 1;
		}
	}
}
