package com.tr.ui.widgets;

import com.tr.R;
import com.tr.model.knowledge.Column;
import com.tr.model.knowledge.UserCategory;
import com.tr.model.obj.Connections;
import com.tr.ui.widgets.KnoCategoryAlertDialog.OperType;
import com.utils.common.EUtil;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 关系列表 删除 操作弹出框
 * @author gintong
 *
 */
public class ConnsListDelDialog extends Dialog implements View.OnClickListener{

	private final String TAG = getClass().getSimpleName();
	
	private LinearLayout parentLl;
//	private TextView addTv; // 添加
//	private TextView modTv; // 修改
	private TextView delTv; // 删除
	
	private Context mContext;
	private OnSelectListener mListener;
	/** 操作的 联系人 */
	private Connections mAttachConnections;
	
	public ConnsListDelDialog(Context context) {
		super(context, R.style.dialog);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.widget_conns_list_del_dialog);
		mContext = context;
		mAttachConnections = new Connections();
		initControls();
	}
	
	private void initControls(){
		parentLl = (LinearLayout) findViewById(R.id.parentLl);
		/*addTv = (TextView) findViewById(R.id.addTv);
		addTv.setOnClickListener(this);
		modTv = (TextView) findViewById(R.id.modTv);
		modTv.setOnClickListener(this);*/
		delTv = (TextView) findViewById(R.id.delTv);
		delTv.setOnClickListener(this);
	}
	
	/**
	 * 设置关联的控件
	 * @param view
	 */
	@SuppressWarnings("deprecation")
	public void setAttachViewAndData(View attachView, Connections connections){
		if(attachView != null){
			
			int notiBarHeight = EUtil.getStatusBarHeight(mContext);
			
			int[] location = new int[2];
			attachView.getLocationOnScreen(location); //  getLocationOnScreen();getLocationInWindow()
			
			int[] size = new int[2];
			attachView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			size[0] = attachView.getMeasuredWidth();
			size[1] = attachView.getMeasuredHeight();
			
			WindowManager manage = ((Activity) mContext).getWindowManager();
			Display display = manage.getDefaultDisplay();
			int [] screenSize =  new int[2];
			screenSize[0] = display.getWidth();
			screenSize[1] = display.getHeight();
			
			/*
			DisplayMetrics dm = new DisplayMetrics();
	        getWindowManager().getDefaultDisplay().getMetrics(dm);
	        */
			
			int[] targetSize = new int[2];
			parentLl.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			targetSize[0] = parentLl.getMeasuredWidth();
			targetSize[1] = parentLl.getMeasuredHeight();
			
			int [] targetLocation = new int[2];
			targetLocation[0] = (screenSize[0] - targetSize[0]) / 2; 
			if((location[1] + size[1] + targetSize[1]) >= screenSize[1]){ // 下方显示不开
				targetLocation[1] = location[1] - targetSize[1];
				parentLl.setBackgroundResource(R.drawable.kno_category_oper_down_bg);
			}
			else{ // 下方可以显示
				targetLocation[1] = location[1] + size[1];
				parentLl.setBackgroundResource(R.drawable.kno_category_oper_up_bg);
			}
			
			// 设置弹出框的位置
			WindowManager.LayoutParams wmlp = getWindow().getAttributes();
			wmlp.gravity = Gravity.TOP | Gravity.LEFT;
			wmlp.x = targetLocation[0]; // x position
			wmlp.y = targetLocation[1] - notiBarHeight;  //y position
			
			Log.d(TAG, location.toString());
			Log.d(TAG, size.toString());
			Log.d(TAG, screenSize.toString());
		}
		if(connections != null){ // 保存目录信息
			mAttachConnections = connections;
		}
	}
	
	/**
	 * 设置操作点击事件
	 * @param listener
	 */
	public void setOnSelectListener(OnSelectListener listener){
		mListener = listener;
	}

	@Override
	public void onClick(View v) {
		if(mListener == null){
			return;
		}
		/*if(v == addTv){ // 添加
			mListener.onSelect(OperType.Create, mAttachCategory);
			this.dismiss();
		}
		else if(v == modTv){ // 修改
			mListener.onSelect(OperType.Modify, mAttachCategory);
			this.dismiss();
		}
		else*/ 
		if(v == delTv){ // 删除
			mListener.onSelect(OperType.Delete, mAttachConnections);
			this.dismiss();
		}
	}
	
	/**
	 * 操作选择监听器
	 * 1-添加;2-修改;3-删除
	 * column-需要编辑的栏目对象
	 * @author gintong
	 */
	public interface OnSelectListener{
		public void onSelect(OperType operType, Connections mAttachConnections);
	}
	
	
}
