package com.tr.ui.common;

import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tr.R;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.utils.HomeCommonUtils;

public class ViewWebURLActivity extends JBaseFragmentActivity {
	private WebView mWebUrlShow;
	private android.app.ActionBar actionbar;

	@Override
	public void initJabActionBar() {
		actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_url);
		/* 初始化控件 */
		initializeControl();
		String webURlString= getIntent().getStringExtra("WEBURL");
		String titleString = getIntent().getStringExtra("WEBVIEWTITLE");

		// 设置WebView属性，能够执行Javascript脚本
		mWebUrlShow.getSettings().setJavaScriptEnabled(true);
		mWebUrlShow.getSettings().setSupportZoom(false);//支持缩放
		mWebUrlShow.getSettings().setDomStorageEnabled(true);//显示放大缩小
		// 加载需要显示的网页
		mWebUrlShow.loadUrl(webURlString);
		mWebUrlShow.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
			
			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				handler.proceed();
			}
		});
		if(mWebUrlShow != null && !TextUtils.isEmpty(mWebUrlShow.getTitle())){
			HomeCommonUtils.initLeftMainActionBar(ViewWebURLActivity.this, actionbar, mWebUrlShow.getTitle().toString() , false, null, R.color.registration_total_color);
		}else if(!TextUtils.isEmpty(titleString)){
			titleString = titleString.replace("同盟","桐盟");
			HomeCommonUtils.initLeftMainActionBar(ViewWebURLActivity.this, actionbar,titleString , false, null, R.color.registration_total_color);
		}else {
			HomeCommonUtils.initLeftMainActionBar(ViewWebURLActivity.this, actionbar,"桐人" , false, null, R.color.registration_total_color);
		}
	}

	/**
	 * 初始化控件
	 */
	public void initializeControl() {
		mWebUrlShow = (WebView) findViewById(R.id.webUrlShow);
	}
}
