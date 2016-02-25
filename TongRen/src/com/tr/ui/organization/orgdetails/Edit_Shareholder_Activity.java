package com.tr.ui.organization.orgdetails;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tr.R;
import com.tr.api.OrganizationReqUtil;
import com.tr.ui.organization.create_clientele.ShareholderActivity;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;
import com.tr.ui.organization.model.stock.CustomerStock;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.CustomActivity;
import com.tr.ui.people.cread.view.MyEditTextView;
import com.utils.http.EAPIConsts.OrganizationReqType;
import com.utils.http.IBindData;

public class Edit_Shareholder_Activity extends BaseActivity implements OnClickListener,IBindData{

	private ImageView iv_editsave_back;
	private RelativeLayout rl_addmore;
	private MyEditTextView eidt_custom_Etv;
	private RelativeLayout editsave_back;
	private ArrayList<String> list;
	private LinearLayout edit_Ll;
	private MyEditTextView controlling_shareholder_name_Etv;
	private MyEditTextView controlling_shareholder_ratio_Etv;
	private MyEditTextView concert_party_name_Etv;
	private MyEditTextView concert_party_ratio_Etv;
	private MyEditTextView Ultimate_controlling_people_name_Etv;
	private MyEditTextView Ultimate_controlling_people_ratio_Etv;
	private ArrayList<CustomerPersonalLine> arrayList;
	private ArrayList<MyEditTextView> editTextViews;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		editTextViews = new ArrayList<MyEditTextView>();
		setContentView(R.layout.activity_edit_shareholder);
        editsave_back = (RelativeLayout) findViewById(R.id.editsave_back);
        edit_Ll = (LinearLayout) findViewById(R.id.edit_Ll);
        controlling_shareholder_name_Etv = (MyEditTextView) findViewById(R.id.controlling_shareholder_name_Etv);
        controlling_shareholder_ratio_Etv = (MyEditTextView) findViewById(R.id.controlling_shareholder_ratio_Etv);
        concert_party_name_Etv = (MyEditTextView) findViewById(R.id.concert_party_name_Etv);
        concert_party_ratio_Etv = (MyEditTextView) findViewById(R.id.concert_party_ratio_Etv);
        Ultimate_controlling_people_name_Etv = (MyEditTextView) findViewById(R.id.Ultimate_controlling_people_name_Etv);
        Ultimate_controlling_people_ratio_Etv = (MyEditTextView) findViewById(R.id.Ultimate_controlling_people_ratio_Etv);
        eidt_custom_Etv = (MyEditTextView) findViewById(R.id.eidt_custom_Etv);
        editsave_back.setOnClickListener(this);
        eidt_custom_Etv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Edit_Shareholder_Activity.this, CustomActivity.class);
//				intent.putParcelableArrayListExtra("Custom_Bean", arrayList);
				intent.putExtra("fengxing", true);
				Bundle bundle = new Bundle();
				bundle.putSerializable("Custom_Bean", arrayList);
				intent.putExtras(bundle);
				startActivityForResult(intent, 999);
			}
		});
        list = new ArrayList<String>();
        CustomerStock stock = (CustomerStock) this.getIntent().getParcelableExtra("Edit_Shareholder_Bean");
        if (stock!=null) {
        	 controlling_shareholder_name_Etv.setText(stock.cShareholder);
    		  controlling_shareholder_ratio_Etv.setText(stock.cStockPercent);
    		 concert_party_name_Etv.setText(stock.rShareholder );
    		  concert_party_ratio_Etv.setText(stock.rStockPercent);
    		  Ultimate_controlling_people_name_Etv.setText(stock.fShareholder);
    		  Ultimate_controlling_people_ratio_Etv.setText(stock.fStockPercent);
		}
        
	}
	public void save(View v){
		CustomerStock stock = new CustomerStock();
		stock.cShareholder =controlling_shareholder_name_Etv.getText();
		stock.cStockPercent = controlling_shareholder_ratio_Etv.getText();
		stock.rShareholder = concert_party_name_Etv.getText();
		stock.rStockPercent = concert_party_ratio_Etv.getText();
		stock.fShareholder = Ultimate_controlling_people_name_Etv.getText();
		stock.fStockPercent = Ultimate_controlling_people_ratio_Etv.getText();
		if (stock!=null) {
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putParcelable("Edit_Shareholder_Bean", stock);
			if (arrayList!=null) {
				stock.personalLineList = arrayList;
				bundle.putSerializable("Custom_Bean", arrayList);
//				intent.putParcelableArrayListExtra("Custom_Bean", arrayList);
			}
			
			OrganizationReqUtil.doRequestWebAPI(this, this, stock, null, OrganizationReqType.ORGANIZATION_REQ_SAVEORUPDATE);
			intent.putExtras(bundle);
			setResult(0, intent);
			
		}
		finish();
	}
	@Override
	public void onClick(View v) {
		if(v == editsave_back){
			finish();
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data!=null) {
			switch (resultCode) {
			case 999:
				boolean isNull =  data.getBooleanExtra("isNull", false);
				arrayList = custom2(data, eidt_custom_Etv, edit_Ll,isNull,editTextViews);
				break;

			default:
				break;
			}
		}
	}
	@Override
	public void bindData(int tag, Object object) {
		
	}
}
