package com.tongmeng.alliance.view;

import com.tr.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InfoDialog extends Activity implements OnClickListener{

	EditText edittext;
	Button cancleBtn,okBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.addinfo_dialog);
		
		edittext = (EditText) findViewById(R.id.addinfo_text);
		cancleBtn = (Button) findViewById(R.id.addinfo_cancel);
		okBtn = (Button) findViewById(R.id.addinfo_ok);
		
		cancleBtn.setOnClickListener(this);
		okBtn.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.addinfo_cancel:
			finish();
			break;
		case R.id.addinfo_ok:
			String s = edittext.getText().toString();
			Log.e("InfoDialog::", "输入字段"+s);
			if(TextUtils.equals(s,null) || "".endsWith(s)){
				Toast.makeText(this, "请输入您的自定义字段！", 0).show();
			}else if(s.length()>6){
				Toast.makeText(this, "您输入的自定义字段已超过6个汉字！", 0).show();
			}else{
				Intent intent = new Intent();
				intent.putExtra("custom", s);
				setResult(RESULT_OK, intent);
				finish();
			}
			
			break;

		default:
			break;
		}
	}
}
