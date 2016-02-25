package com.tr.ui.knowledge;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.R;
import com.tr.model.obj.Connections;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.common.EConsts;
import com.utils.time.Util;

public class KnowledgePermissionActivity extends JBaseActivity {

	public static final String TAG = "KnowledgePermissionActivity";

	public static final int REQUEST_CODE_HIGHT_PERMISSION = 1001;
	public static final int REQUEST_CODE_MIDDLE_PERMISSION = 1002;
	public static final int REQUEST_CODE_LOW_PERMISSION = 1003;

	private CheckBox noPermissionSwitchCb;
	private CheckBox highPermissionRightArrowsCb;//大乐更改为与独乐一样 是个开关，大乐里是全平台2015/10/30
	private RelativeLayout lowPermissionRl;
	private TextView lowPermissionContentTv;
	private RelativeLayout middlePermissionRl;
	private TextView middlePermissionContentTv;
	private RelativeLayout highPermissionRl;
	private TextView highPermissionContentTv;
	private ImageView lowPermissionRightArrowsIv;
	private ImageView middlePermissionRightArrowsIv;
	@SuppressWarnings("unused")
	private ImageView highPermissionRightArrowsIv;//大乐更改为与独乐一样 是个开关，大乐里是全平台
	
	private TextView noPermissionTitleTv;//独乐
	private TextView lowPermissionTitleTv;//小乐
	private TextView middlePermissionTitleTv;//独乐
	private TextView highPermissionTitleTv;//独乐

	private Context context; 

	// 大乐权限人脉对象列表
	private ArrayList<Connections> listHightPermission = new ArrayList<Connections>();
	// 中乐权限人脉对象列表
	private ArrayList<Connections> listMiddlePermission = new ArrayList<Connections>();
	// 小乐权限人脉对象列表
	private ArrayList<Connections> listLowPermission = new ArrayList<Connections>();
   //知识进来默认的是大乐
	private boolean noPermission = true;//独乐 true为选中  默认为true；false为不选
	/*private boolean highPermission = false;//大乐 true为选中 默认为false 
*/
	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar(),"权限控制" ,false,null,false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_knowledge_permission);
		context = this;
		initData();
		initComponent();
	}

	void initData() {
		Intent intent = getIntent();
		ArrayList<Connections> listHightPermission1 = (ArrayList<Connections>) intent
				.getSerializableExtra("listHightPermission");
		if (listHightPermission1 != null && listHightPermission1.size() > 0) {
			listHightPermission = listHightPermission1;
			/*highPermission=true;*/
			
		}/*else
			highPermission=false;*/
		ArrayList<Connections> listMiddlePermission1 = (ArrayList<Connections>) intent
				.getSerializableExtra("listMiddlePermission");
		if (listMiddlePermission1 != null && listMiddlePermission1.size() > 0) {
			listMiddlePermission = listMiddlePermission1;
		}

		ArrayList<Connections> listLowPermission1 = (ArrayList<Connections>) intent
				.getSerializableExtra("listLowPermission");
		if (listLowPermission1 != null && listLowPermission1.size() > 0) {
			listLowPermission = listLowPermission1;
		}
	}

	private void initComponent() {
		noPermissionSwitchCb = (CheckBox) findViewById(R.id.noPermissionSwitchCb);
		noPermissionSwitchCb
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						setPermissionState(isChecked, 1);
					}
				});
		
		noPermissionTitleTv = (TextView) findViewById(R.id.noPermissionTitleTv);//独乐
		lowPermissionTitleTv = (TextView) findViewById(R.id.lowPermissionTitleTv);//小乐
		middlePermissionTitleTv = (TextView) findViewById(R.id.middlePermissionTitleTv);//独乐
		highPermissionTitleTv = (TextView) findViewById(R.id.highPermissionTitleTv);//大乐

		lowPermissionRl = (RelativeLayout) findViewById(R.id.lowPermissionRl);
		lowPermissionRl.setOnClickListener(mOnClickListener);
		lowPermissionContentTv = (TextView) findViewById(R.id.lowPermissionContentTv);
		lowPermissionRightArrowsIv = (ImageView) findViewById(R.id.lowPermissionRightArrowsIv);

		middlePermissionRl = (RelativeLayout) findViewById(R.id.middlePermissionRl);
		middlePermissionRl.setOnClickListener(mOnClickListener);
		middlePermissionContentTv = (TextView) findViewById(R.id.middlePermissionContentTv);
		middlePermissionRightArrowsIv = (ImageView) findViewById(R.id.middlePermissionRightArrowsIv);

		highPermissionRl = (RelativeLayout) findViewById(R.id.highPermissionRl);
		highPermissionRl.setOnClickListener(mOnClickListener);
		highPermissionContentTv = (TextView) findViewById(R.id.highPermissionContentTv);
		highPermissionRightArrowsCb = (CheckBox) findViewById(R.id.highPermissionRightArrowsCb);
		highPermissionRightArrowsIv = (ImageView) findViewById(R.id.highPermissionRightArrowsIv);
