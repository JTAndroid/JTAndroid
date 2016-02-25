package com.tr.model.user;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tr.App;
import com.tr.model.conference.MMeetingData;
import com.tr.model.conference.MMeetingOrgan;
import com.tr.model.obj.MobilePhone;
import com.utils.string.StringUtils;

/**
 * @ClassName:     OrginazitionMini.java
 * @Description:   机构精简对象
 * @Author         leon
 * @Version        v 1.0  
 * @Create         2014-04-09
 * @Update         2014-04-14
 */
public class OrganizationMini implements Serializable{

	
	private static final long serialVersionUID = 1L;
	public final static int type_fri_friend = 0;//
	public final static int type_fri_himreq = 1;
	public final static int type_fri_noFriend = 2;
	public final static int type_fri_mereq = 3;

	public final static int type_guest_generally = 0;// 一般用户
	public final static int type_guest_cooperation = 1;
	public final static int type_guest_core = 2;

	public final static int type_join_ok = 0;
	public final static int type_join_no = 1;
	public final static int type_join_mereq = 2;
	public final static int type_join_himreq = 3;

	public String mID = "";
	public String mLogo = "";
	public String fullName = "";
	public String shortName = "";
	public int mGuestType = 0;
	public boolean isOnline = false;
	public boolean mIsOffline = false;
	public int mFriendState = 0;
	public int mJoinState = 0;
	public String mTrade = "";
	public char nameChar = 0;
	public ArrayList<MobilePhone> listMobilePhone = new ArrayList<MobilePhone>();
	public ArrayList<String> listEmail = new ArrayList<String>();
	
	public String id;
	public String logo;
//	public String fullName;
//	public String shortName;
	public int guestType;
//	public boolean isOnline;
	public boolean isOffline;
	public int friendState;
	public int jointState;
	public String trade;
	public String ownerid;
	public String ownername;

	public OrganizationMini(){
		
	}
	public boolean isAlterMeeting;
    public MMeetingOrgan alterMMeetingOrgan;
	public OrganizationMini(String fullName, String trade, String logo){
		this.fullName = fullName;
		this.mTrade = trade;
		this.mLogo = logo;
	}
	
	// 进行兼容性处理
	public void doCompatible(){
		this.mID = getId();
		this.mLogo = getLogo();
		this.fullName = getFullName();
		this.shortName = getShortName();
		this.mGuestType = getGuestType();
//		this.isOnline = isOnline();
		this.mIsOffline = isOffline();
		this.mFriendState = getFriendState();
		this.mJoinState = getJointState();
		this.mTrade = getTrade();
	}
	
	public void initWithJson(JSONObject jsonObject) throws JSONException,
			MalformedURLException, ParseException {

		String str_key;

		// 机构id
		str_key = "id";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mID = jsonObject.getString(str_key);
		}

		// 机构logo图片url
		str_key = "logo";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mLogo = jsonObject.getString(str_key);
		}
		
		// 机构全称
		str_key = "fullName";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			fullName = jsonObject.getString(str_key);
		}

		// 机构简称
		str_key = "shortName";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			shortName = jsonObject.getString(str_key);
		}
		
		str_key = "trade";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mTrade = jsonObject.getString(str_key);
		}
		
		// 客户类型
		str_key = "guestType";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mGuestType = jsonObject.getInt(str_key);
		}
		
		// 是否在线
		str_key = "isOffline";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mIsOffline = jsonObject.getBoolean(str_key);
		}
		
		// 是否在线
		str_key = "isOnline";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			isOnline = jsonObject.getBoolean(str_key);
		}
		
		// 是否好友
		str_key = "friendState";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mFriendState  = jsonObject.getInt(str_key);
		}
				
		// 加入机构状态
		str_key = "joinState";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mJoinState = jsonObject.getInt(str_key);
		}
		
		// 首字母
		str_key = "nameChar";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			String _nameChar = jsonObject.getString(str_key);
			if (!StringUtils.isEmpty(_nameChar)) {
				_nameChar = _nameChar.toUpperCase();
				nameChar = _nameChar.charAt(0);
			}
		}

		// 电话号码
		str_key = "listMobilePhone";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			JSONArray jArray = jsonObject.getJSONArray(str_key);
			if (jArray != null && jArray.length() > 0) {
				for (int i = 0; i < jArray.length(); i++) {
					MobilePhone mobilePhone = new MobilePhone();
					mobilePhone.initWithJson(jArray.getJSONObject(i));
					listMobilePhone.add(mobilePhone);
				}
			}
		}

		str_key = "listEmail";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			JSONArray jArray = jsonObject.getJSONArray(str_key);
			if (jArray != null && jArray.length() > 0) {
				for (int i = 0; i < jArray.length(); i++) {
					String email = jArray.getString(i);
					listEmail.add(email);
				}
			}
		}
	}
	
	public JSONObject toJSONObject() throws JSONException{
		JSONObject jObject = new JSONObject();
		jObject.put("id", mID);
		jObject.put("logo", mLogo);
		jObject.put("fullName", fullName);
		jObject.put("short", shortName);
		jObject.put("guestType", mGuestType);
		jObject.put("isOnline", isOnline);
		jObject.put("friendState", mFriendState);
		jObject.put("joinState", mJoinState);
		jObject.put("trade", mTrade);
		jObject.put("ownerid", App.getUserID());
		jObject.put("ownername", App.getNick());
		return jObject;
	}
	
	public char getNameChar() {
		return nameChar;
	}

	public void setNameChar(char nameChar) {
		this.nameChar = nameChar;
	}

	public ArrayList<MobilePhone> getListMobilePhone() {
		return listMobilePhone;
	}

	public void setListMobilePhone(ArrayList<MobilePhone> listMobilePhone) {
		this.listMobilePhone = listMobilePhone;
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
		this.mID = this.id = id;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.mLogo = this.logo = logo;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public int getGuestType() {
		return guestType;
	}

	public void setGuestType(int guestType) {
		this.mGuestType = this.guestType = guestType;
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public boolean isOffline() {
		return isOffline;
	}

	public void setOffline(boolean isOffline) {
		this.mIsOffline = this.isOffline = isOffline;
	}

	public int getFriendState() {
		return friendState;
	}

	public void setFriendState(int friendState) {
		this.mFriendState = this.friendState = friendState;
	}

	public int getJointState() {
		return jointState;
	}

	public void setJointState(int jointState) {
		this.mJoinState = this.jointState = jointState;
	}

	public String getTrade() {
		return trade;
	}

	public void setTrade(String trade) {
		this.mTrade = this.trade = trade;
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
}
