package com.tr.ui.user.modified;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.R;
import com.tr.api.CommonReqUtil;
import com.tr.model.SimpleResult;
import com.tr.model.home.MCustomzation;
import com.tr.model.home.MIndustry;
import com.tr.model.home.MIndustrys;
import com.tr.model.home.MPageIndustrys;
import com.tr.navigate.ENavConsts;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.common.EConsts;
import com.utils.common.Util.DensityUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

public class ProfessionAndCustomzationActivity extends JBaseActivity implements IBindData {

	private MenuItem menuItem;

	private ListView lv_profession;

	private List<MIndustry> data;
	private MIndustrys results;
	private MCustomzation customzation;
	private ProfessionAdapter professionAdapter;
	// 0选择行业 1.定制
	private int type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frame_choose_profession);

		showLoadingDialog();
		results = (MIndustrys) getIntent().getSerializableExtra(ENavConsts.KEY_FRG_SETTING_MINDUSTRYS);
		if (results == null) {
			results = new MIndustrys();
			List<MIndustry> list = new ArrayList<MIndustry>();
			results.setListIndustry(list);
		}
		type = getIntent().getIntExtra(ENavConsts.EProfessionAndCustomzation, 0);

		lv_profession = (ListView) findViewById(R.id.lv_profession);
		
		CommonReqUtil.doGetInterestIndustry(this, this, 1, 10000, null);
	}

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar(), "选择行业", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menuItem = menu.add(0, 1105, 0, "完成");
		menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1105:
			// 0选择行业 1.定制
			if (type == 0) {
				// Toast.makeText(ChooseProfessionActivity.this, "Done",
				// 0).show();
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable(EConsts.Key.INDUSTRYS, results);
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
//				finish();
//				if (results.getListIndustry().size() == 0) {
//					Toast.makeText(ProfessionAndCustomzationActivity.this, "请至少选择一个行业", 0).show();
//					break;
//				}
				finish();
			}
			if (type == 1) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				if (results != null && results.getListIndustry().size() > 0) {

					customzation = new MCustomzation();
					List<String> ids = new ArrayList<String>();
					List<String> names = new ArrayList<String>();
					for (MIndustry data : results.getListIndustry()) {
						ids.add(data.getId());
						names.add(data.getName());
					}
					customzation.setListCareIndustryIds(ids);
					customzation.setListCareIndustryNames(names);
					showLoadingDialog();
					CommonReqUtil.doSetCustomMade(ProfessionAndCustomzationActivity.this, this, customzation, null);

//					bundle.putSerializable(EConsts.Key.INDUSTRYS, results);
//					intent.putExtras(bundle);
//					setResult(RESULT_OK, intent);
//					finish();
				}
				else {
					bundle.putSerializable(EConsts.Key.INDUSTRYS, results);
					intent.putExtras(bundle);
					setResult(RESULT_OK, intent);
					finish();
				}

			}
			break;
		}
		return super.onOptionsItemSelected(item);
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
				convertView = View.inflate(ProfessionAndCustomzationActivity.this, R.layout.list_item_profession, null);
				holder = new ViewHolder();
				holder.industryTv = (TextView) convertView.findViewById(R.id.industry_tv);
				holder.selectIndustryCb = (CheckBox) convertView.findViewById(R.id.select_industry_cb);
				holder.selectIndustryCb.setVisibility(View.VISIBLE);
				convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.industryTv.setText(data.get(position).getName());
			boolean flag = false;
			if (results != null) {
				for (int i = 0; i < results.getListIndustry().size(); i++) {
					if (data.get(position).getId().equals(results.getListIndustry().get(i).getId())||data.get(position).getName().equals(results.getListIndustry().get(i).getName())) {
						holder.selectIndustryCb.setChecked(true);
						flag = true;
					}
				}
				if(flag == false) {
					holder.selectIndustryCb.setChecked(false);
				}
			}
			return convertView;
		}

	}

	@Override
	public void bindData(int tag, Object object) {
		dismissLoadingDialog();
		if (tag == EAPIConsts.CommonReqType.GetInterestIndustry && object != null) {
			MPageIndustrys mProfessions = (MPageIndustrys) object;
			data = mProfessions.getPage().getListIndustry();
//			if(results == null || results.getListIndustry() == null || results.getListIndustry().size() == 0) {
//				for(int i=0; i<data.size();i++) {
//					list.add(data.get(i));
//				}
//				results.setListIndustry(list);
//			}
			init();
		}
		if (tag == EAPIConsts.CommonReqType.setCustomMade && object != null) {
			SimpleResult simpleResult = (SimpleResult) object;
			if (simpleResult.isSucceed()) {
				Toast.makeText(ProfessionAndCustomzationActivity.this, "设置成功", 0).show();
				Bundle bundle = new Bundle();
				Intent intent = new Intent();
				bundle.putSerializable(EConsts.Key.INDUSTRYS, results);
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				finish();
			}
			else {
				Toast.makeText(ProfessionAndCustomzationActivity.this, "没有设置成功", 0).show();
			}
		}
	}

	private void init() {
		professionAdapter = new ProfessionAdapter();
		lv_profession.setAdapter(professionAdapter);
		lv_profession.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				ViewHolder holder = (ViewHolder) view.getTag();
				CheckBox cb = holder.selectIndustryCb;
				if (cb.isChecked()) {
					cb.setChecked(false);
					for (int i = 0; i < results.getListIndustry().size(); i++) {
						if (data.get(position).getName().equals(results.getListIndustry().get(i).getName())||data.get(position).getId().equals(results.getListIndustry().get(i).getId())){

							results.getListIndustry().remove(i);
						}
					}
				}
				else {
					cb.setChecked(true);
					results.getListIndustry().add(data.get(position));
				}
				professionAdapter.notifyDataSetChanged();
			}
		});
		
	}

	class ViewHolder {
		private TextView industryTv;
		private CheckBox selectIndustryCb;
	}
	
}
