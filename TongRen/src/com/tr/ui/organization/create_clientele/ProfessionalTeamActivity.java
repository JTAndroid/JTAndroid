package com.tr.ui.organization.create_clientele;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tr.R;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.CustomActivity;
import com.tr.ui.people.cread.view.MyAddMordView;
import com.tr.ui.people.cread.view.MyDeleteView;
import com.tr.ui.people.cread.view.MyEditTextView;
import com.tr.ui.people.cread.view.MyLineraLayout;
/**
 * 专业团队
 * @author Wxh07151732
 *
 */
public class ProfessionalTeamActivity extends BaseActivity implements OnClickListener{
	private Intent intent;
	private MyAddMordView linkman_MAMV;
	private MyDeleteView linkman_delete_Mdv;
	private MyEditTextView linkman_name_Etv,  linkman_phone_Etv,linkman_work_Etv;
	private MyEditTextView linkman_location_Etv,linkman_area_Etv,custom_Text_Etv;
	private ArrayList<String> list1;
	private ArrayList<MyEditTextView> list2;
	private ArrayList<MyLineraLayout> layouts;
	private LinearLayout team_main_LL;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.org_pofessional_team);
		list1 = new ArrayList<String>();
		list2 = new ArrayList<MyEditTextView>();
		layouts = new ArrayList<MyLineraLayout>();
		
		linkman_MAMV = (MyAddMordView) findViewById(R.id.linkman_MAMV);
		linkman_delete_Mdv = (MyDeleteView) findViewById(R.id.linkman_delete_Mdv);
		
		linkman_name_Etv = (MyEditTextView) findViewById(R.id.linkman_name_Etv);
		custom_Text_Etv = (MyEditTextView) findViewById(R.id.custom_Text_Etv);
		linkman_phone_Etv = (MyEditTextView) findViewById(R.id.linkman_phone_Etv);
		linkman_work_Etv = (MyEditTextView) findViewById(R.id.linkman_work_Etv);
		linkman_location_Etv = (MyEditTextView) findViewById(R.id.linkman_location_Etv);
		linkman_area_Etv = (MyEditTextView) findViewById(R.id.linkman_area_Etv);
		
		team_main_LL = (LinearLayout) findViewById(R.id.team_main_LL);
		
		custom_Text_Etv.setOnClickListener(this);
		linkman_MAMV.setOnClickListener(this);
		linkman_delete_Mdv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_Text_Etv://跳转到自定义页面
			intent = new Intent(this,CustomActivity.class);
			startActivity(intent);
			break;
		case R.id.linkman_MAMV:
			list1.add(linkman_name_Etv.getTextLabel());
			list1.add(linkman_phone_Etv.getTextLabel());
			list1.add(linkman_work_Etv.getTextLabel());
			list1.add(linkman_location_Etv.getTextLabel());
			list1.add(linkman_area_Etv.getTextLabel());
			list1.add(custom_Text_Etv.getTextLabel());
			ContinueAdd2(list1, team_main_LL, list2, layouts);
			break;
		case R.id.linkman_delete_Mdv:
//			Toast.makeText(this, "删除", 0).show();
			break;
		}
		
	}
}
