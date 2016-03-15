package com.tr.ui.user.modified;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.tr.R;
import com.tr.api.CommonReqUtil;
import com.tr.model.SimpleResult;
import com.tr.model.home.PeopleIndustry;
import com.tr.model.home.PeopleIndustrys;
import com.tr.navigate.ENavConsts;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.common.EConsts;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

public class ProfessionSecondActivity extends JBaseActivity implements IBindData{

	private ProfessionAdapter professionAdapter;
	private int pid;
	private ListView lv_profession;
	private List<PeopleIndustry> data;
	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar(), "", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.frame_choose_profession);

		showLoadingDialog();
		pid = getIntent().getIntExtra(ENavConsts.EProfessionAndCustomzationSecond, 0);

		lv_profession = (ListView) findViewById(R.id.lv_profession);
		
		CommonReqUtil.doPeopleInterestIndustry(this, this, pid, 0, 0, null);
		super.onCreate(savedInstanceState);
	}
	private class ProfessionAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = View.inflate(ProfessionSecondActivity.this, R.layout.list_item_profession, null);
				holder = new ViewHolder();
				holder.industryTv = (TextView) convertView.findViewById(R.id.industry_tv);
				convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.industryTv.setText(data.get(position).getName());
			return convertView;
		}

	}
	class ViewHolder {
		private TextView industryTv;
	}

	@Override
	public void bindData(int tag, Object object) {

		dismissLoadingDialog();
		if (tag == EAPIConsts.CommonReqType.GetPeopleProfessionList && object != null) {
			PeopleIndustrys mProfessions = (PeopleIndustrys) object;
			data = mProfessions.getIndustryDirections();
			init();
		}
	}
	
	private void init() {
		professionAdapter = new ProfessionAdapter();
		lv_profession.setAdapter(professionAdapter);
		lv_profession.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable(EConsts.Key.PROFESSION, data.get(position));
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		
	}

}
