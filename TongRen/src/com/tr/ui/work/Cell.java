
package com.tr.ui.work;

import com.tr.ui.work.CalendarView.ActionType;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

/**
 * 应该是每个day小单元
 * @author gushi
 *
 */
public class Cell {
	public static final String TAG = "Cell";
	
	protected Rect mBound = null;
	protected int year;
	protected int month;
	protected int mDayOfMonth = 1;	// from 1 to 31
	protected String mDayStr;
	/**	是否备注	true 显示圆点	false 无圆点	*/
	private boolean mIsMark = true;			//1 显示圆点
	/**	是否备注	true 当前日期和昨天显示红圆点	false 无红圆点	*/
	private boolean mIsMarkRed = true;			//1 显示圆点
	/**	是否选中的日期	true 应该是画大橙色圆背景,字变白色	false 无背景, 字黑色	*/
	public boolean mSelectDate = false;
	/**	是否画线	*/
	public boolean isDrawLine;
	
	protected Paint mPaint = new Paint(Paint.SUBPIXEL_TEXT_FLAG
            |Paint.ANTI_ALIAS_FLAG);
	protected Paint mnPaint = new Paint(Paint.SUBPIXEL_TEXT_FLAG
            |Paint.ANTI_ALIAS_FLAG);
	protected Paint mMarkPaint =new Paint(Paint.SUBPIXEL_TEXT_FLAG
            |Paint.ANTI_ALIAS_FLAG);
	/**	线paint	*/
	protected Paint linePaint =new Paint(Paint.SUBPIXEL_TEXT_FLAG
			|Paint.ANTI_ALIAS_FLAG);
	
	public int mTextColor=0;
	int dx, dy;

	
	public Cell(int year, int month, int dayOfMon, Rect rect, float textSize, float ntextSize,boolean bold) {
		String MSG = "Cell(int year, int month, int dayOfMon, Rect rect, float textSize, float ntextSize,boolean bold)";
		Log.i(TAG, MSG);
		
		
		this.year = year;
		this.month = month;
		mDayOfMonth = dayOfMon;
		
		//Log.d("xmx","1 year:"+year+" month:"+month+" day:"+dayOfMon);
		int vMonth=month;
		if (month<=0)
			vMonth=month+12;
		if (month>12)
			vMonth=month-12;
		
		//mDayStr="";
		mDayStr=CalendarUtil.getChineseDay(year,vMonth,dayOfMon);
		if (mDayStr.equals("初一"))
			mDayStr=CalendarUtil.getChineseMonth(year,vMonth,dayOfMon);
		//Log.d("xmx","4 day:"+mDayOfMonth+ " mDayStr:"+mDayStr);
		
		mBound = rect;
		mPaint.setTextSize(textSize);
		mTextColor=Color.rgb(14, 14, 14);
		mPaint.setColor(Color.rgb(14, 14, 14));
		
		if(bold) mPaint.setFakeBoldText(true);
		
		
		
		dx = (int) mPaint.measureText(String.valueOf(mDayOfMonth)) / 2;
		dy = (int) (-mPaint.ascent() + mPaint.descent()) / 2;
		
		mnPaint.setTextSize(ntextSize);
		mnPaint.setColor(Color.rgb(14, 14, 14));
		
		mMarkPaint.setColor(Color.rgb(185, 185, 185));
		
		linePaint.setColor(Color.RED);
		linePaint.setStrokeWidth(4);
	}
	
	public Cell(int year, int month, int dayOfMon, Rect rect, float textSize,float ntextSize) {
		this(year, month, dayOfMon, rect, textSize,ntextSize, false);
		String MSG = "Cell(int year, int month, int dayOfMon, Rect rect, float textSize,float ntextSize)";
		Log.i(TAG, MSG);
	}
	
