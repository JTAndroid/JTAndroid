package com.tr.ui.widgets;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.R;

public class KnowledgeSquareMenuPopupWindow extends PopupWindow {
	public static final String TAG = "KnowledgeSquareMenuPopupWindow";

	private Context mContext;
	private OnMyHomeMenuItemClickListener mItemClickListener;
	/* 显示小号字体 */
	private TextView font_left_tv;
	/* 显示中号字体 */
	private TextView font_center_tv;
	/* 显示大号字体 */
	private TextView font_right_tv;
	/* 保存按钮 */
	private TextView know_save;
	/* 删除按钮 */
	private TextView know_delete;
	/* 编辑按钮 */
	private TextView know_update;
	/* 保存按钮上面的线 */
	private TextView saveline;
	/* 转换为事务按钮 */
	private TextView change2work;
	/* 转换为事务按钮上面的线 */
	private TextView know_change2work_line;
	/* 删除按钮上面的线 */
	private TextView know_delete_line;
	/* 编辑按钮上面的线 */
	private TextView know_update_line;

	private TextView know_collection;

	/* 显示小号背景 */
	public KnowledgeSquareMenuPopupWindow(Context context, int fontsize,
			boolean isShowSave,boolean isCanDelete,boolean isCanUpdate,boolean isShowCollection) {
		mContext = context;

		View root = LayoutInflater.from(mContext).inflate(R.layout.knowledge_square_popuwindow, null);
		font_left_tv = (TextView) root.findViewById(R.id.font_left_tv);
		font_right_tv = (TextView) root.findViewById(R.id.font_right_tv);
		font_center_tv = (TextView) root.findViewById(R.id.font_center_tv);
		saveline = (TextView) root.findViewById(R.id.saveline);
		know_save = (TextView) root.findViewById(R.id.know_save);
		know_delete = (TextView) root.findViewById(R.id.know_delete);
		know_delete_line = (TextView) root.findViewById(R.id.know_delete_line);
		know_update = (TextView) root.findViewById(R.id.know_update);
		know_update_line = (TextView) root.findViewById(R.id.know_update_line);
		change2work = (TextView) root.findViewById(R.id.change2work);
		know_collection = (TextView) root.findViewById(R.id.know_collection);
		know_change2work_line = (TextView) root.findViewById(R.id.know_change2work_line);
		root.setOnClickListener(mClickListener);

		LinearLayout container = (LinearLayout) root
				.findViewById(R.id.know_square_popu);

		for (int i = 0; i < container.getChildCount(); i++) {
			container.getChildAt(i).setOnClickListener(mClickListener);
		}
		font_left_tv.setOnClickListener(mClickListener);
		font_right_tv.setOnClickListener(mClickListener);
		font_center_tv.setOnClickListener(mClickListener);
		know_delete.setOnClickListener(mClickListener);
		if (!isCanDelete) {
			 know_delete.setVisibility(View.GONE);
			 know_delete_line.setVisibility(View.GONE);
		}
		if (!isCanUpdate) {
			know_update.setVisibility(View.GONE);
			know_update_line.setVisibility(View.GONE);
		}
		if (isCanUpdate && isCanDelete) {
			 know_save.setVisibility(View.GONE);
			 saveline.setVisibility(View.GONE);
		}
		if (!isShowCollection) {
			know_collection.setVisibility(View.GONE);
			saveline.setVisibility(View.GONE);
		}
			
		if (!isShowSave) {
			// know_save.setVisibility(View.GONE);
			// saveline.setVisibility(View.GONE);
			change2work.setVisibility(View.GONE);
			know_change2work_line.setVisibility(View.GONE);
			know_save.setTextColor(mContext.getResources().getColor(R.color.knowledge_popuwindow_share_gray));
			know_save.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					Toast.makeText(mContext, "您暂未获得保存该知识的权限", 1).show();
					dismiss();
					return true;
				}
			});
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
			changeFontSize3(fontsize);
		
	}

