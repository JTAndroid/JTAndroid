package com.tr.ui.relationship;

import java.util.ArrayList;

import com.tr.R;
import com.tr.model.SimpleResult;
import com.tr.model.im.MGetListIMRecord;
import com.tr.model.obj.IMRecord;
import com.tr.model.page.IPageBaseItem;
import com.tr.model.page.JTPage;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.conference.home.MeetingMasterActivity;
import com.tr.ui.home.MainActivity;
import com.tr.ui.home.frg.FrgConnections2;
import com.tr.ui.widgets.EProgressDialog;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.log.KeelLog;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 *  我的好友和人脉(这个类没有用到)
 *  
 * @author gushi 
 * */
public class MyFriendAndPeopleListActivity extends JBaseFragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_friend_and_people_list);
		initJabActionBar();
		
	}

	
	public void initJabActionBar() {
		// 设置ActionBar样式
		android.app.ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(false);
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setTitle("(好友) 人脉");

		// 设置actionbar的背景图
		Drawable myDrawable = getResources().getDrawable(R.drawable.auth_title_back);
		actionbar.setBackgroundDrawable(myDrawable); // 设置背景图片
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
	}
	
}
