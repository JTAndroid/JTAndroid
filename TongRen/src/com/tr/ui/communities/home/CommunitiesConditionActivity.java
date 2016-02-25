package com.tr.ui.communities.home;

import android.app.ActionBar;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;

public class CommunitiesConditionActivity extends JBaseActivity{

	private TelephonyManager mTelephonyManager;
	private String mString;
	private LinearLayout addressbookLl;
	private ImageView addressbookIv;
	private TextView addressbookTv;
	private LinearLayout registrationDateLl;
	private ImageView registrationDateIv;
	private TextView registrationDateTv;
	private LinearLayout integrityLl;
	private ImageView integrityIv;
	private TextView integrityTv;
	@Override
	public void initJabActionBar() {
		ActionBar jabGetActionBar = jabGetActionBar();
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar, "创建社群条件", false, null, true, true);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_communitiescondition);
		initView();
		initData();
	}
	private void initData() {
		testContacts();
	}
	public void testContacts(){ 
		try {
			 Uri uri = Uri.parse("content://com.android.contacts/contacts");  
		        //获得一个ContentResolver数据共享的对象  
		        ContentResolver reslover = this.getContentResolver();  
		        //取得联系人中开始的游标，通过content://com.android.contacts/contacts这个路径获得  
		        Cursor cursor = reslover.query(uri, null, null, null, null);  
		          
		        //上边的所有代码可以由这句话代替：Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);  
		        //Uri.parse("content://com.android.contacts/contacts") == ContactsContract.Contacts.CONTENT_URI  
		          
		        while(cursor.moveToNext()){  
		            //获得联系人ID  
		            String id = cursor.getString(cursor.getColumnIndex(android.provider.ContactsContract.Contacts._ID));    
		            //获得联系人姓名  
		            String name = cursor.getString(cursor.getColumnIndex(android.provider.ContactsContract.Contacts.DISPLAY_NAME));  
		            //获得联系人手机号码  
		            Cursor phone =   reslover.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,   
		                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id, null, null);  
		              
		            StringBuilder sb = new StringBuilder("contactid=").append(id).append(name);  
		            while(phone.moveToNext()){ //取得电话号码(可能存在多个号码)  
		                int phoneFieldColumnIndex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);  
		                String phoneNumber = phone.getString(phoneFieldColumnIndex);  
		                sb.append(phoneNumber+"www");  
		            }  
		            //建立一个Log，使得可以在LogCat视图查看结果  
		            Log.i(TAG, sb.toString());
		            addressbookTv.setText("手机通讯录已开启");
		            addressbookIv.setImageResource(R.drawable.hy_check_pressed);
		        }  
		} catch (Exception e) {
			addressbookLl.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//申请获取联系人权限。目前系统不支持
				}
			});
		}
       
    }  
	private void initView() {
		addressbookLl = (LinearLayout) findViewById(R.id.addressbookLl);
		addressbookIv = (ImageView) findViewById(R.id.addressbookIv);
		addressbookTv = (TextView) findViewById(R.id.addressbookTv);
		registrationDateLl = (LinearLayout) findViewById(R.id.addressbookLl);
		registrationDateIv = (ImageView) findViewById(R.id.registrationDateIv);
		registrationDateTv = (TextView) findViewById(R.id.registrationDateTv);
		integrityLl = (LinearLayout) findViewById(R.id.addressbookLl);
		integrityIv = (ImageView) findViewById(R.id.integrityIv);
		integrityTv = (TextView) findViewById(R.id.integrityTv);
	}
}
