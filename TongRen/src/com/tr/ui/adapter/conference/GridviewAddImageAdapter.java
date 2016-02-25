package com.tr.ui.adapter.conference;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.R;
import com.tr.model.conference.MPhotoItem;
import com.utils.picture.BitmapCache;
import com.utils.picture.BitmapCache.ImageCallback;
import com.utils.time.Util;

public class GridviewAddImageAdapter extends BaseAdapter {
	private final String TAG = getClass().getSimpleName();
	private Context context;
//	private int type;
	private BitmapCache bmCache;
	private List<MPhotoItem> photoList = new ArrayList<MPhotoItem>();
	private int SCALESIZE = 100;
	private ImageLoader imageLoader = ImageLoader.getInstance();
			//InitiatorDataCache.getInstance().introduce.photoList;//= new ArrayList<MPhotoItem>();
	private static GridviewAddImageAdapter instance;
	
	public GridviewAddImageAdapter(Context context){
		instance = this;
//		initImageLoader(context);
		this.context = context;
		MPhotoItem ap = new MPhotoItem();
		photoList.add(ap);
		bmCache = new BitmapCache();
	}
	/* public void initImageLoader(Context context) {
			// 缓存文件的目录
			File cacheDir = StorageUtils.getOwnCacheDirectory(context, "/gintong/image/");
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
					context)
					.memoryCacheExtraOptions(Utils.dipToPx(context, SCALESIZE  ),
							Utils.dipToPx(context, SCALESIZE ))
					// max width, max height，即保存的每个缓存文件的最大长宽
					.discCacheExtraOptions(240, 400, CompressFormat.PNG, 40, null)
					.threadPoolSize(3)
					// 线程池内加载的数量
					.threadPriority(Thread.NORM_PRIORITY - 2)
					.denyCacheImageMultipleSizesInMemory()
					.discCacheFileNameGenerator(new Md5FileNameGenerator()) // 将保存的时候的URI名称用MD5
																			// 加密
					.memoryCache(new UsingFreqLimitedMemoryCache(4 * 1024 * 1024)) // You
																					// can
																					// pass
																					// your
																					// own
																					// memory
																					// cache
																					// implementation/你可以通过自己的内存缓存实现
					.memoryCacheSize(4 * 1024 * 1024) // 内存缓存的最大值
					.discCacheSize(50 * 1024 * 1024) // 50 Mb sd卡(本地)缓存的最大值
					.tasksProcessingOrder(QueueProcessingType.LIFO)
					// 由原先的discCache -> diskCache
					.discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
					.imageDownloader(
							new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout
																					// (5
																					// s),
																					// readTimeout
																					// (30
																					// s)超时时间
					.writeDebugLogs() // Remove for release app
					.build();
			// 全局初始化此配置
			imageLoader = ImageLoader.getInstance();
			imageLoader.init(config);
		}
	    
	    
	    public ImageLoader getImageLoader(Context context) {
	    	if (!imageLoader.isInited()) {
	    		initImageLoader(context);
			}
			return imageLoader;
		}*/
	public GridviewAddImageAdapter(Context context, List<MPhotoItem> photoList){
		instance = this;
		this.context = context;
//		initImageLoader(context);
		this.photoList = photoList;
		MPhotoItem ap = new MPhotoItem();
		photoList.add(ap);
		bmCache = new BitmapCache();
	}
	public static GridviewAddImageAdapter getInstance(){
		return instance;
	}
	public void update(List<MPhotoItem> photoList){
		if(Util.isNull(photoList)){
			this.photoList.clear();
		}else{
			this.photoList.clear();
//			this.peopleList = peopleList;
			this.photoList.addAll(photoList);
		}
		notifyDataSetChanged();
	}
	public List<MPhotoItem> getPhotoList(){
		return photoList;
	}
	public void update(){
		notifyDataSetChanged();
	}
	public void clear(){
		this.photoList.clear();
	}
	@Override
	public int getCount() {
		return photoList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return photoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holderView;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(
					R.layout.hy_item_addimage, parent, false);
			holderView = new HolderView();
			holderView.image = (ImageView) convertView.findViewById(R.id.hy_item_add_image);
			convertView.setTag(holderView);
		}else{
			holderView = (HolderView) convertView.getTag();
		}
		setHolderView(holderView, position);
		return convertView;
	}
	//TODO
	/**
	 * modified by sunjianan
	 * @param holderView
	 * @param position
	 * @param isLimit  -> true  youtupianxianz  , boolean isLimit else if(position > 8){
			holderView.image.setVisibility(View.VISIBLE);
		}
		 && position != 9
	 */
	private void setHolderView(HolderView holderView, int position){
	    MPhotoItem pi;
		if((photoList.size() - 1) == position){
			holderView.image.setImageResource(R.drawable.hy_addimage_bg_selector);
		}else{
		    pi = photoList.get(position);
		    if(pi.isAlterMeeting){
		        holderView.image.setImageResource(R.drawable.common_loading01);
		        if(!Util.isNull(pi.alterMeetingPic) && !Util.isNull(pi.alterMeetingPic.getPicPath()) ){
		          imageLoader.displayImage(pi.alterMeetingPic.getPicPath(), holderView.image);
		        }
		    }else{
		        holderView.image.setTag(pi.path);
	            bmCache.displayBmp(context, holderView.image, pi.thumbnailPath, pi.path,
	                    callback);
		    }
//			holderView.image.setImageBitmap(photoList.get(position).photo);
			
		}
	}
	private class HolderView{
		public ImageView image;
	}
	private ImageCallback callback = new ImageCallback() {
		@Override
		public void imageLoad(ImageView imageView, Bitmap bitmap,
				Object... params) {
			if (imageView != null && bitmap != null) {
				String url = (String) params[0];
				String tag = (String) imageView.getTag();
				if (url != null && url.equals(tag)) {
					((ImageView) imageView).setImageBitmap(bitmap);
				} else {
					Log.e(TAG, "callback, bmp not match");
				}
			} else {
				Log.e(TAG, "callback, bmp null");
			}
		}
	};
}
