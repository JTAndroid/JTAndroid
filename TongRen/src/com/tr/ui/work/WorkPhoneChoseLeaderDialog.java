package com.tr.ui.work;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.R;
import com.tr.model.work.BUAffarMember;
import com.tr.model.work.BUPhone;
import com.tr.ui.work.WorkPhoneChoseAdapter.OnPhoneChangeListenerAdapt;
import com.tr.ui.work.WorkPhoneChoseDialog.OnPhoneChangeListener;

public class WorkPhoneChoseLeaderDialog implements OnPhoneChangeListenerAdapt {
	private AlertDialog mAlterDialog;
	private Activity activity;

	private List<BUAffarMember> mMembers;
	private String mType;
	
	private OnPhoneChangeListener mPhoenChangeListener;

	DisplayImageOptions options = new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.ic_default_avatar)
	.showImageForEmptyUri(R.drawable.ic_default_avatar)
	.showImageOnFail(R.drawable.ic_default_avatar).cacheInMemory(true)
	.cacheOnDisc(true).considerExifParams(false).build();
	
	public WorkPhoneChoseLeaderDialog(Activity activity, List<BUAffarMember> inMember,String inType) {
		this.activity = activity;
		this.mMembers = inMember;
		this.mType=inType;
	}


	
	
	
	public AlertDialog selectPhoneDialog() {
		if (mMembers!=null && mMembers.size()>0)
		{
			BUAffarMember vItem = mMembers.get(0);
			
			LayoutInflater layoutInflater = (LayoutInflater) activity.getLayoutInflater();  

			View mDialogView = layoutInflater.inflate(R.layout.work_phone_chose_cell, null); 
			
			ImageView ImageViewPic = (ImageView) mDialogView
					.findViewById(R.id.ImageViewPic);
			LinearLayout LinearLayoutPhone = (LinearLayout) mDialogView
					.findViewById(R.id.LinearLayoutPhone);
			TextView TextViewName=(TextView) mDialogView.findViewById(R.id.TextViewName);
			
			TextViewName.setText(vItem.name);
			
			if (vItem.getHeadPic()!=null && !(vItem.getHeadPic().equals("")))
			{
				ImageLoader.getInstance().displayImage(vItem.getHeadPic(),
						ImageViewPic, options);
			}
			
			int i;
			
			for (i=0;i<vItem.phones.size();i++)
			{
				LayoutInflater inflater = (LayoutInflater) activity
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				BUPhone vPhone=vItem.phones.get(i);
				LinearLayout vPhoneView = (LinearLayout) inflater.inflate(
						R.layout.work_phone_chose_cell_onephone, null);
				TextView TextViewType=(TextView) vPhoneView.findViewById(R.id.TextViewType);
				TextView TextViewPhone=(TextView) vPhoneView.findViewById(R.id.TextViewPhone);
				TextViewType.setText(vPhone.type);
				TextViewPhone.setText(vPhone.phoneNo);
				
				Button ButtonMsg=(Button) vPhoneView.findViewById(R.id.ButtonMsg);
				ButtonMsg.setTag(vPhone.phoneNo);
				ButtonMsg.setOnClickListener(mMsgClick);
				
				Button ButtonPhone=(Button) vPhoneView.findViewById(R.id.ButtonPhone);
				ButtonPhone.setTag(vPhone.phoneNo);
				ButtonPhone.setOnClickListener(mPhoneClick);
				
				LinearLayoutPhone.addView(vPhoneView);
				
			}
			
			
		    //创建对话框并显示  
			mAlterDialog= new  AlertDialog.Builder(activity).setView(mDialogView).show(); 
			mAlterDialog.setCanceledOnTouchOutside(true);
			return mAlterDialog;
		}
		else
		{
			return null;
		}
		
			
	}


	private OnClickListener mMsgClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.d("xmx", "mLineRelationClick:" + v.getTag());
			
			if (mPhoenChangeListener!=null)
			{
				mPhoenChangeListener.onPhoneChagne("m",v.getTag().toString());
			}
		
		}
	}; 

	private OnClickListener mPhoneClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.d("xmx", "mLineRelationClick:" + v.getTag());
			if (mPhoenChangeListener!=null)
				mPhoenChangeListener.onPhoneChagne("p",v.getTag().toString());
		}
	}; 
	

	public void setPhoneChangeListener(OnPhoneChangeListener dayChangeListener) {
		this.mPhoenChangeListener = dayChangeListener;
	}


	@Override
	public void onPhoneChagne(String outPhone) {
		// TODO Auto-generated method stub
		if (mPhoenChangeListener!=null)
		{
			String vType=outPhone.substring(0,1);
			String vPhone=outPhone.substring(1,outPhone.length());
			mPhoenChangeListener.onPhoneChagne(vType, vPhone);
			
		}
		mAlterDialog.dismiss();
	}
}
