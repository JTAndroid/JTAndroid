package com.tr.model.obj;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.tr.model.demand.ASSORPOK;
import com.tr.model.demand.DemandASSOData;
import com.tr.model.page.IPageBaseItem;
import com.tr.model.user.OrganizationMini;
import com.utils.pinyin.PingYinUtil;
import com.utils.string.StringUtils;

/**
 * @ClassName:     Connections.java
 * @Description:   关系对象
 * @Author         leon
 * @Version        v 1.0  
 * @Create         2014-04-14
 * @Update         2014-04-14
 */
public class Connections extends ResourceBase implements IPageBaseItem,Serializable{
	
	private final String TAG = getClass().getSimpleName();
	
	private static final long serialVersionUID = 1L;
	// 个人和企业 只有个人才有 mListContactsMini,企业只有mListOrganizationMini
	public final static int type_persion = 1;
	public final static int type_org = 2;
	
	public int type = 0;//  1人  2组织
	public String sourceFrom = "";
	public JTContactMini jtContactMini = new JTContactMini();
	public OrganizationMini organizationMini = new OrganizationMini();
	public int sqlDBKeyId = 0;
	boolean isFocuse = false;
	
	public String id;
	private boolean focuse;

	private String name;
	
	public int gender;

	public Connections(){
		
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public Connections(JTContactMini jtContact) {
		type = type_persion;
		this.jtContactMini = jtContact;
	}
	
	public Connections(OrganizationMini organization){
		type = type_org;
		this.organizationMini = organization;
	}
	
	public static Connections createFactory(JSONObject jsonObject){
		try{
			Connections self = new Connections();
			self.initWithJson(jsonObject);
			return self;
		}
		catch(Exception e){
			return null;
		}
	}
	
	// 兼容性处理
	public void doCompatible(){
		this.type = Integer.parseInt(getType());
		this.sourceFrom = getSourceFrom();
		this.isFocuse = isFocuse();
		if(jtContactMini != null){
			jtContactMini.doCompatible();
		}
		if(organizationMini != null){
			organizationMini.doCompatible();
		}
	}
	public DemandASSOData toDemandASSOData() {
		DemandASSOData conn = new DemandASSOData();
		conn.id=this.getId();
		conn.name=this.getName();
		conn.career=this.getCareer();
		conn.company=this.getCompany();
		conn.picPath=this.getImage();
		conn.type=Integer.valueOf(this.getType());
		return conn;
	}
	/**
	 *  从json字符串里解析出数据 并 给本类的成员赋值
	 * @param jsonObject
	 * @throws JSONException
	 * @throws MalformedURLException
	 * @throws ParseException
	 */
	public void initWithJson(JSONObject jsonObject) throws JSONException,
			MalformedURLException, ParseException {

		String str_key = null;

		// 机构id
		str_key = "id";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			setID(jsonObject.getString(str_key));
		}

		// 类型
		str_key = "type";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			type = jsonObject.getInt(str_key);
		}
		
