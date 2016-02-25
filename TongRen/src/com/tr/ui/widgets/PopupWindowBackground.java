package com.tr.ui.widgets;

import java.util.ArrayList;
import java.util.List;

import com.tr.R;
import com.tr.model.obj.EduExperience;
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

public class PopupWindowBackground extends PopupWindow {

	private final String TAG = getClass().getSimpleName();
	public final static int userType_showtitle=1;
	
	private ListView progressLv;
	private View container;
	private Context mContext;
	private ProgressAdapter mAdapter;
	private String datas[];
	public int type=0;
	public int useType=0;
	
	public PopupWindowBackground(Context context) {
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
	
	public void setData(String indata[],int usertype){
		datas=indata;
		this.useType=usertype;
		mAdapter = new ProgressAdapter(mContext);
		progressLv.setAdapter(mAdapter);
	}
	
	
	public void setListener(OnItemClickListener on){
		progressLv.setOnItemClickListener(on);
	}
	
	public void showAsPullUp(View anchor){
				
		int[] location = new int[2];
		anchor.getLocationOnScreen(location);
		//anchorTv = anchor;
		        
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        container.measure(w, h);
        int height =container.getMeasuredHeight();
        
		super.showAtLocation(anchor, Gravity.TOP, location[0],
				location[1] - height);
    }
	
	public static class BackgroudObj{
		public int id;
		public String name;
	}
	
	class ProgressAdapter extends BaseAdapter{

		private List<BackgroudObj> mProgress;
		private Context mContext;
		
		public ProgressAdapter(Context context){
			mContext = context;
			mProgress=new ArrayList<BackgroudObj>();
			for(int i=0;i<datas.length;i++){
				BackgroudObj bj=new BackgroudObj();
				bj.id=i;
				bj.name=datas[i];
				mProgress.add(bj);
			}
		}
		
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(useType== userType_showtitle){
				return mProgress.size()-1;
			}
			return mProgress.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return mProgress.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			BackgroudObj bj=mProgress.get(arg0);
			// TODO Auto-generated method stub
			if(arg1 == null){
				arg1 = LayoutInflater.from(mContext).inflate(
						R.layout.list_item_progress, arg2, false);
				 ((TextView) arg1.findViewById(R.id.progressTv)).setText(bj.name);
				 arg1.setTag(bj);
			}else{
				 ((TextView) arg1.findViewById(R.id.progressTv)).setText(bj.name);
				 arg1.setTag(bj);
			}
			return arg1;
		}
		
	}
}
