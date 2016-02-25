package com.tr.ui.demand;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.tr.R;
import com.tr.api.DemandReqUtil;
import com.tr.model.demand.ImageItem;
import com.tr.model.obj.JTFile;
import com.tr.navigate.ENavConsts;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.demand.util.DemandUtil;
import com.utils.http.IBindData;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video.Thumbnails;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

/**
 * @ClassName: BrowesPhotoVideo.java
 * @author lxc
 * @Date 2015年3月20日 下午14:30:22
 * @Description: 介绍
 */
public class BrowesPhotoVideo extends JBaseActivity implements IBindData{

	private ViewPager browesPhotoVp;
	private ImageView browesVideoPlayIv;
	/**
	 * 存放图片地址集合
	 */
	private ArrayList<JTFile> selectedPicture = new ArrayList<JTFile>();

	
	private Bitmap extractMiniThumb;
	private Bitmap netBitmap;
	//加载本地视频
	private Handler handlerVideo = new Handler() {
		public void handleMessage(Message msg) {
			((ImageView) msg.obj).setImageBitmap(extractMiniThumb);
		};
	};
	//加载网络视频
	private Handler handlerNetVideo = new Handler() {
		public void handleMessage(Message msg) {
			((ImageView) msg.obj).setImageBitmap(netBitmap);
		};
	};

	
	private DisplayImageOptions options;
	private ImageLoader loader;

	// 点击的角标
	private int index = 1;
	private MyAdapter mAdapter;

