package com.tr.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tr.R;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.im.IMCreateGroupCategoryActivity;
import com.tr.ui.im.IMRelationSelectActivity;

/**
 * @ClassName:     PersonalizedCustomActivity.java
 * @Description:   个性定制
 * @Author         leon
 * @Version        v 1.0  
 * @Created        2014-04-11
 * @LastEdit       2014-04-11
 */
public class PersonalizedActivity extends JBaseFragmentActivity {

	@Override
	public void initJabActionBar() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.persionalized);
		 this.findViewById(R.id.outdo).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intet=new Intent(PersonalizedActivity.this,IMCreateGroupCategoryActivity.class);
				PersonalizedActivity.this.startActivity(intet);
			}
		});
		 this.findViewById(R.id.indo).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intet=new Intent(PersonalizedActivity.this,IMCreateGroupCategoryActivity.class);
					PersonalizedActivity.this.startActivity(intet);
				}
			});
	}

}
