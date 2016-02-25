package com.tr.ui.demand;

import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tr.App;
import com.tr.AppData;
import com.tr.R;
import com.tr.model.demand.AmountData;
import com.tr.model.obj.MoneyType;
import com.tr.navigate.ENavConsts;
import com.tr.ui.base.JBaseActivity;
import com.utils.common.ViewHolder;

/**
 * 币种选择
 * 
 * @author main
 * 
 */
public class CurrencyActivity extends JBaseActivity {

	private ListView currencyLv;// 金额范围
	private List<MoneyType> currencyData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demand_currency);
		initView();
	}

	private void initView() {
		currencyLv = (ListView) findViewById(R.id.currencyLv);
		AppData appData = App.getApp().getAppData();
		currencyData = appData.getListMoneyType();
		CurrencyAdapter adapter = new CurrencyAdapter(this);
		currencyLv.setAdapter(adapter);
		currencyLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MoneyType type = currencyData.get(position);
				AmountData moneyType = new AmountData();
				moneyType.unit =type.tag;
				moneyType.unitName =type.name;
				closeActivity(moneyType);
			}
		});
	}

	public void closeActivity(AmountData moneyType) {
		Intent intent = new Intent();
		intent.putExtra(ENavConsts.DEMAND_MONEY_TAG, moneyType);
		CurrencyActivity.this.setResult(RESULT_OK, intent);
		CurrencyActivity.this.finish();
	}

	@Override
	public void initJabActionBar() {
		ActionBar mActionBar = jabGetActionBar();
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(false);// 不显示应用图标
		mActionBar.setDisplayShowTitleEnabled(true);
		View mCustomView = getLayoutInflater().inflate(
				R.layout.demand_actionbar, null);
		mActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView
				.getLayoutParams();
		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK
				| Gravity.CENTER_HORIZONTAL;
		mActionBar.setCustomView(mCustomView, mP);
		mActionBar.setTitle(" ");
		TextView titleTv = (TextView) mCustomView.findViewById(R.id.titleTv);
		titleTv.setText("币种");
	}

	public class CurrencyAdapter extends BaseAdapter {
		private Context context;

		public CurrencyAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return currencyData != null ? currencyData.size() : 0;
		}

		@Override
		public MoneyType getItem(int arg0) {
			// TODO Auto-generated method stub
			return currencyData.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup paren) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.demand_item, null);
				ViewHolder.get(convertView, R.id.rightLl).setVisibility(View.GONE);
			}
			TextView text = ViewHolder.get(convertView, R.id.nameTv);
			text.setTextColor(getResources()
					.getColor(R.color.demand_text_color));
			MoneyType money = currencyData.get(position);
			text.setText(money.tag + "-" + money.name);
			return convertView;
		}
	}

}
