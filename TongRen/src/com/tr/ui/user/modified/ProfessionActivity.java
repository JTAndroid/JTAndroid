package com.tr.ui.user.modified;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.tr.R;
import com.tr.api.CommonReqUtil;
import com.tr.model.home.PeopleIndustry;
import com.tr.model.home.PeopleIndustrys;
import com.tr.model.home.PeopleProfession;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.common.EConsts;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

public class ProfessionActivity extends JBaseActivity implements IBindData {

	private ListView lv_profession;

	private List<PeopleIndustry> data;
	private PeopleProfession profession;
	private ProfessionAdapter professionAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frame_choose_profession);

		showLoadingDialog();
		profession = new PeopleProfession();
		lv_profession = (ListView) findViewById(R.id.lv_profession);
		
		CommonReqUtil.doPeopleInterestIndustry(this, this, 0, 0, 0, null);
	}

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar(), "选择行业", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
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
				convertView = View.inflate(ProfessionActivity.this, R.layout.list_item_profession, null);
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
				profession.setFirstIndustryDirection(data.get(position).getName());
				profession.setFirstIndustryDirectionId(data.get(position).getId());
				ENavigate.startProfessionSecondActivityForResult(ProfessionActivity.this, 
						ENavConsts.ActivityReqCode.REQUEST_CODE_SETTING_INDUSTRY_SECOND_ACTIVITY, data.get(position).getId());
				
			}
		});
		
	}

	class ViewHolder {
		private TextView industryTv;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == ENavConsts.ActivityReqCode.REQUEST_CODE_SETTING_INDUSTRY_SECOND_ACTIVITY) {
			PeopleIndustry industry = (PeopleIndustry) data.getSerializableExtra(EConsts.Key.PROFESSION);
//			public static final int GetPeopleProfessionList = ReqBase + 19; // 获取职业列表
//			public static final String PeopleInterestIndustry = "code/peopleIndustrysList.json";
			profession.setSecondIndustryDirection(industry.getName());
			profession.setSecondIndustryDirectionId(industry.getId());
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putSerializable(EConsts.Key.INDUSTRYS, profession);
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
