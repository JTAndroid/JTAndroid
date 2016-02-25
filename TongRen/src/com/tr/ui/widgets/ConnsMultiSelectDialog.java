package com.tr.ui.widgets;

import java.util.ArrayList;
import java.util.List;

import com.tr.App;
import com.tr.R;
import com.tr.model.obj.IMGroupCategory;
import com.tr.model.obj.InvestType;
import com.tr.model.obj.Trade;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 人脉多选对话框(行业、类型)
 * @author leon
 *
 */
public class ConnsMultiSelectDialog extends Dialog {

	private final String TAG = getClass().getSimpleName();
	
	private TextView cancelTv;
	private TextView okTv;
	// private ViewPager contentVp;
	private GridView itemGv;
	private Activity mContext;
	private DialogType mDialogType = DialogType.Trade; // 默认是行业类型
	private OnFinishListener mListener;
	// private ItemPagerAdapter mAdapter;
	private GridAdapter mAdapter;
	private List<IMGroupCategory> mListItem;
	
	public ConnsMultiSelectDialog(Activity context,DialogType dialogType ,ArrayList<IMGroupCategory> listSelectItem, OnFinishListener listener) {
		super(context,R.style.ConnsDialogTheme);
		setContentView(R.layout.widget_conns_multi_select_dialog);
		mContext = context;
		mDialogType = dialogType;
		mListener = listener;
		mListItem = new ArrayList<IMGroupCategory>();
		if(mDialogType == DialogType.Trade){
			if (listSelectItem != null && listSelectItem.size() > 0) {
				for (IMGroupCategory item : App.getApp().getAppData().getListTrade()) {
					item.setFocuse(false);
					for(IMGroupCategory subItem : listSelectItem){
						if(((Trade)subItem).getName().equals(((Trade)item).getName())){
							item.setFocuse(true);
							break;
						}
					}
					mListItem.add(item);
				}
			}
			else{
				for (IMGroupCategory item : App.getApp().getAppData().getListTrade()) {
					item.setFocuse(false);
					mListItem.add(item);
				}
			}
		}
		else if(mDialogType == DialogType.InInvestType){ // 投资类型
			if (listSelectItem != null && listSelectItem.size() > 0) {
				for (IMGroupCategory item : App.getApp().getAppData().getListInInvestType()) {
					item.setFocuse(false);
					for(IMGroupCategory subItem : listSelectItem){
						if(((InvestType)subItem).getName().equals(((InvestType)item).getName())){
							item.setFocuse(true);
							break;
						}
					}
					mListItem.add(item);
				}
			}
			else{
				for (IMGroupCategory item : App.getApp().getAppData().getListInInvestType()) {
					item.setFocuse(false);
					mListItem.add(item);
				}
			}
		}
		else if(mDialogType == DialogType.OutInvestType){ // 融资类型
			if (listSelectItem != null && listSelectItem.size() > 0) {
				for (IMGroupCategory item : App.getApp().getAppData().getListOutInvestType()) {
					for(IMGroupCategory subItem : listSelectItem){
						if(((InvestType)subItem).getName().equals(((InvestType)item).getName())){
							item.setFocuse(true);
							break;
						}
					}
					mListItem.add(item);
				}
			}
			else{
				for (IMGroupCategory item : App.getApp().getAppData().getListOutInvestType()) {
					mListItem.add(item);
				}
			}
		}
		initVars();
		initDialogStyle();
		initControls();
	}

	@SuppressWarnings("deprecation")
	private void initDialogStyle() {
		getWindow().setWindowAnimations(R.style.ConnsDialogAnim);
		WindowManager.LayoutParams wmlp = getWindow().getAttributes();
		wmlp.width = mContext.getWindowManager().getDefaultDisplay().getWidth();
		wmlp.gravity = Gravity.BOTTOM;
	}
	
	private void initVars(){
		// mAdapter = new ItemPagerAdapter(mContext, mListItem);
		mAdapter = new GridAdapter(mContext, mListItem);
	}
	
	private void initControls(){
		// 取消
		cancelTv = (TextView) findViewById(R.id.cancelTv);
		cancelTv.setOnClickListener(mClickListener);
		// 确定
		okTv = (TextView) findViewById(R.id.okTv);
		okTv.setOnClickListener(mClickListener);
		// 内容区
		itemGv = (GridView) findViewById(R.id.itemGv);
		itemGv.setAdapter(mAdapter);
		/*
		contentVp = (ViewPager) findViewById(R.id.contentVp);
		contentVp.setAdapter(mAdapter);
		contentVp.setOnPageChangeListener(new OnPageChangeListener(){

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		*/
	}
	
