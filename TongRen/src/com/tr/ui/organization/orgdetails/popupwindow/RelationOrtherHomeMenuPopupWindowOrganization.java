package com.tr.ui.organization.orgdetails.popupwindow;

import com.tr.R;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class RelationOrtherHomeMenuPopupWindowOrganization extends PopupWindow {

	private Context mContext;
	private OnOrtherHomeMenuItemClickListeners mItemClickListener;
	private TextView org_ReportTv;
	
	public RelationOrtherHomeMenuPopupWindowOrganization(Context context){
		mContext = context;

		View root = LayoutInflater.from(mContext)
				.inflate(R.layout.widget_popup_relation_other_menu_org, null);
		
		root.setOnClickListener(mClickListener);
		org_addFriendTv = (TextView) root.findViewById(R.id.org_addFriendTv);
		org_addBlackTv = (TextView) root.findViewById(R.id.org_blackNumTv);
		lineView1 = root.findViewById(R.id.lineView1);
		lineView2 = root.findViewById(R.id.lineView2);
		org_ReportTv = (TextView) root.findViewById(R.id.org_ReportTv);
		LinearLayout container = (LinearLayout) root.findViewById(R.id.containerLl);
		
		for (int i = 0; i < container.getChildCount(); i++) {
			container.getChildAt(i).setOnClickListener(mClickListener);
		}
		
		// 设置SelectPicPopupWindow的View
		setContentView(root);
		// 设置SelectPicPopupWindow弹出窗体的宽
		setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		setFocusable(true);
		setAnimationStyle(R.style.PupwindowAnimation);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x44000000);
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
			
			case R.id.org_jointTv:
				mItemClickListener.joint();
				break;
			
			case R.id.org_addFriendTv:
				mItemClickListener.addFriend();
				break;
			case R.id.org_blackNumTv:
				mItemClickListener.blackNum();
				break;
			case R.id.org_ReportTv:
				mItemClickListener.ToReport();
				break;
			}
			dismiss();
		}
	};
	private TextView org_addFriendTv;
	private TextView org_addBlackTv;
	private View lineView1;
	private View lineView2;
	
	public void hintAddFriendTv(){
		lineView1.setVisibility(View.GONE);
		org_addFriendTv.setVisibility(View.GONE);
	}
	public void hintAddBlackListTv(){
		lineView2.setVisibility(View.GONE);
		org_addBlackTv.setVisibility(View.GONE);
	}
	
	/**
	 * 弹出
	 */
	public void showAsDropDown(View anchor){
		
		int[] location = new int[2];
		anchor.getLocationOnScreen(location);
		super.showAsDropDown(anchor);
	}
	
	/**
	 * 设置监听器
	 * @param listener
	 */
	public void setOnItemClickListener(OnOrtherHomeMenuItemClickListeners listener){
		mItemClickListener = listener;
	}
	
	public interface OnOrtherHomeMenuItemClickListeners{
		public void joint();
		public void ToReport(); //举报
		public void addFriend(); // 刷新
		public void blackNum(); // 刷新
		
	}
}
