package com.tr.ui.widgets;

import com.tr.R;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class AddFriendDialog extends Dialog {
	
	private EditText messageEt;
	private Button addBtn;
	private Context mContext;
	private OnAddFriendListener mAddFriendListener;
	
	public AddFriendDialog(Context context){
		super(context);
		mContext = context;
		View container = LayoutInflater.from(mContext)
				.inflate(R.layout.widget_popup_add_friend, null);
		messageEt = (EditText) container.findViewById(R.id.messageEt);
		addBtn = (Button) container.findViewById(R.id.addBtn);
		addBtn.setOnClickListener(mClickListener);
		// 去掉标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(container);
	}
	
	private View.OnClickListener mClickListener =new View.OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v == addBtn){
				mAddFriendListener.add(messageEt.getText().toString());
			}
			dismiss();
		}
	};
	
	/**
	 * 设置监听器
	 * @param listener
	 */
	public void setOnAddFriendListener(OnAddFriendListener listener){
		mAddFriendListener = listener;
	}
	
	/**
	 * 显示内容
	 * @param extra
	 */
	public void show(String extra){
		messageEt.setText(extra);
		super.show();
	}
	
	public interface OnAddFriendListener{
		public void add(String message);
	}
	
}
