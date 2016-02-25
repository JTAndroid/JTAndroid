package com.tr.ui.share;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.R;
import com.tr.model.obj.KnowledgeMini;
import com.tr.ui.base.JBaseFragmentActivity;
import com.utils.common.EConsts;

public class ShareDetailActivity extends JBaseFragmentActivity {

	private final String TAG = getClass().getSimpleName();

	// 常量
	private final int MENU_ITEM_ID_BASE = 100;
	private final int MENU_ITEM_ID_SHARE = MENU_ITEM_ID_BASE + 1;

	// 控件
	private ScrollView contentSv;
	private WebView contentWv;
	private TextView contentTv;
	// 变量
	private KnowledgeMini mKnoMini;
	private WebSettings webSettings;

	@Override
	public void initJabActionBar() {
		jabGetActionBar().setTitle("    ");
		jabGetActionBar().setDisplayShowTitleEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// MenuItem overflowItem = menu.add(0, MENU_ITEM_ID_SHARE, 0, "分享");
		// overflowItem.setIcon(R.drawable.ic_action_share);
		// overflowItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == MENU_ITEM_ID_SHARE) { // 分享
			if (mKnoMini != null) {
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				if (mKnoMini.mTitle != null && mKnoMini.mTitle.contains("http")) {
					intent.putExtra(Intent.EXTRA_TEXT, mKnoMini.mTitle);
				} else {
					intent.putExtra(Intent.EXTRA_TEXT, mKnoMini.mTitle + " "
							+ mKnoMini.mUrl);
				}
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(Intent.createChooser(intent, "分享至"));
			}
		}
		if (item.getItemId() == android.R.id.home) {
//			if (contentWv != null) {
//				contentWv.reload();
//			}
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share_detail);
		initVars();
		initControls();
		showLoadingDialog();
	}

	private void initVars() {
		mKnoMini = (KnowledgeMini) getIntent().getSerializableExtra(
				EConsts.Key.KNOWLEDGE);
	}

	private void initControls() {
		contentSv = (ScrollView) findViewById(R.id.contentSv);
		contentTv = (TextView) findViewById(R.id.contentTv);
		contentWv = (WebView) findViewById(R.id.contentWv);
		contentWv.getSettings().setJavaScriptEnabled(true);
		webSettings = contentWv.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setAllowFileAccess(true);
		webSettings.setBuiltInZoomControls(false);// 设置支持缩放

		contentWv.getSettings().setJavaScriptEnabled(true);

		final Activity activity = this;
		contentWv.setWebViewClient(new WebViewClient() {
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(activity, "打开知识失败了", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				
			}
			
			
		});
		contentWv.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				activity.setProgress(progress * 1000);
			}
			
		});
		contentWv.loadUrl(mKnoMini.mUrl);
		

		if (mKnoMini != null) {
			if (mKnoMini.mUrl != null && mKnoMini.mUrl.length() > 0) {
				contentWv.loadUrl(mKnoMini.mUrl);
				contentSv.setVisibility(View.GONE);
			} else {
				contentWv.setVisibility(View.GONE);
				contentTv.setText(mKnoMini.mTitle);
			}
		}
		ShareDetailActivity.this.dismissLoadingDialog();
	}

	@Override
	public void onPause() {
		if (contentWv != null) {
			contentWv.reload();
		}
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		if (contentWv != null) {
			contentWv.reload();
		}
		super.onBackPressed();
	}
}
