package com.tr.ui.conference.common;

import com.tr.ui.widgets.EProgressDialog;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * ui页面抽象类
 * 
 * @author d.c
 */

public abstract class BaseActivity extends Activity {

	protected final String TAG = getClass().getSimpleName();

	// 加载框
	protected EProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	/**
	 * 初始化View控件
	 */
	public abstract void initView();

	/**
	 * 舒适化数据
	 */
	public abstract void initData();

	/**
	 * 显示加载框
	 */
	public void showLoadingDialog() {
		if (null == progressDialog) {
			progressDialog = new EProgressDialog(this);
		}
		progressDialog.setMessage("");
		progressDialog.show();
	}

	/**
	 * 隐藏加载框
	 */
	public void dismissLoadingDialog() {
		try {
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
		}
	}

}