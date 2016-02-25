package com.tr.ui.organization.orgdetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tr.R;
import com.tr.model.demand.Metadata;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.demand.util.ChooseDataUtil;
import com.tr.ui.organization.create_clientele.ClientDataActivity;
import com.tr.ui.organization.create_clientele.ClientIntroductionActivity;
import com.tr.ui.organization.create_clientele.MarketActivity;
import com.tr.ui.organization.create_clientele.OrgTypeActivity;
import com.tr.ui.organization.firstpage.BasicData;
import com.tr.ui.organization.firstpage.OrganizationDataActivity;
import com.tr.ui.organization.firstpage.OrganizationIntroductionActivity;
import com.tr.ui.organization.firstpage.RelateDescriptionActivity;
import com.tr.ui.organization.model.Customer;
import com.tr.ui.organization.model.JCustomer;
import com.tr.ui.organization.model.industry.CustomerIndustry;
import com.tr.ui.organization.model.param.ClientDetailsParams;
import com.tr.ui.organization.model.profile.CustomerInfo;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.RemarkActivity;
import com.tr.ui.people.cread.utils.MakeListView;
import com.tr.ui.people.cread.view.MyEditTextView;
import com.tr.ui.people.cread.view.MyitemView;
import com.utils.common.EConsts;

public class EditClientBasicInfomationActivity extends BaseActivity implements OnClickListener{
    
	private ImageView edit_basic_iv;
	private TextView edit_basic_tv;
	private MyEditTextView edit_basic_custom;
	private MyEditTextView edit_basic_introduction;
	private MyEditTextView edit_basic_email;
	private MyEditTextView edit_basic_phone;
	private ClientDetailsParams customer;
	private EditText edit_basicinfomation_edittext;
	private ImageView industry_iv;
	private EditText industry_EditText;
	private BasicData basicData;
	private static MyEditTextView basic_org_type_Etv;
	private MyEditTextView basic_org_market_Etv;
	private TextView edit_client_money;
	private boolean isNull;
	private ArrayList<CustomerPersonalLine> arrayList;
	private int Type = 1;
	private CustomerInfo customerInfo;
	private int requestCode;
	private ArrayList<String> listview_item;
	private HashMap<String, MyitemView> module_Map;
	private String remark;
	private RelativeLayout rl_stockNum;
	private int marketlabel = 1;
	
