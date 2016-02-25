package com.tr.ui.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 自定义滑出顶部的View
 * 
 * @author 钟山
 * 
 */
public class SlideView extends ViewGroup {

	private int mMostRecentY;

	private final int SCREEN_MAIN = 0; // 主界面
	private final int SCREEN_MENU = 1; // 菜单界面
	private int currentScreen = SCREEN_MENU; // 当前屏幕, 默认为: 主界面

	private Scroller mScroller; // Scroller只能用来模拟数据. 不会帮你切换界面去.

	public SlideView(Context context, AttributeSet attrs) {
		super(context, attrs);

		mScroller = new Scroller(context);
	}

	/**
	 * 当自定义的ViewGroup测量宽和高时触发此方法.
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		// 测量当前ViewGroup中所有子布局的宽和高

		View menuView = getChildAt(0); // 获得菜单布局
		// 测量菜单的宽和高, 宽度是: 布局文件中指定的240dip. 高度是: 父控件的高度是多高自己就多高
		// menuView.measure(menuView.getLayoutParams().width,
		// heightMeasureSpec);
		menuView.measure(widthMeasureSpec, menuView.getLayoutParams().height);

		View mainView = getChildAt(1); // 获得主界面布局
		mainView.measure(widthMeasureSpec, heightMeasureSpec); // 指定和父控件的宽高都一样.
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		View menuView = getChildAt(0);
		// menuView.layout(-menuView.getMeasuredWidth(), t, 0, b); // 指定菜单界面的左边是
		// 菜单宽度的负数, 右边就是0
		menuView.layout(l, 0, r, menuView.getMeasuredHeight());

		View mainView = getChildAt(1);
		// mainView.layout(l, t, r, b); // 指定主界面的上下左右的边界和父控件一样
		// mainView.layout(l, menuView.getMeasuredHeight(), r, b); //
		mainView.layout(l, menuView.getMeasuredHeight(), r, mainView.getMeasuredHeight() + menuView.getMeasuredHeight());
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 把x轴的偏移量记录下来
			mMostRecentY = (int) event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			int moveY = (int) event.getY();
			int deltaY = mMostRecentY - moveY; // 计算出增量值
			int newScrollY = getScrollY() + deltaY;
			if (newScrollY > 0) {
				scrollTo(0, newScrollY);
			}
			if (newScrollY >= getChildAt(0).getHeight()) {
				scrollTo(0, getChildAt(0).getHeight());
			}
			mMostRecentY = moveY;
			break;
		case MotionEvent.ACTION_UP:
			int scrollY = getScrollY();
			break;
		default:
			break;
		}
		return true;
	}


	/**
	 * 方法请求x轴和y轴的偏移量, 做移动的动画.
	 */
	@Override
	public void computeScroll() {
		// 把Scroller模拟的值, 取出来通过scrollTo移动屏幕
		if (mScroller.computeScrollOffset()) { // 判断当前是否正在模拟数据中, true 正在模拟.
			scrollTo(0, mScroller.getCurrY());

			// 流程: drawChild -> child.draw -> computeScroll
			invalidate(); // 触发当前方法computeScroll的调用
		}
	}

	private float xDistance, yDistance, xLast, yLast;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xDistance = yDistance = 0f;
			xLast = ev.getX();
			yLast = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float curX = ev.getX();
			final float curY = ev.getY();

			xDistance += Math.abs(curX - xLast);
			yDistance += Math.abs(curY - yLast);
			xLast = curX;
			yLast = curY;

			if (xDistance > yDistance) {
				return false;
			} else if (xDistance < yDistance) {
				return getScrollY() >= getChildAt(0).getHeight() ? false : true;
			}
		}
		return super.onInterceptTouchEvent(ev);
	}
}
