package com.tr.ui.work;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.tr.R;
import com.tr.model.work.BUAffarMember;
import com.tr.ui.work.WorkPhoneChoseDialog.OnPhoneChangeListener;
/**
 * 更多按钮的弹窗
 * @author Administrator
 *
 */
public class WorkNewAffarDealDialog {
	private AlertDialog mAlterDialog;
	private Activity activity;

	private String mType;  //0 创建  1 参与
	private String mStatus;  //0 未完成 1完成
	
	private OnDealChoseListener mDealChoseListener;
	

	
	public WorkNewAffarDealDialog(Activity activity, String inType,String inStatus) {
		this.activity = activity;
		this.mType=inType;
		this.mStatus=inStatus;
	}


	
	public AlertDialog showDialog() {
		LayoutInflater layoutInflater = (LayoutInflater) activity.getLayoutInflater();  

		View mDialogView = layoutInflater.inflate(R.layout.work_new_affar_deal_dialog, null); 
		
		LinearLayout LinearLayoutEdit = (LinearLayout) mDialogView.findViewById(R.id.LinearLayoutEdit);
		LinearLayoutEdit.setOnClickListener(mOnEditClick);
		
		LinearLayout LinearLayoutConfirm = (LinearLayout) mDialogView.findViewById(R.id.LinearLayoutConfirm);
		LinearLayoutConfirm.setOnClickListener(mOnConfirmClick);
		
		LinearLayout LinearLayoutReOpen = (LinearLayout) mDialogView.findViewById(R.id.LinearLayoutReOpen);
		LinearLayoutReOpen.setOnClickListener(mOnReOpenClick);
		
		LinearLayout LinearLayoutDel = (LinearLayout) mDialogView.findViewById(R.id.LinearLayoutDel);
		LinearLayoutDel.setOnClickListener(mOnDelClick);
		
		LinearLayout LinearLayoutOut = (LinearLayout) mDialogView.findViewById(R.id.LinearLayoutOut);
		LinearLayoutOut.setOnClickListener(mOnOutClick);
		
		LinearLayout LinearLayoutReport = (LinearLayout) mDialogView.findViewById(R.id.LinearLayoutReport);
		LinearLayoutReport.setOnClickListener(mOnReportClick);
		
		if (mType.equals("0"))
		{
			LinearLayoutReport.setVisibility(View.GONE);
			//创建
			if (mStatus.equals("0"))
			{
				//未完成
				LinearLayoutReOpen.setVisibility(View.GONE);
				LinearLayoutOut.setVisibility(View.GONE);
			}
			else
			{
				LinearLayoutConfirm.setVisibility(View.GONE);
				LinearLayoutOut.setVisibility(View.GONE);
			}
		}
		else
		{
			//参与
			if (mStatus.equals("0"))
			{
				//未完成
				LinearLayoutEdit.setVisibility(View.GONE);
				LinearLayoutReOpen.setVisibility(View.GONE);
				LinearLayoutDel.setVisibility(View.GONE);
			}
			else
			{
				LinearLayoutEdit.setVisibility(View.GONE);
				LinearLayoutConfirm.setVisibility(View.GONE);
				LinearLayoutDel.setVisibility(View.GONE);
			}
		}
	    //创建对话框并显示  
		mAlterDialog= new  AlertDialog.Builder(activity).setView(mDialogView).show(); 
		mAlterDialog.setCanceledOnTouchOutside(true);

		return mAlterDialog;
	}

	private OnClickListener mOnEditClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.d("xmx", "mOnEditClick:" + v.getTag());
			if (mDealChoseListener!=null)
				mDealChoseListener.onDealChose("e");
			mAlterDialog.dismiss();
		}
	};
	
	private OnClickListener mOnConfirmClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.d("xmx", "mOnConfirmClick:" + v.getTag());
			if (mDealChoseListener!=null)
				mDealChoseListener.onDealChose("f");
			mAlterDialog.dismiss();
		}
	};
	
	private OnClickListener mOnReOpenClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.d("xmx", "mOnReOpenClick:" + v.getTag());
			if (mDealChoseListener!=null)
				mDealChoseListener.onDealChose("r");
			mAlterDialog.dismiss();

		}
	};
	
	private OnClickListener mOnDelClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.d("xmx", "mOnDelClick:" + v.getTag());
			if (mDealChoseListener!=null)
				mDealChoseListener.onDealChose("d");
			mAlterDialog.dismiss();

		}
	};
	
	private OnClickListener mOnOutClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.d("xmx", "mOnOutClick:" + v.getTag());
			if (mDealChoseListener!=null)
				mDealChoseListener.onDealChose("q");
			mAlterDialog.dismiss();

		}
	};
	
	private OnClickListener mOnReportClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.d("xmx", "mOnReportClick:" + v.getTag());
			if (mDealChoseListener!=null)
				mDealChoseListener.onDealChose("c");
			mAlterDialog.dismiss();

		}
	};

	public void setDealSelListener(OnDealChoseListener inDealSelListener) {
		this.mDealChoseListener = inDealSelListener;
	}

	public interface OnDealChoseListener {
		public void onDealChose(String outOperate);
	}
	
	
}
