package com.tr.ui.conference.initiatorhy;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.R;
import com.tr.model.conference.MPhotoItem;
import com.tr.ui.adapter.conference.GridviewPhotoAlbumBoothAdapter;
import com.tr.ui.conference.common.BaseActivity;
import com.utils.picture.AlbumHelper;
import com.utils.time.Util;

public class PhotoAlbumBoothActivity extends BaseActivity {
	private GridView boothGridView;
	private GridviewPhotoAlbumBoothAdapter boothGvAdp;
	private List<MPhotoItem> photoList;
	private AlbumHelper helper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hy_activity_photo_album_booth);
		init();
	}
	public void init() {
		findAndInitTitleViews();
		findAndInitGridViews();
	}
	private void findAndInitTitleViews(){
		LinearLayout backBtn = (LinearLayout)findViewById(R.id.hy_layoutTitle_backBtn);
		TextView title = (TextView)findViewById(R.id.hy_layoutTitle_title);
		TextView rightTextBtn = (TextView)findViewById(R.id.hy_layoutTitle_rightTextBtn);
		title.setText("相册");
		rightTextBtn.setText("确定");
		backBtn.setOnClickListener(new MyOnClickListener());
		rightTextBtn.setOnClickListener(new MyOnClickListener());
	}
	private void findAndInitGridViews(){
		Bundle b = getIntent().getExtras();
//		photoList = (List<MPhotoItem>)b.getSerializable(Util.IK_VALUE);
		int pos = b.getInt(Util.IK_VALUE);
		photoList = InitiatorDataCache.getInstance().albumBucketList.get(pos).photoList;
		if(Util.isNull(photoList)){
			Toast.makeText(this, "没有图片", Toast.LENGTH_SHORT).show();
//			finishActivity();
			return;
		}
//		helper = AlbumHelper.getHelper();
//		helper.init(getApplicationContext());
		
		boothGridView = (GridView) findViewById(R.id.hy_actPhotoAlbumBooth_gridView);
		boothGvAdp = new GridviewPhotoAlbumBoothAdapter(this, photoList);
		boothGridView.setAdapter(boothGvAdp);
		boothGridView.setOnItemClickListener(new MyOnItemClickListener());
	}
	public void finishActivity(){
		this.finish();
	}
	private class MyOnClickListener implements View.OnClickListener{
		@Override
		public void onClick(View v) {
			try {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				boolean isOpen = imm.isActive();
				if (isOpen) {
					imm.hideSoftInputFromWindow(PhotoAlbumBoothActivity.this.getCurrentFocus().getWindowToken(), 0);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			int clickId = v.getId();
			switch (clickId) {
				case R.id.hy_layoutTitle_backBtn:{
					finishActivity();
				}break;
				case R.id.hy_layoutTitle_rightTextBtn:{
					if(boothGvAdp.getSelPhotoList().size() > 0){
//						List<MPhotoItem> addPhotoList = IntroduceActivity.getInstance().getAddPhotoList();
						List<MPhotoItem> addPhotoList = new ArrayList<MPhotoItem>();
						addPhotoList.addAll(addPhotoList.size() - 1, boothGvAdp.getSelPhotoList());
					}
					Util.activitySetResult(PhotoAlbumBoothActivity.this, PhotoAlbumBucketActivity.class);
					finishActivity();
				}break;
			}
		}
	}
	private class MyOnItemClickListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
		}
	}
	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

}
