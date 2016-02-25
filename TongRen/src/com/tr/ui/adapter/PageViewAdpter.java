package com.tr.ui.adapter;

import java.util.ArrayList;

import com.tr.ui.widgets.CommonSmileyParser;
import com.tr.ui.widgets.SmileyView;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
/**
 * 显示表情适配器
 */
public class PageViewAdpter extends PagerAdapter{
	
	private ArrayList<SmileyView> listSmileyViews = new ArrayList<SmileyView>();
	/**
	 * 表情适配器构造函数
	 * @param context
	 * @param smileyViewClickListener 点击子view的事件监听
	 */
	public PageViewAdpter(Context context,SmileyView.OnItemClickListener smileyViewClickListener) {
		super();
		int totalPage = (int) Math.ceil(CommonSmileyParser.mEnhancedIconIds.length * 1.0 / SmileyView.MaxSmileyNumber);
		for (int i = 0; i < totalPage; i++) {
			SmileyView sv = new SmileyView(context, i);
			listSmileyViews.add(sv);
			sv.setOnItemClickListener(smileyViewClickListener);
		}
	}

	@Override
	public int getCount() {
		return listSmileyViews.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(View container, int position) {
		((ViewPager) container).addView(listSmileyViews.get(position));
		return listSmileyViews.get(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(listSmileyViews.get(position));
	}
}
