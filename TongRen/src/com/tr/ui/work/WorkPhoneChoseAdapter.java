package com.tr.ui.work;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.R;
import com.tr.model.work.BUAffarLog;
import com.tr.model.work.BUAffarMember;
import com.tr.model.work.BUPhone;
import com.tr.ui.work.WorkLogAdapter.AffairLogItem;
import com.tr.ui.work.WorkPhoneChoseDialog.OnPhoneChangeListener;
/**
 * 事务详情--成员负责人的适配器
 * @author Administrator
 *
 */
public class WorkPhoneChoseAdapter extends BaseAdapter {

	private Activity mParentActivity;
	public List<BUAffarMember> mItemsList = null;
	private ListView mListView;
	public String mSectionName = null;
	public boolean mIsFavour;
	
	private OnPhoneChangeListenerAdapt mPhoenChangeListener;
	
	DisplayImageOptions options = new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.ic_default_avatar)
	.showImageForEmptyUri(R.drawable.ic_default_avatar)
	.showImageOnFail(R.drawable.ic_default_avatar).cacheInMemory(true)
	.cacheOnDisc(true).considerExifParams(false).build();
	

	public WorkPhoneChoseAdapter(Activity activity, List<BUAffarMember> inData,
			ListView inListView) {
		this.mParentActivity = activity;
		this.mItemsList = inData;
		this.mListView = inListView;
	}

	public void setItemList(List<BUAffarMember> inData) {
		this.mItemsList = inData;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (mItemsList != null)
			return mItemsList.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int i;
		AffairPhoneChoseItem vi;
		View infoView = convertView;
		View vPhoneView;
		if (mItemsList != null) {
			BUAffarMember vItem = mItemsList.get(position);
			
			if (infoView==null)
			{
				LayoutInflater inflater = (LayoutInflater) mParentActivity
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				vi = new AffairPhoneChoseItem();
				infoView = (LinearLayout) inflater.inflate(
						R.layout.work_phone_chose_cell, null);
				vi.ImageViewPic = (ImageView) infoView
						.findViewById(R.id.ImageViewPic);
				vi.LinearLayoutPhone = (LinearLayout) infoView
						.findViewById(R.id.LinearLayoutPhone);
				vi.TextViewName=(TextView) infoView.findViewById(R.id.TextViewName);

				infoView.setTag(vi);
			}
			else
			{
				vi=(AffairPhoneChoseItem) infoView.getTag();
			}
			
			vi.TextViewName.setText(vItem.name);
			
			if (vItem.getHeadPic()!=null && !(vItem.getHeadPic().equals("")))
			{
				ImageLoader.getInstance().displayImage(vItem.getHeadPic(),
						vi.ImageViewPic, options);
			}
			
			
			
			vi.LinearLayoutPhone.removeAllViews();
			for (i=0;i<vItem.phones.size();i++)
			{
				LayoutInflater inflater = (LayoutInflater) mParentActivity
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				BUPhone vPhone=vItem.phones.get(i);
				vPhoneView = (LinearLayout) inflater.inflate(
						R.layout.work_phone_chose_cell_onephone, null);
				TextView TextViewType=(TextView) vPhoneView.findViewById(R.id.TextViewType);
				TextView TextViewPhone=(TextView) vPhoneView.findViewById(R.id.TextViewPhone);
				TextViewType.setText(vPhone.type);
				TextViewPhone.setText(vPhone.phoneNo);
				
				Button ButtonMsg=(Button) vPhoneView.findViewById(R.id.ButtonMsg);
				ButtonMsg.setTag("m"+vPhone.phoneNo);
				ButtonMsg.setOnClickListener(mMsgClick);
				
				Button ButtonPhone=(Button) vPhoneView.findViewById(R.id.ButtonPhone);
				ButtonPhone.setTag("p"+vPhone.phoneNo);
				ButtonPhone.setOnClickListener(mPhoneClick);
				
				vi.LinearLayoutPhone.addView(vPhoneView);
				
			}
			
		}
		return infoView;
	}
	//点击短信图标的跳转
	private OnClickListener mMsgClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.d("xmx", "mLineRelationClick:" + v.getTag());
			if (mPhoenChangeListener!=null)
				mPhoenChangeListener.onPhoneChagne(v.getTag().toString());
		
		}
	}; 
	//点击电话图标的跳转
	private OnClickListener mPhoneClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.d("xmx", "mLineRelationClick:" + v.getTag());
			if (mPhoenChangeListener!=null)
				mPhoenChangeListener.onPhoneChagne(v.getTag().toString());
		}
	}; 

	public class AffairPhoneChoseItem {
		public ImageView ImageViewPic;
		public LinearLayout LinearLayoutPhone;
		public TextView TextViewName;

	}
	
	public void setPhoneChangeAdapListener(OnPhoneChangeListenerAdapt dayChangeListener) {
		this.mPhoenChangeListener = dayChangeListener;
	}

	public interface OnPhoneChangeListenerAdapt {
		public void onPhoneChagne(String outPhone);
	}
}
