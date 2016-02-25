package com.tr.ui.demand;

import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.App;
import com.tr.AppData;
import com.tr.R;
import com.tr.model.demand.AmountData;
import com.tr.navigate.ENavConsts;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.demand.util.AmountUtil;
import com.utils.common.ViewHolder;

/**
 * 金额选择 界面
 * 
 * @author main
 * 
 */
public class AmountOfMoneyActivity extends JBaseActivity {

	private LinearLayout moneyTypeLl; // 顶部显示金额信息
	private TextView moneyTypeTv;
	private ListView sumRange;// 金额范围
	private AmountMoneyAdapter adapter;
	private EditText startSum;// 自定义起始金额
	private EditText finishSum;// 自定义结束金额
	private Button sumOK;// 确认按钮
	private AmountData moneyType;

	private boolean isStart = false; // 起始金额是否输入
	private boolean isFinis = false;// 结束金额是否输入

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demand_money);
		getParam();
		init();// 初始化
	}

	private void getParam() {
		Intent intent = this.getIntent();
		moneyType = (AmountData) intent
				.getSerializableExtra(ENavConsts.DEMAND_MONEY_TAG);
		if (moneyType == null || TextUtils.isEmpty(moneyType.unit)) {
			moneyType = new AmountData();
			moneyType.unitName = "人民币";
			moneyType.unit = "CNY";
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK
				&& requestCode == ENavConsts.ActivityReqCode.REQUEST_MOENY_SELECT) {
			moneyType = (AmountData) data
					.getSerializableExtra(ENavConsts.DEMAND_MONEY_TAG);
			moneyTypeTv.setText(moneyType.unit + "—" + moneyType.unitName);
		}
	}

	public void init() {
		moneyTypeLl = (LinearLayout) findViewById(R.id.moneyTypeLl);
		moneyTypeTv = (TextView) findViewById(R.id.moneyTypeTv);
		sumRange = (ListView) findViewById(R.id.sumRange);
		startSum = (EditText) findViewById(R.id.startSum);
		finishSum = (EditText) findViewById(R.id.finishSum);
		sumOK = (Button) findViewById(R.id.sumOK);

		AppData appData = App.getApp().getAppData();
		List<String> listMoneyRange = appData.getListMoneyRange();

		adapter = new AmountMoneyAdapter(listMoneyRange, this);// 金币范围的adaper
		sumRange.setAdapter(adapter);
		/**
		 * 金币范围的点击事件
		 */
		sumRange.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				moneyType = AmountUtil.strToAmount(moneyType.unit,
						moneyType.unitName, adapter.getItem(arg2));
				closeActivity();
			}
		});
		sumOK.setEnabled(false);
		sumOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String finish = finishSum.getText().toString();
				String start1 = startSum.getText().toString();
				boolean isStart = false;
				boolean isFinis = false;
				if (!TextUtils.isEmpty(start1)) {
					isStart = true;
				}
				if (!TextUtils.isEmpty(finish)) {
					isFinis = true;
				}
				
				if (isStart && isFinis) {
					float number = 0 ;
					float number2 = 0 ;
					try {
						 number = Float.parseFloat(start1);
						 number2 = Float.parseFloat(finish);
					} catch (Exception e) {
						showToast("金额解析错误");
						return;
					}
					if (number > number2) {
						Toast.makeText(AmountOfMoneyActivity.this,
								"起始金额不能大于等于截止金额", 0).show();
						return;
					} else {
						moneyType.beginAmount = number;
						moneyType.endAmount = number2;
					}
				} else if (isStart) {
					float number = Float.parseFloat(start1);
					moneyType.beginAmount = number;
					moneyType.endAmount = -1;
				} else if (isFinis) {
					float number2 =Float.parseFloat(finish);
					moneyType.beginAmount = -1;
					moneyType.endAmount = number2;
				}
				closeActivity();
			}
		});

		/**
		 * 最小金额
		 */
		startSum.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.toString().contains(".")) {
					if (s.length() - 1 - s.toString().indexOf(".") > 1) {
						s = s.toString().subSequence(0,
								s.toString().indexOf(".") + 2);
						startSum.setText(s);
						startSum.setSelection(s.length());
					}
				}
				if (s.toString().trim().substring(0).equals(".")) {
					s = "0" + s;
					startSum.setText(s);
					startSum.setSelection(2);
				}

				if (s.toString().startsWith("0")
						&& s.toString().trim().length() > 1) {
					if (!s.toString().substring(1, 2).equals(".")) {
						startSum.setText(s.subSequence(0, 1));
						startSum.setSelection(1);
						return;
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String keyWordStr = startSum.getText().toString();
				if (keyWordStr.equals("") && !isFinis) {
					isStart = false;
					sumOK.setEnabled(false);
					sumOK.setBackgroundResource(R.drawable.hy_icon_meeting_detail_cancel_pressed);
				} else {
					isStart = true;
					sumOK.setEnabled(true);
					sumOK.setBackgroundResource(R.drawable.amount_money_select);
				}
			}
		});
		// 最大金额
		finishSum.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.toString().contains(".")) {
					if (s.length() - 1 - s.toString().indexOf(".") > 1) {
						s = s.toString().subSequence(0,
								s.toString().indexOf(".") + 2);
						finishSum.setText(s);
						finishSum.setSelection(s.length());
					}
				}
				if (s.toString().trim().substring(0).equals(".")) {
					s = "0" + s;
					finishSum.setText(s);
					finishSum.setSelection(2);
				}

				if (s.toString().startsWith("0")
						&& s.toString().trim().length() > 1) {
					if (!s.toString().substring(1, 2).equals(".")) {
						finishSum.setText(s.subSequence(0, 1));
						finishSum.setSelection(1);
						return;
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String keyWordStr = finishSum.getText().toString();
				if (keyWordStr.equals("") && !isStart) {
					isFinis = false;
					sumOK.setEnabled(false);
					sumOK.setBackgroundResource(R.drawable.hy_icon_meeting_detail_cancel_pressed);
				} else {
					isFinis = true;
					sumOK.setEnabled(true);
					sumOK.setBackgroundResource(R.drawable.amount_money_select);
				}

			}
		});
		moneyTypeLl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startCurrency();
			}
		});// 币种的点击事件
		moneyTypeTv.setText(moneyType.unit + "—" + moneyType.unitName);
	}

	/**
	 * 打开币种选择界面
	 */
	public void startCurrency() {
		startActivityForResult(new Intent(this, CurrencyActivity.class),
				ENavConsts.ActivityReqCode.REQUEST_MOENY_SELECT);
	}

	public void closeActivity() {
		Intent intent = new Intent();
		intent.putExtra(ENavConsts.DEMAND_CHOOSE_TYEP, moneyType);
		// intent.putExtra(ENavConsts.DEMAND_MONEY_DATA, str);
		AmountOfMoneyActivity.this.setResult(RESULT_OK, intent);
		AmountOfMoneyActivity.this.finish();
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
		titleTv.setText("金额");
	}

	class AmountMoneyAdapter extends BaseAdapter {

		private List<String> data;
		private Context context;

		public AmountMoneyAdapter(List<String> data, Context context) {
			super();
			this.data = data;
			this.context = context;
		}

		@Override
		public int getCount() {
			return data != null ? data.size() : 0;
		}

		@Override
		public String getItem(int arg0) {
			// TODO Auto-generated method stub
			return data.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup paren) {
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.demand_item, null);
				ViewHolder.get(convertView, R.id.rightLl).setVisibility(
						View.GONE);
			}
			TextView text = ViewHolder.get(convertView, R.id.nameTv);
			text.setText(data.get(position));
			return convertView;
		}

	}
}