	// 记录当前浏览的页数
	private MenuItem deleteMenu;// 删除图片效果
	private TextView title;
	private boolean isDelete = false;//

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getParams();
		setContentView(R.layout.demand_browes_photo_video);
		browesPhotoVp = (ViewPager) findViewById(R.id.browesPhotoVp);
		loader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
		// 设置图片在加载中显示的图片
				.showImageOnLoading(R.drawable.demand_defaultimg)
				// 设置图片Uri为空或是错误的时候显示的图片
				.showImageForEmptyUri(R.drawable.demand_defaultimg)
				// 设置图片加载/解码过程中错误时候显示的图片
				.showImageOnFail(R.drawable.demand_defaultimg)
				// 设置下载的图片是否缓存在内存中
//				 .cacheInMemory(true)
				// 设置下载的图片是否缓存在SD卡中
//				 .cacheOnDisk(true)
				// 保留Exif信息
//				 .considerExifParams(false)
				// 设置图片以如何的编码方式显示
				.imageScaleType(ImageScaleType.EXACTLY)
				// 设置图片的解码类型
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		initView();
	}

	@SuppressWarnings("unchecked")
	public void getParams() {
		Intent intent = getIntent();
		selectedPicture.clear();
		selectedPicture = (ArrayList<JTFile>) intent
				.getSerializableExtra(ENavConsts.DEMAND_INTENT_SELECTED_PICTURE);
		index = intent.getIntExtra("index", 1);
		isDelete = intent.getBooleanExtra(ENavConsts.DEMAND_BROWER_DELETE,
				false);

	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.demand_browes_menu, menu);
		deleteMenu = menu.getItem(0);
		deleteMenu.setVisible(false);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (isDelete && deleteMenu != null) {
			deleteMenu.setVisible(true);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void initJabActionBar() {
		ActionBar mActionBar = jabGetActionBar();
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(true);
		View mCustomView = getLayoutInflater().inflate(
				R.layout.demand_actionbar_title, null);
		mActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView
				.getLayoutParams();
		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK
				| Gravity.CENTER_HORIZONTAL;
		mActionBar.setCustomView(mCustomView, mP);
		mActionBar.setTitle(" ");
		title = (TextView) mCustomView.findViewById(R.id.titleTv);
		title.setText("图片");
		mCustomView.findViewById(R.id.titleIv).setVisibility(View.GONE);

	}

	@SuppressWarnings("unchecked")
	private void initView() {

		mAdapter = new MyAdapter();
		browesPhotoVp.setAdapter(mAdapter);

		// 设置开始浏览的的item是当前的角标
		browesPhotoVp.setCurrentItem(index);
		title.setText(index + 1 + "/" + selectedPicture.size());

		browesPhotoVp.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				index = arg0;
				title.setText(index + 1 + "/" + selectedPicture.size());
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: // 按下ActionBar上的返回键回到上一页
			if (isDelete) {
				Intent intent = new Intent();
				intent.putExtra(ENavConsts.DEMAND_INTENT_SELECTED_PICTURE,
						selectedPicture);
				setResult(RESULT_OK, intent);
			}
			this.finish();
			break;
		case R.id.browsDelete:
			// 删除
			JTFile imageItem = selectedPicture.get(index);
			if(selectedPicture !=null && selectedPicture.size() > 1){
				if(!(new File(imageItem.mLocalFilePath).isFile())){
					deleteFileById(imageItem.getId());
				}
				selectedPicture.remove(index);
				mAdapter.notifyDataSetChanged();
				index =browesPhotoVp.getCurrentItem();
				title.setText(index + 1 + "/" + selectedPicture.size());
			}else{
				if(selectedPicture.size() >0){
					if(!(new File(imageItem.mLocalFilePath).isFile())){//删除网络图片
						deleteFileById(imageItem.getId());
					}
					selectedPicture.clear();
					mAdapter.notifyDataSetChanged();
				}
				Intent intent = new Intent();
				intent.putExtra(ENavConsts.DEMAND_INTENT_SELECTED_PICTURE,
						selectedPicture);
				setResult(RESULT_OK, intent);
				finish();
			}

			break;
		}
		return true;
	}
	
	/**
	 * 
	 * @param id
	 */
	private void deleteFileById(String id){
		if(TextUtils.isEmpty(id)){
			return;
		}
		DemandReqUtil.deleteDemandFile(BrowesPhotoVideo.this, BrowesPhotoVideo.this, id, null);
	}

	public class MyAdapter extends PagerAdapter {

		private View view;
		

		@Override
		public int getCount() {
			return selectedPicture == null ? 0 : selectedPicture.size();
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return POSITION_NONE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@SuppressLint("NewApi")
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
//			// super.destroyItem(container, position, object);
//			String path = selectedPicture.get(position);
//			view = View.inflate(BrowesPhotoVideo.this,
//					R.layout.demand_browes_photo_video_item, null);
//
//			ImageView browesPhotoVideoIv = (ImageView) view
//					.findViewById(R.id.browesPhotoVideoIv);
//			ImageView browesVideoPlayIv = (ImageView) view
//					.findViewById(R.id.browesVideoPlayIv);
//
//			if (!(path.matches("^.*?\\.(3gp|mp4|rmvb|flv|avi)"))) {
//				loader.displayImage("file://" + path, browesPhotoVideoIv,
//						options);
//				/*
//				 * Bitmap bm = BitmapFactory.decodeFile(item.path);
//				 * holder.iv.setImageBitmap(bm);
//				 */
//				browesVideoPlayIv.setVisibility(View.GONE);
//
//			} else { // 获取到视频的第一帧显示到控件上去
//				MediaMetadataRetriever media = new MediaMetadataRetriever();
//				media.setDataSource(path);
//
//				Bitmap bitmap = media.getFrameAtTime();
//				browesPhotoVideoIv.setImageBitmap(bitmap);
//				browesVideoPlayIv.setVisibility(View.VISIBLE);
//			}

			  container.removeView((View) object);  
//			container.removeView(view);
		}

		@SuppressLint("NewApi")
		@Override
		public Object instantiateItem(ViewGroup container, final int position) {

			JTFile imageItem = selectedPicture.get(position);

			view = View.inflate(BrowesPhotoVideo.this,
					R.layout.demand_browes_photo_video_item, null);

			ImageView browesPhotoVideoIv = (ImageView) view
					.findViewById(R.id.browesPhotoVideoIv);
			browesVideoPlayIv = (ImageView) view
					.findViewById(R.id.browesVideoPlayIv);
			browesVideoPlayIv.setTag(position);
			if (!(imageItem.mType == JTFile.TYPE_FILE)) {
				if(!(new File(imageItem.mLocalFilePath).isFile())){
					loader.displayImage(imageItem.mLocalFilePath, browesPhotoVideoIv,
							options);
				}else{
					loader.displayImage("file://" + imageItem.mLocalFilePath, browesPhotoVideoIv,
							options);
				}
				/*
				 * Bitmap bm = BitmapFactory.decodeFile(item.path);
				 * holder.iv.setImageBitmap(bm);
				 */
				browesVideoPlayIv.setVisibility(View.GONE);
			} else { // 获取到视频的第一帧显示到控件上去
				if(!(new File(imageItem.mLocalFilePath).isFile())){
					//获取视频的第一帧
					videoNetThread(browesPhotoVideoIv, imageItem.mLocalFilePath,getWindowManager().getDefaultDisplay().getWidth(),getWindowManager().getDefaultDisplay().getHeight());
					
				}else{
					videoThread(browesPhotoVideoIv, imageItem.mLocalFilePath);
				}
				
				browesVideoPlayIv.setVisibility(View.VISIBLE);
				browesVideoPlayIv.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(!DemandUtil.isVideo(BrowesPhotoVideo.this)){
							showToast("已设置当前的网络不能播放视频");
							return;
						}
							int position = (Integer) v.getTag();
				           Intent video = new Intent(Intent.ACTION_VIEW);
				           if(!(new File(selectedPicture.get(position).mLocalFilePath).isFile())){
				        	   video.setDataAndType(Uri.parse(selectedPicture.get(position).mLocalFilePath), "video/*");  
				           }else{
				        	   video.setDataAndType(Uri.parse("file://" + selectedPicture.get(position).mLocalFilePath), "video/*");  
				           }
				           startActivity(video);  
					}
				});
			}
			container.addView(view);
			return view;
		}
		
		//本地视频的异步加载
		private void videoThread(final ImageView image, final String imagePath) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					MediaMetadataRetriever media = new MediaMetadataRetriever();
					media.setDataSource(imagePath);
					extractMiniThumb = media.getFrameAtTime();
					
					Message msg = handlerVideo.obtainMessage();
					msg.obj = image;
					handlerVideo.sendMessage(msg);
				}
			}).start();
		}
		
		//网络视频的异步加载
		private void videoNetThread(final ImageView image, final String imagePath,final int width,final int height) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					netBitmap = null;
					MediaMetadataRetriever retriever = new MediaMetadataRetriever();
					int kind = MediaStore.Video.Thumbnails.MINI_KIND;
					try {
						if (Build.VERSION.SDK_INT >= 14) {
							retriever.setDataSource(imagePath, new HashMap<String, String>());
						} else {
							retriever.setDataSource(imagePath);
						}
						netBitmap = retriever.getFrameAtTime();
					} catch (IllegalArgumentException ex) {
						// Assume this is a corrupt video file
					} catch (RuntimeException ex) {
						// Assume this is a corrupt video file.
					} finally {
						try {
							retriever.release();
						} catch (RuntimeException ex) {
							// Ignore failures while cleaning up.
						}
					}
					if (kind == Images.Thumbnails.MICRO_KIND && netBitmap != null) {
						netBitmap = ThumbnailUtils.extractThumbnail(netBitmap, netBitmap.getWidth()>width?width:netBitmap.getWidth(), netBitmap.getHeight()>height?netBitmap.getHeight():height,
								ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
					}
					Message msg = handlerNetVideo.obtainMessage();
					msg.obj = image;
					handlerNetVideo.sendMessage(msg);
					
				}
			}).start();
		}
		
	}

	@Override
	public void bindData(int tag, Object object) {
		// TODO Auto-generated method stub
		
	}
}
