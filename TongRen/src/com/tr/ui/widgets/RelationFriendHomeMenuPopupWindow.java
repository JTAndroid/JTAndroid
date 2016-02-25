package com.tr.ui.widgets;

import com.tr.R;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class RelationFriendHomeMenuPopupWindow extends PopupWindow {

	private Context mContext;
	private OnFriendHomeMenuItemClickListener mItemClickListener;
	private TextView toReportTv;
	
	public RelationFriendHomeMenuPopupWindow(Context context){
		mContext = context;

		View root = LayoutInflater.from(mContext)
				.inflate(R.layout.widget_popup_relation_friend_menu, null);
		
		root.setOnClickListener(mClickListener);
		toPeopleTv = (TextView) root.findViewById(R.id.toRenMaiTv);
		lookPeopleTv = (TextView) root.findViewById(R.id.lookRenMaiTv);
		
		toReportTv = (TextView) root.findViewById(R.id.toReportTv);
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
			case R.id.jointTv:
				mItemClickListener.joint();
				break;
			case R.id.deletFriendTv:
				mItemClickListener.deletFriend();
				break;
			case R.id.blackNumTv:
				mItemClickListener.blackNum();
				break;
			case R.id.toRenMaiTv:
				mItemClickListener.toRenMai();
				break;
			case R.id.lookRenMaiTv:
				mItemClickListener.lookRenMai();
				break;
			case R.id.toAffairTv:
				mItemClickListener.toAffair();
				break;
			case R.id.toReportTv:
				mItemClickListener.toReport();
			}
			dismiss();
		}
	};
	private TextView lookPeopleTv;
	private TextView toPeopleTv;
	
	/**
	 * 弹出
	 */
	public void showAsDropDown(View anchor){
		
		int[] location = new int[2];
		anchor.getLocationOnScreen(location);
		super.showAsDropDown(anchor);
	}
	//显示转人脉
	public void showToPeopleView(){
		toPeopleTv.setVisibility(View.VISIBLE);
		lookPeopleTv.setVisibility(View.GONE);
		
	}
	//显示查看人脉
	public void showLookPeopleView(){
		toPeopleTv.setVisibility(View.GONE);
		lookPeopleTv.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 设置监听器
	 * @param listener
	 */
	public void setOnItemClickListener(OnFriendHomeMenuItemClickListener listener){
		mItemClickListener = listener;
	}
	
	public interface OnFriendHomeMenuItemClickListener{
		public void joint(); //修改
		public void toReport(); //举报
		public void lookRenMai();//查看人脉
		public void toRenMai();//转人脉
		public void deletFriend(); // 刷新
		public void blackNum(); // 刷新
		public void toAffair(); // 转为事务
	}
}
