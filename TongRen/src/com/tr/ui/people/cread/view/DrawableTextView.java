package com.tr.ui.people.cread.view;

import java.lang.ref.WeakReference;  
  
import android.content.Context;  
import android.graphics.Bitmap;  
import android.graphics.BitmapFactory;  
import android.graphics.Canvas;  
import android.graphics.Color;  
import android.graphics.Rect;  
import android.util.AttributeSet;  
import android.view.MotionEvent;  
import android.widget.TextView;  
  
public class DrawableTextView extends TextView {  
  
    private WeakReference<Bitmap> normalReference;  
    private WeakReference<Bitmap> pressReference;  
    private WeakReference<Bitmap> showReference;  
  
    private int normalColor = Color.WHITE, pressColor = Color.WHITE;  
  
    private String text;  
    private int textWidth = 0;  
    private int textHeight = 0;  
  
    public DrawableTextView(Context context) {  
        super(context);  
    }  
  
    public DrawableTextView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
  
    public DrawableTextView(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
    }  
  
    @Override  
    protected void onFinishInflate() {  
        super.onFinishInflate();  
        initText();  
    }  
  
    private void initText() {  
        text = super.getText().toString();  
        initVariable();  
    }  
  
    /** 
     * 初始化，测量Textview内容的长度，高度 
     */  
    private void initVariable() {  
        textWidth = (int) (getPaint().measureText(text));  
        final Rect rect = new Rect();  
        getPaint().getTextBounds(text, 0, 1, rect);  
        textHeight = rect.height();  
    }  
  
    /** 
     * 设置TextView的内容 
     * @param text 
     */  
    public void setText(String text) {  
        this.text = text;  
        initVariable();  
        invalidate();  
    }  
  
    /** 
     * 获取TextView内容 
     */  
    public String getText() {  
        return text;  
    }  
  
    /** 
     * 设置TextView的Drawable内容，目前仅支持DrawableLeft 
     * @param normalDrawableId 
     *              DrawableLeft的normal状态Id 
     * @param pressDrawableId 
     *              DrawableLeft的press状态的Id（没有press状态，请传-1） 
     */  
    public void setDrawableLeftId(final int normalDrawableId, final int pressDrawableId) {  
        normalReference = new WeakReference<Bitmap>(BitmapFactory.decodeResource(getResources(), normalDrawableId));  
        if (pressDrawableId != -1) {  
            pressReference = new WeakReference<Bitmap>(BitmapFactory.decodeResource(getResources(), pressDrawableId));  
        }  
        showReference = normalReference;  
        invalidate();  
    }  
  
    /** 
     * 设置TextView的Color 
     * @param normalColor 
     *              TextView normal状态的Color值 
     * @param pressDrawableId 
     *              TextView press状态的Color值（如果没有press状态，请传与normal状态的值） 
     */  
    public void setTextColor(final int normalColor, final int pressColor) {  
        this.normalColor = normalColor;  
        this.pressColor = pressColor;  
        getPaint().setColor(normalColor);  
        initVariable();  
    }  
  
    @Override  
    protected void onDraw(Canvas canvas) {  
        if (showReference != null && showReference.get() != null) {  
            final int bitmapWidth = showReference.get().getWidth();  
            final int bitmapHeight = showReference.get().getHeight();  
            final int viewHeight = getHeight();  
            final int drawablePadding = getCompoundDrawablePadding();  
            final int start = (getWidth() - (bitmapWidth + drawablePadding + textWidth)) >> 1;  
            canvas.drawBitmap(showReference.get(), start, (viewHeight >> 1) - (bitmapHeight >> 1), getPaint());  
              
            /** 
             * 注意改方法，第三个参数y，本人也被误导了好久，原来在画文字的时候，y表示文字最后的位置（不是下笔点的起始位置） 
             * 所以为什么 是TextView高度的一半（中间位置） + 文字高度的一半 = 文字居中 
             */  
            canvas.drawText(text, start + drawablePadding + bitmapWidth, (viewHeight >> 1) + (textHeight >> 1), getPaint());  
        }  
    }  
  
    @Override  
    public boolean onTouchEvent(MotionEvent event) {  
        if (event.getAction() == MotionEvent.ACTION_DOWN) {  
            if (pressReference != null && pressReference.get() != null) {  
                showReference = pressReference;  
            }  
            getPaint().setColor(pressColor);  
        } else if (event.getAction() == MotionEvent.ACTION_UP) {  
            if (normalReference != null && normalReference.get() != null) {  
                showReference = normalReference;  
            }  
            getPaint().setColor(normalColor);  
        }  
        invalidate();  
        return super.onTouchEvent(event);  
    }  
  
}  