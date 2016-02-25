package com.tr.ui.work;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.tr.ui.knowledge.GlobalKnowledgeTagActivity.OperateType;
import com.tr.ui.work.CalendarView.ActionType;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class CalendarLayout extends RelativeLayout implements OnGestureListener{

	private static final String TAG = "CalendarLayout";
	
//	public static final int 
	
	public enum OperateType {
		/** 单选返回 */
		Radio,
		/** 正常多选 */
		MultipleChoice,
		/** 什么都不做 自己处理	*/
		Nothing
	}

	private OperateType operateType = OperateType.Radio;
	
	
	
	private CalendarView mainView;
	private CalendarView anotherView;
	private GestureDetector gestureDetector;
	private OnDayClickListener dayClickListener;
	private boolean hasmoved;
	private boolean isAnimationStarted = false;
	private static final int SWIPE_MIN_DISTANCE = 60;
	private static final int SWIPE_THRESHOLD_VELOCITY = 180; 
	
	private int mOldx=0,mOldy=0;
	
	/**	备注列表	*/
	private List<String> mMarkList;
	/**	备注红点列表	*/
	public Map<String,Boolean> mMarkRedMap;
	/**	选中日期列表	*/
	public List<String> mSelectDateList;
	
	private int mMonthAdd;



//	private CalendarView takeBackView;



	private ActionType actionType;






	
	public CalendarLayout(Context context) {
		this(context, null);
	}

	public CalendarLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mMarkList = new  ArrayList<String>();
		mMarkRedMap = new HashMap<String, Boolean>();
		mSelectDateList = new  ArrayList<String>();
		// 这些是模拟数据
//		mSelectDateList.add("20150707");
//		mSelectDateList.add("20150708");
//		mSelectDateList.add("20150709");
//		mSelectDateList.add("20150710");
		
		
		
		
		mainView = new CalendarView(context, null);
		mainView.mMarkList = mMarkList;
		mainView.mMarkRedMap=mMarkRedMap;
		// 设置选中日期列表
		mainView.mSelectDateList = mSelectDateList;
		
		mainView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		mainView.setmCurView(true);
		
		
		anotherView = new CalendarView(context, null);
		anotherView.mMarkList=mMarkList;
		anotherView.mMarkRedMap=mMarkRedMap;
		// 设置选中日期列表
		anotherView.mSelectDateList = mSelectDateList;
		anotherView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
//		takeBackView = new CalendarView(context, null);
//		takeBackView.mMarkList=mMarkList;
//		// 设置选中日期列表
//		takeBackView.mSelectDateList = mSelectDateList;
//		takeBackView.setActionType(ActionType.TakeBack);
//		takeBackView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
//				LayoutParams.FILL_PARENT));
//		View view = takeBackView;
//		float[] f = new float[2];
//		f[0] = 0.0F;
//		f[1] = -takeBackView.getHeight();
//		ObjectAnimator animator1 = ObjectAnimator.ofFloat(view,
//				"translationY", f);
//		animator1
//				.setInterpolator(new AccelerateDecelerateInterpolator());
//		animator1.setDuration(50);
//		animator1.start();
		
		// 这里写单选时初始化选中日期的数据
		if (operateType == OperateType.Radio) {
//			mSelectDateList.add(CalendarView.getTodayDate());
			mainView.setOperateType(CalendarView.OperateType.Radio);
			anotherView.setOperateType(CalendarView.OperateType.Radio);
//			takeBackView.setOperateType(CalendarView.OperateType.Radio);
		
		}
		else if (operateType == OperateType.MultipleChoice){
			mainView.setOperateType(CalendarView.OperateType.MultipleChoice);
			anotherView.setOperateType(CalendarView.OperateType.MultipleChoice);
//			takeBackView.setOperateType(CalendarView.OperateType.MultipleChoice);
		}
		else if (operateType == OperateType.Nothing){
			mainView.setOperateType(CalendarView.OperateType.Nothing);
			anotherView.setOperateType(CalendarView.OperateType.Nothing);
//			takeBackView.setOperateType(CalendarView.OperateType.Nothing);
		}
		
//		this.addView(takeBackView);
		this.addView(anotherView);
		this.addView(mainView);
		gestureDetector = new GestureDetector(this);
	}
	/**设置操作日历的动作的类型*/
	public void setActionType(ActionType actionType){
		this.actionType = actionType;
		if (actionType ==ActionType.PullDown) {
//			mainView.setVisibility(View.VISIBLE);
//			anotherView.setVisibility(View.VISIBLE);
//			View view = takeBackView;
//			float[] f = new float[2];
//			f[0] = 0.0F;
//			f[1] = -takeBackView.getHeight();
//			ObjectAnimator animator1 = ObjectAnimator.ofFloat(view,
//					"translationY", f);
//			animator1
//					.setInterpolator(new AccelerateDecelerateInterpolator());
//			animator1.setDuration(400);
//			animator1.start();
			View view2 = mainView;
			float[] f1 = new float[2];
			f1[0] = -mainView.getHeight() ;
			f1[1] = 0.0F;
			ObjectAnimator animator2 = ObjectAnimator.ofFloat(view2,
					"translationY", f1);
			animator2
					.setInterpolator(new AccelerateDecelerateInterpolator());
			animator2.setDuration(400);
			animator2.addListener(mAnimatorListener);
			animator2.start();
			View view3 = anotherView;
			ObjectAnimator animator3 = ObjectAnimator.ofFloat(view3,
					"translationY", f1);
			animator3
					.setInterpolator(new AccelerateDecelerateInterpolator());
			animator3.setDuration(400);
			animator3.start();
			
		}else if(actionType == ActionType.TakeBack){
//			mainView.setVisibility(View.GONE);
//			anotherView.setVisibility(View.GONE);
//			View view = takeBackView;
//			float[] f = new float[2];
//			f[0] = -takeBackView.getHeight();
//			f[1] = 0F;
//			ObjectAnimator animator1 = ObjectAnimator.ofFloat(view,
//					"translationY", f);
//			animator1.setDuration(400);
//			animator1
//					.setInterpolator(new AccelerateDecelerateInterpolator());
//			animator1.start();
			
			View view2 = mainView;
			float[] f1 = new float[2];
			f1[0] =  0.0F;
			f1[1] = -mainView.getHeight()+mainView.getHeight()/5.5f ;
			ObjectAnimator animator2 = ObjectAnimator.ofFloat(view2,
					"translationY", f1);
			animator2
					.setInterpolator(new AccelerateDecelerateInterpolator());
			animator2.setDuration(400);
			animator2.addListener(mAnimatorListener);
			animator2.start();
			View view3 = anotherView;
			ObjectAnimator animator3 = ObjectAnimator.ofFloat(view3,
					"translationY", f1);
			animator3
					.setInterpolator(new AccelerateDecelerateInterpolator());
			animator3.setDuration(400);
			animator3.start();
		}
		mainView.setActionType(actionType);
		setSelectDateList(mSelectDateList);
	}
	/**设置点击天的监听*/
	public void setDayClickListener(OnDayClickListener dayClickListener) {
		this.dayClickListener = dayClickListener;
	}

	public CalendarView getMainView() {
		return mainView;
	}

	public CalendarView getAnotherView() {
		return anotherView;
	}
	
	/**************************************************
	 * 设置选择类型
	 * @param	OperateType operateType	单选或多选
	 ***************************************************/
	public void setOperateType(OperateType operateType) {
		this.operateType = operateType;
		
		if (operateType == OperateType.Radio) {
			
			mainView.setOperateType(CalendarView.OperateType.Radio);
			anotherView.setOperateType(CalendarView.OperateType.Radio);
//			takeBackView.setOperateType(CalendarView.OperateType.Radio);
		}
		else if (operateType == OperateType.MultipleChoice){
			
			mainView.setOperateType(CalendarView.OperateType.MultipleChoice);
			anotherView.setOperateType(CalendarView.OperateType.MultipleChoice);
//			takeBackView.setOperateType(CalendarView.OperateType.MultipleChoice);
		}
		else if (operateType == OperateType.Nothing){
			
			mainView.setOperateType(CalendarView.OperateType.Nothing);
			anotherView.setOperateType(CalendarView.OperateType.Nothing);
//			takeBackView.setOperateType(CalendarView.OperateType.Nothing);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gestureDetector.onTouchEvent(event);
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
				int row = mainView.getCellAtPoint(x, y);
				if (dayClickListener!=null)
					dayClickListener.onDayClick(mainView.mTouchedCell,row);
			}
			setSelectDateList(mSelectDateList);
			break;
			
		case MotionEvent.ACTION_CANCEL:
			mainView.setmTouchedCell(null);
			break;
			
		default:
			break;
		}
		
