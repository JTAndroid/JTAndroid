package com.tr.model.obj;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tr.App;
import com.tr.model.conference.MMeetingMember;
import com.tr.model.conference.MMeetingPeople;
import com.tr.model.conference.MMeetingTopicQuery;
import com.tr.model.conference.MSpeakerTopic;
import com.utils.string.StringUtils;

/**
 * @ClassName:     JTContactMini.java
 * @Description:   人脉精简对象
 * @Author         leon
 * @Version        v 1.0  
 * @Create         2014-04-14
 * @Update         2014-04-15
 */
public class JTContactMini implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public final static int type_friend = 0;
	public final static int type_himreq = 1;
	public final static int type_noFriend = 2;
	public final static int type_mereq = 3;
	
	public final static int SELECTED_MASK_NONE = 0x00000000;
	public final static int SELECTED_MASK_INVITE_ATTEND = 0x00000001;
	public final static int SELECTED_MASK_INVITE_SPEAKER = 0x00000002;
	public final static int SELECTED_MASK_SHARE_PEOPLEHUB = 0x00000004;
	
	/**	我创建的	*/
	public final static int FROM_TYPE_I_CREATE = 1;
	/**	我保存的	*/
	public final static int FROM_TYPE_I_SAVE = 2;
	/**	我收藏的	*/
	public final static int FROM_TYPE_I_COLLECT = 3;
	
	/**	人脉对象id，上传时为null	*/
	public String id="";
	/** 名 */
	public String name;
	/** 姓 */
	public String lastName="";
	/** 头像	*/
	public String image;
	/**	是否离线 这个现在不用了	*/
	public boolean isOffline= false;
	/** 所属机构 */
	public String company;
	/**	0-好友；1-对方请求我为好友；2-非好友；3-等待对方验证	*/
	public int mFriendState;
	/** 是否同事 */
	public boolean mIsWorkmate=false;
	public char nameChar=0;
	public int selectedMark = SELECTED_MASK_NONE; //0-none 1-invite attend 2-invite speaker 3-share peoplehub
	/**旧版主讲人议题列表---新版不用了*/
	public List<MSpeakerTopic> inviteSpeakerTopicList = new ArrayList<MSpeakerTopic>();
	
