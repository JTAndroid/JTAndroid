package com.tr.ui.knowledge;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.tr.R;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.people.cread.utils.Utils;

/**
 * 扫一扫pc端新闻跳转到此页面
 * @author gintong
 *
 */
public class NeturlKnowledgeActivity extends JBaseActivity{

	private String netUrl;//扫一扫传过来的URL链接
	@Override
	public void initJabActionBar() {
		getActionBar().show();
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_neturlknowledge);
		WebView webView = (WebView) findViewById(R.id.show_netUrl);
		netUrl = getIntent().getStringExtra("netUrl");
		webView.loadUrl(netUrl);
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
//		String title=webView.getTitle();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.my_knowledge_activity_actions, menu);
		menu.findItem(R.id.action_type).setIcon(R.drawable.knowledge_save_action_bg);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_type: //保存为知识
			ENavigate.startCreateKnowledgeActivity(NeturlKnowledgeActivity.this,true,netUrl,true);
//			final PopupWindow popupWindow = new PopupWindow(NeturlKnowledgeActivity.this);
//			popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
//			popupWindow.setWidth(LayoutParams.WRAP_CONTENT);
//			popupWindow.setFocusable(true);
//			popupWindow.setOutsideTouchable(true);
//			ColorDrawable dw = new ColorDrawable(0000000);
//            popupWindow.setBackgroundDrawable(dw);
//			popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
//			View convertView = View.inflate(NeturlKnowledgeActivity.this, R.layout.main_menu_more_popupwindow, null);
//			LinearLayout home_scan = (LinearLayout) convertView.findViewById(R.id.home_scan);
//			convertView.findViewById(R.id.invite_friends).setVisibility(View.GONE);
//			convertView.findViewById(R.id.onekeyback).setVisibility(View.GONE);
//			convertView.findViewById(R.id.line_3).setVisibility(View.GONE);
//			convertView.findViewById(R.id.scan_Iv).setVisibility(View.GONE);
//			convertView.findViewById(R.id.invite_friends_Iv).setVisibility(View.GONE);
//			convertView.findViewById(R.id.onekeyback_Iv).setVisibility(View.GONE);
//			
//			TextView scan_Tv = (TextView) convertView.findViewById(R.id.scan_Tv);
//			scan_Tv.setText("保存为知识");
//			home_scan.setOnClickListener(new View.OnClickListener() {// 创建知识
//
//				@Override
//				public void onClick(View v) {
//					ENavigate.startCreateKnowledgeActivity(NeturlKnowledgeActivity.this,true,netUrl,true);
//					popupWindow.dismiss();
//				}
//			});
//			
//			popupWindow.setContentView(convertView);
//			popupWindow.showAsDropDown(getActionBar().getCustomView(),(int)Utils.convertDpToPixel(120),0);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
