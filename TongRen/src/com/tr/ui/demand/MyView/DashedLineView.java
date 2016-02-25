package com.tr.ui.demand.MyView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.tr.R;

@SuppressLint("NewApi")
public class DashedLineView extends View {
	private int lineColor = Color.BLACK; // 线条颜色
	private float strokeWidth = 1;// 线宽

	public DashedLineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.DashedLine);
		lineColor = a.getColor(R.styleable.DashedLine_lineColor, Color.BLACK);
		strokeWidth = a.getFloat(R.styleable.DashedLine_strokewidth, 1.0f);
		a.recycle();
		this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);// 关闭硬件加速
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(Color.TRANSPARENT); // 设置背景颜色
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setColor(lineColor); // 设置画笔颜色
		DashPathEffect effects = new DashPathEffect(new float[] { 5, 5, 5, 5 },
				1);
		paint.setPathEffect(effects);
		paint.setStrokeWidth(strokeWidth); // 设置线宽
		float[] pts = { 0, 0, getWidth(), 0 };
		canvas.drawLines(pts, paint);
	}

}