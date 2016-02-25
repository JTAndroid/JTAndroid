package com.tr.ui.tongren.home;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.tr.R;
import com.tr.api.TongRenReqUtils;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.tongren.adapter.OrganizationMemberAdapter;
import com.tr.ui.tongren.model.project.OrganizationMemberParameter;
import com.tr.ui.tongren.model.project.OrganizationRoleInfo;
import com.tr.ui.widgets.IMEditMumberGrid;
import com.tr.ui.widgets.IMEditMumberGrid.HeadName;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

public class OrganizationMemberActivity extends JBaseActivity implements IBindData {

	private IMEditMumberGrid organizationMumberGrid;
	private OrganizationMemberAdapter organizationMemberAdapter;
	private String organizationId;
	private OrganizationMemberType organizationMemberType;
	public ArrayList<HeadName> useSelectIdList = new ArrayList<HeadName>();
	private int status;
	private FromType fromType;
	private ActionBar jabGetActionBar;
	public enum OrganizationMemberType{
		select,
		show;
	}
	public enum FromType{
		project,
		organization;
	}
	@Override
	public void initJabActionBar() {
		jabGetActionBar = jabGetActionBar();
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar, "成员", false, null, true, true);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_organizationmember);
		initView();
		initData();
	}
	private void initData() {
		organizationId = getIntent().getStringExtra("organizationId");
		organizationMemberType = (OrganizationMemberType) getIntent().getSerializableExtra("organizationMemberType");
		fromType = (FromType) getIntent().getSerializableExtra("fromType");
		useSelectIdList = (ArrayList<HeadName>) getIntent().getSerializableExtra("useSelectIdList");
		if (fromType==FromType.organization) {
			status =3;
		}else if(fromType==FromType.project){
			status =1;
		}else{
			status =3;
		}
		if (organizationMemberType== OrganizationMemberType.select) {
			HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar, "选择指派人", false, null, true, true);
			organizationMumberGrid.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					useSelectIdList.clear();
					HeadName itemHeadName = (HeadName) organizationMemberAdapter.getItem(arg2);
					if (organizationMemberAdapter.getRemoveState(arg2)) {
						organizationMemberAdapter.setRemoveState(false,arg2);
						useSelectIdList.remove(itemHeadName);
						organizationMemberAdapter.notifyDataSetChanged();
					}else{
						organizationMemberAdapter.setRemoveState(true,arg2);
						useSelectIdList.add(itemHeadName);
						organizationMemberAdapter.notifyDataSetChanged();
						Intent intent = new Intent();
						Bundle bundle =  new Bundle();
						bundle.putSerializable("useSelectIdList", useSelectIdList);
						intent.putExtras(bundle);
						setResult(RESULT_OK, intent);
						finish();
					}
					
					
					
				}
			});
			
		}
		showLoadingDialog();
		OrganizationMemberParameter organizationMemberParameter = new OrganizationMemberParameter();
		organizationMemberParameter.oid = organizationId;
		organizationMemberParameter.status = status;
		TongRenReqUtils.doRequestWebAPI(this, this, organizationMemberParameter, null, EAPIConsts.TongRenRequestType.TONGREN_REQ_GETORGANIZATIONMEMBERINFO);
	}
	private void initView() {
		organizationMumberGrid = (IMEditMumberGrid) findViewById(R.id.organizationMumberGrid);
		organizationMemberAdapter = new OrganizationMemberAdapter(this);
		
		organizationMumberGrid.setAdapter(organizationMemberAdapter);
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		if (organizationMemberType== OrganizationMemberType.select) {
//			getMenuInflater().inflate(R.menu.menu_createflow, menu);
//			menu.findItem(R.id.flow_create).setTitle("完成");
//		}
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.flow_create:
			Intent intent = new Intent();
			Bundle bundle =  new Bundle();
			bundle.putSerializable("useSelectIdList", useSelectIdList);
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void bindData(int tag, final Object object) {
		if (object!=null) {
			switch (tag) {
			case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETORGANIZATIONMEMBERINFO:// 查看正式组织成员列表
				final ArrayList<HeadName> headNames = new ArrayList<HeadName>();
				final List<OrganizationRoleInfo> listOrganizationRoleInfo =  (List<OrganizationRoleInfo>) object;
				new AsyncTask<Void, Void, ArrayList<HeadName>>() {

					@Override
					protected ArrayList<HeadName> doInBackground(Void... params) {
						for (int k = 0; k < listOrganizationRoleInfo.size(); k++) {
							OrganizationRoleInfo organizationRoleInfo = listOrganizationRoleInfo.get(k);
							HeadName headName = new HeadName();
							headName.setImage(organizationRoleInfo.logo);
							headName.setUserID(organizationRoleInfo.userId);
							headName.setName(organizationRoleInfo.userName);
							if ("创建者".equals(organizationRoleInfo.roleName)) {
								headName.setCreater(true);
							}else if ("管理者".equals(organizationRoleInfo.roleName)) {
								headName.setIsFriend(true);
							}
							headNames.add(headName);
							if (useSelectIdList!=null&&!useSelectIdList.isEmpty()) {
								for (int i = 0; i < useSelectIdList.size(); i++) {
									HeadName name = useSelectIdList.get(i);
									if (name.getUserID().equals(organizationRoleInfo.userId)) {
										organizationMemberAdapter.setRemoveState(true, k);
									}
								}
							}
							
						}
						return headNames;
					}
					@Override
					protected void onPostExecute(ArrayList<HeadName> result) {
						organizationMemberAdapter.setData(headNames);
						organizationMemberAdapter.notifyDataSetChanged();
					};
				}.execute();
				
				break;

			default:
				break;
			}
		}
		dismissLoadingDialog();
	}
}