	private View.OnClickListener mClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.cancelTv: // 取消
				ConnsMultiSelectDialog.this.dismiss();
				break;
			case R.id.okTv: // 确定
				ConnsMultiSelectDialog.this.dismiss();
				if(mListener != null){
					List<IMGroupCategory> listItem = new ArrayList<IMGroupCategory>();
					for (IMGroupCategory item : mListItem) {
						if (item.isFocuse()) {
							listItem.add(item);
						}
					}
					if (mListener != null) {
						mListener.onFinish(listItem);
					}		
				}
				break;
			}
		}
	};
	
	/*
	private class ItemPagerAdapter extends PagerAdapter{

		private Context mContext;
		private List<GridView> mListView = new ArrayList<GridView>();
		
		public ItemPagerAdapter(Context context,List<IMGroupCategory> listItem){
			mContext = context;
			int itemCount = 9;
			int pageCount = (int) Math.ceil(listItem.size() * 1.0/ itemCount);
			for (int i = 0; i < pageCount; i++) {
				GridView gridView = (GridView) LayoutInflater.from(mContext).inflate(R.layout.widget_conns_multi_select_page, null);
				GridAdapter adapter = new GridAdapter(mContext, listItem.subList(i * itemCount,((i + 1) * itemCount > listItem.size()) ? listItem.size(): (i + 1) * itemCount));
				gridView.setAdapter(adapter);
				gridView.setClickable(true);
				gridView.setFocusable(true);
				mListView.add(gridView);
			}
		}
		
		@Override
		public int getCount() {
			return mListView.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		@Override  
        public void destroyItem(ViewGroup container, int position, Object object) {  
            container.removeView(mListView.get(position));  
        }  

        @Override  
        public Object instantiateItem(ViewGroup container, int position) {  
            container.addView(mListView.get(position));  
            return mListView.get(position);  
        }  
	}
	*/
	
	private class GridAdapter extends BaseAdapter{

		private List<IMGroupCategory> mSubListItem = new ArrayList<IMGroupCategory>();
		private Context mContext;
		
		public GridAdapter(Context context, List<IMGroupCategory> listItem){
			mSubListItem = listItem;
			mContext = context;
		}
		
		@Override
		public int getCount() {
			return mSubListItem.size();
		}

		@Override
		public Object getItem(int position) {
			return mSubListItem.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
			ViewHolder holder;
			if(convertView == null){
				holder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.widget_conns_multi_select_item, parent,false);
				holder.itemTv = (TextView) convertView.findViewById(R.id.itemTv);
				convertView.setTag(holder);
			}
			else{
				holder = (ViewHolder) convertView.getTag();
			}
			if(mSubListItem.get(position).isFocuse()){
				holder.itemTv.setBackgroundResource(R.drawable.conns_item_on);
				holder.itemTv.setTextColor(mContext.getResources().getColor(R.color.common_white));
			}
			else{
				holder.itemTv.setBackgroundResource(R.drawable.conns_item_off);
				holder.itemTv.setTextColor(mContext.getResources().getColor(R.color.common_gray));
			}
			holder.itemTv.setText(mSubListItem.get(position).getName());
			holder.itemTv.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v) {
					
					mSubListItem.get(position).setFocuse(!mSubListItem.get(position).isFocuse());
					if(mSubListItem.get(position).isFocuse()){
						((TextView) v).setBackgroundResource(R.drawable.conns_item_on);
						((TextView) v).setTextColor(mContext.getResources().getColor(R.color.common_white));
					}
					else{
						((TextView) v).setBackgroundResource(R.drawable.conns_item_off);
						((TextView) v).setTextColor(mContext.getResources().getColor(R.color.common_gray));
					}
					// 更新全局变量
					for(IMGroupCategory item : mListItem){
						if(mDialogType == DialogType.Trade){
							if(((Trade) item).getName().equals(((Trade)mSubListItem.get(position)).getName())){
								item.setFocuse(mSubListItem.get(position).isFocuse());
								break;
							}
						}
						else{
							if(((InvestType) item).getName().equals(((InvestType) mSubListItem.get(position)).getName())){
								item.setFocuse(mSubListItem.get(position).isFocuse());
								break;
							}
						}
					}
				}
			});
			return convertView;
		}
		
		class ViewHolder{
			TextView itemTv;
		}
	}
	
	public interface OnFinishListener{
		public void onFinish(List<IMGroupCategory> listItem);
	}
	
	public enum DialogType{
		Trade, // 行业
		InInvestType, // 融资类型
		OutInvestType // 投资类型
	}
}
