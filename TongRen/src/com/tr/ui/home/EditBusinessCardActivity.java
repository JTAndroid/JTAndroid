package com.tr.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.tr.R;
import com.tr.navigate.ENavConsts;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.frg.EditBusinessCardFrag;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.people.model.PeopleDetails;

/**
 * @ClassName: EdiBusinessCardActivity
 * @Description: 编辑名片信息页面
 * @author cui
 * @date 2015-11-30 上午10:16:51
 * 
 */
public class EditBusinessCardActivity extends JBaseFragmentActivity {
	private PeopleDetails people_details;// 传递的对象
	private boolean is_self_bool;
	
	@Override
	public void initJabActionBar() {
		getBundle();
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "编辑个人资料", false, null, false, true);
		setContentView(R.layout.activity_edit_business_card);
//		jabGetActionBar().setDisplayHomeAsUpEnabled(true);;
//		ViewUtils.inject(this); // 注入view和事件..
		initView();
	}

	private void getBundle() {
		people_details = (PeopleDetails) this.getIntent().getSerializableExtra(
				ENavConsts.datas);
		is_self_bool = this.getIntent().getBooleanExtra(ENavConsts.IS_SELF_BOOL, false);
	}

	private void initView() {
		EditBusinessCardFrag businessCardFrag= new EditBusinessCardFrag();
		Bundle bundle = new Bundle();
		bundle.putSerializable(ENavConsts.datas, people_details);
		bundle.putBoolean(ENavConsts.IS_SELF_BOOL, is_self_bool);
		businessCardFrag.setArguments(bundle);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(R.id.fragment_EditBusinessCard, businessCardFrag).commitAllowingStateLoss();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_new_main, menu);
		menu.findItem(R.id.home_new_menu_search).setVisible(false);
		menu.findItem(R.id.home_new_menu_more).setVisible(false);
		menu.findItem(R.id.menu_save).setVisible(true);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_save:
			// TODO 保存动作
			EditBusinessCardFrag editBusinessCardFrag=(EditBusinessCardFrag) getSupportFragmentManager().getFragments().get(0);
			editBusinessCardFrag.onSave();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if(null!=intent){
			EditBusinessCardFrag editBusinessCardFrag=(EditBusinessCardFrag) getSupportFragmentManager().getFragments().get(0);
			editBusinessCardFrag.handerRequsetCode(requestCode, intent);
		}
		super.onActivityResult(requestCode, resultCode, intent);
	}

}
