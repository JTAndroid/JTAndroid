package com.tr.ui.widgets;

import com.tr.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/** 下拉刷新控件（畅聊列表刷新专用）
 * 
 * @author leon */
public class PullToRefreshListView extends ListView {

	private final String TAG = getClass().getSimpleName();

	private float mLastY = -1; // 滑动位置
	private RelativeLayout headerRl; // 头部控件
	private ProgressBar loadingPb; // 加载框
	private OnRefreshListener refreshListener; // 刷新监听器
	private boolean isRefreshing = false; // 当前状态
	private boolean enablePullToRefresh = true;

	/** 构造函数
	 * 
	 * @param context */
	public PullToRefreshListView(Context context) {
		super(context);
		initWithContext(context);
	}

	/** 构造函数
	 * 
	 * @param context */
	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWithContext(context);
	}

	/** 构造函数
	 * 
	 * @param context */
	public PullToRefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initWithContext(context);
	}

	/** 初始化控件
	 * 
	 * @param context */
	private void initWithContext(Context context) {
		headerRl = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.widgets_pull_to_refresh_header, null);
		loadingPb = (ProgressBar) headerRl.findViewById(R.id.loadingPb);
		hideHeader();
		addHeaderView(headerRl);
	}

	/** 显示Header */
	private void showHeader() {
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) loadingPb.getLayoutParams();
		params.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
		params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
		loadingPb.setLayoutParams(params);
	}

	/** 隐藏Header */
	private void hideHeader() {
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) loadingPb.getLayoutParams();
		params.width = 0;
		params.height = 0;
		loadingPb.setLayoutParams(params);
	}

	/** 开始刷新 */
	private void startRefresh() {
		if (isRefreshing()) {
			return;
		}
		isRefreshing = true;
		showHeader();
		if (refreshListener != null) {
			refreshListener.onRefresh();
		}
	}

	/** 停止刷新 */
	public void stopRefresh() {
		if (isRefreshing == true) {
			isRefreshing = false;
			hideHeader();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		// modified by sun jianan, ensure dont use UI thread 2015-3-17 1:17
		if (isRefreshing) {
			return false;
		}

		// 禁止下拉刷新
		if (!enablePullToRefresh) {
			return super.onTouchEvent(ev);
		}
		if (mLastY == -1) {
			mLastY = ev.getRawY();
		}
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastY = ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float deltaY = ev.getRawY() - mLastY;
			mLastY = ev.getRawY();
			if (getFirstVisiblePosition() == 0 && deltaY > 0) {

				// modified by Sunjianan
				//isRefreshing = true;
				enablePullToRefresh = false;

				startRefresh();
			}
			break;
		case MotionEvent.ACTION_UP:
			break;
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	/** 设置下拉刷新监听器
	 * 
	 * @param listener */
	public void setOnRefreshListener(OnRefreshListener listener) {
		refreshListener = listener;
	}

	/** 下拉刷新接口 implements this interface to get refresh/load more event. */
	public interface OnRefreshListener {
		public void onRefresh();
	}

	/** 是否在刷新中
	 * 
	 * @return */
	public boolean isRefreshing() {
		return isRefreshing;
	}

	/** 是否允许下拉刷新
	 * 
	 * @return */
	public boolean isEnablePullToRefresh() {
		return enablePullToRefresh;
	}

	/** 设置是否允许下拉刷新
	 * 
	 * @param enablePullToRefresh */
	public void setEnablePullToRefresh(boolean enablePullToRefresh) {
		this.enablePullToRefresh = enablePullToRefresh;
	}
}
