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

public class EditOrDeletePopupWindow extends PopupWindow {

	private Context mContext;
	private OnMeetingOptItemClickListener mItemClickListener;
	private View root;
	private TextView deleteTv;
	private TextView editTv;
	private String editStr;
	private String deleteStr;
	public EditOrDeletePopupWindow(Context context) {
		mContext = context;
		root = LayoutInflater.from(mContext).inflate(R.layout.meeting_edit_or_delete_pop, null);
		root.setOnClickListener(mClickListener);
		LinearLayout container = (LinearLayout) root.findViewById(R.id.containerLl);
		
		deleteTv = (TextView) root.findViewById(R.id.meetingDeleteTv);
		editTv = (TextView) root.findViewById(R.id.meetingEditTv);

		for (int i = 0; i < container.getChildCount(); i++) {
			container.getChildAt(i).setOnClickListener(mClickListener);
		}
		setContentView(root);
		popWindowConfig();
	}

	private void popWindowConfig() {
		// 设置SelectPicPopupWindow的View
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

	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (mItemClickListener == null) {
				return;
			}
			switch (v.getId()) {
			case R.id.meetingEditTv:
				mItemClickListener.edit(editStr);
				break;
			case R.id.meetingDeleteTv:
				mItemClickListener.delete(deleteStr);
				break;
			}
			dismiss();
		}
	};
	/**
	 * 只显示编辑按钮
	 */
	public void hideDeleteButton(String showEditStr){
		this.editStr = showEditStr;
		deleteTv.setVisibility(View.GONE);
		editTv.setVisibility(View.VISIBLE);
		editTv.setText(showEditStr);
		root.findViewById(R.id.meetingView).setVisibility(View.GONE);
	}
	/**
	 * 显示所有的按钮:编辑和删除
	 */
	public void showAllButton(String showEditStr ,String showDeleteStr) {
		this.editStr = showEditStr;
		this.deleteStr = showDeleteStr;
		deleteTv.setVisibility(View.VISIBLE);
		deleteTv.setText(showDeleteStr);
		root.findViewById(R.id.meetingView).setVisibility(View.VISIBLE);
		editTv.setVisibility(View.VISIBLE);
		editTv.setText(showEditStr);
	}
	/**
	 * 只显示删除按钮
	 */
	public void showDeleteButton() {
		deleteTv.setVisibility(View.VISIBLE);
		editTv.setVisibility(View.GONE);
		root.findViewById(R.id.meetingView).setVisibility(View.GONE);
	}
	
	/**
	 * 弹出
	 */
	public void showAsDropDown(View anchor) {
		int[] location = new int[2];
		anchor.getLocationOnScreen(location);
		super.showAsDropDown(anchor);
	}
	/**
	 * 设置监听器
	 * 
	 * @param listener
	 */
	public void setOnItemClickListener(OnMeetingOptItemClickListener listener) {
		mItemClickListener = listener;
	}

	public void setBTText(String edittext, String deltext){
		editTv.setText(edittext);
		deleteTv.setText(deltext);
	}
	
	public interface OnMeetingOptItemClickListener {
		public void edit(String editStr); // 编辑
		public void delete(String deleteStr); // 删除
	}
}
