package com.tr.ui.widgets;

import java.util.Hashtable;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
/**
 * @ClassName PredicateLayout.java
 * @Description 自定义自动换行LinearLayout
 * @Author CJJ
 */
public class PredicateLayout extends ViewGroup {
	int mLeft, mRight, mTop, mBottom;
	Hashtable<View, Position> map = new Hashtable<View, Position>();
	/**
	 * 每个view上下的间距
	 */
	private int dividerLine;
	/**
	 * 每个view左右的间距
	 */
	private int dividerCol;
	private int childw;
	private int childh;

	public PredicateLayout(Context context) {
		super(context);
	}

	public PredicateLayout(Context context, int horizontalSpacing,
			int verticalSpacing) {
		super(context);
	}

	public PredicateLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setDividerLine(int dividerLine) {
		this.dividerLine = dividerLine;
	}

	public void setDividerCol(int dividerCol) {
		this.dividerCol = dividerCol;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int mWidth = MeasureSpec.getSize(widthMeasureSpec);
		int mCount = getChildCount();

		mLeft = 0;
		mRight = 0;
		mTop = 0;
		mBottom = 0;

		int j = 0;

		for (int i = 0; i < mCount; i++) {
			final View child = getChildAt(i);

			child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			childw = child.getMeasuredWidth();
			childh = child.getMeasuredHeight();
			mRight += childw; // 将每次子控件宽度进行统计叠加，如果大于设定的宽度则需要换行，高度即Top坐标也需重新设置

			Position position = new Position();
			mLeft = getPosition(i - j, i);
			mRight = mLeft + childw;
			if (mRight >= mWidth) {
				j = i;
				mLeft = getPaddingLeft();
				mRight = mLeft + childw;
				mTop += childh + dividerLine;
			}
			mBottom = mTop + childh;
			position.left = mLeft;
			position.top = mTop;
			position.right = mRight;
			position.bottom = mBottom;
			map.put(child, position);
		}
		setMeasuredDimension(mWidth, mBottom + getPaddingBottom());
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(1, 1);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			View child = getChildAt(i);
			Position pos = map.get(child);
			if (pos != null) {
				child.layout(pos.left, pos.top, pos.right, pos.bottom);
			} else {
			}
		}
	}

	private class Position {
		int left, top, right, bottom;
	}

	public int getPosition(int IndexInRow, int childIndex) {
		if (IndexInRow > 0) {
			return getPosition(IndexInRow - 1, childIndex - 1)
					+ getChildAt(childIndex - 1).getMeasuredWidth()
					+ dividerCol;
		}
		return getPaddingLeft();
	}
}
