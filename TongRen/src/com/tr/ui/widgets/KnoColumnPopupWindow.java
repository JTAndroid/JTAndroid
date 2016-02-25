package com.tr.ui.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.tr.R;
import com.tr.api.KnowledgeReqUtil;
import com.tr.model.knowledge.Column;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.knowledge.GlobalKnowledgeColumnActivity;
import com.utils.http.IBindData;
import com.utils.http.EAPIConsts.KnoReqType;
import com.utils.log.KeelLog;

import org.askerov.dynamicgrid.BaseDynamicGridAdapter;
import org.askerov.dynamicgrid.DynamicGridView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 知识栏目弹出框
 * @author leon
 */
public class KnoColumnPopupWindow extends PopupWindow implements OnClickListener  {

	private final String TAG = getClass().getSimpleName();
	
	private Context mContext;
	private ColumnDynamicAdapter mAdapter;
	private List<Column> mListColumn;
	private OnOperateListener mListener;
	
	private View container;
	private DynamicGridView columnDgv;
	private ImageView columnIv;  // 栏目按钮
//	private RelativeLayout editParentRl;
	private TextView editHintTv;  //编辑提示
	private TextView editTv;  // 编辑/完成按钮
	
	private EProgressDialog mProgressDialog;
	
	public KnoColumnPopupWindow(Context context, List<Column> listColumn){
		// 初始化变量
		initVars(context, listColumn);
		// 初始化控件
		initControls();
		// 设置PopupWindow的View
		// 设置PopupWindow弹出窗体的宽
		setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		// 设置PopupWindow弹出窗体的高
		setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
		// 设置PopupWindow弹出窗体可点击
		setFocusable(true);
		setOutsideTouchable(true);
		// 实例化一个ColorDrawable颜色为半透明
		// 设置PopupWindow弹出窗体的背景
		 setBackgroundDrawable(new ColorDrawable(0x55000000));
		
	}
	
	private void initVars(Context context, List<Column> listColumn){
		
		mContext = context;
		if(listColumn != null){
			mListColumn = listColumn;
		}
		else{
			mListColumn = new ArrayList<Column>();
		}
 		mAdapter = new ColumnDynamicAdapter(context, mListColumn, 4);
 		setFocusable(true);
 		setOutsideTouchable(true);
 		setBackgroundDrawable(new BitmapDrawable());
	}
	
