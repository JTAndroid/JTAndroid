package com.tr.ui.organization.create_clientele;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.tr.R;
import com.tr.ui.organization.orgdetails.EditClientBasicInfomationActivity;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.utils.MakeListView;
/**
 * 上市信息跳转选择
 * @author Wxh07151732
 *
 */
public class MarketActivity extends BaseActivity {
	private ListView market_Lv;
	private String[] market ={"上市公司","非上市公司"};
	private RelativeLayout quit_market_Rl;
	private int market2;
	private int marketlabel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.org_market_activity);
		marketlabel = this.getIntent().getIntExtra("marketlabel", 1);
		init();
		initData();
	}

	private void initData() {
		MakeListView.makelistviewAdapter(context, market_Lv, market );
		market_Lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String text = market[position];
				if(marketlabel == 0){
					Intent intent = new Intent(MarketActivity.this, CreateClienteleActivity.class);
					intent.putExtra("market", text);
					setResult(20, intent);
					finish();
				}else if(marketlabel == 1){
					Intent intent = new Intent(MarketActivity.this, EditClientBasicInfomationActivity.class);
					intent.putExtra("market", text);
					setResult(20, intent);
					finish();
				}
			
				
			}
		});
	}

	private void init() {
		market_Lv = (ListView) findViewById(R.id.market_Lv);
		quit_market_Rl = (RelativeLayout) findViewById(R.id.quit_market_Rl);
		quit_market_Rl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
