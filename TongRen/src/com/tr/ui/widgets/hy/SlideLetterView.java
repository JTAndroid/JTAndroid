package com.tr.ui.widgets.hy;

import com.tr.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SlideLetterView extends View {
	private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
//	private Context context;
	private String str;
	private String[] b;
	private String color;
	private Paint paint = new Paint();
	private int choose = -1;
	
	
	public SlideLetterView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);

	}
	
	public SlideLetterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SlideLetterView(Context context, String attrs) {
		super(context);
		this.str = attrs;
		init(context);
	}

	public SlideLetterView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
//		this.context = context;
		b = context.getResources().getStringArray(R.array.hy_slide_letters);
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (showBkg) {
//			canvas.drawColor(Color.parseColor("#dcdcdc"));//点击时背景变色
		}
		int height = getHeight();
		int width = getWidth();
		int singleHeight = height / b.length;
		for (int i = 0; i < b.length; i++) {
			if (b[i].equals(str)) {
				paint.setColor(Color.parseColor(color));
			}else{
				paint.setColor(Color.parseColor("#888888"));
			}
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAntiAlias(true);
			paint.setTextSize(20);
			if (i == choose) { 
				paint.setColor(Color.parseColor("#4bbe00"));
				paint.setFakeBoldText(true);
			}
			float xPos = width / 2 - paint.measureText(b[i]) / 2;
			float yPos = singleHeight * i + singleHeight;
			canvas.drawText(b[i], xPos, yPos, paint);
			paint.reset();
		}

	}

	public void setPaintColor(String str) {
		this.str = str;
		color = "#4bbe00";
		invalidate();
	}

	private boolean showBkg = false;

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();
		final int oldChoose = choose;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		final int c = (int) (y / getHeight() * b.length);

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			showBkg = true;
			if (oldChoose != c && listener != null) {
				if (c > 0 && c < b.length) {
					listener.onTouchingLetterChanged(b[c], false);
					choose = c;
					invalidate();
				}
			}

			break;
		case MotionEvent.ACTION_MOVE:
			if (oldChoose != c && listener != null) {
				if (c > 0 && c < b.length) {
					listener.onTouchingLetterChanged(b[c], false);
					choose = c;
					invalidate();
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			listener.onTouchingLetterChanged("", true);
			showBkg = false;
			choose = -1;
			invalidate();
			break;
		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);

	}

	public void setOnTouchingLetterChangedListener(
			OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;

	}

	public interface OnTouchingLetterChangedListener {
		public void onTouchingLetterChanged(String s, boolean fal);
	}

}