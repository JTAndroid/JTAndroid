package com.tr.ui.organization.create_clientele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tr.R;
import com.tr.ui.organization.model.Relation;
import com.tr.ui.organization.model.profile.CustomerInfo;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.CustomActivity;
import com.tr.ui.people.cread.view.MyDeleteView;
import com.tr.ui.people.cread.view.MyEditTextView;
import com.tr.ui.people.cread.view.WordWrapView;

/**
 * 电视广播
 * 
 * @author Wxh07151732
 * 
 */
public class TelecastActivity extends BaseActivity implements OnClickListener {
	private RelativeLayout quit_telecast_Rl;
	private MyEditTextView telecast_principal_Etv;
	private MyEditTextView telecast_program_Etv;
	private MyEditTextView telecast_compere_Etv;
	private MyEditTextView telecast_channel_Etv;
	private MyEditTextView telecast_audience_Etv;
	private MyEditTextView telecast_superior_Etv;
	private MyEditTextView telecast_url_Etv;
	private MyEditTextView telecast_custom_Etv;
	private MyEditTextView telecast_history_Etv;
	private MyDeleteView telecast_delete_Mdv;
	private WordWrapView telecast_label_Wwv;
	private ImageView telecast_label_Iv;
	private ArrayList<String> list;
	private List<String> typesList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.org_telecast);
		editTextViews = new ArrayList<MyEditTextView>();
		list = new ArrayList<String>();
		typesList = new ArrayList<String>();
		init();
		initData();
	}

	private void initData() {
		quit_telecast_Rl.setOnClickListener(this);
		telecast_label_Iv.setOnClickListener(this);
		telecast_custom_Etv.setOnClickListener(this);
		telecast_delete_Mdv.setOnClickListener(this);
	}

	public void finish(View v) {
		if (!TextUtils.isEmpty(telecast_principal_Etv.getText())) {
			list.add(telecast_principal_Etv.getTextLabel() + "_"
					+ telecast_principal_Etv.getText());
		}
		if (!TextUtils.isEmpty(telecast_program_Etv.getText())) {
			list.add(telecast_program_Etv.getTextLabel() + "_"
					+ telecast_program_Etv.getText());
		}
		if (!TextUtils.isEmpty(telecast_compere_Etv.getText())) {
			list.add(telecast_compere_Etv.getTextLabel() + "_"
					+ telecast_compere_Etv.getText());
		}
		if (!TextUtils.isEmpty(telecast_channel_Etv.getText())) {
			list.add(telecast_channel_Etv.getTextLabel() + "_"
					+ telecast_channel_Etv.getText());
		}
		if (!TextUtils.isEmpty(telecast_audience_Etv.getText())) {
			list.add(telecast_audience_Etv.getTextLabel() + "_"
					+ telecast_audience_Etv.getText());
		}
		if (!TextUtils.isEmpty(telecast_superior_Etv.getText())) {
			list.add(telecast_superior_Etv.getTextLabel() + "_"
					+ telecast_superior_Etv.getText());
		}
		if (!TextUtils.isEmpty(telecast_url_Etv.getText())) {
			list.add(telecast_url_Etv.getTextLabel() + "_"
					+ telecast_url_Etv.getText());
		}
		if (!TextUtils.isEmpty(telecast_custom_Etv.getText())) {
			list.add(telecast_custom_Etv.getTextLabel() + "_"
					+ telecast_custom_Etv.getText());
		}
		if (!TextUtils.isEmpty(telecast_history_Etv.getText())) {
			list.add(telecast_history_Etv.getTextLabel() + "_"
					+ telecast_history_Etv.getText());
		}
		CustomerInfo customerInfo = new CustomerInfo();
		Relation leader = new Relation();
		leader.relation = telecast_principal_Etv.getText();
		customerInfo.leader = leader;
		customerInfo.famous = telecast_program_Etv.getText();
		Relation hostess = new Relation();
		hostess.relation = telecast_compere_Etv.getText();
		customerInfo.hostess = hostess;
		for (int i = 0; i < telecast_label_Wwv.getChildCount(); i++) {
			TextView childAt = (TextView) telecast_label_Wwv.getChildAt(i);
			typesList.add(childAt.getText().toString());
		}
		customerInfo.typesList = typesList;
		Relation parentOrg = new Relation();
		parentOrg.relation = telecast_superior_Etv.getText();
		customerInfo.devArea = telecast_audience_Etv.getText();
		customerInfo.parentOrg = parentOrg;
		customerInfo.website = telecast_url_Etv.getText();
		customerInfo.history = telecast_history_Etv.getText();
		Bundle bundle = new Bundle();
		bundle.putSerializable("Telecast_Bean", customerInfo);
		if (!list.isEmpty()) {
			Intent intent = new Intent();
			intent.putStringArrayListExtra("Telecast_Activity", list);
			intent.putExtras(bundle);
			setResult(7011, intent);
		}
		finish();
	}

	private void init() {
		quit_telecast_Rl = (RelativeLayout) findViewById(R.id.quit_telecast_Rl);
		telecast_principal_Etv = (MyEditTextView) findViewById(R.id.telecast_principal_Etv);
		telecast_program_Etv = (MyEditTextView) findViewById(R.id.telecast_program_Etv);
		telecast_compere_Etv = (MyEditTextView) findViewById(R.id.telecast_compere_Etv);
		telecast_channel_Etv = (MyEditTextView) findViewById(R.id.telecast_channel_Etv);
		telecast_audience_Etv = (MyEditTextView) findViewById(R.id.telecast_audience_Etv);
		telecast_superior_Etv = (MyEditTextView) findViewById(R.id.telecast_superior_Etv);
		telecast_url_Etv = (MyEditTextView) findViewById(R.id.telecast_url_Etv);
		telecast_custom_Etv = (MyEditTextView) findViewById(R.id.telecast_custom_Etv);
		telecast_history_Etv = (MyEditTextView) findViewById(R.id.telecast_history_Etv);
		telecast_delete_Mdv = (MyDeleteView) findViewById(R.id.telecast_delete_Mdv);
		telecast_label_Wwv = (WordWrapView) findViewById(R.id.telecast_label_Wwv);
		telecast_label_Iv = (ImageView) findViewById(R.id.telecast_label_Iv);
		org_telecast_Ll = (LinearLayout) findViewById(R.id.org_telecast_Ll);
		CustomerInfo customerInfo = (CustomerInfo) this.getIntent()
				.getSerializableExtra("Telecast_Bean");
		if (customerInfo != null) {
			telecast_principal_Etv.setText(customerInfo.leader.relation);
			telecast_program_Etv.setText(customerInfo.famous);
			telecast_compere_Etv.setText(customerInfo.hostess.relation);
			for (int i = 0; i < customerInfo.typesList.size(); i++) {
				TextView textView = new TextView(context);
				textView.setText(customerInfo.typesList.get(i));
				telecast_label_Wwv.addView(textView);
			}
			telecast_superior_Etv.setText(customerInfo.parentOrg.relation);
			telecast_audience_Etv.setText(customerInfo.devArea);
			telecast_url_Etv.setText(customerInfo.website);
			telecast_history_Etv.setText(customerInfo.history);
		}
	}

	private AlertDialog create;
	private TextView alertdialog_Tv;
	private TextView alertdialog_Yes;
	private TextView alertdialog_No;
	private EditText alertdialog_Et;
	private boolean isNull;
	private LinearLayout org_telecast_Ll;
	private ArrayList<CustomerPersonalLine> arrayList;
	private ArrayList<MyEditTextView> editTextViews;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.quit_telecast_Rl:
			finish();
			break;
		case R.id.telecast_label_Iv:

			AlertDialog.Builder builder = new Builder(this);
			View view2 = View.inflate(this, R.layout.people_alertdialog_module,
					null);
			builder.setView(view2);
			create = builder.create();
			alertdialog_Tv = (TextView) view2
					.findViewById(R.id.alertdialog_module_Tv);
			alertdialog_Yes = (TextView) view2
					.findViewById(R.id.alertdialog_module_Yes);
			alertdialog_No = (TextView) view2
					.findViewById(R.id.alertdialog_module_No);
			alertdialog_Et = (EditText) view2
					.findViewById(R.id.alertdialog_module_Et);
			alertdialog_Tv.setText("添加新的性质");
			alertdialog_Yes.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String text = alertdialog_Et.getText().toString().trim();
					TextView view = new TextView(TelecastActivity.this);
					view.setText(text);
					telecast_label_Wwv.addView(view);
					create.dismiss();
				}
			});
			alertdialog_No.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					create.dismiss();
				}
			});
			create.show();

			break;
		case R.id.telecast_custom_Etv:
			Intent intent = new Intent(this, CustomActivity.class);
			intent.putExtra("fengxing", true);
			if (arrayList!=null) {
				if (!isNull) {
					Bundle bundle = new Bundle();
					bundle.putSerializable("Customer_Bean", arrayList);
					intent.putExtras(bundle);
				}
				
			}
			startActivityForResult(intent, 0);
			break;
		case R.id.telecast_delete_Mdv:
			finish();
			break;
		default:
			break;
		}
	}
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg2!=null) {
			switch (arg0) {
			case 999:
				arrayList = custom2(arg2, telecast_custom_Etv, org_telecast_Ll, isNull,editTextViews);
				break;

			default:
				break;
			}
		}
	
	}
}