//	public void setSaveButtonVisibility(int visibility) {
//		if (null != know_save) {
//			know_save.setVisibility(visibility);
//		}
//	}

	/**
	 * 修改网页中的字体大小
	 */
	private void changeFontSize3(int size) {

		switch (size) {
		case 0:
			font_left_tv
					.setBackgroundResource(R.drawable.know_popu_font_bg_checked);
			font_center_tv
					.setBackgroundResource(R.drawable.know_popu_font_bg_default);
			font_right_tv
					.setBackgroundResource(R.drawable.know_popu_font_bg_default);
			font_left_tv.setTextColor(mContext.getResources().getColor(
					R.color.knowledge_popuwindow_font_checked));
			font_center_tv.setTextColor(mContext.getResources().getColor(
					R.color.knowledge_popuwindow_font_default));
			font_right_tv.setTextColor(mContext.getResources().getColor(
					R.color.knowledge_popuwindow_font_default));
			break;
		case 1:
			font_left_tv
					.setBackgroundResource(R.drawable.know_popu_font_bg_default);
			font_center_tv
					.setBackgroundResource(R.drawable.know_popu_font_bg_checked);
			font_right_tv
					.setBackgroundResource(R.drawable.know_popu_font_bg_default);
			font_left_tv.setTextColor(mContext.getResources().getColor(
					R.color.knowledge_popuwindow_font_default));
			font_center_tv.setTextColor(mContext.getResources().getColor(
					R.color.knowledge_popuwindow_font_checked));
			font_right_tv.setTextColor(mContext.getResources().getColor(
					R.color.knowledge_popuwindow_font_default));
			break;
		case 2:
			font_left_tv
					.setBackgroundResource(R.drawable.know_popu_font_bg_default);
			font_center_tv
					.setBackgroundResource(R.drawable.know_popu_font_bg_default);
			font_right_tv
					.setBackgroundResource(R.drawable.know_popu_font_bg_checked);
			font_left_tv.setTextColor(mContext.getResources().getColor(
					R.color.knowledge_popuwindow_font_default));
			font_center_tv.setTextColor(mContext.getResources().getColor(
					R.color.knowledge_popuwindow_font_default));
			font_right_tv.setTextColor(mContext.getResources().getColor(
					R.color.knowledge_popuwindow_font_checked));
			break;
		default:
			break;
		}
	}

	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String MSG = "onClick()";

			if (mItemClickListener == null) {
				return;
			}
			switch (v.getId()) {
			case R.id.know_comment:// 评论
				mItemClickListener.comment();
				break;
			case R.id.know_collection:// 收藏
				mItemClickListener.collection();
				break;
			case R.id.know_save:// 保存
				mItemClickListener.save();
				break;
			case R.id.change2work:// 保存
				mItemClickListener.change2work();
				break;
			case R.id.know_ecologicalDocking:// 对接
				mItemClickListener.ecologicalDocking();
				break;
			case R.id.know_delete:// 删除
				mItemClickListener.delete();
				break;
			case R.id.know_update:// 编辑
				mItemClickListener.update();
				break;
			case R.id.font_left_tv:// 小字体
				Log.i(TAG, MSG + " R.id.font_left_tv");
				mItemClickListener.smallfont();
				break;
			case R.id.font_center_tv:// 中字体
				Log.i(TAG, MSG + " R.id.font_center_tv");
				mItemClickListener.middlefont();
				break;
			case R.id.font_right_tv:// 大字体
				Log.i(TAG, MSG + " R.id.font_right_tv");
				mItemClickListener.bigfont();
				break;
			}
			dismiss();
		}
	};

	/**
	 * 弹出
	 */
	public void showAsDropDown(View anchor) {

		int[] location = new int[7];
		anchor.getLocationOnScreen(location);
		super.showAsDropDown(anchor);
	}

	/**
	 * 设置监听器
	 * 
	 * @param listener
	 */
	public void setOnItemClickListener(OnMyHomeMenuItemClickListener listener) {
		mItemClickListener = listener;
	}

	public interface OnMyHomeMenuItemClickListener {
		public void comment();// 评论

		public void change2work();//转换为事务

		public void collection();// 收藏

		public void save();// 保存

		public void ecologicalDocking();// 对接
		
		public void delete();// 删除
		
		public void update();// 更新

		public void smallfont();// 小字体

		public void middlefont();// 中字体

		public void bigfont();// 大字体
	}
}
