package com.tongmeng.alliance.view;

import java.lang.reflect.Field;

import com.tongmeng.alliance.view.MyCatalogDialog.OperType;
import com.tr.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyCatalogDialog1 extends Dialog implements
		android.view.View.OnClickListener {
	private final String TAG = getClass().getSimpleName();
	private Context mContext;

	private LinearLayout parentLl;
	private TextView addTv; // 添加
	private TextView modTv; // 修改
	private TextView delTv; // 删除
	private TextView delTagTv; // 删除

	private TextView divider;
	private TextView divider2;

	private OnSelectListener mListener;
	private OnDelTagListener mdeDelTagListener;

	public MyCatalogDialog1(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.widget_kno_category_oper_dialog);
		this.mContext = context;
		initControls();
	}

	private void initControls() {
		// TODO Auto-generated method stub
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

	public void setDialogLocation(View attachView) {
		if (attachView != null) {

			int notiBarHeight = getStatusBarHeight(mContext);

			int[] location = new int[2];
			attachView.getLocationOnScreen(location); // getLocationOnScreen();getLocationInWindow()

			int[] size = new int[2];
			attachView
					.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			size[0] = attachView.getMeasuredWidth();
			size[1] = attachView.getMeasuredHeight();

			WindowManager manage = ((Activity) mContext).getWindowManager();
			Display display = manage.getDefaultDisplay();
			int[] screenSize = new int[2];
			screenSize[0] = display.getWidth();
			screenSize[1] = display.getHeight();

			/*
			 * DisplayMetrics dm = new DisplayMetrics();
			 * getWindowManager().getDefaultDisplay().getMetrics(dm);
			 */

			int[] targetSize = new int[2];
			parentLl.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			targetSize[0] = parentLl.getMeasuredWidth();
			targetSize[1] = parentLl.getMeasuredHeight();

			int[] targetLocation = new int[2];
			targetLocation[0] = (screenSize[0] - targetSize[0]) / 2;
			if ((location[1] + size[1] + targetSize[1]) >= screenSize[1]) { // 下方显示不开
				targetLocation[1] = location[1] - targetSize[1];
				parentLl.setBackgroundResource(R.drawable.kno_category_oper_down_bg);
			} else { // 下方可以显示
				targetLocation[1] = location[1] + size[1];
				parentLl.setBackgroundResource(R.drawable.kno_category_oper_up_bg);
			}

			// 设置弹出框的位置
			WindowManager.LayoutParams wmlp = getWindow().getAttributes();
			wmlp.gravity = Gravity.TOP | Gravity.LEFT;
			wmlp.x = targetLocation[0]; // x position
			wmlp.y = targetLocation[1] - notiBarHeight; // y position

			Log.d(TAG, location.toString());
			Log.d(TAG, size.toString());
			Log.d(TAG, screenSize.toString());
		}
	}

	private void showknoDialog() {
		// TODO Auto-generated method stub
		addTv.setVisibility(View.GONE);
		modTv.setVisibility(View.GONE);
		delTv.setVisibility(View.GONE);
		divider.setVisibility(View.GONE);
		divider2.setVisibility(View.GONE);
		delTagTv.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (mListener == null && mdeDelTagListener == null) {
			return;
		}
		if (v == addTv) { // 添加
			mListener.onSelect(OperType.Create);
			this.dismiss();
		} else if (v == modTv) { // 修改
			mListener.onSelect(OperType.Modify);
			this.dismiss();
		} else if (v == delTv) { // 删除
			mListener.onSelect(OperType.Delete);
			this.dismiss();
		} else if (v == delTagTv) { // 删除
			mdeDelTagListener.onDelTag();
			this.dismiss();
		}
	}

	public static int getStatusBarHeight(Context context) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return statusBarHeight;
	}

	public interface OnSelectListener {
		public void onSelect(OperType operType);
	}

	public interface OnDelTagListener {
		public void onDelTag();
	}

	/**
	 * 设置操作点击事件
	 * 
	 * @param listener
	 */
	public void setOnSelectListener(OnSelectListener listener) {
		mListener = listener;
	}

	public void setOnDelTagListener(OnDelTagListener listener) {
		mdeDelTagListener = listener;
	}
}
