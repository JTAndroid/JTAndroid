package com.tr.ui.conference.initiatorhy;

import java.io.File;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.R;
import com.tr.api.ConferenceReqUtil;
import com.tr.model.SimpleResult;
import com.tr.model.conference.MPhotoItem;
import com.tr.ui.adapter.conference.GridviewAddImageAdapter;
import com.tr.ui.conference.common.BaseActivity;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.picture.BitmapCache;
import com.utils.picture.BitmapCache.ImageCallback;
import com.utils.time.Util;

public class PhotoBrowseActivity extends BaseActivity implements IBindData {

	private ViewPager viewPager;
	private List<MPhotoItem> photoList;
//	private ArrayList<View> listViews = null;
	private int pagePos;
	private BitmapCache bmCache;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hy_activity_browse_photo);
		init();
	}

	private void init() {
		Bundle b = getIntent().getExtras();
		photoList = GridviewAddImageAdapter.getInstance().getPhotoList();
		if (Util.isNull(photoList) || photoList.size() < 2) {
			Toast.makeText(this, "没有图片", Toast.LENGTH_SHORT).show();
			return;
		}
		bmCache = new BitmapCache();
		viewPager = (ViewPager) findViewById(R.id.hy_actBroPhoto_viewPager);
		viewPager.setOnPageChangeListener(pageChangeListener);
		viewPager.setAdapter(new SamplePagerAdapter());// 设置适配器
		int pos = b.getInt("pos", 0);
		viewPager.setCurrentItem(pos);

		Button exitBtn = (Button) findViewById(R.id.hy_actBroPhoto_exit);
		Button delBtn = (Button) findViewById(R.id.hy_actBroPhoto_del);
		Button okBtn = (Button) findViewById(R.id.hy_actBroPhoto_ok);
		exitBtn.setOnClickListener(new MyOnClickListener());
//		delBtn.setOnClickListener(new MyOnClickListener());
		okBtn.setOnClickListener(new MyOnClickListener());
	}

	private void initListViews(Bitmap bm) {
//		if (listViews == null) {
//			listViews = new ArrayList<View>();
//		}
//		ImageView img = new ImageView(this);// 构造textView对象
//		img.setBackgroundColor(0xff000000);
//		img.setImageBitmap(bm);
//		img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//		listViews.add(img);// 添加view
	}

	private void initListViews(MPhotoItem pi) {
//		if (listViews == null) {
//			listViews = new ArrayList<View>();
//		}
//		ImageView img = new ImageView(this);// 构造textView对象
//		img.setBackgroundColor(0xff000000);
//		Bitmap bm = null;
//		try {
//			bm = BitmapFactory.decodeFile(pi.path);
//
//			if (bm == null) {
//				bm = BitmapFactory.decodeFile(pi.thumbnailPath);
//			}
//		} catch (Exception e) {
//		}
//		if (bm != null) {
//			img.setImageBitmap(bm);
//		}
//		img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//		listViews.add(img);// 添加view
	}

	public static boolean delFile(String path) {
		try {
			File file = new File(path);
			if (file.isFile() && file.exists()) {
				return file.delete();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		public void onPageSelected(int arg0) {// 页面选择响应函数
			pagePos = arg0;
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {// 滑动中。。。

		}

		public void onPageScrollStateChanged(int arg0) {// 滑动状态改变

		}
	};

	private class SamplePagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			if (Util.isNull(photoList)) {
				return 0;
			} else {
				return photoList.size() - 1;
			}
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			View v = LayoutInflater.from(PhotoBrowseActivity.this).inflate(
					R.layout.hy_item_show_image, null);
			ImageView image = (ImageView) v.findViewById(R.id.hy_item_show_image);
			if(photoList.get(position).isAlterMeeting){
			    image.setImageResource(R.drawable.common_loading01);
                if(!Util.isNull(photoList.get(position).alterMeetingPic) 
                        && !Util.isNull(photoList.get(position).alterMeetingPic.getPicPath()) 
                        && ImageLoader.getInstance() != null){
                    ImageLoader.getInstance().displayImage(photoList.get(position).alterMeetingPic.getPicPath(), image);
                }
            }else{
                bmCache.displayBmp(PhotoBrowseActivity.this, 
                        image, photoList.get(position).thumbnailPath, photoList.get(position).path, callback);
            }
			container.addView(v);
			return v;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}
	private ImageCallback callback = new ImageCallback() {
		@Override
		public void imageLoad(ImageView imageView, Bitmap bitmap,
				Object... params) {
			if (imageView != null && bitmap != null) {
				String url = (String) params[0];
				String tag = (String) imageView.getTag();
				((ImageView) imageView).setImageBitmap(bitmap);
//				if (url != null && url.equals(tag)) {
					((ImageView) imageView).setImageBitmap(bitmap);
//				} else {
//				}
			} else {
			}
		}
	};
	private class MyOnClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
//			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//			boolean isOpen = imm.isActive();
//			if (isOpen) {
//				imm.hideSoftInputFromWindow(PhotoBrowseActivity.this.getCurrentFocus().getWindowToken(), 0);
//			}
			int clickId = v.getId();
			switch (clickId) {
			case R.id.hy_actBroPhoto_exit: {
				if (photoList.size() == 2) {
					if(null!=photoList.get(0))
					{
						MPhotoItem item = photoList.get(0);
						if(null!=item.alterMeetingPic){
							if(null!=item.alterMeetingPic.getId()){
								long id = item.alterMeetingPic.getId().longValue();
								ConferenceReqUtil.doDeletePhoto(PhotoBrowseActivity.this, PhotoBrowseActivity.this, String.valueOf(id), null);
							}
						}
					}
					photoList.remove(0);
					
					finish();
				} else {
					if(null!=photoList.get(pagePos))
					{
						MPhotoItem item = photoList.get(pagePos);
						if(null!=item.alterMeetingPic){
							if(null!=item.alterMeetingPic.getId()){
								long id = item.alterMeetingPic.getId().longValue();
								ConferenceReqUtil.doDeletePhoto(PhotoBrowseActivity.this, PhotoBrowseActivity.this, String.valueOf(id), null);
							}
						}
					}
					photoList.remove(pagePos);
					viewPager.setAdapter(new SamplePagerAdapter());
					if (pagePos <= 0) {
						viewPager.setCurrentItem(0);
					} else {
						viewPager.setCurrentItem((--pagePos));
					}
					InitiatorDataCache.getInstance().introduce.photoList = photoList;
				}
			}
				break;
			case R.id.hy_actBroPhoto_del: {
//				if(!delFile(photoList.get(pagePos).path)){
//					 delFile(photoList.get(pagePos).thumbnailPath);
//				}
				if (photoList.size() == 2) {
					photoList.remove(0);
					finish();
				} else {
					photoList.remove(pagePos);
					viewPager.setAdapter(new SamplePagerAdapter());
					if (pagePos >= photoList.size() - 2) {
						viewPager.setCurrentItem(0);
					} else {
						viewPager.setCurrentItem(pagePos);
					}
				}
				
			}
				break;
			case R.id.hy_actBroPhoto_ok: {
				finish();
			}
				break;
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// // TODO Auto-generated method stub
	// if (keyCode==KeyEvent.KEYCODE_BACK) {
	// return super.onKeyDown(keyCode, event);
	// }
	// }

	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void bindData(int tag, Object object) {
		if (tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_DELETE_PHOTO) {		
			SimpleResult flag = (SimpleResult) object;
			if (null != flag) {
				if (flag.isSucceed()) {
					Toast.makeText(this, "图片删除成功", 0).show();
				}else{
					Toast.makeText(this, "图片删除失败", 0).show();
				}
			}
		}
		
	}
}
