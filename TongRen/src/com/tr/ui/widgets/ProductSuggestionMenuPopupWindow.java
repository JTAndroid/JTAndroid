package com.tr.ui.widgets;

import java.util.ArrayList;

import com.tr.R;
import com.tr.ui.demand.NewDemandActivity;
import com.utils.common.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class ProductSuggestionMenuPopupWindow extends PopupWindow {

	private Context mContext;
	private OnMyHomeMenuItemClickListener mItemClickListener;
	private TextView suggestion_style_value;//建议类型值
	private TextView suggestion_style_content;//建议类型值
	
	public ProductSuggestionMenuPopupWindow(Context context , ArrayList<String> suggestionTypeValueList){
		mContext = context;

		View root = LayoutInflater.from(mContext).inflate(R.layout.suggestion_commit_popuwindow, null);
		root.setOnClickListener(mClickListener);
		
		LinearLayout container = (LinearLayout) root.findViewById(R.id.suggestion_style_popu);
		//1.清空所有组件
		container.removeAllViews();
		for (final String suggestionTypeValue : suggestionTypeValueList) {
			
			View convertView = View.inflate(context,R.layout.suggestion_commit_popuwindow_item, null);
			TextView suggestion_style_value = ViewHolder.get(convertView, R.id.suggestion_style_value);
			suggestion_style_value.setText(suggestionTypeValue);
			container.addView(convertView);
			suggestion_style_value.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					suggestion_style_content = (TextView) ((Activity) mContext).findViewById(R.id.suggestion_style_content);
					suggestion_style_content.setText(suggestionTypeValue);
					mItemClickListener.setSuggestionTypeValue();
					dismiss();
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
	}
	
	
	private OnClickListener mClickListener =new OnClickListener(){

		@Override
		public void onClick(View v) {
			if(mItemClickListener ==null){
				return;
			}
			dismiss();
		}
	};
	
	/**
	 * 弹出
	 */
	public void showAsDropDown(View anchor){
		
		int[] location = new int[5];
		anchor.getLocationOnScreen(location);
		super.showAsDropDown(anchor);
	}
	
	/**
	 * 设置监听器
	 * @param listener
	 */
	public void setOnItemClickListener(OnMyHomeMenuItemClickListener listener){
		mItemClickListener = listener;
	}
	
	public interface OnMyHomeMenuItemClickListener{
		public void setSuggestionTypeValue();//产品建议
	}
}
