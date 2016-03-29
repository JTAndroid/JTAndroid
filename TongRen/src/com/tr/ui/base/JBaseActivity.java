package com.tr.ui.base;

import android.app.ActionBar;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.tr.App;
import com.tr.R;
import com.tr.ui.widgets.EProgressDialog;
import com.umeng.analytics.MobclickAgent;
import com.utils.http.EAPIConsts;
import com.utils.log.KeelLog;

/**
 * @Filename JBaseActivity.java
 * @Author xuxinjian
 * @Date 2014-3-13
 * @description
 */
public abstract class JBaseActivity extends Activity {

    public static final String TAG="EBaseActivity";
    protected EProgressDialog mProgressDialog;
    protected LayoutInflater inflater;  
    protected boolean isDestroy = false;
//	private int SCALESIZE=100;
//	private ImageLoader imageLoader;
    
    public Handler getHandler() {
		return mHandler;
	}

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDestroy = false;
        inflater = LayoutInflater.from(this);  
        initJActionBarImpl();
        // 添加到Activity堆栈
        App.getApp().addActivity(this);
    }
    
    //隐藏actionbar
    private void initJActionBarImpl(){
		// 设置ActionBar样式
		ActionBar actionbar = this.getActionBar();
		actionbar.setDisplayShowTitleEnabled(true);
		actionbar.setHomeButtonEnabled(false);
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setIcon(R.color.none);
		// 设置actionbar的背景图
		Drawable myDrawable = getResources().getDrawable(
				R.drawable.action_bar_bg);
		actionbar.setBackgroundDrawable(myDrawable); // 设置背景图片
		actionbar.setSplitBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_bg));
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    	initJabActionBar();
    }

	@Override
 	public boolean onOptionsItemSelected(MenuItem item) {
 		switch (item.getItemId()) {
 		case android.R.id.home:
 			finish();
 			break;
 		}
 		return super.onOptionsItemSelected(item);
 	}
    
    public LayoutInflater getLayoutInflater(){
    	return inflater;
    }
    
    //子类在该回调中实现actionbar相关修改和设置
    public abstract void initJabActionBar();
    
    public void jabHideActionBar(){
    	getActionBar().hide();
    }
    
    protected ActionBar jabGetActionBar(){
    	return getActionBar();
    }

    protected void showLoadingDialog() {
        showLoadingDialog("");
    }

    protected void showLoadingDialog(int resId) {
        showLoadingDialog(getString(resId));
    }

    protected void showLoadingDialog(final String message) {
        if (null==mProgressDialog) {
            mProgressDialog=new EProgressDialog(JBaseActivity.this);
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    KeelLog.d(TAG, "mProgressDialog.onCancel");
                    //JBaseActivity.this.finish();
                    if("".equals(message)){
                    	onLoadingDialogCancel();
                    }
                    else{
                    	onLoadingDialogCancel("555");
                    }
                }
            });
        }
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }
    /**
     * 取消loading对话框动作回调，如果想要处理，就继承该函数，默认关闭当前页面
     */
    public void onLoadingDialogCancel(){
    	//finish();
    }
    
    /**
     * 取消loading对话框动作回调,什么都不做
     * @param message
     */
    public void onLoadingDialogCancel(String message){
    	
    }
    
    /**
	 * 是否显示
	 * @return
	 */
	public boolean isLoadingDialogShowing() {
		if (mProgressDialog != null) {
			return mProgressDialog.isShowing();
		} 
		else {
			return false;
		}
	}
    
    // 加载框是否可以取消
    protected void showLoadingDialog(String message,boolean cancelable,DialogInterface.OnCancelListener cancelListener) {
        if (null==mProgressDialog) {
            mProgressDialog=new EProgressDialog(JBaseActivity.this);
            mProgressDialog.setCancelable(cancelable);
			if (cancelable) {
				mProgressDialog.setOnCancelListener(cancelListener);
			}
        }
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }
    
    protected void dismissLoadingDialog() {
    	try{
	        if (mProgressDialog!=null&&mProgressDialog.isShowing()) {
	            mProgressDialog.dismiss();
	        }
    	}catch(Exception e){
    		
    	}
    }
    
	public  void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
	
	public  void showLongToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}
	
	protected Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {  
            if(msg.what==EAPIConsts.handler.show_err){
            	
            }
        } 
	};
	
	public boolean hasDestroy(){
		return isDestroy;
	}
	
	@Override
    protected void onDestroy() {
		isDestroy = true;
        super.onDestroy();
    }
	
	
	@Override
	public void onPause(){
		super.onPause();
		MobclickAgent.onPause(this); // 友盟统计
		ActivityHolder.getInstance().pop(this); // 出栈
	}
	
	
}
