package com.tr.ui.widgets;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.R;
import com.tr.model.conference.MSociality;
import com.tr.model.obj.JTFile;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 社交分享弹出框
 * @author leon
 */
public class SocialShareDialog extends Dialog implements android.view.View.OnClickListener{

	private final String TAG = getClass().getSimpleName();
	
	// 变量
	private Context context;
	private JTFile jtFile; // 待分享的对象
	private Object object; // 社交列表对象
	
	// 控件
	private TextView titleTv; // 标题
	private ImageView avatarIv; // 图片
	private TextView contentTv; // 内容
	private EditText messageEt; // 消息
	private TextView cancelTv; // 取消
	private TextView okTv; // 确定
	private OnDialogClickListener listener;
	
	public SocialShareDialog(Context context) {
		super(context, R.style.SocialShareDialogStyle);
		setContentView(R.layout.widget_social_share_dialog);
		initControls();
	}
	
	private void initControls(){
		titleTv = (TextView) findViewById(R.id.titleTv);
		avatarIv = (ImageView) findViewById(R.id.avatarIv);
		contentTv = (TextView) findViewById(R.id.contentTv);
		messageEt = (EditText) findViewById(R.id.messageEt);
		cancelTv = (TextView) findViewById(R.id.cancelTv);
		cancelTv.setOnClickListener(this);
		okTv = (TextView) findViewById(R.id.okTv);
		okTv.setOnClickListener(this);
	}
	
	/**
	 * 初始化界面
	 * @param jtFile
	 */
	private void initWithData(JTFile jtFile){
		if(jtFile != null){
			switch(jtFile.mType){
			case JTFile.TYPE_REQUIREMENT: // 需求
				titleTv.setText(jtFile.mFileName); // 标题
				contentTv.setText(jtFile.reserved1);
				avatarIv.setVisibility(View.GONE); // 不需要显示图片
				break;
			case JTFile.TYPE_KNOWLEDGE: // 旧知识
				titleTv.setText(jtFile.mSuffixName);
				contentTv.setText(jtFile.mUrl);
				if(!TextUtils.isEmpty(jtFile.mUrl)){
					ImageLoader.getInstance().displayImage(jtFile.mUrl, avatarIv);
				}
				else{
					avatarIv.setVisibility(View.GONE); // 默认图片
				}
				break;
			case JTFile.TYPE_KNOWLEDGE2:  // 新知识
				titleTv.setText(jtFile.reserved2);
				contentTv.setText(jtFile.mSuffixName);
				if(!TextUtils.isEmpty(jtFile.mUrl)){
					ImageLoader.getInstance().displayImage(jtFile.mUrl, avatarIv);
				}
				else{
					avatarIv.setVisibility(View.GONE); // 默认图片
				}
				break;
			case JTFile.TYPE_CONFERENCE: // 会议
				titleTv.setText(jtFile.mSuffixName);
				contentTv.setText(jtFile.reserved1);
				if(!TextUtils.isEmpty(jtFile.mUrl)){
					ImageLoader.getInstance().displayImage(jtFile.mUrl, avatarIv);
				}
				else{
					avatarIv.setVisibility(View.GONE); // 默认图片
				}
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 显示对话框
	 * @param jtFile
	 */
	public void show(Object object, JTFile jtFile){
		initWithData(jtFile);
		this.jtFile = jtFile;
		this.object = object;
		super.show();
	}

	@Override
	public void onClick(View v) {
		if(v == cancelTv){ // 取消
			if(listener != null){
				listener.onCancel();
			}
		}
		else if(v == okTv){ // 确定
			if(jtFile != null){
				switch (jtFile.mType) {
				case JTFile.TYPE_REQUIREMENT:
					jtFile.mSuffixName = messageEt.getText().toString();
					break;
				case JTFile.TYPE_KNOWLEDGE:
				case JTFile.TYPE_KNOWLEDGE2:
				case JTFile.TYPE_CONFERENCE:
					jtFile.mFileName = messageEt.getText().toString();
					break;
				default:
					break;
				}
				if(listener != null){
					listener.onOK(object, jtFile);
				}
			}
		}
		dismiss();
	}
	
	/**
	 * 设置监听函数
	 * @param listener
	 */
	public void setOnDialogClickListener(OnDialogClickListener listener){
		this.listener = listener;
	}
	
	/**
	 * 对话框点击事件接口
	 * @author leon
	 */
	public interface OnDialogClickListener{
		public void onCancel();
		public void onOK(Object object, JTFile jtFile);
	}
}
