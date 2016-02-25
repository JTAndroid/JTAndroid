package com.tr.ui.widgets;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.tr.R;
import com.tr.model.knowledge.UserTag;
import com.tr.model.model.PeopleAddress;
import com.tr.model.obj.MobilePhone;
import com.tr.ui.knowledge.CreateKnowledgeActivity;
import com.tr.ui.knowledge.MyKnowledgeActivity;
import com.tr.ui.knowledge.GlobalKnowledgeTagActivity.TagListAdapter.ViewHolder;

public class ConnsCallAndSendSmsDialog extends Dialog {

	private final String TAG = getClass().getSimpleName();
	
	public static final int TYPE_CALL = 1;
	public static final int TYPE_SEND_SMS = 2;
	
	
	// 控件
	private ListView mainLv;
	
	// 变量
	private Activity mContext;
	private int type;
	private ArrayList<MobilePhone> mMobilePhoneList;
	private ArrayList<MobilePhone> mFixedPhoneList;
	private ArrayList<MobilePhone> allMobilePhoneList = new ArrayList<MobilePhone>();
	private MobilePhoneListAdapter mobilePhoneListAdapter;
	
	public ConnsCallAndSendSmsDialog(Activity context, int type, ArrayList<MobilePhone> mobilePhoneList, ArrayList<MobilePhone> fixedPhoneList) {
//		super(context,R.style.ConnsDialogTheme);
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.widget_conns_call_and_sendsms_dialog);
		mContext = context;
		this.type = type;
		if(mobilePhoneList != null){
			mMobilePhoneList = mobilePhoneList;
		}
		else {
			mMobilePhoneList = new ArrayList<MobilePhone>();
		}
		if(fixedPhoneList != null){
			mFixedPhoneList = fixedPhoneList;
		}
		else {
			mFixedPhoneList = new ArrayList<MobilePhone>();
		}
		
//		initDialogStyle();
		initVars();
		initControls();
	}
	
	/*// 初始化对话框样式
	@SuppressWarnings("deprecation")
	private void initDialogStyle(){
		getWindow().setWindowAnimations(R.style.ConnsDialogAnim);
		WindowManager.LayoutParams wmlp = getWindow().getAttributes();
		wmlp.width = mContext.getWindowManager().getDefaultDisplay().getWidth();
		wmlp.gravity = Gravity.CENTER ;
	}
	*/
	// 初始化变量
	private void initVars(){
		
	}
	
	// 初始化控件
	private void initControls(){
		mainLv = (ListView) findViewById(R.id.mainLv);
		if(mMobilePhoneList != null && mMobilePhoneList.size() > 0){
			allMobilePhoneList.addAll(mMobilePhoneList);
		}
		if(mFixedPhoneList != null && mFixedPhoneList.size() > 0){
			allMobilePhoneList.addAll(mFixedPhoneList);
		}
		mobilePhoneListAdapter = new MobilePhoneListAdapter(mContext, allMobilePhoneList);
		mainLv.setAdapter(mobilePhoneListAdapter);
		mainLv.setOnItemClickListener(mOnItemClickListener);
		
	}
	
	
	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			MobilePhoneListAdapter mobilePhoneListAdapter = (MobilePhoneListAdapter) parent.getAdapter();
			MobilePhone mobilePhone = mobilePhoneListAdapter.getItem(position);
			String mobile = mobilePhone.getMobile();
			if( TYPE_CALL == type ){
				Uri uri = Uri.parse("tel:" +mobile);   
				Intent intent = new Intent(Intent.ACTION_DIAL, uri);     
				mContext.startActivity(intent);
			}
			else if (TYPE_SEND_SMS == type ){
				Uri uri = Uri.parse("smsto:" + mobile);     
				Intent it = new Intent(Intent.ACTION_SENDTO, uri);     
//				it.putExtra("sms_body", "The SMS text");     
				mContext.startActivity(it); 
			}
		}
	};
	
	
	class MobilePhoneListAdapter extends BaseAdapter {
		
		private Context mContext;
		private ArrayList<MobilePhone> mobilePhoneList; 
		
		public MobilePhoneListAdapter(Context mContext, ArrayList<MobilePhone> mobilePhoneList) {
			super();
			this.mContext = mContext;
			if(mobilePhoneList != null){
				this.mobilePhoneList = mobilePhoneList;
			}
//			
			else {
				this.mobilePhoneList = new ArrayList<MobilePhone>();
			}
		}

		@Override
		public int getCount() {
			return mobilePhoneList.size();
		}

		@Override
		public MobilePhone getItem(int position) {
			return mobilePhoneList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;

			if (convertView == null) {
				holder = new ViewHolder();

				LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.list_item_coons_call_and_send_sms_dialog_mobile_phone, null);

				holder.keyTv = (TextView) convertView.findViewById(R.id.keyTv);
				holder.valueTv = (TextView) convertView.findViewById(R.id.valueTv);
				
				convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}

			MobilePhone  mobilePhone = mobilePhoneList.get(position);
			if (mobilePhone.getMobile() != null&&!TextUtils.isEmpty(mobilePhone.getMobile())) {
				if(mobilePhone.getName() != null){
					holder.keyTv.setText(mobilePhone.getName());
				}
				holder.valueTv.setText(mobilePhone.getMobile());
			}
			return convertView;
		}

		public final class ViewHolder {
			public TextView keyTv;
			public TextView valueTv;
		}
		
	}
}
