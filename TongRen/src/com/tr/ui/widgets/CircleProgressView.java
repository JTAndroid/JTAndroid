package com.tr.ui.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.tr.R;

public class CircleProgressView extends View {

	//中间图标
	private Bitmap mBitmap;
	private int width;
	private int height;//图标宽高
	private float left;
	private float top;//图标坐标
	private int resId;//图片id
	
	private Paint mPaintBackground; // 绘制背景圆环的画笔
	private Paint mPaintProgress; // 绘制背景进度的画笔
	private Paint mPaintText; // 绘制背景字体的画笔
	private int bgColor = Color.WHITE; // 背景圆环的颜色
	private int textColor = Color.CYAN; // 字体的颜色
	private float textSize = 60; // 字体的颜色
	private int progressColor = Color.CYAN; // 进度条的颜色
	private float mStrokeWidth = 10;// 背景圆环的宽度
	private float mRadius = 60; // 背景圆环的半径
	private RectF rectPro;// 进度条的绘制外接矩形
	private float mProgress = 0; // 当前进度
	private float currentProgress = 0; // 当前进度
	private int mMaxProgress = 100; // 最大进度
	private int mWidth, mHeight;
	private onProgressListener mOnProgressListener;
	private int cir_type = 0;//1组织/2知识/3人脉/4需求
	
	private float half_radius = 30;
	private boolean isTouch = false;
	
	public void setOnProgressListener(onProgressListener mOnProgressListener) {
		this.mOnProgressListener = mOnProgressListener;
	}
	/**
	 * 回调接口
	 * 
	 */
	public  interface onProgressListener{
	/**
	 * 回调函数 当进度条满时调用此方法
	 */
		public void onEnd(); 
	
	}
	
	public CircleProgressView(Context context) {
		this(context, null);
	}

	public CircleProgressView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CircleProgressView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		if(attrs!=null){
			TypedArray ta = context.obtainStyledAttributes(attrs,
					R.styleable.CircleProgress);
			int count = ta.getIndexCount();
			for (int i = 0; i < count; i++) {
				int attr = ta.getIndex(i);
				switch (attr) {
				case R.styleable.CircleProgress_circleRadius:
					mRadius = ta.getDimension(R.styleable.CircleProgress_circleRadius, mRadius);
					half_radius = mRadius/2;
					break;
				case R.styleable.CircleProgress_circleStrokeWidth:
					mStrokeWidth = ta.getDimension(R.styleable.CircleProgress_circleStrokeWidth, mStrokeWidth);
					break;
				case R.styleable.CircleProgress_bgColor:
					bgColor = ta.getColor(R.styleable.CircleProgress_bgColor, bgColor);
					break;
				case R.styleable.CircleProgress_progressColor:
					progressColor = ta.getColor(R.styleable.CircleProgress_progressColor, progressColor);
					break;
				case R.styleable.CircleProgress_android_textColor:
					textColor = ta.getColor(R.styleable.CircleProgress_android_textColor, textColor);
					break;
				case R.styleable.CircleProgress_android_textSize:
					textSize = ta.getDimension(R.styleable.CircleProgress_android_textSize, textSize);
					break;
				case R.styleable.CircleProgress_iconType:
					cir_type = ta.getInteger(R.styleable.CircleProgress_iconType, 0);
					break;
				}
			}
			ta.recycle();
		}
		
		initPaint();
	}

	private void initPaint() {
		mPaintBackground = new Paint();
		mPaintBackground.setColor(bgColor);
		// 设置抗锯齿
		mPaintBackground.setAntiAlias(true);
		// 设置防抖动
		mPaintBackground.setDither(true);
		// 设置样式为环形
		mPaintBackground.setStyle(Style.STROKE);
		// 设置环形的宽度
		mPaintBackground.setStrokeWidth(mStrokeWidth);

		mPaintProgress = new Paint();
		mPaintProgress.setColor(progressColor);
		// 设置抗锯齿
		mPaintProgress.setAntiAlias(true);
		// 设置防抖动
		mPaintProgress.setDither(true);
		// 设置样式为环形
		mPaintProgress.setStyle(Style.STROKE);
		// 设置环形的宽度
		mPaintProgress.setStrokeWidth(mStrokeWidth);

		mPaintText = new Paint();
		mPaintText.setColor(textColor);
		// 设置抗锯齿
		mPaintText.setAntiAlias(true);
		mPaintText.setTextAlign(Align.CENTER);
		mPaintText.setTextSize(textSize);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		mWidth = getRealSize(widthMeasureSpec);
		mHeight = getRealSize(heightMeasureSpec);
		setMeasuredDimension(mWidth, mHeight);
	}

	private void initRect() {
		if (rectPro == null) {
			rectPro = new RectF();
			int viewSize = (int) (mRadius * 2);
			int left = (mWidth - viewSize) / 2;
			int top = (mHeight - viewSize) / 2;
			int right = left + viewSize;
			int bottom = top + viewSize;
			rectPro.set(left, top, right, bottom);
		}
	}

	private int getRealSize(int measureSpec) {
		int result = -1;
		int mode = MeasureSpec.getMode(measureSpec);
		int size = MeasureSpec.getSize(measureSpec);
		if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.UNSPECIFIED) { // 这两种模式需要自己计算
			result = (int) (mRadius * 2 + mStrokeWidth*2);
		} else {
			result = size;
		}
		return result;
	}
