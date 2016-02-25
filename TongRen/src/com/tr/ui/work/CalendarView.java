package com.tr.ui.work;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.tr.R;
import com.tr.model.knowledge.UserTag;
import com.tr.ui.knowledge.GlobalKnowledgeTagActivity.OperateType;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.MonthDisplayHelper;
import android.view.MotionEvent;
import android.view.View;
/**
 * 日历控件
 * @author yuanzhi.cjy
 *
 */
public class CalendarView extends View {
	public static final String TAG = "CalendarView";
	
	public enum OperateType {
		/** 单选 */
		Radio,
		/** 多选 */
		MultipleChoice,
		/** 什么都不做 自己处理	*/
		Nothing,
	}

	private OperateType operateType = OperateType.Radio;
	
	/** 画家类型 */
	public enum PainterType {
		/** 我自己 */
		Myself,
		/** 别人 */
		Other
	}
	
	public PainterType painterType = PainterType.Myself;
	
	public enum ActionType {
		/**下拉*/
		PullDown,
		/**收回*/
		TakeBack
	}
	private ActionType actionType = ActionType.PullDown;
	
	
	public static final int DEFAULT_BOARD_SIZE = 100;
	private static float CELL_TEXT_SIZE;
	private static float CELL_TEXT_SIZE_NOLI;
	
	
	private int mCellWidth;
	private int mCellHeight;
	private int mHeight;
	private int mWidth;
	private int mTitleHeight;
	
	public static final int CURRENT_MOUNT = 0;
    public static final int NEXT_MOUNT = 1;
    public static final int PREVIOUS_MOUNT = -1;
    public static final int CURRENT_WEEK = 0;
    public static final int NEXT_WEEK = 1;
    public static final int PREVIOUS_WEEK = -1;
	private static final String[] weekTitle = {"日","一","二","三","四","五","六"};
	
	private Calendar mRightNow = null;
	private Cell mToday = null;
	public Cell mTouchedCell = null;
	private Cell[][] mCells = new Cell[6][7];
	
	private OnMonthChangeListener monthChangeListener;
	MonthDisplayHelper mHelper;
	
	private Paint mBackgroundColor;
	private Paint mBackgroundColorToday;
	private Paint mBackgroundColorTouched;
	private Paint mWeekTitle;
	private Paint mLinePaint;
	private Paint mLinePaint2;
	
	public int mSelYear;
	public int mSelMonth;
	public int mSelDay;
	
	private float mScan;
	/** 是否当前view	*/
	private boolean mCurView=false;
	/**	备注列表	*/
	public List<String> mMarkList;
	/**	备注红点列表	*/
	public Map<String,Boolean> mMarkRedMap;
	/**	选中日期列表	*/
	public List<String> mSelectDateList ;


	private boolean TakeBack;


	private boolean isNext = false;


	private int mOldx;


	private int mOldy;


	private boolean hasmoved;


	private OnTakeBackDayClickListener TakeBackdayClickListener;


	private int mRow;


	private boolean NextWeek;


