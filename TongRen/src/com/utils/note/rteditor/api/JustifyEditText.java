package com.utils.note.rteditor.api;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by zhangchangwei on 2015/8/14.
 */
public class JustifyEditText extends EditText {
    private float mLineY;
    private int mViewWidth;
    private boolean mIsJustify;

    public JustifyEditText(Context context) {
        super(context);
    }

    public JustifyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JustifyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setJustify(Boolean is) {
        mIsJustify = is;
    }

    public boolean getIsJustify() {
        return mIsJustify;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        /*super.onDraw(canvas);
        if(mIsJustify){
            TextPaint paint = getPaint();
            paint.setColor(getCurrentTextColor());
            paint.drawableState = getDrawableState();
            mViewWidth = getMeasuredWidth();
            String text = getText().toString();
            final Layout layout = getLayout();
            mLineY = layout.getLineTop(0) + 5;
            mLineY += getTextSize();
            for (int i = 0; i < layout.getLineCount(); i++) {
                int lineStart = layout.getLineStart(i);
                int lineEnd = layout.getLineEnd(i);
                String line = text.substring(lineStart, lineEnd);

                float width = StaticLayout.getDesiredWidth(text, lineStart, lineEnd, getPaint());
                if(text.length() == 0){
                    return;
                }
                char c = text.charAt(lineEnd >= text.length()?text.length() -1:lineEnd);
                if (c != '\n'&& needScale(line,width,c)) {
                    drawScaledText(canvas, lineStart, line, width,i);
                } else {
//                        canvas.drawText(line, 0, mLineY, paint);
                }

                mLineY += getLineHeight();
            }
        }*/
        /*else{
            super.onDraw(canvas);
        }*/
        super.onDraw(canvas);
        if (mIsJustify) {
            TextPaint paint = getPaint();
            paint.setColor(getCurrentTextColor());
            paint.drawableState = getDrawableState();
            mViewWidth = getMeasuredWidth();
            String text = getText().toString();
            mLineY = 0;
            mLineY += getTextSize();
            Layout layout = getLayout();

            // layout.getLayout()在4.4.3出现NullPointerException
            if (layout == null) {
                return;
            }

            Paint.FontMetrics fm = paint.getFontMetrics();

            int textHeight = (int) (Math.ceil(fm.descent - fm.ascent));
            textHeight = (int) (textHeight * layout.getSpacingMultiplier() + layout
                    .getSpacingAdd());
            //解决了最后一行文字间距过大的问题
            for (int i = 0; i < layout.getLineCount(); i++) {
                int lineStart = layout.getLineStart(i);
                int lineEnd = layout.getLineEnd(i);
                float width = StaticLayout.getDesiredWidth(text, lineStart,
                        lineEnd, getPaint());
                String line = text.substring(lineStart, lineEnd);

                if (i < layout.getLineCount() - 1) {
                    if (needScale(line)) {
                        drawScaledText(canvas, lineStart, line, width, i);
                    } else {
//                    canvas.drawText(line, 0, mLineY, paint);
                    }
                } else {
//                canvas.drawText(line, 0, mLineY, paint);
                }
                mLineY += textHeight;
            }

        }
    }

    private void drawScaledText(Canvas canvas, int lineStart, String line, float lineWidth, int index) {
        /*Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        Layout layout = getLayout();
        int top = layout.getLineTop(index);
        int bottom = layout.getLineBottom(index);
        canvas.drawRect(0,top,mViewWidth,bottom,paint);
        layout.increaseWidthTo(mViewWidth);
        float x = 0;
        *//*if (isFirstLineOfParagraph(lineStart, line)) {
            String blanks = " ";
//            canvas.drawText(blanks, x, mLineY, getPaint());
            canvas.drawText(blanks, x, top + getTextSize(), getPaint());
            float bw = StaticLayout.getDesiredWidth(blanks, getPaint());
            x += bw;

            line = line.substring(3);
        }*//*
        float d = (float)(mViewWidth - lineWidth) / (line.length() - 1);
        for (int i = 0; i < line.length(); i++) {
            System.out.println(top);
            String c = String.valueOf(line.charAt(i));
            float cw = StaticLayout.getDesiredWidth(c, getPaint());
//            canvas.drawText(c, x, mLineY, getPaint());
            canvas.drawText(c, x, top + getTextSize(), getPaint());
            x += cw + d;
        }*/

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        Layout layout = getLayout();
        int top = layout.getLineTop(index);
        int bottom = layout.getLineBottom(index);
        canvas.drawRect(0, top, mViewWidth, bottom, paint);
        float x = 0;
        if (isFirstLineOfParagraph(lineStart, line)) {
            String blanks = "  ";
            canvas.drawText(blanks, x, mLineY, getPaint());
            float bw = StaticLayout.getDesiredWidth(blanks, getPaint());
            x += bw;

            line = line.substring(3);
        }

        if (line.endsWith(" ")) {
            line = line.substring(0, line.length() - 1);
        }
        int gapCount = line.length() - 1;
        int i = 0;
        if (line.length() > 2 && line.charAt(0) == 12288
                && line.charAt(1) == 12288) {
            String substring = line.substring(0, 2);
            float cw = StaticLayout.getDesiredWidth(substring, getPaint());
            canvas.drawText(substring, x, mLineY, getPaint());
            x += cw;
            i += 2;
        }

        float d = (mViewWidth - lineWidth) / gapCount;
        for (; i < line.length(); i++) {
            String c = String.valueOf(line.charAt(i));
            float cw = StaticLayout.getDesiredWidth(c, getPaint());
            canvas.drawText(c, x, mLineY, getPaint());
            x += cw + d;
        }
    }

    private boolean isFirstLineOfParagraph(int lineStart, String line) {
        return line.length() > 3 && line.charAt(0) == ' ' && line.charAt(1) == ' ';
    }

    private boolean needScale(String line) {
//        if (line.length() == 0) {
//            return false;
//        } else {
//            if(line.startsWith("<img") || line.startsWith("<embed")){
//                return false;
//            }
//            float d = mViewWidth - lineWidth;
//            String s = String.valueOf(c);
//            float cw = StaticLayout.getDesiredWidth(s, getPaint());
//            return line.charAt(line.length() - 1) != '\n'&& cw > d;
//        }

        if (line == null || line.length() == 0) {
            return false;
        } else {
            if (line.startsWith("<img") || line.startsWith("<embed")) {
                return false;
            }
            return line.charAt(line.length() - 1) != '\n';
        }
    }
}
