package com.tr.ui.widgets;

import com.tr.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ScrollViewHeader extends RelativeLayout {  
    
    public final static int STATE_NORMAL = 0;  
    public final static int STATE_READY = 1;  
    public final static int STATE_REFRESHING = 2;  
    private final int ROTATE_ANIM_DURATION = 180;  
    private int topMargin = 0;  
	private ImageView mArrowImageView;
	private TextView mHintTextView;
	private ImageView mProgressBar;
	private int mState = STATE_NORMAL;
	private RotateAnimation mRotateUpAnim;
	private RotateAnimation mLoadingAni;
	private RotateAnimation mRotateDownAnim;  
      
    public ScrollViewHeader(Context context) {  
        super(context);  
        // TODO Auto-generated constructor stub  
        if(!isInEditMode())   
            initView(context);  
    }  
  
    public ScrollViewHeader(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        // TODO Auto-generated constructor stub  
        if(!isInEditMode())  
            initView(context);  
    }  
  
    public ScrollViewHeader(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
        // TODO Auto-generated constructor stub  
        if(!isInEditMode())  
            initView(context);  
    }  
  
    /** 
     * 初始化相关的view 
     */  
    public void initView(Context context) {  
        setPadding(10, 25, 10, 25);  
        View view = LayoutInflater.from(context).inflate(R.layout.scrollview_header, this, true);  
    	mArrowImageView = (ImageView) view.findViewById(R.id.xlistview_header_arrow);
		mHintTextView = (TextView)view. findViewById(R.id.xlistview_header_hint_textview);
		mProgressBar = (ImageView)view. findViewById(R.id.xlistview_header_progressbar);
		mProgressBar.setVisibility(View.GONE);
		mRotateUpAnim = new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateUpAnim.setFillAfter(true);
		mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateDownAnim.setFillAfter(true);

		mLoadingAni = new RotateAnimation(0f, 359f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mLoadingAni.setDuration(1000);
		mLoadingAni.setRepeatCount(Animation.INFINITE);
		mLoadingAni.setRepeatMode(Animation.RESTART);
		mLoadingAni.setInterpolator(new LinearInterpolator());

    }  
      
    /** 
     * 设置scrollviewHeader的状态 
     * @param state 
     */  
    public void setState(int state) {
		if (state == mState)
			return;

		if (state == STATE_REFRESHING) { // 显示进度
			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.INVISIBLE);
			mProgressBar.setVisibility(View.VISIBLE);
			mProgressBar.setAnimation(mLoadingAni);
			mLoadingAni.startNow();
		} else { // 显示箭头图片
			mArrowImageView.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.GONE);
			mProgressBar.clearAnimation();
		}

		switch (state) {
		case STATE_NORMAL:
			if (mState == STATE_READY) {
				mArrowImageView.startAnimation(mRotateDownAnim);
			}
			if (mState == STATE_REFRESHING) {
				mArrowImageView.clearAnimation();
			}
			mHintTextView.setText(R.string.xlistview_header_hint_normal);
			break;
		case STATE_READY:
			if (mState != STATE_READY) {
				mArrowImageView.clearAnimation();
				mArrowImageView.startAnimation(mRotateUpAnim);
				mHintTextView.setText(R.string.xlistview_header_hint_ready);
			}
			break;
		case STATE_REFRESHING:
			mHintTextView.setText(R.string.xlistview_header_hint_loading);
			break;
		default:
		}

		mState = state;
	}
      
    /** 
     * 更新header的margin 
     * @param margin 
     */  
    public void updateMargin(int margin) {  
        //这里用Linearlayout的原因是Headerview的父控件是scrollcontainer是一个linearlayout   
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this.getLayoutParams();  
        params.topMargin = margin;  
        topMargin = margin;  
        setLayoutParams(params);  
    }  
      
    /** 
     * 获取header的margin 
     * @return 
     */  
    public int getTopMargin() {  
        return topMargin;  
    }  
}  
