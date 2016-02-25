package com.tr.ui.widgets;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.tr.R;
import com.tr.ui.cloudDisk.FileManagementActivity;
import com.utils.common.EUtil;

/**
 * 新建目录对话框
 */
public class CreateCatalogAlertDialog extends Dialog implements View.OnClickListener {

	private TextView tipTv;
	private EditText nameEt;
	private TextView contentTv;
	private TextView cancelTv;
	private TextView okTv;
	private boolean isDelete = false;
	private boolean isBack = false;

	private Context mContext;
	private OnEditDialogClickListener mListener;
	private InputMethodManager imm;

	public CreateCatalogAlertDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.create_catalog_alert_dialog);
		mContext = context;
		initControls();
		imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
	}
	private void initControls() {
		tipTv = (TextView) findViewById(R.id.tipTv);
		contentTv = (TextView) findViewById(R.id.contentTv);
		tipTv.setOnClickListener(this);
		nameEt = (EditText) findViewById(R.id.nameEt);
//		nameEt.addTextChangedListener(new MaxLengthWatcher(mContext, 20, "字符数长度不能超过20", nameEt));
		contentTv = (TextView) findViewById(R.id.contentTv);
		cancelTv = (TextView) findViewById(R.id.cancelTv);
		cancelTv.setOnClickListener(this);
		okTv = (TextView) findViewById(R.id.okTv);
		okTv.setOnClickListener(this);
	}
	
	public void initDeleteDialog()
	{
		tipTv.setVisibility(View.GONE);
		nameEt.setVisibility(View.GONE);
		contentTv.setVisibility(View.VISIBLE);
		isDelete = true;
	}
	
	public void setIsDelete(boolean isDelete)
	{
		this.isDelete = isDelete;
	}
	
	public String getName(){
		return nameEt.getText().toString();
	}
	public void setName(String name){
		nameEt.setText(name);
	}
	
	public boolean isBack() {
		return isBack;
	}
	public void setBack(boolean isBack) {
		this.isBack = isBack;
	}
	@Override
	public void onClick(View v) {
		if (isDelete) {
			if (mListener == null) {
				return;
			}
			if (v == okTv) { // 确定
				mListener.onClick("okTv");
				setBack(true);
				dismiss();
			} else if (v == cancelTv) { // 取消
				mListener.onClick(null);
				setBack(false);
				dismiss();
			}
		} else {
			if (mListener == null) {
				return;
			}
			if (v == okTv) { // 确定
				if (!TextUtils.isEmpty(nameEt.getText().toString())) { // 目录名称不为空
					if (FileManagementActivity.isContainMoji(nameEt.getText().toString())) {
						EUtil.showToast(mContext, "名字不能包含表情");
						return;
					}
					if (FileManagementActivity.isContainName(nameEt.getText().toString())) {
						EUtil.showToast(mContext, "文件名已存在");
						return;
					}else {
						mListener.onClick(nameEt.getText().toString());
						setBack(true);
						dismiss();
					}
				} else {
					EUtil.showToast(mContext, "输入不能为空");
				}
			} else if (v == cancelTv) { // 取消
				mListener.onClick(null);
				setBack(false);
				dismiss();
			}
		}
	}

	public interface OnEditDialogClickListener {
		public void onClick(String evaluationValue);
	}

	/**
	 * 设置监听函数
	 * 
	 * @param listener
	 */
	public void setOnDialogClickListener(OnEditDialogClickListener listener) {
		mListener = listener;
	}

}
