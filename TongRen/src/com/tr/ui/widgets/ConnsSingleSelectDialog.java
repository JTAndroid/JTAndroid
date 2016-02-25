package com.tr.ui.widgets;

import java.util.ArrayList;
import java.util.List;
import com.tr.R;
import com.tr.db.RegionDBManager;
import com.tr.model.obj.JTRegion;
import com.tr.ui.widgets.time.AbstractWheelTextAdapter;
import com.tr.ui.widgets.time.WheelView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * 人脉对话框(性别、民族、国籍、信仰、学历、学位)
 * @author leon
 *
 */
public class ConnsSingleSelectDialog extends Dialog {

	private final String TAG = getClass().getSimpleName();
	
	// 控件
	private WheelView commonWv; // 滚轮
	private TextView cancelTv; // 取消
	private TextView okTv; // 确定
	private TextView tagTv;// 标签
	
	// 变量
	private Activity mContext;
	private CommonAdapter mAdapter;
	private OnFinishListener mListener; 
	private DialogType mDialogType = DialogType.Gender; // 默认性别
	private String [] mData;
	private RegionDBManager mDBManager;
	private String mCurValue;
	private View mView;

	public ConnsSingleSelectDialog(Activity context, View view, String value, DialogType dialogType, OnFinishListener listener) {
		super(context,R.style.ConnsDialogTheme);
		setContentView(R.layout.widget_conns_single_select_dialog);
		mContext = context;
		mView = view;
		mListener = listener;
		mDialogType = dialogType;
		mCurValue = value;
		initDialogStyle();
		initControls();
	}
	
	@SuppressWarnings("deprecation")
	private void initDialogStyle(){
		getWindow().setWindowAnimations(R.style.ConnsDialogAnim);
		WindowManager.LayoutParams wmlp = getWindow().getAttributes();
		wmlp.width = mContext.getWindowManager().getDefaultDisplay().getWidth();
		wmlp.gravity = Gravity.BOTTOM ;
	}
	
	// 初始化控件
	private void initControls(){
		// 取消
		cancelTv = (TextView) findViewById(R.id.cancelTv);
		cancelTv.setOnClickListener(mClickListener);
		// 确定
		okTv = (TextView) findViewById(R.id.okTv);
		okTv.setOnClickListener(mClickListener);
		// 标签
		tagTv = (TextView) findViewById(R.id.tagTv);
		// 滚轮
		commonWv = (WheelView) findViewById(R.id.commonWv);
		commonWv.setVisibleItems(3);
		// 对话框类型
		if(mDialogType == DialogType.Gender){
			tagTv.setText("性别");
			mData = mContext.getResources().getStringArray(R.array.conns_gender);
		}
		else if(mDialogType == DialogType.Ethnic){
			tagTv.setText("民族");
			mData = mContext.getResources().getStringArray(R.array.ethnic);
		}
		else if(mDialogType == DialogType.Nationality){
			tagTv.setText("国籍");
			mDBManager = new RegionDBManager(mContext);
			List<JTRegion> listCountry = new ArrayList<JTRegion>();
			List<JTRegion> listAbroad = mDBManager.query(0 + "");
			listCountry.add(listAbroad.get(0));
			List<JTRegion> listContinent = mDBManager.query(listAbroad.get(1).getId() + "");
			for (JTRegion continent : listContinent) {
				List<JTRegion> tempListCountry = mDBManager.query(continent.getId() + "");
				if(tempListCountry != null){
					listCountry.addAll(tempListCountry);
				}
			}
			mData = new String[listCountry.size()];
			for (int i = 0; i < mData.length; i++) {
				mData[i] = listCountry.get(i).getCname();
			}
		}
		else if(mDialogType == DialogType.Faith){
			tagTv.setText("信仰");
			mData = mContext.getResources().getStringArray(R.array.conns_religious);
		}
		else if(mDialogType == DialogType.Education){
			tagTv.setText("学历");
			mData = mContext.getResources().getStringArray(R.array.conns_education);
		}
		else if(mDialogType == DialogType.Degree){
			tagTv.setText("学位");
			mData = mContext.getResources().getStringArray(R.array.conns_degree);
		}else if(mDialogType == DialogType.studyAbroadType){
			tagTv.setText("是否海外留学");
			mData = mContext.getResources().getStringArray(R.array.conns_studyAbroadType);
		}
		// 设置选中项
		mAdapter = new CommonAdapter(mContext, mData);
		commonWv.setViewAdapter(mAdapter);
		if(mCurValue != null && mCurValue.length() > 0){
			for (int i = 0; i < mData.length; i++) {
				if(mCurValue.equals(mData[i])){
					commonWv.setCurrentItem(i);
					break;
				}
			}
		}
	}
	
	private View.OnClickListener mClickListener = new View.OnClickListener(){

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.cancelTv: // 取消
				ConnsSingleSelectDialog.this.dismiss();
				break;
			case R.id.okTv: // 确定
				mCurValue = mAdapter.getItemText(commonWv.getCurrentItem()) + "";
				if(mListener != null){
					mListener.onFinish(mView, mCurValue);
				}
				ConnsSingleSelectDialog.this.dismiss();
				break;
			}
		}
	};
	
	private class CommonAdapter extends AbstractWheelTextAdapter {

		private String [] data;
		
		protected CommonAdapter(Context context,String [] data) {
			super(context, R.layout.widget_wheel_item, R.id.itemTv);
			this.data = data;
		}

		@Override
		public int getItemsCount() {
			return data.length;
		}

		@Override
		protected CharSequence getItemText(int position) {
			return data[position];
		}
	}
	
	public interface OnFinishListener{
		public void onFinish(View view, String value);
	}

	public enum DialogType{
		Gender, // 性别
		Ethnic, // 民族
		Nationality, // 国籍
		Faith, // 信仰
		Education, // 学历
		Degree, // 学位
		studyAbroadType // 学位
	}
}
