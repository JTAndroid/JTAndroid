package com.tr.ui.conference.square;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tr.R;
import com.tr.ui.conference.common.BaseActivity;
import com.tr.ui.widgets.EProgressDialog;
import com.utils.string.StringUtils;
import com.utils.time.Util;
//import com.baidu.navisdk.util.common.StringUtils;

public class ImagesBrowseActivity extends BaseActivity {

	private String[] imgs;
	private int currPosition;

	@ViewInject(R.id.hy_photo_browse_pager)
	private ViewPager pager;
	@ViewInject(R.id.hy_browse_curr_position)
	private TextView text;

	@Override
	public void initView() {
		setContentView(R.layout.hy_fragment_photo_browse);
		ViewUtils.inject(this);
		Intent intent = getIntent();
		imgs = intent.getStringArrayExtra("IMAGESBROWSE_IMGS");
		currPosition = intent.getIntExtra("IMAGESBROWSE_CURRPOSITION", 0);
		PhotoBrowseAdapter adapter = new PhotoBrowseAdapter();
		pager.setAdapter(adapter);
		pager.setCurrentItem(currPosition);

	}

	@Override
	public void initData() {

	}

	class PhotoBrowseAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			int count = 0;
			if (imgs != null) {
				for (int i = 0; i < imgs.length; i++) {
					if (!StringUtils.isEmpty(imgs[i])) {
						count++;
					}
				}
				return count;
			}
			return count;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = View.inflate(ImagesBrowseActivity.this, R.layout.hy_item_photo_browse, null);
			ImageView imageView = (ImageView) view.findViewById(R.id.hy_pager_img);
			LinearLayout dots = (LinearLayout) view.findViewById(R.id.hy_browse_dots_container);
			// TextView tvPosition = (TextView)
			// view.findViewById(R.id.hy_browse_curr_position);
			if (imgs.length <= 9) {
				for (int i = 0; i < imgs.length; i++) {
					if (!StringUtils.isEmpty(imgs[i])) {
						ImageView dot = new ImageView(ImagesBrowseActivity.this);
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Util.DensityUtil.dip2px(ImagesBrowseActivity.this, 5), Util.DensityUtil.dip2px(ImagesBrowseActivity.this, 5));
						params.rightMargin = Util.DensityUtil.dip2px(ImagesBrowseActivity.this, 5);
						params.leftMargin = Util.DensityUtil.dip2px(ImagesBrowseActivity.this, 5);
						dot.setLayoutParams(params);
						if (i == position) {
							dot.setBackgroundResource(R.drawable.hy_icon_meeting_dot_normal);
							dots.addView(dot);
						} else {
							dot.setBackgroundResource(R.drawable.hy_icon_meeting_dot_current);
							dots.addView(dot);
						}
					}
				}
			} else {
				TextView tvHint = new TextView(ImagesBrowseActivity.this);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				tvHint.setLayoutParams(params);
				tvHint.setText((position+1) + "/" + imgs.length);
				dots.addView(tvHint);
			}
			ImageLoader.getInstance().displayImage(imgs[position], imageView, new ImageLoadingListener() {

				private EProgressDialog prgDialog;

				@Override
				public void onLoadingStarted(String imageUri, View view) {
					prgDialog = new EProgressDialog(ImagesBrowseActivity.this);
					prgDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
						@Override
						public void onCancel(DialogInterface dialog) {

						}
					});
					prgDialog.setMessage("加载中...");
					prgDialog.setCancelable(false);
					prgDialog.setCanceledOnTouchOutside(false);
					prgDialog.show();
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					prgDialog.dismiss();
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					prgDialog.dismiss();
				}

				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					prgDialog.dismiss();
				}
			});
			// tvPosition.setText((position+1) + " / " + imgs.size());
			container.addView(view);
			return view;
		}

	}

}
