package com.tr.ui.widgets;

import java.util.ArrayList;
import java.util.List;

import com.tr.App;
import com.tr.R;
import com.tr.navigate.ENavigate;
import com.tr.ui.home.frg.FrgFlow;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ImageView.ScaleType;

public class GuidePopupWindow extends PopupWindow {

	private Activity mContext;
	private ImageView guideIv;
	private ViewPager guideVPager;
	private ArrayList mListViews = new ArrayList<View>();// 将要分页显示的View装入数组中

	public GuidePopupWindow(Activity context) {
		mContext = context;
		View root = LayoutInflater.from(mContext).inflate(
				R.layout.widget_popup_guide, null);
		guideIv = (ImageView) root.findViewById(R.id.guideIv);
		guideVPager = (ViewPager) root.findViewById(R.id.guideVPager);
		root.setOnClickListener(mClickListener);
		DisplayMetrics metrics = new DisplayMetrics();
		mContext.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		// 设置SelectPicPopupWindow的View
		setContentView(root);
		// 设置SelectPicPopupWindow弹出窗体的宽
		setWidth(metrics.widthPixels);
		// 设置SelectPicPopupWindow弹出窗体的高
		setHeight(metrics.heightPixels - getStatusBarHeight());
		// 设置SelectPicPopupWindow弹出窗体可点击
		setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(mContext.getResources().getColor(
				R.color.transparent));
		// 设置SelectPicPopupWindow弹出窗体的背景
		setBackgroundDrawable(dw);

		initGuidPager();
		guideVPager.setAdapter(new MyViewPagerAdapter(mListViews));
	}

	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			dismiss();
		}
	};

	/**
	 * 弹出
	 */
	public void show() {
		showAtLocation(getContentView(), Gravity.BOTTOM, 0, 0);
	}

	public void setImage(int resId) {
		guideIv.setImageResource(resId);
	}

	public void showViewPager() {
		guideVPager.setVisibility(View.VISIBLE);
		guideIv.setVisibility(View.GONE);
	}

	private int getStatusBarHeight() {
		int result = 0;
		int resourceId = mContext.getResources().getIdentifier(
				"status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = mContext.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	private void initGuidPager() {
		ImageView guideView1 = new ImageView(mContext);
		guideView1.setImageResource(R.drawable.main_guide_01);
		guideView1.setScaleType(ScaleType.CENTER_CROP);
		mListViews.add(guideView1);
		ImageView guideView2 = new ImageView(mContext);
		guideView2.setImageResource(R.drawable.main_guide_02);
		guideView2.setScaleType(ScaleType.CENTER_CROP);
		mListViews.add(guideView2);
		ImageView guideView3 = new ImageView(mContext);
		guideView3.setImageResource(R.drawable.main_guide_03);
		guideView3.setScaleType(ScaleType.FIT_XY);
		mListViews.add(guideView3);
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.guide_person, null);
		ImageView iknow = (ImageView) view.findViewById(R.id.iknow);
		ImageView save = (ImageView) view.findViewById(R.id.save);
		mListViews.add(view);

		guideView1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				guideVPager.setCurrentItem(1);
			}
		});
		
		guideView2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				guideVPager.setCurrentItem(2);
			}
		});
		
		guideView3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				guideVPager.setCurrentItem(3);
			}
		});

		iknow.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				ENavigate.startRelationHomeActivity(mContext, App.getUserID(), true, 1);
			}
		});
	}

	public class MyViewPagerAdapter extends PagerAdapter {
		private List<View> mListViews;

		public MyViewPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;// 构造方法，参数是我们的页卡，这样比较方便。
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mListViews.get(position));// 删除页卡
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) { // 这个方法用来实例化页卡
			container.addView(mListViews.get(position), 0);// 添加页卡
			return mListViews.get(position);
		}

		@Override
		public int getCount() {
			return mListViews.size();// 返回页卡的数量
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;// 官方提示这样写
		}
	}
}
