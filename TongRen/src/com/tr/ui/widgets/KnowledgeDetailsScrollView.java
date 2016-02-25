package com.tr.ui.widgets;
 
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;
 
/**
 * @author Cyril Mottier
 */
public class KnowledgeDetailsScrollView extends ScrollView {
 
    /**
     * @author Cyril Mottier
     */
    public interface OnScrollChangedListener {
        void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt);
    }
 
    private OnScrollChangedListener mOnScrollChangedListener;
 
    public KnowledgeDetailsScrollView(Context context) {
        super(context);
    }
 
    public KnowledgeDetailsScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    public KnowledgeDetailsScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
 
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangedListener != null) {
            mOnScrollChangedListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }
 
    public void setOnScrollChangedListener(OnScrollChangedListener listener) {
        mOnScrollChangedListener = listener;
    }
    
    @Override
    public int computeVerticalScrollRange() {
    	// TODO Auto-generated method stub
    	return super.computeVerticalScrollRange();
    }
 
}