	protected void draw(Canvas canvas, ActionType actionType,int dy) {
		String MSG = "draw()";
		Log.i(TAG, MSG);
		
		if (mSelectDate){
			
			if (actionType == ActionType.PullDown) {
				//画圆
				mMarkPaint.setColor(Color.rgb(249, 133, 18));
				canvas.drawCircle((float)(mBound.centerX()), (float)(mBound.top+mBound.height()*0.4140 ), (float)(mBound.height()*0.3600), mMarkPaint);
				
				mPaint.setColor(Color.rgb(255, 255, 255));
				canvas.drawText(String.valueOf(mDayOfMonth), mBound.centerX() - dx, (int)(mBound.top+mBound.height()*0.4230 ), mPaint);
				
				mnPaint.setColor(Color.rgb(255, 255, 255));
				int vdx = (int) mnPaint.measureText(String.valueOf(mDayStr)) / 2;
				canvas.drawText(mDayStr, mBound.centerX() - vdx, (int)(mBound.top+mBound.height()*0.6634 ), mnPaint);
				
			}else if (actionType == ActionType.TakeBack){
				//画圆
				mMarkPaint.setColor(Color.rgb(249, 133, 18));
				canvas.drawCircle((float)(mBound.centerX()), (float)(1.5*dy+mBound.height()*0.4140), (float)(mBound.height()*0.3600), mMarkPaint);
				
				mPaint.setColor(Color.rgb(255, 255, 255));
				canvas.drawText(String.valueOf(mDayOfMonth), mBound.centerX() - dx, (int)(1.5*dy+mBound.height()*0.4230), mPaint);
				
				mnPaint.setColor(Color.rgb(255, 255, 255));
				int vdx = (int) mnPaint.measureText(String.valueOf(mDayStr)) / 2;
				canvas.drawText(mDayStr, mBound.centerX() - vdx, (int)(1.5*dy+mBound.height()*0.6634  ), mnPaint);
			}else{
				//画圆
				mMarkPaint.setColor(Color.rgb(249, 133, 18));
				canvas.drawCircle((float)(mBound.centerX()), (float)(mBound.top+mBound.height()*0.4140 ), (float)(mBound.height()*0.3600), mMarkPaint);
				
				mPaint.setColor(Color.rgb(255, 255, 255));
				canvas.drawText(String.valueOf(mDayOfMonth), mBound.centerX() - dx, (int)(mBound.top+mBound.height()*0.4230 ), mPaint);
				
				mnPaint.setColor(Color.rgb(255, 255, 255));
				int vdx = (int) mnPaint.measureText(String.valueOf(mDayStr)) / 2;
				canvas.drawText(mDayStr, mBound.centerX() - vdx, (int)(mBound.top+mBound.height()*0.6634 ), mnPaint);
				
			}
			
			
		}
		else {
			
			mPaint.setColor(mTextColor);
			mnPaint.setColor(mTextColor);
			if (actionType == ActionType.PullDown) {
				canvas.drawText(String.valueOf(mDayOfMonth), mBound.centerX() - dx, (int)(mBound.top+mBound.height()*0.4230 ), mPaint);
				int vdx = (int) mnPaint.measureText(String.valueOf(mDayStr)) / 2;
				canvas.drawText(mDayStr, mBound.centerX() - vdx, (int)(mBound.top+mBound.height()*0.6634 ), mnPaint);

			}else if (actionType == ActionType.TakeBack){
				canvas.drawText(String.valueOf(mDayOfMonth), mBound.centerX() - dx, (int)(1.5*dy+mBound.height()*0.4230), mPaint);
				int vdx = (int) mnPaint.measureText(String.valueOf(mDayStr)) / 2;
				canvas.drawText(mDayStr, mBound.centerX() - vdx, (int)(1.5*dy+mBound.height()*0.6634), mnPaint);
			}else{
				
				canvas.drawText(String.valueOf(mDayOfMonth), mBound.centerX() - dx, (int)(mBound.top+mBound.height()*0.4230 ), mPaint);
				int vdx = (int) mnPaint.measureText(String.valueOf(mDayStr)) / 2;
				canvas.drawText(mDayStr, mBound.centerX() - vdx, (int)(mBound.top+mBound.height()*0.6634 ), mnPaint);
			}

			
			

		}
		
		if (mIsMark) {
			if (actionType == ActionType.PullDown) {
			mMarkPaint.setColor(Color.rgb(185, 185, 185));
			canvas.drawCircle((float)(mBound.centerX()), (float)(mBound.top+mBound.height()*0.8600 ), (float)(mBound.height()*0.0600), mMarkPaint);

			}else if(actionType == ActionType.TakeBack){
				mMarkPaint.setColor(Color.rgb(185, 185, 185));
				canvas.drawCircle((float)(mBound.centerX()), (float)(1.5*dy+mBound.height()*0.8600 ), (float)(mBound.height()*0.0600), mMarkPaint);

			}
		}
		if (mIsMarkRed) {
			if (actionType == ActionType.PullDown) {
			mMarkPaint.setColor(Color.rgb(185, 45, 46));
			canvas.drawCircle((float)(mBound.centerX()), (float)(mBound.top+mBound.height()*0.8600 ), (float)(mBound.height()*0.0600), mMarkPaint);

			}else if(actionType == ActionType.TakeBack){
				mMarkPaint.setColor(Color.rgb(185, 45, 46));
				canvas.drawCircle((float)(mBound.centerX()), (float)(1.5*dy+mBound.height()*0.8600 ), (float)(mBound.height()*0.0600), mMarkPaint);
	
			}
		}
		
		if(isDrawLine){
			if (actionType == ActionType.PullDown) {
				canvas.drawLine((float)(mBound.left + mBound.width() * 0.200 ),  (float)(mBound.top+mBound.height()*0.7600),   (float) (mBound.right - mBound.width() * 0.200), (float)(mBound.top+mBound.height()*0.7600), linePaint);
			}else if(actionType == ActionType.TakeBack){
				canvas.drawLine((float)(mBound.left + mBound.width() * 0.200 ),  (float)(1.5*dy+mBound.height()*0.7600),   (float) (mBound.right - mBound.width() * 0.200), (float)(1.5*dy+mBound.height()*0.7600), linePaint);

			}
			}
	}
	
	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public int getDayOfMonth() {
		return mDayOfMonth;
	}
	
	public boolean hitTest(int x, int y) {
		return mBound.contains(x, y); 
	}
	
	public Rect getBound() {
		return mBound;
	}
	
	public String toString() {
		return String.valueOf(mDayOfMonth)+"("+mBound.toString()+")";
	}

	public boolean getmIsMark() {
		return mIsMark;
	}

	public void setmIsMark(boolean mIsMark) {
		this.mIsMark = mIsMark;
	}

	public boolean ismIsMarkRed() {
		return mIsMarkRed;
	}

	public void setmIsMarkRed(boolean mIsMarkRed) {
		this.mIsMarkRed = mIsMarkRed;
	}
	
	
	
	
}

