package com.tr.ui.tongren.home;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Toast;

import com.tr.R;
import com.tr.api.TongRenReqUtils;
import com.tr.image.ImageLoader;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.tongren.model.project.Organization;
import com.tr.ui.tongren.model.project.OrganizationDetail;
import com.tr.ui.widgets.MessageDialog;
import com.tr.ui.widgets.MessageDialog.OnDialogFinishListener;
import com.tr.ui.widgets.title.menu.popupwindow.ActionItem;
import com.tr.ui.widgets.title.menu.popupwindow.TitlePopup;
import com.tr.ui.widgets.title.menu.popupwindow.TitlePopup.OnPopuItemOnClickListener;
import com.utils.common.EUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.http.EAPIConsts.TongRenRequestType.TongRenInfoType;
import com.utils.time.TimeUtil;

public class OrganizationActivity extends JBaseActivity implements IBindData, OnClickListener{

	private static final int ORGANIZATIONACCESSORY_REQUESTCODE = 1001;
	private static final int ORGANIZATION_REQUESTCODE = 1001;
	private ImageView orgIv;
	private TextView headerVi,orgName, orgCreater, orgCreateTime, orgInfo, orgType, hyType, orgAddress;
	private RelativeLayout myTaskRl, myResourceRl, myExamineRl, myKaoqinRl, myOrgRl;
	private TitlePopup titlePopup;
	private String actionItem = "";
	private Context mContext;
	private String oid;
	private boolean isMine;
	
	@Override
	public void initJabActionBar() {
		ActionBar jabGetActionBar = jabGetActionBar();
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar, "组织详情", false, null, true, true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.tongren_org_detail_activity);
		initView();
		initListener();
		
		oid = getIntent().getStringExtra("oid");
		getDataFromServer(EAPIConsts.TongRenRequestType.TONGREN_REQ_GETORGANIZATIONBYID);
		
