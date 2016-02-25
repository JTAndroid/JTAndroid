package com.tr.ui.people.cread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.tr.R;
import com.tr.ui.people.cread.nationality.NationalityActivity;
import com.tr.ui.people.cread.view.MyEditTextView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 个人情况(子模块) 
 * 
 * @author Wxh07151732
 * 
 */
public class PersonalInformationActivity extends BaseActivity implements
		OnClickListener {
	private MyEditTextView custom_personal_Etv;
	private MyEditTextView socialRelations_personal_Etv;
	private MyEditTextView data_personal_Etv;
	private MyEditTextView livingHabit_personal_Etv;
	private MyEditTextView hobby_personal_Etv;
	private MyEditTextView physicalCondition_personal_Etv;
	private MyEditTextView belief_personal_Etv;
	private MyEditTextView nativePlace_personal_Etv;
	private MyEditTextView nation_personal_Etv;
	private MyEditTextView nationality_personal_Etv;
	private TextView finish_personal_Tv;
	private ArrayList<String> list;
	private int Personal_information_Activity = 2;
	private String[] data = { "生日", "周年纪念日", "自定义" };
	private String[] socialRelations = { "配偶", "父亲", "母亲", "兄弟", "姐妹", "同居伴侣",
			"子女", "经理", "助手", "合作伙伴", "介绍人" };
	private Intent intent;
	private LinearLayout personal_addmore_ll;
	private LinearLayout delete_Ll;
	private RelativeLayout quit_personal_Rl;
	private ArrayList<MyEditTextView> list2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_personal_information);
		init();
		list = new ArrayList<String>();
		list2 = new ArrayList<MyEditTextView>();
	}

	private void init() {
		quit_personal_Rl = (RelativeLayout) findViewById(R.id.quit_personal_Rl);
		finish_personal_Tv = (TextView) findViewById(R.id.finish_personal_Tv);
		nationality_personal_Etv = (MyEditTextView) findViewById(R.id.nationality_personal_Etv);
		nation_personal_Etv = (MyEditTextView) findViewById(R.id.nation_personal_Etv);
		nativePlace_personal_Etv = (MyEditTextView) findViewById(R.id.nativePlace_personal_Etv);
		belief_personal_Etv = (MyEditTextView) findViewById(R.id.belief_personal_Etv);
		physicalCondition_personal_Etv = (MyEditTextView) findViewById(R.id.physicalCondition_personal_Etv);
		hobby_personal_Etv = (MyEditTextView) findViewById(R.id.hobby_personal_Etv);
		livingHabit_personal_Etv = (MyEditTextView) findViewById(R.id.livingHabit_personal_Etv);
		data_personal_Etv = (MyEditTextView) findViewById(R.id.data_personal_Etv);
		socialRelations_personal_Etv = (MyEditTextView) findViewById(R.id.socialRelations_personal_Etv);
		custom_personal_Etv = (MyEditTextView) findViewById(R.id.custom_personal_Etv);
		personal_addmore_ll = (LinearLayout) findViewById(R.id.personal_addmore_ll);
		delete_Ll =  (LinearLayout) findViewById(R.id.delete_Ll);
		
		delete_Ll.setOnClickListener(this);
		finish_personal_Tv.setOnClickListener(this);
		data_personal_Etv.setOnClickListener(this);
		nationality_personal_Etv.setOnClickListener(this);
		nativePlace_personal_Etv.setOnClickListener(this);
		belief_personal_Etv.setOnClickListener(this);
		physicalCondition_personal_Etv.setOnClickListener(this);
		livingHabit_personal_Etv.setOnClickListener(this);
		hobby_personal_Etv.setOnClickListener(this);
		nation_personal_Etv.setOnClickListener(this);
		custom_personal_Etv.setOnClickListener(this);
		socialRelations_personal_Etv.setOnClickListener(this);
		quit_personal_Rl.setOnClickListener(this);
		data_personal_Etv.MakeAddMoreMethod(data_personal_Etv,
				PersonalInformationActivity.this, 0,personal_addmore_ll, "生日",list2);
		socialRelations_personal_Etv.MakeAddMoreMethod(socialRelations_personal_Etv,
				PersonalInformationActivity.this,0, personal_addmore_ll, "配偶",list2);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.finish_personal_Tv:
			if (!TextUtils.isEmpty(nationality_personal_Etv.getText())) {
				list.add(nationality_personal_Etv.getTextLabel() + "_"
						+ nationality_personal_Etv.getText());

			}
			if (!TextUtils.isEmpty(nation_personal_Etv.getText())) {
				list.add(nation_personal_Etv.getTextLabel() + "_"
						+ nation_personal_Etv.getText());

			}
			if (!TextUtils.isEmpty(nativePlace_personal_Etv.getText())) {
				list.add(nativePlace_personal_Etv.getTextLabel() + "_"
						+ nativePlace_personal_Etv.getText());

			}
			if (!TextUtils.isEmpty(belief_personal_Etv.getText())) {
				list.add(belief_personal_Etv.getTextLabel() + "_"
						+ belief_personal_Etv.getText());

			}
			if (!TextUtils.isEmpty(physicalCondition_personal_Etv.getText())) {
				list.add(physicalCondition_personal_Etv.getTextLabel() + "_"
						+ physicalCondition_personal_Etv.getText());

			}
			if (!TextUtils.isEmpty(hobby_personal_Etv.getText())) {
				list.add(hobby_personal_Etv.getTextLabel() + "_"
						+ hobby_personal_Etv.getText());

			}
			if (!TextUtils.isEmpty(livingHabit_personal_Etv.getText())) {
				list.add(livingHabit_personal_Etv.getTextLabel() + "_"
						+ livingHabit_personal_Etv.getText());

			}
			if (!TextUtils.isEmpty(data_personal_Etv.getText())) {
				list.add(data_personal_Etv.getTextLabel() + "_"
						+ data_personal_Etv.getText());

			}
			if (!TextUtils.isEmpty(socialRelations_personal_Etv.getText())) {
				list.add(socialRelations_personal_Etv.getTextLabel() + "_"
						+ socialRelations_personal_Etv.getText());

			}
			if (!TextUtils.isEmpty(custom_personal_Etv.getText())) {
				list.add(custom_personal_Etv.getTextLabel() + "_"
						+ custom_personal_Etv.getText());

			}
			if (!list2.isEmpty()) {
				for (int i = 0; i < list2.size(); i++) {
					MyEditTextView myEditTextView = list2.get(i);
					list.add(myEditTextView.getTextLabel() + "_"
							+ myEditTextView.getText());
				}
			}
			if (!"[]".equals(list.toString())) {
			intent = new Intent(context, NewConnectionsActivity.class);
			intent.putStringArrayListExtra("Personal_information", list);
			setResult(1, intent);
			}
			finish();
			break;
		case R.id.data_personal_Etv:
			data_personal_Etv.makePopupWindows(PersonalInformationActivity.this,v, data);
			break;
		case R.id.nationality_personal_Etv:
			intent = new Intent(this, NationalityActivity.class);
			startActivityForResult(intent, 0);
			break;
		case R.id.nation_personal_Etv:
			// intent = new Intent(this,Nation_Activity.class);
			// startActivityForResult(intent, 1);
			break;
		case R.id.nativePlace_personal_Etv:
			intent = new Intent(this, NativePlaceAtivity.class);
			startActivityForResult(intent, 2);
			break;
		case R.id.belief_personal_Etv:
			intent = new Intent(this, BeliefActivity.class);
			startActivityForResult(intent, 3);
			break;
		case R.id.physicalCondition_personal_Etv:
			intent = new Intent(this, PhysicalActivity.class);
			startActivityForResult(intent, 4);
			break;
		case R.id.hobby_personal_Etv:
			intent = new Intent(this, HobbyActivity.class);
			startActivityForResult(intent, 5);
			break;
		case R.id.livingHabit_personal_Etv:
			intent = new Intent(this, LivingHabitActivity.class);
			startActivityForResult(intent, 6);
			break;
		case R.id.custom_personal_Etv:
			intent = new Intent(this, CustomActivity.class);
			startActivityForResult(intent, 7);
			break;
		case R.id.socialRelations_personal_Etv:
			socialRelations_personal_Etv.makePopupWindows(this,v, socialRelations);
			break;
		case R.id.quit_personal_Rl:
			finish();
			break;
		case R.id.delete_Ll:
			finish();
			break;
		default:
			break;
		}
		// if (intent!=null) {
		// startActivityForResult(intent, 99);
		// }
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data!=null) {
		switch (resultCode) {
		case 1:
			String Nationality = data.getStringExtra("Nationality");
			nationality_personal_Etv.setText(Nationality);
			break;
		// case 2:
		// String nation = data.getStringExtra("nation");
		// nation_personal_Etv.setText(nation);
		// break;
		case 3:
			String nation_palce = data.getStringExtra("nation_place");
			System.out.println(nation_palce);
			nativePlace_personal_Etv.setText(nation_palce);
			break;
		case 4:
			String belief = data.getStringExtra("belief");
			belief_personal_Etv.setText(belief);
			break;
		case 5:
			 ArrayList<String> stringArrayListExtra1 = data.getStringArrayListExtra("LivingHabit");
			 String text1 = stringArrayListExtra1.toString();
			 livingHabit_personal_Etv.setText(text1);
			 	break;
		case 6:
			 ArrayList<String> stringArrayListExtra2 = data.getStringArrayListExtra("Hobby");
			 String text2 = stringArrayListExtra2.toString();
			 hobby_personal_Etv.setText(text2);
			break;
		case 7:
			 ArrayList<String> stringArrayListExtra3 = data.getStringArrayListExtra("Physical");
			 String text3 = stringArrayListExtra3.toString();
			 physicalCondition_personal_Etv.setText(text3);
			break;
		case 999:
			ArrayList<String> Custom = data.getStringArrayListExtra("Custom");
			if (Custom!=null) {
				if (Custom.size()==1) {
					String text0 = Custom.get(0);
					list.add(text0);
					String[] split = text0.split("_");
					String key0 = split[0];
					String value0 = split[1];
					custom_personal_Etv.setText(value0);
					custom_personal_Etv.setTextLabel(key0);
					custom_personal_Etv.setAddMore_hint(false);
					custom_personal_Etv.setChoose(false);
					custom_personal_Etv.setReadOnly(true);
				}else{
					String text0 = Custom.get(0);
					list.add(text0);
					String[] split = text0.split("_");
					String key0 = split[0];
					String value0 = split[1];
					custom_personal_Etv.setText(value0);
					custom_personal_Etv.setTextLabel(key0);
					custom_personal_Etv.setAddMore_hint(false);
					custom_personal_Etv.setChoose(false);
					custom_personal_Etv.setReadOnly(true);
					String text = Custom.get(1);
					list.add(text);
					String[] split1 = text.split("_");
					String key1 = split1[0];
					String value1 = split1[1];
					MyEditTextView editTextView = new MyEditTextView(this);
					editTextView.setCustomtextLabel(value1);
					editTextView.setTextLabel(key1);
					editTextView.setReadOnly(true);
					editTextView.setCustom_Text(true);
					personal_addmore_ll.addView(editTextView);
				}
			
			}
			break;
		default:
			break;
		}
	}
	}
}
