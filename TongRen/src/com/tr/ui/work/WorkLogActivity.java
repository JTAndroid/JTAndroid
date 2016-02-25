package com.tr.ui.work;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.tr.R;
import com.tr.api.WorkReqUtil;
import com.tr.model.work.BUAffarList;
import com.tr.model.work.BUAffarLog;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.http.IBindData;
import com.utils.http.EAPIConsts.WorkReqType;
/**
 * 事务日志
 * 
 * 启动方法：	WorkReqType.AFFAIR_LOG_LIST_GET
 * 
 * @param Type
 * 			"类型      n：新建 e:编辑，o：完成，r：重新开启，q：退出 x：过期"
 * @param jtContact
 * 			BUAffarLog类型对象
 * 
 * @author Administrator
 *
 */
public class WorkLogActivity  extends JBaseActivity implements OnItemClickListener, IBindData{
	/**事务ID*/
	private long mAffarId=0;
	/**日志的适配器*/
	private WorkLogAdapter mAdapter;
	/**日志显示的listview*/
	private ListView ListViewLog;
	//事务log
	private List<BUAffarLog> mListData;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.work_log_activity);
		
		mAffarId=getIntent().getLongExtra("AffarId",0);
		Log.d("xmx","mAffarId:"+mAffarId);
		//如果对事务进行了操作，进行初始化控件和数据
		if (mAffarId>0)
		{
			initView();//初始化控件
			initData();//初始化数据
		}
	}


	public void initView() {
		ListViewLog = (ListView) findViewById(R.id.ListViewLog);
		ListViewLog.setDividerHeight(0);
		ListViewLog.setOnItemClickListener(this);
		
	}

	public void initData() {
		
		mAdapter= new WorkLogAdapter(WorkLogActivity.this, mListData, ListViewLog);
		ListViewLog.setAdapter(mAdapter);
		
		this.showLoadingDialog("", false, null);
		WorkReqUtil.getAffarLogById(WorkLogActivity.this,  this, mAffarId, null);

	}
	
	@Override
	public void bindData(int tag, Object object) {
		// TODO Auto-generated method stub
		dismissLoadingDialog();
		Log.d("xmx","bindData:"+tag);
		if (object!=null)
		{
			Log.d("xmx","bindData:"+object.toString());
			
		}
		switch (tag) {
			case WorkReqType.AFFAIR_LOG_LIST_GET:
			{
				//事务log
				mListData=(List<BUAffarLog>) object;
				mAdapter.setItemList(mListData);
				
			}
		}
	}

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar(), "日志", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

	
	
}