	public static Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String type = (String) msg.obj;
			basic_org_type_Etv.setText(type);
		};
	};
	
	
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        	super.onCreate(savedInstanceState);
        	setContentView(R.layout.edit_clientbasicinfomation);
        	customer = (ClientDetailsParams)getIntent().getSerializableExtra("edit");
        	
        	module_Map = new HashMap<String, MyitemView>();
        	init();
        	initListener();
        	initData();
        }
        
        private void initData(){
        	//简称
        	if(customer.shotName != null){
        		edit_basicinfomation_edittext.setText(customer.shotName);
        	}
        	else{
        		edit_basicinfomation_edittext.setText("");
        	}
        	//行业
        	List<String> lists = customer.industrys;
    		System.out.println(lists.size());
    		String str = "";
    		if(lists!=null && lists.size()>0){
    			for(int i = 0;i<lists.size();i++){
    				if(i == lists.size()-1){
    					str += lists.get(i);
    				}else{
    					str += lists.get(i) +",";
    				}
    			}
    		}
    		industry_EditText.setText(str);
        	//邮箱
        	if(customer.linkEmail != null){
        		edit_basic_email.setText(customer.linkEmail);
        	}
        	else{
        		edit_basic_email.setText("");
        	}
        	//联系电话
        	if(customer.linkMobile != null){
        		edit_basic_phone.setText(customer.linkMobile);
        	}
        	else{
        		edit_basic_phone.setText("");
        	}
        	//组织简介
        	if(customer.discribe != null){
        		edit_basic_introduction.setText(customer.discribe);
        	}
        	else{
        		edit_basic_introduction.setText("");
        	}
        	
        	requestCode = getIntent().getIntExtra(EConsts.Key.REQUEST_CODE, 0);
        }

		private void initListener() {
			edit_basic_iv.setOnClickListener(this);
        	edit_basic_tv.setOnClickListener(this);
        	edit_basic_custom.setOnClickListener(this);
        	edit_basic_introduction.setOnClickListener(this);
        	industry_iv.setOnClickListener(this);
        	basic_org_type_Etv.setOnClickListener(this);
        	basic_org_market_Etv.setOnClickListener(this);
		}

		private void init() {
			edit_basic_iv = (ImageView) findViewById(R.id.client_edit_basic_iv);
			
        	edit_basic_tv = (TextView) findViewById(R.id.client_edit_basic_tv);
        	industry_iv = (ImageView) findViewById(R.id.client_industry_iv);
        	edit_basic_custom = (MyEditTextView) findViewById(R.id.client_edit_basic_custom);
        	edit_basic_introduction = (MyEditTextView) findViewById(R.id.client_edit_basic_introduction);
        	edit_basicinfomation_edittext = (EditText) findViewById(R.id.client_edit_basicinfomation_edittext);
        	edit_basic_email = (MyEditTextView) findViewById(R.id.client_edit_basic_email);
        	edit_basic_phone = (MyEditTextView) findViewById(R.id.client_edit_basic_phone);
        	industry_EditText = (EditText) findViewById(R.id.client_industry_EditText);
        	basic_org_type_Etv = (MyEditTextView) findViewById(R.id.basic_org_type_Etv);//客户类型
        	basic_org_market_Etv = (MyEditTextView) findViewById(R.id.basic_org_market_Etv);//上市信息
        	edit_client_money = (TextView) findViewById(R.id.edit_client_money);//证劵代码
        	rl_stockNum = (RelativeLayout) findViewById(R.id.rl_stockNum);
		}
        
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.client_edit_basic_iv://点击后退
//				Intent intent_back = new Intent(this,ClientData_Activity.class);
//				startActivity(intent_back);
				finish();
				break;
		    case R.id.client_edit_basic_tv:// 点击完成,将本页面的数据带到基本资料页面
		    	
		    	BasicData basicData = new BasicData();
		    	basicData.setName(edit_basicinfomation_edittext.getText().toString().trim());
		    	basicData.setIndustry(industry_EditText.getText().toString().trim());
		    	basicData.setEmail(edit_basic_email.getText());
		    	basicData.setDial(edit_basic_phone.getText());
		    	basicData.setIntrodution(edit_basic_introduction.getText());
		    	basicData.setType(basic_org_type_Etv.getText().toString());//客户类型
		    	basicData.setOrgmsg(basic_org_market_Etv.getText().toString());//上市信息
		    	basicData.setOrgnum(edit_client_money.getText().toString());//证劵代码
		    	
		    	Intent intent_finish = new Intent(this,ClientDataActivity.class);
		    	intent_finish.putExtra("basic", basicData);
		    	Log.e("LOG", "PUTBASIC:"+basicData.toString());
		    	setResult(333, intent_finish);
                finish();
			    break;
		    case R.id.client_industry_iv://点击行业，进行筛选
		    	ENavigate.startChooseActivityForResult(this, true, "行业",ChooseDataUtil.CHOOSE_type_Trade, null);
		    	break;
		    case R.id.client_edit_basic_custom://点击自定义文本,跳转到相关描述页面
		    	Intent intent_custom = new Intent(this,RelateDescriptionActivity.class);
		    	startActivityForResult(intent_custom, 100);
		    	break;
		    case R.id.client_edit_basic_introduction://点击客户描述，跳到客户描述页面
		    	Intent intent = new Intent(this,RemarkActivity.class);
		    	if (!TextUtils.isEmpty(remark)) {
					intent.putExtra("Remark_Activity", remark);
				}
				startActivityForResult(intent, 1000);
		    	break;
		    case R.id.basic_org_type_Etv://客户类型
		    	intent = new Intent(this, OrgTypeActivity.class);
		    	intent.putExtra("Type", Type);
		    	intent.putExtra("editClient", true);
		    	Log.e("MSG", "编辑Type:"+Type);
				startActivityForResult(intent, 0);
				break;
		    case R.id.basic_org_market_Etv://上市信息
		    	intent = new Intent(this, MarketActivity.class);
				intent.putExtra("marketlabel", marketlabel);
				startActivityForResult(intent, 1);
				break;
			}
		}
		
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
//			ArrayList<Metadata> backList = (ArrayList<Metadata>) data.getSerializableExtra(ENavConsts.DEMAND_CHOOSE_DATA);
//			List<Metadata> list = ChooseDataUtil.getSelectList(backList); 
//            String backData = list.get(0).name;
//			String name = industry_EditText.getText().toString().trim()+",";
//			industry_EditText.setText(name+backData);
//			
//			if(data != null){
//				switch (requestCode) {
//				case 6001:
//					customerInfo = (CustomerInfo) data
//					.getSerializableExtra("Enterprise_Bean");
//
//			customerProfile.info = customerInfo;
//			customer.type = "2";
//			listview_item = data.getStringArrayListExtra("Enterprise");
//			if (listview_item != null) {
//				// 动态的将联系方式的数据填充到listview中
//				AdaptiveListView(MakeListView.ToListviewAdapter(this,
//						listview_item, module_Map.get("一般企业")
//								.getMyitemview_Lv()), displayMetrics);
//			}
//					break;
//
//				default:
//					break;
//				}
			if (data!=null) {
				
			if(999==resultCode){
				String custom_count = data.getExtras().getString("custom_count");
				String Work_Custom_ID = data.getExtras().getString("Work_Custom_ID");
				String Education_Custom_ID = data.getExtras().getString("Education_Custom_ID");
				String Meeting_Custom_ID = data.getExtras().getString("Meeting_Custom_ID");
				String Society_Custom_ID = data.getExtras().getString("Society_Custom_ID");
				
				edit_basic_custom.setText(custom_count+","+Work_Custom_ID+","+Education_Custom_ID+","+Meeting_Custom_ID+","+Society_Custom_ID);
			}
			if (requestCode == 0) {

				switch (resultCode) {

				case 20:
					String market = data.getStringExtra("market");
					basic_org_market_Etv.setText(market);
					if ("上市公司".equals(market)) {
						edit_client_money.setVisibility(View.VISIBLE);
					} else {
						edit_client_money.setVisibility(View.GONE);
					}
					break;
				case 21:
					String type = data.getStringExtra("type");
					basic_org_type_Etv.setText(type);
					if ("政府组织".equals(type)) {
						basic_org_market_Etv.setVisibility(View.GONE);
						rl_stockNum.setVisibility(View.GONE);

					} else {
						basic_org_market_Etv.setVisibility(View.VISIBLE);
						if ("上市公司".equals(basic_org_market_Etv.getText())) {
							rl_stockNum.setVisibility(View.VISIBLE);
						}
						
					}
					break;

				case 24:
					String media_type = data.getStringExtra("media_type");
					basic_org_type_Etv.setText(media_type);
					if ("政府组织".equals(media_type)) {
						basic_org_market_Etv.setVisibility(View.GONE);
						rl_stockNum.setVisibility(View.GONE);

					} else {
						basic_org_market_Etv.setVisibility(View.VISIBLE);
						rl_stockNum.setVisibility(View.VISIBLE);
					}

					break;
				default:
					break;
				}
			}
			if(1000 == requestCode){
				remark = data.getStringExtra("Remark_Activity");
				edit_basic_introduction.setText(remark);
			}
		}}
		}
