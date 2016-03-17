package com.tr.ui.widgets;

import org.greenrobot.eventbus.EventBus;

import com.common.evebusmodel.AddCategoryEvent;
import com.tr.R;
import com.tr.model.knowledge.Column;
import com.tr.model.knowledge.UserCategory;
import com.utils.common.EUtil;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;

public class KnoCategoryAlertDialog extends Dialog implements View.OnClickListener{

	private final String TAG = getClass().getSimpleName();
	
	private TextView tipTv;
	private EditText nameEt;
	private TextView contentTv;
	private TextView cancelTv;
	private TextView okTv;
	
	private OnDialogClickListener mListener;
	private OperType mOperType;
	private Context mContext;
	private UserCategory mCategory;
	public String deleteMessage="目录删除后，文章自动放到未分组目录哦";//删除时的文本提示信息
	
	public KnoCategoryAlertDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.widget_kno_category_alert_dialog);
		mContext = context;
		initControls();
	}

	private void initControls(){
		tipTv = (TextView) findViewById(R.id.tipTv);
		tipTv.setOnClickListener(this);
		nameEt = (EditText) findViewById(R.id.nameEt);
		nameEt.addTextChangedListener(new MaxLengthWatcher(mContext, 40, "字符数长度不能超过40", nameEt));
		contentTv = (TextView) findViewById(R.id.contentTv);
		cancelTv = (TextView) findViewById(R.id.cancelTv);
		cancelTv.setOnClickListener(this);
		okTv = (TextView) findViewById(R.id.okTv);
		okTv.setOnClickListener(this);
	}
	
	/**
	 * 显示对话框
	 * @param operType
	 * @param category
	 */
	public void show(OperType operType, UserCategory category){
		switch(operType){
		case Create: // 新建
			tipTv.setText("新建目录");
			nameEt.setHint("输入您的目录名称");
			nameEt.setText("");
			nameEt.setVisibility(View.VISIBLE);
			contentTv.setVisibility(View.GONE);
			break;
		case Modify: // 修改
			tipTv.setText("修改目录");
			nameEt.setText(category.getCategoryname());
			nameEt.setVisibility(View.VISIBLE);
			contentTv.setVisibility(View.GONE);
			break;
		case Delete: // 删除
			tipTv.setText("提示");
			nameEt.setVisibility(View.GONE);
			contentTv.setText(deleteMessage);
			contentTv.setVisibility(View.VISIBLE);
			break;
		}
		// 栏目对象
		if(category != null){
			mCategory = category;
		}
		// 操作类型
		mOperType = operType;
		// 显示对话框
		show();
	}
	
	
	/**
	 * 设置监听函数
	 * @param listener
	 */
	public void setOnDialogClickListener(OnDialogClickListener listener){
		mListener = listener;
	}

	@Override
	public void onClick(View v) {
		if(mListener == null){
			return;
		}
		if(v == okTv){ // 确定
			switch(mOperType){
			case Create: // 新建
				if(!TextUtils.isEmpty(nameEt.getText().toString())){ // 目录名称不为空
					mListener.onClick(mOperType, 1, mCategory.getId(), nameEt.getText().toString(), mCategory.getLevel());
					dismiss();
//					EventBus.getDefault().post(new AddCategoryEvent(true));
				}
				else{
					EUtil.showToast(mContext, "目录名称不能为空");
				}
				break;
			case Modify: // 修改
				if(!TextUtils.isEmpty(nameEt.getText().toString())){
					mListener.onClick(mOperType, 1, mCategory.getId(), nameEt.getText().toString(), null);
					dismiss();
				}
				else{
					EUtil.showToast(mContext, "目录名称不能为空");
				}
				break;
			case Delete: // 删除
				mListener.onClick(mOperType, 1, mCategory.getId(), null, null);
				dismiss();
				break;
			}
		}
		else if(v == cancelTv){ // 取消
			mListener.onClick(mOperType, 0, 0, null, null);
			dismiss();
		}
	}
	
	public interface OnDialogClickListener{
		public void onClick(OperType operType, int which,long categoryId, String categoryName, Integer mewCreateLevel);
	}
	
	// 操作类型
	public enum OperType{
		Create,
		Modify,
		Delete
	}
}
