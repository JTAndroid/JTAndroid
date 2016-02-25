package com.tr.ui.widgets;

import java.util.ArrayList;
import java.util.List;
import com.tr.R;
import com.tr.model.model.BaseObject;
import com.tr.model.model.PeopleCustomer;
import com.tr.model.model.PeopleGroupTemp;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

/**
 * 人脉弹出框(所在群组和关联客户)
 * @author leon
 *
 */
public class ConnsGroupDialog extends Dialog {

	private final String TAG = getClass().getSimpleName();
	
	private TextView cancelTv;
	private TextView okTv;
	private TextView tipTv;
	private GridView itemGv;
	private Activity mContext;
	private DialogType mDialogType = DialogType.GroupTemp; // 默认是群组
	private OnFinishListener mListener;
	private GridAdapter mAdapter;
	private List<BaseObject> mListItem;
	private List<BaseObject> mListSelectItem;
	
	public ConnsGroupDialog(Activity context,DialogType dialogType ,List<BaseObject> listSelectItem,List<BaseObject> listItem, OnFinishListener listener) {
		super(context,R.style.ConnsDialogTheme);
		setContentView(R.layout.widget_conns_group_dialog);
		mContext = context;
		mDialogType = dialogType;
		mListener = listener;
		mListItem = listItem;
		mListSelectItem = listSelectItem;
		if(mDialogType == DialogType.GroupTemp){ // 群组
			
			if (mListItem != null && mListItem.size() > 0) {
				if (mListSelectItem != null && mListSelectItem.size() > 0) {
					for (BaseObject obj : mListSelectItem) {
						for (BaseObject subObj : mListItem) {
							if (((PeopleGroupTemp) subObj).name.equals(((PeopleGroupTemp) obj).name)) {
								subObj.isFocuse = (true);
								break;
							}
						}
					}
				}
			}
			else{
				mListItem = new ArrayList<BaseObject>();
				// mListItem.add(new PeopleGroupTemp("暂无分组"));
			}
		}
		else if(mDialogType == DialogType.Customer){ // 关联客户
			
			if (mListItem != null && mListItem.size() > 0) {
				if (mListSelectItem != null && mListSelectItem.size() > 0) {
					for (BaseObject obj : mListSelectItem) {
						for (BaseObject subObj : mListItem) {
							if (((PeopleCustomer) subObj).name.equals(((PeopleCustomer) obj).name)) {
								subObj.isFocuse = (true);
								break;
							}
						}
					}
				}
			}
			else{
				mListItem = new ArrayList<BaseObject>();
				// mListItem.add(new PeopleCustomer("暂无分组"));
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
		// 提示
		tipTv = (TextView) findViewById(R.id.tipTv);
		if(mListItem.size() > 0){
			tipTv.setVisibility(View.GONE);
		}
		else{
			if(mDialogType == DialogType.Customer){	
				tipTv.setText("暂无客户");
			}
			else{
				tipTv.setText("暂无分组");
			}
		}
	}
	
	private View.OnClickListener mClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.cancelTv: // 取消
				ConnsGroupDialog.this.dismiss();
				break;
			case R.id.okTv: // 确定
				if (mListSelectItem != null) {
					mListSelectItem.clear();

					for (BaseObject obj : mListItem) {
						if (obj.isFocuse) {
							mListSelectItem.add(obj);
						}
					}
					if (mListener != null) {
						if (mListener != null) {
							mListener.onFinish(mDialogType, mListSelectItem);
						}
					}
				}
				ConnsGroupDialog.this.dismiss();
				break;
			}
		}
	};
	
	private class GridAdapter extends BaseAdapter{

		private List<BaseObject> mSubListItem = new ArrayList<BaseObject>();
		private Context mContext;
		
		public GridAdapter(Context context, List<BaseObject> listItem){
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
			if(mSubListItem.get(position).isFocuse){
				holder.itemTv.setBackgroundResource(R.drawable.conns_item_on);
				holder.itemTv.setTextColor(mContext.getResources().getColor(R.color.common_white));
			}
			else{
				holder.itemTv.setBackgroundResource(R.drawable.conns_item_off);
				holder.itemTv.setTextColor(mContext.getResources().getColor(R.color.common_gray));
			}
			if(mDialogType == DialogType.GroupTemp){
				holder.itemTv.setText(((PeopleGroupTemp) mSubListItem.get(position)).name);
			}
			else if(mDialogType == DialogType.Customer){
				holder.itemTv.setText(((PeopleCustomer) mSubListItem.get(position)).name);
			}
			holder.itemTv.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v) {
					
					mSubListItem.get(position).isFocuse = (!mSubListItem.get(position).isFocuse);
					if(mSubListItem.get(position).isFocuse){
						((TextView) v).setBackgroundResource(R.drawable.conns_item_on);
						((TextView) v).setTextColor(mContext.getResources().getColor(R.color.common_white));
					}
					else{
						((TextView) v).setBackgroundResource(R.drawable.conns_item_off);
						((TextView) v).setTextColor(mContext.getResources().getColor(R.color.common_gray));
					}
					// 更新全局变量
					for(BaseObject obj : mListItem){
						if(mDialogType == DialogType.GroupTemp){
							if(((PeopleGroupTemp) obj).name.equals(((PeopleGroupTemp) mSubListItem.get(position)).name)){
								obj.isFocuse = (mSubListItem.get(position).isFocuse);
								break;
							}
						}
						else if(mDialogType == DialogType.Customer){
							if(((PeopleCustomer) obj).name.equals(((PeopleCustomer) mSubListItem.get(position)).name)){
								obj.isFocuse = (mSubListItem.get(position).isFocuse);
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
		public void onFinish(DialogType dialogType ,List<BaseObject> listSelectItem);
	}
	
	public enum DialogType{
		GroupTemp, // 群组
		Customer // 关联客户
	}

}
