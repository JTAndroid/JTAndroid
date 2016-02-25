package com.tr.ui.people.cread;

import com.tr.R;
import com.tr.ui.people.cread.utils.MakeListView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
/**
 * 民族
 * @author Wxh07151732
 *
 */
public class NationActivity extends BaseActivity{
	private ListView nation_Lv;
	private String[] nation ={"汉族","壮族","满族","回族","苗族","维吾尔族","土家族","彝族","蒙古族","藏族","布依族","侗族","瑶族","朝鲜族","白族","哈尼族","哈萨克族","黎族","傣族","畲族","傈僳族","仡佬族","东乡族","高山族","拉祜族","水族","佤族","纳西族","羌族","土族","仫佬族","锡伯族","柯尔克孜族","达斡尔族","景颇族","毛南族","撒拉族","布朗族","塔吉克族","阿昌族","普米族","鄂温克族","怒族","京族","基诺族","德昂族","保安族","俄罗斯族","裕固族","乌孜别克族","门巴族","鄂伦春族","独龙族","塔塔尔族","赫哲族","珞巴族"};
	private String text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_nation);
		init();
		initData();
	}

	private void initData() {
//		String makelistviewAdapter = MakeListView.makelistviewAdapter(this, nation_Lv, nation);
//		Intent intent = new Intent(Nation_Activity.this, Personal_information_Activity.class);
//		intent.putExtra("nation", makelistviewAdapter);
//		setResult(2, intent);
//		finish();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}
	private void init() {
		nation_Lv = (ListView) findViewById(R.id.nation_Lv);
	}
}
