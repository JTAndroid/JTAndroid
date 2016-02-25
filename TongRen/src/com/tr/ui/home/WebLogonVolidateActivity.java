package com.tr.ui.home;

import com.tr.R;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.http.IBindData;

/**
 * @Classname WebLogonVolidateActivity.java
 *
 * @author chewei
 *
 * @Description web登录验证界面
 *
 * @Parameter 
 */
public class WebLogonVolidateActivity extends JBaseFragmentActivity implements IBindData{

	@Override
	public void bindData(int tag, Object object) {
		// TODO Auto-generated method stub
	}

	@Override
	public void initJabActionBar() {
		// TODO Auto-generated method stub
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), this.getResources().getString(R.string.web_logon_volidate), false, null, false, true);
		setContentView(R.layout.web_validate_logon);
	}

}