//	/**主讲人议题的图片集合*/
//	public List<MMeetingPic> listMeetingPicForSpeakerTopic = new ArrayList<MMeetingPic>();
//	/**主讲人议题的音视频+附件集合*/
//	public List<JTFile2ForHY> listMeetingFileForSpeakerTopic = new ArrayList<JTFile2ForHY>();
	/**新版  主讲人 --- 议题集合*/
	public List<MMeetingTopicQuery> lisMeetingTopicQuery = new ArrayList<MMeetingTopicQuery>();
	
	public boolean isExpandinviteSpeakerGroup = false;
	/** 手机号码 列表 */
	public ArrayList<MobilePhone> listMobilePhone=new ArrayList<MobilePhone>();
	/** 固话号码 列表 */
	public ArrayList<MobilePhone> listFixedPhone=new ArrayList<MobilePhone>();
	public ArrayList<String> listEmail=new ArrayList<String>();
	
	public boolean isAlterMeeting;
	public MMeetingMember alterMeetingMember;
	public MMeetingPeople alterMeetingPeople;
	/**	是否线上人脉  true是好友 false 人脉	*/
	public boolean isOnline = false;
	private int friendState;
	private boolean isWorkmate;
	private String ownerid;
	private String ownername;
	private String career;
	/**	人脉来源类型：1-我创建的；2-我保存的；3-我收藏的	*/
	private int fromType;
	/**性别：1男，2女，其他未知*/
	private int gender;
	
	private List<String> listOrganizationID;

	public JTContactMini(){
		
	}
	
	public JTContactMini(String name, String image){
		this.name = name;
		this.image = image;
	}
	
	public Connections toConnections() {
		Connections connections = new Connections();
		connections.type = Connections.type_persion;
		connections.jtContactMini = this;
		return connections;
	}
	
	public static JTContactMini createFactory(JSONObject jsonObject){
		JTContactMini self = null;
		try{
			self = new JTContactMini();
			self.initWithJson(jsonObject);
			return self;
		}catch(Exception e){
			return null;
		}
	}
	
	// 进行兼容性处理
	public void doCompatible(){
		this.mFriendState = getFriendState();
		this.mIsWorkmate = isWorkmate();
	}
	
	
	/**
	 * 从json字符串里解析出数据 并 给本类的成员赋值
	 * @param jsonObject
	 * @throws JSONException
	 * @throws MalformedURLException
	 * @throws ParseException
	 */
	public void initWithJson(JSONObject jsonObject) throws JSONException,
			MalformedURLException, ParseException {

		String str_key = null;

		// 人脉对象id
		str_key = "id";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			id = jsonObject.getString(str_key);
		}

		// 名
		str_key = "name";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			name = jsonObject.getString(str_key);
		}
		
		// 姓
		str_key = "lastName";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			lastName = jsonObject.getString(str_key);
		}
		
		// 头像
		str_key = "image";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			image = jsonObject.getString(str_key);
		}
		
		// 所属公司
		str_key = "company";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			company = jsonObject.getString(str_key);
		}
		
		// 职位
		str_key = "career";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			career = jsonObject.getString(str_key);
		}

		// 是否离线  现在 不用了 20150130
		str_key = "isOffline";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			isOffline = jsonObject.getBoolean(str_key);
		}

		// 是否在线
		str_key = "isOnline";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			isOnline = jsonObject.getBoolean(str_key);
		}
				
		// 是否好友
		str_key = "friendState";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mFriendState = jsonObject.getInt(str_key);
		}
		
		// 性别
		str_key = "gender";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			gender = jsonObject.getInt(str_key);
		}
		
		// 是否同事
		str_key = "isWorkmate";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mIsWorkmate = jsonObject.getBoolean(str_key);
		}
		
		// 人脉来源类型：1-我创建的；2-我保存的；3-我收藏的
		str_key = "fromType";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			fromType = jsonObject.getInt(str_key);
		}
		
		//首字母
		str_key = "nameChar";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			String _nameChar = jsonObject.getString(str_key);
			if(!StringUtils.isEmpty(_nameChar)){
				_nameChar=_nameChar.toUpperCase();
				nameChar=_nameChar.charAt(0);
			}
		}
		
		// 手机号码 列表
		str_key = "listMobilePhone";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			JSONArray jArray = jsonObject.getJSONArray(str_key);
			if (jArray != null && jArray.length() > 0) {
				for (int i = 0; i < jArray.length(); i++) {
					MobilePhone mobilePhone=new MobilePhone() ;
					mobilePhone.initWithJson(jArray.getJSONObject(i));
					listMobilePhone.add(mobilePhone);
				}
			}
		}	
		
		// 固话号码 列表
		str_key = "listFixedPhone";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			JSONArray jArray = jsonObject.getJSONArray(str_key);
			if (jArray != null && jArray.length() > 0) {
				for (int i = 0; i < jArray.length(); i++) {
					MobilePhone mobilePhone=new MobilePhone() ;
					mobilePhone.initWithJson(jArray.getJSONObject(i));
					listFixedPhone.add(mobilePhone);
				}
			}
		}	
		
		str_key = "listEmail";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			JSONArray jArray = jsonObject.getJSONArray(str_key);
			if (jArray != null && jArray.length() > 0) {
				for (int i = 0; i < jArray.length(); i++) {
					String email=jArray.getString(i);
					listEmail.add(email);
				}
			}
		}
	}
	
	public JSONObject toJSONObject() throws JSONException{
		JSONObject jObject = new JSONObject();
		jObject.put("id", id);
		jObject.put("name", name);
		jObject.put("lastName", lastName);
		jObject.put("company", company);
		jObject.put("isOnline", isOnline);
		jObject.put("isOffline", isOffline);
		jObject.put("friendState", mFriendState);
		jObject.put("isWorkmate",mIsWorkmate);
		jObject.put("ownerid", App.getUserID());
		jObject.put("ownername", App.getNick());
		return jObject;
	}
	
	public char getNameChar() {
		return nameChar;
	}

	public void setNameChar(String nameChar) {
		if(null != nameChar && nameChar.length() > 0){
			this.nameChar = nameChar.charAt(0);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public ArrayList<MobilePhone> getListMobilePhone() {
		return listMobilePhone;
	}

	public void setListMobilePhone(ArrayList<MobilePhone> listMobilePhone) {
		this.listMobilePhone = listMobilePhone;
	}
	
	public ArrayList<MobilePhone> getListFixedPhone() {
		return listFixedPhone;
	}

	public void setListFixedPhone(ArrayList<MobilePhone> listFixedPhone) {
		this.listFixedPhone = listFixedPhone;
	}

	public ArrayList<String> getListEmail() {
		return listEmail;
	}

	public void setListEmail(ArrayList<String> listEmail) {
		this.listEmail = listEmail;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName =  lastName;
	}

	public String getCompany() {
		company = company==null?"":company;
		return company;
	}

	public void setCompany(String company) {
		company = company==null?"":company;
		this.company  = company;
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline =  isOnline;
	}

	public boolean isOffline() {
		return isOffline;
	}

	public void setOffline(boolean isOffline) {
		this.isOffline = isOffline;
	}

	public int getFriendState() {
		return friendState;
	}

	public void setFriendState(int friendState) {
		this.mFriendState = this.friendState = friendState;
	}

	public boolean isWorkmate() {
		return isWorkmate;
	}

	public void setWorkmate(boolean isWorkmate) {
		this.mIsWorkmate = this.isWorkmate = isWorkmate;
	}

	public List<String> getListOrganizationID() {
		return listOrganizationID;
	}

	public void setListOrganizationID(List<String> listOrganizationID) {
		this.listOrganizationID = listOrganizationID;
	}
	public String getOwnerid() {
		return ownerid;
	}

	public void setOwnerid(String ownerid) {
		this.ownerid = ownerid;
	}

	public String getOwnername() {
		return ownername;
	}

	public void setOwnername(String ownername) {
		this.ownername = ownername;
	}

	public String getCareer() {
		career = career==null?"":career;
		return career;
	}

	public void setCareer(String career) {
		career = career==null?"":career;
		this.career = career;
	}

	public int getFromType() {
		return fromType;
	}

	public void setFromType(int fromType) {
		this.fromType = fromType;
	}
	
}
