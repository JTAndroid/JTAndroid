package com.tr.ui.user;

import android.os.Bundle;
import android.webkit.WebView;
import com.tr.R;
import com.tr.ui.base.JBaseFragmentActivity;

public class AgreementActivity extends JBaseFragmentActivity {

	private final String TAG = getClass().getSimpleName();
	
	private WebView agreementWv;
	
	@Override
	public void initJabActionBar() {
		jabGetActionBar().setTitle("金桐网服务协议");
		jabGetActionBar().setDisplayShowTitleEnabled(true);
	}

	@Override
	public void onCreate(Bundle savedInstaceState){
		super.onCreate(savedInstaceState);
		setContentView(R.layout.activity_agreement);
		agreementWv = (WebView) findViewById(R.id.agreementWv);
		agreementWv.getSettings().setJavaScriptEnabled(true); 
		agreementWv.loadUrl("file:///android_asset/agreement.html");
	}
}
