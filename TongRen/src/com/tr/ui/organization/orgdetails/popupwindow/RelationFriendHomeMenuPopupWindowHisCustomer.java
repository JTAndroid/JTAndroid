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

public class RelationFriendHomeMenuPopupWindowHisCustomer extends PopupWindow {

	private Context mContext;
	private OnFriendHomeMenuItemClickListenerHisCustomer mItemClickListener;
	
	public RelationFriendHomeMenuPopupWindowHisCustomer(Context context){
		mContext = context;

		View root = LayoutInflater.from(mContext)
				.inflate(R.layout.widget_popup_relation_friend_menu_hiscustomer, null);
		
		root.setOnClickListener(mClickListener);
		org_scaveCustomerTv = (TextView) root.findViewById(R.id.org_scaveCustomerTv);
		LinearLayout container = (LinearLayout) root.findViewById(R.id.containerLl);
		
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
			
			case R.id.org_scaveCustomerTv:
				
				mItemClickListener.scaveCustomer();
				
				break;
				
			case R.id.org_his_deletFriendTv:
				mItemClickListener.deletFriend();
				break;
			
			case R.id.org_his_jointTv:
				mItemClickListener.joint();
				break;
				
			case R.id.org_change2work:
				mItemClickListener.change2work();
				break;
			
				
			case R.id.org_his_blackNumTv:
				mItemClickListener.blackNum();
				break;
			case R.id.org_nofriend_report:
				mItemClickListener.report();
				break;
			}
			dismiss();
		}
	};
	private TextView org_scaveCustomerTv;
	
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
	public void setOnItemClickListener(OnFriendHomeMenuItemClickListenerHisCustomer listener){
		mItemClickListener = listener;
	}
	
	public interface OnFriendHomeMenuItemClickListenerHisCustomer{
		public void scaveCustomer();//存为客户
		public void report(); // 举报
		public void deletFriend();//删除好友
		public void joint(); //对接
		public void change2work(); //组织转换为事务
		public void blackNum(); //黑名单
	}

	public void ChangeSeeCustomer() {
		org_scaveCustomerTv.setText("查看客户");
	}
}
