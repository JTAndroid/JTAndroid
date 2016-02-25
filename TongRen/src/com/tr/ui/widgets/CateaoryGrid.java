package com.tr.ui.widgets;

import java.util.ArrayList;

import com.tr.R;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This class is inherit from GridView, overriding onMeasure method to 
 * remove scroll function in this gridview , and to avoid only one row 
 * displayed when embedded in a ScrollView control.
 * 
* @author yingjun.bai@renren-inc.com
* @date 2012-8-27 涓婂崍11:03:26
 */
public class CateaoryGrid extends GridView {
	
	public boolean mIsSingleSelection=false;
	public CateaoryGrid(Context context) {
		super(context);
	}

	public CateaoryGrid(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override  
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
	    int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  
	            MeasureSpec.AT_MOST);  
	    super.onMeasure(widthMeasureSpec, expandSpec);  
	  
	}  
	
	// 绂佹Grid婊戝姩
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(ev.getAction() == MotionEvent.ACTION_MOVE)
			return true;
		return super.dispatchTouchEvent(ev);
	}
	/** 选人用的 */
	public static class ImageAdapter extends BaseAdapter {
		private int headInOneRow;
		private int allItemCount;
		private int dataSize;
		private ArrayList<HeadName> data;
		Context mContext=null;
		boolean removeState = false;
		
		public void setRemoveState(boolean removeState){
			this.removeState=removeState;
		}
		public boolean getRemoveState(){
			return removeState;
		}
		
//		private void addBlankItem() {
//			data.add(new HeadName(R.drawable.im_add_persion, null));
//		}

		private int calculateAllDataCount() {
			return headInOneRow * ((dataSize + headInOneRow) / headInOneRow);
		}
		
		public int getAddFriendPosition(){
			return dataSize;
		}

		public int getSize() {
			return dataSize;
		}

		public ImageAdapter(Context c, ArrayList<HeadName> data,
				int headInOneRow) {
			mContext = c;
			this.headInOneRow = headInOneRow;
			this.dataSize = data.size();
			this.data = data;
		}

		public int getCount() {
			return data.size();
		}

		public Object getItem(int position) {
			return data.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.im_editmumber_grid_item, null);
			}
//			ImageView imageView = (ImageView) convertView
//					.findViewById(R.id.grid_item_image);
//			TextView textView = (TextView) convertView
//					.findViewById(R.id.grid_item_label);
//			View deleteImg = convertView.findViewById(R.id.deleteLayout);
//
//			HeadName item = data.get(position);
//			imageView.setAdjustViewBounds(false);
//			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//			imageView.setImageResource(item.getHeadId());
//
//			if (position <= dataSize) {
//
//				imageView.setVisibility(View.VISIBLE);
//				textView.setVisibility(View.VISIBLE);
//				textView.setText(item.getName());
//				if (removeState) {
//					// Display delete icon
//					deleteImg.setVisibility(View.VISIBLE);
//
//					// remove add friend icon from the gridview
//					if (item.getHeadId() == R.drawable.im_add_persion) {
//						imageView.setVisibility(View.INVISIBLE);
//						textView.setVisibility(View.INVISIBLE);
//						deleteImg.setVisibility(View.INVISIBLE);
//					}
//				} else {
//					deleteImg.setVisibility(View.INVISIBLE);
//					convertView.setVisibility(View.VISIBLE);
//				}
//			} else {
//				imageView.setVisibility(View.INVISIBLE);
//				textView.setVisibility(View.INVISIBLE);
//				deleteImg.setVisibility(View.INVISIBLE);
//			}
			return convertView;
		}
	}

	public static class HeadName {
		int headId;
		String name;
		public HeadName(int id, String name) {
			headId = id;
			this.name = name;
		}
		public int getHeadId() {
			return headId;
		}
		public String getName() {
			return name;
		}
	}
	
	
}
