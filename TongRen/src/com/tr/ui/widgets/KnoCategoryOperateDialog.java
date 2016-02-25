package com.tr.ui.widgets;

import com.tr.R;
import com.tr.model.knowledge.Column;
import com.tr.model.knowledge.UserCategory;
import com.tr.model.knowledge.UserTag;
import com.tr.ui.knowledge.GlobalKnowledgeTagActivity;
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
import android.widget.Toast;

/**
 * 栏目操作弹出框
 * @author gintong
 *
 */
public class KnoCategoryOperateDialog extends Dialog implements View.OnClickListener{

	private final String TAG = getClass().getSimpleName();
	
	private LinearLayout parentLl;
	private TextView addTv; // 添加
	private TextView modTv; // 修改
	private TextView delTv; // 删除
	private TextView delTagTv; // 删除
	
	
	private Context mContext;
	private OnSelectListener mListener;
	private OnDelTagListener mdeDelTagListener;
	private UserCategory mAttachCategory; // 关联目录

	private TextView divider;
	private TextView divider2;

	private UserTag userTag;
	
	public KnoCategoryOperateDialog(Context context, int maxCategory) {
		super(context, R.style.dialog);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.widget_kno_category_oper_dialog);
		mContext = context;
		mAttachCategory = new UserCategory();
		initControls();
	}
	
	public KnoCategoryOperateDialog(Context context,UserTag userTag) {
		super(context, R.style.dialog);
		this.userTag = userTag;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.widget_kno_category_oper_dialog);
		mContext = context;
		mAttachCategory = new UserCategory();
		initControls();
	}
	
	private void initControls(){
		parentLl = (LinearLayout) findViewById(R.id.parentLl);
		divider = (TextView) findViewById(R.id.divider);
		divider2 = (TextView) findViewById(R.id.divider2);
		addTv = (TextView) findViewById(R.id.addTv);
		addTv.setOnClickListener(this);
		modTv = (TextView) findViewById(R.id.modTv);
		modTv.setOnClickListener(this);
		delTv = (TextView) findViewById(R.id.delTv);
		delTv.setOnClickListener(this);
		delTagTv = (TextView) findViewById(R.id.delTagTv);
		delTagTv.setOnClickListener(this);
	}
	
	private void showknoDialog()
	{
		addTv.setVisibility(View.GONE);
		modTv.setVisibility(View.GONE);
		delTv.setVisibility(View.GONE);
		divider.setVisibility(View.GONE);
		divider2.setVisibility(View.GONE);
		delTagTv.setVisibility(View.VISIBLE);
	}
	
	
	/**
	 * 设置关联的控件
	 * @param maxCategory 
	 * @param view
	 */
	@SuppressWarnings("deprecation")
	public void setAttachViewAndCategory(View attachView, UserCategory attachCategory, int maxCategory){
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
		if(attachCategory != null){ // 保存目录信息
			mAttachCategory = attachCategory;
		}
//		//超过5层目录将不能创建子目录
//		if (maxCategory+1 >= 5) {
//			addTv.setVisibility(View.GONE);
//			divider.setVisibility(View.GONE);
//		}else {
//			addTv.setVisibility(View.VISIBLE);
//			divider.setVisibility(View.VISIBLE);
//		}
	}
	/**
	 * 弹出框位置
	 * @param attachView
	 */
	@SuppressWarnings("deprecation")
	public void setAttachViewAndCategory(View attachView){
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
		showknoDialog();
	}
	
	/**
	 * 设置操作点击事件
	 * @param listener
	 */
	public void setOnSelectListener(OnSelectListener listener){
		mListener = listener;
	}
	public void setOnDelTagListener(OnDelTagListener listener){
		mdeDelTagListener = listener;
	}
	

	@Override
	public void onClick(View v) {
		if(mListener == null && mdeDelTagListener == null){
			return;
		}
		if(v == addTv){ // 添加
			mListener.onSelect(OperType.Create, mAttachCategory);
			this.dismiss();
		}
		else if(v == modTv){ // 修改
			mListener.onSelect(OperType.Modify, mAttachCategory);
			this.dismiss();
		}
		else if(v == delTv){ // 删除
			mListener.onSelect(OperType.Delete, mAttachCategory);
			this.dismiss();
		}
		else if(v == delTagTv){ // 删除
			mdeDelTagListener.onDelTag(userTag);
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
		public void onSelect(OperType operType, UserCategory category);
	}
	
	public interface OnDelTagListener{
		public void onDelTag(UserTag userTag);
	}
	
	
}
