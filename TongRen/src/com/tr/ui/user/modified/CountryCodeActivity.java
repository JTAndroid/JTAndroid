package com.tr.ui.user.modified;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tr.R;
import com.tr.api.CommonReqUtil;
import com.tr.model.home.MCountry;
import com.tr.model.home.MListCountry;
import com.tr.model.home.McountryCode;
import com.tr.navigate.ENavConsts;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.widgets.TagSideBar;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/**
 * 选择区号
 * CountryCodeActivity
 */
public class CountryCodeActivity extends JBaseActivity implements IBindData {

	private Context context = CountryCodeActivity.this;

	@ViewInject(R.id.tagListLv)
	private ListView tagListLv;

	@ViewInject(R.id.hint_alpha_tv)
	private TextView hint_alpha_tv;

	@ViewInject(R.id.TagSideBar)
	private TagSideBar TagSideBar;

	private ArrayList<McountryCode> countrys;

	private String lastChar = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_countrys_code);
		ViewUtils.inject(this);
		showLoadingDialog();
		CommonReqUtil.doGetCountryCode(this, this, null);
	}

	@Override
	public void initJabActionBar() {
		jabGetActionBar().setTitle("选择国家和地区代码");
	}

	private class CountryAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return countrys.size();
		}

		@Override
		public Object getItem(int position) {
			return countrys.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(CountryCodeActivity.this, R.layout.activity_country_list, null);
				holder.country_tv = (TextView) convertView.findViewById(R.id.country_tv);
				holder.country_code_tv = (TextView) convertView.findViewById(R.id.country_code_tv);
				holder.index_ll = (LinearLayout) convertView.findViewById(R.id.index_ll);
				holder.index_tv = (TextView) convertView.findViewById(R.id.index_tv);
				convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.country_tv.setText(countrys.get(position).getCountry());
			holder.country_code_tv.setText(countrys.get(position).getCode());

			if (lastChar == null) {
				lastChar = countrys.get(position).getNameFirst();
				holder.index_ll.setVisibility(View.VISIBLE);
				holder.index_tv.setText(countrys.get(position).getNameFirst());
			}
			if (lastChar != null && lastChar.equals(countrys.get(position).getNameFirst())) {
				holder.index_ll.setVisibility(View.GONE);
			}
			if (lastChar != null && !lastChar.equals(countrys.get(position).getNameFirst())) {
				lastChar = countrys.get(position).getNameFirst();
				holder.index_ll.setVisibility(View.VISIBLE);
				holder.index_tv.setText(countrys.get(position).getNameFirst());
			}

			return convertView;
		}

	}

	private class ViewHolder {
		TextView country_tv;
		TextView country_code_tv;
		LinearLayout index_ll;
		TextView index_tv;
	}

	@Override
	public void bindData(int tag, Object object) {
		if (tag == EAPIConsts.CommonReqType.getCountryCode && object != null) {
			MListCountry listCountry = (MListCountry) object;
			countrys = new ArrayList<McountryCode>();
			McountryCode mcountryCode = null;
			for (MCountry country : listCountry.getListCountry()) {
				mcountryCode = new McountryCode(country);
				countrys.add(mcountryCode);
			}
			dismissLoadingDialog();
			TagSideBar.setListView(tagListLv, countrys);
			TagSideBar.setTextView(hint_alpha_tv);
			CountryAdapter adapter = new CountryAdapter();
			tagListLv.setAdapter(adapter);
			tagListLv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putSerializable(ENavConsts.ECountry, countrys.get(position));
					intent.putExtras(bundle);
					setResult(RESULT_OK, intent);
					finish();
				}
			});

		}
	}

}