		// 性别
		str_key = "gender";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			gender = jsonObject.getInt(str_key);
		}

		// 关系来源
		str_key = "sourceFrom";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			sourceFrom = jsonObject.getString(str_key);
		}

		// 人脉
		str_key = "jtContactMini";
		if(jsonObject.has(str_key) && !jsonObject.isNull(str_key)){
			jtContactMini.initWithJson(jsonObject.getJSONObject("jtContactMini"));
		}
		
		// 机构
		str_key = "organizationMini";
		if(jsonObject.has(str_key) && !jsonObject.isNull(str_key)){
			organizationMini.initWithJson(jsonObject.getJSONObject("organizationMini"));
		}
	}
	
	public JSONObject toJSONObject() throws JSONException{
		JSONObject jObject = new JSONObject();
		jObject.put("id", getId());
		jObject.put("type", type);
		jObject.put("gender", gender);
		jObject.put("sourceFrom", sourceFrom);
		jObject.put("jtContactMini", jtContactMini !=null ?  jtContactMini.toJSONObject() : null);
		jObject.put("organizationMini", organizationMini !=null ?  organizationMini.toJSONObject() : null);
		return jObject;
	}
	
	public ConnectionsMini toMini(){
		ConnectionsMini connsMini = new ConnectionsMini();
		if (type == type_persion) { // 个人
			connsMini.setId(jtContactMini.id + "");
			connsMini.setName(jtContactMini.name);
			connsMini.setType(type_persion);
		} 
		else { // 机构
			connsMini.setId(organizationMini.mID + "");
			connsMini.setName(organizationMini.fullName);
			connsMini.setType(type_org);
		}
		return connsMini;
	}
	
	
	public String getName(){
		String name=null;
		if(this.type==Connections.type_org){
			if( organizationMini != null ){
				if(!TextUtils.isEmpty(organizationMini.shortName)){
					name = organizationMini.shortName;
				}
				else {
					name = organizationMini.getFullName();
				}
			}
		}
		else if(this.type==Connections.type_persion){
			if( null!= jtContactMini  ){
				name= jtContactMini.name;
			}
		}else{
			return this.name;
		}
		return name;
	}
	public String getLastName(){
		String lastName=null;
		if(this.type==Connections.type_org){
//			lastName= organizationMini != null ? organizationMini.mFullName : "";
		}
		else if(null != jtContactMini){
			lastName= jtContactMini != null ? jtContactMini.getLastName() : "";
		}
		return lastName;
	}
	
	public void setCompany(String company){
		// 现在组织的公司还没有定义 只有个人的公司
		if(this.type==Connections.type_org){
			
		}
		else if (this.type == Connections.type_persion) {
			jtContactMini.setCompany(company);
		}
	}
	public String getCompany(){
		String company=null;
		// 现在组织的公司还没有定义 只有个人的公司
		if(this.type==Connections.type_org){
//			name= organizationMini != null ? organizationMini.mFullName : "";
		}
		else if (this.type == Connections.type_persion) {
			company = jtContactMini != null ? jtContactMini.getCompany() : "";
		}
		return company;
	}
	
	
	public void setCareer(String career){
		// 现在组织的公司还没有定义 只有个人的公司
		if(this.type==Connections.type_org){
			
		}
		else if (this.type == Connections.type_persion) {
			jtContactMini.setCareer(career);
		}
	}
	
	public String getCareer(){
		String career=null;
		// 现在组织的公司还没有定义 只有个人的公司
		if(this.type==Connections.type_org){
//			name= organizationMini != null ? organizationMini.mFullName : "";
		}
		else if (this.type == Connections.type_persion) {
			career = jtContactMini != null ? jtContactMini.getCareer() : "";
		}
		return career;
	}
	
	public int getFromType(){
		int fromType = -1;
		
		if(this.type == Connections.type_org){
			
		}
		else if (this.type == Connections.type_persion) {
			fromType = jtContactMini != null ? jtContactMini.getFromType() : -1;
		}
		return fromType;
	}
	
	public int getGender(){
		int gender = 0;
		if(this.type == Connections.type_org){
					
		}
		else if (this.type == Connections.type_persion) {
			gender = jtContactMini != null ? jtContactMini.getGender() : 0;
		}
		return gender;
	}
	
	public void setGender(int gender){
		if(this.type == Connections.type_org){
			
		}
		else if (this.type == Connections.type_persion) {
			jtContactMini.setGender(gender);
		}
	}
	
	public String getMiniName(){
		String name=null;
		if(this.type==Connections.type_org){
			name= organizationMini != null ? organizationMini.fullName : "";
		}else if(null != jtContactMini){
			name= jtContactMini != null ? jtContactMini.getName() : "";
		}
		return name;
	}
	
