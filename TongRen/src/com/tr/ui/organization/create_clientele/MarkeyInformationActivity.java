package com.tr.ui.organization.create_clientele;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tr.R;
import com.tr.ui.organization.model.Relation;
import com.tr.ui.organization.model.profile.CustomerListing;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.view.MyEditTextView;
/**
 * 上市信息
 * @author Wxh07151732
 *
 */
public class MarkeyInformationActivity extends BaseActivity implements OnClickListener {
	private LinearLayout markey_Ll;
	private MyEditTextView markey_data_Etv;
	private MyEditTextView markey_price_Etv;
	private MyEditTextView markey_mode_Etv;
	private MyEditTextView markey_referrer_Etv;
	private MyEditTextView markey_stock_Etv;
	private MyEditTextView markey_profit_Etv;
	private MyEditTextView markey_underwriter_Etv;
	private MyEditTextView markey_institution_Etv;
	private MyEditTextView markey_specification_Etv;
	private RelativeLayout quit_markey_info_Rl;
	private ArrayList<String> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.org_markey_information);
		list = new ArrayList<String>();
		init();
		initData();
	}

	private void initData() {
		markey_data_Etv.setOnClickListener(this);
		quit_markey_info_Rl.setOnClickListener(this);
	}

	private void init() {
		quit_markey_info_Rl = (RelativeLayout) findViewById(R.id.quit_markey_info_Rl);
		markey_Ll = (LinearLayout) findViewById(R.id.markey_Ll);
		markey_data_Etv = (MyEditTextView) findViewById(R.id.markey_data_Etv);
		markey_price_Etv = (MyEditTextView) findViewById(R.id.markey_price_Etv);
		markey_mode_Etv = (MyEditTextView) findViewById(R.id.markey_mode_Etv);
		markey_referrer_Etv = (MyEditTextView) findViewById(R.id.markey_referrer_Etv);
		markey_stock_Etv = (MyEditTextView) findViewById(R.id.markey_stock_Etv);
		markey_profit_Etv = (MyEditTextView) findViewById(R.id.markey_profit_Etv);
		markey_underwriter_Etv = (MyEditTextView) findViewById(R.id.markey_underwriter_Etv);
		markey_institution_Etv = (MyEditTextView) findViewById(R.id.markey_institution_Etv);
		markey_specification_Etv = (MyEditTextView) findViewById(R.id.markey_specification_Etv);
		
		CustomerListing listing = (CustomerListing) this.getIntent().getSerializableExtra("Markey_information_Bean");
		if (listing!=null) {
			markey_data_Etv.setText(listing.beginTime);
			markey_price_Etv.setText(listing.price);
			 markey_mode_Etv.setText(listing.type );
			 markey_referrer_Etv.setText(listing.referee.relation);
			 markey_stock_Etv.setText(listing.shares);
			 markey_profit_Etv.setText(listing.profit);
			markey_underwriter_Etv.setText(listing.underwriter);
			 markey_specification_Etv.setText(listing.sponsor.relation );
			markey_institution_Etv.setText(listing.taskId );
		}
	}
	public void finish(View v){
		if (!TextUtils.isEmpty(markey_data_Etv.getText())) {
			list.add(markey_data_Etv.getTextLabel()+"_"+markey_data_Etv.getText());
		}
		if (!TextUtils.isEmpty(markey_price_Etv.getText())) {
			list.add(markey_price_Etv.getTextLabel()+"_"+markey_price_Etv.getText());
		}
		if (!TextUtils.isEmpty(markey_mode_Etv.getText())) {
			list.add(markey_mode_Etv.getTextLabel()+"_"+markey_mode_Etv.getText());
		}
		if (!TextUtils.isEmpty(markey_referrer_Etv.getText())) {
			list.add(markey_referrer_Etv.getTextLabel()+"_"+markey_referrer_Etv.getText());
		}
		if (!TextUtils.isEmpty(markey_stock_Etv.getText())) {
			list.add(markey_stock_Etv.getTextLabel()+"_"+markey_stock_Etv.getText());
		}
		if (!TextUtils.isEmpty(markey_profit_Etv.getText())) {
			list.add(markey_profit_Etv.getTextLabel()+"_"+markey_profit_Etv.getText());
		}
		if (!TextUtils.isEmpty(markey_underwriter_Etv.getText())) {
			list.add(markey_underwriter_Etv.getTextLabel()+"_"+markey_underwriter_Etv.getText());
		}
		if (!TextUtils.isEmpty(markey_specification_Etv.getText())) {
			list.add(markey_specification_Etv.getTextLabel()+"_"+markey_specification_Etv.getText());
		}
		if (!TextUtils.isEmpty(markey_institution_Etv.getText())) {
			list.add(markey_institution_Etv.getTextLabel()+"_"+markey_institution_Etv.getText());
		}
		CustomerListing listing = new CustomerListing();
		listing.beginTime=markey_data_Etv.getText();
		listing.price=markey_price_Etv.getText();
		listing.type = markey_mode_Etv.getText();
		Relation referee =  new Relation();
		referee.relation = markey_referrer_Etv.getText();
		listing.referee = referee;
		listing.shares = markey_stock_Etv.getText();
		listing.profit = markey_profit_Etv.getText();
		listing.underwriter = markey_underwriter_Etv.getText();
		Relation sponsor =  new Relation();
		sponsor.relation = markey_specification_Etv.getText();
		listing.sponsor =sponsor;
		listing.taskId = markey_institution_Etv.getText();
		
		Bundle bundle = new Bundle();
		bundle.putSerializable("Markey_information_Bean", listing);
		if (!list.isEmpty()) {
			Intent intent = new Intent(this, CreateClienteleActivity.class);
			intent.putStringArrayListExtra("Markey_information", list);
			intent.putExtras(bundle);
			setResult(7013, intent);
		}
		finish();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.markey_data_Etv:
			Intent intent = new Intent(this, MarkeyDataActivity.class);
			startActivityForResult(intent, 0);
			break;
		case R.id.quit_markey_info_Rl:
				finish();
			break;
		default:
			break;
		}
	}
	@Override
	protected void onActivityResult(int resultCode, int requestCode, Intent data) {
		super.onActivityResult(resultCode, requestCode, data);
		if (data!=null) {
			switch (requestCode) {
			case 9000:
				String Markey_Data = data.getStringExtra("Markey_Data");
				markey_data_Etv.setText(Markey_Data);
				break;

			default:
				break;
			}
		}
	}
}
