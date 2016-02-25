package com.tr.ui.widgets;

import java.util.ArrayList;
import java.util.List;
import com.tr.R;
import com.utils.common.EUtil;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 人脉可编辑多选对话框(身体情况、生活习惯、爱好)
 * @author leon
 *
 */
public class ConnsEditableMultiSelectDialog extends Dialog {

	private final String TAG = getClass().getSimpleName();
	
	private TextView cancelTv;
	private TextView okTv;
	// private ViewPager contentVp;
	private GridView itemGv;
	private EditText customEt;
	private TextView customTv;
	
	private Activity mContext;
	private DialogType mDialogType;
	private OnFinishListener mListener;
	private String mCurItems;
	// private ItemPagerAdapter mAdapter;
	private GridAdapter mAdapter;
	private List<Item> mListItem;
	private View mView;
	
	public ConnsEditableMultiSelectDialog(Activity context,View view, DialogType dialogType ,String items, OnFinishListener listener) {
		super(context,R.style.ConnsDialogTheme);
		setContentView(R.layout.widget_conns_editable_multi_select_dialog);
		mContext = context;
		mView = view;
		mDialogType = dialogType;
		mCurItems = items;
		mListener = listener;
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
		mListItem = new ArrayList<Item>();
		String [] items = null;
		if(mDialogType == DialogType.Healthy){
			items = mContext.getResources().getStringArray(R.array.conns_healthy);
		}
		else if(mDialogType == DialogType.Habit){
			items = mContext.getResources().getStringArray(R.array.conns_habit);
		}
		else if(mDialogType == DialogType.Hobby){
			items = mContext.getResources().getStringArray(R.array.conns_hobby);
		}
		for (int i = 0; i < items.length; i++) {
			mListItem.add(new Item(true, false, items[i]));
		}
		String [] itemsEx = null;
		if(mCurItems != null && mCurItems.length() > 0){
			itemsEx = mCurItems.split(",");
		}
		if (itemsEx != null) {
			for (int i = 0; i < itemsEx.length; i++) {
				boolean exist = false;
				for (Item item : mListItem) {
					if (itemsEx[i].equals(item.getValue())) {
						exist = true;
						item.setSelect(true);
						break;
					}
				}
				if (!exist) {
					mListItem.add(new Item(false, true, itemsEx[i]));
				}
			}
		}
		// 适配器
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
		/*
		contentVp = (ViewPager) findViewById(R.id.contentVp);
		contentVp.setAdapter(mAdapter);
		*/
		itemGv = (GridView) findViewById(R.id.itemGv);
		itemGv.setAdapter(mAdapter);
		// 自定义字段
		customEt = (EditText) findViewById(R.id.customEt);
		customTv = (TextView) findViewById(R.id.customTv);
		customTv.setOnClickListener(mClickListener);
		if(mDialogType == DialogType.Healthy){ // 身体情况
			customEt.setHint("自定义身体情况");
		}
		else if(mDialogType == DialogType.Habit){ // 生活习惯
			customEt.setHint("自定义生活习惯");
		}
		else{ // 爱好
			customEt.setHint("自定义爱好");
		}
	}
	
