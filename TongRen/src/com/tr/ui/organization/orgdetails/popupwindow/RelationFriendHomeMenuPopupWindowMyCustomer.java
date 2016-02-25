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

public class RelationFriendHomeMenuPopupWindowMyCustomer extends PopupWindow {

	private Context mContext;
	private OnFriendHomeMenuItemClickListenerMyCustomer mItemClickListener;
	
	public RelationFriendHomeMenuPopupWindowMyCustomer(Context context){
		mContext = context;

		View root = LayoutInflater.from(mContext)
				.inflate(R.layout.widget_popup_relation_friend_menu_mycustomer, null);
		
		root.setOnClickListener(mClickListener);
		
		LinearLayout container = (LinearLayout) root.findViewById(R.id.containerLl);
		TextView org_report = (TextView) root.findViewById(R.id.org_friend_report);
		org_seeCustomerTv = (TextView) root.findViewById(R.id.org_seeCustomerTv);
		for (int i = 0; i < container.getChildCount(); i++) {
			container.getChildAt(i).setOnClickListener(mClickListener);
		}
		
		// 设置SelectPicPopupWindow的View
		setContentView(root);
		// 设置SelectPicPopupWindow弹出窗体的宽
		setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		setFocusable(true);
		setAnimationStyle(R.style.PupwindowAnimation);
		// 实例化一个ColorDrawable颜色为半透明
		//ColorDrawable dw = new ColorDrawable(0x44000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		//setBackgroundDrawable(dw);
	}
	
	private OnClickListener mClickListener =new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(mItemClickListener ==null){
				return;
			}
			switch(v.getId()){
			
			case R.id.org_seeCustomerTv:
				
				mItemClickListener.seeCustomer();
				
				break;
			case R.id.org_deletFriendTv:
				mItemClickListener.deletFriend();
				break;
			
			case R.id.org_jointTv:
				mItemClickListener.joint();
				break;
				
			case R.id.org_change2work:
				mItemClickListener.change2work();
				break;
			
			case R.id.org_blackNumTv:
				mItemClickListener.blackNum();
				break;
			case R.id.org_friend_report:
				mItemClickListener.report();
				break;
			}
			dismiss();
		}
	};
	private TextView org_seeCustomerTv;
	
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
	public void setOnItemClickListener(OnFriendHomeMenuItemClickListenerMyCustomer listener){
		mItemClickListener = listener;
	}
	public void ChangeSeeCustomer(){
		org_seeCustomerTv.setText("查看客户");
	}
	public interface OnFriendHomeMenuItemClickListenerMyCustomer{
		public void seeCustomer();//查看客户
		public void deletFriend();//解除好友
		public void joint(); //对接
		public void change2work(); //转换为事务
		public void blackNum(); //黑名单
		public void report(); //举报
	}
}
