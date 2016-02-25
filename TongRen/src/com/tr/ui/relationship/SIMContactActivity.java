package com.tr.ui.relationship;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.view.View;
import android.widget.TextView;

import com.tr.R;
import com.tr.api.ConnectionsReqUtil;
import com.tr.model.obj.Connections;
import com.tr.model.obj.MobilePhone;
import com.tr.model.obj.PersonInfo;
import com.tr.navigate.ENavigate;
import com.tr.service.GetConnectionsListService;
import com.tr.service.GetConnectionsListService.RequestType;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.frg.FrgConnections2;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;

public class SIMContactActivity extends JBaseFragmentActivity {
	
	/** 邀请 通过通讯录 为参数 选出可以邀请或加好友的人  */
	public static final String TYPE_INVITE = "type_invite";
	
	/** 同步通讯录并把通讯录的人直接加为人脉  */
	public static final String TYPE_ADD_CONTACTS = "type_add_contacts";
	
	
	/** 功能类型 */
	private String type ;
	
	/** Called when the activity is first created. */
	ArrayList<Consact> consactArray=new ArrayList<Consact>();
	/** 上传按钮 */
	private TextView uploadingButton;

	private Intent intent;
	private Activity mActivity;
	
	private Activity getActivity(){
		return mActivity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simcontact);
		mActivity = this;
		
		intent = getIntent();
		type = intent.getStringExtra("type");
		
		
		uploadingButton = (TextView) findViewById(R.id.btn_ok);
		
		uploadingButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showLoadingDialog("正在上传");
				uploadingButton.setText("正在导入通讯录");
				uploadingButton.setBackground(getResources().getDrawable(R.drawable.sign_in_normal));
				uploadingButton.setEnabled(false);
				new Thread(ReadPhone).start();
				
//				final ProgressDialog mProgressDialog=new ProgressDialog(SIMContactActivity.this);
//		            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//		                @Override
//		                public void onCancel(DialogInterface dialogInterface) {
//		                    KeelLog.d(TAG, "mProgressDialog.onCancel");
//		                    //JBaseActivity.this.finish();
//		                    mProgressDialog.cancel();
//		                }
//		            });

