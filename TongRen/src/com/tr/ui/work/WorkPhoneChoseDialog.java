package com.tr.ui.work;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TimePicker.OnTimeChangedListener;

import com.tr.R;
import com.tr.model.work.BUAffarMember;
import com.tr.ui.work.WorkDatePickerDialog.OnDayChangeListener;
import com.tr.ui.work.WorkPhoneChoseAdapter.OnPhoneChangeListenerAdapt;

public class WorkPhoneChoseDialog implements OnPhoneChangeListenerAdapt {
	private AlertDialog mAlterDialog;
	private Activity activity;

	private List<BUAffarMember> mMembers;
	private String mType;
	
	private OnPhoneChangeListener mPhoenChangeListener;

	/**
	 * 日期时间弹出选择框构造函数
	 * 
	 * @param activity
	 *            ：调用的父activity
	 * @param initDateTime
	 *            初始日期时间值，作为弹出窗口的标题和日期时间初始值
	 */
	public WorkPhoneChoseDialog(Activity activity, List<BUAffarMember> inMember,String inType) {
		this.activity = activity;
		this.mMembers = inMember;
		this.mType=inType;
	}


	public OnItemClickListener mClickListener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Log.d("xmx","arg2:"+arg2);
			/*
			if (mPhoenChangeListener!=null)
			{
				BUAffarMember vMember=mMembers.get(arg2);
				mPhoenChangeListener.onPhoneChagne(mType,vMember.channelId);
				mAlterDialog.dismiss();
			}
			*/
			mAlterDialog.dismiss();
		}
	};
	
	
	public AlertDialog selectPhoneDialog() {
		LayoutInflater layoutInflater = (LayoutInflater) activity.getLayoutInflater();  

		View mDialogView = layoutInflater.inflate(R.layout.work_phone_chose_dialog, null); 
		
		
		ListView vListView = (ListView) mDialogView.findViewById(R.id.ListViewPhone);
		vListView.setDividerHeight(0);
		vListView.setOnItemClickListener(mClickListener) ;
		
		WorkPhoneChoseAdapter vAdapter = new WorkPhoneChoseAdapter(activity, this.mMembers, vListView);
		vListView.setAdapter(vAdapter);
		vAdapter.setPhoneChangeAdapListener(this);
		

	    //创建对话框并显示  
		mAlterDialog= new  AlertDialog.Builder(activity).setView(mDialogView).show(); 
		mAlterDialog.setCanceledOnTouchOutside(true);
		return mAlterDialog;
	}



	public void setPhoneChangeListener(OnPhoneChangeListener dayChangeListener) {
		this.mPhoenChangeListener = dayChangeListener;
	}

	public interface OnPhoneChangeListener {
		public void onPhoneChagne(String ouType,String outPhone);
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
