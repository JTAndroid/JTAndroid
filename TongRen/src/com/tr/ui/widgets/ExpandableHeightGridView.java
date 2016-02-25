package com.tr.ui.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class ExpandableHeightGridView extends GridView {

	public ExpandableHeightGridView(Context context, AttributeSet attrs) { 
        super(context, attrs); 
    } 
	
    public ExpandableHeightGridView(Context context) { 
        super(context); 
    } 
    
    public ExpandableHeightGridView(Context context, AttributeSet attrs, int defStyle) { 
        super(context, attrs, defStyle); 
    }     
    
    @Override 
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {      
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST); 
        super.onMeasure(widthMeasureSpec, expandSpec); 
    }
    
    @Override
    public int computeVerticalScrollRange() {
    	// TODO Auto-generated method stub
    	return super.computeVerticalScrollRange();
    }
}