	private View.OnClickListener mClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.cancelTv: // 取消
				ConnsEditableMultiSelectDialog.this.dismiss();
				break;
			case R.id.okTv: // 确定
				ConnsEditableMultiSelectDialog.this.dismiss();
				if(mListener != null){
					mCurItems = "";
					for(int i = 0; i < mListItem.size(); i++){
						if(mListItem.get(i).isSelect){
							mCurItems += mListItem.get(i).getValue();
							mCurItems += ",";
						}
					}
					if(mCurItems.endsWith(",")){
						mCurItems = mCurItems.substring(0, mCurItems.length() - 1);
					}
					mListener.onFinish(mView, mCurItems);
				}
				break;
			case R.id.customTv: // 自定义
				String value = customEt.getText().toString();
				// 合法性
				if(TextUtils.isEmpty(value)){
					EUtil.showToast(mContext, "标签不能为空");
				}
				else if(value.length() > 5){
					EUtil.showToast(mContext, "标签长度不能超过5个字符");
				}
				else{
					
					// 排重
					boolean exist = false;
					for(Item item : mListItem){
						if(value.equals(item.getValue())){
							exist = true;
							break;
						}
					}
					if(exist){
						EUtil.showToast(mContext, "标签名已存在");
					}
					else{
					
						mListItem.add(new Item(false, true, value));
						mAdapter.update(mListItem);
						// contentVp.setAdapter(mAdapter);
						itemGv.setAdapter(mAdapter);
						customEt.setText("");
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
		
		public ItemPagerAdapter(Context context,List<Item> listItem){
			mContext = context;
			update(listItem);
		}
		
		public void update(List<Item> listItem){
			mListView.clear();
			int pageCount = (int) Math.ceil(listItem.size() / 9.0);
			for (int i = 0; i < pageCount; i++) {
				GridView gridView = (GridView) LayoutInflater.from(mContext)
						.inflate(R.layout.widget_conns_multi_select_page, null);
				GridAdapter adapter = new GridAdapter(mContext,
						listItem.subList(i * 9,((i + 1) * 9 > listItem.size()) ? listItem.size(): (i + 1) * 9));
				gridView.setAdapter(adapter);
				mListView.add(gridView);
			}
			notifyDataSetChanged();
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

		private List<Item> mSubListItem = new ArrayList<Item>();
		private Context mContext;
		
		public GridAdapter(Context context, List<Item> listItem){
			mSubListItem = listItem;
			mContext = context;
		}
		
		public void update(List<Item> listItem){
			mSubListItem = listItem;
			this.notifyDataSetChanged();
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
			
			final ViewHolder holder;
			if(convertView == null){
				holder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.widget_conns_editable_multi_select_item, parent,false);
				holder.parentLl = (LinearLayout) convertView.findViewById(R.id.parentLl);
				holder.itemTv = (TextView) convertView.findViewById(R.id.itemTv);
				holder.deleteIv = (ImageView) convertView.findViewById(R.id.deleteIv);
				convertView.setTag(holder);
			}
			else{
				holder = (ViewHolder) convertView.getTag();
			}
			if(mSubListItem.get(position).isDefault){
				holder.deleteIv.setVisibility(View.GONE);
			}
			else{
				holder.deleteIv.setVisibility(View.VISIBLE);
			}
			if(mSubListItem.get(position).isSelect){
				holder.parentLl.setBackgroundResource(R.drawable.conns_item_on);
				holder.itemTv.setTextColor(mContext.getResources().getColor(R.color.common_white));
			}
			else{
				holder.parentLl.setBackgroundResource(R.drawable.conns_item_off);
				holder.itemTv.setTextColor(mContext.getResources().getColor(R.color.common_gray));
			}
			holder.itemTv.setText(mSubListItem.get(position).getValue());
			holder.parentLl.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v) {
					
					mSubListItem.get(position).setSelect(!mSubListItem.get(position).isSelect);
					if(mSubListItem.get(position).isSelect){
						holder.parentLl.setBackgroundResource(R.drawable.conns_item_on);
						holder.itemTv.setTextColor(mContext.getResources().getColor(R.color.common_white));
					}
					else{
						holder.parentLl.setBackgroundResource(R.drawable.conns_item_off);
						holder.itemTv.setTextColor(mContext.getResources().getColor(R.color.common_gray));
					}
					// 更新全局变量
					for(Item item : mListItem){
						if(item.getValue().equals(mSubListItem.get(position).getValue())){
							item.setSelect(mSubListItem.get(position).isSelect);
							break;
						}
					}
				}
			});
			holder.deleteIv.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v) {
					// 删除标签（修改全局）
					for(Item item : mListItem){
						if(item.getValue().equals(mSubListItem.get(position).getValue())){
							mListItem.remove(item);
							break;
						}
					}
					// 更新ViewPager适配器
					mAdapter.update(mListItem);
					// contentVp.setAdapter(mAdapter);
					itemGv.setAdapter(mAdapter);
				}
			});
			return convertView;
		}
		
		class ViewHolder{
			public LinearLayout parentLl;
			public TextView itemTv;
			public ImageView deleteIv;
		}
	}
	
	private class Item {

		private boolean isDefault;
		private boolean isSelect ;
		private String value;
		
		public Item() {
			isDefault = false;
			isSelect = false;
			value = "";
		}
		
		public Item(boolean isDefault, boolean isSelect, String value) {
			this.isDefault = isDefault;
			this.isSelect = isSelect;
			this.value = value;
		}

		public boolean isDefault() {
			return isDefault;
		}
		public void setDefault(boolean isDefault) {
			this.isDefault = isDefault;
		}
		public boolean isSelect() {
			return isSelect;
		}
		public void setSelect(boolean isSelect) {
			this.isSelect = isSelect;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	}
	
	public interface OnFinishListener{
		public void onFinish(View view, String value);
	}
	
	public enum DialogType{
		Healthy, // 身体情况
		Habit, // 生活习惯
		Hobby, // 爱好
	}
}