		isMine = getIntent().getBooleanExtra("isMine", false);
		if(isMine){
			actionItem = "解散组织";
		}else{
			actionItem = "退出组织";
		}
		initPopWindow();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_more, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId()== R.id.more) {
			titlePopup.show(headerVi);
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void initPopWindow() {
		// 实例化标题栏弹窗
		titlePopup = new TitlePopup(mContext, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		titlePopup.setItemOnClickListener(onitemClick);
		// 给标题栏弹窗添加子类
		titlePopup.addAction(new ActionItem(mContext, actionItem));
	}
	
	private OnPopuItemOnClickListener onitemClick = new OnPopuItemOnClickListener() {
		@Override
		public void onItemClick(ActionItem item, int position) {
			if(item.mTitle.equals("解散组织")){
				MessageDialog messageDialog = new MessageDialog(OrganizationActivity.this);
				messageDialog.setContent("确定解散吗？");
				messageDialog.show();
				messageDialog.setOnDialogFinishListener(new OnDialogFinishListener(){
					@Override
					public void onFinish(String content) {
						getDataFromServer(EAPIConsts.TongRenRequestType.TONGREN_REQ_DISBAND);
					}

					@Override
					public void onCancel(String content) {
						// TODO Auto-generated method stub
						
					}
				});
			}else if(item.mTitle.equals("退出组织")){
				MessageDialog messageDialog = new MessageDialog(OrganizationActivity.this);
				messageDialog.setContent("确定退出吗？");
				messageDialog.show();
				messageDialog.setOnDialogFinishListener(new OnDialogFinishListener(){
					@Override
					public void onFinish(String content) {
						getDataFromServer(EAPIConsts.TongRenRequestType.TONGREN_REQ_EXIT);
					}

					@Override
					public void onCancel(String content) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		}
		
	};
	private OrganizationDetail orgdetail;
	
	private void initView(){
		headerVi = (TextView) findViewById(R.id.text_transparent_line);
		orgIv = (ImageView) findViewById(R.id.orgIv);
		orgName = (TextView) findViewById(R.id.orgName);
		orgCreater = (TextView) findViewById(R.id.orgCreater);
		orgCreateTime = (TextView) findViewById(R.id.orgCreateTime);
		orgInfo = (TextView) findViewById(R.id.orgInfo);
		orgType = (TextView) findViewById(R.id.orgType);
		hyType = (TextView) findViewById(R.id.hyType);
		orgAddress = (TextView) findViewById(R.id.orgAddress);
		myTaskRl = (RelativeLayout) findViewById(R.id.myTaskRl);
		myResourceRl = (RelativeLayout) findViewById(R.id.myResourceRl);
		myExamineRl = (RelativeLayout) findViewById(R.id.myExamineRl);
		myKaoqinRl = (RelativeLayout) findViewById(R.id.myKaoqinRl);
		myOrgRl = (RelativeLayout) findViewById(R.id.myOrgRl);
	}
	
	private void setData(OrganizationDetail orgdetail){
		ImageLoader.load(orgIv, orgdetail.getSuccess().getLogo(), R.drawable.ic_default_avatar);
		orgName.setText(orgdetail.getSuccess().getName());
		orgCreater.setText(orgdetail.getCreateName()+"");
		if (!TextUtils.isEmpty(orgdetail.getSuccess().getCreateTime())) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String time = sdf.format(new Date(Long.valueOf(orgdetail.getSuccess().getCreateTime())));
			orgCreateTime.setText("创建于"+TimeUtil.TimeFormat(time));
		}
		orgInfo.setText(orgdetail.getSuccess().getIntroduction());
		orgType.setText(listToString(orgdetail.getClassificationName()));
		hyType.setText(listToString(orgdetail.getIndustryName()));
		orgAddress.setText(listToString(orgdetail.getAreaName()));
	}
	
	private void initListener(){
		myTaskRl.setOnClickListener(this);
		myResourceRl.setOnClickListener(this);
		myExamineRl.setOnClickListener(this);
		myKaoqinRl.setOnClickListener(this);
		myOrgRl.setOnClickListener(this);
	}
	
	@Override
	public void bindData(int tag, Object object) {
		if(object==null){
			return;
		}
		boolean success = false;
		switch(tag){
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETORGANIZATIONBYID://组织详情
			orgdetail = (OrganizationDetail) object;
			setData(orgdetail);
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_DISBAND://解散组织
			success = (Boolean) object;
			if(success){
				Toast.makeText(OrganizationActivity.this, "解散组织成功", Toast.LENGTH_SHORT).show();
				setResult(RESULT_OK);
				finish();
			}else{
				Toast.makeText(OrganizationActivity.this, "解散组织失败", Toast.LENGTH_SHORT).show();
			}
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_EXIT://退出组织
			success = (Boolean) object;
			if(success){
				Toast.makeText(OrganizationActivity.this, "退出申请已发送", Toast.LENGTH_SHORT).show();
				setResult(RESULT_OK);
				finish();
			}else{
				Toast.makeText(OrganizationActivity.this, "退出申请发送失败", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch(v.getId()){
		case R.id.myTaskRl:
			Intent taskIntent = new Intent(this, ProjectTaskActivtiy.class);
			taskIntent.putExtra("organizationId",oid);
			taskIntent.putExtra(EAPIConsts.TongRenRequestType.TongRenInfoType, TongRenInfoType.MY);
			taskIntent.putExtra("projectName", "我的任务");
			startActivityForResult(taskIntent,ORGANIZATION_REQUESTCODE);
			break;
		case R.id.myResourceRl:
			Intent AccessoryIntent = new Intent(OrganizationActivity.this, ProjectAccessoryActivtiy.class);
			AccessoryIntent.putExtra("organizationId",oid);
			AccessoryIntent.putExtra(EAPIConsts.TongRenRequestType.TongRenInfoType, TongRenInfoType.MY);
			startActivityForResult(AccessoryIntent, ORGANIZATIONACCESSORY_REQUESTCODE);
			break;
		case R.id.myExamineRl:
			intent = new Intent(OrganizationActivity.this, OrganizationReviewActivity.class);
			intent.putExtra("oid", oid);
			startActivity(intent);
			break;
		case R.id.myKaoqinRl:
			intent = new Intent(OrganizationActivity.this, OrganizationRecordActivity.class);
			intent.putExtra("oid", oid);
			startActivity(intent);
			break;
		case R.id.myOrgRl:
			intent = new Intent(OrganizationActivity.this, OrganizationSumupActivity.class);
			intent.putExtra("oid", oid);
			intent.putExtra("organizationName", "组织-"+orgdetail.getSuccess().getName());
			startActivity(intent);
			break;
		}
	}
	
	public String listToString(List<String> stringList){
        if (stringList==null) {
            return "";
        }
        StringBuilder result=new StringBuilder();
        boolean flag=false;
        for (String string : stringList) {
            if (flag) {
                result.append(",");
            }else {
                flag=true;
            }
            result.append(string);
        }
        return result.toString();
    }

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case EAPIConsts.handler.show_err:
				Bundle bundle = (Bundle) msg.getData();
				String errCode = (String) bundle
						.getString(EAPIConsts.Header.ERRORCODE);
				String errMessage = (String) bundle
						.getString(EAPIConsts.Header.ERRORMESSAGE);
				// EUtil.showToast(errMessage + " errCode:" + errCode);
				if (!isMine) {
//					111111 操作错误，出现异常了
//					-1  已提交退出组织申请
//					100608 您还有未完成的项目任务 不能申请退出组织
					if(errCode.equals("111111")){
						showToast("操作错误，出现异常了");
					}else if(errCode.equals("-1")){
						showToast("已提交退出组织申请");
					}else if(errCode.equals("100608")){
						showToast("您还有未完成的项目任务 不能申请退出组织");
					}
				}else{
					showToast(errMessage);
				}
				break;
			}
		}
	};
	
	private void getDataFromServer(int requestType) {
		JSONObject jobj = new JSONObject();
		try {
			jobj.put("oid", oid);
			switch (requestType) {
			case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETORGANIZATIONBYID:
				break;
			case EAPIConsts.TongRenRequestType.TONGREN_REQ_DISBAND:
				break;
			case EAPIConsts.TongRenRequestType.TONGREN_REQ_EXIT:
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		TongRenReqUtils.doRequestOrg(this, this, jobj, handler, requestType);
	}
}