	private void initControls(){
		
		container = LayoutInflater.from(mContext)
				.inflate(R.layout.widget_kno_column_popup_window, null);
		setContentView(container);
//		container.setOnClickListener(this);
		
		editHintTv = (TextView) container.findViewById(R.id.editHintTv);
		editTv = (TextView) container.findViewById(R.id.editTv);
		editTv.setOnClickListener(mOnClickListener);
		columnIv = (ImageView) container.findViewById(R.id.columnIv);
		columnIv.setOnClickListener(mOnClickListener);
		
		
		
		columnDgv = (DynamicGridView) container.findViewById(R.id.columnDgv);
		columnDgv.setAdapter(mAdapter);
		columnDgv.setOnDragListener(new DynamicGridView.OnDragListener() {
            @Override
            public void onDragStarted(int position) {
                // Log.d(TAG, "drag started at position " + position);
            }

            @Override
            public void onDragPositionsChanged(int oldPosition, int newPosition) {
               //  Log.d(TAG, String.format("drag item position changed from %d to %d", oldPosition, newPosition));
                
                mListColumn.add(newPosition, mListColumn.remove(oldPosition));
                
            }
            
        });
		columnDgv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            	String MSG = "onItemLongClick()";
            	
            	Log.i(TAG, MSG + " position = " + position);
            	
            	if(position < mListColumn.size()){
            		columnDgv.startEditMode(position); // 进入编辑模式
                	mAdapter.notifyDataSetChanged(); // 取消添加栏目
                	updateEditModeText();
					if (mListener != null) {
						mListener.onStartEdit();
					}
            	}
                return true;
            }
        });
		columnDgv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                
            	/*
            	Toast.makeText(mContext, parent.getAdapter().getItem(position).toString(),
                        Toast.LENGTH_SHORT).show();
                */
                
                if(mListener != null){
                	if(position < mListColumn.size()){
                		mListener.onColumnSelect(mListColumn.get(position));
                		dismiss();
                	}
                	else{
                		mListener.onAddColumn();
//                		dismiss();
                	}
                }
            }
        });
	}
	
	/**
	 * 弹出框监听器
	 * @param listener
	 */
	public void setOnOperateListener(OnOperateListener listener){
		if(listener != null){
			mListener = listener;
		}
	}
	
	/**
	 * 更新栏目数据
	 * @param listColumn
	 */
	public void updateColumnData(List<Column> listColumn){
		if(listColumn != null){
			this.mListColumn = listColumn;
			mAdapter.set(mListColumn);
			mAdapter.notifyDataSetChanged();
			
			columnDgv.stopEditMode(); 
	    	updateEditModeText();
		}
	}
	
	/**
	 * 获取栏目数据
	 * @return
	 */
	public List<Column> getColumnData(){
		return this.mListColumn;
	}
	
	/**
	 * 打开编辑模式
	 */
	public void startEditMode(){
		if(!columnDgv.isEditMode()){
			columnDgv.startEditMode();
			mAdapter.notifyDataSetChanged(); // 刷新界面
		}
	}
	
	/**
	 * 关闭编辑模式
	 */
	public void stopEditMode(){
		if(columnDgv.isEditMode()){
			columnDgv.stopEditMode();
			mAdapter.notifyDataSetChanged(); // 刷新界面
			if(mListener != null){
				mListener.onStopEdit();
			}
		}
	}
	
	/**
	 * 在指定控件下方显示PopupWindow
	 */
	@Override
	public void showAsDropDown(View anchor){
		super.showAsDropDown(anchor);
		stopEditMode();
	}
	
	/**
	 * 隐藏PopupWindow
	 */
	@Override
	public void dismiss(){
		super.dismiss();
		stopEditMode();
	}
	
	class ColumnDynamicAdapter extends BaseDynamicGridAdapter implements OnClickListener{
	    
		public ColumnDynamicAdapter(Context context, List<?> items, int columnCount) {
	        super(context, items, columnCount);
	    }
		
		@Override
		public int getCount(){
			
			if(columnDgv.isEditMode()){
				return mListColumn.size();
			}
			else{
				return mListColumn.size() + 1;
			}
		}

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	    	ColumnViewHolder holder;
	        if (convertView == null) {
	            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_kno_column, null);
	            holder = new ColumnViewHolder(convertView);
	            convertView.setTag(holder);
	        } 
	        else {
	            holder = (ColumnViewHolder) convertView.getTag();
	        }
	        if(position == mListColumn.size()){
	        	holder.delIv.setVisibility(View.GONE);
	        	holder.colTv.setBackgroundResource(R.drawable.kno_column_add);
	        	holder.colTv.setText("");
	        }
	        else{
	        	if(columnDgv.isEditMode()){
	        		holder.delIv.setVisibility(View.VISIBLE);
	        	}
	        	else{
	        		holder.delIv.setVisibility(View.GONE);
	        	}
	        	holder.colTv.setBackgroundResource(R.drawable.kno_column_bg);
	        	holder.build(mListColumn.get(position));
	        	holder.delIv.setTag(position);
	        	holder.delIv.setOnClickListener(ColumnDynamicAdapter.this);
	        }
	        return convertView;
	    }

	    

		@Override
		public void onClick(final View v) {
			
			if(mListColumn.size() <= 1){
				Toast.makeText(mContext, "至少要订阅一个栏目！", Toast.LENGTH_SHORT).show();
				return;
			}
			
			final int position = ((Integer) v.getTag()).intValue();
				
			IBindData iBindData = new IBindData() {
				
				@Override
				public void bindData(int tag, Object object) {
					switch (tag) {
					case KnoReqType.EditSubscribedColumn:
						
						if (object != null) {
							
							boolean success = (Boolean) object;
							if (success) {

								if (position < mListColumn.size()) {

									mListColumn.remove(position);
									notifyDataSetChanged();
									
									Toast.makeText(mContext, "取消成功 !", Toast.LENGTH_SHORT).show();
								}
							}
							else {
								Toast.makeText(mContext, "取消失败 !", Toast.LENGTH_SHORT).show();
							}
						}
						dismissLoadingDialog();
						
						break;

					default:
						break;
					}
					
					
					
				}
			};
				
			
			KnowledgeReqUtil.doEditSubscribedColumn(mContext, iBindData, 0, mListColumn.get(position).getId(), null);
			showLoadingDialog("");
			
			
		}
	}
	
	

	@Override
	public void onClick(View v) {
		
		
		if(columnDgv.isEditMode()){
			columnDgv.stopEditMode();
			mAdapter.notifyDataSetChanged();
			updateEditModeText();
			if(mListener != null){
				mListener.onStopEdit();
			}
		}
	}
	
	class ColumnViewHolder {
    	
        private TextView colTv;
        private ImageView delIv;

        private ColumnViewHolder(View view) {
        	colTv = (TextView) view.findViewById(R.id.columnTv);
        	delIv = (ImageView) view.findViewById(R.id.deleteIv);
        }

        void build(Column column) {
        	colTv.setText(column.getColumnname());
        }
    }
	
	public interface OnOperateListener{
		/**
		 * 选择指定栏目
		 * @param column
		 */
		public void onColumnSelect(Column column);
		/**
		 * 添加栏目
		 */
		public void onAddColumn();
		/**
		 * 停止编辑模式
		 */
		public void onStopEdit();
		/**
		 * 开始编辑模式
		 */
		public void onStartEdit();
	}
	
	/**
	 * 获得 栏目动态GridView
	 * @return 是否在编辑状态
	 */
	public boolean isEditMode(){
		return columnDgv.isEditMode();
	}

	
	public void showLoadingDialog(String message) {
		if (null == mProgressDialog) {
			mProgressDialog = new EProgressDialog(mContext);
			mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialogInterface) {
					KeelLog.d(TAG, "mProgressDialog.onCancel");
					// onLoadingDialogCancel();//如果取消对话框， 结束当前activity
				}
			});
		}
		mProgressDialog.setMessage(message);
		// 加载进度条show判断 如果已经是show状态 就不再调show方法
		// if (!mProgressDialog.isShowing()) {
		mProgressDialog.show();
		// }
	}
	
	public void dismissLoadingDialog() {
		try{
			if (mProgressDialog != null && mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
			}
		}catch(Exception e){
			
		}
	}
	
	
	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			
			if (columnIv == v) { // 显示栏目
				stopEditMode();
				updateEditModeText();
				dismiss();
			}
			else if( editTv == v ){ // 编辑标签
				if (!isEditMode()) {
					startEditMode();
				}
				// 当是编辑状态点击时 , 
				else {
					// 上传当前栏目顺序
					
					 ArrayList<Long> listColumnId = new ArrayList<Long>();
					 for(int i = 0; i < mListColumn.size(); i++){
						 long id = mListColumn.get(i).getId();
						 listColumnId.add(id);
					 }
					KnowledgeReqUtil.doUpdateSubscribedColumn(mContext, iBindData, listColumnId, null);
					stopEditMode();
				}
				updateEditModeText();
			}
		}
	};
	
	
	
	void updateEditModeText(){
		if (isEditMode()) {
			editHintTv.setText("点击删除/拖动排序");
			editTv.setText("完成");
		}
		else {
			editHintTv.setText("选择栏目");
			editTv.setText("排序/删除");
		}
	}
	
	
	IBindData iBindData = new IBindData() {
		
		@Override
		public void bindData(int tag, Object object) {
			String MSG = "bindData()";
			
			switch (tag) {
			case KnoReqType.UpdateSubscribedColumn:  // 更新订阅的栏目
				
				if (object != null) {
					
					boolean success = (Boolean) object;
					if (success) {
//							Toast.makeText(mContext, "排序成功 !", Toast.LENGTH_SHORT).show();
					}
					else {
//						Toast.makeText(mContext, "排序失败 !", Toast.LENGTH_SHORT).show();
					}
				}
				
				break;

			default:
				break;
			}
			
			
			
		}
	};
	
	
}