		      //  mProgressDialog.setMessage("正在同步");
		       // mProgressDialog.show();
			}
		});
	}
	
	
	public JSONObject getSimContacts(){
		// 获得所有的联系人
		Cursor cur = getContentResolver().query(  
                ContactsContract.Contacts.CONTENT_URI,  
                null,  
                null,  
                null,  
                ContactsContract.Contacts.DISPLAY_NAME  
                        + " COLLATE LOCALIZED ASC"); 		
				// 循环遍历
				if (cur.moveToFirst()) {
					int idColumn = cur.getColumnIndex(ContactsContract.Contacts._ID);  
		            int displayNameColumn = cur  
		                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);  
		            int commentNote =  cur.getColumnIndex(Note.NOTE);
					do {
						Consact consact=new Consact();
						String contactId = cur.getString(idColumn);  
						String disPlayName = cur.getString(displayNameColumn);
						consact.name=disPlayName;
						
						//add by zhongshan get comment备注
						if (commentNote>=0) {
							String comment = cur.getString(commentNote); 
							consact.comment = comment;
						}
						
						//Log.v("SIMContactActivity", disPlayName);
						int phoneCount = cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
						if (phoneCount > 0) {  
		                    // 获得联系人的电话号码  
		                    Cursor phones = getContentResolver().query(  
		                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,  
		                            null,  
		                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID  
		                                    + " = " + contactId, null, null);  
		                    if (phones.moveToFirst()) {  
		                    	String phoneNumber=null;
		                    	
		                    	//add by zhongsham
		                    	/**电话类型*/
		                    	String phoneName=null;
		                    	
		                    	
		                    	//各种电话
		                 		 Cursor phone = getPhoneByType(contactId,
		                 		 ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
		                 		 while (phone.moveToNext()) {
		                 			phoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace(" ", "").replace("-", "");
		                 			consact.phones.add(phoneNumber);
		                 			MobilePhone mobilePhone = new MobilePhone();
		                 			mobilePhone.setMobile(phoneNumber);
		                 			mobilePhone.setName("手机");
		                 			consact.mobilePhones.add(mobilePhone);
		                 		 }
		                 		phone.close();
		                 		phone = getPhoneByType(contactId,
				                ContactsContract.CommonDataKinds.Phone.TYPE_HOME);
				                 while (phone.moveToNext()) {
				                 	phoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace(" ", "").replace("-", "");
				                 	consact.phones.add(phoneNumber);
				                 	MobilePhone mobilePhone = new MobilePhone();
				                 	mobilePhone.setMobile(phoneNumber);
		                 			mobilePhone.setName("固话");
		                 			consact.mobilePhones.add(mobilePhone);
				                 }
				                 phone.close();
				                 phone = getPhoneByType(contactId,
							     ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE);
								while (phone.moveToNext()) {
									phoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace(" ", "").replace("-", "");
									consact.phones.add(phoneNumber);
									MobilePhone mobilePhone = new MobilePhone();
									mobilePhone.setMobile(phoneNumber);
									mobilePhone.setName("单位手机");
									consact.mobilePhones.add(mobilePhone);
								}
							     phone.close();
							     phone = getPhoneByType(contactId,
										                ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
								while (phone.moveToNext()) {
									phoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace(" ", "").replace("-", "");
									consact.phones.add(phoneNumber);
									MobilePhone mobilePhone = new MobilePhone();
									mobilePhone.setMobile(phoneNumber);
									mobilePhone.setName("单位");
									consact.mobilePhones.add(mobilePhone);
								}
								phone.close();
								phone = getPhoneByType(contactId,
										ContactsContract.CommonDataKinds.Phone.TYPE_OTHER);
								while (phone.moveToNext()) {
									phoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace(" ", "").replace("-", "");
									consact.phones.add(phoneNumber);
									MobilePhone mobilePhone = new MobilePhone();
									mobilePhone.setMobile(phoneNumber);
									mobilePhone.setName("其他");
									consact.mobilePhones.add(mobilePhone);
								}
								phone.close();
								//文件名
//								Cursor nameCur=getContentResolver().query(ContactsContract.Data.CONTENT_URI,
//										   new String[] { StructuredName.FAMILY_NAME, StructuredName.GIVEN_NAME},
//										    ContactsContract.Data.CONTACT_ID + "=?",
//										   new String[]{contactId}, null);
//								
//								if(nameCur.moveToFirst()){
//									consact.familyName=nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
//									consact.givenName=nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
//							     }
//								nameCur.close();
		                    }
		                    phones.close();
						}
						
						
						 Cursor emails = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId,null, null);  
				            while (emails.moveToNext())   
				            {                   
				                String emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));  
				               // String emailType = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));      

				               // Log.d(TAG,"testNum="+ emailAddress + "type:"+emailType);
				                consact.emails.add(emailAddress);
				            }      
				            emails.close();
						consactArray.add(consact);
					} while (cur.moveToNext());
					JSONObject tempJsonObj=new JSONObject();
					JSONArray js=new JSONArray();
					for(Consact consact: consactArray){
						tempJsonObj=consact.toJson();
						if(tempJsonObj!=null){
							js.put(tempJsonObj);
						}
					}
					consactArray.clear();
					try {
						JSONObject jsonObj=new JSONObject();
						jsonObj.put(EAPIConsts.JsonKey.CONTACT_listPhoneBookItem, js);
						return jsonObj;
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						hander.sendEmptyMessage(uploaderr);
						e.printStackTrace();
					}
				}
				return null;
	}
	
	
	 private Cursor getPhoneByType(String contactId, int type) {
		  Cursor phone = SIMContactActivity.this.getContentResolver().query(
		    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
		    null,
		    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
		      + contactId + " AND "
		      + ContactsContract.CommonDataKinds.Phone.TYPE + "="
		      + type, null, null);
		  return phone;
   }
	 public final int uploadOk=0;
	 public final int uploaderr=1;
	 public final int simbookempty=2;
	// public final int closeDialog=0;
	 Handler hander=new Handler(){
		 public void handleMessage(Message msg) {   
             switch (msg.what) {   
                  case uploadOk:   
                	  dismissLoadingDialog();
                	  showToast("同步成功");
                       break; 
                  case uploaderr:   
                	  dismissLoadingDialog();
                	  showToast("同步失败");
                       break; 
                  case simbookempty:   
                	  dismissLoadingDialog();
                	  showToast("本地电话本为空");
                       break; 
             }   
             super.handleMessage(msg);   
        }   
	 };

	 Runnable ReadPhone=new Runnable() {
		@Override
		public void run() {
			JSONObject json= getSimContacts();
			if(json==null){
				hander.sendEmptyMessage(simbookempty);
			}
			else{
				
				if(TYPE_INVITE.equals(type)){
					ConnectionsReqUtil.doCheckMobiles(SIMContactActivity.this, ib, json, hander);
				}
				else if(TYPE_ADD_CONTACTS.equals(type)){
					ConnectionsReqUtil.uploadSim(SIMContactActivity.this, ib, json, hander);
				}

				
			}
		}
	};
	
	IBindData ib=new IBindData() {
		@Override
		public void bindData(int tag, Object object) {
			
			if (object==null) {
				showLongToast(SIMContactActivity.this.getString(R.string.neterr_sync));
				return;
			}
			
			if(tag==EAPIConsts.concReqType.im_upphonebook){
				//hander.sendEmptyMessage(uploadOk);
//				if(object==null){
//					showLongToast(SIMContactActivity.this.getString(R.string.neterr_sync));
//				}else{
					String str=(String)object;
					String netokstr=SIMContactActivity.this.getString(R.string.net_return_ok);
					if(str.equals(netokstr)){
						//dismissLoadingDialog();
						//showLongToast(SIMContactActivity.this.getString(R.string.netok_sync));
						showLongToast("同步成功，正在更新金桐关系列表");
//						startGetConnections();
						GetConnectionsListService.startGetConnectionsListService(getActivity(), RequestType.FriendAll);
						finish();
						return ;
					}else{
						showLongToast(SIMContactActivity.this.getString(R.string.neterr_sync));
					}
//				}
				dismissLoadingDialog();
				finish();
			}
			else if(tag==EAPIConsts.concReqType.CONNECTIONSLIST){
				dismissLoadingDialog();
				if(object!=null){
					FrgConnections2.contactAdapter.dataChange();
					showLongToast("更新金桐关系列表完成");
				}else{
					showToast("更新金桐关系列表失败");
				}
			}
			// 新上传电话本获得推荐人脉列表 
			else if(tag==EAPIConsts.concReqType.CheckMobiles){
				dismissLoadingDialog();
				if(object!=null){
					HashMap<String, Object> dataMap = (HashMap<String, Object>) object;
//					boolean sucess = (Boolean) dataMap.get("sucess");
					ArrayList<Connections> userConnectionsList = (ArrayList<Connections>) dataMap.get("listOnLine");
					ArrayList<Connections> unUserConnectionsList = (ArrayList<Connections>) dataMap.get("listOffLine");
					ENavigate.startPushPeople2Activity(SIMContactActivity.this, userConnectionsList, unUserConnectionsList);
					showLongToast("新上传电话本获得推荐人脉列表 完成");
					finish();
				}
				else {
					showToast("新上传电话本获得推荐人脉列表 失败");
				}
			}
		}
	};
	
	private void startGetConnections(){
		JSONObject jb=new JSONObject();
		try {
			jb.put("type", "0");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ConnectionsReqUtil.doGetConnectionsList(this, ib, jb, null);
	}
	
	
	public class Consact{
		//modified by zhongshan 
		/**转人脉 邀请和转为人脉中listMobilePhone不同邀请为phones转人脉为mobilePhone*/
		public ArrayList<MobilePhone> mobilePhones=new ArrayList<MobilePhone>();
		/**转人脉 附加PersonInfo个人信息对象集合*/
		public ArrayList<PersonInfo> listPersonInfos=new ArrayList<PersonInfo>();
		/**转人脉 附加备注*/
		public String comment=null;
		public ArrayList<String> phones=new ArrayList<String>();
		public ArrayList<String> emails=new ArrayList<String>();
		public String name=null;
		public String familyName=null;
		public String givenName=null;
		
			
		
		public ArrayList<PersonInfo> getListPersonInfos() {
			return listPersonInfos;
		}

		public String getComment() {
			return comment;
		}

		public ArrayList<MobilePhone> getMobilePhones() {
			return mobilePhones;
		}
		
		public String getFamilyName(){
			if(StringUtils.isEmpty(familyName)){
				return null;
			}else{
				return familyName;
			}
		}
		public String getGivenName(){
			if(StringUtils.isEmpty(givenName)){
				return name;
			}else{
				return givenName;
			}
		}
		public boolean ishasFamilyName(){
			if(!StringUtils.isEmpty(givenName)){
				return true;
			}else{
				return false;
			}
		}
		
		public boolean isValid(){
			if(StringUtils.isEmpty(name)){
				return false;
			}else{
				return true;
			}
		}
		
		public JSONObject toJson(){
			JSONObject jsonobj=new JSONObject();
			if(!isValid()){
				return jsonobj;
			}else{
				try {
					jsonobj.put(EAPIConsts.JsonKey.CONTACT_givenName, getGivenName());
					if(ishasFamilyName()){
						jsonobj.put(EAPIConsts.JsonKey.CONTACT_familyName, getFamilyName());
					}
					if(phones.size()>0){
						//modified by zhongshan 
						//区别邀请和转人脉电话号码的封装格式
						/**邀请*/
						JSONArray ja=new JSONArray();
						if (TYPE_INVITE.equals(type)) {
							for(String data:phones){
								ja.put(data);
							}
						}
						/**转人脉*/
						if (TYPE_ADD_CONTACTS.equals(type)) {
							JSONArray mobilePhonesja=new JSONArray();
							for(MobilePhone mobilePhone:mobilePhones){
								JSONObject jsonObjectMobilePhone = mobilePhone.toJSONObject();
								ja.put(jsonObjectMobilePhone);
							}
						}
						jsonobj.put(EAPIConsts.JsonKey.CONTACT_listMobilePhone, ja);
					}
					if(emails.size()>0){
						JSONArray ja=new JSONArray();
						for(String data:emails){
							ja.put(data);
						}
						jsonobj.put(EAPIConsts.JsonKey.CONTACT_listEmail, ja);
					}
					
					//add by zhongshan
					//如果是转为人脉:将comment，listPersonInfos封装到json
					if (TYPE_ADD_CONTACTS.equals(type)) {
						jsonobj.put(EAPIConsts.JsonKey.CONTACT_comment, getComment());
						jsonobj.put(EAPIConsts.JsonKey.CONTACT_listPersonInfo, getListPersonInfos());//手机通讯录中没有喜好
					}
					
					return jsonobj;
				} catch (JSONException e) {
					hander.sendEmptyMessage(uploaderr);
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}	
		}
	}



	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this,  getActionBar(), "金桐", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}
}