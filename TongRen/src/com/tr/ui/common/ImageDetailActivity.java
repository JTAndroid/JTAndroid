package com.tr.ui.common;

import java.io.File;

import uk.co.senab.photoview.PhotoViewAttacher;
import android.app.ActionBar;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.tr.R;
import com.tr.ui.base.JBaseFragmentActivity;
import com.utils.common.EConsts;

public class ImageDetailActivity extends JBaseFragmentActivity {

	private final String TAG = getClass().getSimpleName();
	
	// 控件
	private ImageView imageIv;
	// 变量
	private String mImageUrl = "";
	private int mTag = 0;
	
	@Override
	public void initJabActionBar() {
		ActionBar actionBar = jabGetActionBar();
		actionBar.setTitle("图片");
		actionBar.setDisplayShowTitleEnabled(true);
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_detail);
		initVars();
		initControls();
	}

	private void initVars(){
		if(getIntent().hasExtra(EConsts.Key.IMAGE_URL)){
			mImageUrl = getIntent().getStringExtra(EConsts.Key.IMAGE_URL);
			mTag = 0;
		}
		else if(getIntent().hasExtra(EConsts.Key.IMAGE_PATH)){
			mImageUrl = getIntent().getStringExtra(EConsts.Key.IMAGE_PATH);
			mTag = 1;
		}
	}
	
	private void initControls(){
		imageIv = (ImageView) findViewById(R.id.imageIv);
		String url = getIntent().getStringExtra(EConsts.Key.IMAGE_URL);
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();
		if(mTag == 0){
			ImageLoader.getInstance().displayImage(url, imageIv, options);
		}
		else{
			ImageLoader.getInstance().displayImage(Uri.fromFile(new File(mImageUrl)).getPath(), imageIv);
		}
		new PhotoViewAttacher(imageIv);
	}
}
