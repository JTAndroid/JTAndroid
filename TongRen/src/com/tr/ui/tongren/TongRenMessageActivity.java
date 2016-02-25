package com.tr.ui.tongren;

import java.util.List;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.tr.R;
import com.tr.api.TongRenReqUtils;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.common.view.MyXListView;
import com.tr.ui.common.view.MyXListView.IXListViewListener;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.tongren.adapter.TongRenMessageAdapter;
import com.tr.ui.tongren.adapter.TongRenMessageAdapter.TongRenMessageOperateListener;
import com.tr.ui.tongren.model.message.MessageReqType;
import com.tr.ui.tongren.model.message.TongRenMessage;
import com.tr.ui.tongren.model.message.TongRenMessageOperate;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

public class TongRenMessageActivity extends JBaseActivity implements IBindData {

	private MyXListView tongrenMessageLv;
	private TongRenMessageAdapter tongRenMessageAdapter;
	private LinearLayout rootLl;
	@Override
	public void initJabActionBar() {
		ActionBar jabGetActionBar = jabGetActionBar();
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar, "消息", false, null, true, true);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tongrenmessage);
		initView();
		initData();
	}
	private void initData() {
		tongRenMessageAdapter = new TongRenMessageAdapter(this,new TongRenMessageOperateListener() {
			
			@Override
			public void doRefuse(View view, String messageReceiveID) {
				TongRenMessageOperate tongRenMessageOperateRefuse = new TongRenMessageOperate();
				tongRenMessageOperateRefuse.messageReceiveId = messageReceiveID;
				tongRenMessageOperateRefuse.status ="2";
				TongRenReqUtils.doRequestWebAPI(TongRenMessageActivity.this, TongRenMessageActivity.this, tongRenMessageOperateRefuse, null,EAPIConsts.TongRenRequestType.TONGREN_REQ_PROCESSING);
				view.setEnabled(false);
			}
			
			@Override
			public void doDelete(TongRenMessage tongRenMessage) {
				TongRenMessageOperate tongRenMessageOperateDelete = new TongRenMessageOperate();
				tongRenMessageOperateDelete.messageReceiveId =tongRenMessage. messageReceiveID;
				TongRenReqUtils.doRequestWebAPI(TongRenMessageActivity.this, TongRenMessageActivity.this, tongRenMessageOperateDelete, null,EAPIConsts.TongRenRequestType.TONGREN_REQ_DELMESSAGE);
				tongRenMessageAdapter.getListTongRenMessage().remove(tongRenMessage);
				tongRenMessageAdapter.notifyDataSetChanged();
			}
			
			@Override
			public void doAgree(View view, String messageReceiveID) {
				TongRenMessageOperate tongRenMessageOperateAgree = new TongRenMessageOperate();
				tongRenMessageOperateAgree.messageReceiveId = messageReceiveID;
				tongRenMessageOperateAgree.status ="1";
				TongRenReqUtils.doRequestWebAPI(TongRenMessageActivity.this, TongRenMessageActivity.this, tongRenMessageOperateAgree, null,EAPIConsts.TongRenRequestType.TONGREN_REQ_PROCESSING);
				view.setEnabled(false);
			}
		});
		tongrenMessageLv.setAdapter(tongRenMessageAdapter);
		tongrenMessageLv.setPullLoadEnable(false);
		tongrenMessageLv.setPullRefreshEnable(true);
		tongrenMessageLv.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				getData();
			}

			@Override
			public void onLoadMore() {
			}
		});
		getData();
	}
	private void getData() {
		showLoadingDialog();
		MessageReqType messageReqType = new MessageReqType();
		messageReqType.type = "0";
		TongRenReqUtils.doRequestWebAPI(this, this, messageReqType, null,EAPIConsts.TongRenRequestType.TONGREN_REQ_USERMESSAGE);
	}
	private void initView() {
		tongrenMessageLv = (MyXListView) findViewById(R.id.tongrenMessageLv);
		rootLl = (LinearLayout) findViewById(R.id.rootLl);
	}
	@Override
	public void bindData(int tag, Object object) {
		if (object!=null) {
			dismissLoadingDialog();
			rootLl.setBackgroundResource(R.color.project_bg);
			switch (tag) {
			case EAPIConsts.TongRenRequestType.TONGREN_REQ_USERMESSAGE:
				tongrenMessageLv.stopRefresh();
				List<TongRenMessage> tongRenMessagesList = (List<TongRenMessage>) object;
				tongRenMessageAdapter.setListTongRenMessage(tongRenMessagesList);
				tongRenMessageAdapter.notifyDataSetChanged();
				break;

			default:
				break;
			}
		}else{
			tongrenMessageLv.stopRefresh();
			if (tongRenMessageAdapter.getListTongRenMessage().isEmpty()) {
				rootLl.setBackgroundResource(R.drawable.empty);
			}
			dismissLoadingDialog();
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case android.R.id.home:
			setResult(TongRenFragment.REQ_ORG);
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			setResult(TongRenFragment.REQ_ORG);
			finish();
        }
        return true;
	}
}