	private boolean previousWeek;


	






	
	
	
	public CalendarView(Context context) {
		this(context, null);
	}
	public CalendarView(Context context, AttributeSet attrs){
		super(context, attrs);
		String MSG = "CalendarView(Context context, AttributeSet attrs)";
		Log.i(TAG, MSG);
		mMarkList = new ArrayList<String>();
		mMarkRedMap = new HashMap<String, Boolean>();
		mSelectDateList = new ArrayList<String>();
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.CalendarView);
		TakeBack = typedArray.getBoolean(R.styleable.CalendarView_TakeBack,false);
		if (TakeBack) {
			this.actionType = ActionType.TakeBack;
		}
		initCalendarView();
	}
	
	private void initCalendarView() {
		String MSG = "initCalendarView()";
		Log.i(TAG, MSG);
		
		mRightNow = Calendar.getInstance();
		mHelper = new MonthDisplayHelper(
					mRightNow.get(Calendar.YEAR),
					mRightNow.get(Calendar.MONTH),
					mRightNow.getFirstDayOfWeek()
				);
		
		mSelYear=mRightNow.get(Calendar.YEAR);
		mSelMonth=mRightNow.get(Calendar.MONTH)+1;
		mSelDay=mRightNow.get(Calendar.DAY_OF_MONTH);
		
//		Log.d("xmx","mSelYear:"+mSelYear+" mSelMonth:"+mSelMonth+" mSelDay:"+mSelDay);
		Log.i(TAG, MSG + " mSelYear = " + mSelYear + " mSelMonth = " + mSelMonth + " mSelDay = " + mSelDay);
		mBackgroundColor = new Paint();
		mBackgroundColorToday = new Paint();
		mBackgroundColorTouched = new Paint();
		mWeekTitle = new Paint(Paint.SUBPIXEL_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
		mLinePaint = new Paint();
		mLinePaint2 = new Paint();
		
		mBackgroundColor.setColor(Color.rgb(247, 247, 247));
		mBackgroundColorToday.setColor(Color.RED);
		//mBackgroundColorToday.setAlpha(100);
		mBackgroundColorTouched.setColor(Color.BLUE);
		mBackgroundColorTouched.setAlpha(100);
		mWeekTitle.setColor(Color.rgb(135, 135, 135));
		mLinePaint.setColor(Color.rgb(204, 204, 204));
		mLinePaint2.setColor(Color.rgb(90, 90, 90));
	}
	
	/**
	 * 得到今天日期字符串 
	 * @return
	 */
	public static String getTodayDate(){
		Calendar calendar  = Calendar.getInstance();
		return getDayStrforDay(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
	}
	
	/**
	 * 初始化所有单元
	 * 给所有单元赋值
	 */
	private void initCells() {
		String MSG = "initCells()";
		Log.i(TAG, MSG);
		
		class _calendar {
			public int year;
			public int month;
			public int week;
	    	public int day;
	    	public int whichMonth;  // -1 为上月  1为下月  0为此月
	    	public int whichWeek;   // -1 为上周  1为下周  0为此周
	    	
	    	public _calendar(int y, int m, int d, int b) {
	    		year = y;
	    		month = m;
	    		day = d;
	    		whichMonth = b;
	    		
	    		int vMonth=m;
	    		if (m<=0)
	    		{
	    			month=month+12;
	    			year=y-1;
	    		}
	    		if (m>12)
	    		{
	    			month=month-12;
	    			year=year+1;
	    		}
	    		
	    	}
	    	public _calendar(int y, int m, int d) { // 上个月 默认为
	    		this(y, m, d, PREVIOUS_MOUNT);
	    	}
	    	public _calendar(int y, int m, int d, int b ,int w ,int ww) {
	    		year = y;
	    		month = m;
	    		day = d;
	    		whichMonth = b;
	    		whichWeek = w;
	    		int vMonth=m;
	    		if (m<=0)
	    		{
	    			month=month+12;
	    			year=y-1;
	    		}
	    		if (m>12)
	    		{
	    			month=month-12;
	    			year=year+1;
	    		}
	    		if (w<=0) {
	    			week = week+6;
	    			month = m-1;
				}
	    		if (w>6) {
	    			week = week-6;
	    			month = m+1;
				}
	    	}
	    	
//	    	public _calendar(int y, int m,int w, int d) { // 上个月 默认为
//	    		this(y, m, d, PREVIOUS_MOUNT,w,PREVIOUS_WEEK);
//	    	}
	    };
	    
		Log.d(TAG, MSG + " mCurView = " + mCurView);
		Log.d(TAG, MSG + " init Cale y = " + mHelper.getYear() + " m = " + (mHelper.getMonth() + 1));
	    
	    _calendar tmp[][] = new _calendar[6][7];
	    
	    for(int i=0; i<tmp.length; i++) {
	    	int n[] = mHelper.getDigitsForRow(i);
	    	for(int d=0; d<n.length; d++) {
	    		if(mHelper.isWithinCurrentMonth(i,d))
	    			tmp[i][d] = new _calendar(mHelper.getYear(), mHelper.getMonth()+1, n[d], CURRENT_MOUNT);
	    		else if(i == 0) {
	    			tmp[i][d] = new _calendar(mHelper.getYear(), mHelper.getMonth(), n[d]);
	    		} else {
	    			tmp[i][d] = new _calendar(mHelper.getYear(), mHelper.getMonth()+2, n[d], NEXT_MOUNT);
	    		}
	    		
	    	}
	    }
	    
	    Calendar today = Calendar.getInstance();
	    int thisDay = 0;
	    mToday = null;
	    mTouchedCell=null;
	    if(mHelper.getYear()==today.get(Calendar.YEAR) && mHelper.getMonth()==today.get(Calendar.MONTH)) {
	    	thisDay = today.get(Calendar.DAY_OF_MONTH);
	    }
	    Rect Bound  = null;
	    if (actionType ==ActionType.PullDown ) {
			Bound = new Rect(getPaddingLeft(), mTitleHeight+getPaddingTop(), mCellWidth+getPaddingLeft(), mTitleHeight+mCellHeight+getPaddingTop());
		}else if(actionType ==ActionType.TakeBack ||TakeBack) {
			Bound = new Rect(getPaddingLeft(), mTitleHeight, mCellWidth+getPaddingLeft(), mTitleHeight+mCellHeight);

		}
	    // build cells
		
		for(int week=0; week<mCells.length; week++) {
			for(int day=0; day<mCells[week].length; day++) {
					
			
					if(tmp[week][day].whichMonth == CURRENT_MOUNT) { // 此月  开始设置cell
						mCells[week][day] = new Cell(tmp[week][day].year, tmp[week][day].month, tmp[week][day].day, new Rect(Bound), CELL_TEXT_SIZE,CELL_TEXT_SIZE_NOLI);
						/*
						if(day==0 || day==6 )
							mCells[week][day] = new RedCell(tmp[week][day].year, tmp[week][day].month, tmp[week][day].day, new Rect(Bound), CELL_TEXT_SIZE,CELL_TEXT_SIZE_NOLI);
						else 
							mCells[week][day] = new Cell(tmp[week][day].year, tmp[week][day].month, tmp[week][day].day, new Rect(Bound), CELL_TEXT_SIZE,CELL_TEXT_SIZE_NOLI);
						*/
						
					} else if(tmp[week][day].whichMonth == PREVIOUS_MOUNT) {  // 上月为gray
						mCells[week][day] = new GrayCell(tmp[week][day].year, tmp[week][day].month, tmp[week][day].day, new Rect(Bound), CELL_TEXT_SIZE,CELL_TEXT_SIZE_NOLI);
					} else { // 下月为LTGray
						mCells[week][day] = new LTGrayCell(tmp[week][day].year, tmp[week][day].month, tmp[week][day].day, new Rect(Bound), CELL_TEXT_SIZE,CELL_TEXT_SIZE_NOLI);
					}
				
				// 设置day备注
				mCells[week][day].setmIsMark(getMarkByDay(mCells[week][day].year, mCells[week][day].month, mCells[week][day].mDayOfMonth));
				// 设置day红点备注
				mCells[week][day].setmIsMarkRed(getMarkRedByDay(mCells[week][day].year, mCells[week][day].month, mCells[week][day].mDayOfMonth));
				// 设置选中日期
				mCells[week][day].mSelectDate = getSelectByDay(mCells[week][day].year, mCells[week][day].month, mCells[week][day].mDayOfMonth);
				if (mCells[week][day].mSelectDate) {
					mRow = week+1;
				}
				
				Bound.offset(mCellWidth, 0); // move to next column 
				
				// get today
				if(tmp[week][day].day==thisDay && tmp[week][day].whichMonth == 0) {
					mToday = mCells[week][day];
					mToday.isDrawLine = true;
				}
				
				//select day 初始化被选中的日期  这里只有一天   应该要改成支持一个列表(选中的状态在选中的操作里统一处理,这里只作统一初始化 )
				if (tmp[week][day].year==mSelYear && tmp[week][day].month== mSelMonth && tmp[week][day].day==mSelDay)
				{
					//Log.d("xmx","tmp[week][day].year:"+tmp[week][day].year+" tmp[week][day].month:"+tmp[week][day].month+" tmp[week][day].day:"+tmp[week][day].day);
					
					mTouchedCell= mCells[week][day];
					// 这里是之前的单选逻辑
//					mTouchedCell.mSelectDate=true;
				}
				
			}
			Bound.offset(0, mCellHeight); // move to next row and first column
			Bound.left = getPaddingLeft();
			Bound.right = getPaddingLeft()+mCellWidth;
		}
	}

	public int getYear() {
		return mHelper.getYear();
	}
	    
	public int getMonth() {
		return mHelper.getMonth()+1;
	}
	
	public void nextMonth() {
		mHelper.nextMonth();
		initCells();
		invalidate();
		if(monthChangeListener!=null)
			monthChangeListener.onMonthChanged(true);
	}
	    
	public void previousMonth() {
		mHelper.previousMonth();
		initCells();
		invalidate();
		if(monthChangeListener!=null)
			monthChangeListener.onMonthChanged(false);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		String MSG = "onMeasure()";
		Log.i(TAG, MSG);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//        Log.d("xmx","widthSize:"+widthSize+" heightSize"+heightSize+" getPaddingTop()"+getPaddingTop()+" getPaddingLeft()"+getPaddingLeft());
		Log.d(TAG, MSG + " widthSize = " + widthSize + " heightSize = " + heightSize + " getPaddingTop() = " + getPaddingTop() + " getPaddingLeft() = " + getPaddingLeft());
        
        int width = -1, height = -1;
        if (widthMode == MeasureSpec.EXACTLY) {
        	width = widthSize;
        } else {
        	width = DEFAULT_BOARD_SIZE;
        	if (widthMode == MeasureSpec.AT_MOST && width > widthSize ) {
        		width = widthSize;
        	}
        }
        if (heightMode == MeasureSpec.EXACTLY) {
        	height = heightSize;
        } else {
        	height = DEFAULT_BOARD_SIZE;
        	if (heightMode == MeasureSpec.AT_MOST && height > heightSize ) {
        		height = heightSize;
        	}
        }
        
        if (widthMode != MeasureSpec.EXACTLY) {
        	width = height;
        }
        
        if (heightMode != MeasureSpec.EXACTLY) {
        	height = width;
        }
        
    	if (widthMode == MeasureSpec.AT_MOST && width > widthSize ) {
    		width = widthSize;
    	}
    	if (heightMode == MeasureSpec.AT_MOST && height > heightSize ) {
    		height = heightSize;
    	}
    	
    	
    	mCellWidth = (width - getPaddingLeft() - getPaddingRight()) / 7;
    	
    	
    	mScan=width* 1.0f/750.0f;
    	mTitleHeight=(int)(38*mScan);
    	mCellHeight = (int) (104*mScan);
    	
    	if (actionType == ActionType.PullDown) {
    		 height=mTitleHeight+mCellHeight*6;
		}else if(actionType == ActionType.TakeBack){
			 height=mTitleHeight+mCellHeight;
		}
        mHeight=height;
    	mWidth=width;
        setMeasuredDimension(width, height);
        CELL_TEXT_SIZE = mCellHeight * 0.35f;
        CELL_TEXT_SIZE_NOLI=mCellHeight * 0.2f;
        mWeekTitle.setTextSize(mCellHeight * 0.2f);
        initCells();
        
        Log.d("xmx","mCellWidth:"+mCellWidth+" mCellHeight:"+mCellHeight);
        
	}
	
	public void resetRemark()
	{
		if (mCells !=null)
		{
		for(Cell[] week : mCells) {
			if (week!=null)
			{
			for(Cell day : week) {
				if (day!=null)
					day.setmIsMark(getMarkByDay(day.year, day.month, day.mDayOfMonth));
			}
			}
		}
		}
		invalidate();
	}
	/**重置日期*/
	public void resetRemarkMap()
	{
		if (mCells !=null)
		{
			for(Cell[] week : mCells) {
				if (week!=null)
				{
					for(Cell day : week) {
						if (day!=null)
							day.setmIsMarkRed(getMarkRedByDay(day.year, day.month, day.mDayOfMonth));
					}
				}
			}
		}
		invalidate();
	}
	
	/**
	 * 重新装载数据 刷新界面
	 */
	public void refresh(){
		initCells();
		invalidate();
	}
	
	
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		String MSG = "onDraw()";
  		
		//draw backrgound
		canvas.drawRect(getPaddingLeft(), getPaddingTop(), mWidth, mHeight, mBackgroundColor);
		// 画星期标题
		Rect tempBound = new Rect(getPaddingLeft(), getPaddingTop(), getPaddingLeft()+mCellWidth, getPaddingTop()+mTitleHeight);
		int dx,dy = 0;
		for(String str:weekTitle){
			
			dx = (int) (mWeekTitle.measureText(str)/2);
			dy = (int) (((-mWeekTitle.ascent() + mWeekTitle.descent()) / 2)-2*mScan);
			if ((str.equals("日")) || (str.equals("六")))
				mWeekTitle.setColor(Color.rgb(135, 135, 135));
			else
				mWeekTitle.setColor(Color.rgb(0, 0, 0));
			
			
			canvas.drawText(str, tempBound.centerX()-dx, tempBound.centerY()+dy, mWeekTitle);
			tempBound.offset(mCellWidth, 0);
		}
		
		// draw cells
		for (int week = 0; week < mCells.length; week++) {
			for (int day = 0; day < mCells[week].length; day++) {
				if(actionType ==ActionType.PullDown){
					mCells[week][day].draw(canvas,actionType, tempBound.centerY()+dy);
				}	else if(actionType == ActionType.TakeBack||TakeBack){
					if (NextWeek) {
						if (mRow==week+1) {
						if (week+1>mCells.length) {
							nextMonth();
						}else{
						for (int i = 0; i < mCells[week+1].length; i++) {
							mCells[week+1][i].draw(canvas,actionType, tempBound.centerY()+dy);
						}
						}
						NextWeek = false;
						mRow = mRow+1;
						}
					}else if(previousWeek){
						if (mRow==week+1) {
						if (week-1<mCells.length) {
							previousMonth();
						}else{
							for (int i = 0; i < mCells[week-1].length; i++) {
								mCells[week-1][i].draw(canvas,actionType, tempBound.centerY()+dy);
							}
						}
						mRow = mRow-1;
						previousWeek = false;
						}
					}else{
						if (mCells[week][day].mSelectDate) {
							for (int i = 0; i < mCells[week].length; i++) {
								mCells[week][i].draw(canvas,actionType, tempBound.centerY()+dy);
							}
						}
					}
				}
			}
		}
		/*
		// draw today
		if(mToday!=null){
			Rect bound = mToday.getBound();
			mBackgroundColorToday.setColor(Color.rgb(249, 133, 18));
			
			canvas.drawCircle((float)(bound.centerX()), (float)(bound.top+bound.height()*0.4140 ), (float)(bound.height()*0.3600), mBackgroundColorToday);
			
			mToday.draw(canvas);
			
			
			
			
		}
		*/
		
		
		/*
		//draw touched
		if(mTouchedCell!=null){
			Rect bound = mTouchedCell.getBound();
			mBackgroundColorToday.setColor(Color.rgb(249, 133, 18));
			
			canvas.drawCircle((float)(bound.centerX()), (float)(bound.top+bound.height()*0.4140 ), (float)(bound.height()*0.3600), mBackgroundColorToday);
			
			mTouchedCell.draw(canvas);
			
			
			
		}
		*/
		
		//draw vertical lines
		/*
		for (int c=0; c <= 7; c++) {
			float x = c * mCellWidth + getPaddingLeft();
			canvas.drawLine(x-0.5f, getPaddingTop(), x-0.5f, 7*mCellHeight+getPaddingTop(), mLinePaint2);
			canvas.drawLine(x+0.5f, getPaddingTop(), x+0.5f, 7*mCellHeight+getPaddingTop(), mLinePaint);
			
		}
		*/
		
		// draw horizontal lines
		float y=mTitleHeight;
		for (int r=0; r <= 7; r++) {
			canvas.drawLine(getPaddingLeft(), y, 7*mCellWidth+getPaddingLeft(), y, mLinePaint);
			if (r==0)
				canvas.drawLine(getPaddingLeft(), y+1, 7*mCellWidth+getPaddingLeft(), y+1, mLinePaint);
			 
//			canvas.drawLine(getPaddingLeft(), y-0.5f, 7*mCellWidth+getPaddingLeft(), y-0.5f, mLinePaint);
			//canvas.drawLine(getPaddingLeft(), y+0.5f, 7*mCellWidth+getPaddingLeft(), y+0.5f, mLinePaint2);
			y= y+ mCellHeight ;
		}
		
	}
	public void setActionType(ActionType actionType){
		this.actionType = actionType;
		refresh();
	}
	
	public void clearCellMark()
	{
		if (mCells!=null)
		{
		for(Cell[] week : mCells) {
			for(Cell day : week) {
				day.setmIsMark(false);			
			}
		}
		}
	}
	public void clearCellMarkMap()
	{
		if (mCells!=null)
		{
		for(Cell[] week : mCells) {
			for(Cell day : week) {
				day.setmIsMarkRed(false);			
			}
		}
		}
	}
	
	/**
	 * 判断是否是标记的日期
	 * @param inYear
	 * @param inMonth
	 * @param inDay
	 * @return
	 */
	private boolean getMarkByDay(int inYear,int inMonth,int inDay)
	{
		int i;
		String vStrIn=getDayStrforDay(inYear,inMonth,inDay);
		//Log.d("xmx","vStrDay:"+vStrIn);
		
		for (i = 0;  i< mMarkList.size(); i++) {
			String vStr=mMarkList.get(i);
			if (vStr.equals(vStrIn))
				return true;
		}
		return false;
	}
	/**
	 * 判断是否是标记的日期(红点)
	 * @param inYear
	 * @param inMonth
	 * @param inDay
	 * @return
	 */
	private boolean getMarkRedByDay(int inYear,int inMonth,int inDay)
	{
		String vStrIn=getDayStrforDay(inYear,inMonth,inDay);
		//Log.d("xmx","vStrDay:"+vStrIn);
		Set<Entry<String,Boolean>> entrySet = mMarkRedMap.entrySet();
		for (Entry<String, Boolean> entry : entrySet) {
			String key = entry.getKey();
			Boolean value = entry.getValue();
			if (key.equals(vStrIn)&&value)
				return true;
		}
		return false;
	}
	
	/**
	 * 判断是否是选中的日期(指定日期是否在选中日期列表中存在)
	 * @param inYear
	 * @param inMonth
	 * @param inDay
	 * @return
	 */
	private boolean getSelectByDay(int inYear,int inMonth,int inDay)
	{
		int i;
		String vStrIn=getDayStrforDay(inYear,inMonth,inDay);
		//Log.d("xmx","vStrDay:"+vStrIn);
		
		for (i = 0;  i< mSelectDateList.size(); i++) {
			String vStr = mSelectDateList.get(i);
			if (vStr.equals(vStrIn))
				return true;
		}
		return false;
	}
	
	public void setSelDayByDay(int inYear,int inMonth,int inDay)
	{
		if (mCells!=null)
		{
			for(Cell[] week : mCells) {
				for(Cell day : week) {
					if (day.year==inYear && day.month==inMonth && day.mDayOfMonth==inDay)
					{
						if (mTouchedCell != null)
							mTouchedCell.mSelectDate=false;
						
							mTouchedCell =day;
							mTouchedCell.mSelectDate=true;
						
						mSelYear=mTouchedCell.year;
						mSelMonth=mTouchedCell.month;
						mSelDay=mTouchedCell.mDayOfMonth;
					}
				}
			}
			invalidate();
		}
	}
	
	/**
	 * 把年月日拼成字符串格式    ####年##月##日
	 * @param inYear
	 * @param inMonth
	 * @param inDay
	 * @return
	 */
	private static String getDayStrforDay(int inYear,int inMonth,int inDay)
	{
		String vMonth="0"+inMonth;
		vMonth=vMonth.substring(vMonth.length()-2, vMonth.length());
		String vDay="0"+inDay;
		vDay=vDay.substring(vDay.length()-2, vDay.length());
		return inYear+vMonth+vDay;
	}
	
	//	原来的方法
//	public void getCellAtPoint(int x, int y){
//		int lx = x - getPaddingLeft();
//		int ly = y - getPaddingTop();
//		
//		int row = (int) ((ly-mTitleHeight) / mCellHeight)+1;
//		int col = (int) (lx / mCellWidth);
//		
//		if(col>=0 && col<7 && row>=1 && row<7){
//			//mToday=mCells[row-1][col];
//			if (mTouchedCell != null)
//				mTouchedCell.mSelectDate=false;
//			
//			mTouchedCell = mCells[row-1][col];
//			mTouchedCell.mSelectDate=true;
//			
//			mSelYear=mTouchedCell.year;
//			mSelMonth=mTouchedCell.month;
//			
//			mSelDay=mTouchedCell.mDayOfMonth;
//			
//			invalidate();
//		}else {
//			//mTouchedCell = null;
//		}
//	}
	
	/**
	 * 得到选中的cell后  并做相应的处理
	 * @param x
	 * @param y
	 * @return 
	 */
	public int getCellAtPoint(int x, int y){
		int lx = x - getPaddingLeft();
		int ly = y - getPaddingTop();
		int row = 0;
		int col = 0;
		if (TakeBack) {
			row = mRow;
		}else{
			row = (int) ((ly-mTitleHeight) / mCellHeight)+1;
		}
		col = (int) (lx / mCellWidth);
		if(col>=0 && col<7 && row>=1 && row<7){
			
			// 得到选中的Cell
			mTouchedCell = mCells[row-1][col];
			
			// 把 选中的cell的年月日 赋值给本地其它变量
			mSelYear=mTouchedCell.year;
			mSelMonth=mTouchedCell.month;
			mSelDay=mTouchedCell.mDayOfMonth;
			
			
//			if (painterType == PainterType.Myself) {

				// 得到选中日期字符串
				String selectDataStr = getDayStrforDay(mSelYear, mSelMonth, mSelDay);
				// 单选
				if (operateType == OperateType.Radio) {

					mSelectDateList.clear();
					mSelectDateList.add(selectDataStr);

				} 
				// 多选
				else if (operateType == OperateType.MultipleChoice) {

					// 这里是新选择处理 要判断
					// 如果当前选中列表中是否已经存在选中那天
					boolean isExist = getSelectByDay(mTouchedCell.year, mTouchedCell.month, mTouchedCell.mDayOfMonth);

					// 2 如果已经存在 把选中的日期从列表中删除
					if (isExist) {

						deleteSelectDateListByYearMonthDay(selectDataStr);

					}
					// 1 如果不存在 把选中的 日期加入到选中列表里
					else {

						mSelectDateList.add(selectDataStr);

					}
					
					
				} 
				//	不处理数据
				else if (operateType == OperateType.Nothing){
					
					
					
				}
				
				
//				重新装载数据
				initCells();
				//	刷新view界面
				invalidate();

			
//			else if (painterType == PainterType.Other ){
//				
//				
//				
//			}
			
			
			
		}
		else {
			//mTouchedCell = null;
		}
		return row;
	}
	
	/**
	 * 删除选中的日期在选择列表中
	 */
	public void deleteSelectDateListByYearMonthDay(String inYearMonDay)
	{
		int i=mSelectDateList.size()-1;
		while (i>=0)
		{
			String vStr=mSelectDateList.get(i);
			if (vStr.substring(0, 8).equals(inYearMonDay))
			{
				Log.d("xmx","mSelectDateList:"+vStr+",inYearMonDay:"+inYearMonDay);
				mSelectDateList.remove(i);
			}
			i=i-1;
		}
	}
	
	private class GrayCell extends Cell {
		public GrayCell(int year, int month, int dayOfMon, Rect rect, float s,float sn) {
			super(year, month, dayOfMon, rect, s,sn);
			mTextColor=Color.rgb(162, 162, 162);
		}
	}
	
	
	private class LTGrayCell extends Cell {
		public LTGrayCell(int year, int month, int dayOfMon, Rect rect, float s,float sn) {
			super(year, month, dayOfMon, rect, s,sn);
			mTextColor=Color.rgb(162, 162, 162);
		}
	}
	
	private class RedCell extends Cell {
		public RedCell(int year, int month, int dayOfMon, Rect rect, float s,float sn) {
			super(year, month, dayOfMon, rect, s,sn);
			mTextColor=0xdddd0000;
			//mPaint.setColor(0xdddd0000);
		}
		
	}
	
	public Cell getmTouchedCell() {
		return mTouchedCell;
	}
	
	public void setmTouchedCell(Cell mTouchedCell) {
		this.mTouchedCell = mTouchedCell;
	}

	public void setMonthChangeListener(OnMonthChangeListener monthChangeListener) {
		this.monthChangeListener = monthChangeListener;
	}

	public boolean ismCurView() {
		return mCurView;
	}

	public void setmCurView(boolean mCurView) {
		this.mCurView = mCurView;
	}

	public Map<String, Boolean> getmMarkRedMap() {
		return mMarkRedMap;
	}
	public void setmMarkRedMap(Map<String, Boolean> mMarkRedMap) {
		this.mMarkRedMap = mMarkRedMap;
	}

	public interface OnMonthChangeListener{
		public void onMonthChanged(boolean isNext);
	}
	/**
	 * 设置操作类
	 */
	public void setOperateType(OperateType operateType) {
		this.operateType = operateType;
	}
	
	public List<String> getSelectDateList() {
		return mSelectDateList;
	}
	/**设置选中日期列表*/
	public void setSelectDateList(List<String> mSelectDateList) {
		this.mSelectDateList = mSelectDateList;
		refresh();
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (TakeBack) {
			int x = (int)event.getX();
			int y = (int)event.getY();
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mOldx=x;
				mOldy=y;
				hasmoved = false;
				//mainView.getCellAtPoint(x, y);
				break;
			case MotionEvent.ACTION_MOVE:
				hasmoved = true;
				//mainView.getCellAtPoint(x, y);
				
				break;
				
			case MotionEvent.ACTION_UP:
					
				//if(!hasmoved && dayClickListener!=null)
				if (Math.abs(x-mOldx+y-mOldy)<20 )
				{
					Log.d("xmx","ACTION_UP");
					this.getCellAtPoint(x, y);
					if (TakeBackdayClickListener!=null)
						TakeBackdayClickListener.onTakeBackDayClick(this.mTouchedCell);
				}
//				ArrayList<String> list = new  ArrayList<String>();
//				list.add(getDayStrforDay(this.mTouchedCell.getYear(), this.mTouchedCell.getMonth(), this.mTouchedCell.mDayOfMonth));
//				this.setSelectDateList(list);
//				if (y-mOldy>this.getHeight()/2) {
//					previousWeek = true;
//					invalidate();
//				}else if(mOldy-y<this.getHeight()/2){
//					NextWeek = true ;
//					invalidate();
//				}
				break;
				
			case MotionEvent.ACTION_CANCEL:
				this.setmTouchedCell(null);
				break;
				
			default:
				break;
			}
			
			postInvalidate();
			return true;
		}
		return super.onTouchEvent(event);
	}
	public interface OnTakeBackDayClickListener{
		public void onTakeBackDayClick(Cell touchedCell);
	}

	public OnTakeBackDayClickListener getTakeBackdayClickListener() {
		return TakeBackdayClickListener;
	}
	public void setTakeBackdayClickListener(
			OnTakeBackDayClickListener takeBackdayClickListener) {
		TakeBackdayClickListener = takeBackdayClickListener;
	}
	public int getmRow() {
		return mRow;
	}
	public void setmRow(int mRow) {
		this.mRow = mRow;
	}
	
	
}
