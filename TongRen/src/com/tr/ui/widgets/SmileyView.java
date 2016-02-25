package com.tr.ui.widgets;

import com.tr.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 表情控件
 * 
 * @author Tony
 * 
 */
public class SmileyView extends LinearLayout {
	
	public static final int MaxSmileyNumber = 20;
	
	private OnItemClickListener listener;
	private Context mContext;
	private boolean mIsFirst = false;
	private int position = 0;

	public SmileyView(Context context, boolean isFirst) {
		super(context);
		mContext = context;
		mIsFirst = isFirst;
		init(context);
	}
	
	/**
	 * 根据当前表情面板位置设置面表中的表情
	 * @param context
	 * @param position
	 */
	public SmileyView(Context context, int position) {
		super(context);
		mContext = context;
		this.position = position;
		init(context, position);
	}

	public SmileyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// init(context);
	}

	private void init(Context context) {
		LayoutInflater.from(context).inflate(R.layout.im_smiley_gridview, this,
				true);

		final GridView gridView = (GridView) findViewById(R.id.gridview);
		FaceAdapter adapter;
		if (mIsFirst) {
			adapter = new FaceAdapter(context, SmileyParser.mIconIds);
		} else {
			adapter = new FaceAdapter(context, SmileyParser2.mIconIds);
		}
		
		

		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new GridView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (listener != null) {
					listener.onItemClick(arg0, arg1, position, arg3);
				}
			}
		});
	}
	
	/**
	 * 初始化表情
	 * @param context
	 * @param position
	 */
	private void init(Context context, int position) {
		LayoutInflater.from(context).inflate(R.layout.im_smiley_gridview, this, true);

		final GridView gridView = (GridView) findViewById(R.id.gridview);
		int [] iconIds = null;
		// 表情总页数
		final int smileyPages = (int) Math.ceil(SmileyParser.mEnhancedIconIds.length * 1.0 / MaxSmileyNumber);
		if(position < smileyPages){
			int smileyNumbers = MaxSmileyNumber;
			if(position == smileyPages - 1){
				smileyNumbers = SmileyParser.mEnhancedIconIds.length % MaxSmileyNumber == 0 ? MaxSmileyNumber : SmileyParser.mEnhancedIconIds.length % MaxSmileyNumber;
			}
			iconIds = new int[smileyNumbers];
			for(int i = 0; i < smileyNumbers; i++){
				iconIds[i] = SmileyParser.mEnhancedIconIds[position * MaxSmileyNumber + i];
			}
		}
		else{
			return;
		}
		FaceAdapter adapter = new FaceAdapter(context, iconIds);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new GridView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				
				if (listener != null
						&& ((position == MaxSmileyNumber) || (position < ((FaceAdapter) arg0.getAdapter()).getSmileyResourceIds().length))) {
					
					listener.onItemClick(arg0, arg1, position, SmileyView.this.position * MaxSmileyNumber + position);
				}
			}
		});
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		this.listener = listener;
	}

	public interface OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3);
	}
	
	/**
	 * GridView适配器
	 * @author leon
	 */
	private class FaceAdapter extends BaseAdapter {
		
		private Context context;
		private int[] smileyResourceIds;

		public int[] getSmileyResourceIds() {
			return smileyResourceIds;
		}

		public FaceAdapter(Context context, int[] smileyResourceIds) {
			this.context = context;
			this.smileyResourceIds = smileyResourceIds;
		}

		@Override
		public int getCount() {
			return MaxSmileyNumber + 1; // 增加一个删除键
		}

		@Override
		public Object getItem(int position) {
			return context.getResources().getDrawable(smileyResourceIds[position]);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(
						R.layout.im_chatdetail_smile_grid_item, parent, false);
			}
			ImageView imageView = (ImageView) convertView
					.findViewById(R.id.im_chatdetail_smile_grid_item);
			imageView.setAdjustViewBounds(true);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

			if (position == MaxSmileyNumber) {
				imageView.setImageResource(R.drawable.chat_back);
			} 
			else {
				if(position < smileyResourceIds.length){
					imageView.setImageResource(smileyResourceIds[position]);
				}
				else{
					imageView.setImageDrawable(null);
				}
			}
			return convertView;
		}
	}

	
	public int getPosition() {
		return position;
	}
}
