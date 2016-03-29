package com.tongmeng.alliance.view;

import com.tr.R;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MyCatalogDialog extends Dialog implements View.OnClickListener{

	private TextView tipTv;
	private EditText nameEt;
	private TextView contentTv;
	private TextView cancelTv;
	private TextView okTv;
	
	private OnDialogClickListener mListener;
	private OperType mOperType;
	private Context mContext;
	public String deleteMessage="目录删除后，文章自动放到未分组目录哦";//删除时的文本提示信息
	
	public MyCatalogDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.widget_kno_category_alert_dialog);
		mContext = context;
		initControls();
	}

	private void initControls(){
		tipTv = (TextView) findViewById(R.id.tipTv);
		tipTv.setOnClickListener(this);
		nameEt = (EditText) findViewById(R.id.nameEt);
		nameEt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String str = s.toString();
				if(str.length()>40){
					Toast.makeText(mContext, "字符数长度不能超过40", 0).show();
				}
			}
		});
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
	public void show(OperType operType,String name){
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
			nameEt.setText(name);
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
		// 操作类型
		mOperType = operType;
		// 显示对话框
		show();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(mListener == null){
			return;
		}
		if(v == okTv){ // 确定
			switch(mOperType){
			case Create: // 新建
				if(!TextUtils.isEmpty(nameEt.getText().toString())){ // 目录名称不为空
					mListener.onClick(mOperType, 1,  nameEt.getText().toString());
					dismiss();
				}
				else{
					Toast.makeText(mContext, "目录名称不能为空", 0).show();
				}
				break;
			case Modify: // 修改
				if(!TextUtils.isEmpty(nameEt.getText().toString())){
					mListener.onClick(mOperType, 1, nameEt.getText().toString());
					dismiss();
				}
				else{
					Toast.makeText(mContext, "目录名称不能为空", 0).show();
				}
				break;
			case Delete: // 删除
				mListener.onClick(mOperType, 1, null);
				dismiss();
				break;
			}
		}
		else if(v == cancelTv){ // 取消
			mListener.onClick(mOperType, 0, null);
			dismiss();
		}
	}

	
	/**
	 * 设置监听函数
	 * @param listener
	 */
	public void setOnDialogClickListener(OnDialogClickListener listener){
		mListener = listener;
	}
	
	public interface OnDialogClickListener{
		public void onClick(OperType operType, int which, String categoryName);
	}
	
	// 操作类型
	public enum OperType{
		Create,
		Modify,
		Delete
	}
}
