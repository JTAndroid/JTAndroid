package com.tr.ui.widgets;


import com.tr.R;
import com.utils.common.EUtil;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
public class MenuItemView extends ViewGroup{
	public static final String TAG = "MenuItemView";
	
	public static final int POSITION_LEFT_TOP = 1;
	public static final int POSITION_RIGHT_TOP = 2;
	public static final int POSITION_LEFT_BOTTOM = 3;
	public static final int POSITION_RIGHT_BOTTOM = 4;
	public  final static int STATUS_CLOSE = 5;
	public  final static int STATUS_OPEN = 6;
	
	private int flagX;
	private int flagY;
	private float radius;
	private int status;
	
	private int positon;
	
	private Context context;
	
	
	float textSize=0;
	float px=0;
	public MenuItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		String MSG = "MenuItemView() ";
		
		DisplayMetrics metric = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metric);
		float densityDpi=metric.densityDpi;
		float density=metric.density;
		float widthPixels=metric.widthPixels;
//		Log.i(TAG, MSG + " densityDpi = " + densityDpi);
//		Log.i(TAG, MSG + " density = " + density);
//		Log.i(TAG, MSG + " widthPixels = " + widthPixels);
		if(densityDpi>480){
			px=densityDpi*1/6;
			//textSize=context.getResources().getDimensionPixelSize(R.dimen.text_size_480);
			textSize=densityDpi*1/60;
		}else if(densityDpi==480){
			px=densityDpi*1/7;
			//textSize=context.getResources().getDimensionPixelSize(R.dimen.text_size_480);
			textSize=densityDpi*1/55;
		}else if(densityDpi==320){
			px=densityDpi *7/40;
			textSize=densityDpi*5 /140;
		}else if(densityDpi==240){
			px=densityDpi *5/40;
			textSize=densityDpi*4 /140;
		}
		else if(densityDpi==210){
			px=widthPixels *3/40;
			textSize=widthPixels *1 /40;
		}
		else {//if(densityDpi==160)
			px=widthPixels*3 /40;
			textSize=widthPixels*1 /40;
		}
		
		if(textSize > 16){
			textSize = 16;
		}
//		Log.i(TAG, MSG + " px = " + px);
//		Log.i(TAG, MSG + " textSize = " + textSize);
		
//		if(density >= 3){
//			textSize = EUtil.convertDpToPx(14);
//		}
//		else if(density >= 2){
//			textSize = EUtil.convertDpToPx(16);
//		}
//		else if(density >= 1.5){
//			textSize = EUtil.convertDpToPx(16);
//		}
//		else if(density >= 1.0){
//			textSize = EUtil.convertDpToPx(12);
//		}
//		else if(density >= 0.75){
//			textSize = EUtil.convertDpToPx(12);
//		}
		
		
		
		
		
		this.setPosition(MenuItemView.POSITION_RIGHT_BOTTOM);
		//int densityDpi=metric.widthPixels;
		float radiussize=0;
		if(densityDpi>=480){
			radiussize =densityDpi*4 /15;
		}else if(densityDpi==320){
			radiussize =densityDpi *8/15;
		}else if(densityDpi==240){
			radiussize =densityDpi *7/15;
		}else{
			radiussize =widthPixels*6 /15;
		}
		this.setRadius(radiussize);
		
		init(context);
	}

	
	private void init(Context context) {
		this.context = context;
		this.status = STATUS_CLOSE;
		
	}
	
	public void setPosition(int position){
		this.positon = position;
		flagX = position == POSITION_LEFT_TOP || position == POSITION_LEFT_BOTTOM ? -1 : 1;
		flagY = position == POSITION_LEFT_TOP || position == POSITION_RIGHT_TOP ? -1 : 1;
	}
	

	
	@Override
	     protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	         for (int index = 0; index < getChildCount(); index++) {
	             final View child = getChildAt(index);
	             // measure
	            child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
	        }
	
	        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	    }

	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if(positon == 0)
			throw new RuntimeException("PositonUnknow!Use method setPosition to set the position first!");
		if(radius == 0)
			throw new RuntimeException("RadiusUnknow!Use method setRadiusByDP to set the radius first!");
		
		if(changed){
			int count = getChildCount();
			int dx = -flagX * (int) MyAnimations.dip2px(context, 10);
			int dy = -flagY * (int) MyAnimations.dip2px(context, 10);
		for (int i = 0; i < count; i++) {
			View childView = getChildAt(i);
			childView.setVisibility(View.GONE);
			int width = childView.getMeasuredWidth();
			int height = childView.getMeasuredHeight();
			//the position of childview leftTop
			int x = (int) (MyAnimations.dip2px(context, radius) *  Math.sin(MyAnimations.PI / 2 / (float)(count - 1) * i));
			int y = (int) (MyAnimations.dip2px(context, radius) *  Math.cos(MyAnimations.PI / 2 / (float)(count - 1) * i));
			x = positon == POSITION_RIGHT_BOTTOM || positon == POSITION_RIGHT_TOP ? (getMeasuredWidth() - x -width) : x;
			y = positon == POSITION_LEFT_BOTTOM || positon == POSITION_RIGHT_BOTTOM ? (getMeasuredHeight() - y - height) : y;
			childView.layout(x + dx , y + dy, x + width+ dx, y + height + dy );
				}
		}
	}
	
	public int getFlagX() {
		return flagX;
	}

	public void setFlagX(int flagX) {
		this.flagX = flagX;
	}

	public int getFlagY() {
		return flagY;
	}

	public void setFlagY(int flagY) {
		this.flagY = flagY;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	
	public void addItem(String text) {

		TextView img1 = new TextView(context);
		img1.setText(text);
		img1.setTextColor(0xffffffff);
		img1.setGravity(Gravity.CENTER);
		img1.setTextSize(textSize);
		LinearLayout ll=new LinearLayout(context);
		ll.setGravity(Gravity.CENTER);
		ll.setBackgroundResource(R.drawable.im_pathmenu_roundbg);
		LinearLayout.LayoutParams ll_l=new LinearLayout.LayoutParams((int)px, (int)px);
		ll.addView(img1,ll_l);
		this.addView(ll);
	}
	
}