//		postInvalidate();
		return true;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if(isAnimationStarted)
			return false;
		if (e2.getY()-e1.getY()>SWIPE_MIN_DISTANCE && Math.abs(velocityY)>SWIPE_THRESHOLD_VELOCITY) {
			anotherView.mSelYear=mainView.mSelYear;
			anotherView.mSelMonth=mainView.mSelMonth;
			anotherView.mSelDay=mainView.mSelDay;

			anotherView.previousMonth();
			Animation hideAnimation = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 1.0f);
			hideAnimation.setDuration(500);
			mainView.startAnimation(hideAnimation);
			
			Animation showAnimation = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, -1.0f,
					Animation.RELATIVE_TO_SELF, 0.0f);
			showAnimation.setDuration(500);
			anotherView.startAnimation(showAnimation);
			showAnimation.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					isAnimationStarted = true;
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					mainView.previousMonth();
					isAnimationStarted = false;
				}
			});
			Log.i(TAG, "velocityY=" + velocityY);
		}
		if(e1.getY()-e2.getY()>SWIPE_MIN_DISTANCE && Math.abs(velocityY)>SWIPE_THRESHOLD_VELOCITY){
			anotherView.mSelYear=mainView.mSelYear;
			anotherView.mSelMonth=mainView.mSelMonth;
			anotherView.mSelDay=mainView.mSelDay;
			
			anotherView.nextMonth();
			Animation hideAnimation = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, -1.0f);
			hideAnimation.setDuration(500);
			mainView.startAnimation(hideAnimation);
			
			Animation showAnimation = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 1.0f,
					Animation.RELATIVE_TO_SELF, 0.0f);
			showAnimation.setDuration(500);
			anotherView.startAnimation(showAnimation);
			showAnimation.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					isAnimationStarted = true;
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					mainView.nextMonth();
					isAnimationStarted = false;
				}
			});
			
			Log.i(TAG, "velocityY=" + velocityY);
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
	
	public interface OnDayClickListener{
		public void onDayClick(Cell touchedCell, int row);
	}
	
	public void clearMarkList()
	{
		mMarkList.clear();
	}
	public void clearMarkRedMap()
	{
		mMarkRedMap.clear();
	}

	public void addMarkList(List<String> inMarkList)
	{
		int i;
		
		if (inMarkList.size()>0)
		{
			String vStrOld=inMarkList.get(0);
			String vStrMon=vStrOld.substring(0,6);
			deleteYearMonth(vStrMon);
		}
		mMarkList.clear();
		for (i = 0;  i< inMarkList.size(); i++) {
			String vStr=inMarkList.get(i);
			Log.d("xmx","addmark:"+vStr);
			mMarkList.add(vStr);
		}
		mainView.resetRemark();
		anotherView.resetRemark();
		refresh();
	}
	
	public void addMarkRedMap(HashMap<String,Boolean> inMarkMap)
	{
		mMarkRedMap.clear();
		if (inMarkMap.size()>0)
		{
			mMarkRedMap.putAll(inMarkMap);
		}
		mainView.resetRemarkMap();
		anotherView.resetRemarkMap();
		refresh();
	}
	
	public void deleteYearMonthMap(String inYearMon)
	{
		int i=mMarkRedMap.size()-1;
		while (i>=0)
		{
			Set<Entry<String,Boolean>> entrySet = mMarkRedMap.entrySet();
			Iterator<Entry<String, Boolean>> iterator = entrySet.iterator();
			if(iterator.hasNext()) {
				Entry<String, Boolean> entry = iterator.next();
				String key = entry.getKey();
				if (key.substring(0, 6).equals(inYearMon))
				{
				Log.d("xmx","deleteYearMonth:"+key+",inYearMon:"+inYearMon);
				mMarkRedMap.remove(i);
				}
			i=i-1;
			}
		}
	}
	
	public void deleteYearMonth(String inYearMon)
	{
		int i=mMarkList.size()-1;
		while (i>=0)
		{
			String vStr=mMarkList.get(i);
			if (vStr.substring(0, 6).equals(inYearMon))
			{
				Log.d("xmx","deleteYearMonth:"+vStr+",inYearMon:"+inYearMon);
				mMarkList.remove(i);
			}
			i=i-1;
		}
	}
	
	
	public void setYearMonth(int inYear,int inMonth,int inDay)
	{
		int j;
		int vYear=inYear;
		int vMonth=inMonth;
		int vYearMonth=inYear*100+inMonth;
		int vCaYear=anotherView.mHelper.getYear();
		int vCaMonth=anotherView.mHelper.getMonth()+1;
		int vCaYearMonth=vCaYear*100+vCaMonth;
		mMonthAdd=1;
		
		if (vYearMonth>vCaYearMonth)
		{
			mMonthAdd=(vYear-vCaYear)*12+vMonth-vCaMonth;
			
			anotherView.mSelYear=mainView.mSelYear;
			anotherView.mSelMonth=mainView.mSelMonth;
			anotherView.mSelDay=mainView.mSelDay;
			
			for (j=0;j<mMonthAdd;j++)
				anotherView.nextMonth();
			anotherView.setSelDayByDay(inYear, inMonth, inDay);
			Animation hideAnimation = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, -1.0f);
			hideAnimation.setDuration(500);
			mainView.startAnimation(hideAnimation);
			
			Animation showAnimation = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 1.0f,
					Animation.RELATIVE_TO_SELF, 0.0f);
			showAnimation.setDuration(500);
			anotherView.startAnimation(showAnimation);
			showAnimation.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					isAnimationStarted = true;
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					int j;
					for (j=0;j<mMonthAdd;j++)
						mainView.nextMonth();
					
					mainView.setSelDayByDay(anotherView.mSelYear, anotherView.mSelMonth, anotherView.mSelDay);
					isAnimationStarted = false;
				}
			});
			
		}
		else if (vYearMonth<vCaYearMonth)
		{
			mMonthAdd=(vCaYear-vYear)*12+vCaMonth-vMonth;
			
			anotherView.mSelYear=mainView.mSelYear;
			anotherView.mSelMonth=mainView.mSelMonth;
			anotherView.mSelDay=mainView.mSelDay;

			for (j=0;j<mMonthAdd;j++)
				anotherView.previousMonth();
			anotherView.setSelDayByDay(inYear, inMonth, inDay);
			Animation hideAnimation = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 1.0f);
			hideAnimation.setDuration(500);
			mainView.startAnimation(hideAnimation);
			
			Animation showAnimation = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, -1.0f,
					Animation.RELATIVE_TO_SELF, 0.0f);
			showAnimation.setDuration(500);
			anotherView.startAnimation(showAnimation);
			showAnimation.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					isAnimationStarted = true;
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					int j;
					for (j=0;j<mMonthAdd;j++)
						mainView.previousMonth();
					mainView.setSelDayByDay(anotherView.mSelYear, anotherView.mSelMonth, anotherView.mSelDay);
					isAnimationStarted = false;
				}
			});
		}
		else
		{
			//==
			Log.d("xmx","cur month ");
			mainView.setSelDayByDay(inYear, inMonth, inDay);
			//invalidate();
			
		}
		
	}
	
	/**	设置选择列表数 并通知刷新	*/
	public void setSelectDateList(List<String> selectDataList){
		mSelectDateList = selectDataList;
		mainView.setSelectDateList(mSelectDateList);
		anotherView.setSelectDateList(mSelectDateList);
//		takeBackView.setSelectDateList(mSelectDateList);
		refresh();
	}
	
	/**
	 * 刷新自己 及  两个 CalendarView
	 */
	public void refresh(){
		mainView.refresh();
		anotherView.refresh();
//		takeBackView.refresh();
		invalidate();
		
	}
	Animator.AnimatorListener mAnimatorListener= new Animator.AnimatorListener()
	{

		@Override
		public void onAnimationCancel(Animator arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationEnd(Animator arg0) {
//			mainView.setActionType(actionType);
//			setSelectDateList(mSelectDateList);
//			if (actionType == ActionType.PullDown) {
//				View view = takeBackView;
//				float[] f = new float[2];
//				f[0] = 0F;
//				f[1] = -mainView.getHeight() ;
//				ObjectAnimator animator1 = ObjectAnimator.ofFloat(view,
//						"translationY", f);
//				animator1.setDuration(400);
//				animator1
//						.setInterpolator(new AccelerateDecelerateInterpolator());
//				animator1.start();
//			}else if(actionType == ActionType.TakeBack){
//				
//				View view = mainView;
//				float[] f = new float[2];
//				f[0] = -mainView.getHeight();
//				f[1] = 0F;
//				ObjectAnimator animator1 = ObjectAnimator.ofFloat(view,
//						"translationY", f);
//				animator1.setDuration(400);
//				animator1
//						.setInterpolator(new AccelerateDecelerateInterpolator());
//				animator1.start();
//			}
		}

		@Override
		public void onAnimationRepeat(Animator arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationStart(Animator arg0) {
			
		}
		
	};
}
 