package com.tr.ui.widgets;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.tr.R;

public class TagListSideBar extends View {  
	 	private char[] charArray;  
	 	ArrayList<String> tagList = null;  
	    private ListView listView;  
	    private TextView mDialogText;
	    private  int m_nItemHeight = 15;  
	    private int textSize=0;
	    Context context;
	    public TagListSideBar(Context context) {  
	        super(context);  
	        this.context=context;
	        textSize=context.getResources().getDimensionPixelSize(R.dimen.sidebar_text_size);
	        init();  
	    }  
	    public TagListSideBar(Context context, AttributeSet attrs) {  
	        super(context, attrs);  
	        textSize=context.getResources().getDimensionPixelSize(R.dimen.sidebar_text_size);
	        charArray = new char[] {'#','A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',  
	                'T', 'U', 'V', 'W', 'X', 'Y', 'Z','*' };    
	    }  
	    public void init() {  
	          
	    }  
	    public TagListSideBar(Context context, AttributeSet attrs, int defStyle) {  
	        super(context, attrs, defStyle); 
	        init();  
	    }  
	    public void setListView(ListView _list,ArrayList<String> tagList) {  
	        listView = _list;  
	        this.tagList = tagList;
	    }  
	    public void setTextView(TextView mDialogText) {  
	    	this.mDialogText = mDialogText;  
	    }  
	    public boolean onTouchEvent(MotionEvent event) {  
	        super.onTouchEvent(event);  
	        if(listView!=null&&tagList!=null){
	        	 
	        }else{
	        	return true;
	        }
	        int i = (int) event.getY();  
	        int idx = i / m_nItemHeight;  
	        if (idx >= charArray.length) {  
	            idx = charArray.length - 1;  
	        } else if (idx < 0) {  
	            idx = 0;  
	        }  
	        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {  
	        	mDialogText.setVisibility(View.VISIBLE);
	        	mDialogText.setText(""+charArray[idx]);

//	            int position =  tagList.getCharAt(charArray[idx]);
//	            if (position == -1) {  
//	                return true;  
//	            }  
//	            listView.setSelection(position);  
	        }else{
	        	mDialogText.setVisibility(View.INVISIBLE);
	        }  
	        return true;  
	    }  
	    
	    protected void onDraw(Canvas canvas) {
	    	m_nItemHeight=this.getHeight()/charArray.length;
	        Paint paint = new Paint();  
	        paint.setColor(0xff595c61);  
	        paint.setTextSize(textSize);  
	        paint.setTextAlign(Paint.Align.CENTER);  
	        float widthCenter = getMeasuredWidth() / 2;  
	        for (int i = 0; i < charArray.length; i++) {  
	            canvas.drawText(String.valueOf(charArray[i]), widthCenter, m_nItemHeight + (i * m_nItemHeight), paint);  
	        }  
	        super.onDraw(canvas);  
	    }  
}
