package com.tr.ui.widgets;


import com.tr.R;
import com.tr.model.model.PeoplePersonalLine;
import com.utils.common.EUtil;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 人脉对话框(自定义字段)
 * @author leon
 *
 */
public class ConnsCustomTagDialog extends Dialog {

	private final String TAG = getClass().getSimpleName();
	
	// 控件
	private TextView cancelTv; // 取消
	private TextView okTv; // 确定
	private EditText keyEt; // 标签名
	private EditText valueEt; // 标签值
	private ImageView sendIv; // 确定添加
	
	// 变量
	private Activity mContext;
	private PeoplePersonalLine mTag;
	private OnFinishListener mListener;
	
	public ConnsCustomTagDialog(Activity context, PeoplePersonalLine tag,OnFinishListener listener) {
		super(context,R.style.ConnsDialogTheme);
		setContentView(R.layout.widget_conns_custom_tag_dialog);
		mContext = context;
		mListener = listener;
		mTag = tag;
		initDialogStyle();
		initControls();
	}
	
	// 初始化对话框样式
	private void initDialogStyle(){
		getWindow().setWindowAnimations(R.style.ConnsDialogAnim);
		WindowManager.LayoutParams wmlp = getWindow().getAttributes();
		wmlp.width = mContext.getWindowManager().getDefaultDisplay().getWidth();
		wmlp.gravity = Gravity.BOTTOM ;
	}
	
	// 初始化控件
	private void initControls(){
		// 取消
		cancelTv = (TextView) findViewById(R.id.cancelTv);
		cancelTv.setOnClickListener(mClickListener);
		// 确定
		okTv = (TextView) findViewById(R.id.okTv);
		okTv.setOnClickListener(mClickListener);
		// 标签名
		keyEt = (EditText) findViewById(R.id.keyEt);
		// 标签值
		valueEt = (EditText) findViewById(R.id.valueEt);
		// 确定添加
		sendIv = (ImageView) findViewById(R.id.sendIv);
		sendIv.setOnClickListener(mClickListener);
		
		if(mTag != null){ // 编辑
			
			keyEt.setText(mTag.name);
			valueEt.setText(mTag.content);
			okTv.setVisibility(View.VISIBLE);
			sendIv.setVisibility(View.GONE);
		}
	}
	
	
	
	private View.OnClickListener mClickListener = new View.OnClickListener(){

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.cancelTv: // 取消
				ConnsCustomTagDialog.this.dismiss();
				break;
			case R.id.okTv:  // 完成
			case R.id.sendIv:
				
				if(mTag == null){
					mTag = new PeoplePersonalLine();
				}
				String key = keyEt.getText().toString();
				String value = valueEt.getText().toString();
				
				if(TextUtils.isEmpty(key)){
					EUtil.showToast("标签名不能为空");
				}
				else if(key.length() > 5){
					EUtil.showToast("标签名长度不能超过5个字符");
				}
				else{
					mTag.name = (key);
					mTag.content =(value);
					if (mListener != null) {
						mListener.onFinish(mTag);
					}
					EUtil.showToast("添加成功");
					keyEt.setText("");
					valueEt.setText("");
				}
				break;
			}
		}
	};
	
	
	public interface OnFinishListener{
		public void onFinish(PeoplePersonalLine tag);
	}

}