/**
 * 设置进度
 * @param progress
 */
	public void setProgress(float progress){
		this.mProgress =progress;
		invalidate();
	}
	
	public float getProgress(){
		return mProgress;
	}
	@Override
	protected void onDraw(Canvas canvas) {
		float angle = currentProgress / (mProgress) * 360; // 圆弧角度
		float maxangle = mProgress / (mMaxProgress) * 360; // 圆弧角度
		initRect();
		if(isTouch){
			//绘制背景圆环
			canvas.drawCircle(mWidth / 2, mHeight / 2, half_radius, mPaintBackground);
			//绘制进度条
			canvas.drawArc(rectPro, -90, angle>maxangle?maxangle:angle, false, mPaintProgress);
			switch(cir_type){
			case 0:
				break;
			case 1://组织
				mBitmap = ((BitmapDrawable) getContext().getResources().getDrawable(R.drawable.usermap_organization_select)).getBitmap();
				break;
			case 2://知识
				mBitmap = ((BitmapDrawable) getContext().getResources().getDrawable(R.drawable.usermap_knowdeg_select)).getBitmap();
				break;
			case 3://人脉
				mBitmap = ((BitmapDrawable) getContext().getResources().getDrawable(R.drawable.usermap_connection_select)).getBitmap();
				break;
			case 4://需求
				mBitmap = ((BitmapDrawable) getContext().getResources().getDrawable(R.drawable.usermap_require_select)).getBitmap();
				break;
			}
		}else{
			//绘制背景圆环
			canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mPaintBackground);
			//绘制进度条
			canvas.drawArc(rectPro, -90, angle>maxangle?maxangle:angle, false, mPaintProgress);

			switch(cir_type){
			case 0:
				break;
			case 1://组织
				mBitmap = ((BitmapDrawable) getContext().getResources().getDrawable(R.drawable.usermap_organization)).getBitmap();
				break;
			case 2://知识
				mBitmap = ((BitmapDrawable) getContext().getResources().getDrawable(R.drawable.usermap_knowdeg)).getBitmap();
				break;
			case 3://人脉
				mBitmap = ((BitmapDrawable) getContext().getResources().getDrawable(R.drawable.usermap_connection)).getBitmap();
				break;
			case 4://需求
				mBitmap = ((BitmapDrawable) getContext().getResources().getDrawable(R.drawable.usermap_require)).getBitmap();
				break;
			}
		}
		
		if(cir_type==0){
			String text = currentProgress + "%";
			FontMetrics fm = mPaintText.getFontMetrics();
			//文本的宽度
			float textWidth = mPaintText.measureText(text);
			float textCenterVerticalBaselineY = mHeight / 2 - fm.descent + (fm.descent - fm.ascent) / 2;
			//绘制字体
			canvas.drawText(text, mWidth / 2, textCenterVerticalBaselineY, mPaintText);
			if (currentProgress < mProgress) {
				currentProgress += 1;
				if(currentProgress>mProgress){
					currentProgress = mProgress;
				}
				invalidate();
			}
		}else{
			//绘制图标
			width = mBitmap.getWidth();
			height = mBitmap.getHeight();
			left = (mWidth - width) / 2;
			top = (mHeight - height) / 2;
			canvas.drawBitmap(mBitmap, left, top, mPaintText);
		}
		
		//当进度到达最大值时  调用此函数
		if(mOnProgressListener != null){
		if(mProgress == mMaxProgress){
				mOnProgressListener.onEnd();
			}
		}
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			isTouch = true;
			mPaintBackground.setStrokeWidth(mRadius+mStrokeWidth);
			mPaintBackground.setColor(progressColor);
			mPaintText.setColor(0xffffffff);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			isTouch = false;
			mPaintBackground.setStrokeWidth(mStrokeWidth);
			mPaintBackground.setColor(bgColor);
			mPaintText.setColor(textColor);
			invalidate();
			break;
		}
		return super.onTouchEvent(event);
	}

}
