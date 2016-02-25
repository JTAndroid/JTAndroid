package com.tr.ui.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class CustomRelativeLayout extends RelativeLayout{

	private GestureDetector detector;
	private Context ctx;
	public CustomRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.ctx = context;
		initView();
	}
	
	private boolean needInterceptTouch = true;
	/**解决当评论的表情输入框弹出时，无法滑动*/
	public void setNeedInterceptTouch(boolean needInterceptTouch) {
		this.needInterceptTouch = needInterceptTouch;
	}

	private OnFlyingListener flyingListener;
	
	/**
	 * 设置监听器
	 * @param listener
	 */
	public void setOnFlyingListener(OnFlyingListener listener){
		flyingListener = listener;
	}
	
	public interface OnFlyingListener{
		public void onFlaying(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY);
	}
	
	private void initView() {
		detector = new GestureDetector(ctx, new OnGestureListener() {
			
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				return false;
			}
			
			@Override
			public void onShowPress(MotionEvent e) {
				
			}
			
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
					float distanceY) {
			
				return false;
			}
			
			@Override
			public void onLongPress(MotionEvent e) {
				
			}
			
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
					float velocityY) {
				flyingListener.onFlaying(e1,e2,velocityX,velocityY);
				return false;
			}
			
			@Override
			public boolean onDown(MotionEvent e) {
				return false;
			}
		});
		
	}
	

	
	public void setGestureDetector(GestureDetector detector){
		this.detector = detector;
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}
	private float downX;
	private float downY;
	@Override
	/**
	 * 是否中断事件的传递，默认返回false，意思为，不中断，按正常的情况，传递事件。
	 * 如果返回true ,就将事件中断，直接执行自己的onTouchEvent方法 
	 */
	public boolean onInterceptTouchEvent(MotionEvent event) {
		boolean result = false;
		//如果水平滑动的距离，大于竖起滑动，就中断事件的传递
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//解决 onTouchEvent 执行时，没有down事件，导致计算出错
			detector.onTouchEvent(event);
			downX = event.getX();
			downY = event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			int distanceX = (int) Math.abs(event.getX()-downX);
			int distanceY = (int) Math.abs(event.getY()-downY);
			if(distanceX > distanceY && distanceX>10 &&needInterceptTouch){
				result = true;
			}
			break;
		case MotionEvent.ACTION_UP:
			
			break;
		}
		return result;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		/**
		 * 在listView中先上下滑动时，onTouchEvent ，没有执行
		 * 然后，水平滑动，超过竖直滑动时，onInterceptTouchEvent 返回为true ，中断事件的传递，
		 * onTouchEvent 方法才开始执行。
		 * 此时，onTouchEvent 方法收到的第一个事件，是move事件。
		 */
		detector.onTouchEvent(event);
		return true;
	}
}
