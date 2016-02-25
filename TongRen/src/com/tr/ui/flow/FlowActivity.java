package com.tr.ui.flow;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

import com.tr.App;
import com.tr.R;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.flow.frg.FrgFlow;
import com.tr.ui.widgets.viewpagerheaderscroll.tools.ScrollableFragmentListener;
import com.tr.ui.widgets.viewpagerheaderscroll.tools.ScrollableListener;

public class FlowActivity extends JBaseFragmentActivity implements ScrollableFragmentListener{

	private FrgFlow flow;
	private int count;
	private long firClick, secClick;

	private int type;
	private long userid;
	
	@Override
	public void initJabActionBar() {
		type = getIntent().getIntExtra(FrgFlow.FLOW_TYPE, FrgFlow.FLOW_PERSON);
		userid = getIntent().getLongExtra(FrgFlow.FLOW_USERID, 0);
		
		getActionBar().setDisplayShowCustomEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);
		getActionBar().setDisplayShowTitleEnabled(false);
		View mCustomView = View.inflate(this, R.layout.home_flow_actionbar_title, null);
		mCustomView.findViewById(R.id.image).setVisibility(View.GONE);
		TextView titleTv = (TextView) mCustomView.findViewById(R.id.actionBartitleTv);

		if(type == flow.FLOW_GT){
			titleTv.setText("推荐");
		}else{
			titleTv.setText("动态");
		}
		getActionBar().setCustomView(mCustomView, new ActionBar.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView.getLayoutParams();
		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK | Gravity.CENTER_HORIZONTAL;
		getActionBar().setCustomView(mCustomView, mP);
		mCustomView.setOnTouchListener(new onDoubleClick());
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_flow);
		
		flow = new FrgFlow();
		flow.type = type;
		flow.userId = userid;
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(R.id.fragment_conainer, flow, "FrgFlow");
		transaction.commitAllowingStateLoss();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_flow, menu);
		if(type != flow.FLOW_FRIEND){
			menu.findItem(R.id.createFlow).setVisible(false);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId()== R.id.createFlow) {
			ENavigate.startCreateFlowActivity(this, App.getUserID());
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		flow.onActivityResult(requestCode, resultCode, intent);
	}
	@Override
	public void onFragmentAttached(ScrollableListener fragment, int position) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFragmentDetached(ScrollableListener fragment, int position) {
		// TODO Auto-generated method stub
		
	}
	
	class onDoubleClick implements OnTouchListener{  
		  
	    @Override  
	    public boolean onTouch(View v, MotionEvent event) {  
	        if(MotionEvent.ACTION_DOWN == event.getAction()){  
	            count++;  
	            if(count == 1){  
	                firClick = System.currentTimeMillis();  
	                  
	            } else if (count == 2){  
	                secClick = System.currentTimeMillis();  
	                if(secClick - firClick < 1000){  
	                      flow.setSelection();
	                }  
	                count = 0;  
	                firClick = 0;  
	                secClick = 0;  
	                  
	            }  
	        }  
	        return true;  
	    }  
	      
	}
}
