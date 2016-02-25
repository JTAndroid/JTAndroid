package com.tr.ui.conference.initiatorhy;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tr.R;
import com.tr.model.conference.MAlbumBucket;
import com.tr.ui.adapter.conference.GridviewPhotoAlbumBucketAdapter;
import com.tr.ui.conference.common.BaseActivity;
import com.utils.picture.AlbumHelper;
import com.utils.time.Util;

public class PhotoAlbumBucketActivity extends BaseActivity {
	private final static int requestCode_albumBooth = 1;
	private GridView albumGridView;
	private GridviewPhotoAlbumBucketAdapter albumGvAdp;
	private AlbumHelper helper;
	private List<MAlbumBucket> dataList;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hy_activity_photo_album_bucket);
		init();
	}

	public void init() {
		findAndInitTitleViews();
		if(Util.isNull(InitiatorDataCache.getInstance().albumBucketList)){
			helper = AlbumHelper.getHelper();
			helper.init(getApplicationContext());
			List<MAlbumBucket> bucketList = helper.getImagesBucketList(false);
			if(!Util.isNull(bucketList)){
				InitiatorDataCache.getInstance().albumBucketList.addAll(bucketList);
				helper.release();
			}
		}
		dataList = InitiatorDataCache.getInstance().albumBucketList;
		albumGridView = (GridView) findViewById(R.id.hy_actPhotoAlbumBucket_gridView);
		albumGvAdp = new GridviewPhotoAlbumBucketAdapter(this, dataList);
		albumGridView.setAdapter(albumGvAdp);
		albumGridView.setOnItemClickListener(new MyOnItemClickListener());
	}
	private void findAndInitTitleViews(){
		LinearLayout backBtn = (LinearLayout)findViewById(R.id.hy_layoutTitle_backBtn);
		TextView title = (TextView)findViewById(R.id.hy_layoutTitle_title);
		title.setText("相册");
		backBtn.setOnClickListener(new MyOnClickListener());
	}
	public void finishActivity(){
		this.finish();
	}
	private class MyOnItemClickListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Bundle b = new Bundle();
//			b.putSerializable(Util.IK_VALUE, (Serializable) dataList.get(position).photoList);
			b.putInt(Util.IK_VALUE, position);
//			b.putSerializable("activity", (Serializable) PhotoAlbumBucketActivity.this);
			Util.forwardTargetActivityForResult(PhotoAlbumBucketActivity.this, PhotoAlbumBoothActivity.class, 
					b, requestCode_albumBooth);
		}
		
	}
	private class MyOnClickListener implements View.OnClickListener{
		@Override
		public void onClick(View v) {
//			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//			boolean isOpen = imm.isActive();
//			if (isOpen) {
//				imm.hideSoftInputFromWindow(PhotoAlbumBucketActivity.this.getCurrentFocus().getWindowToken(), 0);
//			}
			int clickId = v.getId();
			switch (clickId) {
				case R.id.hy_layoutTitle_backBtn:{
					finishActivity();
				}break;
			}
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK){
			return;
		}
		switch (requestCode) {
			case requestCode_albumBooth:{
				finishActivity();
			}break;
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
