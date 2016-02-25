package com.tr.ui.adapter.conference;

import java.util.ArrayList;
import java.util.List;

import com.tr.R;
import com.tr.model.conference.MPhotoItem;
import com.tr.ui.conference.initiatorhy.ConferenceIntroduceActivity;
import com.utils.picture.BitmapCache;
import com.utils.picture.BitmapCache.ImageCallback;
import com.utils.time.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GridviewPhotoAlbumBoothAdapter extends BaseAdapter {
	private final String TAG = getClass().getSimpleName();
	private Context context;
	private List<MPhotoItem> dataList;
	private BitmapCache bmCache;
//	private Handler handler;
	private int selectTotal = 0;
	private List<MPhotoItem> selPhotoList = new ArrayList<MPhotoItem>();
	
	public GridviewPhotoAlbumBoothAdapter(Context context, 
			List<MPhotoItem> dataList){
		this.context = context;
		this.dataList = dataList;
//		this.handler = handler;
		bmCache = new BitmapCache();
	}
	public List<MPhotoItem> getSelPhotoList(){
		return selPhotoList;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.hy_item_photo_album_boooth, parent, false);
			holderView = new HolderView();
			holderView.photo = (ImageView) convertView.findViewById(R.id.hy_itemPhotoAlbumBooth_photo);
			holderView.check = (ImageView) convertView.findViewById(R.id.hy_itemPhotoAlbumBooth_check);
			holderView.selTextBg = (TextView) convertView.findViewById(R.id.hy_itemPhotoAlbumBooth_sleTextBg);
			convertView.setTag(holderView);
		}else{
			holderView = (HolderView) convertView.getTag();
		}
		setHolderView(holderView, position);
		return convertView;
	}
	private void setHolderView(final HolderView holderView, final int position){
		final MPhotoItem item = dataList.get(position);
		
		holderView.photo.setTag(item.path);
		bmCache.displayBmp(context, holderView.photo, item.thumbnailPath, item.path,
				callback);
		if (item.isSelected) {
			holderView.check.setVisibility(View.VISIBLE);
			holderView.check.setBackgroundResource(R.drawable.hy_ic_selected);
		} else {
			holderView.check.setVisibility(View.GONE);
//			holderView.check.setBackgroundResource(-1);
		}
		holderView.photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				List<MPhotoItem> addPhotoList = IntroduceActivity.getInstance().getAddPhotoList();
				List<MPhotoItem> addPhotoList = new ArrayList<MPhotoItem>();
				
//				String path = dataList.get(position).path;
				int hasAddSize = addPhotoList.size() - 1;
				if ((hasAddSize + selectTotal) < 9) {
					item.isSelected = !item.isSelected;
					if (item.isSelected) {
						holderView.check.setVisibility(View.VISIBLE);
						holderView.check.setImageResource(R.drawable.hy_ic_selected);
						holderView.selTextBg.setBackgroundResource(R.drawable.hy_shape_redround_forcheckphoto_color);
						selectTotal++;
						selPhotoList.add(item);
					} else if (!item.isSelected) {
						holderView.check.setVisibility(View.GONE);
//						holderView.check.setImageResource(-1);
						holderView.selTextBg.setBackgroundResource(R.color.transparent);
						selectTotal--;
						selPhotoList.remove(item);
					}
				} else if ((hasAddSize + selectTotal) >= 9) {
					if (item.isSelected == true) {
						item.isSelected = !item.isSelected;
						holderView.check.setImageResource(-1);
						holderView.selTextBg.setBackgroundResource(R.color.transparent);
						selectTotal--;
						selPhotoList.remove(item);
					} else {
						Toast.makeText(context, "最多选择9张图片", Toast.LENGTH_SHORT).show();
					}
				}
			}

		});
	}
	private class HolderView{
		private ImageView photo;
		private ImageView check;
		private TextView selTextBg;
	}
}
