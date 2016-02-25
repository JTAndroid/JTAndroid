package com.tr.ui.adapter.conference;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tr.R;
import com.tr.model.conference.MAlbumBucket;
import com.utils.picture.BitmapCache;
import com.utils.picture.BitmapCache.ImageCallback;
import com.utils.time.Util;

public class GridviewPhotoAlbumBucketAdapter extends BaseAdapter {
	private final String TAG = getClass().getSimpleName();
	private Context context;
	private List<MAlbumBucket> dataList;
	private BitmapCache bmCache;
	
	public GridviewPhotoAlbumBucketAdapter(Context context, List<MAlbumBucket> dataList){
		this.context = context;
		this.dataList = dataList;
		bmCache = new BitmapCache();
	}
	private ImageCallback callback = new ImageCallback() {
		@Override
		public void imageLoad(ImageView imageView, Bitmap bitmap,
				Object... params) {
			if (imageView != null && bitmap != null) {
				String url = (String) params[0];
				if (url != null && url.equals((String) imageView.getTag())) {
					((ImageView) imageView).setImageBitmap(bitmap);
				} else {
					Log.e(TAG, "callback, bmp not match");
				}
			} else {
				Log.e(TAG, "callback, bmp null");
			}
		}
	};
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(Util.isNull(dataList)){
			return 0;
		}else{
			return dataList.size();
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		HolderView holderView;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.hy_item_photo_album_bucket, parent, false);
			holderView = new HolderView();
			holderView.photo = (ImageView) convertView.findViewById(R.id.hy_itemPhotoAlbumBucket_photo);
			holderView.name = (TextView) convertView.findViewById(R.id.hy_itemPhotoAlbumBucket_name);
			holderView.count = (TextView) convertView.findViewById(R.id.hy_itemPhotoAlbumBucket_count);
			convertView.setTag(holderView);
		}else{
			holderView = (HolderView) convertView.getTag();
		}
		setHolderView(holderView, position);
		return convertView;
	}
	private void setHolderView(HolderView holderView, int position){
		MAlbumBucket item = dataList.get(position);
		holderView.count.setText(String.valueOf(item.count));
		holderView.name.setText(item.bucketName);
		if (!Util.isNull(item.photoList)) {
			String thumbPath = item.photoList.get(0).thumbnailPath;
			String sourcePath = item.photoList.get(0).path;
			holderView.photo.setTag(sourcePath);
			bmCache.displayBmp(context, holderView.photo, thumbPath, sourcePath, callback);
		} else {
			holderView.photo.setImageResource(R.drawable.ic_avatar);
		}
	}
	private class HolderView{
		private ImageView photo;
		private TextView name;
		private TextView count;
	}
}
