package com.tr.ui.people.contactsdetails;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.R;

/**
 * 会面中的颜色(目前会面未显示)
 * @author John
 *
 */
public class ColorActivity extends Activity implements OnClickListener {

	private ImageView color_back;

	private TextView color_complete;

	private RadioButton noColorChecked, greenColorChecked, blueColorChecked,
			orangeColorChecked, redColorChecked;
	
	private String colorStr = "";
	
	private int colorId = 0;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_color);

		color_back = (ImageView) findViewById(R.id.color_back);
		color_back.setOnClickListener(this);

		color_complete = (TextView) findViewById(R.id.color_complete);
		color_complete.setOnClickListener(this);
		
		noColorChecked = (RadioButton) findViewById(R.id.noColorChecked);
		noColorChecked.setOnClickListener(this);
		
		greenColorChecked = (RadioButton) findViewById(R.id.greenColorChecked);
		greenColorChecked.setOnClickListener(this);
		
		blueColorChecked = (RadioButton) findViewById(R.id.blueColorChecked);
		blueColorChecked.setOnClickListener(this);
		
		orangeColorChecked = (RadioButton) findViewById(R.id.orangeColorChecked);
		orangeColorChecked.setOnClickListener(this);
		
		redColorChecked = (RadioButton) findViewById(R.id.redColorChecked);
		redColorChecked.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.color, menu);
		return true;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.color_back:

			finish();

			break;

		case R.id.color_complete:
			
			if(!colorStr.equals("") && colorId != 0 ){
				
				Intent intent = new Intent(ColorActivity.this,
						NewCreateActivity.class);

				intent.putExtra("COLOR", colorStr);
				
				intent.putExtra("COLORIMAGE",colorId);

				setResult(2, intent);

				finish();
				
			}else{
				
				Toast.makeText(this, "请选择颜色", Toast.LENGTH_SHORT)
				.show();
			}

			break;
			
		case R.id.noColorChecked:
			
			colorStr = "无色";

			colorId = R.drawable.people_nocolor;
			
			break;
			
			
		case R.id.greenColorChecked:

			colorStr = "绿色";

			colorId = R.drawable.people_green;
			
			break;
			
			
		case R.id.blueColorChecked:
			
			colorStr = "蓝色";

			colorId = R.drawable.people_blue;

			break;
			
			
		case R.id.orangeColorChecked:
			
			colorStr = "橙色";

			colorId = R.drawable.people_orange;

			break;
			
			
		case R.id.redColorChecked:
			
			colorStr = "红色";

			colorId = R.drawable.people_red;

			break;

		}

	}

}
