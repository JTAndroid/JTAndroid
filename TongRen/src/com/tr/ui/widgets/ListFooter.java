package com.tr.ui.widgets;

import com.tr.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 列表页脚
 * @author leon
 *
 */
public class ListFooter extends FrameLayout {

	private TextView loadMoreTv;
	private LinearLayout statusLl;
	private OnLoadMoreListener listener;
	
	public ListFooter(Context context) {
		super(context);
		View root = LayoutInflater.from(context).inflate(R.layout.widget_list_footer, null);
		loadMoreTv = (TextView) root.findViewById(R.id.loadMoreTv);
		loadMoreTv.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				loadMoreTv.setVisibility(View.GONE);
				statusLl.setVisibility(View.VISIBLE);
				
				if(listener != null){
					listener.loadMore();
				}
			}
		});
		statusLl = (LinearLayout) root.findViewById(R.id.statusLl);
		this.addView(root);
	}
	
	public void setLoading(boolean loading){
		
		if(loading){
			loadMoreTv.setVisibility(View.GONE);
			statusLl.setVisibility(View.VISIBLE);
		}
		else{
			statusLl.setVisibility(View.GONE);
			loadMoreTv.setVisibility(View.VISIBLE);
		}
	}
	
	public void setOnLoadMoreListener(OnLoadMoreListener listener){
		this.listener = listener;
	}

	public interface OnLoadMoreListener{
		public void loadMore();
	}
}
