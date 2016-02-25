package com.tr.ui.widgets;

import com.tr.App;
import com.tr.R;
import com.tr.model.obj.Requirement;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class OverflowReqPopupWindow extends PopupWindow {

	// 修改-0 ， 终止-1，刷新-2，关注-3，转业务需求-4，转项目-5
	
	private Context mContext;
	private LinearLayout editLl;
	private LinearLayout closeLl;
	private LinearLayout refreshLl;
	private LinearLayout followLl;
	private LinearLayout toBReqLl;
	private LinearLayout toProjLl;
	private OnOverflowReqItemClickListener mItemClickListener;
	
	public OverflowReqPopupWindow(Context context){
		mContext = context;
		View root =  LayoutInflater.from(mContext)
				.inflate(R.layout.widget_popup_overflow_req, null);
		root.setOnClickListener(mClickListener);
		editLl = (LinearLayout) root.findViewById(R.id.editLl);
		editLl.setOnClickListener(mClickListener);
		closeLl = (LinearLayout) root.findViewById(R.id.closeLl);
		closeLl.setOnClickListener(mClickListener);
		refreshLl = (LinearLayout) root.findViewById(R.id.refreshLl);
		refreshLl.setOnClickListener(mClickListener);
		followLl = (LinearLayout) root.findViewById(R.id.followLl);
		followLl.setOnClickListener(mClickListener);
		toBReqLl = (LinearLayout) root.findViewById(R.id.toBReqLl);
		toBReqLl.setOnClickListener(mClickListener);
		toProjLl = (LinearLayout) root.findViewById(R.id.toProjLl);
		toProjLl.setOnClickListener(mClickListener);

		// 设置SelectPicPopupWindow的View
		setContentView(root);
		// 设置SelectPicPopupWindow弹出窗体的宽
		setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x77000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		setBackgroundDrawable(dw);
	}
	
	private OnClickListener mClickListener =new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(mItemClickListener ==null){
				return;
			}
			switch(v.getId()){
			case R.id.editLl:
				mItemClickListener.edit();
				break;
			case R.id.closeLl:
				mItemClickListener.close();
				break;
			case R.id.refreshLl:
				mItemClickListener.refresh();
				break;
			case R.id.followLl:
				mItemClickListener.follow();
				break;
			case R.id.toBReqLl:
				mItemClickListener.toBReq();
				break;
			case R.id.toProjLl:
				mItemClickListener.toProj();
				break;
			default:
				break;
			}
			dismiss();
		}
	};
	
	/**
	 * 显示弹出框
	 * @param anchor
	 * @param user_id
	 */
	public void showAsDropDown(View anchor, Requirement req) {
		
		if (req != null) {
			if (req.mUserID.equals(App.getUserID())) { // 自己发布的需求
				followLl.setVisibility(View.GONE);
			}
			else{ // 他人发布的需求（以下功能禁止）
				editLl.setVisibility(View.GONE); // 编辑
				closeLl.setVisibility(View.GONE); // 关闭
				toBReqLl.setVisibility(View.GONE); // 转业务需求
				toProjLl.setVisibility(View.GONE); // 转项目
			}
		}
		int[] location = new int[2];
		anchor.getLocationOnScreen(location);
		super.showAsDropDown(anchor);
	}
	
	/**
	 * 设置监听器
	 * @param listener
	 */
	public void setOnItemClickListener(OnOverflowReqItemClickListener listener) {
		mItemClickListener = listener;
	}
	
	public interface OnOverflowReqItemClickListener{
		public void edit(); // 修改
		public void close(); // 终止此需求
		public void refresh(); // 刷新
		public void follow(); // 关注
		public void toBReq(); // 转为业务需求
		public void toProj(); // 转为项目
	}
}