//	public String getName2(){
//		String name=null;
//		if(Integer.valueOf(getType()) == Connections.type_org){
//			name=organizationMini.getFullName();
//		}else{
//			name=jtContactMini.getName();
//		}
//		return name;
//	}
	
	
	
	public boolean isOnline(){
		if(this.type==Connections.type_org){
			return organizationMini.isOnline;
		}else if(null != jtContactMini){
			return jtContactMini.isOnline;
		}
		return false;
	}
	
	public boolean isOffline(){
		if(this.type==Connections.type_org){
			return organizationMini.mIsOffline;
		}else{
			return jtContactMini.isOffline;
		}
	}
	
	public void setOffline(boolean isOffline) {
		if (this.type == Connections.type_org) {
			organizationMini.mIsOffline = isOffline;
			organizationMini.setOffline(isOffline);
		} 
		else {
			jtContactMini.setOffline(isOffline);
		}
	}
	
	public int isFriendState(){
		if(this.type==Connections.type_org){
			return organizationMini.mFriendState;
		}else{
			return jtContactMini.mFriendState;
		}

	}
	
	public String getId(){
		if(this.type==Connections.type_org && null != organizationMini){
			return organizationMini.mID;
		}else if(null != jtContactMini ){
			return !jtContactMini.id.equals("") ?  jtContactMini.id : jtContactMini.id;
		}
		return "0";
	}

	public int getmType() {
		return type;
	}

	public void setmType(int mType) {
		this.type = mType;
	}

	public String getmSourceFrom() {
		return sourceFrom;
	}

	public void setmSourceFrom(String mSourceFrom) {
		this.sourceFrom = mSourceFrom;
	}


	public boolean ismIsFocuse() {
		return isFocuse;
	}

	public void setmIsFocuse(boolean mIsFocuse) {
		this.isFocuse = mIsFocuse;
	}

	public void setName(String name) {
		this.name=name;
		if (type == type_org) {
			organizationMini.fullName = name;
		}
		else {
//			jtContactMini.name = name;
			jtContactMini.setName(name);
		}
	}
	
	public void setOnline(boolean isOnline) {
		if (type == type_org) {
			organizationMini.isOnline = isOnline;
		} 
		else {
			jtContactMini.isOnline = isOnline;
		}
	}
	
	public void setID(String id) {
		if (type == type_org) {
			organizationMini.mID = id;
			organizationMini.setId(id);
		} 
		else {
			jtContactMini.id = id;
		}
	}
	
	public void setEMail(String email) {
		if (type == type_org) {
			organizationMini.listEmail.clear();
			organizationMini.listEmail.add(email);
		} 
		else {
			jtContactMini.listEmail.clear();
			jtContactMini.listEmail.add(email);
		}
	}
	
	public void setPhone(String phone) {
		if (StringUtils.isEmpty(phone)) {
			return;
		}
		MobilePhone mobilePhone = new MobilePhone();
		mobilePhone.mobile = phone;
		if (type == type_org) {
			organizationMini.listMobilePhone.clear();
			organizationMini.listMobilePhone.add(mobilePhone);
		} 
		else {
			jtContactMini.listMobilePhone.clear();
			jtContactMini.listMobilePhone.add(mobilePhone);
		}
	}
	
	public String getMobilePhone(){
		String text="";
		if(this.type==Connections.type_org){
			if(organizationMini.listMobilePhone!=null&&organizationMini.listMobilePhone.size()>0){
				if(organizationMini.listMobilePhone.get(0)!=null){
					text=organizationMini.listMobilePhone.get(0).mobile;
				}
			}
		}else{
			if(jtContactMini.listMobilePhone!=null&&jtContactMini.listMobilePhone.size()>0){
				if(jtContactMini.listMobilePhone.get(0)!=null){
					text=jtContactMini.listMobilePhone.get(0).mobile;
				}
			}
		}
		if(StringUtils.isEmpty(text)){
			text="";
		}
		return text;
	}
	
	
	/**
	 * 设置 手机 号码列表
	 * @return
	 */
	public void setMobilePhoneList(ArrayList<MobilePhone> mobilePhoneList){
		if(this.type==Connections.type_org){
			// 这里现在没做组织
			
		}
		else{
				jtContactMini.setListMobilePhone(mobilePhoneList);
		}
	}
	
	/**
	 * 返回手机号码列表
	 * @return
	 */
	public ArrayList<MobilePhone> getMobilePhoneList(){
		ArrayList<MobilePhone> mobilePhoneList = new ArrayList<MobilePhone>();
		if(this.type==Connections.type_org){
			// 这里现在没做组织
			/*if(organizationMini.listMobilePhone!=null&&organizationMini.listMobilePhone.size()>0){
				if(organizationMini.listMobilePhone.get(0)!=null){
					text=organizationMini.listMobilePhone.get(0).mobile;
				}
			}*/
		}
		else{
			if (jtContactMini.getListMobilePhone() != null && jtContactMini.getListMobilePhone().size() > 0) {
				mobilePhoneList =  jtContactMini.getListMobilePhone();
			}
		}
		return mobilePhoneList;
	}
	
	/**
	 * 设置 固定电话 号码列表
	 * @return
	 */
	public void setFixedPhoneList(ArrayList<MobilePhone> fixedPhoneList){
		if(this.type==Connections.type_org){
			// 这里现在没做组织
			
		}
		else{
				jtContactMini.setListFixedPhone(fixedPhoneList);
		}
	}
	/**
	 * 返回 固定电话 号码列表
	 * @return
	 */
	public ArrayList<MobilePhone> getFixedPhoneList(){
		ArrayList<MobilePhone> fixedPhoneList = new ArrayList<MobilePhone>();
		if(this.type==Connections.type_org){
			// 这里现在没做组织
			/*if(organizationMini.listMobilePhone!=null&&organizationMini.listMobilePhone.size()>0){
				if(organizationMini.listMobilePhone.get(0)!=null){
					text=organizationMini.listMobilePhone.get(0).mobile;
				}
			}*/
		}
		else{
			if (jtContactMini.getListFixedPhone() != null && jtContactMini.getListFixedPhone().size() > 0) {
				fixedPhoneList =  jtContactMini.getListFixedPhone();
			}
		}
		return fixedPhoneList;
	}
	
	public String getEmail(){
		String text="";
		if(this.type==Connections.type_org){
			if(organizationMini.listEmail!=null&&organizationMini.listEmail.size()>0){
				if(organizationMini.listEmail.get(0)!=null){
					text=organizationMini.listEmail.get(0);
				}
			}
		}else{
			if(jtContactMini.listEmail!=null&&jtContactMini.listEmail.size()>0){
				if(jtContactMini.listEmail.get(0)!=null){
					text=jtContactMini.listEmail.get(0);
				}
			}
		}
		if(StringUtils.isEmpty(text)){
			text="";
		}
		return text;
	}
	
	public String getImage(){
		if(this.type==Connections.type_org && null != organizationMini){
			return organizationMini.mLogo;
		}else if (null != jtContactMini){
			return jtContactMini.getImage();
		}
		return "";
	}
	
	public void setImage(String image) {
		if (type == type_org) {
			organizationMini.mLogo = image;
			organizationMini.setLogo(image);
		} 
		else {
			jtContactMini.setImage(image);
		}
	}
	
	public int getFriendState(){
		if(this.type==Connections.type_org){
			return organizationMini.mFriendState;
		}else{
			return jtContactMini.mFriendState;
		}
	}
	
	public void setFriendState(int state) {
		if (this.type == Connections.type_org) {
			organizationMini.mFriendState = state;
			organizationMini.setFriendState(state);
		} 
		else {
			jtContactMini.mFriendState = state;
			jtContactMini.setFriendState(state);
		}
	}
	
	public int getJoinState(){
		return organizationMini.mJoinState;
	}
	
	public void setJoinState(int state) {
		organizationMini.mJoinState = state;
		organizationMini.setJointState(state);
	}
	
	public void setCharName(char _nameChar){
		if(this.type==Connections.type_org){
			 organizationMini.nameChar=_nameChar;
		}else{
			 jtContactMini.nameChar=_nameChar;
		}
	}
	
	public char getCharName(){
		if(this.type==Connections.type_org){
			if (StringUtils.isEmpty(organizationMini.nameChar+"")) {
				if (!StringUtils.isEmpty(organizationMini.fullName)) {
					String pingYin = PingYinUtil.getPingYin(organizationMini.fullName);
					return pingYin.charAt(0);
				}else if(!StringUtils.isEmpty(organizationMini.shortName)){
					String pingYin = PingYinUtil.getPingYin(organizationMini.shortName);
					return pingYin.charAt(0);
				}
			}
			 return organizationMini.nameChar;
		}
		else{
			if (StringUtils.isEmpty(jtContactMini.nameChar+"")) {
				if (!StringUtils.isEmpty(jtContactMini.getLastName())) {
					String pingYin = PingYinUtil.getPingYin(jtContactMini.getLastName());
					return pingYin.charAt(0);
				}else if(!StringUtils.isEmpty(jtContactMini.getName())){
					String pingYin = PingYinUtil.getPingYin(jtContactMini.getName());
					return pingYin.charAt(0);
				}
			}
			 return jtContactMini.nameChar;
		}
	} 
	
	public String getType() {
		return type+"";
	}

	public void setType(String type) {
		this.type = Integer.parseInt(type);
	}

	public String getSourceFrom() {
		return sourceFrom;
	}

	public void setSourceFrom(String sourceFrom) {
		this.sourceFrom  = sourceFrom;
	}
	
	public boolean isFocuse() {
		return focuse;
	}

	public void setFocuse(boolean focuse) {
		this.focuse = focuse;
	}
	
	public JTContactMini getJtContactMini() {
		return jtContactMini;
	}

	public void setJtContactMini(JTContactMini jtContactMini) {
		if(null != jtContactMini){
			this.jtContactMini = jtContactMini;
		}
	}

	public OrganizationMini getOrganizationMini() {
		return organizationMini;
	}

	public void setOrganizationMini(OrganizationMini organizationMini) {
		this.organizationMini = organizationMini;
	}
	
	public int getSqlDBKeyId() {
		return sqlDBKeyId;
	}

	public void setSqlDBKeyId(int sqlDBKeyId) {
		this.sqlDBKeyId = sqlDBKeyId;
	}
}
