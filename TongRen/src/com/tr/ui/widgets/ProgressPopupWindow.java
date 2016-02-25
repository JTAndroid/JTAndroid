package com.tr.ui.widgets;

import java.util.ArrayList;
import java.util.List;

import com.tr.R;
import com.utils.common.EUtil;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class ProgressPopupWindow extends PopupWindow {

	private final String TAG = getClass().getSimpleName();
	
	private ListView progressLv;
	private TextView anchorTv;
	private View container;
	private Context mContext;
	private ProgressAdapter mAdapter;
	private OnProgressItemSelectListener mListener;
	
	public ProgressPopupWindow(Context context) {
		super(context);
		mContext = context;
		container = LayoutInflater.from(context).inflate(
				R.layout.widget_popup_progress, null);
		container.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dismiss();
			}
			
		});
		progressLv = (ListView) container.findViewById(R.id.progressLv);
		mAdapter = new ProgressAdapter(mContext);
		progressLv.setAdapter(mAdapter);
		progressLv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(mListener != null){
					mListener.onSelect(arg2);
				}
				dismiss();
			}
			
		});
		// 设置SelectPicPopupWindow的View
		setContentView(container);
		// 设置SelectPicPopupWindow弹出窗体的宽
		setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x00000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		setBackgroundDrawable(dw);
    }
	
	public void showAsPullUp(TextView anchor,int selection){
				
		int[] location = new int[2];
		anchor.getLocationOnScreen(location);
		anchorTv = anchor;
		mAdapter.update(selection);
		        
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        container.measure(w, h);
        int height =container.getMeasuredHeight();
        
		super.showAtLocation(anchor, Gravity.TOP, location[0],
				location[1] - height);
    }
	
	public void setOnProgressItemSelectListener(OnProgressItemSelectListener listener){
		mListener = listener;
	}
	
	class ProgressAdapter extends BaseAdapter{

		private int[] mProgressSet;
		private Context mContext;
		private int mProgress;
		
		public ProgressAdapter(Context context){
			mProgressSet = new int[11];
			for (int i = 0; i < mProgressSet.length; i++) {
				mProgressSet[i] = i * 10;
			}
			mProgress = -1;
			mContext = context;
		}
		
		public void update(int progress){
			mProgress = progress;
			notifyDataSetChanged();
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mProgressSet.length;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return mProgressSet[arg0];
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if(arg1 == null){
				arg1 = LayoutInflater.from(mContext).inflate(
						R.layout.list_item_progress, arg2, false);
				holder = new ViewHolder();
				holder.progressTv = (TextView) arg1
						.findViewById(R.id.progressTv);
				arg1.setTag(holder);
			}
			else{
				holder =  (ViewHolder) arg1.getTag();
			}
			if(mProgressSet[arg0] == mProgress){
				holder.progressTv.setBackgroundColor(mContext.getResources().getColor(R.color.commen_text_color_3));
				holder.progressTv.setTextColor(mContext.getResources().getColor(R.color.commen_text_color_4));
			}
			else{
				holder.progressTv.setBackgroundResource(R.drawable.progress_item_bg);
				holder.progressTv.setTextColor(mContext.getResources().getColor(R.color.commen_text_color_1));
			}
			holder.progressTv.setText(mProgressSet[arg0]+"%");
			return arg1;
		}
		
		class ViewHolder{
			TextView progressTv;
		}
	}
	
	public interface OnProgressItemSelectListener{
		public void onSelect(int selection);
	}
}