//		highPermissionRightArrowsIv.setVisibility(View.GONE);
//		highPermissionRightArrowsCb.setVisibility(View.VISIBLE);
		highPermissionRightArrowsCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				setPermissionState(isChecked, 4);
			}
		});
		if (listHightPermission.size() > 0 || listMiddlePermission.size() > 0
				|| listLowPermission.size() > 0) {
			noPermission = false;
			noPermissionSwitchCb.setChecked(noPermission);
		}else{
			noPermission = true;
			setPermissionState(noPermission, 1);
		}
		/*if (highPermission) {
			setPermissionState(highPermission, 4);
		}*/
		
		updateAllPermissionContentTv();

	}

	/**
	 * 独乐/大乐状态
	 * 
	 * @param noPermission
	 *            true为选中 反之为false
	 * @param type
	 *            1：独乐， 4：大乐
	 */
	private void setPermissionState(Boolean permission, int type) {
		switch (type) {
		case 1://独乐
			noPermission=permission;
			/*if(highPermission&&noPermission){
				highPermission=!permission;
			}*/
			if(noPermission){
				listHightPermission.clear();
				listLowPermission.clear();
				listMiddlePermission.clear();
			}

			updateAllPermissionContentTv();
			upDateCheckBox();
			break;
		case 4://大乐
			/*highPermission=permission;
			if(highPermission&&noPermission){
				noPermission=!permission;
			}*/
			/*if(highPermission)
				highPermissionContentTv.setText("全平台");
			else*/
				highPermissionContentTv.setText("（可见、可对接、可分享）");
			listLowPermission.clear();
			listMiddlePermission.clear();
			updateAllPermissionContentTv();
			upDateCheckBox();
			break;

		default:
			break;
		}
	}
	private void upDateCheckBox(){
		/*if(noPermission||highPermission){
			lowPermissionRightArrowsIv.setVisibility(View.GONE);
			middlePermissionRightArrowsIv.setVisibility(View.GONE);
		}else{
			lowPermissionRightArrowsIv.setVisibility(View.VISIBLE);
			middlePermissionRightArrowsIv.setVisibility(View.VISIBLE);
		}*/
		
		/*highPermissionRightArrowsCb.setChecked(highPermission);*/
		noPermissionSwitchCb.setChecked(noPermission);
		/*if(highPermission){
			noPermissionTitleTv.setTextColor(0xffaaaaaa);//独乐
			lowPermissionTitleTv.setTextColor(0xffaaaaaa);//小乐
			middlePermissionTitleTv.setTextColor(0xffaaaaaa);//独乐
			highPermissionTitleTv.setTextColor(0xff000000);//独乐
		}else*/ if(noPermission){
			noPermissionTitleTv.setTextColor(0xff000000);//独乐
			lowPermissionTitleTv.setTextColor(0xffaaaaaa);//小乐
			middlePermissionTitleTv.setTextColor(0xffaaaaaa);//独乐
			highPermissionTitleTv.setTextColor(0xffaaaaaa);//独乐
		}else if(!noPermission /*&& !highPermission*/){
			noPermissionTitleTv.setTextColor(0xffaaaaaa);//独乐
			lowPermissionTitleTv.setTextColor(0xff000000);//小乐
			middlePermissionTitleTv.setTextColor(0xff000000);//独乐
			highPermissionTitleTv.setTextColor(0xff000000);//独乐
		}
		
	}
	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String MSG = "onClick()";

			Intent intent = new Intent(KnowledgePermissionActivity.this,
					SelectConnectionsActivity.class);
			intent.putExtra(EConsts.Key.FROM_ACTIVITY,
					KnowledgePermissionActivity.class.getSimpleName());

			if (noPermission == true /*&&!highPermission*/
					&& (lowPermissionRl == v || middlePermissionRl == v || highPermissionRl == v)) {
				Toast.makeText(context, "关闭独乐，方可选择大、中、小乐哦", 0).show();
			} /*else if (!noPermission &&highPermission
				&& (lowPermissionRl == v || middlePermissionRl == v || highPermissionRl == v)) {
			Toast.makeText(context, "关闭大乐，方可选择独、中、小乐哦", 0).show();
		   }*/else if (lowPermissionRl == v) {
				intent.putExtra("listConnections", listLowPermission);
				startActivityForResult(intent, REQUEST_CODE_LOW_PERMISSION);
			} else if (middlePermissionRl == v) {
				intent.putExtra("listConnections", listMiddlePermission);
				startActivityForResult(intent, REQUEST_CODE_MIDDLE_PERMISSION);
			} else if (highPermissionRl == v) {
				intent.putExtra("listConnections", listHightPermission);
				startActivityForResult(intent, REQUEST_CODE_HIGHT_PERMISSION);
			}
		}
	};


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		// case R.id.create_ok:
		//
		//
		//
		// break;

		case android.R.id.home:
			finishBack();
			break;
		default:
			break;
		}

		return true;
	}

	@Override
	public void onBackPressed() {
		finishBack();
		// super.onBackPressed();
	}

	private void finishBack() {
		if(/*!highPermission&&*/!noPermission&&(listMiddlePermission == null||listMiddlePermission.size()==0)&&(listLowPermission == null||listLowPermission.size()==0)
				&& (listHightPermission == null || listHightPermission.size()==0)){
			Toast.makeText(KnowledgePermissionActivity.this, "请选择权限", Toast.LENGTH_SHORT).show();
		}else{
			Intent data = new Intent();
			/*if(highPermission){
				listHightPermission.clear();
				Connections connections = new Connections();
				connections.type = Connections.type_org;
				connections.setID(String.valueOf(-1));
				connections.setName("全平台");
				connections.setmSourceFrom("全金桐网");
				listHightPermission.add(connections);
				Util.removeDuplicatesConnections(listHightPermission,
						listLowPermission, listMiddlePermission);
			}else{
				listHightPermission = new ArrayList<Connections>();
			}*/
			if (listHightPermission == null) {
				listHightPermission = new ArrayList<Connections>();
			}
			if (listMiddlePermission == null) {
				listMiddlePermission = new ArrayList<Connections>();
			}
			if (listLowPermission == null) {
				listLowPermission = new ArrayList<Connections>();
			}
			if (noPermission
					|| (listHightPermission.size() == 0
							& listMiddlePermission.size() == 0 & listLowPermission
							.size() == 0)) {
				data.putExtra("noPermission", noPermission);
			} else {
				data.putExtra("listHightPermission", listHightPermission);
				data.putExtra("listMiddlePermission", listMiddlePermission);
				data.putExtra("listLowPermission", listLowPermission);
			}
			setResult(Activity.RESULT_OK, data);
			finish();
		}
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 小乐
		if (REQUEST_CODE_LOW_PERMISSION == requestCode) {
			if (resultCode == Activity.RESULT_OK) {
				listLowPermission = (ArrayList<Connections>) data
						.getSerializableExtra("listConnections");
				Util.removeDuplicatesConnections(listLowPermission,
						listMiddlePermission, listHightPermission);
				updateAllPermissionContentTv();
			}
		}

		// 中乐
		if (REQUEST_CODE_MIDDLE_PERMISSION == requestCode) {
			if (resultCode == Activity.RESULT_OK) {
				listMiddlePermission = (ArrayList<Connections>) data
						.getSerializableExtra("listConnections");
				Util.removeDuplicatesConnections(listMiddlePermission,
						listLowPermission, listHightPermission);
				updateAllPermissionContentTv();
			}
		}

		// 大乐
		if (REQUEST_CODE_HIGHT_PERMISSION == requestCode) {
			if (resultCode == Activity.RESULT_OK) {
				listHightPermission = (ArrayList<Connections>) data
						.getSerializableExtra("listConnections");
				Util.removeDuplicatesConnections(listHightPermission,
						listLowPermission, listMiddlePermission);
				updateAllPermissionContentTv();
			}
		}

	}

	void updateAllPermissionContentTv() {
		if (listLowPermission.size() > 0) {
			lowPermissionContentTv.setText(CreateKnowledgeActivity
					.listPermission2Str(listLowPermission));
		} else if (listLowPermission.size() <= 0) {
			lowPermissionContentTv.setText("（不可见、可对接、不可分享）");
		}

		if (listMiddlePermission.size() > 0) {
			middlePermissionContentTv.setText(CreateKnowledgeActivity
					.listPermission2Str(listMiddlePermission));
		} else if (listMiddlePermission.size() <= 0) {
			middlePermissionContentTv.setText("（可见、可对接、不可分享）");
		}

		if (listHightPermission.size() > 0/*|highPermission*/) {
			highPermissionContentTv.setText(/*"全平台"*/CreateKnowledgeActivity
					.listPermission2Str(listHightPermission));
		} else if (listHightPermission.size() <= 0) {
			highPermissionContentTv.setText("（可见、可对接、可分享）");
		}
	}

}
