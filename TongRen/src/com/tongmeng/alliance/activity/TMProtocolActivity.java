package com.tongmeng.alliance.activity;

import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;

public class TMProtocolActivity extends JBaseActivity {

	private WebView webview;
	JavascriptInterface js;

	@Override
	public void initJabActionBar() {
		// TODO Auto-generated method stub
		setContentView(R.layout.register_protocol);
		initTitle();
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		webview = (WebView) findViewById(R.id.webview);
		WebSettings webSettings = webview.getSettings();
		// 设置WebView属性，能够执行Javascript脚本
		webSettings.setJavaScriptEnabled(true);
		// 设置可以访问文件
		webSettings.setAllowFileAccess(true);
		// 设置支持缩放
		// webSettings.setBuiltInZoomControls(true);
		// 加载需要显示的网页
		String path = "file:///android_asset/protocol.html";
		webview.loadUrl(path);
		// 设置Web视图
		webview.setWebViewClient(new webViewClient());
		// js = new JavascriptInterface() {
		//
		// @Override
		// public Class<? extends Annotation> annotationType() {
		// // TODO Auto-generated method stub
		// return null;
		// }
		// };
		// webview.addJavascriptInterface(js,"jsinterface");
		//
		// commint_htm=(Button)findViewById(R.id.commint_htm);
		// commint_htm.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// // String htmlname=getHtml();
		// }
		// });
	}

	@Override
	// 设置回退
	// 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
			webview.goBack(); // goBack()表示返回WebView的上一页面
			return true;
		}
		finish();// 结束退出程序
		return false;
	}

	// Web视图
	private class webViewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

	private void initTitle() {
		// TODO Auto-generated method stub
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "桐盟条款",
				false, null, false, true);
	}

}
