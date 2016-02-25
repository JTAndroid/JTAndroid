package com.tr.ui.user.frg;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.tr.R;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.user.modified.MarkTagActivity;

public class FrgRegisterPersonalTwo extends JBaseFragment implements OnClickListener{

	private EditText companyEt, hangyeEt;
	private TextView nextTv;
	private String nick, image;
	private int sex;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		nick = getArguments().getString("nick");
		image = getArguments().getString("image");
		sex = getArguments().getInt("sex");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.user_frg_register_personal_two,
				container, false);
		companyEt = (EditText) view.findViewById(R.id.companyEt);
		hangyeEt = (EditText) view.findViewById(R.id.hangyeEt);
		nextTv = (TextView) view.findViewById(R.id.nextTv);
		
		nextTv.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.nextTv:
			Intent intent = new Intent(getActivity(), MarkTagActivity.class);
			intent.putExtra("nick", nick);
			intent.putExtra("image", image);
			intent.putExtra("sex", sex);
			intent.putExtra("company", companyEt.getText().toString());
			intent.putExtra("hangye", hangyeEt.getText().toString());
			startActivity(intent);
			break;
		}
	}
	
}
