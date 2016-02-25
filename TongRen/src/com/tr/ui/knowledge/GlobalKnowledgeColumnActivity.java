package com.tr.ui.knowledge;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.App;
import com.tr.R;
import com.tr.api.KnowledgeReqUtil;
import com.tr.model.knowledge.Column;
import com.tr.ui.base.JBaseFragmentActivity;
import com.utils.http.EAPIConsts.KnoReqType;
import com.utils.http.IBindData;

/**
 * 全局知识栏目
 * @author leon
 */
public class GlobalKnowledgeColumnActivity extends JBaseFragmentActivity implements IBindData{

	private final String TAG = getClass().getSimpleName();
	
	// 控件
	// private SwipeRefreshLayout refreshSrl;
	private ListView columnLv;
	private ListView subColumnLv;
	
	// 变量
	private Column mColumn; // 全部的栏目
	private ArrayList<Column> mListSubscribedColumn; // 已订阅的栏目
	private ColumnAdapter mAdapter;
	private SubColumnAdapter mSubAdapter;
	private int mCurIndex;
	
	private final int   SUBSCRIBED_STATE_NORMAL = 1001;
	private final int   SUBSCRIBED_STATE_SUBSCRIBED = 1002;
	private final int   SUBSCRIBED_STATE_CANCEL = 1003;
	private int subscribedState = SUBSCRIBED_STATE_NORMAL ;
	private Column  curColumn;
	private String fromActivity;
	@Override
	public void initJabActionBar() {
		jabGetActionBar().setTitle("    ");
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kno_act_column);
		initVars();
		initControls();
		doUpdate();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			
			Intent intent = new Intent();
			intent.putExtra("listSubscribedColumn", mListSubscribedColumn);
			setResult(Activity.RESULT_OK, intent);
			finish();
			break;
		}
		return true;
	}
	
	/**
	 * 重写返回键
	 */
	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		intent.putExtra("listSubscribedColumn", mListSubscribedColumn);
		setResult(Activity.RESULT_OK, intent);
		super.onBackPressed();
	}
	
	
	
	private void initVars(){
		String MSG = "initVars()";
		mCurIndex = 0;
		mAdapter = new ColumnAdapter(this);
		mSubAdapter = new SubColumnAdapter(this);
		// 默认加入金桐栏目
		mColumn = new Column();
		mColumn.setListColumn(new ArrayList<Column>());
		mListSubscribedColumn = new ArrayList<Column>();
		
		Intent intent = getIntent();
		ArrayList<Column> listColumn = (ArrayList<Column>) intent.getSerializableExtra("listColumn");
		if(listColumn != null){
			mListSubscribedColumn  = listColumn;
		}
		
	}
	
	@SuppressWarnings("deprecation")
	private void initControls(){
		/*
		refreshSrl = (SwipeRefreshLayout) findViewById(R.id.refreshSrl);
		refreshSrl.setColorScheme(android.R.color.holo_blue_bright, 
	            android.R.color.holo_green_light, 
	            android.R.color.holo_orange_light, 
	            android.R.color.holo_red_light);
		refreshSrl.setOnRefreshListener(new OnRefreshListener(){
			@Override
			public void onRefresh() {
				
			}
		});
		*/
		columnLv = (ListView) findViewById(R.id.columnLv);
		columnLv.setAdapter(mAdapter);
		columnLv.setOnItemClickListener(new OnItemClickListener(){
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				mCurIndex = position;
				mAdapter.notifyDataSetChanged();
				mSubAdapter.updateAdapter(mColumn.getListColumn().get(mCurIndex).getListColumn());
			}
		});
		subColumnLv = (ListView) findViewById(R.id.subColumnLv);
		subColumnLv.setAdapter(mSubAdapter);
	}
	
	// 请求数据
	private void doUpdate(){
		// refreshSrl.setRefreshing(true);
		showLoadingDialogWithoutOnCancelListener();
		KnowledgeReqUtil.doGetColumnByUserId(this, this, App.getUserID(), null);
	}
	
	// 栏目是否已订阅
	private boolean isColumnSubscribed(Column targetColumn){
		
		boolean isSubscribed = false;
		for (Column column : mListSubscribedColumn) {
			if (column.getId() == targetColumn.getId()) {
				isSubscribed = true;
				return isSubscribed ;
			}
		}
		return isSubscribed;
	}
	
	// 一级栏目
	class ColumnAdapter extends BaseAdapter{

		private Context mContext;
		private List<Column> mListColumn;
		
		public ColumnAdapter(Context context){
			mContext = context;
			mListColumn = new ArrayList<Column>();
		}
		
		public void updateAdapter(List<Column> listColumn){
			if(listColumn != null){
				mListColumn = listColumn;
			}
			else{
				mListColumn.clear();
			}
			notifyDataSetChanged();
		}
		
		@Override
		public int getCount() {
			return mListColumn.size();
		}

		@Override
		public Object getItem(int position) {
			return mListColumn.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if(convertView == null){
				convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_kno_column, null);
				holder = new ViewHolder();
				holder.init(convertView);
				convertView.setTag(holder);
			}
			else{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.init(convertView);
			if(mCurIndex == position){ // 选中
				holder.parentRl.setBackgroundColor(0xffffffff);
				holder.prefixIv.setVisibility(View.VISIBLE);
				holder.nameTv.setTextColor(0xfff39700);
			}
			else{
				holder.parentRl.setBackgroundColor(0xfff6f6f6);
				holder.prefixIv.setVisibility(View.INVISIBLE);
				holder.nameTv.setTextColor(0xff000000);
			}
			holder.build(mListColumn.get(position));
			return convertView;
		}
		
		class ViewHolder{
			
			RelativeLayout parentRl;
			ImageView prefixIv;
			TextView nameTv;
			
			public void init(View container){
				parentRl = (RelativeLayout) container.findViewById(R.id.parentRl);
				prefixIv = (ImageView) container.findViewById(R.id.prefixIv);
				nameTv = (TextView) container.findViewById(R.id.nameTv);
			}
			
			public void build(Column column){
				nameTv.setText(column.getColumnname());
			}
			
		}
	}
	
	// 子集栏目
	class SubColumnAdapter extends BaseAdapter implements OnClickListener{

		private Context mContext;
		private List<Column> mListColumn;
		
		public SubColumnAdapter(Context context){
			mContext = context;
			mListColumn = new ArrayList<Column>();
		}
		
		public void updateAdapter(List<Column> listColumn){
			if(listColumn == null){
				mListColumn.clear();
			}
			else{
				mListColumn = listColumn;
			}
			notifyDataSetChanged();
		}
		
		@Override
		public int getCount() {
			return mListColumn.size();
		}

		@Override
		public Object getItem(int position) {
			return mListColumn.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if(convertView == null){
				convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_kno_sub_column, null);
				holder = new ViewHolder();
				holder.init(convertView);
				convertView.setTag(holder);
			}
			else{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.build(mListColumn.get(position));
			boolean isSubscribed = isColumnSubscribed(mListColumn.get(position));
			if(isSubscribed){
				holder.subscribIv.setImageResource(R.drawable.kno_column_subscribed);
			}
			else{
				holder.subscribIv.setImageResource(R.drawable.kno_column_unsubscrib);
			}
			holder.subscribIv.setOnClickListener(SubColumnAdapter.this);
			holder.subscribIv.setTag(position);
			return convertView;
		}
		
		class ViewHolder{
			
			TextView nameTv;
			ImageView subscribIv;
			
			public void init(View container){
				nameTv = (TextView) container.findViewById(R.id.nameTv);
				subscribIv = (ImageView) container.findViewById(R.id.subscribIv);
			}
			
			public void build(Column column){
				nameTv.setText(column.getColumnname());
			}
		}

		@Override
		public void onClick(View v) {
			String MSG = "onClick()";
			
			int position = (Integer) v.getTag();
			if (position < mListColumn.size()) {
				
				curColumn = mListColumn.get(position);
				
				if(isColumnSubscribed(curColumn)){ // 取消订阅
					
					if(mListSubscribedColumn.size() == 1){
						Toast.makeText(mContext, "至少要订阅一个栏目！", 0).show();
						return;
					}
					
					KnowledgeReqUtil.doEditSubscribedColumn(GlobalKnowledgeColumnActivity.this, GlobalKnowledgeColumnActivity.this, 
							0, curColumn.getId(), null);
					subscribedState = SUBSCRIBED_STATE_CANCEL;
				}
				else{ // 订阅
					KnowledgeReqUtil.doEditSubscribedColumn(GlobalKnowledgeColumnActivity.this, GlobalKnowledgeColumnActivity.this, 
							1, curColumn.getId(), null);
					subscribedState = SUBSCRIBED_STATE_SUBSCRIBED;
				}
				
				showLoadingDialogWithoutOnCancelListener();
				
			}
		}
	}

	@Override
	public void bindData(int tag, Object object) {

		if(!isLoadingDialogShowing()){
			return;
		}
		dismissLoadingDialog();
		switch(tag){
		case KnoReqType.GetColumnByUserId: // 获取用户栏目
			// refreshSrl.setRefreshing(false);
			if(object != null){
				mColumn.getListColumn().clear();
				mColumn.getListColumn().add(new Column("金桐栏目"));
				List<Column> listColumn = ((Column) object).getListColumn();
				if(listColumn != null){
					mColumn.getListColumn().get(0).setListColumn(listColumn);
					mColumn.getListColumn().addAll(listColumn);
				}
				if(mColumn.getListColumn().size() <= mCurIndex){
					mCurIndex = 0;
				}
				mAdapter.updateAdapter(mColumn.getListColumn());
				mSubAdapter.updateAdapter(mColumn.getListColumn().get(mCurIndex).getListColumn());
			}
			break;
		case KnoReqType.EditSubscribedColumn: // 订阅/取消订阅
			if(object != null){
				boolean success = (Boolean) object;
				if(success){
					// 订阅列表中删除指定项
					if(SUBSCRIBED_STATE_SUBSCRIBED == subscribedState ){
						mListSubscribedColumn.add(curColumn);
						mAdapter.notifyDataSetChanged();
						mSubAdapter.updateAdapter(mColumn.getListColumn().get(mCurIndex).getListColumn());
						showToast("订阅成功");
						
					}
					//
					else if ( SUBSCRIBED_STATE_CANCEL == subscribedState ){
//						mListSubscribedColumn.remove(curColumn);
						for (int i = 0; i < mListSubscribedColumn.size(); i++) {
							Column  column = mListSubscribedColumn.get(i);
							if(column.getId() == curColumn.getId()){
								mListSubscribedColumn.remove(i);
							}
							
						}
						
						mAdapter.notifyDataSetChanged();
						mSubAdapter.updateAdapter(mColumn.getListColumn().get(mCurIndex).getListColumn());
						showToast("取消成功");
						
					}
					
				}
			}
			break;
		}
	}

}
